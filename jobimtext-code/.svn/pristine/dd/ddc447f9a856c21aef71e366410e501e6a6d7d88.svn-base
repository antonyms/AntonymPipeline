/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.consistency.validate.testcases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.focus.FocusIndicator;
import com.ibm.bluej.consistency.inference.SingleMAP;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.SparseVectorTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.HashMapUtil;
import com.ibm.bluej.util.common.MutableDouble;
import com.ibm.bluej.util.common.RandomUtil;

//Better than gmeans - even in the simplest version
//do a speed comparison though
public class JMeans {
	//Weight of /users/glassm/Medical/Finding/clusterIdToWordsG.ser.gz = 566.6734241380217
	//Computation time: 
	//	CPU Usage: user = 3492 seconds 698280 us, system = 0 seconds 988062us
	//	ELAPSED Time: 3504 seconds 446818 us

	//Weight of /users/glassm/Medical/Finding/clusterIdToWordsJ.ser.gz = 953.9918448657824
	//Completed 1115.1927164533304 flips per sec, 448.353 total (+320 sec to read data, gmeans uses easier format so it's faster at reading data)
	//Best weight = 953.9918448657779

	//CONSIDER: this could be zero...
	public static final double EPSILON = 0.1;
	
	private static final StringTerm cluster = new StringTerm("cluster");
	private static final StringTerm hasDist = new StringTerm("hasDist");
	private static final StringTerm inCluster = new StringTerm("inCluster");
	
	//so JMeans crushes GMeans?
	//verify that ours is a legit clustering
	
	public static void main(String[] args) {
		String vectorFile = "/users/glassm/Medical/Finding/wordToContextVectorPMIT.ser.gz";
		String jClusterFile = "/users/glassm/Medical/Finding/clusterIdToWordsJ2.ser.gz";
		String gClusterFile = "/users/glassm/Medical/Finding/clusterIdToWordsG.ser.gz";
		int numClusters = 40;
		int numFlips = 500000;
		jmeans(vectorFile, jClusterFile, numClusters, numFlips);
		
		compareGMeans(vectorFile, gClusterFile, null);
	}
	
	
	public static void jmeans(String vectorFile, String clusterFile, int numClusters, int numFlips) {
		//TODO: modify to start from last cluster file
		
		SGSearch.loadDescription(FileUtil.readResourceAsString("jmeans.con"));
		
		FocusIndicator focusG = SGSearch.getFocusIndicators().get("g");
		focusG.setActive(false);
		
		//create numClusters
		StringTerm[] cids = new StringTerm[numClusters];
		for (int i = 0; i < numClusters; ++i) {
			cids[i] = new StringTerm("c"+i);
			SGSearch.nowTrue(new ScanTerm(cluster, cids[i]));
		}
		
		ArrayList<ATerm>[] clusterAssignments = new ArrayList[numClusters];
		for (int i = 0; i < numClusters; ++i) {
			clusterAssignments[i] = new ArrayList<ATerm>();
		}
		
		long time = System.currentTimeMillis();
		//assign each vector randomly to a cluster
		HashMap<String, HashMap<String, MutableDouble>> dwordToContextVector = 
				(HashMap<String, HashMap<String, MutableDouble>>) FileUtil.loadObjectFromFile(vectorFile);
		System.out.println("Read data "+(System.currentTimeMillis()-time)/1000.0);
		time = System.currentTimeMillis();
		for (Map.Entry<String, HashMap<String,MutableDouble>> e : dwordToContextVector.entrySet())	{
			ATerm vid = new StringTerm(e.getKey());
			SGSearch.nowTrue(new ScanTerm(hasDist, vid, new SparseVectorTerm((HashMap)e.getValue())));
			int whichCluster = RandomUtil.randomInt(0, numClusters);
			clusterAssignments[whichCluster].add(vid);
			SGSearch.nowTrue(new ScanTerm(inCluster, vid, cids[whichCluster]));
			//TODO: initialize clusters to be far apart
		}
		System.out.println("Initialized "+(System.currentTimeMillis()-time)/1000.0);
		//TODO: maintain an "unassigned" list
		// gradually assign vectors to clusters, keeping clusters tight and centroids distant
		// most important to have frequent words assigned
		// cluster 1000 most freq words and track the 1M marginals?
		// even if you have those marginals it doesn't really solve the problem
		// can we specify our centroids in advance? - not that hard really
		focusG.setActive(true);focusG.setActive(false);
		
		time = System.currentTimeMillis();
		SGSearch.setTransaction(true);
		SingleMAP map = new SingleMAP(SGSearch.getCRFState());
		SGSearch.setMAP(map);
		int lastBestReport = 0; double lastBestValue = Double.NEGATIVE_INFINITY;
		//keep track of where they are and move them around randomly
		for (int fi = 0; fi < numFlips; ++fi) {
			int fromCluster = 0;
			do {
				fromCluster = RandomUtil.randomInt(0, numClusters);
			} while (clusterAssignments[fromCluster].isEmpty());
			
			int fromNdx = RandomUtil.randomInt(0, clusterAssignments[fromCluster].size());
			ATerm vid = clusterAssignments[fromCluster].get(fromNdx);
			int toCluster = 0;
			do {
				toCluster = RandomUtil.randomInt(0, numClusters);
			} while (fromCluster == toCluster);
			SGSearch.nowFalse(new ScanTerm(inCluster, vid, cids[fromCluster]));
			SGSearch.nowTrue(new ScanTerm(inCluster, vid, cids[toCluster]));
			if (SGSearch.epsilonSoft(0)) { //CONSIDER: be less greedy?
				clusterAssignments[fromCluster].remove(fromNdx);
				clusterAssignments[toCluster].add(vid);
				//CONSIDER: only checkBest on focus? - doesn't matter in this case
				if(map.getBestWeight() > lastBestValue && fi > lastBestReport + 500) {
					//let's report on our improvements
					System.out.println("score = "+map.getBestWeight());
					lastBestReport = fi;
					lastBestValue = map.getBestWeight();
				}
			}
			//alternate between focused and non-focused
			//or just recompute periodically (let metroHastings do your first variations)
			if (fi % 10000 == 0) {
				System.out.println("Refocus at "+fi);
				focusG.setActive(true);
				map.checkBest();
				focusG.setActive(false);
				//focusG.setActive(!focusG.isActive());
			}
		}
		double sec = (System.currentTimeMillis()-time)/1000.0;
		System.out.println("Completed "+numFlips/sec+" flips per sec, "+sec+" total");
		
		Collection<ScanTerm> best = map.getBest();
		//only care about recording the best inCluster
		HashMap<String, HashSet<String>> clusterIdToWords = new HashMap<String, HashSet<String>>();
		for (ScanTerm b : best) {
			if (b.parts[0] == inCluster) {
				HashMapUtil.addHS(clusterIdToWords, ATerm.getString(b.parts[2]), ATerm.getString(b.parts[1]));
			}
		}
		FileUtil.saveObjectAsFile(clusterFile, clusterIdToWords);	
		
		System.out.println("Best weight = "+map.getBestWeight());
	}
	
