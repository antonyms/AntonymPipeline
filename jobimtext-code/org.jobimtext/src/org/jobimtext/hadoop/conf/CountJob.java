package org.jobimtext.hadoop.conf;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountJob extends HadoopJob {

	public CountJob() throws IOException {
		super();
	}

	public static class Map extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		private Text keyOut = new Text();
		private String[] tokens;
		private int token;

		public void setup(Context context) {
			token = context.getConfiguration().getInt("token", -1);
		}

		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			tokens = value.toString().split("\t");
			keyOut.set(tokens[token]);
			if (tokens.length != 3) {
				System.err.println("Token length != 3: " + value.toString());
			} else {
				try {
					context.write(keyOut,
							new IntWritable(Integer.parseInt(tokens[2])));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void run(java.util.Map<String, String> generalSettings)
			throws Exception {
		job.setOutputValueClass(IntWritable.class);
		job.setMapperClass(Map.class);
		super.run();
	}

}
