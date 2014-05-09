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

import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.learning.IDeltaWeight;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.util.common.FunST;

public class SavedWorld implements Comparable<SavedWorld> {
	public IWorldWeight worldWeight;
	public ArrayList<ScanTerm> world;
	
	private SavedWorld(){}
	
	public static SavedWorld instance(IDeltaWeight worldWeight, Iterator<IndexEntry> truth, FunST<ScanTerm,Boolean> shouldSave) {
		SavedWorld world = new SavedWorld();
		world.worldWeight = worldWeight.recordWeight();
		if (shouldSave != null) {
			world.world = new ArrayList<ScanTerm>();
		
			while (truth.hasNext()) {
				ScanTerm t = truth.next().term;
				if (shouldSave.f(t)) {
					world.world.add(t);
				}
			}
		}
		return world;
	}
	
	@Override
	public int compareTo(SavedWorld o) {
		return (int) Math.ceil(worldWeight.getWeight() - o.worldWeight.getWeight());
	}

}
