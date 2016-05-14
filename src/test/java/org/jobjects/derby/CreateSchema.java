package org.jobjects.derby;

import java.sql.Connection;
import java.sql.ResultSet;
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

      {
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

      {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE " + schema + ".SECU_USER (";
        sql += " USERNAME VARCHAR(255) NOT NULL,";
        sql += " PASSWORD VARCHAR(255),";
        sql += " MONCHAMPSDATETIME TIMESTAMP,";
        sql += " PRIMARY KEY (USERNAME)";
        sql += " )";
        stmt.execute(sql);
        // stmt.execute("ALTER TABLE "+schema+".SECU_USER ADD PRIMARY KEY
        // (username)");
        stmt.execute("INSERT INTO " + schema + ".SECU_USER (USERNAME, PASSWORD) VALUES ('myName', 'myPassword')");
        stmt.close();
      }

      {
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
      LOGGER.log(Level.SEVERE, "Erreur non prévu : ", e.getMessage());
    }
  }

  public static void afficheSchema(Connection conn, String schema) {
    try (Statement stmt = conn.createStatement()) {
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".SECU_USER"));
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".SECU_USER_ROLE"));
      SqlUtils.Affiche(stmt.executeQuery("SELECT * FROM " + schema + ".MYTABLE"));

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Erreur non prévu : ", e.getMessage());
    }
  }
}
