/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and

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
package org.jobimtext.util;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;

public class RunJoBimIngestionLocal {
	
	private static String aggregatePath;
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) aggregatePath = args[0];
			XMLInputSource in = new XMLInputSource(aggregatePath);					
			ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
			JCas jcas = ae.newJCas();
			jcas.setDocumentText("");
			jcas.setDocumentLanguage("en");
			ae.process(jcas);
			jcas.reset();
			ae.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
