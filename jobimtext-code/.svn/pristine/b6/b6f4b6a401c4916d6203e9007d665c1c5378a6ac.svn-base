package com.ibm.bluej.util.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class NBest<T> extends PriorityQueue<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private int limit;

  /**
   * constructor that takes int limit
   * 
   * @param limit
   */
  public NBest(int limit) {
    super(limit);
    this.limit = limit;
  }

  /**
   * constructor that takes limit and Comparator
   * 
   * @param limit
   * @param comparator
   */
  public NBest(int limit, Comparator<T> comparator) {
    super(limit, comparator);
    this.limit = limit;
  }

  /**
   * @return the limit
   */
  public int getLimit() {
    return limit;
  }

  /**
   * makes sure limit is not exceeded, NBest should contain the <limit> elements that are greatest
   */
  @Override
  public boolean add(T o) {
    super.add(o);
    T removed = null;
    if (size() > limit) {
      removed = poll();
    }
    return o == removed ? false : true;
  }

  public T addRemove(T o) {
    super.add(o);
    T removed = null;
    if (size() > limit) {
      removed = poll();
    }
    return removed;
  }  
  
  /**
   * removes all elements and returns them in greatest to least order
   * 
   * @return list
   */
  public List<T> empty() {
    List<T> list = new ArrayList<T>();
    while (!isEmpty()) {
      list.add(remove());
    }
    Collections.reverse(list);
    return list;
  }
}
