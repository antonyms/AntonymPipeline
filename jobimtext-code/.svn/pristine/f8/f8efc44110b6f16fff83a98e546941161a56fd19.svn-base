/**
 *   Copyright (c) 2012 IBM Corp.
 *   
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *         
 *   http://www.apache.org/licenses/LICENSE-2.0
 *               	
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *               	               
 *   @author: Bonaventura Coppola (coppolab@gmail.com)
 *   
 */

package com.ibm.sai.dca.server;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FilenameFilter;
import java.util.zip.GZIPInputStream;

import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.common.ContentValue_Table;
import com.ibm.sai.dca.common.ContentValue_Score;

import com.ibm.sai.dca.common.IO;

public class ContentReader {

	private static enum FILETYPE {PLAIN, GZIP};
	
	// TODO: SELECT RELEVANT RANGE. Possibly, use custom RandomBufferedAccess
	// TODO: Write server redirection info for entries which are not loaded
	
	//public static final String DUMP_FILE="/users/bcoppola/uFrames/DTSimilarity/ServerHashDump";
	
	public static void loadMap(String[] iFileOrDirNameArray, ContentTable map, int maxEntries, ContentValue.VALTYPE type, String fileExtensionFilter) {
		for (int i = 0; i < iFileOrDirNameArray.length; i++) {
			loadMapFromFileOrDir(iFileOrDirNameArray[i], map, maxEntries, type, fileExtensionFilter);
		}
	}
	
	public static void loadMapFromFileOrDir(String iFileOrDirName, ContentTable map, int maxEntries, ContentValue.VALTYPE type, String fileExtensionFilter) {
		File f = new File(iFileOrDirName);
		File[] fileList;
		//System.out.println("LoadMap");
		if (f.isDirectory()) {
			if ((fileExtensionFilter != null) && !fileExtensionFilter.equals("") && !fileExtensionFilter.equals("NONE")) {
				fileList = f.listFiles(new FileExtensionFilter(fileExtensionFilter));
			} else {
				fileList = f.listFiles();
			}
		} else {
			fileList = new File[1];
			fileList[0] = f;
		}
		for (int i = 0; i < fileList.length; i++) {
			//System.out.println("Reader: Adding file to list: "+fileList[i]);
			loadMapFromFile(fileList[i], map, maxEntries, type);
		}
	}
	
	public static void loadMapFromFile(File iFile, ContentTable map, int maxEntries, ContentValue.VALTYPE type) {

		try {
			String iFileName = iFile.getCanonicalPath();
			String mimeType = IO.getMimeType("file://"+iFileName);
			FILETYPE fileType = FILETYPE.PLAIN;
			if (iFileName.endsWith(".gz")) fileType = FILETYPE.GZIP;
			System.out.println("mimeType: "+mimeType);
			System.out.println("inferred: "+fileType.toString());
			System.out.println("Reading : "+iFileName);
			
			BufferedReader bfr;
			if (fileType.equals(FILETYPE.GZIP)) {
				bfr = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(iFileName))));
			} else {
				bfr = new BufferedReader(new FileReader(iFileName));
			}
			
			long count = 0;
			String line = bfr.readLine();
			while (line != null) {
				count++;
				if (count % 100000 == 0) System.out.println("Reading Line: "+count);
				String[] fields = line.split("\\t");
				
				if ( (type.equals(ContentValue.VALTYPE.TABLE) && (fields.length != 3)) ||
					 (type.equals(ContentValue.VALTYPE.SCORE) && (fields.length != 2)) ) {
					throw new RuntimeException("Wrong format in data input file");
				}
				
				if (type.equals(ContentValue.VALTYPE.TABLE)) {
					String item1 = fields[0];
					String item2 = fields[1];
					Double score = Double.parseDouble(fields[2]);
			
					if (((ContentTable_HashMap) map).get(item1) == null) {
						ContentValue newInner = new ContentValue_Table();
						((ContentTable_HashMap) map).put(item1, newInner);
					}
					ContentValue inner = ((ContentTable_HashMap) map).get(item1);
					if ( ((ContentValue_Table) inner).size() < maxEntries) ((ContentValue_Table) inner).put(item2, score);
				} else {
					String item1 = fields[0];
					Long score = Long.parseLong(fields[1]);
					ContentValue inner = new ContentValue_Score();
					((ContentValue_Score) inner).setScore(score);
					((ContentTable_HashMap) map).put(item1, inner);
				}
				line = bfr.readLine();
			}
			//tableDump(DUMP_FILE+"_"+iFileName.substring(iFileName.lastIndexOf('/')+1), map);
			bfr.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}

	/*
	public static void tableDump(String fileName, ContentTable map) {
		
		System.out.println("Writing Hash Test Dump");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			for (String key : map.keySet()) {
				bw.write(key+"\n");
			}
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	*/
}

class FileExtensionFilter implements FilenameFilter {

	private String extension;
	    
	public FileExtensionFilter(String extension) {
		this.extension = ("." + extension).toLowerCase();
	}
	        
	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(extension);
	}
}