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

package com.ibm.bluej.consistency.formula;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Bag;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.TerminalUpdatable;
import com.ibm.bluej.consistency.validate.SGDebug;

public class MemberCreate extends TerminalUpdatable implements FormulaCreate {

	protected boolean active;
	protected OriginatingTermCollection bag;
	public ATerm term; 
	private Object unlink;
	
	//No real reason for factory pattern...
	private MemberCreate() {}
	public static final MemberCreate makeMemberCreate(OriginatingTermCollection bag, ATerm t, Binds binds) {
		MemberCreate c = new MemberCreate();
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have Function links");
		}
		t = t.ground(binds, c);
		c.unlink = binds.popUnlinks();
		c.term = t;
		c.bag = bag;
		return c;
	}	

	public double getWeight() {
		return 0;
	}
	
	public void dropConditional() {
		if (active) {
			active = false;
			bag.removeMember(term);
		}
	}
	public void drop() {
		dropConditional();
		if (bag != null) {
			Binds.unlink(unlink);
			bag = null;
			term = null;
			unlink = null;
		}
	}

	public void add(Formula origin) {
		if (!active) {
			active = true;
			bag.addMember(term);
		}
	}

	public boolean isGarbage() {
		return bag == null;
	}
	
	public String toString() {
		return "MemberOf: "+term+" "+bag;
	}
	
	//hash and equals by term
	public int hashCode() {
		return term.hashCode();
	}
	public boolean equals(Object o) {
		MemberCreate m = (MemberCreate)o;
		if (this == m) {
			return true;
		}
		return term.equals(m.term);
	}

	
	public final boolean isReady() {
		return active;
	}
	
	public void update(Function source, Object... msg) {
		//NOTE: this will work for Bags
		// what doesn't work is true SETs based on functions
		// size( {size({0,1}) , size({a,b})} ) = 2
		// if you want it to be 1 instead
		//  hash/equals Functions on value
		//  or implement identity scan remove on TermSet
		((Bag)bag).modifyMember(term, source, msg); //the member has already been changed
	}
}
