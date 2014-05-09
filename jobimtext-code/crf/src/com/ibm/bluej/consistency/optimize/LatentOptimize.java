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

package com.ibm.bluej.consistency.optimize;

import java.util.ArrayList;

import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;

public class LatentOptimize {
	
	//TODO: instead of rankScore could take a LR style penalty on the difference between the weight of most likely world and correct (zero if correct is most likely)
	private static FunST<WorldWeightParam, String> optStringer = new FunST<WorldWeightParam,String>() {
		public String f(WorldWeightParam o) {
			return o.toOptimizeString();
		}
	};
	
	public static String stringInstance(ArrayList<WorldWeightParam> good, ArrayList<WorldWeightParam> bad) {
		if (good.isEmpty() || bad.isEmpty()) {
			throw new IllegalArgumentException("Must not have empty good or empty bad! good "+good.size()+" bad "+bad.size());
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("\\oddsScore(\\max(");
		buf.append(Lang.stringerList(good, optStringer, ", "));
		buf.append("), \\max(");
		buf.append(Lang.stringerList(bad, optStringer, ", "));
		buf.append("))");
		//\rankScore(\max((+ (P_WAWA * 2.0) ... ), (+ (P_WAWA * 1.0) ...) ...), (+ (P_WAWA * 3.0) ...), ...)
		return buf.toString();
	}
	
}
