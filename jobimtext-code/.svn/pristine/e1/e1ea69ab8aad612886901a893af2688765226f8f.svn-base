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

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.rl.GroundingPlan;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.util.CleaningCollection;
import com.ibm.bluej.util.common.FunST;


public class RemovableFormula extends Formula {

	public RemovableFormula(Creator creator, ATerm[] condition,
			boolean[] positive, GroundingPlan groundingPlan, CRFState crfState) {
		super(creator, condition, positive, groundingPlan, crfState);
	}
	
	public void uncreate() {
		for (FormulaCreate c : creates) {
			c.drop();
		}
		creates.clear();
	}
	public void addCreate(FormulaCreate create) {
		creates.add(create);
	}
	
	/*
	public boolean hasNoDepends() {
		creates.clean();
		return creates.size() == 0;
	}
	
	public Iterable<FormulaCreate> getCreates() {
		return creates;
	}
	*/
	
	private CleaningCollection<FormulaCreate> creates = new CleaningCollection<FormulaCreate>(
			new FunST<FormulaCreate, Boolean>() {
				public Boolean f(FormulaCreate o) {
					return o.isGarbage();
				} 
			});	
}