	private static void checkClusteringForLegit(Set<String> words, HashMap<String, HashSet<String>> clusterIdToWords) {
		int totalClusterSize = 0;
		HashSet<String> cwords = new HashSet<String>();
		for (HashSet<String> cluster : clusterIdToWords.values()) {
			totalClusterSize += cluster.size();
			cwords.addAll(cluster);
		}
		if (cwords.size() != totalClusterSize || totalClusterSize != words.size() || !words.containsAll(cwords)) {
			System.err.println("Not a valid clustering! "+totalClusterSize+" "+cwords.size()+" "+words.size());
		}
	}
	
	private static void compareGMeans(String vectorFile, String clusterFile, Boolean withFocus) {
		SGSearch.loadDescription(FileUtil.readResourceAsString("jmeans.con"));
		//read in GMeans results
		long time = System.currentTimeMillis();
		HashMap<String, HashMap<String, MutableDouble>> dwordToContextVector = 
				(HashMap<String, HashMap<String, MutableDouble>>) FileUtil.loadObjectFromFile(vectorFile);
		HashMap<String, HashSet<String>> clusterIdToWords = 
				(HashMap<String, HashSet<String>>) FileUtil.loadObjectFromFile(clusterFile);
		System.out.println("Read in data "+(System.currentTimeMillis()-time)/1000.0);
		System.out.println("#vectors = "+dwordToContextVector.size()+" #clusters = "+clusterIdToWords.size());
		time = System.currentTimeMillis();
		checkClusteringForLegit(dwordToContextVector.keySet(), clusterIdToWords);
		//assert them in factor graph
		for (Map.Entry<String, HashMap<String,MutableDouble>> e : dwordToContextVector.entrySet())	{
			ATerm vid = new StringTerm(e.getKey());
			SGSearch.nowTrue(new ScanTerm(hasDist, vid, new SparseVectorTerm((HashMap)e.getValue())));
		}
		System.out.println("Asserted vectors "+(System.currentTimeMillis()-time)/1000.0);
		
		FocusIndicator focusG = SGSearch.getFocusIndicators().get("g");
		if (withFocus == null || withFocus) {
			focusG.setActive(false);
		}
		
		time = System.currentTimeMillis();
		int opNum = 0;
		for (Map.Entry<String, HashSet<String>> e : clusterIdToWords.entrySet()) {
			StringTerm cid = new StringTerm(e.getKey());
			SGSearch.nowTrue(new ScanTerm(cluster, cid));
			for (String word : e.getValue()) {
				++opNum;
				if (withFocus == null && opNum % 1000 == 0) {
					focusG.setActive(true);focusG.setActive(false);
				}
				SGSearch.nowTrue(new ScanTerm(inCluster, new StringTerm(word), cid));
				if (opNum % 1000 == 0) {
					System.out.println("On word/cluster "+opNum);
				}
			}
		}
		System.out.println("Asserted cluster members "+(System.currentTimeMillis()-time)/1000.0);
		
		//check it's weight
		time = System.currentTimeMillis();
		focusG.setActive(true);
		System.out.println("Refocused "+(System.currentTimeMillis()-time)/1000.0);
		System.out.println("Weight of "+clusterFile+" = "+SGSearch.getWeight());
		
		//are we doing as well at maximizing the objective function??
		//what do our respective time curves look like?
	}
}
