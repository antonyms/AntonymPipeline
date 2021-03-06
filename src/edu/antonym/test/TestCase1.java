package edu.antonym.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.RawPILSAVec;
import edu.antonym.Util;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

/**
 * This is the test case for antonyms, ant0.txt and ant1.txt.
 * Each line represents one single test.
 * If there are two columns of words, the third column corresponds to the cosine-similarity of the two words.
 * If there are three columns of words, the fourth column represents cosine-similarity of word0 and word1,
 * fifth column word0 and word2, sixth column word1 and word2.
 * Mean squared error of antonym/synonym is also measured at the last.
 * ant: -1
 * syn: 1
 * 
 */
public class TestCase1 implements MetricEvaluator{
	
	@Override
	public double score(WordMetric metric) throws IOException {
		
		String test_folder = "data/test-data/";
		String result_folder = "data/result-data/";
		new File(result_folder).mkdirs();
		
		String ant0 = "ant0.txt";
		String ant1 = "ant1.txt";
		
		ArrayList<String> ants = new ArrayList<String>();
		double acc =0.0d;
		ants.add(ant0);
		ants.add(ant1);		//add more file if needed
		
		Vocabulary vocab = metric.getVocab();
		int OOV = vocab.OOVindex();
		
		for(int i=0;i<ants.size();i++){
			int ant_num = 0;
			int syn_num = 0;
			double mse_ant = 0.0;
			double mse_syn = 0.0;
			
			FileWriter out = new FileWriter(result_folder+ ants.get(i) ); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test_folder+ants.get(i))));
			String line = br.readLine();
			while(line!=null){
				String[] words = line.split("\\s+");
				ArrayList<Integer> wids = new ArrayList<Integer>();
				for(int j=0;j<words.length;j++){
					int wordID = vocab.lookupWord(words[j]);
					if (wordID != OOV) {
						wids.add(wordID);
						out.append(words[j]+" ");
					}
					else {
						out.append("No such word: " + words[j]+ " ");
					}
				}	
				if(wids.size()==2 && words.length==2){
					double cs = metric.similarity(wids.get(0), wids.get(1));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs));
				}
				else if(wids.size()==3){
					double cs = metric.similarity(wids.get(0), wids.get(1));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs)+" ");
					cs =metric.similarity(wids.get(0), wids.get(2));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs)+" ");
					cs = metric.similarity(wids.get(1), wids.get(2));
					syn_num++;
					mse_syn+= Math.pow(1-cs,2);
					out.append(Double.toString(cs)+" ");
				}
							
				out.append("\n");
				line = br.readLine();
			}
			mse_ant = mse_ant/ant_num;
			mse_syn = mse_syn/syn_num;
			acc-=mse_ant;
			out.append("\n");
			out.append("Mean squared error for antonym is: " + mse_ant +"\n");
			System.out.println("[SIMPLE ANT TEST" + i + "] Mean squared error for antonym is: " + Double.toString(mse_ant));
			if(syn_num!=0){
				out.append("Mean squared error for synonym is: " + mse_syn +"\n");
				System.out.println("[SIMPLE ANT TEST" + i + "] Mean squared error for synonym is: " + Double.toString(mse_syn));
			}
			out.close();
			br.close();
		}
		return acc;
	}
}
