package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldTypeEnum;


/**
 * Tag query-param. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public class XmlQueryParam {
  public final static int POSITION = 0;
  public final static int CONSTANTE = 1;

  private FieldTypeEnum type = FieldTypeEnum.STRING;
  private String dateformat = null;

  private XmlPosition position = null;
  private XmlConstante constante = null;
  private int discriminator = POSITION;

  public int discriminator() {
    return discriminator;
  }

  // ---------------------------------------------------------------------------

  /**
   * @return Returns the dateformat.
   */
  public String getDateformat() {
    return dateformat;
  }

  /**
   * @param dateformat
   *          The dateformat to set.
   */
  public void setDateformat(String dateformat) {
    this.dateformat = dateformat;
  }

  /**
   * @return Returns the type.
   */
  public FieldTypeEnum getType() {
    return type;
  }

  /**
   * @param type
   *          The type to set.
   */
  public void setType(FieldTypeEnum type) {
    this.type = type;
  }

  public XmlPosition getPosition() throws Exception {
    if (discriminator != POSITION) {
      throw new Exception();
    }
    return position;
  }

  // ---------------------------------------------------------------------------

  public void setPosition(XmlPosition position) {
    discriminator = POSITION;
    this.position = position;
  }

  // ---------------------------------------------------------------------------

  public XmlConstante getConstante() throws Exception {
    if (discriminator != CONSTANTE) {
      throw new Exception();
    }
    return constante;
  }

  // ---------------------------------------------------------------------------

  public void setConstante(XmlConstante constante) {
    discriminator = CONSTANTE;
    this.constante = constante;
  }

  // ---------------------------------------------------------------------------

  public String toString() {
    String returnValue = "        <query-param>";

    switch (type) {
    case DATETIME:
      returnValue += "<" + type.getTypeString() + " dateformat=\"" + dateformat + "\"/>";
      break;
    case DOUBLE:
    case FLOAT:
    case INTEGER:
    case LONG:
    case STRING:
    default:
      returnValue += "<" + type.getTypeString() + "/>";
      break;
    }

    switch (discriminator()) {
    case POSITION:
      returnValue += position.toString();
      break;
    case CONSTANTE:
      returnValue += constante.toString();
      break;
    default:
      returnValue += "<!-- Erreur de type -->";
      break;
    }

    returnValue += "        </query-param>";
    return returnValue;
  }
  // ---------------------------------------------------------------------------
}