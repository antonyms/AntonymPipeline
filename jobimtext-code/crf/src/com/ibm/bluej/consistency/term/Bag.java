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
import java.util.IdentityHashMap;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Formula;




//implements FormulaCreate, Creator, Collection<Term>
public final class Bag extends FormulaTermCollection<Boolean>  {

	Bag(ATerm bagMember, Formula formula, CRFState crfState) {
		this.usedBy = new ArrayList<Updatable>();
		this.member = bagMember;
		this.formula = formula;
		this.formula.creator = this;
		this.parts = CompositeTerm.EMPTY_TERM; //technically a function but not really a CompositeTerm
		contents = new IdentityHashMap<ATerm, Boolean>();
		this.crfState = crfState;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		appendMembers(buf);
		buf.append("]");
		return buf.toString();
	}
	
	//these call update
	public void addMember(ATerm t) {
		contents.put(t, Boolean.TRUE);
		passUpdate(OriginatingTermCollection.ADD, t); //send an incremental update message
	}
	public void removeMember(ATerm t) {
		contents.remove(t);
		passUpdate(OriginatingTermCollection.REM, t);
	}
	public void modifyMember(ATerm t, Function source, Object[] sourceMsg) {
		passUpdate(OriginatingTermCollection.MOD, t, source, sourceMsg);
	}
	
	public void dropConditional() {
		if (!active) {
			return;
		}
		active = false;
		crfState.removeFormula(formula);
	}
	
	public boolean contains(Object o) {
		throw new UnsupportedOperationException("contains not implemented on Bag, you can use setFromBag.");
	}
	
}
