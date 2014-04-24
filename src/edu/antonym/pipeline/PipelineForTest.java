package edu.antonym.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cc.mallet.types.Metric;
import edu.antonym.RawPILSAVec;
import edu.antonym.TextFileEmbedding;
import edu.antonym.ThesaurusImp;
import edu.antonym.metric.InvertMetric;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;
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
		Thesaurus t = new ThesaurusImp(new File("data/WordNet-3.0/antonym.txt"), new File("data/WordNet-3.0/synonym.txt"), new File("data/huangvocab.txt"));
		Vocabulary voc = t.getVocab();
		int black = voc.lookupWord("black");
		System.out.println(t.numEntries());
		int white = voc.lookupWord("white");				
		System.out.println(t.isAntonym(black, white));		
		List<Integer> ants = t.getSynonyms(black);
		for (int w : ants) {
			System.out.println(voc.lookupIndex(w));
		}
		
		VectorEmbedding embedding = new TextFileEmbedding(new File("data/huangvectors.txt"), voc);
		WordMetric metric= new InvertMetric(embedding);
		MetricEvaluator evaluator = new TestCaseGRE();
		double score= evaluator.score(metric);
//		evaluator = new TestCase1();
//		evaluator.score(embedding);
//		evaluator = new TestCase2();
//		evaluator.score(embedding);
	}
}
