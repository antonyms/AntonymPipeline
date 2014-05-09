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

import java.util.Iterator;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.util.IRandomAccessible;

public interface FormulaFindGroundRand {
	public ScanTerm randomMatch(ATerm[] specifiers);
	public void loadConfig(String[] lines);
	public String configToString();
	
	public IndexEntry contains(ScanTerm t);
	
	//t should be present
	public void remove(ScanTerm t, boolean defined);
	
	public void add(IndexEntry ti);
	
	public IRandomAccessible<IndexEntry> find(ScanTerm t, Binds binds);
	
	public int estimateMatching(ScanTerm t, Binds binds);
	
	public Iterator<IndexEntry> getGroundTerms();
	
	public void clear();
	
	public int size();
}
