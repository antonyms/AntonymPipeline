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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.TermCollection;


public class FuncConnectedTo extends TermCollection {
	NumberTerm conSize = new NumberTerm(0);
	HashMap<ATerm, HashSet<ATerm>> edges = new HashMap<ATerm, HashSet<ATerm>>();
	HashSet<ATerm> connected = new HashSet<ATerm>();

	private void recompute(ATerm root) {
		//starting from root, find the set of connected terms
		connected.clear();
		connected.add(root);
		ArrayList<ATerm> addList = new ArrayList<ATerm>();
		while (true) {
			for (ATerm con : connected) {
				HashSet<ATerm> conTo = edges.get(con);
				if (conTo != null) {
					addList.addAll(conTo);
				}
			}
			if (!connected.addAll(addList)) {
				break;
			}
		}
		conSize.value = connected.size();
		passUpdate(); //no incremental updates here
	}
	
	private void addInternal(ATerm e) {
		if (!(e instanceof CompositeTerm) || ((CompositeTerm)e).parts.length != 2) {
			throw new Error("Tried to assess connectivity of a set non-edge member: "+e);
		}
		ATerm v1 = ((CompositeTerm)e).parts[0];
		ATerm v2 = ((CompositeTerm)e).parts[1];
		HashSet<ATerm> to = edges.get(v1);
		if (to == null) {
			to = new HashSet<ATerm>();
			edges.put(v1,to);
		}
		to.add(v2);
	}
	
	private void removeInternal(ATerm e) {
		if (!(e instanceof CompositeTerm) || ((CompositeTerm)e).parts.length != 2) {
			throw new Error("Tried to assess connectivity of a set non-edge member: "+e);
		}
		ATerm v1 = ((CompositeTerm)e).parts[0];
		ATerm v2 = ((CompositeTerm)e).parts[1];
		if (!edges.get(v1).remove(v2)) {
			throw new Error("Removed but not present: "+e);
		}
	}
	
	public void update(Function source, Object... msg) {
		assert (parts.length == 2);
		ATerm root = ATerm.getTerm(parts[0]);
		
		//graph must be a Set of composite terms of length 2 (edges)
		//this is a directed graph description
		if (msg.length == 0) {
			Collection<ATerm> graph = ATerm.getCollection(parts[1]);
			for (ATerm e : graph) {
				addInternal(e);
			}
		} else if (source == parts[1]) {
			//modify edges map
			String operation = (String)msg[0];
			ATerm element = (ATerm)msg[1];

			if (operation == OriginatingTermCollection.ADD) {
				addInternal(element);
			} else if (operation == OriginatingTermCollection.REM) {
				removeInternal(element);
			} else {
				throw new Error("Unknown operation: "+operation);
			}
		} else {
			//must be the root that changed	
		}
		recompute(root);
	}

	public int size() {
		return connected.size();
	}

	public boolean contains(Object o) {
		return connected.contains(o);
	}

	public Iterator<ATerm> iterator() {
		return connected.iterator();
	}

}
