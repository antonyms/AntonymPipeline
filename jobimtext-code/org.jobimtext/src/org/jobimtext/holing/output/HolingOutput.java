/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and
* 
* FG Language Technologie
* Technische Universitaet Darmstadt
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;
import org.jobimtext.holing.type.JoBim;

/**
 * 
  *
 * @deprecated
 * This class does not handle the counts of jo's and bim's correctly
 */
@Deprecated
public abstract class HolingOutput extends JCasAnnotator_ImplBase {
  
  protected static final String PARAM_EXTRACTOR_CONFIGURATION_FILE = 
      "ExtractorConfigurationFile";

  protected static final String PARAM_OUTPUT_PATH = 
      "OutputPath";
  
  protected String outputPath;
  protected String extractorConfFileName;
  protected JobimAnnotationExtractor extractor;
  protected Logger logger;
  
  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException {
    super.initialize(context);    
    logger = context.getLogger();
    logger.log(Level.FINE, "FeatureCounter:initialize");
    outputPath = (String)context.getConfigParameterValue(
        PARAM_OUTPUT_PATH);    
    extractorConfFileName = (String)context.getConfigParameterValue(
        PARAM_EXTRACTOR_CONFIGURATION_FILE);    
    try {
		extractor = JobimExtractorConfiguration
		    .getExtractorFromXmlFile(extractorConfFileName);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
  }
  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    synchronized(this) {
      Map<String, StringBuffer> sbMap = new HashMap<String, StringBuffer>();      
      for (JoBim jobim : select(aJCas, JoBim.class)) {
        String model = jobim.getModel();
        if (!sbMap.containsKey(model)) {
          sbMap.put(model, new StringBuffer());
        }
        StringBuffer sb = sbMap.get(model);
        sb.append(extractor.extractKey(jobim));
        sb.append(extractor.getConfiguration().getKeyValuesDelimiter());
        sb.append(extractor.extractValues(jobim));
        sb.append('\n');
      }
      for (String key : sbMap.keySet()) {
        StringBuffer sb = sbMap.get(key);
        writeJoBimString(sb.toString(), key);
      }
      for (String key : sbMap.keySet()) {
        closeWriter(key);
      }
    }    
  }   
  
  protected abstract void writeJoBimString(String jobimString, String modelId);
  
  protected abstract void closeWriter(String modelId);
  
}
