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

package com.ibm.sai.distributional_frame_semantics.utility;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author mchowdh
 *
 */
public class SpellChecker {

	public static boolean isSameTermAfterCheckingSpellingMistake ( String firstTerm, String secondTerm  ) {
		
		if ( firstTerm.matches("[A-Z\\s]+") ^ secondTerm.matches("[A-Z\\s]+") ) {
			System.out.println(1);
			return false;
		}
		
		firstTerm = firstTerm.toLowerCase(); 
		secondTerm = secondTerm.toLowerCase();
		
		// e.g. "Barack Obama" vs "Bar ack Obama" 
		if ( firstTerm.replaceAll("\\s+", "").equalsIgnoreCase(secondTerm.replaceAll("\\s+", "")) ) {
			System.out.println(2);
			return true;
		}
		// e.g. "Obama" vs. "Obam"
		else if ( firstTerm.length() >=4 && secondTerm.length() >=4 
				& StringUtils.getLevenshteinDistance(firstTerm, secondTerm) == 1 ) {
			System.out.println(3);
			return true;
		}
					
		System.out.println(4);
		return false;
	}
	
	public static void main ( String[] args ) {
		System.out.println(isSameTermAfterCheckingSpellingMistake("aBC", "aBC"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("AB C", "aBC"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("AB C", "ABC"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("AB C", "A BC"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("abd", "a bd"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("abde", "a bd"));
		System.out.println(isSameTermAfterCheckingSpellingMistake("abdef", "abdf"));
	}
}
