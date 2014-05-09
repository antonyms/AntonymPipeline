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

public class SimTableReader extends TableReader {

  protected void processLine(String line, DSDictionary dict,
      IntObjectOpenHashMap<IntArrayList> map, int max) {

    String[] fields = line.split("\\t");

    if ((fields.length < 3)) {
      throw new RuntimeException("Wrong format in data input file");
    }

    String term1 = fields[0].trim();
    String term2 = fields[1].trim();
    double score = Double.parseDouble(fields[2].trim());

    int i1 = dict.addTerm(term1);
    if (!map.containsKey(i1)) {
      map.put(i1, new IntArrayList());
    }

    int i2 = dict.addTerm(term2);
    if (!map.containsKey(i2)) {
      map.put(i2, new IntArrayList());
    }

    IntArrayList list = map.get(i1);
    if (max < 1 || list.size() < max*2) {
      list.add(i2);
      list.add((int) score); // FIXME assumes int
    }    

  }
	
}