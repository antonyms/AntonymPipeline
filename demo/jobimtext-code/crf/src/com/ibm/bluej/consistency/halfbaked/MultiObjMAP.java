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


/**
 * Not tested, probably not working
 * @author mrglass
 *
 */
public class MultiObjMAP {
	/*
	public static double VARY_INC = 0.1;
	//NOTE: don't bother with weight updating, we don't SampleRank in MultiObj
	//Instead we can use the Thetas to group our multiObj factors
	//actually we will do weight updating to track the current and max weight of each solution
	
	//we need to specify exploration intervals for our Thetas
	//at construction, give a list of Term[] (the specifiers on the Thetas) and intervals to explore (and step sizes?)
	//also specify the maximum number of solutions we wish to test
	public MultiObjMAP(int maxSolutions, double variance, MultiObjInitializer moi) {
		maxPossibleBest = maxSolutions;
		objectiveWeights = moi.getParamWeights();
		weightMins = new double[objectiveWeights.length];
		weightMaxs = new double[objectiveWeights.length];
		for (int i = 0; i < objectiveWeights.length; ++i) {
			weightMins[i] = objectiveWeights[i].value.value - variance;
			weightMaxs[i] = objectiveWeights[i].value.value + variance;
		}
		for (int i = 0; i < objectiveWeights.length; ++i) {
			objectiveWeights[i].value.value = RandomUtil.randomDbl(weightMins[i], weightMaxs[i]);
		}
		VARY_INC = variance / 5.0;
	}
	
	public Pair<ArrayList<MultiObjSolution>, double[]> getSolutions() {
		ArrayList<MultiObjSolution> solutions = new ArrayList<MultiObjSolution>();
		for (PossibleBest pb : bestSolutions) {
			MultiObjSolution s = new MultiObjSolution(objectiveWeights, pb.terms, (WorldWeightParam)pb.currentWeight);
			solutions.add(s);
		}
		double[] objWeights = new double[objectiveWeights.length];
		for (int i = 0; i < objWeights.length; ++i) {
			objWeights[i] = objectiveWeights[i].value.value;
		}
		return new Pair<ArrayList<MultiObjSolution>, double[]>(solutions, objWeights);
	}
	
	private int maxPossibleBest;
	private ParamWeight[] objectiveWeights;
	private double[] weightMins;
	private double[] weightMaxs;
	
	
	private ArrayList<PossibleBest> bestSolutions = new ArrayList<PossibleBest>();
	private double overallCurrentMax = Double.NEGATIVE_INFINITY;
	
	public void clear() {
		bestSolutions.clear();
		overallCurrentMax = Double.NEGATIVE_INFINITY;
	}
	
	public IWorldWeight getWorldWeight() {
		throw new UnsupportedOperationException();
	}
	
	public class PossibleBest {
		PossibleBest(ArrayList<ScanTerm> terms, IWorldWeight weight) {
			this.terms = terms;
			this.currentWeight = weight;
			this.maxWeightEver = weight.getWeight();
			this.avgWeight = maxWeightEver;
			rescoreCount = 1;
		}
		ArrayList<ScanTerm> terms;
		private IWorldWeight currentWeight;
		double maxWeightEver;
		double avgWeight; 
		double rescoreCount = 0;
		void rescore() {
			weight.updateBestWeight(currentWeight);
			double cweight = currentWeight.getWeight();
			if (cweight > maxWeightEver) {
				maxWeightEver = cweight;
			}
			rescoreCount += 1.0;
			avgWeight = (rescoreCount-1)/rescoreCount * avgWeight + 1/rescoreCount * cweight;
			if (cweight > overallCurrentMax) {
				overallCurrentMax = cweight;
			}
		}
	}
	
	
	
	private void addPossibleBest(PossibleBest pb) {
		//add it to list, also keep the list smallish
		// maintain average weight for each PossibleBest, remove those with the smallest averageWeight
		// this avoids the tricky computation of checking if a solution is dominated
		if (bestSolutions.size() >= maxPossibleBest) {
			int minNdx = 0;
			double minWeight = Double.MAX_VALUE;
			for (int i = 0; i < bestSolutions.size(); ++i) {
				if (bestSolutions.get(i).avgWeight < minWeight) {
					minWeight = bestSolutions.get(i).avgWeight;
					minNdx = i;
				}
			}
			bestSolutions.set(minNdx, pb);
		} else {
			bestSolutions.add(pb);
		}
	}
	
	public void varyObjectiveWeights() {
		//select random direction to vary each objective function
		//then rescore every PossibleBest and find new overallCurrentMax
		double oneNorm = 0;
		for (int i = 0; i < objectiveWeights.length; ++i) {
			double action = Math.random();
			if (action < 0.33 && objectiveWeights[i].value.value > weightMins[i] - VARY_INC) {
				objectiveWeights[i].value.value -= VARY_INC;
			} else if (action < 0.66 && objectiveWeights[i].value.value < weightMaxs[i] + VARY_INC) {
				objectiveWeights[i].value.value += VARY_INC;
			}
			oneNorm += Math.abs(objectiveWeights[i].value.value);
		}
		//renormalize to objectiveWeights.length (avg |weight| will be 1)
		double normalize = objectiveWeights.length / oneNorm;
		for (int i = 0; i < objectiveWeights.length; ++i) {
			objectiveWeights[i].value.value *= normalize;
		}
		overallCurrentMax = Double.NEGATIVE_INFINITY;
		for (PossibleBest pb : bestSolutions) {
			pb.rescore();
		}
	}
	
	public double getBestWeight() {
		return overallCurrentMax;
	}
	
	public boolean checkBest() {
		if (!SGSearch.getFocusIndicators().safeToSave()) {
			return false;
		}
		if (weight.getWeight() > overallCurrentMax) {
			//save as new solution
			ArrayList<ScanTerm> bestTerms = new ArrayList<ScanTerm>();
			Iterator<IndexEntry> pit = posGrnd.getGroundTerms();
			while (pit.hasNext()) {
				IndexEntry e = pit.next();
				if (!(e instanceof DefIndexEntry)) {
					bestTerms.add(e.term);
				}
			}
			addPossibleBest(new PossibleBest(bestTerms, weight.recordWeight()));
			overallCurrentMax = weight.getWeight();
			return true;
		}
		return false;
	}

	//TODO:
	public void trackProposalFunction(String name, ProposalFunction f) {
		throw new UnsupportedOperationException("TODO");
	}
*/
}
