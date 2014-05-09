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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.io.ByteStreams;

public class ComputeSimilarityIndexedWithFeatures {

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
		Map<String, String> params = parseParameters(args);

		verifyInput(params);
		verifyOutput(params);

		try {
			jobs.add(new PigJob("WORDFEATURE_COUNT", PIG_WORDFEATURE_COUNT,
					params));

			jobs.add(new PigJob("PRUNE_FPW", PIG_PRUNE_FPW, params));

			/*
			 * jobs.add(new CountJob("COUNT_WORDS", 0,
			 * params.get("contextout_filter"), params.get("wordcountout")));
			 * 
			 * jobs.add(new CountJob("COUNT_FEATURES", 1,
			 * params.get("contextout_filter"), params.get("featurecountout")));
			 */

			jobs.add(new PigJob("LMI", PIG_COMPUTE_LMI, params));

			jobs.add(new PigJob("PRUNE", PIG_PRUNE_GRAPH, params));

			jobs.add(new AggregatePerFeatureJob("AGP", params.get("prunegraphout"), params
					.get("aggrperftout")));

			jobs.add(new SimCountFeatureJob("SCF", params.get("aggrperftout"), params
					.get("simcountsout")));

			jobs.add(new PigJob("SIMSORT", PIG_SIM_SORT, params));

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		for (JobimJob job : jobs) {
			System.out.println("Running Job: " + job.getName());
			try {
				job.run();
			} catch (Exception e) {
				e.printStackTrace();
				//System.exit(1);
			}
		}

		fetchOutput(params);
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
				File[] inputHoles = new File(holesPath).listFiles();
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

	private static void verifyOutput(Map<String, String> params) {
		try {
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			Path outputPath = new Path(params.get("output"));
			if (hdfs.exists(outputPath)) {
				System.out.println("Deleting existing output in hdfs: "
						+ outputPath);
				System.out.println("No Deleting done at all");
				// hdfs.delete(outputPath, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void fetchOutput(Map<String, String> params) {
		try {
			String srcPath = params.get("output");
			String destPath = params.get("destination");
			System.out.println("Copying model files to: " + destPath);
			if(!new File(destPath).exists()){
				
				if(!new File(destPath).mkdirs()){
					System.err.println("Folder "+ destPath+" can not be generated (e.g. permission, disc space)");
				}
			}
			hdfsText(srcPath + "/WordCount/p*", destPath + "/WordCount");
			hdfsText(srcPath + "/FeatureCount/p*", destPath + "/FeatureCount");
			hdfsText(srcPath + "/FreqSigLMI/p*", destPath + "/FreqSigLMI");
			hdfsText(srcPath + "/SimSortlimit/p*", destPath + "/SimSortlimit");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void hdfsText(String src, String dst) throws IOException {
		Process p = Runtime.getRuntime().exec("hadoop fs -text " + src);
		FileOutputStream out = new FileOutputStream(dst);
		InputStream in = p.getInputStream();
		ByteStreams.copy(in, out);
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
		params.put("contextout_filter", output + "/WordFeatureCount_filter");
		params.put("wordcountout", output + "/WordCount");
		params.put("featurecountout", output + "/FeatureCount");
		params.put("freqsigout", output + "/FreqSigLMI");
		params.put("prunegraphout", output + "/PruneGraph");
		params.put("aggrperftout", output + "/AggrPerFt");
		params.put("simcountsout", output + "/SimCounts1WithFeatures");
		params.put("IN", output + "/SimCounts1WithFeatures");
		params.put("OUT", output + "/SimSortlimit");
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
