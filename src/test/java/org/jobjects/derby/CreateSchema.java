package org.jobjects.derby;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.dbimp.sql.SqlUtils;

public class CreateSchema {
  private static Logger LOGGER = Logger.getLogger(CreateSchema.class.getName());

  public static void createSchema(Connection conn, String schema) {
    try {

      Statement stmp = conn.createStatement();
      try {
        stmp.execute("create schema " + DerbyConstantes.SCHEMA_NAME + " AUTHORIZATION " + DerbyConstantes.USER_VALUE);
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, e.getMessage());
      } finally {
        stmp.close();
      }

      if (!isExistTable(conn, schema, "MYTABLE")) {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE " + schema + ".MYTABLE (";
        sql += " MONCHAMPSTEXTE VARCHAR(6) NOT NULL,";
        sql += " MONCHAMPSCHAR CHAR(2) NOT NULL,";
        sql += " MONCHAMPSDATE DATE,";
        sql += " MONCHAMPSDATETIME TIMESTAMP,";
        sql += " MONCHAMPSDECIMAL DOUBLE";
        sql += " )";
        stmt.execute(sql);
        stmt.execute("ALTER TABLE " + schema + ".MYTABLE ADD PRIMARY KEY (MONCHAMPSTEXTE, MONCHAMPSCHAR)");
        stmt.close();
      }

      if (!isExistTable(conn, schema, "SECU_USER")) {
        // gender name.title name.first name.last location.street location.city
        // location.state location.postcode email login.username login.password
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE " + schema + ".SECU_USER (";
        sql += " USERNAME VARCHAR(20) NOT NULL,";
        sql += " PASSWORD VARCHAR(64),";
        sql += " MONCHAMPSDATETIME TIMESTAMP,";
        sql += " GENDER VARCHAR(10),";
        sql += " TITLE VARCHAR(15),";
        sql += " FIRSTNAME VARCHAR(20),";
        sql += " LASTNAME VARCHAR(20),";
        sql += " STREET VARCHAR(120),";
        sql += " CITY VARCHAR(40),";
        sql += " STATE VARCHAR(40),";
        sql += " POSTCODE VARCHAR(20),";
        sql += " EMAIL VARCHAR(320),";
        sql += " PRIMARY KEY (USERNAME)";
        sql += " )";
        stmt.execute(sql);
        // stmt.execute("ALTER TABLE "+schema+".SECU_USER ADD PRIMARY KEY
        // (username)");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER (USERNAME, PASSWORD) VALUES ('myName', 'myPassword')");
        stmt.close();
      }

      if (!isExistTable(conn, schema, "SECU_USER_ROLE")) {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE " + schema + ".SECU_USER_ROLE (";
        sql += " USERNAME VARCHAR(255) NOT NULL,";
        sql += " ROLENAME VARCHAR(255) NOT NULL,";
        sql += " MONCHAMPSDATETIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,";
        sql += " PRIMARY KEY (USERNAME, ROLENAME),";
        sql += " FOREIGN KEY(USERNAME) REFERENCES " + schema + ".SECU_USER (USERNAME)";
        sql += " )";
        stmt.execute(sql);
        // stmt.execute("ALTER TABLE "+schema+".secu_user_role ADD PRIMARY KEY
        // (username, rolename)");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'tomcat')");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'admin')");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'root')");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'dieu')");
        stmt.close();
      }

      final ResultSet tables = conn.getMetaData().getTables(null, schema, "%", new String[] { "TABLE" });
      List<String> tableNames = new ArrayList<String>();
      while (tables.next()) {
        tableNames.add(tables.getString("TABLE_NAME").toLowerCase());
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

  public static boolean isExistTable(Connection conn, String schema, String table) throws SQLException {
    boolean returnValue = false;
    LOGGER.log(Level.FINEST,
        String.format("Vérification de la présence de la table %s car derby n'a pas de [IF EXIST]", table));
    DatabaseMetaData databaseMetadata = conn.getMetaData();
    ResultSet resultSet = databaseMetadata.getTables(null, schema, table, null);
    returnValue = resultSet.next();
    return returnValue;
  }

  public static void afficheSchema(Connection conn, String schema) {
    try (Statement stmt = conn.createStatement()) {
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".SECU_USER"));
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".SECU_USER_ROLE"));
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".MYTABLE"));

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Erreur non prévu : ", e);
    }
  }
}
