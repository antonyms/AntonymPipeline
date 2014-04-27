package edu.antonym.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class TestCase implements MetricEvaluator{
	
	/*	
	 * 	Input arguments:
	 *  Usage: 
 	 *	-a    test files with antonyms
 	 *	-s    test files with synonyms
 	 *	-h    print out usage
 	 *	
	 *  example:
	 *  	String[] input = {"-s", "data/test-data/syn0.txt", "data/test-data/syn1.txt"};
	 *		TestCase n = new TestCase(input);
	 *		double score = n.score(metric);
	 *
	 *		or 
	 *		String[] input = {"-a", "data/test-data/ant0.txt"};
	 *		TestCase n = new TestCase(input);
	 *		double score = n.score(metric);
	 *
	 *		or 
	 *		String[] input = {"-a", "data/test-data/ant0.txt", "-s", "data/test-data/syn0.txt",};
	 *		TestCase n = new TestCase(input);
	 *		double score = n.score(metric);
	 *		Note: 
	 *		You can still see standard output results for both ant and syn
	 *			, but only the last score would be returned
	 *
	 */
	public ArrayList<String> antfiles = new ArrayList<String>();
	public ArrayList<String> synfiles = new ArrayList<String>();
	
	@SuppressWarnings("static-access")
	private static Options createOptions() {
	    Options options = new Options();
	    options.addOption(OptionBuilder.hasArgs().withDescription("followed by test files with antonyms").isRequired(false).create("a"));
	    options.addOption(OptionBuilder.hasArgs().withDescription("followed by test files with synonyms").isRequired(false).create("s"));
	    options.addOption("h", false, "print out usage");
	    return options;
	  }

	private static void showHelp(Options options) {
	    HelpFormatter h = new HelpFormatter();
	    h.printHelp("help", options);
	    System.exit(-1);
	  }
	  
	public TestCase(String[]  args){
		Options options = createOptions();
	    try {
	      CommandLineParser parser = new PosixParser();
	      CommandLine cmd = parser.parse(options, args);

	      if(cmd.hasOption("s")){
	    	  String path[] = cmd.getOptionValues("s"); 
	    	  for(int j=0;j<path.length;j++){
	    		  try{
	    			  FileInputStream f = new FileInputStream(path[j]);
		    		  synfiles.add(path[j]);
	    		  }catch(FileNotFoundException e){
	    			  System.out.println("File not found, exit");
	    			  System.exit(0);
	    		  }
	    		  
	    	  }  
	      }
	      if(cmd.hasOption("a")){
	    	  String path[] = cmd.getOptionValues("a"); 
	    	  for(int j=0;j<path.length;j++){
	    		  try{
	    			  FileInputStream f = new FileInputStream(path[j]);
		    		  antfiles.add(path[j]);
	    		  }catch(FileNotFoundException e){
	    			  System.out.println("File not found, exit");
	    			  System.exit(0);
	    		  }
	    	  }  
	      }
	      if(cmd.hasOption("h")){
	    	  showHelp(options);
	      }
	      if((!cmd.hasOption("h"))&&(!cmd.hasOption("a"))&&(!cmd.hasOption("s"))){
	          showHelp(options);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      showHelp(options);
	    }
		
	}
	
	@Override
	public double score(WordMetric metric) throws IOException {

		String result_folder = "data/result-data/";
		new File(result_folder).mkdirs();

		double acc = 0.0d;
		
		if(!antfiles.isEmpty()){
			
			Vocabulary vocab = metric.getVocab();
			int OOV = vocab.OOVindex();
			
			for(int i=0;i<antfiles.size();i++){
				int ant_num = 0;
				int syn_num = 0;
				double mse_ant = 0.0;
				double mse_syn = 0.0;
				
				FileWriter out = new FileWriter(result_folder+ "ant"+i+".txt"); 
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(antfiles.get(i))));
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
			System.out.println("Overall accuracy is: "+ acc);
		}
		if(!synfiles.isEmpty()){
			Vocabulary vocab = metric.getVocab();
			int OOV = vocab.OOVindex();
			
			acc = 0.0d;
			for (int i = 0; i < synfiles.size(); i++) {
				int syn_num = 0;
				double mse_syn = 0.0;

				FileWriter out = new FileWriter(result_folder + "syn"+i+".txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(synfiles.get(i))));
				String line = br.readLine();

				while (line != null) {
					String[] words = line.split("\\s+");
					ArrayList<Integer> vectors = new ArrayList<Integer>();
					for (int j = 0; j < words.length; j++) {
						int wordID = vocab.lookupWord(words[j]);
						if (wordID != OOV) {
							vectors.add(wordID);
							out.append(words[j] + " ");
						} else {
							out.append("No such word: " + words[j] + " ");
						}
					}

					for (int k = 0; k < vectors.size(); k++) {
						double cs = metric.similarity(
								vectors.get(k % vectors.size()),
								vectors.get((k + 1) % vectors.size()));
						syn_num++;
						mse_syn += Math.pow(1 - cs, 2);
						out.append(Double.toString(cs) + " ");
					}

					out.append("\n");
					line = br.readLine();
				}
				mse_syn = mse_syn / syn_num;
				acc -= mse_syn;
				out.append("\n");
				out.append("Mean squared error for synonym is: " + mse_syn + "\n");
				System.out.println("[SIMPLE SNY TEST" + i
						+ "] Mean squared error for synonym is: "
						+ Double.toString(mse_syn));
				out.close();
				br.close();

			}
			System.out.println("Overall accuracy is: "+ acc);
		}
		
		return acc;
	}


}
