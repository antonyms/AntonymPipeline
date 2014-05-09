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

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
import org.jobimtext.hadoop.mapreducer.SumReducer.IntSumReducer;

/*
 ** Take the context output and count all words
 ** (i.e. the first column multiplied by the number
 ** of occurrences)
 **/
public class UniqMapper {

	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {

		private int[] columns;
		final IntWritable ONE = new IntWritable(1);

		@Override
		public void configure(JobConf job) {
			String columns = job.get("columns");
			String[] strCols = columns.split(",");
			this.columns = new int[strCols.length];
			int i = 0;
			for (String col : strCols) {
				this.columns[i] = Integer.parseInt(col);
				System.out.println(this.columns[i]);
				i++;
			}
			
			super.configure(job);
		}

		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

			// split the line separated by tabs
			String[] tokens = value.toString().split("\t");
			String newKey = "";
			for (int col : columns) {
				newKey += tokens[col] + "\t";
			}
			newKey = newKey.trim();

			output.collect(new Text(newKey), ONE);

		} // map()

	} // class map

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		JobConf conf = HadoopUtil.generateJobConf(args);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(IntSumReducer.class);
		conf.setReducerClass(IntSumReducer.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		String columns = args[2];

		int maptasks = 120;

		conf.set("columns", columns);

		/* set the maximum number of task per node */
		conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
		conf.set("mapred.map.tasks", "" + maptasks);
		conf.set("mapred.tasktracker.map", "" + maptasks);

		int reducetasks = 100;

		conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
		conf.set("mapred.reduce.tasks", "" + reducetasks);
		conf.set("mapred.tasktracker.reduce", "" + reducetasks);

		/*
		 * how much virtual memory the entire process tree of each map/reduce
		 * task will use
		 */
		conf.set("mapred.job.map.memory.mb", "2048");
		conf.set("mapred.job.reduce.memory.mb", "2048");

		conf.set("dfs.replication", "1");

		JobClient.runJob(conf);

	} // main

} // class WordCount
