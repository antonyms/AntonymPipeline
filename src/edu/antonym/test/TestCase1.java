package edu.antonym.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.antonym.RawPILSAVec;

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
public class TestCase1 {
	
	public static void main(String[] args) throws IOException{
		
		String test_folder = "test-data/";
		String result_folder = "result-data/";	// create this folder in advance for output
		
		String ant0 = "ant0.txt";
		String ant1 = "ant1.txt";
		
		ArrayList<String> ants = new ArrayList<String>();
		ants.add(ant0);
		ants.add(ant1);		//add more file if needed
		
		RawPILSAVec pilsaVect = new RawPILSAVec(false);
		
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
				if(vectors.size()==2 && words.length==2){
					double cs = new CosineSimilarity().cosineSimilarity(vectors.get(0), vectors.get(1));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs));
				}
				else if(vectors.size()==3){
					double cs = new CosineSimilarity().cosineSimilarity(vectors.get(0), vectors.get(1));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs)+" ");
					cs = new CosineSimilarity().cosineSimilarity(vectors.get(0), vectors.get(2));
					ant_num++;
					mse_ant+= Math.pow(-1-cs,2);
					out.append(Double.toString(cs)+" ");
					cs = new CosineSimilarity().cosineSimilarity(vectors.get(1), vectors.get(2));
					syn_num++;
					mse_syn+= Math.pow(1-cs,2);
					out.append(Double.toString(cs)+" ");
				}
							
				out.append("\n");
				line = br.readLine();
			}
			mse_ant = mse_ant/ant_num;
			mse_syn = mse_syn/syn_num;
			out.append("\n");
			out.append("Mean squared error for antonym is: " + mse_ant +"\n");
			if(syn_num!=0){
				out.append("Mean squared error for synonym is: " + mse_syn +"\n");
			}
			out.close();
			br.close();
		}
					
		System.out.println("finished!");
		
		
	}
}
