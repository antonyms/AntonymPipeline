package edu.antonym.develtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import cc.mallet.optimize.tests.TestOptimizable;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.TextFileEmbedding;
import edu.antonym.nn.LinearEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class CheckLinearEmbeddingGradient {
	public static void main(String[] args) throws FileNotFoundException {
		Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th=new SimpleThesaurus(vocab, new File("data/simplesyn.txt"), new File("data/simpleant.txt"));
		VectorEmbedding origEmbed = new TextFileEmbedding(new File(
				"data/huangvectors.txt"), vocab);
		LinearEmbedding emb = new LinearEmbedding(origEmbed,
				origEmbed.getDimension());
		emb.setThesaurus(th);
		
		Random r = new Random();

		int niter = 100;
		for (int i = 0; i < niter; i++) {
			TestOptimizable.testValueAndGradientRandomParameters(emb, r);
		
			/*double[] params = new double [emb.getNumParameters()];
			for (int j = 0; j < params.length; j++) {
				params[j] = r.nextDouble ();
				if (r.nextBoolean ()) 
					params [j] = -params[j];
			}
			emb.setParameters (params);
			System.out.println("Error is "+emb.getValue());
			*/
		}

	}
}
