package org.jobjects.dbimp.xml;

import java.util.LinkedList;

import jakarta.validation.constraints.NotNull;

/**
 * Tag query. Utilisé dans la lecture du fichier de paramètrage.
 * 
 * @author Mickael Patron
 * @version 2.0
 */

public class XmlQuery {

  @NotNull
  private String sql = null;

  private LinkedList<XmlQueryParam> queryParams = new LinkedList<XmlQueryParam>();

  /**
   * @return Returns the queryParams.
   */
  public LinkedList<XmlQueryParam> getQueryParams() {
    return queryParams;
  }

  /**
   * @param queryParams
   *          The queryParams to set.
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
   * @param sql
   *          The sql to set.
   */
  public void setSql(String sql) {
    this.sql = sql;
  }

  public XmlQuery() {

  }

  public String toString() {
    String returnValue = "      <query sql=\"" + sql + "\">";
    for (XmlQueryParam queryParam : queryParams) {
      returnValue += queryParam.toString() + System.lineSeparator();
    }
    returnValue += "      </query>";
    return returnValue;
  }

}