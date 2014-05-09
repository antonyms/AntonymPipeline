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

package com.ibm.bluej.consistency.optimize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.consistency.CRFDescription;
import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.FunctionMaker;
import com.ibm.bluej.consistency.ParserBasic;
import com.ibm.bluej.consistency.ProposalFunction;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.formula.FactorFunctionCreator;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.interactive.InteractiveControl;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.corefunc.FuncIdentity;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.HashMapUtil;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PeriodicChecker;
import com.ibm.bluej.util.common.Warnings;

public class Optimizer {
	//TODO: test after converting from static
	
	public static final String PARAM_PREFIX = "P_";
	
	private static boolean GREEDY = false;
	private static final boolean METRO_ACCEPT = true;
	private static final double EPSILON = 0.1;
	private static final int SAMPLE_SIZE = 100;
	
	private static final ScanTerm DUMMY_TRUE = new ScanTerm(new StringTerm("dummy"));
	
	private CRFState state;
	private HashMap<String,ProposalFunction> proposals;
	
	//public static void main(String[] args) {
	//	System.out.println(SGSParser.parseFunction(FileUtil.readFileAsString("/users/glassm/justify/logpre/oneFormula.txt"), null));
	//}
	
	//TODO: somehow get a inform-analysis on the various features
	
	private static Map<String,Class> getFunNameToClass() {
		Map<String,Class> funNameToClass = ParserBasic.predeclaredFunctions();
		funNameToClass.put("logProb", FuncLogProbScore.class);
		funNameToClass.put("rankScore", FuncRank.class);
		funNameToClass.put("oddsScore", FuncOddsScore.class);
		ArrayList<Pair<String,Class>> toAdd = new ArrayList<Pair<String,Class>>();
		for (Class c : funNameToClass.values()) {
			toAdd.add(Pair.make(c.getSimpleName(), c));
		}
		for (Pair<String,Class> p : toAdd) {
			funNameToClass.put(p.first, p.second);
		}
		return funNameToClass;
	}
	
	public static double getValue(String stringFormula, Map<String,Double> paramValues) {
		return getValue(ParserBasic.parseFunction(stringFormula, getFunNameToClass()), paramValues);
	}
	
	public static double getValue(ATerm paramFormula, Map<String,Double> paramValues) {	
		if (paramFormula == null) {
			return Double.NaN;
		}
		Map<String, ArrayList<Pair<CompositeTerm,Integer>>> occurrences = gatherProposalFunctionPlaceholders(paramFormula);
		for (Map.Entry<String, ArrayList<Pair<CompositeTerm,Integer>>> e : occurrences.entrySet()) {
			Double pv = paramValues.get(e.getKey());
			if (pv == null) {
				Warnings.limitWarn("MISSING_OPT_PARAM_VALUE", 10, "mrglasslogwarn: No value for parameter: "+e.getKey());
				pv = 0.0;
			}
			NumberTerm repWith = new NumberTerm(pv);
			for (Pair<CompositeTerm,Integer> p : e.getValue()) {
				if (p.first == null) {
					paramFormula = repWith;
				} else {
					p.first.parts[p.second] = repWith;
				}
			}
		}	
		paramFormula = paramFormula.ground(new Binds(null), null);
		//restore the formula to original state
		for (Map.Entry<String, ArrayList<Pair<CompositeTerm,Integer>>> e : occurrences.entrySet()) {
			ATerm repWith = new StringTerm(e.getKey());
			for (Pair<CompositeTerm,Integer> p : e.getValue()) {
				if (p.first != null) {
					p.first.parts[p.second] = repWith;
				}
			}
		}
		return ATerm.getDouble(paramFormula);
	}
	
	public HashMap<String,ProposalFunction> getParameters() {
		return proposals;
	}
	public CRFState getState() {
		return state;
	}
	
