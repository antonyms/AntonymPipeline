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
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.util.IRandomAccessible;
import com.ibm.bluej.consistency.util.RandArray;
import com.ibm.bluej.util.common.HashMapUtil;


public class GroundFindFormulaMap extends GroundFindFormula {

	private ATerm[] quicksave;
	public GroundFindFormulaMap(ATerm[] quicksave) {
		this.quicksave = quicksave;
	}
	
	//first k indexing
	//use a tree of possible matches (one for positive and one for negative)
	//also IdentityHashMap<formula, to it's index entries>
	
	private IdentityHashMap<Formula, ArrayList<FormulaTermNode>> forRemoval = new IdentityHashMap<Formula, ArrayList<FormulaTermNode>>();
	private ArrayList<FormulaTermNode> roots = new ArrayList<FormulaTermNode>();
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (FormulaTermNode r : roots) {
			FormulaTermNode.stringTree(r, "", buf);
		}
		return buf.toString();
	}
	
	static class FormulaTermNode {
		FormulaTermNode(FormulaTermNode parent, ATerm myIndex) {
			this.parent = parent;
			this.myIndex = myIndex;
			children = new HashMap<ATerm, FormulaTermNode>();
			maybeMatching = new ArrayList<FormulaTermEntry>();
		}
		
		ATerm myIndex;
		FormulaTermNode parent;
		HashMap<ATerm, FormulaTermNode> children; //TODO: more advanced version would have ArrayList of HashMap, permitting skips in the grounding
		ArrayList<FormulaTermEntry> maybeMatching; 
		
		FormulaTermNode findOrCreateMostSpecific(ScanTerm term, int pos) {
			if (term.parts.length > pos && term.parts[pos].isGround(null)) {
				FormulaTermNode child = children.get(term.parts[pos]);
				if (child == null) {
					child = new FormulaTermNode(this, term.parts[pos]);
					children.put(child.myIndex, child);
				}
				return child.findOrCreateMostSpecific(term, pos+1);
			} else {
				return this;
			}
		}
		
		void findAllMatching(ScanTerm t, ArrayList<FormulaTermEntry> matching, int pos, Binds b) {
			for (FormulaTermEntry fte : maybeMatching) {
				if (fte.term.matches(t, b)) {
					matching.add(fte);
				}
			}
			
			if (t.parts.length > pos) {
				FormulaTermNode child = children.get(t.parts[pos]);
				if (child != null) {
					child.findAllMatching(t, matching, pos+1, b);
				}
			}
		}
		
		void remove(Formula f) {
			for (int i = 0; i < maybeMatching.size(); ++i) {
				if (maybeMatching.get(i).formula == f) {
					maybeMatching.remove(i);
					--i;
				}
			}
			//if both children and maybeMatching are null, remove this
			if (children.size() == 0 && maybeMatching.size() == 0 && parent != null) {
				parent.removeChild(myIndex);
			}
		}
		
		void removeChild(ATerm childIndex) {
			children.remove(childIndex);
			//if both children and maybeMatching are null, remove this
			if (children.size() == 0 && maybeMatching.size() == 0 && parent != null) {
				parent.removeChild(myIndex);
			}
		}
		
		static void stringTree(FormulaTermNode n, String indent, StringBuffer buf) {
			for (FormulaTermEntry fte : n.maybeMatching) {
				buf.append(fte.term.toString()).append("; ");
			}
			buf.append('\n');
			indent = indent + "  ";
			for (Map.Entry<ATerm, FormulaTermNode> c : n.children.entrySet()) {
				buf.append(indent).append(c.getKey()).append("->");
				stringTree(c.getValue(), indent, buf);
			}
		}
	}
	
	static class FormulaTermEntry {
		FormulaTermEntry(Formula formula, ScanTerm term, int condNdx) {
			this.formula = formula;
			this.term = term;
			this.index = condNdx;
		}
		ScanTerm term;
		Formula formula;
		int index;
	}
	
	public IRandomAccessible<Formula> findMatching(boolean positive, ScanTerm term) {
		RandArray<Formula> found = new RandArray<Formula>();
		int rootNdx = (term.parts.length << 1) + (positive ? 1 : 0);
		if (rootNdx >= roots.size()) {
			return found;
		}
		ArrayList<FormulaTermEntry> matching = new ArrayList<FormulaTermEntry>();
		Binds binds = new Binds(quicksave);
		roots.get(rootNdx).findAllMatching(term, matching, 0, binds);
		for (FormulaTermEntry m : matching) {
			found.add(m.formula);
		}
		return found;
	}
	
	public boolean groundNew(boolean positive, IndexEntry ti) {
		Binds binds = new Binds(quicksave);
		List<Integer> conditions = new ArrayList<Integer>();
		
		int rootNdx = (ti.term.parts.length << 1) + (positive ? 1 : 0);
		if (rootNdx >= roots.size()) {
			return false;
		}
		ArrayList<FormulaTermEntry> matching = new ArrayList<FormulaTermEntry>();
		roots.get(rootNdx).findAllMatching(ti.term, matching, 0, binds);
		boolean oneGrounding = false;	
		for (FormulaTermEntry m : matching) {
			IndexEntry[] found = new IndexEntry[m.formula.condition.length];
			((ScanTerm)m.formula.condition[m.index]).bind(ti.term, binds);
			found[m.index] = ti;
			oneGrounding = m.formula.groundOut(binds, found, conditions) || oneGrounding;
			binds.clear();
			conditions.clear();
		}
		
		return oneGrounding;
	}


	public void add(Formula f) {
		for (int condNdx = 0; condNdx < f.condition.length; ++condNdx) {
			if (f.condition[condNdx] instanceof ScanTerm) {
				ScanTerm term = (ScanTerm)f.condition[condNdx];
				boolean positive = f.isPositive(condNdx);
				int rootNdx = (term.parts.length << 1) + (positive ? 1 : 0);
				addOne(f, rootNdx, term, condNdx);
			}
		}
		if (f.condition.length == 0) {
			forRemoval.put(f, null);
		}
		Binds binds = new Binds(quicksave);
		IndexEntry[] found = new IndexEntry[f.condition.length];
		f.groundOut(binds, found, new ArrayList<Integer>());
	}

	private void addOne(Formula f, int rootNdx, ScanTerm term, int condNdx) {
		while (roots.size() <= rootNdx) {
			roots.add(new FormulaTermNode(null, null));
		}
		FormulaTermNode myroot = roots.get(rootNdx);
		FormulaTermNode mostSpec = myroot.findOrCreateMostSpecific(term, 0);
		FormulaTermEntry one = new FormulaTermEntry(f, term, condNdx);
		mostSpec.maybeMatching.add(one);
		HashMapUtil.addAL(forRemoval, f, mostSpec);
	}

	public void remove(Formula f) {
		ArrayList<FormulaTermNode> indexEntries = forRemoval.remove(f);
		for (FormulaTermNode ftn : indexEntries) {
			ftn.remove(f);
		}
		f.uncreate();
	}

	public void clear() {
		//clear all, re-add the non-removable formulas later
		roots.clear();
		forRemoval.clear();
	}

	public Iterator<Formula> getFormulas() {
		return forRemoval.keySet().iterator();
	}

}
