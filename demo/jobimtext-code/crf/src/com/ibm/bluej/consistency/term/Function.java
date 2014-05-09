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
import java.util.List;

import com.ibm.bluej.consistency.focus.FocusIndicator;



public abstract class Function extends CompositeTerm implements Updatable,Cloneable {
	public static boolean FOR_GCC = false;
	
	//default constructor is required
	public Function() {}
	
	public int valueHash() {
		return getValue().valueHash();
	}
	public boolean valueEquals(ATerm t) {
		return getValue().valueEquals(t);
	}
	public ATerm valueClone() {
		//it should make the value do a true clone
		ATerm v = getValue();
		if (v instanceof ValueTerm) {
			return ((ValueTerm)getValue()).clone();
		}
		return v.valueClone();
	}
	
	public static final Function NO_LINK = new Function() {
		public ATerm getValue() {return null;}
		public void update(Function source, Object... msg) {
			throw new Error ("Updated the dummy NO_LINK "+source+" "+msg);
		}
		public String toString() {return "NO_LINK";}
	};
	
	//SANITY: usedBy for non-ground should be null
	//SANITY: a Function should only appear in the usedBy of it's arguments
	protected ArrayList<Updatable> usedBy = null;//new ArrayList<Function>();
	public List<Updatable> getUsedBy() {
		return usedBy;
	}
	
	public final void addUsedBy(Updatable neededBy) {
		if (neededBy != null && neededBy != NO_LINK) {
			usedBy.add(neededBy);
		}
	}
	
	public abstract ATerm getValue();
	
	//only called by Function.ground
	//we use clone because the subtype of the function must be retained
	protected Function clone() {
		try {
			Function f = (Function) super.clone();
			//need to copy parts
			f.parts = new ATerm[this.parts.length];
			return f;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	public boolean isFunctionFree() {
		return false; 
	}
	
	protected void bind(ATerm ground, Binds binds) {
		throw new UnsupportedOperationException("You cannot bind a function: is there a function in your ScanTerm?");
	}
	protected boolean matchesInner(ATerm ground, Binds binds) {
		throw new UnsupportedOperationException("You cannot bind a function: is there a function in your ScanTerm?");
	}
	
	//when a function is grounded it should update it's argument's usedBy
	//we also call update on the first grounding of a function
	//if a function is grounded with no function arguments it just returns its value NOT a function
	public ATerm ground(Binds binds, Updatable neededBy) {
		Function t = this.clone(); 
		Function subNeededBy = t;
		if (neededBy == NO_LINK) {
			subNeededBy = NO_LINK;
		}
		//only need to copy if at least one part is different...
		for (int i = 0; i < parts.length; ++i) {
			t.parts[i] = parts[i].ground(binds, subNeededBy);
		}
		//could be incompletely ground
		if (!t.isGround(null)) {
			return t;
		}
		coreGround(t,subNeededBy);
		
		//if it doesn't depend on any functions then return the value
		boolean funcFree = true;
		for (ATerm p : t.parts) {
			funcFree = funcFree && p.isFunctionFree();
		}
		if (funcFree) {
			return t.getValue();
		}
		t.addUsedBy(neededBy);

		return t;
	}
	
	protected void coreGround(Function t, Function subNeededBy) {
		t.usedBy = new ArrayList<Updatable>();
		try {
			t.update(null);
		} catch (RuntimeException e) {
			for (int i = 0; i < parts.length; ++i) {
				System.err.println("arg "+i+" = "+parts[i] +" -> "+t.parts[i]);
			}
			throw e;
		}
		
		if (focus != null && subNeededBy != NO_LINK) {focus.addDepend(t);}
	}
	
	//Initial grounding update message has null source
	public abstract void update(Function source, Object... msg);
	
	
	
	protected void passUpdate(Object... msg) {
		//base just calls update on every usedBy
		for (Updatable f : usedBy) {
			//Don't pass updates to partially grounded functions
			if (f.isReady()) {
				//recomputeStack.addRecompute(f, this, msg);
				f.update(this, msg);
			}
		}
	}
	//for the breadth first version
	//private static final FunctionRecomputeStack recomputeStack = new FunctionRecomputeStack();
	//public static void flush() { recomputeStack.flush();}
	
	
	//CONSIDER: remove focus indicators - we use smart functions now
	private FocusIndicator focus; 
	public void parserSetsFocusIndicator(FocusIndicator focus) {this.focus = focus;}
	public boolean isReady() {
		return usedBy != null //usedBy != null means it is fully grounded.
				&& (focus == null || focus.isActive()); //also need to check that it is within focus
	}
	
	public void zzParserInitUsedBy() {
		usedBy = new ArrayList<Updatable>();
	}
	
	//Identity hash and equals on functions
	//public int hashCode() {
	//	return System.identityHashCode(this);
	//}
	//public boolean equals(Object o) {
	//	return this == o;
	//}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\\"+this.getClass().getSimpleName());
		buf.append("(");
		if (parts == null) {
			buf.append("NULL");
		} else {
			for (int i = 0; i < parts.length; ++i) {
				if (i > 0) {
					buf.append(", ");
				}
				buf.append(parts[i]);
			}
		}
		buf.append(")");
		ATerm v = getValue();
		if (v != null && v != this) {
			buf.append("=").append(getValue());
		}
		return buf.toString();
	}
	


}
