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

import java.io.Serializable;

//NOTE: there are actually very few VarTerms, the only place that creates them is the formula parser
public class VarTerm extends ATerm implements Serializable {
	private static final long serialVersionUID = 1L;
	int varNum;
	private String varName;
	
	public VarTerm(String varName, int varNum) {
		this.varName = varName;
		this.varNum = varNum;
	}
	
	public boolean isGround(Binds binds) {
		return binds != null && binds.get(this) != null;
	}

	public ATerm ground(Binds binds, Updatable neededBy) {
		ATerm t = binds.get(this);
		//used for partial ground
		if (t == null) {
			return this;
		}
		//we need to add a function unroll when 
		//  neededBy != null && neededBy != NO_LINK && !t.isFunctionFree()        
		//this is the (only) place where we add a function unroll
		binds.addFunctionUnlink(t, neededBy);
		return t;
	}
	
	public boolean isFunctionFree() {
		return true;
		//throw new UnsupportedOperationException("Function free is defined only on ground terms");
	}
	
	public void bind(ATerm ground, Binds binds) {
		ATerm bound = binds.get(this);
		if (bound == null) {
			binds.set(this, ground);
		}
	}

	protected boolean matchesInner(ATerm ground, Binds binds) {
		ATerm bound = binds.get(this);
		if (bound == null) {
			binds.set(this, ground);
			return true;
		}
		return bound.equals(ground);
	}
	
	public String toString() {
		return varName != null ? varName : "$"+varNum;
	}

	public int compareTo(ATerm other) {
		if (!(other instanceof VarTerm)) {
			return ATerm.crossClassCompare(this, other);
		}
		return varNum - ((VarTerm)other).varNum;
	}
	
	public int valueHash() {
		throw new UnsupportedOperationException("VarTerm cannot be used in valueHash/valueEquals/valueClone");
	}
	public boolean valueEquals(ATerm t) {
		throw new UnsupportedOperationException("VarTerm cannot be used in valueHash/valueEquals/valueClone");
	}
	public ATerm valueClone() {
		throw new UnsupportedOperationException("VarTerm cannot be used in valueHash/valueEquals/valueClone");
	}
}
