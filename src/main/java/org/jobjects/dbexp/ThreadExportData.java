package org.jobjects.dbexp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.jobjects.dbimp.FileAsciiReader;
import org.jobjects.dbimp.FileAsciiWriter;

/**
 * <p>Title: IHM</p>
 * <p>Description: Exportation</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * <p>2 mars 04</p>
 * @author Mickael Patron
 * @version 1.0
 */
public class ThreadExportData extends Thread {

	private String url = null;

	private String username = null;

	private String password = null;

	private String fileAsc = null;

	private String fileXml = null;

	private boolean end = false;

	public ThreadExportData(String url, String username, String password,
			String fileAsc, String fileXml) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.fileAsc = fileAsc;
		this.fileXml = fileXml;
	}

	private int progress = 0;

	public int getProgress() {
		return progress;
	}

	private String errorDescription = null;

	public String getErrorDescription() {
		return errorDescription;
	}

	public void run() {
		errorDescription = null;
		/*
		 * Execution du programme
		 */
		try {
			String driverClassName = "oracle.jdbc.driver.OracleDriver";
			Driver driver = (Driver) Class.forName(driverClassName)
					.newInstance();
			DriverManager.registerDriver(driver);
			Connection connection = DriverManager.getConnection(url, username,
					password);

			FileAsciiWriter fileWriterAsc = new FileAsciiWriter(fileAsc,
					"UTF-8");
			FileAsciiWriter fileWriterXml = new FileAsciiWriter(fileXml);
			fileWriterXml.write(SystemUtils.LINE_SEPARATOR);
			ExpTable expTable = new ExpTable(connection,
					username.toUpperCase(), fileWriterAsc, fileWriterXml);

			InputStream is = ThreadExportData.class
					.getResourceAsStream("header.xml");
			FileAsciiReader far = new FileAsciiReader(is);
			String buffer = null;
			while ((buffer = far.readLine()) != null) {
				fileWriterXml.write(buffer);
				fileWriterXml.write(SystemUtils.LINE_SEPARATOR);
			}
			far.close();
			is.close();
			fileWriterXml.flush();
			fileWriterXml.write("<document>");

			List<String> listTables = new ArrayList<String>();
			ResultSet rs = connection.getMetaData().getTables(null, username, "%", new String[] { "TABLE" });
			String chaine;
			while (rs.next()) {
				chaine = rs.getString("TABLE_NAME");
				if (!listTables.contains(chaine))
					listTables.add(chaine);
			}
			rs.close();
			String[] tables = (String[]) listTables
					.toArray(new String[listTables.size()]);
			for (int i = 0; i < tables.length; i++) {
				expTable.run(tables[i]);
				float value = i;
				value = value / tables.length;
				value *= 100;
				int iv = (int) value;
				progress = iv;
			}

			fileWriterXml.write("</document>");
			fileWriterXml.write(SystemUtils.LINE_SEPARATOR);
			fileWriterAsc.close();
			fileWriterXml.close();

			connection.close();
			DriverManager.deregisterDriver(driver);
		} catch (Exception e) {
			errorDescription = e.getMessage();
			e.printStackTrace();
		}
		end = true;
	}

	/**
	 * @return true if is the process is end.
	 */
	public boolean isEnd() {
		return end;
	}

}