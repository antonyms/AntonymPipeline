package edu.antonym.prototype;

public interface VectorEmbedding extends WordMetric {

	int getDimension();
	public double[] getVectorRep(int word);
	public int findClosestWord(double[] vector);
	Vocabulary getVocab();
}
