package org.jobjects.dbimp.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class SQLDatatbaseTypeTest {

  private static Logger LOGGER = Logger.getLogger(SQLDatatbaseTypeTest.class.getName());

  @Test
  public void SQLDatatbaseType() {
    assertNotNull(SQLDatatbaseType.ORACLE);
    assertEquals("ORACLE", SQLDatatbaseType.ORACLE.name());
  }

  @Test
  public void getDriver() {
    assertEquals(SQLDatatbaseType.ORACLE.getDriver(), "oracle.jdbc.OracleDriver");
  }

  @Test
  public void getEnum() {
    LOGGER.log(Level.INFO, "Derby Stating");
    assertEquals(SQLDatatbaseType.getEnum("ORACLE"), SQLDatatbaseType.ORACLE);
    assertEquals(SQLDatatbaseType.getEnum("DB2AS400"), SQLDatatbaseType.DB2AS400);
    assertEquals(SQLDatatbaseType.getEnum("SQLSERVER"), SQLDatatbaseType.SQLSERVER);
    assertEquals(SQLDatatbaseType.getEnum("UNKNOW_DATABASE"), SQLDatatbaseType.UNKNOW_DATABASE);
    assertEquals(SQLDatatbaseType.getEnum(null), SQLDatatbaseType.UNKNOW_DATABASE);
    assertEquals(SQLDatatbaseType.getEnum("" + Math.random()), SQLDatatbaseType.UNKNOW_DATABASE);
  }

  @Test
  public void getType() {
    assertEquals(SQLDatatbaseType.getType("jdbc:oracle:thin:@<server>:1521:<instance>"), SQLDatatbaseType.ORACLE);
  }
}
