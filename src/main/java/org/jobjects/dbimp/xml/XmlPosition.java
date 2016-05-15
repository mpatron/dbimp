package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FiletypeEnum;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.trigger.Position;

/**
 * Utilisé dans la lecture du fichier de paramètrage. Le tag position permet
 * d'enregistrer l'index du caractère de début dans le 'startposition' et 'size'
 * qui est la taille de la chaine à lire. La lecture se fait comme pour le C :
 * le premier caractère a pour index 0.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlPosition implements Position {
  private FiletypeEnum filetype;
  private int startposition = 0;
  private int size = 0;
  
  public XmlPosition(FiletypeEnum filetype) {
    this.filetype=filetype;
  }

  /* (non-Javadoc)
   * @see org.jobjects.dbimp.xml.Position#getSize()
   */
  @Override
  public int getSize() {
    return size;
  }

  /* (non-Javadoc)
   * @see org.jobjects.dbimp.xml.Position#setSize(int)
   */
  @Override
  public void setSize(int size) {
    this.size = size;
  }

  /* (non-Javadoc)
   * @see org.jobjects.dbimp.xml.Position#getStartposition()
   */
  @Override
  public int getStartposition() {
    return startposition;
  }

  /* (non-Javadoc)
   * @see org.jobjects.dbimp.xml.Position#setStartposition(int)
   */
  @Override
  public void setStartposition(int startposition) {
    this.startposition = startposition;
  }

  public String getValue(Line line) {
    String returnValue = null;
    if(FiletypeEnum.FILE_TEXT.equals(filetype)) {
//      throw new RuntimeException("to do...");
    } else {
//      throw new RuntimeException("to do...");
    }
    return returnValue;
  }
  
  public String toString() {
    String returnValue = "<position startposition=\"" + startposition + "\" size=\"" + size + "\"/>";
    return returnValue;
  }
}