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

package com.ibm.bluej.consistency.learning.coordescent;

import java.util.ArrayList;

import com.ibm.bluej.consistency.learning.LearningState;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.util.common.IdentitySet;
import com.ibm.bluej.util.common.Pair;

public class RankLearn {
	//CONSIDER: hold the coordinates fixed for some time before switching?
	
	public static void learn(LearningState learningState, ArrayList<Pair<WorldWeightFunction, Double>> scoreToGain, double margin) {
		//nested loop, for a badDelta, call complete on each WorldWeightFunction
		//  subtract, badDelta
		//  use a small increment? adjustable increment?
		for (Pair<WorldWeightFunction, Double> p1 : scoreToGain) {
			for (Pair<WorldWeightFunction, Double> p2 : scoreToGain) {
				if (p1 != p2) {
					if (p1.second > p2.second && p1.first.getValue()-margin <= p2.first.getValue()) {
						rerank(learningState, p2.first, p1.first);
						recompute(scoreToGain);
					} else if (p1.second > p2.second && p1.first.getValue() >= p2.first.getValue()-margin) {
						rerank(learningState, p1.first, p2.first);
						recompute(scoreToGain);
					}
				}
			}
		}
	}
	
	public static void recompute(ArrayList<Pair<WorldWeightFunction, Double>> scoreToGain) {
		for (Pair<WorldWeightFunction, Double> p1 : scoreToGain) {
			p1.first.clear();
		}
	}
	
	public static void rerank(LearningState learningState, WorldWeightFunction lower, WorldWeightFunction higher) {
		IdentitySet<ParamWeight> fixed = new IdentitySet<ParamWeight>();
		lower.completeFixed(fixed);
		higher.completeFixed(fixed);
		//TODO: everything is zero because initial parameters are zero
		WorldWeightParam diff = WorldWeightParam.difference(lower.getWeight(fixed), higher.getWeight(fixed));
		boolean showDiff = Math.random() < 0.01;
		if (showDiff) System.out.println("Pre:"+diff);
		diff.badDelta(learningState);
		if (showDiff) System.out.println("Post:"+diff);
		learningState.incrementTimestep();
	}
}
