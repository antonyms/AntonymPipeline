package com.ibm.bluej.util.common;

import java.io.*;
import java.util.Comparator;

/**
 * orders Pairs based on their first element
 * @author partha
 *
 */
public class FirstPairComparator implements Comparator<Pair>, Serializable {

  private Comparator comparator;
  boolean reverse;
  
  /**
   * default constructor that assumes the first element is Comparable
   */
  public FirstPairComparator() {
   
  }
  /**
   * constructor that takes a Comparator where null means treat the elements as Comparable
   * @param comp
   */
  public FirstPairComparator(Comparator comparator) {
    this.comparator = comparator;
  }
  
  /**
   * reverses the order
   */
  public void setReverseOrdering() {
    this.reverse = true;
  }
  
  @Override
  public int compare(Pair p1, Pair p2) {
    if (comparator != null) {
      return reverse?comparator.compare(p2.first, p1.first):comparator.compare(p1.first, p2.first);
    } else {
      return reverse?((Comparable) p2.first).compareTo(p1.first): ((Comparable) p1.first).compareTo(p2.first);  //null means treat the elements as Comparable
    }
  }
}
