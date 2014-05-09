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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.ibm.bluej.consistency.SGSearch.ExcessiveGroundOut;
import com.ibm.bluej.consistency.focus.FocusIndicatorState;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.RemovableFormula;
import com.ibm.bluej.consistency.inference.IMarginal;
import com.ibm.bluej.consistency.interactive.InteractiveControl;
import com.ibm.bluej.consistency.learning.DeltaOnly;
import com.ibm.bluej.consistency.learning.FixedDeltaWeight;
import com.ibm.bluej.consistency.learning.IDeltaWeight;
import com.ibm.bluej.consistency.learning.Learnable;
import com.ibm.bluej.consistency.learning.LearningState;
import com.ibm.bluej.consistency.learning.SingleDelta;
import com.ibm.bluej.consistency.learning.TrainingAnalysis;
import com.ibm.bluej.consistency.proposal.Proposal;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.PeriodicChecker;

public final class CRFState {
	//Just copy all methods from SGSearch into this
	//then add CRFState to Formula and as parameter to Creator.create

	
	public static CRFState getCRFState(String fileContents) {
		CRFState crfState = new CRFState();
		crfState.desc = ParserAntrl.parse(crfState, fileContents);
		crfState.loadDescription(crfState.desc);
		return crfState;
	}
	public CRFState() {}

	
	public void loadDescription(CRFDescription desc) {
		if (this.desc != null) {
			throw new Error("Description already loaded!");
		}
		this.desc = desc;
		this.quickBinds = new ATerm[Math.max(Binds.MIN_BIND_LEN, desc.maxVarCount+(desc.maxVarCount>>1))];
		formFinder = new GroundFindFormulaMap(this.quickBinds);
		for (Formula f : desc.formulas) {
			addFormula(f);
		}
	}
	
	CRFDescription desc;
	public CRFDescription getDescription() {
		return desc;
	}
	private ATerm[] quickBinds;
	public ATerm[] getQuicksave() {
		return quickBinds;
	}
	
	//Truth tables:
	public FormulaFindGroundRand posGrnd = new FormulaFindGroundAdaptive();
	public CheckNegatedGround negGrnd = new CheckNegatedGround();	
	DefineState defineState = new DefineState();
	//formula index
	public GroundFindFormulaMap formFinder;
	public void addFormula(Formula f) {
		formFinder.add(f);
	}
	public void removeFormula(Formula f) {
		formFinder.remove(f);
	}
	
	//simple transaction, just track the ScanTerms in a stack
	public SimpleTransaction transaction = new SimpleTransaction();
	
	//If the factor graph isn't learning, you should set this to FixedDeltaWeight()
	private IDeltaWeight weight = new SingleDelta(); // new DeltaList(0); new FixedDeltaWeight(); 
	
	private double objectiveScore = 0;
	private double prevObjectiveScore = 0;
	
	public LearningState learningState = new LearningState();

	
	
	public GroundingLimits groundingLimits = new GroundingLimits();
	FunctionMemoryLeak functionMemoryLeak = new FunctionMemoryLeak();
	
	public  FocusIndicatorState getFocusIndicators() {
		return desc.focusIndicators;
	}
	
	public void loadParamters(String parameterContents) {
		String[] lines = parameterContents.split("\n");
		learningState.load(lines);
		posGrnd.loadConfig(lines);
	}
	public String stringParameters() {
		return learningState.allToString() + "\n" + posGrnd.configToString() + "\n";
	}
	public double getWeight() {
		return weight.getWeight();
	}
	public IDeltaWeight getWorldWeight() {
		return weight;
	}
	public void addWeight(Learnable l, double w) {
		weight.addWeight(l,w);
	}
	public void addWeight(double w) {
		weight.addWeight(w);
	}
	public void beginDelta() {
		if (!desc.loadedObjectives) {
			for (Formula f : desc.objectives) {
				addFormula(f);
			}
			desc.loadedObjectives = true;
		}
		weight.beginDelta();
		prevObjectiveScore = objectiveScore;
	}
	public boolean endDelta() {
		if (SGDebug.END_DELTA && (objectiveScore !=0 || prevObjectiveScore != 0)) {
			System.out.println("From "+weight.deltaString()+" with "+prevObjectiveScore+" to "+objectiveScore);
		}
		return weight.endDelta(learningState, objectiveScore, prevObjectiveScore);
	}
	public void addObjective(double objective) {
		objectiveScore += objective;
	}
	public double getObjectiveScore() {
		return objectiveScore;
	}
	
