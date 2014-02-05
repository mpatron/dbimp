package org.jobjects.dbimp.xml;

import java.util.Iterator;
import java.util.LinkedList;

import org.jobjects.dbimp.trigger.Line;

/**
 * Tag document.
 * Utilisé dans la lecture du fichier de paramètrage.
 * @author Mickael Patron
 * @version 2.0
 */
public class XmlDocument {
  private LinkedList<Line> lines = new LinkedList<Line>();
	
	private String description=null;
	
  public String toString() {
    String returnValue= "<document>"+System.getProperty("line.separator");

    for (Iterator<Line> it= lines.iterator(); it.hasNext();) {
      returnValue += it.next().toString()+System.getProperty("line.separator");
    }
    returnValue += "</document>";
    return returnValue;
  }

	/**
	 * Returns the document attribute.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the document attribute.
	 * @param description The document to set
	 */
	public void setDescription(String description) {
		this.description= description;
	}

  /**
   * @return Returns the lines.
   */
  public LinkedList<Line> getLines() {
    return lines;
  }
}