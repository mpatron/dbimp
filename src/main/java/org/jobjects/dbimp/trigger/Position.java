package org.jobjects.dbimp.trigger;

public interface Position {

  /**
   * @return Returns the size.
   */
  int getSize();

  /**
   * @param size
   *          The size to set.
   */
  void setSize(int size);

  /**
   * @return Returns the startposition.
   */
  int getStartposition();

  /**
   * @param startposition
   *          The startposition to set.
   */
  void setStartposition(int startposition);


  /**
   * Retourne la valeur du champs de la position à partir de la ligne donnée en paramettre.
   * @param ligne
   * @return
   */
  String getValue(String ligne);
}