package edu.antonym.nn.training;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cc.mallet.optimize.GradientAscent;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.OptimizationException;
import cc.mallet.optimize.Optimizer;
import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.metric.LinearVectorMetric;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.SubThesaurus;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.test.RandomizedTestCase;
import edu.antonym.test.TestCase;

public class LinearMetricTrainer {

	Optimizer cachedOptimizer;
	LinearVectorMetric embedding;

	int batchsize;
	int numiterations = Integer.MAX_VALUE;

	public LinearMetricTrainer(NormalizedVectorEmbedding orig, int batchsize) {
		this.embedding = new LinearVectorMetric(orig);
		this.batchsize = batchsize;
	}

	public void setNumIterations(int n) {
		this.numiterations = n;
	}

	public Optimizer getOptimizer() {
		if (cachedOptimizer == null) {
			cachedOptimizer = new GradientAscent(new RegularizedOptimizable( embedding, 1, 0.1));
		}
		return cachedOptimizer;
	}

	public LinearVectorMetric train(Thesaurus th) {
		return train(th, numiterations);
	}

	public LinearVectorMetric train(Thesaurus th, int iterations) {
		embedding.setThesaurus(th);
		Optimizer optim = getOptimizer();
		System.out.println("Beginning Batch training. Objective value "
				+ embedding.getValue());
		training_loop: for (int i = 0; i < iterations; i++) {
			int batchstart = 0;
			int batchend = batchsize;
			boolean converged=true;
			for(int j=0; j<th.numEntries()/batchsize; j++) {
				try {
				embedding.setThesaurus(SubThesaurus.SampleThesaurus(th,batchsize));
				if (optim.optimize(1)) { // if convergence
					break training_loop;
				}
				}catch(OptimizationException e) {
				}
				System.out
				.println("One minibatch complete. Batch value "
						+ embedding.getValue());	
			}
			embedding.setThesaurus(th);
			System.out
					.println("One pass of dataset complete.  Objective value "
							+ embedding.getValue());	
			if(converged) {
				System.out.println("Converged!");
					break training_loop;
				}
		}

		return embedding;
	}

	public static void main(String[] args) throws IOException {
		Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th = new SimpleThesaurus(vocab, new File(
				"data/Rogets/synonym.txt"), new File("data/Rogets/antonym.txt"));
		NormalizedVectorEmbedding origEmbed = new NormalizedTextFileEmbedding(
				new File("data/huangvectors.txt"), vocab);
		LinearMetricTrainer t = new LinearMetricTrainer(origEmbed, 1000);
		LinearVectorMetric m = t.train(th);
		
		String[] input = {"-s", "data/test-data/syn1.txt","-a","data/test-data/ant1.txt"};
		RandomizedTestCase n = new RandomizedTestCase(input);
		double score = n.score(m);
		System.out.println("score = "+score);
		
		score = n.score(origEmbed);
		System.out.println("origscore = "+score);
		
		String[] input2 = {"-s", "data/test-data/syn0.txt","-a","data/test-data/ant0.txt"};
		n = new RandomizedTestCase(input2);
		score = n.score(m);
		System.out.println("trainscore = "+score);
		
		score = n.score(origEmbed);
		System.out.println("origtrainscore = "+score);

	}
}
