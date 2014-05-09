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

public class SenseTableReader extends TableReader {

  protected void processLine(String line, DSDictionary dict,
      IntObjectOpenHashMap<IntArrayList> map, int max) {
    
    // ensure terms in dictionary
    String[] fields = line.split("\\t");
    if(fields.length<3)return;
    String term = fields[0].trim();
        
    int index = dict.addTerm(term);
    if (!map.containsKey(index)) {
      map.put(index, new IntArrayList());
    }
    String[] terms = fields[2].split(",");
    for (int i=0; i<terms.length; i++) {
      String sim = terms[i].trim();
      index = dict.addTerm(sim);
      if (!map.containsKey(index)) {
        map.put(index, new IntArrayList());
      }
    }

    // add senses
    index = dict.getIndex(term);
    IntArrayList list = map.get(index);
    list.add(Integer.parseInt(fields[1].trim()));
    if (max < 0 || list.size() < max) {
      for (int i=0; i<terms.length; i++) {
        String sim = terms[i].trim();
        list.add(dict.getIndex(sim));
      }
      list.add(-1);
    }

  }
	
}