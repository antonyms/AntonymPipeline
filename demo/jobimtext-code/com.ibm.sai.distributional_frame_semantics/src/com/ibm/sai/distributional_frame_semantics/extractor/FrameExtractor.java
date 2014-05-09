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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.util.FileUtils;

import com.ibm.sai.distributional_frame_semantics.utility.AllPossibleWordCombinationProducer;
import com.ibm.sai.distributional_frame_semantics.utility.DataReaderWriter;
import com.ibm.sai.distributional_frame_semantics.utility.NormalizedTerm;
import com.ibm.sai.distributional_frame_semantics.utility.TermStatistics;

/**
 * 
 * @author mchowdh
 *
 */
public class FrameExtractor {
	
	static List<String> listRoleExpansion = Arrays.asList(new String[] {"COM", " Comitatives", "LOC", " Locatives", "DIR", " Directional", "GOL", " Goal", "MNR", " Manner", "TMP", " Temporal", "EXT", " Extent", "REC", " Reciprocals", "PRD", " Secondary Predication", "PRP", " Purpose Clauses", "CAU", " Cause Clauses", "DIS", " Discourse", "MOD", " Modals", "NEG", " Negation", "DSP", " Direct Speech", "LVB", " Light Verb", "ADV", " Adverbials", "ADJ", " Adjectival", "PPT", " Prototypical Patient", "PAG",  " Prototypical Agent", "VSP", " Verb Specific Patient"});
	
	/**
	 * 
	 * @param abrv
	 * @return
	 */
	public static String getExpandedRoles ( String abrv ) {
		String[] tmp = abrv.split("-");
		if ( tmp.length < 2 )
			return abrv;
		
		int x = listRoleExpansion.indexOf(tmp[tmp.length-1]);
		if ( x >= 0 )
			return abrv.replace(tmp[tmp.length-1], listRoleExpansion.get(x+1));
		
		return abrv;
	}
	
	class FrameElementValue {
		String argument = "";
		int freq = 0;
	}
	
	class FrameElement {
		String roleName = "";
		int freq = 0;
		Map<String, FrameElementValue> roleFillers = new TreeMap<String, FrameElementValue>();
	}
	
	class Frame {
		String name = "";
		
		int freq = 0;
		Map<String,FrameElement> roles = new TreeMap<String, FrameExtractor.FrameElement>();
	}
	
	
	/**
	 * Find the corresponding normalized MWE if the argument is a term.
	 * If it is not a term, then find the closes term and replace the argument's text
	 * with the most preferred MWE of that term.   
	 * 
	 * @param mapOriginalTextToNormalizedMWEs
	 * @param origArg
	 * @param head
	 * @return
	 */
	private String getNormalizedArgument ( Map<String, String> mapOriginalTextToNormalizedMWEs, String origArg, 
			String head, TreeMap<String, NormalizedTerm> mapNormalizedMWEs ) {
		
		String origArgLowerCase = origArg.toLowerCase();
		String str = mapOriginalTextToNormalizedMWEs.get(origArgLowerCase);
		
		if ( str != null )
			return str;
		
		str = "";
		// search for the closest term
		for ( NormalizedTerm objNormalizedTerm : mapNormalizedMWEs.values() ) {
			if ( objNormalizedTerm.isOrigTextContainedByStringX(origArgLowerCase) ) { 
				if ( objNormalizedTerm.getAllSyntacticHeadVariations().contains(head)
					&& (str.isEmpty() || str.length() < objNormalizedTerm.getMostPreferredNormalizedMweVariations().length()) ) {
						str = objNormalizedTerm.getMostPreferredNormalizedMweVariations();
					}
			}
		}
		
		if ( str == null || str.isEmpty() )
			return origArg;
		
		return str;
	}
	
	// originalText, lemma, headWord, aoriginalText, alemma, aheadWord, arg.getSemanticRoleType()
	
