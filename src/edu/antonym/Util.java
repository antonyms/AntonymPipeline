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
}
