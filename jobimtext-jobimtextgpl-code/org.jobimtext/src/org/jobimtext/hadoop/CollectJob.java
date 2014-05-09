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

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CollectJob extends HadoopJob {
    
  public CollectJob(String name, String input, String output) 
      throws IOException {
    job = createJob(input, output, null);
    job.setOutputValueClass(IntWritable.class);
    job.setMapperClass(Map.class);
    this.name = name;
  }
  
  public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {  
    public static IntWritable ONE = new IntWritable(1);    
    public void map(LongWritable key, Text value, Context context)
        throws IOException {
      String line = value.toString();
      try {
        context.write(new Text(line), ONE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }    
  }

}
