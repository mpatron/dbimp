package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlFieldTypesTest {

  @Test(groups = "MaSuite")
  public void XmlFieldTypes() {
    Assert.assertEquals(FieldFormatEnum.valueOfByType("integer"),FieldFormatEnum.INTEGER);
  }

  @Test(groups = "MaSuite")
  public void getTypeInt() {
    Assert.assertEquals(FieldFormatEnum.valueOfByType("integer"),FieldFormatEnum.INTEGER);
  }

  @Test(groups = "MaSuite")
  public void getTypeString() {
    Assert.assertEquals(FieldFormatEnum.valueOfByType("integer"),FieldFormatEnum.INTEGER);
  }
}
