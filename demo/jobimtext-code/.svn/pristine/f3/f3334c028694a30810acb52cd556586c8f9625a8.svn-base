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
import com.ibm.bluej.consistency.term.StringTerm;

public class FuncConcat extends Function {
	StringTerm value;
	public ATerm getValue() {
		return value;
	}

	@Override
	public void update(Function source, Object... msg) {
		String p1 = ATerm.getString(parts[0]);
		String p2 = ATerm.getString(parts[1]);
		String nv = p1+p2;
		if (value == null) {
			value = new StringTerm(nv);
			passUpdate();
		} else {
			if (!value.value.equals(nv)) {
				value.value = nv;
				passUpdate();
			}
		}
	}

}
