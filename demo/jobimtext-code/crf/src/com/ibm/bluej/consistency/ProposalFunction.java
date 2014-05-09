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

package com.ibm.bluej.consistency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.ibm.bluej.consistency.optimize.FuncLogProbScore;
import com.ibm.bluej.consistency.optimize.Regularizer;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.Updatable;
import com.ibm.bluej.consistency.term.corefunc.NumberFunction;
import com.ibm.bluej.util.common.FirstPairComparator;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;


/**
 * Should be renamed to NumericProposalFunction, there can be other types of proposal functions
 * @author mrglass
 *
 */
public class ProposalFunction extends NumberFunction {	
	private String name;
	
	public String getName() {
		return name;
	}

	public ProposalFunction(String name, double initialValue) {
		this.name = name;
		value = new NumberTerm(initialValue);
		usedBy = new ArrayList<Updatable>();
	}
	
	public boolean isGround(Binds binds) {
		return true;
	}
	
	public void setValue(double v) {
		if (v != value.value) {
			value.value = v;
			passUpdate(); //CONSIDER: could pass a delta update
		}
	}

	private void forceUpdate(double v) {
		value.value = v;
		passUpdate();
	}
	
	public void setSampleValue(double v, int[] sample) {
		value.value = v;
		passSampleUpdate(sample);
	}
	
	//fake ground is intentional, useful when a create contains a proposal function, as in Optimizer	
	public ATerm ground(Binds binds, Updatable neededBy) {
		addUsedBy(neededBy);
		return this;
	}
	
	public int[] getSample(Random r, int numToSample) {
		int[] sample = new int[numToSample];
		for (int i = 0; i < numToSample; ++i) {
			sample[i] = r.nextInt(usedBy.size()); //may contain duplicates but it doesn't much matter
		}
		return sample;
	}
	
	private void passSampleUpdate(int[] sample) {
		for (int sndx : sample) {
			Updatable f = usedBy.get(sndx);
			//Don't pass updates to partially grounded functions
			if (f.isReady()) {
				//recomputeStack.addRecompute(f, this, msg);
				f.update(this);
			}
		}
	}
	
	public void update(Function source, Object... msg) {
		throw new UnsupportedOperationException(); //ProposalFunctions are originating Functions
	}

	public String toString() {
		return (name != null ? name :"ProF") 
				+"="+Lang.dblStr(value.value); 
	}
	
	//TODO: track how often you make an improvement that could not be hillclimbed separately
	public static double jointHillclimb(CRFState crfState, ProposalFunction one, ProposalFunction two, int sampleSize, Regularizer regularize) {
		if (one.instanceIdUsedByAlt == null) {
			one.buildForJoint();
		}
		if (two.instanceIdUsedByAlt == null) {
			two.buildForJoint();
		}
		int[] sampleOne = new int[sampleSize];
		int[] sampleTwo = new int[sampleSize];
		
		if (alternatingIntJointSample(one.instanceIdUsedByAlt, two.instanceIdUsedByAlt, sampleOne, sampleTwo) != sampleSize) {
			return 0;
		}
		
		double maxStepOne = Math.abs(one.value.value+0.1)/2;
		double maxStepTwo = Math.abs(one.value.value+0.1)/2;
		
		boolean change = false;
		double initWeight = crfState.getWeight();
		double centerOne = one.value.value;
		double centerTwo = two.value.value;
		double initOne = centerOne;
		double initTwo = centerTwo;
		
		for (int i = 0; i < 20; ++i) {
			double stepOne = (Math.random()-0.5) * maxStepOne;
			double stepTwo = (Math.random()-0.5) * maxStepTwo;
			double prevWeight = crfState.getWeight();
			one.setSampleValue(centerOne+stepOne, sampleOne);
			two.setSampleValue(centerTwo+stepTwo, sampleTwo);
			if (crfState.getWeight() > prevWeight) {
				centerOne += stepOne;
				centerTwo += stepTwo;
				change = true;
			}
		}
		
		if (change) {
			one.forceUpdate(centerOne);
			two.forceUpdate(centerTwo);
		} else {
			one.setSampleValue(centerOne, sampleOne);
			two.setSampleValue(centerTwo, sampleTwo);
		}	
		
		return (crfState.getWeight() - initWeight) + 
				regularize.regularizeDelta(initOne, centerOne) + 
				regularize.regularizeDelta(initTwo, centerTwo);
	}
	
