package edu.antonym.nn;

import java.util.Arrays;
import java.util.List;

import cc.mallet.optimize.Optimizable;
import cc.mallet.types.MatrixOps;
import edu.antonym.Util;
import edu.antonym.prototype.NormalizedVectorEmbedding;
import edu.antonym.prototype.Thesaurus;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class LinearEmbedding implements VectorEmbedding,
		Optimizable.ByGradientValue {
	Thesaurus th;
	VectorEmbedding orig;
	int newdim;
	// Column major matrix
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

	public LinearEmbedding(NormalizedVectorEmbedding orig, int ndim) {
		this.orig = orig;
		this.newdim = ndim;
		parameters = new double[orig.getDimension() * ndim];
		this.vocabSize = orig.getVocab().getVocabSize();

		// Initialize the embedding to the "identity" matrix
		int mindim = ndim < orig.getDimension() ? ndim : orig.getDimension();
		for (int i = 0; i < mindim; i++) {
			parameters[i * ndim + i] = 1.0d;
		}

		cacheDirty = true;
		cachedGradient = new double[orig.getDimension() * ndim];
	}

	public void setThesaurus(Thesaurus th) {
		this.th = th;
		this.activeThesaurusStart = 0;
		this.activeThesaurusEnd = th.numEntries();
	}
	
	public void setActiveEntries(int start, int end) {
		this.activeThesaurusStart=start;
		this.activeThesaurusEnd=end;
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

	public void transformVec(double[] in, double[] out) {
		Arrays.fill(out, 0.0d);
		for (int i = 0; i < in.length; i++) {
			for (int j = 0; j < out.length; j++) {
				out[j] += in[i] * parameters[i * out.length + j];
			}
		}
	}

	
	double cosineSimGradAndValue(int w1ind, int w2ind, double[] oldvec1, double[] newvec1, double[] newvec2,double[] gradOut) {
		double[] oldvec2 = orig.getVectorRep(w2ind);
		
		transformVec(oldvec2, newvec2);
		
		double vnorm=MatrixOps.twoNormSquared(newvec1);
		double wnorm=MatrixOps.twoNormSquared(newvec2);
		double dot=MatrixOps.dotProduct(newvec1, newvec2);
		
		//Value of cosine similarity
		double value=dot/Math.sqrt(vnorm*wnorm);
		
		//Now we calculate the gradient
		//See doc/linearembeddingtheory for the math behind this
		
		double denom=Math.pow(vnorm*wnorm,3/2);
		for(int i=0; i<newdim; i++) {
			for(int j=0; j<orig.getDimension(); j++) {
				gradOut[j*newdim+i]=  (oldvec1[j]*newvec2[i]       + newvec1[i]*oldvec2[j]      ) * vnorm*wnorm 
									- (oldvec1[j]*newvec1[i]*wnorm + newvec2[i]*oldvec2[j]*vnorm) * dot;
				gradOut[j*newdim+i]/=denom;
			}
		}
		return value;
	}

	void computeGradientAndValue() {
		cacheDirty = false;
		cachedValue = 0;
		Arrays.fill(cachedGradient, 0.0d);

		double[] newvec1 = new double[newdim];
		double[] newvec2 = new double[newdim];

		double[] gradTmp1 = new double[parameters.length];
		double[] gradTmp2 = new double[parameters.length];

		for (int i = activeThesaurusStart; i < activeThesaurusEnd; i++) {
			int wind = th.getEntry(i);

			double[] oldvec1 = orig.getVectorRep(wind);
			transformVec(oldvec1, newvec1);

			List<Integer> syn = th.getSynonyms(wind);
			List<Integer> ant = th.getAntonyms(wind);

			for (int s : syn) {
				int randSample = Util.hashIntegerPair(wind, s, vocabSize);

				double synsim = cosineSimGradAndValue(wind, s, oldvec1,
						newvec1, newvec2, gradTmp1);
				double rsim = cosineSimGradAndValue(wind, randSample, oldvec1,
						newvec1, newvec2, gradTmp2);

				if (rsim > synsim) {
					cachedValue += synsim - rsim;
					for (int p = 0; p < parameters.length; p++) {
						cachedGradient[p] += gradTmp1[p] - gradTmp2[p];
					}
				}
			}
			for (int a : ant) {
				int randSample = Util.hashIntegerPair(wind, a, vocabSize);

				double antsim = cosineSimGradAndValue(wind, a, oldvec1,
						newvec1, newvec2, gradTmp1);
				double rsim = cosineSimGradAndValue(wind, randSample, oldvec1,
						newvec1, newvec2, gradTmp2);

				if (rsim < antsim) {
					cachedValue -= antsim - rsim;
					for (int p = 0; p < parameters.length; p++) {
						cachedGradient[p] -= gradTmp1[p] - gradTmp2[p];
					}
				}
			}
		}

		cacheDirty=false;
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
	public int getDimension() {
		return newdim;
	}

	@Override
	public double[] getVectorRep(int word) {
		double[] newec = new double[newdim];
		transformVec(orig.getVectorRep(word), newec);
		return newec;
	}

	@Override
	public int findClosestWord(double[] vector) {
		// TODO Implement later
		return 0;
	}

	@Override
	public Vocabulary getVocab() {
		return orig.getVocab();
	}

}
