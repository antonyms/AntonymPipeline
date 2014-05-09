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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.ibm.bluej.util.common.MutableInteger;


public class RandomSet<T> implements IRandomAccessible<T> {
	protected HashMap<T, MutableInteger> map; //CONSIDER Obj2Int HashMap
	protected ArrayList<T> array;
	
	public RandomSet(Collection<T> init) {
		map = new HashMap<T, MutableInteger>((int)(init.size()*0.75+1));
		array = new ArrayList<T>(init.size());
		for (T i : init) {
			add(i);
		}
	}
	
	public RandomSet() {
		map = new HashMap<T, MutableInteger>(40);
		array = new ArrayList<T>(40);
	}
	
	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(Object key) {
		return map.containsKey(key);
	}

	public Iterator<T> iterator() {
		return array.iterator();
	}

	public Object[] toArray() {
		return array.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return array.toArray(a);
	}

	@Override
	public boolean add(T e) {
		MutableInteger ndx = new MutableInteger(array.size());
		MutableInteger prev = map.put(e, ndx);
		if (prev != null) {
			ndx.value = prev.value;
			return false;
		}
		array.add(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		MutableInteger ndx = map.remove(o);
		if (ndx == null) {
			return false;
		}
		T last = array.remove(array.size()-1);
		if (ndx.value != array.size()) {
			array.set(ndx.value, last);
			map.get(last).value = ndx.value;
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object i : c) {
			if (!contains(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean addedOne = false;
		for (T i : c) {
			addedOne = add(i) || addedOne;
		}
		return addedOne;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removedOne = false;
		for (Object i : c) {
			removedOne = remove(i) || removedOne;
		}
		return removedOne;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		map.clear();
		array.clear();
	}

	@Override
	public T getRandom(Random r) {
		if (array.isEmpty()) {
			return null;
		}
		return array.get(r.nextInt(array.size()));
	}


}
