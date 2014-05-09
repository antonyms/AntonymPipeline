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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;

public abstract class HadoopJob implements JobimJob {
  
  protected Job job;
  protected String name;
  
  public String getName() {
    return name;
  }
  
  /**
   * Create a configuration with some reasonable defaults
   */
  protected static Configuration createConf() {
    Configuration conf = new Configuration();
    
    int maptasks = 100; 
    conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
    conf.set("mapred.map.tasks", "" + maptasks);
    conf.set("mapred.tasktracker.map", "" + maptasks);
    
    int reducetasks = 100;
    conf.set("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
    conf.set("mapred.reduce.tasks", "" + reducetasks);
    conf.set("mapred.tasktracker.reduce", "" + reducetasks);  
    
    conf.set("mapred.job.map.memory.mb", "2048");
    conf.set("mapred.job.reduce.memory.mb", "2048");
    
    conf.set("dfs.replication", "1"); 
    
    conf.setBoolean("mapred.output.compress", true);
    conf.set("mapred.output.compression.codec",
          "org.apache.hadoop.io.compress.GzipCodec");
    
    return conf;
  }
  
  /**
   * Create a JoBim Job with reasonable defaults
   */
  protected static Job createJob(String input, String output,
      Configuration conf) throws IOException {
    
    if (conf == null) {
      conf = createConf();
    }
    
    Job job = new Job(conf);    
    job.setJarByClass(getClassObject());
    job.setJobName(getShortClassName() + " " + input + " " + output);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);

    FileInputFormat.setInputPaths(job, new Path(input));
    FileOutputFormat.setOutputPath(job, new Path(output));
    
    return job;
  }
  
  public void run() throws Exception {
    job.waitForCompletion(true);
  }
  
  public static String getClassName() {
    return (new CurClassNameGetter()).getClassName();
  }

  public static String getShortClassName() {
    String cn = getClassName();
    return cn.substring(cn.lastIndexOf('.') + 1);
  }

  public static Class getClassObject() {
    String cn = getClassName();
    Class c = null;
    try {
      c = Class.forName(cn);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return c;
  }
  
  // Static Nested Class doing the trick
  public static class CurClassNameGetter extends SecurityManager {
    public String getClassName() {
      StackTraceElement[] stack = Thread.currentThread().getStackTrace();
      for(StackTraceElement e : stack){
        if(e.getClassName().startsWith("org.jobimtext.hadoop")){             
          return e.getClassName();
        }
      }
      return "";
    }
  }

}
