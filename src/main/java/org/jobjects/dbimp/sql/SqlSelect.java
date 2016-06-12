package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.dbimp.MathUtils;
import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;

/**
 * Class permettant de vérifier si la donnée est présente dans la base. Créer le
 * 24 janv. 2003.
 * 
 * @author Mickaël Patron
 */
public class SqlSelect extends SqlStatement {

  private static Logger LOGGER = Logger.getLogger(SqlSelect.class.getName());

  private int countSelect = 0;

  // ---------------------------------------------------------------------------

  /**
   * Method SqlSelect.
   * 
   * @param connection
   * @param xmlline
   * @param reportTypeLine
   * @throws SQLException
   */
  public SqlSelect(Connection connection, String schemaName, boolean cached, Line xmlline, ReportTypeLine reportTypeLine)
      throws SQLException {
    super(connection, schemaName, cached, xmlline, reportTypeLine);
    LOGGER.finest("schemaName=" + schemaName);
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlStatement#createSQL()
   * @throws SQLException
   */
  public String createSQL() throws SQLException {
    String returnValue = "select ";
    String where = "";

    boolean first = true;

    for (Field field : getXmlline().getFields()) {
      if (!field.isUse())
        continue;

      if (first) {
        first = false;
        returnValue += field.getName();
      } else {
        returnValue += (", " + field.getName());
      }
    }

    returnValue += (" from " + getSQLSchemaName() + getXmlline().getTableName());

    if (first) {
      LOGGER.severe("Error no field");

      return null;
    }

    first = true;

    for (Field field : getXmlline().getFields()) {
      if (!field.isUse())
        continue;

      if (getPrimaries().contains(field.getName())) {
        if (first) {
          first = false;
          where += (field.getName() + "=?");
        } else {
          where += (" and " + field.getName() + "=?");
        }
      }
    }

    if (!where.equals("")) {
      returnValue += (" where " + where);
    }

    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#execute(int nbLigne)
   */
  public int execute(int nbLigne) {
    Map<String, Object> returnValue = null;
    LOGGER.fine(getSql());

    PreparedStatement pstmt = null;
    try {
      if (isCached()) {
        pstmt = getPstmtCached();
      } else {
        pstmt = getConnection().prepareStatement(getSql());
      }

      try {
        int i = 1;

        for (Field field : getXmlline().getFields()) {
          if (!field.isUse())
            continue;

          if (getPrimaries().contains(field.getName().toUpperCase())) {
            if ((field.getBuffer() == null) || field.getBuffer().equals("")) {
              setNull(pstmt, i, field);
            } else {
              setAll(pstmt, i, field);
            }
            i++;
          }
        }

        ResultSet rs = pstmt.executeQuery();

        try {
          if (rs.next()) {
            returnValue = new HashMap<String, Object>();

            for (Field field : getXmlline().getFields()) {
              if (!field.isUse())
                continue;

              switch (field.getTypeFormat()) {
              case INTEGER:
                int i_value = rs.getInt(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), new Integer(i_value));
                }
                break;

              case LONG:
                long l_value = rs.getLong(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), new Long(l_value));
                }
                break;

              case FLOAT:
                float f_value = rs.getFloat(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), new Float(f_value));
                }
                break;

              case DOUBLE:
                double d_value = rs.getDouble(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), new Double(d_value));
                }
                break;

              case DATETIME:
                Timestamp ts_value = rs.getTimestamp(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), ts_value);
                }
                break;

              default:
                String s_value = rs.getString(field.getName());
                if (rs.wasNull()) {
                  returnValue.put(field.getName(), null);
                } else {
                  returnValue.put(field.getName(), s_value);
                }
              }
            }
          }
        } finally {
          rs.close();
          rs = null;
        }
      } finally {
        if (!isCached()) {
          pstmt.close();
          pstmt = null;
        }
      }

    } catch (SQLException ex) {
      SqlUtils.AfficheSQLException(getXmlline(), getSql(), ex, getReportTypeLine().getReportLine());
    }
    countSelect++;
    getReportTypeLine().addToNbSelected(1);

    if (returnValue == null)
      return -1;
    else
      return isEquals(returnValue, nbLigne);
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#getCount()
   */
  public int getCount() {
    return countSelect;
  }

  // ---------------------------------------------------------------------------

  private int isEquals(Map<String, Object> items, int nbLigne) {
    int returnValue = 0;
    boolean flag = true;

    for (Field field : getXmlline().getFields()) {
      if (!field.isUse())
        continue;

      Object value = items.get(field.getName());

      try {
        if ((field.getBuffer() == null) || field.getBuffer().trim().equals("")) {
          if (value != null) {
            returnValue = 1;
          }
        } else {
          if (value == null) {
            returnValue = 2;
          } else {
            /**
             * buffer et value ne sont pas nulls.
             */
            switch (field.getTypeFormat()) {
            case INTEGER:
              double i_value = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
              if (MathUtils.isInteger(i_value)) {
                if (((Integer) value).compareTo(new Integer((int) i_value)) != 0) {
                  returnValue = 3;
                }
              } else {
                returnValue = 3;
              }
              break;

            case LONG:
              double l_value = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
              if (MathUtils.isInteger(l_value)) {
                if (((Long) value).compareTo(new Long((long) l_value)) != 0) {
                  returnValue = 4;
                }
              } else {
                returnValue = 4;
              }
              break;

            case FLOAT:

              float f_value = Float.parseFloat(field.getBuffer()) * (float) field.getCoefficient();
              if (((Float) value).compareTo(new Float(f_value)) != 0) {
                returnValue = 5;
              }
              break;

            case DOUBLE:

              double d_value = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
              if (((Double) value).compareTo(new Double(d_value)) != 0) {
                returnValue = 6;
              }
              break;

            case DATETIME:

              if (field.getBuffer().equalsIgnoreCase("sysdate")) {
                if (System.currentTimeMillis() != ((Timestamp) value).getTime()) {
                  returnValue = 7;
                }
              } else {
                SimpleDateFormat sdf = new SimpleDateFormat(field.getDateFormat());

                if (sdf.parse(field.getBuffer()).getTime() != ((Timestamp) value).getTime()) {
                  returnValue = 8;
                }
              }
              break;

            default:

              if (!field.getBuffer().equals((String) value)) {
                returnValue = 9;
              }
            }
          }
        }
      } catch (NumberFormatException nfe) {
        LOGGER.log(Level.SEVERE,
            "Line (" + nbLigne + ") " + field.getName() + "=" + field.getBuffer() + " is not a " + field.getTypeFormat().getTypeString(),
            nfe);
        getReportTypeLine().getReportLine().getReportField(field).ERROR_FIELD_TYPE();
      } catch (ParseException pe) {
        LOGGER.log(Level.SEVERE,
            "Line (" + nbLigne + ") " + field.getName() + "=" + field.getBuffer() + " is not a " + field.getTypeFormat().getTypeString(),
            pe);
        getReportTypeLine().getReportLine().getReportField(field).ERROR_FIELD_TYPE();
      }

      if ((returnValue != 0) && flag) {
        LOGGER.fine("Update for ligne=" + nbLigne + " " + field.getName() + " : in file=" + field.getBuffer() + " in database=" + value);
        if (value == null) {
          value = "";
        }
        // /*
        // * Afin de ne pas mettre des informations qui ne sont pas des erreurs
        // * dans le fichier de rapport, il ne faut pas executer cette commande.
        // */
        if (getReportTypeLine().getReporting().isVerbose()) {
          getReportTypeLine().getReportLine().getReportField(field).INFO_FIELD_UPDATED_IN_DB(field.getBuffer(), value.toString());
        }

        flag = false;
      }
    }

    // return returnValue == 0;
    return returnValue;
  }
  // ---------------------------------------------------------------------------

}
