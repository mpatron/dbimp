package org.jobjects.dbimp.xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jobjects.dbimp.MathUtils;
import org.jobjects.dbimp.report.ReportField;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.trigger.FieldTypeEnum;

/**
 * Tag field. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlField implements Field, Comparable<XmlField> {

  public XmlField(String name, FieldFormatEnum type) {
    this.name = name;
    this.typeFormat = type;
  }

  public XmlField(String name, FieldFormatEnum type, boolean nullable, boolean nullableError, boolean isUse) {
    this.name = name;
    this.typeFormat = type;
    this.nullable = nullable;
    this.nullableError = nullableError;
    this.isUse = isUse;
  }

  // ---------------------------------------------------------------------------
  /**
   * Nom du champs.
   */
  @NotNull
  private String name = null;

  /**
   * Une erreur doit être signalé si la valeur est null. Impose que
   * nullable="false"
   */
  private boolean nullableError = true;

  /**
   * Type de la donnée.
   * 
   * @see FieldFormatEnum#toInt(java.lang.String)
   * @see FieldFormatEnum#toString(int)
   */
  private FieldFormatEnum typeFormat = FieldFormatEnum.STRING;

  private String dateFormat = null;

  /**
   * Coéficient de multiplication. Utilisé pour multiplié la valeur sielle est
   * de typeFormat integer|long|float|double.
   */
  private double coefficient = 1;

  /**
   * Si la valeur ne peut être null alors nullable est false.
   */
  private boolean nullable = true;

  /**
   * Valeur brut du text qui provient de la source de donnée
   * (position|constante|query)
   */
  private String buffer = null;

  /**
   * Liste des valeurs posibles du champs.
   */
  private Collection<String> checkIn = null;

  /**
   * requête permettant de recupérer la liste de valeur possible.
   */
  private String checkInSql = null;

  /**
   * Determine si le champ est Utilisé dans le requête. Valeur par default est
   * true. Cet indicateur permet d'utiliser des champs dans les triggers qui
   * n'appartiennent à la table.
   */
  private boolean isUse = true;

  private XmlPosition position = null;

  private XmlConstante constante = null;

  private XmlQuery query = null;

  private FieldTypeEnum discriminator = FieldTypeEnum.POSITION;

  private Logger LOGGER = Logger.getLogger(getClass().getName());

  /**
   * Method getDiscriminator. Distingue le typeFormat de la source de donnée.
   * 
   * @return int = [ POSITION | CONSTANTE | QUERY ]
   */
  public FieldTypeEnum getDiscriminator() {
    return discriminator;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method getPosition. Retourne la position du champ dans le fichier.
   * 
   * @return XmlPosition
   * @throws Exception
   */
  public XmlPosition getPosition() throws Exception {
    if (!FieldTypeEnum.POSITION.equals(discriminator)) {
      throw new Exception("is not a position.");
    }

    return position;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method setPosition. Affecte une position du champ dans le fichier.
   * 
   * @param position
   */
  public void setPosition(XmlPosition position) {
    discriminator = FieldTypeEnum.POSITION;
    this.position = position;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method getConstante. Retourne la constante du champ.
   * 
   * @return XmlConstante
   * @throws Exception
   */
  public XmlConstante getConstante() throws Exception {
    if (!FieldTypeEnum.CONSTANTE.equals(discriminator)) {
      throw new Exception("is not a constante.");
    }

    return constante;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method setConstante. Affecte une constante au champ.
   * 
   * @param constante
   */
  public void setConstante(XmlConstante constante) {
    discriminator = FieldTypeEnum.CONSTANTE;
    this.constante = constante;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method getQuery. Retourne la requête source de donnée du champ.
   * 
   * @return XmlQuery
   * @throws Exception
   */
  public XmlQuery getQuery() throws Exception {
    if (!FieldTypeEnum.QUERY.equals(discriminator)) {
      throw new Exception("is not a query.");
    }
    return query;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method setQuery. Affecte une requête source de donnée au champ.
   * 
   * @param query
   */
  public void setQuery(XmlQuery query) {
    discriminator = FieldTypeEnum.QUERY;
    this.query = query;
  }

  // ---------------------------------------------------------------------------

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    String returnValue = "    <field fieldname=\"" + name + "\">";

    switch (typeFormat) {
    case DATETIME:
      returnValue += "<" + typeFormat.getTypeString() + " dateformat=\"" + getDateFormat() + "\"/>";
      break;
    case DOUBLE:
    case FLOAT:
    case INTEGER:
    case LONG:
      if (coefficient != 1) {
        returnValue += "<" + typeFormat.getTypeString() + " coefficient=\"" + coefficient + "\"/>";
      } else {
        returnValue += "<" + typeFormat.getTypeString() + "/>";
      }
      break;
    default: // XmlFieldTypes._STRING
      returnValue += "<" + typeFormat.getTypeString() + "/>";
      break;
    }

    switch (getDiscriminator()) {
    case POSITION:
      if (position != null) {
        returnValue += position.toString();
      }
      break;
    case CONSTANTE:
      if (constante != null) {
        returnValue += constante.toString();
      }
      break;
    case QUERY:
      if (query != null) {
        returnValue += System.lineSeparator();
        returnValue += query.toString();
        returnValue += System.lineSeparator();
      }
      break;
    default:
      returnValue += "<!-- Erreur de typeFormat -->";
      break;
    }

    if (checkInSql != null && !checkInSql.trim().equals("")) {
      returnValue += System.lineSeparator();
      returnValue += "<check_in sql=\"" + checkInSql + "\"/>";
    }

    returnValue += "</field>";
    return returnValue;
  }

  // ---------------------------------------------------------------------------

  /**
   * Method isBufferValid. Validation de l'intégrité de la valeur.
   * 
   * @param reportField
   * @return boolean Retourne faux si une erreur a été trouvé. Les problèmes de
   *         format, de source de donnée, ... seront enregistrés dans
   *         'reporting'.
   */
  public boolean isBufferValid(ReportField reportField) {

    boolean returnValue = true;
    if (nullable) {
      if (StringUtils.isEmpty(buffer)) {
        return true;
      }
    }

    switch (typeFormat) {
    case INTEGER:
      try {
        double d = Double.parseDouble(buffer) * coefficient;
        if (!MathUtils.isInteger(d)) {
          throw new NumberFormatException("" + buffer + "x" + coefficient + " is not a integer.");
        }
      } catch (NumberFormatException nfe) {
        if (nullableError | !StringUtils.isBlank(buffer)) {
          String message = reportField.ERROR_FIELD_NOT_A_INTEGER(buffer);
          LOGGER.warning(message);
        }
        returnValue = false;
      }
      break;

    case LONG:
      try {
        double d = Double.parseDouble(buffer) * coefficient;
        if (!MathUtils.isInteger(d)) {
          throw new NumberFormatException("" + buffer + "x" + coefficient + " is not a long.");
        }
      } catch (NumberFormatException nfe) {
        if (nullableError | !StringUtils.isBlank(buffer)) {
          String message = reportField.ERROR_FIELD_NOT_A_LONG(buffer);
          LOGGER.warning(message);
        }
        returnValue = false;
      }
      break;

    case FLOAT:
      try {
        Double.parseDouble(buffer);
      } catch (NumberFormatException nfe) {
        if (nullableError | !StringUtils.isBlank(buffer)) {
          String message = reportField.ERROR_FIELD_NOT_A_FLOAT(buffer);
          LOGGER.warning(message);
        }
        returnValue = false;
      }

      break;

    case DOUBLE:
      try {
        Double.parseDouble(buffer);
      } catch (NumberFormatException nfe) {
        if (nullableError | !StringUtils.isBlank(buffer)) {
          String message = reportField.ERROR_FIELD_NOT_A_DOUBLE(buffer);
          LOGGER.warning(message);
        }
        returnValue = false;
      }
      break;

    case DATETIME:
      try {
        if (buffer.equalsIgnoreCase("sysdate")) {
          new Timestamp(System.currentTimeMillis());
        } else {
          SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
          sdf.setLenient(false);
          new Timestamp(sdf.parse(buffer).getTime());
        }
      } catch (ParseException pe) {
        if (nullableError) {
          String message = reportField.ERROR_FIELD_NOT_A_DATETIME(buffer, getDateFormat());
          LOGGER.warning(message);
        }
        returnValue = false;
      }
      break;

    case STRING:
      returnValue = true;
      break;

    default:
      returnValue = false;
    }

    return returnValue;
  }

  // ---------------------------------------------------------------------------

  public boolean isEmptyOrNullBuffer() {
    return (getBuffer() == null) || getBuffer().equals("");
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#getBuffer()
   */
  public String getBuffer() {
    return buffer;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#getCoefficient()
   */
  public double getCoefficient() {
    return coefficient;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#getDateFormat()
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#isUse()
   */
  public boolean isUse() {
    return isUse;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#getName()
   */
  public String getName() {
    return name;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#isNullable()
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * @return boolean
   */
  public boolean isNullableError() {
    return nullableError;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Field#getType()
   */
  public FieldFormatEnum getTypeFormat() {
    return typeFormat;
  }

  /**
   * Sets the buffer.
   * 
   * @param buffer
   *          The buffer to set
   * @see org.jobjects.dbimp.trigger.Field#setBuffer(java.lang.String)
   */
  public void setBuffer(String buffer) {
    this.buffer = buffer;
  }

  /**
   * Sets the coefficient.
   * 
   * @param coefficient
   *          The coefficient to set
   */
  public void setCoefficient(double coefficient) {
    this.coefficient = coefficient;
  }

  /**
   * Sets the dateformat.
   * 
   * @param dateFormat
   *          The dateformat to set
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Sets the isUse.
   * 
   * @param isUse
   *          The isUse to set
   */
  public void setUse(boolean isUse) {
    this.isUse = isUse;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *          The name to set
   */
  public void setName(String name) {
    this.name = name;
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
   * Sets the nullableError.
   * 
   * @param nullableError
   *          The nullableError to set
   */
  public void setNullableError(boolean nullableError) {
    this.nullableError = nullableError;
  }

  /**
   * Sets the typeFormat.
   * 
   * @param typeFormat
   *          The typeFormat to set
   */
  public void setTypeFormat(FieldFormatEnum type) {
    this.typeFormat = type;
  }

  /**
   * @return Returns the checkIn.
   */
  public Collection<String> getCheckIn() {
    return checkIn;
  }

  /**
   * @param checkIn
   *          The checkIn to set.
   */
  public void setCheckIn(Collection<String> checkIn) {
    this.checkIn = checkIn;
  }

  /**
   * @return Returns the checkInSql.
   */
  public String getCheckInSql() {
    return checkInSql;
  }

  /**
   * @param checkInSql
   *          The checkInSql to set.
   */
  public void setCheckInSql(String checkInSql) {
    this.checkInSql = checkInSql;
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Field)) {
      return false;
    }
    Field rhs = (Field) object;
    return new EqualsBuilder().append(this.name, rhs.getName()).isEquals();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return new HashCodeBuilder(206116827, -753321887).append(this.name).toHashCode();
  }

  /**
   * @see java.lang.Comparable#compareTo(Object)
   */
  public int compareTo(XmlField object) {
    return new CompareToBuilder().append(this.name, object.name).toComparison();
  }

  /**
   * Method loadBuffer. Charge l'attribut 'buffer' à partir du contenu du
   * fichier. Les erreurs seront envoyé sur 'reporting'.
   * 
   * @param connection
   * @param ligne
   *          Valeur de la ligne en cours de lecture.
   * @param reportField
   * @return Retourne faux si une erreur a été trouvé. Les problèmes de format,
   *         de source de donnée, ... seront enregistrés dans 'reporting'.
   */
  public boolean loadBuffer(Connection connection, String ligne, ReportField reportField) {
    boolean returnValue = true;
    try {
      switch (this.getDiscriminator()) {
      case CONSTANTE:
        buffer = this.getConstante().getValue();
        break;

      case POSITION:

        try {
          buffer = ligne
              .substring(this.getPosition().getStartposition(), this.getPosition().getStartposition() + this.getPosition().getSize())
              .trim();
          returnValue = isBufferValid(reportField);
        } catch (IndexOutOfBoundsException ioobe) {
          LOGGER.log(Level.SEVERE, "Ligne(" + reportField.getReportLine().getNumberLine() + ") : " + buffer);
          LOGGER.log(Level.SEVERE, reportField.ERROR_FIELD_NOT_IN_FILE());
          /*
           * Si on veut arrêter l'importation de la ligne si les champs est
           * inaxcessible alors il faut decommenter : 'returnValue= false;'
           */
          // returnValue= false;
        }

        break;

      case QUERY:
        buffer = null;
        returnValue &= executeSubQuery(connection, ligne, reportField);
        /** *********************************** */
        LOGGER.finest("Ligne(" + reportField.getReportLine().getNumberLine() + ") : " + buffer);
        break;
      }
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Ligne(" + reportField.getReportLine().getNumberLine() + ") : ", ex);
      returnValue = false;
    }

    if (buffer != null) {
      buffer = buffer.trim();
    }

    return returnValue;
  }

  // ---------------------------------------------------------------------------

  private boolean executeSubQuery(Connection connection, String ligne, ReportField reportField) throws Exception {
    boolean returnValue = true;
    if (getQuery().getQueryParams().size() != 0) {
      try {
        PreparedStatement pstmt = connection.prepareStatement(this.getQuery().getSql());

        try {
          int i = 1;
          StringBuffer message_params = new StringBuffer();

          for (XmlQueryParam queryparam : this.getQuery().getQueryParams()) {
            String s_value = null;

            switch (queryparam.getDiscriminator()) {
            case CONSTANTE:
              s_value = queryparam.getConstante().getValue();
              message_params.append("|        " + i + ") Paramêtre Constante Valeur=" + s_value);
              break;

            case POSITION:
              try {
                s_value = ligne.substring(queryparam.getPosition().getStartposition(),
                    queryparam.getPosition().getStartposition() + queryparam.getPosition().getSize()).trim();
              } catch (IndexOutOfBoundsException ioobe) {
                LOGGER.severe(reportField.ERROR_FIELD_NOT_IN_FILE());
                setBuffer(null);
                returnValue = false;
              }
              message_params.append("|        " + i + ") Position=" + queryparam.getPosition().getStartposition() + " Size="
                  + queryparam.getPosition().getSize() + " Valeur=" + s_value);
              break;
            case QUERY:
              throw new IllegalArgumentException("Pas de typeFormat QUERY en sous requête.");
            default:
              throw new IllegalArgumentException("Seul les types CONSTANTE et POSITION en sous requête.");
            }

            boolean isNull = s_value == null ? true : s_value.trim().equals("");

            switch (queryparam.getType()) {
            case INTEGER:
              try {
                if (!isNull) {
                  int i_value = Integer.parseInt(s_value);
                  pstmt.setInt(i, i_value);
                } else {
                  pstmt.setNull(i, java.sql.Types.INTEGER);
                }
              } catch (NumberFormatException nfe) {
                LOGGER.severe(reportField.ERROR_FIELD_NOT_A_INTEGER(s_value));
                setBuffer(null);
                returnValue = false;
              }
              break;

            case LONG:
              try {
                if (!isNull) {
                  long l_value = Long.parseLong(s_value);
                  pstmt.setLong(i, l_value);
                } else {
                  pstmt.setNull(i, java.sql.Types.INTEGER);
                }
              } catch (NumberFormatException nfe) {
                LOGGER.severe(reportField.ERROR_FIELD_NOT_A_LONG(s_value));
                setBuffer(null);
                returnValue = false;
              }
              break;

            case FLOAT:
              try {
                if (!isNull) {
                  float f_value = Float.parseFloat(s_value);
                  pstmt.setFloat(i, f_value);
                } else {
                  pstmt.setNull(i, java.sql.Types.FLOAT);
                }
              } catch (NumberFormatException nfe) {
                LOGGER.severe(reportField.ERROR_FIELD_NOT_A_FLOAT(s_value));
                setBuffer(null);
                returnValue = false;
              }
              break;

            case DOUBLE:
              try {
                if (!isNull) {
                  double d_value = Double.parseDouble(s_value);
                  pstmt.setDouble(i, d_value);
                } else {
                  pstmt.setNull(i, java.sql.Types.DOUBLE);
                }
              } catch (NumberFormatException nfe) {
                LOGGER.severe(reportField.ERROR_FIELD_NOT_A_DOUBLE(s_value));
                setBuffer(null);
                returnValue = false;
              }
              break;

            case DATETIME:
              if (!isNull) {
                Timestamp ts_value = null;
                try {
                  if (s_value.equalsIgnoreCase("sysdate")) {
                    ts_value = new Timestamp(System.currentTimeMillis());
                  } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(queryparam.getDateformat());
                    ts_value = new Timestamp(sdf.parse(s_value).getTime());
                  }
                } catch (ParseException pe) {
                  LOGGER.severe(reportField.ERROR_FIELD_NOT_A_DATETIME(s_value, queryparam.getDateformat()));
                  setBuffer(null);
                  returnValue = false;
                }
                pstmt.setTimestamp(i, ts_value);
              } else {
                pstmt.setNull(i, java.sql.Types.TIMESTAMP);
              }
              break;

            default:
              if (!isNull) {
                pstmt.setString(i, s_value);
              } else {
                pstmt.setNull(i, java.sql.Types.VARCHAR);
              }
              break;
            }
            // LOGGER.fatal( "("+i+")"+queryparam.type+"="+s_value);
            i++;
          }

          // LOGGER.fatal(this.query().sql);
          ResultSet rs = pstmt.executeQuery();

          try {
            if (rs.next()) {
              setBuffer(rs.getString(1));
            }

            if (isEmptyOrNullBuffer() && (!isNullable())) {
              if (isNullableError()) {
                LOGGER.info(reportField.ERROR_FIELD_MANDATORY(message_params.toString()));
              }
              returnValue = false;
            }

          } finally {
            rs.close();
          }
        } finally {
          pstmt.close();
        }
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE,
            "Ligne(" + reportField.getReportLine().getNumberLine() + ") for field name : " + getName() + " : " + getQuery().getSql(), ex);
        returnValue = false;
      }
    } else {
      /** *********************************** */
      try {
        Statement stmt = connection.createStatement();

        try {
          ResultSet rs = stmt.executeQuery(this.getQuery().getSql());

          try {
            if (rs.next()) {
              setBuffer(rs.getString(1));
            }
          } finally {
            rs.close();
          }
        } finally {
          stmt.close();
        }
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE,
            "Ligne(" + reportField.getReportLine().getNumberLine() + ") for field name : " + getName() + " : " + getQuery().getSql(), ex);
        returnValue = false;
      }
    }

    return returnValue;
  }

}