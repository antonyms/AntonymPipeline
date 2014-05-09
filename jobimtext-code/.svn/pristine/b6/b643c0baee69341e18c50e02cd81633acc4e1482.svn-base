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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.jobimtext.ingestion.type.CorpusInfo;
import org.jobimtext.ingestion.type.DocumentInfo;


/**
 * The collection reader is used to process a set of corpora. It determines the
 * number of files and creates a work item for each one with a corresponding
 * number of threads per file.
 */
public class JoBimIngestionCollectionReader extends CollectionReader_ImplBase {

	// UIMA input parameters
	public final static String LANGUAGE_CODE = "en";
	public final static String INPUT_PATH_PARAM = "InputPath";
	public final static String FILE_PATTERN_PARAM = "FilePattern";

	// member variables
	protected int index;
	protected int numFiles;
	protected static int numDocuments = 0;
	protected Logger logger;
	protected int size;
	protected Iterator<String> iter;

	/**
	 * Adds one to the number of documents in this collection
	 */
	public synchronized static void incrementNumDocuments() {
		numDocuments++;
	}

	/**
	 * Gets the current number of documents.
	 * 
	 * @return
	 */
	public static int getNumDocuments() {
		return numDocuments;
	}

	/**
	 * Gets all files within a particular path.
	 * 
	 * @param path
	 *            Path to search in for files
	 * @param extensions
	 *            Extension the file must have.
	 * @return set of all available files in the given path.
	 * @throws IOException
	 */
	private static HashSet<String> getFilesFromPath(File path,
			HashSet<String> extensions) throws IOException {
		HashSet<String> fileList = new HashSet<String>();
		if (path.isFile()) {
			String name = path.getName();
			int index = name.lastIndexOf('.');
			if ((index == -1 || index == name.length() - 1)
					&& extensions.contains(""))
				fileList.add(path.getPath());
			else if (extensions.contains(name.substring(index + 1)))
				fileList.add(path.getPath());
		} else if (path.isDirectory())
			for (File file : path.listFiles())
				fileList.addAll(getFilesFromPath(file, extensions));
		else
			throw new IOException("No such file or directory = "
					+ path.getAbsolutePath(), null);

		return fileList;
	}

	/**
	 * Gets all files within a particular path.
	 * 
	 * @param path
	 *            Path to search in for files
	 * @param filePattern
	 *            Pattern the file name must match.
	 * @return set of all available files in the given path.
	 * @throws IOException
	 */
	private static HashSet<String> getFilesFromPath(File path,
			String filePattern) throws IOException {
		HashSet<String> fileList = new HashSet<String>();
		if (path.isFile()) {
			String name = path.getName();
			if (name.matches(filePattern))
				fileList.add(path.getPath());
		} else if (path.isDirectory())
			for (File file : path.listFiles())
				fileList.addAll(getFilesFromPath(file, filePattern));
		else
			throw new IOException("No such file or directory = "
					+ path.getAbsolutePath(), null);

		return fileList;
	}

	/**
	 * Gets all files within a set of paths.
	 * 
	 * @param paths
	 *            Set of paths to search in.
	 * @param extensions
	 *            Extension the file must have.
	 * @return set of all available files in the paths.
	 * @throws ResourceInitializationException
	 */
	public static HashSet<String> getFilesFromPaths(String[] paths,
			HashSet<String> extensions) throws IOException {
		HashSet<String> fileList = new HashSet<String>();
		for (String path : paths)
			fileList.addAll(getFilesFromPath(new File(path), extensions));
		return fileList;
	}

	/**
	 * Gets all files within a set of paths.
	 * 
	 * @param paths
	 *            Set of paths to search in.
	 * @param filePattern
	 *            pattern the file name must match.
	 * @return set of all available files in the paths.
	 * @throws ResourceInitializationException
	 */
	public static HashSet<String> getFilesFromPaths(String[] paths,
			String filePattern) throws IOException {
		HashSet<String> fileList = new HashSet<String>();
		for (String path : paths)
			fileList.addAll(getFilesFromPath(new File(path), filePattern));
		return fileList;
	}

