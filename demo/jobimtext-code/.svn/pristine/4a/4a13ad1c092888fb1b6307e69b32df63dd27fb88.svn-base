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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.ibm.bluej.util.common.FunST;



//essentially ArrayList<S>, but before each add that would cause a grow, scans element data and calls shouldClean
//unneeded elements are removed
public class CleaningCollection<S> implements Iterable<S> {
	FunST<S,Boolean> shouldClean;
	private Object[] elementData;
	private int size = 0;
	
	public CleaningCollection(FunST<S,Boolean> shouldClean) {
		this.shouldClean = shouldClean;
	}
	
	public int size() {
		return size;
	}
	
	public final void clear() {
		if (elementData != null) {
			Arrays.fill(elementData, null);
		}
		size = 0;
	}
	
	public final void clean() {
		for (int i = 0; i < size; ++i) {
			if (shouldClean.f((S)elementData[i])) {
				removeReorder(i);
				--i;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void add(S obj) {
		if (elementData == null) {
			elementData = new Object[8];
		}
		if (size == elementData.length) {
			clean();
			if (size == elementData.length) {
				//increase by 50%
				elementData = Arrays.copyOf(elementData, elementData.length + (elementData.length >> 1));
			}
		}
		elementData[size++] = obj;
	}
	
	public void removeReorder(int ndx) {
		elementData[ndx] = elementData[size-1];
		--size;
	}
	
	
    public Iterator<S> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<S> {
        int ndx = 0;

        public boolean hasNext() {
            return ndx < size;
        }

        @SuppressWarnings("unchecked")
        public S next() {
            if (ndx >= size)
                throw new NoSuchElementException();
            return (S) elementData[ndx++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }	
}