	public void optimize(int scanCount, FunST<String,Boolean> isInteresting) {
		optimize(scanCount, new L1Regularizer(1), isInteresting);
	}
	public void optimize(int scanCount, Regularizer regularize, FunST<String,Boolean> isInteresting) {
		System.out.println("Beginning optimization. Score = "+state.getWeight());
		ArrayList<ProposalFunction> pfs = new ArrayList<ProposalFunction>(proposals.values());

		double regWeight = 0;
		ProposalFunctionMAP map = new ProposalFunctionMAP(pfs, state.getWeight());
		PeriodicChecker report = new PeriodicChecker(100, true);
		int nonGreedyAccepts = 0;
		for (int c = 0; c < scanCount && InteractiveControl.checkContinue(); ++c) {
			if (report.isTime()) {
				//TODO: proposals per second, improvement per second
				System.out.println("On proposal "+c);
				System.out.println("Weight = "+Lang.dblStr(state.getWeight())+" + "+Lang.dblStr(regWeight)+"="+Lang.dblStr(state.getWeight()+regWeight)+
						", best so far = "+Lang.dblStr(map.bestWeight)+" + "+Lang.dblStr(map.bestRegularization)+"="+Lang.dblStr(map.bestRegularization+map.bestWeight));
				//TODO: remove static stuff in FuncLogProbScore
				if (FuncLogProbScore.allFunctions != null) {
					Pair<Double,Double> summary = FuncLogProbScore.getSummaryScores();
					System.out.println("Max F1 = "+Lang.dblStr(summary.first)+" AUC = "+Lang.dblStr(summary.second));
					double avgLogLike = map.bestWeight/FuncLogProbScore.allFunctions.size();
					System.out.println("Best avg. log-likelihood = "+Lang.dblStr(avgLogLike)+" avg. probability = "+Math.exp(avgLogLike));
				}
				int nonzero = 0;
				for (ProposalFunctionMAP.PFWithBest pfb : map.allProposals) {
					if (pfb.pf.value.value != 0) ++nonzero;
					if ((isInteresting == null && pfb.pf.value.value != 0) || (isInteresting != null && isInteresting.f(pfb.pf.getName()))) {
						System.out.println(pfb.pf +" with best value "+Lang.dblStr(pfb.bestValue));
					}
				}
				System.out.println(nonzero+" non-zero params out of "+map.allProposals.size());
			}
			
			if (c % 10000 == 0) {
				GREEDY = !GREEDY;
			}
			
			//TODO: periodic full weight recompute? in case there is numeric drift
			
			//don't wander too far from best known
			//CONSIDER: base this on how long since the last new best?
			if (!GREEDY && c % 1000 == 0) {				
				regWeight = map.loadBest();
			}
			
			double prevScore = state.getWeight() + regWeight;
			ProposalFunction pf = pfs.get(SGSearch.rand.nextInt(pfs.size()));
			ProposalFunction opf = null;
			double prevValue = pf.value.value;
			double opfPrevValue = 0;
			double delta = 0;
			int operation = SGSearch.rand.nextInt(10);
			if (operation < 2) {
				delta = pf.scanMAP(state, SGSearch.rand, -5, 5, 10, SAMPLE_SIZE, regularize);
			} else if (operation < 4 && pfs.size() > 1) {
				do {
					opf = pfs.get(SGSearch.rand.nextInt(pfs.size()));
				} while (pf == opf);
				opfPrevValue = opf.value.value;
				delta = ProposalFunction.jointHillclimb(state, pf, opf, SAMPLE_SIZE, regularize);
			} else if (operation < 9) {
				delta = pf.hillClimb(state, SGSearch.rand, SAMPLE_SIZE, regularize);
			} else {
				//"try zero" option when using L1 regularization
				delta = pf.tryZero(state, SGSearch.rand, SAMPLE_SIZE, regularize);
			}		
			if (delta < 0) {
				if (GREEDY) {
					pf.setValue(prevValue); if (opf != null) opf.setValue(opfPrevValue);
				} else if (METRO_ACCEPT && Math.random() > Math.exp((prevScore-map.getBestScore()) + delta)) { //become more and more greedy as you wander from the best known
					pf.setValue(prevValue); if (opf != null) opf.setValue(opfPrevValue);
				} else if (Math.random() > EPSILON) {
					pf.setValue(prevValue); if (opf != null) opf.setValue(opfPrevValue);
				}
			}
			if (pf.value.value != prevValue)
				regWeight += regularize.regularizeDelta(prevValue, pf.value.value);
			if (opf != null && opf.value.value != opfPrevValue) {
				regWeight += regularize.regularizeDelta(opfPrevValue, opf.value.value);
			}
			
			map.checkBest(state.getWeight(), regWeight);
		}
		System.out.println("Finished optimization, loading best. Score = "+map.getBestScore());
		map.loadBest();
	}
	

	

	
	public Optimizer(String trainContent) {
		this(trainContent, 0);
	}
	public Optimizer(String trainContent, int minOccurrenceCount) {
		
		CRFDescription cd = new CRFDescription();
		cd.funNameToClass = getFunNameToClass();
		proposals = new HashMap<String, ProposalFunction>();
		cd.formulas = new ArrayList<Formula>();
		cd.maxVarCount = 0;
		cd.objectives = new ArrayList<Formula>();
		cd.constants = new HashMap<String, StringTerm>();
		
		state = new CRFState();
		state.groundingLimits.MAX_GROUND_OUT_TIME = 1200000;
		
		FunctionMaker funMaker = new FunctionMaker(cd.funNameToClass);
		for (String line : trainContent.split("\n")) {
			if (line.length() == 0 || line.startsWith("//")) {
				continue;
			}
			if (line.startsWith("DECLARE>")) {
				int beginClassName = line.indexOf('[')+1;
				int endClassName = line.indexOf(']');
				String funcName = line.substring(line.indexOf('>')+1, line.indexOf(':')).trim();
				try {
				Class funcClass = Class.forName(line.substring(beginClassName, endClassName).trim());
				cd.funNameToClass.put(funcName, funcClass);
				} catch (Exception e) {
					throw new Error(e);
				}
			} else {
				try {
					cd.formulas.add(makeFormula(state, line, funMaker, cd.funNameToClass, proposals));
				} catch (RuntimeException e) {
					System.err.println("\nLast part: "+line.substring(Math.max(0, line.length()-100)));
					throw e;
				}
			}
		}
		
		state.loadDescription(cd);
		//evidence the dummy ScanTerm
		state.evidence(DUMMY_TRUE); 
		
		//FIXME: this only works when optimizing with FuncLogProbScore
		setBiases(proposals.values());
		
		if (minOccurrenceCount > 1) {
			pruneByOccurrenceCount(proposals, minOccurrenceCount);
		}
	}
	
