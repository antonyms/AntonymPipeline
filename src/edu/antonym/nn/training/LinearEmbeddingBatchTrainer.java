package edu.antonym.nn.training;

import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizer;
import edu.antonym.nn.LinearEmbedding;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;

public class LinearEmbeddingBatchTrainer implements EmbeddingTrainer {

	Optimizer cachedOptimizer;
	LinearEmbedding embedding;

	int batchsize;
	int numiterations = Integer.MAX_VALUE;

	public LinearEmbeddingBatchTrainer(NormalizedVectorEmbedding orig, int ndim,
			int batchsize) {
		this.embedding = new LinearEmbedding(orig, ndim);
		this.batchsize = batchsize;
	}

	public void setNumIterations(int n) {
		this.numiterations = n;
	}

	public Optimizer getOptimizer() {
		if (cachedOptimizer == null) {
			cachedOptimizer = new LimitedMemoryBFGS(embedding);
		}
		return cachedOptimizer;
	}

	@Override
	public LinearEmbedding train(Thesaurus th) {
		return train(th, numiterations);
	}

	public LinearEmbedding train(Thesaurus th, int iterations) {
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
}
