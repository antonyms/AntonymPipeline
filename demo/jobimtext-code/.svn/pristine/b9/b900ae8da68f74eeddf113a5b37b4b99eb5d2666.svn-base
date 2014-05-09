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

package com.ibm.bluej.consistency;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;


public class CheckNegatedGround {
	private HashMap<ATerm, IndexEntry> negTerms = new HashMap<ATerm, IndexEntry>();
	
	private static final int MIN_PUTS_BEFORE_CLEAN = 1000;
	int nextCleanIn = MIN_PUTS_BEFORE_CLEAN;
	
	void clear() {
		negTerms.clear();
	}
	
	public int size() {
		return negTerms.size();
	}
	
	public void clean() {
		Iterator<Map.Entry<ATerm, IndexEntry>> it = negTerms.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue().hasNoDepends()) {
				it.remove();
			}
		}
	}
	
	public IndexEntry contains(ATerm t) {
		return negTerms.get(t);
	}
	
	//if t is not present, does nothing
	public void remove(ATerm t, boolean defined) {
		IndexEntry e = negTerms.remove(t);
		if (e != null) {
			//This doesn't work right now, we add things in CheckNegatedGround because they weren't present during grounding
			//  but we don't know whether they are supposed to be proposal terms or defined terms
			/*
			if ((e instanceof DefIndexEntry) != defined) {
				System.err.println(t+" problematic define status should be "+defined);
				throw new Error("The proposal distribution cannot suggest terms that are defined by DEF>.");
			}
			*/
			e.uncreate();
		}
	}
	
	public void add(IndexEntry ti) {
		//not ideal but the fields of capacity and threshold are not visible (wish they were protected instead)
		if (nextCleanIn <= 0) {
			clean();
			nextCleanIn = Math.min(negTerms.size(), MIN_PUTS_BEFORE_CLEAN);
		}
		--nextCleanIn;
		if (negTerms.put(ti.term, ti) != null) {
			//actually this is completely fine
			//if (Warnings.limitWarn("CheckNegatedGround.add", 10, "CheckNegatedGround.add, OVERWROTE CheckNegatedGround index entry! "+ti.term)) {
			//	new Exception().printStackTrace();
			//}
		}
	}
	
	public Iterator<IndexEntry> getGroundTerms() {
		clean();
		return negTerms.values().iterator();
	}
}
