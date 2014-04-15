package edu.antonym.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.RawPILSAVec;
import edu.antonym.Util;
import edu.antonym.prototype.EmbeddingEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

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
public class TestCaseGRE implements EmbeddingEvaluator {
	
	@Override
	public List<Float> score(VectorEmbedding pilsaVect) throws IOException {
		
		String test_folder = "test-data/";
		String result_folder = "result-data/";	// create this folder in advance for output
		
		String test0 = "gre_testset.txt";
		
		ArrayList<String> gres = new ArrayList<String>();
		List<Float> acc = new ArrayList<Float>();
		gres.add(test0);	//add more file if needed
		
		
		Vocabulary vocab = pilsaVect.getVocab();
		int OOV = vocab.OOVindex();
		
		for(int i=0;i<gres.size();i++){
			int accuracy_pos = 0;
			int accuracy_neg = 0;
			
			FileWriter out = new FileWriter(result_folder+ gres.get(i) ); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test_folder+gres.get(i))));
			String line = br.readLine();
			while(line!=null){
				int index_target = line.indexOf(":");
				int index_answer = line.indexOf("::");
				String target = line.substring(0,index_target);
				String choice = line.substring(index_target+2,index_answer);
				String answer = line.substring(index_answer+3,line.length());
				String[] choices = choice.split("\\s+");
				
				float[] target_vec = new float[300];
				int wordID = vocab.lookupWord(target);
				if (wordID != OOV) {
					target_vec = pilsaVect.getVectorRep(wordID);
					out.append(target+" ");
				}
				else {
					out.append("Cannot find target: " + target);
					out.append(" WRONG ");
					accuracy_neg++;
					out.append("\n");
					line = br.readLine();
					continue;
				}
				
				// calculate dist to target from each choice
				double closest = Double.MAX_VALUE;
				String test_answer = null;
				
				for(int j=0;j<choices.length;j++){
					wordID = vocab.lookupWord(choices[j]);
					if (wordID != OOV) {
						float[] vect = pilsaVect.getVectorRep(wordID);
						out.append(choices[j]+" ");
						double cs = Util.cosineSimilarity(target_vec, vect);
						double current_dist = Math.abs(-1-cs);
						if(current_dist<closest){
							closest = current_dist;
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
			if((accuracy_pos+accuracy_neg)!=0){
				accuracy = ((double)accuracy_pos)/(double)(accuracy_pos+accuracy_neg);
			}
			acc.add((float) accuracy);
			out.append("Final accuracy = "+ Double.toString(accuracy));
			System.out.println("[GRE TEST] Final accuracy = " + Double.toString(accuracy));
			br.close();
			out.close();
		}
		
		return acc;
	}
	
	
}
