/*
 * Créé le 24 févr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.jobjects.dbimp.sql;

import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jobjects.dbimp.report.ReportLine;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.Line;
import org.jobjects.dbimp.xml.XmlField;


/**
 * @author MP
 *
 * Utiliaire pour les fonctions Sql
 */
public class SqlUtils {

  private static Log log= LogFactory.getLog(SqlUtils.class);

  public static void AfficheSQLException(Line xmlline, String message, SQLException ex, ReportLine reporting) {
    //ORA-02290: violation de contraintes
    //ORA-00001: unicité
    //if(!ex.getMessage().startsWith("ORA-00001")) {
    String chaine= xmlline.getName() + " : " + "Ligne(" + reporting.getNumberLine() + ") : ";
    chaine += (ex.getMessage() + "." + System.getProperty("line.separator"));
    chaine += showLine(reporting.getNumberLine(), xmlline);
    log.error(message + System.getProperty("line.separator") + chaine, ex);

    reporting.ERROR_MESSAGE(xmlline.getName(), ex.getMessage());
    reporting.showLine();
  }
  //---------------------------------------------------------------------------

  public static String showLine(int nbLigne, Line xmlline) {
    String returnValue= "Ligne(" + nbLigne + ") :" + System.getProperty("line.separator");

    for (Iterator<Field> it= xmlline.getFields().iterator(); it.hasNext();) {
      XmlField field= (XmlField) it.next();

      try {
        switch (field.getDiscriminator()) {
          case XmlField.POSITION :
            returnValue
              += ("  "
                + field.getName()
                + "("
                + field.getPosition().getStartposition()
                + ", "
                + field.getPosition().getSize()
                + ") = \"");
            returnValue += field.getBuffer();
            returnValue += ("\"" + System.getProperty("line.separator"));

            break;

          case XmlField.CONSTANTE :
            returnValue += ("  " + field.getName() + "(cste) = \"");
            returnValue += field.getConstante().getValue();
            returnValue += ("\"" + System.getProperty("line.separator"));

            break;

          case XmlField.QUERY :
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
  //---------------------------------------------------------------------------
}
