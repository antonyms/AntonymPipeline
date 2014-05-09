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


import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;



public class DefineCreator implements Creator {
	//TODO: right now we can't allow multiple definitions of terms that contain functions
	//CONSIDER: multiple definitions of terms containing functions...
	//since functions all hash/equals by identity
	//maybe functions should hash/equals that all functions are equal

	public ScanTerm toDefine;
	
	public DefineCreator(ScanTerm toDefine) {
		this.toDefine = toDefine;
	}
	
	//it may already be defined, look it up
	// if it is defined already we add a supportingCreate for it
	// only when all supportingCreates are dropped is the define nowFalse
	public FormulaCreate create(Binds binds, CRFState crfState) {
		return new DefineCreate(toDefine, binds, crfState);
	}
	
	public String toString() {
		return "DEF> "+toDefine.toString();
	}
}
