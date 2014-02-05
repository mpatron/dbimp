/**
 * 
 */
package org.jobjects.dbimp.trigger;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jobjects.dbimp.report.ReportTrigger;

/**
 * @author Mickael
 * 
 */
public class MyTrigger implements Trigger {

  private Logger log = Logger.getLogger(getClass().getName());

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#beforeAction(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public void beforeAction(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "beforeAction : " + nbLigne);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#afterAction(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public void afterAction(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "afterAction : " + nbLigne);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#beforeInsert(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public boolean beforeInsert(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "beforeInsert : " + nbLigne);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#afterInsert(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public void afterInsert(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "beforeInsert : " + nbLigne);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#beforeUpdate(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public boolean beforeUpdate(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "beforeUpdate : " + nbLigne);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#afterUpdate(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public void afterUpdate(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "afterUpdate : " + nbLigne);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#beforeDelete(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public boolean beforeDelete(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "beforeDelete : " + nbLigne);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jobjects.dbimp.trigger.Trigger#afterDelete(java.sql.Connection,
   * int, org.jobjects.dbimp.report.ReportTrigger,
   * org.jobjects.dbimp.trigger.Line)
   */
  public void afterDelete(Connection connection, int nbLigne,
      ReportTrigger report, Line line) {
    log.log(Level.INFO, "afterDelete : " + nbLigne);
  }

}
