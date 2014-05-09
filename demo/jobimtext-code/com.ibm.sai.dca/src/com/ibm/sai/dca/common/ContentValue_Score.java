/**
 *   Copyright (c) 2012 IBM Corp.
 *   
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *         
 *   http://www.apache.org/licenses/LICENSE-2.0
 *               	
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *               	               
 *   @author: Bonaventura Coppola (coppolab@gmail.com)
 *   
 */


package com.ibm.sai.dca.common;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class ContentValue_Score extends ContentValue {

	private static int LONG_CACHE_SIZE = 5000000;
	
	private static Map<Long, Long> staticValues = Collections.synchronizedMap(new HashMap<Long, Long>(LONG_CACHE_SIZE));
	private Long val;
	
	private Long staticValue(Long iVal) {
		Long oVal = staticValues.get(iVal);
		if (oVal == null) {
			staticValues.put(iVal, iVal);
		}
		oVal = staticValues.get(iVal);
		return oVal;
	}
	
	
	public Long getScore() {
		return this.val;
	}
	
	public void setScore(Long iVal) {
		this.val = staticValue(iVal);
	}

	public void dump() {
		System.out.println(this.val);
	}

	
}