	/**
	 * 
	 * @param mapFrames
	 * @param csvFile
	 * @param caseSensitive
	 * @param mapOriginalTextToNormalizedMWEs
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private ArrayList<String> generateFrames ( Map<String, Frame> mapFrames, String csvFile, 
			boolean caseSensitive, Map<String, String> mapOriginalTextToNormalizedMWEs,
			TreeMap<String, NormalizedTerm> mapNormalizedMWEs, List<String> listOfStopWords ) throws FileNotFoundException, IOException {

		String[] listOfLines = (FileUtils.reader2String(new FileReader(csvFile))).split("\\n+");
		
		ArrayList<String> listPrdPrdIdArgArgIdArgRoleArgHead = new ArrayList<String>();
		
		boolean title = true;
		for ( String str : listOfLines ) {
			
			if ( title ) {
				title = false;
				continue;
			}
						
			String[] columns = str.split(",");
			if ( columns.length < 5 )
				continue;
			
			String prdHead = columns[2];
			String prdLemma = columns[1];
			
			String prdId = columns[3];
			
			String key = prdLemma;
			
			String prdPrdIdArgArgIdArgRoleArgHead = prdLemma + "," + prdId;
			
			if ( !TerminologyExtractor.isValidAsTerm( prdLemma, prdHead, listOfStopWords, false) )
				continue;
			
			List<String> tmp = new ArrayList<String>();
			
			List<String[]> listOfArgs = new ArrayList<String[]>();
			for ( int i=4; i<columns.length; i+=5 ) {
				
				String[] argNormHeadIdRole = new String[4];
				
				argNormHeadIdRole[1] = columns[i+2].toLowerCase();
				argNormHeadIdRole[0] = getNormalizedArgument(mapOriginalTextToNormalizedMWEs, columns[i], 
						argNormHeadIdRole[1], mapNormalizedMWEs);
				
				argNormHeadIdRole[2] = columns[i+4];
				argNormHeadIdRole[3] = columns[i+3];
				
				if ( TerminologyExtractor.isValidAsTerm(argNormHeadIdRole[0].toLowerCase(), argNormHeadIdRole[1], listOfStopWords, false) ) { 
					if ( argNormHeadIdRole[1].equalsIgnoreCase("it") || argNormHeadIdRole[1].equalsIgnoreCase("you") )
						argNormHeadIdRole[1].trim();
					tmp.add( argNormHeadIdRole[3] + "," + argNormHeadIdRole[0]);
					listOfArgs.add(argNormHeadIdRole);
				}
			}
			
			Collections.sort(tmp);
			key += tmp.toString();
			
			if ( !caseSensitive ) {
				prdLemma = prdLemma.toLowerCase();
				prdHead = prdHead.toLowerCase();
			}
			
			if ( !caseSensitive )
				key = key.toLowerCase();
			
			Frame newFrame = mapFrames.get(key);
						
			if ( newFrame == null ) {
				newFrame = new Frame();
			}
			
			newFrame.name = prdLemma;
			
			System.out.println(str);
			for ( String[] argNormHeadIdRole : listOfArgs ) {
				String shortenRoleName = argNormHeadIdRole[3];
				FrameElement fe = newFrame.roles.get(shortenRoleName);
				
				if ( fe == null )
					fe = new FrameElement();
				
				fe.freq++;
				
				String argNormalizedTerm = argNormHeadIdRole[0];
				String argHead = argNormHeadIdRole[1];
				String argId = argNormHeadIdRole[2];
				
				prdPrdIdArgArgIdArgRoleArgHead += "," + argNormalizedTerm + "," + argId
								+ "," + shortenRoleName + "," + argHead;
				
				fe.roleName = getExpandedRoles(shortenRoleName);
				
				FrameElementValue ev = fe.roleFillers.get(argNormalizedTerm);
				if ( ev != null )
					ev.freq++;
				else {
					ev = new FrameElementValue();
					ev.freq++;
					
					ev.argument = argNormalizedTerm;
					
					fe.roleFillers.put(ev.argument, ev);
				}
				
				if ( !fe.roleFillers.isEmpty() )
					newFrame.roles.put(shortenRoleName, fe);
			}
			
			listPrdPrdIdArgArgIdArgRoleArgHead.add(prdPrdIdArgArgIdArgRoleArgHead);
			
			newFrame.freq++;
			
			if ( !newFrame.roles.isEmpty() )
				mapFrames.put(key, newFrame);
		}
		
		return listPrdPrdIdArgArgIdArgRoleArgHead;
	}
	
	/**
	 * 
	 * @param outFile
	 * @param mapFrames
	 * @throws IOException
	 */
	private void printFrames ( String outFile, Map<String, Frame> mapFrames ) throws IOException {
				
		System.out.println("\n\nPrinting:\n");
		
		Map<Integer, List<String>> mapSortedByFreq = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		
		for ( String key : mapFrames.keySet() ) {
			
			Frame frm = mapFrames.get(key);
			if ( frm.freq < 2 || frm.roles.size() < 2 )
				continue;
			
			System.out.println(key);
			
			StringBuilder sb = new StringBuilder();
			
			String xx = "Frame: " + frm.name + "(" + frm.freq + ")\n";
			for ( FrameElement elem : frm.roles.values() ) {
				xx += ( "\tRole: " + elem.roleName +  " (" + elem.freq + ")\n");
				for ( FrameElementValue val : elem.roleFillers.values() ) {
					xx += ( "\t\tArgument: " + val.argument + " (" + val.freq + ")\n");
				}
			}
			
			System.out.println(xx);
			
		//	sb.append(key + "\n\n");
			sb.append(xx+"\n");
			
			List<String> list = mapSortedByFreq.get(frm.freq);
			if ( list == null )
				list = new ArrayList<String>();
			list.add(sb.toString());
			
			mapSortedByFreq.put( frm.freq, list);
		}
		
		StringBuilder sb = new StringBuilder();
		for ( List<String> list : mapSortedByFreq.values() ) {
			for ( String str : list )
				sb.append(str);
		}
		
		DataReaderWriter.write(sb.toString(), false, outFile);
	}
	
	
	/**
	 * 
	 * @param csvFile
	 * @param caseSensitive
	 * @param mapNormalizedMWEs
	 * @param listOfStopWords
	 * @param mapTermStatistics
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void extractFrames( String csvFile, boolean caseSensitive, TreeMap<String, NormalizedTerm> mapNormalizedMWEs, 
				List<String> listOfStopWords, Map<String, TermStatistics> mapTermStatistics,
				boolean extractTermFeatures ) throws FileNotFoundException, IOException {
			
		Map<String, Frame> mapFrames = new TreeMap<String, Frame>();
		
		Map<String, String> mapOriginalTextToNormalizedMWEs = new HashMap<String, String>();
		
		for ( String key : mapNormalizedMWEs.keySet() ) {
			NormalizedTerm clsNormMweFreq = mapNormalizedMWEs.get(key);
			for ( String str : clsNormMweFreq.getAllMweVariations() ) {
				mapOriginalTextToNormalizedMWEs.put( str.toLowerCase(), key);
			}
		}

		ArrayList<String> listPrdPrdIdArgArgIdArgRoleArgHead = 
				generateFrames(mapFrames, csvFile, caseSensitive, mapOriginalTextToNormalizedMWEs, mapNormalizedMWEs, listOfStopWords);
		printFrames( csvFile + ".frames.txt", mapFrames);
		
		StringBuilder sb = new StringBuilder();
		for ( int x=0; x<listPrdPrdIdArgArgIdArgRoleArgHead.size(); x++ )
			sb.append(listPrdPrdIdArgArgIdArgRoleArgHead.get(x) + "\n");
		
		DataReaderWriter.write(sb.toString(), false, csvFile + ".tmp");
		
		if ( extractTermFeatures )
			printFeatureFileForTerms(mapNormalizedMWEs, mapTermStatistics, csvFile);
		
		//writeJoBimHolingOutput(csvFile, listPrdPrdIdArgArgIdArgRoleArgHead);
	}



	/**
	 * 
	 * @param mapFrames
	 * @param holingOutFile
	 * @throws IOException
	 */
	public void writeJoBimHolingOutput ( String holingOutFile, 
			List<String> listPrdPrdIdArgArgIdArgRole ) throws FileNotFoundException, IOException {
		
		int ddd = 0;
		
		StringBuilder sb = new StringBuilder();
		String holingOutFilePrefix = holingOutFile.substring(holingOutFile.lastIndexOf("/")+1);
		
		int prevNoOfArg = -1;
		for ( String str : listPrdPrdIdArgArgIdArgRole ) {
						
			String[] columns = str.split(",");
			if ( columns.length < 5 )
				continue;
			
			String bimID = columns[1];
			String prdLemma = columns[0];
			
			String bim = prdLemma +  "(" ;
			
			List<String> argRoleArgNameIdHead = new ArrayList<String>();
			for ( int k=2; k<columns.length; k+=4 )
				argRoleArgNameIdHead.add(columns[k+2] + "," + columns[k] + "," + columns[k+1] + "," + columns[k+3]);
			
			List<String> argName = new ArrayList<String>();
			List<String> argHead = new ArrayList<String>();
			List<String> argID = new ArrayList<String>();
			List<String> argRole = new ArrayList<String>();
			
			Collections.sort(argRoleArgNameIdHead);
			
			if ( argRoleArgNameIdHead.size() < 2 )
				continue;
			
			ArrayList<String> listOfArgRoleArgHead = new ArrayList<String>();
			
			for ( int k=0; k<argRoleArgNameIdHead.size(); k++ ) {
				columns = argRoleArgNameIdHead.get(k).split(",");
				
				argRole.add(columns[0]);
				argName.add(columns[1]);
				argID.add(columns[2]);
				argHead.add(columns[3]);
				
				listOfArgRoleArgHead.add(columns[0] + ":" + columns[3]);
				
				//bim += columns[0] + ":" + columns[3].toLowerCase() + ",";
			}
			
			//bim = bim.substring(0, bim.length()-1) + ")";
		
			
			ArrayList<String> listOfArgCombinations 
				= AllPossibleWordCombinationProducer.generateCombinations(listOfArgRoleArgHead, ",");
			for ( int d=0; d<argID.size(); d++ ) {			
				String jo = argName.get(d);
			
				if ( argID.size() > 3 )
					jo.trim();
				
				if ( prevNoOfArg > 3 )
					jo.trim();
				
				for ( int c=0; c<listOfArgCombinations.size(); c++ ) {
					if ( listOfArgCombinations.get(c).contains(argRole.get(d) + ":" + argHead.get(d))) {
						String tmpBim = prdLemma +  "(" 
							+ listOfArgCombinations.get(c).replace(argRole.get(d) + ":" + argHead.get(d), argRole.get(d) + ":@") 
							+ ")";
				
						ddd++;
						System.out.println(ddd + " " + tmpBim);
						
						String jobimRow = jo + "\t" + tmpBim + "\t" + holingOutFilePrefix + "\t" 
								+ argID.get(d) + "\t" + bimID + "\n";
			
						sb.append(jobimRow);
					}
				}
				

				/*
				for ( int k=0; k<argRole.size(); k++ ) {
					
					if ( k != d ) {
						String tmpBim = bim + (k < d ? 
									(argRole.get(k) + ":" + argHead.get(k) + "," + argRole.get(d) + ":@")									
									: (argRole.get(d) + ":@," + argRole.get(k) + ":" + argHead.get(k)))
									+ ")";
						
						String jobimRow = jo + "\t" + tmpBim + "\t" + holingOutFilePrefix + "\t" 
										+ argID.get(d) + "\t" + bimID + "\n";
					
						sb.append(jobimRow);
					}
				}	
				*/
				//String jobimRow = jo + "\t" + bim.replace(argRole.get(d) + ":" + argHead.get(d), argRole.get(d) + ":@") + "\t" + holingOutFilePrefix + "\t" 
					//				+ argID.get(d) + "\t" + bimID + "\n";
				
				//sb.append(jobimRow);
			}
			
			prevNoOfArg = argName.size();
		}
		
		DataReaderWriter.write(sb.toString(), false, holingOutFile + "_@_Jo_Bim_1_part_0.txt");
		System.out.println(holingOutFile + "_@_Jo_Bim_1_part_0.txt");
	}
	
	
	private void printFeatureFileForTerms (  TreeMap<String, NormalizedTerm> mapNormalizedMWEs,
			Map<String, TermStatistics> mapTermStatistics, String csvFile) throws IOException {
		// Normalized Multi-word Term,Frequency as Argument,POS,Roles:Frequency,Number of Unique Frames where term is seen,Number of Sub-Terms Found, Number of Properties Found
		ArrayList<String> keys = new ArrayList<String>(mapNormalizedMWEs.keySet());
		Map<Integer, List<String>> mapTermPrint = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		
		for ( String key : keys ) {
			System.out.println("Printing key : " + key);
			NormalizedTerm obj = mapNormalizedMWEs.get(key);
			if ( obj.getFrequency() >= 3 && obj.getPOS().toUpperCase().startsWith("N") ) {
				List<String> list = mapTermPrint.get(obj.getFrequency());
				if ( list == null )
					list = new ArrayList<String>();
				
				StringBuilder w = new StringBuilder(obj.getMostPreferredNormalizedMweVariations() + "," + obj.getFrequency() + "," + obj.getPOS() + ",");
				
				TermStatistics tStat = mapTermStatistics.get(key);
				if ( tStat != null ) {
					String str = "";
					for ( String role : tStat.mapRoleStatistics.keySet() ) {
						str += role + ":" + tStat.mapRoleStatistics.get(role) + "    ";
					}
					str = str.trim().replaceAll("    ", " | ");
					
					w.append(str + ",");
					w.append(tStat.listOfUniqueFrames.size() + ",");
					w.append(tStat.totalSubTerms + ",");
					w.append(tStat.totalProperties);
				}
				
				list.add(w.toString() + "\n");
				mapTermPrint.put(obj.getFrequency(), list);
			}
		}
		
		DataReaderWriter.printMapValues( csvFile + ".term-extraction-features.csv", mapTermPrint, "Normalized Multi-word Term,Frequency as Argument,POS,Roles:Frequency,Number of Unique Frames where term is seen,Number of Sub-Terms Found, Number of Properties Found\n-------------------------------------------------\n");
	}

}
