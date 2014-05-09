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

import java.util.ArrayList;

/**
 * 
 * @author mchowdh
 *
 */
public class AllPossibleWordCombinationProducer {  
    
    /**
     * 
     * @param position
     * @param remainder
     */
    private static void nextCombination(int position, int remainder, String delimiter, 
    		StringBuffer output, String[] arrayOfWords, ArrayList<String> listOfCombinations) {     
       
    	output.append(arrayOfWords[position] + delimiter);  
        
        if (remainder == 1) {       
          listOfCombinations.add(output.substring(0,output.length()-1));
        } 
        else {       
          for (int i = position + 1; i + remainder - 1 <= arrayOfWords.length; i++)         
            nextCombination(i, remainder - 1, delimiter, output, arrayOfWords, listOfCombinations);     
        }
        
        if ( output.indexOf(arrayOfWords[position]) >= 0 )
        	output.delete(output.indexOf(arrayOfWords[position]),output.length());
    }    
    
    /**
     * 
     * @param input
     * @param delimiter
     * @return
     */
    public static ArrayList<String> generateCombinations( ArrayList<String> listOfWords, String delimiter) {     
          StringBuffer output = new StringBuffer();           
          ArrayList<String> listOfCombinations = new ArrayList<String>();
          
          if ( listOfWords.size() < 2 )
        	  return listOfWords;
          
          String[] arrayOfWords = listOfWords.toArray(new String[listOfWords.size()]);
          
          
          for (int j = 1; j <= arrayOfWords.length; j++) {       
              for (int k = 0; k + j <= arrayOfWords.length; k++) {
                  nextCombination(k, j, delimiter, output, arrayOfWords, listOfCombinations);                     
              }
          }
          
          return listOfCombinations;
    }
}
