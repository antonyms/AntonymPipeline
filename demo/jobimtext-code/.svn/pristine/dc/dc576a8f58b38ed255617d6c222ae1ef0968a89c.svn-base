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

package com.ibm.bluej.consistency.optimize;

import java.util.ArrayList;
import java.util.Collection;

import com.ibm.bluej.consistency.ProposalFunction;

public class ProposalFunctionMAP {
	//TODO: store the full paramaterized regularizer score with the map solution
	//  if we change parameters during search, we can update our map value
	
	//TODO: try saving multiple proposal vectors
	//  save the bestWeight one, and others if they have good weight and are different from each other
	//  within X of best and no closer than Y to another solution
	
	double bestWeight;
	double bestRegularization;
	ArrayList<PFWithBest> allProposals;
	
	public double getBestScore() {
		return bestWeight + bestRegularization;
	}
	
	public ProposalFunctionMAP(Collection<ProposalFunction> proposalFunctions, double initWeight) {
		allProposals = new ArrayList<PFWithBest>(proposalFunctions.size());
		for (ProposalFunction pf : proposalFunctions) {
			allProposals.add(new PFWithBest(pf, pf.value.value));
		}
		bestWeight = initWeight;
	}
	
	public void checkBest(double weight, double regularization) {
		if (weight + regularization > bestWeight + bestRegularization) {
			bestWeight =  weight;
			bestRegularization = regularization;
			for (PFWithBest pfb : allProposals) {
				pfb.bestValue = pfb.pf.value.value;
			}
		}
	}
	
	public double loadBest() {
		for (PFWithBest pfb : allProposals) {
			pfb.pf.setValue(pfb.bestValue);
		}
		return bestRegularization;
	}
	
	static final class PFWithBest {
		PFWithBest(ProposalFunction pf, double bestValue) {
			this.pf = pf;
			this.bestValue = bestValue;
		}
		ProposalFunction pf;
		double bestValue;
	}
}