	public TrainingAnalysis train(int burnIn, int proposalCount, double minAccept, FunST<ScanTerm,Boolean> trackImprovementIn) {	
		transaction.setEnabled(true);
		if (!desc.loadedObjectives) {
			for (Formula f : desc.objectives) {
				addFormula(f);
			}
			desc.loadedObjectives = true;
			if (SGDebug.TRAIN) {
				System.out.println("PRE TRAIN");
				System.out.println(desc);
				printTruth();
			}
		}
		
		TrainingAnalysis trainingAnalysis = null;
		if (trackImprovementIn != null) {
			trainingAnalysis = new TrainingAnalysis(this, trackImprovementIn, burnIn, proposalCount);
			//trainingAnalysis.setEvidence(posGrnd.getGroundTerms());
			trainingAnalysis.preTrain();
		}
		
		if (SGSearch.FAST_WEIGHTS) weight = new DeltaOnly();
		
		for (int fi = 0; fi < proposalCount && InteractiveControl.checkContinue(); ++fi) {

			if (fi >= burnIn) {
				beginDelta();//initial WorldWeight			
			}
			desc.proposals.propose();
			if (trainingAnalysis != null) {
				trainingAnalysis.trackObjectiveFunction(objectiveScore, proposalCount);
			}
			//do badDelta before accept - get stuck less this way
			//NOTE: still must be careful about weightedMax proposals
			if (fi >= burnIn && endDelta() && trainingAnalysis != null) {
				trainingAnalysis.deltaCount += 1;
				trainingAnalysis.recordDelta(transaction.changes, objectiveScore, prevObjectiveScore, weight);
			}
			if (minAccept >= 1) {
				transaction.commit();
				if (trainingAnalysis != null) trainingAnalysis.acceptCount += 1;
			} else {
				if (transaction.metroHastingsMinAccept(1.0, minAccept, this) && trainingAnalysis != null) {
					trainingAnalysis.acceptCount += 1;
				}
			}
		}
		
		learningState.recordTimestep(); //CONSIDER: too frequent?
		
		if (trainingAnalysis != null) {
			trainingAnalysis.postTrain();
		}
		//CONSIDER: track the changed parameters, hold a recentlyUpdated IdentitySet
		//setParametersToAverage(); - so slow, can be made faster by only updating the changed parameters
		
		if (SGDebug.TRAIN) {
			System.out.println("POST TRAIN");
			System.out.println(desc);
			printTruth();
		}
		
		return trainingAnalysis;
	}
	public void marginalInference(int burnIn, int proposalCount, IMarginal marginal) {
		transaction.setEnabled(true);
		if (SGSearch.FAST_WEIGHTS) weight = new FixedDeltaWeight();
		transaction.setMarginal(null, posGrnd);
		for (int fi = 0; fi < proposalCount && InteractiveControl.checkContinue(); ++fi) {
			if (fi == burnIn) {
				if (SGDebug.BURN_IN) {
					System.out.println("************BURN IN COMPLETE************");
				}		
				transaction.setMarginal(marginal, posGrnd);
			}
			desc.proposals.propose();
			transaction.metroHastings(this);
		}
		transaction.setMarginal(null, posGrnd);
	}
	public void mapInference(MaximumAPost map, int proposalCount, double epsilon) {
		mapInference(map, proposalCount, epsilon, 0);
	}
	public void mapInference(MaximumAPost map, int proposalCount, double epsilon, int reportEvery) {
		map.crfState = this;
		if (SGDebug.MAP_INFERENCE) {
			System.out.println("PRE MAP");
			System.out.println(desc.toString());
			printTruth();
		}
		transaction.setEnabled(true);
		if (SGSearch.FAST_WEIGHTS) weight = new FixedDeltaWeight(weight.getWeight()); 
		transaction.setMAP(map);
		//map.reallyCheckProb = 0.001;
		long time = System.currentTimeMillis();
		double weightV = weight.getWeight();

		PeriodicChecker report = null;
		if (reportEvery > 0) report = new PeriodicChecker(reportEvery);
		
		for (int fi = 0; fi < proposalCount && InteractiveControl.checkContinue(); ++fi) {
			double preWeight = weight.getWeight();
			int pndx = desc.proposals.propose();
			double newWeight = weight.getWeight();
			if (pndx >= 0 && transaction.epsilonSoft(epsilon, this)) 
				desc.proposals.accepted(pndx, newWeight-preWeight);

			//CONSIDER: if epsilon is zero and it's been too long since an accept, use metroHastings
			
			if (report != null && report.isTime()) {
				double elapsed = ((System.currentTimeMillis()-time)/1000.0);
				System.out.println(fi/elapsed+" proposals per second");
				if (map.getWorldWeight() != null) {
					System.out.println((map.getWorldWeight().getWeight()-weightV)/elapsed+" improvement per second");
					System.out.println(map.getWorldWeight().toString());
				}
				System.out.println(weight.toString());
				//report proposal details
				for (Proposal pro : desc.proposals.getProposals()) {
					System.out.println(pro);
				}
			}
		}
		transaction.setMAP(null);
		if (SGDebug.MAP_INFERENCE) {
			System.out.println("POST MAP");
			System.out.println(desc.toString());
			printTruth();
		}
	}
	
