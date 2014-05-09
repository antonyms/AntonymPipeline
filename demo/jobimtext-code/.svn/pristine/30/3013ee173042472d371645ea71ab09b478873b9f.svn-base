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

import java.util.HashMap;

public final class StringTerm extends ValueTerm {
	private static final long serialVersionUID = 1L;

	public static void clearCanonical() {
		interned = null;
	}
	
	private static HashMap<String, StringTerm> interned = null;
	public static StringTerm canonical(String value) {
		if (interned == null) {
			interned = new HashMap<String,StringTerm>();
		}
		StringTerm st = interned.get(value);
		if (st == null) {
			st = new StringTerm(value);
			interned.put(value, st);
		}
		return st;
	}
	
	public StringTerm(String value) {
		this.value = value;
	}
	
	public String value;
	
	public int hashCode() {
		return value.hashCode();
	}
	//Similar implementation for all AtomicTerms
	public boolean equals(Object o) {
		if (o instanceof Function) {
			return this.equals(((Function)o).getValue());
		}
		if (o instanceof StringTerm) {
			return ((StringTerm)o).value.equals(this.value);
		}
		return false;
	}
	public String toString() {
		return value;
	}
	
	public int compareTo(ATerm other) {
		if (!(other instanceof StringTerm)) {
			return ATerm.crossClassCompare(this, other);
		}
		return this.value.compareTo(((StringTerm)other).value);
	}
}
