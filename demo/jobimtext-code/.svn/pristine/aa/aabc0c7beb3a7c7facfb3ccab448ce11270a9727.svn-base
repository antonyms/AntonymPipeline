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


public abstract class CompositeTerm extends ATerm {
	public ATerm[] parts;
	public static final ATerm[] EMPTY_TERM = new ATerm[0];
	
	//CONSIDER: isFFandGround()
	
	public boolean isFunctionFree() {
		for (ATerm p : parts) {
			if (!p.isFunctionFree()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isGround(Binds binds) {
		for (ATerm p : parts) {
			if (!p.isGround(binds)) {
				return false;
			}
		}
		return true;
	}
	
	//used for GC'ing the function forest
	/*
	public void markRetainDown() {
		for (Term t : parts) {
			if (t instanceof CompositeTerm) {
				((CompositeTerm)t).markRetainDown();
			}
		}
	}
	*/
	public int compareTo(ATerm other) {
		if (this.getClass() != other.getClass()) {
			return ATerm.crossClassCompare(this, other);
		}
		CompositeTerm c = (CompositeTerm)other;
		if (this.parts.length != c.parts.length) {
			return this.parts.length > c.parts.length ? 1 : -1;
		}
		for (int i = 0; i < parts.length; ++i) {
			int ci = parts[i].compareTo(c.parts[i]);
			if (ci != 0) {
				return ci;
			}
		}
		return 0;
	}
}
