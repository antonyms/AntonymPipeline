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

package com.ibm.bluej.consistency;

import java.util.ArrayList;
import java.util.Random;

import com.ibm.bluej.consistency.focus.FocusIndicatorState;
import com.ibm.bluej.consistency.inference.IMarginal;
import com.ibm.bluej.consistency.learning.LearningState;
import com.ibm.bluej.consistency.learning.TrainingAnalysis;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;



public class SGSearch {
	/*
	 Major todos: 
	   Thread safe version, strip the statics
	   DCL
	     VariationRank and training with hidden structure using data likelihood
	     multi-level learning
	   Metropolis-Independent/Joint (Metropolis-Roulette)

	   Multi-level learning
	     finish com.ibm.bluej.consistency.learning.multilevel
	     test thoroughly

	   VariationRank
	     extend VariationMAP to deal with multiple acceptable completions
	     extend to allow multiple updates on a single checkBest, multiple world types at once
	     test on HMMTest
	       VariationRank at the type level
	       it is allowed to change word/type assignments and weights
	       a nearest neighbor / alignment strategy for SUI->type (logistic)
	         needs to be generative though
	       the idea was that you would actually generate variations...
	       generate variations from different CUIs
	       CUI/WordNet variation rank?	   	   

	   MetropolisIndependentJoint - measure convergence and acceptance rate
		I_{LINK,link,type1,word} :
		J_{LINKJ,link,type1,type2} :
		MIJPROPOSE> hasCUI(esgId, cui) ^ hasType(esgId,type) 
		  VALUES> possibleCUI(esgId, cui) ^ cuiType(cui,type) 
		  VARIABLE> hasAPossibleCUI(esgId)
		But during learning the wheels will change
		during learning the weights must be known too, so that SampleRank knows what ordering is predicted
		mark the values as dirty, they can then be recomputed
		The final part of the proposal must contain only evidence
		the points on the roulette are drawn from all bindings over the middle - must also be evidence
		each roulette wheel keeps a link to the last proposal
		MIJ proposals have no value unless all proposals are MIJ
    
     Minor todos:
       sgs.grammar: support separating lines by ; rather than \n
       rename Term, AbsTerm
       more complete GroundFindsFormula indexing
       make clean distinction between train and test - Proposals divided into train only, marginal only, MAP only
        special inTraining() predicate?
       test zero arg functions
       ask Marshall about his advanced HashMap - and profile impact
       try Guava goodFastHash
       handle anonymous vars better?, VarTerm.id = -1, don't add to binds?
       consider SampleRank regularization!
       InteractiveControl
         examine proposal data
         add a function eval. IC
         add examine weights and examine average weights IC
         IC:showMAP (and kmap)
       more robust Declarative Proposals
         note that order definitely matters in proposals right now
       make an easier coding style for expand terms
         collection expand, map expand
       need some kind of warning if we are missing an index on a formula?
       add defines based on evidence as evidence
         and don't bother inserting into define table
         (it must be the case that EVERY term in the condition is evidence)  
	   is it safe to == the predicate and type names?
	     track arity/predicate/position where equals does not imply ==
	   create variation analysis class
	     show world with some of the map results - and IWorldWeight
	     show the differences between the analyses
     
     Testing
       test parallel SampleRank
         ParamWeight.merge
         examine differences between the averages and the splits
       test flexible MAP
         try multiple times, see if results are same? 
         try with mst.con
	 */
	
	//don't worry about too much static stuff right now
	//ALL STATIC:
	//  SGSearch, DefineCreator, ParamWeight, SGWeight
	//    No harm in each DefineCreator holding a reference to a particular define table
	//    No harm in each Proposal method holding a reference to it's AvgParameters
	//       Proposal can also be responsible for tracking bestWeight and bestTerms
	//    No harm in each ParamCreator holding a reference to a particular parameter map
	//    However:
	//      everything that needs access to SGSearch's static stuff holding a reference to particular things seems hard
	
	//TODO: refactor, this class does too much
	//  separate search, indexes, weights
	
