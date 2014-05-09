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

import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.util.common.IdentitySet;




public interface OriginatingTermCollection extends FormulaCreate {
	public static final String ADD = "ADD";
	public static final String REM = "REM";
	public static final String MOD = "MOD"; //the modify operation will pass in msg = [MOD, element, funcSource, funcMsg...]
	
	//TODO: consolidate some of the shared functionality for Bag and TermSet
	//TODO: when you do incremental updates be aware that you can get updates from
	//  FuncMemberCreates that are the wrappedCreate of a FunctionConditional
	//  therefore some updates may come from NON-MEMBERS
	
	//both of these call update
	public void addMember(ATerm t);
	public void removeMember(ATerm t);
	
	public void addEndpointFunctions(IdentitySet<Updatable> ends, boolean print);
}
