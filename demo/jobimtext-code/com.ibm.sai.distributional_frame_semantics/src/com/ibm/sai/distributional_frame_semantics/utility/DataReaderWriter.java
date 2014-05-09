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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.examples.xmi.XmiCollectionReader;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.FileUtils;

import com.ibm.sai.distributional_frame_semantics.extractor.FrameExtractor;
import com.ibm.sai.distributional_frame_semantics.extractor.TerminologyExtractor;
import com.ibm.sai.semantic_role_annotator.types.Argument;
import com.ibm.sai.semantic_role_annotator.types.Predicate;
import com.ibm.sai.semantic_role_annotator.types.Token;

/**
 * 
 * @author mchowdh
 *
 */
public class DataReaderWriter {

	public static final String[] Verbs_with_Apostrophe = new String[] {"n't", " not", "'ll", " will", "'m", " am", "'re", " are", "'s", " is", "'ve", " have", "'d", " had"}; 

	class Sentence{
		int senIndex = -1;
		Map<String, Predicate> mapPredicates = new HashMap<String, Predicate>();
	}
		
		
	
	/**
	 * FIXME: Right now, we assume possessive 's is not part of any string.
	 * 
	 * @param str
	 * @param headWord
	 * @return
	 */
	private String removeApostrophe( String str ) {
	
		if ( str != null && !str.isEmpty() ) {
			int twice = 1;
			while ( twice <= 2 ) {
				for ( int k=0; k<Verbs_with_Apostrophe.length; k+=2 ) {
					if ( twice == 2 ) {
						if ( str.contains(Verbs_with_Apostrophe[k].toUpperCase()) )
							str = str.replaceAll( Verbs_with_Apostrophe[k].toUpperCase(), Verbs_with_Apostrophe[k+1].toUpperCase());
					}
					else if ( str.contains(Verbs_with_Apostrophe[k]) )
						str = str.replaceAll( Verbs_with_Apostrophe[k], Verbs_with_Apostrophe[k+1]);
						
				}
				twice++;
			}
		}
		
		return str.trim();
	}
	
	/**
	 * 
	 * @param casDir
	 * @param argOutFile
	 * @param predOutFile
	 * @throws IOException
	 * @throws UIMAException 
	 */
	private void readCAS ( String casDir, String argOutFile, String predOutFile ) throws IOException, UIMAException {
		
		Map<String, String> mapPredicateText = new TreeMap<String,String>();
		Map<String, String> mapArgumentText = new TreeMap<String,String>();
		
		write("Arg. Original Text,Lemma,Role Type,Head Word,Predicate Lemma\n", false, argOutFile);
		write("Predicate Lemma,Predicate Head Word,Arg. Head Word,Arg. Role Type\n", false, predOutFile);
		
		int casNum = 0;
			
		System.out.println("reading directory: " + casDir);
		
		final TypeSystemDescription typeSystem = 
				TypeSystemDescriptionFactory.createTypeSystemDescriptionFromPath("/users/mchowdh/wksp_mchowdh_jobim/com.ibm.sai.semantic_role_annotator/descriptors/SemanticRoleTypeSystemDescriptor.xml");
		final CollectionReaderDescription xmiReader = CollectionReaderFactory.createReaderDescription(XmiCollectionReader.class,
				  typeSystem, XmiCollectionReader.PARAM_INPUTDIR, casDir);
		AnalysisEngineDescription ae = AnalysisEngineFactory.createEngineDescription(NoOpAnnotator.class, typeSystem);
		JCasIterable iter = new JCasIterable( xmiReader , ae);
		
		
		for (final JCas cas : iter ) {
			
			++casNum;
			System.out.println(".....................................\nReading CAS " + casNum );
			TextDocument objDoc = new TextDocument(cas);
			
			
			/*
			Iterable<Token> iterToken = UimaJava5.annotations(cas, Token.class);
			TreeMap<Integer, int[]> mapSentenceBoundaries = new TreeMap<Integer, int[]>();
		
			for (Token tok : iterToken) {
				int[] boundaries = mapSentenceBoundaries.get(tok.getSentenceIndex());
				
				if ( boundaries == null ) {
					boundaries = new int[2];
					boundaries[0] = tok.getBegin();
					boundaries[1] = tok.getEnd();
				}
				else {
					if ( boundaries[0] > tok.getBegin() )
						boundaries[0] = tok.getBegin();
					if ( boundaries[1] < tok.getEnd() )
						boundaries[1] = tok.getEnd();
				}
					
				mapSentenceBoundaries.put(tok.getSentenceIndex(), boundaries);
			}

			TreeMap<Integer, String> mapSentenceText = new TreeMap<Integer, String>();
			
			for ( int senIndex : mapSentenceBoundaries.keySet()) {
				int[] boundaries = mapSentenceBoundaries.get(senIndex);
				mapSentenceText.put(senIndex, cas.getDocumentText().substring(boundaries[0], boundaries[1]));
			}

			Map<String, List<Argument>> mapArguments = new HashMap<>();
			Iterable<Argument> iterArg = UimaJava5.annotations(cas, Argument.class);

			for (Argument arg : iterArg) {
				List<Argument> args = mapArguments.get(arg.getArgumentID());
				
				if ( args == null )
					args = new ArrayList<Argument>();
				
				args.add(arg);
				
				mapArguments.put(arg.getArgumentID(), args);
				sbArg.append( arg.getOriginalText() + "," + arg.getLemma() + ","
						+ arg.getSemanticRoleType() + "," + arg.getHeadWord() + "," + (arg.getParentPredicate()).getLemma() + "\n");
			}
			*/
			
			for (Predicate prd : objDoc.getPredicates().values()) {
				
				Map<String, Argument> mapArguments = objDoc.getArguments(prd);
				
				if ( mapArguments == null || mapArguments.isEmpty() ) {
					System.err.println("No argument found for predicate: " + prd.getOriginalText() );
					continue;
				}
					
				String originalText = removeApostrophe(prd.getOriginalText()).replace(",", " ");
				String lemma = removeApostrophe(prd.getLemma()).replace(",", " ");
				String headWord = removeApostrophe(prd.getHeadWord()).replace(",", " ");
				String str = originalText + "," + lemma + "," + headWord + "," + casNum + ":" + prd.getBegin() + ":" + prd.getEnd();
				// #,#,#1 4 days,0 0 days,days,A1
				for ( Argument arg : mapArguments.values() ) {
					String aoriginalText = removeApostrophe(arg.getOriginalText()).replace(",", " ");
												
					String alemma = removeApostrophe(arg.getLemma()).replace(",", " ");
					String aheadWord = removeApostrophe(arg.getHeadWord()).replace(",", " ");
					
					str += "," + aoriginalText + "," + alemma + "," + aheadWord + "," + arg.getSemanticRoleType()
							+ "," + casNum + ":" + arg.getBegin() + ":" + arg.getEnd();
					
					Token tok = objDoc.getToken( arg.getBegin() + "-" + arg.getEnd());
					String posOfFirstWord = tok == null ? "N" : tok.getPos();
					
					if ( aoriginalText.contains(" ") ) {
						Token firstWord = objDoc.getToken( arg.getBegin() + "-" + (arg.getBegin()+aoriginalText.indexOf(" ")));
						posOfFirstWord = firstWord == null ? "N" : firstWord.getPos();
					}
					  					
					String pos = tok == null ? "N" : tok.getPos();
					String tmp = aoriginalText + "," + alemma + "," + arg.getSemanticRoleType() + "," + aheadWord + "," 
							+ originalText + "," + lemma + ","  + headWord + "," + pos + "," + posOfFirstWord;
									
					// NOTE: Out target is not to exploit absolute frequency of a term, but rather to exploit frequency 
					// of it's distinct predicates and it's corresponding roles
					mapArgumentText.put(tmp + "-" + casNum + "-" + arg.getBegin() + "-" + arg.getEnd() , tmp);
				}

				// NOTE: We only consider unique predicates
				mapPredicateText.put(str + "-" + casNum + "-" + prd.getBegin() + "-" + prd.getEnd(), str);
			}	
		}
		

		StringBuilder sbArg = new StringBuilder();
		StringBuilder sbPrd = new StringBuilder();

		for ( String str : mapPredicateText.values() )
			sbPrd.append(str + "\n");
		
		for ( String str : mapArgumentText.values() )
			sbArg.append(str + "\n");
		
		write(sbArg.toString(), true, argOutFile);
		write(sbPrd.toString(), true, predOutFile);

		
		System.out.println("Finished reading from directory: " + casDir);
	}	
	
