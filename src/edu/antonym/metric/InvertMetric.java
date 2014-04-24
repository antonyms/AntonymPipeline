package edu.antonym.metric;

import edu.antonym.prototype.Vocabulary;
import edu.antonym.prototype.WordMetric;

public class InvertMetric implements WordMetric {
	WordMetric orig;

	public InvertMetric(WordMetric orig) {
		this.orig = orig;
	}

	@Override
	public Vocabulary getVocab() {
		return orig.getVocab();
	}

	@Override
	public double similarity(int word1, int word2) {
		return -orig.similarity(word1, word2);
	}

}
