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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.MaximumAPost;
import com.ibm.bluej.consistency.learning.IWorldWeight;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.SecondPairComparator;

/**
 * Saves the best world of each "type" where type is defined as having a distinct getWorldType()
 * @author mrglass
 *
 */
public abstract class FlexibleMAP extends MaximumAPost {
	public FlexibleMAP(CRFState crfState) {
		super(crfState);
	}
	
	protected Map<Object, MAPWorldSaver> worldTypes = new HashMap<Object, MAPWorldSaver>();
	IWorldWeight overallBest;
	
	public boolean checkBest() {
		Object worldType = getWorldType();
		//CONSIDER: iterate over the different world types?
		if (worldType == null) {
			return false;
		}
		MAPWorldSaver saver = worldTypes.get(worldType);
		if (saver == null) {
			saver = getWorldSaver();
			worldTypes.put(worldType, saver);
		}
		IWorldWeight saved = saver.save(crfState.getWorldWeight(), crfState.posGrnd.getGroundTerms());
		if (saved == null) {
			return false;
		}
		if (overallBest == null || saved.getWeight() > overallBest.getWeight()) {
			overallBest = saved;
		}
		return true;
	}
	
	public void clear() {
		overallBest = null;
		for (MAPWorldSaver mws : worldTypes.values()) {
			mws.clear();
		}
	}
	
	public ArrayList<Pair<Object,Double>> getOrderedMAPs() {
		ArrayList<Pair<Object,Double>> keyAndBest = new ArrayList<Pair<Object,Double>>();
		for (Map.Entry<Object, MAPWorldSaver> e : worldTypes.entrySet()) {
			Iterator<SavedWorld> it = e.getValue().getSaved();
			ArrayList<Double> weights = new ArrayList<Double>();
			while (it.hasNext()) {
				weights.add(it.next().worldWeight.getWeight());
			}
			if (!weights.isEmpty()) {
				Collections.sort(weights);
				Collections.reverse(weights);
				keyAndBest.add(Pair.make(e.getKey(), weights.get(0)));
			}
		}
		
		Collections.sort(keyAndBest, new SecondPairComparator(SecondPairComparator.REVERSE));		
		return keyAndBest;
	}
	
	public String toString() {
		ArrayList<Pair<Object,Double>> keyStrAndBest = getOrderedMAPs();
		if (keyStrAndBest.isEmpty()) {
			return "No MAP";
		}
		double worst = keyStrAndBest.get(keyStrAndBest.size()-1).second;
		StringBuffer buf = new StringBuffer();
		int top = 10;//just top ten
		for (Pair<Object,Double> p : keyStrAndBest) {
			buf.append(Lang.RPAD(p.first.toString(), 60)).append(Lang.dblStr(p.second-worst)).append('\n');
			if (--top <= 0) {
				break;
			}
		}
		return buf.toString();
	}
	
	public IWorldWeight getWorldWeight() {
		return overallBest;
	}
	
	/**
	 * 
	 * @return A HashMap key for the current world type
	 */
	public abstract Object getWorldType();
	/**
	 * 
	 * @return A world saver appropriate for the current world type
	 */
	public abstract MAPWorldSaver getWorldSaver();
}
