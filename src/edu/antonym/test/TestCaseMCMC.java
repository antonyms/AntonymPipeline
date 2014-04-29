package edu.antonym.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class TestCaseMCMC implements MetricEvaluator {

	/*
	 * important notes: adjust n or gamma if you want the algorithm to go faster
	 * 
	 * Example usage:
	 * 	TestCaseMCMC n = new TestCaseMCMC(input);
	 *	ArrayList<ArrayList<String>> r = n.get2cluster(metric);
	 *  // print out result cluster 1, cluster 2, word not found
	 *	for(int i=0;i<r.size();i++){
	 *		for(int j=0;j<r.get(i).size();j++){
	 *			System.out.print(r.get(i).get(j)+" ");
	 *		}
	 *		System.out.println();
	 *	}
	 */
	public ArrayList<String> wordsense = new ArrayList<String>();
	
	public TestCaseMCMC(ArrayList<String> input){
		wordsense = new ArrayList<String>(input);
	}
	
	@Override
	public double score(WordMetric metric) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<ArrayList<String>> get2cluster(WordMetric metric) throws IOException{
		
		Vocabulary vocab = metric.getVocab();
		int OOV = vocab.OOVindex();
		int size = wordsense.size();
		
		double best = Double.MIN_VALUE;	// maximum likelihood
		
		ArrayList<Integer> c1 = new ArrayList<Integer>();
		ArrayList<Integer> c2 = new ArrayList<Integer>();
		ArrayList<String> notfound = new ArrayList<String>();
		HashMap<Integer,String> clusterword = new HashMap<Integer,String>();
		
		// initialize 2 clusters
		int i=0;
		for(i=0;i<size/2;i++){
			String w = wordsense.get(i);
			int wordID = vocab.lookupWord(w);
			if (wordID != OOV) {
				c1.add(wordID);
				clusterword.put(wordID, w);
			} else {
				notfound.add(w);
			}
		}
		while(i<size){
			String w = wordsense.get(i);
			int wordID = vocab.lookupWord(w);
			if (wordID != OOV) {
				c2.add(wordID);
				clusterword.put(wordID, w);
			} else {
				notfound.add(w);
			}
			i++;
		}
		
		for(int it=0;it<40;it++){	// run clustering 40 times and find optimum
			ArrayList<Integer> cluster1 = new ArrayList<Integer>(c1);
			ArrayList<Integer> cluster2 = new ArrayList<Integer>(c2);
			
			double difference = Double.MAX_VALUE;
			int previousword = 0;
			int n=0;	
			while(n<18){	// if the distribution is not changed for n times
				//	randomly pick ID
				boolean inCluster1 = false;
				Random rand = new Random();
			    int randomNum = rand.nextInt((cluster1.size()+cluster2.size() - 1) + 1);
			    int t;
			    if(randomNum>cluster1.size()-1){
			    	t = cluster2.get(randomNum-cluster1.size());
			    }
			    else{
			    	t = cluster1.get(randomNum);
			    	inCluster1 = true;
			    }

				if(previousword!=t){	// avoid the case where the word found are the same as previous one 
					double sim1 = 0.0d;
					double sim2 = 0.0d;
					double gamma = 5.0d;
					
					for(int k=0;k<cluster1.size();k++){
						if(cluster1.get(k)==t){
							continue;
						}
						sim1 += metric.similarity(cluster1.get(k),t);
					}
					for(int k=0;k<cluster2.size();k++){
						if(cluster2.get(k)==t){
							continue;
						}
						sim2 += metric.similarity(cluster2.get(k),t);
					}
					
					if(inCluster1){
						difference = sim2 - sim1;
					}
					else{
						difference = sim1 - sim2;
					}
					
					// calculating probability
					double p = Math.exp(gamma*difference);
					if(p>=1.0 || Math.random()<p){
						n=0;
						if(inCluster1){
							cluster1.remove(randomNum);
							cluster2.add(t);
						}
						else{
							cluster2.remove(randomNum-cluster1.size());
							cluster1.add(t);
						}
					}
					else{
						n++;
					}
					previousword = t;
				}
			}
			
			// calculate likelihood
			double likelihood = 0.0d;
			
			for(int k=0;k<cluster1.size()-1;k++){
				for(int l=k+1;l<cluster1.size();l++){
					likelihood += metric.similarity(cluster1.get(k),cluster1.get(l));
				}
			}
			for(int k=0;k<cluster1.size();k++){
				for(int l=0;l<cluster2.size();l++){
					likelihood -= metric.similarity(cluster1.get(k),cluster2.get(l));
				}
			}
			for(int k=0;k<cluster2.size()-1;k++){
				for(int l=k+1;l<cluster2.size();l++){
					likelihood += metric.similarity(cluster2.get(k),cluster2.get(l));
				}
			}
			
			if(likelihood>best){
				//System.out.print("changed! "+ likelihood+" ");
				best = likelihood;
				c1 = new ArrayList<Integer>(cluster1);
				c2 = new ArrayList<Integer>(cluster2);
			}
		}
		
		System.out.println("maximum likelihood: "+ best);
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		ArrayList<String> result1 = new ArrayList<String>();
		ArrayList<String> result2 = new ArrayList<String>();
		for(int j=0;j<c1.size();j++){
			result1.add(clusterword.get(c1.get(j)));
		}
		for(int j=0;j<c2.size();j++){
			result2.add(clusterword.get(c2.get(j)));
		}
		result.add(result1);
		result.add(result2);
		result.add(notfound);
		return result;
	}

}