	private int[] instanceIdUsedByAlt;
	private void buildForJoint() {
		//first build pairs, sort by FirstPairComparator
		ArrayList<Pair<Integer,Integer>> instanceIdUsedByNdxPair = new ArrayList<Pair<Integer,Integer>>();		
		for (int i = 0; i < usedBy.size(); ++i) {
			instanceIdUsedByNdxPair.add(Pair.make(getInstanceId(usedBy.get(i)), i));
		}
		Collections.sort(instanceIdUsedByNdxPair, new FirstPairComparator());
		
		instanceIdUsedByAlt = new int[usedBy.size()*2];
		for (int i = 0; i < instanceIdUsedByNdxPair.size(); ++i) {
			Pair<Integer,Integer> p = instanceIdUsedByNdxPair.get(i);
			instanceIdUsedByAlt[2*i] = p.first;
			instanceIdUsedByAlt[2*i+1] = p.second;
		}
	}
	
	/**
	 * WARNING: only applies in the SampleWalkLogistic optimization!
	 * assumes that this proposal function is used by FuncLogProgScore specifically
	 * @return
	 */
	public double percentPositiveWhereOccurrs() {
		double positive = 0;
		for (Updatable f : usedBy) {
			if (isPositiveInstance(f))
				++positive;
		}
		return positive / usedBy.size();
	}
	
	private static Boolean isPositiveInstance(Updatable f) {
		if (f instanceof FuncLogProbScore) {
			return ((FuncLogProbScore)f).isPositiveInstance();
		}
		for (Updatable fu : f.getUsedBy()) {
			Boolean ans = isPositiveInstance(fu);
			if (ans != null) {
				return ans;
			}
		}
		return null;
	}
	
	private static int getInstanceId(Updatable f) {
		if (f instanceof FuncLogProbScore) {
			return ((FuncLogProbScore)f).instanceNdx;
		}
		for (Updatable fu : f.getUsedBy()) {
			int iid = getInstanceId(fu);
			if (iid != -1) {
				return iid;
			}
		}
		return -1;
	}
	
	private static int alternatingIntJointSample(int[] first, int[] second, int[] firstSample, int[] secondSample) {
		assert(firstSample.length == secondSample.length);
		int si = 0;
		int fi = 0;
		int numFound = 0;
		double numSeen = 0;
		while (fi < first.length && si < second.length) {
			if (first[fi] == second[si]) {
				++numSeen;
				if (numFound >= firstSample.length) {	
					if (SGSearch.rand.nextDouble() < numFound/numSeen) {
						int ndx = SGSearch.rand.nextInt(firstSample.length);
						firstSample[ndx] = first[fi+1];
						secondSample[ndx] = second[si+1];
					}
				} else {
					firstSample[numFound] = first[fi+1];
					secondSample[numFound] = second[si+1];
					++numFound;
				}
				fi += 2;
				si += 2;
			} else if (first[fi] > second[si]) {
				si += 2;
			} else {
				fi += 2;
			}
		}
		return numFound;
	}
	
	//TODO: step 1 for jointSample is to build an array of instance ids to match the usedBys
	//  or create Map<Updatable, Integer>, then create the corresponding arrays for each proposal function
	//public static double jointSample()
	//from the formulas build a instancedId -> Set<ProposalFunction>
	//  when you pair two ProposalFunctions you have a set of instanceIds where they occur - mapped to the Updatable
	//  two possibilities: take intersection first, sample with rejection
	//    could cache all intersections? - let's cache all intersections
	
	public double tryZero(CRFState crfState, Random rand, int sampleSize, Regularizer regularize) {
		if (usedBy.size()/2 < sampleSize) {
			return tryZero(crfState, rand, regularize);
		}
		int[] sample = getSample(rand, sampleSize);
		double weightMult = (double)usedBy.size()/sampleSize;
		double initWeight = crfState.getWeight();
		double initValue = value.value;
		setSampleValue(0, sample);
		double regDelt = regularize.regularizeDelta(initValue, 0);
		if ((crfState.getWeight()-initWeight)*weightMult + regDelt > 0) {
			forceUpdate(0);
			return crfState.getWeight() - initWeight + regDelt;
		} else {
			setSampleValue(initValue, sample);
			return 0;
		}
	}
	public double tryZero(CRFState crfState, Random rand,  Regularizer regularize) {		
		double initWeight = crfState.getWeight();
		double initValue = value.value;
		setValue(0);
		return (crfState.getWeight()-initWeight) + regularize.regularizeDelta(initValue, 0);
	}

