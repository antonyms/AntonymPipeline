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

package com.ibm.bluej.consistency.proposal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.util.common.Warnings;

public class ProposalSet {
	//TODO: need to have a better way to do training vs. inference proposals
	
	public static int MAX_TRIES = 100;
	
	public ProposalSet(Collection<Proposal> pros) {
		proposals = new Proposal[pros.size()];
		int ndx = 0;
		for (Proposal pro : pros) {
			proposals[ndx++] = pro;
		}
		/*
		value = new double[proposals.length];
		for (int i = 0; i < value.length; ++i) {
			value[i] = 1;
		}
		sumValue = value.length;
		*/
	}
	
	public List<Proposal> getProposals() {
		return Collections.unmodifiableList(Arrays.<Proposal>asList(proposals));
	}
	
	private Proposal[] proposals;
	//private Proposal[] trainProposals; //focused on finding badDeltas
	//private Proposal[] testProposals; //For marginal, focused on detailed balance; For MAP, focused on rapid weight gain
	
	//for MAP inference we can put anything here
	//for marginal we must take care to keep symmetric - or track the lack of symmetry
	private double[] value; //the value of making the proposal
	private double sumValue;
	
	public void accepted(int proNdx, double value) {
		if (value > 0)
			++proposals[proNdx].acceptedCount;
		proposals[proNdx].improvement += value;
	}
	
	public int propose() {
		//keep trying proposals until one succeeds
		int tryCount = 0;
		while (true) {
			if (tryCount++ > MAX_TRIES) {
				
				if (Warnings.limitWarn("ProposalSet.propose", 10, "No proposal seems possible")) {
					for (Proposal p : proposals) {
						System.err.println(p);
					}
				}
				return -1;
			}
			int selected = proposals.length - 1;
			if (value != null && sumValue > 0) {
				//propose one of the proposals, weighted max by value
				double selectOption = Math.random();
				double cumVal = 0;
				for (int i = 0; i < proposals.length; ++i) {
					cumVal += value[i]/sumValue;
					if (selectOption <= cumVal) {
						selected = i;
						break;
					}
				}
			} else {
				selected = SGSearch.rand.nextInt(proposals.length);
			}
			if (proposals[selected].propose()) {
				return selected;
			}
		}
	}
}
