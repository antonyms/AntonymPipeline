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


public class DeltaOnly implements IDeltaWeight {
	private WorldWeightParam delta;
	
	public String deltaString() {
		return delta == null ? "NO DELTA" : delta.toString();
	}
	
	public DeltaOnly() {}
	
	public String toString() {
		return ""+delta;
	}
	
	public IWorldWeight recordWeight() {
		throw new UnsupportedOperationException();
	}
	public void updateBestWeight(IWorldWeight bestWeight) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		delta = null;
	}
	
	public void addWeight(double v) {
		if (delta != null) {
			delta.update(v);
		}
	}
	public void addWeight(Learnable p, double v) {
		if (delta != null) {
			delta.update(p,v);
		}
	}

	public double getWeight() {
		return delta == null ? 0 : delta.getWeight();
	}

	public void beginDelta() {
		delta = new WorldWeightParam();
	}
	
	//CONSIDER: push margin?
	public boolean endDelta(LearningState learningState, double goldScore, double prevGoldScore) {
		boolean updated = false;
		//Might be getting deltas based on super tiny 
		if (delta != null) {
			if (prevGoldScore > goldScore + 0.001) {
				if (delta.getWeight() >= 0) {
					delta.badDelta(learningState); 
					updated = true;
				}
			} else if (prevGoldScore + 0.001 < goldScore) {
				if (delta.getWeight() <= 0) {
					delta.switchDeltaDirection();
					delta.badDelta(learningState); 
					updated = true;
				}
			}
		} 
		++learningState.timestep;
		return updated;
	}

}
