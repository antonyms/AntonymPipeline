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

package com.ibm.bluej.consistency.validate.testcases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.CoreConsistency;
import com.ibm.bluej.consistency.ISimpleConsistency;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.ProposalFunction;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.inference.CompletionMAP;
import com.ibm.bluej.consistency.inference.VariationMAP;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.VariationRank;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.consistency.optimize.L1Regularizer;
import com.ibm.bluej.consistency.optimize.LatentOptimize;
import com.ibm.bluej.consistency.optimize.Optimizer;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PeriodicChecker;
import com.ibm.bluej.util.common.SecondPairComparator;
import com.ibm.bluej.util.common.Warnings;

public class HMMLatent {
	//data generation
	private static String[] pClasses = new String[] {"V", "D", "N"};
	private static String[] pWords = new String[] {"go", "work", "smile", "a", "an", "the", "cat", "bob", "dog"};
	
	//(pClasses U {BEGIN}) x (pClasses U {END})
	//pClasses x pWords
	//don't create a purely random matrix
	/*
	private static double[][] pClassT = new double[][] {
		//V  D   N   END
		{0, .4, .4, .2}, //V
		{0,  0,  1,  0}, //D
		{.4, 0, .4, .2}, //N
		{.3, .5, .2, 0}  //BEGIN
	};
	private static double[][] pC2W = new double[][] {
		//go work smile  a   an  the  cat  bob  dog
		{.3,   .3,  .3,  0,  0,   0,   0,   0,  .1}, //V
		{0,     0,   0, .3, .3,  .4,   0,   0,   0}, //D
		{0,    .2,  .1,  0,  0,   0,  .2,  .2,  .3}  //N
	};
	*/
	
	private static double[][] pClassT = new double[][] {
		//V  D   N   END
		{0, .5, .5, 0}, //V
		{0,  0,  1,  0}, //D
		{.7, 0, .1, .2}, //N
		{0, .9, .1, 0}  //BEGIN
	};
	private static double[][] pC2W = new double[][] {
		//go work smile  a   an  the  cat  bob  dog
		{.4,   .3,  .3,  0,  0,   0,   0,   0,  0}, //V
		{0,     0,   0, .3, .3,  .4,   0,   0,   0}, //D
		{0,    0,  0,  0,  0,   0,  .4,  .3,  .3}  //N
	};
	
	/*
	private static double[][] pClassT = new double[][] {
		//V  D   N   END
		{0, .5,  0, .5}, //V
		{0,  0,  1,  0}, //D
		{.5, 0, .0, .5}, //N
		{0, 1, 0, 0}  //BEGIN
	};
	private static double[][] pC2W = new double[][] {
		//go work smile  a   an  the  cat  bob  dog
		{1,    0,   0,  0,  0,   0,   0,   0,  0}, //V
		{0,    0,   0,  0,  0,   1,   0,   0,  0}, //D
		{0,    0,   0,  0,  0,   0,   1,   0,  0}  //N
	};
	*/
	private static void toCDF(double[] dist) {
		double sum = 0;
		for (double p : dist) {
			sum += p;
		}
		double cumulative = 0;
		for (int i = 0; i < dist.length; ++i) {
			cumulative += dist[i];
			dist[i] = cumulative / sum;
		}
	}
	static {
		//convert transition matrices to Cumulative probability distributions
		for (double[] dist : pClassT) {
			toCDF(dist);
		}
		for (double[] dist : pC2W) {
			toCDF(dist);
		}
	}
	
	private static StringTerm classAt = StringTerm.canonical("classAt");
	private static StringTerm wordAt = StringTerm.canonical("wordAt");
	private static StringTerm possibleWordPred = StringTerm.canonical("possibleWord");
	private static StringTerm possibleClassPred = StringTerm.canonical("possibleClass");
	private static StringTerm completionWordPred = StringTerm.canonical("completionWord");
	
