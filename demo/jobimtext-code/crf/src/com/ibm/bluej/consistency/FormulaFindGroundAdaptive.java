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
import java.util.Iterator;
import java.util.Random;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.util.IRandomAccessible;
import com.ibm.bluej.consistency.util.RandArray;
import com.ibm.bluej.consistency.util.RandSingle;
import com.ibm.bluej.consistency.util.RandomSet;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.Lang;


public class FormulaFindGroundAdaptive implements FormulaFindGroundRand {
	private static final int CONVERT_TO_HASHSET_SIZE = 100; //TODO: select this parameter more intelligently
	
	private long totalAccesses = 0;
	private HashMap<ATerm,IndexEntry> grndToEntry = new HashMap<ATerm,IndexEntry>();
	private HashMap<PartialGround, IRandomAccessible<IndexEntry>> index = 
			new HashMap<PartialGround, IRandomAccessible<IndexEntry>>();
	
	public void clear() {
		grndToEntry.clear();
		index.clear();
	}
	
	private static class Signature {
		Signature(int arity, int includeMask) {
			this.length = arity;
			this.includeMask = includeMask;
		}
		int length;
		int includeMask;
		long useCount = 0;
		
		static Signature fromString(String str) {
			int pos = 1;
			int includeMask = 0;
			int arity = str.length();
			for (int i = 0; i < str.length(); ++i) {
				char c = str.charAt(i);
				if (c == 'G') {
					includeMask |= pos;
				}
				pos = pos << 1;
			}
			includeMask |= pos; //length indicator
			return new Signature(arity, includeMask);
		}
		static int makeIncludeMask(PartialGround probe) {
			int includeMask = 0;
			int pos = 1;
			for (ATerm t : probe.parts) {
				if (t != null) {
					includeMask |= pos;
				}
				pos = pos << 1;
			}
			includeMask |= pos; //length indicator
			return includeMask;
		}
		boolean matchesGrnd(ScanTerm t) {
			//CONSIDER: also check parts[0]?
			return (length == t.parts.length);
		}
		public String toString() {
			StringBuffer buf = new StringBuffer();
			int pos = 1;
			for (int i = 0; i < length; ++i) {
				if ((includeMask & pos) != 0) {
					buf.append('G');
				} else {
					buf.append('_');
				}
				pos = pos << 1;
			}
			return buf.toString();
		}
	}

	//TODO: faster integer to object HashMap
	private HashMap<Integer, Signature> mostSpecific = new HashMap<Integer, Signature>();
	//active set depends ONLY on arity
	private ArrayList<Signature>[] activeSet = new ArrayList[3];
	
	//get or create
	private Signature getMostSpecific(PartialGround probe) {
		int includeMask = Signature.makeIncludeMask(probe);
		Signature mss = mostSpecific.get(includeMask);
		//if we don't have it even once, create it
		if (mss == null) {
			probes[probe.parts.length-1] = new PartialGround(probe.parts.length); //prevent our current probe from being overwritten
			mss = new Signature(probe.parts.length, includeMask);
			makeSignatureActive(mss);
		}
		return mss;
	}
	//get or create
	private ArrayList<Signature> getAllActive(int arity) {
		if (arity > activeSet.length) {
			ArrayList<Signature>[] more = new ArrayList[arity];
			System.arraycopy(activeSet, 0, more, 0, activeSet.length);
			activeSet = more;
		}
		ArrayList<Signature> sigs = activeSet[arity-1];
		if (sigs == null) {
			sigs = new ArrayList<Signature>();
			activeSet[arity-1] = sigs;
		}
		return sigs;
	}
	private void makeSignatureActive(Signature sig) {
		//we have no choice but to try to add everything using this signature
		for (IndexEntry ti : grndToEntry.values()) {
			if (sig.matchesGrnd(ti.term)) {
				PartialGround probe = getProbe(ti.term.parts, sig);
				IRandomAccessible<IndexEntry> basket = getOrCreateBestCollection(probe);
				basket.add(ti);
			}
		}
		getAllActive(sig.length).add(sig);
		mostSpecific.put(sig.includeMask, sig);
		if (SGDebug.INDEXING) {
			System.out.println("added sig = "+sig);
		}
	}
	
