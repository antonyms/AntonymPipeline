package edu.antonym.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.antonym.Util;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

/**
 * This is the test case for synonyms, syn0.txt and syn1.txt. Each line
 * represents one single test. Each two words of a line are compared. Mean
 * squared error of synonym is also measured at the last. syn: 1
 * 
 */
public class TestCase2 implements MetricEvaluator {

	@Override
	public double score(WordMetric metric) throws IOException {

		String test_folder = "data/test-data/";
		String result_folder = "data/result-data/";
		new File(result_folder).mkdirs();

		String syn0 = "syn0.txt";
		String syn1 = "syn1.txt";

		ArrayList<String> syns = new ArrayList<String>();
		double acc = 0.0d;
		syns.add(syn0);
		syns.add(syn1); // add more file if needed

		Vocabulary vocab = metric.getVocab();
		int OOV = vocab.OOVindex();

		for (int i = 0; i < syns.size(); i++) {
			int syn_num = 0;
			double mse_syn = 0.0;

			FileWriter out = new FileWriter(result_folder + syns.get(i));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(test_folder + syns.get(i))));
			String line = br.readLine();

			while (line != null) {
				String[] words = line.split("\\s+");
				ArrayList<Integer> vectors = new ArrayList<Integer>();
				for (int j = 0; j < words.length; j++) {
					int wordID = vocab.lookupWord(words[j]);
					if (wordID != OOV) {
						vectors.add(wordID);
						out.append(words[j] + " ");
					} else {
						out.append("No such word: " + words[j] + " ");
					}
				}

				for (int k = 0; k < vectors.size(); k++) {
					double cs = metric.similarity(
							vectors.get(k % vectors.size()),
							vectors.get((k + 1) % vectors.size()));
					syn_num++;
					mse_syn += Math.pow(1 - cs, 2);
					out.append(Double.toString(cs) + " ");
				}

				out.append("\n");
				line = br.readLine();
			}
			mse_syn = mse_syn / syn_num;
			acc -= mse_syn;
			out.append("\n");
			out.append("Mean squared error for synonym is: " + mse_syn + "\n");
			System.out.println("[SIMPLE SNY TEST" + i
					+ "] Mean squared error for synonym is: "
					+ Double.toString(mse_syn));
			out.close();
			br.close();

		}
		return acc;
	}
}
