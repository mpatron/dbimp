package org.jobjects.dbimp.trigger;

/**
 * Constante : Champ de type position : POSITION Constante : Champ de type
 * constante : CONSTANTE Constante : Champ de type query : QUERY
 * 
 * @author Mickael
 *
 */
public enum FieldTypeEnum {
  POSITION(0), /**/
  CONSTANTE(1), QUERY(3);

  private FieldTypeEnum(int value) {
    this.value = value;
  }

  private int value;

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(int value) {
    this.value = value;
  }

}
