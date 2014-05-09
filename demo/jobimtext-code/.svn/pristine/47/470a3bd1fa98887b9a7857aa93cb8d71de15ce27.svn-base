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

package com.ibm.bluej.consistency.old;

import java.util.ArrayList;

import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;

//TODO: move to old

//TODO: this class is out of date, don't use without major rewrite
//INVARIANTS:
//  all weights referenced are up-to-date (recompute wise)
//  the main weight is the current weight of the search
//  timeSortedDeltas and weightSortedDeltas contain exactly the same elements
//  weightSortedDeltas are always sorted by both weight and goldScore
//    if an addition forces this ordering to break, we do an update (of any kind)
//    clear both delta lists and set the zeroGoldScore to the last delta gold score, recompute the main
//  timeSortedDeltas contains at most the last maxDeltas deltas
//    if a new one is added, the oldest is removed from both lists
//  the currentDelta is not in the delta lists
/**
 * @deprecated
 * @author mrglass
 *
 */
public class DeltaList {//implements IDeltaWeight {
	//CONSIDER: also hold bestWeight?
	//No, just record whether it has been recomputed since last check
	//CONSIDER: hold average
	
	long timestep;
	
	public DeltaList(int maxDeltas) {
		this.maxDeltas = maxDeltas;
	}
	
	public void updateBestWeight(IWorldWeight bestWeight) {
		if (recentParamChange) {
			//((WorldWeightParam)bestWeight).recompute();
			recentParamChange = false;
		}
	}
	public IWorldWeight recordWeight() {
		return main.clone();
	}
	private static class Delta {
		WorldWeightParam d = new WorldWeightParam();
		double baseWeight;
		double goldScore;
		double getWeight() {
			return baseWeight + d.getWeight();
		}
	}
	
	public void clear() {
		recentParamChange = false;
		zeroGoldScore = 0;
		main = new WorldWeightParam();
		timeSortedDeltas.clear();
		weightSortedDeltas.clear();
		currentDelta = null;
	}
	
	private boolean recentParamChange = false;
	
	private double zeroGoldScore; //the goldScore of the implicit zero delta
	
	private Delta currentDelta;
	
	private int maxDeltas;
	private WorldWeightParam main = new WorldWeightParam(); //this is always up to date
	
	//this only exists to allow easy removal of the oldest weight from weightSortedDeltas
	//and to merge deltas
	private ArrayList<Delta> timeSortedDeltas = new ArrayList<Delta>(maxDeltas); 
	private ArrayList<Delta> weightSortedDeltas = new ArrayList<Delta>(maxDeltas); 
	//when adding a new weight we linear search for best position
	//then check the goldScore on either side
	
	/**
	 * Add to main and current delta
	 * @param v
	 */
	public void addWeight(double v) {
		main.update(v);
		if (currentDelta != null) {
			currentDelta.d.update(v);
		}
	}
	public void addWeight(ParamWeight p, double v) {
		main.update(p,v);
		if (currentDelta != null) {
			currentDelta.d.update(p,v);
		}
	}
	
	/**
	 * @returns the current weight of the search
	 */
	public double getWeight() {
		return main.getWeight();
	}
	
	private WorldWeightParam merge(Delta after, Delta last) {
		boolean isAfter = false;
		for (Delta d : timeSortedDeltas) {
			if (isAfter) {
				last.d.merge(d.d);
			} else if (d == after) {
				isAfter = true;
			}
		}
		return last.d;
	}
	
	private void goodDelta(WorldWeightParam d) {
		//d.switchDeltaDirection();
		//d.badDelta(timestep++);
		recentParamChange = true;
	}
	private void badDelta(WorldWeightParam d) {
		//d.badDelta(timestep++);
		recentParamChange = true;
	}
	
	private int findSpotToAdd(Delta d) {
		if (weightSortedDeltas.size() == 0) {
			if (zeroGoldScore < d.goldScore) {
				if (d.d.getWeight() <= 0) {
					goodDelta(d.d); return -1;
				}
			} else if (zeroGoldScore > d.goldScore) {
				if (d.d.getWeight() >= 0) {
					badDelta(d.d); return -1;
				}
			}
			return 0;
		}
		assert (weightSortedDeltas.size() <= maxDeltas);
		for (int i = 0; i < weightSortedDeltas.size(); ++i) {
			Delta di = weightSortedDeltas.get(i);
			if (di.goldScore < d.goldScore) {
				//check if weights are mis-ordered
				if (di.getWeight() >= d.getWeight()) {
					goodDelta(merge(di,d)); return -1;
				}
			} else if (di.goldScore > d.goldScore) {
				//check if weights are mis-ordered
				if (di.getWeight() <= d.getWeight()) {
					badDelta(merge(di,d)); return -1;
				}
				return i;
			}
		}		
		return weightSortedDeltas.size();
	}
	
	private boolean addDelta(Delta d) {	
		//now position in weightSortedDeltas
		int addAt = findSpotToAdd(d);	
		if (addAt < 0) {
			if (timeSortedDeltas.size() == 0) {
				zeroGoldScore = currentDelta.goldScore;
			} else {
				zeroGoldScore = timeSortedDeltas.get(0).goldScore;
			}
			timeSortedDeltas.clear();
			weightSortedDeltas.clear();
			//main.recompute();
			return true;
		} else {
			weightSortedDeltas.add(addAt, d);
			timeSortedDeltas.add(d);
			return false;
		}
	}
	private void removeDelta() {
		Delta toRemove = timeSortedDeltas.remove(0);
		if (!weightSortedDeltas.remove(toRemove)) {
			throw new Error("Could not removeDelta from weight sorted!");
		}
		zeroGoldScore = toRemove.goldScore;
	}
	
	/**
	 * Called between coordinated changes made by the proposal distribution.
	 * The Gold Score (data or ground truth) is now goldScore
	 * also called on the first delta, right after set-up
	 * and on the last delta
	 * @param goldScore larger is better
	 */
	public boolean nextDelta(double goldScore, double prevGoldScore) {
		throw new Error("Doesn't work right now. Can't deal with badDelta called without an accept");
		/*
		//This is where updates will occur, if any
		boolean updated = false;
		if (currentDelta != null) {
			currentDelta.goldScore = goldScore;
			updated = addDelta(currentDelta);
			if (timeSortedDeltas.size() > maxDeltas) {
				removeDelta();
			}
		} else {
			timestep = (int)ParamWeight.instance(SingleDelta.TIMESTEP_KEY).getValue() + 1;
			zeroGoldScore = goldScore;
		}
		currentDelta = new Delta();
		currentDelta.baseWeight = main.getWeight();	
		return updated;
		*/
	}

}
