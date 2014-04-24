package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import cc.mallet.types.MatrixOps;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class TextFileEmbedding implements VectorEmbedding {

	Vocabulary vocab;
	int dim;
	double[][] embeddings;

	boolean renormalized = false;

	public TextFileEmbedding(File f, Vocabulary vocab)
			throws FileNotFoundException {
		this.vocab = vocab;
		Scanner s = new Scanner(f);

		ArrayList<double[]> emb = new ArrayList<double[]>();
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] spl = line.split("\\s");
			dim = spl.length; // should be the same every time
			double[] vec = new double[dim];
			for (int i = 0; i < dim; i++) {
				vec[i] = Double.parseDouble(spl[i]);
			}
			emb.add(vec);
		}
		embeddings = new double[emb.size()][dim];
		for (int i = 0; i < emb.size(); i++) {
			embeddings[i] = emb.get(i);
		}
	}

	@Override
	public int getDimension() {
		return dim;
	}

	@Override
	public double[] getVectorRep(int word) {
		return embeddings[word];
	}

	@Override
	public int findClosestWord(double[] vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vocabulary getVocab() {
		return vocab;
	}

	@Override
	public double similarity(int word1, int word2) {
		return Util.cosineSimilarity(embeddings[word1], embeddings[word2]);
	}

}
