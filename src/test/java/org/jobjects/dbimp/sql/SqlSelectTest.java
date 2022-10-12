/**
 * 
 */
package org.jobjects.dbimp.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.util.Precision;
import org.jobjects.dbimp.tools.log.JObjectsLogFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author micka
 *
 */
class SqlSelectTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @BeforeAll
  static void beforeAll() {
    JObjectsLogFormatter.initializeLogging();
  }

  @Test
  void testDoubleAsInteger() {
    Object value = new Double(1.25d);
    Double i_value = 1.24999999d;

    LOGGER.log(Level.INFO, String.format("value=%s i_value=%s", value, i_value));
    assertEquals(value, 1.25d);
    assertEquals(value, 1.25d);
    assertTrue(Precision.equals(i_value, (Double) value, 0.0001d));

    LOGGER.log(Level.INFO, String.format("Math.ceil(i_value)=%s", Math.ceil(i_value)));
    LOGGER.log(Level.INFO, String.format("Math.floor(i_value)=%s", Math.floor(i_value)));
    LOGGER.log(Level.INFO, String.format("Double.isInfinite(i_value)=%s", Double.isInfinite(i_value)));

    assertTrue((Math.ceil(i_value) == Math.floor(i_value)) && !Double.isInfinite(i_value));
//    Math.ceil(returnValue)
  }

  /**
   * Test method for {@link org.jobjects.dbimp.sql.SqlSelect#createSQL()}.
   */
  @Test @Disabled
  void testCreateSQL() {
    fail("Not yet implemented");
  }

  /**
   * Test method for
   * {@link org.jobjects.dbimp.sql.SqlSelect#SqlSelect(java.sql.Connection, java.lang.String, boolean, org.jobjects.dbimp.trigger.Line, org.jobjects.dbimp.report.ReportTypeLine)}.
   */
  @Test @Disabled
  void testSqlSelect() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link org.jobjects.dbimp.sql.SqlSelect#execute(int)}.
   */
  @Test @Disabled
  void testExecute() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link org.jobjects.dbimp.sql.SqlSelect#getCount()}.
   */
  @Test @Disabled
  void testGetCount() {
    fail("Not yet implemented");
  }

}
