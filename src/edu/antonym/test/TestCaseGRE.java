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
import edu.antonym.SimpleVocab;
import edu.antonym.Util;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

/**
 * This is the test case for gre. 
 * The first word is the target word.
 * The last word is the true results.
 * Calculate the cosine-similarity of the five choices of words to the target word.
 * Display the word with smallest distance to -1. 
 * Compare the test result with the true answer.
 * Calculate the accuracy at last.
 * 
 */
public class TestCaseGRE implements MetricEvaluator {
	
	@Override
	public double score(WordMetric metric) throws IOException {
		return score(metric, "gre_testset.txt");
	}
	
	public double score(WordMetric metric, String test0) throws IOException {
		
		String test_folder = "data/test-data/";
		String result_folder = "data/result-data/";
		new File(result_folder).mkdirs();		
		
		
		ArrayList<String> gres = new ArrayList<String>();
		double acc = 0.0d;
		gres.add(test0);	//add more file if needed
		
		Vocabulary smallvocab = new SimpleVocab(new File("/home/jingwei/workspace/pmf/roget_mm-voc"), -1);
		Vocabulary vocab = metric.getVocab();
		int OOV = vocab.OOVindex();
		
		for(int i=0;i<gres.size();i++){
			int accuracy_pos = 0;
			int accuracy_neg = 0;
			int all = 0;
			
			FileWriter out = new FileWriter(result_folder+ gres.get(i) ); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test_folder+gres.get(i))));
			String line = br.readLine();
			while(line!=null){
				all++;
				int index_target = line.indexOf(":");
				int index_answer = line.indexOf("::");
				String target = line.substring(0,index_target);
				String choice = line.substring(index_target+2,index_answer);
				String answer = line.substring(index_answer+3,line.length());
				String[] choices = choice.split("\\s+");
				
				int wordID = vocab.lookupWord(target);
				if (wordID != OOV) {
					if (vocab.lookupWord(answer) == OOV) {
						line = br.readLine();
						continue;
					}
					
					out.append(target+" ");
				}
				else {
					out.append("Cannot find target: " + target);
					out.append(" WRONG ");
					//accuracy_neg++;
					out.append("\n");

					line = br.readLine();
					continue;
				}
				if(vocab.lookupWord(answer)==OOV) {
					line=br.readLine();
					continue;
				}
				
				// calculate dist to target from each choice
				double closest = Double.MAX_VALUE;
				String test_answer = null;
				
				for(int j=0;j<choices.length;j++){
					if(smallvocab.lookupWord(choices[j])==OOV) {
					continue;
					}
					int choiceID = vocab.lookupWord(choices[j]);
					if (choiceID != OOV) {
						out.append(choices[j]+" ");
						double cs = metric.similarity(wordID, choiceID);
						if(cs<closest){
							closest = cs;
							test_answer = choices[j];
						}
					}
					else {
						out.append("Cannot find: " + choices[j]+ " ");
					}
				}	
				
				out.append("result: " +test_answer);
				out.append("("+Double.toString(closest)+") ");
				out.append("true: "+answer);
				if (vocab.lookupWord(answer) != OOV)
					out.append("("+metric.similarity(wordID, vocab.lookupWord(answer))+") ");
				
				//accuracy
				if(test_answer.equals(answer)){
					out.append(" RIGHT ");
					accuracy_pos++;
				}
				else{
					out.append(" WRONG ");
					accuracy_neg++;
				}
				out.append("\n");
				
				line = br.readLine();
			}
			
			out.append("\n");
			double accuracy = 0.0;
			double recall = 0.0;
			if((accuracy_pos+accuracy_neg)!=0){
				accuracy = ((double)accuracy_pos)/(double)(accuracy_pos+accuracy_neg);
				recall = ((double)accuracy_pos)/(double)(all);
			}
<<<<<<< HEAD
			acc+=accuracy;
=======
			double f1 = 2*accuracy*recall/(accuracy+recall);
			acc+=recall;
>>>>>>> 2c964b6... add bptf vector
			out.append("Final accuracy = "+ Double.toString(accuracy));
			System.out.println("[GRE TEST] Final accuracy = " + Double.toString(accuracy) + "\t" + Double.toString(recall) + "\t" + Double.toString(f1));
			System.out.println(""+accuracy_pos+"\t"+(accuracy_pos+accuracy_neg));
			br.close();
			out.close();
		}
		
		return acc;
	}
	
	
}
