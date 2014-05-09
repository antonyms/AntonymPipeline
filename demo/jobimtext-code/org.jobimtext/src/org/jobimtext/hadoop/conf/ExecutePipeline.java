package org.jobimtext.hadoop.conf;


public class ExecutePipeline {
	public static void main(String[] args) throws Exception {
		String file = args[0];
		PipelineConfiguration pipeline = PipelineConfiguration
				.loadFromXml(file);
		pipeline.putConf("hdfsDirectory", pipeline.getHdfsDirectory());
		// check input
		// verifyInput(pipeline.getLocalInputDirectory(),
		// pipeline.getHdfsDirectory());

		System.out.println(pipeline.jobSize());
		pipeline.simplifyConfiguration();
		for (JobimJob j : pipeline.getJobs()) {
			try {
				System.out.println(j.getName());
				j.run(pipeline.getGeneralConfiguration());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void verifyInput(String localInput, String hdfsDir) {
		System.out.println("Verify Input!");
		// try {
		// // get the input path in HDFS
		// Configuration conf = new Configuration();
		// FileSystem hdfs = FileSystem.get(conf);
		//
		// if (hdfs.exists(new Path(hdfsDir))) {
		// System.out
		// .println("HDFS input path already exists, skipping put.."
		// + localInput);
		// return;
		// }
		// Path inputPath = new Path(localInput.substring(0,
		// localInput.lastIndexOf('/')));
		// // get the data path and copy to HDFS
		// if (hdfsDir != null) {
		// Path holesPath = new Path(hdfsDir);
		// System.out.println("Creating HDFS input directory at "
		// + inputPath);
		// hdfs.mkdirs(inputPath);
		// System.out.println("Copying input data to HDFS from "
		// + holesPath);
		// hdfs.copyFromLocalFile(holesPath, inputPath);
		// } else {
		// System.err
		// .println("Must specify a valid path to holing output with -h");
		// System.exit(1);
		// }
		// } catch (IOException e) {
		// System.out.println("Exception in verify input");
		// e.printStackTrace();
		// }
	}
}
