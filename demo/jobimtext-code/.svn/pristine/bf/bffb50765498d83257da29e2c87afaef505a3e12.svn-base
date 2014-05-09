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
package org.jobimtext.sense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.ByteStreams;

public class NodeEdgeFileWriter {
  
  private Map<String, Integer> dict = 
      new HashMap<String, Integer>();

  public void generateFiles(String simsort,String outPath, String nodePath, String edgePath) 
      throws IOException {
    
    if (nodePath == null)
      nodePath = outPath+"/sense.nodes";
    
    if (edgePath == null)
      edgePath = outPath+"/sense.edges";
    
    BufferedReader reader = 
        new BufferedReader(
            new FileReader(simsort));
    
    BufferedWriter nodes =
        new BufferedWriter(
            new FileWriter(nodePath));
    
    BufferedWriter edges =
        new BufferedWriter(
            new FileWriter(edgePath+".dup"));
    
    int count = 1;
    String line = reader.readLine();
    while (line != null) {
      String[] tokens = line.split("\t");
      if (!dict.containsKey(tokens[0])) {
        nodes.write(count+"\t"+tokens[0]+"\n");
        dict.put(tokens[0], count);
        count++;
      }
      if (!dict.containsKey(tokens[1])) {
        nodes.write(count+"\t"+tokens[1]+"\n");
        dict.put(tokens[1], count);
        count++;
      }
      if (!tokens[0].equals(tokens[1])) {
        int t1 = dict.get(tokens[0]);
        int t2 = dict.get(tokens[1]);
        int t3 = (int)Double.parseDouble(tokens[2]);
        edges.write(t2+"\t"+t1+"\t"+t3+"\n");
        edges.write(t1+"\t"+t2+"\t"+t3+"\n");
      }
      line = reader.readLine();
    }
    
    nodes.close();
    edges.close();
    reader.close();
    
    // sort the edge file
    // FIXME: remove use of google commons I/O
    
    Process p = system("sort -u "+edgePath+".dup");
    FileOutputStream out = new FileOutputStream(edgePath+".dup.sort");
    InputStream in = p.getInputStream();
    ByteStreams.copy(in, out);
    ByteStreams.copy(p.getErrorStream(), System.err);
    out.close();
               
    // need pb because of the \t argument
    ProcessBuilder pb = new ProcessBuilder();
    pb.command("sort", "-k1n", "-k3nr", "-t", "\t", edgePath+".dup.sort");
    p = pb.start();    
    out = new FileOutputStream(edgePath);
    in = p.getInputStream();
    ByteStreams.copy(in, out);
    ByteStreams.copy(p.getErrorStream(), System.err);
    out.close();
    
    system("rm "+edgePath+".dup");
    system("rm "+edgePath+".dup.sort");
  }
  
  private static final Process system(String cmd) throws IOException {
    return Runtime.getRuntime().exec(cmd);
  }
  
}
