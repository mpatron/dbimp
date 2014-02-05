package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.report.RessourceReporting;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.FieldTypeEnum;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.xml.XmlField;


/**
 * Class permettant de modifier une donnée qui est présente dans la base. 
 * Créer le 24 janv. 2003.
 * @author Mickaël Patron
 */
public class SqlUpdate extends SqlStatement {

  //mettre static car log doit être construit avant le constructeur pour qu'il soit non null pour createSQL();
  //qui est appellé par le constructeur SqlStatement. 
  private static Log log= LogFactory.getLog(SqlUpdate.class);

  private int countUpdate= 0;
  //---------------------------------------------------------------------------

  /**
   * Method SqlUpdate.
   * @param connection de la base de donnée.
   * @param xmlline contient le liste des champs.
   * @param reportTypeLine est pointeur sur le Rapport.
   * @throws SQLException
   */
  public SqlUpdate(Connection connection, String schemaName, boolean cached, Line xmlline, ReportTypeLine reportTypeLine)
    throws SQLException {
    super(connection, schemaName, cached, xmlline, reportTypeLine);
  }
  //---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlStatement#createSQL()
   * @throws SQLException
   */
  public String createSQL() {
    String returnValue= "update " + getSQLSchemaName() + getXmlline().getTableName() + " set ";
    String where= "";

    boolean first= true;

    for (Iterator<Field> it= getXmlline().getFields().iterator(); it.hasNext();) {
      XmlField field= (XmlField) it.next();
      if (!field.isUse())
        continue;
      if (field.getType() == FieldTypeEnum.BLOB)
        continue;
      if (!getPrimaries().contains(field.getName().toUpperCase())) {
        if (first) {
          first= false;
          returnValue += (field.getName() + "=?");
        } else {
          returnValue += (", " + field.getName() + "=?");
        }
      }
    }

    if (first) {
      String message= RessourceReporting.getString("ERROR_PARAMETRAGE", new Object[] { getXmlline().getName(), getXmlline().getTableName() });
      log.error(message);
      return null;
    }

    first= true;

    for (Iterator<Field> it= getXmlline().getFields().iterator(); it.hasNext();) {
      XmlField field= (XmlField) it.next();
      if (!field.isUse())
        continue;
      if (field.getType() == FieldTypeEnum.BLOB)
        continue;
      if (getPrimaries().contains(field.getName())) {
        if (first) {
          first= false;
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
  //---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#execute(int nbLigne)
   */
  public int execute(int nbLigne) {
    int returnValue= 0;
    int i= 1;
    boolean flag= true;

    try {
      if (getXmlline().getTrigger() != null) {
        flag=
          getXmlline().getTrigger().beforeUpdate(
            getConnection(),
            nbLigne,
            getReportTypeLine().getReportTrigger(),
            getXmlline());
      }

      PreparedStatement pstmt= null;
      try {
        if (isCached()) {
          pstmt= getPstmtCached();
        } else {
          pstmt= getConnection().prepareStatement(getSql());
        }

        if (flag) {
          for (Iterator<Field> it= getXmlline().getFields().iterator(); it.hasNext();) {
            XmlField field= (XmlField) it.next();
            if (!field.isUse())
              continue;
            if (field.getType() == FieldTypeEnum.BLOB)
              continue;
            flag &= checkIn(getXmlline(), field, getReportTypeLine().getReportLine());
            if (!getPrimaries().contains(field.getName().toUpperCase())) {
              if (field.isEmptyOrNullBuffer()) {
                setNull(pstmt, i, field);
              } else {
                setAll(pstmt, i, field);
              }
              i++;
            }
          }
        }

        if (flag) {
          for (Iterator<Field> it= getXmlline().getFields().iterator(); it.hasNext();) {
            XmlField field= (XmlField) it.next();
            if (!field.isUse())
              continue;
            if (field.getType() == FieldTypeEnum.BLOB)
              continue;
            if (getPrimaries().contains(field.getName().toUpperCase())) {
              flag &= checkIn(getXmlline(), field, getReportTypeLine().getReportLine());
              if (StringUtils.isEmpty(field.getBuffer())) {
                setNull(pstmt, i, field);
              } else {
                setAll(pstmt, i, field);
              }

              i++;
            }
          }
        }
        if (flag) {
          returnValue= pstmt.executeUpdate();
          log.debug("Mise à jour effectué : " + getSql());
          if (getXmlline().getTrigger() != null) {
            getXmlline().getTrigger().afterUpdate(
              getConnection(),
              nbLigne,
              getReportTypeLine().getReportTrigger(),
              getXmlline());
          }
        }
      } finally {
        if (!isCached()) {
          pstmt.close();
          pstmt= null;
        }
      }
    } catch (SQLException ex) {
      SqlUtils.AfficheSQLException(getXmlline(), getSql(), ex, getReportTypeLine().getReportLine());
    }
    countUpdate += returnValue;
    getReportTypeLine().addToNbUpdate(returnValue);
    return returnValue;
  }
  //---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#getCount()
   */
  public int getCount() {
    return countUpdate;
  }

}
