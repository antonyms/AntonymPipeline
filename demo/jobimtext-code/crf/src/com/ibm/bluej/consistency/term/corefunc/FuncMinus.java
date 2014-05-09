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

public class FuncMinus extends SimpleNumberFunction {
	public FuncMinus() {}
	public FuncMinus(Object... args) {
		super(args);
	}
	
	protected double compute() {
		assert(parts.length == 2 || parts.length == 1);
		double p1 = ATerm.getDouble(parts[0]);
		if (parts.length == 1) {
			return -p1;
		}
		double p2 = ATerm.getDouble(parts[1]);
		return p1-p2;
	}
	
	public String toString() {
		if (parts.length == 1) {
			return "(-"+parts[0]+")";
		}
		if (parts.length == 2) {
			return "("+parts[0]+" - "+parts[1]+")";
		}
		return "USAGE ERROR: "+super.toString();	
	}
}
