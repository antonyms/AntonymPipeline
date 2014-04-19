package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;

import cc.mallet.types.MatrixOps;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class NormalizedTextFileEmbedding extends TextFileEmbedding implements NormalizedVectorEmbedding {

	public NormalizedTextFileEmbedding(File f, Vocabulary vocab)
			throws FileNotFoundException {
		super(f, vocab);
		
		//Normalize embeddings
		for (int i = 0; i < embeddings.length; i++) {
			MatrixOps.twoNormalize(embeddings[i]);
		}
	}

}
