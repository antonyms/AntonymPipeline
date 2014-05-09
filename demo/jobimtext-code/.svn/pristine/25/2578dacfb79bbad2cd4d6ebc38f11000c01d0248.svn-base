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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ibm.bluej.util.common.IdentitySet;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PrecisionRecallThreshold;


public abstract class AbstractClassifier implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//used for binary features
	public static final MutableDouble PRESENT = new MutableDouble(1.0);
	
	protected HashMap<Object, Integer> keyToIndex = new HashMap<Object, Integer>();
	
	/**
	 * Converts a set of binary features into the feature/value map format expected by train
	 * @param features binary features
	 * @return map of the feature to a MutableDouble with value 1.0
	 */
	public static <T> Map<T, MutableDouble> convertBinaryFeatures(Iterable<T> features) {
		HashMap<T, MutableDouble> m = new HashMap<T, MutableDouble>();
		for (T f : features) {
			m.put(f, PRESENT);
		}
		return m;
	}
	
	public static Collection<? extends Map<?, MutableDouble>>[] pruneFeatures(Collection<? extends Map<?, MutableDouble>>[] trainingData, int minOccurrences) {
		Map<Object, MutableDouble> featureCount = new HashMap<Object, MutableDouble>();
		for (int i = 0; i < trainingData.length; ++i) {
			for (Map<?, MutableDouble> inst : trainingData[i]) {
				for (Object o : inst.keySet()) {
					MutableDoubleHashMap.increase(featureCount, o, 1.0);
				}
			}
		}
		ArrayList<Object> toRemove = new ArrayList<Object>();
		for (Map.Entry<Object, MutableDouble> e : featureCount.entrySet()) {
			if (e.getValue().value < minOccurrences) {
				toRemove.add(e.getKey());
			}
		}
		for (int i = 0; i < trainingData.length; ++i) {
			for (Map<?, MutableDouble> inst : trainingData[i]) {
				for (Object r : toRemove) {
					inst.remove(r);
				}
			}
		}
		return trainingData;
	}
	
	protected HashMap<Object,MutableDouble> filterByKnownFeatures(Map<Object,MutableDouble> v) {
		HashMap<Object,MutableDouble> vf = new HashMap<Object,MutableDouble>();
		for (Map.Entry<Object,MutableDouble> e : v.entrySet()) {
			if (keyToIndex.get(e.getKey()) != null) {
				vf.put(e.getKey(), e.getValue());
			}
		}
		return vf;
	}
	
	//TODO: standardizeFeatures
	
	/**
	 * Generate some data for testing, if numFeatures and numInstances are pretty high it should be possible to do well on this data
	 * @param numClasses
	 * @param numInstances
	 * @param numFeatures
	 * @param numNoise
	 * @return
	 */
	public static ArrayList<Map<Integer, MutableDouble>>[] testingData(int numClasses, int numInstances, int numFeatures, int numNoise) {
		ArrayList<Map<Integer, MutableDouble>>[] data = new ArrayList[numClasses];
		for (int i = 0; i < data.length; ++i) {
			data[i] = new ArrayList<Map<Integer, MutableDouble>>();
		}
		Random rand = new Random();
		for (int i = 0; i < numInstances; ++i) {
			int cn = rand.nextInt(numClasses);
			Map<Integer, MutableDouble> instVector = new HashMap<Integer, MutableDouble>();
			for (int fi = 0; fi < numFeatures; ++fi) {
				double v =  (fi % numClasses == cn ? 1.0 : 0.0) + rand.nextGaussian()/2.0;
				instVector.put(fi,new MutableDouble(v));
			}
			for (int noise = 0; noise < numNoise; ++noise) {
				instVector.put(noise+numFeatures,new MutableDouble(rand.nextGaussian()));
			}
			
			data[cn].add(instVector);
		}
		return data;
	}
	
	protected int calculateTotalInstances(Collection<? extends Map<?, MutableDouble>>[] allVectors) {
		int totalInstances = 0;
		for (Collection<? extends Map<?, MutableDouble>> classInstances : allVectors) {
			totalInstances += classInstances.size();
		}
		return totalInstances;
	}
	
	public static double probToWeight(double prob) {
		//take care of edge cases, and don't let any weight get too high
		if (prob < .0001) {
			return -10; //-9.2
		}
		if (prob > .9999) {
			return 10;
		}
		return -Math.log(1/prob - 1);
	}	
	
	public static <S, M extends Map<S,MutableDouble>> Collection<M>[] holdOut(
			Collection<M>[] allVectors, IdentitySet<M> heldOut) 
	{
		Collection<M>[] ablated = new Collection[allVectors.length];
		for (int i = 0; i < ablated.length; ++i) {
			ablated[i] = new ArrayList<M>();
			for (M inst : allVectors[i]) {
				if (!heldOut.contains(inst)) {
					ablated[i].add(inst);
				}
			}
		}
		return ablated;
	}
	
	/**
	 * If binary classifier it assumes the "relevant" class for P/R is index 0
	 * @param allVectors
	 * @param folds
	 */
	public <T> PrecisionRecallThreshold crossValidate(List<Map<T, MutableDouble>>[] allVectors, int folds, long seed) {
		PrecisionRecallThreshold pr = new PrecisionRecallThreshold();
		//PrecisionRecallThreshold zeroIsRel = new PrecisionRecallThreshold();
		int totalInstances = calculateTotalInstances(allVectors);
		ArrayList<Pair<Map<T, MutableDouble>, Integer>> testInstances = new ArrayList<Pair<Map<T, MutableDouble>, Integer>>();
		Random rand = null;
		if (seed == 0)
			rand = new Random();
		else
			rand = new Random(seed);
		
		for (int fi = 0; fi < folds; ++fi) {
			/*if (folds == 10) {
				
				for (int cn = 0; cn < allVectors.length; ++cn) {
					ArrayList<Map<T,MutableDouble>> toRemove = new ArrayList<Map<T,MutableDouble>>();
					for (int vi = 0; vi < allVectors[cn].size(); ++vi) {
						if (vi % folds == fi) {
							testInstances.add(Pair.make(allVectors[cn].get(vi),cn));
							toRemove.add(allVectors[cn].get(vi));
						}
					}
					allVectors[cn].removeAll(toRemove);
				}
				
			} else {*/
				for (int testinst = 0; testinst < totalInstances/10; ++testinst) {
					int instNum = rand.nextInt(totalInstances);
					int sumInsts = 0;
					int cn;
					for (cn = 0; cn < allVectors.length-1; ++cn) {
						sumInsts += allVectors[cn].size();
						if (instNum < sumInsts) {
							break;
						}
					}
					
					if (allVectors[cn].size() < 2) {
						--testinst;
						continue;
					}
	
					int ndx = rand.nextInt(allVectors[cn].size());
					Map<T, MutableDouble> v = (Map<T, MutableDouble>)allVectors[cn].remove(ndx);
					testInstances.add(Pair.make(v,cn));
				}
			//}
			train(allVectors);
			if (folds == 1 || keyToIndex.size() < 100)
				System.out.println(this);
			for (Pair<Map<T, MutableDouble>, Integer> ti : testInstances) {
				double[] probs = predictProb(ti.first);
				if (allVectors.length != 2) {
					for (int ai = 0; ai < probs.length; ++ai) {
						pr.addAnswered(ai == ti.second, probs[ai]);
					}
				} else {
					pr.addAnswered(ti.second == 0, probs[0]);
				}
			}
			
			for (Pair<Map<T, MutableDouble>, Integer> ti : testInstances) {
				allVectors[ti.second].add(ti.first);
			}
			testInstances.clear();
		}
		
		return pr;
	}
	
	/**
	 * Ten fold  cross validation
	 * @param allVectors
	 */
	public <T> PrecisionRecallThreshold crossValidate(List<Map<T, MutableDouble>>[] allVectors) {
		return crossValidate(allVectors, 10, 12345);
	}
	
	/**
	 * Trains the model using the allVectors as training data
	 * @param allVectors The indices of the array correspond to the classes to be distinguished. Each class has an Iterable of instances - feature/value maps.
	 */
	public abstract void train(Collection<? extends Map<?, MutableDouble>>[] allVectors);
	/**
	 * Predict a probability for each class
	 * @param features The features for the instance to be classified
	 * @return The probability for each class
	 */
	public abstract double[] predictProb(Map<?, MutableDouble> features);
	/**
	 * Predict the most likely class label
	 * @param features The features for the instance to be classified
	 * @return The class label
	 */
	public abstract int predict(Map<?, MutableDouble> features);
}
