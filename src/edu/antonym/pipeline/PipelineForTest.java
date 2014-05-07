package edu.antonym.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cc.mallet.types.Metric;
import edu.antonym.MarketMatrixTensor;
import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.RawPILSAVec;
import edu.antonym.SimpleVocab;
import edu.antonym.TextFileEmbedding;
import edu.antonym.ThesaurusImp;
import edu.antonym.Util;
import edu.antonym.metric.InvertMetric;
import edu.antonym.nn.training.LinearMetricTrainer;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;
import edu.antonym.test.TestCase;
import edu.antonym.test.TestCase1;
import edu.antonym.test.TestCase2;
import edu.antonym.test.TestCaseGRE;
import edu.antonym.traindata.prepare.WordNetHelper;

/**
 * TEST0: lists of antonym and synonym, contains multiple words in a row. 
 * TEST1: lists of antonym and synonym pairs.
 */

public class PipelineForTest {
	public static void main(String[] args) throws IOException{
		/*Thesaurus t = new ThesaurusImp(new File("data/test-data/ant0.txt"), new File("data/test-data/syn0.txt"), new File("data/huangvocab.txt"));
		Vocabulary voc = t.getVocab();
		int black = voc.lookupWord("black");
		System.out.println(t.numEntries());
		int white = voc.lookupWord("white");				
		System.out.println(t.isAntonym(black, white));		

		NormalizedVectorEmbedding embedding = new NormalizedTextFileEmbedding(new File("data/huangvectors.txt"), voc);
		LinearMetricTrainer trainer=new LinearMetricTrainer(embedding);
		
		WordMetric metric= trainer.train(t);
		MetricEvaluator evaluator = new TestCaseGRE();
		double score= evaluator.score(metric);*/
		
		//String[] input = {"-a", "data/test-data/ant1.txt", "-s", "data/test-data/syn1.txt",};
		//MetricEvaluator evaluator = new TestCase(input);		
		//evaluator.score(embedding);
		TestCaseGRE evaluator = new TestCaseGRE();
		double[] score = new double[40];
		for(int i = 0; i < 1; i++) {
			VectorEmbedding embedding = new MarketMatrixTensor(i);			
			score[i] = evaluator.score(embedding, "testset.txt");			
		}
		double max = 0;
		double argmax = -1;
		for(int i = 0; i < 1; i++) {
			if(max < score[i]) {
				max = score[i];
				argmax = i;
			}
		}
		System.out.println(max);
		System.out.println(argmax);
		
		
		
	}
}
