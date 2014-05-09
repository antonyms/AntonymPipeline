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

package com.ibm.bluej.consistency.proposal;

import java.util.ArrayList;
import java.util.Iterator;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.VarTerm;

public class ProposalConditionScan implements ProposalCondition {
	private ATerm[] parts;
	
	private ATerm[] tmpParts;
	private VarTerm[] tmpVars;
	
	public ProposalConditionScan(ATerm[] parts) {
		this.parts = parts;
		tmpParts = new ATerm[parts.length];
		tmpVars = new VarTerm[parts.length];
	}
	
	public boolean expandRandom(Binds binds, CRFState crfState) {
		for (int i = 0; i < parts.length; ++i) {
			ATerm p = parts[i].ground(binds, null);
			if (p instanceof VarTerm) {
				tmpParts[i] = null;
				tmpVars[i] = (VarTerm)p;
			} else {
				tmpParts[i] = p;
				tmpVars[i] = null;
			}
		}
		ScanTerm t = crfState.posGrnd.randomMatch(tmpParts);
		if (t == null) return false;
		for (int i = 0; i < t.parts.length; ++i) {
			if (tmpVars[i] != null) 
				binds.set(tmpVars[i], t.parts[i]);
		}
		return true;
	}

	public Iterator<Binds> expandAll(Binds binds, CRFState crfState) {
		for (int i = 0; i < parts.length; ++i) {
			ATerm p = parts[i].ground(binds, null);
			if (p instanceof VarTerm) {
				tmpParts[i] = null;
				tmpVars[i] = (VarTerm)p;
			} else {
				tmpParts[i] = p;
				tmpVars[i] = null;
			}
		}
		ArrayList<ScanTerm> ts = crfState.allMatch(tmpParts);
		if (ts.isEmpty()) return null;
		ArrayList<Binds> expands = new ArrayList<Binds>();
		for (ScanTerm t : ts) {
			Binds exp = new Binds(binds.getQuicksave());
			for (int i = 0; i < t.parts.length; ++i) {
				if (tmpVars[i] != null) 
					exp.set(tmpVars[i], t.parts[i]);
			}
			expands.add(exp);
		}
		return expands.iterator();
	}

	public String toString() {
		return new ScanTerm(parts).toString();
	}
}
