package org.jobjects.dbimp.sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;

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
 * Date : 24 fevr. 2004
 * </p>
 * Utiliaire pour les fonctions Sql
 * 
 * @author Mickael Patron
 * @version 1.0
 */
public class SqlUtils {

  private static Logger log = Logger.getLogger(SqlUtils.class.getName());

  public static void AfficheSQLException(Line xmlline, String message, SQLException ex, ReportLine reporting) {
    // ORA-02290: violation de contraintes
    // ORA-00001: unicité
    // if(!ex.getMessage().startsWith("ORA-00001")) {
    String chaine = xmlline.getName() + " : " + "Ligne(" + reporting.getNumberLine() + ") : ";
    chaine += (ex.getMessage() + "." + System.getProperty("line.separator"));
    chaine += showLine(reporting.getNumberLine(), xmlline);
    log.log(Level.SEVERE, message + System.getProperty("line.separator") + chaine, ex);

    reporting.ERROR_MESSAGE(xmlline.getName(), ex.getMessage());
    reporting.showLine();
  }
  // ---------------------------------------------------------------------------

  public static String showLine(int nbLigne, Line xmlline) {
    String returnValue = "Ligne(" + nbLigne + ") :" + System.getProperty("line.separator");

    for (Field field : xmlline.getFields()) {
      try {
        switch (field.getDiscriminator()) {
        case POSITION:
          returnValue += ("  " + field.getName() + "(" + field.getPosition().getStartposition() + ", " + field.getPosition().getSize() + ") = \"");
          returnValue += field.getBuffer();
          returnValue += ("\"" + System.getProperty("line.separator"));
          break;

        case CONSTANTE:
          returnValue += ("  " + field.getName() + "(cste) = \"");
          returnValue += field.getConstante().getValue();
          returnValue += ("\"" + System.getProperty("line.separator"));
          break;

        case QUERY:
          returnValue += ("  " + field.getName() + "(query) = \"");
          returnValue += field.getQuery().getSql();
          returnValue += ("\"" + System.getProperty("line.separator"));
          break;
        }
      } catch (Exception exinternal) {
        returnValue += ("  " + field.getName() + " (unkown error)" + System.getProperty("line.separator"));
      }
    }

    return returnValue;
  }
  // ---------------------------------------------------------------------------
}
