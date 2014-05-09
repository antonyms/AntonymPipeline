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

import java.util.Random;

public class RandArray<T> extends java.util.ArrayList<T> implements IRandomAccessible<T> {
	private static final long serialVersionUID = 1L;

	//hack to make remove in FormulaFindGroundAdaptive fast
	public boolean add(T e) {
		if (this.isEmpty()) {
			return super.add(e);
		}
		super.add(this.get(0));
		this.set(0, e);
		return true;
	}
	
	public T getRandom(Random r) {	
		if (size() == 0) {
			return null;
		}
		return get(r.nextInt(size()));
	}

}
