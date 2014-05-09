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

package com.ibm.bluej.consistency.term;

import java.util.Collection;

public abstract class TermCollection extends Function implements Collection<ATerm> {
	public int valueHash() {
		throw new UnsupportedOperationException("TermCollection cannot be used in valueHash/valueEquals/valueClone");
	}
	public boolean valueEquals(ATerm t) {
		throw new UnsupportedOperationException("TermCollection cannot be used in valueHash/valueEquals/valueClone");
	}
	public ATerm valueClone() {
		throw new UnsupportedOperationException("TermCollection cannot be used in valueHash/valueEquals/valueClone");
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public Object[] toArray() {
		throw new UnsupportedOperationException("not implemented");
	}
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("not implemented");
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("not implemented");
	}
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("no modification allowed!");
	}
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("no modification allowed!");
	}
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("no modification allowed!");
	}
	public boolean add(ATerm e) {
		throw new UnsupportedOperationException("no modification allowed!");
	}
	public boolean addAll(Collection<? extends ATerm> c) {
		throw new UnsupportedOperationException("no modification allowed!");
	}
	public void clear() {
		throw new UnsupportedOperationException("no modification allowed!");
	}


	public ATerm getValue() {
		return this;
	}
	

}
