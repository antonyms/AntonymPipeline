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

import java.util.Map;

/**
 * 
 * @author mchowdh
 *
 */
public class OrigTextOfTerm extends OrigTextNormTextHeadFreq {

	public OrigTextOfTerm() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param mapKeysWithFreq
	 * @param key
	 * @param lemma
	 * @param origText
	 * @param head
	 * @param f
	 * @param pos
	 */
	public static void addItemInCollection ( Map<String, OrigTextOfTerm> mapKeysWithFreq, String key, String lemma, 
			String origText, String head, int f, String pos, String posOfFirstTok ) {

		OrigTextOfTerm objNormMweFreq = mapKeysWithFreq.get(key.toLowerCase());
		if ( objNormMweFreq == null )
			objNormMweFreq = new OrigTextOfTerm();

		objNormMweFreq.addPreferredNormalizedMweVariations(lemma);
		
		objNormMweFreq.addMweVariation(origText);
		
		objNormMweFreq.addSyntacticHeadVariations(head);
		
		objNormMweFreq.addFrequency(f);
		objNormMweFreq.setPOS(pos);
		objNormMweFreq.setPosOfFirstTok(posOfFirstTok);
		
		mapKeysWithFreq.put(key.toLowerCase(), objNormMweFreq);
	}
}
