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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;

import com.ibm.sai.semantic_role_annotator.clearNLP.SematicRoleParserV2_2;
import com.ibm.sai.semantic_role_annotator.types.Argument;
import com.ibm.sai.semantic_role_annotator.types.Predicate;
import com.ibm.sai.semantic_role_annotator.types.Token;
import com.ibm.sai.semantic_role_annotator.util.DateExtractor;
import com.ibm.sai.semantic_role_annotator.util.DateString;

/**
 * 
 * @author mchowdh
 *
 */
public class SemanticRoleAnnotator extends JCasAnnotator_ImplBase {
		
	SematicRoleParserV2_2 sp;
	public final static String separator = "\n\nPARSING OUTPUT OF THE ABOVE TEXT:\n\n";
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			sp = new SematicRoleParserV2_2(null);
		} catch (Exception e1) {
			throw new ResourceInitializationException(e1);
		}
	}


	/**
	 * 
	 * @param cas
	 * @param initTok
	 * @return
	 */
	private Token loadToknen ( JCas cas, InitialToken initTok ) {
		
		Token arg = new Token(cas, initTok.getBegIndex(), initTok.getEndIndex());
		arg.setDependencyTypeWithGovernor(initTok.getDependencyTypeWtihGovernor());
		arg.setLemma(initTok.getLemma());
		
		arg.setDependencyTypeWithGovernor(initTok.getDependencyTypeWtihGovernor());
		
		arg.setPos(initTok.getPOS());
		arg.setOriginalText(initTok.getText());
		arg.setTokenID(getID( initTok.getSentenceIndex(), initTok.getIndex()));
		
		arg.setSentenceIndex(initTok.getSentenceIndex());
		
		return arg;
	}
	
	/**
	 * 
	 * @param senIndex
	 * @param tokenIndex
	 * @return
	 */
	private String getID ( int senIndex, int tokenIndex ) {
		return "s" + senIndex + "t" + tokenIndex;
	}
	
	/**
	 * 
	 * @param s
	 * @param cas
	 */
	private void initializeTokens ( Sentence s, JCas cas, Map<String, Token> mapTokens ) {
				
		for ( InitialToken tok : s.getInitialTokens() ) { 
			mapTokens.put(getID( tok.getSentenceIndex(), tok.getIndex()), loadToknen(cas, tok));
			
		}
		
		System.out.println(new Date() + " Total tokens : " + mapTokens.size());
		
		/// sychronize dependents and governors
		for ( InitialToken tok : s.getInitialTokens() ) {
			int gid = tok.getGovernorIndex();
			if ( gid > -1 ) {
				mapTokens.get(getID( tok.getSentenceIndex(), tok.getIndex())).setGovernorToken(mapTokens.get(getID( tok.getSentenceIndex(), gid)));
				FSArray listOfDeps = mapTokens.get(getID( tok.getSentenceIndex(), gid)).getListOfDependents();
				
				List<Token> list = new ArrayList<Token>();
				if ( listOfDeps != null )
					list = makeList(listOfDeps);
				
				list.add(mapTokens.get(getID( tok.getSentenceIndex(), tok.getIndex())));
				mapTokens.get(getID( tok.getSentenceIndex(), gid)).setListOfDependents(makeFsArray(list, cas));
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 * @param mapPredicates
	 * @param cas
	 */
	private void initializePredicate ( Sentence s, Map<String, Predicate> mapPredicates, JCas cas ) {
		/**
		 * Initializing predicates
		 */
		for ( InitialToken term : s.getTerms() ) {
			
			//System.out.println(term.getBegIndex() + " --- " + term.getEndIndex() + " [ " + term.getIndex());
								
			if ( term.isItselfPredicate() ) {
				Predicate prd = new Predicate(cas);
				prd.setBegin(term.getBegIndex());
				prd.setEnd(term.getEndIndex());
				prd.setPredicateID(getID( term.getSentenceIndex(), term.getIndex()));
				prd.setLemma(term.getLemma());
		
				prd.setOriginalText(term.getText());
				prd.setSentenceIndex(term.getSentenceIndex());
				
				prd.setIsNegated(false);
				prd.setHeadWord(term.getHeadWord());
				
				mapPredicates.put(prd.getPredicateID(), prd);
			}
		}
		
		System.out.println(new Date() + " Total predicates : " + mapPredicates.size());
		
	}
	
	
	/**
	 * 
	 * @param s
	 * @param mapPredicates
	 * @param cas
	 */
	private void initializeArgumentsAndPredicates ( Sentence s, Map<String, Predicate> mapPredicates, 
			JCas cas, List<Argument> listArgumentsToBeAddedInCas ) {
		/**
		 * Initializing arguments and updating predicates correspondingly
		 */
		
		for ( Term term : s.getTerms() ) {
			if ( term.isItselfPredicate() ) {
				
				Predicate curPredicate = mapPredicates.get(getID( term.getSentenceIndex(), term.getIndex()));
			
				// adding parent predicates						
				for ( int pp : term.getParentPredicateIndexes() ) {
					
					Predicate parentPredicate = mapPredicates.get(getID( term.getSentenceIndex(), pp));
					
					FSArray arr = curPredicate.getListOfParentPredicates();
					List<Predicate> tmp = new ArrayList<Predicate>();
				
					if ( arr != null )
						tmp = makeList(arr);
					
					tmp.add(parentPredicate);
					curPredicate.setListOfParentPredicates(makeFsArray(tmp, cas));
				}
				
				
				for ( Term argToekn : term.getArguments() ) {
					
					//System.out.println(term.getBegIndex() + " * " + term.getEndIndex() + " arg : " + argToekn.getIndex());
			
					InitialToken otherTerm = argToekn;
					
					if ( term.getSrlType(argToekn).toUpperCase().contains("NEG") ) {
						curPredicate.setIsNegated(true);
						continue;
					}
					
					if ( term.getSrlType(argToekn).toUpperCase().contains("MOD") ) {
						StringArray sarr = curPredicate.getListOfModals();
						List<String> tmpS = new ArrayList<String>();
						if ( sarr != null ) 
							tmpS = makeList(sarr);
						tmpS.add(argToekn.getText());
						curPredicate.setListOfModals(makeStringArray(tmpS, cas));
						continue;
					}
					
					//System.out.println(otherTerm.getBegIndex() + " " + otherTerm.getEndIndex());
			
					Argument arg = new Argument(cas);
					arg.setBegin(otherTerm.getBegIndex());
					arg.setEnd(otherTerm.getEndIndex());
					arg.setLemma(otherTerm.getLemma());
					arg.setOriginalText(otherTerm.getText());
					arg.setSentenceIndex(otherTerm.getSentenceIndex());
					arg.setArgumentID(getID( otherTerm.getSentenceIndex(), otherTerm.getIndex()));
					arg.setParentPredicate(curPredicate);
					arg.setHeadWord(otherTerm.getHeadWord());
					arg.setPosOfHeadWord(otherTerm.getPOS());
					
					if ( term.isItselfPredicate() ) {
						arg.setIsItselfPredicate(true);
						arg.setSemanticRoleType(term.getSrlType(argToekn));								
						arg.setCorrespondingPredicate(mapPredicates.get(arg.getArgumentID()));
					}
					else
						arg.setIsItselfPredicate(false);
						
					// adding arguments
					List<Argument> tmpArg = new ArrayList<Argument>();
					FSArray arr = curPredicate.getListOfSemanticRoleArguments();
					if ( arr != null )
						tmpArg = makeList(arr);							
					tmpArg.add(arg);
					curPredicate.setListOfSemanticRoleArguments(makeFsArray(tmpArg, cas));
					
					// adding semantic role types of the arguments with
					StringArray sarr = curPredicate.getListOfSemanticRoleTypes();
					List<String> tmpS = new ArrayList<String>();
					if ( sarr != null ) 
						tmpS = makeList(sarr);
					tmpS.add(arg.getSemanticRoleType());
					curPredicate.setListOfSemanticRoleTypes(makeStringArray(tmpS, cas));
					
					listArgumentsToBeAddedInCas.add(arg);
				}					
			}
		}
	}

	/**
	 * 	
	 */
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		
		boolean eachLineIsSeparateSentence = false;
		
		//ConsoleDemo cn = new ConsoleDemo();
				
		String[] contents = cas.getDocumentText().split(separator); 
		String docText = contents[0];
		String parsedData = "";
		
		Date et = new Date();
		System.out.println(et.toString() + " -- reading document ");
		String ss = docText.trim();
		if ( ss.isEmpty() )
			return;
		
		System.out.println(ss.length() > 30 ? ss.substring(0, 30) : ss + "  ......");
		
		Map<Integer, DateString> mapDates = DateExtractor.extractDates(docText);
		
		if ( contents.length > 1 )
			parsedData = contents[1];
			
		List<Sentence> listOfProcessedSentences = new ArrayList<Sentence>();
		try {
			if ( parsedData.isEmpty() )
				parsedData = // cn.parseText(docText, eachLineIsSeparateSentence);
						 sp.parseText(docText, 10);
			
			System.out.println(new Date() + " Entering getProcessedSentences()");
			LinguisticPreprocessor.getProcessedSentences( docText, parsedData, 
					listOfProcessedSentences, mapDates, false );
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		} 
		
		System.out.println(new Date() + " Creating uima objects");
		for ( Sentence s : listOfProcessedSentences ) {
		
			Map<String, Token> mapTokens = new HashMap<String, Token>();
			initializeTokens(s, cas, mapTokens);
			
			Map<String, Predicate> mapPredicates = new HashMap<String, Predicate>();				
			initializePredicate(s, mapPredicates, cas);				

			List<Argument> listArgumentsToBeAddedInCas = new ArrayList<Argument>();
			initializeArgumentsAndPredicates(s, mapPredicates, cas, listArgumentsToBeAddedInCas);

			createIndexes(cas, mapPredicates, mapTokens, listArgumentsToBeAddedInCas);
		}		
		
		et = new Date();
		System.out.println(et.toString() + " Ended");
	}
	
	
	/**
	 * 
	 * @param cas
	 * @param mapPredicates
	 * @param mapTokens
	 * @param listArgumentsToBeAddedInCas
	 */
	private void createIndexes ( JCas cas, Map<String, Predicate> mapPredicates, Map<String, Token> mapTokens, 
			List<Argument> listArgumentsToBeAddedInCas ) {
		/**
		 * Indexing arguments and predicates inside cas
		 */
		for ( Argument arg  : listArgumentsToBeAddedInCas ) {
			if ( arg.getCorrespondingPredicate() == null )
				arg.setIsItselfPredicate(false);
			else
				arg.setIsItselfPredicate(true);
			arg.addToIndexes(cas);
		}

		for ( Token tok  : mapTokens.values() ) {
			tok.addToIndexes(cas);
		}
		
		for ( Predicate prd : mapPredicates.values() ) {
			if ( prd.getListOfParentPredicates() != null || prd.getListOfSemanticRoleArguments() != null )
				prd.addToIndexes(cas);
		}

	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static <T> List<T> makeList(FSArray array) {
	    List<T> list = new ArrayList<T>();

	    for (int index = 0; index < array.size(); index++) {
	      list.add((T) array.get(index));
	    }
	    return list;
	  }
	
	public static FSArray makeFsArray(Collection<? extends TOP> list, JCas jcas) {
	    if (list == null)
	      return null;
	    FSArray fsa = new FSArray(jcas, list.size());
	    int i = 0;
	    for (TOP element : list)
	      fsa.set(i++, element);
	    return fsa;
	  }
	
	public static <T> List<T> makeList(StringArray array) {
	    List<T> list = new ArrayList<T>();

	    for (int index = 0; index < array.size(); index++) {
	      list.add((T) array.get(index));
	    }
	    return list;
	  }
	
	public static StringArray makeStringArray(Collection<String> list, JCas jcas) {
	    if (list == null)
	      return null;
	    StringArray fsa = new StringArray(jcas, list.size());
	    int i = 0;
	    for (String element : list)
	      fsa.set(i++, element);
	    return fsa;
	  }

}
