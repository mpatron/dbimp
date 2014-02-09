package org.jobjects.dbexp;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Title: IHM</p>
 * <p>Description: Exportation dbExp</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: JObjects</p>
 * 
 * @author Mickael Patron
 * @version 1.0
 */

public class SqlTypesEnum implements Serializable, Comparable<SqlTypesEnum> {
  private static final long serialVersionUID = -1;

  private final static String BOOLEAN = "java.lang.Boolean";

  private final static String BYTE = "java.lang.Byte";

  private final static String SHORT = "java.lang.Short";

  private final static String INTEGER = "java.lang.Integer";

  private final static String LONG = "java.lang.Long";

  private final static String FLOAT = "java.lang.Float";

  private final static String DOUBLE = "java.lang.Double";

  private final static String STRING = "java.lang.String";

  private final static String DATE = "java.sql.Timestamp";

  private final static String PREPAREDSTATEMENT_BOOLEAN = "Boolean";

  private final static String PREPAREDSTATEMENT_BYTE = "Byte";

  private final static String PREPAREDSTATEMENT_SHORT = "Short";

  private final static String PREPAREDSTATEMENT_INTEGER = "Int";

  private final static String PREPAREDSTATEMENT_LONG = "Long";

  private final static String PREPAREDSTATEMENT_FLOAT = "Float";

  private final static String PREPAREDSTATEMENT_DOUBLE = "Double";

  private final static String PREPAREDSTATEMENT_STRING = "String";

  private final static String PREPAREDSTATEMENT_DATE = "Timestamp";

  // ---------------------------------------------------------------------------

  private final static String _string = "string";

  private final static String _integer = "integer";

  private final static String _long = "long";

  private final static String _float = "float";

  private final static String _double = "double";

  private final static String _datetime = "datetime";

  private String name = null;

  private int dataType = 0;

  private boolean nullable = false;

  private String javaType = null;

  private String preparedStatement = null;

  private int position = 0;

  private int size = 0;

  private int decimalDigits = 0;

  private String typeName = null;

  private String xmlType = null;

  // ---------------------------------------------------------------------------

  public SqlTypesEnum(String COLUMN_NAME, short DATA_TYPE, int COLUMN_SIZE,
      int DECIMAL_DIGITS, int NULLABLE, int ORDINAL_POSITION, String TYPE_NAME) {

    name = COLUMN_NAME;
    dataType = DATA_TYPE;
    nullable = (NULLABLE == 1);
    position = ORDINAL_POSITION;
    size = COLUMN_SIZE;
    decimalDigits = DECIMAL_DIGITS;
    typeName = TYPE_NAME;

    if (getTypeName().equals("NUMBER")) {
      if (getSize() == 1) {
        javaType = SqlTypesEnum.BOOLEAN;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_BOOLEAN;
        xmlType = SqlTypesEnum._integer;
      }
      if ((1 < getSize()) && (getSize() <= 3) && (getDecimalDigits() == 0)) {
        javaType = SqlTypesEnum.BYTE;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_BYTE;
        xmlType = SqlTypesEnum._integer;
      }
      if ((3 < getSize()) && (getSize() <= 5) && (getDecimalDigits() == 0)) {
        javaType = SqlTypesEnum.SHORT;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_SHORT;
        xmlType = SqlTypesEnum._integer;
      }
      if ((5 < getSize()) && (getSize() <= 10) && (getDecimalDigits() == 0)) {
        javaType = SqlTypesEnum.INTEGER;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_INTEGER;
        xmlType = SqlTypesEnum._integer;
      }
      if ((10 < getSize()) && (getSize() <= 19) && (getDecimalDigits() == 0)) {
        javaType = SqlTypesEnum.LONG;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_LONG;
        xmlType = SqlTypesEnum._long;
      }
      if ((19 < getSize()) && (getSize() <= 38) && (getDecimalDigits() == 0)) {
        // ?? Es ce vraiment le bon type ??
        javaType = SqlTypesEnum.LONG;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_LONG;
        xmlType = SqlTypesEnum._long;
      }
      if ((1 < getSize()) && (getSize() <= 38) && (0 < getDecimalDigits())
          && (getDecimalDigits() <= 7)) {
        javaType = SqlTypesEnum.FLOAT;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_FLOAT;
        xmlType = SqlTypesEnum._float;
      }
      if ((1 < getSize()) && (getSize() <= 38) && (7 < getDecimalDigits())
          && (getDecimalDigits() <= 15)) {
        javaType = SqlTypesEnum.DOUBLE;
        preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_DOUBLE;
        xmlType = SqlTypesEnum._double;
      }
    }
    if (getTypeName().equals("INTEGER")) {
      javaType = SqlTypesEnum.INTEGER;
      preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_INTEGER;
      xmlType = SqlTypesEnum._integer;
    }
    if (getTypeName().equals("FLOAT")) {
      javaType = SqlTypesEnum.FLOAT;
      preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_FLOAT;
      xmlType = SqlTypesEnum._float;
    }
    if (getTypeName().equals("DATE")) {
      javaType = SqlTypesEnum.DATE;
      preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_DATE;
      xmlType = SqlTypesEnum._datetime;
    }
    if (getTypeName().equals("VARCHAR") || getTypeName().equals("VARCHAR2")) {
      javaType = SqlTypesEnum.STRING;
      preparedStatement = SqlTypesEnum.PREPAREDSTATEMENT_STRING;
      xmlType = SqlTypesEnum._string;
    }
  }

