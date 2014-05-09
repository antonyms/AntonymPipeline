/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and
* 
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
package org.jobimtext.hadoop;
	    
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The Aggregate Per Feature (APF) job aggregates all words per feature.
 */
public class AggregatePerFeatureJob extends HadoopJob {
  
  /*
   * Main
   */
  public AggregatePerFeatureJob (String name, String input, String output) 
      throws IOException {
    job = createJob(input, output, null);
    job.setMapperClass(Map.class);
    job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);
    this.name = name;
  }

  /*
   * The Mapper
   */
	public static class Map extends Mapper <LongWritable, Text, Text, Text> {

		private Text keyOut = new Text();
		private Text valOut = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			String[] tokens = value.toString().split("\t");
			String word = trim(tokens[0]);
			String feature = trim(tokens[1]);
			if (word.length() > 0 && feature.length() > 0) {
				keyOut.set(feature);
				valOut.set(word);
				try {
          context.write(keyOut, valOut);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
			}
		}

	}

	/*
	 * The Reducer
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
	  
	  @Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException {
			StringBuilder concat = new StringBuilder();
			for (Text t: values) {
				String next = t.toString();
				if (!next.trim().equals("")) {
					concat.append("\t").append(next);
				}
			}
			try {
        context.write(key, new Text(trim(concat.toString())));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
		}
		
	}

	public static String trim(String word){
    return word.replace((char)65533, ' ').trim()
        .replaceAll("^[\\s\\xA0]+", "")
        .replaceAll("[\\s\\xA0]+$", "");
  }
	
}
