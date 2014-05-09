/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
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
package org.jobimtext.ingestion;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.ByteArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.ingestion.type.CorpusInfo;
import org.jobimtext.ingestion.type.DocumentInfo;
import org.apache.uima.fit.util.JCasUtil;



/**
 * Takes a work item from the collection reader and multiplies the CAS into the number of threads for the given file to be processed.
 * 
 * @author john.bufe
 */
public abstract class JoBimDocumentSplitter extends JCasMultiplier_ImplBase {

	/**
	 * Possible states of the CAS multiplier
	 */
	protected enum States {
		running, done, failed
	}

	// language code
	private static final String LANGUAGE_CODE = "en";
	// member variables
	protected Logger logger;
	protected int index;
	protected int numFiles;
	
	protected String errorMessage;
	protected String inputFile;
	protected States state;

	/**
	 * Returns whether or not there are more threads to spawn for this file.
	 * @return <i>True</i> if there are more threads to spawn.
	 */
	@Override
	public abstract boolean hasNext();

	/**
	 * Initializes the CAS multiplier to the 'running' state.
	 */
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		logger = aContext.getLogger();

		inputFile = "";
		errorMessage = "";
		index = -1;
		
		state = States.running;

		logger.log(Level.FINE, "CM:initialize");
	}

	/**
	 * Creates a new CAS for the given file with the next available index.
	 * @return a new CAS for the given file with the next available index.
	 */
	@Override
	public AbstractCas next() {
		long start = System.currentTimeMillis();

		// create a new CAS
		JCas aJCas = getEmptyJCas();
		
		CorpusInfo corpusInfo = new CorpusInfo(aJCas);
		corpusInfo.addToIndexes(aJCas);
		corpusInfo.setNumFiles(numFiles);
		
		DocumentInfo docInfo = new DocumentInfo(aJCas);
		docInfo.addToIndexes(aJCas);

		// make sure this annotator hasn't failed or is done.
		if (state == States.failed) {
			docInfo.setError(true);
			docInfo.setErrorMessage(errorMessage);
			return aJCas;
		} else if (state == States.done) {
			docInfo.setError(true);
			docInfo.setErrorMessage("This file has been completely processed.");
			return aJCas;
		}

		// add the document information to the CAS
		docInfo.setInputFile(inputFile);
		docInfo.setThreadId(index);

		try {
			byte[] serializedDocument = getNextDocument();
			int length = serializedDocument.length;
			ByteArray sD = new ByteArray(aJCas, length);
			sD.copyFromArray(serializedDocument, 0, 0, length);
			docInfo.setSerializedDocument(sD);
			
			
		} catch (Exception e) {
			state = States.failed;
			docInfo.setError(true);
			docInfo.setErrorMessage(e.getMessage());
			return aJCas;
		}

		try {
      setDocumentText(aJCas);
    } catch (AnalysisEngineProcessException e) {
      e.printStackTrace();
    }
		
		setDocumentLanguage(aJCas);
		
		index++;

		// check if done
		if (hasNext()) {
			docInfo.setLast(false);
			JoBimIngestionCollectionReader.incrementNumDocuments();
		} else {
			docInfo.setLast(true);
			state = States.done;
		}
		
		logger.log(Level.FINE, "CM:next:index=" + index + ":time=" + (System.currentTimeMillis()-start)/1000.0);

		return aJCas;
	}
	
	protected void setDocumentText(JCas aJCas) throws AnalysisEngineProcessException{
	  aJCas.setDocumentText("");
	}
	
	protected void setDocumentLanguage(JCas aJCas) {
	  aJCas.setDocumentLanguage(LANGUAGE_CODE);
	}
	
	protected abstract byte[] getNextDocument() throws Exception;

	/**
	 * Processes the input CAS and reads the input file into a byte array.
	 * @param aJCas the input CAS
	 */
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		state = States.running;
		index = 0;
		
		try {
			
			// get the corpus info from the CR
			CorpusInfo corpusInfo = JCasUtil.selectSingle(aJCas, CorpusInfo.class);
			if(corpusInfo == null) {
				logger.log(Level.SEVERE, "CM:process:error=No corpus info");
				throw new AnalysisEngineProcessException("LSA ingestion CM: received CAS with no corpus info,", null);
			}
			numFiles = corpusInfo.getNumFiles();
			
			// gets the document information from the collection reader
			DocumentInfo docInfo = JCasUtil.selectSingle(aJCas, DocumentInfo.class);
			if (docInfo == null) {
				logger.log(Level.SEVERE, "CM:process:error=No document info");
				throw new AnalysisEngineProcessException("LSA ingestion CM: received CAS with no documentInfo,", null);
			}
			inputFile = docInfo.getInputFile();
			
			readFile();
			
			if(hasNext())
				JoBimIngestionCollectionReader.incrementNumDocuments();
			
		} catch (Exception e) {
			state = States.failed;
			errorMessage = e.getMessage();
			logger.log(Level.SEVERE, "CM:process:error=" + errorMessage, e);
			throw new AnalysisEngineProcessException(errorMessage, null, e);
		}
	}
	
	protected abstract void readFile() throws Exception;

}