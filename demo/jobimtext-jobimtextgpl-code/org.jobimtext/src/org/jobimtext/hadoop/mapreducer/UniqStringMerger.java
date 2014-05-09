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
import java.util.Iterator;

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
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

/*
 ** Take the context output and count all words
 ** (i.e. the first column multiplied by the number
 ** of occurrences)
 **/
public class UniqStringMerger {

	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, Text> {

		private int[] columnsUniq;
		private int[] columnsMerge;
		final IntWritable ONE = new IntWritable(1);

		@Override
		public void configure(JobConf job) {
			String columns = job.get("columns.uniq");
			
			this.columnsUniq = getColumns(job.get("columns.uniq"));
			this.columnsMerge = getColumns(job.get("columns.merge"));
			

			super.configure(job);
		}
		
		private int[] getColumns(String strColumn){
			String[] strColumns = strColumn.split(",");
			
			int[] columns= new int[strColumns.length];
			int i = 0;
			for (String col : strColumns) {
				columns[i] = Integer.parseInt(col);
				i++;
			}
			return columns;

		}
		private String getColumnsToString(String[] tokens, int[] cols){
			String value= "";
			for (int col : cols) {
				value += tokens[col] + "\t";
			}
			if(value.length()>0){
				value = value.substring(0,value.length()-1);
			}
			return value;
		}
		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			// split the line separated by tabs
			String[] tokens = value.toString().split("\t");
				String newKey = getColumnsToString(tokens, columnsUniq);
			String newCount = getColumnsToString(tokens, columnsMerge);
			
			

			output.collect(new Text(newKey), new Text(newCount));

		} // map()

	} // class map
	public static class StringMerger extends MapReduceBase implements
	Reducer<Text, Text, Text, Text>{
		@Override
		public void configure(JobConf job) {
			super.configure(job);
		}
		@Override
		public void reduce(Text key, Iterator<Text> iterator,
				OutputCollector<Text, Text> output, Reporter arg3)
				throws IOException {
			String merged = "";
			while(iterator.hasNext()){
				merged+=iterator.next().toString()+"_";
//				System.out.println(merged);
			}
			if(merged.length()>0){
				merged = merged.substring(0,merged.length()-1);
			}
			output.collect(key, new Text(merged));
		}
		
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		JobConf conf = HadoopUtil.generateJobConf(args);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(StringMerger.class);
		conf.setReducerClass(StringMerger.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		String columnsUniq = args[2];
		String columnsMerge = args[3];
		int maptasks = 120;

		conf.set("columns.uniq", columnsUniq);
		conf.set("columns.merge", columnsMerge);

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
