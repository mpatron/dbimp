package org.jobjects.dbimp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.derby.CreateSchema;
import org.jobjects.derby.DerbyConstantes;
import org.jobjects.derby.DerbySingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImportationTest {
  private static Logger LOGGER = Logger.getLogger(ImportationTest.class.getName());

  private static Connection conn;

  @BeforeAll
  public static void beforeClass() throws Exception {
    DerbySingleton.getInstance().start();
    conn = DerbySingleton.getInstance().getConnection();

    CreateSchema.createSchema(conn, DerbyConstantes.SCHEMA_NAME);
  }

  @AfterAll
  public static void afterClass() throws Exception {
    CreateSchema.afficheSchema(conn, DerbyConstantes.SCHEMA_NAME);
    conn.close();
    DerbySingleton.getInstance().stop();
  }

  @Test
  public void importFileParam() {

  }

  @Test
  @DisplayName("IMportation des fichiers users")
  public void importFileAsc() {
    try {
      LOGGER.fine("" + ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename.asc"));
      String fileSource = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename.asc").toURI()).getAbsolutePath();
      String fileSourceEncoding = "ISO-8859-1";
      String fileNameParameter = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename-asc.xml").toURI()).getAbsolutePath();
      // String schemaName="MYDERBYDB";
      boolean cached = false;
      boolean verbose = true;
      String fileNameReport = File.createTempFile("imp", ".txt").getAbsolutePath();
      LOGGER.fine("fileNameReport=" + fileNameReport);
      Importation.importFile(fileSource, fileSourceEncoding, fileNameParameter, conn, DerbyConstantes.SCHEMA_NAME, cached, verbose,
          fileNameReport);
      assertTrue(true); // ??? Pas sur
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      assertTrue(false);
    }
  }

  @Test
  public void importFileCsv() {
    try {
      LOGGER.fine("" + ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename.csv"));
      String fileSource = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename.csv").toURI()).getAbsolutePath();
      String fileSourceEncoding = "ISO-8859-1";
      String fileNameParameter = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/userfilename-csv.xml").toURI()).getAbsolutePath();
      // String schemaName="MYDERBYDB";
      boolean cached = false;
      boolean verbose = true;
      String fileNameReport = File.createTempFile("imp", ".txt").getAbsolutePath();
      LOGGER.fine("fileNameReport=" + fileNameReport);
      Importation.importFile(fileSource, fileSourceEncoding, fileNameParameter, conn, DerbyConstantes.SCHEMA_NAME, cached, verbose,
          fileNameReport);
      assertTrue(true); // ??? Pas sur
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      assertTrue(false);
    }
  }

  @Test
  public void importFileCsvAll() {
    try {
      LOGGER.fine("" + ClassLoader.getSystemResource("org/jobjects/dbimp/api.randomuser.me.csv"));
      String fileSource = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/api.randomuser.me.csv").toURI()).getAbsolutePath();
      String fileSourceEncoding = "UTF-8";
      String fileNameParameter = new File(ClassLoader.getSystemResource("org/jobjects/dbimp/api.randomuser.me.csv.xml").toURI()).getAbsolutePath();
      // String schemaName="MYDERBYDB";
      boolean cached = false;
      boolean verbose = true;
      String fileNameReport = File.createTempFile("imp", ".txt").getAbsolutePath();
      LOGGER.fine("fileNameReport=" + fileNameReport);
      Importation.importFile(fileSource, fileSourceEncoding, fileNameParameter, conn, DerbyConstantes.SCHEMA_NAME, cached, verbose,
          fileNameReport);
     assertTrue(true); // ??? Pas sur
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      assertTrue(false);
    }
  }

  
}