	private static void setBiases(Collection<ProposalFunction> proposals) {
		for (ProposalFunction pf : proposals) {
			if (pf.getName().endsWith("_BIAS")) {
				double prob = pf.percentPositiveWhereOccurrs();
				if (prob < 0.01) prob = 0.01;
				if (prob > 0.99) prob = 0.99;
				pf.setValue(Math.log(prob/(1-prob)));
			}
		}
	}
	
	private static void pruneByOccurrenceCount(Map<String,ProposalFunction> proposals, int minCount) {
		ArrayList<String> toRemove = new ArrayList<String>();
		for (ProposalFunction pf : proposals.values()) {
			if (pf.getUsedBy().size() < minCount) {
				toRemove.add(pf.getName());
			}
		}
		for (String rm : toRemove) {
			proposals.remove(rm);
		}
	}
	
	private static Formula makeFormula(CRFState state, String formula, FunctionMaker funMaker, Map<String,Class> funNameToClass, HashMap<String, ProposalFunction> proposals) {
		ATerm t = null;
		if (formula.indexOf('\t') != -1) {
			//format: feature formulas separated by spaces and then \t and the score
			String[] parts = formula.split("\t");
			t = ParserBasic.parseFunction("(+ "+parts[1]+" "+PARAM_PREFIX+"BIAS)", funNameToClass);		
			t = funMaker.makeFunction("\\logProb", new StringTerm(parts[0]), t, NumberTerm.canonical(Double.parseDouble(parts[2])));
		} else {
			t = ParserBasic.parseFunction(formula, funNameToClass);
		}
		fixProposalFunctions(t, proposals);
		return new Formula(
				new FactorFunctionCreator((ParamWeight)null, t, state), 
				new ATerm[] {DUMMY_TRUE}, new boolean[] {true}, null, state);
	}
	
