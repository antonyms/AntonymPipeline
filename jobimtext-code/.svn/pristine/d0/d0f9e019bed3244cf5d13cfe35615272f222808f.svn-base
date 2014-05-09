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
import com.ibm.bluej.consistency.term.Binds;

public interface Creator {
	// it takes a bind list and produces a formulaCreate
	
	//There are three implementations of this - the factor, the define, and the bag (and factor function?)
	
	//TODO: add CRFState as a parameter
	public FormulaCreate create(Binds binds, CRFState crfState);
}
