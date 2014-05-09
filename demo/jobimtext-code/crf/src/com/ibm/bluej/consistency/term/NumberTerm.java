/*
Copyright (c) 2012 IBM Corp. and Michael Glass

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.consistency.term;

import java.util.ArrayList;
import java.util.HashMap;

public final class NumberTerm extends ValueTerm {
	private static final long serialVersionUID = 1L;

	private static HashMap<Double, NumberTerm> interned = null;
	public static void clearCanonical() {
		interned = null;
	}
	
	private static ArrayList<NumberTerm> indexes = null;
	public static NumberTerm canonical(int value) {
		//use ArrayList for small numbers
		if (value >= 0 && value < 500) {
			if (indexes == null || indexes.size() <= value) {
				if (indexes == null) indexes = new ArrayList<NumberTerm>();
				while (indexes.size() <= value) {
					indexes.add(new NumberTerm(indexes.size()));
				}
			}
			return indexes.get(value);
		}
		return canonical((double)value);
	}
	public static NumberTerm canonical(Double value) {
		if (interned == null) {
			interned = new HashMap<Double,NumberTerm>();
		}
		NumberTerm st = interned.get(value);
		if (st == null) {
			st = new NumberTerm(value);
			interned.put(value, st);
		}
		return st;
	}
	
	public NumberTerm(double value) {
		this.value = value;
	}
	
	public double value;
	
	public int hashCode() {
		long v = Double.doubleToLongBits(value);
		return (int)(v^(v>>>32));
	}
	//Similar implementation for all AtomicTerms
	public boolean equals(Object o) {
		if (o instanceof Function) {
			return this.equals(((Function)o).getValue());
		}
		if (o instanceof NumberTerm) {
			return ((NumberTerm)o).value == this.value;
		}
		return false;
	}
	
	public String toString() {
		return ""+value;
	}
	
	public int compareTo(ATerm other) {
		if (!(other instanceof NumberTerm)) {
			return ATerm.crossClassCompare(this, other);
		}
		NumberTerm n = (NumberTerm)other;
		if (value == n.value) {
			return 0;
		}
		return value > n.value ? 1 : -1;
	}
}
