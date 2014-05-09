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

package com.ibm.bluej.consistency.learning;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.TerminalUpdatable;
import com.ibm.bluej.consistency.validate.SGDebug;

public class BetaWeights extends TerminalUpdatable implements FormulaCreate  {

	private boolean active;
	private ParamWeight[] params;
	private ATerm arg;
	private Object unlink;
	private double[] prevV;
	private CRFState crfState;
	
	public BetaWeights(String id, ATerm arg, Binds binds, CRFState crfState) {
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have Function links");
		}
		this.arg = arg.ground(binds, this);
		unlink = binds.popUnlinks();
		double[] v = ATerm.getVector(this.arg);
		params = new ParamWeight[v.length];
		ATerm[] paramKey = new ATerm[1];
		for (int i = 0; i < params.length; ++i) {
			paramKey[0] = new StringTerm(id+i);
			params[i] = crfState.learningState.instance(paramKey);
		}
		if (!this.arg.isFunctionFree()) {
			prevV = new double[params.length];
		}
		this.crfState = crfState;
	}
	
	public double getWeight() {
		if (!active) return 0;
		double[] v = ATerm.getVector(arg);
		double w = 0;
		for (int i = 0; i < params.length; ++i) {
			w += v[i] * params[i].value.value;
		}
		return w;
	}
	
	public boolean isGarbage() {
		return params == null;
	}

	public void drop() {
		dropConditional();
		Binds.unlink(unlink);
		unlink = null;
		params = null;
		arg = null;
	}

	public void dropConditional() {
		//subtract param
		if (active) {
			double[] v = ATerm.getVector(arg);
			active = false;
			for (int i = 0; i < params.length; ++i) {
				crfState.addWeight(params[i], -v[i]);
			}
		}
	}

	public void add(Formula origin) {
		if (!active) {
			double[] v = ATerm.getVector(arg);
			if (prevV != null) {
				assert(prevV.length == v.length);
				System.arraycopy(v, 0, prevV, 0, v.length);
			}
			active = true;
			for (int i = 0; i < params.length; ++i) {
				crfState.addWeight(params[i], v[i]);
			}
		}
	}

	public void update(Function source, Object... msg) {
		assert (prevV != null);
		double[] v = ATerm.getVector(arg);
		if (prevV.length != v.length) {
			throw new Error("The length of the vectors for Beta weights cannot vary.");
		}
		for (int i = 0; i < params.length; ++i) {
			crfState.addWeight(params[i], v[i]-prevV[i]);
		}
		System.arraycopy(v, 0, prevV, 0, v.length);
	}

	public boolean isReady() {
		return active;
	}
	
}
