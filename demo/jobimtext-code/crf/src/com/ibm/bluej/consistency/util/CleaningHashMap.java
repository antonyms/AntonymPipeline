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

package com.ibm.bluej.consistency.util;

import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.util.common.FunST;



public class CleaningHashMap<K,V> extends java.util.HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	private static final int MIN_PUTS_BEFORE_CLEAN = 1000;
	
	int nextCleanIn = MIN_PUTS_BEFORE_CLEAN;
	
	FunST<V,Boolean> isGarbage;
	
	public CleaningHashMap(FunST<V,Boolean> isGarbage) {
		super();
		this.isGarbage = isGarbage;
	}
	public CleaningHashMap() {
		throw new Error("Must supply a cleaning function");
	}
	
	public void clean() {
		Iterator<Map.Entry<K, V>> it = entrySet().iterator();
		while (it.hasNext()) {
			if (isGarbage.f(it.next().getValue())) {
				it.remove();
			}
		}
	}
	
	public V put(K key, V value) {
		//not ideal but the fields of capacity and threshold are not visible (wish they were protected instead)
		if (nextCleanIn <= 0) {
			clean();
			nextCleanIn = Math.min(size(), MIN_PUTS_BEFORE_CLEAN);
		}
		--nextCleanIn;
		return super.put(key, value);
	}
}
