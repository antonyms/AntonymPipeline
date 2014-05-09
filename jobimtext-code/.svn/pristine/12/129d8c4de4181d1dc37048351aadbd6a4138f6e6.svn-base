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


/**
 * Only makes sense if doing MAP inference and learning at the same time
 * Otherwise either DeltaOnly or FixedDeltaWeight is the right choice
 * @author mrglass
 *
 */
public class SingleDelta implements IDeltaWeight {
	private WorldWeightParam main = new WorldWeightParam();
	private WorldWeightParam delta;
	private boolean recentParamChange = false;
	
	public String deltaString() {
		return delta == null ? "NO DELTA" : delta.toString();
	}
	
	public SingleDelta() {
		//timestep = (int)ParamWeight.instance(SingleDelta.TIMESTEP_KEY).value.value + 1;
	}
	
	public String toString() {
		return main.toString();
	}
	
	public IWorldWeight recordWeight() {
		return main.clone();
	}
	public void updateBestWeight(IWorldWeight bestWeight) {
		if (recentParamChange) {
			((WorldWeightParam)bestWeight).recompute();
			recentParamChange = false;
		}
	}

	public void clear() {
		recentParamChange = false;
		main = new WorldWeightParam();
		delta = null;
	}
	
	public void addWeight(double v) {
		main.update(v);
		if (delta != null) {
			delta.update(v);
		}
	}
	public void addWeight(Learnable p, double v) {
		main.update(p,v);
		if (delta != null) {
			delta.update(p,v);
		}
	}

	public double getWeight() {
		return main.getWeight();
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
					main.recompute();recentParamChange = true;
					updated = true;
				}
			} else if (prevGoldScore + 0.001 < goldScore) {
				if (delta.getWeight() <= 0) {
					delta.switchDeltaDirection();
					delta.badDelta(learningState); 
					main.recompute();recentParamChange = true;
					updated = true;
				}
			}
		} 
		++learningState.timestep;
		return updated;
	}	

}
