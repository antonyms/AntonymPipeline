package edu.antonym.nn;

import java.util.Arrays;
import java.util.List;

import cc.mallet.optimize.Optimizable;
import cc.mallet.types.MatrixOps;
import edu.antonym.Util;
import edu.antonym.prototype.WordMetric;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class LinearVectorMetric implements Optimizable.ByGradientValue,WordMetric {
	Thesaurus th;
	VectorEmbedding orig;
	// lower diagonal half of symmetric matrix
	// 0
	// 1 2
	// 3 4 5
	// 6 7 8 9
	double[] parameters;

	int vocabSize;

	double regularizationStrength = 1;

	// Beginning and end index
	int activeThesaurusStart;
	int activeThesaurusEnd;

	// cached optimization values;
	boolean cacheDirty;
	double cachedValue;
	double[] cachedGradient;

	public LinearVectorMetric(NormalizedVectorEmbedding orig) {
		this.orig = orig;
		parameters = new double[(orig.getDimension() * (orig.getDimension() + 1)) / 2];
		this.vocabSize = orig.getVocab().getVocabSize();

		// Initialize the embedding to the identity matrix
		int ind = 0;
		for (int i = 0; i < orig.getDimension(); i++) {
			parameters[ind] = 1.0d;
			ind += (i + 2);
		}

		cacheDirty = true;
		cachedGradient = new double[parameters.length];
	}

	public void setThesaurus(Thesaurus th) {
		this.th = th;
		this.activeThesaurusStart = 0;
		this.activeThesaurusEnd = th.numEntries();
	}

	public void setActiveEntries(int start, int end) {
		this.activeThesaurusStart = start;
		this.activeThesaurusEnd = end;
	}

	@Override
	public int getNumParameters() {
		return parameters.length;
	}

	@Override
	public double getParameter(int arg0) {
		return parameters[arg0];
	}

	@Override
	public void getParameters(double[] arg0) {
		System.arraycopy(parameters, 0, arg0, 0, parameters.length);
	}

	@Override
	public void setParameter(int arg0, double arg1) {
		parameters[arg0] = arg1;
		cacheDirty = true;
	}

	@Override
	public void setParameters(double[] arg0) {
		System.arraycopy(arg0, 0, parameters, 0, parameters.length);
		cacheDirty = true;
	}

	public double innerProd(double[] vec1, double[] vec2) {

		double tot = 0;

		int ind = 0;
		for (int i = 0; i < orig.getDimension(); i++) {
			for (int j = 0; j < i; j++) {
				// Calculate the contribution from this entry and the reflected
				// entry in the upper half part
				tot += vec1[i] * parameters[ind]* vec2[j];
				tot += vec1[j] * parameters[ind] * vec2[i];
				ind++;
			}
			tot += vec1[i] * parameters[ind] * vec2[i];
			ind++;
		}
		return tot;
	}

	double cosineSimGradAndValue(int w1ind, int w2ind, double[] vec1,
			double[] gradOut) {
		double[] vec2 = orig.getVectorRep(w2ind);

		double n = innerProd(vec1, vec1);
		double m = innerProd(vec2, vec2);
		double l = innerProd(vec1, vec2);

		// Value of cosine similarity
		double value = l ;// / Math.sqrt(n * m);


		//System.out.println("l="+l+" n="+n+" m="+m+" cosinesim="+value);
		
		// Now we calculate the gradient
		// See doc/linearembeddingtheory for the math behind this

		double denom = Math.pow(n * m, 3 / 2);

		int ind = 0;
		for (int i = 0; i < orig.getDimension(); i++) {
			for (int j = 0; j < i; j++) {
				// Off diagonal elements
				gradOut[ind] = n * m * (vec1[i] * vec2[j] + vec1[j] * vec2[i])
						     - l * m *  vec1[i] * vec1[j] 
						     - l * n *  vec2[i] * vec2[j];
				gradOut[ind] /= denom;
				
				gradOut[ind]=vec1[i]*vec2[j]+vec1[j]*vec2[i];
				ind++;
			}
			// on diagonal elements
			gradOut[ind] = 		n * m * vec1[i] * vec2[i] 
						- 0.5 * l * m * vec1[i] * vec1[i] 
						- 0.5 * l * n * vec2[i] * vec2[i];
			gradOut[ind] /= denom;
			
			gradOut[ind]=vec1[i]*vec2[i];
			
			ind++;
		}
		return value;
	}

	void computeGradientAndValue() {
		cacheDirty = false;
		cachedValue = 0;
		Arrays.fill(cachedGradient, 0.0d);

		double[] gradTmp1 = new double[parameters.length];
		double[] gradTmp2 = new double[parameters.length];

		for (int i = activeThesaurusStart; i < activeThesaurusEnd; i++) {
			int wind = th.getEntry(i);

			double[] vec1 = orig.getVectorRep(wind);

			List<Integer> syn = th.getSynonyms(wind);
			List<Integer> ant = th.getAntonyms(wind);

			for (int s : syn) {
				int randSample = Util.hashIntegerPair(wind, s, vocabSize);

				double synsim = cosineSimGradAndValue(wind, s, vec1, gradTmp1);
				double rsim = cosineSimGradAndValue(wind, randSample, vec1,
						gradTmp2);
				// System.out.println("synsim " + synsim + " rsim " + rsim);
				 if (rsim > synsim) {
				cachedValue += synsim - rsim;
				for (int p = 0; p < parameters.length; p++) {
					cachedGradient[p] += gradTmp1[p] - gradTmp2[p];
				}
				 }
			}
			for (int a : ant) {
				int randSample = Util.hashIntegerPair(wind, a, vocabSize);

				double antsim = cosineSimGradAndValue(wind, a, vec1, gradTmp1);
				double rsim = cosineSimGradAndValue(wind, randSample, vec1,
						gradTmp2);

				 if (rsim < antsim) {
				cachedValue -= antsim - rsim;
				for (int p = 0; p < parameters.length; p++) {
					cachedGradient[p] -= gradTmp1[p] - gradTmp2[p];
				}
				 }
			}
		}

		cacheDirty = false;
	}

	@Override
	public void getValueGradient(double[] arg0) {
		if (cacheDirty) {
			computeGradientAndValue();
		}
		System.arraycopy(cachedGradient, 0, arg0, 0, cachedGradient.length);
	}

	@Override
	public double getValue() {
		if (cacheDirty) {
			computeGradientAndValue();
		}
		return cachedValue;

	}

	@Override
	public Vocabulary getVocab() {
		return orig.getVocab();
	}

	@Override
	public double similarity(int word1, int word2) {
		double[] d1 = orig.getVectorRep(word1);
		double[] d2 = orig.getVectorRep(word2);
		return innerProd(d1, d2);
	}
}
