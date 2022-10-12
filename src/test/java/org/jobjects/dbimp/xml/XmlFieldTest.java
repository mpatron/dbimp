package org.jobjects.dbimp.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.junit.jupiter.api.Test;

public class XmlFieldTest {

  @Test
  public void testCompareTo() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    XmlField a1 = new XmlField("aa", FieldFormatEnum.STRING);
    XmlField b = new XmlField("bb", FieldFormatEnum.STRING);
    assertEquals(a.compareTo(a), 0);
    assertEquals(a.compareTo(a1), 0);
    assertTrue(a.compareTo(b) < 0);
  }

  @Test
  public void testEquals() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    XmlField a1 = new XmlField("aa", FieldFormatEnum.STRING);
    assertTrue(a.equals(a1));
  }

  @Test
  public void testHashCode() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    assertTrue(a.hashCode() != 0);
  }

  @Test
  public void testToString() {
    XmlField a = new XmlField("aa", FieldFormatEnum.STRING);
    assertTrue(a.toString().length() > 0);
  }
}