	static class PartialGround {
		PartialGround(int arity) {
			parts = new ATerm[arity];
		}
		PartialGround(ATerm[] parts) {
			this.parts = parts;
		}
		PartialGround(PartialGround pg) {
			this.parts = new ATerm[pg.parts.length];
			for (int i = 0; i < parts.length; ++i) {
				this.parts[i] = pg.parts[i];
			}
		}
		ATerm[] parts;
		void set(ATerm[] p) {
			assert (p.length == parts.length);
			for (int i = 0; i < p.length; ++i) {
				if (p[i].isGround(null)) {
					parts[i] = p[i];
				} else {
					parts[i] = null;
				}
			}
		}
		public int hashCode() {
			int h = ATerm.mdjbFirst();
			for (ATerm t : parts) {
				if (t == null) {
					h = ATerm.mdjbNext(h, 0);
				} else {
					h = ATerm.mdjbNext(h, t.hashCode());
				}
			}
			return h;
		}
		public boolean equals(Object o) {
			if (!(o instanceof PartialGround)) {
				return false;
			}
			PartialGround pg = (PartialGround)o;
			if (pg.parts.length != parts.length) {
				return false;
			}
			for (int i = 0; i < parts.length; ++i) {
				if (parts[i] == null) {
					if (pg.parts[i] != null) {
						return false;
					}
				} else if (!parts[i].equals(pg.parts[i])) {
					return false;
				}
			}
			return true;
		}
		public String toString() {
			return "PartialGround "+Lang.stringList(parts, ", ");
		}
	}
	
	//CONSIDER: take a shouldCountAccess parameter, so we don't create sigs for estimateMatching
	//  well, then we may end up with bad grounding plans because estimateMatching is bad
	private IRandomAccessible<IndexEntry> getBestCollection(ATerm[] parts) {
		PartialGround probe = getProbe(parts.length);
		for (int i = 0; i < probe.parts.length; ++i) {
			if (parts[i] != null && parts[i].isGround(null)) {
				probe.parts[i] = parts[i];
			} else {
				probe.parts[i] = null;
			}
		}
		//needed to make sure index is hashed on the signature of probe
		Signature mss = getMostSpecific(probe); 
		++mss.useCount;
		++totalAccesses;
		IRandomAccessible<IndexEntry> basket = index.get(probe);
		//if (SGDebug.FORMULA_FIND_GROUND) checkBasket(basket,probe);
		return basket;
	}
	
	private static void checkBasket(IRandomAccessible<IndexEntry> basket, PartialGround probe) {
		if (basket == null) return;
		for (IndexEntry ie : basket) {
			ScanTerm t = ie.term;
			if (t.parts.length != probe.parts.length) {
				throw new Error("No match "+t+" != "+probe);
			}
			for (int i = 0; i < probe.parts.length; ++i) {
				if (probe.parts[i] != null && !probe.parts[i].equals(t.parts[i])) {
					throw new Error("No match "+t+" != "+probe);
				}
			}
		}
	}
	
	private PartialGround getProbe(ATerm[] parts, Signature sig) {
		PartialGround probe = getProbe(parts.length);	
		int pos = 1;
		for (int i = 0; i < probe.parts.length; ++i) {
			if ((sig.includeMask & pos) == 0) {
				probe.parts[i] = null;
			} else {
				probe.parts[i] = parts[i];
			}
			pos = pos << 1;
		}
		return probe;
	}
	
