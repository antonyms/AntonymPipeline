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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.ibm.bluej.consistency.ProposalFunction;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.corefunc.FuncIdentity;
import com.ibm.bluej.consistency.term.corefunc.FuncMult;
import com.ibm.bluej.consistency.term.corefunc.SimpleNumberFunction;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PrecisionRecall;
import com.ibm.bluej.util.common.PrecisionRecallThreshold;
import com.ibm.bluej.util.common.SecondPairComparator;
import com.ibm.bluej.util.common.Warnings;

public class FuncLogProbScore extends SimpleNumberFunction {

	public static double scoreToProb(double s) {
		return 1.0 / (1.0 + Math.exp(-s));
	}
	
	//private static int printCount = 0;
	
	public static class Instance {
		public Instance(String id, boolean positive, ATerm score) {
			this.id = id;
			this.positive = positive;
			this.score = score;
		}
		public String id;
		public boolean positive;
		public ATerm score;
	}
	
	public int instanceNdx;
	public static ArrayList<Instance> allFunctions = new ArrayList<Instance>();
	
	public boolean isPositiveInstance() {
		return ATerm.getDouble(parts[2]) > 0;
	}
	
	public static PrecisionRecallThreshold getPR() {
		PrecisionRecallThreshold pr = new PrecisionRecallThreshold();
		for (Instance i : allFunctions) {
			pr.addAnswered(i.positive, scoreToProb(ATerm.getDouble(i.score)));
			//if (Math.random() < 0.1) {
			//	System.out.println(p.second+": "+p.first+" ~-> "+scoreToProb(Term.getDouble(p.first)));
			//}
		}
		return pr;
	}
	
	private static void addIfProFun(ATerm t, ATerm v, Map<String, Double> fv) {
		if (t instanceof FuncIdentity) {
			addIfProFun(((FuncIdentity)t).parts[0], v, fv);
		}
		if (t instanceof ProposalFunction) {
			String pn = ((ProposalFunction)t).getName();
			Double val = ATerm.getDouble(v);
			if (fv.containsKey(pn)) {
				//fv.put(pn, null);
				fv.put(pn, Math.max(fv.get(pn), val));
			} else {
				fv.put(pn, val);
			}
		}
	}
	
	private static void featureValues(ATerm t, Map<String, Double> fv) {
		if (t instanceof FuncMult) {
			FuncMult mt = (FuncMult)t;
			if (mt.parts.length == 2) {
				addIfProFun(mt.parts[0], mt.parts[1], fv);
				addIfProFun(mt.parts[1], mt.parts[0], fv);
			}
		}
		if (t instanceof ProposalFunction) {
			return;
		}
		if (t instanceof Function) {
			for (ATerm p : ((Function)t).parts) {
				featureValues(p, fv);
			}
		}
	}
	
	/**
	 * use this to get the feature informativeness, GROUND_TRUTH is the key for the truth
	 * @return
	 */
	public static Map<String,double[]> getFeatureValues() {
		HashSet<String> parameterFeatures = new HashSet<String>();
		HashSet<String> excludeParameterFeatures = new HashSet<String>();
		//build the set of features
		for (Instance i : allFunctions) {
			Map<String,Double> fv = new HashMap<String,Double>();
			featureValues(i.score, fv);
			for (Map.Entry<String,Double> e : fv.entrySet()) {
				//if (e.getValue() == null) {
				//	excludeParameterFeatures.add(e.getKey());
				//}
				parameterFeatures.add(e.getKey());
			}
		}
		parameterFeatures.removeAll(excludeParameterFeatures);
		HashMap<String,double[]> featureValues = new HashMap<String, double[]>();
		int ndx = 0;
		for (String param : parameterFeatures) {
			featureValues.put(param, new double[allFunctions.size()]);
		}
		featureValues.put("GROUND_TRUTH", new double[allFunctions.size()]);
		for (Instance i : allFunctions) {
			featureValues.get("GROUND_TRUTH")[ndx] = i.positive ? 1 : -1;
			Map<String,Double> fv = new HashMap<String,Double>();
			featureValues(i.score, fv);
			for (String param : parameterFeatures) {
				Double v = fv.get(param);
				if (v == null) {
					v = 0.0;
				}
				featureValues.get(param)[ndx] = v;
			}
			++ndx;
		}
		return featureValues;
	}
	
	/**
	 * 
	 * @return maxF1, AUC
	 */
	public static Pair<Double,Double> getSummaryScores() {
		if (allFunctions == null) {
			return null;
		}
		ArrayList<Pair<Boolean,Double>> scoreAndPositive = new ArrayList<Pair<Boolean,Double>>();
		int totalPositive = 0;
		for (Instance i : allFunctions) {
			scoreAndPositive.add(Pair.make(i.positive, ATerm.getDouble(i.score)));
			if (i.positive) ++totalPositive;
		}
		SecondPairComparator.sortR(scoreAndPositive);
		int fp = 0;
		int tp = 0;
		double maxF = 0;
		double auc = 0;
		//TODO: also get P,R,F when |P-R| is minimized
		//TODO: also average likelihood
		for (Pair<Boolean,Double> p : scoreAndPositive) {
			if (p.first) {
				++tp; 
				auc += 1.0/totalPositive * (double)(tp)/(tp+fp);
			} else {
				++fp;
			}
			double precision = (double)(tp)/(tp+fp);
			double recall = (double)(tp)/totalPositive;
			double f1 = PrecisionRecall.fScore(precision, recall);
			if (f1 > maxF) {
				maxF = f1;
			}
		}
		return Pair.make(maxF,auc);
	}
	
	@Override
	protected double compute() {
		if (value == null) {
			instanceNdx = allFunctions.size();
			allFunctions.add(
					new Instance(ATerm.getString(parts[0]),
							ATerm.getDouble(parts[2]) > 0,
							parts[1]));
		}
		double score = ATerm.getDouble(parts[1]);
		double eval = 0;
		if (ATerm.getDouble(parts[2]) > 0) {
			eval = Math.log(scoreToProb(score));
		} else {
			eval = Math.log(1.0-scoreToProb(score));
		}
		
		if (Double.isNaN(eval)) {
			String p0 = parts[1].toString();
			if (p0.length() > 200)
				p0 = p0.substring(0,200)+"...";
			Warnings.limitWarn("LogProbNan", 20, "NaN from :"+p0+" to "+eval);
		}
		
		if (eval < -10 || Double.isInfinite(eval) || Double.isNaN(eval)) {
			eval = -10;
		}
		/*
		if (printCount < 100 && Math.random() < .001 ) {
			System.out.println(score + " for "+parts[1]+" gives "+eval);
			++printCount;
		}
		*/
		return eval;
	}

	public String toString() {
		if (Function.FOR_GCC) {
			return "logProb("+parts[0]+", "+parts[1]+")";
		}
		return super.toString();
	}
}
