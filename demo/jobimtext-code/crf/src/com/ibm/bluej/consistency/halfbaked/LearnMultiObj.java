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

package com.ibm.bluej.consistency.halfbaked;

import java.util.ArrayList;

//nested loop over the values, SampleRank as you go
//then take the averages
public class LearnMultiObj {
	//TODO: once you learn the multiobj functions you should fix them and run SingleMAP
	//This output should be either an initializer function or something to load the ParamWeight map with
	//let's do it through initializer function
	
	public static double FIXED_SCALE = 0.1;
	
	public LearnMultiObj(ArrayList<MultiObjSolution> solutions, double[] initialObjWeights) {
		this.solutions = solutions;
		objWeights = initialObjWeights;
		avgObjWeights = new double[objWeights.length];
	}

	//update MultiObjInitializer
	public void updateWeights(MultiObjInitializer moi) {
		moi.objWeights = avgObjWeights;
	}
	
	private ArrayList<MultiObjSolution> solutions;
	private double[] avgObjWeights;
	private double[] objWeights;
	
	public void learn(int outerLoops) {
		double avgDenom = 0;
		for (int i = 0; i < outerLoops; ++i) {
			for (int j = 0; j < solutions.size(); ++j) {
				for (int k = 0; k < solutions.size(); ++k) {
					if (j == k) continue;
					sampleRank(solutions.get(j), solutions.get(k));
					avgIn();
					++avgDenom;
				}
			}
		}
		for (int i = 0; i < avgObjWeights.length; ++i) {
			avgObjWeights[i] /= avgDenom;
		}
	}
	
	//CONSIDER: increase margin
	private void sampleRank(MultiObjSolution a, MultiObjSolution b) {
		double aw = a.getWeight(objWeights);
		double bw = a.getWeight(objWeights);
		if (bw > aw && b.score < a.score) {
			sampleRankIncrease(a.objValues, b.objValues);
		} else if (aw > bw && a.score < b.score) {
			sampleRankIncrease(b.objValues, a.objValues);
		}
	}
	
	private void sampleRankIncrease(double[] objValuesA, double[] objValuesB) {
		//increase A relative to B
		for (int i = 0; i < objValuesA.length; ++i) {
			double delta = objValuesA[i] - objValuesB[i];
			objWeights[i] += Math.signum(delta) * FIXED_SCALE;
		}
	}
	private void avgIn() {
		for (int i = 0; i < objWeights.length; ++i) {
			avgObjWeights[i] += objWeights[i];
		}
	}
	
	
}
