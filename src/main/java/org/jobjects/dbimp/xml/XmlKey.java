package org.jobjects.dbimp.xml;

import javax.validation.constraints.NotNull;

import org.jobjects.dbimp.trigger.Key;

/**
 * Tag key. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlKey implements Key {
  @NotNull
  private String value = null;

  private int startposition = 0;

  private int size = 0;

  private boolean isBlank = false;

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
   * @see org.jobjects.dbimp.xml.Key#getSize()
   */
  public int getSize() {
    return size;
  }

  /**
   * @param size
   *          The size to set.
   */
  public void setSize(int size) {
    this.size = size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Key#getStartposition()
   */
  public int getStartposition() {
    return startposition;
  }

  /**
   * @param startposition
   *          The startposition to set.
   */
  public void setStartposition(int startposition) {
    this.startposition = startposition;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Key#getValue()
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *          The value to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    String returnValue = "";
    if (value != null && !value.trim().equals("")) {
      returnValue = "    <key value=\"" + value + "\" startposition=\"" + startposition + "\" size=\"" + size + "\"/>";
    }
    if (isBlank) {
      returnValue = "    <key isBlank=\"" + isBlank + "\" startposition=\"" + startposition + "\" size=\"" + size + "\"/>";
    }
    return returnValue;
  }
}