	private IRandomAccessible<IndexEntry> getOrCreateBestCollection(PartialGround probe) {		
		IRandomAccessible<IndexEntry> basket = index.get(probe);
		//if (SGDebug.FORMULA_FIND_GROUND) checkBasket(basket,probe);
		//CONSIDER: singleton baskets will be common, perhaps create a singleton IRandomAccessible
		if (basket == null) {
			basket = new RandSingle<IndexEntry>();
			index.put(new PartialGround(probe), basket);
		} else if (basket instanceof RandSingle && !basket.isEmpty()) {
			IndexEntry val = basket.iterator().next();
			basket = new RandArray<IndexEntry>();
			basket.add(val);
			index.put(new PartialGround(probe), basket);
		} else if (!(basket instanceof RandomSet) && basket.size() >= CONVERT_TO_HASHSET_SIZE) {
			basket = new RandomSet<IndexEntry>(basket);
			index.put(new PartialGround(probe), basket);
		}
		
		return basket;
	}
	
	private static IndexEntry ieProbe = new IndexEntry(null);
	public void remove(ScanTerm t, boolean defined) {
		//find all signatures for t and remove them
		ArrayList<Signature> sigs = getAllActive(t.parts.length);
		for (Signature sig : sigs) {
			PartialGround probe = getProbe(t.parts, sig);
			IRandomAccessible<IndexEntry> basket = index.get(probe);
			ieProbe.term = t;
			if (basket != null) {
				basket.remove(ieProbe); //if this causes a concurrent mod exception fix findType to not return SIMPLE_FIND
				if (basket.isEmpty()) {
					index.remove(probe);
				}
			} else if (SGDebug.BASIC) {
				System.err.println("removal failed on "+t+" "+(defined?"DEF":""));
			}
		}

		//remove from core index
		IndexEntry toUncreate = grndToEntry.remove(t);
		if (toUncreate != null) {
			if ((toUncreate instanceof DefIndexEntry) != defined) {
				throw new Error("The proposal distribution cannot suggest terms that are defined by DEF>.");
			}
			toUncreate.uncreate(); //note, this can call remove
		} else {
			throw new Error("Not present for removal "+t);
		}
	}
	
	public void add(IndexEntry ti) {
		//core add
		IndexEntry e = grndToEntry.put(ti.term, ti);
		if (e != null) {
			grndToEntry.put(ti.term, e); //restore previous entry
			throw new SGSearch.AlreadyTrueException("Already present "+ti.term);
		}
		
		//for all signatures, add	
		ArrayList<Signature> sigs = getAllActive(ti.term.parts.length);
		for (Signature sig : sigs) {
			PartialGround probe = getProbe(ti.term.parts, sig);
			IRandomAccessible<IndexEntry> basket = getOrCreateBestCollection(probe);
			basket.add(ti); //if this causes a concurrent mod exception fix findType to not return SIMPLE_FIND
		}
	}
	
	PartialGround[] probes = new PartialGround[3];
	private PartialGround getProbe(int arity) {
		if (probes.length < arity) {
			PartialGround[] moreProbes = new PartialGround[arity];
			System.arraycopy(probes, 0, moreProbes, 0, probes.length);
			probes = moreProbes;
		}
		if (probes[arity-1] == null) {
			probes[arity-1] = new PartialGround(arity);
		}
		return probes[arity-1];
	}
	
