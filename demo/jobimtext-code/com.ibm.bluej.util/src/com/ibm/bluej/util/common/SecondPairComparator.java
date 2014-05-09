package com.ibm.bluej.util.common;

import java.io.*;
import java.util.*;

/**
 * orders Pairs based on their first element
 * @author partha
 *
 */
public class SecondPairComparator<T1, T2> implements Comparator<Pair<T1,T2>>, Serializable {

  private Comparator comparator;
  boolean reverse;

  public static boolean REVERSE = true;
  
	public static <S extends Pair, T extends List<S>> void sortR(T list) {
		SecondPairComparator rev = new SecondPairComparator();
		rev.setReverseOrdering();
		Collections.sort(list, rev);
	}
	
	public static <S extends Pair, T extends List<S>> void sort(T list) {
		Collections.sort(list, new SecondPairComparator());
	}
  
  /**
   * default constructor that assumes the second element is Comparable
   */
  public SecondPairComparator () {
   
  }
  
  public SecondPairComparator (boolean reverse) {
	   this.reverse = reverse;
  }
  
  /**
   * constructor that takes a Comparator where null means treat the elements as Comparable
   * @param comp
   */
  public SecondPairComparator (Comparator comparator) {
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
      return reverse?comparator.compare(p2.second, p1.second):comparator.compare(p1.second, p2.second);
    } else {
      return reverse?((Comparable) p2.second).compareTo(p1.second): ((Comparable) p1.second).compareTo(p2.second);  //null means treat the elements as Comparable
    }

  }
}