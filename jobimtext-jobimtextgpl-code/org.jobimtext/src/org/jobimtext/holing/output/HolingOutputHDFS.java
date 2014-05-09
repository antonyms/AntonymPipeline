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
package org.jobimtext.holing.output;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * This class outputs the content of the holing system to the
 * Hadoop file system.
 * @deprecated
 * This class does not handle the counts of jo's and bim's correctly
 */
@Deprecated
public class HolingOutputHDFS extends HolingOutput {
  
  private Map<String, FSDataOutputStream> writerMap = 
      new HashMap<String, FSDataOutputStream>();
  
  private static long uid;
    
  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException {
    super.initialize(context);
    try {
      Configuration conf = new Configuration();
      URI uri = URI.create(outputPath);
      FileSystem hdfs = FileSystem.get(uri, conf);
      Path path = new Path(outputPath);
      if (!hdfs.exists(path)) {
        hdfs.mkdirs(new Path(outputPath));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initFileForModel(String model) {
    try {
      String path = outputPath+"/"+model+"_"+(++uid);
      Configuration conf = new Configuration();
      URI uri = URI.create(outputPath);
      FileSystem hdfs = FileSystem.get(uri, conf);
      Path hdfsFile = new Path(path);
      FSDataOutputStream out = hdfs.create(hdfsFile);
      writerMap.put(model, out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  protected void writeJoBimString(String s, String modelId) {
    if (s.length() > 0) {
      try {
        if (!writerMap.containsKey(modelId)) {
          initFileForModel(modelId);
        }
        FSDataOutputStream out = writerMap.get(modelId);
        out.writeChars(s);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    for (FSDataOutputStream out : writerMap.values()) {
      try {
        out.flush(); out.close();        
      } catch (IOException e) {
        e.printStackTrace();
      } 
    }
  }

  @Override
  protected void closeWriter(String modelId) {
    // do nothing    
  }
  
}
