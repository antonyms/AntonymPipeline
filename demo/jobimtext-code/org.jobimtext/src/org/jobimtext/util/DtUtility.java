/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
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
package org.jobimtext.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DtUtility {
	
	private String file;
	private int threshold;
	static String encoding = "UTF-8";
	
	private DtUtility(){
		
	}

	public static Map<String,Double> readWords(String file, double threshold ,boolean gzip) throws NumberFormatException, IOException {
		Map<String, Double> words = new HashMap<String, Double>();
		Reader decoder;
		if (gzip) {
			InputStream fileStream = new FileInputStream(new File(file));
			InputStream gzipStream = new GZIPInputStream(fileStream);
			decoder = new InputStreamReader(gzipStream, encoding);
		} else {
			decoder = new FileReader(new File(file));
		}
		BufferedReader reader = new BufferedReader(decoder);
		
		String line;
		while((line=reader.readLine())!=null){
			String[] sp = line.split("\t");
			String word = sp[0];
			Double score = Double.parseDouble(sp[1]);
			if(score>= threshold){
				words.put(word,score);
			}
		}
		return words;
	}

}
