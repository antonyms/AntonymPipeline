package edu.antonym.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.antonym.RawPILSAVec;
import edu.antonym.ThesaurusImp;
import edu.antonym.prototype.EmbeddingEvaluator;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
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
		Thesaurus t = new ThesaurusImp(new File("data/WordNet-3.0/antonym.txt"), new File("data/WordNet-3.0/synonym.txt"), new File("data/WordNet-3.0/vocabulary.txt"));
		Vocabulary voc = t.getVocab();
		int black = voc.lookupWord("black");
		System.out.println(t.numEntries());
		int white = voc.lookupWord("white");				
		System.out.println(t.isAntonym(black, white));		
		List<Integer> ants = t.getSynonyms(black);
		for (int w : ants) {
			System.out.println(voc.lookupIndex(w));
		}
		
		VectorEmbedding embedding = new RawPILSAVec(false);
		EmbeddingEvaluator evaluator = new TestCaseGRE();
		evaluator.score(embedding);
//		evaluator = new TestCase1();
//		evaluator.score(embedding);
//		evaluator = new TestCase2();
//		evaluator.score(embedding);
	}
}
