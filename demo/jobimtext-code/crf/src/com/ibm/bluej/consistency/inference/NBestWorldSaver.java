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

import java.util.HashSet;
import java.util.Iterator;

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.learning.IDeltaWeight;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.NBest;


public class NBestWorldSaver implements MAPWorldSaver {

	private HashSet<IWorldWeight> distinctWorlds;
	private NBest<SavedWorld> best;
	private FunST<ScanTerm,Boolean> shouldSave;
	
	public NBestWorldSaver(int nbest, FunST<ScanTerm,Boolean> shouldSave) {
		best = new NBest<SavedWorld>(nbest);
		distinctWorlds = new HashSet<IWorldWeight>();
		this.shouldSave = shouldSave;
	}
	
	public IWorldWeight save(IDeltaWeight worldWeight, Iterator<IndexEntry> truth) {
		if (best.size() >= best.getLimit() && best.peek().worldWeight.getWeight() >= worldWeight.getWeight()) {
			return null;
		}
		
		SavedWorld world = SavedWorld.instance(worldWeight, truth, shouldSave);
		if (best.getLimit() > 1 && distinctWorlds.contains(world.worldWeight)) {
			return null;
		}
		SavedWorld r = best.addRemove(world);
		if (best.getLimit() > 1 && r != world) {
			if (r != null)
				distinctWorlds.remove(r.worldWeight);
			distinctWorlds.add(world.worldWeight);
		}
		return world.worldWeight;
	}

	@Override
	public Iterator<SavedWorld> getSaved() {
		return best.iterator();
	}

	public void clear() {
		best.clear();
	}
}
