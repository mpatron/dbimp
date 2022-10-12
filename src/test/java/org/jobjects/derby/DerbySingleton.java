package org.jobjects.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.tools.JObjectsLogFormatter;

public class DerbySingleton {
  private transient static Logger LOGGER = Logger.getLogger(DerbySingleton.class.getName());

  private static final DerbySingleton instance = new DerbySingleton();

  protected DerbySingleton() {
    try {
      JObjectsLogFormatter.initializeLogging();
      //Class.forName(DerbyConstantes.DRIVER_EMBEDDED_CLASSNAME);
      // Class.forName(DerbyConstantes.DRIVER_CLASSNAME).newInstance();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

  // Runtime initialization
  // By default ThreadSafe
  public static DerbySingleton getInstance() {
    return instance;
  }

  private boolean started = false;

  /**
   * @return the started
   */
  public boolean isStarted() {
    return started;
  }

  /**
   * @param started
   *          the started to set
   */
  protected void setStarted(boolean started) {
    this.started = started;
  }

  public void start() {
    if (!isStarted()) {
      try {
        Properties p = new Properties();
        p.setProperty(DerbyConstantes.USER, DerbyConstantes.USER_VALUE);
        p.setProperty(DerbyConstantes.PASSWORD, DerbyConstantes.PASSWORD_VALUE);
        p.setProperty("create", "true");
        Connection conn = DriverManager.getConnection(DerbyConstantes.URL, p);
        CreateSchema.createSchema(conn, DerbyConstantes.SCHEMA_NAME);
        conn.close();
        setStarted(true);
        LOGGER.log(Level.INFO, "Derby is started.");
      } catch (SQLException e) {
        setStarted(false);
        LOGGER.log(Level.SEVERE, "Erreur non prévu : ", e);
      }
    } else {
      LOGGER.log(Level.WARNING, "Derby is even started.");
    }
  }

  public void stop() {
    if (isStarted()) {
      try {
        LOGGER.info("Extinction de Derby");
        if(isStarted()) {
          DriverManager.getConnection(DerbyConstantes.URL + ";shutdown=true");
        }
        setStarted(false);
      } catch (SQLException ignored) {
        LOGGER.log(Level.INFO, "Extinction de " + DerbyConstantes.URL + " : " + ignored.getLocalizedMessage());
      }
      try {
        LOGGER.info("Extinction de Derby");
        if(isStarted()) {
          DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        setStarted(false);
      } catch (SQLException ignored) {
        LOGGER.log(Level.INFO, "Extinction de derby : " + ignored.getLocalizedMessage());
      }
    } else {
      LOGGER.log(Level.WARNING, "Derby is not started.");
    }
  }

  public Connection getConnection() throws SQLException {
    Properties p = new Properties();
    p.setProperty(DerbyConstantes.USER, DerbyConstantes.USER_VALUE);
    p.setProperty(DerbyConstantes.PASSWORD, DerbyConstantes.PASSWORD_VALUE);
    return DriverManager.getConnection(DerbyConstantes.URL, p);
  }

}
