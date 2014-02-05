package org.jobjects.dbimp.xml;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportField;
import org.jobjects.dbimp.report.ReportLine;
import org.jobjects.dbimp.tools.CollectionMap;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Key;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.trigger.LineActionTypeEnum;
import org.jobjects.dbimp.trigger.Trigger;


/**
 * Tag line.
 * Utilisé dans la lecture du fichier de paramètrage.
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlLine implements Line {

  /**
   * Constructeur d'une line.
   * @param name
   * @param tableName
   * @param action
   * @param trigger
   */
  public XmlLine(String name, String tableName, LineActionTypeEnum action, Trigger trigger) {
    this.name= name;
    this.tableName= tableName;
    this.action= action;
    this.trigger= trigger;
  }

  /**
   * trigger prend la valeur null.
   * @param name
   * @param tableName
   * @param action
   */
  public XmlLine(String name, String tableName, LineActionTypeEnum action) {
    this.name= name;
    this.tableName= tableName;
    this.action= action;
    this.trigger= null;
  }

  /**
   * trigger prend la valeur null.
   * action prend la valeur INSERT_UPDATE.
   * @param name
   * @param tableName
   */
  public XmlLine(String name, String tableName) {
    this.name= name;
    this.tableName= tableName;
    this.action= LineActionTypeEnum.INSERT_UPDATE;
    this.trigger= null;
  }

  /**
   * Nom du type de ligne, il doit être unique.
   */
  private String name= null;

  /**
   * Nom de la table de la base de donnée.
   */
  private String tableName= null;

  /**
   * Type de l'action du type de ligne : INSERT|UPDATE|INSERT_UPDATE|DELETE|SHOW
   */
  private LineActionTypeEnum action= null;

  /**
   * Instance du trigger affecté au type de ligne.
   */
  private Trigger trigger= null;

  /**
   * Liste des  déclancheurs.
   */
  private Collection<Key> keys= new LinkedList<Key>();

  /**
   * Liste des champs ce  type de ligne.
   */
  private  CollectionMap<Field> fields= new CollectionMap<Field>();
  
  public  CollectionMap<Field> getFields() {
    return fields;
  }

  //---------------------------------------------------------------------------
  private Logger log = Logger.getLogger(getClass().getName());
  //---------------------------------------------------------------------------

  /**
   * Method loadFields. Chargement de l'attribut buffer de tout les champs pour la ligne en cours.
   * Si des erreurs sont reperés, elles seront enregistrés dans reporting.
   * @param connection
   * @param ligne Valeur de la ligne en cours de lecture.
   * @param reporting
   * @return Retourne faux si une erreur a été trouvé. Les problèmes de format, de source de donnée, ... seront enregistrés dans
   * 'reporting'.
   * @see XmlField#loadBuffer(Connection , String , ReportField )
   */
  public boolean loadFields(Connection connection, String ligne, ReportLine reporting) {
    boolean returnValue= true;
    
    for (Iterator<Field> it= fields.iterator(); it.hasNext();) {
      XmlField field= (XmlField) it.next();
      returnValue &= field.loadBuffer(connection, ligne, reporting.getReportField(field));

      if (field.isEmptyOrNullBuffer() && (!field.isNullable())) {
        if (field.isNullableError()) {
          log.info(reporting.getReportField(field).ERROR_FIELD_MANDATORY());
        }
        returnValue= false;
      }
    }

    return returnValue;
  }
  //---------------------------------------------------------------------------

  /**
   * Method unloadFields. Mise en null de buffer de tous les champs.
   */
  public void unloadFields() {
    for (Iterator<Field> it= fields.iterator(); it.hasNext();) {
      XmlField field= (XmlField) it.next();
      field.setBuffer(null);
    }
  }
  //---------------------------------------------------------------------------

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    String returnValue= "  <line name=\"" + name + "\" tablename=\"" + tableName + "\">";
    returnValue += System.getProperty("line.separator");
    for (Iterator<Key> it= keys.iterator(); it.hasNext();) {
      returnValue += it.next().toString() + System.getProperty("line.separator");
    }
    for (Iterator<Field> it= fields.iterator(); it.hasNext();) {
      returnValue += it.next().toString() + System.getProperty("line.separator");
    }
    returnValue += "</line>" + System.getProperty("line.separator");
    return returnValue;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Line#getName()
   */
  public String getName() {
    return name;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Line#getTableName()
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * @see org.jobjects.dbimp.trigger.Line#getAction()
   */
  public LineActionTypeEnum getAction() {
    return action;
  }

  /**
   * @return Trigger
   */
  public Trigger getTrigger() {
    return trigger;
  }

  /**
   * Sets the action.
   * @param action The action to set
   */
  public void setAction(LineActionTypeEnum action) {
    this.action= action;
  }

  /**
   * @return Returns the keys.
   */
  public Collection<Key> getKeys() {
    return keys;
  }
  /**
   * @param keys The keys to set.
   */
  public void setKeys(Collection<Key> keys) {
    this.keys = keys;
  }
}
