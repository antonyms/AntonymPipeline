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

import java.util.HashMap;

import com.ibm.sai.dca.common.ClusterConfig;
import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.server.ServerListener;

public class Server {

	private final float TABLE_LOAD_FACTOR = (float) 0.75; 
	private String serverHostName;
	protected static ClusterConfig clusterConfig;
	protected static HashMap<String, ContentTable> contentTables;
	@SuppressWarnings("rawtypes")
	private HashMap<String, ServerListener> sysListeners;
	
	@SuppressWarnings("rawtypes")
	public Server(String clusterConfigFileName) {
		
		serverHostName = System.getenv("HOSTNAME");
		clusterConfig = new ClusterConfig(clusterConfigFileName);
		contentTables = new HashMap<String, ContentTable>();
		sysListeners = new HashMap<String, ServerListener>();
		
		for(String tableID : clusterConfig.getTableIDsForServer(serverHostName)) {
			ContentTable ct;
			//if (clusterConfig.getTableValType(tableID).equals(ContentValue.VALTYPE.TABLE)) {
				ct = new ContentTable_HashMap(new Double(Math.ceil(clusterConfig.getTableSize(tableID)/TABLE_LOAD_FACTOR)).intValue());
			//} else {
				// VALTYPE = SCORE
				//ct = new ContentTable_SuffixTree();
			//}
			contentTables.put(tableID, ct);
			sysListeners.put(tableID, new ServerListener<ServerThread_GeneralInstance>(clusterConfig.getPortNumber(tableID), tableID, ServerThread_GeneralInstance.class)); 
		}
	}
	
	private void startServer() {
		
		System.out.println("Start Server");
		System.out.println("contentTables size: "+contentTables.size());
		for (String tableID : contentTables.keySet()) {
			System.out.println("Loading Table: "+tableID);
			ContentReader.loadMap(clusterConfig.getTableFileNames(tableID), contentTables.get(tableID), clusterConfig.getMaxInnerValues(tableID), clusterConfig.getTableValType(tableID), clusterConfig.getFileExtensionFilter(tableID));
		}

		for (String tableID : contentTables.keySet()) {
			if (clusterConfig.getTableValType(tableID).equals(ContentValue.VALTYPE.TABLE)) {
				System.out.println(tableID+" size: "+ ((ContentTable_HashMap) contentTables.get(tableID)).size());
			} else {
				System.out.println(tableID+" is SCORE type");
			}
		}

		System.out.println("Server Maps Loading Done");
		System.out.println("Server Listener Starting.");

		for (String tableID : sysListeners.keySet()) {
			sysListeners.get(tableID).start();
		}
	}
	
	@SuppressWarnings("unused")
	private Server() {
	}
	
	public static void main(String[] args) {
		
		Server s = new Server(args[0]);
		s.startServer();
	}
}
