package edu.antonym.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.Util;
import edu.antonym.prototype.EmbeddingEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

/**
 * This is the test case for synonyms, syn0.txt and syn1.txt.
 * Each line represents one single test.
 * Each two words of a line are compared.
 * Mean squared error of synonym is also measured at the last.
 * syn: 1
 * 
 */
public class TestCase2 implements EmbeddingEvaluator {
	
	@Override
	public List<Float> score(VectorEmbedding pilsaVect) throws IOException {
		
		String test_folder = "test-data/";
		String result_folder = "result-data/";	// create this folder in advance for output
		
		String syn0 = "syn0.txt";
		String syn1 = "syn1.txt";
		
		ArrayList<String> syns = new ArrayList<String>();
		List<Float> acc = new ArrayList<Float>();
		syns.add(syn0);
		syns.add(syn1);		//add more file if needed
		
		Vocabulary vocab = pilsaVect.getVocab();
		int OOV = vocab.OOVindex();
		
		for(int i=0;i<syns.size();i++){
			int syn_num = 0;
			double mse_syn = 0.0;
			
			FileWriter out = new FileWriter(result_folder+ syns.get(i) ); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test_folder+syns.get(i))));
			String line = br.readLine();
			
			while(line!=null){
				String[] words = line.split("\\s+");
				ArrayList<double[]> vectors = new ArrayList<double[]>();
				for(int j=0;j<words.length;j++){
					int wordID = vocab.lookupWord(words[j]);
					if (wordID != OOV) {
						vectors.add(pilsaVect.getVectorRep(wordID));
						out.append(words[j]+" ");
					}
					else {
						out.append("No such word: " + words[j]+ " ");
					}
				}	
				
				for(int k=0;k<vectors.size();k++){
					double cs = Util.cosineSimilarity(vectors.get(k%vectors.size()), vectors.get((k+1)%vectors.size()));
					syn_num++;
					mse_syn+= Math.pow(1-cs,2);
					out.append(Double.toString(cs)+" ");
				}

				out.append("\n");
				line = br.readLine();
			}
			mse_syn = mse_syn/syn_num;
			acc.add((float) mse_syn);
			out.append("\n");
			out.append("Mean squared error for synonym is: " + mse_syn +"\n");
			System.out.println("[SIMPLE SNY TEST" + i + "] Mean squared error for synonym is: " + Double.toString(mse_syn));
			out.close();
			br.close();
			
		}
		return acc;		
	}
}
