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

public class FactorFunctionCreator implements Creator {
	ATerm defFun;
	ParamWeight theta;
	ATerm[] specifiers;
	CRFState crfState;
	
	public ATerm getWeightFunction() {
		return defFun;
	}
	
	public FactorFunctionCreator(ATerm[] specifiers, ATerm defFun, CRFState crfState) {
		this.specifiers = specifiers;
		this.defFun = defFun;
		this.crfState = crfState;
	}
	
	public FactorFunctionCreator(ParamWeight theta, ATerm defFun, CRFState crfState) {
		this.theta = theta;
		this.defFun = defFun;
		this.crfState = crfState;
	}
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		if (specifiers != null) {
			ATerm[] gspecs = new ATerm[specifiers.length];
			for (int i = 0; i < specifiers.length; ++i) {
				gspecs[i] = specifiers[i].ground(binds, Function.NO_LINK);
			}
			theta = crfState.learningState.instance(gspecs);
		}
		try {
			return new FactorFunction(theta, defFun, binds, crfState);
		} catch (RuntimeException re) {
			System.err.println("FactorFunctionCreator: ");
			System.err.println("Theta = "+theta);
			System.err.println("defFun = "+defFun);
			System.err.println("binds = "+binds);
			throw re;
		}
	}

	public String toString() {
		if (theta != null) {
			return theta.toAbstractString() + "(" + defFun.toString() + ")";
		} else if (specifiers != null) {
			StringBuffer buf = new StringBuffer();
			buf.append("T_{");
			for (int i = 0; i < specifiers.length; ++i) {
				if (i > 0) {
					buf.append(",");
				}
				buf.append(specifiers[i]);
			}
			buf.append("}");
			return buf.toString()  + "(" + defFun.toString() + ")";
		}
		return defFun.toString();
	}
}
