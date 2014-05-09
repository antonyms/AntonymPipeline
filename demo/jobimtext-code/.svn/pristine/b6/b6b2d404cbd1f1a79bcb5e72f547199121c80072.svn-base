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

package com.ibm.bluej.consistency.proposal;

import java.util.Iterator;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.util.common.RandomUtil;


public class ProposalConditionExpandTerm implements ProposalCondition {
	public ProposalConditionExpandTerm(ExpandTerm et) {
		this.et = et;
	}
	
	private ExpandTerm et;
	
	public boolean expandRandom(Binds binds, CRFState crfState) {
		Iterator<Binds> pos = et.expand(binds);
		if (!pos.hasNext()) {
			return false;
		}
		Binds expandWith = RandomUtil.randomFromIterator(pos);
		binds.expand(expandWith);
		return true;
	}

	public Iterator<Binds> expandAll(Binds binds, CRFState crfState) {
		return et.expand(binds);
	}

	public String toString() {
		return et.toString();
	}
}
