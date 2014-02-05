/*
 * Créé le 5 sept. 2003
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.jobjects.dbimp.report;

import java.util.Comparator;

/**
 * @author MP
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class IntegerComparator implements Comparator<Integer> {

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Integer o1, Integer o2) {
    if ((o1 == null) | !(o1 instanceof Integer)) {
      return 1;
    }
    if ((o2 == null) | !(o2 instanceof Integer)) {
      return -1;
    }
    Integer i1= (Integer) o1;
    Integer i2= (Integer) o2;
    return i1.compareTo(i2);
  }

}
