package org.jobimtext.api.annotator;
/*******************************************************************************
 * Copyright 2012
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

import java.util.Map;

import org.apache.uima.fit.component.Resource_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.jobimtext.api.struct.IThesaurusDatastructure;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;


public class UimaThesaurus extends Resource_ImplBase {
	public static final String PARAM_EXTRACTOR_CONFIGURATION_FILE = "ExtractorConfigurationFile";
	public static final String PARAM_STRING_THESAURUS = "StringThesaurus";
	public JobimAnnotationExtractor extractor;

	public UimaThesaurus() {

	}

	public UimaThesaurus(IThesaurusDatastructure<String, String> thesaurus,
			String extractorConfigurationFile) {
		this.thesaurus = thesaurus;
		this.extractorConfigurationFile = extractorConfigurationFile;
	}

	@ConfigurationParameter(name = PARAM_EXTRACTOR_CONFIGURATION_FILE, mandatory = true)
	private String extractorConfigurationFile;

	@ExternalResource(key = PARAM_STRING_THESAURUS, mandatory = true)
	public IThesaurusDatastructure<String, String> thesaurus;

	@Override
	public boolean initialize(ResourceSpecifier aSpecifier,
			Map<String, Object> aAdditionalParams)
			throws ResourceInitializationException {

		boolean value = super.initialize(aSpecifier, aAdditionalParams);
		return value & connect();

	}
	
	

	private boolean connect() {
		try {
			extractor = JobimExtractorConfiguration
					.getExtractorFromXmlFile(extractorConfigurationFile);
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}
}
