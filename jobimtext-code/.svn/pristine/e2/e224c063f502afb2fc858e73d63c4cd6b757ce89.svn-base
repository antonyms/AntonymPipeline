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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.util.FileUtils;

/**
 * 
 * @author mchowdh
 *
 */
public class TrecDataReader {
	
	/**
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void convertXmlToPlainText ( File inputFile, File outputFile ) throws FileNotFoundException, IOException {
		String text = FileUtils.file2String(inputFile);
		
		int si = text.indexOf("<text>") + "<text>".length(),
				ei = text.indexOf("</text>");
		
		text = normalizeAnnomysedEntities(text.substring(si, ei)).replaceAll("\n", "\n\n").trim();
		
		FileUtils.saveString2File(text, outputFile);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private String normalizeAnnomysedEntities( String input ) {
		Pattern p = Pattern.compile("\\[[^\\[\\]]*\\]");
		
		Matcher m = p.matcher(input);
		while (m.find()) {
			String old = m.group(0);
			String newS = old.replace("[", "$").replace("]", "$").replaceAll(" ", "_");
		    input = input.replace(old, newS);
		    m = p.matcher(input);
		}
		
		return input;
	}
	
	/**
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readTrecFormatFiles ( String inputFolder, String outputFolder ) throws FileNotFoundException, IOException {
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	convertXmlToPlainText(file, new File(outputFolder + "/" + file.getName().replace(".xml", ".txt")));
		    }
		}
	}
	
	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main (String[] args) throws FileNotFoundException, IOException {
		//*
		String inputDir = "/users5/wea-colab/corpora/usaa_corpus_trec_20131205",
				outputDir = "/users5/distsem/Corpora/usaa_corpus_trec_20131205_text";
		
		File dir = new File(outputDir);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		
		TrecDataReader td = new TrecDataReader();
		td.readTrecFormatFiles(inputDir, outputDir);
		//*/
		
		//createBOWmatrix();
	}
}
