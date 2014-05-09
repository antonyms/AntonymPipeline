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

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.SparseVectorTerm;
import com.ibm.bluej.util.common.DenseVectors;
import com.ibm.bluej.util.common.MutableDoubleHashMap;


public class FuncCosine extends SimpleNumberFunction {

	protected double compute() {
		//if there are three args, the third is the size penalty
		double sizePenalty = 0;
		if (parts.length == 3) {
			sizePenalty = ATerm.getDouble(parts[2]);
		}
		if (ATerm.getTerm(parts[0]) instanceof SparseVectorTerm) {
			return MutableDoubleHashMap.cosineSimilarityAlt(
				ATerm.getSparseVector(parts[0]), 
				ATerm.getSparseVector(parts[1]), 
				sizePenalty); 
		}
		
		double[] v1 = ATerm.getVector(parts[0]);
		double[] v2 = ATerm.getVector(parts[1]);
		if (v1 == null || v1.length == 0 || v2 == null || v2.length == 0) {
			return 0;
		}
		return DenseVectors.cosine(v1,v2);
	}

}
