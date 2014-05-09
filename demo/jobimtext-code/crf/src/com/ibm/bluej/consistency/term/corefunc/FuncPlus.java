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
import com.ibm.bluej.util.common.Lang;


public class FuncPlus extends SimpleNumberFunction {
	public FuncPlus() {}
	public FuncPlus(Object... args) {
		super(args);
	}
	
	protected double compute() {
		//assert(parts.length == 2);
		double sum = 0;
		for (int i = 0; i < parts.length; ++i) {
			sum += ATerm.getDouble(parts[i]);
		}
		return sum;
	}
	public String toString() {
		if (Function.FOR_GCC) {
			return "("+Lang.stringList(parts, " + ")+")";
		}
		if (parts.length == 2) {
			return "("+parts[0]+" + "+parts[1]+")";
		}
		return "(+ "+Lang.stringList(parts, " ")+")";		
	}
}