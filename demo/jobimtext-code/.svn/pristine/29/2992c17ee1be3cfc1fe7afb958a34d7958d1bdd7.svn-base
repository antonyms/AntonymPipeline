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
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.RemovableFormula;

public class SetDefinition  extends CollectionDefinition {
	public SetDefinition(ATerm created, ATerm[] condition, boolean[] positive, CRFState crfState) {
		this.created = created;
		this.condition = condition;
		this.positive = positive;
		this.crfState = crfState;
	}

	//TODO: right now it is an error for NONCONST to be bound into set definition
	// at a minimum it is a memory leak
	public ATerm ground(Binds binds, Updatable neededBy) {
		//this returns a bag
		ATerm[] gcondition = new ATerm[condition.length];
		for (int i = 0; i < condition.length; ++i) {
			//NOTE: this could link a Function to a CheckTerm...
			//  will it ever be undone?
			gcondition[i] = condition[i].ground(binds, null);
		}
		Formula f = new RemovableFormula(null, gcondition, positive, groundingPlan, crfState);
		TermSet set = new TermSet(created.ground(binds, null), f, crfState);
		//The Term returned is a Bag linked to the partially grounded formula
	
		if (neededBy == Function.NO_LINK) {
			System.err.println("Called BagDefinition.ground with Function.NO_LINK");
			new Exception().printStackTrace();
		}
		set.addUsedBy(neededBy);
		
		return set;
	}
	

}
