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
import java.util.Map;

import com.ibm.bluej.consistency.focus.FocusIndicatorState;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.proposal.ProposalSet;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.util.common.Lang;

public class CRFDescription {
	public ArrayList<Formula> formulas;
	public ProposalSet proposals;
	public ArrayList<Formula> objectives;
	public boolean loadedObjectives = false;
	
	//mostly for debugging I guess
	public Map<String, StringTerm> constants;
	public Map<String, Class> funNameToClass;
	public int maxVarCount = 0;
	
	//not used, for old declarative test cases idea
	//public ArrayList<SearchTest> tests;
	
	public FocusIndicatorState focusIndicators;
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("FORMULAS:\n"+Lang.stringList(formulas, "\n")+"\n");
		if (objectives != null)
			buf.append("OBJECTIVES:\n"+Lang.stringList(objectives, "\n")+"\n");
		if (proposals != null)
			buf.append("PROPOSALS:\n"+Lang.stringList(proposals.getProposals(), "\n")+"\n");
		buf.append("NUM VARS:\n"+maxVarCount+"\n");
		buf.append("CONSTANTS:\n"+Lang.stringList(constants.keySet(), "; ")+"\n");
		return buf.toString();
	}
}
