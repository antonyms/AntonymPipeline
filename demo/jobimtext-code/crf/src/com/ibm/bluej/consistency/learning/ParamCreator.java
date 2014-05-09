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
import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.validate.SGDebug;

public class ParamCreator implements Creator {

	private ATerm[] specifiers;
	
	//one or the other of these may be defined
	//if init is null, initValue is the relevant one
	private InitializerFunction init;
	private double initValue;
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("T_{");
		for (int i = 0; i < specifiers.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append(specifiers[i]);
		}
		buf.append("}");
		return buf.toString();
	}
	
	public ParamCreator(ATerm[] specifiers, InitializerFunction init, double initValue) {
		this.specifiers = specifiers;
		this.init = init;
		this.initValue = initValue;
	}
	
	public FormulaCreate create(Binds binds, CRFState crfState) {
		ParamCreate create = new ParamCreate();
		//bind all specifiers
		//  if not all are function free
		//  create a forUpdate object
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have Function links");
		}
		ATerm[] spec = new ATerm[specifiers.length];
		for (int i = 0; i < specifiers.length; ++i) {
			spec[i] = specifiers[i].ground(binds, create);
		}
		Object unlink = binds.popUnlinks();
		if (unlink != null) {
			create.forUpdate = new ParamCreate.UpdateRecord(unlink, spec, init, initValue);
		} else {
			create.param = crfState.learningState.instance(spec, init, initValue);
		}
		create.crfState = crfState;
		
		return create;
	}

}
