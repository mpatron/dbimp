package org.jobjects.dbimp.xml;

import java.util.LinkedList;

import javax.validation.constraints.NotNull;

import org.jobjects.dbimp.trigger.FiletypeEnum;
import org.jobjects.dbimp.trigger.Line;

/**
 * Tag document. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlDocument {
  private LinkedList<Line> lines = new LinkedList<Line>();

  @NotNull
  private String description = null;

  @NotNull
  private FiletypeEnum filetype = FiletypeEnum.FILE_TEXT;

  public String toString() {
    String returnValue = "<document>" + System.lineSeparator();

    for (Line line : lines) {
      returnValue += line.toString() + System.lineSeparator();
    }
    returnValue += "</document>";
    return returnValue;
  }

  /**
   * Returns the document attribute.
   * 
   * @return String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the document attribute.
   * 
   * @param description
   *          The document to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the filetype
   */
  public FiletypeEnum getFiletype() {
    return filetype;
  }

  /**
   * @param filetype
   *          the filetype to set
   */
  public void setFiletype(FiletypeEnum filetype) {
    this.filetype = filetype;
  }

  /**
   * @return Returns the lines.
   */
  public LinkedList<Line> getLines() {
    return lines;
  }
}