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

package com.ibm.bluej.consistency.old;

import java.util.IdentityHashMap;
import java.util.Map;

import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.Updatable;

//Initial tests showed this is slower than using the depth first
public class FunctionRecomputeStack {
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
	
	
	private static final Object[] FROM_SCRATCH = new Object[0];
	private static class StackElement {
		StackElement(Updatable target, Function source, Object[] message) {
			this.target = target;
			this.source = source;
			this.message = message;
		}
		Updatable target;
		Function source;
		Object[] message;
		StackElement prev;
		StackElement next;
		public String toString() {
			String str = super.toString();
			return str.substring(str.lastIndexOf('@')+1);
		}
	}
	private StackElement first, last;
	private IdentityHashMap<Updatable, StackElement> map = new IdentityHashMap<Updatable, StackElement>();
	
	public void addRecompute(Updatable target, Function source, Object[] msg) {
		try {
		StackElement existing = map.get(target);
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
			existing.source = null;
			existing.message = FROM_SCRATCH;
		} else {
			StackElement fresh = new StackElement(target,source,msg);
			//add to front
			if (first == null) {
				first = fresh;
				last = fresh;
			} else {
				first.prev = fresh;
				fresh.next = first;
				first = fresh;
			}
			map.put(target, fresh);
		}
		} catch (Exception e) {
			e.printStackTrace();
			printDebug();
			throw new Error(e);
		}
	}

	private StackElement pop() {
		StackElement popped = first;
		if (first != null) {
			first = first.next;
			if (first != null)
				first.prev = null;
			map.remove(popped.target);
		}
		return popped;
	}
	
	public void flush() {
		while (first != null) {
			StackElement current = pop();
			current.target.update(current.source, current.message);
		}
	}
	
	private void printDebug() {
		System.out.println("FIRST "+first);
		System.out.println("LAST  "+last);
		for (Map.Entry<Updatable, StackElement> e : map.entrySet()) {
			StackElement s = e.getValue();
			System.out.println(s.prev +" -> "+s+" -> "+s.next);
		}
	}
}
