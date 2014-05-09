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

package com.ibm.bluej.consistency.old;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.GroundFindFormula;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;



public class GroundFindFormulaList extends GroundFindFormula {

	GroundFindFormulaList(ATerm[] quicksave) {
		this.quicksave = quicksave;
	}
	
	private IdentityHashMap<Formula,Formula> formulas = new IdentityHashMap<Formula,Formula>();

	public void clear() {
		formulas.clear();
		addQueue.clear();
		removeQueue.clear();
		groundNewActive = false;
	}
	
	private List<Formula> addQueue = new ArrayList<Formula>();
	private List<Formula> removeQueue = new ArrayList<Formula>();
	private boolean groundNewActive = false;
	private ATerm[] quicksave;
	
	//This wouldn't need to return anything if it took a term and an IndexEntry and the negative was a DummyIndexEntry.negScan for nonpositive
	public boolean groundNew(boolean positive, IndexEntry ti) {
		Binds binds = new Binds(quicksave);
		List<Integer> conditions = new ArrayList<Integer>();
		boolean addedOne = false;
		boolean prevGroundNewActive = groundNewActive;
		groundNewActive = true; //avoids concurrent mod. exception on iterator
		for (Formula f : formulas.keySet()) {
			for (int si = 0; si < f.condition.length; ++si) {
				if (f.canMatch(si, positive)) {		
					if (((ScanTerm)f.condition[si]).matches(ti.term, binds)) {
						((ScanTerm)f.condition[si]).bind(ti.term, binds);
						IndexEntry[] found = new IndexEntry[f.condition.length];
						found[si] = ti;
						addedOne = f.groundOut(binds, found, conditions) || addedOne;
						binds.clear();
						conditions.clear(); //should already be clear right?
					}
				}
			}
		}
		groundNewActive = prevGroundNewActive;
		if (!groundNewActive) {
			//this checks that everything is order independent
			if (SGDebug.BASIC) {
				//TODO: check that the removeQueue and addQueue are non-overlapping
				//you would need to define hashCode/equals on Formula and each Creator
				Collections.shuffle(removeQueue);
				Collections.shuffle(addQueue);
			}
			//TODO: there should be a single queue right?
			//  we might add something and then remove it (or the reverse)
			//  I think that is probably a bug if it happens
			for (Formula f : removeQueue) {
				remove(f);
			}
			for (Formula f : addQueue) {
				add(f);
			}
		}
		return addedOne;
	}

	//they add to the adding queue when groundNew is active
	public void add(Formula f) {
		if (SGDebug.LOGGING) SGLog.fine("Adding Formula "+(groundNewActive?"(inner)":"")+": "+f);
		if (groundNewActive) {
			addQueue.add(f);
			return;
		}
		/*
		//adds formula to index
		for (int i = 0; i < f.condition.length; ++i) {
			Term t = f.condition[i];
		}
		 */
		
		formulas.put(f,f);
		//ground it out too
		Binds binds = new Binds(quicksave);
		IndexEntry[] found = new IndexEntry[f.condition.length];
		f.groundOut(binds, found, new ArrayList<Integer>());
	}

	//just removes formula from index, doesn't need to drop creates
	public void remove(Formula f) {
		if (SGDebug.LOGGING) SGLog.fine("Removing Formula ("+groundNewActive+"): "+f);
		if (groundNewActive) {
			f.uncreate();
			removeQueue.add(f);
			return;
		}
		f.uncreate();
		formulas.remove(f);
	}

	public Iterator<Formula> getFormulas() {
		return formulas.keySet().iterator();
	}


}
