package org.jobjects.dbexp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.SystemUtils;
import org.jobjects.dbimp.FileAsciiWriter;

/**
 * <p>Title: IHM</p>
 * <p>Description: Exportation dbExp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * 
 * @author Mickael Patron
 * @version 1.0
 */

public class ExpTable {
  private Logger log = Logger.getLogger(getClass().getName());

  private Connection connection = null;
  private String schema = null;
  private FileAsciiWriter fileWriterAsc = null;
  private FileAsciiWriter fileWriterXml = null;

  public ExpTable(Connection connection, String schema,
      FileAsciiWriter fileWriterAsc, FileAsciiWriter fileWriterXml)
      throws IOException {
    this.connection = connection;
    this.schema = schema;
    this.fileWriterAsc = fileWriterAsc;
    this.fileWriterXml = fileWriterXml;
  }

  public void run(String tablename) {
    StringBuffer xml = new StringBuffer();
    String sql = "select * from " + tablename;
    try {
      Statement stmt = connection.createStatement();
      try {
        ResultSet rs = stmt.executeQuery(sql);
        try {
          // ResultSetMetaData rsmd= rs.getMetaData();
          // int columnCount= rsmd.getColumnCount();

          Collection<SqlTypesEnum> fields = SqlTypesEnum.getColumns(
              connection.getMetaData(), schema, tablename.toUpperCase());

          boolean first = true;
          int position = 0;
          int old_position = 0;
          xml.append("<line name=\"" + tablename + "\" tablename=\""
              + tablename + "\">");
          xml.append(SystemUtils.LINE_SEPARATOR);
          xml.append("  <key value=\"" + tablename
              + "#\" startposition=\"0\" size=\"" + (tablename.length() + 1)
              + "\"/>");
          xml.append(SystemUtils.LINE_SEPARATOR);
          position = tablename.length() + 1; // le +1 du au #
          old_position = position;
          while (rs.next()) {

            fileWriterAsc.write(tablename + "#");

            Iterator<SqlTypesEnum> it = fields.iterator();
            while (it.hasNext()) {
              SqlTypesEnum sqlTypesEnum = it.next();
              String format = "MM/dd/yyyy HH:mm:ss.SSS";

              String buffer = null;
              if ((sqlTypesEnum.getDataType() == Types.TIMESTAMP)
                  || (sqlTypesEnum.getDataType() == Types.TIME)
                  || (sqlTypesEnum.getDataType() == Types.DATE)) {
                Timestamp timestamp = rs.getTimestamp(sqlTypesEnum.getName());
                buffer = formatWithLength(timestamp, format.length(), format);
                position += format.length();
              } else {
                Object object = rs.getObject(sqlTypesEnum.getName());
                buffer = formatWithLength(object, sqlTypesEnum.getSize());
                position += sqlTypesEnum.getSize();
              }

              if (first) {
                xml.append("  <field fieldname=\"" + sqlTypesEnum.getName()
                    + "\">");
                if ((sqlTypesEnum.getDataType() == Types.TIMESTAMP)
                    || (sqlTypesEnum.getDataType() == Types.TIME)
                    || (sqlTypesEnum.getDataType() == Types.DATE)) {
                  xml.append("<datetime dateformat=\"" + format + "\"/>");
                } else {
                  xml.append("<");
                  xml.append(sqlTypesEnum.getXmlType());
                  xml.append("/>");
                }
                xml.append("<position startposition=\"" + old_position + "\"");
                xml.append(" size=\"" + (position - old_position) + "\"/>");
                xml.append("</field>");
                xml.append(SystemUtils.LINE_SEPARATOR);
              }
              old_position = position;

              fileWriterAsc.write(buffer);

            }

            fileWriterAsc.write(SystemUtils.LINE_SEPARATOR);
            first = false;
          }

          xml.append("</line>");
          xml.append(SystemUtils.LINE_SEPARATOR);

          if (!first) {
            fileWriterXml.write(xml.toString());
          }

        } finally {
          rs.close();
        }
        rs = null;
      } finally {
        stmt.close();
      }
      stmt = null;
    } catch (SQLException e) {
      log.log(Level.SEVERE, sql, e);
    } catch (IOException e) {
      log.log(Level.SEVERE, sql, e);
    }
  }

  /**
   * Renvoie l'entier passée en paramètre (sous forme de chaine) avec les
   * espaces manquants pour atteindre la longueur <i>length</i>.<br>
   */
  public String formatWithLength(Object buff, int length) {
    /**
     * Information : String.valueOf(buff) return "null" si buff est null.
     */
    String returnValue = null;
    try {
      if (buff != null) {
        returnValue = String.valueOf(buff).trim();
        if (returnValue.length() <= length) {
          if ((buff instanceof java.lang.Long)
              || (buff instanceof java.lang.Double)) {
            for (int i = returnValue.length(); i < length; i++) {
              returnValue = " " + returnValue;
            }
          } else {
            for (int i = returnValue.length(); i < length; i++) {
              returnValue += " ";
            }
          }
        } else {
          returnValue = returnValue.substring(0, length);
        }
      } else {
        returnValue = "";
        for (int i = 0; i < length; i++) {
          returnValue += " ";
        }
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error during the formatting of string", e);
    }
    return returnValue;
  }

  // -------------------------------------------------------------------------

  /**
   * Renvoie la date passée en paramètre (sous forme de chaine formatée selon le
   * format <i>format</i><br>
   * avec les espaces manquants pour atteindre la longueur <i>length</i>.<br>
   */
  public String formatWithLength(Timestamp buff, int length, String format) {
    String returnValue = "";
    try {
      if (buff != null) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        returnValue = formatter.format(buff);
        if (returnValue.length() <= length) {
          for (int i = returnValue.length(); i < length; i++) {
            returnValue += " ";
          }
        } else {
          returnValue = returnValue.substring(0, length);
        }
      } else {
        for (int i = 0; i < length; i++) {
          returnValue += " ";
        }
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error during formatting of timestamp.", e);
    }
    return returnValue;
  }

  // ---------------------------------------------------------------------------

}
