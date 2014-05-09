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

package com.ibm.sai.distributional_frame_semantics.extractor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.uima.util.FileUtils;

import com.ibm.bluej.util.common.SimpleSpellChecker;
import com.ibm.sai.distributional_frame_semantics.utility.CaseInsensitiveStringList;
import com.ibm.sai.distributional_frame_semantics.utility.DataReaderWriter;
import com.ibm.sai.distributional_frame_semantics.utility.NormalizedTerm;
import com.ibm.sai.distributional_frame_semantics.utility.OrigTextOfTerm;
import com.ibm.sai.distributional_frame_semantics.utility.SynHeadOfTerm;
import com.ibm.sai.distributional_frame_semantics.utility.TermStatistics;

/**
 * 
 * @author mchowdh
 *
 */
public class TerminologyExtractor {
	
	final static int thresholdForBeingStopWordAdjective = 7;
	
	List<String> listOfDynamicStopWordAdjective = new ArrayList<String>();
	
	/**
	 * 
	 * @param lemma
	 * @param head
	 * @param stopWords
	 * @return
	 */
	public static boolean isValidAsTerm ( String lemma, String head, List<String> stopWords, boolean toBeUsedForTermExtraction ) {
		if ( lemma.length() > 60 || lemma.length() < 2 || lemma.matches("[^a-zA-Z].*") || lemma.matches(".*[^\\sa-zA-Z0-9._-].*") )
			return false;
	
		// removing MWEs having tokens with repeated characters
		if ( lemma.matches(".*\\s+(.)\\1{1,}") || lemma.matches("(.)\\1{1,}\\s+.*") || lemma.matches("(.)\\1{1,}")  )
			return false;
			
		// removing MWEs having duplicate tokens
		if ( lemma.matches(".*\\b(\\w+)\\b[\\w\\W]*\\b\\1\\b.*")  )
			return false;
		
		if ( !head.matches(".*[A-Za-z].*") || lemma.contains("#") || (toBeUsedForTermExtraction && lemma.contains("_")) 
				|| lemma.matches("(.*\\s)*[^a-zA-Z]+(\\s.*)*") || stopWords.contains(head.toLowerCase()) )
			return false;
		
		return true;
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param mapMWEs
	 * @param stopWordFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void groupByOriginalText ( String csvFile, Map<String, OrigTextOfTerm> mapMWEs, 
			Map<String, OrigTextOfTerm> mapSWEs, List<String> stopWords ) throws FileNotFoundException, IOException {

		String[] listOfLines = (FileUtils.reader2String(new FileReader(csvFile))).split("\\n+");
		
		//			sbArg.append("Arg. Original Text,Lemma,Role Type,Head Word,Predicate Lemma\n");
		int c=0;
		for ( String str : listOfLines ) {
			String[] columns = str.split(",");
			if ( columns.length < 4 )
				continue;
			
			String lemma = columns[1], origText = columns[0], head = columns[3],
					 pos = columns[columns.length-2], posOfFirstTok = columns[columns.length-1];
			
			if ( lemma.matches(".*[^o][^f] [tT]he .*") ) {
				lemma = lemma.substring(lemma.toLowerCase().indexOf("the") + 4);
				origText = origText.substring(origText.toLowerCase().indexOf("the") + 4);
			}
			
			if ( !isValidAsTerm(lemma, head, stopWords, true) )
				continue;
			
			System.out.println(lemma);
			
			if ( mapSWEs != null && !origText.contains(" ") )
				OrigTextOfTerm.addItemInCollection( mapSWEs, origText, lemma, origText, head, 1, pos, posOfFirstTok);
			else
				OrigTextOfTerm.addItemInCollection( mapMWEs, origText, lemma, origText, head, 1, pos, posOfFirstTok);
		
			c++;
			System.out.println(c);
		}
	}
	
	/**
	 * 
	 * @param customizedLemmas
	 */
	private void collectCusomizedLemmas ( Map<String, OrigTextOfTerm> mapMWEs,
			List<String> customizedLemmas, OrigTextOfTerm curOrigTextOfTermFreq ) {

		for ( String head : curOrigTextOfTermFreq.getAllSyntacticHeadVariations() ) {
			OrigTextOfTerm lmf = mapMWEs.get(head.toLowerCase());
			if ( lmf != null && lmf.getFrequency() > 1 ) {
				for ( String alterHead : lmf.getAllPreferredNormalizedMweVariations() ) {
					customizedLemmas.add(curOrigTextOfTermFreq.getMostPreferredMweVariation().replaceAll( "\\b" + Pattern.quote(head) + "\\w*\\b", alterHead));
				}
			}
		}
	}

	/**
	 * 
	 * @param mapMWEs
	 * @param isSweAlreadyChecked
	 */
	private void  customizeLemma ( Map<String, OrigTextOfTerm> mapMWEs, boolean isSweAlreadyChecked ) {

		for ( OrigTextOfTerm curOrigTextOfTermFreq : mapMWEs.values() ) {
							
			System.out.println("Checking " + curOrigTextOfTermFreq.getMostPreferredMweVariation() + "  " + curOrigTextOfTermFreq.getFrequency());
			
			// if it is all upper case, it should be already normalized 
			if ( curOrigTextOfTermFreq.getMostPreferredMweVariation().replaceAll(" ", "").matches("[A-Z]+") ) {
				curOrigTextOfTermFreq.removeAllPreferredNormalizedMweVariation();
				curOrigTextOfTermFreq.addPreferredNormalizedMweVariations(curOrigTextOfTermFreq.getMostPreferredMweVariation());
				continue;
			}
			
			if ( !curOrigTextOfTermFreq.consistsOfMultipleWords() )  {

				if ( isSweAlreadyChecked )
					continue;
				
				curOrigTextOfTermFreq.addAlternativeNormalizedMweVariations(curOrigTextOfTermFreq.getAllPreferredNormalizedMweVariations());
				
				curOrigTextOfTermFreq.keepOnlyShortestNormalizedMweVariation();
				continue;
			}

			CaseInsensitiveStringList customizedLemmas = new CaseInsensitiveStringList();
			
			collectCusomizedLemmas(mapMWEs, customizedLemmas, curOrigTextOfTermFreq);
			
			curOrigTextOfTermFreq.removeAllPreferredNormalizedMweVariation();
			
			// if no suitable lemma is found, the MWE is lemma of itself
			if ( customizedLemmas.isEmpty() ) {
				curOrigTextOfTermFreq.addPreferredNormalizedMweVariations(curOrigTextOfTermFreq.getAllMweVariations());
				curOrigTextOfTermFreq.addAlternativeNormalizedMweVariations(curOrigTextOfTermFreq.getAllMweVariations());
			}
			// take the shortest one as lemma
			else {
				curOrigTextOfTermFreq.addPreferredNormalizedMweVariations(customizedLemmas);
				curOrigTextOfTermFreq.keepOnlyShortestNormalizedMweVariation();
				
				curOrigTextOfTermFreq.addAlternativeNormalizedMweVariations(customizedLemmas);
			}
		}
	}
	
	
	/**
	 * 
	 * @param mapNormalizedTerms
	 */
	private void checkSpelling ( TreeMap<String, NormalizedTerm> mapNormalizedTerms ) {
		ArrayList<String> listOfStrings = new ArrayList<String>(mapNormalizedTerms.keySet());
		
		for ( int i=listOfStrings.size()-1; i>0; i-- ) {
			
		//	System.out.println("Spell checking " + listOfStrings.get(i));
			
			for ( int k=i-1; k>0; k-- ) {
				
				// the map is sorted
				if ( listOfStrings.get(k).toLowerCase().charAt(0) != listOfStrings.get(i).toLowerCase().charAt(0) )
					break;
								
				/// checking spelling variation
				if ( SimpleSpellChecker.isSameTermAfterCheckingSpellingVairance( 
						listOfStrings.get(k), listOfStrings.get(i) )
					) {
					NormalizedTerm x = mapNormalizedTerms.get(listOfStrings.get(k));
					NormalizedTerm y = mapNormalizedTerms.get(listOfStrings.get(i));
					
					if ( x.getFrequency() >= y.getFrequency()  ) {
						x.addFrequency(y.getFrequency());
						x.addMweVariations(y.getAllMweVariations());
						mapNormalizedTerms.remove(listOfStrings.get(i));
						mapNormalizedTerms.put(listOfStrings.get(k), x);
						listOfStrings.remove(i);
						x = mapNormalizedTerms.get(listOfStrings.get(k));
						System.out.println( listOfStrings.get(k) + " | " + x.getMostPreferredNormalizedMweVariations() + " " + x.getFrequency());
					}
					else {
						y.addFrequency(x.getFrequency());
						y.addMweVariations(x.getAllMweVariations());
						mapNormalizedTerms.remove(listOfStrings.get(k));
						mapNormalizedTerms.put(listOfStrings.get(i), y);
						listOfStrings.set(k, listOfStrings.get(i));
						listOfStrings.remove(i);
						x = mapNormalizedTerms.get(listOfStrings.get(k));
						System.out.println( listOfStrings.get(k) + " | " + x.getMostPreferredNormalizedMweVariations() + " " + x.getFrequency());
					}
					
					i--;
					break;
				}	
			}
		}
	}
	
	/**
	 * 
	 * @param mapNormalizedTerms
	 */
	private void checkNonRelevantToken ( TreeMap<String, NormalizedTerm> mapNormalizedTerms ) {
		ArrayList<String> listOfStrings = new ArrayList<String>(mapNormalizedTerms.keySet());
		
		for ( int i=listOfStrings.size()-1; i>0; i-- ) {
			
		//	System.out.println("Spell checking " + listOfStrings.get(i));
			String curTerm = listOfStrings.get(i);
			NormalizedTerm objCurTerm = mapNormalizedTerms.get(curTerm); 
			
			String firstToken = curTerm.split("\\s+")[0].toLowerCase();
			

			if ( firstToken.equals("wrong") )
				firstToken.trim();
			
			if ( objCurTerm.getFrequency() < 3 || !objCurTerm.getPosOfFirstTok().equalsIgnoreCase("JJ") )
				continue;
			
			NormalizedTerm other = mapNormalizedTerms.get(firstToken);
			if ( listOfDynamicStopWordAdjective.contains(firstToken)|| 
					(other != null && 
						(!other.getPOS().equalsIgnoreCase("JJ") || other.getFrequency() >= 3)) )
				continue;
			
			
			int count = 0;
			for ( int k=i-1; k>0; k-- ) {
				
				// the map is sorted
				if ( listOfStrings.get(k).toLowerCase().startsWith(firstToken + " ") && count < thresholdForBeingStopWordAdjective )
					count++;
				else
					break;
			}
			
			if ( count >= thresholdForBeingStopWordAdjective )
				listOfDynamicStopWordAdjective.add(firstToken);
		}
		
		for ( int i=listOfStrings.size()-1; i>0; i-- ) {
			
			NormalizedTerm objCurTerm = mapNormalizedTerms.get(listOfStrings.get(i));
			String curTerm = objCurTerm.getMostPreferredNormalizedMweVariations();
			boolean changed = false;
			
			for ( int k=0; k < listOfDynamicStopWordAdjective.size(); k++ ) {
				String adj = listOfDynamicStopWordAdjective.get(k);
				if ( curTerm.toLowerCase().matches(".*\\b" + adj + "\\b.*") ) {
					int x = curTerm.toLowerCase().indexOf(" of the " + adj);
					
					if ( x < 0 )
						x = curTerm.toLowerCase().indexOf(" of " + adj);
					
					// account of other customer,  account of the other customer
					if ( x > 0 ) {
						curTerm = curTerm.substring(0, x).trim();
						k=-1;
						changed = true;
						continue;						
					}

					int z = curTerm.toLowerCase().indexOf(adj);
					x = curTerm.toLowerCase().indexOf(" of ");
			
					// other account, other account of customer
					if ( x > z || x < 0 ) {
						curTerm = curTerm.substring(z + adj.length()).trim();
						k=-1;
						changed = true;
						continue;
					}
					
					// account of big other customer
					if ( x < z && x > 0 ) {
						curTerm = curTerm.substring(0, z).trim();
						k=-1;
						changed = true;
						continue;
					}
				}
			}
		
			NormalizedTerm objEquivTerm = mapNormalizedTerms.get(curTerm.toLowerCase());
			if ( objEquivTerm != null && changed ) {
				objCurTerm.addFrequency(objEquivTerm.getFrequency());
				objEquivTerm.addMweVariation(curTerm);
			
				mapNormalizedTerms.put( curTerm.toLowerCase(), objEquivTerm);
				mapNormalizedTerms.remove(listOfStrings.get(i));
				
				System.out.println("Removed : " + listOfStrings.get(i) + ",   Replaced with: " + curTerm);
				listOfStrings.remove(i);
			}
			
		}				
	}

	/**
	 * 
	 * @param csvFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public TreeMap<String, NormalizedTerm> extractTerminology( String csvFile, List<String> listOfStopWords,
			Map<String, TermStatistics> mapTermStatistics, boolean extractTaxonomy ) throws FileNotFoundException, IOException {
		
		Map<String, OrigTextOfTerm> mapMWEs = new TreeMap<String, OrigTextOfTerm>(), mapSWEs = new TreeMap<String, OrigTextOfTerm>();

		groupByOriginalText(csvFile, mapMWEs, mapSWEs, listOfStopWords );
		
		//customizeLemma(mapSWEs, false);
		mapMWEs.putAll(mapSWEs);
		//customizeLemma(mapMWEs, true);
		
		TreeMap<String, NormalizedTerm> mapArgLemmas = new TreeMap<String, NormalizedTerm>();
		Map<String, SynHeadOfTerm> mapArgHeads = new TreeMap<String, SynHeadOfTerm>();
		
		for ( OrigTextOfTerm objMWE : mapMWEs.values() ) {
			
			String origText = objMWE.getMostPreferredMweVariation(), head = objMWE.getAllSyntacticHeadVariations().get(0);
			
			//System.out.println("Lemma for " + origText);
			
			for ( String lemma : objMWE.getAllPreferredNormalizedMweVariations() ) {
				NormalizedTerm.addItemInCollection( mapArgLemmas, lemma, lemma, origText, head, objMWE.getFrequency(), objMWE.getPOS(), objMWE.getPosOfFirstTok());
				SynHeadOfTerm.addItemInCollection( mapArgHeads, head, lemma, origText, head, objMWE.getFrequency(), objMWE.getPOS());
			}
		}
				
		checkSpelling(mapArgLemmas);
		checkNonRelevantToken(mapArgLemmas);
		
		Map<Integer, List<String>> mapTermPrint = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		Map<Integer, List<String>> mapOrigTextPrint = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		Map<Integer, List<String>> mapHeadPrint = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		
		for ( OrigTextOfTerm obj : mapMWEs.values() ) {
			if ( obj.getFrequency() >= 3 ) {
				
				String w = obj.getMostPreferredMweVariation() + "," + obj.getFrequency();
				for ( String str : obj.getAllAlternativeNormalizedMweVariations() )
					w += ", " + str;
				
				List<String> list = mapOrigTextPrint.get(obj.getFrequency());
				if ( list == null )
					list = new ArrayList<String>();
				list.add(w + "\n");
				mapOrigTextPrint.put(obj.getFrequency(), list);
			}
		}
		
		for ( SynHeadOfTerm obj : mapArgHeads.values() ) {
			List<String> list = mapHeadPrint.get(obj.getFrequency());
			if ( list == null )
				list = new ArrayList<String>();
			list.add(obj.getAllSyntacticHeadVariations().get(0) + "," + obj.getFrequency() + "\n");
			mapHeadPrint.put(obj.getFrequency(), list);
		}
		
		ArrayList<String> keys = new ArrayList<String>(mapArgLemmas.keySet());
		for ( String key : keys ) {
			System.out.println("Printing key : " + key);
			NormalizedTerm obj = mapArgLemmas.get(key);
			if ( obj.getFrequency() >= 3 && !obj.getPOS().toUpperCase().startsWith("V") && obj.getPOS().toUpperCase().startsWith("N") ) {
				List<String> list = mapTermPrint.get(obj.getFrequency());
				if ( list == null )
					list = new ArrayList<String>();
				
				String w = obj.getMostPreferredNormalizedMweVariations() + "," + obj.getFrequency() + "," + obj.getPOS();
				for ( String str : obj.getAllMweVariations() )
					w += ", " + str;
				
				list.add(w + "\n");
				mapTermPrint.put(obj.getFrequency(), list);
			}
			else
				mapArgLemmas.remove(key);
		}		
						
		DataReaderWriter.printMapValues( csvFile + ".terms.csv", mapTermPrint, "Normalized Multi-word,Frequency,POS,Original Text(s)\n-------------------------------------------------\n");
		DataReaderWriter.printMapValues( csvFile + ".head.csv", mapHeadPrint, "Frequency,Head\n");
		DataReaderWriter.printMapValues( csvFile + ".origTextToLemmas.csv", mapOrigTextPrint, "Multi-word Expression,Frequency,Lemma(s)\n");
		
		if ( extractTaxonomy )
			mapTermStatistics = new OntologyExtractor().extractOntology(mapArgLemmas, csvFile, mapTermStatistics);
		
		return mapArgLemmas;
	}

}
