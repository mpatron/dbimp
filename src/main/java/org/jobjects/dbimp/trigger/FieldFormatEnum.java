package org.jobjects.dbimp.trigger;

/**
 * Utilisé dans la lecture du fichier de paramétrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public enum FieldFormatEnum {
  INTEGER("integer", 0), LONG("long", 1), FLOAT("float", 2), DOUBLE("double", 3), DATETIME("datetime", 4), STRING("string", 5), BLOB("file",
      6);

  private FieldFormatEnum(String typeString, int typeInt) {
    this.typeString = typeString;
    this.typeInt = typeInt;
  }

  private int typeInt;
  private String typeString;

  /**
   * @return the typeInt
   */
  public int getTypeInt() {
    return typeInt;
  }

  /**
   * @return the typeString
   */
  public String getTypeString() {
    return typeString;
  }

  static public FieldFormatEnum valueOfByType(String type) {
    FieldFormatEnum returnValue = null;
    if ("file".equals(type)) {
      returnValue = FieldFormatEnum.BLOB;
    }
    if (null != type) {
      returnValue = FieldFormatEnum.valueOf(type.toUpperCase());
    }
    return returnValue;
  }

}
