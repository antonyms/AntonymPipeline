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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.formula.MemberCreate;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.IdentitySet;
import com.ibm.bluej.util.common.Lang;



public abstract class FormulaTermCollection<V> extends TermCollection implements Creator, OriginatingTermCollection {
	boolean active;
	ATerm member;
	Formula formula; //reference to the formula that has this as it's creator
	Map<ATerm, V> contents;
	CRFState crfState;
	
	public double getWeight() {
		return 0;
	}
	
	public int compareTo(ATerm other) {
		if (this.getClass() != other.getClass()) {
			return ATerm.crossClassCompare(this, other);
		}
		TermCollection tc = (TermCollection)other;
		if (this.size() != tc.size()) {
			return this.size() > tc.size() ? 1 : -1;
		}
		//maybe just throw unsupported operation exception
		//CONSIDER: compare contents
		//this only exists for debugging so don't worry too much
		return 0;
	}
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		FormulaCreate c = MemberCreate.makeMemberCreate(this, member, binds);
		return c;
	}
	
	public void update(Function source, Object... msg) {
		throw new Error("FormulaTermCollections should not be updated by other functions!");
	}
	public void add(Formula origin) {
		crfState.addFormula(formula);
		active = true;
	}
	
	//NOTE: no need for an actual difference between drop and drop conditional
	public void drop() {
		dropConditional();
		member = null;
		formula = null;
		contents = null;
	}
	
	
	public boolean isGarbage() {
		//This should never happen because drop is only called by the DefineCreate and it has no cleaning collection
		System.err.println("isGarbage called on FormulaTermCollection: ");
		new Exception().printStackTrace();
		return formula == null;
	}
	
	public ATerm ground(Binds binds, Function neededBy) {
		throw new UnsupportedOperationException();
	}
	
	public Iterator<ATerm> iterator() {
		return contents.keySet().iterator();
	}
	public int size() {
		return contents.size();
	}
	
	protected void appendMembers(StringBuffer buf) {
		if (SGDebug.CANON_TOSTRING) {
			//don't really need all the compareTo junk, can just add as strings here...
			ArrayList<String> sortedTerms = new ArrayList<String>();
			for (ATerm t : this) {
				sortedTerms.add(t.toString());
			}
			Collections.sort(sortedTerms);
			for (String s : sortedTerms) {
				buf.append(s).append(" ");
			}
		} else {
			for (ATerm t : this) {
				buf.append(t).append(" ");
			}
		}
		if (this.size() > 0) {
			buf.setLength(buf.length()-1);
		}
	}
	
	/*
	public static final class Iter implements Iterator<Term> {
		private Iter(Iterator<MemberCreate> bmcit) {
			this.bmcit = bmcit;
		}
		
		Iterator<MemberCreate> bmcit;

		public boolean hasNext() {
			return bmcit.hasNext();
		}

		public Term next() {
			return bmcit.next().term;
		}

		public void remove() {
			throw new UnsupportedOperationException("no modification allowed!");
		}
	}
	*/
	
	public void addEndpointFunctions(IdentitySet<Updatable> ends, boolean print) {
		endFuns(this, ends, print, 0);
	}
	private void endFuns(Updatable f, IdentitySet<Updatable> ends, boolean print, int ind) {
		//print the Function tree
		if (print) {
			System.out.println(Lang.LPAD("", ind*2)+f);
		}
		List<Updatable> usedBy = f.getUsedBy();
		if (usedBy == null || usedBy.size() == 0) {
			ends.add(f);
			return;
		}
		for (Updatable u : usedBy) {
			endFuns(u,ends,print, ind+1);
		}
	}
}
