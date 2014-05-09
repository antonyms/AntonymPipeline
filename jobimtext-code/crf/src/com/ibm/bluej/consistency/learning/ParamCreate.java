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

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.TerminalUpdatable;

public class ParamCreate extends TerminalUpdatable implements FormulaCreate {
	static class UpdateRecord {
		UpdateRecord(Object unlink, ATerm[] specifiers, InitializerFunction initializer, double initValue) {
			this.unlink = unlink;
			this.specifiers = specifiers;
			this.initializer = initializer;
			this.initValue = initValue;
		}
		Object unlink;
		ATerm[] specifiers;
		InitializerFunction initializer;
		double initValue;
	}
	private boolean active = false;
	UpdateRecord forUpdate;
	ParamWeight param;
	CRFState crfState;
	
	public double getWeight() {
		if (!active) return 0;
		return param.value.value;
	}
	
	public String toString() {
		return param.toAbstractString()+"("+param.value.value+")";
	}
	
	public boolean isReady() {
		return active;
	}
	
	public void update(Function source, Object... msg) {
		//The specifiers have changed, drop the old param and find/initialize the new one	
		ParamWeight newParam = crfState.learningState.instance(forUpdate.specifiers, forUpdate.initializer, forUpdate.initValue);
		if (param != newParam) {
			crfState.addWeight(param, -1.0);
			crfState.addWeight(newParam, 1.0);
		}
	}

	public void dropConditional() {
		//subtract param
		if (active) {
			active = false;
			crfState.addWeight(param, -1.0);
		}
	}

	public void add(Formula origin) {
		//add param	
		if (!active) {
			//if it is dropConditional and re-added it might have changed (if not function free)
			if (forUpdate != null) {
				param = crfState.learningState.instance(forUpdate.specifiers, forUpdate.initializer, forUpdate.initValue);
			}
			active = true;
			crfState.addWeight(param, 1.0);
		}
	}
	
	public boolean isGarbage() {
		return param == null;
	}

	public void drop() {
		dropConditional();
		if (forUpdate != null) {
			Binds.unlink(forUpdate.unlink);		
		}	
		forUpdate = null;
		param = null;
	}
}
