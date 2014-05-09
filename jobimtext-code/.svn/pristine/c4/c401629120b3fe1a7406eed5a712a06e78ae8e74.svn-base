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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.DefIndexEntry;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;


public class FormulaFindGroundList extends FormulaFindGround {
	private List<IndexEntry> entries = new ArrayList<IndexEntry>();
	
	public void add(IndexEntry ti) {
		if (SGDebug.LOGGING) SGLog.fine("Adding: "+ti.term.toString());
		entries.add(ti);
	}

	public IndexEntry contains(ScanTerm t) {
		for (IndexEntry e : entries) {
			if (e.term.equals(t)) {
				return e;
			}
		}
		return null;
	}

	public void remove(ScanTerm t, boolean defined) {
		IndexEntry toUncreate = null;
		for (int i = 0; i < entries.size(); ++i) {
			IndexEntry e = entries.get(i);
			if (e.term.equals(t)) {
				if (SGDebug.LOGGING) SGLog.fine("Removing: "+t.toString());
				toUncreate = e; 
				int lastNdx = entries.size()-1;
				IndexEntry last = entries.get(lastNdx);
				entries.set(i, last);
				entries.remove(entries.size()-1);
				break;
			}
		}
		if (toUncreate != null) {
			if ((toUncreate instanceof DefIndexEntry) != defined) {
				throw new Error("The proposal distribution cannot suggest terms that are defined by DEF>.");
			}
			toUncreate.uncreate(); //note, this can call remove
		} else {
			System.err.println("Not present for removal "+t);
		}
	}

	public void find(ScanTerm t, Binds binds, List<IndexEntry> matching) {
		//fills the matching list (initially empty) with the ground terms that match t/binds
		assert(matching.size() == 0);
		for (IndexEntry e : entries) {
			if (t.matches(e.term, binds)) {
				matching.add(e);
			}
		}
	}

	public int estimateMatching(ScanTerm t, Binds binds) {
		//estimate based on number of missing variables?
		//the index can estimate based on size of hashed list
		return 10;
	}

	public Iterator<IndexEntry> getGroundTerms() {
		return entries.iterator();
	}

}
