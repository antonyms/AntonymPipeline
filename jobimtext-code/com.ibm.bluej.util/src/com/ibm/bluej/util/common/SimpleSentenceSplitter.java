package com.ibm.bluej.util.common;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author mchowdh
 *
 */
public class SimpleSentenceSplitter {
	
	  /**
	   * Returns the sentence boundaries inside a given text, 
	   * starting from the 1st character index to the last character index.
	   * 
	   * @param text
	   * @return
	 * @throws Exception 
	   */
	  public static int[] getSentenceBoundaryStarts( String text ) throws Exception {
		  BreakIterator breakIterator = BreakIterator.getSentenceInstance();
		    
		  if ( text.trim().isEmpty() )
			  return new int[0];

		  if (breakIterator == null )
		  		breakIterator = BreakIterator.getSentenceInstance();
	    
		  breakIterator.setText(text);

		  List<Integer> listOfSentenceBoundaries = new ArrayList<Integer>();
			
		  try {
			  int start = breakIterator.first();
			  int end = breakIterator.next();
			  listOfSentenceBoundaries.add(start);
				
			  while (end != BreakIterator.DONE) {        
			    	start = end;
			    	listOfSentenceBoundaries.add(start);
			    	end = breakIterator.next();
			  }

		  } catch ( Exception ex ) {
		    	throw new Exception ("Following error occurred for the sentence \"" + text + "\""
		    			+ ex.getStackTrace());
		  }
		    			
		  int[] arrSentenceBoundaries = ArrayUtils.toPrimitive(listOfSentenceBoundaries.toArray(new Integer[listOfSentenceBoundaries.size()]));
		
		  return arrSentenceBoundaries;
	  }
}
