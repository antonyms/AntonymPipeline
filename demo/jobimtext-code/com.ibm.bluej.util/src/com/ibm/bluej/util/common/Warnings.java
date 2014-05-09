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

package com.ibm.bluej.util.common;

import java.util.*;
import java.util.concurrent.*;


public class Warnings {
	//No need to be super careful about multi-threading issues. Doesn't matter if we output slightly too few or too many warnings
	//just don't crash with concurrent mod. exception
	private static Map<String, MutableInteger> warningCount = new ConcurrentHashMap<String, MutableInteger>();
	public static boolean limitWarn(String category, int limit, String message) {
		MutableInteger catCount = warningCount.get(category);
		if (catCount != null && catCount.value >= limit) {
			return false;
		}
		if (catCount == null) {
			catCount = new MutableInteger(0);
			warningCount.put(category, catCount);
		}		
		catCount.value += 1;
		if (catCount.value >= limit) {
			System.err.println(message+" ***(Last warning)***");
		} else {
			System.err.println(message);
		}
		return true;
	}
	
	public static void reset() {
		warningCount.clear();
	}
}
