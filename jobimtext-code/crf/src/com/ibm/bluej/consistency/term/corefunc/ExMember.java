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

package com.ibm.bluej.consistency.term.corefunc;

import java.util.Collection;
import java.util.Iterator;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.VarTerm;

//This doesn't work in general. ExpandTerms can't take NONCONST (usually)
public class ExMember extends ExpandTerm {
	//member(element, Set)
	
	public int estimateExpand(Binds binds) {
		if (parts[1].isGround(binds) && parts[0] instanceof VarTerm) {
			return ATerm.getCollection(parts[1]).size();
		}
		return Integer.MAX_VALUE;
	}

	private static class ExIt implements Iterator<Binds> {
		Binds binds;
		Iterator<ATerm> it;
		VarTerm v;
		
		ExIt(ATerm[] quicksave, Collection<ATerm> tcol, VarTerm v) {
			this.v = v;
			this.binds = new Binds(quicksave);
			this.it = tcol.iterator();
		}
		
		public boolean hasNext() {
			return it.hasNext();
		}

		public Binds next() {
			binds.set(v, it.next());
			return binds;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public Iterator<Binds> expand(Binds binds) {
		return new ExIt(binds.getQuicksave(), ATerm.getCollection(parts[1]), (VarTerm)parts[0]);
	}


	public void update(Function source, Object... msg) {
		assert( parts.length == 2);
		BooleanTerm prevTruth = truth;
		truth = ATerm.getCollection(parts[1]).contains(parts[0]) ? BooleanTerm.TRUE : BooleanTerm.FALSE;
		if (!truth.equals(prevTruth)) {
			passUpdate();
		}
	}

}
