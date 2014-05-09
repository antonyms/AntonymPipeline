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

package com.ibm.bluej.consistency.learning;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;

public class BetaWeightCreator implements Creator { 
	public BetaWeightCreator(String id, ATerm arg) {
		this.id = id;
		this.arg = arg;
	}
	
	private String id;
	private ATerm arg;
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		return new BetaWeights(id, arg, binds, crfState);
	}

	public String toString() {
		return "B_{"+id+"}("+arg+")";
	}
}