	public boolean isTrue(ScanTerm t) {
		return posGrnd.contains(t) != null;
	}
	public ArrayList<ScanTerm> allMatch(ATerm... probe) {
		ArrayList<ScanTerm> mterms = new ArrayList<ScanTerm>();
		
		int vndx = 0;
		for (int i = 0; i < probe.length; ++i) {
			if (probe[i] == null) {
				probe[i] = new VarTerm(null, vndx++);
			}
		}
		ScanTerm t = new ScanTerm(probe);
		Collection<IndexEntry> forFind = posGrnd.find(t, new Binds(quickBinds));
		for (IndexEntry e : forFind) {
			mterms.add(e.term);
		}
		return mterms;
	}
	public double nowTrue(ScanTerm t) {
		groundingLimits.clearGroundOutCount();
		//InteractiveControl.periodicCheck();
		functionMemoryLeak.functionNoLeak(this);
		transaction.addNowTrue(t,this);
		double prevWeight = weight.getWeight();
		try {
			nowTrueInternal(new IndexEntry(t));
		} catch(ExcessiveGroundOut e) {
			System.err.println("nowTrue: "+t);
			throw e;
		}
		//if (autoCheckBest)checkBest();
		return weight.getWeight() - prevWeight;
	}
	public double nowFalse(ScanTerm t) {
		groundingLimits.clearGroundOutCount();
		functionMemoryLeak.functionNoLeak(this);
		transaction.addNowFalse(t,this);
		double prevWeight = weight.getWeight();
		try {
			nowFalseInternal(new IndexEntry(t));
		} catch(ExcessiveGroundOut e) {
			System.err.println("nowFalse: "+t);
			throw e;
		}
		//if (autoCheckBest) checkBest();
		return weight.getWeight() - prevWeight;
	}
	public void evidence(ScanTerm t) {
		groundingLimits.clearGroundOutCount();
		try {
			nowTrueInternal(new EvidIndexEntry(t));
		} catch(ExcessiveGroundOut e) {
			System.err.println("evidence: "+t);
			throw e;
		}
	}
	public void newValue(ProposalFunction f, double value) {
		groundingLimits.clearGroundOutCount();
		transaction.newValue(f, value);
	}
	
