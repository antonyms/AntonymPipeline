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
 * Simple interface to Consistency, includes only the basic functions
 * @author mrglass
 *
 */
public interface ISimpleConsistency {
	public void loadDescription(String fileContents);
	public void newProblem();
	public void loadParamters(String parameterContents);
	
	public void evidence(ATerm... terms);
	public void reEvidence(ATerm... terms);
	public void initialTrue(ATerm... terms);
	
	public TrainingAnalysis train(int burnIn, int proposalCount, double minAccept, FunST<ScanTerm,Boolean> trackImprovementIn);
	public void setParametersToAverage();
	public String stringParameters();
	
	public void marginalInference(int burnIn, int proposalCount, IMarginal marginal);
	public void mapInference(MaximumAPost map, int proposalCount, double epsilon, int reportEvery);
	
	public CRFState getState();
}
