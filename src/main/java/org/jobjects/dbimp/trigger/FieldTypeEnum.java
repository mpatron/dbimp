package org.jobjects.dbimp.trigger;

/**
 * Utilisé dans la lecture du fichier de paramètrage.
 * @author Mickael Patron
 * @version 2.0
 */
public enum FieldTypeEnum {  
  INTEGER("integer",0),
  LONG("long",1),
  FLOAT("float",2),
  DOUBLE("double",3 ),
  DATETIME("datetime",4 ),
  STRING("string",5 ),
  BLOB("file",6 );

  private FieldTypeEnum(String typeString, int typeInt) {
    this.typeString=typeString;
    this.typeInt=typeInt;
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
  
  static public FieldTypeEnum valueOfByType(String type) {
    FieldTypeEnum returnValue=null;    
    if("file".equals(type)) {
      returnValue=FieldTypeEnum.BLOB;
    } if(null!=type) {
      returnValue=FieldTypeEnum.valueOf(type.toUpperCase());
    }
    return returnValue;
  }
  
}
