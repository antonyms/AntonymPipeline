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

package com.ibm.bluej.consistency;

import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.TerminalUpdatable;
import com.ibm.bluej.consistency.validate.SGDebug;

//it makes a valueCopy and nowTrues it. If it is ever updated it nowFalses the prev copy, makes a new copy and nowTrues it
public class MakeConstCreator implements Creator {
	public MakeConstCreator(ScanTerm toConst) {
		this.toConst = toConst;
	}
	
	private ScanTerm toConst;
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		//TODO: debug, check that every bind in binds is present in toConst
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have Function links");
		}
		MakeConst mc = new MakeConst(crfState);
		mc.nonConst = (ScanTerm)toConst.ground(binds, mc); 
		mc.unlink = binds.popUnlinks();
		return mc;
	}

	public String toString() {
		return "MAKECONST> " + toConst;
	}
	
	static class MakeConst extends TerminalUpdatable implements FormulaCreate {
		boolean active = false;
		ScanTerm nonConst;
		ScanTerm valueCopy;
		Object unlink;
		CRFState crfState;
		
		MakeConst(CRFState crfState) {
			this.crfState = crfState;
		}
		
		public double getWeight() {
			return 0;
		}
		
		public boolean isReady() {
			return active;
		}
		
		public boolean isGarbage() {
			return nonConst == null;
		}

		public void drop() {
			dropConditional();
			Binds.unlink(unlink);
			unlink = null;
			valueCopy = null;
			nonConst = null;
		}

		public void dropConditional() {
			if (active) {
				active = false;
				crfState.nowFalseInternal(new DefIndexEntry(valueCopy));
			}
		}

		public void add(Formula origin) {
			if (!active) {
				active = true;
				valueCopy = (ScanTerm)nonConst.valueClone();
				crfState.nowTrueInternal(new DefIndexEntry(valueCopy));
			}
		}

		public void update(Function source, Object... msg) {
			crfState.nowFalseInternal(new DefIndexEntry(valueCopy));
			valueCopy = (ScanTerm)nonConst.valueClone();
			crfState.nowTrueInternal(new DefIndexEntry(valueCopy));
		}
		
		public ATerm getValue() {
			throw new UnsupportedOperationException();
		}

		public ATerm ground(Binds binds, Function neededBy) {
			throw new UnsupportedOperationException();
		}
	}
}
