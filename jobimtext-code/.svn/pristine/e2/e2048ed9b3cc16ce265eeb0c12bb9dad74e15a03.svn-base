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
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.CheckTerm;
import com.ibm.bluej.consistency.term.Function;

public class FuncIsSubstring extends CheckTerm {

	public void update(Function source, Object... msg) {
		assert (parts.length == 2);
		String sub = ATerm.getString(parts[0]);
		String large = ATerm.getString(parts[1]);
		BooleanTerm prevTruth = truth;
		boolean nt = large.indexOf(sub) != -1;
		truth = nt ? BooleanTerm.TRUE : BooleanTerm.FALSE;
		if (truth != prevTruth) {
			passUpdate();
		}
	}

}
