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

package com.ibm.bluej.consistency.focus;

import java.lang.ref.WeakReference;

import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.util.CleaningCollection;
import com.ibm.bluej.util.common.FunST;


public class FocusIndicator {
	/*
	public static boolean ONLY_SAVE_MAP_ON_FOCUS = true;
	
	private static HashMap<String, FocusIndicator> indicators = new HashMap<String, FocusIndicator>();
	
	public static void clear() {
		indicators.clear();
	}
	
	public static void forceCheckBest(MaximumAPost map) {
		IdentityHashMap<FocusIndicator, Boolean> prevVals = new IdentityHashMap<FocusIndicator, Boolean>();
		for (FocusIndicator f : indicators.values()) {
			prevVals.put(f, f.isActive());
			f.setActive(true);
		}
		map.checkBest();
		for (Map.Entry<FocusIndicator, Boolean> e : prevVals.entrySet()) {
			e.getKey().setActive(e.getValue());
		}
	}
	
	public static boolean safeToSave() {
		return !ONLY_SAVE_MAP_ON_FOCUS || allFocused();
	}
	
	public static boolean allFocused() {
		for (FocusIndicator f : indicators.values()) {
			if (!f.isActive()) {
				return false;
			}
		}
		return true;
	}
	
	public static FocusIndicator get(String id) {
		FocusIndicator focus = indicators.get(id);
		if (focus == null) {
			focus = new FocusIndicator();
			indicators.put(id, focus);
		}
		return focus;
	}
	*/
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		boolean update = !this.active && active;
		this.active = active;
		if (update) {
			for (WeakReference<Function> wrf : depends) {
				Function f = wrf.get();
				if (f != null) f.update(null); //NOTE: this will sometimes do unnecessary updates
			}
		}
	}
	
	FocusIndicator() {}
	private boolean active = true;
	
	private CleaningCollection<WeakReference<Function>> depends = new CleaningCollection<WeakReference<Function>>(new FunST<WeakReference<Function>,Boolean>() {
		public Boolean f(WeakReference<Function> o) {
			return o.get() == null;
		}
	});
	
	public void addDepend(Function f) {
		WeakReference<Function> wrf = new WeakReference<Function>(f);
		depends.add(wrf);
	}
	
}
