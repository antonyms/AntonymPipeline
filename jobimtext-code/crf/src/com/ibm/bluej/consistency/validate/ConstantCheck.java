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

package com.ibm.bluej.consistency.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.ibm.bluej.consistency.DefineCreator;
import com.ibm.bluej.consistency.ParserAntrl;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.BagDefinition;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.SetDefinition;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;



public final class ConstantCheck {
	public static void main(String[] args) {
		String contents = FileUtil.readFileAsString("test.con");
		Collection<Formula> formulas = ParserAntrl.parse(SGSearch.getCRFState(), contents).formulas;
		Collection<ConstantsPattern> cPats = constantsInference(formulas);
		for (ConstantsPattern cPat : cPats) {
			System.out.println(cPat);
		}
		for (String error : checkConstant(formulas)) {
			System.out.println(error);
		}
	}
	
	//if a term occurs in a non-constant position in a ScanTerm
	//  that term must be a variable that occurs in NO OTHER ScanTerm in the condition
	public static HashSet<String> checkConstant(Collection<Formula> formulas) {
		//returns a HashSet of error messages
		HashSet<String> errors = new HashSet<String>();
		Collection<ConstantsPattern> cPats = constantsInference(formulas);
		for (Formula f : formulas) {

			//the number of times each variable occurs in a ScanTerm
			HashMap<VarTerm, MutableDouble> varCount = new HashMap<VarTerm, MutableDouble>();
			for (int i = 0; i < f.condition.length; ++i) {
				ATerm c = f.condition[i];
				//if a NONCONST occurs in an ExpandTerm, 
				//the grounding can dead-end if the NONCONST variable is Expanded
				//this isn't necessarily an error though, the ExpandTerm might have expansions defined that ensure a grounding
				if (c instanceof ScanTerm || (c instanceof ExpandTerm && f.isPositive(i))) {
					countVars(c, varCount);
				}
			}
			for (ATerm c : f.condition) {
				for (ConstantsPattern cPat : cPats) {
					if (cPat.matches(c)) {
						ScanTerm st = (ScanTerm)c;
						for (int i = 0; i < cPat.parts.length; ++i) {
							if (cPat.parts[i] == nonConst) {
								if (st.parts[i] instanceof VarTerm) {
									VarTerm v = (VarTerm)st.parts[i];
									//check it's count, should be 1
									MutableDouble d = varCount.get(v);
									if (d.value != 1) {
										errors.add("The term "+st+" matches the type signature "+cPat+
										" but a variable in a NONCONST position occurs more than once in a ScanTerm or ExpandTerm");
									}
								} else {
									errors.add("The term "+st+" matches the type signature "+cPat+
											" but one of the NONCONST is not bound to a variable");
								}
							}
						}
					}
				}
			}
		}
		return errors;
	}
	
	static void countVars(ATerm t, HashMap<VarTerm, MutableDouble> varCount) {
		if (t instanceof VarTerm) {
			MutableDoubleHashMap.increase(varCount, (VarTerm)t, 1.0);
		} else if (t instanceof CompositeTerm) {
			for (ATerm p : ((CompositeTerm)t).parts) {
				countVars(p, varCount);
			}
		}
	}
	


	
	private static Collection<VarTerm> gatherNonConstVars(ATerm[] conditions, Collection<ConstantsPattern> constantPatterns) {
		Collection<VarTerm> nonConstVars = new HashSet<VarTerm>();
		for (ATerm cond : conditions) {
			for (ConstantsPattern cPat : constantPatterns) {
				//if cond matches a cPat then all vars in nonConst parts are nonConst
				if (cPat.matches(cond)) {
					for (VarTerm t : cPat.gather((ScanTerm)cond)) {
						nonConstVars.add(t);
					}
				}
			}
		}
		return nonConstVars;
	}

	static final Function nonConst = new Function() {
		public ATerm getValue() {return null;}
		public void update(Function source, Object... msg) {}
	};
	
	static void gatherVars(ATerm t, Collection<VarTerm> ncvs) {
		if (t instanceof VarTerm) {
			ncvs.add((VarTerm)t);
		} else if (t instanceof CompositeTerm) {
			for (ATerm p : ((CompositeTerm)t).parts) {
				gatherVars(p, ncvs);
			}
		}
	}
	
	private static class ConstantsPattern {
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("[");
			for (int i = 0; i < parts.length; ++i) {
				if (i > 0) {
					buf.append(" ");
				}
				if (parts[i] == null) {
					buf.append("_");
				} else if (parts[i] instanceof StringTerm) {
					buf.append(parts[i].toString());
				} else if (parts[i] == nonConst) {
					buf.append("NONCONST");
				} else {
					buf.append("ERROR:"+parts[i]);
				}
			}
			buf.append("]");
			return buf.toString();
		}
		
