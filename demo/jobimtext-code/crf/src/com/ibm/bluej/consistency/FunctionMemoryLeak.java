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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.ibm.bluej.consistency.formula.FactorFunction;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.learning.ParamCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.FunctionConditional;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.Updatable;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.IdentitySet;


public class FunctionMemoryLeak {
	void clear() {
		origTermCollections.clear();
	}
	
	//CONSIDER: not all function roots are sets/bags. You can have functions controlled by the proposal distribution
	//gather all sets/bags - from DefineCreates the only place they can be
	Collection<OriginatingTermCollection> origTermCollections = new IdentitySet<OriginatingTermCollection>();
	void functionNoLeak(CRFState crfState) {
		if (!SGDebug.FUNC_NOLEAK) {
			return;
		}

		//starting from bags, gather the set of terminal Functions (those not usedBy others)
		//now gather all topLevel functions in FunctionConditionals, FactorFunctions and DefineCreates
		//  a FunctionConditional is itself a topFunction
		//  the WeightFunction in the FactorFunction is top
		//  the term in a DefineCreate is the source of top
		//these sets should be identical or there is a memory leak
		Collection<Updatable> topFunctions = new IdentitySet<Updatable>();
		
		Iterator<IndexEntry> git = crfState.posGrnd.getGroundTerms();
		while (git.hasNext()) {
			IndexEntry e = git.next();
			Collection<Function> funs = new ArrayList<Function>();
			for (FormulaCreate c : e.getCreates()) {
				if (c instanceof DefineCreate) {
					ATerm.gatherTopFunctions(((DefineCreate) c).defined, funs);
					topFunctions.addAll(funs);
					funs.clear();
				} else if (c instanceof FunctionConditional) {
					topFunctions.add((Updatable)c);
				} else if (c instanceof FactorFunction) {
					topFunctions.add((FactorFunction)c);
				} else if (c instanceof ParamCreate) {
					topFunctions.add((Updatable)c);
				}
				//What about the MemberCreate for NONCONST in Bag?
			}
		}
		
		//Gather the endpoint functions from the OriginatingTermCollections
		IdentitySet<Updatable> endFromTCols = new IdentitySet<Updatable>();
		for (OriginatingTermCollection tc : origTermCollections) {
			tc.addEndpointFunctions(endFromTCols, false);
		}
		
		if (!topFunctions.containsAll(endFromTCols)) {
			//TODO: actually OK to have MemberCreate in endFromTCols
			// as long as that MemberCreate is part of a origTermCollection
			// you can also have a endFromTCols bound into the condition of a FormulaTermCollection
			// this will be unwound when/if the define is dropped, which is appropriate of course
			System.err.println("Function collection mismatch!");
			//detailed report of the errors
			/*
			System.err.println("Top = "+topFunctions.size()+" End = "+endFromTCols.size());
			for (Updatable f : topFunctions) {
				//if (!endFromTCols.contains(f)) {
					System.err.println("Top = "+f);
				//}
			}
			*/
			System.err.println("Apparant leak for: ");
			for (Updatable f : endFromTCols) {
				if (!topFunctions.contains(f)) {
					System.err.println("End = "+f);
				}

			}
			for (OriginatingTermCollection tc : origTermCollections) {
				tc.addEndpointFunctions(endFromTCols, true);
			}
		}
	}
}
