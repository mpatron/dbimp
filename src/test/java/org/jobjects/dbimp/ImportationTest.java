package org.jobjects.dbimp;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.derby.CreateSchema;
import org.jobjects.derby.DerbyConstantes;
import org.jobjects.derby.DerbySingleton;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ImportationTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  private Connection conn;

  @BeforeClass(groups = "MaSuite")
  public void beforeClass() throws Exception {
    DerbySingleton.getInstance().start();
    conn = DerbySingleton.getInstance().getConnection();

    Statement stmp = conn.createStatement();
    try {
      stmp.execute("create schema " + DerbyConstantes.SCHEMA_NAME + " AUTHORIZATION " + DerbyConstantes.USER_VALUE);
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, e.getMessage());
    } finally {
      stmp.close();
    }
    CreateSchema.createSchema(conn, DerbyConstantes.SCHEMA_NAME);
  }

  @AfterClass(groups = "MaSuite")
  public void afterClass() throws Exception {
    CreateSchema.afficheSchema(conn, DerbyConstantes.SCHEMA_NAME);
    conn.close();
  }

  @Test(groups = "MaSuite")
  public void importFileParam() {

  }

  @Test(groups = "MaSuite")
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
      Assert.assertTrue(true); // ??? Pas sur
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.assertTrue(false);
    }
  }

  @Test(groups = "MaSuite")
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
      Assert.assertTrue(true); // ??? Pas sur
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.assertTrue(false);
    }

  }
}
