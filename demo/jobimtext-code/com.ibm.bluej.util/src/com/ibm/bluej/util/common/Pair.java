package com.ibm.bluej.util.common;

import java.io.Serializable;

public class Pair <T1, T2> implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  
  //two public members, first and second
  public T1 first;
  public T2 second;
  
  public Pair() {
  }
  public static <F,S> Pair<F,S> make(F f, S s) {
      return new Pair<F,S>(f,s);
}
  public Pair(T1 first, T2 second) {
    super();
    this.first = first;
    this.second = second;
  }
  
  public T1 getFirst() {
    return first;
  }

  public void setFirst(T1 first) {
    this.first = first;
  }

  public T2 getSecond() {
    return second;
  }

  public void setSecond(T2 second) {
    this.second = second;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Pair)) {
      return false;
    }
    Pair other = (Pair) obj;
    if (first == null) {
      if (other.first != null) {
        return false;
      }
    } else if (!first.equals(other.first)) {
      return false;
    }
    if (second == null) {
      if (other.second != null) {
        return false;
      }
    } else if (!second.equals(other.second)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return first.toString()+" "+second.toString();
  }
  
  public static <T1,T2> Pair<T1,T2> of(T1 f, T2 s) {
	  return new Pair<T1,T2>(f,s);
  }
}
