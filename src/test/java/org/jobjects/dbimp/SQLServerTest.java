package org.jobjects.dbimp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLServerTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  private java.sql.Connection con = null;

  private final String url = "jdbc:microsoft:sqlserver://";

  private final String serverName = "MY_SQLSERVE";// localhost

  private final String portNumber = "1433";

  private final String databaseName = "pubs";

  private final String userName = "sa";

  private final String password = "jupiter";

  // Informs the driver to use server a side-cursor,
  // which permits more than one active statement
  // on a connection.
  private final String selectMethod = "cursor";

  // "jdbc:microsoft:sqlserver://MY_SERVE:1433;DatabaseName=MYDB" -U sa -P
  // jupiter -f init22.asc -x init22.xml -r .
  // -d com.microsoft.jdbc.sqlserver.SQLServerDriver

  // Constructor
  public SQLServerTest() {
  }

  private String getConnectionUrl() {
    return url + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";selectMethod=" + selectMethod + ";";
  }

  private java.sql.Connection getConnection() {
    try {
      Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
      con = java.sql.DriverManager.getConnection(getConnectionUrl(), userName, password);
      if (con != null)
        LOGGER.fine("Connection Successful!");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    return con;
  }

  /*
   * Display the driver properties, database details
   */

  public void displayDbProperties() {
    java.sql.DatabaseMetaData dm = null;
    java.sql.ResultSet rs = null;
    try {
      con = this.getConnection();
      if (con != null) {
        dm = con.getMetaData();
        LOGGER.finest("Driver Information");
        LOGGER.finest("\tDriver Name: " + dm.getDriverName());
        LOGGER.finest("\tDriver Version: " + dm.getDriverVersion());
        LOGGER.finest("\nDatabase Information ");
        LOGGER.finest("\tDatabase Name: " + dm.getDatabaseProductName());
        LOGGER.finest("\tDatabase Version: " + dm.getDatabaseProductVersion());
        LOGGER.finest("Avalilable Catalogs ");
        rs = dm.getCatalogs();
        while (rs.next()) {
          LOGGER.finest("\tcatalog: " + rs.getString(1));
        }
        rs.close();
        rs = null;
        closeConnection();
      } else
        LOGGER.severe("Error: No active Connection");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    dm = null;
  }

  private void closeConnection() {
    try {
      if (con != null)
        con.close();
      con = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    SQLServerTest myDbTest = new SQLServerTest();
    myDbTest.displayDbProperties();
  }
}