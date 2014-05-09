
package org.jobimtext.hadoop.conf;
	    
import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The Aggregate Per Feature (APF) job aggregates all words per feature.
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class AggregatePerFeature extends HadoopJob {
  
  /*
   * Main
   */
  public AggregatePerFeature () throws IOException {
    super();
  }

  /*
   * The Mapper
   */
	public static class Map extends Mapper <LongWritable, Text, Text, Text> {

		private Text keyOut = new Text();
		private Text valOut = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException {
			String[] tokens = value.toString().split("\t");
			String word = trim(tokens[0]);
			String feature = trim(tokens[1]);
			if (word.length() > 0 && feature.length() > 0) {
				keyOut.set(feature);
				valOut.set(word);
				try {
          context.write(keyOut, valOut);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
			}
		}

	}

	/*
	 * The Reducer
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
	  
	  @Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException {
			StringBuilder concat = new StringBuilder();
			for (Text t: values) {
				String next = t.toString();
				if (!next.trim().equals("")) {
					concat.append("\t").append(next);
				}
			}
			try {
        context.write(key, new Text(trim(concat.toString())));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
		}
		
	}

	public static String trim(String word){
    return word.replace((char)65533, ' ').trim()
        .replaceAll("^[\\s\\xA0]+", "")
        .replaceAll("[\\s\\xA0]+$", "");
  }

	@Override
	public void run(java.util.Map<String, String> generalSettings)
			throws Exception {
		System.out.println("CONFIG");
		 for(Entry<String, String> e: job.getConfiguration()){
		    	System.out.println(e.getKey()+"\t"+e.getValue());
		    }
		job = createJob(generalSettings);
	    job.setJarByClass(this.getClass());
		job.setMapperClass(Map.class);
	    job.setCombinerClass(Reduce.class);
	    job.setReducerClass(Reduce.class);
	   
	    super.run();
	}
	
}
