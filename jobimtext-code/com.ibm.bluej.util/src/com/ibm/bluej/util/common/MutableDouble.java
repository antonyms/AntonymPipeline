package com.ibm.bluej.util.common;

import java.io.Serializable;
import java.util.*;


public class MutableDouble implements Cloneable, Comparable<MutableDouble>, Serializable {

  private static final long serialVersionUID = 1L;

  public double value;

  public MutableDouble() {
    value = 0;
  }

  public MutableDouble(double value) {
    this.value = value;
  }

  @Override
  public int compareTo(MutableDouble that) {
    return this.value == that.value ? 0 : this.value < that.value ? -1 : 1;
  }
  
  @Override
  public String toString() {
    return String.valueOf(this.value);
  }
  
	public static class AbsValueComparator implements Comparator<MutableDouble> {
		public int compare(MutableDouble o1, MutableDouble o2) {
			if (o1 == null || o2 == null) return 0;
			return (int)Math.signum(Math.abs(o1.value) - Math.abs(o2.value));
		}
		
	}  
}
