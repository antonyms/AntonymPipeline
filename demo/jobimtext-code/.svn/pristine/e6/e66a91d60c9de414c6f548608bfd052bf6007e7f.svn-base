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


public class ExInside extends ExpandTerm {
	//arg0 is between [arg1, arg2] inclusive
	
	
	public int estimateExpand(Binds binds) {
		if (parts[1].isGround(binds) && parts[2].isGround(binds) && parts[0] instanceof VarTerm) {
			binds.quickCheckPoint();
			ATerm minL = parts[1].ground(binds, Function.NO_LINK);
			ATerm maxL = parts[2].ground(binds, Function.NO_LINK);
			if (minL instanceof Function || maxL instanceof Function) {
				return Integer.MAX_VALUE;
			}
			double minLv = ATerm.getDouble(minL);
			double maxLv = ATerm.getDouble(maxL);
			binds.quickRollback();
			return (int)(maxLv - minLv);
		}
		return Integer.MAX_VALUE;
	}

	private static class ExIt implements Iterator<Binds> {
		Binds binds;
		double current;
		double max;
		VarTerm v;
		
		ExIt(ATerm[] quicksave, VarTerm v, double min, double max) {
			this.v = v;
			this.binds = new Binds(quicksave);
			this.current = min;
			this.max = max;
		}
		
		public boolean hasNext() {
			return current <= max;
		}

		public Binds next() {
			binds.set(v, new NumberTerm(current));
			++current;
			return binds;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public Iterator<Binds> expand(Binds binds) {
		return new ExIt(
				binds.getQuicksave(),
				(VarTerm)parts[0], 
				ATerm.getDouble(parts[1].ground(binds, Function.NO_LINK)), 
				ATerm.getDouble(parts[2].ground(binds, Function.NO_LINK)));
	}

	public void update(Function source, Object... msg) {
		double in = ATerm.getDouble(parts[0]);
		double minL = ATerm.getDouble(parts[1]);
		double maxL = ATerm.getDouble(parts[2]);
		
		BooleanTerm prevTruth = truth;
		truth = (in >= minL && in <= maxL) ? BooleanTerm.TRUE : BooleanTerm.FALSE;
		if (!truth.equals(prevTruth)) {
			passUpdate();
		}
	}

}
