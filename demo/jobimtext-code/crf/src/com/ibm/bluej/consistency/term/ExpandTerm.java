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

import java.util.Iterator;

public abstract class ExpandTerm extends CheckTerm {
	
	//NOTE: you can only return 0 if you are SURE that there is no true grounding possible
	public abstract int estimateExpand(Binds binds);
	
	//The Iterator over Binds can recycle the same Binds object
	//but the object must be distinct from the binds passed
	//the objects bound must not be recycled
	public abstract Iterator<Binds> expand(Binds binds);
	
	//Grounds self and checks if all arguments are function free
	//you may only expand a ExpandTerm if all args are function free
	protected CompositeTerm groundMe(Binds binds) {
		CompositeTerm t = (CompositeTerm)this.ground(binds, Function.NO_LINK);
		boolean funcFree = true;
		for (ATerm p : t.parts) {
			funcFree = funcFree && p.isFunctionFree();
		}
		if (!funcFree) {
			return null;
		}
		return t;
	}
}
