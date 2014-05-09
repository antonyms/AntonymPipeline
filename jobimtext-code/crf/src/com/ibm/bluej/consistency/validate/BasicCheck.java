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

package com.ibm.bluej.consistency.validate;

import java.util.Collection;
import java.util.HashSet;

import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.ATerm;


//TODO: loop and recursion checking on defines

public class BasicCheck {
	//TODO: no CollectionDefinition anywhere but DefineCreator
	//  no functions in ScanTerms of condition
	//  no CollectionDefinition in the Creator for a set/bag
	public static HashSet<String> checkBasic(Collection<Formula> formulas) {
		HashSet<String> errors = new HashSet<String>();
		for (Formula f : formulas) {
			
		}
		return errors;
	}
	
	//TODO: just make public some methods so that each Creator can define validate()
	
	public static void checkCollectionDefinition(ATerm t, boolean isOk) {
		
	}
}
