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

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.rl.GroundingPlan;

public abstract class CollectionDefinition extends ATerm {
	//[p(x,y) : v(u,y) ^ t(u,x)]
	ATerm created; //the p(x,y) or x or 1
	ATerm[] condition; //the v(u,y) ^ t(u,x)
	boolean[] positive;
	GroundingPlan groundingPlan;
	CRFState crfState;
	
	//set/bag definitions can only appear in the create of a define formula
	public boolean isGround(Binds binds) {
		//really should never be called
		return false;
	}
	
	public boolean isFunctionFree() {
		throw new UnsupportedOperationException("Not implemented - do we need this?");
	}

	protected void bind(ATerm ground, Binds binds) {
		throw new UnsupportedOperationException("Will implement bags of sets later.");
	}
	protected boolean matchesInner(ATerm ground, Binds binds) {
		throw new UnsupportedOperationException("Will implement bags of sets later.");
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append((this instanceof SetDefinition) ? "{" : "[");
		buf.append(created.toString());
		buf.append(" :: ");
		for (int i = 0; i < condition.length; ++i) {
			if (i > 0) {
				buf.append(" ^ ");
			}
			if (!positive[i]) {
				buf.append("!");
			}
			buf.append(condition[i].toString());
		}
		buf.append((this instanceof SetDefinition) ? "}" : "]");
		return buf.toString();
	}
	
	public int compareTo(ATerm other) {
		if (this.getClass() != other.getClass()) {
			return ATerm.crossClassCompare(this, other);
		}
		//CONSIDER: compare create, positive/condition
		//this only exists for debugging so don't worry too much
		//or just throw UnsupportedOperationException
		return 0;
	}
	
	public int valueHash() {
		throw new UnsupportedOperationException("CollectionDefinition cannot be used in valueHash/valueEquals/valueClone");
	}
	public boolean valueEquals(ATerm t) {
		throw new UnsupportedOperationException("CollectionDefinition cannot be used in valueHash/valueEquals/valueClone");
	}
	public ATerm valueClone() {
		throw new UnsupportedOperationException("CollectionDefinition cannot be used in valueHash/valueEquals/valueClone");
	}
}
