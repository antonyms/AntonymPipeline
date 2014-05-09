/*******************************************************************************
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
 ** This is the MapReduce step to aggregate all words per feature.
 ** 
 ** This step additionally uniques the position that is attached to the word#POS
 ** 
 ** @author Richard Steuer
 ** 
 **/
public class AggrPerFtUniquePositions {

	/**
	 * The mapper reads the following input: word feature significance hard
	 * cheese#adj 15.8 cheese Gouda-like#adj 7.6 cheese hard#adj 0.4
	 * 
	 * It will produce the following output: feature word cheese#adj hard
	 * cheese#adj yellow cheese#adj French
	 * 
	 * @author Richard Steuer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		// the key and value map() will output
		private Text keyOut = new Text();
		private Text valOut = new Text();

		String wordSubstring;

		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			wordSubstring = "";

			// split lines into the two tokens: word and feature
			// (these are separated by at least one whitespace)
			String[] tokens = value.toString().split("\\s+");

			// now we have: word = tokens[0], feature = tokens[1]
			// the feature is now the value map() will output
			// the word is now the feature map() will output

			// remove position from the word (!)
			// look for last '#' and construct substring
			wordSubstring = tokens[0].substring(0, tokens[0].lastIndexOf("#"));

			keyOut.set(tokens[1]);
			valOut.set(wordSubstring);

			// emit output
			output.collect(keyOut, valOut);

		} // map()

	} // class map

	/**
	 * The reducer reads the following input: feature word cheese#adj hard
	 * cheese#adj yellow cheese#adj French
	 * 
	 * It will produce the following output: cheese#adj, hard, yellow, French,
	 * ...
	 * 
	 * @author Richard Steuer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static class Reduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {

		// the output text
		private final Text outputVal = new Text();

		// that will be all words having the same feature
		StringBuilder concat = new StringBuilder();

		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			// very important: re-empty stringbuffer for each reduce process
			concat.setLength(0);

			while (values.hasNext()) {

				// get next
				String next = values.next().toString().replace("^(\\s,)+", "");

				if (!next.equals("")) {

					// concatenate string
					concat.append(" ").append(next);

				} // if not empty

			} // while hasNext

			// set output
			outputVal.set(concat.toString());

			// write output
			output.collect(key, outputVal);

		} // reduce()

	} // class reduce

	/**
	 * Set the job configuration, classes and run the job.
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		JobConf conf = HadoopUtil.generateJobConf(args);
//		JobConf conf = new JobConf(AggrPerFtUniquePositions.class);
		conf.setJobName("AggrPerFtUniquePositions " + args[0] + " " + args[1]);
		
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		/*
		 * use compression
		 */
		conf.set("mapred.output.compress", "true");
		conf.set("mapred.map.output.compress", "true");
		conf.set("mapred.map.output.compression.codec",
				"org.apache.hadoop.io.compress.SnappyCodec");
		conf.set("mapred.output.compression.codec",
				"org.apache.hadoop.io.compress.SnappyCodec");

		/* set the maximum number of task per node */
		int maptasks = 120;

		conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
		conf.set("mapred.map.tasks", "" + maptasks);
		conf.set("mapred.tasktracker.map", "" + maptasks);

		int reducetasks = 60;

		conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
		conf.set("mapred.reduce.tasks", "" + reducetasks);
		conf.set("mapred.tasktracker.reduce", "" + reducetasks);

		/*
		 * heap size for the job
		 */
		conf.set("mapred.child.java.opts", "-Xmx1500m");

		/*
		 * how much virtual memory the entire process tree of each map/reduce
		 * task will use
		 */
		conf.set("mapred.job.map.memory.mb", "2048");
		conf.set("mapred.job.reduce.memory.mb", "2048");

		JobClient.runJob(conf);

	} // main

} // class AggrPerFt
