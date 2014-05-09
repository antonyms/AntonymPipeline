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

import java.util.Collection;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.StringTerm;

//always passes the delta (newValue - oldValue) as the update message
public abstract class SimpleNumberFunction extends NumberFunction {

	protected abstract double compute();
	
	public SimpleNumberFunction(Object... args) {
		if (args.length == 1 && args[0] instanceof Collection) {
			args = ((Collection)args[0]).toArray();
		}
		this.parts = new ATerm[args.length];
		for (int i = 0; i < parts.length; ++i) {
			if (args[i] instanceof ATerm) {
				parts[i] = (ATerm)args[i];
			} else if (args[i] instanceof String) {
				parts[i] = new StringTerm((String)args[i]);
			} else if (args[i] instanceof Double) {
				parts[i] = new NumberTerm((Double)args[i]);
			} else if (args[i] instanceof Integer) {
				parts[i] = new NumberTerm((Integer)args[i]);
			} else if (args[i] instanceof Float) {
				parts[i] = new NumberTerm((Float)args[i]);
			} else if (args[i] instanceof Long) {
				parts[i] = new NumberTerm((Long)args[i]);
			} else {
				throw new IllegalArgumentException("Unknown type: "+args[i]);
			}
		}
	}
	
	public void update(Function source, Object... msg) {
		assert(parts.length == 2);
		double newVal = compute();
		if (value == null) {
			value = new NumberTerm(newVal);
			passUpdate(newVal);
		} else if (value.value != newVal) {
			double prev = value.value;
			value.value = newVal;
			passUpdate(value.value - prev);
		}
		//SGSearch.logFine("Update "+this.toString());
	}
}
