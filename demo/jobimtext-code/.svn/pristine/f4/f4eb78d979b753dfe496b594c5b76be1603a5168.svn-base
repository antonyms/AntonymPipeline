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

import java.util.Iterator;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.BooleanTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.util.SingleObjIterator;

public class ExGensym extends ExpandTerm {

	private static int id = 0;
	
	public int estimateExpand(Binds binds) {
		return 1;
	}

	public Iterator<Binds> expand(Binds binds) {
		Binds ex = new Binds(binds.getQuicksave());
		if (parts.length == 2) {
			ex.set((VarTerm)parts[1], new StringTerm(ATerm.getString(parts[0])+id++));
		} else {
			ex.set((VarTerm)parts[0], new StringTerm("gensym"+id++));
		}
		return new SingleObjIterator<Binds>(ex);
	}

	//gensym always true
	public void update(Function source, Object... msg) {
		this.truth = BooleanTerm.TRUE;
	}

}
