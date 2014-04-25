package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class Util {
	static void saveVectors(VectorEmbedding embed, Vocabulary vocab, File out) throws FileNotFoundException {
		int dim=embed.getDimension();
		
		PrintStream ps=new PrintStream(out);
		ps.println(dim);
		for(int i=0; i<vocab.size(); i++) {
			double[] vect=embed.getVectorRep(i);
			for(int j=0; j<dim; j++) {
				ps.print(vect[j]);
				ps.print(' ');
			}
			ps.println();
		}
		ps.close();
	}
	
	public static double cosineSimilarity(double[] vector1, double[] vector2) {
		double dotProduct = 0.0;
		double v1 = 0.0;
		double v2 = 0.0;
		double cosineSimilarity = 0.0;

        for (int i = 0; i < vector1.length; i++) //vector1 and vector2 must be of same length
        {
            dotProduct += vector1[i] * vector2[i];  
            v1 += Math.pow(vector1[i], 2);  
            v2 += Math.pow(vector2[i], 2); 
        }

        v1 = Math.sqrt(v1);	
        v2 = Math.sqrt(v2);	

        if (v1 != 0.0 && v2 != 0.0) {
            cosineSimilarity = dotProduct / (v1 * v2);
        } else {
            return 0;
        }
        return cosineSimilarity;
    }
	static Random r=new Random();
	
	public static int hashInteger(int i, int max) {
		r.setSeed(i);
		return r.nextInt(max);
	}

	public static int hashIntegerPair(int i, int j, int max) {
		return hashInteger(31*i+j, max);
	}
	
	public static void saveThesaurusAsMatrixMarket(Thesaurus th, File out) throws FileNotFoundException {
		Vocabulary voc = th.getVocab();		
		int dim = voc.getVocabSize();
		int n = th.numEntries();
		int count = 0;		
		StringBuffer sbAnt = new StringBuffer();
		StringBuffer sbSyn = new StringBuffer();
		for(int i = 0; i < n; i++) {
			int idx = th.getEntry(i);
			List<Integer> ants = th.getAntonyms(idx);
			for(int j : ants) {
				count += 2;
				sbAnt.append(Integer.toString(idx+1) + " " + Integer.toString(j+1) + " -1\n");
				sbAnt.append(Integer.toString(j+1) + " " + Integer.toString(idx+1) + " -1\n");
			}
			List<Integer> syns = th.getAntonyms(idx);
			for(int j : syns) {
				count += 2;
				sbSyn.append(Integer.toString(idx+1) + " " + Integer.toString(j+1) + " 1\n");
				sbSyn.append(Integer.toString(j+1) + " " + Integer.toString(idx+1) + " 1\n");
			}
		}
		PrintStream ps = new PrintStream(out);
		ps.println("%%MatrixMarket matrix coordinate real general");		
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		ps.println("% Generated " + dateFormat.format(new Date()));
		ps.println(Integer.toString(dim) + " " + Integer.toString(dim) + " " + Integer.toString(count));
		ps.print(sbAnt);
		ps.print(sbSyn);
		ps.close();
	}
	
	public static void main(String[] args) throws IOException{
		Thesaurus t = new ThesaurusImp(new File("data/WordNet-3.0/antonym.txt"), new File("data/WordNet-3.0/synonym.txt"), new File("data/WordNet-3.0/vocabulary.txt"));
		saveThesaurusAsMatrixMarket(t, new File("wordnet_mm"));
	}
}
