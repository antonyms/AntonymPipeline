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

import com.ibm.bluej.consistency.inference.IMarginal;
import com.ibm.bluej.consistency.learning.TrainingAnalysis;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;

/**
 * Implements most important functions of Consistency
 * @author mrglass
 *
 */
public class CoreConsistency implements ISimpleConsistency {
	
	private CRFState state;
	
	public CoreConsistency() {
		state = new CRFState();
	}
	
	/**
	 * assert a ScanTerm(terms), if already true then fine
	 */
	public void reEvidence(ATerm... terms) {
		try {
			evidence(terms);
		} catch (SGSearch.AlreadyTrueException e) {}
	}
	
	public void loadDescription(CRFDescription crf) {
		state.clear();
		state.loadDescription(crf);
	}
	@Override
	public void loadDescription(String fileContents) {
		loadDescription(ParserAntrl.parse(state, fileContents));
	}
	
	

	@Override
	public void newProblem() {
		state.newProblem();
	}

	@Override
	public void loadParamters(String parameterContents) {
		state.loadParamters(parameterContents);
	}

	@Override
	public void evidence(ATerm... terms) {
		if (terms.length == 1 && terms[0] instanceof ScanTerm)
			state.evidence((ScanTerm)terms[0]);
		else
			state.evidence(new ScanTerm(terms));
	}

	@Override
	public void initialTrue(ATerm... terms) {
		if (terms.length == 1 && terms[0] instanceof ScanTerm)
			state.nowTrue((ScanTerm)terms[0]);
		else
			state.nowTrue(new ScanTerm(terms));
	}
	
	@Override
	public TrainingAnalysis train(int burnIn, int proposalCount,
			double minAccept, FunST<ScanTerm, Boolean> trackImprovementIn) {
		return state.train(burnIn, proposalCount, minAccept, trackImprovementIn);
	}

	@Override
	public void setParametersToAverage() {
		state.learningState.setParametersToAverage();
	}

	@Override
	public String stringParameters() {
		return state.stringParameters();
	}

	@Override
	public void marginalInference(int burnIn, int proposalCount, IMarginal marginal) {
		state.marginalInference(burnIn, proposalCount, marginal);
	}

	@Override
	public void mapInference(MaximumAPost map, int proposalCount, double epsilon, int reportEvery) {
		state.mapInference(map, proposalCount, epsilon, reportEvery);
	}

	public CRFState getState() {
		return state;
	}
}
