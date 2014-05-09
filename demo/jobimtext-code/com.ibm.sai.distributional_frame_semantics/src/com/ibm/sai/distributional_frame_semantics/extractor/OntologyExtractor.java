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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.ibm.sai.distributional_frame_semantics.utility.DataReaderWriter;
import com.ibm.sai.distributional_frame_semantics.utility.NormalizedTerm;
import com.ibm.sai.distributional_frame_semantics.utility.TermStatistics;

/**
 * 
 * @author mchowdh
 *
 */
public class OntologyExtractor {


	class OntoItem {
		String term = "";
		Map<String, OntoItem> mapSubtypeTerms = null;
		Map<String, OntoItem> mapPropertyTerms = null;
		int freq = 0;
		int totalSubTerms = 0;
		int totalProperties = 0;
	}
	
	/**
	 * 
	 * @param mapOntology
	 * @param lemma
	 * @return
	 */
	private boolean addPropertyInOntology ( Map<String, OntoItem> mapOntology, OntoItem nItem ) {
		
		if ( mapOntology == null )
			return false;
		
		boolean isAddedAtleastOnce = false;
		
		if ( !mapOntology.isEmpty() ) {
			for ( OntoItem tmp : mapOntology.values() ) {
				
				if ( tmp.term.equalsIgnoreCase(nItem.term) )
					continue;
				
			//	System.out.println( tmp.term + " | " + lemma );
				
				// if a previously added item is a property of the new item
				if ( (!tmp.term.contains(" of ") && tmp.term.startsWith(nItem.term + " "))
						|| tmp.term.matches(".*\\s+of\\s+(.*\\s)+" + Pattern.quote(nItem.term) + "(\\s.*)+") ) {
				
					boolean isAdded = addPropertyInOntology(nItem.mapSubtypeTerms, tmp);					
					if ( !isAdded ) {
						if ( nItem.mapPropertyTerms == null )
							nItem.mapPropertyTerms = new TreeMap<String, OntoItem>();
						
						if ( !nItem.mapPropertyTerms.containsKey(tmp.term) ) {
							nItem.mapPropertyTerms.put(tmp.term, tmp);
							nItem.totalProperties++;
						}
					}
					else
						isAddedAtleastOnce = isAdded;
				}
				else if ( (!nItem.term.contains(" of ") && nItem.term.startsWith(tmp.term + " "))
						|| nItem.term.matches(".*\\s+of\\s+(.*\\s)+" + Pattern.quote(tmp.term) + "(\\s.*)+") ) {
					boolean isAdded = addPropertyInOntology(tmp.mapSubtypeTerms, nItem);					
					if ( !isAdded ) {						
						// the new item is a new property of an exiting item
						if ( tmp.mapPropertyTerms == null )
							tmp.mapPropertyTerms = new TreeMap<String, OntoItem>();
						
						if ( !tmp.mapPropertyTerms.containsKey(nItem.term) ) {
							tmp.mapPropertyTerms.put(nItem.term, nItem);
							tmp.totalProperties++;
						}
					}
					else
						isAddedAtleastOnce = isAdded;
				}
			}
		}
		
		return isAddedAtleastOnce;
	}

	/**
	 * 
	 * @param mapOntology
	 * @param lemma
	 * @return
	 */
	private boolean addTermInOntology ( Map<String, OntoItem> mapOntology, OntoItem nItem ) {
		boolean isAdded = false;
		List<String> listOfItemsToBeRemoved = new ArrayList<String>();
		
		if ( mapOntology == null )
			mapOntology = new TreeMap<String, OntoItem>();

		if ( !mapOntology.isEmpty() ) {
			for ( OntoItem tmp : mapOntology.values() ) {
				
			//	System.out.println( tmp.term + " | " + lemma );
				
				// if it is generalization of a previously added item
				if ( (!tmp.term.contains(" of ") && tmp.term.endsWith(" " + nItem.term))
						|| tmp.term.matches("(.*\\s)?" + Pattern.quote(nItem.term) + " of .*") ) {
					nItem.mapSubtypeTerms = new TreeMap<String, OntoItem>();
					nItem.mapSubtypeTerms.put(tmp.term, tmp);
					nItem.totalSubTerms++;
					
					listOfItemsToBeRemoved.add(tmp.term);
				}
				else if ( (!nItem.term.contains(" of ") && nItem.term.endsWith(" " + tmp.term))
						|| nItem.term.matches("(.*\\s)?" + Pattern.quote(tmp.term) + " of .*") ) {
					isAdded = addTermInOntology( tmp.mapSubtypeTerms, nItem);
					// new subordinate item
					if( !isAdded ) {
						if ( tmp.mapSubtypeTerms == null )
							tmp.mapSubtypeTerms = new TreeMap<String, OntoItem>();
						tmp.mapSubtypeTerms.put(nItem.term, nItem);
						tmp.totalSubTerms++;
						
						isAdded = true;
						for ( String key : listOfItemsToBeRemoved ) {
							mapOntology.remove(key);
						}
					}
				}
				
				if ( isAdded )
					break;
			}
		}
		
		// new item
		if( !isAdded ) {
			mapOntology.put( nItem.term, nItem);
			for ( String key : listOfItemsToBeRemoved )
				mapOntology.remove(key);
		}
		
		return isAdded;
	}
	
