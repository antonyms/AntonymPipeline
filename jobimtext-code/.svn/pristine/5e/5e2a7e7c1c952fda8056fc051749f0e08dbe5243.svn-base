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

package com.ibm.bluej.consistency.term.corefunc;

import java.util.HashMap;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.Updatable;

/**
 * Use at own risk, not well tested
 * @author mrglass
 *
 */
public class FuncMemoized extends Function {
	private static HashMap<MemoizedKey, ATerm> memoized = new HashMap<MemoizedKey, ATerm>();
	
	public static void clear() {
		memoized.clear();
	}
	
	private static MemoizedKey probe = new MemoizedKey();
	private static ATerm getMemoized(FuncMemoized f) {
		probe.funcClass = f.parts[0].getClass();
		probe.parts = f.parts;
		ATerm res = memoized.get(probe);
		if (res == null) {
			Function inner = (Function)f.parts[0];
			inner.parts = new ATerm[f.parts.length-1];
			for (int i = 1; i < f.parts.length; ++i) {
				inner.parts[i-1] = f.parts[i];
			}
			inner.update(null);
			res = inner.getValue(); 
			inner.parts = CompositeTerm.EMPTY_TERM;
			memoized.put(new MemoizedKey(f.parts), inner.valueClone());
		}
		return res;
	}
	
	private static class MemoizedKey {
		MemoizedKey() {}
		
		MemoizedKey(ATerm[] fparts) {
			funcClass = fparts[0].getClass();
			parts = new ATerm[fparts.length];
			for (int i = 1; i < fparts.length; ++i) {
				parts[i] = fparts[i].valueClone();
			}
		}
		
		Class funcClass;
		ATerm[] parts; //valueHash, valueEquals, constructed with valueClone
		
		//duplicate the logic of the ParamWeight hashtable
		
		public int hashCode() {
			int h = ATerm.mdjbFirst();
			h = ATerm.mdjbNext(h, funcClass.hashCode());
			for (int i = 1; i < parts.length; ++i) {
				h = ATerm.mdjbNext(h, parts[i].valueHash());
			}
			return h;
		}
		public boolean equals(Object o) {
			if (!(o instanceof MemoizedKey)) {
				return false;
			}
			MemoizedKey mk = (MemoizedKey)o;
			if (funcClass != mk.funcClass) {
				return false;
			}
			if (parts.length != mk.parts.length) {
				return false;
			}
			for (int i = 1; i < parts.length; ++i) {
				if (!parts[i].valueEquals(mk.parts[i])) {
					return false;
				}
			}
			return true;
		}
	}
	
	private ATerm value;
	
	@Override
	public ATerm getValue() {
		return value;
	}

	@Override
	public void update(Function source, Object... msg) {
		value = getMemoized(this);
	}

	//changed to skip the first arg (the function variable)
	public ATerm ground(Binds binds, Updatable neededBy) {
		Function t = this.clone(); 
		Function subNeededBy = t;
		if (neededBy == NO_LINK) {
			subNeededBy = NO_LINK;
		}
		t.parts[0] = parts[0];
		//only need to copy if at least one part is different...
		for (int i = 1; i < parts.length; ++i) {
			t.parts[i] = parts[i].ground(binds, subNeededBy);
		}
		//could be incompletely ground
		if (!t.isGround(null)) {
			return t;
		}
		coreGround(t,subNeededBy);
		
		//if it doesn't depend on any functions then return the value
		boolean funcFree = true;
		for (int i = 1; i < t.parts.length; ++i) {
			funcFree = funcFree && t.parts[i].isFunctionFree();
		}
		if (funcFree) {
			return t.getValue();
		}
		t.addUsedBy(neededBy);

		return t;
	}
}