	//generate words and classes
	private static Pair<ScanTerm[],String[]> generateWords() {
		//use pWords, pClasses and transition matrix to generate a random "sentence"
		ArrayList<String> wordSeq = new ArrayList<String>();
		ArrayList<String> classSeq = new ArrayList<String>();
		int currentClass = pClasses.length;
		while (true) {
			//select next class
			double crnd = Math.random();
			for (int i = 0; i < pClassT[currentClass].length; ++i) {
				if (crnd <= pClassT[currentClass][i]) {
					currentClass = i;
					break;
				}
			}
			if (currentClass == pClasses.length) {
				break;
			}
			//select word
			double wrnd = Math.random();
			for (int i = 0; i < pC2W[currentClass].length; ++i) {
				if (wrnd <= pC2W[currentClass][i]) {
					wordSeq.add(pWords[i]);
					classSeq.add(pClasses[currentClass]);
					break;
				}
			}
		} 
		
		if (SGDebug.LOGGING) SGLog.fine(Lang.stringList(wordSeq, " "));
		
		//wordSeq to ScanTerm
		ScanTerm[] words = new ScanTerm[wordSeq.size()];
		for (int i = 0; i < words.length; ++i) {
			words[i] = new ScanTerm(
				wordAt, NumberTerm.canonical(i), new StringTerm(wordSeq.get(i))	
			);
		}
		
		return new Pair<ScanTerm[], String[]>(words,
				classSeq.toArray(new String[classSeq.size()]));
	}
	
	
	
	private static ScanTerm initialize(ISimpleConsistency con) {
		con.newProblem();
		for (String pc : pClasses) {
			con.evidence(possibleClassPred, new StringTerm(pc));
		}
		for (String pw : pWords) {
			con.evidence(possibleWordPred, new StringTerm(pw));
		}
		
		Pair<ScanTerm[],String[]> gen = generateWords();
		// randomly select one word as the completion
		int compNdx = SGSearch.rand.nextInt(gen.first.length);
		con.evidence(completionWordPred, NumberTerm.canonical(compNdx));
		for (int i = 0; i < gen.first.length; ++i) {
			if (i != compNdx)
				con.evidence(gen.first[i]);
			else
				con.initialTrue(wordAt, NumberTerm.canonical(i), new StringTerm(pWords[SGSearch.rand.nextInt(pWords.length)]));
			con.initialTrue(classAt, NumberTerm.canonical(i), new StringTerm(pClasses[SGSearch.rand.nextInt(pClasses.length)]));
		}
		ScanTerm completion = gen.first[compNdx];
		return completion;
	}
	
	private static VariationMAP forPerceptronStyle(ISimpleConsistency con, ScanTerm completion) {
		ScanTerm variationPattern = new ScanTerm(wordAt, completion.parts[1], null);
		return new VariationMAP(con.getState(), completion, 5, variationPattern);
	}
	
	private static String trainOptStyle() {
		ISimpleConsistency con = new CoreConsistency();
		con.getState().learningState.randomInitialValue = true;
		con.loadDescription(FileUtil.readResourceAsString("HMM.con"));
		PeriodicChecker report = new PeriodicChecker(30);
		for (int outer = 0; outer < 3; ++outer) {
			StringBuffer trainData = new StringBuffer();
			int oneEmpty = 0;
			for (int inst = 0; inst < 4000; ++inst) {
				if (report.isTime()) {
					System.out.println("On instance "+inst+" skipped "+oneEmpty);
				}
				CompletionMAP map = new CompletionMAP(con.getState(), initialize(con), 50, 100);
				con.mapInference(map, 4000, 0.1, 0);
				Pair<ArrayList<WorldWeightParam>, ArrayList<WorldWeightParam>> goodBad = map.getGoodAndBad();
				if (goodBad.first.isEmpty() || goodBad.second.isEmpty()) {
					Warnings.limitWarn("GOODBADEMPTY", 10, "good "+goodBad.first.size()+" bad "+goodBad.second.size());
					++oneEmpty;
					continue;
				}
				trainData.append(LatentOptimize.stringInstance(goodBad.first, goodBad.second)).append('\n');
			}
			Optimizer opt = new Optimizer(trainData.toString());
			opt.optimize(1000, new L1Regularizer(1), null);
			for (ParamWeight pw : con.getState().learningState.getAllParamWeights()) {
				ProposalFunction p = opt.getParameters().get(ParamWeight.proposalFunctionName(pw.getName()));
				if (p != null) {
					p.setValue(pw.getWeight());
				}
			}
			for (ProposalFunction p : opt.getParameters().values()) {
				ParamWeight pw = con.getState().learningState.instance(ParamWeight.paramKeyFromProposalName(p.getName()));
				pw.pushDelta(p.value.value-pw.getWeight(), con.getState().learningState);
				//con.getState().learningState.set(ParamWeight.paramKeyFromProposalName(p.getName()), p.value.value);
			}
			con.getState().learningState.incrementTimestep();
			//con.setParametersToAverage();
			System.out.println(con.stringParameters());
		}
		return con.stringParameters();
	}
	
