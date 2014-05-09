package com.ibm.bluej.util.common;

import java.io.Serializable;

public class MutableInteger implements Cloneable, Comparable<MutableInteger>, Serializable {

  private static final long serialVersionUID = 1L;

  public int value;

  public MutableInteger() {
    value = 0;
  }

  public MutableInteger(int value) {
    this.value = value;
  }

  @Override
  public int compareTo(MutableInteger that) {
    return this.value == that.value ? 0 : this.value < that.value ? -1 : 1;
  }

}
