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

package com.ibm.bluej.consistency.term.corefunc;

import java.util.Collection;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;

public class FuncCollectionMax extends NumberFunction {

	public void update(Function source, Object... msg) {
		if (value == null) {
			value = new NumberTerm(Double.NEGATIVE_INFINITY);
		}
		value.value = Double.NEGATIVE_INFINITY;
		double prevValue = value.value;
		Collection<ATerm> col = ATerm.getCollection(parts[0]);
		for (ATerm t : col) {
			double v = ATerm.getDouble(t);
			if (v > value.value) {
				value.value = v;
			}
		}
		if (value.value != prevValue) {
			passUpdate();
		}
		//TODO: incremental
	}

}
