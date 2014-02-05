/*
 * Created on 6 mars 03
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jobjects.dbimp.trigger;

import java.util.Collection;

import org.jobjects.dbimp.tools.CollectionMap;

/**
 * Tag line.
 * Utilisé dans la lecture du fichier de paramètrage.
 * @author Mickael Patron
 * @version 2.0
 */
public interface Line {
  
  /**
   * Retourne le nom du type de ligne, il est unique.
	 * @return String
	 */
	public String getName();
  /**
   * Retourne le nom de la table de la base de donnée.
	 * @return String
	 */
	public String getTableName();
	
  /**
   * @return Returns the keys selectors.
   */
  public Collection<Key> getKeys();	
  /**
   * Retourne la liste des champs ce type de ligne.
	 * @return Fields
	 */
	public CollectionMap<Field> getFields();
  /**
   * Retourn le type de l'action du type de ligne : INSERT|UPDATE|INSERT_UPDATE|DELETE|SHOW
	 * @return String
	 */
	public LineActionTypeEnum getAction();
  
  public Trigger getTrigger();
}
