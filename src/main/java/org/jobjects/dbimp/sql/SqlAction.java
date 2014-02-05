package org.jobjects.dbimp.sql;

import java.sql.SQLException;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface SqlAction {

  /** Execute l'action Selection, insertion, destruction, mise � jour..
   * @param nbLigne Num�ro de ligne
   */
  public int execute(int nbLigne);

  /** Ferme les curseurs.
   */
  public void close() throws SQLException;

  /** Retourne le nombre de ligne selectionn�,inser�,updat�,delet� dans la base.
   * @return L'entier retourn� est sup�rieur ou �gal � z�ro.
   */
  public int getCount();
}
