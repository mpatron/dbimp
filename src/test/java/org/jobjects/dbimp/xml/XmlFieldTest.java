package org.jobjects.dbimp.xml;

import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlFieldTest {

  @Test
  public void testCompareTo() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    XmlField a1= new XmlField("aa", FieldFormatEnum.STRING);
    XmlField b = new XmlField("bb", FieldFormatEnum.STRING);
    Assert.assertEquals(a.compareTo(a), 0);
    Assert.assertEquals(a.compareTo(a1), 0);
    Assert.assertTrue(a.compareTo(b) < 0);
  }

  @Test
  public void testEquals() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    XmlField a1= new XmlField("aa", FieldFormatEnum.STRING);
    Assert.assertTrue(a.equals(a1));
  }

  @Test
  public void testHashCode() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    Assert.assertTrue(a.hashCode()!=0);
  }

  @Test
  public void testToString() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    Assert.assertTrue(a.toString().length()>0);
  }
}
