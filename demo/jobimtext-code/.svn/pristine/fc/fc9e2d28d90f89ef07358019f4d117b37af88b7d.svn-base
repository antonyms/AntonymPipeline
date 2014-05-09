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

import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.util.CleaningCollection;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.FunST;



//The FormulaFindGround signature hashes all point to these structures
public class IndexEntry {
	public double getImmediateWeight() {
		double sum = 0;
		for (FormulaCreate c : creates) {
			if (!c.isGarbage()) {
				sum += c.getWeight();
			}
		}
		return sum;
	}
	
	public IndexEntry(ScanTerm term) {
		this.term = term;
	}
	
	public void uncreate() {
		for (FormulaCreate c : creates) {
			if (!c.isGarbage()) {
				if (SGDebug.LOGGING) SGLog.fine("Dropping: "+c);
				c.drop();
			}
		}
	}
	public void addCreate(FormulaCreate create) {
		creates.add(create);
	}
	
	public boolean hasNoDepends() {
		creates.clean();
		return creates.size() == 0;
	}
	
	public Iterable<FormulaCreate> getCreates() {
		return creates;
	}
	
	public ScanTerm term; //the ground term
	//the factors/defines/bagMembers that depend on the truth value of the term
	private CleaningCollection<FormulaCreate> creates = new CleaningCollection<FormulaCreate>(
			new FunST<FormulaCreate, Boolean>() {
				public Boolean f(FormulaCreate o) {
					return o.isGarbage();
				} 
			});	
	
	public int hashCode() {
		return term.hashCode();
	}
	public boolean equals(Object o) {
		if (!(o instanceof IndexEntry)) {
			return false;
		}
		return term.equals(((IndexEntry)o).term);
	}
	
	public String toString() {
		return this.getClass().getSimpleName()+(term != null ? term.toString(): "");
	}
}
