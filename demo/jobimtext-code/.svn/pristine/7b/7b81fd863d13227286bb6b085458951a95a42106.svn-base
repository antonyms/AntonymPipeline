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

package com.ibm.bluej.consistency.formula;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.DummyIndexEntry;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.rl.GroundingPlan;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.CheckTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.FunctionConditional;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.Lang;



public class Formula {
	public Creator creator;
	
	public ATerm[] condition;
	private GroundingPlan groundingPlan;
	private CRFState crfState;
	//TODO: every Formula should hold a CRFState, it then passes it to the creates
	
	public Formula(Creator creator, ATerm[] condition, boolean[] positive, GroundingPlan groundingPlan, CRFState crfState) {
		this.groundingPlan = groundingPlan;
		//Note that bagDefinition creates formulas with the creator initially null
		assert(condition.length == positive.length);
		this.creator = creator;
		this.condition = condition;
		this.typeFlags = new int[condition.length];
		for (int i = 0; i < typeFlags.length; ++i) {
			int termType = getTermType(condition[i]);
			typeFlags[i] = termType << 1 | (positive[i] ? 1 : 0);
		}
		this.crfState = crfState;
	}
	
	public String conditionToString(Binds binds) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < condition.length; ++i) {
			if (i > 0) {
				buf.append(" ^ ");
			}
			if (!isPositive(i)) {
				buf.append("!");
			}
			buf.append(condition[i].ground(binds, Function.NO_LINK).toString());
		}
		return buf.toString();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(creator);
		if (condition.length > 0) {
			buf.append(" :: ");
		}
		for (int i = 0; i < condition.length; ++i) {
			if (i > 0) {
				buf.append(" ^ ");
			}
			if (!isPositive(i)) {
				buf.append("!");
			}
			buf.append(condition[i].toString());
		}
		return buf.toString();
	}
	
	protected void addCreate(FormulaCreate c) {
		//Nothing for normal (non-removable) formula
	}
	public void uncreate() {
		throw new UnsupportedOperationException("You can only call uncreate on RemovableFormula");
	}
	
	//TODO: need outer groundOut so we can call reward on the groundingPlan
	//create new version of groundOut, keep old
	
	public boolean groundOut(Binds binds, IndexEntry[] found, List<Integer> conditions) {
		try {
			crfState.groundingLimits.zzCheckGroundOutCount();
		} catch (SGSearch.ExcessiveGroundOut e) {
			e.printStackTrace();
			System.err.println(this);
			System.err.println("BINDS: "+binds);
			System.err.println("FOUND ENTRIES: "+Lang.stringList(found, ", "));
			System.err.println("CONDITION INDICES: "+Lang.stringList(conditions, ","));
			throw e;
		}
		//need to print every call here when ground debug is enabled
		if (SGDebug.GROUND_OUT) {
			SGLog.fine(this.toString());
			SGLog.fine(binds.toString());
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < found.length; ++i) {
				IndexEntry e = found[i];
				if (conditions.contains(i)) {
					buf.append("*");
				} else {
					buf.append(" ");
				}
				if (e == null) {
					buf.append("?     ");
				} else if (e == DummyIndexEntry.check) {
					buf.append("CHECK ");
				} else if (e == DummyIndexEntry.negScan) {
					buf.append("NEG   ");
				} else {
					buf.append("FOUND ");
				}
			}
			SGLog.fine(buf.toString());
			
		}
		
		//for every formula.condition not matched in found
		//  is it a fully ground check term
		//    check it
		//  estimate size of expand or match
		//continue with best condition part
		//if all condition parts are bound we add the create
		int bestFound = -1;
		int bestEstimated = Integer.MAX_VALUE;
		boolean allFound = true;
		//TODO: use groundingPlan
		for (int fi = 0; fi < condition.length; ++fi) {
			if (found[fi] == null) {
				allFound = false;
				int estimated;
				if (termType(fi) == CHECK_TERM || 
						(termType(fi) == EXPAND_TERM)) 
				{
					if (condition[fi].isGround(binds)) {
						Boolean result = ((CheckTerm)condition[fi]).checkConstant(binds);
						//if it depends on a function it auto-succeeds 
						//and is added to the list of Check terms for the functionConditional
						if (result == null) {
							conditions.add(fi);
						} else {
							if (isPositive(fi) != result) {
								return false;
							}
						}
						found[fi] = DummyIndexEntry.check;
						boolean wentWell = groundOut(binds, found, conditions);
						found[fi] = null;
						if (result == null) { conditions.remove(conditions.size()-1); }
						return wentWell;
					//an expand term that is negated can only be used as a check term	
					} else if (termType(fi) == EXPAND_TERM && isPositive(fi)) { 
						estimated = ((ExpandTerm)condition[fi]).estimateExpand(binds);
					} else {
						estimated = Integer.MAX_VALUE;
					}
					
				} else {
					assert(termType(fi) == SCAN_TERM);
					//handle ground first as special case; same semantics as check term
					if (condition[fi].isGround(binds)) {
						ScanTerm cgrnd = (ScanTerm)condition[fi].ground(binds, Function.NO_LINK);
						IndexEntry e = crfState.posGrnd.contains(cgrnd);
						//if (e == null) {
						//	//add to SGSearch.motivated if term is not found
						//	SGSearch.addMotivated(cgrnd);
						//}
						if (isPositive(fi)) {
							if (e == null) {
								return false;
							}
							found[fi] = e;
						} else {
							if (e != null) {
								return false;	
							}
							found[fi] = DummyIndexEntry.negScan;
						}
						boolean wentWell = groundOut(binds, found, conditions);
						found[fi] = null;
						return wentWell;
					}
					
					//scan term, use posGrnd to estimate
					if (isPositive(fi)) {
						estimated = crfState.posGrnd.estimateMatching((ScanTerm)condition[fi], binds);
					} else {
						estimated = Integer.MAX_VALUE;
					}
				}
				if (estimated < bestEstimated) {
					bestFound = fi;
					bestEstimated = estimated;
				}
				//CONSIDER: break on 1 too?
				//Definitely need to break on 0
				if (estimated < 2) {
					break;
				}
			}
		}
		//base case, all conditions found
		if (allFound) {
			if (creator == null) {
				System.err.println(this);
			}
			FormulaCreate c = creator.create(binds, crfState);
			if (conditions.size() != 0) {
				c = new FunctionConditional(c, this, conditions, binds);
			}
			if (SGDebug.LOGGING) SGLog.fine("Creating: "+c+" from "+this+" "+binds);
			this.addCreate(c);
			c.add(this);

			for (int fci = 0; fci < found.length; ++fci) {
				IndexEntry e = found[fci];
				if (e != DummyIndexEntry.check) {
					if (e == DummyIndexEntry.negScan) {
						//does the index entry exist in the negGrnd?
						// if not create it
						ScanTerm cgrnd = (ScanTerm)condition[fci].ground(binds, Function.NO_LINK);
						e = crfState.negGrnd.contains(cgrnd);
						if (e == null) {
							//no way to know if this should be DefIndexEntry or regular IndexEntry
							e = new IndexEntry(cgrnd);
							crfState.negGrnd.add(e);
						}
					}
					e.addCreate(c);
				}
			}
			return true;
		}
		if (bestFound == -1) {
			//TODO: more detail
			System.err.println(this);
			System.err.println(binds);
			throw new Error("Formula cannot be grounded!");
		}
		//we don't get here with bestFound as check term
		//loop over matching ground or expanded ground
		//call set found[bestFound] and call groundOut recursively
		//checkpoint and rollback binds
		boolean atLeastOneGround = false;
		Object cp = binds.checkpoint();
		if (termType(bestFound) == EXPAND_TERM) {
			//loop over extended binds
			Iterator<Binds> bit = ((ExpandTerm)condition[bestFound]).expand(binds);
			found[bestFound] = DummyIndexEntry.check;
			while (bit.hasNext()) {
				binds.expand(bit.next());
				atLeastOneGround = groundOut(binds, found, conditions) || atLeastOneGround;
				binds.rollback(cp);
			}
		} else {
			//loop over matching ground
			ScanTerm st = (ScanTerm)condition[bestFound];
			Collection<IndexEntry> matching = crfState.posGrnd.find(st, binds);
			for (IndexEntry m : matching) {
				found[bestFound] = m;
				st.bind(m.term, binds);
				atLeastOneGround = groundOut(binds, found, conditions) || atLeastOneGround;
				binds.rollback(cp);
			}
		}
		found[bestFound] = null;
		return atLeastOneGround;
	}
	
	private int[] typeFlags;
	public final boolean isPositive(int i) {
		return (typeFlags[i] & 1) == 1;
	}
	public final int termType(int i) {
		return typeFlags[i] >> 1;
	}
	public final boolean canMatch(int i, boolean positive) {
		return typeFlags[i] == (positive ? 1 : 0);
	}
	public static final int SCAN_TERM = 0;
	public static final int CHECK_TERM = 1;
	public static final int EXPAND_TERM = 2;
	private static int getTermType(ATerm t) {
		if (t instanceof ScanTerm) {
			return 0;
		}
		if (t instanceof ExpandTerm) {
			return 2;
		}
		if (t instanceof CheckTerm) {
			return 1;
		}
		throw new Error("Bad term type: "+t);
	}
}
