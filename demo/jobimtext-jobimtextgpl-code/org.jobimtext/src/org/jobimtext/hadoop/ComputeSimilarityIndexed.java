/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package org.jobimtext.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.io.ByteStreams;

public class ComputeSimilarityIndexed {

	private static final String PIG_WORDFEATURE_COUNT = "pig/WordFeatureCount.pig";
	private static final String PIG_PRUNE_FPW = "pig/PruneFeaturesPerWord.pig";
	private static final String PIG_COMPUTE_LMI = "pig/FreqSigLMI.pig";
	private static final String PIG_PRUNE_GRAPH = "pig/PruneGraph.pig";
	private static final String PIG_SIM_SORT = "pig/SimSortWithFeatures.pig";

	private static List<JobimJob> jobs = new ArrayList<JobimJob>();

	/*
	 * Hadoop pipeline for computing similarity
	 */

	public static void main(String[] args) {
		new ComputeSimilarityIndexed().startJob(args);
	}

	public void startJob(String[] args) {
		System.out.println(PIG_SIM_SORT);
		Map<String, String> params = parseParameters(args);

		verifyInput(params);
		verifyOutput(params);

		try {
			jobs.add(new PigJob("WORDFEATURE_COUNT", PIG_WORDFEATURE_COUNT,
					params));

			jobs.add(new PigJob("PRUNE_FPW", PIG_PRUNE_FPW, params));

			/**
			 * replace the $contextout by the filtered one, otherwise the
			 * parameter names have to be changed within the scripts, which
			 * might make it more complicated
			 */
			params.put("contextout", params.get("contextout_filter"));
			/*
			 * jobs.add(new CountJob("COUNT_WORDS", 0,
			 * params.get("contextout_filter"), params.get("wordcountout")));
			 * 
			 * jobs.add(new CountJob("COUNT_FEATURES", 1,
			 * params.get("contextout_filter"), params.get("featurecountout")));
			 */

			jobs.add(new PigJob("LMI", PIG_COMPUTE_LMI, params));

			jobs.add(new PigJob("PRUNE", PIG_PRUNE_GRAPH, params));

			jobs.add(new AggregatePerFeatureJob("AggregatePerFeature", params
					.get("prunegraphout"), params.get("aggrperftout")));

			jobs.add(new SimCountFeatureJob("SCF", params.get("aggrperftout"),
					params.get("simcountsout")));

			jobs.add(new PigJob("SIMSORT", PIG_SIM_SORT, params));

		} catch (IOException e) {
			e.printStackTrace();
			// System.exit(1);
		}

		for (JobimJob job : jobs) {
			System.out.println("Running Job: " + job.getName());
			try {
				job.run();
			} catch (Exception e) {
				e.printStackTrace();
				// System.exit(1);
			}
		}

		fetchOutput(params);
		System.out.println( "------------------------------");
		System.out.println("PLEASE CLEANUP THE DIRECTORY AND DELETE THE FILES ON THE HDFS,:");
		System.out.println("WHICH ARE NO LONGER NEEDED:");
		try {
			printFiles(params.get("output"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println( "------------------------------");
		
	}

	private static void verifyInput(Map<String, String> params) {
		System.out.println("Verify Input!");
		try {
			// get the input path in HDFS
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			String ip = params.get("input");
			// TODO this substring is a kludge needed because
			// of the way HDFS copyFromLocalFile works
			Path inputPath = new Path(ip);
			System.out.println(inputPath);
			if (hdfs.exists(inputPath)) {
				System.out
						.println("HDFS input path already exists, skipping put.."
								+ inputPath);
				return;
			}
			// get the data path and copy to HDFS
			if (params.get("holes") != null) {
				String holesPath = params.get("holes");
				System.out.println(holesPath);
				File[] inputHoles = new File(holesPath).listFiles();
				System.out.println(Arrays.toString( new File(holesPath).list()));
				Path[] inputHolesPath = new Path[inputHoles.length];
				for (int i = 0; i < inputHoles.length; i++) {
					inputHolesPath[i] = new Path(
							inputHoles[i].getAbsolutePath());
				}
				System.out.println("Creating HDFS input directory at "
						+ inputPath);
				hdfs.mkdirs(inputPath);

				System.out.println("Copying input files to HDFS from "
						+ holesPath);

				hdfs.copyFromLocalFile(false, true, inputHolesPath, inputPath);
			} else {
				System.err
						.println("Must specify a valid path to holing output with -h");
				System.exit(1);
			}
		} catch (IOException e) {
			System.out.println("Exception in verify input");
			e.printStackTrace();
		}
	}
	private static void printFiles(String folder) throws IOException{
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		printFiles(hdfs,folder);
	}
	private static void printFiles(FileSystem hdfs, String folder) throws IOException{
		Path path = new Path(folder);
		FileStatus[] files = hdfs.listStatus(path);
		for(FileStatus f: files){
			System.out.format("%n%10d %-100s", hdfs.getContentSummary(f.getPath()).getLength() ,f.getPath());
		}
	}
	private static void verifyOutput(Map<String, String> params) {
		try {
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			Path outputPath = new Path(params.get("output"));
			if (hdfs.exists(outputPath)) {
				
				System.out.println("Some of the outputs (listed below) already exist.");
				System.out.println("--------------------------------------------------");
				printFiles(hdfs, params.get("output"));
				System.out.println("\n--------------------------------------------------");
				
				System.out.println("Do you want to delete them (d), continue (c) or stop the process (s)?");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			      String input = null;

			      //  read the username from the command-line; need to use try/catch with the
			      //  readLine() method
			      try {
			         input = br.readLine();
			      } catch (IOException ioe) {
			         System.out.println("IO error trying to read the user input!");
			         System.exit(1);
			      }

			      if(input.equals("d")){
			    	  System.out.println("Deleting existing output in hdfs: " + outputPath);	  
			    	  hdfs.delete(outputPath, true);
			      }else if(input.equals("c")){
			    	  System.out.println("No files are deleted and the process will continue.");	  
			      }else{
			    	  System.out.println("The process is beeing stopped.");
			    	  System.exit(0);
			      }
				
				

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void fetchOutput(Map<String, String> params) {

		String srcPath = params.get("output");
		String destPath = params.get("destination");
		System.out.println("Copying model files to: " + destPath);
		if (!new File(destPath).exists()) {

			if (!new File(destPath).mkdirs()) {
				System.err
						.println("Folder "
								+ destPath
								+ " can not be generated (e.g. permission, disc space)");
				return;
			}
		}
		String wcout = new File(params.get("wordcountout")).getName();
		String fcout = new File(params.get("featurecountout")).getName();
		String fsigout = new File(params.get("freqsigout")).getName();
		String simout = new File(params.get("simsortout")).getName();
		
		
		hdfsText(srcPath + "/" + wcout + "/p*", destPath + "/" + wcout);
		hdfsText(srcPath + "/" + fcout + "/p*", destPath + "/" + fcout);
		hdfsText(srcPath + "/" + fsigout + "/p*", destPath+ "/" + fsigout);
		hdfsText(srcPath + "/" + simout + "/p*", destPath+ "/" + simout);

	}

	private static void hdfsText(String src, String dst) {
		Process p;
		try {
			p = Runtime.getRuntime().exec("hadoop fs -text " + src);

			FileOutputStream out = new FileOutputStream(dst);
			InputStream in = p.getInputStream();
			ByteStreams.copy(in, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<String, String> parseParameters(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (cmd == null || !cmd.hasOption("i")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ComputeSimilarity", options);
			System.exit(1);
		}

		String input = cmd.getOptionValue("i");
		String output = cmd.getOptionValue("o", input + "_output");

		// set parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("input", input);
		params.put("output", output);
		params.put("holes", cmd.getOptionValue("h"));
		params.put("destination", cmd.getOptionValue("d"));
		params.put("s", cmd.getOptionValue("s"));
		params.put("t", cmd.getOptionValue("t"));
		params.put("p", cmd.getOptionValue("p"));
		params.put("w", cmd.getOptionValue("w"));
		params.put("limit", cmd.getOptionValue("l"));
		params.put("contextout", output + "/WordFeatureCount");
		params.put("contextout_filter",
				output + "/WordFeatureCount_w" + params.get("w"));
		params.put("wordcountout", output + "/WordCount");
		params.put("featurecountout", output + "/FeatureCount");
		params.put("freqsigout", output + "/FreqSigLMI_w" + params.get("w")
				+ "_s" + params.get("s") + "_t" + params.get("t"));
		params.put("prunegraphout", output + "/PruneGraph_w" + params.get("w")
				+ "_s" + params.get("s") + "_t" + params.get("t") + "_p"
				+ params.get("p"));
		params.put("aggrperftout", output + "/AggrPerFt" + params.get("w")
				+ "_s" + params.get("s") + "_t" + params.get("t") + "_p"
				+ params.get("p"));
		params.put("simcountsout",
				output + "/SimCounts1WithFeatures" + params.get("w") + "_s"
						+ params.get("s") + "_t" + params.get("t") + "_p"
						+ params.get("p"));
		params.put("simsortout", output + "/SimSortWithFeatureslimit_w" + params.get("w")
				+ "_s" + params.get("s") + "_t" + params.get("t") + "_p"
				+ params.get("p") + "_l" + params.get("limit"));
		return params;
	}

	private static Options createOptions() {
		Options options = new Options();
		options.addOption("i", "input", true, "input directory in hdfs");
		options.addOption("h", "holes", true,
				"input holing data on local file system");
		options.addOption("d", "destination", true,
				"output directory on local file system");
		options.addOption("o", "output", true, "output directory in hdfs");

		// options.addOption("f", "hdfs_folder", true,
		// "directory in hdfs where all the processing is done");
		// options.addOption("lh", "local_holing", true,
		// "directory on HDD where the holing output is located");
		// options.addOption("lo", "local_output", true,
		// "directory on the HDD where the computations are stored");
		options.addOption("s", "s", true,
				"lower threshold for significance score");
		options.addOption("t", "t", true, "lower threshold for word count");
		options.addOption("p", "p", true,
				"maximal number of features a word is allowed to have");
		options.addOption("w", "w", true,
				"maximal number of uniq words a feature is allowed to have");
		options.addOption("l", "limit", true, "the limit parameter");
		return options;
	}

}
