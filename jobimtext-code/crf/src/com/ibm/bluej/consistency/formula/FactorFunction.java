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

package com.ibm.bluej.consistency.formula;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ParamWeightTerm;
import com.ibm.bluej.consistency.term.TerminalUpdatable;
import com.ibm.bluej.consistency.validate.SGDebug;


public class FactorFunction extends TerminalUpdatable implements FormulaCreate {	
	//CONSIDER refactor; this handles three separate cases:
	//  a function alone - nonParamWeight
	//  a function modifying a fixed ParamWeight
	//  a function that returns a ParamWeight and a value to multiply it by
	
	private boolean active = false;
	private ATerm weight;
	private double prevValue;
	private Object unlink;
	private ParamWeight theta;
	private ParamWeight prevTheta;
	private CRFState crfState;

	//be able to handle functions that return a ParamWeightTerm
	private double gatherWeight() {
		ATerm t = ATerm.getTerm(weight);
		if (t instanceof ParamWeightTerm) {
			ParamWeightTerm pwt = (ParamWeightTerm)t;
			theta = pwt.param;
			return pwt.weight;
		}
		try {
		return ((NumberTerm)t).value;
		} catch (ClassCastException e) {
			System.err.println("weight = "+weight+" t = "+t+" theta = "+theta+" prevTheta = "+prevTheta);
			throw e;
		}
	}
	
	public double getWeight() {
		if (!active) return 0;
		double w = gatherWeight();
		if (theta == null) 
			return w; 
		return theta.getWeight() * w;
	}
	
	public String toString() {
		if (theta == null) {
			return "Weight Fun: "+weight;
		} else {
			return "Weight Fun: "+theta+"*"+weight;
		}
	}
		
	public FactorFunction(ParamWeight theta, ATerm weightFun, Binds binds, CRFState crfState) {
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already some Function links");
		}
		this.theta = theta;
		weight = weightFun.ground(binds, this);
		unlink = binds.popUnlinks();
		this.crfState = crfState;
	}
	
	public void add(Formula origin) {
		if (!active) {
			double curValue = gatherWeight();
			if (theta != null) {
				crfState.addWeight(theta, curValue);
			} else {
				crfState.addWeight(curValue);
			}
			prevValue = curValue;
			prevTheta = theta;
			active = true;
		}
	}

	public void drop() {
		dropConditional();
		Binds.unlink(unlink);
		unlink = null;
		weight = null;
	}

	public void dropConditional() {
		if (active) {
			double curValue = gatherWeight();
			assert(curValue == prevValue);
			if (theta != null) {
				crfState.addWeight(theta, -curValue);
			} else {
				crfState.addWeight(-curValue);
			}
			active = false;
		}
	}

	public boolean isGarbage() {
		return weight == null;
	}

	public boolean isReady() {
		return active;
	}
	
	public void update(Function source, Object... msg) {
		double curValue = gatherWeight();
		if (theta != null || prevTheta != null) {
			if (theta != null && prevTheta != null) {
				crfState.addWeight(prevTheta, -prevValue);
				crfState.addWeight(theta, curValue);
			} else if (prevTheta == null) {
				crfState.addWeight(-prevValue);
				crfState.addWeight(theta, curValue);
			} else if (theta == null) {
				crfState.addWeight(prevTheta, -prevValue);
				crfState.addWeight(curValue);
			}
		} else {
			crfState.addWeight(curValue - prevValue);
		}
		prevValue = curValue;
		prevTheta = theta;
	}

}
