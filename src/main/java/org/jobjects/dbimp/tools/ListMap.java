package org.jobjects.dbimp.tools;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ListMap<K,V> implements Map<K,V> {
  private Map<K,V> listmap=new LinkedHashMap<K,V>();
  private Deque<Map.Entry<K,V>> list=new LinkedList<Map.Entry<K,V>>();

  public int size() {
    return listmap.size();
  }

  public boolean isEmpty() {
    return listmap.isEmpty();
  }

  public boolean containsKey(Object key) {
    return listmap.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return listmap.containsValue(value);
  }

  public V get(Object key) {
    return listmap.get(key);
  }

  @SuppressWarnings({
      "unchecked",
      "rawtypes" })
  public V put(K key, V value) {
    list.add(new AbstractMap.SimpleEntry(key,value));
    return listmap.put(key, value);
  }

  public V remove(Object key) {
    if(null!=key) {
      for (Map.Entry<K,V> entry : list) {
        if(key.equals(entry.getKey())) {
          list.remove(entry);
          break;
        }
      }
    }
    return listmap.remove(key);
  }

  @SuppressWarnings("unchecked")
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K,? extends V> entry : m.entrySet()) {
      list.add((java.util.Map.Entry<K, V>) entry);
    }
    //list.addAll();
    listmap.putAll(m);
  }

  public void clear() {
    list.clear();
    listmap.clear();
  }

  public Set<K> keySet() {
    return listmap.keySet();
  }

  public Collection<V> values() {
    return listmap.values();
  }

  public Set<java.util.Map.Entry<K, V>> entrySet() {
    return listmap.entrySet();
  }

  public K getFirstKey() {
    return list.getFirst().getKey();
  }

  public K getLastKey() {
    return list.getLast().getKey();
  }

  public Map.Entry<K,V> getFirst() {
    return list.getFirst();
  }

  public Map.Entry<K,V> getLast() {
    return list.getLast();
  }
  
  
  @Override
  public boolean equals(Object o) {
    return listmap.equals(o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(listmap);
  }
  
}
