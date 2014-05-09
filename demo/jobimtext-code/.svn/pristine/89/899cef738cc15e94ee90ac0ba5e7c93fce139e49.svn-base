package org.jobimtext.hadoop;
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
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class SimCountFeatureJob extends HadoopJob {

	public SimCountFeatureJob(String name, String input, String output) throws IOException {

		Configuration conf = createConf();
		// conf.setInt("threshold", threshold);
		conf.set("dfs.block.size", "134217728");
		// conf.setInt("mapred.task.timeout", "600000");
		conf.set("io.sort.mb", "300");
		conf.set("io.sort.factor", "30");

		conf.set("mapred.map.output.compression.codec","org.apache.hadoop.io.compress.GzipCodec");
		
		
		conf.setBoolean("mapred.compress.map.output", true);
		

		conf.setBoolean("mapred.output.compress", true);
		conf.set("mapred.output.compression.codec",	"org.apache.hadoop.io.compress.GzipCodec");
		
		
		conf.setBoolean("mapred.compress.map.output", true);
		
		int maptasks = 200; 
	    conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
	    conf.set("mapred.map.tasks", "" + maptasks);
	    conf.set("mapred.tasktracker.map", "" + maptasks);
	    
	    int reducetasks = 200;
	    conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
	    conf.set("mapred.reduce.tasks", "" + reducetasks);
	    conf.set("mapred.tasktracker.reduce", "" + reducetasks);  
	    
	    conf.set("mapred.job.map.memory.mb", "4048");
	    conf.set("mapred.job.reduce.memory.mb", "4048");
	    

		job = createJob(input, output, conf);
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		this.name = name;
	}

	/*
	 * The mapper takes the feature and its word list (all the words having this
	 * feature) as input It then compares all words pair-wise and outputs 1/n,
	 * with n being the number of words.
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		private Text wfeat = new Text();
		private int threshold;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			this.threshold = context.getConfiguration().getInt("threshold",
					1000);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			String[] line = value.toString().split("\t");
			wfeat.set(Double.toString(1.0f) + "\t" + line[0]);
			if (line.length >= 2 && line.length < threshold) {
				try {
					for (int i = 1; i < line.length; i++) {
						for (int j = 1; j < line.length; j++) {
							context.write(new Text(line[i] + "\t" + line[j]),
									wfeat);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * The reducer step will sum all float values, i.e. the weight for any
	 * (word1,word2) pair sharing a feature.
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException {

			double sum = 0f;
			String[] inputList = null;
			ArrayList<String> resultList = new ArrayList<String>();
			String endResult = "";

			// sum all values of this key
			for (Text t : values) {
				String line = t.toString();
				inputList = line.split("\t");
				// the weight is the first element
				sum += Double.valueOf(inputList[0]);
				// rest: concatenate the features list
				for (int i = 1; i < inputList.length; i++) {
					resultList.add(inputList[i]);
				}
			}

			// begin final result with the weight and then tab
			endResult = Double.toString(sum) + "\t";

			// then concatenate the feature list
			for (String s : resultList) {
				endResult += s + " ";
			}

			// output new string: summed weight and expanded list
			try {
				context.write(key, new Text(endResult));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
