package org.jobjects.dbimp.xml;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.jobjects.dbimp.trigger.FiletypeEnum;
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
  @NotNull
  private FiletypeEnum filetype;
  private String separateur;
  private int startposition = 0;
  private int size = 0;

  public XmlPosition(FiletypeEnum filetype, String separateur) {
    this.filetype = filetype;
    this.separateur = separateur;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Position#getSize()
   */
  @Override
  public int getSize() {
    return size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Position#setSize(int)
   */
  @Override
  public void setSize(int size) {
    this.size = size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Position#getStartposition()
   */
  @Override
  public int getStartposition() {
    return startposition;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Position#setStartposition(int)
   */
  @Override
  public void setStartposition(int startposition) {
    this.startposition = startposition;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.xml.Position#getValue(Line)
   */
  @Override
  public String getValue(String ligne) {
    String returnValue = null;
    switch (filetype) {
    case FILE_TEXT:
      returnValue = StringUtils.substring(ligne, getStartposition(), getStartposition() + getSize());
      break;
    case FILE_CSV:
      String separatorChar = StringUtils.defaultString(separateur, ",");
      String[] champs = StringUtils.split(ligne, separatorChar);
      returnValue = champs[getStartposition()];
      break;
    default:
      StringBuffer sb = new StringBuffer();
      sb.append("Type de fichier :").append(filetype).append(System.lineSeparator());
      sb.append("Startposition").append(getStartposition()).append(System.lineSeparator());
      sb.append("Size").append(getSize()).append(System.lineSeparator());
      sb.append("Ligne :").append(ligne);
      throw new IllegalArgumentException(sb.toString());
      // break;
    }
    return returnValue;
  }

  public String toString() {
    String returnValue = "<position startposition=\"" + startposition + "\" size=\"" + size + "\"/>";
    return returnValue;
  }
}