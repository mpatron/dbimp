package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldTypeEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlFieldTest {

  @Test
  public void testCompareTo() {
    XmlField a = new XmlField("aa", FieldTypeEnum.STRING);
    XmlField a1= new XmlField("aa", FieldTypeEnum.STRING);
    XmlField b = new XmlField("bb", FieldTypeEnum.STRING);
    Assert.assertEquals(a.compareTo(a), 0);
    Assert.assertEquals(a.compareTo(a1), 0);
    Assert.assertTrue(a.compareTo(b) < 0);
  }

  @Test
  public void testEquals() {
    XmlField a = new XmlField("aa", FieldTypeEnum.STRING);
    XmlField a1= new XmlField("aa", FieldTypeEnum.STRING);
    Assert.assertTrue(a.equals(a1));
  }

  @Test
  public void testHashCode() {
    XmlField a = new XmlField("aa", FieldTypeEnum.STRING);
    Assert.assertTrue(a.hashCode()!=0);
  }

  @Test
  public void testToString() {
    XmlField a = new XmlField("aa", FieldTypeEnum.STRING);
    Assert.assertTrue(a.toString().length()>0);
  }
}
