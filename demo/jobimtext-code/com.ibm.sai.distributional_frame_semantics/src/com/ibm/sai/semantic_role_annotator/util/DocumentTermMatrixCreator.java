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

package com.ibm.sai.semantic_role_annotator.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.util.FileUtils;

import com.ibm.sai.distributional_frame_semantics.utility.DataReaderWriter;

/**
 * 
 * @author mchowdh
 *
 */
public class DocumentTermMatrixCreator {

	public static void main ( String[] args ) throws IOException {
	
		args = "/users/distsem/SRL/holing /users/distsem/SRL/DocumentTermMatrix_usaa_corpus_trec-20131205-v01.24.14".split("\\s+");

		String inputFolder = args[0];
		String outputFile = args[1];
		
		//createDocumentTermMatrixIncludingSparseTerms(inputFolder, outputFile);
		
		createBOWmatrix(inputFolder, outputFile);
	}
	
	private static void createBOWmatrix ( String inputFolder, String outputFile ) throws IOException {
		
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();
		int docID = 1;
		Integer f = 0;
		StringBuilder sb = new StringBuilder();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	Map<String, Integer> mapWords = new HashMap<String,Integer>();
				
		    	String[] allLines = FileUtils.file2String(file).split("\\n+");
		    	for ( String line : allLines ) {
		    		String term = line.split("\\t+")[0].trim().replaceAll("\\s+", "_").replaceAll(":", "_");
		    		if ( !term.isEmpty() ) {
		    			System.out.println(term);
		    			f = mapWords.get(term);
		    			if ( f == null )
		    				f = 1;
		    			else
		    				f++;
		    			mapWords.put(term, f);
		    		}
		    	}
		    	
		    	sb.append(docID + " ");
		    	StringBuilder  ss = new StringBuilder();
		    	for ( String term : mapWords.keySet() ) {
		    		f = mapWords.get(term);
		    		if ( f >= 3 )
		    			ss.append(term + ":" + mapWords.get(term) + " ");
		    	}
		    	sb.append(ss.toString().trim() + "\n");
		    	docID++;
		    }
		}
		
		File outFile = new File(outputFile);
			
		FileUtils.saveString2File(sb.toString(), outFile);
	}
	
	
	private static void createDocumentTermMatrixIncludingSparseTerms ( String inputFolder, String outputFile ) throws IOException {
		Map<Integer, Map<String, Integer>> mapDocs = new TreeMap<Integer, Map<String,Integer>>();
		Map<String, Integer> mapTerms = new HashMap<String, Integer>();
		
		collectTermStatInDocs(inputFolder, outputFile, mapDocs, mapTerms);
		createDocumentTermMatrix(mapDocs, new ArrayList<String>(mapTerms.keySet()), outputFile);		
	}

	private static void collectTermStatInDocs ( String inputFolder, String outputFile, 
			Map<Integer, Map<String, Integer>> mapDocs, Map<String, Integer> mapTerms ) throws IOException {		
		
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();
		int docID = 1;
		Integer f = 0;
				
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	Map<String, Integer> mapWords = new HashMap<String,Integer>();
		    	System.out.println("Reading doc id " + docID);
				
		    	String[] allLines = FileUtils.file2String(file).split("\\n+");
		    	for ( String line : allLines ) {
		    		String term = line.split("\\t+")[0].trim().replaceAll("\\s+", "_").replaceAll(":", "_");
		    		if ( !term.isEmpty() ) {
		    			f = mapWords.get(term);
		    			if ( f == null )
		    				f = 1;
		    			else
		    				f++;
		    			mapWords.put(term, f);
		    			mapTerms.put(term, 1);
		    		}
		    	}
		    	
		    	mapDocs.put(docID, mapWords);
		    	docID++;
		    }
		}		
	}
	
	/**
	 * 
	 * @param mapDocs
	 * @param listOfTerms
	 * @param outputFile
	 * @throws IOException
	 */
	private static void createDocumentTermMatrix( Map<Integer, Map<String, Integer>> mapDocs, ArrayList<String> listOfTerms, String outputFile ) throws IOException {
		
		DataReaderWriter.write("", false, outputFile);
				
		for ( Integer docID : mapDocs.keySet() ) {
			Map<String, Integer> mapWords = mapDocs.get(docID);
			StringBuilder sb = new StringBuilder();
			sb.append(docID + " ");
			System.out.println("Writing output for doc id " + docID);
		    
			StringBuilder ss = new StringBuilder();
			for ( String term : listOfTerms ) {
			
				Integer freq = mapWords.get(term);
				if ( freq == null )
					freq = 0;
				ss.append(term + ":" + freq + " ");
		    }

	    	sb.append(ss.toString().trim() + "\n");
	    	DataReaderWriter.write( sb.toString(), true, outputFile);
		}
	}

}