	//could also just use a more general sig and sample with rejection
	private Random r = new Random();
	public ScanTerm randomMatch(ATerm[] specifiers) {
		IRandomAccessible<IndexEntry> basket = getBestCollection(specifiers);
		if (basket == null) {
			return null;
		}

		IndexEntry ie = basket.getRandom(r);
		if (ie == null) {
			return null;
		}
		return ie.term;
		/*
		ScanTerm t = null;
		//try 20 random
		for (int attempt = 0; attempt < 20; ++attempt) {
			IndexEntry ie = basket.getRandom(r);
			if (ie == null) return null;
			t = ie.term;
			for (int pi = 0; pi < specifiers.length; ++pi) {
				if (specifiers[pi] != null && !specifiers[pi].equals(t.parts[pi])) {
					t = null; break;
				}
			}
			if (t != null) return t;
		}
		//give up and scan
		for (IndexEntry e : basket) {
			t = e.term;
			for (int pi = 0; pi < specifiers.length; ++pi) {
				if (specifiers[pi] != null && !specifiers[pi].equals(t.parts[pi])) {
					t = null; break;
				}
			}
			if (t != null) return t;
		}
		return null;
		*/
	}
	
	
	private static final int FULLY_GROUND = 0;
	private static final int SIMPLE_FIND = 1;
	private static final int SCAN = 2;
	private ArrayList<ATerm> vars = new ArrayList<ATerm>();
	private int findType(ScanTerm t) {
		int type = FULLY_GROUND;
		vars.clear();
		for (int i = 0; i < t.parts.length; ++i) {
			if (!t.parts[i].isGround(null)) {
				//return SCAN;
				//TODO: if we implement this we must queue up incoming additions, otherwise we get concurrent mod exception on add
				//that is extremely unlikely though. 
				//If you don't use blank predicates it is impossible, since we don't allow defs and proposal terms to overlap
				
				type = SIMPLE_FIND;
				if (t.parts[i] instanceof VarTerm && vars.contains(t.parts[i])) {
					//need to check that the VarTerm is not repeated
					//only works if SGSearch is very careful about keeping VarTerms that have same varNum identical
					vars.add(t.parts[i]);
				} else {
					return SCAN;
				}
				
			}
		}
		return type;
	}
	
	private static final RandArray<IndexEntry> NO_MATCHING = new RandArray<IndexEntry>();
	public IRandomAccessible<IndexEntry> find(ScanTerm t, Binds binds) {
		t = (ScanTerm)t.ground(binds, Function.NO_LINK);
		
		int type = findType(t);
		
		if (type == FULLY_GROUND) { 
			RandArray<IndexEntry> matching = new RandArray<IndexEntry>();
			//fully ground, just a lookup
			IndexEntry e = grndToEntry.get(t);
			if (e != null) {
				matching.add(e);
			}
			return matching;
		}
		
		IRandomAccessible<IndexEntry> basket = getBestCollection(t.parts);
		if (basket == null) {
			return NO_MATCHING;
		}
		if (type == SIMPLE_FIND) {
			return basket;
		}
		RandArray<IndexEntry> matching = new RandArray<IndexEntry>();
		for (IndexEntry e : basket) {
			if (t.matches(e.term, binds)) {
				matching.add(e);
			}
		}
		return matching;
	}

	public int estimateMatching(ScanTerm t, Binds binds) {
		t = (ScanTerm)t.ground(binds, Function.NO_LINK);
		
		if (t.isGround(null)) {
			return grndToEntry.get(t) != null ? 1 : 0;
		}
		
		IRandomAccessible<IndexEntry> basket = getBestCollection(t.parts);
		if (basket == null) {
			return 0;
		}
		return basket.size();
	}
	
	public Iterator<IndexEntry> getGroundTerms() {
		return grndToEntry.values().iterator();
	}
	
	public IndexEntry contains(ScanTerm t) {
		return grndToEntry.get(t);
	}

	//load and save active signatures (like in a parameters file)
	//the signatures will be loaded and saved in the policy control file
	private static final String SIG_PREFIX = "SIG=";
	public void loadConfig(String[] lines) {
		if (!grndToEntry.isEmpty()) {
			System.err.println("Cannot load configuration after entries are present in the ground term table!");
		}
		//mostSpecific.clear();
		//activeSet = new ArrayList[3];
		for (String line : lines) {
			if (!line.startsWith(SIG_PREFIX)) {
				continue;
			}
			line = line.substring(SIG_PREFIX.length());
			Signature sig = Signature.fromString(line);
			if (mostSpecific.put(sig.includeMask, sig) == null) {
				getAllActive(sig.length).add(sig);
			}
			//TODO: also need to re-hash
		}
	}
	
	public String configToString() {
		StringBuffer buf = new StringBuffer();
		for (Signature sig : mostSpecific.values()) {
			buf.append(SIG_PREFIX);
			buf.append(sig.toString());
			buf.append('\n');
		}
		return buf.toString();
	}
	
	public int size() {
		return grndToEntry.size();
	}

}
