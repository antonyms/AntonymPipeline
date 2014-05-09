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
import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.TerminalUpdatable;

public class ObjectiveCreator implements Creator {
	ATerm weightFun; //may be function or just NumberTerm, if null it is just weight that matters
	double weight;
	
	public ObjectiveCreator(double weight, ATerm weightFun) {
		this.weight = weight;
		this.weightFun = weightFun;
	}
	
	static class ObjectiveCreate extends TerminalUpdatable implements FormulaCreate {
		boolean active;
		double weight;
		Object unlink;
		ATerm weightFun;
		CRFState crfState;
		ObjectiveCreate(double weight, ATerm weightFun, Binds binds, CRFState crfState) {
			this.weight = weight;
			if (weightFun != null) {
				this.weightFun = weightFun.ground(binds, this);
				unlink = binds.popUnlinks();
				weight = ATerm.getDouble(this.weightFun);
				if (!(this.weightFun instanceof Function)) {
					weightFun = null;
					unlink = null;
				}
			}
			this.crfState = crfState;
		}
		
		public double getWeight() {
			if (!active) return 0;
			return weight;
		}
		
		public boolean isGarbage() {
			return weight == Double.MAX_VALUE;
		}
		
		public final void dropConditional() {
			if (active) {
				crfState.addObjective(-weight);
				active = false;
			}
		}
		
		public void drop() {
			dropConditional();
			Binds.unlink(unlink);
			unlink = null;
			weightFun = null;
			weight = Double.MAX_VALUE;
		}

		public void add(Formula origin) {
			if (weightFun != null) {
				weight = ATerm.getDouble(weightFun);
			}
			crfState.addObjective(weight);
			active = true;
		}
		
		public String toString() {
			return "Weight: "+weight;
		}

		public void update(Function source, Object... msg) {
			double w = ATerm.getDouble(weightFun);
			crfState.addObjective(w - weight);
			weight = w;
		}

		public boolean isReady() {
			return active;
		}
	}
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		//System.out.println("Creating with "+binds);
		return new ObjectiveCreate(weight, weightFun, binds, crfState);
	}

}
