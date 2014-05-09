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

import java.util.Iterator;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.util.SingleObjIterator;


public class ExSucc extends ExpandTerm {

	public int estimateExpand(Binds binds) {
		boolean expandable = true;
		if (!parts[1].isGround(binds) && parts[1] instanceof VarTerm) {
			binds.quickCheckPoint();
			expandable = parts[0].ground(binds, Function.NO_LINK) instanceof NumberTerm;
			binds.quickRollback();
		} else if (!parts[0].isGround(binds) && parts[0] instanceof VarTerm) {
			binds.quickCheckPoint();
			expandable = parts[1].ground(binds, Function.NO_LINK) instanceof NumberTerm;
			binds.quickRollback();
		}
		return expandable ? 1 : Integer.MAX_VALUE;
	}

	public Iterator<Binds> expand(Binds binds) {
		Binds exBind = new Binds(binds.getQuicksave());
		if (!parts[1].isGround(binds)) {
			exBind.set((VarTerm)parts[1], 
					new NumberTerm(ATerm.getDouble(parts[0].ground(binds, Function.NO_LINK)) + 1.0));
		} else {
			exBind.set((VarTerm)parts[0], 
					new NumberTerm(ATerm.getDouble(parts[1].ground(binds, Function.NO_LINK)) - 1.0));
		}
		//SGSearch.logFine("Expand: "+this.toString()+" with "+exBind+" added to "+binds);
		return new SingleObjIterator<Binds>(exBind);
	}

	public void update(Function source, Object... msg) {
		BooleanTerm prevTruth = truth;
		truth = ATerm.getDouble(parts[0]) + 1.0 == ATerm.getDouble(parts[1]) ? BooleanTerm.TRUE : BooleanTerm.FALSE;
		//SGSearch.logFine("Check: "+this.toString());
		if (!truth.equals(prevTruth)) {
			passUpdate();
		}
	}

}
