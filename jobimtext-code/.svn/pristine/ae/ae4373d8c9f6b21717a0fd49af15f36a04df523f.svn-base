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

package com.ibm.bluej.consistency.classifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.MutableDouble;

import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;


public class LibLinearClassifier extends AbstractClassifier {
	public double C = 1;
	public double eps = 0.001;
	public SolverType solverType = SolverType.L2R_LR;
	public boolean noBias = false;
	private Model model;
	
	public boolean debugOutput = false;
	
	private static final long serialVersionUID = 1L;

	public Map<String,Double> getParamValues() {
		Object[] featureNames = new Object[keyToIndex.size()+(noBias?0:1)];
		for (Map.Entry<Object, Integer> e : keyToIndex.entrySet()) {
			featureNames[e.getValue()] = e.getKey();
		}
		if (!noBias) {
			featureNames[featureNames.length-1] = "Bias";
		}
		double[] weights = model.getFeatureWeights();
		
		Map<String,Double> paramValues = new HashMap<String,Double>();
		for (int i = 0; i < weights.length; ++i) {
			paramValues.put(featureNames[i].toString(), weights[i]);
		}
		return paramValues;
	}
	
	public String toString() {
		Object[] featureNames = new Object[keyToIndex.size()+(noBias?0:1)];
		for (Map.Entry<Object, Integer> e : keyToIndex.entrySet()) {
			featureNames[e.getValue()] = e.getKey();
		}
		if (!noBias) {
			featureNames[featureNames.length-1] = "Bias";
		}
		double[] weights = model.getFeatureWeights();
		
		StringBuffer buf = new StringBuffer();
		buf.append("Feature weights: ");
		boolean appendedOne = false;
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i] != 0) {
				if (appendedOne) {
					buf.append(", ");
				}
				buf.append(featureNames[i].toString()+" = "+Lang.dblStr(weights[i]));
				appendedOne = true;
			}
		}
		return buf.toString();
	}
	
	private FeatureNode[] toFeatureNodeArray(Map<?, MutableDouble> v) {
		FeatureNode[] fn = new FeatureNode[v.size()+(noBias?0:1)];
		int ndx = 0;
		for (Map.Entry<?, MutableDouble> e : v.entrySet()) {
			fn[ndx++] = new FeatureNode(keyToIndex.get(e.getKey())+1, e.getValue().value);
		}
		Arrays.sort(fn, 0, ndx);
		if (ndx < fn.length) {
			fn[fn.length-1] = new FeatureNode(this.keyToIndex.size()+1, 1.0);
		}
		
		return fn;
	}
	
	
	public void train(Collection<? extends Map<?, MutableDouble>>[] allVectors) {
		if (!debugOutput) Linear.disableDebugOutput();
		Problem problem = new Problem();
		int totalInstances = 0;
		for (Collection<? extends Map<?, MutableDouble>> classInstances : allVectors) {
			totalInstances += classInstances.size();
			for (Map<?,MutableDouble> inst : classInstances) {
				for (Object k : inst.keySet()) {
					if (!this.keyToIndex.containsKey(k)) {
						this.keyToIndex.put(k, this.keyToIndex.size());
					}
				}
			}
		}
		problem.y = new int[totalInstances];
		problem.x = new FeatureNode[totalInstances][];
		int trainingInstance = 0;
		for (int cndx = 0; cndx < allVectors.length; ++cndx) {
			for (Map<?, MutableDouble> v : allVectors[cndx]) {
				problem.y[trainingInstance] = cndx;
				problem.x[trainingInstance] = toFeatureNodeArray(v);
				++trainingInstance;
			}
		}
		problem.bias = -1;
		problem.l = totalInstances;
		problem.n = this.keyToIndex.size()+(noBias?0:1); 
		model = Linear.train(problem, new Parameter(solverType, C, eps));
		StringBuffer labels = new StringBuffer();
		for (int l : model.getLabels()) {
			labels.append(l).append(' ');
		}
		//System.out.println("Feature weights = "+DenseVectors.toString(model.getFeatureWeights())+" labels = "+labels.toString());
	}

	public double[] predictProb(Map<?, MutableDouble> features) {	
		double[] probValues = new double[model.getNrClass()];
		Linear.predictProbability(model, toFeatureNodeArray(filterByKnownFeatures((Map<Object,MutableDouble>)features)), probValues);
		return probValues;
		/*
		double[] labelShuffled = new double[probValues.length];
		int[] labels = model.getLabels();
		for (int i = 0; i < labels.length; ++i) {
			labelShuffled[labels[i]] = probValues[i];
		}
		return labelShuffled;
		*/
	}

	public int predict(Map<?, MutableDouble> features) {
		//return model.getLabels()[Linear.predict(model, toFeatureNodeArray(filterByKnownFeatures((Map<Object,MutableDouble>)features)))];
		return Linear.predict(model, toFeatureNodeArray(filterByKnownFeatures((Map<Object,MutableDouble>)features)));
	}

	public static void main(String[] args) {
		LibLinearClassifier c = new LibLinearClassifier();
		c.crossValidate(AbstractClassifier.testingData(2, 1000, 50, 10));
	}
	
}
