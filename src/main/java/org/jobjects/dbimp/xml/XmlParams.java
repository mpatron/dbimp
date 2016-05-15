package org.jobjects.dbimp.xml;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.BooleanUtils;
import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.trigger.FiletypeEnum;
import org.jobjects.dbimp.trigger.LineActionTypeEnum;
import org.jobjects.dbimp.trigger.Position;
import org.jobjects.dbimp.trigger.Trigger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public class XmlParams extends DefaultHandler {

  private XmlDocument document = null;

  private int level = 0;

  private String[] xmlPath = new String[100];

  private boolean error_in_xml = false;

  private Logger LOGGER = Logger.getLogger(getClass().getName());

  /** Warning. */
  public void warning(SAXParseException ex) {
    System.err.println("[Warning] " + getLocationString(ex) + ": " + ex.getMessage());
    error_in_xml = true;
  }

  /** Error. */
  public void error(SAXParseException ex) {
    System.err.println("[Error] " + getLocationString(ex) + ": " + ex.getMessage());
    error_in_xml = true;
  }

  /** Fatal error. */
  public void fatalError(SAXParseException ex) throws SAXException {
    System.err.println("[Fatal Error] " + getLocationString(ex) + ": " + ex.getMessage());
    error_in_xml = true;
    throw ex;
  }

  /** Returns a string of the location. */
  private String getLocationString(SAXParseException ex) {
    StringBuffer str = new StringBuffer();
    str.append(System.lineSeparator());
    str.append("Ligne:" + ex.getLineNumber());
    str.append(System.lineSeparator());
    str.append("Column:" + ex.getColumnNumber());
    str.append(System.lineSeparator());
    str.append("PublicId:" + ex.getPublicId());
    str.append(System.lineSeparator());
    str.append("SystemId:" + ex.getSystemId());
    str.append(System.lineSeparator());

    // String systemId= ex.getSystemId();
    //
    // if (systemId != null) {
    // int index= systemId.lastIndexOf('/');
    // if (index != -1) {
    // systemId= systemId.substring(index + 1);
    // }
    // str.append(systemId);
    // }
    // str.append("Ligne:");
    // str.append(ex.getLineNumber());
    // str.append(" Column:");
    // str.append(ex.getColumnNumber());

    return str.toString();
  } // getLocationString(SAXParseException):String

  public void startDocument() {
    LOGGER.finest("startDocument");
  }

  public void startElement(String uri, String local, String raw, Attributes attrs) {
    try {
      String Path = new String();
      xmlPath[level] = raw;
      for (int i = 0; i <= level; i++) {
        if (i == 0) {
          Path = xmlPath[i];
        } else {
          Path += ("." + xmlPath[i]);
        }
      }

      LOGGER.finest("Path=" + Path);
      if ("document".equals(Path)) {
        if (attrs != null) {
          document = new XmlDocument();

          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("description".equalsIgnoreCase(attrs.getQName(i))) {
                document.setDescription(attrs.getValue(i));
              }
              if ("filetype".equalsIgnoreCase(attrs.getQName(i))) {
                document.setFiletype(FiletypeEnum.valueOf(attrs.getValue(i)));
              }
            } catch (Exception e) {
              LOGGER.log(Level.SEVERE, "Error in document", e);
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line".equals(Path)) {
        if (attrs != null) {
          int len = attrs.getLength();
          String name = null;
          String tableName = null;
          LineActionTypeEnum action = LineActionTypeEnum.INSERT_UPDATE;
          Trigger trigger = null;
          for (int i = 0; i < len; i++) {
            try {
              if ("name".equalsIgnoreCase(attrs.getQName(i))) {
                name = attrs.getValue(i);
              }

              if ("tablename".equalsIgnoreCase(attrs.getQName(i))) {
                tableName = attrs.getValue(i);
              }

              if ("action".equalsIgnoreCase(attrs.getQName(i))) {
                action = LineActionTypeEnum.valueOf(attrs.getValue(i));
              }

              if ("trigger_class_name".equalsIgnoreCase(attrs.getQName(i))) {
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                trigger = (Trigger) cl.loadClass(attrs.getValue(i)).newInstance();
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
          XmlLine xmlline = new XmlLine(name, tableName, action, trigger);

          document.getLines().add(xmlline);
        }
      }

      if ("document.line.key".equals(Path)) {
        if (attrs != null) {
          XmlKey key = new XmlKey();
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              LOGGER.finest(String.format("QName=%s Value=%s", attrs.getQName(i), attrs.getValue(i)));

              if ("value".equalsIgnoreCase(attrs.getQName(i))) {
                key.setKeyValue(attrs.getValue(i));
              }

              if ("startposition".equalsIgnoreCase(attrs.getQName(i))) {
                key.setStartposition(Integer.parseInt(attrs.getValue(i)));
              }

              if ("size".equalsIgnoreCase(attrs.getQName(i))) {
                key.setSize(Integer.parseInt(attrs.getValue(i)));
              }

              if ("isBlank".equalsIgnoreCase(attrs.getQName(i))) {
                key.setBlank(BooleanUtils.toBoolean(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          xmlline.getKeys().add(key);
        }
      }

      if ("document.line.field".equals(Path)) {
        if (attrs != null) {
          int len = attrs.getLength();
          String name = null;
          FieldFormatEnum type = FieldFormatEnum.STRING;
          boolean nullable = false;
          boolean nullableError = true;
          boolean isUse = true;
          for (int i = 0; i < len; i++) {
            try {
              if ("fieldname".equals(attrs.getQName(i))) {
                name = attrs.getValue(i).toUpperCase();
              }

              if ("type".equals(attrs.getQName(i))) {
                type = FieldFormatEnum.valueOfByType(attrs.getValue(i));
              }

              if ("nullable".equals(attrs.getQName(i))) {
                nullable = Boolean.valueOf(attrs.getValue(i)).booleanValue();
              }

              if ("nullable_error".equals(attrs.getQName(i))) {
                nullableError = Boolean.valueOf(attrs.getValue(i)).booleanValue();
              }

              if ("isuse".equals(attrs.getQName(i))) {
                isUse = Boolean.valueOf(attrs.getValue(i)).booleanValue();
              }

            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
          XmlField field = new XmlField(name, type, nullable, nullableError, isUse);

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          xmlline.getFields().add(field);
        }
      }

      if ("document.line.field.string".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.STRING);
      }

      if ("document.line.field.integer".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.INTEGER);

        if (attrs != null) {
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("coefficient".equals(attrs.getQName(i))) {
                field.setCoefficient(Float.parseFloat(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line.field.long".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.LONG);

        if (attrs != null) {
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("coefficient".equals(attrs.getQName(i))) {
                field.setCoefficient(Float.parseFloat(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line.field.float".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.FLOAT);

        if (attrs != null) {
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("coefficient".equals(attrs.getQName(i))) {
                field.setCoefficient(Float.parseFloat(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line.field.double".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.DOUBLE);

        if (attrs != null) {
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("coefficient".equals(attrs.getQName(i))) {
                field.setCoefficient(Float.parseFloat(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line.field.datetime".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.DATETIME);

        if (attrs != null) {
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("dateformat".equals(attrs.getQName(i))) {
                field.setDateFormat(attrs.getValue(i));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }

      if ("document.line.field.file".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();
        field.setTypeFormat(FieldFormatEnum.BLOB);
      }

      if ("document.line.field.position".equals(Path)) {
        if (attrs != null) {
          Position position = new XmlPosition(document.getFiletype());
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("startposition".equals(attrs.getQName(i))) {
                position.setStartposition(Integer.parseInt(attrs.getValue(i)));
              }

              if ("size".equals(attrs.getQName(i))) {
                position.setSize(Integer.parseInt(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();
          field.setPosition(position);
        }
      }

      if ("document.line.field.constante".equals(Path)) {
        if (attrs != null) {
          XmlConstante constante = new XmlConstante();
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("value".equals(attrs.getQName(i))) {
                constante.setValue(attrs.getValue(i));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();
          field.setConstante(constante);
        }
      }

      if ("document.line.field.query".equals(Path)) {
        if (attrs != null) {
          XmlQuery query = new XmlQuery();
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("sql".equals(attrs.getQName(i))) {
                query.setSql(attrs.getValue(i));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();
          field.setQuery(query);
        }
      }

      if ("document.line.field.query.query-param".equals(Path)) {
        if (attrs != null) {
          XmlQueryParam query_param = new XmlQueryParam();
          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();

          try {
            XmlQuery query = field.getQuery();
            query.getQueryParams().add(query_param);
          } catch (Exception ex) {
            ex.printStackTrace();
            error_in_xml = true;
          }
        }
      }

      if ("document.line.field.query.query-param.string".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.STRING);
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.integer".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.INTEGER);
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.long".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.LONG);
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.float".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.FLOAT);
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.double".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.DOUBLE);
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.datetime".equals(Path)) {
        XmlLine xmlline = (XmlLine) document.getLines().getLast();
        XmlField field = (XmlField) xmlline.getFields().getLast();

        try {
          XmlQuery query = field.getQuery();
          XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
          query_param.setType(FieldFormatEnum.DATETIME);

          if (attrs != null) {
            int len = attrs.getLength();

            for (int i = 0; i < len; i++) {
              try {
                if ("dateformat".equals(attrs.getQName(i))) {
                  query_param.setDateformat(attrs.getValue(i));
                }
              } catch (Exception e) {
                e.printStackTrace();
                error_in_xml = true;
              }
            }
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          error_in_xml = true;
        }
      }

      if ("document.line.field.query.query-param.position".equals(Path)) {
        if (attrs != null) {
          Position position = new XmlPosition(document.getFiletype());
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("startposition".equals(attrs.getQName(i))) {
                position.setStartposition(Integer.parseInt(attrs.getValue(i)));
              }

              if ("size".equals(attrs.getQName(i))) {
                position.setSize(Integer.parseInt(attrs.getValue(i)));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();

          try {
            XmlQuery query = field.getQuery();
            XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
            query_param.setPosition(position);
          } catch (Exception ex) {
            ex.printStackTrace();
            error_in_xml = true;
          }
        }
      }

      if ("document.line.field.query.query-param.constante".equals(Path)) {
        if (attrs != null) {
          XmlConstante constante = new XmlConstante();
          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("value".equals(attrs.getQName(i))) {
                constante.setValue(attrs.getValue(i));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }

          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();

          try {
            XmlQuery query = field.getQuery();
            XmlQueryParam query_param = (XmlQueryParam) query.getQueryParams().getLast();
            query_param.setConstante(constante);
          } catch (Exception ex) {
            ex.printStackTrace();
            error_in_xml = true;
          }
        }
      }

      if ("document.line.field.check_in".equals(Path)) {
        if (attrs != null) {
          XmlLine xmlline = (XmlLine) document.getLines().getLast();
          XmlField field = (XmlField) xmlline.getFields().getLast();

          int len = attrs.getLength();

          for (int i = 0; i < len; i++) {
            try {
              if ("sql".equals(attrs.getQName(i))) {
                field.setCheckInSql(attrs.getValue(i));
              }
            } catch (Exception e) {
              e.printStackTrace();
              error_in_xml = true;
            }
          }
        }
      }
    } catch (Throwable t) {
      String message = "Unknow error";
      message += System.lineSeparator() + "  Path =";
      for (int i = 0; i < xmlPath.length; i++) {
        message += xmlPath[i] + ".";
      }
      message += System.lineSeparator() + "  uri=" + uri;
      message += System.lineSeparator() + "  local=" + local;
      message += System.lineSeparator() + "  raw=" + raw;
      message += System.lineSeparator() + "  level=" + level;
      message += System.lineSeparator() + "  attrs=";
      if (attrs != null) {
        for (int i = 0; i < attrs.getLength(); i++) {
          message += System.lineSeparator() + "    attr=(" + attrs.getQName(i) + ", " + attrs.getValue(i) + ")";
        }
        if (attrs.getLength() == 0) {
          message += " aucun élément.";
        }
      } else {
        message += " null";
      }
      LOGGER.log(Level.SEVERE, message, t);
    }
    level++;
  }

  public void endElement(String uri, String local, String raw) {
    level--;
  }

  public void characters(char[] ch, int start, int length) {
    // Donne de contenu du valeur d'un node
    LOGGER.finest(new String(ch, start, length));
  }

  public XmlDocument parseFile(File file) throws SAXException // "D:\\Personnel\\generate\\tmp\\table.xml"
  {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setValidating(true);
      SAXParser parser = factory.newSAXParser();
      error_in_xml = false;
      parser.parse(file, this);
      // parser.parse(uri);

      ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
      Validator validator = validatorFactory.getValidator();
      Set<ConstraintViolation<XmlDocument>> violations = validator.validate(document);
      StringBuffer sb = new StringBuffer();
      for (ConstraintViolation<XmlDocument> violation : violations) {
        sb.append(String.format("%s: %s%n", violation.getPropertyPath(), violation.getMessage()));
        sb.append(System.lineSeparator());
      }
      LOGGER.log(Level.SEVERE, "Validation du xml : " + sb.toString());

      if (error_in_xml) {
        LOGGER.log(Level.SEVERE, "Error in the file " + file);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unknow error with the file " + file, e);
      throw new SAXException(e);
    }

    return document;
  }

}
