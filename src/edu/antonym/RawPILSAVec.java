package edu.antonym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class RawPILSAVec implements VectorEmbedding {

	Vocabulary vocab; // May change to share one vocabulary for all method
	Map<Integer, double[]> vectors;

	static final int dim = 300;
	static final String lsaPath = "data/PILSA/s03.04b--06.lsa.k300.txt";
	static final String s2netPath = "data/PILSA/s03.04b--06.s2net.fromLSA.k300.pickle.r11.txt";
	static final String vocPath = "data/PILSA/s03.04b.matrix_sparse_tfidf.voc";

	public RawPILSAVec(Boolean useS2net) throws IOException {
		vocab = new SimpleVocab(new File(vocPath));
		vectors = new HashMap<Integer, double[]>();

		String line;
		BufferedReader br;
		if (!useS2net) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					lsaPath)));
		} else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					s2netPath)));
		}

		while ((line = br.readLine()) != null) {
			int pos = line.indexOf('\t');
			String word = line.substring(0, pos);
			String[] svec = line.substring(pos + 1).split(" ", dim);
			double[] vec = new double[300];
			for (int i = 0; i < dim; i++) {
				vec[i] = Float.parseFloat(svec[i]);
			}
			int index = vocab.lookupWord(word);
			vectors.put(index, vec);
		}
		br.close();
	}

	@Override
	public int getDimension() {
		return dim;
	}

	@Override
	public Vocabulary getVocab() {
		return vocab;
	}

	@Override
	public double[] getVectorRep(int word) {
		return vectors.get(word);
	}

	@Override
	public int findClosestWord(double[] vector) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String[] args) throws IOException {
		RawPILSAVec word2vec = new RawPILSAVec(false);
		Vocabulary voc = word2vec.getVocab();
		System.out.println(voc.getVocabSize());
		System.exit(0);
	}

	@Override
	public double distance(int word1, int word2) {
		return Util.cosineSimilarity(vectors.get(word1), vectors.get(word2));
	}

}
