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
package org.jobimtext.holing.extractor;

import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.holing.type.Token;


public class TokenExtractors {

	/**
	 * returns the covered text of the Annotation
	 */
	public static class CoveredText extends JobimAnnotationExtractor {
		public CoveredText(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {

			return a.getCoveredText().replaceAll("[\t\r\n ]+", " ");
		}
	}
	
	public static class CoveredTextLowercase extends JobimAnnotationExtractor {
		public CoveredTextLowercase(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {

			return a.getCoveredText().replaceAll("[\t\r\n ]+", " ").toLowerCase();
		}
	}

	/**
	 * return the token lowercased and a * if tokens are skipped
	 * 
	 * @author mriedl
	 * 
	 */
	public static class TokenLowercase extends JobimAnnotationExtractor {
		public TokenLowercase(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			// make sure we don't have any invalid XML characters
			// text = text.replaceAll("&(?!(?:[a-z]+|#[0-9]+|#x[0-9a-f]+);)",
			// "&amp;");
			// text = text.replaceAll("<(?![_:a-z][-._:a-z0-9]*\\b[^<>]*>)",
			// "&lt;");
			// text = text.replaceAll("[\u0000-\u001f]", "");
			Token t = (Token) a;
			String text = (t.getSkip()) ? "*" : a.getCoveredText();
			text = text.toLowerCase().replaceAll("[\t\r\n ]+", " ");
			return text;
		}
	}
	

}
