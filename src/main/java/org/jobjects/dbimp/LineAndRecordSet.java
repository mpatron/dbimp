package org.jobjects.dbimp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.sql.SqlAction;
import org.jobjects.dbimp.sql.SqlDelete;
import org.jobjects.dbimp.sql.SqlInsert;
import org.jobjects.dbimp.sql.SqlSelect;
import org.jobjects.dbimp.sql.SqlUpdate;
import org.jobjects.dbimp.sql.SqlUpdateBlob;
import org.jobjects.dbimp.sql.SqlUtils;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Key;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.trigger.LineActionTypeEnum;
import org.jobjects.dbimp.xml.XmlField;
import org.jobjects.dbimp.xml.XmlLine;

/**
 * Il a possibilité de prévoir dans le future plusieur connecteur qui pourrait
 * plusieur type de fichier : - fichier plat - fichier xml - fichier xls ...
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class LineAndRecordSet {
  private Logger log = Logger.getLogger(getClass().getName());
  // ---------------------------------------------------------------------------
  private Connection connection = null;
  private Line xmlline = null;
  private int countRejected = 0;
  private LineActionTypeEnum InsertAndUpdate = LineActionTypeEnum.INSERT;
  private int nbLigne = 1;
  private SqlAction sql_select = null;
  private SqlAction sql_insert = null;
  private SqlAction sql_delete = null;
  private SqlAction sql_update = null;
  private SqlAction sql_update_blob = null;
  // ---------------------------------------------------------------------------

  private ReportTypeLine reportTypeLine = null;

  // ---------------------------------------------------------------------------

  public LineAndRecordSet(Connection connection, String schemaName, boolean cached, Line xmlline, ReportTypeLine reportTypeLine) throws SQLException {

    this.connection = connection;
    this.xmlline = xmlline;

    if (xmlline.getAction() == null) {
      this.InsertAndUpdate = LineActionTypeEnum.INSERT_UPDATE;
    } else {
      this.InsertAndUpdate = xmlline.getAction();
    }
    this.reportTypeLine = reportTypeLine;
    // for(Field field : xmlline.getFields()) {
    for (Iterator<Field> it = xmlline.getFields().iterator(); it.hasNext();) {
      XmlField field = (XmlField) it.next();
      if (!field.isUse())
        continue;
      if (StringUtils.isNotEmpty(field.getCheckInSql())) {
        try {
          Statement stmt = connection.createStatement();
          try {
            ResultSet rs = stmt.executeQuery(field.getCheckInSql());
            try {
              field.setCheckIn(new LinkedList<String>());
              while (rs.next()) {
                field.getCheckIn().add(rs.getString(1));
              }
            } finally {
              rs.close();
            }
            rs = null;
          } finally {
            stmt.close();
          }
          stmt = null;
        } catch (Exception ex) {
          log.log(Level.SEVERE, "", ex);
          field.setCheckIn(null);
          field.setCheckInSql(null);
        }
      }
    }
    sql_select = new SqlSelect(connection, schemaName, cached, xmlline, reportTypeLine);
    log.finest("Load SqlSelect.");
    if ((LineActionTypeEnum.INSERT.equals(InsertAndUpdate)) || (LineActionTypeEnum.INSERT_UPDATE.equals(InsertAndUpdate))) {
      sql_insert = new SqlInsert(connection, schemaName, cached, xmlline, reportTypeLine);
      log.finest("Load SqlInsert.");
      if (SqlUpdateBlob.hasBlob(xmlline)) {
        sql_update_blob = new SqlUpdateBlob(connection, schemaName, cached, xmlline, reportTypeLine);
        log.finest("Load SqlUpdateBlob.");
      }
    }
    if ((LineActionTypeEnum.UPDATE.equals(InsertAndUpdate)) || (LineActionTypeEnum.INSERT_UPDATE.equals(InsertAndUpdate))) {
      sql_update = new SqlUpdate(connection, schemaName, cached, xmlline, reportTypeLine);
      log.finest("Load SqlUpdate.");
      if (SqlUpdateBlob.hasBlob(xmlline)) {
        sql_update_blob = new SqlUpdateBlob(connection, schemaName, cached, xmlline, reportTypeLine);
        log.finest("Load SqlUpdateBlob.");
      }
    }
    if (LineActionTypeEnum.DELETE.equals(InsertAndUpdate)) {
      sql_delete = new SqlDelete(connection, schemaName, cached, xmlline, reportTypeLine);
      log.finest("Load SqlDelete.");
    }
    if (xmlline.getTrigger() != null)
      xmlline.getTrigger().beforeAction(connection, nbLigne, reportTypeLine.getReportTrigger(), xmlline);
  }

  // ---------------------------------------------------------------------------
  /**
   * Renvoie la variable xmlline qui contient les informations sur le format de
   * ligne à lire.
   * 
   * @return Renvoie l'instance du paramêtre.
   */
  public Line getXmlLine() {
    return xmlline;
  }

  // ---------------------------------------------------------------------------
  /**
   * Retourne le nombre de ligne rejeté.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCountRejected() {
    return this.countRejected;
  }

  // ---------------------------------------------------------------------------
  /**
   * Indique si la ligne lue répond au critère de lecture. C'est à dire s'il les
   * tag <key value="CU1" startposition="0" size="3"/>
   * 
   * @param ligne
   *          Ligne en cours de lecture.
   * @return Returne un booleen indiquant si la ligne est active ou pas.
   */
  public boolean isActive(String ligne) {
    boolean returnValue = true;
    for (Key key : xmlline.getKeys()) {
      if (key.getIsBlank() == null) {
        if ((key.getStartposition() > ligne.length()) || ((key.getStartposition() + key.getSize()) > ligne.length())) {
          returnValue &= false;
          break;
        } else {
          String buffer = ligne.substring(key.getStartposition(), key.getStartposition() + key.getSize());
          returnValue &= buffer.equals(key.getValue());
        }
      } else {
        String buffer = ligne.substring(key.getStartposition(), key.getStartposition() + key.getSize());
        returnValue &= !(key.getIsBlank().booleanValue() ^ StringUtils.isEmpty(buffer.trim()));
      }
    }
    return returnValue;
  }

  // ---------------------------------------------------------------------------
  /**
   * Execute l'ensemble des actions d'insertion, de mise à jour, de suppression,
   * les actions du trigger en fonction des valeurs du fichier de paramètrage.
   * 
   * @param nbLigne
   *          Numéro de la ligne lue
   * @param ligne
   *          Valeur de la ligne lue
   */
  public void execute(int nbLigne, String ligne) {
    this.nbLigne = nbLigne;
    boolean line_is_good = ((XmlLine) xmlline).loadFields(connection, ligne, reportTypeLine.getReportLine());
    if (line_is_good) {
      int count_modif = sql_select.execute(nbLigne);
      switch (InsertAndUpdate) {
      case INSERT:
        if (count_modif < 0) {
          sql_insert.execute(nbLigne);
          if (SqlUpdateBlob.hasBlob(xmlline)) {
            sql_update_blob.execute(nbLigne);
          }
        }
        break;
      case UPDATE:
        if (count_modif > 0) {
          sql_update.execute(nbLigne);
          if (SqlUpdateBlob.hasBlob(xmlline)) {
            sql_update_blob.execute(nbLigne);
          }
        }
        break;
      case INSERT_UPDATE:
        if (count_modif < 0) {
          sql_insert.execute(nbLigne);
        } else if (count_modif > 0) {
          sql_update.execute(nbLigne);
        }
        if (SqlUpdateBlob.hasBlob(xmlline)) {
          sql_update_blob.execute(nbLigne);
        }
        break;
      case DELETE:
        if (count_modif >= 0) {
          try {
            connection.setAutoCommit(false);
            int c = sql_delete.execute(nbLigne);
            if (c == 1) {
              connection.commit();
            } else {
              connection.rollback();
              log.log(Level.SEVERE, "ligne " + nbLigne + " not deleted, try to delete more one line.");
            }
            connection.setAutoCommit(true);
          } catch (SQLException ex) {
            SqlUtils.AfficheSQLException(xmlline, "DELETE", ex, reportTypeLine.getReportLine());
          }
        }
        break;
      default:
        reportTypeLine.getReportLine().showLine();
      }
    } else {
      countRejected++;
      reportTypeLine.addToNbReject(1);
      // reportTypeLine.println(nbLigne,
      // "Erreur non afficheable nullable_error=false", xmlline);
      // reportTypeLine.ERROR_FIELD_NULL(nbLigne,"?");
      log.finest("ligne " + nbLigne + " rejected.");
    }
    ((XmlLine) xmlline).unloadFields();
  }

  // ---------------------------------------------------------------------------
  /**
   * Netoyage des connections ouvertes.
   * 
   * @throws SQLException
   *           Exception possible de la base de donnée.
   */
  public void release() throws java.sql.SQLException {
    // if ((reportTypeLine != null) && (reportTypeLine.getLevel() >=
    // Report.VERBOSE)) {
    // log.debug(
    // reportTypeLine.INFO_STATUS(
    // getXmlLine().getName(),
    // sql_select == null ? 0 : sql_select.getCount(),
    // sql_insert == null ? 0 : sql_insert.getCount(),
    // sql_update == null ? 0 : sql_update.getCount(),
    // sql_delete == null ? 0 : sql_delete.getCount(),
    // getCountRejected()));
    // }
    if (sql_select != null) {
      sql_select.close();
      sql_select = null;
    }
    if (sql_insert != null) {
      sql_insert.close();
      sql_insert = null;
    }
    if (sql_delete != null) {
      sql_delete.close();
      sql_delete = null;
    }
    if (sql_update != null) {
      sql_update.close();
      sql_update = null;
    }
    if (sql_update_blob != null) {
      sql_update_blob.close();
      sql_update_blob = null;
    }
  }

  // ---------------------------------------------------------------------------

  /**
   * 
   */
  public void doAfterAction() throws IOException {
    if (xmlline.getTrigger() != null) {
      xmlline.getTrigger().afterAction(connection, nbLigne, reportTypeLine.getReportTrigger(), xmlline);
    }
  }

  /**
   * Retourne le nombre de ligne lue dans la base. Il faut que ce nombre
   * corresponde avec le nombre de ligne du fichier.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCountSelect() {
    if (sql_select != null)
      return sql_select.getCount();
    else
      return 0;
  }

  // ---------------------------------------------------------------------------
  /**
   * Retourne le nombre de ligne inseré dans la base.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCountInsert() {
    if (sql_insert != null)
      return sql_insert.getCount();
    else
      return 0;
  }

  // ---------------------------------------------------------------------------
  /**
   * Retourne le nombre de ligne mise à jour dans la base.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCountUpdate() {
    if (sql_update != null)
      return sql_update.getCount();
    else
      return 0;
  }

  // ---------------------------------------------------------------------------
  /**
   * Retourne le nombre de ligne supprimé dans la base.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCountDelete() {
    if (sql_delete != null)
      return sql_delete.getCount();
    else
      return 0;
  }
  // ---------------------------------------------------------------------------
}
