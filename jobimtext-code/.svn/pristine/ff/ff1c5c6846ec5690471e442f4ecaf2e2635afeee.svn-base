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

public class SingleObjIterator<T> implements Iterator<T> {
	private T obj;
	public SingleObjIterator(T obj) {
		this.obj = obj;
	}
	public boolean hasNext() {
		return obj != null;
	}
	public T next() {
		T toRet = obj;
		obj = null;
		return toRet;
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}	
}
