package edu.antonym.test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.RawPILSAVec;
import edu.antonym.test.ClusterHelper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class TestCaseKmeans {

	/**
	 * Given a string, cluster the words into two clusters
	 * Put the result in a file result.txt
	 * 
	 */
	public static double[] toDoubleArray(float[] f){
		double[] d = new double[f.length];
	    for(int i=0;i<f.length;i++){
	    	d[i] = (double)f[i];
	    }
	    return d;
	}
  
	public static void main(String args[]) throws Exception {
    
	    int k = 2;
	    
	    RawPILSAVec pilsaVect = new RawPILSAVec(false);
	   
	    //String s = new String("tidy#JJ	4	opulent#JJ, unassuming#JJ, quiet#JJ, charming#JJ, slick#JJ, splendid#JJ, elaborate#JJ, plush#JJ, sumptuous#JJ, pretty#JJ, supple#JJ, satisfying#JJ, dignified#JJ, uncluttered#JJ, lucid#JJ, lively#JJ, glossy#JJ, stylish#JJ, lovely#JJ, homey#JJ, colorful#JJ, chic#JJ, lush#JJ, messy#JJ, fancy#JJ, appealing#JJ, sedate#JJ, elegant#JJ, nifty#JJ, tasteful#JJ, comfy#JJ, no-nonsense#JJ, luxurious#JJ, snappy#JJ, luminous#JJ, trim#JJ, stunning#JJ, neat#JJ, resonant#JJ, beautiful#JJ, humble#JJ, groomed#JJ, slim#JJ, sleek#JJ, bright#JJ, complex#JJ, vibrant#JJ, pleasing#JJ, minimalist#JJ, utilitarian#JJ, pleasant#JJ, sparkling#JJ, energetic#JJ, shimmering#JJ, perfect#JJ, nice#JJ, intricate#JJ, spare#JJ, decent#JJ, unpretentious#JJ, brilliant#JJ, inviting#JJ, graceful#JJ, airy#JJ");
	    //String s = new String("million-dollar#JJ	0	adjoining#JJ, luxury#JJ, century-old#JJ, plush#JJ, spacious#JJ, historic#JJ, refurbished#JJ, storied#JJ, beachfront#JJ, three-story#JJ, posh#JJ, nearby#JJ, turn-of-the-century#JJ, two-story#JJ, seaside#JJ, dilapidated#JJ, converted#JJ, renovated#JJ, brand-new#JJ, landmark#JJ");
	    //String s = new String("enormous#JJ	1	diplomatic#JJ, monetary#JJ, psychological#JJ, technological#JJ, ecological#JJ, geographical#JJ, emotional#JJ, moral#JJ, budgetary#JJ, personal#JJ, financial#JJ, historical#JJ, social#JJ, political#JJ, economic#JJ, societal#JJ, intellectual#JJ, geopolitical#JJ, existential#JJ, human#JJ, cultural#JJ, artistic#JJ, fiscal#JJ, electoral#JJ, stylistic#JJ, strategic#JJ");
	    String s = new String("considerable#JJ	0	racial#JJ, ideological#JJ, tactical#JJ, diplomatic#JJ, personal#JJ, theological#JJ, historical#JJ, literary#JJ, intellectual#JJ, societal#JJ, existential#JJ, geopolitical#JJ, moral#JJ, fiscal#JJ, stylistic#JJ, organizational#JJ, mental#JJ, internal#JJ, scientific#JJ, strategic#JJ, technological#JJ, linguistic#JJ, economic#JJ, managerial#JJ, budgetary#JJ, statistical#JJ, financial#JJ, technical#JJ, artistic#JJ, social#JJ, aesthetic#JJ, institutional#JJ, psychological#JJ, emotional#JJ, editorial#JJ, rhetorical#JJ, esthetic#JJ, cultural#JJ, monetary#JJ, marital#JJ, spiritual#JJ, physical#JJ, political#JJ, visual#JJ)");
	    
	    List<String> voc = new ArrayList<String>();
	    List<Vector> vectors = new ArrayList<Vector>();
	    List<String> notfound = new ArrayList<String>();
	    
	    String[] words = s.split("\\s+");
	    for(int i=0;i<words.length;i++){
	    	if(i==1){
	    		continue;
	    	}
	    	String word = words[i];
	    	int index = word.indexOf("#");
	    	if(index!=-1){
	    		word = word.substring(0, index);
	    	}
	    	try{
	    		float[] f = pilsaVect.getVectorRep(pilsaVect.getWordId(word));
	    		double[] d = toDoubleArray(f);
	    		Vector vec = new RandomAccessSparseVector(d.length);
	    	    vec.assign(d);
	    	    vectors.add(vec);
	    	    voc.add(word);
			}
			catch(NullPointerException e){
				notfound.add(word);
				System.out.println("No such word: " + word+ " ");
			}
	    }
	    
	    File testData = new File("testdata");
	    if (!testData.exists()) {
	      testData.mkdir();
	    }
	    testData = new File("testdata/points");
	    if (!testData.exists()) {
	      testData.mkdir();
	    }
	    
	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(conf);
	    ClusterHelper.writePointsToFile(vectors, conf, new Path("testdata/points/file1"));
	    
	    Path path = new Path("testdata/clusters/part-00000");
	    SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf,
	        path, Text.class, Kluster.class);
	    
	    for (int i = 0; i < k; i++) {
	      Vector vec = vectors.get(i);
	      Kluster cluster = new Kluster(vec, i, new EuclideanDistanceMeasure());
	      writer.append(new Text(cluster.getIdentifier()), cluster);
	    }
	    writer.close();
	    
	    Path output = new Path("output");
	    HadoopUtil.delete(conf, output);
	    
	    
	    KMeansDriver.run(conf, new Path("testdata/points"), new Path("testdata/clusters"),
	      output, new EuclideanDistanceMeasure(), 0.001, 10,
	      true, 0.0, false);
	  
	   
	    SequenceFile.Reader reader = new SequenceFile.Reader(fs,
	        new Path("output/" + Kluster.CLUSTERED_POINTS_DIR
	                 + "/part-m-00000"), conf);
	    
	    IntWritable key = new IntWritable();
	    WeightedVectorWritable value = new WeightedVectorWritable();
	    
	    FileWriter out = new FileWriter("output/result.txt"); 
	    List<String> cluster0 = new ArrayList<String>();
	    List<String> cluster1 = new ArrayList<String>();
	    int j = 0;
	    while (reader.next(key, value)) {
	    	if(key.toString().equalsIgnoreCase("0")){
	    		cluster0.add(voc.get(j));
	    	}
	    	else{
	    		cluster1.add(voc.get(j));
	    	}
	    	//System.out.println(value.toString() + " belongs to cluster "+ key.toString());
	    	j++;
	    }
	    out.append("cluster 0: ");
	    for(j=0;j<cluster0.size();j++){
	    	out.append(cluster0.get(j)+" ");
	    }
	    out.append("\n");
	    out.append("cluster 1: ");
	    for(j=0;j<cluster1.size();j++){
	    	out.append(cluster1.get(j)+" ");
	    }
	    out.append("\n");
	    
	    if(notfound.size()!=0){
	    	out.append("word not found: ");
	        for(j=0;j<notfound.size();j++){
	        	out.append(notfound.get(j)+" ");
	        }
	    }
	    out.append("\n");
	    System.out.println("finished!");
	    
	    reader.close();
	    out.close();
  }
  
}
