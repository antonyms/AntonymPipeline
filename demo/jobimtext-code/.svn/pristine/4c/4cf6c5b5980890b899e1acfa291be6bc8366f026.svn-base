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

public class DummyIndexEntry extends IndexEntry {

	public static DummyIndexEntry check = new DummyIndexEntry(null);
	public static DummyIndexEntry negScan = new DummyIndexEntry(null);
	
	public DummyIndexEntry(ScanTerm term) {
		super(term);
	}

	public void uncreate() {
		throw new UnsupportedOperationException("Called uncreate on the dummy");
	}
	public void addCreate(FormulaCreate create) {
		throw new UnsupportedOperationException("Called addCreate on the dummy");
	}
}
