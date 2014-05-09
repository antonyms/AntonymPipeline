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

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.ibm.sai.dca.client.CacheMemory;
import com.ibm.sai.dca.client.TableClient;
import com.ibm.sai.dca.common.ClusterConfig;
import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.common.ContentValue.VALTYPE;
import com.ibm.sai.dca.common.ContentValue_Table;
import com.ibm.sai.dca.common.ContentValue_Score;

public class DCA_Client {

	private static ClusterConfig clusterConfig;
	private static final boolean ENABLE_CACHE = true;
	private final Object lock_cm = new Object();
	private final Object lock_cc = new Object();
	
	private static HashMap<String, CacheMemory<String, ContentValue>> globalCache;
	private HashMap<String, TableClient> tableClients;
	
	public DCA_Client(String clusterConfigFileName) throws IOException {
		synchronized (lock_cc) {
			if (clusterConfig == null) {
				clusterConfig = new ClusterConfig(clusterConfigFileName); 
			}
		}
		synchronized (lock_cm) {
			if (globalCache == null) {
				globalCache = new HashMap<String, CacheMemory<String, ContentValue>>();
			}
		}
		tableClients = new HashMap<String, TableClient>();
		for (String tableID : clusterConfig.getAllTableIDs()) {
			tableClients.put(tableID, new TableClient(clusterConfig, tableID));
			synchronized (lock_cm) {
				if (globalCache.get(tableID) == null) {
					globalCache.put(tableID, new CacheMemory<String, ContentValue>(clusterConfig.getCacheSize(tableID)));
				}
			}
		}
	}
	
	public ContentValue getContentValue(String tableID, String key, ContentValue.VALTYPE type) {

		//long startTime = System.currentTimeMillis();
		ContentValue outValue = null;
		String cache_hit = "N";
		if (ENABLE_CACHE) {
			ContentValue cacheEntry = globalCache.get(tableID).get(key);
			if (cacheEntry != null) {
				outValue = cacheEntry;
				cache_hit = "Y";
			}
		}
		if (outValue == null) {
			if (type == VALTYPE.TABLE) {
				outValue = new ContentValue_Table();
			} else if (type == VALTYPE.SCORE) {
				outValue = new ContentValue_Score();
			}
			String serverName = tableClients.get(tableID).getRandomServerName();
			Vector<String> result = tableClients.get(tableID).getRawData(serverName, key);
			for (String line : result) {
				String[] fields = line.split("\\t");
				if (type.equals(ContentValue.VALTYPE.TABLE)) {
					((ContentValue_Table) outValue).put(fields[0], Double.parseDouble(fields[1]));
				} else if (type.equals(ContentValue.VALTYPE.SCORE)) {
					// In this case, the for cycle does exactly ONE iteration.
					((ContentValue_Score) outValue).setScore(Long.parseLong(fields[0]));
				}
			}
			if (ENABLE_CACHE) globalCache.get(tableID).put(key, outValue);
			//cm.inc_frame();
		}
		//long getTime = System.currentTimeMillis();
		
		//System.out.println("Client: word: "+element+"; results:"+outTable.size()+"; CacheHit: "+cache_hit+"; getTime: "+(getTime-startTime));
		return outValue;
	}
	
	public void shudtown() {
		for (String tableID : tableClients.keySet()) {
			tableClients.get(tableID).shutdown();
		}
	}
	
	public static void main (String args[]) throws IOException {
		DCA_Client dca = new DCA_Client(args[0]);
		ContentValue.VALTYPE type = ContentValue.VALTYPE.TABLE;
		ContentValue w2w = dca.getContentValue("Word2WordSim", "(54)", type);
		ContentValue c2c = dca.getContentValue("Ctx2CtxSim", "<IsA_targetnoun:Anti-Social Personality Disorder", type);
		ContentValue w2c = dca.getContentValue("Word2CtxSim", "0.3", type);
		
		w2w.dump();
		c2c.dump();
		w2c.dump();
	}
}