	public double hillClimb(CRFState crfState, Random rand, Regularizer regularize) {
		double maxStep = Math.abs(value.value+0.1)/2;
		double centerValue = value.value;
		double initWeight = crfState.getWeight();
		double initValue = centerValue;
		for (int i = 0; i < 40; ++i) {
			double prevWeight = crfState.getWeight();
			double step = (rand.nextDouble()-0.5) * maxStep;
			setValue(centerValue - step);
			
			if ((crfState.getWeight() - prevWeight) + regularize.regularizeDelta(centerValue, value.value) > 0) {
				centerValue = value.value;
			}
		}		
		return crfState.getWeight() - initWeight + regularize.regularizeDelta(initValue, value.value);
	}
	
	public double hillClimb(CRFState crfState, Random rand, int sampleSize, Regularizer regularize) {
		if (sampleSize > usedBy.size()/2) {
			return hillClimb(crfState, rand, regularize);
		}
		int[] sample = getSample(rand, sampleSize);
		double weightMult = (double)usedBy.size()/sampleSize;
		
		double maxStep = Math.abs(value.value+0.1)/2;

		double centerValue = value.value;
		double initWeight = crfState.getWeight();
		double initValue = centerValue;

		for (int i = 0; i < 40; ++i) {
			double prevWeight = crfState.getWeight();
			double step = (rand.nextDouble()-0.5) * maxStep;
			setSampleValue(centerValue - step, sample);
			
			if ((crfState.getWeight() - prevWeight)*weightMult + regularize.regularizeDelta(centerValue, value.value) > 0) {
				centerValue = value.value;
			}
		}
		if (centerValue != initValue) {
			forceUpdate(centerValue);
			return crfState.getWeight() - initWeight + regularize.regularizeDelta(initValue, centerValue);
		} else {
			setSampleValue(initValue, sample);
			return 0;
		}
		
	}
	
	public double hillClimbOld(CRFState crfState, Random rand, int sampleSize, Regularizer regularize) {
		int[] sample = getSample(rand, sampleSize);
		double weightMult = (double)usedBy.size()/sampleSize;
		
		double initialStep = rand.nextDouble()/4 * Math.abs(value.value+0.1);
		if (initialStep == 0) {
			initialStep = 0.001;
		}
		double minStep = initialStep/100;
		
		double stepSize = initialStep;
		double centerValue = value.value;
		double initWeight = crfState.getWeight();
		double initValue = centerValue;
		int direction = 0;
		while (stepSize > minStep && Math.abs(initValue - centerValue) < 4*initialStep) {
			double prevWeight = crfState.getWeight();
			if (direction == -1) {
				setSampleValue(centerValue - stepSize, sample);
			} else if (direction == 1) {
				setSampleValue(centerValue + stepSize, sample);
			} else {
				setSampleValue(centerValue + stepSize, sample);
				if ((crfState.getWeight() - prevWeight)*weightMult + regularize.regularizeDelta(centerValue, value.value) > 0) {
					direction = 1;
				} else {
					setSampleValue(centerValue - stepSize, sample);
					if ((crfState.getWeight() - prevWeight)*weightMult + regularize.regularizeDelta(centerValue, value.value) > 0) {
						direction = -1;
					}
				}
			}
			if ((crfState.getWeight() - prevWeight)*weightMult + regularize.regularizeDelta(centerValue, value.value) < 0) {
				stepSize /= 2;
				direction = 0;
			} else {
				centerValue = value.value;
			}
		}
		if (centerValue != initValue) {
			forceUpdate(centerValue);
		} else {
			setSampleValue(initValue, sample);
		}
		return crfState.getWeight() - initWeight + regularize.regularizeDelta(initValue, centerValue);
	}
	
