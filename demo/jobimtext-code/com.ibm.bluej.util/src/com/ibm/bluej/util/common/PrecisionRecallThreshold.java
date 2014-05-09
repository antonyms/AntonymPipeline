/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.util.common;

import java.util.*;

import org.apache.commons.math.stat.correlation.*;

public class PrecisionRecallThreshold {
  // Force the random shuffling to use a fixed seed so we get the same results every time
  // we run this.
  private static final Random RANDOMNESS = new Random(10598l);
  
	public static class SummaryScores {
		public double logLikelihood;
		public double avgLogLikelihood;
		public double maxFScore;
		public double maxFScoreThreshold;
		public double auc;
		public double pearsonsR;
		public double maxAccuracy;
		public double maxAccuracyThreshold;
		//CONSIDER: accuracy at 0.5 - useful in a one-vs-all reduction?
		
		public double relativePrecision;
		public double relativeRecall;
		public double relativeFScore;
		public double relativeThreshold;
		
		public double positiveCount;
		
		public String toString() {
			return "Max F-Score = "+maxFScore+" at "+maxFScoreThreshold+
				   "\nMax Accuracy = "+maxAccuracy+" at "+maxAccuracyThreshold+
				   "\nAuC = "+auc+"\nPearson's R = "+pearsonsR+"\nAvg. Log-Likelihood = "+avgLogLikelihood+
					"\nRelative P/R/F = "+relativePrecision+" "+relativeRecall+" "+relativeFScore+" at "+relativeThreshold+
					"\nNumber of positive = "+positiveCount;
		}
	}

	public String tsvPRCurve() {
		Collections.shuffle(scoredPlusGold, RANDOMNESS);
		Collections.sort(scoredPlusGold, new FirstPairComparator(null));
		Collections.reverse(scoredPlusGold);
		double allPositive = 0;
		for (Pair<Double, Boolean> p : scoredPlusGold) {
			if (p.second)
				allPositive += 1;
		}
		
		StringBuffer buf = new StringBuffer("Recall\tPrecision\n");
		
		double cummulativeCorrect = 0;
		for (int i = 0; i < scoredPlusGold.size(); ++i) {
			Pair<Double, Boolean> p = scoredPlusGold.get(i);
			if (p.second) {
				++cummulativeCorrect;
				double precision = cummulativeCorrect / (i + 1);
				double recall = cummulativeCorrect / allPositive;
				buf.append(recall+"\t"+precision+"\n");
			}
		}
		return buf.toString();
	}
	
	public SummaryScores computeSummaryScores() {
		return computeSummaryScores(0.99,1.0);
	}
	
	public SummaryScores computeSummaryScores(double limitProb) {
		return computeSummaryScores(limitProb, 1.0);
	}
	
	public SummaryScores computeSummaryScores(double limitProb, double betaForFScore) {
		if (limitProb < 0.5) {
			throw new IllegalArgumentException("Probabilities cannot be limited to below 50%");
		}
		double betaForFScoreSqr = betaForFScore*betaForFScore;
		SummaryScores sum = new SummaryScores();
		Collections.shuffle(scoredPlusGold, RANDOMNESS);
		Collections.sort(scoredPlusGold, new FirstPairComparator(null));
		Collections.reverse(scoredPlusGold);

		double tpRelative = 0;
		double fpRelative = 0;

		double cummulativeCorrect = 0;
		double auc = 0;
		double allPositive = 0;
		double averageScore = 0;
		for (Pair<Double, Boolean> p : scoredPlusGold) {
			if (p.second)
				allPositive += 1;
			averageScore += p.first;
		}
		averageScore /= scoredPlusGold.size();
		sum.relativeThreshold = averageScore;

		double maxF = 0;
		double maxFThresh = 0;
		double logLike = 0;
		double maxAcc = 0;
		double maxAccThresh = 0;
		sum.relativePrecision = Double.NaN;
		for (int i = 0; i < scoredPlusGold.size(); ++i) {
			Pair<Double, Boolean> p = scoredPlusGold.get(i);
			if (p.second) {
				++cummulativeCorrect;
				auc += cummulativeCorrect / ((i + 1) * allPositive);
			}

			double tp = cummulativeCorrect;
			double fp = (i + 1) - cummulativeCorrect;
			double fn = allPositive - cummulativeCorrect;
			double tn = (scoredPlusGold.size() - (i + 1)) - fn;
			double precision = (double) (tp) / (tp + fp);
			double recall = (double) (tp) / (tp + fn);
			double accuracy = (tp + tn) / scoredPlusGold.size();
			double f1 = (1+betaForFScoreSqr) * precision * recall / (betaForFScoreSqr*precision + recall);

			if (p.second) {
				if (p.first > sum.relativeThreshold)
					tpRelative++;
			} else {
				if (p.first > sum.relativeThreshold)
					fpRelative++;
			}

			if (f1 > maxF) {
				maxF = f1;
				maxFThresh = p.first;
			}
			if (accuracy > maxAcc) {
				maxAcc = accuracy;
				maxAccThresh = p.first;
			}

			double prob = p.second ? p.first : 1 - p.first;
			if (prob < 0 || prob > 1) {
				logLike = Double.NaN;
			}
			if (prob > limitProb) {
				prob = limitProb;
			}
			if (prob < 1 - limitProb) {
				prob = 1 - limitProb;
			}
			logLike += Math.log(prob);
		}

		sum.positiveCount = allPositive;
		
		sum.maxFScore = maxF;
		sum.maxFScoreThreshold = maxFThresh;
		sum.auc = auc;
		sum.pearsonsR = pearsonsR();
		sum.logLikelihood = logLike;
		sum.avgLogLikelihood = logLike / scoredPlusGold.size();
		sum.maxAccuracy = maxAcc;
		sum.maxAccuracyThreshold = maxAccThresh;

		sum.relativePrecision = tpRelative / (tpRelative + (fpRelative * (allPositive / (scoredPlusGold.size() - allPositive))));
		if (Double.isNaN(sum.relativePrecision))
			sum.relativePrecision = 0;
		sum.relativeRecall = tpRelative / allPositive;
		sum.relativeFScore = 2 * sum.relativePrecision * sum.relativeRecall / (sum.relativePrecision + sum.relativeRecall);
		if (Double.isNaN(sum.relativeFScore))
			sum.relativeFScore = 0;

		return sum;
	}
	
