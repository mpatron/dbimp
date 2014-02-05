package org.jobjects.dbimp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.derby.CreateSchema;
import org.jobjects.derby.DerbyConstantes;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ImportationTest {
	private Logger LOGGER = Logger.getLogger(getClass().getName());
	
	@BeforeClass(groups = "MaSuite")
	public void beforeClass() throws Exception {
		// DerbyStart.setUpBeforeClass();
	}

	@AfterClass(groups = "MaSuite")
	public void afterClass() throws Exception {
		// DerbyStop.tearDownAfterClass();
	}

	@Test(groups = "MaSuite")
	public void importFile() {
		try {
			Class.forName(DerbyConstantes.DRIVER_CLASSNAME);
			Properties p = new Properties();
			p.setProperty(DerbyConstantes.USER, DerbyConstantes.USER_VALUE);
			p.setProperty(DerbyConstantes.PASSWORD,
					DerbyConstantes.PASSWORD_VALUE);
			p.setProperty("create", "true");// +";create=true"
			Connection conn = DriverManager.getConnection(DerbyConstantes.URL,
					p);

			Statement stmp = conn.createStatement();
			try {
				stmp.execute("create schema " + DerbyConstantes.SCHEMA_NAME
						+ " AUTHORIZATION " + DerbyConstantes.USER_VALUE);
			} catch(Exception e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} finally {
				stmp.close();
			}
			CreateSchema.createSchema(conn, DerbyConstantes.SCHEMA_NAME);

			System.out.println(ClassLoader
					.getSystemResource("org/jobjects/dbimp/userfilename.asc"));
			String fileSource = new File(ClassLoader.getSystemResource(
					"org/jobjects/dbimp/userfilename.asc").toURI())
					.getAbsolutePath();
			String fileSourceEncoding = "ISO-8859-1";
			String fileNameParameter = new File(ClassLoader.getSystemResource(
					"org/jobjects/dbimp/userfilename.xml").toURI())
					.getAbsolutePath();
			// String schemaName="MYDERBYDB";
			boolean cached = false;
			boolean verbose = true;
			String fileNameReport = File.createTempFile("imp", ".txt")
					.getAbsolutePath();
			System.out.println("fileNameReport=" + fileNameReport);
			Importation.importFile(fileSource, fileSourceEncoding,
					fileNameParameter, conn,  DerbyConstantes.SCHEMA_NAME, cached, verbose,
					fileNameReport);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
