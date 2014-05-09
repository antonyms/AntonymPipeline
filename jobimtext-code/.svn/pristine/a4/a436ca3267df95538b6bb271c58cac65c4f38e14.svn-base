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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.lang.Class;

public class ServerListener<D> extends Thread {

	private int listenPort;
	private String tableID;
	private Class<D> serverThreadClass;
	
	public ServerListener(int listenPort, String tableID, Class<D> serverThreadClass) {
		this.listenPort = listenPort;
		this.tableID = tableID;
		this.serverThreadClass = serverThreadClass;
	}
	
	public void run() {

		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(listenPort);
		} catch(IOException e) {
			System.out.println("Server Init Failed");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			while(true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("\nAccepted "+serverThreadClass.getName()+" Connection from: "+clientSocket.getInetAddress());
				ServerThread thread = (ServerThread) serverThreadClass.getConstructor(java.net.Socket.class, java.lang.String.class).newInstance(clientSocket, tableID);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

