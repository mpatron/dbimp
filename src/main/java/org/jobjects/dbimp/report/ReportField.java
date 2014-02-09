package org.jobjects.dbimp.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.SystemUtils;
import org.jobjects.dbimp.trigger.Field;

/**
 * <p>Title: IHM</p>
 * <p>Description: Importation dbImp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * <p>Date :  4 sept. 2003</p>
 * @author Mickael Patron
 * @version 1.0
 */
public class ReportField implements Reporting {
  private BufferedWriter bw= null;
  private Field field= null;
  private ReportLine reportLine= null;
  private HashSet<String> listMessages= new HashSet<String>();
  private StringBuffer internalBuffer= new StringBuffer();

  public ReportField(BufferedWriter bw, Field field, ReportLine reportLine) {
    this.bw= bw;
    this.field= field;
    this.reportLine= reportLine;
  }

  /**
   * @return Le nom du champ
   */
  public String getName() {
    return field.getName();
  }

  /**
   * @param field
   */
  public void setField(Field field) {
    this.field= field;
  }

  /**
   * @return Le propriétaire de l'instance
   */
  public ReportLine getReportLine() {
    return reportLine;
  }

  /**
   * Method INFO_FIELD_UPDATED_IN_DB=|        Le champ a pour valeur "{0}" dans le fichier et a pour valeur "{1}" dans la base.
   * @param infile
   * @param indb
   */
  public void INFO_FIELD_UPDATED_IN_DB(String infile, String indb) {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("INFO_FIELD_UPDATED_IN_DB", new Object[] { infile, indb }));
    used= true;
  }

  /**
   * Method ERROR_FIELD_NULL=|        Le champ est null alors qu'il est not nullable.
   */
  public void ERROR_FIELD_NULL() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("ERROR_FIELD_NULL", new Object[] {
    }));
    used= true;
  }

  /**
   * Method |        Le champ a pour valeur {0} ne correspond pas à son type {1}
   */
  public void ERROR_FIELD_TYPE() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(
      RessourceReporting.getString(
        "ERROR_FIELD_NUMERIC",
        new Object[] { field.getBuffer(), field.getType().getTypeString() }  ));
    used= true;
  }

  /**
   * Method ERROR_FIELD_NUMERIC=|        Le champ a pour valeur {0} n'est pas un numérique du type {1}.
   */
  public void ERROR_FIELD_NUMERIC() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(
      RessourceReporting.getString(
        "ERROR_FIELD_NUMERIC",
        new Object[] { field.getBuffer(), field.getType().getTypeString() } ));
    used= true;
  }

  /**
   * Method ERROR_FIELD_VALUE_IN_LIST=|        Le champ a pour valeur {0} et elle n appartient pas à sa liste de valeur.
   */
  public void ERROR_FIELD_VALUE_IN_LIST() {
    internalBuffer.append(SystemUtils.LINE_SEPARATOR);
    internalBuffer.append(RessourceReporting.getString("ERROR_FIELD_VALUE_IN_LIST", new Object[] { field.getBuffer()}));
    used= true;
  }

  /**
   * Method ERROR_FIELD_MANDATORY=|     !! Le champ est obligatoire alors qu'il y a une valeur null ou vide. Ligne rejectée.
   * @return String
   */
  public String ERROR_FIELD_MANDATORY() {
    String message= RessourceReporting.getString("ERROR_FIELD_MANDATORY", new Object[] {
    });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_MANDATORY=|     !! Le champ est obligatoire alors qu'il y a une valeur null ou vide. Ligne rejectée.
   * @return String
   */
  public String ERROR_FIELD_MANDATORY(String messageToAdd) {
    String message= RessourceReporting.getString("ERROR_FIELD_MANDATORY", new Object[] {
    });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(messageToAdd);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_IN_FILE=|        Le champ est inaxessible. La ligne à une longueur trop court. 
   */
  public String ERROR_FIELD_NOT_IN_FILE() {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_IN_FILE", new Object[] {
    });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_A_INTEGER=|        La valeur {0} du champ n est pas un entier.
   * @param value
   */
  public String ERROR_FIELD_NOT_A_INTEGER(String value) {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_A_INTEGER", new Object[] { value });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_A_LONG=|        La valeur {0} du champ n est pas un long.
   * @param value
   */
  public String ERROR_FIELD_NOT_A_LONG(String value) {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_A_LONG", new Object[] { value });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_A_FLOAT=|        La valeur {0} du champ n est pas un reel.
   * @param value
   */
  public String ERROR_FIELD_NOT_A_FLOAT(String value) {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_A_FLOAT", new Object[] { value });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_A_DOUBLE=|        La valeur {0} du champ n est pas un reel long.
   * @param value
   */
  public String ERROR_FIELD_NOT_A_DOUBLE(String value) {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_A_DOUBLE", new Object[] { value });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  /**
   * Method ERROR_FIELD_NOT_A_DATETIME=|        La valeur {0} du champ n est pas une date heure de format {1}.
   * @param value
   * @param dateformat
   * @return String
   */
  public String ERROR_FIELD_NOT_A_DATETIME(String value, String dateformat) {
    String message= RessourceReporting.getString("ERROR_FIELD_NOT_A_DATETIME", new Object[] { value, dateformat });
    if (!listMessages.contains(reportLine.getNumberLine() + "#" + message)) {
      listMessages.add(reportLine.getNumberLine() + "#" + message);
      internalBuffer.append(SystemUtils.LINE_SEPARATOR);
      internalBuffer.append(message);
      used= true;
    }
    return message;
  }

  public void write() throws IOException {
    bw.newLine();
    String message= RessourceReporting.getString("INFO_FIELD", new Object[] { getName() });
    bw.write(message);
    bw.write(internalBuffer.toString());
    bw.flush();
  }

  private boolean used= false;

  public boolean isUsed() {
    return used;
  }

  public void clear() {
    used= false;
    internalBuffer.delete(0, internalBuffer.length());
  }
}
