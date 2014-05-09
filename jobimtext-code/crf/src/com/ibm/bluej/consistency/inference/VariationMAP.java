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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.DenseVectors;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.HashMapUtil;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PrecisionRecall;
import com.ibm.bluej.util.common.SecondPairComparator;
import com.ibm.bluej.util.common.Warnings;

/**
 * Used for DVL/Contrastive Estimation training with latent variables. The world types are the different variations, with the correct world type known as the completion
 * @author mrglass
 *
 */
public class VariationMAP extends FlexibleMAP {

	//TODO: version that just takes a variationPattern
	
	
	public VariationMAP(CRFState crfState, ScanTerm completion, int kBestCompletions, ScanTerm variationPattern) {
		this(crfState, completion, kBestCompletions, variationPattern, null);
	}
	public VariationMAP(CRFState crfState, ScanTerm completion, int kBestCompletions, ScanTerm variationPattern,
			FunST<ScanTerm,Boolean> shouldSave) {
		super(crfState);
		this.kBestCompletions = kBestCompletions;
		this.variations = variationPattern;
		completionKey = makeKey(completion);
		this.shouldSave = shouldSave;
	}
	
	public double correlationByRank(Map<String, Double> objective, boolean rankObjective) {
		
		if (rankObjective) {
			ArrayList<Pair<String,Double>> objList = HashMapUtil.toPairs(objective);
			SecondPairComparator.sortR(objList);
			//build Map String->rank
			objective = new HashMap<String,Double>();
			int rank = 0;
			for (Pair<String,Double> p : objList) {
				++rank;
				objective.put(p.first, -1.0*rank);
			}
		}
		ArrayList<Pair<Object,Double>> maps = getOrderedMAPs();
		int rank = 0;
		double[] ourRanks = new double[maps.size()];
		double[] objRanks = new double[maps.size()];
		Double worstRank = -1.0*(objective.size()+1);
		for (Pair<Object,Double> p : maps) {
			String comp = p.first.toString();
			ourRanks[rank] = -(rank+1);
			Double objr = objective.get(comp);
			if (objr == null) {
				Warnings.limitWarn("OBJvsVariationMismatch", 40, "for variation: "+comp+" not found in objective map");
			}
			objRanks[rank] = Lang.NVL(objr, worstRank);
			++rank;
		}
		return DenseVectors.pearsonsR(ourRanks, objRanks);
	}

	//P/R by rank
	public void updatePR(PrecisionRecall pr) {
		ArrayList<Pair<Object,Double>> maps = getOrderedMAPs();
		int rank = 0;
		for (Pair<Object,Double> p : maps) {
			++rank;
			String key = p.first.toString(); //(could also just cast)
			boolean correct = key.equals(completionKey);
			pr.addAnswered(PrecisionRecall.OVERALL, correct, -rank);
			pr.addAnswered(completionKey, correct, -rank);
		}
		pr.addTotalGold(PrecisionRecall.OVERALL, 1);
		pr.addTotalGold(completionKey, 1);
	}
	
	public ArrayList<Pair<WorldWeightParam, String>> getVariationsWithKey() {
		ArrayList<Pair<WorldWeightParam, String>> vs = new ArrayList<Pair<WorldWeightParam, String>>();
		for (Map.Entry<Object, MAPWorldSaver> e : this.worldTypes.entrySet()) {
			Iterator<SavedWorld> it = e.getValue().getSaved();
			Double quality = null;
			if (e.getKey().equals(completionKey)) {
				quality = 1.0;	
			} else {
				quality = 0.0;
			}
			while (it.hasNext()) {
				IWorldWeight ww = it.next().worldWeight;
				vs.add(Pair.make((WorldWeightParam)ww, quality+" "+e.getKey().toString()));
			}
		}
		return vs;
	}
	
	public Pair<ArrayList<WorldWeightParam>, ArrayList<WorldWeightParam>> getGoodAndBad() {
		ArrayList<WorldWeightParam> good = new ArrayList<WorldWeightParam>();
		ArrayList<WorldWeightParam> bad = new ArrayList<WorldWeightParam>();
		for (Map.Entry<Object, MAPWorldSaver> e : this.worldTypes.entrySet()) {
			Iterator<SavedWorld> it = e.getValue().getSaved();
			ArrayList<WorldWeightParam> saveTo = null;
			if (e.getKey().equals(completionKey)) {
				saveTo = good;
			} else {
				saveTo = bad;
			}
			while (it.hasNext()) {
				IWorldWeight ww = it.next().worldWeight;
				saveTo.add((WorldWeightParam)ww);
			}
		}
		return Pair.make(good, bad);
	}
	
	public ArrayList<Pair<WorldWeightParam, Double>> getVariations() {
		ArrayList<Pair<WorldWeightParam, Double>> vs = new ArrayList<Pair<WorldWeightParam, Double>>();
		for (Map.Entry<Object, MAPWorldSaver> e : this.worldTypes.entrySet()) {
			Iterator<SavedWorld> it = e.getValue().getSaved();
			Double quality = null;
			if (e.getKey().equals(completionKey)) {
				quality = 1.0;	
			} else {
				quality = 0.0;
			}
			while (it.hasNext()) {
				IWorldWeight ww = it.next().worldWeight;
				vs.add(Pair.make((WorldWeightParam)ww, quality));
			}
		}
		return vs;
	}
	
	private FunST<ScanTerm,Boolean> shouldSave;
	private int kBestCompletions;
	private String completionKey; //CONSIDER: list of these too?
	private ScanTerm variations;
	private String worldTypeKey; //TODO: list of these?
	
	private String makeKey(ScanTerm t) {
		StringBuffer buf = new StringBuffer();
		assert (t.parts.length == variations.parts.length);
		for (int i = 0; i < variations.parts.length; ++i) {
			if (variations.parts[i] == null) {
				buf.append(t.parts[i].toString()).append('\t');
			}
		}
		if (buf.length() > 0) {
			buf.setLength(buf.length()-1); //strip trailing tab
		}
		return buf.toString();
	}
	
	@Override
	public void nowTrue(ScanTerm t) {
		//TODO: only if t matches pattern
		if (!t.parts[0].equals(variations.parts[0]))
			return;
		worldTypeKey = makeKey(t);
	}

	@Override
	public void nowFalse(ScanTerm t) {
		//TODO: only if t matches pattern
		if (!t.parts[0].equals(variations.parts[0]))
			return;
		
		String nokey = makeKey(t);
		if (nokey.equals(worldTypeKey)) {
			worldTypeKey = null;
		}
	}

	@Override
	public boolean isInterested(ScanTerm t) {
		if (t.parts.length != variations.parts.length) {
			return false;
		}
		for (int i = 0; i < variations.parts.length; ++i) {
			if (variations.parts[i] != null && !variations.parts[i].equals(t.parts[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Object getWorldType() {
		return worldTypeKey;
	}

	//TODO: for multi-variation need this to return a collection
	@Override
	public MAPWorldSaver getWorldSaver() {	
		//if worldTypeKey is the completion key, then return NBestWorldSaver
		//otherwise return singleBestWorldSaver
		if (completionKey != null && completionKey.equals(worldTypeKey)) {
			return new NBestWorldSaver(kBestCompletions, shouldSave);
		}
		return new SingleWorldSaver(shouldSave);
	}

}
