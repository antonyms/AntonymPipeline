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

package com.ibm.sai.dca.common;

import java.util.HashMap;
import java.util.Vector;
//import java.util.Random;
import java.util.HashSet;
import java.util.Set;

import com.ibm.sai.dca.common.IO;

//	0		1		2		3			4		5		6																		7							8
// TableId1	TABLE	50000	32354345	1500	200		bluej1[0-999], bluej2[0-999], bluej3[1000-1999], bluej4[1000-1999]		TableFile1, TableFile2		NONE

public class ClusterConfig {

	private static HashMap<String, TableInfo> clusterTable;
	private static boolean init = false;
	//private static Random rand;
	
	public ClusterConfig(String fileName) {
		if (!init) {
			clusterTable = loadTable(fileName);
			//rand = new Random();
			init = true;
		}
	}
	
	@SuppressWarnings("unused")
	private ClusterConfig() {
	}
	
	public Set<String> getTableIDsForServer(String serverName) {
		
		Set<String> outTable = new HashSet<String>();
		for (String tableID : clusterTable.keySet()) {
			if (clusterTable.get(tableID).serverRangeTable.containsKey(serverName)) outTable.add(tableID);
		}
		return outTable;
	}
	
	public Set<String> getDefinedServersForTableID(String tableID) {
		HashSet<String> outSet = new HashSet<String>();
		for (String serverName : clusterTable.get(tableID).serverRangeTable.keySet()) outSet.add(serverName);
		return outSet;
	}
	
	public int getTableSize(String tableID) {
		return clusterTable.get(tableID).tableSize;
	}
	
	public String[] getTableFileNames(String tableID) {
		return clusterTable.get(tableID).tableFileNames;
	}
	
	public int getMaxInnerValues(String tableID) {
		return clusterTable.get(tableID).maxInnerValues;
	}
	
	public int getPortNumber(String tableID) {
		return clusterTable.get(tableID).portNumber;
	}
	
	public Set<String> getAllTableIDs() {
		return clusterTable.keySet();
	}
	
	public int getCacheSize(String tableID) {
		return clusterTable.get(tableID).clientCacheSize;
	}
	
	public String getFileExtensionFilter(String tableID) {
		return clusterTable.get(tableID).fileExtensionFilter;
	}
	
	public ContentValue.VALTYPE getTableValType(String tableID) {
		return clusterTable.get(tableID).innerValType;
	}
	
	private HashMap<String, TableInfo> loadTable(String fileName) {
		
		System.out.println("Loading ClusterConfig Table");
		HashMap<String, TableInfo> clusterTable = new HashMap<String, TableInfo>();
		Vector<String> lines = IO.loadFileByLines(fileName);
		for (String line : lines) {
			System.out.println("  line: "+line);
			if (line.startsWith("#")) continue;
			String[] fields = line.split("\\t+");
			if (fields.length == 9) {
				TableInfo tableInfo = new TableInfo();
				String innerVal = fields[1];
				if ("TABLE".equals(innerVal)) {
					tableInfo.innerValType = ContentValue.VALTYPE.TABLE;
				} else if ("SCORE".equals(innerVal)) {
					tableInfo.innerValType = ContentValue.VALTYPE.SCORE;
				} else {
					throw new RuntimeException("Wrong format in Cluster Config Table file");
				}
				tableInfo.portNumber = Integer.parseInt(fields[2]);
				tableInfo.tableSize = Integer.parseInt(fields[3]);
				tableInfo.clientCacheSize = Integer.parseInt(fields[4]);
				tableInfo.maxInnerValues = Integer.parseInt(fields[5]);
				tableInfo.tableFileNames = fields[7].split("\\,\\s*");
				tableInfo.fileExtensionFilter = fields[8];
				String[] serverFields = fields[6].split("\\,\\s*");
				tableInfo.serverRangeTable = new ServerRangeTable();
				for (int i = 0; i < serverFields.length; i++) {
					int openBracket = serverFields[i].indexOf('[');
					int closeBracket = serverFields[i].indexOf(']');
					String serverName = serverFields[i].substring(0, openBracket);
					String lineRange = serverFields[i].substring(openBracket+1, closeBracket);
					tableInfo.serverRangeTable.put(serverName, lineRange);
				}
				clusterTable.put(fields[0], tableInfo);
				System.out.println(" Got Table Config: "+fields[0]);
			} else {
				System.err.println("Wrong #fields="+fields.length+" in line: "+line);
			}
		}
		return clusterTable;
	}	
	
	private class TableInfo
	{
		ContentValue.VALTYPE innerValType;
		int portNumber;
		int tableSize;
		int clientCacheSize;
		int maxInnerValues;
		String tableFileNames[];
		String fileExtensionFilter;
		ServerRangeTable serverRangeTable;
	}
	
	@SuppressWarnings("serial")
	private class ServerRangeTable extends HashMap<String, String>
	{
		// NOT ENCODED YET
		
		@SuppressWarnings("unused")
		public int getMin(String server) {
			return 0;
		}
		
		@SuppressWarnings("unused")
		public int getMax(String server) {
			return 0;
		}
	}
	
}

