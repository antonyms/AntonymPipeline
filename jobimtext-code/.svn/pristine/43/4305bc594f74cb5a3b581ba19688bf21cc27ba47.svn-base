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

import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.consistency.term.ScanTerm;

public class MultiObjSolution {
	
	public MultiObjSolution(ParamWeight[] objectiveWeights, ArrayList<ScanTerm> terms, WorldWeightParam worldWeight) {
		//TODO:
	}
	
	public ArrayList<ScanTerm> terms;
	double[] objValues;
	double nonParamWeight;
	
	public double score;
	
	double getWeight(double[] objWeights) {
		double w = nonParamWeight; 
		for (int i = 0; i < objWeights.length; ++i) {
			w += objWeights[i] * objValues[i];
		}
		return w;
	}
}
