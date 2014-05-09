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

public class MaxWWF extends WorldWeightFunction {
	private static final long serialVersionUID = 1L;

	public MaxWWF(Object... args) {
		super(args);
	}
	
	public String toString() {
		return "max("+argsToString(", ")+")";
	}
	
	@Override
	protected WorldWeightParam recompute(Set<ParamWeight> fixed) {
		WorldWeightParam max = null;
		for (int i = 0; i < getNumArgs(); ++i) {
			WorldWeightParam v = getArgWeight(i, fixed);
			if (max == null || v.getWeight() > max.getWeight()) {
				max = v;
			}
		}
		return max;
	}

	@Override
	protected boolean multipleUnfixedAllowed() {
		return true;
	}

	@Override
	protected double recomputeValue() {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < getNumArgs(); ++i) {
			double v = getArgValue(i);
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

}
