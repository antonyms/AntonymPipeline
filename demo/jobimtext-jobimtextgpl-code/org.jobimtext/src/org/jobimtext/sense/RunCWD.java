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
import org.apache.commons.cli.CommandLine;
import de.tudarmstadt.langtech.ChiWhiDisamb.DisambChineseWhispers;

/**
 * Utility class to run the Chinese whispers algorithm.
 * 
 * @author Matthew Hatem
 */
class RunCWD {
  
	private CommandLine cmd;

	public void run(CommandLine cmd) {
		this.cmd = cmd;
		
    String nodeWeightScheme = "";
		String alg = cmd.getOptionValue("a", "1");
		if (alg.equals("1")) {
			nodeWeightScheme = "1";
		}
		else if (alg.equals("log")) {
			nodeWeightScheme = "log";
		}
		else if (alg.equals("lin")) {
			nodeWeightScheme = "lin";
		}
		
		String outPath = sval("o");
		String nodePath = sval("wl");
		String edgePath = sval("el");
    if (nodePath == null)
      nodePath = outPath+"/sense.nodes";    
    if (edgePath == null)
      edgePath = outPath+"/sense.edges";
				
    DisambChineseWhispers cw = new DisambChineseWhispers();
		cw.disamb_start(
		    nodePath,                     //nodeListFile, 
		    edgePath,                     //edgeListFile, 
		    ival("dt", 0),                //diskThresh, 
		    nodeWeightScheme, 
		    ival("d", 10),                //nr_of_iterations, 
		    sval("f"),                    //outFileName, 
		    ival("from", -1),             //disamb_from, 
		    ival("to", 0),                //disamb_to, 
		    sval("tg", ""),               //targetFile, 
		    ival("N", Integer.MAX_VALUE), //entry_topN,
		    ival("T", 0),                 //entry_thresh,
		    ival("n", Integer.MAX_VALUE), //edge_topN,
		    ival("t", 0));                //edge_thresh
	}
	
	private final int ival(String opt, int def) {
	  return Integer.parseInt(sval(opt, 
	      Integer.toString(def)));
	}
	
	private final String sval(String opt) {
	  return cmd.getOptionValue(opt);
	}
	
	private final String sval(String opt, String def) {
	  return cmd.getOptionValue(opt, def);
	}
}

