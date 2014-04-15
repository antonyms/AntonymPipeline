package edu.antonym.prototype;

import java.io.IOException;
import java.util.List;

public interface EmbeddingEvaluator {
	public List<Float> score(VectorEmbedding embedding) throws IOException;
}
