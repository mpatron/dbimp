package org.jobjects.dbimp.xml;

/**
 * Tag constante.
 * Utilis� dans la lecture du fichier de param�trage.
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlConstante {
  /** Valeur de la constante du param�trage
   * @see XmlField
   */
  private String value = null;

  /**
   * @return Returns the value.
   */
  public String getValue() {
    return value;
  }
  
  /**
   * @param value The value to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    String returnValue= "";
    if (value != null || !value.trim().equals("")) {
      returnValue= "<constante value=\"" + value + "\"/>";
    }
    return returnValue;
  }
  
}