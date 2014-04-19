package edu.antonym.develtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import cc.mallet.optimize.tests.TestOptimizable;
import edu.antonym.NormalizedTextFileEmbedding;
import edu.antonym.SimpleThesaurus;
import edu.antonym.SimpleVocab;
import edu.antonym.nn.LinearVectorMetric;
import edu.antonym.nn.training.RegularizedOptimizable;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Vocabulary;

public class CheckLinearVectorMetric {
	public static void main(String[] args) throws FileNotFoundException {
		Vocabulary vocab = new SimpleVocab(new File("data/huangvocab.txt"));
		Thesaurus th = new SimpleThesaurus(vocab,
				new File("data/simplesyn.txt"), new File("data/simpleant.txt"));
		NormalizedVectorEmbedding origEmbed = new NormalizedTextFileEmbedding(
				new File("data/huangvectors.txt"), vocab);
		LinearVectorMetric emb = new LinearVectorMetric(origEmbed);
		emb.setThesaurus(th);

		RegularizedOptimizable reg = new RegularizedOptimizable(emb, 1, 1);
		// double[] params=new double[emb.getNumParameters()];
		// params[0]=1;
		// emb.setParameters(params);
		// TestOptimizable.testValueAndGradientCurrentParameters(emb);
		Random r = new Random();

		int niter = 100;
		for (int i = 0; i < niter; i++) {
			TestOptimizable.testValueAndGradientRandomParameters(reg, r);

			/*
			 * double[] params = new double [emb.getNumParameters()]; for (int j
			 * = 0; j < params.length; j++) { params[j] = r.nextDouble (); if
			 * (r.nextBoolean ()) params [j] = -params[j]; } emb.setParameters
			 * (params); System.out.println("Error is "+emb.getValue());
			 */
		}

	}
}
