/*
Copyright (c) 2012 IBM Corp.

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

import java.io.Serializable;

import com.ibm.bluej.util.common.Lang;

public abstract class ValueTerm extends ATerm implements Serializable,Cloneable {

	//NOTE: AtomicTerms must define hashCode and equals
	private static final long serialVersionUID = 1L;

	public ATerm ground(Binds binds, Updatable neededBy) {
		return this;
	}

	public static ValueTerm basicFromString(String p) {
		if (p.startsWith("\"") && p.endsWith("\"")) {
			return new StringTerm(p.substring(1, p.length()-1));
		}
		if (Lang.isDouble(p)) {
			return new NumberTerm(Double.parseDouble(p));
		} 
		return new StringTerm(p);
	}
	
	public boolean isGround(Binds binds) {
		return true;
	}
	
	protected final void bind(ATerm ground, Binds binds) {}
	protected boolean matchesInner(ATerm ground, Binds binds) {
		//a common case is when the two instances are identical
		return this == ground || this.equals(ground);
	}
	
	public int valueHash() {
		return hashCode();
	}
	public boolean valueEquals(ATerm t) {
		if (t instanceof Function) {
			t = ((Function)t).getValue();
		}
		return equals(t);
	}
	
	//don't usually need to clone, only when this is the value of a function
	// and Function will take care of that
	public ATerm valueClone() {	
		return this;
	}
	
	public ATerm clone() {
		try {
			return (ATerm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	//is there any point to overriding this??
	/*
	public AtomicTerm clone() {
		try {
			return (AtomicTerm)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	*/
}
