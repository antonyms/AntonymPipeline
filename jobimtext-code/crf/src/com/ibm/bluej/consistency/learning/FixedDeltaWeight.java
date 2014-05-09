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


public class FixedDeltaWeight implements IDeltaWeight {
	double weight = 0;
	
	public FixedDeltaWeight() {}
	
	public FixedDeltaWeight(double weight) { this.weight = weight;}
	
	/*
	/**
	 * @deprecated doesn't work yet
	 * @param rebuild	
	public FixedDeltaWeight(boolean rebuild) {
		if (rebuild) {
			weight = 0;
			Iterator<IndexEntry> it = SGSearch.posGrnd.getGroundTerms();
			//TODO: would need to create IdentitySet<FormulaCreate>, then getImmediateWeight on those
			while (it.hasNext()) {
				weight += it.next().getImmediateWeight();
			}
		}
	}
	*/
	
	public IWorldWeight recordWeight() {
		return new WorldWeightFixed(weight);
	}

	public void updateBestWeight(IWorldWeight bestWeight) {
		//Nothing
	}

	public void addWeight(double v) {
		weight += v;
	}

	public void addWeight(Learnable p, double v) {
		weight += p.getWeight() * v;
	}

	public double getWeight() {
		return weight;
	}

	public void beginDelta() {}
	
	public boolean endDelta(LearningState learningState, double goldScore, double prevGoldScore) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		weight = 0;
	}

	public String deltaString() {
		return "FixedDeltaWeight: NO DELTA";
	}
	
	public String toString() {
		return "FixedDeltaWeight = "+weight;
	}
}
