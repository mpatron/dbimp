package org.jobjects.dbimp.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.junit.jupiter.api.Test;

public class XmlFieldTypesTest {

  @Test
  public void XmlFieldTypes() {
    assertEquals(FieldFormatEnum.valueOfByType("integer"), FieldFormatEnum.INTEGER);
  }

  @Test
  public void getTypeInt() {
    assertEquals(FieldFormatEnum.valueOfByType("integer"), FieldFormatEnum.INTEGER);
  }

  @Test
  public void getTypeString() {
    assertEquals(FieldFormatEnum.valueOfByType("integer"), FieldFormatEnum.INTEGER);
  }
}
