package org.jobjects.dbimp.xml;

import javax.validation.constraints.NotNull;

import org.jobjects.dbimp.trigger.FiletypeEnum;
import org.jobjects.dbimp.trigger.Key;

/**
 * Tag key. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlKey extends XmlPosition implements Key {

  @NotNull
  private String keyValue = null;

  private boolean isBlank = false;

  public XmlKey(FiletypeEnum filetype) {
    super(filetype);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Key#getIsBlank()
   */
  public boolean isBlank() {
    return isBlank;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Key#setIsBlank(java.lang.Boolean)
   */
  public void setBlank(boolean isBlank) {
    this.isBlank = isBlank;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Key#getValue()
   */
  public String getKeyValue() {
    return keyValue;
  }

  /**
   * @param keyValue
   *          The keyValue to set.
   */
  public void setKeyValue(String value) {
    this.keyValue = value;
  }

  public String toString() {
    String returnValue = "";
    if (keyValue != null && !keyValue.trim().equals("")) {
      returnValue = "    <key value=\"" + keyValue + "\" startposition=\"" + getStartposition() + "\" size=\"" + getSize() + "\"/>";
    }
    if (isBlank) {
      returnValue = "    <key isBlank=\"" + isBlank + "\" startposition=\"" + getStartposition() + "\" size=\"" + getSize() + "\"/>";
    }
    return returnValue;
  }
}