  // ---------------------------------------------------------------------------

  /**
   * Returns the columnName.
   * 
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the dataType.
   * 
   * @return int
   */
  public int getDataType() {
    return dataType;
  }

  /**
   * Returns the javaType.
   * 
   * @return String
   */
  public String getJavaType() {
    String returnValue = javaType;
    if (javaType != null) {
      if (javaType.startsWith("java.lang.")) {
        returnValue = javaType.substring("java.lang.".length());
      }
    }
    return returnValue;
  }

  /**
   * Returns the nullable.
   * 
   * @return boolean
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * Returns the position.
   * 
   * @return int
   */
  public int getPosition() {
    return position;
  }

  /**
   * Returns the preparedStatement.
   * 
   * @return String
   */
  public String getPreparedStatement() {
    return preparedStatement;
  }

  public String getPreparedStatementFct() {
    return this.preparedStatement;
  }

  // ---------------------------------------------------------------------------

  /**
   * Sets the columnName.
   * 
   * @param columnName
   *          The columnName to set
   */
  public void setName(String columnName) {
    this.name = columnName;
  }

  /**
   * Sets the dataType.
   * 
   * @param dataType
   *          The dataType to set
   */
  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  /**
   * Sets the javaType.
   * 
   * @param javaType
   *          The javaType to set
   */
  public void setJavaType(String javaType) {
    this.javaType = javaType;
  }

  /**
   * Sets the nullable.
   * 
   * @param nullable
   *          The nullable to set
   */
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  /**
   * Sets the position.
   * 
   * @param position
   *          The position to set
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Returns the size.
   * 
   * @return int
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the size.
   * 
   * @param size
   *          The size to set
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Returns the decimalDigits.
   * 
   * @return int
   */
  public int getDecimalDigits() {
    return decimalDigits;
  }

  /**
   * Sets the decimalDigits.
   * 
   * @param decimalDigits
   *          The decimalDigits to set
   */
  public void setDecimalDigits(int decimalDigits) {
    this.decimalDigits = decimalDigits;
  }

  /**
   * Returns the sqlType.
   * 
   * @return String
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Sets the sqlType.
   * 
   * @param sqlType
   *          The sqlType to set
   */
  public void setTypeName(String sqlType) {
    this.typeName = sqlType;
  }

  /**
   * Sets the preparedStatement.
   * 
   * @param preparedStatement
   *          The preparedStatement to set
   */
  public void setPreparedStatement(String preparedStatement) {
    this.preparedStatement = preparedStatement;
  }

  public String toString() {
    String distance = "\t";
    String returnValue = distance;
    returnValue += "<field";
    returnValue += " columnname=\"" + getName() + "\"";
    returnValue += " position=\"" + getPosition() + "\"";
    returnValue += " javatype=\"" + getJavaType() + "\"";
    returnValue += " size=\"" + getSize() + "\"";
    returnValue += " decimaldigits=\"" + getDecimalDigits() + "\"";
    returnValue += " sqltype=\"" + getTypeName() + "\"";
    returnValue += "/>";
    return returnValue;
  }

  public static Collection<SqlTypesEnum> getColumns(DatabaseMetaData metadata,
      String schemaName, String tableName) throws SQLException {
    LinkedList<SqlTypesEnum> returnValue = new LinkedList<SqlTypesEnum>();
    ResultSet rs = metadata.getColumns(null, schemaName, tableName, "%");
    while (rs.next()) {
      SqlTypesEnum f = new SqlTypesEnum(rs.getString("COLUMN_NAME"),
          (short) rs.getInt("DATA_TYPE"), rs.getInt("COLUMN_SIZE"),
          rs.getInt("DECIMAL_DIGITS"), rs.getInt("NULLABLE"),
          rs.getInt("ORDINAL_POSITION"), rs.getString("TYPE_NAME"));
      returnValue.add(f);
    }
    rs.close();
    return returnValue;
  }

  // ----------------------------------------------------------------------------------------------------

  /**
   * @return la chaine du type pour le paramètrage xml.
   */
  public String getXmlType() {
    return xmlType;
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof SqlTypesEnum)) {
      return false;
    }
    SqlTypesEnum rhs = (SqlTypesEnum) object;
    return new EqualsBuilder().append(this.name, rhs.name).isEquals();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return new HashCodeBuilder(1429000583, -239805079).append(this.name)
        .toHashCode();
  }

  /**
   * @see java.lang.Comparable#compareTo(Object)
   */
  public int compareTo(SqlTypesEnum object) {
    return new CompareToBuilder().append(this.name, object.name).toComparison();
  }
}