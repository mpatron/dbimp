package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.trigger.FieldTypeEnum;
import org.jobjects.dbimp.trigger.Position;

/**
 * Tag query-param. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public class XmlQueryParam {
  public final static int POSITION = 0;
  public final static int CONSTANTE = 1;

  private FieldFormatEnum type = FieldFormatEnum.STRING;
  private String dateformat = null;

  private Position position = null;
  private XmlConstante constante = null;
  private FieldTypeEnum discriminator = FieldTypeEnum.POSITION;

  public FieldTypeEnum getDiscriminator() {
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
  public FieldFormatEnum getType() {
    return type;
  }

  /**
   * @param type
   *          The type to set.
   */
  public void setType(FieldFormatEnum type) {
    this.type = type;
  }

  public Position getPosition() throws Exception {
    if (!FieldTypeEnum.POSITION.equals(discriminator)) {
      throw new Exception();
    }
    return position;
  }

  // ---------------------------------------------------------------------------

  public void setPosition(Position position) {
    discriminator = FieldTypeEnum.POSITION;
    this.position = position;
  }

  // ---------------------------------------------------------------------------

  public XmlConstante getConstante() throws Exception {
    if (!FieldTypeEnum.CONSTANTE.equals(discriminator)) {
      throw new Exception();
    }
    return constante;
  }

  // ---------------------------------------------------------------------------

  public void setConstante(XmlConstante constante) {
    discriminator = FieldTypeEnum.CONSTANTE;
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

    switch (getDiscriminator()) {
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