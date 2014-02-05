package org.jobjects.dbimp.tools;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author X113446
 * 
 */
public class ListMapTest {

  ListMap<String, Integer> toTest = new ListMap<String, Integer>();

  public ListMapTest() {
    toTest.put("AaA", 123);
    Random randomInt = new Random();
    for (int i = 0; i < 99; i++) {
      String key = RandomStringUtils.randomAlphabetic(randomInt.nextInt(20));
      int value = randomInt.nextInt(1000);
      toTest.put(key, value);
    }
    toTest.put("aAa", 321);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#hashCode()}.
   */
  @Test(groups = "MaSuite")
  public void testHashCode() {
    Assert.assertTrue(toTest.hashCode() > 0);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#size()}.
   */
  @Test(groups = "MaSuite")
  public void testSize() {
    Assert.assertTrue(toTest.size() > 0);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#isEmpty()}.
   */
  @Test(groups = "MaSuite")
  public void testIsEmpty() {
    Assert.assertTrue(!toTest.isEmpty());
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#containsKey(java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testContainsKey() {
    for (String key : toTest.keySet()) {
      Assert.assertTrue(toTest.containsKey(key));
    }
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#containsValue(java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testContainsValue() {
    for (Map.Entry<String, Integer> map : toTest.entrySet()) {
      Assert.assertTrue(toTest.containsValue(map.getValue()));
    }
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#get(java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testGet() {
    for (String key : toTest.keySet()) {
      Assert.assertTrue(toTest.get(key) != null);
    }
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#put(java.lang.Object, java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testPut() {
    Assert.assertTrue(true,"Deja fait au loading.");
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#remove(java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testRemove() {
    ListMap<String, Integer> my = new ListMap<String, Integer>();
    my.put("AaA", 123);
    my.put("aAa", 321);
    my.put("bBb", 456);
    my.remove("aAa");
    Assert.assertTrue(my.size() == 2);
    my.clear();
    Assert.assertTrue(my.size() == 0);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#putAll(java.util.Map)}.
   */
  @Test(groups = "MaSuite")
  public void testPutAll() {
    ListMap<String, Integer> my1 = new ListMap<String, Integer>();
    my1.put("AaA", 123);
    my1.put("aAa", 321);
    Assert.assertTrue(my1.size() == 2);
    ListMap<String, Integer> my2 = new ListMap<String, Integer>();
    my2.putAll(my1);
    Assert.assertTrue(my2.size() == 2);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#clear()}.
   */
  @Test(groups = "MaSuite")
  public void testClear() {
    ListMap<String, Integer> my = new ListMap<String, Integer>();
    my.put("AaA", 123);
    my.put("aAa", 321);
    Assert.assertTrue(my.size() == 2);
    my.clear();
    Assert.assertTrue(my.size() == 0);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#keySet()}.
   */
  @Test(groups = "MaSuite")
  public void testKeySet() {
    boolean flag = false;
    for (String key : toTest.keySet()) {
      flag = (key != null);
      break;
    }
    Assert.assertTrue(flag);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#values()}.
   */
  @Test(groups = "MaSuite")
  public void testValues() {
    Assert.assertTrue(toTest.values().size() > 0);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#entrySet()}.
   */
  @Test(groups = "MaSuite")
  public void testEntrySet() {
    boolean flag = false;
    for (Map.Entry<String, Integer> map : toTest.entrySet()) {
      flag = (map != null);
      break;
    }
    Assert.assertTrue(flag);
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#getFirstKey()}.
   */
  @Test(groups = "MaSuite")
  public void testGetFirstKey() {
    Assert.assertEquals("AaA", toTest.getFirstKey());
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#getLastKey()}.
   */
  @Test(groups = "MaSuite")
  public void testGetLastKey() {
    Assert.assertEquals("aAa", toTest.getLastKey());
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#getFirst()}.
   */
  @Test(groups = "MaSuite")
  public void testGetFirst() {
    Assert.assertEquals("AaA", toTest.getFirst().getKey());
    Assert.assertEquals(new Integer(123), toTest.getFirst().getValue());
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#getLast()}.
   */
  @Test(groups = "MaSuite")
  public void testGetLast() {
    Assert.assertEquals("aAa", toTest.getLast().getKey());
    Assert.assertEquals(new Integer(321), toTest.getLast().getValue());
  }

  /**
   * Test method for {@link org.jobjects.util.ListMap#equals(java.lang.Object)}.
   */
  @Test(groups = "MaSuite")
  public void testEqualsObject() {
    ListMap<String, Integer> my1 = new ListMap<String, Integer>();
    my1.put("AaA", 123);
    my1.put("aAa", 321);
    Assert.assertTrue(my1.size() == 2);
    ListMap<String, Integer> my2 = new ListMap<String, Integer>();
    my2.put("AaA", 123);
    my2.put("aAa", 321);
    Assert.assertTrue(my1.equals(my2));
  }

}
