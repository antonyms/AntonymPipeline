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

package com.ibm.bluej.consistency.halfbaked;

import java.util.ArrayList;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.Updatable;

//Term -> Term -> ... Term -> Integer
public class NonConstTermMap {
	//The value is either a MutableInteger or a NonConstTermMap
	Map<ATerm, Object> theMap;
	
	public void addEntry(ATerm...terms) {
		
	}
	public void removeEntry(ATerm...terms) {
		
	}
	public void modifiedEntry(ATerm...terms) {
		//any one of these could be the one that is modified
		removeEntry(terms);
		updateConstHelpers(terms);
		//TODO: passUpdate?
		addEntry(terms);
		//def. passUpdate here
	}
	
	private void updateConstHelpers(ATerm[] terms) {
		for (ATerm t : terms) {
			if (t instanceof ConstHelper) {
				((ConstHelper) t).modified();
			}
		}
	}
	
	//we wrap every mutable term in a ConstHelper (a Function with special hashCode)
	
	private static class ConstHelper extends Function {
		private int hashCode;
		public ConstHelper(ATerm nonConst) {
			//assert (!nonConst.isFunctionFree());
			parts = new ATerm[] {nonConst};
			addToUsedBys(nonConst);
			usedBy = new ArrayList<Updatable>();
			hashCode = nonConst.valueHash();
		}
		
		public void modified() {
			hashCode = parts[0].valueHash();
		}
		
		public ATerm getValue() {
			return parts[0];
		}

		public void update(Function source, Object... msg) {
			//TODO: pass a modified message to the set
			passUpdate(msg);
		}
		
		private void addToUsedBys(ATerm t) {
			if (t instanceof Function) {
				((Function) t).addUsedBy(this);
				return;
			}
			if (t instanceof ScanTerm) {
				for (ATerm p : ((ScanTerm) t).parts) {
					addToUsedBys(p);
				}
			}
		}
		
		public int hashCode() {
			return hashCode;
		}
		//leave equals as equality
	}
}
