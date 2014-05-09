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

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.MaximumAPost;
import com.ibm.bluej.consistency.learning.IWorldWeight;

/**
 * Version of MAP that tracks weight and objective score, essentially tracking the objective score of the highest weight world
 * @author mrglass
 *
 */
public class EvaluateMAP extends MaximumAPost {
	private IWorldWeight bestWeight = null;
	private double objectiveScore = Double.NaN;
	
	public EvaluateMAP(CRFState crfState) {
		super(crfState);
	}
	
	@Override
	public boolean checkBest() {
		if (bestWeight == null || crfState.getWeight() > bestWeight.getWeight()) {
			bestWeight = crfState.getWorldWeight().recordWeight();
			objectiveScore = crfState.getObjectiveScore();
			return true;
		}
		return false;
	}

	@Override
	public IWorldWeight getWorldWeight() {
		return bestWeight;
	}

	@Override
	public void clear() {
		bestWeight = null;
		objectiveScore = Double.NaN;
	}

	public double getObjectiveScore() {
		return objectiveScore;
	}
}
