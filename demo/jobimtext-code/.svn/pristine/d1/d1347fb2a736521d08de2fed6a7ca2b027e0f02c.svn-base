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

package com.ibm.bluej.consistency.term.corefunc;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;

public class FuncSum extends NumberFunction {
	public void update(Function source, Object... msg) {
		//compute from scratch
		if (msg.length == 0) {
			double prevValue = value == null ? 0 : value.value;
			if (value == null) {
				value = new NumberTerm(0);
			} else {
				value.value = 0;
			}
			assert (parts.length == 1);
			for (ATerm t : ATerm.getCollection(parts[0])) {
				value.value += ATerm.getDouble(t);
			}
			passUpdate(value.value - prevValue);
			return;
		}

		//incremental update
		String operation = (String)msg[0];
		ATerm element = (ATerm)msg[1];
		
		double prev = value.value;
		if (operation == OriginatingTermCollection.ADD) {
			value.value += ATerm.getDouble(element);
		} else if (operation == OriginatingTermCollection.REM) {
			value.value -= ATerm.getDouble(element);
		} else if (operation == OriginatingTermCollection.MOD) {
			Object[] sourceMsg = (Object[])msg[3];
			double delta = (Double)sourceMsg[0];
			value.value += delta;
		} else {
			throw new Error("Unknown update: "+operation);
		}
		if (value.value != prev) {
			passUpdate(value.value - prev);
		}
	}

}
