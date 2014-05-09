/**
 *   Copyright (c) 2012 IBM Corp.
 *   
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *         
 *   http://www.apache.org/licenses/LICENSE-2.0
 *               	
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *               	               
 *   @author: Bonaventura Coppola (coppolab@gmail.com)
 *   
 */

package com.ibm.sai.dca.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMemory<K, V> {

	static final float LOAD_FACTOR = (float) 0.75;
	static final boolean ACCESS_ORDER = true;
	
	private Map<K, V> map;
	
	protected int cacheMemorySize;

	@SuppressWarnings("unused")
	private CacheMemory() {
	}

	public CacheMemory(int cacheMemorySize) {
		this.cacheMemorySize = cacheMemorySize;
		map = Collections.synchronizedMap(new CacheMap(cacheMemorySize, LOAD_FACTOR, ACCESS_ORDER));
	}
	
	public V get(K key) {
		return map.get(key);
	}
	
	public void put(K key, V value) {
		map.put(key,  value);
	}
	
	@SuppressWarnings("serial")
	private class CacheMap extends LinkedHashMap<K, V>
	{
		private int maxEntries;
		
		public CacheMap(int maxEntries, float loadFactor, boolean accessOrder) {
			super(maxEntries, loadFactor, accessOrder);
			this.maxEntries = maxEntries;
		}
		
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			return size() > maxEntries;
		}
	}
}
