package org.jobjects.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DerbyStart {

  private static Logger LOGGER = Logger.getLogger(DerbyStart.class.getName());

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    try {
      Class.forName(DerbyConstantes.DRIVER_CLASSNAME).newInstance();
      Properties p = new Properties();
      p.setProperty(DerbyConstantes.USER, DerbyConstantes.USER_VALUE);
      p.setProperty(DerbyConstantes.PASSWORD, DerbyConstantes.PASSWORD_VALUE);
      p.setProperty("create", "true");
      Connection conn = DriverManager.getConnection(DerbyConstantes.URL, p);

      Properties p2 = new Properties();
      p2.setProperty(DerbyConstantes.USER, DerbyConstantes.USER_VALUE);
      p2.setProperty(DerbyConstantes.PASSWORD, DerbyConstantes.PASSWORD_VALUE);
      Connection conn2 = DriverManager.getConnection(DerbyConstantes.URL, p2);
      conn2.close();

      CreateSchema.createSchema(conn, DerbyConstantes.SCHEMA_NAME);

      conn.close();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Erreur non prévu : ", e);
    }
  }

  @Test(groups = "MaSuite")
  public void testStart() {
    LOGGER.log(Level.INFO, "Derby Stating");
  }

}
