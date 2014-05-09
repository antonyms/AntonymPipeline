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

import java.util.ArrayList;
import java.util.Arrays;

public class Binds {
	public static final int MIN_BIND_LEN = 4;
	private ArrayList<Updatable> unlink;
	private ATerm[] bound = new ATerm[MIN_BIND_LEN];
	//TODO: hold reference to the CRFDescription's quickRollBack
	
	private ATerm[] quicksave;
	public Binds(ATerm[] quicksave) {
		this.quicksave = quicksave;
	}
	
	//NOTE: only called by VarTerm
	public void addFunctionUnlink(ATerm b, Updatable neededBy) {
		if (neededBy == null || neededBy == Function.NO_LINK) {
			return;
		}
		addFunctionUnlinkInternal(b, neededBy);
	}
	
	private void addFunctionUnlinkInternal(ATerm b, Updatable neededBy) {
		if (b instanceof Function) {
			ArrayList<Updatable> usedBy = ((Function)b).usedBy;
			if (usedBy.isEmpty()) {
				usedBy.add(neededBy);
			} else {
				//add first for faster rollbacks
				usedBy.add(usedBy.get(0));
				usedBy.set(0, neededBy);
			}
			addUnlink((Function)b, neededBy);
			return;
		}
		if (b instanceof CompositeTerm) {
			assert (b instanceof ScanTerm);
			for (ATerm p : ((CompositeTerm)b).parts) {
				addFunctionUnlinkInternal(p, neededBy);
			}
		}
	}
	private void addUnlink(Function used, Updatable needs) {
		if (unlink == null) {
			unlink = new ArrayList<Updatable>();
		}
		unlink.add(used);
		unlink.add(needs);
	}
	//Called only when a Create is constructed
	public Object popUnlinks() {
		Object u = unlink;
		unlink = null;
		return u;
	}
	//Called only in the drop of a Create
	public static void unlink(Object u) {
		if (u != null) {
			ArrayList<Updatable> unlink = (ArrayList<Updatable>)u;
			for (int i = 0; i < unlink.size(); i+=2) {
				unlink((Function)unlink.get(i), unlink.get(i+1));
			}
		}
	}
	public static void unlink(Function used, Updatable needs) {
		used.usedBy.remove(needs);
	}

	public void expand(Binds be) {
		for (int i = 0; i < be.bound.length; ++i) {
			if (be.bound[i] != null) {
				setInternal(i, be.bound[i]);
			}
		}
	}
	
	public Binds copy() {
		Binds fb = new Binds(quicksave);
		for (int i = 0; i < bound.length; ++i) {
			if (bound[i] != null) {
				fb.setInternal(i, bound[i]);
			}
		}
		return fb;
	}
	
	public final boolean contains(ATerm t) {
		for (ATerm b : bound) {
			if (b == t) {
				return true;
			}
		}
		return false;
	}
	
	/*
	//TODO: this implementation is wrong
	//  The goal should be to remove the "bottom level" of Functions not in the binds
	//  from the top level of Functions in the binds	
	//NOTE: the common case is that there is nothing to be done here!
	public void removeUsedBys(Term t) {
		//first check if t is in bound, if so return
		if (contains(t)) {
			return;
		}

		//check if t is composite, call on each part
		if (t instanceof CompositeTerm) {
			//check if t is a function, remove it from every usedBy list
			if (t instanceof Function) {
				for (Term b : bound) {
					//TODO: not sufficient - a bind could be a composite term that contains a function
					//TODO: make abstract function on CompositeTerm
					if (b instanceof Function) {
						((Function) b).usedBy.remove(t);
					}
				}
			}
			
			for (Term p : ((CompositeTerm)t).parts) {
				removeUsedBys(p);
			}
		}
		//for every function in t (not in bound), remove it from the usedBys for every function in the binds
		//not all will be present	
	}
	*/
	
	
	private void grow(int to) {
		//increase by at least 50%
		int newCap = Math.max(to, bound.length + (bound.length >> 1));
		bound = Arrays.copyOf(bound, newCap);
	}
	
	public void clear() {
		Arrays.fill(bound, null);
	}
	
	public ATerm get(VarTerm v) {
		if (v.varNum >= bound.length) {
			return null;
		}
		return bound[v.varNum];
	}
	private final void setInternal(int v, ATerm t) {
		if (v >= bound.length) {
			grow(v+1);
		}
		bound[v] = t;
	}
	public void set(VarTerm v, ATerm t) {
		setInternal(v.varNum, t);
	}
	
	public Object checkpoint() {
		//CONSIDER: never going to be more than 32 vars right
		boolean[] cpBound = new boolean[bound.length];
		for (int i = 0; i < bound.length; ++i) {
			cpBound[i] = bound[i] != null;
		}
		return cpBound;
	}
	public void rollback(Object o) {
		if (o instanceof boolean[]) {
			boolean[] cpBound = (boolean[])o;
			for (int i = 0; i < cpBound.length; ++i) {
				if (!cpBound[i]) {
					bound[i] = null;
				}
			}
			for (int i = cpBound.length; i < bound.length; ++i) {
				bound[i] = null;
			}
		} else {
			throw new Error("unexpected rollback object "+o);
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bound.length; ++i) {
			if (bound[i] != null) {
				if (buf.length() > 0) {
					buf.append("; ");
				}
				buf.append(i).append("->").append(bound[i]);
			}
		}
		return buf.toString();
	}
	
	//versions of these that use a static checkpoint avoiding the need for fancy object creation
	
	public void quickCheckPoint() {
		System.arraycopy(bound, 0, quicksave, 0, bound.length);
		Arrays.fill(quicksave, bound.length, quicksave.length, null);
	}
	public void quickRollback() {
		System.arraycopy(quicksave, 0, bound, 0, bound.length);
	}
	public ATerm[] getQuicksave() {
		return quicksave;
	}
	/*
	private static ATerm[] quicksave = new ATerm[32]; 
	//SGSParser inits this for a tighter (and safer) bound
	public static void zzForParserToSetMaxVars(int maxVars) {
		//System.out.println("Maximum vars is "+maxVars);
		//check Binds.grow for why this is a tight bound
		quicksave = new ATerm[Math.max(MIN_BIND_LEN, maxVars+(maxVars>>1))];
	}
	*/
	/*
	private static boolean[] quicksave = new boolean[4];
	public void quickishCheckPoint() {
		if (bound.length > quicksave.length) {
			quicksave = new boolean[bound.length];
		}
		
		for (int i = 0; i < bound.length; ++i) {
			quicksave[i] = bound[i] != null;
		}
		Arrays.fill(quicksave, bound.length, quicksave.length, false);
	}
	public void quickishRollack() {
		for (int i = 0; i < bound.length; ++i) {
			if (!quicksave[i]) {
				bound[i] = null;
			}
		}
		Arrays.fill(bound, quicksave.length, bound.length, null);
	}
	*/

}
