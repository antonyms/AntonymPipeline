package edu.antonym.pipeline;

import java.io.IOException;

import edu.antonym.RawPILSAVec;
import edu.antonym.prototype.EmbeddingEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.test.TestCase1;
import edu.antonym.test.TestCase2;
import edu.antonym.test.TestCaseGRE;

/**
 * TEST0: lists of antonym and synonym, contains multiple words in a row. 
 * TEST1: lists of antonym and synonym pairs.
 */

public class PipelineForTest {
	public static void main(String[] args) throws IOException{
		VectorEmbedding embedding = new RawPILSAVec(false);
		EmbeddingEvaluator evaluator = new TestCaseGRE();
		evaluator.score(embedding);
		evaluator = new TestCase1();
		evaluator.score(embedding);
		evaluator = new TestCase2();
		evaluator.score(embedding);
	}
}
