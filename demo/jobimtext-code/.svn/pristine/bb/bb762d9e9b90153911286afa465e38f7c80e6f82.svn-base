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

import java.util.Random;

/**
 * Should probably go in halfbaked, this is not used or tested at all
 * @author mrglass
 *
 * @param <T>
 */
public class LinearSARSA<T> {
	public interface Features<T> {
		public double[] features(T state, int action);
		public int getNumFeatures();
		public int getNumActions();
		public void setWeights(double[] ws);
	}
	
	private Random rand;
	private Features<T> features;
	
	private int numActions;	
	private double gamma;
	private double learnRate;
	private double epsilon;
	
	private double[] ws;
	private int lastAction;
	private double[] lastFeatureValues = null;
	
	public LinearSARSA(Features<T> features, double gamma, double learnRate, double epsilon) {
		rand = new Random();
		this.features = features;
		ws = new double[features.getNumFeatures()];
		features.setWeights(ws);
		numActions = features.getNumActions();
		lastFeatureValues = new double[features.getNumFeatures()+1];
		this.gamma = gamma;
		this.learnRate = learnRate;
		this.epsilon = epsilon;
	}
	
	private void expectedValue(T state, int action, double[] featureValues) {
		double e = 0;
		double[] fs = features.features(state, action);
		for (int i = 0; i < fs.length; ++i) {
			featureValues[i] = fs[i];
			e += ws[i]*featureValues[i];
		}
		featureValues[fs.length] = e;
	}
	
	public int firstAction(T state) {
		double[] featureValues = new double[lastFeatureValues.length];
		lastFeatureValues[ws.length] = Double.NEGATIVE_INFINITY;
		if (rand.nextDouble() < epsilon) {
			lastAction = rand.nextInt(numActions);
			expectedValue(state, lastAction, lastFeatureValues);
		} else {
			for (int ai = 0; ai < numActions; ++ai) {
				expectedValue(state, ai, featureValues);
				if (featureValues[ws.length] > lastFeatureValues[ws.length]) {
					lastFeatureValues = featureValues;
					lastAction = ai;
				}
			}
		}
		return lastAction;
	}
	
	public int rewardAndNextAction(double reward, T state) {
		int nextAction = 0;
		double[] nextFeatureValues = new double[lastFeatureValues.length];
		if (rand.nextDouble() < epsilon) {
			nextAction = rand.nextInt(numActions);
			expectedValue(state, nextAction, nextFeatureValues);
		} else {
			double[] featureValues = new double[lastFeatureValues.length];
			nextFeatureValues[ws.length] = Double.NEGATIVE_INFINITY;
			for (int ai = 0; ai < numActions; ++ai) {
				expectedValue(state, ai, featureValues);
				if (featureValues[ws.length] > nextFeatureValues[ws.length]) {
					nextFeatureValues = featureValues;
					nextAction = ai;
				}
			}
		}
		double delta = reward + gamma*nextFeatureValues[ws.length] - lastFeatureValues[ws.length];
		for (int i = 0; i < ws.length; ++i) {
			ws[i] = ws[i] + learnRate * delta * lastFeatureValues[i];
		}
		lastAction = nextAction;
		lastFeatureValues = nextFeatureValues;
		return nextAction;
	}
}
