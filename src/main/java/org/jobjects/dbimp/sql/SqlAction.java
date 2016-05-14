package org.jobjects.dbimp.sql;

import java.sql.SQLException;

/**
 * <p>
 * Title: IHM
 * </p>
 * <p>
 * Description: Importation dbImp
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: JObjects
 * </p>
 * <p>
 * Date : 5 sept. 2003
 * </p>
 * 
 * @author Mickael Patron
 * @version 1.0
 */
public interface SqlAction {

  /**
   * Execute l'action Selection, insertion, destruction, mise à jour..
   * 
   * @param nbLigne
   *          Numéro de ligne
   */
  public int execute(int nbLigne);

  /**
   * Ferme les curseurs.
   */
  public void close() throws SQLException;

  /**
   * Retourne le nombre de ligne selectionné,inseré,updaté,deleté dans la base.
   * 
   * @return L'entier retourné est supérieur ou égal à zéro.
   */
  public int getCount();
}
