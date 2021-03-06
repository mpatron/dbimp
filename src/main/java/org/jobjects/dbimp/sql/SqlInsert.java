package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.trigger.Line;

/**
 * Class permettant d'insérer une donnée dans la base. Créer le 24 janv. 2003.
 * 
 * @author Mickaël Patron
 */
public class SqlInsert extends SqlStatement {

  private Logger LOGGER = Logger.getLogger(getClass().getName());

  private int countInsert = 0;

  // ---------------------------------------------------------------------------

  public SqlInsert(Connection connection, String schemaName, boolean cached, Line xmlline, ReportTypeLine reportTypeLine)
      throws SQLException {
    super(connection, schemaName, cached, xmlline, reportTypeLine);
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlStatement#createSQL()
   */
  public String createSQL() {
    String returnValue = "insert into " + getSQLSchemaName() + getXmlline().getTableName() + " (";
    String values = "";
    boolean first = true;

    for (Field field : getXmlline().getFields()) {
      if (!field.isUse())
        continue;

      if (first) {
        first = false;
        returnValue += field.getName();
        values += "?";
      } else {
        returnValue += (", " + field.getName());
        values += ", ?";
      }
    }

    returnValue += (") values (" + values + ")");

    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#execute(int nbLigne)
   */
  public int execute(int nbLigne) {
    int returnValue = 0;
    int i = 1;
    boolean flag = true;

    try {
      if (getXmlline().getTrigger() != null) {
        flag = getXmlline().getTrigger().beforeInsert(getConnection(), nbLigne, getReportTypeLine().getReportTrigger(), getXmlline());
      }

      PreparedStatement pstmt = null;
      try {
        if (isCached()) {
          pstmt = getPstmtCached();
        } else {
          pstmt = getConnection().prepareStatement(getSql());
        }

        if (flag) {
          for (Field field : getXmlline().getFields()) {
            if (!field.isUse())
              continue;
            flag &= checkIn(getXmlline(), field, getReportTypeLine().getReportLine());
            if ((field.getTypeFormat() != FieldFormatEnum.BLOB)) {
              if (field.isEmptyOrNullBuffer()) {
                setNull(pstmt, i, field);
              } else {
                setAll(pstmt, i, field);
              }
            }
            i++;
          }
        }

        if (flag) {
          returnValue = pstmt.executeUpdate();
          LOGGER.finest(getSql());
          if (getXmlline().getTrigger() != null) {
            getXmlline().getTrigger().afterInsert(getConnection(), nbLigne, getReportTypeLine().getReportTrigger(), getXmlline());
          }
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
    countInsert += returnValue;
    getReportTypeLine().addToNbInsert(returnValue);
    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#getCount()
   */
  public int getCount() {
    return this.countInsert;
  }
  // ---------------------------------------------------------------------------
}
