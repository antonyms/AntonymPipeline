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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.inference.IMarginal;
import com.ibm.bluej.consistency.term.ScanTerm;



public class SimpleTransaction {
	WorldChange changes = new WorldChange();
	private Map<ProposalFunction, Double> prevPFVals = new HashMap<ProposalFunction, Double>();
	private IMarginal marginal;
	private MaximumAPost map;
	private boolean enabled = false;
	private double prevWeight = 0;
	
	private double acceptPercent = 0.5; //initial value
	private static final double ACCEPT_PERCENT_RECENCY = 10000;
	private static final double ACCEPT_BIAS_SCALE = 0.9999;
	private double acceptBias = 1.0;
	
	
	public String toString() {
		return changes.toString();
		//CONSIDER: how to print prev. proposal function values?
	}
	
	private WorldChange inferenceCares;
	void inCaseInferenceCares(ScanTerm t, Boolean old) {
		if (enabled && inferenceCares != null &&
			(marginal != null && marginal.isInterested(t) ||
			map != null && map.isInterested(t))) 
		{
			inferenceCares.addChange(t,old);
		}
	}

	
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	
	void setMarginal(IMarginal marginal, FormulaFindGroundRand posGrnd) {
		this.marginal = marginal;
		if (marginal != null) {
			inferenceCares = new WorldChange();
			Iterator<IndexEntry> it = posGrnd.getGroundTerms();
			while (it.hasNext()) {
				IndexEntry e = it.next();
				if (marginal.isInterested(e.term)) {
					marginal.nowTrue(e.term);
				}
			}
		}
	}
	void setMAP(MaximumAPost map) {
		this.map = map;
		if (map != null) {
			inferenceCares = new WorldChange();
			map.checkBest();
		}
	}
	
	void newValue(ProposalFunction f, double value) {
		if (!enabled) return;
		if (noChange()) {
			prevWeight = SGSearch.getWeight();
		}
		Double v = prevPFVals.get(f);
		if (v == null) {
			prevPFVals.put(f, f.value.value); 
		}
		if (!Double.isNaN(value)) {
			f.setValue(value);
		}
	}
	
	void addNowTrue(ScanTerm t, CRFState crfState) {
		addChange(t,Boolean.FALSE, crfState);
	}
	void addNowFalse(ScanTerm t, CRFState crfState) {
		addChange(t,Boolean.TRUE, crfState);
	}
	private void addChange(ScanTerm t, Boolean prev, CRFState crfState) {
		if (!enabled) {
			return;
		}
		if (noChange()) {
			prevWeight = crfState.getWeight();
		}
		changes.addChange(t, prev);
	}
	
	private boolean noChange() {
		return changes.isEmpty() && prevPFVals.isEmpty();
	}

	
	boolean epsilonSoft(double epsilon, CRFState crfState) {
		if (!enabled) {
			return true;
			//throw new Error("Called accept function without transactions enabled!");
		}
		double logratio = 0;
		if (!noChange()) {
			logratio = crfState.getWeight() - prevWeight;
		}
		if (logratio >= 0 || Math.random() < epsilon) {
			return commit();
		}
		return rollback(crfState);
	}
	
	
	/**
	 * For uniform proposal distribution
	 * @return true iff state changed
	 */
	boolean metroHastings(CRFState crfState) {
		return metroHastings(1.0, crfState);
	}
	
	boolean metroHastingsMinAccept(double forwardToBackRatio, double minAcceptProb, CRFState crfState) {
		double diff = acceptPercent - minAcceptProb;
		if (diff < 0) {
			acceptBias *= ACCEPT_BIAS_SCALE;
			acceptBias = Math.nextAfter(acceptBias, 0); //slowest possible convergence
		} else if (acceptBias < 1) {
			acceptBias /= ACCEPT_BIAS_SCALE;
			acceptBias = Math.nextAfter(acceptBias, 1);
		}
		return metroHastings(forwardToBackRatio * acceptBias, crfState);
	}
	
	//NOTE: you can do any acceptance function if you are doing MAP
	//if you are doing marginal or combo you should use the Metropolis�Hastings acceptance function
	//http://en.wikipedia.org/wiki/Metropolis%E2%80%93Hastings_algorithm
	//the proposal distribution must be able to tell you how often it would propose the reverse
	//adds to its trace if true
	
	boolean metroHastings(double forwardToBackRatio, CRFState crfState) {
		if (!enabled) {
			//throw new Error("Called accept function without transactions enabled!");
			return true;
		}
		double ratio = 1;
		if (!noChange()) {
			ratio = (Math.exp(crfState.getWeight()-prevWeight))/forwardToBackRatio;
		}
		//if (Math.random() < 0.01) {
		//	System.out.println("Ratio = "+ratio+" forwardToBackRation = "+forwardToBackRatio);
		//}
		if (ratio >= 1) {
			return commit();
		}
		if (Math.random() < ratio) {
			return commit();
		}
		return rollback(crfState);
	}
	
	private void resetForNext() {
		changes.clear();
		prevPFVals.clear();
		//++numFlips;
		if (inferenceCares != null) {
			inferenceCares.clear();
		}
	}
	
	boolean commit() {
		if (!enabled) {
			return true;
		}
		if (marginal != null) {
			//CONSIDER: (and test) marginalCares.compress();
			for (int i = 0; i < inferenceCares.size(); ++i) {
				if (inferenceCares.getPrevValue(i)) {
					marginal.nowFalse(inferenceCares.getTerm(i));
				} else {
					marginal.nowTrue(inferenceCares.getTerm(i));
				}
			}
			marginal.nextState();
		}
		if (map != null) {
			for (int i = 0; i < inferenceCares.size(); ++i) {
				if (inferenceCares.getPrevValue(i)) {
					map.nowFalse(inferenceCares.getTerm(i));
				} else {
					map.nowTrue(inferenceCares.getTerm(i));
				}
			}
			map.checkBest();
		}
		//++numAccepted;
		acceptPercent = acceptPercent*(ACCEPT_PERCENT_RECENCY-1)/ACCEPT_PERCENT_RECENCY + 1/ACCEPT_PERCENT_RECENCY;
		resetForNext();
		
		return true;
	}
	boolean rollback(CRFState crfState) {
		if (!enabled) {
			return true;
		}
		//CONSIDER: changes.compress();
		enabled = false;
		while (!changes.isEmpty()) {
			if (changes.popPrevValue()) {
				crfState.nowTrue(changes.popTerm());
			} else {
				crfState.nowFalse(changes.popTerm());
			}
		}
		enabled = true;
		for (Map.Entry<ProposalFunction, Double> e : prevPFVals.entrySet()) {
			e.getKey().setValue(e.getValue());
		}
		acceptPercent = acceptPercent*(ACCEPT_PERCENT_RECENCY-1)/ACCEPT_PERCENT_RECENCY;
		if (marginal != null) {
			marginal.nextState();
		}
		resetForNext();
		return false;
	}
}
