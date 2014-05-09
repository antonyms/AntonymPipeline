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

package com.ibm.sai.semantic_role_annotator.clearNLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.uima.util.FileUtils;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;
import com.clearnlp.util.UTInput;
import com.clearnlp.util.UTOutput;
import com.google.common.collect.Lists;
import com.ibm.sai.distributional_frame_semantics.utility.DataReaderWriter;
import com.ibm.sai.semantic_role_annotator.LinguisticPreprocessor;
import com.ibm.sai.semantic_role_annotator.SemanticRoleAnnotator;
import com.ibm.sai.semantic_role_annotator.Sentence;
import com.ibm.sai.semantic_role_annotator.util.DateExtractor;
import com.ibm.sai.semantic_role_annotator.util.DateString;

/**
 * 
 * @author mchowdh
 *
 */
public class SematicRoleParserV2_2 {
	final String language = AbstractReader.LANG_EN;
	AbstractComponent[] components;
	AbstractTokenizer tokenizer;
	
	/**
	 * 
	 * @param modelType
	 * @throws Exception
	 */
	public SematicRoleParserV2_2(String modelType) throws Exception {
		initialize(modelType);
	}
	
	/**
	 * 
	 * @param modelType
	 * @throws Exception
	 */
	public void initialize (String modelType) throws Exception	{
		
		if ( modelType == null || modelType.isEmpty() )
			modelType = "general-en";
		
		tokenizer  = NLPGetter.getTokenizer(language);
		AbstractComponent tagger     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser     = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler    = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		
		components = new AbstractComponent[]{tagger, parser, identifier, classifier, labeler};
	}
	
	
	/**
	 * 
	 * @param fullText
	 * @param numThreads
	 * @return
	 * @throws IOException
	 */
	public String parseText( String fullText, int numThreads) throws IOException {
		AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<DEPTree> trees = Lists.newArrayList();
		DEPTree tree;
		
		String[] paragraphs = fullText.trim().split("\\n{2,}");
		List<List<String>> listOfTokenizedSentences = new ArrayList<List<String>>();
		
        for ( String text : paragraphs ) {
        	StringReader srd = new StringReader(text);
            BufferedReader br = new BufferedReader(srd);
            
            listOfTokenizedSentences.addAll(segmenter.getSentences(br));
            br.close();
        }
		
		for (List<String> tokens : listOfTokenizedSentences) {
			tree = NLPGetter.toDEPTree(tokens);
			trees.add(tree);
			executor.execute(new MultiThreadProcessor(tree));
		}
		
		executor.shutdown();
		
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e) {e.printStackTrace();}
		
		StringBuilder sb = new StringBuilder();
		for (DEPTree t : trees)
			sb.append(t.toStringSRL()+"\n\n");			
		
		return sb.toString();
	}
	
	
	class MultiThreadProcessor implements Runnable
	{
		DEPTree cur_tree;
		
		public MultiThreadProcessor(DEPTree tree) {
			cur_tree = tree;
		}
		
		public void run() {
			for (AbstractComponent component : components)
				component.process(cur_tree);			
		}
    }
	
	/**
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param numThreads
	 * @throws IOException
	 */
	private void parseFile ( String inputFile, String outputFile, int numThreads ) throws IOException {
		BufferedReader reader = UTInput.createBufferedFileReader(inputFile);
		PrintStream fout = UTOutput.createPrintBufferedFileStream(outputFile);
		
		String fullText = FileUtils.reader2String(reader);		
		fout.print(parseText(fullText, numThreads));		
		fout.close();
	}

	/**
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 * @param numThreads
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readFilesFromDirAndParse ( String inputFolder, String outputFolder, int numThreads ) throws FileNotFoundException, IOException {
				
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	System.out.println(new Date() + "  Parsing " + file.getName());
		    	String outFileName = outputFolder + "/" + file.getName() + ".2.0.1.parsed";
		    	parseFile( file.getCanonicalPath(), outFileName, numThreads);
		    	String str = FileUtils.file2String(file) + SemanticRoleAnnotator.separator + FileUtils.file2String(new File(outFileName));
		    	DataReaderWriter.write( str, false, outFileName);
		    }
		}
	}
	
	/**
	 * 
	 * @param text
	 * @param numOfThreads
	 * @return
	 * @throws IOException
	 */
	public List<Sentence> getParsedSentencesFromText ( String text, int numOfThreads, boolean printOnScreen ) throws IOException {
		String parsedData = parseText( text, 1);
		Map<Integer, DateString> mapDates = DateExtractor.extractDates(text);
			
		List<Sentence> listOfProcessedSentences = new ArrayList<Sentence>();
	
		LinguisticPreprocessor.getProcessedSentences( text, parsedData, 
			listOfProcessedSentences, mapDates, printOnScreen );
		
		return listOfProcessedSentences;
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		args = "general-en ../org.jobimtext.examples.oss/corpus/ parsed 10".split("\\s+");
		
		Date d = new Date();
		String modelType  = args[0]; //"general-en";// args[0];	// "general-en" or "medical-en"
		String inputFolder  = args[1]; // "/users/distsem/Corpora/anzi_corpus_trec-20131205_text";// args[1];
		String outputFolder = args[2]; //"/users/distsem/SRL/anzi_corpus_trec-20131205_parsed"; //args[2];

		File dir = new File(outputFolder);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		
		int numThreads = Integer.parseInt(args[3]);
		
		
		SematicRoleParserV2_2 srlP = new SematicRoleParserV2_2(modelType);
		
		srlP.readFilesFromDirAndParse(inputFolder, outputFolder, numThreads);
		
		System.out.println(d);
		System.out.println(new Date());
		
	}

}
