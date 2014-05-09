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

package com.ibm.bluej.consistency.old;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.ibm.bluej.consistency.DefIndexEntry;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.util.ApproxRandSet;
import com.ibm.bluej.consistency.util.IRandomAccessible;
import com.ibm.bluej.consistency.util.RandArray;

public class FormulaFindGroundFirstK extends FormulaFindGround {
	private static final int CONVERT_TO_HASHSET_SIZE = 1000; //TODO: select this parameter more intelligently
	
	private Random r = new Random();
	public ScanTerm randomMatch(ATerm[] probe) {
		int k = 0;
		while (k < probe.length && probe[k] != null) {
			++k;
		}
		int hash = firstKHash(probe,k);
		if (arityToFirstKToGround.size() <= probe.length) {
			return null;
		}
		IRandomAccessible<IndexEntry> posMatch = arityToFirstKToGround.get(probe.length).get(hash);
		if (posMatch == null) {
			return null;
		}
		ScanTerm t = null;
		//try 20 random
		for (int attempt = 0; attempt < 20; ++attempt) {
			IndexEntry ie = posMatch.getRandom(r);
			if (ie == null) return null;
			t = ie.term;
			for (int pi = 0; pi < k; ++pi) {
				if (!probe[pi].equals(t.parts[pi])) {
					t = null; break;
				}
			}
			if (t != null) return t;
		}
		//give up and scan
		for (IndexEntry e : posMatch) {
			t = e.term;
			for (int pi = 0; pi < k; ++pi) {
				if (!probe[pi].equals(t.parts[pi])) {
					t = null; break;
				}
			}
			if (t != null) return t;
		}
		return null;
	}

	//the collections in firstKToGround must be random accessible to help the proposal distribution
	private HashMap<ATerm,IndexEntry> grndToEntry = new HashMap<ATerm,IndexEntry>();
	private ArrayList<HashMap<Integer, IRandomAccessible<IndexEntry>>> arityToFirstKToGround = 
			new ArrayList<HashMap<Integer, IRandomAccessible<IndexEntry>>>();
	
	public IndexEntry contains(ScanTerm t) {
		return grndToEntry.get(t);
	}

	
	private int firstKHash(ATerm[] parts, int k) {
		int h = ATerm.mdjbFirst();
		for (int i = 0; i < k; ++i) {
			h = ATerm.mdjbNext(h, parts[i].hashCode());
		}
		return h;
	}
	
	private static IndexEntry probe = new IndexEntry(null);
	public void remove(ScanTerm t, boolean defined) {
		//remove from fancy index
		HashMap<Integer, IRandomAccessible<IndexEntry>> firstKToGround = arityToFirstKToGround.get(t.parts.length);
		for (int k = 1; k < t.parts.length; ++k) {
			Collection<IndexEntry> mayMatch = firstKToGround.get(firstKHash(t.parts,k));
			probe.term = t;
			mayMatch.remove(probe);
			//CONSIDER: if mayMatch is empty, remove it in firstKToGround
		}
		
		//remove from core index
		IndexEntry toUncreate = grndToEntry.remove(t);
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
		for (int i = arityToFirstKToGround.size(); i <= ti.term.parts.length+1; ++i) {
			arityToFirstKToGround.add(new HashMap<Integer, IRandomAccessible<IndexEntry>>());
		}
		HashMap<Integer, IRandomAccessible<IndexEntry>> firstKToGround = arityToFirstKToGround.get(ti.term.parts.length);
		for (int k = 1; k < ti.term.parts.length; ++k) {
			Integer hash = firstKHash(ti.term.parts, k);
			IRandomAccessible<IndexEntry> grnd = firstKToGround.get(hash);
			if (grnd == null) {
				grnd = new RandArray<IndexEntry>();
				firstKToGround.put(hash, grnd);
			}
			//change to HashSet if size grows beyond some point (so removal will be faster)
			grnd.add(ti);
			if (!(grnd instanceof ApproxRandSet) && grnd.size() >= CONVERT_TO_HASHSET_SIZE) {
				grnd = new ApproxRandSet<IndexEntry>(grnd);
				firstKToGround.put(hash, grnd);
			}
		}
	}

	public void find(ScanTerm t, Binds binds, List<IndexEntry> matching) {
		assert(matching.size() == 0);
		
		if (arityToFirstKToGround.size() <= t.parts.length) {
			return;
		}
	
		//how many ground parts till first non-ground?
		int k = 0;
		while (k < t.parts.length && t.parts[k].isGround(binds)) {
			++k;
		}
		
		if (k == 0) {
			//You want a table scan? Ok here's your table scan.
			for (IndexEntry e : grndToEntry.values()) {
				if (t.matches(e.term, binds)) {
					matching.add(e);
				}
			}
			return;
		}
		
		t = (ScanTerm)t.ground(binds, Function.NO_LINK);
		if (k == t.parts.length) {
			//fully ground, just a lookup
			IndexEntry e = grndToEntry.get(t);
			if (e != null) {
				matching.add(e);
			}
			return;
		}
		
		HashMap<Integer, IRandomAccessible<IndexEntry>> firstKToGround = arityToFirstKToGround.get(t.parts.length);
		//otherwise hash on first k ground
		Collection<IndexEntry> mayMatch = firstKToGround.get(firstKHash(t.parts,k));
		if (mayMatch != null) {
			for (IndexEntry e : mayMatch) {
				//CONSIDER: this could be a little smarter...
				//if we know there are no repeated varids in t and 
				//we store the prefix at the along with the Collection<IndexEntry>
				//then we can addAll mayMatch to matching
				//note that returning mayMatch directly could cause concurrent mod exception
				//if we make this a function jvisualvm will tell us its selftime
				if (t.matches(e.term, binds)) {
					matching.add(e);
				}
			}
		}
	}

	public int estimateMatching(ScanTerm t, Binds binds) {
		if (t.parts.length >= arityToFirstKToGround.size()) {
			return 0;
		}
		int k = 0;
		while (k < t.parts.length && t.parts[k].isGround(binds)) {
			++k;
		}
		if (k == 0) {
			return grndToEntry.size();
		}
		t = (ScanTerm)t.ground(binds, Function.NO_LINK);	
		if (k == t.parts.length) {
			return grndToEntry.containsKey(t) ? 1 : 0;
		}
		HashMap<Integer, IRandomAccessible<IndexEntry>> firstKToGround = arityToFirstKToGround.get(t.parts.length);
		Collection<IndexEntry> mayMatch = firstKToGround.get(firstKHash(t.parts,k));
		if (mayMatch == null) {
			return 0;
		}
		return mayMatch.size();
	}

	public Iterator<IndexEntry> getGroundTerms() {
		return grndToEntry.values().iterator();
	}


	public void loadConfig(String[] lines) {
		//nothing to config
	}
	public String configToString() {
		return "";
	}
}
