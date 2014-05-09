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
package org.jobimtext.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class VerifyHoleOutput {

  public static void main(String[] args) throws IOException {    
    String inputDir = args[0];
    File dir = new File(inputDir);
    if (!dir.exists()) 
      error(dir, "Input path is not valid");
    File[] files = dir.listFiles();
    for (File file: files) {
      BufferedReader r = new BufferedReader(new FileReader(file));
      String line = r.readLine();
      int lines = 0;
      while (line != null) {
        if (line.split("\t").length != 5) 
          error(file, "Invalid number of columns for: "+line);
        line = r.readLine();
        lines++;
      }
      System.out.println("Processing: "+file.getName()+" lines: "+lines);
      if (lines <=0)
        error(file, "No lines for file");
    }    
  }
  
  private static void error(File file, String message)  {
    System.out.println("Error reading: "+file.getAbsolutePath()+"\n"+message);
    System.exit(1);
  }

}
