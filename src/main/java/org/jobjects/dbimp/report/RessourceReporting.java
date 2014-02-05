/*
 * Créé le 4 sept. 2003
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.jobjects.dbimp.report;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author MP
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class RessourceReporting /* extends ResourceBundle */{

  private static ResourceBundle instance = null;

  private RessourceReporting() {
  }

  /**
   * @see java.util.ResourceBundle#getBundle(String)
   */
  public static final ResourceBundle getBundle() {
    ResourceBundle returnValue = instance;
    if (instance == null) {
      instance = ResourceBundle.getBundle(RessourceReporting.class.getPackage()
          .getName() + ".reporting");
      // instance= getBundle(RessourceReporting.class.getPackage().getName());
      // instance=
      // ResourceBundle.getBundle("org.jobjects.dbimp.report.reporting");
      returnValue = instance;
    }
    return returnValue;
  }

  /**
   * @see java.util.ResourceBundle#handleGetObject(String)
   */
  protected Object handleGetObject(String arg0) {
    return null;
  }

  /**
   * @see java.util.ResourceBundle#getKeys()
   */
  public Enumeration<String> getKeys() {
    return null;
  }

  static public String getString(String key) {
    String returnValue = getBundle().getString(key);
    return returnValue;
  }

  static public String getString(String key, Object[] objects) {
    String chaine = getBundle().getString(key);
    String returnValue = java.text.MessageFormat.format(chaine, objects);
    return returnValue;
  }
}
