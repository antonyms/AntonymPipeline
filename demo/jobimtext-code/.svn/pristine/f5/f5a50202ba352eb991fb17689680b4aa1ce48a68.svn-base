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

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.VectorTerm;

public class FuncVectorSubtract extends VectorFunction {

	public void update(Function source, Object... msg) {
		double[] from = ATerm.getVector(parts[0]);
		double[] sub = ATerm.getVector(parts[1]);
		if (this.vector == null || this.vector.value.length == 0) {
			this.vector = new VectorTerm(new double[from.length]);
		}
		if (from != null && from.length > 0 && sub != null && sub.length > 0) {
			System.arraycopy(from, 0, vector.value, 0, from.length);
			for (int i = 0; i < sub.length; ++i) {
				vector.value[i] -= sub[i];
			}
			passUpdate();
		}
	}

}
