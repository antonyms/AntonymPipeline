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

package com.ibm.bluej.consistency;

import java.util.ArrayList;
import java.util.Collection;

import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;



public class DefineCreate implements FormulaCreate {
	
	boolean active;
	//CONSIDER: a more efficient version could be modeled on the distinction between MemberCreate and FuncMemberCreate
	//the CONST define could simply keep a reference to the key in the DefineCreate table
	public ScanTerm defined;
	Object unlink;
	Collection<OriginatingTermCollection> tcols = new ArrayList<OriginatingTermCollection>();
	CRFState crfState;
	
	DefineCreate(ScanTerm toDefine, Binds binds, CRFState crfState) {
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have Function links");
		}
		//TODO: make CollectionDefinition add to a static list of collections
		//  mark size of current list
		//  after ground add every new tcol to our tcols list
		this.defined = (ScanTerm)toDefine.ground(binds, null);
		//Consider: only make a copy of binds if there are functions in the toDefine
		unlink = binds.popUnlinks();
		gatherTCols(defined, binds);
		this.crfState = crfState;
	}
	
	public double getWeight() {
		return 0;
	}
	
	public boolean isGarbage() {
		return defined == null;
	}
	
	private void gatherTCols(ATerm t, Binds binds) {
		if (binds.contains(t)) {
			return;
		}
		if (t instanceof OriginatingTermCollection) {
			tcols.add((OriginatingTermCollection)t);
		} else if (t instanceof CompositeTerm) {
			for (ATerm p : ((CompositeTerm)t).parts) {
				gatherTCols(p, binds);
			}
		}
	}
	
	//NOTE: there is really no difference between tColsDropConditional and tColsDrop
	private void tColsDropConditional() {
		for (OriginatingTermCollection tc : tcols) {
			tc.dropConditional();
		}
		if (SGDebug.FUNC_NOLEAK) {
			crfState.functionMemoryLeak.origTermCollections.removeAll(tcols);
		}
	}
	private void tColsDrop() {
		for (OriginatingTermCollection tc : tcols) {
			tc.drop();
		}
		if (SGDebug.FUNC_NOLEAK) {
			crfState.functionMemoryLeak.origTermCollections.removeAll(tcols);
		}
	}
	private void tColsAdd() {
		for (OriginatingTermCollection tc : tcols) {
			tc.add(null);
		}
		if (SGDebug.FUNC_NOLEAK) {
			crfState.functionMemoryLeak.origTermCollections.addAll(tcols);
		}
	}
	
	public void drop() {
		if (!active) {
			if (!isGarbage()) {
				//it may be dropConditional though not fully dropped
				//Don't need tColsDrop because tColsDropConditional does the same thing
				Binds.unlink(unlink);
				defined = null;
				unlink = null;
				tcols = null;
			}
			return;
		}
		active = false;
		if (crfState.defineState.decrement(defined)) {
			//does this order matter - yes, nowFalse is better first, may avoid function recomputes
			crfState.nowFalseInternal(new DefIndexEntry(defined));
			tColsDrop();
		}	
		
		Binds.unlink(unlink);
		defined = null;
		unlink = null;
		tcols = null;
	}

	public void add(Formula origin) {
		if (active) {
			return;
		}
		active = true;
		if (crfState.defineState.increment(defined, this, origin)) {
			tColsAdd();
			crfState.nowTrueInternal(new DefIndexEntry(defined));
			
		} else if (SGDebug.DEFINE_DEBUG) {
			//TODO: won't actually catch the multiple definition of non-function free
			// would need a hashCode/equals that ignores functions
			if (!defined.isFunctionFree()) {
				SGLog.info("Multiple definition with functions is not supported: "+defined);
			}
		}
		
	}

	public final void dropConditional() {
		if (!active) {
			return;
		}
		active = false;	
		if (crfState.defineState.decrement(defined)) {
			//does this order matter - yes, nowFalse is better first, may avoid function recomputes
			crfState.nowFalseInternal(new DefIndexEntry(defined));
			tColsDropConditional();
		}
	}

	public String toString() {
		return "Define: "+defined;
	}

}
