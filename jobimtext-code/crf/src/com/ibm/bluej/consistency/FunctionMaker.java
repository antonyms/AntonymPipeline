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

package com.ibm.bluej.consistency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.util.common.Pair;

public class FunctionMaker {
	private Map<String, Class> funNameToClass = null;
	public FunctionMaker() {
		funNameToClass = ParserBasic.predeclaredFunctions();
	}
	public FunctionMaker(Map<String, Class> funNameToClass) {
		this.funNameToClass = funNameToClass;
	}
	//TODO: version that can pass a class
	
	public Function makeFunction(String predStr, ATerm... args) {
		return ParserBasic.makeFunction(predStr, funNameToClass, args);
	}
	public Function makeFunction(String predStr, Collection<? extends ATerm> args) {
		return makeFunction(predStr, args.toArray(new ATerm[args.size()]));
	}
	public ATerm parseFunction(String func) {
		return ParserBasic.parseFunction(func, funNameToClass);
	}
	public void registerFunction(String name, Class functionClass) {
		funNameToClass.put(name, functionClass);
	}
	
	public void addFunctionClassNames() {
		ArrayList<Pair<String,Class>> toAdd = new ArrayList<Pair<String,Class>>();
		for (Class c : funNameToClass.values()) {
			toAdd.add(Pair.make(c.getSimpleName(), c));
		}
		for (Pair<String,Class> p : toAdd) {
			funNameToClass.put(p.first, p.second);
		}
	}
}
