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

import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.formula.FormulaCreate;
import com.ibm.bluej.consistency.validate.SGDebug;


public class FunctionConditional extends Function implements FormulaCreate {
	
	boolean active;
	FormulaCreate wrappedCreate;
	boolean[] positive;
	boolean currentTruth = false;
	private Object unlink;
	
	private Formula forDefineDebug;
	
	//created only by Formula.groundOut when checkTerms are found to depend on binds in the function
	public FunctionConditional(FormulaCreate wrappedCreate, Formula f, Collection<Integer> conditions, Binds binds) {
		this.wrappedCreate = wrappedCreate;
		this.positive = new boolean[conditions.size()];
		this.parts = new ATerm[conditions.size()];
		int ndx = 0;
		if (SGDebug.FUNC_NOLEAK && binds.popUnlinks() != null) {
			throw new Error("Already have stuff linked in FunctionConditional!");
		}
		for (Integer i : conditions) {
			this.parts[ndx] = f.condition[i].ground(binds, this);
			this.positive[ndx] = f.isPositive(i);
			++ndx;
		}
		unlink = binds.popUnlinks();
		forDefineDebug = f;
	}
	
	public double getWeight() {
		return 0;
	}
	
	public boolean isReady() {
		return true;
	}
	
	public void drop() {
		if (active) {
			active = false;
			wrappedCreate.drop(); //NOTE: this may already be dropConditional
			wrappedCreate = null;
			Binds.unlink(unlink);
			unlink = null;
			parts = null;
			positive = null;
			forDefineDebug = null;
		}
	}
	
	public void update(Function source, Object... msg) {
		if (!active) {
			throw new Error("Called update on inactive FunctionConditional: "+this.toString()+" this is a Function memory leak");
		}
		
		//recompute truth
		// false -> true => wrappedCreate.add
		// true -> false => wrappedCreate.drop
		boolean truth = true;
		for (int i = 0; i < parts.length; ++i) {
			truth = truth && ((CheckTerm)parts[i]).getValue().value;
		}
		if (currentTruth && !truth) {
			currentTruth = truth;
			wrappedCreate.dropConditional();
		} else if (!currentTruth && truth) {
			currentTruth = truth;
			wrappedCreate.add(forDefineDebug);
		}
		
		//it never has a usedBy, so never passUpdate
		assert(usedBy.size() == 0);
	}


	public void add(Formula origin) {
		if (!active) {
			active = true;
			update(null);
		}
	}


	public void dropConditional() {
		throw new Error("Cannot have a conditional function conditional!");
	}

	public boolean isGarbage() {
		return wrappedCreate == null;
	}

	public ATerm getValue() {
		throw new UnsupportedOperationException();
	}
	public ATerm ground(Binds binds, Function neededBy) {
		throw new UnsupportedOperationException();
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Conditional: IF ");
		for (int i = 0; i < parts.length; ++i) {
			if (i > 0) {
				buf.append(" ^ ");
			}
			if (!positive[i]) {
				buf.append("!");
			}
			buf.append(parts[i]);
		}
		buf.append(" THEN ").append(wrappedCreate);
		return buf.toString();
	}
}
