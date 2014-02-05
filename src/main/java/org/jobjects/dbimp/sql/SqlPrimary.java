package org.jobjects.dbimp.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class SqlPrimary {

  private Logger log = Logger.getLogger(getClass().getName());

  protected Connection connection = null;

  private String schemaName = null;

  private static HashMap<String, Collection<String>> tablesPrimaries = null;

  public SqlPrimary(Connection connection, String schemaName) {
    this.connection = connection;
    this.schemaName = schemaName;
  }

  /**
   * Liste des champs de la clef primaire.
   * 
   * @param tableName
   *          Nom de la table
   * @return Un vector contenant la liste des nom de champs sous la forme d'un
   *         String.
   */
  public Collection<String> getPrimaryColumns(String tableName) {
    if (tablesPrimaries == null) {
      tablesPrimaries = new HashMap<String, Collection<String>>();
    }
    Collection<String> primaries = tablesPrimaries.get(tableName);
    if (primaries == null) {
      primaries = loadPrimaryColumns(tableName);
      tablesPrimaries.put(tableName, primaries);
    }
    return primaries;
  }

  /**
   * Liste des champs de la clef primaire.
   * 
   * @param tableName
   *          Nom de la table
   * @return Un vector contenant la liste des nom de champs sous la forme d'un
   *         String.
   */
  private Collection<String> loadPrimaryColumns(String tableName) {
    LinkedList<String> returnValue = new LinkedList<String>();
    ResultSet rs = null;
    try {
      DatabaseMetaData metadata = connection.getMetaData();
      SQLDatatbaseType sqlDatatbaseType = SQLDatatbaseType.getType(metadata
          .getURL());
      /**
       * Specificité Oracle : Il y a des schema et pas de catalog Specificité
       * SQLServer : Il y a des catalogs et pas de schema
       */
      String catalog, schema, table;
      switch (sqlDatatbaseType) {
      case SQLSERVER:
        /*
         * ==SQLServer== il est important que l'url de SQLServer contienne la
         * base jdbc:microsoft:sqlserver://MY_SQLSERVE:1433;DatabaseName=MYDB
         */
        if (StringUtils.isEmpty(schemaName)) {
          catalog = null;
          schema = null;
          table = tableName.toUpperCase();
        } else {
          catalog = null;
          schema = schemaName;
          table = tableName.toUpperCase();
        }
        break;
      case ORACLE:
        /*
         * ==Oracle== Il est important que l'importation se fasse avec le
         * propriétaire du schema.
         */
        if (StringUtils.isEmpty(schemaName)) {
          catalog = null;
          schema = metadata.getUserName();
          table = tableName.toUpperCase();
        } else {
          catalog = null;
          schema = schemaName;
          table = tableName.toUpperCase();
        }
        break;
      case DB2AS400:
        /*
         * ==DB2 AS400== Il est important que l'importation se fasse avec le
         * propriétaire du schema.
         */
        if (StringUtils.isEmpty(schemaName)) {
          catalog = null;
          schema = null;
          table = tableName.toUpperCase();
        } else {
          catalog = null;
          schema = schemaName;
          table = tableName.toUpperCase();
        }
        break;
      default:
        catalog = null;
        schema = schemaName;
        table = tableName;
        break;
      }
      rs = metadata.getPrimaryKeys(catalog, schema, table);
      try {
        while (rs.next()) {
          String chaine = rs.getString("COLUMN_NAME").toUpperCase();
          if (!returnValue.contains(chaine)) {
            returnValue.add(chaine);
          }
        }
      } finally {
        rs.close();
      }
      rs = null;
    } catch (java.sql.SQLException sqle) {
      log.log(Level.SEVERE, "", sqle);
    }
    return returnValue;
  }

  public Connection getConnection() {
    return connection;
  }

  // ---------------------------------------------------------------------------

  public String getSQLSchemaName() {
    return StringUtils.isEmpty(schemaName) ? StringUtils.EMPTY : schemaName
        + ".";
  }

}
