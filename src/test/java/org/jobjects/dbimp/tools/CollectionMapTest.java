package org.jobjects.dbimp.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.jobjects.dbimp.trigger.Field;
import org.jobjects.dbimp.trigger.FieldFormatEnum;
import org.jobjects.dbimp.xml.XmlField;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CollectionMapTest {
  
  CollectionMap<XmlField> toTest = new CollectionMap<XmlField>();
  
  public CollectionMapTest() {
    toTest.add(new XmlField("AaA", FieldFormatEnum.STRING));
    Random randomInt = new Random();
    for (int i = 0; i < 99; i++) {
      String key = RandomStringUtils.randomAlphabetic(randomInt.nextInt(20));
      //int value = randomInt.nextInt(1000);
      toTest.add(new XmlField(key, FieldFormatEnum.STRING));
    }
    toTest.add(new XmlField("aAa", FieldFormatEnum.STRING));
  }


  @Test(groups = "MaSuite")
  public void testAdd() {
    CollectionMap<XmlField> my = new CollectionMap<XmlField>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    Assert.assertTrue(my.add(item1));
    Assert.assertTrue(my.size() == 1);
  }

  @Test(groups = "MaSuite")
  public void testAddAll() {
    CollectionMap<XmlField> my1 = new CollectionMap<XmlField>();
    CollectionMap<XmlField> my = new CollectionMap<XmlField>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item2=new XmlField("aAa", FieldFormatEnum.STRING);
    XmlField item3=new XmlField("bBb", FieldFormatEnum.STRING);
    my.add(item1);
    my.add(item2);
    my.add(item3);
    my1.addAll(my);
    Assert.assertTrue(my1.size() == 3);
  }

  @Test(groups = "MaSuite")
  public void testClear() {
    CollectionMap<XmlField> my = new CollectionMap<XmlField>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item2=new XmlField("aAa", FieldFormatEnum.STRING);
    XmlField item3=new XmlField("bBb", FieldFormatEnum.STRING);
    my.add(item1);
    my.add(item2);
    my.add(item3);
    my.clear();
    Assert.assertTrue(my.size() == 0);
  }

  @Test(groups = "MaSuite")
  public void testContains() {
    XmlField item=new XmlField("AaA", FieldFormatEnum.STRING);
    Assert.assertTrue(toTest.contains(item));
  }

  @Test(groups = "MaSuite")
  public void testContainsAll() {
    CollectionMap<XmlField> my1 = new CollectionMap<XmlField>();
    XmlField item11=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item12=new XmlField("aAa", FieldFormatEnum.STRING);
    my1.add(item11);
    my1.add(item12);
    CollectionMap<XmlField> my2 = new CollectionMap<XmlField>();
    XmlField item21=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item22=new XmlField("aAa", FieldFormatEnum.STRING);
    my1.add(item21);
    my1.add(item22);
    Assert.assertTrue(my1.containsAll(my2));
  }

  @Test(groups = "MaSuite")
  public void testEquals() {
    CollectionMap<XmlField> my1 = new CollectionMap<XmlField>();
    XmlField item11=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item12=new XmlField("aAa", FieldFormatEnum.STRING);
    my1.add(item11);
    my1.add(item12);
    CollectionMap<XmlField> my2 = new CollectionMap<XmlField>();
    XmlField item21=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item22=new XmlField("aAa", FieldFormatEnum.STRING);
    my2.add(item21);
    my2.add(item22);
    Assert.assertTrue(my1.equals(my2));
  }

  @Test(groups = "MaSuite")
  public void testGet() {
    Assert.assertEquals(toTest.get("AaA").getName(), "AaA");
  }

  @Test(groups = "MaSuite")
  public void testGetFirst() {
    Assert.assertEquals(toTest.getFirst().getName(), "AaA");
  }

  @Test(groups = "MaSuite")
  public void testGetLast() {
    Assert.assertEquals(toTest.getLast().getName(), "aAa");
  }

  @Test(groups = "MaSuite")
  public void testHashCode() {
    Assert.assertTrue(toTest.hashCode()!=0);
  }

  @Test(groups = "MaSuite")
  public void testIsEmpty() {
    Assert.assertTrue(!toTest.isEmpty());
  }

  @Test(groups = "MaSuite")
  public void testIterator() {
    Assert.assertTrue(toTest.iterator().hasNext());
  }

  @Test(groups = "MaSuite")
  public void testRemove() {
    CollectionMap<XmlField> my = new CollectionMap<XmlField>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item2=new XmlField("aAa", FieldFormatEnum.STRING);
    XmlField item3=new XmlField("bBb", FieldFormatEnum.STRING);
    my.add(item1);
    my.add(item2);
    my.add(item3);
    my.remove(item2);
    Assert.assertTrue(my.size() == 2);
    my.clear();
    Assert.assertTrue(my.size() == 0);
  }

  @Test(groups = "MaSuite")
  public void testRemoveAll() {
    CollectionMap<Field> my = new CollectionMap<Field>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item2=new XmlField("aAa", FieldFormatEnum.STRING);
    XmlField item3=new XmlField("bBb", FieldFormatEnum.STRING);
    my.add(item1);
    my.add(item2);
    my.add(item3);
    Collection<Field> c = new ArrayList<Field>();
    c.add(item2);
    my.removeAll(c);
    Assert.assertTrue(my.size() == 2);
  }

  @Test(groups = "MaSuite")
  public void testRetainAll() {
    CollectionMap<Field> my = new CollectionMap<Field>();
    XmlField item1=new XmlField("AaA", FieldFormatEnum.STRING);
    XmlField item2=new XmlField("aAa", FieldFormatEnum.STRING);
    XmlField item3=new XmlField("bBb", FieldFormatEnum.STRING);
    my.add(item1);
    my.add(item2);
    my.add(item3);
    Collection<Field> c = new ArrayList<Field>();
    c.add(item2);
    my.retainAll(c);
    Assert.assertTrue(my.size() == 1);
  }

  @Test(groups = "MaSuite")
  public void testSize() {
    Assert.assertEquals(toTest.size(), 101);
  }

  @Test(groups = "MaSuite")
  public void testToArray() {
    Assert.assertTrue(toTest.toArray() != null);
  }

  @Test(groups = "MaSuite")
  public void testTArrayT() {
    Assert.assertTrue(toTest.toArray(new Field[toTest.size()]).length == toTest.size());
  }
}
