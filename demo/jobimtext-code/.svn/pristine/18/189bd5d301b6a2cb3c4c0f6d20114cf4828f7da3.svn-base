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

package com.ibm.bluej.consistency.learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.FirstPairComparator;
import com.ibm.bluej.util.common.HashMapUtil;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;
import com.ibm.bluej.util.common.Pair;


//TODO: comparable
public class WorldWeightParam implements IWorldWeight,Cloneable {
	private static final long serialVersionUID = 1L;
	
	private static final double FIXED_SCALE = 0.1;
	private static final double LEARN_RATE = 1.0;
	private static final double MARGIN = 0.5;
	
	
	
	//I'm interested in viewing the ParamWeights during SampleRank as undergoing a particular kind of random walk
	//  where the direction and magnitude of the next step are drawn from some distribution 
	//  such that the random walk focuses on the ParamWeight's "true value"
	//  What are the characteristics of this distribution?
	//  How can I tell when two ParamWeights have significantly different distributions?
	
	private double totalWeight = 0;
	//TODO: drop this learnable stuff...
	protected IdentityHashMap<Learnable, MutableDouble> weights = new IdentityHashMap<Learnable, MutableDouble>();
	protected double nonParamWeight = 0;
	
	public String toOptimizeString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(+ ").append(nonParamWeight);
		for (Map.Entry<Learnable,MutableDouble> e : weights.entrySet()) {
			buf.append(" (").append(ParamWeight.proposalFunctionName(e.getKey().getName())).append(" * ").append(e.getValue()).append(")");
		}
		buf.append(")");
		return buf.toString();
	}
	
	/**
	 * Expert usage
	 * @param scale
	 */
	public void scaleEveryPart(double scale) {
		nonParamWeight *= scale;
		for (MutableDouble d : weights.values()) {
			d.value *= scale;
		}
	}
	
	public boolean hasParam() {
		return !weights.isEmpty();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("total= "+Lang.dblStr(totalWeight)+" nonParam="+Lang.dblStr(nonParamWeight));
		//sort the per param weights
		ArrayList<String> paramVals = new ArrayList<String>();
		for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
			paramVals.add(e.getKey()+"*"+e.getValue().value);
		}
		Collections.sort(paramVals);
		buf.append("\n  ").append(Lang.stringList(paramVals, " "));
		return buf.toString();
	}
	
	/**
	 * if this is actually a goodDelta, switchDirection and call badDelta
	 */
	void switchDeltaDirection() {
		for (MutableDouble d : weights.values()) {
			d.value = -d.value;
		}
	}
	
	//clone for bestWeight
	public WorldWeightParam clone() {
		try {
		WorldWeightParam w = (WorldWeightParam)super.clone();
		w.weights = new IdentityHashMap<Learnable, MutableDouble>();
		for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
			w.weights.put(e.getKey(), new MutableDouble(e.getValue().value));
		}
		return w;
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	//creates a delta from two potentially very different world weights
	public static WorldWeightParam difference(WorldWeightParam w1, WorldWeightParam w2) {
		WorldWeightParam delta = new WorldWeightParam();
		delta.update(w1.nonParamWeight - w2.nonParamWeight);
		for (Map.Entry<Learnable, MutableDouble> e1 : w1.weights.entrySet()) {
			Learnable param = e1.getKey();
			MutableDouble d1 = e1.getValue();
			MutableDouble d2 = Lang.NVL(w2.weights.get(param), new MutableDouble());
			delta.update(param, d1.value-d2.value);
		}
		for (Map.Entry<Learnable, MutableDouble> e2 : w2.weights.entrySet()) {
			Learnable param = e2.getKey();
			if (!w1.weights.containsKey(param)) {
				MutableDouble d2 = e2.getValue();
				delta.update(param, -d2.value);
			}
		}
		return delta;
	}
	
	/**
	 * Expert
	 * @param delta
	 */
	public void merge(WorldWeightParam delta) {
		update(delta.nonParamWeight);
		for (Map.Entry<Learnable, MutableDouble> e : delta.weights.entrySet()) {
			update(e.getKey(), e.getValue().value);
		}
	}
	
	public double getWeight() {
		return totalWeight;
	}
	
	public void clear() {
		weights.clear();
		totalWeight = 0;
		nonParamWeight = 0;
	}
	
	void recompute() {
		totalWeight = 0;
		for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
			totalWeight += e.getKey().getWeight() * e.getValue().value;
		}
		totalWeight += nonParamWeight;
	}
	
	public void update(double v) {
		totalWeight += v;
		nonParamWeight += v;
	}
	
	public void update(Learnable p, double v) {
		if (v == 0) {
			return;
		}
		MutableDouble d = weights.get(p);
		if (d == null) {
			d = new MutableDouble(0);
			weights.put(p,d);
		}
		d.value += v;
		if (Math.abs(d.value) < 0.001) {
			weights.remove(p);
		}
		totalWeight += p.getWeight() * v;
	}
	
	//TODO: subtract, implemented like merge, for use with VariationRank
	
	/**
	 * This SGWeight represents the difference between two states:
	 * this = weight(s2) - weight(s1).
	 * gold(s1) > gold(s2).
	 * |weight(s1)| =< |weight(s2)|.
	 * So |this| is non-negative.
	 * NOTE: after any learning update recompute() will have to be called on all SGWeights
	 * @param timestep The number of SampleRanks so far, beginning at 1
	 */
	public void badDelta(LearningState learningState) {
		// so the paramWeights of this need to decrease if the associated value is positive
		//  and increase if the associated value is negative
		// a rate of 1.0 should cause this.getWeight() == 0
		
		if (SGDebug.SAMPLE_RANK) {
			System.out.println("Bad Delta at:");
			for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
				System.out.println(e.getKey()+" * "+e.getValue());
			}
		}
		
		//compute the totalError and totalCount in one loop
		double perTermScale = 0;
		if (FIXED_SCALE > 0) {
			perTermScale = FIXED_SCALE;
		} else {
			double totalError = 0; 
			double totalCount = 0;
			for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
				double c = e.getValue().value;
				totalError += e.getKey().getWeight() * c;
				totalCount += Math.abs(c);
			}
			if (totalError < 0) {
				throw new Error("Called badDelta with a negative error");
			}
			//totalError could be zero (consider when all ParamWeights are initialized to zero)
			if (MARGIN > 0 && LEARN_RATE == 1) {
				totalError += MARGIN; //The quasi-MIRA case
			} else {
				totalError = Math.max(totalError, 0.5); 
			}
			perTermScale = (LEARN_RATE*totalError)/totalCount;
		}
		for (Map.Entry<Learnable, MutableDouble> e : weights.entrySet()) {
			double v = e.getValue().value;
			Learnable p = e.getKey();
			
			//update the parameter
			//double delta = -Math.signum(v) * perTermScale;
			p.pushDelta(- v * perTermScale, learningState);

		}
	}
	

	//ignores nonparamweight
	public int hashCode() {
		int h = ATerm.mdjbFirst();
		List<Pair<Learnable,MutableDouble>> pairs = HashMapUtil.toPairs(weights);
		Collections.sort(pairs, new FirstPairComparator(new ParamWeight.NameComparator()));
		for (Pair<Learnable, MutableDouble> p : pairs) {
			h = ATerm.mdjbNext(h, p.hashCode());
		}
		return h;
	}
	public boolean equals(Object o) {
		if (!(o instanceof WorldWeightParam)) {
			return false;
		}
		WorldWeightParam wwp = (WorldWeightParam)o;
		return MutableDoubleHashMap.equals(wwp.weights, this.weights);
	}
	
}