		ConstantsPattern(int size) {
			parts = new ATerm[size];
		}
		ATerm[] parts;
		

		
		Collection<VarTerm> gather(ScanTerm t) {
			Collection<VarTerm> ncvs = new ArrayList<VarTerm>();
			assert (t.parts.length == parts.length);
			for (int i = 0; i < parts.length; ++i) {
				if (parts[i] == nonConst) {
					gatherVars(t.parts[i], ncvs);
				}
			}
			return ncvs;
		}
		
		boolean matches(ATerm t) {
			if (!(t instanceof ScanTerm)) {
				return false;
			}
			ATerm[] tparts = ((ScanTerm)t).parts;
			if (tparts.length != parts.length) {
				return false;
			}
			for (int i = 0; i < parts.length; ++i) {
				if (parts[i] instanceof StringTerm) {
					if (!parts[i].equals(tparts[i]) && !(tparts[i] instanceof VarTerm)) {
						return false;
					}
				}
			}
			return true;
		}
		public int hashCode() {
			int hash = ATerm.mdjbFirst();
			for (int i = 0; i < parts.length; ++i) {
				hash = ATerm.mdjbNext(hash, parts[i] == null ? 0 : parts[i].hashCode());
			}
			return hash;
		}
		public boolean equals(Object o) {
			if (!(o instanceof ConstantsPattern)) {
				return false;
			}
			ConstantsPattern cp = (ConstantsPattern)o;
			if (this.parts.length != cp.parts.length) {
				return false;
			}
			for (int i = 0; i < parts.length; ++i) {
				if (this.parts[i] != cp.parts[i]) {
					return false;
				}
			}
			return true;
		}
	}
	
	
	

	//a position in a ScanTerm is non-constant if
	//  the ScanTerm is a define
	//  and that position of a ScanTerm contains a Collection or Function of a Collection
	//  that position contains a non-constant variable or a function of a non-constant variable
	//This will just be a basic inference that will conservatively rule things out
	//It will err on the safe side
	static Collection<ConstantsPattern> constantsInference(Collection<Formula> formulas) {
		//build non-constant patterns
		//assuming x,y are const vars and z is non-const
		//outcount(x,y,size({})) -> [outcount _ _ nonConst]
		//(x blah {}) -> [_ blah nonConst]
		//happy(funtime(x,y), z) -> [happy _ nonConst] 
		//CONSIDER: happy(scanTerm(x,z), y) -> [happy nonConst _]
		// but more fine grained would say (happy (scanTerm _ nonConst) _)
		
		//outcount(x,y,pair(c,d)) is an error - in this version
		//outcount(x,y,c) matches [outcount _ _ nonConst]
		//  c is now non-const var
		//happy(c,{}) :: outcount(x,y,c) means that we add [happy nonConst nonConst]
		
		
		//these Term[] contain StringTerms, nulls or the special Function, nonConst
		HashSet<ConstantsPattern> constantPatterns = new HashSet<ConstantsPattern>();
		boolean changed = false;
		FunST<ATerm, Boolean> defsCollection = new FunST<ATerm, Boolean>() {
			public Boolean f(ATerm o) {
				if (o instanceof BagDefinition || o instanceof SetDefinition) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
		};
		do {
			changed = false;
			for (Formula f : formulas) {
				if (f.creator instanceof DefineCreator) {
					//locate the nonConst vars in the condition
					final Collection<VarTerm> nonConstVars = gatherNonConstVars(f.condition, constantPatterns);
					
					FunST<ATerm, Boolean> isNonConstVar = new FunST<ATerm, Boolean>() {
						public Boolean f(ATerm o) {
							if (nonConstVars.contains(o)) {
								return Boolean.TRUE;
							}
							return Boolean.FALSE;
						}
					};
					
					ScanTerm c = ((DefineCreator)f.creator).toDefine;
					ConstantsPattern cPat = new ConstantsPattern(c.parts.length);
					//loop through the parts of c
					//  it can be a StringTerm
					//  contain a SetDefinition or a BagDefinition
					//  or neither
					for (int i = 0; i < c.parts.length; ++i) {
						if (c.parts[i] instanceof StringTerm) {
							cPat.parts[i] = c.parts[i];
						} else if (ATerm.testRecursive(c.parts[i], defsCollection)) {
							cPat.parts[i] = nonConst;
						} else if (ATerm.testRecursive(c.parts[i], isNonConstVar)) {
							cPat.parts[i] = nonConst;
						} else {
							cPat.parts[i] = null;
						}
					}
					//System.out.println(cPat);Lang.readln();
					changed = constantPatterns.add(cPat) || changed;
				}
			}
		} while (changed);
		return constantPatterns;
	}
}
