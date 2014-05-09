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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import com.ibm.sai.semantic_role_annotator.types.Argument;
import com.ibm.sai.semantic_role_annotator.types.Predicate;
import com.ibm.sai.semantic_role_annotator.types.Token;

import static org.apache.uima.fit.util.JCasUtil.select;

/**
 * 
 * @author mchowdh
 *
 */
public class TextDocument {

	private Map<String, Token> mapTokens = new HashMap<String,Token>();
	private Map<String, Predicate> mapPredicates = new HashMap<String,Predicate>();

	private String fileName = "";
	
	public TextDocument( JCas cas ) {
		
		Iterable<Token> iterToken = select(cas, Token.class);
		
		for (Token tok : iterToken) {
			mapTokens.put(tok.getBegin() + "-" + tok.getEnd(), tok);
		}
		
		List<String> listOfPredicateAlreadyAdded = new ArrayList<String>();
		Iterable<Predicate> iterPred = select(cas, Predicate.class);
		
		for (Predicate prd : iterPred) {
			if ( !listOfPredicateAlreadyAdded.contains(prd.getPredicateID()) ) {
				listOfPredicateAlreadyAdded.add(prd.getPredicateID());
			
				mapPredicates.put(prd.getPredicateID(), prd);
			}
		}
	}
	
	public Map<String, Token> getTokens() {
		return mapTokens;
	}

	public Token getToken( String tokenID ) {
		return mapTokens.get(tokenID);
	}
	
	public TreeMap<String, Argument> getArguments( Predicate prd) {

		TreeMap<String, Argument> mapArgumentsSortedByRole = new TreeMap<String, Argument>();
		
		FSArray fs = prd.getListOfSemanticRoleArguments();
		if ( fs == null ) 
			return mapArgumentsSortedByRole;
				
		// FIXME: remove UimaJava5
		List<Argument> listArgs = DataReaderWriter.makeList(fs);
		
		for ( Argument arg : listArgs )
			if ( !arg.getOriginalText().trim().isEmpty() && !arg.getHeadWord().isEmpty() )
				mapArgumentsSortedByRole.put( arg.getSemanticRoleType(), arg);
	
		return mapArgumentsSortedByRole;
	}
	
	public Map<String, Predicate> getPredicates() {
		return mapPredicates;
	}
}