	//TODO: it learns a little now, try to make it faster and give it some harder problems
	//variation rank for DCL with HMM
	private static String trainPerceptronStyle() {
		ISimpleConsistency con = new CoreConsistency();
		con.loadDescription(FileUtil.readResourceAsString("HMM.con"));
		VariationRank vrank = new VariationRank(con.getState().learningState);
		PeriodicChecker report = new PeriodicChecker(30);
		for (int i = 0; i < 10000; ++i) {		
			VariationMAP map = forPerceptronStyle(con, initialize(con));
			con.mapInference(map, 5000, 0.1, 0);
			Pair<ArrayList<WorldWeightParam>, ArrayList<WorldWeightParam>> goodBad = map.getGoodAndBad();
			vrank.multiVariationRank(goodBad.first, goodBad.second);
			//vrank.variationRank(map.getVariations());
			
			//con.setParametersToAverage();
			
			if (report.isTime()) {
				
				System.out.println("Over "+vrank.vcount+
						" Accuracy = "+Lang.dblStr(vrank.recencyWeightedAccuracy)+
						" Rank = "+Lang.dblStr(1+vrank.recencyWeightedUpdateCount));	
				Iterator<IndexEntry> it = con.getState().posGrnd.getGroundTerms();
				ArrayList<Pair<String,Integer>> words = new ArrayList<Pair<String,Integer>>();
				int comp = -1;
				while (it.hasNext()) {
					ScanTerm t = it.next().term;
					
					if (t.parts.length == 3 && t.parts[0].equals(wordAt)) {
						words.add(Pair.make(ATerm.getString(t.parts[2]), ATerm.getInt(t.parts[1])));
					} else if (t.parts.length == 2 && t.parts[0].equals(completionWordPred)) {
						comp = (int)ATerm.getInt(t.parts[1]);
					}
					
				}
				SecondPairComparator.sort(words);
				for (int wi = 0; wi < words.size(); ++wi) {
					if (wi == comp) {
						System.out.print("*** ");
					} else {
						System.out.print(words.get(wi).first+" ");
					}
				}
				System.out.println();
				
				Map<String,Double> paramValues = new HashMap<String,Double>();
				Iterator<ATerm[]> keyIt = con.getState().learningState.getAllSpecifiers();
				while (keyIt.hasNext()) {
					ParamWeight p = con.getState().learningState.instance(keyIt.next());
					paramValues.put(ParamWeight.proposalFunctionName(p.getName()), p.getWeight());
				}
				String func = LatentOptimize.stringInstance(goodBad.first, goodBad.second);
				System.out.println(Optimizer.getValue(func, paramValues) + " = "+func);
				
				//for (Pair<WorldWeightParam, String> v : map.getVariationsWithKey()) {
				//	System.out.println(v.second);
				//	System.out.println("  "+v.first);
				//}
				//con.getState().printTruth();
				con.setParametersToAverage();
			}
		}
		
		System.out.println(con.getState().getDescription());
		return con.stringParameters();
	}
	
	public static void main(String[] args) {
		trainOptStyle();//trainPerceptronStyle();
		Lang.readln();
	}
}
