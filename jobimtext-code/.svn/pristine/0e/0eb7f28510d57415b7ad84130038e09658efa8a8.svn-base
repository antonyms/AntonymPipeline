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

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * The main entry point to compute sense clusters using
 * the Chinese Whispers algorithm of Chris Biemman.
 * 
 * @param args - command line arguments
 */
public class ComputeSenseClusters {

  public static void main(String[] args) {
	  System.out.println(Integer.MAX_VALUE);
    CommandLine cmd = parseParameters(args);
    new File(cmd.getOptionValue("o")).mkdir();
    // generate the node and edge files
    NodeEdgeFileWriter nefw = new NodeEdgeFileWriter();
    try {
      nefw.generateFiles(
    	  cmd.getOptionValue("i"),
          cmd.getOptionValue("o"), 
          cmd.getOptionValue("wl"), 
          cmd.getOptionValue("el"));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
        
    // run the cwd algorithm
    RunCWD cwd = new RunCWD();
    cwd.run(cmd);      
  }

  private static CommandLine parseParameters(String[] args) {
    Options options = createOptions();
    CommandLineParser parser = new PosixParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "ComputeSenseClusters", options);
      System.exit(1);
    }
        
    return cmd;
  }
  
  private static Options createOptions() {
    Options options = new Options();
    options.addOption("i", "input", true, "input simsort file from jobimtext output");
   
    options.addOption("a", "algorithm", true, "sets algorithm options");
    options.addOption("h", "help", true, "displays help message");
    options.addOption("wl", "words", true, "location of word list file");
    options.addOption("el", "edges", true, "location of edge list file");    
    options.addOption("d", "iterations", true, "number of iterations");
    options.addOption("dt", "dt", true, "on-disk edge threshold");
    options.addOption("t", "t", true, "edge threshold within entries");
    options.addOption("T", "T", true, "edge threshold for entry creation");
    options.addOption("n", "n", true, "edge top N within entry");
    options.addOption("N", "N", true, "entry creation top N filter");
    options.addOption("o", "o", true, "output folder to store temporary files");
    options.addOption("f", "f",true,"Output file for the senses");
    options.addOption("S", "S", true, "input will not be sorted and renumbered");
    options.addOption("from", "from", true, "the word number to start from");
    options.addOption("to", "t", true, "the word number to end with");
    options.addOption("tg", "tg", true, "file containing target words");
    return options;
  }
  
}