	/**
	 * 
	 * @param mapLemmas
	 * @param csvFile
	 * @return
	 * @throws IOException
	 */
	public Map<String, TermStatistics> extractOntology ( Map<String, NormalizedTerm> mapLemmas, String csvFile, Map<String, TermStatistics> mapTermStatistics ) throws IOException {
		
		Map<String, OntoItem> mapOntology = new TreeMap<String, OntoItem>();
		
		for ( String lemma : mapLemmas.keySet() ) {
			NormalizedTerm objLMF = mapLemmas.get(lemma);
			OntoItem nItem = new OntoItem();
			nItem.term = lemma;
			nItem.freq = objLMF.getFrequency();
			//if ( lemma.contains("access") )
				addTermInOntology(mapOntology, nItem);
				addPropertyInOntology(mapOntology, nItem);
		}
	
		StringBuilder sb = new StringBuilder();
		
		addOntoItemToPrintStringGenericToSpecific(mapOntology, sb, "\n", mapTermStatistics);		
		DataReaderWriter.write(sb.toString(), false, csvFile + ".ontology.txt");
		
		ArrayList<String> lines = addOntoItemToPrintStringSpecificToGeneric(mapOntology);
		sb = new StringBuilder();
		for ( String str : lines )
			sb.append(str + "\n");
		
		DataReaderWriter.write(sb.toString(), false, csvFile + ".SpecToGen.txt");
		
		return mapTermStatistics;
	}
	
	/**
	 * 
	 * @param mapOntology
	 * @param sb
	 * @param indentString
	 * @param mapTermStatistics
	 */
	private void addOntoItemToPrintStringGenericToSpecific ( Map<String, OntoItem> mapOntology, StringBuilder sb, 
			String indentString, Map<String, TermStatistics> mapTermStatistics ) {
		
		if ( mapOntology == null )
			return;
		
		if ( indentString.length()> 1 ) {
			sb.append(indentString + "--------------");
			sb.append(indentString + "More specific:");
			sb.append(indentString + "--------------");
		}
		
		String childIndentString = indentString + "\t";
		Map<Integer, List<String>> mapSortedByFreq = new TreeMap<Integer, List<String>>();
		
		for ( OntoItem item : mapOntology.values() ) {
			List<String> list = mapSortedByFreq.get(item.freq);
			if ( list == null )
				list = new ArrayList<String>();
			
			list.add(item.term);			
			mapSortedByFreq.put( item.freq, list);
		}
		
		List<Integer> listOfFreq = new ArrayList<Integer>(mapSortedByFreq.keySet());
		for ( int f=listOfFreq.size()-1; f>=0; f-- ) {
			List<String> list = mapSortedByFreq.get(listOfFreq.get(f));
			for ( String key : list ) {
				OntoItem item = mapOntology.get(key);
				
				sb.append(indentString + item.term + " (" + item.freq + ")");
				TermStatistics newStat = new TermStatistics();
				newStat.term = item.term;
				newStat.totalProperties = item.totalProperties;
				newStat.totalSubTerms = item.totalSubTerms;
				mapTermStatistics.put(item.term, newStat);
				
				addOntoItemToPrintStringGenericToSpecific(item.mapSubtypeTerms, sb, childIndentString, mapTermStatistics);
				
				if ( item.mapPropertyTerms != null ) {
					sb.append(childIndentString + "***********");
					sb.append(childIndentString + "Properties:");
					sb.append(childIndentString + "***********");
					for ( OntoItem prop : item.mapPropertyTerms.values() ) {
						sb.append(childIndentString + prop.term);					
					}
				}
			}			
		}
		
		sb.append(indentString + "................");
	}
	
	/**
	 * 
	 * @param mapOntology
	 * @return
	 */
	private ArrayList<String> addOntoItemToPrintStringSpecificToGeneric ( Map<String, OntoItem> mapOntology ) {
		if ( mapOntology == null )
			return null;
		
		ArrayList<String> listOfRows = new ArrayList<String>();
		
		for ( OntoItem item : mapOntology.values() ) {
			if ( item.mapSubtypeTerms == null || item.mapSubtypeTerms.isEmpty() )
				listOfRows.add(item.term);
			else {
				ArrayList<String> tmp = addOntoItemToPrintStringSpecificToGeneric(item.mapSubtypeTerms);
				if ( tmp == null ) {
					if ( !listOfRows.contains(item.term) )
						listOfRows.add(item.term);
				}
				else {
					for ( int x =0; x<tmp.size(); x++ ) {
						listOfRows.add(tmp.get(x) + " -> " + item.term);
					}
				}
			}
		}
		
		return listOfRows;
	}
}
