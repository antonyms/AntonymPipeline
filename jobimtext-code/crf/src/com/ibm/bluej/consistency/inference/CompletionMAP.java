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

package com.ibm.bluej.consistency.inference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.Pair;

public class CompletionMAP extends FlexibleMAP {
	public CompletionMAP(CRFState crfState, ScanTerm goodWorldMarker, int numGood, int numBad) {
		super(crfState);
		this.goodWorldMarker = goodWorldMarker;
		this.numGood = numGood;
		this.numBad = numBad;
		this.goodWorld = crfState.isTrue(goodWorldMarker) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public boolean isInterested(ScanTerm t) {
		return t.equals(goodWorldMarker);
	}
	
	@Override
	public void nowTrue(ScanTerm t) {
		//if (t.equals(goodWorldMarker))
			goodWorld = Boolean.TRUE;
	}

	@Override
	public void nowFalse(ScanTerm t) {
		//if (t.equals(goodWorldMarker))
			goodWorld = Boolean.FALSE;
	}
	
	private int numGood, numBad;
	private ScanTerm goodWorldMarker;
	private Boolean goodWorld;
	
	@Override
	public Object getWorldType() {
		return goodWorld;
	}

	@Override
	public MAPWorldSaver getWorldSaver() {
		if (goodWorld == Boolean.TRUE)
			return new NBestWorldSaver(numGood, null);
		if (goodWorld == Boolean.FALSE)
			return new NBestWorldSaver(numBad, null);
		return null;
	}

	public Pair<ArrayList<WorldWeightParam>, ArrayList<WorldWeightParam>> getGoodAndBad() {
		ArrayList<WorldWeightParam> good = new ArrayList<WorldWeightParam>();
		ArrayList<WorldWeightParam> bad = new ArrayList<WorldWeightParam>();
		for (Map.Entry<Object, MAPWorldSaver> e : this.worldTypes.entrySet()) {
			Iterator<SavedWorld> it = e.getValue().getSaved();
			ArrayList<WorldWeightParam> saveTo = null;
			if (e.getKey().equals(Boolean.TRUE)) {
				saveTo = good;
			} else if (e.getKey().equals(Boolean.FALSE)) {
				saveTo = bad;
			} else {
				throw new Error("Unexpected key in CompletionMAP: "+e.getKey());
			}
			while (it.hasNext()) {
				IWorldWeight ww = it.next().worldWeight;
				saveTo.add((WorldWeightParam)ww);
			}
		}
		return Pair.make(good, bad);
	}
}
