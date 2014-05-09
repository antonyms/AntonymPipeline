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

import java.util.Arrays;
import java.util.Collection;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.OriginatingTermCollection;
import com.ibm.bluej.consistency.term.VectorTerm;

public class FuncVectorSum extends VectorFunction {
	//this approximation is only acceptable when small PERCENTAGE differences will not matter much
	private static double APPROXIMATE = 10.0;
	private static double APPROXIMATION_SIZE = APPROXIMATE * APPROXIMATE;
	private boolean dirty;
	
	private void addVector(double[] vector) {	
		if (this.vector == null || this.vector.value.length == 0) {
			this.vector = new VectorTerm(new double[vector.length]);
		}
		for (int i = 0; i < this.vector.value.length; ++i) {
			this.vector.value[i] += vector[i];
		}		
	}
	
	private void subtractVector(double[] vector) {
		if (this.vector == null || this.vector.value.length == 0) {
			this.vector = new VectorTerm(new double[vector.length]);
		}
		for (int i = 0; i < this.vector.value.length; ++i) {
			this.vector.value[i] -= vector[i];
		}
	}
	
	private void fromScratch() {
		Collection<ATerm> vectors = ATerm.getCollection(parts[0]);
		if (this.vector != null) {
			Arrays.fill(this.vector.value, 0.0);
		}
		for (ATerm v : vectors) {
			addVector(ATerm.getVector(v));
		}
		if (this.vector == null) {
			this.vector = new VectorTerm(new double[0]);
		}
		passUpdate();
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
		
		String operation = (String)msg[0];
		ATerm element = (ATerm)msg[1];
		
		if (operation == OriginatingTermCollection.ADD) {
			addVector(ATerm.getVector(element));
		} else if (operation == OriginatingTermCollection.REM) {
			subtractVector(ATerm.getVector(element));
		} else {
			throw new Error("Unknown update: "+operation);
		}
		
		passUpdate();
	}

}
