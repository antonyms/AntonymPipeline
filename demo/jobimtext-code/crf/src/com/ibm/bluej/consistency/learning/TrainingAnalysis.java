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

package com.ibm.bluej.consistency.learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.WorldChange;
import com.ibm.bluej.consistency.inference.MultiMarginal;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.RandomUtil;


public class TrainingAnalysis {
	public TrainingAnalysis(CRFState crfState, FunST<ScanTerm,Boolean> trackImprovementIn, int burnIn, int proposalCount) {
		this.trackImprovementIn = trackImprovementIn;
		this.proposalCount = proposalCount;
		this.burnIn = burnIn;
		if (proposalCount < numObjFuncBuckets) {
			proposalCount = numObjFuncBuckets;
		}
		this.crfState = crfState;
	}
	private CRFState crfState;
	private int proposalCount;
	private int burnIn;
	private FunST<ScanTerm,Boolean> trackImprovementIn;
	
	private HashMap<String, MutableDouble> preTrainParams = null;
	private HashMap<String, MutableDouble> postTrainParams = null;
	private Collection<Pair<ScanTerm,Double>> preTrainMarginals = null;
	private Collection<Pair<ScanTerm,Double>> postTrainMarginals = null;
	private static final int numObjFuncBuckets = 10;
	private double[] objFuncSum = new double[numObjFuncBuckets];
	private double[] objFuncCount = new double[numObjFuncBuckets];
	private int objFuncNdx = -1;
	private int proposalNum = 0;
	
	public int deltaCount = 0;
	public int acceptCount = 0;
	public RandomUtil.Sample<DeltaAnalysis> deltas = 
			new RandomUtil.Sample<DeltaAnalysis>(10);
	
	/*
	public void setEvidence(Iterator<IndexEntry> it) {
		StringBuffer buf = new StringBuffer();
		while (it.hasNext()) {
			IndexEntry e = it.next();
			if (e instanceof EvidIndexEntry) {
				buf.append(e.term).append(' ');			
			}
		}
		evidence = buf.toString();
	}
	*/
	
	public static class DeltaAnalysis {
		public Collection<ScanTerm> world;
		public WorldChange change;
		public double objectiveScore;
		public double prevObjectiveScore;
		public String delta;
		
		public DeltaAnalysis(Collection<ScanTerm> world, WorldChange change, double objectiveScore, double prevObjectiveScore, String delta) {
			this.world = world;
			this.change = change;
			this.objectiveScore = objectiveScore;
			this.prevObjectiveScore = prevObjectiveScore;
			this.delta = delta;
		}
		
		public String toString() {
			return "CHANGED: "+change +" => "+delta+" OBJ:"+Lang.dblStr(objectiveScore-prevObjectiveScore);
		}
	}
	
	public void recordDelta(WorldChange delta, double objectiveScore, double prevObjectiveScore, IDeltaWeight deltaStr) {
		if (deltas.shouldSave()) {
			ArrayList<ScanTerm> posGrndCopy = new ArrayList<ScanTerm>();
			Iterator<IndexEntry> it = crfState.posGrnd.getGroundTerms();
			while (it.hasNext()) {
				IndexEntry e = it.next();
				posGrndCopy.add((ScanTerm)e.term.valueClone());
				//posGrndCopy.add(e.term);
			}
			deltas.save(new DeltaAnalysis(posGrndCopy, delta.copy(), 
					objectiveScore, prevObjectiveScore, deltaStr.deltaString()));
		}
	}
	
	//functions to collect results
	public Collection<Pair<ScanTerm,Double>> getPreTrainMarginals() {
		return preTrainMarginals;
	}
	public Collection<Pair<ScanTerm,Double>> getPostTrainMarginals() {
		return postTrainMarginals;
	}
	/**
	 * 
	 * @return objective function change over time (averaged into 10 buckets)
	 */
	public double[] getAvgObjective() {
		double[] avgs = new double[numObjFuncBuckets];
		for (int i = 0; i < avgs.length; ++i) {
			avgs[i] = objFuncSum[i]/objFuncCount[i];
		}
		return avgs;
	}
	public HashMap<String,MutableDouble> getParamWeightDiff() {
		//TODO: a smarter way to diff
		HashMap<String,MutableDouble> diff = MutableDoubleHashMap.copyValues(postTrainParams);
		MutableDoubleHashMap.subtract(diff, preTrainParams, true);
		MutableDoubleHashMap.trimByThresholdAbs(diff, 0.01);
		return diff;
	}
	
	public void trackObjectiveFunction(double objVal, int proposalCount) {
		if (proposalNum++ % (proposalCount/numObjFuncBuckets) == 0) {
			++objFuncNdx;
		}
		objFuncSum[objFuncNdx] += objVal;
		objFuncCount[objFuncNdx] += 1;
	}
	
	public void preTrain() {
		preTrainParams = getParams();
		preTrainMarginals = getMarginals(burnIn,proposalCount);
	}
	public void postTrain() {
		postTrainParams = getParams();
		postTrainMarginals = getMarginals(burnIn,proposalCount);
	}
	
	private HashMap<String, MutableDouble> getParams() {
		HashMap<String, MutableDouble> params = new HashMap<String, MutableDouble>();
		Iterator<ATerm[]> it = crfState.learningState.getAllSpecifiers();
		while (it.hasNext()) {
			ATerm[] spec = it.next();
			double val = crfState.learningState.instance(spec).value.value;
			params.put(Lang.stringList(spec, ","), new MutableDouble(val));
		}
		return params;
	}
	
	private Collection<Pair<ScanTerm,Double>> getMarginals(int burnIn, int proposalCount) {
		MultiMarginal marginal = new MultiMarginal(trackImprovementIn);
		crfState.marginalInference(burnIn, proposalCount, marginal);
		return marginal.getProbs();
	}
 }