	private static CRFState crfState = new CRFState();
	public static CRFState getCRFState() {
		return crfState;
	}
	
	public static LearningState getLearningState() { 
		return crfState.learningState;
	}
	public static void showDefineTable() {
		crfState.defineState.showDefineTable();
	}
	
	//static FunctionMemoryLeak functionMemoryLeak = new FunctionMemoryLeak();

	public static FocusIndicatorState getFocusIndicators() {
		return crfState.desc.focusIndicators;
	}
	
	public static CRFDescription getCRFDescription() { 
		return crfState.desc;
	}
	public static void loadDescription(CRFDescription crf) {
		crfState.clear();
		crfState.desc = null;
		crfState.loadDescription(crf);
	}
	
	public static void loadDescription(String fileContents) {
		loadDescription(ParserAntrl.parse(crfState, fileContents));
	}

	public static void loadParamters(String parameterContents) {
		String[] lines = parameterContents.split("\n");
		crfState.learningState.load(lines);
		crfState.posGrnd.loadConfig(lines);
	}
	public static String stringParameters() {
		return crfState.learningState.allToString() + "\n" + crfState.posGrnd.configToString() + "\n";
	}
	
	public static void newProblem() {
		crfState.newProblem();
	}
	

	public static double getWeight() {
		//Function.flush();
		return crfState.getWeight();
	}
	public static String weightString() {
		return crfState.getWorldWeight().toString();
	}
	
	/*
	 * @deprecated use SGSearch.train or SGSearch.beginDelta and SGSearch.endDelta
	 * @param objScore
	 * @return
	public static boolean nextDelta(double objScore) {
		objectiveScore = objScore;
		boolean update = weight.endDelta(objectiveScore, prevObjectiveScore);
		weight.beginDelta();
		prevObjectiveScore = objectiveScore;
		return update;
	}
	*/
	public static void beginDelta() {
		crfState.beginDelta();
	}
	public static boolean endDelta() {
		return crfState.endDelta();
	}
	
	public static void setParametersToAverage() {
		crfState.learningState.setParametersToAverage();
	}
	public static void setMarginal(IMarginal marginal) {
		crfState.transaction.setMarginal(marginal, crfState.posGrnd);
	}
	public static void setMAP(MaximumAPost map) {
		crfState.transaction.setMAP(map);
	}
	
	
	public static Random rand = new Random();
	
	public static boolean FAST_WEIGHTS = false;
	
	public static TrainingAnalysis train(int burnIn, int proposalCount, double minAccept, FunST<ScanTerm,Boolean> trackImprovementIn) {	
		return crfState.train(burnIn, proposalCount, minAccept, trackImprovementIn);
	}
	
	public static void marginalInference(int burnIn, int proposalCount, IMarginal marginal) {
		crfState.marginalInference(burnIn, proposalCount, marginal);
	}
	public static void mapInference(MaximumAPost map, int proposalCount, double epsilon) {
		mapInference(map, proposalCount, epsilon, 0);
	}
	public static void mapInference(MaximumAPost map, int proposalCount, double epsilon, int reportEvery) {
		crfState.mapInference(map, proposalCount, epsilon, reportEvery);
	}
	
	/** Sets the ScanTerm to be true
	 * 
	 * @param t			the ScanTerm that will become true, it should be false currently
	 */
	public static double nowTrue(ScanTerm t) {
		return crfState.nowTrue(t);
	}
	public static void nowTrue(Iterable<ScanTerm> ct) {
		for (ScanTerm t : ct) {
			crfState.nowTrue(t);
		}
	}
	
	public static boolean isTrue(ScanTerm t) {
		return crfState.posGrnd.contains(t) != null;
	}
	
	public static void evidence(ATerm... terms) {
		crfState.evidence(new ScanTerm(terms));
	}
	public static void evidence(ScanTerm t) {
		crfState.evidence(t);
	}
	
