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
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.jobimtext.hadoop.mapreducer.SumReducer.DoubleSumReducer;


/**
** This is the last main MapReduce step in our
** process chain.
** 
** This step will count the similarity, i.e.:
** How often do two words share the same feature?
** 
** @author Richard Steuer
** 
**/
public class SimCounts {

	/**
	 * The mapper takes the feature and its word list
	 * (all the words having this feature) as input
	 * It then compares all words pair-wise and
	 * outputs 1/n, with n being the number of words.
	 *
	 */
	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, DoubleWritable> {

		/*
		 * this will be the (inverted) bag size of the
		 * words having a feature in common 
		 */
		private DoubleWritable size = new DoubleWritable();
		private Text word = new Text();

		/* 
		 * max. number of words per feature
		 * (may be obtained experimentally)
		 * 
		 * this is important because this MapReduce step
		 * is computationally very intensive ( O(n^2) ) 
		 */
		private final int threshold = 1000;

		/* the line as a list */
		private List<String> line = new ArrayList<String>();
		private int linesize;

		/* the two word window we are looping through */
		private String word1, word2;

		public void map(LongWritable key, Text value,
				OutputCollector<Text, DoubleWritable> output, Reporter reporter)
				throws IOException {

			// split line into tokens
			// the first is the word feature, the rest are all
			// word having this feature
			line = Arrays.asList(value.toString().split("\t"));
			linesize = line.size();
			
			/*
			 * calculate the weight of these two words
			 * inverted: to filter noise (like stopwords)
			 */
			double weight = 1.0 / (double) linesize;
			
			// set the mapper output once (stays the same
			// for all words in the line)
			size.set( (float) weight );
			
			// consider lower and upper limit
			if (linesize >= 2 && linesize < threshold) {

				// count all word pairs in that line
				// skip first entry since this is the feature
				for (int i = 1; i < linesize; i++) {

					for (int j = i; j < linesize; j++) {

						// skip same index
						// if (i == j) { continue; }
						// update: no, it's good for normalization

						word1 = line.get(i);
						word2 = line.get(j);

						/*
						 * output words in one direction as we already have all combinations
						 * using two loops
						 */
						word.set(word1 + "\t" + word2);
						output.collect(word, size);

					} // for (inner)

				} // for (outer)

			} // if < threshold

		} // map()

	} // class map



	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		JobConf conf = HadoopUtil.generateJobConf(args);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(DoubleWritable.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(DoubleSumReducer.class);
		conf.setReducerClass(DoubleSumReducer.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		/* number of milliseconds before killing a not responding task */
		conf.set("mapred.task.timeout", "600000");
		
		/* change to 128mb */
		conf.set("dfs.block.size", "134217728");
		
		/* set the maximum number of task per node */
		int maptasks = 100;

		/* Number of map tasks to deploy on each machine. 0.5 to 2 * (cores/node) */
		conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
		conf.set("mapred.tasktracker.map", "" + maptasks);
		/* The default number of map tasks per job. Typically set to a prime several
			times greater than number of available hosts. */ 
		conf.set("mapred.map.tasks", "" + maptasks);
		
		int reducetasks = 100;
		
		conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
		conf.set("mapred.tasktracker.reduce", "" + reducetasks);
		conf.set("mapred.reduce.tasks", "" + reducetasks);
				
		JobClient.runJob(conf);

	} // main

} // class SimCounts
