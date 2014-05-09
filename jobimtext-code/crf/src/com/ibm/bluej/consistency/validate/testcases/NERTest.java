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

import java.util.HashMap;
import java.util.Map;

import com.ibm.bluej.consistency.CoreConsistency;
import com.ibm.bluej.consistency.ISimpleConsistency;
import com.ibm.bluej.consistency.inference.MultiMarginal;
import com.ibm.bluej.consistency.inference.SingleMAP;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.CheckTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.PrecisionRecall;


public class NERTest {

	public static void main(String[] args) {
		String modelFile = "nerTest3.con";
		String dataDir = "/users/glassm/Consistency/";
		
		ISimpleConsistency con = new CoreConsistency();
		
		con.loadDescription(FileUtil.readResourceAsString(modelFile));
		
		//train
		System.out.println("**** Training ****");
		train(con, dataDir, modelFile);
		
		//test
		System.out.println("**** MAP ****");
		evaluate(dataDir+"jane-austen-emma-ch2.tsv", false, mapInference(con,dataDir));
		System.out.println("**** Marginal ****");
		evaluate(dataDir+"jane-austen-emma-ch2.tsv", false, marginalInference(con,dataDir));
	}
	
	private static void initialize(ISimpleConsistency con, String filename, boolean training) {
		con.newProblem();
		int ndx = 0;
		for (String line : FileUtil.getLines(filename)) {
			String[] parts = line.split("\t");
			String word = parts[0];
			con.evidence(StringTerm.canonical("wordAt"), new NumberTerm(ndx), new StringTerm(word));
			if (training) {
				String tag = parts[1];
				con.evidence(StringTerm.canonical("goldTagAt"), new NumberTerm(ndx), StringTerm.canonical(tag));
			}
			++ndx;
		}
		//Two tags PERS (person) and O (other)
		con.evidence(StringTerm.canonical("possibleTag"), StringTerm.canonical("PERS"));
		con.evidence(StringTerm.canonical("possibleTag"), StringTerm.canonical("O"));
	}
	
	private static void train(ISimpleConsistency con, String dataDir, String modelFile) {
		long time = System.currentTimeMillis();
		initialize(con, dataDir+"jane-austen-emma-ch1.tsv", true);
		con.train(0, 1000000, 0.1, null);
		//System.out.println(con.stringParameters());
		con.setParametersToAverage();
		FileUtil.writeFileAsString(dataDir+modelFile+".params.txt", con.stringParameters());
		System.out.println("Finished training in "+(System.currentTimeMillis()-time)/1000.0);
		//System.out.println(con.stringParameters());
	}
	
	private static Map<Integer, Pair<String,Double>> marginalInference(ISimpleConsistency con, String dataDir) {
		long time = System.currentTimeMillis();
		//con.loadParamters(FileUtil.readFileAsString(dataDir+modelFile+".params.txt"));
		initialize(con, dataDir+"jane-austen-emma-ch2.tsv", false);
		final StringTerm tagAt = StringTerm.canonical("tagAt");
		MultiMarginal marginal = new MultiMarginal(new FunST<ScanTerm,Boolean>() {
			public Boolean f(ScanTerm t) {
				return t.parts[0] == tagAt;
			}
		});
		con.marginalInference(100000, 1000000, marginal);
		Map<Integer, Pair<String,Double>> ndxToTag = new HashMap<Integer, Pair<String,Double>>();
		for (Pair<ScanTerm,Double> p : marginal.getProbs()) {
			Pair<String,Double> existing = ndxToTag.get((int)ATerm.getDouble(p.first.parts[1]));
			if (existing == null || existing.second < p.second) {
				ndxToTag.put((int)ATerm.getDouble(p.first.parts[1]), Pair.make(ATerm.getString(p.first.parts[2]), p.second));
			}
		}
		
		System.out.println("Finished test in "+(System.currentTimeMillis()-time)/1000.0);
		return ndxToTag;
	}
	
	private static Map<Integer, Pair<String,Double>> mapInference(ISimpleConsistency con, String dataDir) {
		long time = System.currentTimeMillis();
		//con.loadParamters(FileUtil.readFileAsString(dataDir+modelFile+".params.txt"));
		initialize(con, dataDir+"jane-austen-emma-ch2.tsv", false);
		
		SingleMAP map = new SingleMAP(con.getState()); 
		final StringTerm tagAt = StringTerm.canonical("tagAt");
		map.shouldSave = new FunST<ScanTerm,Boolean>() {
			public Boolean f(ScanTerm t) {
				return t.parts[0] == tagAt;
			}
		};
		con.mapInference(map, 200000, 0, -1);
		Map<Integer,Pair<String,Double>> ndxToTag = new HashMap<Integer,Pair<String,Double>>();
		for (ScanTerm b : map.getBest()) {
			ndxToTag.put((int)ATerm.getDouble(b.parts[1]), Pair.make(ATerm.getString(b.parts[2]), 1.0));
		}
		System.out.println("Finished test in "+(System.currentTimeMillis()-time)/1000.0);
		return ndxToTag;
	}
	
	private static void evaluate(String filename, boolean errorAnalysis, Map<Integer, Pair<String,Double>> ndxToTag) {
		int ndx = 0;
		PrecisionRecall pr = new PrecisionRecall(); //TODO: this is a strange/bad definition of P/R
		boolean showPRCurve = false;
		for (String line : FileUtil.getLines(filename)) {
			String[] parts = line.split("\t");
			String tag = parts[2];
			Pair<String,Double> pred = ndxToTag.get(ndx);
			if (pred.second != 1.0) 
				showPRCurve = true;
			String ptag = pred.first;
			if (!"O".equals(tag) || !"O".equals(ptag)) {
				pr.addAnswered(tag.equals(ptag), pred.second);
				if (errorAnalysis)
					if (!tag.equals(ptag)) {
						System.out.println(Lang.RPAD(parts[0], 50)+tag +" != "+ ptag);
					} else if (Math.random() < 0.1) {
						System.out.println(Lang.RPAD(parts[0], 50)+tag);
					}
			}
			++ndx;
		}
		pr.printSummary();
		if (showPRCurve) {
			pr.printPRCurve(10);
		}
	}
	
	public static class FuncCapitalized extends CheckTerm {
		@Override
		public void update(Function source, Object... msg) {
			assert (parts.length == 0);
			String input = ATerm.getString(parts[0]);
			BooleanTerm prev = this.truth;
			this.truth = input.length() > 0 && Character.isUpperCase(input.charAt(0)) ? BooleanTerm.TRUE : BooleanTerm.FALSE;
			if (prev != this.truth)
				passUpdate();
		}	
	}
}
