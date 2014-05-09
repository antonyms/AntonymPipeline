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

import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;


public class VariationRank {
	//This is the version of VariationRank without the deep learning
	//  once you implement Learnable functions though, this may work
	
	public LearningState learningState;

	public static final double RECENCY = 0.001;
	public double recencyWeightedUpdateCount = 0;
	public double recencyWeightedAccuracy = 0;
	public int vcount = 0;
	
	public double margin = 1;
	
	public boolean errorAnalysis = false;
	
	public VariationRank(LearningState learningState) {
		this.learningState = learningState;
	}
	
	/**
	 * We want one of the good to be better than all of the bad (perhaps by a margin)
	 * If this is not so, we badDelta on the good/bad pairs
	 * @param good 
	 * @param bad
	 */
	public void multiVariationRank(ArrayList<WorldWeightParam> good, ArrayList<WorldWeightParam> bad) {
		double updateCount = 0;
		
		double bestGood = Double.NEGATIVE_INFINITY;
		double totalWeight = 0;
		for (WorldWeightParam g : good) {
			bestGood = Math.max(g.getWeight(), bestGood);
			totalWeight += Math.exp(g.getWeight());
		}
		for (WorldWeightParam b : bad) {
			if (b.getWeight() + margin >= bestGood) {
				for (WorldWeightParam g : good) {
					WorldWeightParam delta = WorldWeightParam.difference(b,g);
					double scaler = Math.exp(g.getWeight())/Math.exp(bestGood);//good.size() * Math.exp(g.getWeight())/totalWeight;
					if (Double.isNaN(scaler) || Double.isInfinite(scaler)) {
						scaler = 1.0;
					}
					if (scaler < 0.0001) {
						scaler = 0.0001;
					}
					delta.scaleEveryPart(scaler);
					delta.badDelta(learningState);
				}
				if (b.getWeight() >= bestGood)
					++updateCount;
			}
			++learningState.timestep; //seems like the right place for it
		}
		
		
		double recency = RECENCY;
		if (1.0/(vcount+1) > RECENCY) {
			recency = 1.0/(vcount+1);
		}
		recencyWeightedUpdateCount = (1-recency) * recencyWeightedUpdateCount + recency * (updateCount);
		recencyWeightedAccuracy = (1-recency) * recencyWeightedAccuracy + recency * (updateCount == 0 ? 1.0 : 0.0);
		++vcount;
	}
	
	//CONSIDER: make Pair<WorldWeightParam,Double> -> Pair<WorldWeightParam,Boolean>
	
	//takes the set of correct MAPs and the set of incorrect MAPs
	//for every incorrect MAP weighted higher than a correct MAP, badDelta(iMAP - cMAP)
	public void variationRank(ArrayList<Pair<WorldWeightParam, Double>> variations) {
		double updateCount = 0;
		for (Pair<WorldWeightParam, Double> v1 : variations) {
			for (Pair<WorldWeightParam, Double> v2 : variations) {
				if (v1.second > v2.second && v1.first.getWeight() <= (v2.first.getWeight() + margin * (v1.second-v2.second)) ) {
					WorldWeightParam delta = WorldWeightParam.difference(v2.first,v1.first);
					if (errorAnalysis) {
						System.out.println(v1.second + " but " + v1.first.getWeight()+" vs. "+v2.second + " but " + v2.first.getWeight());
						System.out.println(v1.first);
						System.out.println(v2.first);
						System.out.println(delta);
						Lang.readln();
					}
					
					delta.badDelta(learningState);
					//if (Math.random() < 0.01) System.out.println("BAD DELTA: "+delta);
					//don't count the margin updates for purposes of accuracy or rank tracking
					if (v1.first.getWeight() <= v2.first.getWeight())
						++updateCount;
				}
				++learningState.timestep; //seems like the right place for it
			}
		}
		double recency = RECENCY;
		if (1.0/(vcount+1) > RECENCY) {
			recency = 1.0/(vcount+1);
		}
		recencyWeightedUpdateCount = (1-recency) * recencyWeightedUpdateCount + recency * (updateCount);
		recencyWeightedAccuracy = (1-recency) * recencyWeightedAccuracy + recency * (updateCount == 0 ? 1.0 : 0.0);
		++vcount;
	}
	
	public void finish() {
		learningState.setParametersToAverage();
	}

}
