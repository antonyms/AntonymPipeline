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

import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.term.ScanTerm;

public abstract class MaximumAPost {
	//TODO: SimpleTransaction should accept one of these (and checkBest on every commit)
	//TODO: change all testcases to use transaction, and regression test
	
	//this is the only really common aspect of SingleMAP and MultiObjMAP
	public abstract boolean checkBest();

	//public abstract void trackProposalFunction(String name, ProposalFunction f);
	
	protected CRFState crfState;
	protected MaximumAPost(CRFState crfState) {
		this.crfState = crfState;
	}
	
	public abstract IWorldWeight getWorldWeight();
	
	public abstract void clear();
	
	public boolean isInterested(ScanTerm t) {return false;}
	
	//default is nothing
	/**
	 * Called during the search (only if transactions are enabled)
	 * enables the MAPTracker to know what type of world this is currently
	 * Ex: a correct one, or a variant one
	 * @param t
	 */
	public void nowTrue(ScanTerm t) {}
	/**
	 * Called during the search (only if transactions are enabled)
	 * enables the MAPTracker to know what type of world this is currently
	 * Ex: a correct one, or a variant one
	 * @param t
	 */	
	public void nowFalse(ScanTerm t) {}
}
