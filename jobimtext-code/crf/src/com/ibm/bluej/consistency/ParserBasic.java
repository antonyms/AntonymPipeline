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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.consistency.focus.FocusIndicatorState;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.ValueTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.term.corefunc.ExGensym;
import com.ibm.bluej.consistency.term.corefunc.ExInside;
import com.ibm.bluej.consistency.term.corefunc.ExMember;
import com.ibm.bluej.consistency.term.corefunc.ExOutside;
import com.ibm.bluej.consistency.term.corefunc.ExSucc;
import com.ibm.bluej.consistency.term.corefunc.FuncAbs;
import com.ibm.bluej.consistency.term.corefunc.FuncAverage;
import com.ibm.bluej.consistency.term.corefunc.FuncConcat;
import com.ibm.bluej.consistency.term.corefunc.FuncCosine;
import com.ibm.bluej.consistency.term.corefunc.FuncDiv;
import com.ibm.bluej.consistency.term.corefunc.FuncDotProduct;
import com.ibm.bluej.consistency.term.corefunc.FuncEquals;
import com.ibm.bluej.consistency.term.corefunc.FuncExp;
import com.ibm.bluej.consistency.term.corefunc.FuncIdentity;
import com.ibm.bluej.consistency.term.corefunc.FuncIntersection;
import com.ibm.bluej.consistency.term.corefunc.FuncIsSubstring;
import com.ibm.bluej.consistency.term.corefunc.FuncLess;
import com.ibm.bluej.consistency.term.corefunc.FuncLessEquals;
import com.ibm.bluej.consistency.term.corefunc.FuncLog;
import com.ibm.bluej.consistency.term.corefunc.FuncLogistic;
import com.ibm.bluej.consistency.term.corefunc.FuncMax;
import com.ibm.bluej.consistency.term.corefunc.FuncMemoized;
import com.ibm.bluej.consistency.term.corefunc.FuncMin;
import com.ibm.bluej.consistency.term.corefunc.FuncMinus;
import com.ibm.bluej.consistency.term.corefunc.FuncMult;
import com.ibm.bluej.consistency.term.corefunc.FuncNonMember;
import com.ibm.bluej.consistency.term.corefunc.FuncNotEqual;
import com.ibm.bluej.consistency.term.corefunc.FuncPlus;
import com.ibm.bluej.consistency.term.corefunc.FuncSetFromBag;
import com.ibm.bluej.consistency.term.corefunc.FuncSign;
import com.ibm.bluej.consistency.term.corefunc.FuncSize;
import com.ibm.bluej.consistency.term.corefunc.FuncSparseVectorSum;
import com.ibm.bluej.consistency.term.corefunc.FuncSum;
import com.ibm.bluej.consistency.term.corefunc.FuncThreshold;
import com.ibm.bluej.consistency.term.corefunc.FuncToSparseVector;
import com.ibm.bluej.consistency.term.corefunc.FuncUnion;
import com.ibm.bluej.consistency.term.corefunc.FuncVectorSubtract;
import com.ibm.bluej.consistency.term.corefunc.FuncVectorSum;
import com.ibm.bluej.util.common.Lang;

public class ParserBasic {

	
	static final String[] OPERATORS = new String[] {"+", "-", "*", "/", "<", ">", ">=", "<=", "=", "!="};
	static {
		Arrays.sort(OPERATORS);
	}
	
