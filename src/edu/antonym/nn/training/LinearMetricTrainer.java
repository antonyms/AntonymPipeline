package edu.antonym.nn.training;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cc.mallet.optimize.ConjugateGradient;
import cc.mallet.optimize.GradientAscent;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable;
import cc.mallet.optimize.OptimizationException;
import cc.mallet.optimize.Optimizer;
import cc.mallet.optimize.tests.TestOptimizable;
import edu.antonym.MarketMatrixTensor;
import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.Util;
import edu.antonym.metric.LinearVectorMetric;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.SubThesaurus;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.test.RandomizedTestCase;
import edu.antonym.test.TestCase;
import edu.antonym.test.TestCaseGRE;

public class LinearMetricTrainer {

	Optimizer cachedOptimizer;
	LinearVectorMetric embedding;

	int batchsize;
	int numiterations = Integer.MAX_VALUE;

	double regweight;
	boolean LBFGS;

	public LinearMetricTrainer(NormalizedVectorEmbedding orig) {
		this(orig, 1000, 0.1, false);
	}

	public LinearMetricTrainer(NormalizedVectorEmbedding orig, int batchsize,
			double regweight, boolean LBFGS) {
		this.embedding = new LinearVectorMetric(orig);
		this.batchsize = batchsize;
		this.regweight = regweight;
		this.LBFGS = LBFGS;
	}

	public void setNumIterations(int n) {
		this.numiterations = n;
	}

	public Optimizer getOptimizer() {
		if (cachedOptimizer == null) {
			Optimizable.ByGradientValue o =embedding;
					//new RegularizedOptimizable(					embedding, Math.sqrt(embedding.getNumParameters()), regweight);
			if (LBFGS) {
				cachedOptimizer = new LimitedMemoryBFGS(o);
			} else {
				GradientAscent a=new GradientAscent(o);
				cachedOptimizer = a;
			}
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
			boolean converged = true;
			for (int j = 0; j < th.numEntries() / batchsize; j++) {
				try {
					embedding.setThesaurus(SubThesaurus.SampleThesaurus(th,
							batchsize));
					if (!optim.optimize(1)) { // if convergence
						converged=false;
					} else {
						break training_loop;
					}
				} catch (OptimizationException e) {
				}
				System.out.println("One minibatch complete. Batch value "
						+ embedding.getValue());
			}
			embedding.setThesaurus(th);
			System.out
					.println("One pass of dataset complete.  Objective value "
							+ embedding.getValue());
			if (converged) {
				System.out.println("Converged!");
				break training_loop;
			}
		}

		return embedding;
	}

	public static void main(String[] args) throws IOException {
	MarketMatrixTensor origEmbed=new MarketMatrixTensor(58);
		Vocabulary vocab = origEmbed.getVocab();
		//Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th = new SimpleThesaurus(vocab, new File(
				"data/combine/synonym.txt"), new File("data/combine/antonym.txt"));
		//NormalizedVectorEmbedding origEmbed = new NormalizedTextFileEmbedding(
		//		new File("data/huangvectors.txt"), vocab);
		
		
		Thesaurus[] split = SubThesaurus.splitThesaurus(th, new double[] { 0.8,
				0.2 }); // train/test split
		LinearMetricTrainer t = new LinearMetricTrainer(origEmbed, 1000, 1,
				true);

		int ind = 0;
		for (int i = 0; i < origEmbed.getDimension(); i++) {
			t.embedding.setParameter(ind, origEmbed.getT(i));
			ind += (i + 2);
		}
		LinearVectorMetric m = t.train(split[0],100);


		RandomizedTestCase n = new RandomizedTestCase(split[1]);
		double score = n.score(m);
		System.out.println("score = " + score);

		score = n.score(origEmbed);
		System.out.println("origscore = " + score);

	
		n = new RandomizedTestCase(split[0]);
		score = n.score(m);
		System.out.println("trainscore = " + score);

		score = n.score(origEmbed);
		System.out.println("origtrainscore = " + score);

		TestCaseGRE g=new TestCaseGRE();
		score=g.score(m);
		double origscore=g.score(origEmbed);
		System.out.println("GRE results originally "+origscore+" after optimization "+score);
		System.out.println("PARAMETERS");
		for(int i=0; i<m.getNumParameters(); i++) {
			System.out.println(m.getParameter(i));
		}
	}
}
