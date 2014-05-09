package com.ibm.bluej.util.common;

import org.apache.commons.lang.StringUtils;

/**
 * This class implements a very basic spell checking mechanism.
 * Given two terms A and B, the class tries to figure out whether one of them is a spelling variation of the other.
 *  
 * @author mchowdh
 *
 */
public class SimpleSpellChecker {

	/**
	 * Given two terms A and B, the method tries to figure out whether one of them is a spelling variation of the other.
	 * 
	 * @param firstTerm
	 * @param secondTerm
	 * @return
	 */
	public static boolean isSameTermAfterCheckingSpellingVairance ( String firstTerm, String secondTerm  ) {
		
		// if any of the term is all upper case but the other term is not, return false
		// Assumption: the term which is all uppercase is probably an acronym
		if ( firstTerm.matches("[A-Z\\s]+") ^ secondTerm.matches("[A-Z\\s]+") ) {
			return false;
		}
		
		firstTerm = firstTerm.toLowerCase(); 
		secondTerm = secondTerm.toLowerCase();
		
		// e.g. "Barack Obama" vs "Bar ack Obama" 
		if ( firstTerm.replaceAll("\\s+", "").equalsIgnoreCase(secondTerm.replaceAll("\\s+", "")) ) {
			return true;
		}
		// e.g. "Obama" vs. "Obam"
		else if ( firstTerm.length() >=4 && secondTerm.length() >=4 
				& StringUtils.getLevenshteinDistance(firstTerm, secondTerm) == 1 ) {
			return true;
		}
				
		return false;
	}
}