	/**
	 * Closes the collection reader
	 */
	@Override
	public void close() throws IOException {
		logger.log(Level.FINE, "CR:close");
		iter = null;
		index = 0;
	}

	/**
	 * Gets the next work item in the list and adds the document information to
	 * the CAS.
	 * 
	 * @param cas
	 *            CAS to place the document info into
	 */
	@Override
	public synchronized void getNext(CAS cas) throws IOException,
			CollectionException {
		if (!iter.hasNext())
			throw new CollectionException("CR:error=No more files to process.",
					null);

		cas.reset();
		JCas aJCas;
		try {
			aJCas = cas.getJCas();
		} catch (CASException e) {
			throw new CollectionException(e);
		}

		String file = iter.next();

		if (index % 1000 == 0)
			logger.log(Level.INFO, "CR:next:index=" + index + ":file=" + file);
		else
			logger.log(Level.FINE, "CR:next:index=" + index + ":file=" + file);

		// put the document information from the work item in the CAS
		CorpusInfo corpusInfo = new CorpusInfo(aJCas);
		corpusInfo.addToIndexes(aJCas);
		corpusInfo.setNumFiles(numFiles);

		DocumentInfo docInfo = new DocumentInfo(aJCas);
		docInfo.addToIndexes(aJCas);
		docInfo.setInputFile(file);

		index++;

		// set the document text
		cas.setDocumentText(docInfo.getInputFile());
		cas.setDocumentLanguage(LANGUAGE_CODE);
		return;
	}

	/**
	 * Gets the progress of the collection reader
	 * 
	 * @return the current index and the total number of work items.
	 */
	@Override
	public Progress[] getProgress() {
		ProgressImpl[] retVal = new ProgressImpl[1];
		retVal[0] = new ProgressImpl(index, size, "InputFiles");
		return retVal;
	}

	/**
	 * Whether or not there are more work items to be processed.
	 * 
	 * @return <i>True</i> if there are more work items left.
	 */
	@Override
	public boolean hasNext() throws IOException, CollectionException {
		logger.log(Level.FINE, "CR:hasNext:hasNext=" + iter.hasNext());
		return iter.hasNext();
	}

	/**
	 * Processes the input paths and creates the work item list.
	 */
	@Override
	public void initialize() throws ResourceInitializationException {
		super.initialize();

		// get the logger
		logger = getLogger();
		logger.log(Level.FINE, "CR:initialize:begin");

		// get the input files
		String[] inputPaths = (String[]) getConfigParameterValue(INPUT_PATH_PARAM);
		for (int i = 0; i < inputPaths.length; i++)
			// try {
			// URI basePath = new URI(inputPaths[i]);
			// URI uri = basePath.resolve("foo.txt");
			inputPaths[i] = inputPaths[i];
		// inputPaths[i] =
		// SaiDataClient.getInstance().resolve(inputPaths[i]).toString();
		/*
		 * } catch (IOException e) { throw new
		 * ResourceInitializationException(e); }
		 */

		// get the input files
		String filePattern = (String) getConfigParameterValue(FILE_PATTERN_PARAM);
		HashSet<String> allFileNames;
		try {
			if (filePattern.startsWith("/"))
				allFileNames = getFilesFromPaths(inputPaths,
						filePattern.substring(1));
			else {
				HashSet<String> extensions = new HashSet<String>();
				extensions.addAll(Arrays.asList(filePattern.split(":")));
				allFileNames = getFilesFromPaths(inputPaths, extensions);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"CR:initialize:error=Error getting input files.");
			throw new ResourceInitializationException(e);
		}

		// create the work item list and ensure it's not empty
		if (allFileNames.size() == 0)
			throw new ResourceInitializationException(
					"File name list is empty. Can not continue.", null);

		iter = allFileNames.iterator();
		numFiles = allFileNames.size();

		logger.log(Level.INFO, "CR:initialize:files=" + numFiles);
	}

}
