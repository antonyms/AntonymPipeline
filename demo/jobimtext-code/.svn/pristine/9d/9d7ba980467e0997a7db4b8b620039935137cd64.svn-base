package org.jobimtext.hadoop.conf;

import java.io.File;
import java.util.HashMap;



public class TestExec {
public static void main(String[] args) throws Exception {
	PipelineConfiguration pipeline = new PipelineConfiguration("in","out", "fake");
	
	HashMap hm =new HashMap<String, String>();
	hm.put("input", "input/path");
	hm.put("output", "{start.input}");
	
	pipeline.addJob(new PigJob("test", "input/script.pig", hm));
	pipeline.addJob(new PigJob("test", "input/script.pig", hm));
	hm.put("mapred.queue.name", "NNNNN");
	
	pipeline.addJob(new PigJob("test", "input/script.pig", hm));
	pipeline.addJob(new SimCounts1WithFeatures());
	pipeline.addJob(new AggregatePerFeature());
	
	System.out.println(pipeline.jobSize());
	pipeline.putConf("mapred.queue.name", "ltec");
	pipeline.putConf("start.input", "STARTINPUT");
	String reducetasks = "{NUM_REDUCER}__{start.input}";
	pipeline.putConf("NUM_REDUCER", "100");
	
	pipeline.putConf("mapred.tasktracker.reduce.tasks.maximum", "" + reducetasks);
	pipeline.putConf("mapred.reduce.tasks", "" + reducetasks);
	pipeline.putConf("mapred.tasktracker.reduce", "" + reducetasks); 
	File f =new File("config_hadoop.xml");
	pipeline.run();
	pipeline.saveAsXml(f);
	PipelineConfiguration p = pipeline.loadFromXml(f);
	System.out.println(p.getGeneralConfiguration());
}
}
