package edu.antonym.test;

public class CosineSimilarity {
	
	public double cosineSimilarity(float[] vector1, float[] vector2) {
		double dotProduct = 0.0;
		double v1 = 0.0;
		double v2 = 0.0;
		double cosineSimilarity = 0.0;

        for (int i = 0; i < vector1.length; i++) //vector1 and vector2 must be of same length
        {
            dotProduct += vector1[i] * vector2[i];  
            v1 += Math.pow(vector1[i], 2);  
            v2 += Math.pow(vector2[i], 2); 
        }

        v1 = Math.sqrt(v1);	
        v2 = Math.sqrt(v2);	

        if (v1 != 0.0 && v2 != 0.0) {
            cosineSimilarity = dotProduct / (v1 * v2);
        } else {
            return 0;
        }
        return cosineSimilarity;
    }
}
