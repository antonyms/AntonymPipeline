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

import com.ibm.bluej.consistency.ParserAntrl;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.util.common.FileUtil;



public class GroundableCheck {
	public static void main(String[] args) {
		String contents = FileUtil.readFileAsString("test.con");
		Collection<Formula> formulas = ParserAntrl.parse(SGSearch.getCRFState(), contents).formulas;
		for (String error : checkBindable(formulas)) {
			System.out.println(error);
		}
	}
	
	//  this method checks that all formulas can be grounded
	//  every variable in a function must be BINDABLE
	//  a variable is BINDABLE if for ANY term in the condition:
	//    the term is a ScanTerm and the variable occurs in it
	//    the term is an ExpandTerm and the position the variable occurs in is expandable
	//  we first check all the variables that are bindable based on ScanTerms
	//  then for every ExpandTerm we query if it can fill the rest
	
	public static HashSet<String> checkBindable(Collection<Formula> formulas) {
		HashSet<String> errors = new HashSet<String>();
		for (Formula f : formulas) {
			HashSet<VarTerm> inExpand = new HashSet<VarTerm>();
			HashSet<VarTerm> inCheck = new HashSet<VarTerm>();
			HashSet<VarTerm> inScan = new HashSet<VarTerm>();
			//NOTE: for purposes of BINDABLE, negated ScanTerms are CheckTerms
			for (int i = 0; i < f.condition.length; ++i) {
				ATerm c = f.condition[i];
				if (!(c instanceof ScanTerm) || !f.isPositive(i)) {
					ConstantCheck.gatherVars(c, inCheck);
					if (c instanceof ExpandTerm) {
						ConstantCheck.gatherVars(c, inExpand);
					}
				} else {
					ConstantCheck.gatherVars(c, inScan);
				}
			}
			
			inCheck.removeAll(inScan);
			if (!inCheck.isEmpty()) {
				if (inExpand.containsAll(inCheck)) {
					//may still be an error
					StringBuffer buf = new StringBuffer();
					buf.append("WARNING: in formula: "+f.toString()+" the variable(s): ");
					for (VarTerm v : inCheck) {
						buf.append(v).append(", ");
					}
					buf.setLength(buf.length()-2);
					buf.append(" occur only in CheckTerms.");
					buf.append("They also occur in ExpandTerms. Depending on the implementation of the ExpandTerms this may be ok");
					errors.add(buf.toString());
				} else {
					//an error
					StringBuffer buf = new StringBuffer();
					buf.append("ERROR: in formula: "+f.toString()+" the variable(s): ");
					for (VarTerm v : inCheck) {
						buf.append(v).append(", ");
					}
					buf.setLength(buf.length()-2);
					buf.append(" occur only in CheckTerms");
					errors.add(buf.toString());
				}
			}
			
			//TODO: if it is a Define or FunctionFactor check that the vars in the Create make sense
		}
		return errors;
	}
}
