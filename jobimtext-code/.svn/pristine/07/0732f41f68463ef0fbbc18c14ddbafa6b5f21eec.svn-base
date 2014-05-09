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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ATerm;


public class TestResult {
	//takes current contents of the SGSearch as the result
	public TestResult() {
		this.weight = SGSearch.getWeight();
		Iterator<IndexEntry> it = SGSearch.getCRFState().posGrnd.getGroundTerms();
		while (it.hasNext()) {
			IndexEntry e = it.next();
			terms.add(e.term.toString());
			for (FormulaCreate fc : e.getCreates()) {
				if (!fc.isGarbage()) {
					creates.add(fc.toString());
				}
			}
		}
		it = SGSearch.getCRFState().negGrnd.getGroundTerms();
		while (it.hasNext()) {
			IndexEntry e = it.next();
			terms.add("!"+e.term.toString());
			//pretty sure nothing new can be added to creates here
			for (FormulaCreate fc : e.getCreates()) {
				if (!fc.isGarbage()) {
					if (creates.add(fc.toString())) {
						System.err.println("Added new create from negGrnd, seems impossible: "+fc.toString());
					}
				}
			}
		}
		Iterator<Formula> fit = SGSearch.getCRFState().formFinder.getFormulas();
		while (fit.hasNext()) {
			formulas.add(fit.next().toString());
		}
	}
	
	public HashSet<String> creates = new HashSet<String>();
	public HashSet<String> terms = new HashSet<String>();
	public HashSet<String> formulas = new HashSet<String>();
	public double weight;
	//CONSIDER: anything else? Maybe set of all active creates?
	
	//TODO: highlightDiff
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Weight = "+weight+"\n");
		buf.append("Terms:\n");
		ArrayList<String> sortedTerms = new ArrayList<String>(terms);
		Collections.sort(sortedTerms);
		ArrayList<String> sortedFormulas = new ArrayList<String>(formulas);
		Collections.sort(sortedFormulas);
		for (String t : sortedTerms) {
			buf.append("  "+t+"\n");
		}
		buf.append("Formulas:\n");
		for (String f : sortedFormulas) {
			buf.append("  "+f+"\n");
		}
		buf.append("Creates:\n");
		for (String fc : creates) {
			buf.append("  "+fc+"\n");
		}
		return buf.toString();
	}
	
	Integer hash;
	
	public int hashCode() {
		if (hash == null) {
			int h = ATerm.mdjbFirst();
			//need to sort the terms...
			ArrayList<String> sortedTerms = new ArrayList<String>(terms);
			Collections.sort(sortedTerms);
			for (String t : sortedTerms) {
				h = ATerm.mdjbNext(h, t.hashCode());
			}
			ArrayList<String> sortedForms = new ArrayList<String>(formulas);
			Collections.sort(sortedForms);
			for (String t : sortedForms) {
				h = ATerm.mdjbNext(h, t.hashCode());
			}
			//h = new Double(weight).hashCode();
			hash = h;
		}
		return hash;
	}
	public boolean equals(Object o) {
		if (!(o instanceof TestResult)) {
			return false;
		}
		TestResult t = (TestResult)o;
		if (terms.size() != t.terms.size() || !terms.containsAll(t.terms)) {
			return false;
		}
		if (formulas.size() != t.formulas.size() || !formulas.containsAll(t.formulas)) {
			return false;
		}
		return weight - t.weight < 0.01;
	}
}
