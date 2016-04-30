package org.jobjects.dbimp.sql;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jobjects.dbimp.report.ReportLine;
import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;

/**
 * Class d'utilitaire pour les class herités.
 * 
 * @author Mickael Patron
 * 
 */
public abstract class SqlStatement extends SqlPrimary implements SqlAction {

  private static Logger log = Logger.getLogger(SqlStatement.class.getName());

  private Line xmlline = null;

  private ReportTypeLine reportTypeLine = null;

  private Collection<String> primaries = null;

  private boolean cached = false;

  private PreparedStatement pstmtCached = null;

  private String sql = null;

  protected SqlStatement(Connection connection, String schemaName, boolean cached, Line xmlline, ReportTypeLine reportTypeLine) throws SQLException {
    super(connection, schemaName);
    this.cached = cached;
    this.reportTypeLine = reportTypeLine;
    this.xmlline = xmlline;

    primaries = getPrimaryColumns(xmlline.getTableName());
    sql = createSQL();
    log.fine("Bufferisation : " + sql);
    if (StringUtils.isEmpty(sql)) {
      log.severe("La requête, vennant du parametrage suivant, est vide :" + SystemUtils.LINE_SEPARATOR + xmlline.toString());
    }
    if (cached) {
      this.pstmtCached = getConnection().prepareStatement(sql);
    }
  }

  /**
   * @return La chaine sql crée spécialisalement pour le traitement.
   * @throws SQLException
   */
  public abstract String createSQL() throws SQLException;

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#close()
   */
  public void close() throws SQLException {
    if (pstmtCached != null) {
      pstmtCached.close();
      pstmtCached = null;
    }
  }

  // ---------------------------------------------------------------------------

  protected boolean checkIn(Line xmlline, Field xmlfield, ReportLine reporting) {
    boolean returnValue = true;
    if (!(xmlfield.isNullable() && xmlfield.isEmptyOrNullBuffer())) {
      if (xmlfield.getCheckIn() != null) {
        if (!xmlfield.getCheckIn().contains(xmlfield.getBuffer())) {
          if (xmlfield.isNullableError()) {
            reporting.getReportField(xmlfield).ERROR_FIELD_VALUE_IN_LIST();
          }
          String message = "Line (" + reporting.getNumberLine() + ") " + xmlfield.getName() + " has a bad value. ";
          message += SqlUtils.showLine(reporting.getNumberLine(), xmlline);
          log.severe(message);

          returnValue = false;
        }
      }
    }
    return returnValue;
  }

  // ---------------------------------------------------------------------------

  protected void setNull(PreparedStatement pstmt, int i, Field field) throws SQLException {
    switch (field.getTypeFormat()) {
    case STRING:
      pstmt.setNull(i, java.sql.Types.VARCHAR);
      break;
    case INTEGER:
      pstmt.setNull(i, java.sql.Types.INTEGER);

      break;

    case LONG:
      pstmt.setNull(i, java.sql.Types.INTEGER);

      break;

    case FLOAT:
      pstmt.setNull(i, java.sql.Types.FLOAT);

      break;

    case DOUBLE:
      pstmt.setNull(i, java.sql.Types.DOUBLE);

      break;

    case DATETIME:
      pstmt.setNull(i, java.sql.Types.TIMESTAMP);

      break;
    case BLOB:
      pstmt.setNull(i, java.sql.Types.BLOB);
      break;
    default:
      throw new SQLException(field.getName() + " = " + field.getBuffer() + " type de champ du parametrage inconnu.");
    }
  }

  // ---------------------------------------------------------------------------

  protected void setAll(PreparedStatement pstmt, int i, Field field) throws SQLException {
    double d;

    try {
      switch (field.getTypeFormat()) {
      case STRING:
        pstmt.setString(i, field.getBuffer());
        break;
      case INTEGER:
        d = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
        pstmt.setInt(i, (int) d);

        break;

      case LONG:
        d = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
        pstmt.setLong(i, (long) d);

        break;

      case FLOAT:
        d = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
        pstmt.setFloat(i, (float) d);

        break;

      case DOUBLE:
        d = Double.parseDouble(field.getBuffer()) * field.getCoefficient();
        pstmt.setDouble(i, d);

        break;

      case DATETIME:

        if (field.getBuffer().equalsIgnoreCase("sysdate")) {
          pstmt.setTimestamp(i, new Timestamp(System.currentTimeMillis()));
        } else {
          SimpleDateFormat sdf = new SimpleDateFormat(field.getDateFormat());
          pstmt.setTimestamp(i, new Timestamp(sdf.parse(field.getBuffer()).getTime()));
        }

        break;
      case BLOB:
        String filename = field.getBuffer();
        File file = new File(filename);
        DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int l = (int) file.length();
        byte[] bytes = new byte[l];
        fis.read(bytes);
        pstmt.setBytes(i, bytes);
        fis.close();
        fis = null;
        file = null;
        bytes = null;
        break;
      default:
        throw new SQLException(field.getName() + " = " + field.getBuffer() + " type de champ du parametrage inconnu.");
      }
    } catch (Exception pe) {
      log.severe(ExceptionUtils.getStackTrace(pe));
      // new SQLException("String message", pe);
      throw new SQLException(field.getName() + " = '" + field.getBuffer() + "' " + pe.getMessage());
    }
  }

  // ---------------------------------------------------------------------------

  /**
   * @return le curseur cached.
   */
  public PreparedStatement getPstmtCached() {
    return pstmtCached;
  }

  /**
   * @return Es-ce les curseur sont cachés ? Demande un nombre important de
   *         curseur.
   */
  public boolean isCached() {
    return cached;
  }

  /**
   * @return Retourne les clefs primaires.
   */
  public Collection<String> getPrimaries() {
    return primaries;
  }

  /**
   * @return Retourne le rapport.
   */
  public ReportTypeLine getReportTypeLine() {
    return reportTypeLine;
  }

  /**
   * @return La chaine SQL.
   */
  public String getSql() {
    return sql;
  }

  /**
   * @return Le parametrage de la ligne.
   */
  public Line getXmlline() {
    return xmlline;
  }

}