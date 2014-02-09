package org.jobjects.dbimp.report;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jobjects.dbimp.FileAsciiWriter;
import org.jobjects.dbimp.trigger.Line;

/**
 * <p>Title: IHM</p>
 * <p>Description: Importation dbImp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * <p>Date :  4 sept. 2003</p>
 * @author Mickael Patron
 * @version 1.0
 */
public class Report implements Reporting {
	private String description = null;
	private String inputFile = null;
	private String paramFile = null;
	private String date = DateFormatUtils.ISO_DATETIME_FORMAT
			.format(new Date());
	private long duration;
	boolean verbose = false;

	private HashMap<String,ReportTypeLine> reportTypeLinesHashMap = new HashMap<String,ReportTypeLine>();
	private Stack<ReportTypeLine> reportTypeLines = new Stack<ReportTypeLine>();

	private StringBuffer internalBuffer = new StringBuffer();

	/*-----------------------------------*/
	private Logger log = Logger.getLogger(getClass().getName());
	private FileAsciiWriter bw = null;

	/**
	 * Création du rapport. Le flux est donnée ouvert à la classe. Il est donnée
	 * à la classe appelante de gérer le fichier.
	 * 
	 * @param bw
	 *            Flux ou fichier.
	 */
	public Report(FileAsciiWriter bw) {

		this.bw = bw;
	}

	// ---------------------------------------------------------------------------

	public FileAsciiWriter getWriter() {
		return this.bw;
	}

	/**
	 * @return La date heure de maintenant
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return La description contenu dans le fichier de paramètrage xml.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Le nom du fichier d'entrée passé en paramètre
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * @return Le nom du fichier de paramètrage passé en paramètre
	 */
	public String getParamFile() {
		return paramFile;
	}

	/**
	 * @param string
	 *            Spécifie la description
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 *            Spécifie le nom du fichier d'entrée passé en paramètre
	 */
	public void setInputFile(String string) {
		inputFile = string;
	}

	/**
	 * @param string
	 *            Spécifie le nom du fichier de paramètrage passé en paramètre
	 */
	public void setParamFile(String string) {
		paramFile = string;
	}

	/**
	 * @return Retourne la durée du traitement.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param l
	 *            Affecte la durée du traitement.
	 */
	public void setDuration(long l) {
		duration = l;
	}

	/**
	 * @return Le rapporteur type de ligne
	 */
	public ReportTypeLine getTypeLine(Line line) {
		ReportTypeLine returnValue = (ReportTypeLine) reportTypeLinesHashMap
				.get(line.getName());
		if (returnValue == null) {
			try {
				returnValue = new ReportTypeLine(this, line);
			} catch (IOException ioe) {
				log.log(Level.SEVERE,"Error while creating temporary file.", ioe);
			}
			reportTypeLinesHashMap.put(line.getName(), returnValue);
			reportTypeLines.push(returnValue);
		}
		return returnValue;

	}

	/**
	 * Method INFO_STATUS : Status sur l''importation {0} :
	 * 
	 * @param status
	 * @param selected
	 * @param inserted
	 * @param updated
	 * @param deleted
	 * @param rejected
	 * @return String
	 */
	public String INFO_STATUS(String status, int selected, int inserted,
			int updated, int deleted, int rejected) {
		String returnValue = null;
		internalBuffer.append(SystemUtils.LINE_SEPARATOR);
		internalBuffer
				.append("+=============================================================================+");
		internalBuffer.append(SystemUtils.LINE_SEPARATOR);
		internalBuffer.append("|");
		internalBuffer.append(SystemUtils.LINE_SEPARATOR);
		returnValue = RessourceReporting.getString("INFO_STATUS",
				new Object[] { status }) + SystemUtils.LINE_SEPARATOR;
		returnValue += RessourceReporting.getString("INFO_STATUS_SELECT",
				new Object[] { new Integer(selected) })
				+ SystemUtils.LINE_SEPARATOR;
		returnValue += RessourceReporting.getString("INFO_STATUS_INSERT",
				new Object[] { new Integer(inserted) })
				+ SystemUtils.LINE_SEPARATOR;
		returnValue += RessourceReporting.getString("INFO_STATUS_UPDATE",
				new Object[] { new Integer(updated) })
				+ SystemUtils.LINE_SEPARATOR;
		returnValue += RessourceReporting.getString("INFO_STATUS_DELETE",
				new Object[] { new Integer(deleted) })
				+ SystemUtils.LINE_SEPARATOR;
		// returnValue += RessourceReporting.getString("INFO_STATUS_REJECT", new
		// Object[] { new Integer(rejected)})
		// + SystemUtils.LINE_SEPARATOR;
		log.finest(returnValue);
		internalBuffer.append(returnValue);
		return returnValue;
	}

	public void nextLine(int numberLine) throws IOException {
		// Iterator it= reportTypeLines.values().iterator();
		Iterator<ReportTypeLine> it = reportTypeLines.iterator();
		while (it.hasNext()) {
			ReportTypeLine reportTypeLine = (ReportTypeLine) it.next();
			reportTypeLine.nextLine(numberLine);
		}
	}

	public void write() throws IOException {
		bw.writeln("+-----------------------------------------------------------------------------+");
		bw.writeln("|");
		bw.writeln("|    "
				+ RessourceReporting.getString("PROCESS_DESCRIPTION") + " : "
				+ description);
		bw.writeln("+=============================================================================+");
		bw.writeln("|");
		bw.writeln("|"
				+ StringUtils.center(
						RessourceReporting.getString("PROCESS_DATE"), 29, "")
				+ ": " + date);
		bw.writeln("|"
				+ StringUtils.center(
						RessourceReporting.getString("PROCESS_FILE_INPUT"), 29,
						"") + ": " + inputFile);
		bw.writeln("|"
				+ StringUtils.center(
						RessourceReporting.getString("PROCESS_FILE_PARAMETER"),
						29, "") + ": " + paramFile);
		bw.writeln("|"
				+ StringUtils.center(
						RessourceReporting.getString("PROCESS_DURATION"), 29,
						"") + ": " + getDuration() + " milisecondes.");
		bw.write("|");
		bw.flush();

		// Iterator it= reportTypeLines.values().iterator();
		Iterator<ReportTypeLine> it = reportTypeLines.iterator();
		while (it.hasNext()) {
			// while(reportTypeLinesStack.isEmpty()) {
			ReportTypeLine reportTypeLine = (ReportTypeLine) it.next();
			// ReportTypeLine reportTypeLine= (ReportTypeLine)
			// reportTypeLinesStack.pop();

			if (reportTypeLine.isUsed()) {
				bw.newLine();
				bw.write(reportTypeLine.writeBegin());

				FileReader fileReader = new FileReader(
						reportTypeLine.getReportLineTmpFile());
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String buffer = null;
				while ((buffer = bufferedReader.readLine()) != null) {
					bw.newLine();
					bw.write(buffer);
				}
				bufferedReader.close();
				bufferedReader = null;
				fileReader.close();
				fileReader = null;

				bw.write(reportTypeLine.writeEnd());

			}
			reportTypeLine.close();
		}

		bw.write(internalBuffer.toString());
		bw.newLine();
		bw.writeln("+=============================================================================+");
		bw.flush();
	}

	private boolean used = false;

	/**
	 * @see org.jobjects.dbimp.report.Reporting#isUsed()
	 */
	public boolean isUsed() {
		return used;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
