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

package com.ibm.bluej.consistency.term.corefunc;

import java.util.Collection;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.CheckTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.StringTerm;


public class FuncEquals extends CheckTerm {

	static boolean areEqual(ATerm t1, ATerm t2) {
		if (t1 instanceof Collection) {
			throw new UnsupportedOperationException(
					"Equality between bags/sets is not currently implemented. " +
					"You should use set difference anyway so you can get a gradient.");
			/*
			Collection<Term> tc1 = Term.getCollection(t1);
			Collection<Term> tc2 = Term.getCollection(t1);
			if (tc1.size() != tc2.size()) {
				return false;
			}
			//TODO: doesn't work right now
			return (tc1.containsAll(tc2));
			*/
		}
		if (t1 instanceof Function) {
			t1 = ((Function)t1).getValue();
		}
		if (t1 instanceof NumberTerm) {
			return ATerm.getDouble(t1) == ATerm.getDouble(t2);
		} else if (t1 instanceof StringTerm) {
			return ATerm.getString(t1).equals(ATerm.getString(t2));
		} else if (t1 instanceof BooleanTerm) {
			return ATerm.getBoolean(t1) == ATerm.getBoolean(t2);
		} else {
			throw new Error("Cannot test equality with "+t1+" and "+t2);
		}
	}
	
	public void update(Function source, Object... msg) {
		assert(parts.length == 2);
		BooleanTerm prevTruth = truth;
		truth = areEqual(parts[0],parts[1]) ? BooleanTerm.TRUE : BooleanTerm.FALSE;
		if (!truth.equals(prevTruth)) {
			passUpdate();
		}
	}

	public String toString() {
		if (parts.length == 2) {
			return "("+parts[0]+" = "+parts[1]+")";
		}
		return "USAGE ERROR: "+super.toString();	
	}
}
