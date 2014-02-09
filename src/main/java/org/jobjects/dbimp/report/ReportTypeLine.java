package org.jobjects.dbimp.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
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
public class ReportTypeLine implements Reporting {

	private Report reporting = null;
	private Line line = null;
	private int nbSelected = 0;
	private int nbInsert = 0;
	private int nbUpdate = 0;
	private int nbDelete = 0;
	private int nbReject = 0;
	private ReportLine reportLine = null;
	private File reportLineTmpFile = null;
	private FileWriter reportLineTmpFileWriter = null;
	private BufferedWriter bufferedWriter = null;
	private ReportTrigger reportTrigger = null;

	public ReportTypeLine(Report reporting, Line line) throws IOException {
		this.line = line;
		this.reporting = reporting;

		reportLineTmpFile = File.createTempFile(
				"bimptypetine_" + line.getName(), ".txt");
		reportLineTmpFile.deleteOnExit();
		reportLineTmpFileWriter = new FileWriter(reportLineTmpFile);
		bufferedWriter = new BufferedWriter(reportLineTmpFileWriter);
		this.reportLine = new ReportLine(bufferedWriter, this);
		if (line.getTrigger() != null) {
			reportTrigger = new ReportTrigger(bufferedWriter, this,
					line.getTrigger());
		}
	}

	public Report getReporting() {
		return reporting;
	}

	/**
	 * @return int
	 */
	public int getNbSelected() {
		return nbSelected;
	}

	/**
	 * @return int
	 */
	public int getNbDelete() {
		return nbDelete;
	}

	/**
	 * @return int
	 */
	public int getNbInsert() {
		return nbInsert;
	}

	/**
	 * @return int
	 */
	public int getNbReject() {
		return nbReject;
	}

	/**
	 * @return int
	 */
	public int getNbUpdate() {
		return nbUpdate;
	}

	/**
	 * @param nb
	 *            Quantité à inserer
	 */
	public void addToNbSelected(int nb) {
		nbSelected += nb;
	}

	/**
	 * @param nb
	 *            Quantité à inserer
	 */
	public void addToNbDelete(int nb) {
		nbDelete += nb;
	}

	/**
	 * @param nb
	 *            Quantité à inserer
	 */
	public void addToNbInsert(int nb) {
		nbInsert += nb;
	}

	/**
	 * @param nb
	 *            Quantité à inserer
	 */
	public void addToNbReject(int nb) {
		nbReject += nb;
	}

	/**
	 * @param nb
	 *            Quantité à inserer
	 */
	public void addToNbUpdate(int nb) {
		nbUpdate += nb;
	}

	/**
	 * @return le nom du type de ligne
	 */
	public String getName() {
		return line.getName();
	}

	public Line getLine() {
		return line;
	}

	/**
	 * @return ReportLine
	 */
	public ReportLine getReportLine() {
		return reportLine;
	}

	/**
	 * @return ReportLine
	 */
	public ReportTrigger getReportTrigger() {
		return reportTrigger;
	}

	public String writeBegin() {
		StringBuffer sb = new StringBuffer();
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("+=============================================================================+");
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|");
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|  " + RessourceReporting.getString("PROCESS_TYPE_LINE")
				+ " : " + getName());
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|  ---------------------------------------------------------------------------+");
		return sb.toString();
	}

	/**
	 * @see Reporting#write()
	 */
	public void write() throws IOException {

		if ((reportTrigger != null) && reportTrigger.isUsed()) {
			bufferedWriter.newLine();
			bufferedWriter
					.write("|                                                                             |");
			bufferedWriter.newLine();
			if (getReportLine().getNumberLine() == 0) {
				bufferedWriter.write("|    "
						+ RessourceReporting
								.getString("ERROR_TRIGGER_START_FILE") + " :");
			} else {
				bufferedWriter.write("|    "
						+ RessourceReporting.getString("ERROR_TRIGGER_ON_LINE")
						+ " : " + getReportLine().getNumberLine() + " :");
			}
			reportTrigger.write();
			bufferedWriter.flush();
		}

		if (reportLine.isUsed()) {
			reportLine.write();
		}

		bufferedWriter.flush();
	}

	public String writeEnd() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|  ---------------------------------------------------------------------------+");
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|");
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|    "
				+ RessourceReporting.getString("INFO_NUMBER_OF_SELECT")
				+ "     : " + nbSelected);
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|    "
				+ RessourceReporting.getString("INFO_NUMBER_OF_INSERT")
				+ "      : " + nbInsert);
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|    "
				+ RessourceReporting.getString("INFO_NUMBER_OF_UPDATE")
				+ "      : " + nbUpdate);
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|    "
				+ RessourceReporting.getString("INFO_NUMBER_OF_DELETE")
				+ "     : " + nbDelete);
		sb.append(SystemUtils.LINE_SEPARATOR);
		// sb.append("|    Nombre de reject     : " + nbReject);
		// sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("|");
		return sb.toString();
	}

	private boolean used = false;

	/**
	 * @see Reporting#isUsed()
	 */
	public boolean isUsed() {
		if (reportTrigger != null) {
			used |= reportTrigger.isUsed();
		}
		used |= reportLine.isUsed();
		return used;
	}

	public void nextLine(int numberLine) throws IOException {
		reportLine.setNumberLine(numberLine);

		write();
		isUsed();

		if (reportTrigger != null) {
			reportTrigger.clear();
		}

		reportLine.clear();
	}

	public void close() throws IOException {
		bufferedWriter.flush();
		bufferedWriter.close();
		bufferedWriter = null;
		reportLineTmpFileWriter.close();
		reportLineTmpFileWriter = null;
		reportLineTmpFile = null;
	}

	/**
	 * @return ReportLineTmpFile, soit le fichier temporaire du rapport de
	 *         ligne.
	 */
	public File getReportLineTmpFile() {
		return reportLineTmpFile;
	}

}
