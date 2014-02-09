package org.jobjects.dbimp.xml;
/**
 * Utilisé dans la lecture du fichier de paramètrage. Le tag position permet
 * d'enregistrer l'index du caractère de début dans le 'startposition' et 'size' 
 * qui est la taille de la chaine à lire. La lecture se fait comme pour le C : 
 * le premier caractère a pour index 0.
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlPosition {
  private int startposition = 0;
  private int size = 0;

  /**
   * @return Returns the size.
   */
  public int getSize() {
    return size;
  }
  /**
   * @param size The size to set.
   */
  public void setSize(int size) {
    this.size = size;
  }
  /**
   * @return Returns the startposition.
   */
  public int getStartposition() {
    return startposition;
  }
  /**
   * @param startposition The startposition to set.
   */
  public void setStartposition(int startposition) {
    this.startposition = startposition;
  }

  public String toString() {
    String returnValue= "<position startposition=\"" + startposition + "\" size=\"" + size + "\"/>";
    return returnValue;
  }
}