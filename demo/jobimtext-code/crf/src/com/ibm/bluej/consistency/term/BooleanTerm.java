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

public final class BooleanTerm extends ValueTerm {
	private static final long serialVersionUID = 1L;

	public BooleanTerm(boolean value) {
		this.value = value;
	}
	
	public static final BooleanTerm TRUE = new BooleanTerm(true);
	public static final BooleanTerm FALSE = new BooleanTerm(false);
	
	public boolean value;
	
	public int hashCode() {
		return value ? 1 : 0;
	}
	//Similar implementation for all AtomicTerms
	public boolean equals(Object o) {
		if (o instanceof BooleanTerm) {
			return ((BooleanTerm)o).value == this.value;
		}
		if (o instanceof Function) {
			return this.equals(((Function)o).getValue());
		}
		return false;
	}
	
	public String toString() {
		return value ? "TRUE" : "FALSE";
	}

	public int compareTo(ATerm other) {
		if (!(other instanceof BooleanTerm)) {
			return ATerm.crossClassCompare(this, other);
		}
		if (this.value == ((BooleanTerm)other).value) {
			return 0;
		}
		return value ? 1 : -1;	
	}
}
