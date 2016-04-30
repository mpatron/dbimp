package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportTypeLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.xml.XmlField;

/**
 * <p>Title: IHM</p>
 * <p>Description: Importation dbImp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * <p>Date :  1 mai 2003</p>
 * @author Mickael Patron
 * @version 1.0
 */
public class SqlUpdateBlob extends SqlStatement {
  private static Logger log = Logger.getLogger(SqlUpdateBlob.class.getName());

  private int count = 0;

  // ---------------------------------------------------------------------------

  /**
   * Method SqlSelect.
   * 
   * @param connection
   * @param xmlline
   * @param reportTypeLine
   * @throws SQLException
   */
  public SqlUpdateBlob(Connection connection, String schemaName,
      boolean cached, Line xmlline, ReportTypeLine reportTypeLine)
      throws SQLException {
    super(connection, schemaName, cached, xmlline, reportTypeLine);
  }

  // ---------------------------------------------------------------------------

  /**
   * Method createSQL.
   * 
   * @return String
   * @throws SQLException
   */
  public String createSQL() throws SQLException {
    String returnValue = "select ";
    String where = "";
    boolean first = true;
    for (Field field : getXmlline().getFields()) {
      if (!field.isUse())
        continue;
      if (field.getType() != FieldFormatEnum.BLOB) {
        continue;
      }
      if (first) {
        first = false;
        returnValue += field.getName();
      } else {
        returnValue += (", " + field.getName());
      }
    }
    returnValue += (" from " + getSQLSchemaName() + getXmlline().getTableName());
    if (first) {
      log.severe("Error no field with type BLOB.");
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
    returnValue += " for update";
    return returnValue;
  }

  // ---------------------------------------------------------------------------
  /**
   * @see org.jobjects.dbimp.sql.SqlAction#execute(int nbLigne)
   */
  public int execute(int nbLigne) {
    HashMap<String, String> returnValue = null;
    log.fine(getSql());
    try {
      boolean autoCommit = getConnection().getAutoCommit();
      getConnection().setAutoCommit(false);

      PreparedStatement pstmt = null;
      try {
        if (isCached()) {
          pstmt = getPstmtCached();
        } else {
          pstmt = getConnection().prepareStatement(getSql());
        }

        int i = 1;
        for (Field field : getXmlline().getFields()) {
          if (!field.isUse())
            continue;
          if (getPrimaries().contains(field.getName().toUpperCase())) {
            if (field.isEmptyOrNullBuffer()) {
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
            i = 1;
            for (Field field : getXmlline().getFields()) {
              if (!field.isUse())
                continue;
              if (field.getType() != FieldFormatEnum.BLOB)
                continue;
              /* ============= Code BLOB pour Oracle =========================== */
              // BLOB blob= ((OracleResultSet) rs).getBLOB(field.getName());
              // String filename= field.getBuffer();
              // File binaryFile= new File(filename);
              // log.debug("" + filename + " length = " + binaryFile.length());
              // DataInputStream fis= null;
              // try {
              // fis = new DataInputStream(new BufferedInputStream(new
              // FileInputStream(binaryFile)));
              // OutputStream os= blob.getBinaryOutputStream();
              // int size= 1024;
              // byte[] buffer= new byte[size];
              // int length= -1;
              // while ((length= fis.read(buffer)) != -1) {
              // os.write(buffer, 0, length);
              // }
              // os.close();
              // fis.close();
              // os= null;
              // fis= null;
              // } catch (IOException ioe) {
              // log.error("" + field.getBuffer(), ioe);
              // }
              /* =============================================================== */
              i++;
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
      getConnection().setAutoCommit(autoCommit);
    } catch (SQLException ex) {
      SqlUtils.AfficheSQLException(getXmlline(), getSql(), ex,
          getReportTypeLine().getReportLine());
    }
    count++;
    return (returnValue == null) ? -1 : 0;
  }

  // ---------------------------------------------------------------------------

  /**
   * @see org.jobjects.dbimp.sql.SqlAction#getCount()
   */
  public int getCount() {
    return count;
  }

  // ---------------------------------------------------------------------------

  public static boolean hasBlob(Line line) {
    boolean returnValue = false;
    //for(Field field : line.getFields())
    for (Iterator<Field> it = line.getFields().iterator(); it.hasNext();) {
      XmlField field = (XmlField) it.next();
      if (!field.isUse())
        continue;
      if (field.getType() == FieldFormatEnum.BLOB) {
        returnValue = true;
      }
    }
    return returnValue;
  }
}
