package edu.antonym.nn.training;

import java.io.File;
import java.io.FileNotFoundException;

import cc.mallet.optimize.GradientAscent;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizer;
import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.nn.LinearVectorMetric;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

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
			cachedOptimizer = new GradientAscent(new RegularizedOptimizable(
					embedding, 1, 1));
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
			while (batchend < th.numEntries()) {
				embedding.setActiveEntries(batchstart, batchend);
				if (optim.optimize(1)) { // if convergence
					break training_loop;
				}
				batchstart = batchend;
				batchend += batchsize;
			}
			embedding.setActiveEntries(batchstart, th.numEntries());
			if (optim.optimize(1)) {
				break training_loop;
			}
			System.out
					.println("One pass of dataset complete.  Objective value "
							+ embedding.getValue());
		}

		return embedding;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th = new SimpleThesaurus(vocab, new File(
				"data/test-data/syn0.txt"), new File("data/test-data/ant0.txt"));
		NormalizedVectorEmbedding origEmbed = new NormalizedTextFileEmbedding(
				new File("data/huangvectors.txt"), vocab);
		LinearMetricTrainer t = new LinearMetricTrainer(origEmbed, 100);
		LinearVectorMetric m = t.train(th);
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("abroad")),
				origEmbed.getVectorRep(vocab.lookupWord("overseas"))));
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("abroad")),
				origEmbed.getVectorRep(vocab.lookupWord("home"))));
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("abroad")),
				origEmbed.getVectorRep(vocab.lookupWord("cat"))));
		
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("above")),
				origEmbed.getVectorRep(vocab.lookupWord("below"))));
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("above")),
				origEmbed.getVectorRep(vocab.lookupWord("top"))));
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("abroad")),
				origEmbed.getVectorRep(vocab.lookupWord("cat"))));
		
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("ask")),
				origEmbed.getVectorRep(vocab.lookupWord("inquire"))));
		System.out.println(m.innerProd(
				origEmbed.getVectorRep(vocab.lookupWord("ask")),
				origEmbed.getVectorRep(vocab.lookupWord("cat"))));
	}
}
