package edu.antonym.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.RawPILSAVec;

/**
 * This is the test case for synonyms, syn0.txt and syn1.txt.
 * 
 *
 */
public class TestCase2 {
	
	public static void main(String[] args) throws IOException{
		
		String test_folder = "test-data/";
		String result_folder = "result-data/";	// create this folder for output
		
		String syn0 = "syn0.txt";
		String syn1 = "syn1.txt";
		
		ArrayList<String> syns = new ArrayList<String>();
		syns.add(syn0);
		syns.add(syn1);		//add more file if needed
		
		RawPILSAVec pilsaVect = new RawPILSAVec(false);
		
		for(int i=0;i<syns.size();i++){

			int syn_num = 0;
			double mse_syn = 0.0;
			
			FileWriter out = new FileWriter(result_folder+ syns.get(i) ); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(test_folder+syns.get(i))));
			String line = br.readLine();
			
			while(line!=null){
				String[] words = line.split("\\s+");
				ArrayList<float[]> vectors = new ArrayList<float[]>();
				for(int j=0;j<words.length;j++){
					try{
						vectors.add(pilsaVect.getVectorRep(pilsaVect.getWordId(words[j])));
						out.append(words[j]+" ");
					}
					catch(NullPointerException e){
						out.append("No such word: " + words[j]+ " ");
					}
				}	
				
				for(int k=0;k<vectors.size();k++){
					double cs = new CosineSimilarity().cosineSimilarity(vectors.get(k%vectors.size()), vectors.get((k+1)%vectors.size()));
					syn_num++;
					mse_syn+= Math.pow(1-cs,2);
					out.append(Double.toString(cs)+" ");
				}

				out.append("\n");
				line = br.readLine();
			}
			mse_syn = mse_syn/syn_num;
			out.append("\n");
			out.append("Mean squared error for synonym is: " + mse_syn +"\n");
			out.close();
			br.close();
			
		}
		System.out.println("finished!");			
		
		
		
	}
}
