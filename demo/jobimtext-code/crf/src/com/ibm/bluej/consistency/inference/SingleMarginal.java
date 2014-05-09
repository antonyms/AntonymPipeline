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

package com.ibm.bluej.consistency.inference;

import com.ibm.bluej.consistency.term.ScanTerm;

public class SingleMarginal implements IMarginal {
	public SingleMarginal(ScanTerm query) {
		this.query = query;
	}
	
	public boolean isInterested(ScanTerm t) {
		return query.equals(t);
	}
	
	private ScanTerm query;
	private int sum = 0;
	private int flipNumWhenSet = 0;
	
	private int flipCount = 0;
	
	public void nowTrue(ScanTerm t) {	
		flipNumWhenSet = flipCount;
	}
	public void nowFalse(ScanTerm t) {
		sum += flipCount - flipNumWhenSet;
	}
	
	/**
	 * Called on commit or rollback
	 */
	public void nextState() {
		++flipCount;
	}
	
	public double getProb() {
		sum += flipCount - flipNumWhenSet;
		flipNumWhenSet = flipCount;
		return sum*1.0/flipCount;
	}
}
