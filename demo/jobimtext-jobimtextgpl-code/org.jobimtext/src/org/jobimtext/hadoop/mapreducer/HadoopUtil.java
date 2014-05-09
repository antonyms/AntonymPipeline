package org.jobimtext.hadoop.mapreducer;

import java.util.Map;

import org.apache.hadoop.mapred.JobConf;

public class HadoopUtil {
	public static String generateJobName(String[] args) {
		return getClassName() + ": " + args[0] + " " + args[1];
	}

	public static String getClassName() {
		String cn = (new CurClassNameGetter()).getClassName();
		return cn;
	}

	public static String getShortClassName() {
		String cn = getClassName();
		return cn.substring(cn.lastIndexOf('.') + 1);
	}

	public static Class getClassObject() {
		String cn = getClassName();
		Class c = null;
		try {
			c = Class.forName(cn);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	// Static Nested Class doing the trick
	public static class CurClassNameGetter extends SecurityManager {
		public String getClassName() {
			for (final Map.Entry<String, String> entry : System.getenv()
					.entrySet()) {
				 StackTraceElement[] stack = Thread.currentThread ().getStackTrace ();
				 for(StackTraceElement e:stack){
					 if(e.getClassName().startsWith("org.jobimtext")){
						 
						 return e.getClassName();
					 }
				 }
			}
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	public static JobConf generateJobConf(String[] args) {
		if (args.length >= 3) {
			return generateJobConf(args, Boolean.parseBoolean(args[2]));
		} else {
			return generateJobConf(args, false);
		}
		

	}

	public static JobConf generateJobConf(String[] args, boolean compressed) {
		JobConf conf = new JobConf(getClassObject());
		System.out.println(getShortClassName());
		String queue = "longrunning";
		conf.setJobName(getShortClassName() + " " + args[0] + " " + args[1]);
		if (compressed) {
			/*
			 * use compression
			 */
//			conf.setBoolean("mapred.compress.map.output",true);
//			conf.setBoolean("mapred.compress.map.output",true);
//			System.out.println("Compression: "+conf.getBoolean("mapred.compress.map.output", false));
			conf.setBoolean("mapred.output.compress", true);
//			conf.set("mapred.map.output.compression.codec",
//					"org.apache.hadoop.io.compress.SnappyCodec");
//			conf.set("mapred.output.compression.codec",
//					"org.apache.hadoop.io.compress.SnappyCodec");
			conf.set("mapred.output.compression.codec",
					"org.apache.hadoop.io.compress.GzipCodec");
			
			
			
		}
//		conf.set("mapred.queue.name", queue);
//		conf.setQueueName(queue);
//		conf.set("mapred.job.queue.name", queue);
		return conf;
	}
}