	public int size() {
		return scoredPlusGold.size();
	}
	
	private ArrayList<Pair<Double,Boolean>> scoredPlusGold = new ArrayList<Pair<Double,Boolean>>();
	
	public Map<String,Pair<Boolean, Double>> getScores() {
		Map<String,Pair<Boolean, Double>> os = new HashMap<String,Pair<Boolean, Double>>();
		int ndx = 0;
		for (Pair<Double, Boolean> p : scoredPlusGold) {
			os.put("ID"+ndx++, Pair.of(p.second,p.first));
		}
		return os;
	}
	
	public void addAnswered(boolean isRelevant, double score) {
		scoredPlusGold.add(Pair.of(score, isRelevant));
	}
	public void printPRCurve(int numPoints) {
		double total = 0;
		for (Pair<Double,Boolean> sg : scoredPlusGold) {
			if (sg.second)
				total += 1.0;
		}
		System.out.println(stringPRCurve(scoredPlusGold, total, numPoints));
	}
	
	public double pearsonsR() {
		if (scoredPlusGold.size() < 2)
			return 0.0;
		double[] x = new double[scoredPlusGold.size()];
		double[] y = new double[scoredPlusGold.size()];
		int ndx = 0;
		for (Pair<Double,Boolean> sg : scoredPlusGold) {
			x[ndx] = sg.first;
			y[ndx] = sg.second ? 1.0 : -1.0;
			++ndx;
		}
		double corrValue = new PearsonsCorrelation().correlation(x, y);
		if (Double.isNaN(corrValue))
			corrValue = 0.0;
		
		return corrValue;
	}
	
	private static String stringPRCurve(ArrayList<Pair<Double,Boolean>> pr, double total, int buckets) {
		StringBuffer buf = new StringBuffer();
		Collections.shuffle(pr);
		Collections.sort(pr, new FirstPairComparator(null));
		Collections.reverse(pr);
		double[] correct = new double[buckets];
		double[] answered = new double[buckets];
		double[] thresholds = new double[buckets];
		int bucketNdx = 0;
		double cummulativeCorrect = 0;
		double auc = 0;
		double allPositive = 0;
		for (Pair<Double, Boolean> p : pr) {
			if (p.second)
				allPositive += 1;
		}
		
		double maxF = 0;
		for (int i = 0; i < pr.size(); ++i) {
			if (i >= (1+bucketNdx) * pr.size()/buckets && bucketNdx < buckets-1) {
				thresholds[bucketNdx] = pr.get(i).first;
				++bucketNdx;
			}
			for (int j = bucketNdx; j < buckets; ++j) {
				++answered[j];
				if (pr.get(i).second) {
					++correct[j];
				}
			}
			if (pr.get(i).second) {
				++cummulativeCorrect;
				auc += cummulativeCorrect/((i+1) * allPositive);
			}
						
			double tp = cummulativeCorrect;
			double fp = (i+1) - cummulativeCorrect;
			double fn = allPositive - cummulativeCorrect;
			double precision = (double)(tp)/(tp+fp);
			double recall = (double)(tp)/(tp+fn);
			double f1 = 2*precision*recall/(precision+recall);
			if (f1 > maxF)
				maxF = f1;
		}
		
		for (int i = 0; i < buckets; ++i) {
			double precision = correct[i] / answered[i];
			double recall = correct[i] / total;
			double FScore = 2*precision*recall/(precision+recall);
			buf.append("P = "+Lang.dblStr(precision)+" R = "+Lang.dblStr(recall)+" F = "+Lang.dblStr(FScore)+
					" (C = "+(int)answered[i]+" T = "+Lang.dblStr(thresholds[i])+")"+"\n");
		}
		buf.append("AUC = "+auc+"\n"); //not sure if computed correctly
		buf.append("Max F-Score = "+maxF+"\n");
		return buf.toString();
	}
}
