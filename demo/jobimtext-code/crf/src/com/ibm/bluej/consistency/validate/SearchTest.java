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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.RandomUtil;



public class SearchTest {
	//CONSIDER: would be even better if the TestCase just took a description of the Feasible region
	//then it could select some operations at random

	ArrayList<ScanTerm> setTrue = new ArrayList<ScanTerm>();
	String name;
	
	//TODO: getRandomFeasible function too
	// will need to also store setTrue as a hashset so we can reject any getRandomFeasible that are in setTrue
	
	private static void validateOperationList(List<Pair<Boolean, ScanTerm>> operations) {
		HashSet<ScanTerm> trueTerms = new HashSet<ScanTerm>();
		int opNum = 0;
		for (Pair<Boolean, ScanTerm> op : operations) {
			++opNum;
			if (op.first) {
				if (!trueTerms.add(op.second)) {
					throw new Error(opNum+" already true: "+op.second+" in \n"+Lang.stringList(operations, ";\n"));
				}
			} else {
				if (!trueTerms.remove(op.second)) {
					throw new Error(opNum+" already false: "+op.second+" in \n"+Lang.stringList(operations, ";\n"));
				}
			}
		}
	}
	
	public SearchTest(String name, Collection<ATerm> setTrue) {
		this.name = name;
		Binds empty = new Binds(null);
		for (ATerm t : setTrue) {
			if (!(t instanceof ScanTerm)) {
				throw new Error(t + " is not a ScanTerm");
			}
			if (!t.isGround(empty)) {
				throw new Error(t + " is not ground");
			}
			this.setTrue.add((ScanTerm)t);
		}
	}
	
	//run multiple randomizations of the testCase and compare them
	public void test(int numTests) {
		//SGSearch.setLogLevel(Level.ALL);

		HashSet<TestResult> theResults = new HashSet<TestResult>();
		for (int ti = 0; ti < numTests; ++ti) {
			SGSearch.newProblem();
			List<Pair<Boolean, ScanTerm>> operations = randomOperations();
			//validateOperationList(operations);
			for (Pair<Boolean, ScanTerm> op : operations) {
				if (op.first) {
					SGSearch.nowTrue(op.second);
				} else {
					SGSearch.nowFalse(op.second);
				}
				//DefineCreator.showDefineTable();
			}
			TestResult tr = new TestResult();
			theResults.add(tr);
		}
		if (theResults.size() > 1) {
			System.err.println("On test case "+name+" "+theResults.size()+" different results from "+numTests+" runs");
			for (TestResult tr : theResults) {
				System.out.println("\n****************************************\n");
				System.out.println(tr);
			}
		} else {
			System.out.println("All test results give same answer.");
			for (TestResult tr : theResults) {
				System.out.println(tr);
			}
		}
	}
	
	
	private ArrayList<ScanTerm> getNegatable(ArrayList<Pair<Boolean, ScanTerm>> operations, int negNdx) {
		HashSet<ScanTerm> negatable = new HashSet<ScanTerm>();
		for (int i = 0; i < negNdx; ++i) {
			Pair<Boolean, ScanTerm> op = operations.get(i);
			if (op.first) {
				negatable.add(op.second);
			} else {
				negatable.remove(op.second);
			}
		}
		return new ArrayList<ScanTerm>(negatable);
	}
	
	public List<Pair<Boolean, ScanTerm>> randomOperations() {
		ArrayList<Pair<Boolean, ScanTerm>> operations = new ArrayList<Pair<Boolean, ScanTerm>>();
		//first shuffle setTrue
		Collections.shuffle(setTrue);
		//Initialize operations
		for (ScanTerm st : setTrue) {
			operations.add(new Pair<Boolean, ScanTerm>(Boolean.TRUE, st));
		}
		//Then select a random number of negations
		int numNegations = RandomUtil.randomInt(0, setTrue.size()+1);
		//for each negation
		for (int i = 0; i < numNegations; ++i) {
		    //select a random position negNdx > 0
			//select a random member of setTrue with position < negNdx
			int negNdx = -1;
			ScanTerm toNegate = null;
			while (negNdx <= 0) {
				negNdx = RandomUtil.randomInt(1, operations.size()+1);
				ArrayList<ScanTerm> negatable = getNegatable(operations, negNdx);	
				if (negatable.size() == 0) {
					negNdx = -1;
				} else {
					toNegate = RandomUtil.randomMember(negatable);
				}
			}
			operations.add(negNdx, new Pair<Boolean, ScanTerm>(Boolean.FALSE, toNegate));
			//select a new random position to restore the truth resNdx > negNdx
			int restoreNdx = RandomUtil.randomInt(negNdx+1, operations.size()+1);
			operations.add(restoreNdx, new Pair<Boolean, ScanTerm>(Boolean.TRUE, toNegate));
		}
		
		return operations;
	}
	

}
