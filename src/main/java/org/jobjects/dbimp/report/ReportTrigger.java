/*
 * Cr�� le 5 sept. 2003
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.jobjects.dbimp.report;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.jobjects.dbimp.trigger.Trigger;


/**
 * @author MP
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et
 *         commentaires
 */
public class ReportTrigger implements Reporting {

	private BufferedWriter bw = null;
	private ReportTypeLine reportTypeLine = null;
	private StringBuffer internalBuffer = new StringBuffer();
	private Trigger trigger = null;

	public ReportTrigger(BufferedWriter bw, ReportTypeLine reportTypeLine,
			Trigger trigger) {
		this.bw = bw;
		this.reportTypeLine = reportTypeLine;
		this.trigger = trigger;
	}

	public BufferedWriter getBufferedWriter() {
		return bw;
	}

	public ReportTypeLine getReportTypeLine() {
		return reportTypeLine;
	}

	/**
	 * Method ERROR_TRIGGER : | Message : {0}
	 * 
	 * @param message
	 */
	public void ERROR_TRIGGER(String message) {
		internalBuffer.append(SystemUtils.LINE_SEPARATOR);
		internalBuffer.append(RessourceReporting.getString("ERROR_TRIGGER",
				new Object[] { message }));
		used = true;
	}

	/**
	 * @see org.jobjects.dbimp.report.Reporting#write()
	 */
	public void write() throws IOException {
		bw.newLine();
		bw.write("|      Trigger class: " + trigger.getClass().getName());
		bw.write(internalBuffer.toString());
		bw.flush();
	}

	private boolean used = false;

	/**
	 * @see org.jobjects.dbimp.report.Reporting#isUsed()
	 */
	public boolean isUsed() {
		return used;
	}

	public void clear() {
		used = false;
		internalBuffer.delete(0, internalBuffer.length());
	}
}
