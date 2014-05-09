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

package com.ibm.bluej.consistency.validate.testcases;

import java.util.ArrayList;
import java.util.logging.Level;

import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.inference.SingleMAP;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.consistency.validate.SearchTest;
import com.ibm.bluej.util.common.FileUtil;


public class MinSpanTree {
	
	public static void main(String[] args) {
		//SGSearch.setLogLevel(Level.FINE);
		//firstTest();
		search();
		//unwind();
	}
	
	private static void unwind() {
		SGSearch.loadDescription(FileUtil.readResourceAsString("mst.con"));
		ArrayList<ScanTerm> links = new ArrayList<ScanTerm>();
		links.add(ScanTerm.basicFromString("link(A,B,10)"));
		links.add(ScanTerm.basicFromString("link(A,C,10)"));
		links.add(ScanTerm.basicFromString("link(A,D,10)"));
		links.add(ScanTerm.basicFromString("link(A,E,10)"));
		links.add(ScanTerm.basicFromString("link(A,F,10)"));
		links.add(ScanTerm.basicFromString("link(B,C,1)"));
		links.add(ScanTerm.basicFromString("link(D,C,1)"));
		links.add(ScanTerm.basicFromString("link(E,C,1)"));
		for (ScanTerm l : links) {
			SGSearch.nowTrue(l);
		}
		SGSearch.nowTrue(ScanTerm.basicFromString("intree(A,C)"));
		SGSearch.nowTrue(ScanTerm.basicFromString("intree(B,C)"));
		SGSearch.nowTrue(ScanTerm.basicFromString("intree(D,C)"));
		SGSearch.nowTrue(ScanTerm.basicFromString("intree(E,C)"));
		SGSearch.nowTrue(ScanTerm.basicFromString("intree(A,F)"));
		SGSearch.showDefineTable();
		SGSearch.nowFalse(ScanTerm.basicFromString("intree(A,C)"));
		SGSearch.showDefineTable();
	}
	
	private static void search() {
		//Lang.readln();
		//SGLog.setLevel(Level.FINE);
		SGSearch.loadDescription(FileUtil.readResourceAsString("mst.con"));
		SGSearch.evidence(ScanTerm.basicFromString("link(A,B,10)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(A,C,10)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(A,D,10)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(A,E,10)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(A,F,10)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(B,C,1)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(D,C,1)"));
		SGSearch.evidence(ScanTerm.basicFromString("link(E,C,1)"));

		SingleMAP map = new SingleMAP(SGSearch.getCRFState());
		SGSearch.mapInference(map, 10000, 0.15);
		
		System.out.println(map.getBestWeight());
		for (ScanTerm b : map.getBest()) {
			System.out.println(b);
		}
		//System.out.println(new TestResult());		
	}
	
	private static void firstTest() {
		SGLog.setLevel(Level.SEVERE);
		SGSearch.loadDescription(FileUtil.readResourceAsString("mst.con"));
		ArrayList<ATerm> setTrue = new ArrayList<ATerm>();
		setTrue.add(ScanTerm.basicFromString("link(A,B,10)"));
		setTrue.add(ScanTerm.basicFromString("link(A,C,10)"));
		setTrue.add(ScanTerm.basicFromString("link(A,D,10)"));
		setTrue.add(ScanTerm.basicFromString("link(A,E,10)"));
		setTrue.add(ScanTerm.basicFromString("link(A,F,10)"));
		setTrue.add(ScanTerm.basicFromString("link(B,C,1)"));
		setTrue.add(ScanTerm.basicFromString("link(D,C,1)"));
		setTrue.add(ScanTerm.basicFromString("link(E,C,1)"));
		
		setTrue.add(ScanTerm.basicFromString("intree(A,C)"));
		setTrue.add(ScanTerm.basicFromString("intree(A,F)"));
		setTrue.add(ScanTerm.basicFromString("intree(B,C)"));
		setTrue.add(ScanTerm.basicFromString("intree(D,C)"));
		setTrue.add(ScanTerm.basicFromString("intree(E,C)"));
		
		SearchTest opt = new SearchTest("optimal", setTrue);
		opt.test(1);		
	}
}
