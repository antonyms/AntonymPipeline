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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Bag;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.TermCollection;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.MutableInteger;



public class FuncIntersection extends TermCollection {
	private Map<ATerm,  MutableInteger> foundIn = new HashMap<ATerm,  MutableInteger>();
	private int size = 0;
	
	public boolean contains(Object o) {
		MutableInteger d = foundIn.get(o);
		return (d != null && d.value == parts.length);
	}
	
	private void addInternal(ATerm element, Object[] msg) {
		 MutableInteger d = foundIn.get(element);
		if (d == null) {
			d = new MutableInteger(1);
			foundIn.put(element, d);
		} else {
			d.value += 1;
		}
		if (d.value == parts.length) {
			++size;
			if (msg != null) {
				passUpdate(msg);
			}
		}
	}
	
	public void update(Function source, Object... msg) {
		//on initial create the source is null and msg zero length
		if (msg.length == 0) {
			foundIn.clear();
			for (ATerm p : parts) {
				for (ATerm m : ATerm.getCollection(p)) {
					addInternal(m, null);
				}
			}
			passUpdate();
			return;
		}
		if (SGDebug.BASIC) {
			if (parts.length < 2) {
				System.err.println("Union should have at least two arguments");
			}
			for (int i = 0; i < parts.length; ++i) {
				if (parts[i] instanceof Bag) {
					System.err.println("Arg "+i+" of "+this.toString()+" is a Bag, not a Set");
				}
			}
		}
		assert(msg.length == 2);
		String operation = (String)msg[0];
		ATerm element = (ATerm)msg[1];
		
		
		if (operation == OriginatingTermCollection.ADD) {
			addInternal(element, msg);
		} else if (operation == OriginatingTermCollection.REM) {
			MutableInteger d = foundIn.get(element);
			assert (d != null);
			d.value -= 1;
			if (d.value == 0) {
				foundIn.remove(element);
			}
			passUpdate(msg);
			--size;
		} else {
			throw new Error("Unsupported update: "+operation);
		}
	}


	private class Iter implements Iterator<ATerm> {
		ATerm nextVal = null;
		Iterator<Map.Entry<ATerm, MutableInteger>> fit;
		Iter() {
			fit = foundIn.entrySet().iterator();
			while (fit.hasNext()) {
				Map.Entry<ATerm, MutableInteger> e = fit.next();
				if (e.getValue().value == parts.length) {
					nextVal = e.getKey();
				}
			}
		}
		
		public boolean hasNext() {
			return nextVal != null;
		}
		
		public ATerm next() {
			if (nextVal == null) {
				return null;
			}
			ATerm toReturn = nextVal;
			nextVal = null;
			while (fit.hasNext()) {
				Map.Entry<ATerm, MutableInteger> e = fit.next();
				if (e.getValue().value == parts.length) {
					nextVal = e.getKey();
				}
			}
			
			return toReturn;
		}

		public void remove() {
			throw new UnsupportedOperationException("Cannot alter");
		}
	}
	
	public Iterator<ATerm> iterator() {
		return new Iter();
	}

	public int size() {
		return size;
	}

}
