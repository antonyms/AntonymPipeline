package org.jobimtext.hadoop.conf;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public abstract class HadoopJob extends
		org.jobimtext.hadoop.conf.JobimJob {
	@XmlTransient
	protected Job job;

	public HadoopJob() throws IOException {
		super();
		job = new Job();
	}

	/**
	 * Create a configuration with some reasonable defaults
	 */
	// protected Configuration createConf() {
	// Configuration conf = new Configuration();
	// for (Entry<String, String> e : hadoopConfiguration.entrySet()) {
	// conf.set(e.getKey(), e.getValue());
	// }
	// return conf;
	// // int maptasks = 100;
	// // conf.set("mapred.tasktracker.map.tasks.maximum", "" + maptasks);
	// // conf.set("mapred.map.tasks", "" + maptasks);
	// // conf.set("mapred.tasktracker.map", "" + maptasks);
	// //
	// // int reducetasks = 100;
	// // conf.set("mapred.tasktracker.reduce.tasks.maximum", "" +
	// // reducetasks);
	// // conf.set("mapred.reduce.tasks", "" + reducetasks);
	// // conf.set("mapred.tasktracker.reduce", "" + reducetasks);
	// //
	// // conf.set("mapred.job.map.memory.mb", "2048");
	// // conf.set("mapred.job.reduce.memory.mb", "2048");
	// //
	// // conf.set("dfs.replication", "1");
	// //
	// // conf.setBoolean("mapred.output.compress", true);
	// // conf.set("mapred.output.compression.codec",
	// // "org.apache.hadoop.io.compress.GzipCodec");
	// //
	// // return conf;
	// }

	/**
	 * Create a JoBim Job with reasonable defaults
	 */
	protected Job createJob(Map<String, String> generalSettings)
			throws IOException {

		Map<String, String> m = PipelineConfiguration.combineSettings(generalSettings, hadoopConfiguration);
		Configuration conf = new Configuration();
		for (Entry<String, String> e : m.entrySet()) {
			conf.set(e.getKey(), e.getValue());
		}
//		conf.addResource(new Path("/etc/hadoop/conf.cloudera.yarn1/mapred-site.xml"));
//	    conf.addResource(new Path("/etc/hadoop/conf.cloudera.hdfs1/hdfs-site.xml"));
	    
		Job job = new Job(conf);
		
		job.setJobName(getName()+" "+conf.get("mapred.input.dir")+"\t"+conf.get("mapred.output.dir"));
		job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		return job;
	}

	public void run() throws Exception {
		job.waitForCompletion(true);
		
	}
	
	
	private  String getClassName() {
		return (new CurClassNameGetter()).getClassName();
	}

	private  String getShortClassName() {
		String cn = getClassName();
		return cn.substring(cn.lastIndexOf('.') + 1);
	}

	private  Class getClassObject() {
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
	private  class CurClassNameGetter extends SecurityManager {
		public String getClassName() {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			for (StackTraceElement e : stack) {
				if (e.getClassName().startsWith("org.jobimtext.hadoop")) {
					return e.getClassName();
				}
			}
			return "";
		}
	}

}
