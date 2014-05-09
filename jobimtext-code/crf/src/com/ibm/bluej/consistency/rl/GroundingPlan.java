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

package com.ibm.bluej.consistency.rl;

import java.util.ArrayList;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.util.common.RandomUtil;


/*
groundingPlan, learned transitions from (binary) found[]
  not TD, just monte carlo
  after a while replace with fixed groundingPlan
  found[] -> ndx to
  for learning version
    vector where action is index to (value/count) vector
    learning version is epsilon greedy, with decreasing epsilon
  for fixed version
    just number of action
*/

public class GroundingPlan {
	public static final int DONE = -1;
	public static final int FAIL = -2;
	
	//CONSIDER: be totally random, need to fully explore, then we will use FixedGroundingPlan
	private static final double EPSILON = 0.1;
	
	//use System.nanoTime()
	public void reward(long nanoDiff) {
		//reward the trace, 	
		double reward = nanoDiff/100;
		for (int ti = 0; ti < tracePos; ti += 2) {
			int found = trace[ti];
			double next = trace[ti+1];
			double[] options = foundToNext[found];
			for (int oi = 0; oi < options.length; oi += 2) {
				if (options[oi] == next) {
					//recency weighted
					options[oi+1] = 0.9 * options[oi+1] + 0.1 * reward;
					break;
				}
			}
		}
		//clear trace
		tracePos = 0;
	}
	
	private void addTrace(int found, int next) {
		//record the transition
		trace[tracePos] = found;
		trace[tracePos+1] = next;
		tracePos += 2;
	}
	
	public GroundingPlan(int conditionLength) {
		//with 2 conditions:
		//[[0 0.0 1 0.0] [0 0.0] [1 0.0] []]
		//construct foundToNext based on number of conditions in formula
		foundToNext = new double[1<<conditionLength][];
		for (int fi = 0; fi < foundToNext.length; ++fi) {
			ArrayList<Integer> nextOptions = new ArrayList<Integer>();
			int moreFi = fi;
			for (int ci = conditionLength-1; ci >= 0; --ci) {
				if (moreFi%2 == 0) {
					nextOptions.add(ci);
				}
				moreFi = moreFi >> 1;
			}
			double[] options = new double[nextOptions.size()*2];
			for (int oi = 0; oi < options.length; oi += 2) {
				options[oi] = nextOptions.get(oi/2);
			}
			foundToNext[fi] = options;
		}
		trace = new int[conditionLength*2];
		tracePos = 0;
	}
	
	//alternates nextNdx and value
	private double[][] foundToNext;
	private int[] trace;
	private int tracePos;
	
	public int getNext(IndexEntry[] found) {
		int ndx = 0;
		for (IndexEntry e : found) {
			ndx = ndx << 1;
			if (e != null) {
				ndx += 1;
			}		
		}
		if (ndx == foundToNext.length-1) {
			return DONE;
		}
		double[] options = foundToNext[ndx];
		//all options have been perm. rejected
		if (options.length == 0) {
			return FAIL;
		}
		//only one viable option, nothing to learn here
		if (options.length == 2) {
			return (int)options[0];
		}
		double bestNdx = -1;
		if (Math.random() < EPSILON) {
			//random option
			int r = RandomUtil.randomInt(0, options.length/2);
			bestNdx = options[r<<1];
		} else {
			//Loop through options and be greedy
			double bestValue = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < options.length; i += 2) {
				if (options[i+1] > bestValue) {
					bestNdx = options[i];
					bestValue = options[i+1];
				}
			}
		}
		int nextNdx = (int)bestNdx;
		addTrace(ndx, nextNdx);
		return nextNdx;
	}
	

	
	public void permanentReject(IndexEntry[] found, int recommended) {
		//in case something is ungroundable, prevent it from being suggested again
		int ndx = 0;
		for (IndexEntry e : found) {
			ndx = ndx << 1;
			if (e != null) {
				ndx += 1;
			}		
		}
		double recValue = recommended;
		double[] oldOptions = foundToNext[ndx];
		double[] newOptions = new double[oldOptions.length - 2];
		int ni = 0;
		for (int oi = 0; oi < oldOptions.length; oi += 2) {
			if (oldOptions[oi] == recValue) {
				continue;
			}
			newOptions[ni] = oldOptions[oi];
			newOptions[ni+1] = oldOptions[oi+1];
			ni += 2;
		}
		foundToNext[ndx] = newOptions;
	}
	
}
