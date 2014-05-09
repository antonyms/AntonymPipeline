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

package com.ibm.sai.dca.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.HashMap;
import java.util.Random;

import com.ibm.sai.dca.common.GeneralizedPacketing;
import com.ibm.sai.dca.common.ClusterConfig;

public class TableClient {

	protected ClusterConfig clusterConfig;
	private String tableID;
	private Random rand;
	private HashMap<String, Socket> connections;
	private HashMap<String, DataInputStream> dataInputStreams;
	private HashMap<String, DataOutputStream> dataOutputStreams;

	public TableClient(ClusterConfig clusterConfig, String tableID) {
		this.clusterConfig = clusterConfig;
		this.tableID = tableID;
		init();
	}

	private void init() {
		
		this.connections = new HashMap<String, Socket>();
		this.dataInputStreams = new HashMap<String, DataInputStream>();
		this.dataOutputStreams = new HashMap<String, DataOutputStream>();
		this.rand = new Random();
		
		for (String serverName : clusterConfig.getDefinedServersForTableID(tableID)) {
			try {
				Socket s = new Socket(serverName, clusterConfig.getPortNumber(tableID));
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());

				this.connections.put(serverName, s);
				this.dataInputStreams.put(serverName, in);
				this.dataOutputStreams.put(serverName, out);
			} catch (Exception e) {
				System.out.println("TableClient: Initialization Exception");
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}
	
	public void shutdown() {
		
		for (String serverName : connections.keySet()) {
			try {
				this.connections.get(serverName).close();
				System.out.println("Connection to "+serverName+"was closed");
			} catch (Exception e) {
				System.err.println("Couldn't close connection to "+serverName);
				e.printStackTrace();
			}
		}
	}
	

	public Vector<String> getRawData(String serverName, String query) {
		DataInputStream in = this.dataInputStreams.get(serverName);
		DataOutputStream out = this.dataOutputStreams.get(serverName);
		Vector<String> resultLines = new Vector<String>();
		//long startTime, writeTime, write1Time, writeN1Time, writeN2Time, endTime;
		try {
			//startTime = System.currentTimeMillis();
			out.writeUTF(query);
			//writeTime = System.currentTimeMillis();
			resultLines = GeneralizedPacketing.receiveData(in);
			//endTime = System.currentTimeMillis();
			//System.out.println("Write: "+(writeTime-startTime)+"\nRead: "+(endTime-writeTime)+"\nTot: "+(endTime-startTime));
		} catch (IOException ioe) {
			System.out.println("getRawData got IOException");
			ioe.printStackTrace();
			throw new RuntimeException();
		}
		return resultLines;
	}
	
	public String getFirstServerName() {
		return (String) clusterConfig.getDefinedServersForTableID(tableID).toArray()[0];
	}
	
	public String getRandomServerName() {
		Object serverNames[] = clusterConfig.getDefinedServersForTableID(tableID).toArray();
		return (String) serverNames[rand.nextInt(serverNames.length)];
	}
	
	@SuppressWarnings("unused")
	private TableClient() {
	}
	
}
