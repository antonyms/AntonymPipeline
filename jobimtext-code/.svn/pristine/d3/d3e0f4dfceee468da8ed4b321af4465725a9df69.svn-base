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

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.util.common.MutableInteger;



public final class TermSet extends FormulaTermCollection<MutableInteger> {

	TermSet(ATerm member, Formula formula, CRFState crfState) {
		this.usedBy = new ArrayList<Updatable>();
		this.member = member;
		this.formula = formula;
		this.formula.creator = this;
		this.parts = CompositeTerm.EMPTY_TERM; //technically a function but not really a CompositeTerm
		contents = new HashMap<ATerm, MutableInteger>();
		this.crfState = crfState;
	}
	
	public void dropConditional() {
		if (!active) {
			return;
		}
		active = false;
		crfState.removeFormula(formula);
	}
	
	//both of these call update
	public void addMember(ATerm t) {
		if (t == null) {
			throw new Error("called addMember with bad MemberCreate term was null");
		}
		//increment
		MutableInteger l = contents.get(t);
		if (l == null) {
			l = new MutableInteger(1);
			contents.put(t, l);
			passUpdate(OriginatingTermCollection.ADD, t); //send an incremental update message
		} else {
			l.value += 1;
		}
	}
	public void removeMember(ATerm t) {
		//decrement
		MutableInteger l = contents.get(t);
		if (l == null) {
			System.err.println("Tried to remove "+t+" from "+this+" but it wasn't present");
		}
		if (l.value == 1) {
			contents.remove(t);
			passUpdate(OriginatingTermCollection.REM, t);
		} else {
			l.value -= 1;		
		}	
	}
	
	public boolean contains(Object o) {
		return contents.containsKey((ATerm)o);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		appendMembers(buf);
		buf.append("}");
		return buf.toString();
	}

}
