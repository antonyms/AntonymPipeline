package edu.antonym;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.antonym.prototype.VectorEmbedding;

public class RawPILSAVec implements VectorEmbedding {

	List<String> words;
	Map<String, Integer> ids;
	Map<Integer, float[]> vectors;
	int OOVindex;
	
	static final int dim = 300;
	static final String lsaPath = "data/PILSA/s03.04b--06.lsa.k300.txt";
	static final String s2netPath = "s03.04b--06.s2net.fromLSA.k300.pickle.r11.txt";
	

	public RawPILSAVec(Boolean useS2net) throws IOException {
		words = new ArrayList<String>();
		ids = new HashMap<String, Integer>();
		vectors = new HashMap<Integer, float[]>();
		
		String line;
		BufferedReader br;
		if (!useS2net) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(lsaPath)));			
		}
		else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(s2netPath)));
		}
		int index = 0; // TODO for now create own lookup dictionary
		while ((line = br.readLine()) != null) {
		    int pos = line.indexOf('\t');
		    String word = line.substring(0, pos);
		    String[] svec = line.substring(pos+1).split(" ", dim);
		    float[] vec = new float[300];
		    for (int i = 0; i < dim; i++) {
		    	vec[i] = Float.parseFloat(svec[i]);
		    }
		    words.add(word);
		    ids.put(word, index);
		    index++;
		    vectors.put(ids.get(word), vec);
		}
		br.close();
	}


	@Override
	public int getDimension() {
		return dim;
	}
	
	public int getVocabSize() {
		return words.size();
	}


	@Override
	public float[] getVectorRep(int word) {		
		return vectors.get(word);
	}

	public int getWordId(String word){
		return ids.get(word);
	}
	
	@Override
	public int findClosestWord(float[] vector) {
		// TODO Auto-generated method stub
		return 0;
	}	
	

	public static void main(String[] args) throws IOException {
		RawPILSAVec word2vec = new RawPILSAVec(false);
		System.out.println(word2vec.getVocabSize());
	    System.exit(0);
	}
}
