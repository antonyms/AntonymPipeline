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

package com.ibm.bluej.consistency.learning.coordescent;

import java.util.Set;

import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.util.common.Lang;

public class MultiplyWWF extends WorldWeightFunction {
	private static final long serialVersionUID = 1L;

	public MultiplyWWF(Object... args) {
		super(args);
	}
	
	public String toString() {
		return Lang.dblStr(getValue())+"=("+argsToString(" * ")+")";
	}
	
	@Override
	protected WorldWeightParam recompute(Set<ParamWeight> fixed) {
		double scalarMult = 1;
		WorldWeightParam mainWW = new WorldWeightParam();
		for (int i = 0; i < getNumArgs(); ++i) {
			if (isConst(i,fixed)) {
				scalarMult *= getArgValue(i);
			} else {
				WorldWeightParam argww = getArgWeight(i,fixed);
				if (mainWW.getWeight() != 0) {
					throw new IllegalArgumentException("completeFixed not called or completeFixed doesn't fix enough");
				}
				mainWW.merge(argww);
			}
		}
		if (scalarMult != 1) {
			mainWW.scaleEveryPart(scalarMult);
		}
		return mainWW;
	}

	@Override
	public double recomputeValue() {
		double v = 1;
		for (int i = 0; i < getNumArgs(); ++i) {
			v *= getArgValue(i);
		}
		return v;
	}

	@Override
	protected boolean multipleUnfixedAllowed() {
		return false;
	}

}
