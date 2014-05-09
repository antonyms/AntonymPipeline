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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountJob extends HadoopJob {
    
  public CountJob(String name, int token, String input, String output) 
      throws IOException {
    Configuration conf = createConf();
    conf.setInt("token", token);
    job = createJob(input, output, conf);
    
    job.setOutputValueClass(IntWritable.class);
    job.setMapperClass(Map.class);
    this.name = name;
  }
  
  public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text keyOut = new Text();
    private String[] tokens;
    private int token;
    
    public void setup(Context context) {
      token = context.getConfiguration().getInt("token", -1);
    }

    public void map(LongWritable key, Text value, Context context)
        throws IOException {
      tokens = value.toString().split("\t");
      keyOut.set(tokens[token]);
      if (tokens.length != 3) {
        System.err.println("Token length != 3: "+value.toString());
      } else {
        try {
          context.write(keyOut, new IntWritable(Integer.parseInt(tokens[2])));
        } catch (NumberFormatException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    
  } 
  
}
