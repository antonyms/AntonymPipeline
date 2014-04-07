package edu.antonym;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class Util {
	static void saveVectors(VectorEmbedding embed, Vocabulary vocab, File out) throws FileNotFoundException {
		int dim=embed.getDimension();
		
		PrintStream ps=new PrintStream(out);
		ps.println(dim);
		for(int i=0; i<vocab.size(); i++) {
			float[] vect=embed.getVectorRep(i);
			for(int j=0; j<dim; j++) {
				ps.print(vect[j]);
				ps.print(' ');
			}
			ps.println();
		}
		ps.close();
	}
}
