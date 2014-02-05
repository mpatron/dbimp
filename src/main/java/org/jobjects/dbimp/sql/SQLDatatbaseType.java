package org.jobjects.dbimp.sql;

import org.apache.commons.lang3.StringUtils;

/**
 * @author MP
 * @version 18/10/2006:
 */
public enum SQLDatatbaseType {

  UNKNOW_DATABASE("sun.jdbc.odbc.JdbcOdbcDriver"),
  ORACLE("oracle.jdbc.OracleDriver"),
  SQLSERVER("com.microsoft.jdbc.sqlserver.SQLServerDriver"),
  DB2AS400("com.ibm.as400.access.AS400JDBCDriver");

  private static final long serialVersionUID = 1L;

  private String driver = null;

  /**
   * @param name
   */
  private SQLDatatbaseType(String driver) {
    this.driver = driver;
  }

  public static SQLDatatbaseType getType(String jdbcUrl) {
    SQLDatatbaseType returnValue = UNKNOW_DATABASE;
    String urlName = jdbcUrl;
    if (urlName.startsWith(StringUtils.lowerCase("jdbc:oracle"), 0)) {
      // jdbc:oracle:thin:@<server>:1521:<instance>
      returnValue = ORACLE;
    }
    if (urlName.startsWith(StringUtils.lowerCase("jdbc:microsoft"), 0)) {
      // jdbc:microsoft:sqlserver://<server>:1433;DatabaseName=<base>
      returnValue = SQLSERVER;
    }
    if (urlName.startsWith(StringUtils.lowerCase("jdbc:as400"), 0)) {
      // jdbc:as400://<server>/<base>
      returnValue = DB2AS400;
    }
    return returnValue;
  }

  public static SQLDatatbaseType getEnum(String name) {
    SQLDatatbaseType returnValue = UNKNOW_DATABASE;
    if (null != name) {
      try {
        returnValue = SQLDatatbaseType.valueOf(name);
      } catch (IllegalArgumentException e) {
        // Ne rien faire, la valeur par defaut est affecté.
      }
    }
    return returnValue;
  }

  public String getDriver() {
    return driver;
  }

}
