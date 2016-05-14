package org.jobjects.derby;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DerbyStart {

  private static Logger LOGGER = Logger.getLogger(DerbyStart.class.getName());

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    DerbySingleton.getInstance().start();
    if (!DerbySingleton.getInstance().isStarted()) {
      LOGGER.log(Level.SEVERE, "Derby is not started !");
    }
  }

  @Test(groups = "MaSuite")
  public void testStart() {
    LOGGER.log(Level.INFO, "Derby Stating");
  }

}