	private static void fixProposalFunctions(ATerm func, HashMap<String, ProposalFunction> proposals) {
		Map<String, ArrayList<Pair<CompositeTerm,Integer>>> occurrences = new HashMap<String, ArrayList<Pair<CompositeTerm,Integer>>>();
		gatherProposalFunctionPlaceholders(func, null, -1, occurrences);
		for (Map.Entry<String, ArrayList<Pair<CompositeTerm,Integer>>> e : occurrences.entrySet()) {
			ProposalFunction pf = proposals.get(e.getKey());
			if (pf == null) {
				pf = new ProposalFunction(e.getKey(), 0.0);
				proposals.put(e.getKey(), pf);
			}
		    //instanceGaurd everything
			ATerm replace = pf;
			if (e.getValue().size() > 1) {
				replace = new FuncIdentity(pf);
			}
			for (Pair<CompositeTerm, Integer> p : e.getValue()) {
				p.first.parts[p.second] = replace;
			}
		}		
	}
	
	public static Map<String, ArrayList<Pair<CompositeTerm,Integer>>> gatherProposalFunctionPlaceholders(ATerm t) {
		Map<String, ArrayList<Pair<CompositeTerm,Integer>>> occurrences = new HashMap<String, ArrayList<Pair<CompositeTerm,Integer>>>();
		gatherProposalFunctionPlaceholders(t, null, -1, occurrences);
		return occurrences;
	}
	
	private static void gatherProposalFunctionPlaceholders(ATerm t, CompositeTerm parent, int position, Map<String, ArrayList<Pair<CompositeTerm,Integer>>> occurrences) {
		if (t instanceof StringTerm && ATerm.getString(t).startsWith(PARAM_PREFIX)) {
			HashMapUtil.addAL(occurrences, ATerm.getString(t), Pair.make(parent, position));
			return;
		}
		if (t instanceof CompositeTerm) {
			CompositeTerm c = (CompositeTerm)t;
			for (int i = 0; i < c.parts.length; ++i) {
				gatherProposalFunctionPlaceholders(c.parts[i], c, i, occurrences);
			}
		}
	}
	

	
	
	public static void writeGCC(String modelContent) {
		//get the modelContent into a giant function
		
		Map<String,Class> funNameToClass = getFunNameToClass();
		ArrayList<ATerm> toSum = new ArrayList<ATerm>();
		for (String line : modelContent.split("\n")) {
			if (line.length() == 0 || line.startsWith("//")) {
				continue;
			}
			if (line.startsWith("DECLARE>")) {
				int beginClassName = line.indexOf('[')+1;
				int endClassName = line.indexOf(']');
				String funcName = line.substring(line.indexOf('>')+1, line.indexOf(':')).trim();
				try {
				Class funcClass = Class.forName(line.substring(beginClassName, endClassName).trim());
				funNameToClass.put(funcName, funcClass);
				} catch (Exception e) {
					throw new Error(e);
				}
			} else {
				try {
					
					if (line.indexOf('\t') != -1) {
						//format: feature formulas separated by spaces and then \t and the score
						String[] parts = line.split("\t");
						line = "\\logProb((+ "+parts[0]+" P_BIAS), "+parts[1]+")";
					}
					ATerm t = ParserBasic.parseFunction(line, funNameToClass);
					toSum.add(t);
				} catch (RuntimeException e) {
					System.err.println("\nLast part: "+line.substring(Math.max(0, line.length()-100)));
					throw e;
				}
			}
		}
		Function f = new FunctionMaker().makeFunction("+", toSum);
		Map<String, ArrayList<Pair<CompositeTerm,Integer>>> occurrences = gatherProposalFunctionPlaceholders(f);
		ArrayList<String> proNames = new ArrayList<String>();
		for (Map.Entry<String, ArrayList<Pair<CompositeTerm,Integer>>> e : occurrences.entrySet()) {
			StringTerm repWith = new StringTerm("params["+proNames.size()+"]");
			proNames.add(e.getKey());
			for (Pair<CompositeTerm,Integer> p : e.getValue()) {
				p.first.parts[p.second] = repWith;
			}
		}
		
		// read in the optTemplate.c
		//  make appropriate replacements
		//  write the file
		Function.FOR_GCC = true;
		String template = FileUtil.readFileAsString("/users/glassm/justify/optTemplate.c");
		template = template.replace("/*${numParams}*/", ""+proNames.size());
		template = template.replace("/*${scoreFormula}*/", f.toString());
		FileUtil.writeFileAsString("/users/glassm/justify/optFilled.c", template);
	}
}
