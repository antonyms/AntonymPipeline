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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.util.FileUtils;

/**
 * 
 * @author mchowdh
 *
 */
public class GlossaryReader {


	public final static String separator = "\n\nPARSING OUTPUT OF THE ABOVE TEXT:\n\n";
	
	public static void main ( String[] args ) throws IOException {
		File directory = new File("/users/distsem/SRL/parsed_wikipedia");      
		File[] myarray;  
		myarray=new File[0];
		myarray=directory.listFiles();
		for (int j = 0; j < myarray.length; j++) {
		       File path=myarray[j];
		       String parsedText = FileUtils.reader2String(new FileReader(path));
		       
		       String[] arrLines = parsedText.split("\\n");
		       StringBuilder sb = new StringBuilder();
		       StringBuilder line = new StringBuilder();
		       
		       for ( int k=0; k<arrLines.length; k++ ) {
		    	   String[] tmp = arrLines[k].trim().split("\\s+");
		    	   if ( tmp.length < 2 ) {
		    		   sb.append(line.toString().trim() + "\n");
		    		   line = new StringBuilder();
		    	   }
		    	   else
		    		   line.append(tmp[1] + " ");
		       }
		       
		       if ( !line.toString().isEmpty() )
		    	   sb.append(line.toString().trim() + "\n");
		       
		       sb.append( separator + parsedText);
		       
		       DataReaderWriter.write( sb.toString(), false, "/users5/distsem/SRL/parsed_wikipedia_merged/" + path.getName());
		}
		
	}
	
	static void mergeTextAndParsedOutput () throws FileNotFoundException, IOException {
		File directory = new File("/users5/distsem/Corpora/usaa_text_bk");      
		File[] myarray;  
		myarray=new File[0];
		myarray=directory.listFiles();
		for (int j = 0; j < myarray.length; j++) {
		       File path=myarray[j];
		       String docText = FileUtils.reader2String(new FileReader(path));
		       
		       String parsedText = FileUtils.reader2String(new FileReader("/users/distsem/SRL/parsed_usaa/" + path.getName() + ".parsed.cp.v1.4.2"));
		       
		       DataReaderWriter.write( docText + separator + parsedText , false, "/users5/distsem/Corpora/usaa_text/" + path.getName() + ".parsed.cp.v1.4.2");
		}
	}
	
	static void splitLargeCorpus () throws Exception {
		File directory = new File("/users5/distsem/Corpora/usaa_text_orig");      
		File[] myarray;  
		myarray=new File[0];
		myarray=directory.listFiles();
		for (int j = 0; j < myarray.length; j++)
		{
		       File path=myarray[j];
		       FileReader fr = new FileReader(path);
		       BufferedReader br = new BufferedReader(fr);
		       
		       List<String> listOfSplitText = new ArrayList<String>();
		       StringBuilder sb = new StringBuilder();
		       int i = 0;
		       
		       while (br.ready()) {
		          sb.append(br.readLine() + "\n");
		          i++;
		          if ( i >= 5000 ) {
		        	  listOfSplitText.add(sb.toString());
		        	  i=0;
		        	  sb = new StringBuilder();
		          }  
		       }
		       
		       if ( !sb.toString().trim().isEmpty() )
		    	   listOfSplitText.add(sb.toString());
		       
		       
		       
		       br.close();
		       fr.close();
		       
		       for ( int f=0; f<listOfSplitText.size(); f++ )
		    	   DataReaderWriter.write( listOfSplitText.get(f), false, "/users5/distsem/Corpora/usaa_text_" + f + ".txt");
		}
	
	}
	
	static void cleanCorpus() throws IOException {
		File directory = new File("/users/distsem/Corpora/tinyemma");      
		File[] myarray;  
		myarray=new File[0];
		myarray=directory.listFiles();
		for (int j = 0; j < myarray.length; j++)
		{
		       File path=myarray[j];
		       FileReader fr = new FileReader(path);
		       BufferedReader br = new BufferedReader(fr);
		       
		       StringBuilder sb = new StringBuilder();
		       
		       while (br.ready()) {
		          sb.append(cleanString(br.readLine()) + "\n");
		       }
		       
		       br.close();
		       fr.close();
		       
		       DataReaderWriter.write(sb.toString(), false, path.getAbsolutePath());
		}
	}
	
	public static String cleanString(String str) {
		String result = str;
		
		result = result.replaceAll("’", "'");
		result = result.replaceAll("[^\\x20-\\x7e]", ""); // Remove all non-printable characters 
	//	result = result.replaceAll("\\s+", " ");		// compress out internal white space  
		result = result.trim(); 
//		result = result.replaceAll("[ \t\r\n]+", " ");		// compress out internal white space  
//		result = result.replaceAll("\"", "&quot;"); 
		return result;
	}

	
	
	public void readTermsFromGlossary ( String fileName, List<String> listOfGlossaryTerms ) throws FileNotFoundException, IOException {
		String[] listOfLines = FileUtils.reader2String(new FileReader(fileName)).split("\\n+");

		for ( String line : listOfLines ) {
			if ( line.contains("dc:title") ) {
				String str = line.substring(line.indexOf("\""), line.lastIndexOf("\""));
				if ( !listOfGlossaryTerms.contains(str) )
					listOfGlossaryTerms.add(str);
			}
		}
	}

}
