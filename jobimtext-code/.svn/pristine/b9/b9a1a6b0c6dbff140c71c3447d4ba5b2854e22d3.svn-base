package org.jobimtext.hadoop.conf;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class SimCounts1WithFeatures extends HadoopJob {

	public SimCounts1WithFeatures() throws IOException {
		super();
	}

	/*
	 * The mapper takes the feature and its word list (all the words having this
	 * feature) as input It then compares all words pair-wise and outputs 1/n,
	 * with n being the number of words.
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		private Text wfeat = new Text();
		private int threshold;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			this.threshold = context.getConfiguration().getInt("l",
					1000);
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			String[] line = value.toString().split("\t");
			wfeat.set(Double.toString(1.0f) + "\t" + line[0]);
			if (line.length >= 2 && line.length < threshold) {
				try {
					for (int i = 1; i < line.length; i++) {
						for (int j = 1; j < line.length; j++) {
							context.write(new Text(line[i] + "\t" + line[j]),
									wfeat);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * The reducer step will sum all float values, i.e. the weight for any
	 * (word1,word2) pair sharing a feature.
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException {

			double sum = 0f;
			String[] inputList = null;
			ArrayList<String> resultList = new ArrayList<String>();
			String endResult = "";

			// sum all values of this key
			for (Text t : values) {
				String line = t.toString();
				inputList = line.split("\t");
				// the weight is the first element
				sum += Double.valueOf(inputList[0]);
				// rest: concatenate the features list
				for (int i = 1; i < inputList.length; i++) {
					resultList.add(inputList[i]);
				}
			}

			// begin final result with the weight and then tab
			endResult = Double.toString(sum) + "\t";

			// then concatenate the feature list
			for (String s : resultList) {
				endResult += s + " ";
			}

			// output new string: summed weight and expanded list
			try {
				context.write(key, new Text(endResult));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void run(java.util.Map<String, String> generalSettings)
			throws Exception {
		job = createJob(generalSettings);
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);
		super.run();

	}

}