	public static double nowFalse(ScanTerm t) {
		return crfState.nowFalse(t);
	}
	public static void nowFalse(Iterable<ScanTerm> ct) {
		for (ScanTerm t : ct) {
			nowFalse(t);
		}
	}
	
	/*
	public static double flip(ScanTerm t) {
		if (posGrnd.contains(t) != null) {
			return nowFalse(t);
		} else {
			return nowTrue(t);
		}
	}
	*/

	public static ScanTerm randomMatch(ATerm... probe) {
		return crfState.posGrnd.randomMatch(probe);
	}
	
	public static ArrayList<ScanTerm> allMatch(ATerm... probe) {
		return crfState.allMatch(probe);
	}
	
	/**
	 * @deprecated
	 * @param t
	 * @return
	 */
	public static double getImmediateWeight(ScanTerm t) {
		IndexEntry e = crfState.posGrnd.contains(t);
		if (e == null) return 0;
		return e.getImmediateWeight();
	}
	 
	
	//TODO: this should be a setMode type thing
	public static void setTransaction(boolean enable) {
		crfState.transaction.setEnabled(enable);
		//CONSIDER: this is called at the end of initialization right?
		//  so this would be a good time to call either checkBest if MAP
		//  or to initialize the marginal tracking with a bunch of nowTrue
	}
	//CONSIDER: don't expose commit and rollback, just give a choice of acceptance functions
	public static void commit() {
		crfState.transaction.commit();
	}
	public static void rollback() {
		crfState.transaction.rollback(crfState);
	}
	public static boolean metroHastings() {
		return crfState.transaction.metroHastings(crfState);
	}
	public static boolean metroHastings(double forwardToBackRatio) {
		return crfState.transaction.metroHastings(forwardToBackRatio, crfState);
	}
	public static boolean metroHastingsMinAccept(double forwardToBackRatio, double minAcceptProb) {
		return crfState.transaction.metroHastingsMinAccept(forwardToBackRatio, minAcceptProb, crfState);
	}
	public static boolean epsilonSoft(double epsilon) {
		return crfState.transaction.epsilonSoft(epsilon, crfState);
	}
	
	
	
	

	/*
	//really shouldn't be public
	public static FormulaFindGroundRand posGrnd = new FormulaFindGroundAdaptive();
	public static CheckNegatedGround negGrnd = new CheckNegatedGround();
	
	public static GroundFindFormulaMap formFinder = new GroundFindFormulaMap();
	
	//If the factor graph isn't learning, you should set this to FixedDeltaWeight()
	static IDeltaWeight weight = new SingleDelta(); // new DeltaList(0); new FixedDeltaWeight(); 
	//simple transaction, just track the ScanTerms in a stack
	private static SimpleTransaction transaction = new SimpleTransaction();
	
	//these just pass straight to GroundFindFormula
	//  the index will have to do the work of grounding new things
	//NOTE: for collection formulas (also used to add initial formulas)
	public static void addFormula(Formula f) {
		formFinder.add(f);
	}
	public static void removeFormula(Formula f) {
		formFinder.remove(f);
	}
	
	public static void addWeight(double v) {
		weight.addWeight(v);
	}
	public static void addWeight(Learnable p, double v) {
		weight.addWeight(p,v);
	}
	*/
	
	//private static IProposalHelper proposalHelper;
	/*
	 * @deprecated
	 * @param t
	 
	public static void addMotivated(ScanTerm t) {
		//this is to help the proposal distribution
		//would it also help to keep a queue of recently touched motivated?
		if (proposalHelper != null) {
			proposalHelper.alertMotivated(t);
		}
	}
	*/

	public static void setMaxGroundOutTime(long millis) {
		crfState.groundingLimits.MAX_GROUND_OUT_TIME = millis;
	}
	
	
	public static class ExcessiveGroundOut extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public ExcessiveGroundOut(String message) {
			super(message);
		}
	}
	
	public static class AlreadyTrueException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public AlreadyTrueException(String message) {
			super(message);
		}
	}
	
	
	
	public static void printTruth() {
		crfState.printTruth();
	}
}
