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

package com.ibm.bluej.consistency.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Pair;


public class MultiMarginal implements IMarginal {
	//TODO: when something is initialized true and never set false
	//  it doesn't have prob = 0, it will simply not be reported
	public MultiMarginal(FunST<ScanTerm,Boolean> interested) {
		this.interested = interested;
	}
	
	public boolean isInterested(ScanTerm t) {
		return interested.f(t);
	}
	
	private HashMap<ScanTerm, MarginalRecord> queries = new HashMap<ScanTerm,MarginalRecord>();
	private FunST<ScanTerm,Boolean> interested;
	private int flipCount = 0;
	
	private static class MarginalRecord {
		int sum = 0;
		int flipNumWhenSet = -1;
		double getProb(int flipCount) {
			if (flipNumWhenSet != -1) {
				sum += flipCount - flipNumWhenSet;
				flipNumWhenSet = flipCount;
			}
			return sum*1.0/flipCount;
		}
	}
	
	private MarginalRecord getRec(ScanTerm t) {
		MarginalRecord r = queries.get(t);
		if (r == null) {
			r = new MarginalRecord();
			queries.put(t,r);
		}
		return r;
	}
	
	public void nowTrue(ScanTerm t) {
		MarginalRecord r = getRec(t);
		if (r.flipNumWhenSet != -1) {
			//this is fine, just move on
			//throw new Error("nowTrue on query that was already true: "+t);
			return;
		}
		r.flipNumWhenSet = flipCount;
	}

	public void nowFalse(ScanTerm t) {
		MarginalRecord r = queries.get(t);
		if (r == null) {
			//This is fine, a proposal that undoes another proposal can result in intermediate nowFalses
			//that are never true
			//Lang.limitWarn("multimarginal.nowFalse", 50, "nowFalse on query that was never true: "+t);
			return;
		}
		if (r.flipNumWhenSet == -1) {
			//throw new Error("nowFalse on query that was already false: "+t);
			return;
		}
		r.sum += flipCount - r.flipNumWhenSet;
		r.flipNumWhenSet = -1;
	}

	public void nextState() {
		++flipCount;
	}

	public ArrayList<Pair<ScanTerm,Double>> getProbs() {
		ArrayList<Pair<ScanTerm,Double>> qp = new ArrayList<Pair<ScanTerm,Double>>();
		for (Map.Entry<ScanTerm, MarginalRecord> e : queries.entrySet()) {
			qp.add(new Pair<ScanTerm,Double>(e.getKey(), e.getValue().getProb(flipCount)));
		}
		return qp;
	}
}
