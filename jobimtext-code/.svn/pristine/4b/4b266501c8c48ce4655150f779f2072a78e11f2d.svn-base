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

import java.util.Collection;
import java.util.Map;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import com.ibm.bluej.util.common.MutableDouble;


public class LibSVMClassifier extends AbstractClassifier {
	private static final long serialVersionUID = 1L;

	public svm_parameter param = new svm_parameter();
	private svm_model model;
	private int nr_class;
	
	public LibSVMClassifier() {
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 40;
		param.C = 1000; //default is 1 
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 1; //default is 0
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];	
		if(param.gamma == 0) param.gamma = 1;
	}
	
	private svm_node[] fromMap(Map<?, MutableDouble> v) {
		//if the feature is not in the keyToIndex you must ignore it
		//which requires readjusting the size of the vector
		int trueSize = v.size();
		svm_node[] current = new svm_node[trueSize];
		int fNdx = 0;
		for (Map.Entry<?, MutableDouble> entry : v.entrySet()) {
			Integer ndx = keyToIndex.get(entry.getKey());
			current[fNdx] = new svm_node();	
			current[fNdx].index = ndx;
			current[fNdx].value = entry.getValue().value;
			++fNdx;
		}
		return current;
	}
	
	public void train(Collection<? extends Map<?, MutableDouble>>[] allVectors) {
		nr_class = allVectors.length;
		int size = 0;
		int featureSize = 0;
		
		for (Iterable<? extends Map<?, MutableDouble>> vectors : allVectors) {
			for (Map<?, MutableDouble> v : vectors) {
				++size;
				for (Object key : v.keySet()) {
					if (!keyToIndex.containsKey(key)) {
						++featureSize;
						keyToIndex.put(key, featureSize);
					}
				}
			}
		}
		
		svm_problem prob = new svm_problem();
		prob.l = size;
		prob.y = new double[prob.l];
		
		prob.x = new svm_node [prob.l][];
		
		int vNdx = 0;
		int classLabel = 0; //classLabels are 1, 2, 3
		for (Iterable<? extends Map<?, MutableDouble>> vectors : allVectors) {	
			++classLabel;
			for (Map<?, MutableDouble> v : vectors) {
				prob.x[vNdx] = fromMap(v);
				prob.y[vNdx] = classLabel;
				++vNdx;
			}
		}
		
		model = svm.svm_train(prob, param);
	}
	
	
	public double[] predictProb(Map<?, MutableDouble> features) {
		double[] dist = new double[nr_class];
		svm.svm_predict_probability(model, fromMap(filterByKnownFeatures((Map<Object,MutableDouble>)features)), dist);
		
		return dist;
	}
	
	public int predict(Map<?, MutableDouble> features) {
		svm_node[] x = fromMap(filterByKnownFeatures((Map<Object,MutableDouble>)features));
		double d = svm.svm_predict(model, x);
		//if (param.svm_type == svm_parameter.ONE_CLASS && d<0) d=2; //don't really know how this works
		return (int)(d-1); //converts classLabels from 1 based to zero based
	}
	
	public static void main(String[] args) {
		LibSVMClassifier c = new LibSVMClassifier();
		c.crossValidate(AbstractClassifier.testingData(2, 1000, 50, 0));
	}
}
