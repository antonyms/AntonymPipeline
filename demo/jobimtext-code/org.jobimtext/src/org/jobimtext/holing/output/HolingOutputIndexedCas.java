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
package org.jobimtext.holing.output;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.UUID;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;
import org.jobimtext.holing.type.JoBim;
import org.apache.uima.fit.util.JCasUtil;



public abstract class HolingOutputIndexedCas extends JCasAnnotator_ImplBase {

	public static final String PARAM_EXTRACTOR_CONFIGURATION_FILE = "ExtractorConfigurationFile";
	public static final String PARAM_OUTPUT_PATH = "OutputPath";
	public static final String PARAM_DOCUMENT_ANNOTATION = "DocumentAnnotation";
	public static final String PARAM_DOCUMENT_ANNOTATION_ID = "DocumentAnnotationId";

	protected String outputPath;
	protected String extractorConfFileName;
	protected JobimAnnotationExtractor extractor;
	protected Logger logger;
	protected String documentAnnotationId;
	protected Class<Annotation> documentAnnotationClass;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		logger = context.getLogger();
		logger.log(Level.FINE, "FeatureCounter:initialize");
		outputPath = (String) context
				.getConfigParameterValue(PARAM_OUTPUT_PATH);
		extractorConfFileName = (String) context
				.getConfigParameterValue(PARAM_EXTRACTOR_CONFIGURATION_FILE);
		String documentAnnotation = (String) context
				.getConfigParameterValue(PARAM_DOCUMENT_ANNOTATION);
		documentAnnotationId = (String) context
				.getConfigParameterValue(PARAM_DOCUMENT_ANNOTATION_ID);

		try {
			extractor = JobimExtractorConfiguration
					.getExtractorFromXmlFile(extractorConfFileName);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
		try {
			this.documentAnnotationClass = (Class<Annotation>) Class
					.forName(documentAnnotation);
		} catch (Exception e) {
			
			System.err.println("Document Annotation does not exists: "+e.getMessage());
		}
	}

	private String getvalue(Annotation instance) {
		Object value;
		try {
			value = new PropertyDescriptor(documentAnnotationId,
					documentAnnotationClass).getReadMethod().invoke(instance);
			return value.toString();
		} catch (Exception e) {
			System.err.println("The attribute: " + documentAnnotationId
					+ " could not be found in object of class "
					+ documentAnnotationClass);
			e.printStackTrace();
		}
		return "_Attribute_not_found_";
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		synchronized (this) {
			logger.log(Level.INFO, "Start HolingOutputWriter");
			String unitId="";
			try{
			Annotation doc = JCasUtil.selectSingle(aJCas,
					documentAnnotationClass);
			
			
			unitId = Integer.toString(getvalue(doc).hashCode());
			}catch(NullPointerException e){
				System.err.println("Document Annotation cannot be found");
			}
			unitId = "h"+unitId +"_"+UUID.randomUUID().toString();
			
			/*
			 * String unitId =
			 * doc.getType().getFeatureByBaseName(documentAnnotationFeaturePath
			 * ).getName(); doc.getFeatureValue(Feature.valueOf("inputFile"));
			 * 
			 */
			unitId = unitId.replaceAll("/", "_");
			HashSet<String> filesToClose = new HashSet<String>();
			for (JoBim jobim : select(aJCas, JoBim.class)) {
				String filename = unitId+"_";
				writeJoBimString(filename, unitId, jobim);
				filesToClose.add(filename);
				if ( filesToClose.size() > 2000 )
					closeFiles(filesToClose);
			}			
			
			closeFiles(filesToClose);
			
			logger.log(Level.INFO, "Finish HolingOutputWriter");	
		}
		

	}
	
	private void closeFiles ( HashSet<String> filesToClose ) {
		for (String filename : filesToClose) {
			  closeWriter(filename);
		}
	}

  protected abstract void writeJoBimString(String filename, String unitId, JoBim jobim);
  
  protected abstract void closeWriter(String filename);

}
