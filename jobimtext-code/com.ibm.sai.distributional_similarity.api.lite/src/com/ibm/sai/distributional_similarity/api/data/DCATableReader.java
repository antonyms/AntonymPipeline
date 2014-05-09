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
 *   @author: Matthew Hatem (mhatem@us.ibm.com)
 *   
 */

package com.ibm.sai.distributional_similarity.api.data;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class DCATableReader extends TableReader {

  protected void processLine(String line, DSDictionary dict,
      IntObjectOpenHashMap<IntArrayList> map, int max) {

    String[] fields = line.split("\\t");
    
    int term = Integer.parseInt(fields[0]);
    if (!map.containsKey(term)) {
      map.put(term, new IntArrayList());
    }
    IntArrayList list = map.get(term);
    
    for (int i=1; i<fields.length; i+=2) {
      int sim = Integer.parseInt(fields[i]);
      double score = Double.parseDouble(fields[i+1].trim());
      if (!map.containsKey(sim)) {
        map.put(sim, new IntArrayList());
      }
      if (max < 1 || list.size() < max*2) {
        list.add(sim);
        list.add((int)score); // FIXME assumes int
      }    
    }
    
  }
	
}