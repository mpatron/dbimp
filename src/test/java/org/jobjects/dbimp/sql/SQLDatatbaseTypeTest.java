package org.jobjects.dbimp.sql;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SQLDatatbaseTypeTest {

  private static Logger LOGGER = Logger.getLogger(SQLDatatbaseTypeTest.class.getName());

  @Test(groups = "MaSuite")
  public void SQLDatatbaseType() {
    Assert.assertNotNull(SQLDatatbaseType.ORACLE);
    Assert.assertEquals("ORACLE", SQLDatatbaseType.ORACLE.name());
  }

  @Test(groups = "MaSuite")
  public void getDriver() {
    Assert.assertEquals(SQLDatatbaseType.ORACLE.getDriver(), "oracle.jdbc.OracleDriver");
  }

  @Test(groups = "MaSuite")
  public void getEnum() {
    LOGGER.log(Level.INFO, "Derby Stating");
    Assert.assertEquals(SQLDatatbaseType.getEnum("ORACLE"), SQLDatatbaseType.ORACLE);
    Assert.assertEquals(SQLDatatbaseType.getEnum("DB2AS400"), SQLDatatbaseType.DB2AS400);
    Assert.assertEquals(SQLDatatbaseType.getEnum("SQLSERVER"), SQLDatatbaseType.SQLSERVER);
    Assert.assertEquals(SQLDatatbaseType.getEnum("UNKNOW_DATABASE"), SQLDatatbaseType.UNKNOW_DATABASE);
    Assert.assertEquals(SQLDatatbaseType.getEnum(null), SQLDatatbaseType.UNKNOW_DATABASE);
    Assert.assertEquals(SQLDatatbaseType.getEnum("" + Math.random()), SQLDatatbaseType.UNKNOW_DATABASE);
  }

  @Test(groups = "MaSuite")
  public void getType() {
    Assert.assertEquals(SQLDatatbaseType.getType("jdbc:oracle:thin:@<server>:1521:<instance>"), SQLDatatbaseType.ORACLE);
  }
}