	/**
	 * 
	 * @param content
	 * @param append
	 * @throws IOException
	 */
	public static void write( String content, boolean append, String outFile) throws IOException  {
		File file = new File(outFile);
		 
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), append);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();					
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws UIMAException 
	 */
	public static void main ( String[] args ) throws IOException, UIMAException {
		
		List<String> listOfStopWords = Arrays.asList((FileUtils.reader2String(new FileReader("/users/distsem/SRL/ling/eng_stop_word_list.txt"))).split("\\n+"));
		
		String fileNamePrefix = "wea";
		
		String dir = "/users/distsem/SRL/" + fileNamePrefix + "-for-scott";
		 
		File theDir = new File(dir);

		// if the directory does not exist, create it		  
		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir);
		    theDir.mkdir();
		}
		
		Map<String, TermStatistics> mapTermStatistics = new HashMap<String, TermStatistics>();
		  
		String argOutFile = dir + "/" + "ncs." + fileNamePrefix + "-argument.csv"; 
		String predOutFile = dir + "/" + "ncs." + fileNamePrefix + "-predicate.csv";
/*
		new DataReaderWriter()
		//.readCAS("/users/distsem/Models/tinyemma-srl-cas101313", argOutFile, predOutFile);
			.readCAS("/users/distsem/Models/wea-trec-srl-cas091813", argOutFile, predOutFile);
	//		.readCAS("/users/distsem/Models/irl-intuit-srl-cas091813", argOutFile, predOutFile);
				
	//*/	
		
		boolean extractTaxonomy = false;
		
		TreeMap<String, NormalizedTerm> mapNormalizedMWEs = new TerminologyExtractor().extractTerminology( argOutFile, listOfStopWords, mapTermStatistics, extractTaxonomy );
		new FrameExtractor().extractFrames(predOutFile, false, mapNormalizedMWEs, listOfStopWords, mapTermStatistics, extractTaxonomy);
		/*/
		List<String> listPrdPrdIdArgArgIdArgRole = Arrays.asList((FileUtils.reader2String(new FileReader(predOutFile + ".tmp"))).split("\\n+"));
		new FrameExtractor().writeJoBimHolingOutput( predOutFile, listPrdPrdIdArgArgIdArgRole);
		//*/
		
	}

	/**
	 * 
	 * @param outFile
	 * @param mapString
	 * @param firstLine
	 * @throws IOException
	 */
	public static void printMapValues( String outFile, Map<Integer, List<String>> mapString, String firstLine ) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		sb.append(firstLine);
			
		for ( List<String> list : mapString.values() ) {
			for ( String str : list ) {
				sb.append(str);
			}
		}
		
		write(sb.toString(), false, outFile);
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
}
