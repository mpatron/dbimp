/*
 * Trigger.java
 *
 * Created on 12 octobre 2002, 23:51
 */
package org.jobjects.dbimp.trigger;

import java.sql.Connection;

import org.jobjects.dbimp.report.ReportTrigger;

/**
 * Interface à implémenté afin de pourvoir executer des actions pendant les
 * traitements.
 * 
 * @author Mickaël PATRON
 * @version 2.0
 */
public interface Trigger {

  /**
   * L'action sera déclanché avant la lecture de la première ligne. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   * @return boolean Si c'est faux, le moteur ne traite pas le fichier.
   */
  public void beforeAction(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché après la lecture de la dernière ligne. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   */
  public void afterAction(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché avant l'insertion de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   * @return boolean Si c'est faux, le moteur ne traite pas la ligne.
   */
  public boolean beforeInsert(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché après l'insertion de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   */
  public void afterInsert(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché avant la mise à jour de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   * @return boolean Si c'est faux, le moteur ne traite pas la ligne.
   */
  public boolean beforeUpdate(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché après la mise à jour de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   */
  public void afterUpdate(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché avant la suppression de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   * @return boolean Si c'est faux, le moteur ne traite pas la ligne.
   */
  public boolean beforeDelete(Connection connection, int nbLigne, ReportTrigger report, Line line);

  /**
   * L'action sera déclanché après la suppression de la ligne en cours. Utiliser
   * field.buffer afin d'avoir la valeur sous la forme d'une chaine.
   * 
   * @param connection
   *          Connection en cour.
   * @param nbLigne
   *          Numéro de ligne du fichier en cours de lecture.
   * @param report
   *          Rapport d'importation.
   * @param line
   *          Permet d'obtenir la liste des champs en cours de lecture.
   */
  public void afterDelete(Connection connection, int nbLigne, ReportTrigger report, Line line);
}