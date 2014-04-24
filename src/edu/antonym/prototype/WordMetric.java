package edu.antonym.prototype;

public interface WordMetric {
	public Vocabulary getVocab();

	public double distance(int word1, int word2);
}
