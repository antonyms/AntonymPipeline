package edu.antonym.nn.training;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;

public interface EmbeddingTrainer {

	public VectorEmbedding train(Thesaurus th);
}
