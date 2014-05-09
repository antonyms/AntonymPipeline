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

package com.ibm.bluej.consistency.util;

import java.util.IdentityHashMap;
import java.util.Map;

//Initial tests showed this is slower than using the depth first
public class RecomputeStack<Key,Value> {
	/*
	  breadth first function eval - hard to know which order will work out to "breadth first"
		use a recompute stack?
		at the acceptance function, process the recompute stack to completion
		so passUpdate just adds a message to the recompute stack
		  which is also a bit of a map
		  a stack entry will be a dest/message pair
		  or dest/messageList
		if there is any from_scratch message or "too many" incremental messages
		they are all compressed into a single from_scratch message
	 */
	
	public static class StackElement<Key,Value> {
		private StackElement(Key key, Value value) {
			this.key = key;
			this.value = value;
		}
		public Key key;
		public Value value;
		private StackElement<Key,Value> prev;
		private StackElement<Key,Value> next;
		public String toString() {
			String str = super.toString();
			return str.substring(str.lastIndexOf('@')+1);
		}
	}
	private StackElement<Key,Value> first, last;
	private IdentityHashMap<Key, StackElement<Key,Value>> map = new IdentityHashMap<Key, StackElement<Key,Value>>();
	
	/**
	 * Complex operation, if the key is not present, it will create at the beginning of the stack, a new element with the value
	 * If the key is present, it will be pushed to the bottom of the stack and the existing element is returned unchanged
	 * @param key
	 * @param value 
	 * @return The existing mapping from key if present, null if key did not exist prior to the call
	 */
	public StackElement<Key,Value> push(Key key, Value value) {
		try {
		StackElement<Key,Value> existing = map.get(key);
		if (existing != null) {
			//shove to end
			if (last != existing) {
				//cut out of chain
				if (existing.prev != null)
					existing.prev.next = existing.next;
				else 
					first = existing.next;
				existing.next.prev = existing.prev;
				
				//put at end
				existing.prev = last;
				existing.next = null;
				last.next = existing;
				last = existing;	
			}
			//existing.value = value;
			return existing;
		} else {
			StackElement<Key,Value> fresh = new StackElement<Key,Value>(key,value);
			//add to front
			if (first == null) {
				first = fresh;
				last = fresh;
			} else {
				first.prev = fresh;
				fresh.next = first;
				first = fresh;
			}
			map.put(key, fresh);
			return null;
		}
		} catch (Exception e) {
			e.printStackTrace();
			printDebug();
			throw new Error(e);
		}
	}

	/**
	 * Pops the element on the top of the stack, removing it. null if empty
	 * @return
	 */
	public StackElement<Key,Value> pop() {
		StackElement<Key,Value> popped = first;
		if (first != null) {
			first = first.next;
			if (first != null)
				first.prev = null;
			map.remove(popped.key);
		}
		return popped;
	}
	
	private void printDebug() {
		System.out.println("FIRST "+first);
		System.out.println("LAST  "+last);
		for (Map.Entry<Key, StackElement<Key,Value>> e : map.entrySet()) {
			StackElement<Key,Value> s = e.getValue();
			System.out.println(s.prev +" -> "+s+" -> "+s.next);
		}
	}
}
