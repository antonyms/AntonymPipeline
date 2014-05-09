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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.ibm.bluej.consistency.MaximumAPost;

public class FocusIndicatorState {
	public boolean ONLY_SAVE_MAP_ON_FOCUS = false;
	
	private HashMap<String, FocusIndicator> indicators = new HashMap<String, FocusIndicator>();
	
	public void clear() {
		indicators.clear();
	}
	
	public void forceCheckBest(MaximumAPost map) {
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
	
	public boolean safeToSave() {
		return !ONLY_SAVE_MAP_ON_FOCUS || allFocused();
	}
	
	public boolean allFocused() {
		for (FocusIndicator f : indicators.values()) {
			if (!f.isActive()) {
				return false;
			}
		}
		return true;
	}
	
	public FocusIndicator get(String id) {
		FocusIndicator focus = indicators.get(id);
		if (focus == null) {
			focus = new FocusIndicator();
			indicators.put(id, focus);
		}
		return focus;
	}
}
