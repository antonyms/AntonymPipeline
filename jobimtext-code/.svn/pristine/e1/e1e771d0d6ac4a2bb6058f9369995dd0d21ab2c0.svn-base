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

package com.ibm.bluej.consistency.halfbaked;

import java.io.Serializable;
import java.util.ArrayList;

import com.ibm.bluej.consistency.learning.InitializerFunction;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.util.common.Lang;

public class MultiObjInitializer implements InitializerFunction, Serializable {
	private static final long serialVersionUID = 1L;
	
	public MultiObjInitializer(ArrayList<ATerm[]> paramSpec, double[] objWeights) {
		this.paramSpec = paramSpec;
		this.objWeights = objWeights;
	}
	
	ArrayList<ATerm[]> paramSpec;
	double[] objWeights;
	
	//Used once the objective weights have been learned
	public double initialValue(ATerm[] specifiers) {
		for (int i = 0; i < paramSpec.size(); ++i) {
			ATerm[] spec = paramSpec.get(i);
			boolean match = true;
			for (int j = 0; j < spec.length; ++j) {
				if (!specifiers[j].equals(spec[j])) {
					match = false;
					break;
				}
			}
			if (match) {
				return objWeights[i];
			}
		}
		throw new Error("Could not find specifiers: "+Lang.stringList(specifiers, ", "));
	}

	ParamWeight[] getParamWeights() {
		return null;
		/*
		ParamWeight[] paramWeights = new ParamWeight[paramSpec.size()];
		int ndx = 0;
		for (ATerm[] spec : paramSpec) {
			paramWeights[ndx] = ParamWeight.instance(spec, null, objWeights[ndx]);
			++ndx;
		}
		return paramWeights;
		*/
	}
}
