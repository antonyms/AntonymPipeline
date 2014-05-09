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

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandSingle<T> implements IRandomAccessible<T> {

	private T obj;
	
	public int size() {
		return obj == null ? 0 : 1;
	}

	public boolean isEmpty() {
		return obj == null;
	}

	public boolean contains(Object o) {
		return (o != null && obj != null && (o == obj || o.equals(obj)));
	}

	public Iterator<T> iterator() {
		return new SingleObjIterator<T>(obj);
	}

	public Object[] toArray() {
		if (obj == null) {
			return new Object[0];
		}
		return new Object[] {obj};
	}

	public boolean add(T e) {
		if (obj != null) 
			throw new UnsupportedOperationException("Cannot have more than one element in a RandSingle");
		obj = e;
		return true;
	}

	public boolean remove(Object o) {
		if (obj != null && obj.equals(o)) {
			obj = null;
			return true;
		}
		return false;
	}

	public void clear() {
		obj = null;
	}

	public T getRandom(Random r) {
		return obj;
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		return c.isEmpty() || (c.size() == 1 && c.iterator().next().equals(obj));
	}

	public boolean addAll(Collection<? extends T> c) {
		if (c.size() + this.size() > 1) {
			throw new UnsupportedOperationException();
		}
		if (c.isEmpty()) {
			return false;
		}
		obj = c.iterator().next();
		return true;
	}

	public boolean removeAll(Collection<?> c) {
		if (obj != null && c.contains(obj)) {
			obj = null;
			return true;
		}
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		if (obj == null || c.contains(obj)) {
			return false;
		}
		obj = null;
		return true;
	}

}
