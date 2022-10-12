package org.jobjects.derby;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class DerbyStop {

  private static Logger LOGGER = Logger.getLogger(DerbyStop.class.getName());

  /**
   * @throws java.lang.Exception
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception {
    DerbySingleton.getInstance().stop();
    if (DerbySingleton.getInstance().isStarted()) {
      LOGGER.log(Level.SEVERE, "Derby can not be stopped !");
    }
  }

  @Test
  public void testStop() {
    LOGGER.log(Level.INFO, "Derby Stop");
  }

}
