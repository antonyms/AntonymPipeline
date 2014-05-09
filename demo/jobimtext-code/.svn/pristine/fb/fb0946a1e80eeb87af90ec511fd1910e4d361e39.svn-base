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

package com.ibm.bluej.consistency.term.corefunc;

import java.util.HashMap;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.SparseVectorTerm;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.MutableDoubleHashMap;

public class FuncSparseVectorSum extends Function {
	//this approximation is only acceptable when small PERCENTAGE differences will not matter much
	private static double APPROXIMATE = 10.0;
	private static double APPROXIMATION_SIZE = APPROXIMATE * APPROXIMATE;
	
	SparseVectorTerm value;
	private boolean dirty;
	public ATerm getValue() {
		return value;
	}

	private void fromScratch() {
		value = new SparseVectorTerm(new HashMap<Object, MutableDouble>());
		assert (parts.length == 1);
		for (ATerm t : ATerm.getCollection(parts[0])) {
			MutableDoubleHashMap.addTo(value.value, ATerm.getSparseVector(t));
		}
		passUpdate();
		dirty = false;
	}
	
	public void update(Function source, Object... msg) {
		//compute from scratch
		if (msg.length == 0) {
			fromScratch();
			return;
		}
		
		int size = ATerm.getCollection(parts[0]).size();
		if (APPROXIMATE > 0 && (size > APPROXIMATION_SIZE || dirty)) {
			if (Math.random() > APPROXIMATE/size) {
				dirty = true;
				return;
			} else if (dirty) {
				fromScratch();
				return;
			}
		}
		
		//incremental update
		String operation = (String)msg[0];
		ATerm element = (ATerm)msg[1];
		
		if (operation == OriginatingTermCollection.ADD) {
			MutableDoubleHashMap.addTo(value.value, ATerm.getSparseVector(element));
		} else if (operation == OriginatingTermCollection.REM) {
			MutableDoubleHashMap.subtract(value.value, ATerm.getSparseVector(element), false);
		/*} else if (operation == OriginatingTermCollection.MOD) {
			Object[] sourceMsg = (Object[])msg[3];
			double delta = (Double)sourceMsg[0];
			value.value += delta;
	    */
		} else {
			throw new Error("Unknown update: "+operation);
		}
		passUpdate();	
	}

}
