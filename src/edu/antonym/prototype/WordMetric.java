package edu.antonym.prototype;

public interface WordMetric {
	public Vocabulary getVocab();

	public double similarity(int word1, int word2);
}
