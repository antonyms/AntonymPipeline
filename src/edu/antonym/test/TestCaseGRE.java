package edu.antonym.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.antonym.RawPILSAVec;

public class TestCaseGRE {
	public static void main(String[] args) throws IOException{
		
		String test_folder = "test-data/";
		String result_folder = "result-data/";	// create this folder for output
		
		String test0 = "gre_testset.txt";
		
		ArrayList<String> gres = new ArrayList<String>();
		gres.add(test0);	//add more file if needed

		RawPILSAVec pilsaVect = new RawPILSAVec(false);
		
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
				try{
					target_vec = pilsaVect.getVectorRep(pilsaVect.getWordId(target));
					out.append(target+" ");
				}
				catch(NullPointerException e){
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
					try{
						float[] vect = pilsaVect.getVectorRep(pilsaVect.getWordId(choices[j]));
						out.append(choices[j]+" ");
						double cs = new CosineSimilarity().cosineSimilarity(target_vec, vect);
						double current_dist = Math.abs(-1-cs);
						if(current_dist<closest){
							closest = current_dist;
							test_answer = choices[j];
						}
					}
					catch(NullPointerException e){
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
			
			out.append("Final accuracy = "+ Double.toString(accuracy));
			br.close();
			out.close();
		}
		System.out.println("finished!");
	}
	
	
}
