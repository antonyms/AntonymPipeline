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

package com.ibm.bluej.consistency.old;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;


public abstract class FormulaFindGround {
	public static FormulaFindGround instance() {
		return new FormulaFindGroundMap();//FormulaFindGroundList();
	}
	
	public abstract IndexEntry contains(ScanTerm t);
	
	//t should be present
	public abstract void remove(ScanTerm t, boolean defined);
	
	public abstract void add(IndexEntry ti);
	
	public abstract void find(ScanTerm t, Binds binds, List<IndexEntry> matching);
	
	public abstract int estimateMatching(ScanTerm t, Binds binds);
	
	public abstract Iterator<IndexEntry> getGroundTerms();
	
	//This is used for comparing the set of true ground terms for various reordering of flips
	public static HashSet<String> makeSet(Iterator<IndexEntry> groundIter) {
		HashSet<String> groundSet = new HashSet<String>();
		//Consider: also gather a hashset of all creates?
		while (groundIter.hasNext()) {
			IndexEntry e = groundIter.next();
			groundSet.add(e.term.toString());
		}
		return groundSet;
	}
}
