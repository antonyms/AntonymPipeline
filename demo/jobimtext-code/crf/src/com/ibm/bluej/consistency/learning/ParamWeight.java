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

import java.io.Serializable;
import java.util.Comparator;

import com.ibm.bluej.consistency.optimize.Optimizer;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.util.common.Lang;


/*
(2 * T1).pushDelta(-1) -> T1.pushDelta(2 * -1) -> T1.value += -2
vs
T1.pushDelta(-1) -> T1.value += -1
T1.pushDelta(-1) -> T1.value += -1
(T1 * T2).pushDelta(-1) -> desired value = T1 * T2 - 1, 
  subtract equals amounts from each, but one could be negative and the other positive, take the partials
  dT1 = T2, T2*T1_delta = -.5 -> T1_delta = -.5/T2
  (T1+d) * (T2+d) = T1*T2 + delta
  T1*T2 + T1d + T2d + d^2 = T1*T2 + delta
  delta = T1d + T2d + d^2
  d^2 + (T1+T2)d - delta = 0
  d = (-(T1+T2)+-sqrt((T1+T2)^2 - 4delta))/2
  (2+-sqrt(8))/2
  T1.pushDelta(getValue(T2) * -1)

Are you sure you need these multiple layers?
Understand backpropagation in detail
  work through a backpropagation example
The WorldWeight is a neural net?
Functions in general are not ILearnable - only single argument functions with a derivative (pass through learnable)
and linear sum
so what's the downside of actually using a neural net?
Seems like it is exactly what term matching needs
  for one way of term matching that is true
Clearly your expanded idea of weights is a natural extension of CRF
but how is it an extension of neural nets?
pushing deltas on a set of learnable does not mean that the weight of the good world will become
higher than the weight of the bad world
consider BadDelta 1 T1, 1 T2, -1 T1 T2
make some learnable functions (FuncMult especially)
remove ParamWeightTerm
test changes
Rather than saying we have arbitrary learnables let's say all learnable are representable as neural nets basically
outputs and weights
 */

//TODO: removed extends Function until 1) multi-level shows results 2) ParamWeight function is safe from memory leak
public final class ParamWeight implements Serializable, Learnable {
	public static final StringTerm NON_WEIGHT = new StringTerm("NON_WEIGHT");
	
	static final String BEGIN_STR = "T_{";
	static final String END_STR = "}";
	
	private static final long serialVersionUID = 1L;
	
	public static ATerm[] paramKeyFromProposalName(String proposalName) {
		proposalName = proposalName.substring(Optimizer.PARAM_PREFIX.length());
		proposalName = BEGIN_STR + proposalName.replace('_', ',') + END_STR;
		return LearningState.keyFromString(proposalName);
	}
	public static String proposalFunctionName(String paramName) {
		paramName = paramName.substring(ParamWeight.BEGIN_STR.length(),paramName.length()-ParamWeight.END_STR.length());
		return Optimizer.PARAM_PREFIX+paramName.replace(".", "2E").replace("-", "%2D").replace("*", "2A").replace("_", "%5F").replace("+", "%20").replace(',', '_');
	}
	
	String name;
	NumberTerm value;
	
	public String getName() {
		return name;
	}
	
	public static class NameComparator implements Comparator<Learnable> {

		@Override
		public int compare(Learnable o1, Learnable o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	
	ParamWeight(String name, double initial) {
		this.name = name;
		value = new NumberTerm(initial);
		//usedBy = new ArrayList<Updatable>();
	}
	//private ParamWeight(String name) {
	//	this(name, 0);
	//}
	
	public String toString() {
		return name + "="+Lang.dblStr(value.value);
		//return reverseLookup(this).toString()+"="+Lang.dblStr(value.value);
		//return value+"";
	}
	public String toAbstractString() {
		return name;//learningState.reverseLookup(this).toString();
	}
	
	//public double getValue() {
	//	return value;
	//}

	public static double MAX_LEARNED_WEIGHT = 10;
	
	/**
	 * TODO: pass LearningState, but not timestep - since LearningState should hold timestep
	 */
	public void pushDelta(double delta, LearningState learningState) {
		double newValue = value.value + delta;
		if (Math.abs(newValue) >= MAX_LEARNED_WEIGHT) {
			return;
		}
		value.value = newValue;
		//TODO: put this logic in a LearningState function
		//sparse averaging for average parameters
		LearningState.AvgParamValue avg = learningState.avgParams.get(this);
		if (avg == null) {
			avg = new LearningState.AvgParamValue(value.value);
			learningState.avgParams.put(this, avg);
		}
		avg.update(this.value.value, learningState.timestep);
		//passUpdate(delta);
	}
	
	public double getWeight() {
		return value.value;
	}

	
	/*
	//private static LearningState state = SGSearch.getLearningState();
	public static long getTimestep() {
		return state.timestep;
	}
	public static void setTimestep(long timestep) {
		set(LearningState.TIMESTEP_KEY, timestep);
	}
	
	public boolean isGround(Binds binds) {
		return true;
	}
	public ATerm getValue() {
		return value;
	}
	public void update(Function source, Object... msg) {
		throw new UnsupportedOperationException();
	}
	
	//TODO: first specifier for non-weight parameters should be this (just so parameter averaging doesn't clobber it)
	
	
	public static Iterator<ParamWeight> getAllParamWeights() {
		return state.allParams.values().iterator();
	}
	
	//enable iterating over Term[] that specify parameters
	public static Iterator<ATerm[]> getAllSpecifiers() {
		return state.getAllSpecifiers();
	}
	
	
	public static ParamWeight instance(ATerm[] paramKey, InitializerFunction initial, double initValue) {
		return state.instance(paramKey, initial, initValue);
	}

	public static void set(ATerm[] paramKey, double value) {
		state.set(paramKey, value);
	}
	public static ParamWeight instance(ATerm[] paramKey) {
		return instance(paramKey, null, 0);
	}
	
	//static Object reverseLookup(ParamWeight param) {
	//	return state.reverseLookup(param);
	//}
	*/	
	
}
