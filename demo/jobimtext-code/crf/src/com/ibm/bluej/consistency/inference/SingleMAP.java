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

package com.ibm.bluej.consistency.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.MaximumAPost;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.FunST;


//TODO: implement as a special case of FexibleMAP with only one world type
public class SingleMAP extends MaximumAPost {
	public SingleMAP(CRFState crfState) {
		super(crfState);
	}
	
	//CONSIDER: only save best if all focus are set
	// or save current best and all focus best
	
	public FunST<ScanTerm,Boolean> shouldSave;
	
	public double reallyCheckProb = 1.0;
	
	private IWorldWeight bestWeight = null;
	private ArrayList<ScanTerm> bestTerms = new ArrayList<ScanTerm>();
	//private ArrayList<Pair<String,Double>> bestProposalFunctionValues = new ArrayList<Pair<String,Double>>();
	
	//used by VariationRank
	public IWorldWeight getWorldWeight() {
		return bestWeight;
	}
	
	public void clear() {
		bestWeight = null;
		bestTerms.clear();
	}
	
	public boolean checkBest() {
		if (Math.random() > reallyCheckProb) {
			return false;
		}
		if (!crfState.getFocusIndicators().safeToSave()) {
			return false;
		}
		//Function.flush();
		boolean newBest = false;
		crfState.getWorldWeight().updateBestWeight(bestWeight);
		if (bestWeight == null || crfState.getWeight() > bestWeight.getWeight()) {
			bestTerms.clear();
			if (SGDebug.MAP_INFERENCE) {
				System.out.println("new best "+(bestWeight == null ? "NONE" : bestWeight.getWeight())+" to "+crfState.getWeight());
			}
			//bestProposalFunctionValues.clear();
			Iterator<IndexEntry> pit = crfState.posGrnd.getGroundTerms();	
			StringBuffer buf = null;
			while (pit.hasNext()) {
				IndexEntry e = pit.next();
				//don't add the subtypes, they are special purpose
				if (shouldSave != null) {
					if (shouldSave.f(e.term)) {
						if (SGDebug.MAP_INFERENCE) {
							System.out.println("Saving "+e.term);
						}
						bestTerms.add((ScanTerm)e.term.valueClone()); //because it may contain functions!
					}
				} else if (e.getClass() == IndexEntry.class) {
					bestTerms.add((ScanTerm)e.term.valueClone()); //because it may contain functions!
				}
				if (SGDebug.TRACK_BEST) {
					if (buf == null) buf = new StringBuffer("BEST TERMS: ");
					buf.append(e.term+" ");
				}
			}
			//for (Map.Entry<String, ProposalFunction> e : proposalFunctions.entrySet()) {
			//	bestProposalFunctionValues.add(new Pair<String,Double>(e.getKey(), Term.getDouble(e.getValue().getValue())));
			//}
			if (SGDebug.TRACK_BEST) {
				SGLog.info(buf == null ? "BEST TERMS EMPTY" : buf.toString());
				SGLog.info("BEST WEIGHT: "+crfState.getWorldWeight());
			}
			bestWeight = crfState.getWorldWeight().recordWeight();
			newBest = true;
			//TODO: reward proposal using callback function
		}
		if (SGDebug.LOGGING) SGLog.fine("Weights = "+crfState.getWeight()+" "+bestWeight.getWeight());
		return newBest;
	}

	public double getBestWeight() {
		if (bestWeight == null) {
			return Double.NaN;
		}
		return bestWeight.getWeight();
	}
	public Collection<ScanTerm> getBest() {
		if (bestWeight != null) {
			return bestTerms;
		}
		return null;
	}

	/*
	public Collection<Pair<String,Double>> getBestFuncValues() {
		if (bestWeight != null) {
			return bestProposalFunctionValues;
		}
		return null;
	}
	
	HashMap<String, ProposalFunction> proposalFunctions = new HashMap<String,ProposalFunction>();
	public void trackProposalFunction(String name, ProposalFunction f) {
		if (proposalFunctions.put(name, f) != null) {
			System.err.println("overwrote a proposal function in SingleMap.trackProposalFunction");
		}
	}
	*/
}
