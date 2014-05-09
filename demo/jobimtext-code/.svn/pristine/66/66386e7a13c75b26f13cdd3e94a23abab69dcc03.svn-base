/*******************************************************************************
* Copyright 2013
* Copyright (c) 2013 IBM Corp.
* 

* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/

package com.ibm.sai.distributional_frame_semantics.utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author mchowdh
 *
 */
public class TermStatistics {
	public String term = "";
	public Set<String> listOfUniqueFrames = new HashSet<String>();
	public Map<String, Integer> mapRoleStatistics = new HashMap<String,Integer>();
	public int totalSubTerms = 0, totalProperties = 0;
	
	public void addFrameName ( String predicate ) {
		listOfUniqueFrames.add(predicate);
	}
	
	public void addRole ( String roleName, int freq ) {
		if ( mapRoleStatistics.containsKey(roleName) )
			mapRoleStatistics.put(roleName, mapRoleStatistics.get(roleName)+freq);
		else
			mapRoleStatistics.put(roleName, freq);
	}
}