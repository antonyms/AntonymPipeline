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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.MutableInteger;

public class DefineState {
	private HashMap<ScanTerm, MutableInteger> defineSupport = new HashMap<ScanTerm, MutableInteger>();
	private HashMap<ScanTerm, IdentityHashMap<DefineCreate, String>> defineSupportDebug = 
		new HashMap<ScanTerm, IdentityHashMap<DefineCreate, String>>();
	
	void clear() {
		defineSupport.clear();
		defineSupportDebug.clear();
	}
	
	boolean decrement(ScanTerm defined) {		
		if (SGDebug.DEFINE_DEBUG) {
			defineSupportDebug.get(defined).remove(this);
		}
		MutableInteger d = defineSupport.get(defined);
		if (d == null) {
			System.err.println("Tried to drop "+defined+" but no support");
			showDefineTable();
		}
		d.value -= 1;
		if (d.value == 0) {
			defineSupport.remove(defined);
			return true;
		}
		return false;
	}
	
	boolean increment(ScanTerm defined, DefineCreate dcreate, Formula origin) {
		MutableInteger d = defineSupport.get(defined);
		if (SGDebug.DEFINE_DEBUG) {
			if (!defined.isGround(null)) {
				throw new Error("Trying to define nonGround term: "+defined);
			}
			IdentityHashMap<DefineCreate, String> support = defineSupportDebug.get(defined);
			if (support == null) {
				support = new IdentityHashMap<DefineCreate, String>();
				defineSupportDebug.put(defined, support);
			}
			support.put(dcreate, origin.conditionToString(new Binds(null)));
		}
		if (d == null) {
			d = new MutableInteger(1);
			defineSupport.put(defined, d);
			return true;
		}
		d.value += 1;	
		return false;
	}
	
	public void showDefineTable() {
		if (SGDebug.DEFINE_DEBUG) {
			int nonEmpty = 0;
			for (Map.Entry<ScanTerm, IdentityHashMap<DefineCreate, String>> e : defineSupportDebug.entrySet()) {
				int size = e.getValue().size();
				if (size > 0) {
					System.out.println(e.getKey()+": ");
					for (String f : e.getValue().values()) {
						System.out.println("   "+f);
					}
					++nonEmpty;
					MutableInteger d = defineSupport.get(e.getKey());
					if (d.value != size) {
						System.err.println("ERROR: Debug table does not match official for: "+
								e.getKey()+" debug = "+size+" official = "+d.value);
					}
				}
			}
			if (nonEmpty != defineSupport.size()) {
				System.err.println("ERROR: Debug table size not equal to official: debug = "+nonEmpty+" official = "+defineSupport.size());
			}
			//System.out.println("Official table follows\n------------------------------");
			//don't print official table, just verify that it matches the unofficial table
		} else {
			
			for (Map.Entry<ScanTerm, MutableInteger> e : defineSupport.entrySet()) {
				System.out.println(e.getKey()+": "+e.getValue());
			}
		}
	}
}
