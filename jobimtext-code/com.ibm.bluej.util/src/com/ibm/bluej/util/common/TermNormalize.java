/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.util.common;

public class TermNormalize {
	public static final String NUMBER_PATTERN = "[0-9\\.\\,]*[0-9]+";
	
	/*
	//private static PorterStemmer porter;
	public static String[] tokenizeUMLSTerm(String term, boolean stemming) {
		String[] tokens = term.toLowerCase().replaceAll(NUMBER_PATTERN, "0").replaceAll("\\W+", " ").trim().split(" ");
		if (stemming) {
			if (porter == null) porter = new PorterStemmer();
			for (int i = 0; i < tokens.length; ++i) {
				tokens[i] = porter.stripAffixes(tokens[i]);
			}
		}
		
		return tokens;
	}
*/
	public static String normalize(String term) {
		return tokensToTerm(tokenizeTerm(term));
		//return term.toLowerCase().replaceAll(NUMBER_PATTERN, "0").replaceAll("\\W+", " ").trim();
	}
	public static String[] tokenizeTerm(String term) {
		return term.toLowerCase().replaceAll(NUMBER_PATTERN, "0").replaceAll("\\W+", " ").trim().split(" ");
	}
	public static String tokensToTerm(String[] termTokens) {
		return Lang.stringList(termTokens, " ");
	}
}