	//NOTE: DefineCreate should call nowTrueInternal(IndexEntry) and nowFalseInternal(IndexEntry)
	//only the proposal dist should call the ScanTerm versions
	void nowTrueInternal(IndexEntry e) {
		if (SGDebug.BASIC || SGDebug.ALREADY_TRUTH_IS_ERROR) {
			SGLog.fine("TRUE: "+e.term);
			if (posGrnd.contains(e.term) != null) {
				if (SGDebug.ALREADY_TRUTH_IS_ERROR) {
					throw new Error("Called nowTrue on "+e.term+" but it was already true");
				}
				System.err.println("Called nowTrue on "+e.term+" but it was already true");
				return;
			}
		}
		
		try {
			
			posGrnd.add(e);
			transaction.inCaseInferenceCares(e.term, Boolean.FALSE);
			//we just make a distinction between terms created by definition and by proposal for debugging
			negGrnd.remove(e.term, (e instanceof DefIndexEntry)); 		
			formFinder.groundNew(true, e);	
		} catch(ExcessiveGroundOut ex) {
			System.err.println("nowTrueInternal: "+e.term);
			throw ex;
		} catch (ClassCastException cce) {
			System.err.println("nowTrue: "+e.term);
			throw cce;
		}
		if(SGDebug.TRACK_TRUTH) printTruth();
	}
	
	//TODO: this should take a ScanTerm, not IndexEntry
	//  pass the ScanTerm to groundNew, with DummyIndexEntry.negScan
	//  turn groundNew and groundOut into void
	void nowFalseInternal(IndexEntry e) {
		if (SGDebug.BASIC || SGDebug.ALREADY_TRUTH_IS_ERROR) {
			SGLog.fine("FALSE: "+e.term);
			if (posGrnd.contains(e.term) == null) {
				if (SGDebug.ALREADY_TRUTH_IS_ERROR) {
					throw new Error("Called nowFalse on "+e.term+" but it was already false");
				}
				System.err.println("Called nowFalse on "+e.term+" but it was already false");
				return;
			}
		}

		transaction.inCaseInferenceCares(e.term, Boolean.TRUE);
		try {
			//we just make a distinction between terms created by definition and by proposal for debugging
			posGrnd.remove(e.term, (e instanceof DefIndexEntry));
			if (formFinder.groundNew(false, e)) {
				negGrnd.add(e); 
			}
		} catch(ExcessiveGroundOut ex) {
			System.err.println("nowFalseInternal: "+e.term);
			throw ex;
		}
		if(SGDebug.TRACK_TRUTH) printTruth();
	}
	
	
	public void newProblem() {
		ArrayList<Formula> formulas = new ArrayList<Formula>();
		Iterator<Formula> itf = formFinder.getFormulas();
		while (itf.hasNext()) {
			Formula f = itf.next();
			if (f instanceof RemovableFormula) {
				continue;
			}
			formulas.add(f);
		}
		
		clear();
		
		for (Formula f : formulas) {
			addFormula(f);
		}		
	}
	
	void clear() {
		groundingLimits.clearGroundOutCount();
		negGrnd.clear();
		posGrnd.clear();
		if (formFinder != null) formFinder.clear();
		weight.clear();
		objectiveScore = 0;
		prevObjectiveScore = 0;
		transaction = new SimpleTransaction();
		functionMemoryLeak.clear();
		defineState.clear();
	}
	
	
	
	
	
	
	public void printTruth() {
		Iterator<IndexEntry> pit = posGrnd.getGroundTerms();
		StringBuffer buf = new StringBuffer();
		buf.append("TRUE:  ");
		ArrayList<String> posStr = new ArrayList<String>();
		while (pit.hasNext()) {
			posStr.add(pit.next().term.toString());
		}
		Collections.sort(posStr);
		buf.append(Lang.stringList(posStr, " "));
		//SGLog.info(buf.toString());
		System.out.println(buf.toString());
		buf.setLength(0);
		Iterator<IndexEntry> nit = negGrnd.getGroundTerms();
		buf.append("FALSE: ");
		ArrayList<String> negStr = new ArrayList<String>();
		while (nit.hasNext()) {
			negStr.add(nit.next().term.toString());
		}
		Collections.sort(negStr);
		buf.append(Lang.stringList(negStr, " "));
		//SGLog.info(buf.toString());
		System.out.println(buf.toString());
	}	
}