	public static Map<String, Class> predeclaredFunctions() {
		//TODO: check that each function gets the right number of args
		 Map<String, Class> funNameToClass = new HashMap<String, Class>();
		 funNameToClass.put("<", FuncLess.class);
		 funNameToClass.put("<=", FuncLessEquals.class);
		 funNameToClass.put("=", FuncEquals.class);
		 funNameToClass.put("!=", FuncNotEqual.class);
		 funNameToClass.put("+", FuncPlus.class);
		 funNameToClass.put("-", FuncMinus.class);
		 funNameToClass.put("*", FuncMult.class);
		 funNameToClass.put("/", FuncDiv.class);
		 funNameToClass.put("inside", ExInside.class);
		 funNameToClass.put("outside", ExOutside.class);
		 funNameToClass.put("size", FuncSize.class);
		 funNameToClass.put("abs", FuncAbs.class);
		 funNameToClass.put("union", FuncUnion.class);
		 funNameToClass.put("setFromBag", FuncSetFromBag.class); //NOTE: set from bag won't fix the fact that sets can't contain NONCONSTS
		 funNameToClass.put("intersection", FuncIntersection.class);
		 funNameToClass.put("nonmember", FuncNonMember.class);
		 funNameToClass.put("member", ExMember.class);
		 funNameToClass.put("min", FuncMin.class);
		 funNameToClass.put("max", FuncMax.class);
		 funNameToClass.put("succ", ExSucc.class);
		 funNameToClass.put("cosine", FuncCosine.class);
		 funNameToClass.put("sum", FuncSum.class);
		 funNameToClass.put("average", FuncAverage.class);
		 funNameToClass.put("sparseVectorSum", FuncSparseVectorSum.class);
		 funNameToClass.put("vectorSum", FuncVectorSum.class);
		 funNameToClass.put("vectorSubtract", FuncVectorSubtract.class);
		 funNameToClass.put("gensym", ExGensym.class);
		 funNameToClass.put("isSubstring", FuncIsSubstring.class);
		 funNameToClass.put("sign", FuncSign.class);
		 funNameToClass.put("exp", FuncExp.class);
		 funNameToClass.put("log", FuncLog.class);
		 funNameToClass.put("toSparseVector", FuncToSparseVector.class);
		 funNameToClass.put("dotProduct", FuncDotProduct.class);
		 funNameToClass.put("concat", FuncConcat.class);
		 funNameToClass.put("memoized", FuncMemoized.class);
		 funNameToClass.put("ident", FuncIdentity.class);
		 funNameToClass.put("logistic", FuncLogistic.class);
		 funNameToClass.put("threshold", FuncThreshold.class);
		 return funNameToClass;
	}
	
	public static ATerm parseFunction(String func, Map<String,Class> funNameToClass) {
		return parseFunction(func.replaceAll("\\s+", " "), funNameToClass, new HashMap<String,ATerm>());
	}
	
	private static ATerm parseFunction(String func, Map<String,Class> funNameToClass, Map<String,ATerm> preBuilt) {
		ATerm pb = preBuilt.get(func);
		if (pb != null) {
			return pb;
		}
		if (funNameToClass == null) {
			funNameToClass = predeclaredFunctions();
		}
		int firstParen = func.indexOf('(');
		if (firstParen == -1) {
			//base case
			//CONSIDER: handle P_x right here?
			ATerm vt = ValueTerm.basicFromString(func);
			preBuilt.put(func, vt);
			return vt;
		}
		ArrayList<String> topParts = new ArrayList<String>();
		if (firstParen != 0) {
			topParts.add(func.substring(0, firstParen));
		}
		int parenDepth = 1;
		int prevPosition = firstParen + 1;
		while (func.charAt(prevPosition) == ' ' || func.charAt(prevPosition) == ',')
			++prevPosition;
		for (int i = prevPosition; i < func.length(); ++i) {
			if (func.charAt(i) == '(')
				++parenDepth;
			else if (func.charAt(i) == ')')
				--parenDepth;
			if (parenDepth == 1 && (func.charAt(i) == ' ' || func.charAt(i) == ',') ||
			    parenDepth == 0) 
			{
				int nextPosition = i;
				topParts.add(func.substring(prevPosition, nextPosition));
				prevPosition = nextPosition+1;
				while (prevPosition < func.length() && (func.charAt(prevPosition) == ' ' || func.charAt(prevPosition) == ','))
					++prevPosition;
				i = prevPosition-1;
			}
		}
		if (parenDepth != 0) {
			if (func.length() > 200) {
				throw new RuntimeException("Unmatched parens: very long function starting with: "+func.substring(0, 200));
			}
			throw new RuntimeException("Unmatched parens:\n"+prettyString(func));
		}
		String predStr = null;
		if (topParts.size() == 3 && Arrays.binarySearch(OPERATORS, topParts.get(1)) >= 0) {
			predStr = topParts.remove(1);
		} else {
			predStr = topParts.remove(0);
		}
		ATerm[] parts = new ATerm[topParts.size()];
		for (int i = 0; i < topParts.size(); ++i) {
			parts[i] = parseFunction(topParts.get(i), funNameToClass, preBuilt);
		}
		if (parts.length == 0) {
			System.err.println("No arg function: "+func);
		}
		Function f = makeFunction(predStr, funNameToClass, parts);
		preBuilt.put(func, f);
		return f;
	}	
	
