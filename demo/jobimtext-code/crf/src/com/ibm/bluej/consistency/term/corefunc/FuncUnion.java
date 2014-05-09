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
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.TermCollection;
import com.ibm.bluej.util.common.MutableInteger;




//This also works on CONST bags (it will provide a set view of the bags)
public class FuncUnion extends TermCollection {
	Map<ATerm, MutableInteger> contents = new HashMap<ATerm, MutableInteger>();

	public boolean contains(Object o) {
		return contents.containsKey(o);
	}
	
	private void addInternal(ATerm element, Object[] msg) {
		 MutableInteger d = contents.get(element);
		if (d == null) {
			d = new  MutableInteger(1);
			contents.put(element, d);
			if (msg != null) {
				passUpdate(msg);
			}
		} else {
			d.value += 1;
		}
	}
	
	//the update will come from one of the sets
	//the message will be ADD or REM some Term
	//if changed then passUpdate(msg);
	public void update(Function source, Object... msg) {
		//on initial create the source is null and msg zero length
		if (msg.length == 0) {
			contents.clear();
			for (ATerm p : parts) {
				for (ATerm m : ATerm.getCollection(p)) {
					addInternal(m, null);
				}
			}
			passUpdate();
			return;
		}
		assert(msg.length == 2);
		String operation = (String)msg[0];
		ATerm element = (ATerm)msg[1];
			
		if (operation == OriginatingTermCollection.ADD) {
			addInternal(element, msg);
		} else if (operation == OriginatingTermCollection.REM) {
			MutableInteger d = contents.get(element);
			assert (d != null);
			d.value -= 1;
			if (d.value == 0) {
				contents.remove(element);
				passUpdate(msg);
			}
		} else {
			throw new Error("Unknown update: "+operation);
		}
		
	}

	public int size() {
		return contents.size();
	}

	public Iterator<ATerm> iterator() {
		return contents.keySet().iterator();
	}

}
