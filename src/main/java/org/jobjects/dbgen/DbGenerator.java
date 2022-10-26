package org.jobjects.dbgen;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jobjects.dbimp.Importation;
import org.jobjects.tools.JObjectsLogFormatter;

import com.github.javafaker.Faker;

public class DbGenerator {
  private Logger LOGGER = Logger.getLogger(DbGenerator.class.getName());
  public final static String USER = "MYUSERNAME".toLowerCase();
  public final static String PASSWORD = "MYPASS";
  public final static String SCHEMA_NAME = "MYSCHEMA";
  //public final static String url = "jdbc:postgresql://localhost:5432/postgres";
  public final static String url = "jdbc:postgresql://";
  public final static String serverName = "localhost";
  public final static String portNumber = "5432";
  public final static String databaseName = "postgres";

  private String getConnectionUrl() {
    return url + serverName + ":" + portNumber + "/" + databaseName;
  }

  private Connection getConnection() {
    Connection returnValue=null;
    try {
      Properties props = new Properties();
      props.setProperty("user", USER);
      props.setProperty("password", PASSWORD);
      props.setProperty("reWriteBatchedInserts", "true");
      props.setProperty("ApplicationName", this.getClass().getName());      
      //props.setProperty("ssl", "true");
      
      Class.forName("org.postgresql.Driver");
      returnValue = java.sql.DriverManager.getConnection(getConnectionUrl(), props);
      if (returnValue != null)
        LOGGER.fine("Connection Successful!");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    return returnValue;
  }

  public static void main(String[] args) throws SQLException {
    DbGenerator instance = new DbGenerator();
    Connection connection=instance.getConnection();
    JObjectsLogFormatter.initializeLogging();
    instance.LOGGER.log(Level.FINEST, "begin");
    instance.parameterizedBatchUpdate(connection);
  }
    
  private void parameterizedBatchUpdate(Connection connection) throws SQLException {    
    String sql = "INSERT INTO " + SCHEMA_NAME + ".SECU_USER";
    sql += " (";
    sql += " USERNAME,";
    sql += " PASSWORD,";
    sql += " MONCHAMPSDATETIME,";
    sql += " GENDER,";
    sql += " TITLE,";
    sql += " FIRSTNAME,";
    sql += " LASTNAME,";
    sql += " STREET,";
    sql += " CITY,";
    sql += " STATE,";
    sql += " POSTCODE,";
    sql += " EMAIL";
    sql += " ) VALUES (";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?,";
    sql += " ?";
    sql += " )";
    
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    connection.setAutoCommit(false);
    LOGGER.log(Level.FINEST, "begin");
    for (int i = 0; i < 10000; i++) {
      int j =1;
      preparedStatement.setString(j++, RandomStringUtils.randomAlphabetic(20));
      preparedStatement.setString(j++, RandomStringUtils.randomAlphabetic(64));
      Calendar beginner = Calendar.getInstance();
      beginner.set(1950, 10, 13);
      java.util.Date start= beginner.getTime();
      java.util.Date end = new java.util.Date();
      java.util.Date randdate = Faker.instance(Locale.FRANCE).date().between(start, end);
      preparedStatement.setDate(j++, new Date(randdate.getTime() ) );
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).demographic().sex());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).demographic().sex());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).name().firstName());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).name().lastName());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).address().fullAddress());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).address().city());
      preparedStatement.setString(j++, StringUtils.truncate(Faker.instance(Locale.FRANCE).address().country(),40));
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).address().countryCode());
      preparedStatement.setString(j++, Faker.instance(Locale.FRANCE).internet().emailAddress());
      preparedStatement.addBatch();
    }
    LOGGER.log(Level.FINEST, "coming");
    int[] updateCounts = preparedStatement.executeBatch();
    //System.out.println(Arrays.toString(updateCounts));
    connection.commit();
    connection.setAutoCommit(true);

    preparedStatement.close();
    LOGGER.log(Level.FINEST, "end");
    connection.close();
    LOGGER.finer("CREATE SCHEMA " + SCHEMA_NAME + " AUTHORIZATION " + USER);

    sql = "CREATE TABLE " + SCHEMA_NAME + ".MYTABLE (";
    sql += " MONCHAMPSTEXTE VARCHAR(6) NOT NULL,";
    sql += " MONCHAMPSCHAR CHAR(2) NOT NULL,";
    sql += " MONCHAMPSDATE DATE,";
    sql += " MONCHAMPSDATETIME TIMESTAMP,";
    sql += " MONCHAMPSDECIMAL DOUBLE";
    sql += " )";
    LOGGER.finer(sql);
    sql = "CREATE TABLE " + SCHEMA_NAME + ".SECU_USER (";
    sql += " USERNAME VARCHAR(20) NOT NULL CONSTRAINT USERNAME_PK PRIMARY KEY,";
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
    sql += " EMAIL VARCHAR(320))";
    // stmt.execute("INSERT INTO " + schema + ".SECU_USER (USERNAME, PASSWORD)
    // VALUES ('myName', 'myPassword')");
    LOGGER.log(Level.FINEST, String.format("Création de la table %s.%s", SCHEMA_NAME, "SECU_USER"));
    LOGGER.log(Level.FINEST, sql);

    sql = "CREATE TABLE " + SCHEMA_NAME + ".SECU_USER_ROLE (";
    sql += " USERNAME VARCHAR(20) NOT NULL,";
    sql += " ROLENAME VARCHAR(255) NOT NULL,";
    sql += " MONCHAMPSDATETIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,";
    sql += " CONSTRAINT SECU_USER_ROLE_PK PRIMARY KEY (USERNAME, ROLENAME),";
    sql += " CONSTRAINT SECU_USER_ROLE_SECU_USER_FK FOREIGN KEY (USERNAME) REFERENCES " + SCHEMA_NAME
        + ".SECU_USER (USERNAME)";
    sql += " )";
    LOGGER.log(Level.FINEST, String.format("Création de la table %s.%s", SCHEMA_NAME, "SECU_USER_ROLE"));
    LOGGER.log(Level.FINEST, sql);

//          stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'tomcat')");
//          stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'admin')");
//          stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'root')");
//          stmt.execute("INSERT INTO " + schema + ".SECU_USER_ROLE (USERNAME, ROLENAME) VALUES ('myName', 'dieu')");

  }



}
