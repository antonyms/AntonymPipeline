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

import java.util.Random;

import com.ibm.bluej.consistency.learning.LearningState;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.util.common.MutableDouble;


public class TiedBackpropagation {
	//Works as a normal neural net just fine
	//  need to test the tied parameter training though
	
	//TODO: immediate need is to train STR
	// also need for the envisioned DCL based term matcher
	
	//try some basic 
	public static void main(String[] args) {
		LearningState learningState = new LearningState();
		ParamWeight.MAX_LEARNED_WEIGHT = 100; //For these weights it is ok (reasonable) to learn very large values
		//function fitting tests
		//create basic three layer
		//number of nodes in each layer equal to number of inputs?
		//no tied parameters
		int numInputs = 2;
		MutableDouble gensym = new MutableDouble(0);
		Double[] inputs = new Double[numInputs+1];
		inputs[numInputs] = 1.0;
		LearningNode ln = makeNN(inputs, gensym, learningState);
		double sumError = 0; int averageover = 0;
		//for some number of training data...
		for (int i = 0; i < 100000; ++i) {
			double c = testf2(inputs);
			double p = ln.getValue();
			sumError += Math.abs(c-p); ++averageover;
			if (i == 40 || i % 1000 == 0) {
				System.out.println("c = "+c+" p = "+p);
				System.out.println(sumError/averageover);
				sumError = 0;
				averageover = 0;
				//SGSearch.setParametersToAverage(); //tried it, didn't seem to work
			}
			//if (i > 25) {
			//System.out.println("c = "+c+" p = "+p);
			//System.out.println(SGSearch.stringParameters());
			ln.pushDelta(c-p, learningState);
			//System.out.println(SGSearch.stringParameters());
			//}
			ln.reset();
		}
		//System.out.println(SGSearch.stringParameters());	
	}
	
	private static LearningNode makeNN(Double[] inputs, MutableDouble gensym, LearningState learningState) {
		int numInputs = inputs.length;
		LearningNode[] inputLayer = new LearningNode[numInputs];
		LearningNode[] hiddenLayer = new LearningNode[numInputs*2];
		
		LearningNode[] outputLayer = new LearningNode[1];;
		for (int i = 0; i < inputLayer.length; ++i) {
			inputLayer[i] = makeLearningNode(inputs, hiddenLayer, gensym, learningState);
		}
		for (int i = 0; i < hiddenLayer.length; ++i) {
			hiddenLayer[i] = makeLearningNode(inputLayer, outputLayer, gensym, learningState);
		}
		outputLayer[0] = makeLearningNode(hiddenLayer, new LearningNode[0], gensym, learningState);
		return outputLayer[0];
	}
	
	//makes a LearningNode with a bias term on the input
	private static LearningNode makeLearningNode(Object[] inputs, LearningNode[] outgoing, MutableDouble gensym, LearningState learningState) {
		LearningNode node = new LearningNode();
		node.outgoing = outgoing;
		//if (inputs instanceof Double[]) {
			node.inputs = inputs;
		//} else {
		//	node.inputs = new Object[inputs.length+1];
		//	System.arraycopy(inputs, 0, node.inputs, 0, inputs.length);
		//	node.inputs[inputs.length] = new Double(1);
		//}
		node.nonlinear = new SigmoidOutputFunction();
		node.weights = makeParamWeights(node.inputs.length, gensym, learningState);
		return node;
	}
	
	private static ParamWeight[] makeParamWeights(int length, MutableDouble gensym, LearningState learningState) {
		ParamWeight[] weights = new ParamWeight[length];
		for (int i = 0; i < length; ++i) {
			weights[i] = learningState.instance(new ATerm[] {new NumberTerm(gensym.value++)});
			//if (i == length - 1) {
			//	weights[i].pushDelta(-0.05, 0); //all bias negative
			//} else {
				weights[i].pushDelta((Math.random()-0.5)/10, learningState);
			//}
		}
		return weights;
	}
	
	private static Random rand = new Random();
	
	//NOTE: function must be between 0 and 1
	
	//weird made up function
	private static double testf1(Double[] inputs) {
		inputs[0] = Math.random();
		inputs[1] = Math.random();
		inputs[2] = Math.random();
		return SigmoidOutputFunction.f(Math.exp(inputs[0])+(.5-inputs[0])*Math.cos(inputs[1])*Math.log(inputs[2]));
	}
	
	//XOR
	private static double testf2(Double[] inputs) {
		inputs[0] = rand.nextBoolean() ? 0.0 : 1.0;
		inputs[1] = rand.nextBoolean() ? 0.0 : 1.0;
		return inputs[0] + inputs[1] == 1.0 ? 1.0 : 0.0;
	}
}
