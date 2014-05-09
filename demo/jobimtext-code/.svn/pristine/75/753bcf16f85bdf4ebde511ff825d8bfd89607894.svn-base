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

package com.ibm.bluej.consistency.term;

public abstract class CheckTerm extends Function {

	public Function clone() {
		Function basic = super.clone();
		((CheckTerm)basic).truth = null; //should be un-needed
		return basic;
	}
	
	protected BooleanTerm truth;
	
	public BooleanTerm getValue() {
		return truth;
	}
	
	//Boolean checkConstant(Binds binds) - null if it depends on a Function in binds
	public Boolean checkConstant(Binds binds) {
		ATerm t = this.ground(binds, Function.NO_LINK);
		if (t instanceof Function) {
			return null;
		}
		return ((BooleanTerm)t).value;
	}
	
}
