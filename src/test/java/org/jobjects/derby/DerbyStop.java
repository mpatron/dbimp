package org.jobjects.derby;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class DerbyStop {

  private static Logger LOGGER = Logger.getLogger(DerbyStop.class.getName());

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    DerbySingleton.getInstance().stop();
    if (DerbySingleton.getInstance().isStarted()) {
      LOGGER.log(Level.SEVERE, "Derby can not be stopped !");
    }
  }

  @Test(groups = "MaSuite")
  public void testStop() {
    LOGGER.log(Level.INFO, "Derby Stop");
  }

}
