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

package com.ibm.sai.semantic_role_annotator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ibm.sai.semantic_role_annotator.InitialToken;
import com.ibm.sai.semantic_role_annotator.Sentence;
import com.ibm.sai.semantic_role_annotator.util.DateString;

/**
 * 
 * @author mchowdh
 *
 */
public class LinguisticPreprocessor {

	/**
	 * Convert SRL output to sentence objects
	 * 
	 * @param docText
	 * @return
	 * @throws IOException 
	 */
	public static void getProcessedSentences ( String docText, String processedTextWithSrl, 
			List<Sentence> listOfProcessedSentences, Map<Integer, DateString> mapDates, boolean printOnScreen ) throws IOException {
						
		int curCharIndex = -1, senIndex = 0;
		
		if ( processedTextWithSrl == null || processedTextWithSrl.isEmpty() )
			return;
		
			
		String[] tmp = processedTextWithSrl.split("\\n");
		
		Sentence objSentence = new Sentence();
		for ( int x=0; x<=tmp.length; x++ ) {
						
			if ( x==tmp.length || tmp[x].isEmpty() ) {
				if ( !objSentence.hasInitialToken() )
					continue;
				
				objSentence.setSentenceIndex(senIndex);
				
				for ( InitialToken tok : objSentence.getInitialTokens() ) {
					objSentence.addTerm(tok.clone());
				}
				
				//System.out.println("Initial output before chunking and dependency collapsing:");
				
				if ( printOnScreen )
					objSentence.printTokens();
		
				objSentence.createDateExpressions(mapDates);
				objSentence.createNPChunks();
				objSentence.collapseDependency();
				

				for ( Term curTerm : objSentence.getTerms() ) {
					
					for ( Term otherTok : objSentence.getTerms() ) {
						// checking if otherTok is an argument of curTok
						if ( curTerm.getIndex() != otherTok.getIndex()
								&& otherTok.containsParentPredicate(curTerm.getIndex()) ) {
							curTerm.setItselfAsPredicate(true);
							curTerm.addArgumentWithSrlType(otherTok, otherTok.getSrlType(curTerm.getIndex()));
						}
					}
				}
				
				listOfProcessedSentences.add(objSentence);
				
				objSentence = new Sentence();
				senIndex++;
			}
			else {
								
				String[] arrTokProperties = tmp[x].split("\\t");
				if ( printOnScreen )
					System.out.println(Arrays.asList(arrTokProperties));
				
				InitialToken objToken = new InitialToken();
				objToken.setIndex(Integer.valueOf(arrTokProperties[0]) - 1);
				objToken.setText(arrTokProperties[1]);
				objToken.setLemma(arrTokProperties[2]);
				objToken.setPOS(arrTokProperties[3].toUpperCase());
				objToken.setSentenceIndex(senIndex);

				objToken.setHeadWord(objToken.getLemma());
				
				if ( arrTokProperties[5].matches("[0-9]+") )
					objToken.setGovernorIndex(Integer.valueOf(arrTokProperties[5]) - 1);
				objToken.setDependencyTypeWtihGovernor(arrTokProperties[6]);
				
				if ( arrTokProperties.length > 7 && arrTokProperties[7].contains(":") ) {
					String[] str = arrTokProperties[7].split("[;:]");
					for ( int z=0; z<str.length; z+=2 ) {
						int pi = Integer.valueOf(str[z])-1;
						objToken.addParentPredicateIndexAndSrlType(pi, str[z+1]);
					}
				}
				
				objToken.setBegIndex(docText.indexOf(objToken.getText(), curCharIndex+1));
				curCharIndex = objToken.getBegIndex() + objToken.getText().length() -1;
				objToken.setEndIndex(curCharIndex + 1);
				
				objSentence.addInitialToken(objToken);
			}
		}
	}

}
