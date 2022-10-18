/**
 * 
 */
package org.jobjects.dbimp.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.jobjects.tools.JObjectsLogFormatter;
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
    try {

      Object value = new Double(1.55d);
      Double i_value = 1.54999999d;

      LOGGER.log(Level.INFO, String.format("value=%s i_value=%s", value, i_value));
      assertEquals((Double)value, (Double)1.55d);
      assertEquals((Double)value, (Double)1.55d);
      assertTrue(Precision.equals(i_value, (Double) value, 0.0001d));

      LOGGER.log(Level.INFO, String.format("Math.round(%s)=%s", i_value, Math.round(i_value)));
      LOGGER.log(Level.INFO, String.format("Math.ceil(%s)=%s", i_value,Math.ceil(i_value)));
      LOGGER.log(Level.INFO, String.format("Math.floor(%s)=%s", i_value,Math.floor(i_value)));
      LOGGER.log(Level.INFO, String.format("Double.isInfinite(%s)=%s", i_value,Double.isInfinite(i_value)));

      Double j_value = 1.24999999d;
      LOGGER.log(Level.INFO, String.format("Math.round(%s)=%s", j_value, Math.round(j_value)));
      LOGGER.log(Level.INFO, String.format("Math.ceil(%s)=%s", j_value,Math.ceil(j_value)));
      LOGGER.log(Level.INFO, String.format("Math.floor(%s)=%s", j_value,Math.floor(j_value)));
      LOGGER.log(Level.INFO, String.format("Double.isInfinite(%s)=%s", j_value,Double.isInfinite(j_value)));

      Double k_value = 1.99999999d;
      LOGGER.log(Level.INFO, String.format("Math.round(%s)=%s", k_value, Math.round(k_value)));
      LOGGER.log(Level.INFO, String.format("Math.ceil(%s)=%s", k_value,Math.ceil(k_value)));
      LOGGER.log(Level.INFO, String.format("Math.floor(%s)=%s", k_value,Math.floor(k_value)));
      LOGGER.log(Level.INFO, String.format("Double.isInfinite(%s)=%s", k_value,Double.isInfinite(k_value)));

      double d1 = 0;
      for (int i = 1; i <= 8; i++) {
        d1 += 0.1;
      }
      double d2 = 0.1 * 8;
      double epsilon = 0.000001d;
      assertTrue(Math.abs(d1 - d2) < epsilon);
      assertTrue(Precision.equals(d1, d2, epsilon));

      Double doubles = 1.99999999d;
      int integer = doubles.intValue();
      LOGGER.log(Level.INFO, String.format("doubles.intValue(%s)=%s", doubles,integer));
      
      i_value = 1.24999999d;
      assertTrue((Math.ceil(i_value) == Math.floor(i_value)) && !Double.isInfinite(i_value));

      i_value = 1.54999999d;
      assertTrue((Math.ceil(i_value) == Math.floor(i_value)) && !Double.isInfinite(i_value));      

    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
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
