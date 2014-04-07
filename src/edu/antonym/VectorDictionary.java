package edu.antonym;

public interface VectorDictionary {

	public float[] getVectorRep(int word);
	public int findClosestWord(float[] vector);
}
