/*******************************************************************************
 ** /*******************************************************************************
 * Copyright 2012
 * FG Language Technologie
 * Technische Universitaet Darmstadt
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

package org.jobimtext.hadoop.mapreducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

/**
 ** This is the last main MapReduce step in our process chain.
 ** 
 ** This step will count the similarity, i.e.: How often do two words share the
 * same feature?
 ** 
 ** This is the same as SimCounts, only it outputs 1 (one) for each word pair
 * occurence (instead if 1/(listsize)).
 ** 
 ** Additionally, this step will carry along the features the two words share.
 * This shall answer the question, why they are similar.
 ** 
 ** @author Richard Steuer, Martin Riedl
 * 
 ** 
 **/
public class SimCounts1WithFeatures {

	/**
	 * The mapper takes the feature and its word list (all the words having this
	 * feature) as input It then compares all words pair-wise and outputs 1/n,
	 * with n being the number of words.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		public void configure(JobConf job) {
			this.threshold = job.getInt("threshold", 1000);

		};

		/*
		 * the output object
		 */
		// WeightAndFeatures wfeat = new WeightAndFeatures();
		Text wfeat = new Text();

		/*
		 * max. number of words per feature (may be obtained experimentally)
		 * 
		 * this is important because this MapReduce step is computationally very
		 * intensive ( O(n^2) )
		 */
		private int threshold = 1000;

		/* the line as a list */
		private int linesize;

		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			// split line into tokens
			// the first is the word feature, the rest are all
			// word having this feature
			String[] line = value.toString().split("\t");
			linesize = line.length;
			// set weight to one
			double weight = 1.0f;

			// set the first token as the feature
			wfeat.set(Double.toString(weight) + "\t" + line[0]);

			// consider lower and upper limit
			if (linesize >= 2 && linesize < threshold) {

				// count all word pairs in that line
				// skip first entry since this is the feature
				for (int i = 1; i < linesize; i++) {

					for (int j = 1; j < linesize; j++) {

						// skip same index
						// if (i == j) { continue; }
						// update: no, it's supposed to be good for
						// normalization

						/*
						 * output words in both directions
						 */
						output.collect(new Text(line[i] + "\t" + line[j]),
								wfeat);

					} // for (inner)

				} // for (outer)

			} // if < threshold

		} // map()

	} // class map

	/**
	 * The reducer step will sum all float values, i.e. the weight for any
	 * (word1,word2) pair sharing a feature.
	 */
	@SuppressWarnings("deprecation")
	public static class Reduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {

		double sum;

		// the whole line coming as value
		String line;

		String[] inputList;
		ArrayList<String> resultList;

		String endResult;

		/**
		 * Sum up all float values (similarity weights).
		 * 
		 * @param key
		 *            The input key (in this case the (word1,word2) pair).
		 * @param values
		 *            The values coming in (in this step the float weights).
		 * @param output
		 *            The output format: (key, value) same as input
		 * @param reporter
		 *            A facility for Map-Reduce applications to report progress
		 *            and update counters, status information etc.
		 */
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			// reset the sum and the featurelist for each key
			sum = 0f;

			inputList = null;
			resultList = new ArrayList<String>();

			endResult = "";

			// sum all values of this key
			while (values.hasNext()) {

				line = values.next().toString();

				inputList = line.split("\t");
				// the weight is the first element
				sum += Double.valueOf(inputList[0]);

				// rest: concatenate the features list
				for (int i = 1; i < inputList.length; i++)
					resultList.add(inputList[i]);

			} // while

			// begin final result with the weight and then tab
			endResult = Double.toString(sum) + "\t";
			// then concatenate the feature list
			for (String s : resultList)
				endResult += s + " ";

			// output new string: summed weight and expanded list
			output.collect(key, new Text(endResult));

		} // reduce()

	} // class reduce

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		JobConf conf = HadoopUtil.generateJobConf(args);

		/* set the new defined type to be used */
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		if (args.length > 3) {
			conf.setInt("threshold", Integer.parseInt(args[3]));
		}
		/* number of milliseconds before killing a not responding task */
		conf.set("mapred.task.timeout", "600000");

		/* change to 128mb */
		conf.set("dfs.block.size", "134217728");

		/* set the maximum number of task per node */
		int maptasks = 200;
		/*
		 * Number of map tasks to deploy on each machine. 0.5 to 2 *
		 * (cores/node)
		 */
		conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
		conf.set("mapred.tasktracker.map", "" + maptasks);
		/*
		 * The default number of map tasks per job. Typically set to a prime
		 * several times greater than number of available hosts.
		 */
		conf.set("mapred.map.tasks", "" + maptasks);

		int reducetasks = 200;

		conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
		conf.set("mapred.tasktracker.reduce", "" + reducetasks);
		conf.set("mapred.reduce.tasks", "" + reducetasks);

		/*
		 * how much virtual memory the entire process tree of each map/reduce
		 * task will use
		 */
		conf.set("mapred.job.map.memory.mb", "4000");
		conf.set("mapred.job.reduce.memory.mb", "4000");

		conf.set("dfs.replication", "1");

		/*
		 * reduce I/O load
		 */
		conf.set("mapred.child.java.opts", "-Xmx1400M");

		conf.set("io.sort.mb", "300");
		conf.set("io.sort.factor", "30");

		JobClient.runJob(conf);

	} // main

} // class SimCounts1WithFeatures
