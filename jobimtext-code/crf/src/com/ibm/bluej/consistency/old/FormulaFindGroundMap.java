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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.DefIndexEntry;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;


//TODO: test me with combo
public class FormulaFindGroundMap extends FormulaFindGround {

	//TODO: better indexing!
	// make formulaFindGround, first n
	
	private HashMap<ATerm,IndexEntry> grndToEntry = new HashMap<ATerm,IndexEntry>();
	private ArrayList<HashMap<ATerm, ArrayList<IndexEntry>>> arityToFirstToGround = new ArrayList<HashMap<ATerm, ArrayList<IndexEntry>>>();
	
	public IndexEntry contains(ScanTerm t) {
		return grndToEntry.get(t);
	}

	public void remove(ScanTerm t, boolean defined) {
		IndexEntry toUncreate = grndToEntry.remove(t);
		Iterator<IndexEntry> git = arityToFirstToGround.get(t.parts.length).get(t.parts[0]).iterator();
		while (git.hasNext()) {
			IndexEntry e = git.next();
			if (t.equals(e.term)) {
				assert(toUncreate == e);
				git.remove();
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

	public void add(IndexEntry ti) {
		IndexEntry e = grndToEntry.put(ti.term, ti);
		if (e != null) {
			System.err.println("Already present "+ti.term);
			return;
		}
		for (int i = arityToFirstToGround.size(); i <= ti.term.parts.length+1; ++i) {
			arityToFirstToGround.add(new HashMap<ATerm, ArrayList<IndexEntry>>());
		}
		HashMap<ATerm, ArrayList<IndexEntry>> firstToGround = arityToFirstToGround.get(ti.term.parts.length);
		ArrayList<IndexEntry> grnd = firstToGround.get(ti.term.parts[0]);
		if (grnd == null) {
			grnd = new ArrayList<IndexEntry>();
			firstToGround.put(ti.term.parts[0], grnd);
		}
		grnd.add(ti);
	}

	public void find(ScanTerm t, Binds binds, List<IndexEntry> matching) {
		assert(matching.size() == 0);

		if (t.isGround(binds)) {
			IndexEntry e = grndToEntry.get(t.ground(binds, Function.NO_LINK));
			if (e != null) {
				matching.add(e);
			}
			return;
		}
		
		if (arityToFirstToGround.size() <= t.parts.length) {
			return;
		}
		HashMap<ATerm, ArrayList<IndexEntry>> firstToGround = arityToFirstToGround.get(t.parts.length);
		if (t.parts[0].isGround(null)) {
			fill(t,binds,matching, firstToGround.get(t.parts[0]));
		} else {
			for (ArrayList<IndexEntry> l : firstToGround.values()) {
				fill(t,binds,matching,l);
			}
		}
	}

	private void fill(ScanTerm t, Binds binds, List<IndexEntry> matching, Collection<IndexEntry> entries) {
		if (entries == null) {
			return;
		}
		//fills the matching list with the ground terms that match t/binds
		for (IndexEntry e : entries) {
			if (t.matches(e.term, binds)) {
				matching.add(e);
			}
		}
	}
	
	public int estimateMatching(ScanTerm t, Binds binds) {
		if (t.isGround(binds)) {
			return grndToEntry.containsKey(t) ? 1 : 0;
		}
		HashMap<ATerm, ArrayList<IndexEntry>> firstToGround;
		try {
			firstToGround = arityToFirstToGround.get(t.parts.length);
		} catch (Exception e) {
			return grndToEntry.size();
		}
		if (t.parts[0].isGround(null)) {
			ArrayList<IndexEntry> grnd = firstToGround.get(t.parts[0]);
			if (grnd == null) {
				return 0;
			}
			return grnd.size();
		} 
		return firstToGround.size() * 10; //bit sloppy
	}

	public Iterator<IndexEntry> getGroundTerms() {
		return grndToEntry.values().iterator();
	}

}
