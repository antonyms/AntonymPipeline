package org.jobimtext.hadoop.conf2;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.jobimtext.hadoop.conf.JobimJob;
import org.jobimtext.hadoop.conf.PipelineConfiguration;

public class HadoopJob extends JobimJob {
	private Mapper<LongWritable, Text, Text, IntWritable> mapper;
	private Reducer<Text, IntWritable, Text, IntWritable> reducer;

	public HadoopJob() {

	}

	@Override
	public void run(Map<String, String> generalSettings) throws Exception {
		Map<String, String> m = PipelineConfiguration.combineSettings(
				generalSettings, hadoopConfiguration);
		Configuration conf = new Configuration();
		for (Entry<String, String> e : m.entrySet()) {
			conf.set(e.getKey(), e.getValue());
		}
		// conf.addResource(new
		// Path("/etc/hadoop/conf.cloudera.yarn1/mapred-site.xml"));
		// conf.addResource(new
		// Path("/etc/hadoop/conf.cloudera.hdfs1/hdfs-site.xml"));

		JobConf job = new JobConf(conf);

		job.setJobName(getName() + " " + conf.get("mapred.input.dir") + "\t"
				+ conf.get("mapred.output.dir"));
		JobClient.runJob(job);
	}

}
