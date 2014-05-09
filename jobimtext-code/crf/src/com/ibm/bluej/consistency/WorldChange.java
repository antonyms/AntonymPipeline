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
import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.MutableInteger;
import com.ibm.bluej.util.common.Pair;


public final class WorldChange {
	private ArrayList<ScanTerm> terms = new ArrayList<ScanTerm>();
	private ArrayList<Boolean> prev = new ArrayList<Boolean>();
	

	public void addChange(ScanTerm t, Boolean oldValue) {
		terms.add(t); prev.add(oldValue);
	}
	
	public boolean isEmpty() {
		return terms.isEmpty();
	}
	
	public void clear() {
		terms.clear(); prev.clear();
	}
	
	//ok to be inefficient here, only used for debugging
	public WorldChange copy() {
		WorldChange wc = new WorldChange();
		wc.prev.addAll(prev);
		for (ScanTerm t : terms) {
			wc.terms.add((ScanTerm)t.valueClone()); 
		}
		return wc;
	}
	
	public int size() {
		return terms.size();
	}
	
	public ScanTerm getTerm(int ndx) {
		return terms.get(ndx);
	}
	public Boolean getPrevValue(int ndx) {
		return prev.get(ndx);
	}
	public ScanTerm popTerm() {
		return terms.remove(terms.size()-1);
	}
	public Boolean popPrevValue() {
		return prev.remove(prev.size()-1);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < terms.size(); ++i) {
			if (!prev.get(i)) {
				buf.append("!");
			}
			buf.append(terms.get(i)).append(' ');
		}
		
		return buf.toString();
	}
	
	public void compress() {
		//only care about odd numbers of appearances in the cares table
		//only care about the last appearance
		HashMap<ScanTerm, Pair<MutableInteger,Boolean>> compressed = new HashMap<ScanTerm, Pair<MutableInteger, Boolean>>();
		for (int i = 0; i < terms.size(); ++i) {
			Pair<MutableInteger,Boolean> p = compressed.get(terms.get(i));
			if (p == null) {
				p = new Pair<MutableInteger,Boolean>(new MutableInteger(0), null);
				compressed.put(terms.get(i), p);
			}
			p.first.value += 1;
			p.second = prev.get(i);
		}
		terms.clear(); prev.clear();
		for (Map.Entry<ScanTerm, Pair<MutableInteger,Boolean>> e : compressed.entrySet()) {
			if (e.getValue().first.value % 2 != 0) {
				terms.add(e.getKey());
				prev.add(e.getValue().second);
			}
		}
	}

}