	static Function makeFunction(String predStr, Map<String,Class> funNameToClass, ATerm[] args) {
		if (predStr.startsWith("\\")) {
			predStr = predStr.substring(1);
		}
		Class c = funNameToClass.get(predStr);
		if (c != null) {
			try {
				Function f = (Function)c.newInstance();
				f.parts = args;
				
				return f;
			} catch (Exception e) {
				throw new Error(e);
			}
		} else {
			System.err.println("All declared:");
			for (Map.Entry<String, Class> e : funNameToClass.entrySet()) {
				System.err.println(e.getKey()+": "+e.getValue().getName());
			}
			throw new Error("Function not declared: "+predStr);
		}		
	}
	
	public static String prettyString(String function) {
		StringBuffer pretty = new StringBuffer();
		int depth = 0;
		for (int i = 0; i < function.length(); ++i) {
			if (function.charAt(i) == '(') {
				++depth;
				pretty.append("\n"+Lang.LPAD("(", depth*2+1));
			} else if (function.charAt(i) == ')') {
				--depth;
				pretty.append(')');
			} else {
				pretty.append(function.charAt(i));
			}
		}
		return pretty.toString();
	}
	
	protected ParserBasic(CRFState crfState) {
		this.funNameToClass = predeclaredFunctions();
		this.crfState = crfState;
	}
	
	protected CRFState crfState;
	protected FocusIndicatorState focusIndicators = new FocusIndicatorState();
	protected Map<String, Class> funNameToClass;
	protected Map<String, StringTerm> constants = new HashMap<String, StringTerm>();
	protected int maxVarCount = 0;
	protected StringTerm getStrConst(String s) {
		if (s.startsWith("\"")) {
			s = s.substring(1, s.length()-1);
		}
		StringTerm sc = constants.get(s);
		if (sc == null) {
			sc = StringTerm.canonical(s);
			constants.put(s, sc);
		}
		return sc;
	}
	protected Map<String, VarTerm> curVarId = new HashMap<String, VarTerm>();
	protected void clearVars() {
		if (curVarId.size() > maxVarCount) {
			maxVarCount = curVarId.size();
		}
		curVarId.clear();
	}
	
	protected VarTerm getVarId(String name) {
		if (name.equals("_")) {
			VarTerm vi = new VarTerm(name, curVarId.size());
			curVarId.put(name+curVarId.size(), vi);
			return vi;
		}
		VarTerm vi = curVarId.get(name);
		if (vi == null) {
			vi = new VarTerm(name, curVarId.size());
			curVarId.put(name, vi);
		}
		return vi;
	}
	
	protected static boolean[] toBoolArray(Collection<Boolean> positive) {
		boolean[] pos = new boolean[positive.size()];
		int ndx = 0;
		for (Boolean p : positive) {
			pos[ndx++] = p;
		}
		return pos;
	}	
	
}
