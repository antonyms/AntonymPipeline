package edu.antonym.prototype;

public interface VectorEmbedding {

	int getDimension();
	public float[] getVectorRep(int word);
	public int findClosestWord(float[] vector);
}
