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
package com.ibm.jobimtext.frame.holing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Lemma;

import com.ibm.sai.distributional_frame_semantics.utility.AllPossibleWordCombinationProducer;
import com.ibm.sai.distributional_frame_semantics.utility.TextDocument;
import com.ibm.sai.semantic_role_annotator.types.Argument;
import com.ibm.sai.semantic_role_annotator.types.Predicate;

/**
 * 
 * @author mchowdh
 *
 */
public class FrameHolingAnnotator extends JCasAnnotator_ImplBase {

	private static String MODEL_ID = "FrameModel";

	private static int count = 1;
	
	public static final String JO_PREDICATE_PREFIX = "@PREDICATE=";
	private static List<String> listOfStopWords = new ArrayList<String>();
	
	private static List<String> listOfPrepositions = null;
	
	@Override
	  public void initialize(UimaContext aContext) throws ResourceInitializationException {
	    super.initialize(aContext);
	    
	    if ( listOfPrepositions == null ) {
	    	String[] arr = new String[] {"aboard", "about", "above", "across", "after", "against", "along", "amid", "among", "anti", "around", "as", "at", "before", "behind", "below", "beneath", "beside", "besides", "between", "beyond", "but", "by", "concerning", "considering", "despite", "down", "during", "except", "excepting", "excluding", "following", "for", "from", "in", "inside", "into", "like", "minus", "near", "of", "off", "on", "onto", "opposite", "outside", "over", "past", "per", "plus", "regarding", "round", "save", "since", "than", "through", "to", "toward", "towards", "under", "underneath", "unlike", "until", "up", "upon", "versus", "via", "with", "within", "without"};
	    	listOfPrepositions = Arrays.asList(arr);
	    }
	    
	    try {
			listOfStopWords = Arrays.asList((FileUtils.reader2String(new FileReader("eng_stop_word_list.txt"))).split("\\n+"));
		} catch (FileNotFoundException e) {
			throw new ResourceInitializationException(e);
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	  }

	  @Override
	  public void process(JCas aJCas) throws AnalysisEngineProcessException {
		  
		  System.out.println(new Date() + " Reading file no. " + count);
		  count++;
		  
		  /**
		   * Read the arguments and predicates from the cas
		   * each argument should be normalized by using only lemmas of the corresponding tokens
		   * generate multiple terms for each normalized argument, increasing the number of tokens starting from the head word
		   * write jos and bims
		   */

		  //System.out.println(new Date() + " Inside frame holing annotator ....");
		  TextDocument objDoc = new TextDocument(aJCas);
		  
		  for (Predicate prd : objDoc.getPredicates().values()) {
				
			  	if ( prd.getLemma().trim().isEmpty() || prd.getLemma().matches("[^A-Za-z]*") )
			  		continue;
			  		
				TreeMap<String, Argument> mapArguments = objDoc.getArguments(prd);
				
				if ( mapArguments == null || mapArguments.isEmpty() ) {
					System.err.println("No argument found for predicate: " + prd.getOriginalText() );
					continue;
				}

				ArrayList<String> listOfArgCombinations = getArgCombinations(mapArguments);
				
				generateJoBimHolingOutputForArgument( aJCas, prd.getLemma(), mapArguments, listOfArgCombinations, objDoc);
				generateJoBimHolingOutputForPredicate( aJCas, prd.getLemma(), prd.getBegin(), prd.getEnd(), listOfArgCombinations);
		  }
	  }
	  
	  /**
	   * buy(a:x, b:y, c:z)
	   * 
	   * x
	   * 
	   * y,z	   * 
	   * buy(a:@,b,c)
	   * 
	   * y
	   * buy(a:@,b)
	   * 
	   * 
	   * @param jcas
	   * @param jo
	   * @param bim
	   * @return
	   */
	  public JoBim createJobim(JCas jcas, Lemma jo, FSArray arrayBims, String relation) {
		  
		  //System.out.println("Creating JoBim pair for " + jo.getValue());
		  		  
		  JoBim jb = new JoBim(jcas, jo.getBegin(), jo.getEnd());
		  jb.setModel(MODEL_ID);
		  
		  jb.setKey(jo);
		    
		  jb.setValues(arrayBims);
			
		  jb.setRelation(relation);
		  jb.setHole(-1);
		  jb.addToIndexes();
		    
		  return jb;
	  }
	  
	  /**
	   * 
	   * @param lemma
	   * @param begI
	   * @param endI
	   * @param jcas
	   * @return
	   */
	  private Lemma createLemmaObject ( String lemma, int begI, int endI, JCas jcas ) {
		  	
		  Lemma la = new Lemma(jcas);
		  la.setValue(lemma);
		  la.setBegin(begI);
		  la.setEnd(endI);
		
		  return la;
	  }

	  /**
	   * 
	   * @param mapArgumentsSortedByRole
	   * @return
	   */
	  private ArrayList<String> getArgCombinations ( TreeMap<String, Argument> mapArgumentsSortedByRole ) {
		
		  ArrayList<String> listOfArgRoleArgHead = new ArrayList<String>();
			
		  for ( Argument arg : mapArgumentsSortedByRole.values() ) {
				if ( isValidAsTerm(arg.getHeadWord()) )
					listOfArgRoleArgHead.add( arg.getSemanticRoleType() + "#:#" + arg.getHeadWord() 
						+ "#:#" + arg.getBegin() + "#:#" + arg.getEnd() );
			}
			
		  ArrayList<String> listOfArgCombinations 
				= AllPossibleWordCombinationProducer.generateCombinations(listOfArgRoleArgHead, ",");
			
		  return listOfArgCombinations;
	  }

	  /**
	   * @param jcas
	   * @param prdLemma
	   * @param mapArgumentsSortedByRole
	   */
	  private void generateJoBimHolingOutputForArgument ( JCas jcas, String prdLemma,   
				TreeMap<String, Argument> mapArgumentsSortedByRole, ArrayList<String> listOfArgCombinations,
				TextDocument objDoc)  {
		  
		for ( Argument arg : mapArgumentsSortedByRole.values() ) {
			/*
			// Avoiding creating Jo for verbs/predicates here
			String posFirstChar =  "";
			if ( arg.getPosOfHeadWord() == null ) {
				Token tok = objDoc.getToken( arg.getBegin() + "-" + arg.getEnd());
				if ( tok != null )
					posFirstChar = (tok.getPos().charAt(0) + "").toLowerCase();
			}
			else
				posFirstChar = (arg.getPosOfHeadWord().charAt(0) + "").toLowerCase();
			
			if ( arg.getCorrespondingPredicate() != null 
					|| posFirstChar.startsWith("v") 
					|| (arg.getLemma().contains("_") && !posFirstChar.startsWith("n")) )
				continue;
			*/
			List<Lemma> listofJos = createJosFromArgument(jcas, arg);
			
			for ( int c=0; c<listOfArgCombinations.size(); c++ ) {
				if ( listOfArgCombinations.get(c).contains(arg.getSemanticRoleType() + "#:#" + arg.getHeadWord()) ) {
				    
					String str = listOfArgCombinations.get(c).replace(arg.getSemanticRoleType()
							+ "#:#" + arg.getHeadWord(), arg.getSemanticRoleType() + "#:#@").trim().replace(",,", ",");
					
					if ( str.startsWith(",") )
						str = str.substring(1);
					
					String[] tmp = str.split(",");

				    FSArray arrayBims = new FSArray(jcas, tmp.length);
				    String tmpBim = prdLemma +  "("; 
												
					for ( int w=0; w<tmp.length; w++ ) {
						if ( !tmp[w].trim().isEmpty() ) {
							String[] xx = tmp[w].split("#:#");
							if ( xx.length < 4  )
								continue;
							
							arrayBims.set( w, createLemmaObject(xx[1], Integer.valueOf(xx[2]), Integer.valueOf(xx[3]), jcas));
							if ( w == 0 )
								tmpBim += xx[0];
							else
								tmpBim += "," + xx[0];
						}
							
					}					
					
					tmpBim += ")";
		
					for ( Lemma jo : listofJos )
						createJobim(jcas, jo, arrayBims, tmpBim);
				}
			}
		}	
	}
    
	  
	/**
	 *   
	 * @param jcas
	 * @param prdLemma
	 * @param mapArgumentsSortedByRole
	 */
	private void generateJoBimHolingOutputForPredicate ( JCas jcas, String prdLemma, int begIndex, int endIndex,
			ArrayList<String> listOfArgCombinations )  {
		
		//System.out.println("Prd. lemma = " + prdLemma);
		String[] toks = prdLemma.trim().split("_");
		
		if ( toks.length > 1 && !toks[1].isEmpty() && !listOfPrepositions.contains(toks[1]) )
			prdLemma = toks[1];
		else
			prdLemma = toks[0];
		
		Lemma jo = createLemmaObject( JO_PREDICATE_PREFIX + prdLemma, begIndex, endIndex, jcas);
			
		for ( int c=0; c<listOfArgCombinations.size(); c++ ) {
			
			String[] tmp = listOfArgCombinations.get(c).split(",");

		    FSArray arrayBims = new FSArray(jcas, tmp.length);
		    String tmpBim = "args_of_pred("; 
										
			for ( int w=0; w<tmp.length; w++ ) {
				if ( !tmp[w].trim().isEmpty() ) {
					String[] xx = tmp[w].split("#:#");
					if ( xx.length < 4  )
						continue;
					
					arrayBims.set( w, createLemmaObject(xx[1], Integer.valueOf(xx[2]), Integer.valueOf(xx[3]), jcas));
					if ( w == 0 )
						tmpBim += xx[0];
					else
						tmpBim += "," + xx[0];
				}
					
			}					
			
			tmpBim += ")";

			createJobim(jcas, jo, arrayBims, tmpBim);				
		}
	}
  
	  
  	/**
  	 * 
  	 * 
  	 * 
  	 * 
  	 * @param jcas
  	 * @param arg
  	 * @return
  	 */
  	private List<Lemma> createJosFromArgument ( JCas jcas, Argument arg ) {
  		
  		HashMap<String, int[]> mapTokenCombination = new HashMap<String, int[]>();
  		
  		String[] argTokens = arg.getLemma().toLowerCase().split("\\s+");
  		
  		if ( argTokens.length == 1 ) {
  			mapTokenCombination.put(argTokens[0], new int[] {arg.getBegin(), arg.getEnd() });
  		}
  		else {
  			int[] arrTSI = new int[argTokens.length];
  	  		int c = 0;
  			for ( int i=0; i<argTokens.length; i++ ) {
  				arrTSI[i] = arg.getBegin() + arg.getLemma().indexOf( argTokens[i], c);
  				c = c + argTokens[i].length() -1;
  			}
  			
  			int ofCI = -1, joEI = -1;
  			int hWI = argTokens.length-1;
  			
  			if ( (ofCI = arg.getLemma().indexOf(" of ")) > -1 ) {
  				String tmp = arg.getLemma().substring(0, ofCI);
  				joEI = arg.getEnd() - (arg.getLemma().length() - tmp.length());
  				
  				hWI = tmp.split("\\s+").length-1;
  			}
  			else
  				joEI = arg.getEnd();
  			
  			String str = "";
  			
  			for ( int t=hWI; t>=0; t-- ) {
  				// looking backward from head
  				str = argTokens[t] + " " + str;
  				mapTokenCombination.put( str.trim(), new int[] { arrTSI[t], joEI });
  				
  				// looking forward from head
  				if ( ofCI > -1 ) {
  					mapTokenCombination.put( (str + arg.getLemma().substring(ofCI)).trim(), new int[] { arrTSI[t], arg.getEnd() });
  				}
  			}
  		}
  		
  		List<Lemma> listOfJos = new ArrayList<Lemma>();
  		
  		for ( String term : mapTokenCombination.keySet() ) {
  			int[] boundary = mapTokenCombination.get(term);
  			if ( isValidAsTerm(term) ) {
  				listOfJos.add(createLemmaObject(term, boundary[0], boundary[1], jcas));
  			}
  		}
  		
  		return listOfJos;
  	}

	/**
	 * 
	 * @param lemma
	 * @param head
	 * @param stopWords
	 * @return
	 */
	private boolean isValidAsTerm ( String lemma ) {
		if ( lemma.length() > 60 || lemma.length() < 2 || lemma.matches("[^a-zA-Z].*") || lemma.matches(".*[^\\sa-zA-Z0-9._-].*") )
			return false;
		
		if ( listOfStopWords.contains(lemma) )
			return false;
	
		// removing MWEs having tokens with repeated characters
		if ( lemma.matches(".*\\s+(.)\\1{1,}") || lemma.matches("(.)\\1{1,}\\s+.*") || lemma.matches("(.)\\1{1,}")  )
			return false;
			
		// removing MWEs having duplicate tokens
		if ( lemma.matches(".*\\b(\\w+)\\b[\\w\\W]*\\b\\1\\b.*")  )
			return false;
		
		return true;
	}
	
}
