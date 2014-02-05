package org.jobjects.dbimp.tools;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jobjects.dbimp.trigger.Field;

public class CollectionMap<E extends Field> implements Collection<E> {

  private LinkedList<E> list = new LinkedList<E>();

  /**
   * @param e
   * @return
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(E e) {
    return list.add(e);
  }

  /**
   * @param c
   * @return
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  /**
   * 
   * @see java.util.Collection#clear()
   */
  public void clear() {
    list.clear();
  }

  /**
   * @param o
   * @return
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object o) {
    return list.contains(o);
  }

  /**
   * @param c
   * @return
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  /**
   * @param o
   * @return
   * @see java.util.Collection#equals(java.lang.Object)
   */
  public boolean equals(Object object) {
    
    if (object == this) {
      return true;
    }
    if (!(object instanceof CollectionMap)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    CollectionMap<E> rhs = (CollectionMap<E>) object;
    boolean returnValue=true;
    for (Field field : rhs) {
      returnValue&=EqualsBuilder.reflectionEquals(this.get(field.getName()), field);
    }
    returnValue&=this.size()==rhs.size();
    return returnValue;
  }

  /**
   * @return
   * @see java.util.Collection#hashCode()
   */
  public int hashCode() {
    return list.hashCode();
  }

  /**
   * @return
   * @see java.util.Collection#isEmpty()
   */
  public boolean isEmpty() {
    return list.isEmpty();
  }

  /**
   * @return
   * @see java.util.Collection#iterator()
   */
  public Iterator<E> iterator() {
    return list.iterator();
  }

  /**
   * @param o
   * @return
   * @see java.util.Collection#remove(java.lang.Object)
   */
  public boolean remove(Object o) {
    return list.remove(o);
  }

  /**
   * @param c
   * @return
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  /**
   * @param c
   * @return
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  /**
   * @return
   * @see java.util.Collection#size()
   */
  public int size() {
    return list.size();
  }

  /**
   * @return
   * @see java.util.Collection#toArray()
   */
  public Object[] toArray() {
    return list.toArray();
  }

  /**
   * @param a
   * @return
   * @see java.util.Collection#toArray(T[])
   */
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  public E get(String key) {
    E returnValue=null;
    for (E item : list) {
      System.out.println(item.getName());
      if(key.equals(item.getName())) {
        returnValue=item;
        break;
      }
    }
    return returnValue;
  }
  
  public E getFirst() {
    return list.getFirst();   
  }
  
  public E getLast() {
    return list.getLast();   
  }
  
}
