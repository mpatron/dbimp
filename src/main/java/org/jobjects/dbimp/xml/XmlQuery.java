package org.jobjects.dbimp.xml;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Tag query. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public class XmlQuery {

  private String     sql          = null;

  private LinkedList<XmlQueryParam> queryParams = new LinkedList<XmlQueryParam>();

  /**
   * @return Returns the queryParams.
   */
  public LinkedList<XmlQueryParam> getQueryParams() {
    return queryParams;
  }
  /**
   * @param queryParams The queryParams to set.
   */
  public void setQueryParams(LinkedList<XmlQueryParam> queryParams) {
    this.queryParams = queryParams;
  }
  /**
   * @return Returns the sql.
   */
  public String getSql() {
    return sql;
  }
  /**
   * @param sql The sql to set.
   */
  public void setSql(String sql) {
    this.sql = sql;
  }
  public XmlQuery() {

  }

  public String toString() {
    String returnValue = "      <query sql=\"" + sql + "\">";
    for (Iterator<XmlQueryParam> it = queryParams.iterator(); it.hasNext();) {
      returnValue += it.next().toString() + System.getProperty("line.separator");
    }
    returnValue += "      </query>";
    return returnValue;
  }

}