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

package com.ibm.bluej.consistency.old;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;


public class FormulaFindGroundComboTest extends FormulaFindGround {

	private FormulaFindGround impl1, impl2;
	
	public FormulaFindGroundComboTest(FormulaFindGround impl1, FormulaFindGround impl2) {
		this.impl1 = impl1;
		this.impl2 = impl2;
	}
	
	public IndexEntry contains(ScanTerm t) {
		IndexEntry e1 = impl1.contains(t);
		IndexEntry e2 = impl2.contains(t);
		assert(e1 == e2);
		return e1;
	}

	public void remove(ScanTerm t, boolean defined) {
		impl1.remove(t, defined);
		impl2.remove(t, defined);
	}

	public void add(IndexEntry ti) {
		impl1.add(ti);
		impl2.add(ti);
	}

	public void find(ScanTerm t, Binds binds, List<IndexEntry> matching) {
		impl1.find(t, binds, matching);
		List<IndexEntry> matching2 = new ArrayList<IndexEntry>();
		impl2.find(t, binds, matching2);
		assert (matching.size() == matching2.size());
		assert (matching.containsAll(matching2));
	}

	public int estimateMatching(ScanTerm t, Binds binds) {
		return impl1.estimateMatching(t, binds);
	}

	public Iterator<IndexEntry> getGroundTerms() {
		return impl1.getGroundTerms();
	}

}
