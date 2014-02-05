/*
 * Created on 6 mars 03
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jobjects.dbimp.trigger;


/**
 * Tag field.
 * Utilisé lors de la lecture du fichier par les trigger.
 * @author Mickael Patron
 * @version 2.0
 */
public interface Field {

  /**
   * Constante : Champ de type position.
   */
  public final static int POSITION= 0;
  
  /**
   * Constante : Champ de type constante.
   */
  public final static int CONSTANTE= 1;
  
  /**
   * Constante : Champ de type query.
   */
  public final static int QUERY= 3;

  /**
   * Retourne le nom du champs.
	 * @return String
	 */
	public String getName();
  
  /**
   * Retourne le contenu du buffer.
	 * @return String
	 */
	public String getBuffer();
  
  /**
   * Spécifie le contenu du buffer. Attention c'est à partir du contenu du buffer que les modifications
   * sont apportés à la base donnée.
	 * @param buffer
	 */
	public void setBuffer(String buffer);
  
  /**
   * Indique si le buffer est null ou vide.
	 * @return boolean
	 */
	public boolean isEmptyOrNullBuffer();
  
  /**
   * Retourne le type du champs. Valeur posible : POSITION, CONSTANTE, QUERY.
	 * @return int
	 */
	public FieldTypeEnum getType();
  
  /**
   * Indique si le champs peut être null.
	 * @return boolean
	 */
	public boolean isNullable();

  /**
   * Format de la date.
   *   <table   border=0 cellspacing=3 cellpadding=0>
   *       <tr       bgcolor="#ccccff">
   *           <th align=left>Letter
   *           <th align=left>Date or Time Component
   *           <th align=left>Presentation
   *           <th align=left>Examples
   *       <tr>
   *           <td><code>G</code>
   *           <td>Era designator
   *           <td>Text
   *           <td><code>AD</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>y</code>
   *           <td>Year
   *           <td>Year
   *           <td><code>1996</code>; <code>96</code>
   *       <tr>
   *           <td><code>M</code>
   *           <td>Month in year
   *           <td>Month
   *           <td><code>July</code>; <code>Jul</code>; <code>07</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>w</code>
   *           <td>Week in year
   *           <td>Number
   *           <td><code>27</code>
   *       <tr>
   *           <td><code>W</code>
   *           <td>Week in month
   *           <td>Number
   *           <td><code>2</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>D</code>
   *           <td>Day in year
   *           <td>Number
   *           <td><code>189</code>
   *       <tr>
   *           <td><code>d</code>
   *           <td>Day in month
   *           <td>Number
   *           <td><code>10</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>F</code>
   *           <td>Day of week in month
   *           <td>Number
   *           <td><code>2</code>
   *       <tr>
   *           <td><code>E</code>
   *           <td>Day in week
   *           <td>Text
   *           <td><code>Tuesday</code>; <code>Tue</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>a</code>
   *           <td>Am/pm marker
   *           <td>Text
   *           <td><code>PM</code>
   *       <tr>
   *           <td><code>H</code>
   *           <td>Hour in day (0-23)
   *           <td>Number
   *           <td><code>0</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>k</code>
   *           <td>Hour in day (1-24)
   *           <td>Number
   *           <td><code>24</code>
   *       <tr>
   *           <td><code>K</code>
   *           <td>Hour in am/pm (0-11)
   *           <td>Number
   *           <td><code>0</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>h</code>
   *           <td>Hour in am/pm (1-12)
   *           <td>Number
   *           <td><code>12</code>
   *       <tr>
   *           <td><code>m</code>
   *           <td>Minute in hour
   *           <td>Number
   *           <td><code>30</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>s</code>
   *           <td>Second in minute
   *           <td>Number
   *           <td><code>55</code>
   *       <tr>
   *           <td><code>S</code>
   *           <td>Millisecond
   *           <td>Number
   *           <td><code>978</code>
   *       <tr bgcolor="#eeeeff">
   *           <td><code>z</code>
   *           <td>Time zone
   *           <td>General time zone
   *           <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code>
   *       <tr>
   *           <td><code>Z</code>
   *           <td>Time zone
   *           <td>RFC 822 time zone
   *           <td><code>-0800</code>
   *   </table>
   * @return String
   */
	public String getDateFormat();
  /**
   * Coéficient de multiplication. Utilisé pour multiplié la valeur sielle est de type integer|long|float|double.
	 * @return float
	 */
	public double getCoefficient();
  
  /**
   * Indique si le champ est utiliser pour faire les mise à jour dans la base.
	 * @return boolean
	 */
	public boolean isUse();
}
