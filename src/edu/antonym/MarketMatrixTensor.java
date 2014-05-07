package edu.antonym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class MarketMatrixTensor implements VectorEmbedding {
	Vocabulary vocab; // May change to share one vocabulary for all method
	Map<Integer, double[]> vectors;
	double[] T;
	double[] TSim;

	int dim;
	static final String rootPath = "data/bptf/";
	
	public MarketMatrixTensor(int idx) throws IOException {
		String uPath = "roget_mm-"+idx+"_U.mm";
		String vPath = "roget_mm-"+idx+"_V.mm";
		vocab = new SimpleVocab(new File(rootPath+"roget_mm-voc"), -1);
		vectors = new HashMap<Integer, double[]>();

		Scanner s = new Scanner(new File(rootPath+vPath));
		s.nextLine();s.nextLine();
		String line = s.nextLine();
		String[] param = line.split("\\s");
		int N = Integer.parseInt(param[0]);
		assert N == vocab.getVocabSize();
		dim = Integer.parseInt(param[1]);
		
		int index = 0; // assume vocabulary index equals to line number
		line = s.nextLine();
		String[] svec = line.trim().split("\\s");
		assert svec.length == dim;
		T = new double[dim];
		for (int i = 0; i < dim; i++) {
			T[i] = Float.parseFloat(svec[i]);
		}
		line = s.nextLine();
		svec = line.trim().split("\\s");
		assert svec.length == dim;
		TSim = new double[dim];
		for (int i = 0; i < dim; i++) {
			TSim[i] = Float.parseFloat(svec[i]);
		}		
		
		s = new Scanner(new File(rootPath+uPath));
		s.nextLine();s.nextLine();
		line = s.nextLine();		
		
		while (s.hasNextLine()) {
			line = s.nextLine();
			svec = line.trim().split("\\s");
			assert svec.length == dim;
			double[] vec = new double[dim];
			for (int i = 0; i < dim; i++) {
				vec[i] = Float.parseFloat(svec[i]);
			}
			vectors.put(index, vec);
			index++;
		}
		
		//GREOOV();
	}
	
	/*private void GREOOV() throws IOException {

		Scanner w = new Scanner(new File("data/test-data/gre_oov.txt.voc"));
		Scanner e = new Scanner(new File("data/test-data/gre_oov.txt"));
		String line;
		while (w.hasNextLine() && e.hasNextLine()) {
			line = w.nextLine().trim();
			assert vocab.lookupWord(line) == vocab.OOVindex();
			int idx = vocab.addWord(line);
			line = e.nextLine();
			String[] svec = line.trim().split("\\s");
			assert svec.length == dim;
			double[] vec = new double[dim];
			for (int i = 0; i < dim; i++) {
				vec[i] = Float.parseFloat(svec[i]);
			}
			vectors.put(idx, vec);
		}
		
	}*/
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


	@Override
	public double similarity(int word1, int word2) {
		double sim = Util.dotProd(Util.elemWiseProd(T, vectors.get(word1)), vectors.get(word2));
		//sim = sim > 1.0 ? 1.0 : sim;
		//sim = sim < -1.0 ? -1.0 : sim;
		return sim;
		//return Util.cosineSimilarity(vectors.get(word1), vectors.get(word2));
	}
}
