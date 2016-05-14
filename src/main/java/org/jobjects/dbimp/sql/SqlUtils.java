package org.jobjects.dbimp.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
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
    chaine += (ex.getMessage() + "." + System.lineSeparator());
    chaine += showLine(reporting.getNumberLine(), xmlline);
    log.log(Level.SEVERE, message + System.lineSeparator() + chaine, ex);

    reporting.ERROR_MESSAGE(xmlline.getName(), ex.getMessage());
    reporting.showLine();
  }
  // ---------------------------------------------------------------------------

  public static String showLine(int nbLigne, Line xmlline) {
    String returnValue = "Ligne(" + nbLigne + ") :" + System.lineSeparator();

    for (Field field : xmlline.getFields()) {
      try {
        switch (field.getDiscriminator()) {
        case POSITION:
          returnValue += ("  " + field.getName() + "(" + field.getPosition().getStartposition() + ", " + field.getPosition().getSize() + ") = \"");
          returnValue += field.getBuffer();
          returnValue += ("\"" + System.lineSeparator());
          break;

        case CONSTANTE:
          returnValue += ("  " + field.getName() + "(cste) = \"");
          returnValue += field.getConstante().getValue();
          returnValue += ("\"" + System.lineSeparator());
          break;

        case QUERY:
          returnValue += ("  " + field.getName() + "(query) = \"");
          returnValue += field.getQuery().getSql();
          returnValue += ("\"" + System.lineSeparator());
          break;
        }
      } catch (Exception exinternal) {
        returnValue += ("  " + field.getName() + " (unkown error)" + System.lineSeparator());
      }
    }

    return returnValue;
  }
  // ---------------------------------------------------------------------------

  /**
   * Affiche sur la sortie standard le contenu du RecordSet.
   * @param rs est le RecordSet à Afficher
   * @return rien.
   */
  public static void Affiche(ResultSet rs)
  {
    try
    {
      Vector<String[]> lignes = new Vector<String[]>();

      ResultSetMetaData rsmd = rs.getMetaData();
      int colcount = rsmd.getColumnCount();

      String[] chaine = new String[colcount];
      int[] colsize = new int[colcount];

      for(int i=1; i<=colcount; i++)
      {
        chaine[i-1] = rsmd.getColumnName(i);
        colsize[i-1] =  chaine[i-1].length();
      }
      lignes.add(chaine);


      while(rs.next())
      {
        chaine = new String[colcount];
        for(int i=1; i<=colcount; i++)
        {
          chaine[i-1] = rs.getString(i);
          if(chaine[i-1] != null)
          {
            if(colsize[i-1] < chaine[i-1].length())
            {
              colsize[i-1] =  chaine[i-1].length();
            }
          }
        }
        lignes.add(chaine);
      }
      rs.close();

      int diff;
      for(int i=0; i<lignes.size(); i++)
      {
        chaine = (String[])lignes.get(i);
        for(int j=0; j<chaine.length; j++)
        {
          if(chaine[j]==null) chaine[j] = "Null";
          diff = colsize[j] - chaine[j].length();
          System.out.print(chaine[j] + " ");
          for(int k=0; k<diff; k++) System.out.print(" ");
          System.out.print(" ");
        }
        System.out.println("");
      }
    }
    catch(Exception e) { e.printStackTrace(System.err); }
  }
  //------------------------------------------------------------------------------
}
