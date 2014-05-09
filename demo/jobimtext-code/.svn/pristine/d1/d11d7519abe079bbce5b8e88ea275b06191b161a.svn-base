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

import java.util.Iterator;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.learning.IDeltaWeight;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.util.SingleObjIterator;
import com.ibm.bluej.util.common.FunST;

public class SingleWorldSaver implements MAPWorldSaver {
	private SavedWorld world;
	private FunST<ScanTerm,Boolean> shouldSave;
	
	public SingleWorldSaver(FunST<ScanTerm,Boolean> shouldSave) {
		this.shouldSave = shouldSave;
	}
	
	public IWorldWeight save(IDeltaWeight worldWeight, Iterator<IndexEntry> truth) {
		if (world != null && worldWeight.getWeight() <= world.worldWeight.getWeight()) {
			return null;
		}
		world = SavedWorld.instance(worldWeight, truth, shouldSave);
		return world.worldWeight;
	}

	@Override
	public Iterator<SavedWorld> getSaved() {
		return new SingleObjIterator<SavedWorld>(world);
	}
	
	public void clear() {
		world = null;
	}
}
