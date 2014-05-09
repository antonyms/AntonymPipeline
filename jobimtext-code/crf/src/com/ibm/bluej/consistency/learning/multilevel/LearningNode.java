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

package com.ibm.bluej.consistency.learning.multilevel;

import com.ibm.bluej.consistency.learning.LearningState;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.util.common.IdentitySet;

//TODO: extend NumberFunction
public class LearningNode {
	//TODO: try dense parameter averaging
	public static double learningRate = 0.1; //typical values range from 0.2 to 0.5 (at least for sigmoid)
	
	//inputs with weights
	ParamWeight[] weights;
	Object[] inputs; //inputs are either LearningNode or Double, TODO: should be parts; use Term.getDouble
	
	//output function
	OutputFunction nonlinear; //actually it is allowed to be linear
	
	//outgoing links
	LearningNode[] outgoing; //TODO: convert to usedBy
	
	//book keeping
	double sumError = 0;
	double output = Double.NaN;
	boolean finishedBackprop = false;
	
	//TODO: big changes needed to the learnable interface... can't pushDelta on all the LearningNodes like you can on ParamWeights
	//Only valid to call this on final output node
	public void pushDelta(double delta, LearningState learningState) {
		assert (outgoing.length == 0);
		sumError = delta;
		IdentitySet<LearningNode> recomputeSet = new IdentitySet<LearningNode>();
		recomputeSet.add(this);
		this.backPropagate(recomputeSet);
		IdentitySet<LearningNode> moreToRecompute = new IdentitySet<LearningNode>();
		boolean finishedAll = false;
		while (!finishedAll) {
			finishedAll = true;
			moreToRecompute.clear();
			for (LearningNode l : recomputeSet) {
				if (!l.finishedBackprop && l.allOutgoingFinished()) {
					finishedAll = false;
					l.backPropagate(moreToRecompute);
				}
			}
			recomputeSet.addAll(moreToRecompute);
		}
		for (LearningNode l : recomputeSet) {
			l.updateWeights(learningState);
		}
		this.reset();
	}
	
	//full recompute
	public double getValue() {
		if (!Double.isNaN(output)) {
			return output;
		}
		output = nonlinear.value(getInputSum());
		return output;
	}
	
	public boolean allOutgoingFinished() {
		for (LearningNode ln : outgoing) {
			if (!ln.finishedBackprop) {
				return false;
			}
		}
		return true;
	}
	
	private double getInputSum() {
		double sum = 0;
		for (int i = 0; i < inputs.length; ++i) {
			if (inputs[i] instanceof LearningNode) {
				sum += weights[i].getWeight() * ((LearningNode)inputs[i]).getValue();
			} else {
				sum += weights[i].getWeight() * (Double)inputs[i];
			}
		}
		return sum;
	}
	
	public boolean backPropagate(IdentitySet<LearningNode> recomputeSet) {
		if (finishedBackprop || !allOutgoingFinished()) {
			return false;
		}	
		double gradient = nonlinear.derivative(getInputSum());
		for (int i = 0; i < inputs.length; ++i) {
			//add to their sumError
			if (inputs[i] instanceof LearningNode) {
				LearningNode xji = (LearningNode)inputs[i];
				ParamWeight wji = weights[i];
				xji.sumError += gradient * wji.getWeight() * sumError;
				recomputeSet.add(xji);
			}
		}
		finishedBackprop = true;
		return true;
	}
	
	private double getInput(int i) {
		if (inputs[i] instanceof LearningNode) {
			return ((LearningNode)inputs[i]).output;
		}
		return (Double)inputs[i];
	}
	
	public void updateWeights(LearningState learningState) {
		if (sumError == 0) {
			return;
		}
		for (int i = 0; i < inputs.length; ++i) {
			//change weights
			weights[i].pushDelta(learningRate*sumError*getInput(i), learningState);
		}
	}
	
	public void reset() {
		sumError = 0;
		output = Double.NaN; 
		finishedBackprop = false; //reset this flag	
		for (int i = 0; i < inputs.length; ++i) {
			if (inputs[i] instanceof LearningNode) {
				LearningNode li = (LearningNode)inputs[i];
				if (!Double.isNaN(li.output)) {
					li.reset();
				}
			}
		}
	}
	
	//TODO: test this before fitting it into Consistency's learning frameworks
}