	public double scanMAP(CRFState crfState, Random rand, double min, double max, int tries, int sampleSize) {
		//don't bother unless we are at least doubling speed
		if (sampleSize > usedBy.size()/2) {
			return scanMAP(crfState,rand,min,max,tries);
		}
		
		int[] sample = getSample(rand, sampleSize);
		//CONSIDER: set focus indicators false?
		
		double initWeight = crfState.getWeight();
		double initValue = value.value;
		double bestWeightGain = 0;
		double bestValue = initValue;
		double bestCount = 1;
		double range = max - min;
		for (int i = 0; i < tries; ++i) {
			double prevValue = value.value;
			double value = (rand.nextDouble()*range)+min; 
			setSampleValue(value, sample);
			double weightGain = (crfState.getWeight()-initWeight);
			if (weightGain > bestWeightGain + 0.001) {
				bestValue = value;
				bestWeightGain = weightGain;
				bestCount = 1.0;	
			} else if (bestWeightGain - weightGain <= 0.001) {
				bestCount += 1.0;
				bestValue = (bestCount-1)/bestCount * bestValue + 1.0/bestCount * value;
			}
		}
		if (bestValue != initValue) {
			forceUpdate(bestValue);
		} else {
			setSampleValue(initValue, sample);
		}
		return crfState.getWeight() - initWeight;
	}
	
	public double scanMAP(CRFState crfState, Random rand, double min, double max, int tries, Regularizer regularize) {
		double initWeight = crfState.getWeight();
		double initValue = value.value;
		double bestWeightGain = 0;
		double bestValue = initValue;
		double bestCount = 1;
		double range = max - min;
		for (int i = 0; i < tries; ++i) {
			double value = (rand.nextDouble()*range)+min;
			setValue(value);
			double weightGain = (crfState.getWeight()-initWeight) + regularize.regularizeDelta(initValue, value);
			if (weightGain > bestWeightGain + 0.001) {
				bestValue = value;
				bestWeightGain = weightGain;
				bestCount = 1.0;	
			} else if (bestWeightGain - weightGain <= 0.001) {
				bestCount += 1.0;
				bestValue = (bestCount-1)/bestCount * bestValue + 1.0/bestCount * value;
			}
		}
		setValue(bestValue);
		return crfState.getWeight() - initWeight + regularize.regularizeDelta(initValue, bestValue);
	}
	public double scanMAP(CRFState crfState, Random rand, double min, double max, int tries, int sampleSize, Regularizer regularize) {
		//don't bother unless we are at least doubling speed
		if (sampleSize > usedBy.size()/2) {
			return scanMAP(crfState,rand,min,max,tries,regularize);
		}
		
		int[] sample = getSample(rand, sampleSize);
		double weightMult = (double)usedBy.size()/sampleSize;
		//CONSIDER: set focus indicators false?
		
		double initWeight = crfState.getWeight();
		double initValue = value.value;
		double bestWeightGain = 0;
		double bestValue = initValue;
		double bestCount = 1;
		double range = max - min;
		for (int i = 0; i < tries; ++i) {
			double value = (rand.nextDouble()*range)+min;
			setSampleValue(value, sample);
			double weightGain = (crfState.getWeight()-initWeight)*weightMult + regularize.regularizeDelta(initValue, value);
			if (weightGain > bestWeightGain + 0.001) {
				bestValue = value;
				bestWeightGain = weightGain;
				bestCount = 1.0;	
			} else if (bestWeightGain - weightGain <= 0.001) {
				bestCount += 1.0;
				bestValue = (bestCount-1)/bestCount * bestValue + 1.0/bestCount * value;
			}
		}
		if (bestValue != initValue) {
			forceUpdate(bestValue);
		} else {
			setSampleValue(initValue, sample);
		}
		return crfState.getWeight() - initWeight + regularize.regularizeDelta(initValue, bestValue);
	}
	
	public double scanMAP(CRFState crfState, Random rand, double min, double max, int tries) {
		double initWeight = crfState.getWeight();
		double bestWeight = initWeight;
		double bestValue = value.value;
		double bestCount = 1;
		double range = max - min;
		for (int i = 0; i < tries; ++i) {
			double value = (rand.nextDouble()*range)+min; 
			setValue(value);
			double weight = crfState.getWeight();
			if (weight > bestWeight + 0.001) {
				bestValue = value;
				bestWeight = weight;
				bestCount = 1.0;	
			} else if (bestWeight - weight <= 0.001) {
				bestCount += 1.0;
				bestValue = (bestCount-1)/bestCount * bestValue + 1.0/bestCount * value;
			}
		}
		setValue(bestValue);
		return crfState.getWeight() - initWeight;
	}
	

}
