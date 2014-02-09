package org.jobjects.dbimp.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.SystemUtils;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.xml.XmlField;


/**
 * <p>Title: IHM</p>
 * <p>Description: Exportation dbExp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * <p>Date :  4 sept. 2003</p>
 * @author Mickael Patron
 * @version 1.0
 */
public class ReportLine implements Reporting {

  private BufferedWriter bufferedWriter = null;

  private Logger log = Logger.getLogger(getClass().getName());

  private int            numberLine     = 0;

  private ReportTypeLine reportTypeLine = null;

  private HashMap<String,ReportField>        reportFields   = new HashMap<String,ReportField>();

  private StringBuffer   internalBuffer = new StringBuffer();

  public ReportLine(BufferedWriter bufferedWriter, ReportTypeLine reportTypeLine) {
    this.bufferedWriter = bufferedWriter;
    this.reportTypeLine = reportTypeLine;
  }

  public ReportTypeLine getReportTypeLine() {
    return reportTypeLine;
  }

  /**
   * @return Le numéro de ligne
   */
  public int getNumberLine() {
    return numberLine;
  }

  /**
   * @return Le numéro de ligne
   */
  public void setNumberLine(int numberLine) {
    this.numberLine = numberLine;
  }

  /**
   * Method showParameter : ligne {0} : Info : Detail de la ligne
   */
  public void showParameter() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("PARAMETER_LINE", new Object[] { new Integer(numberLine) }));
    for (Iterator<Field> it = reportTypeLine.getLine().getFields().iterator(); it.hasNext();) {
      XmlField field = (XmlField) it.next();
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      try {
        switch (field.getDiscriminator()) {
        case XmlField.POSITION:
          internalBuffer.append(RessourceReporting.getString("PARAMETER_LINE_POSITION", new Object[] { field.getName(),
              new Integer(field.getPosition().getStartposition()),
              new Integer(field.getPosition().getSize()),
              field.getBuffer() }));
          break;

        case XmlField.CONSTANTE:
          internalBuffer.append(RessourceReporting.getString("PARAMETER_LINE_CONSTANTE",
              new Object[] { field.getName(), field.getConstante().getValue() }));
          break;

        case XmlField.QUERY:
          internalBuffer.append(RessourceReporting.getString("PARAMETER_LINE_CONSTANTE",
              new Object[] { field.getName(), field.getQuery().getSql() }));
          break;
        }
      } catch (Exception ex) {
        internalBuffer.append(RessourceReporting.getString("PARAMETER_LINE_UNKNOW_ERROR",
            new Object[] { field.getName() }));
        log.log(Level.SEVERE,"", ex);
      }
    }
    used = true;
  }

  // ---------------------------------------------------------------------------

  public void showLine() {
    internalBuffer.append("Line(" + numberLine
        + ") "
        + reportTypeLine.getLine().getName()
        + ":"
        + SystemUtils.LINE_SEPARATOR);

    for (Iterator<Field> it = reportTypeLine.getLine().getFields().iterator(); it.hasNext();) {
      XmlField field = (XmlField) it.next();

      try {
        switch (field.getDiscriminator()) {
        case XmlField.POSITION:
          internalBuffer.append("  " + field.getName()
              + "("
              + field.getPosition().getStartposition()
              + ", "
              + field.getPosition().getSize()
              + ") = \"");
          internalBuffer.append(field.getBuffer());
          internalBuffer.append("\"" + SystemUtils.LINE_SEPARATOR);

          break;

        case XmlField.CONSTANTE:
          internalBuffer.append("  " + field.getName() + "(cste) = \"");
          internalBuffer.append(field.getConstante().getValue());
          internalBuffer.append("\"" + SystemUtils.LINE_SEPARATOR);

          break;

        case XmlField.QUERY:
          internalBuffer.append("  " + field.getName() + "(query) = \"");
          internalBuffer.append(field.getQuery().getSql());
          internalBuffer.append("\"" + SystemUtils.LINE_SEPARATOR);

          break;
        }
      } catch (Exception exinternal) {
        internalBuffer.append("  " + field.getName() + " (unkown error)" + SystemUtils.LINE_SEPARATOR);
      }
    }
    used = true;
  }

  // ---------------------------------------------------------------------------

  public ReportField getReportField(Field field) {
    ReportField returnValue = (ReportField) reportFields.get(field.getName());
    if (returnValue == null) {
      returnValue = new ReportField(bufferedWriter, field, this);
      reportFields.put(field.getName(), returnValue);
    }
    returnValue.setField(field);
    return returnValue;
  }

  /**
   * Method INFO_LINE_DESTROY : ligne {0} : Info : Ligne detruite
   */
  public void INFO_LINE_DESTROY() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("INFO_LINE_DESTROY", new Object[] { new Integer(numberLine) }));
    used = true;
  }

  /**
   * Method ERROR_MESSAGE : ligne {0} : Erreur : {1}
   * 
   * @param message
   */
  public void ERROR_MESSAGE(String lineName, String message) {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("ERROR_MESSAGE", new Object[] { new Integer(numberLine),
        lineName,
        message }));
    used = true;
  }

  /**
   * @see org.jobjects.dbimp.report.Reporting#write()
   */
  public void write() throws IOException {
    bufferedWriter.newLine();
    bufferedWriter.write("|                                                                             |");
    bufferedWriter.newLine();
    bufferedWriter.write("|    " + RessourceReporting.getString("PROCESS_TITLE_LINE") + " :" + numberLine);

    Iterator<ReportField> it = reportFields.values().iterator();
    boolean flag = true;
    while (it.hasNext()) {
      ReportField reportField = (ReportField) it.next();
      if (reportField.isUsed() && flag) {
        bufferedWriter.newLine();
        bufferedWriter.write("|      " + RessourceReporting.getString("PROCESS_TITLE_FIELDS") + " :");
      }
      if (reportField.isUsed()) reportField.write();
      flag = false;
    }
    bufferedWriter.write(internalBuffer.toString());
    bufferedWriter.flush();
  }

  private boolean used = false;

  /**
   * @see org.jobjects.dbimp.report.Reporting#isUsed()
   */
  public boolean isUsed() {
    Iterator<ReportField> it = reportFields.values().iterator();
    while (it.hasNext()) {
      ReportField reportField = (ReportField) it.next();
      used |= reportField.isUsed();
    }
    return used;
  }

  public void clear() {
    used = false;
    internalBuffer.delete(0, internalBuffer.length());
    Iterator<ReportField> it = reportFields.values().iterator();
    while (it.hasNext()) {
      ReportField reportField = (ReportField) it.next();
      reportField.clear();
    }
  }
}
