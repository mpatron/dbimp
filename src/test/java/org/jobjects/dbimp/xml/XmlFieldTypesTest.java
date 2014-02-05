package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldTypeEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlFieldTypesTest {

  @Test(groups = "MaSuite")
  public void XmlFieldTypes() {
    Assert.assertEquals(FieldTypeEnum.valueOfByType("integer"),FieldTypeEnum.INTEGER);
  }

  @Test(groups = "MaSuite")
  public void getTypeInt() {
    Assert.assertEquals(FieldTypeEnum.valueOfByType("integer"),FieldTypeEnum.INTEGER);
  }

  @Test(groups = "MaSuite")
  public void getTypeString() {
    Assert.assertEquals(FieldTypeEnum.valueOfByType("integer"),FieldTypeEnum.INTEGER);
  }
}
