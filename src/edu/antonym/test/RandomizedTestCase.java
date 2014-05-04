package edu.antonym.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import edu.antonym.Util;
import edu.antonym.prototype.MetricEvaluator;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.Thesaurus.Entry;
import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class RandomizedTestCase implements MetricEvaluator {

	public Thesaurus th;

	public RandomizedTestCase(Thesaurus th) {
		this.th = th;
	}

	@Override
	public double score(WordMetric metric) throws IOException {

		String result_folder = "data/result-data/";
		new File(result_folder).mkdirs();

		double acc = 0.0d;

		Vocabulary vocab = metric.getVocab();

		int ant_num = 0;
		int syn_num = 0;
		double mse_ant = 0.0;
		double mse_syn = 0.0;
		FileWriter out = new FileWriter(result_folder + "ant.txt");

		for (int i = 0; i < th.numEntries(); i++) {
			Entry ent = th.getEntry(i);
			out.append(vocab.lookupIndex(ent.word1()) + " "
					+ vocab.lookupIndex(ent.word2()) + " ");
			double cs = metric.similarity(ent.word1(), ent.word2());
			if (ent.isAntonym()) {
				ant_num++;
				mse_ant+=(-1-cs)*(-1-cs);
				out.append(Double.toString(cs));
			} else {
				syn_num++;
				mse_syn+=(1-cs)*(1-cs);
				out.append(Double.toString(cs));
			}
			out.append("\n");
		}
		mse_ant = mse_ant / ant_num;
		mse_syn = mse_syn / syn_num;
		acc -= mse_ant;
		acc -= mse_syn;
		out.append("\n");
		out.append("Mean squared error for antonym is: " + mse_ant + "\n");
		System.out
				.println("[SIMPLE ANT TEST] Mean squared error for antonym is: "
						+ Double.toString(mse_ant));
		if (syn_num != 0) {
			out.append("Mean squared error for synonym is: " + mse_syn + "\n");
			System.out
					.println("[SIMPLE ANT TEST] Mean squared error for synonym is: "
							+ Double.toString(mse_syn));
		}
		out.close();

		return acc;
	}

}
