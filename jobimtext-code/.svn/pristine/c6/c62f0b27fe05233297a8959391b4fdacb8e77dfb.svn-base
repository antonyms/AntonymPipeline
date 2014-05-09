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

package com.ibm.bluej.consistency.term;

import java.util.Collection;
import java.util.Map;

import com.ibm.bluej.consistency.term.corefunc.NumberFunction;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.MutableDouble;



public abstract class ATerm implements Comparable<ATerm> {

	//CONSIDER: this version should be protected abstract
	//the public version should call this with null except when the Term is a Function
	public abstract ATerm ground(Binds binds, Updatable neededBy);
	
	public abstract boolean isGround(Binds binds);
	
	public abstract boolean valueEquals(ATerm t);
	public abstract int valueHash();
	public abstract ATerm valueClone();
	
	//assumes that binding will succeed
	protected abstract void bind(ATerm ground, Binds binds);
	
	//binds the variables in this Term to the proper Terms in ground, adds them to binds
	//returns false if failure and DOES NOT rollback binds
	protected abstract boolean matchesInner(ATerm ground, Binds binds);
	
	//default
	public boolean isFunctionFree() {
		return true;
	}
	
	public static int getInt(ATerm t) {
		return (int)getDouble(t);
	}
	
	public static double getDouble(ATerm t) {
		if (t instanceof NumberFunction) {
			return ((NumberFunction) t).value.value;
		} else if (t instanceof NumberTerm) {
			return ((NumberTerm) t).value;
		}
		try {
			return ((NumberTerm) ((Function) t).getValue()).value;
		} catch (ClassCastException e) {
			System.err.println("Bad cast: "+t);
			throw e;
		}
	}
	public static double[] getVector(ATerm t) {
		if (t instanceof Function) {
			return ((VectorTerm)((Function)t).getValue()).value;
		}
		return ((VectorTerm)t).value;
	}
	public static Map<Object,MutableDouble> getSparseVector(ATerm t) {
		if (t instanceof Function) {
			return ((SparseVectorTerm)((Function)t).getValue()).value;
		}
		return ((SparseVectorTerm)t).value;
	}
	public static String getString(ATerm t) {
		if (t instanceof Function) {
			return ((StringTerm)((Function)t).getValue()).value;
		}
		return ((StringTerm)t).value;
	}
	public static boolean getBoolean(ATerm t) {
		if (t instanceof Function) {
			return ((BooleanTerm)((Function)t).getValue()).value;
		}
		return ((BooleanTerm)t).value;
	}
	public static Collection<ATerm> getCollection(ATerm t) {
		if (t instanceof Function) {
			return (Collection<ATerm>)((Function)t).getValue();
		}
		return ((Collection<ATerm>)t);
	}
	public static ATerm getTerm(ATerm t) {
		if (t instanceof Function) {
			return ((Function)t).getValue();
		}
		return t;
	}
	
	/*
	public static int mdjbHashByte(int[] p) {
		int h = 5381;
		for (int pi : p) {
			h = 33 * h ^ ( (pi << 24) >> 24 );
			h = 33 * h ^ ( (pi << 16) >> 24 );
			h = 33 * h ^ ( (pi << 8) >> 24 );
			h = 33 * h ^ (  pi >> 24 );
		}
		return h;
	}
	*/
	//by far the best
	public static int mdjbFirst() {
		return 5381;
	}
	public static int mdjbNext(int h, int pi) {
		h = 33 * h ^ ( (pi << 24) >> 24 );
		h = 33 * h ^ ( (pi << 16) >> 24 );
		h = 33 * h ^ ( (pi << 8) >> 24 );
		h = 33 * h ^ (  pi >> 24 );
		return h;
	}
	
	public static boolean testRecursive(ATerm t, FunST<ATerm, Boolean> test) {
		if (test.f(t)) {
			return true;
		}
		if (t instanceof CompositeTerm) {
			for (ATerm p : ((CompositeTerm)t).parts) {
				if (testRecursive(p, test)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void gatherTopFunctions(ATerm t, Collection<Function> funs) {
		if (t instanceof Function) {
			funs.add((Function)t);
			return;
		}
		if (t instanceof CompositeTerm) {
			assert (t instanceof ScanTerm);
			for (ATerm p : ((CompositeTerm)t).parts) {
				gatherTopFunctions(p, funs);
			}
		}
	}
	
	public static int crossClassCompare(ATerm first, ATerm second) {
		return first.getClass().getName().compareTo(second.getClass().getName());
	}
}
