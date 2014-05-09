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

public class PrecisionRecall {
	public static final String OVERALL = "*OVERALL*";
	private static final int NUMC = 3;
	private static final int T = 0;
	private static final int C = 1;
	private static final int W = 2;
	
	private HashMap<String, double[]> counts = new HashMap<String, double[]>();
	//if thresholds are passed in, record P/R in the pre-defined buckets
	private HashMap<String, ArrayList<Pair<Double,Boolean>>> prCurve = new HashMap<String, ArrayList<Pair<Double,Boolean>>>();
	private double[] thresholds;
	private HashMap<String, Pair<double[],double[]>> catToCorrectWrong; 
	private HashMap<String, MutableDouble> totalGold = new HashMap<String,MutableDouble>();
	
	public PrecisionRecall() {}
	
	public PrecisionRecall(double[] thresholds) {
		this.thresholds = thresholds;
		catToCorrectWrong = new HashMap<String, Pair<double[],double[]>>();
	}
	
	public static double fScore(double precision, double recall) {
		return 2*precision*recall/(precision+recall);
	}
	
	//CONSIDER: record test time?
	
	private double[] getCounts(String str) {
		double[] c = counts.get(str);
		if (c == null) {
			c = new double[NUMC];
			counts.put(str,c);
		}
		return c;
	}
	private ArrayList<Pair<Double,Boolean>> getPR(String category) {
		ArrayList<Pair<Double,Boolean>> pr = prCurve.get(category);
		if (pr == null) {
			pr = new ArrayList<Pair<Double,Boolean>>();
			prCurve.put(category, pr);
		}
		return pr;
	}
	
	public void addAnswered(String category, boolean correct) {
		double[] c = getCounts(category);
		c[T] += 1;
		c[correct?C:W] += 1;
	}
	public void addAnswered(String category, boolean correct, double confidence) {
		addAnswered(category, correct);
		if (thresholds == null) {
			ArrayList<Pair<Double,Boolean>> pr = getPR(category);
			pr.add(Pair.of(confidence, correct));
		} else {
			Pair<double[],double[]> p = catToCorrectWrong.get(category);
			if (p == null) {
				p = Pair.of(new double[thresholds.length], new double[thresholds.length]);
				catToCorrectWrong.put(category, p);
			}
			for (int i = 0; i < thresholds.length; ++i) {
				if (confidence >= thresholds[i]) {
					if (correct) 
						p.first[i] += 1.0; 
					else 
						p.second[i] += 1.0;
				}
			}
		}
	}
	
	public double getMaxF(String category) {
		if (thresholds == null) {
			throw new UnsupportedOperationException("Can only use maxF with preset thresholds");
		}
		double maxF = 0;
		Pair<double[], double[]> p = catToCorrectWrong.get(category);
		if (p == null) {
			return 0;
		}
		double[] correct = p.first;
		double[] wrong = p.second;
		double total = getTotalGold(category);
		for (int i = 0; i < thresholds.length; ++i) {
			double precision = correct[i] / (correct[i]+wrong[i]);
			if (correct[i] == 0) precision = 0;
			double recall = correct[i] / total;
			double FScore = 2*precision*recall/(precision+recall);
			if (FScore > maxF) {
				maxF = FScore;
			}
		}
		return maxF;
	}
	
	/**
	 * All categories (including OVERALL), sorted alphabetically
	 * @return
	 */
	public Collection<String> getCategories() {
		if (catToCorrectWrong != null)
			return catToCorrectWrong.keySet();
		ArrayList<String> cats = new ArrayList<String>(counts.keySet());
		Collections.sort(cats);
		return cats;
	}
	
	private double getTotalGold(String category) {
		MutableDouble tg = totalGold.get(category);
		if (tg != null) return tg.value;
		return getCounts(category)[T];
	}
	
	public String stringPRCurve(String category) {
		Pair<double[], double[]> p = catToCorrectWrong.get(category);
		if (p == null) {
			return "No output for "+category;
		}
		return stringPRCurve(thresholds, p.first, p.second, getTotalGold(category));
	}
	
	public void printPRCurve(String category) {
		System.out.println(stringPRCurve(category));
	}
	
	public void addUnanswered(String category) {
		getCounts(category)[T] += 1;
		if (totalGold.containsKey(category)) {
			throw new IllegalArgumentException("You should not use both addUnanswered and addTotalGold for a single category");
		}
	}
	
	public void addTotalGold(String category, double addTot) {
		SparseVectors.increase(totalGold, category, addTot);
	}
	
	public String csvStringSummary() {
		ArrayList<String> cats = new ArrayList<String>(getCategories());
		Collections.sort(cats);
		StringBuffer buf = new StringBuffer();
		buf.append("Category\tPrecision\tRecall\tFScore\tTotal\n");
		for (String cat : cats) {
			String[] parts = summaryParts(cat);
			buf.append(Lang.stringList(parts, "\t")).append('\n');
		}
		return buf.toString();
	}
	
	public String csvPRCurve(String category, int numPoints) {
		String raw = stringPRCurve(getPR(category), getTotalGold(category), numPoints);
		raw = raw.replaceAll("[\\(\\)]", "");
		raw = raw.replaceAll("( )?[A-Z] = ", "\t");
		return raw;
	}
	
	public String stringSummary(String category) {
		double[] c = getCounts(category);
		return printScores(category, getTotalGold(category), c[C], c[W]);
	}
	
	public void printSummary(String category) {
		double[] c= getCounts(category);
		System.out.println(printScores(category, getTotalGold(category), c[C], c[W]));
	}
	public void printPRCurve(String category, int numPoints) {
		System.out.println(stringPRCurve(getPR(category), getTotalGold(category), numPoints));
	}
	
	public void printCalibration(String category, int numPoints) {
		System.out.println(stringCalibration(getPR(category), getTotalGold(category), numPoints));
	}
	
	public void printPRCurve(int numPoints) {
		printPRCurve(OVERALL, numPoints);
	}
	public void printCalibration(int numPoints) {
		printCalibration(OVERALL, numPoints);
	}
	public void printSummary() {
		printSummary(OVERALL);
	}
	public void addAnswered(boolean correct) {
		addAnswered(OVERALL, correct);
	}
	public void addAnswered(boolean correct, double score) {
		addAnswered(OVERALL, correct, score);
	}
	public void addUnanswered() {
		addUnanswered(OVERALL);
	}
	
	private String[] summaryParts(String category) {
		double[] c = getCounts(category);
		double total = getTotalGold(category);
		double correct = c[C];
		double wrong = c[W];
		if (total == 0) {
			return new String[] {"No cases of "+category};
		}
		if (correct + wrong == 0) {
			return new String[] {"No output for "+category};
		}
		double precision = correct / (correct + wrong);
		double recall = correct / total;
		double FScore = 2*precision*recall/(precision+recall);
		return new String[] {category,Lang.dblStr(precision),Lang.dblStr(recall),Lang.dblStr(FScore), ""+(int)total};
	}
	
	private static String printScores(String str, double total, double correct, double wrong) {
		
		if (total == 0) {
			return "No cases of "+str;
		}
		if (correct + wrong == 0) {
			return "No output for "+str;
		}
		double precision = correct / (correct + wrong);
		double recall = correct / total;
		double FScore = 2*precision*recall/(precision+recall);
		return str+" P = "+Lang.dblStr(precision)+" R = "+Lang.dblStr(recall)+" F = "+Lang.dblStr(FScore)+" C = "+(int)total;
	}
	
	private static String stringPRCurve(double[] thresholds, double[] correct, double[] wrong, double total) {
		StringBuffer buf = new StringBuffer();
		double auc = 0;
		for (int i = 0; i < thresholds.length; ++i) {
			double precision = correct[i] / (correct[i]+wrong[i]);
			if (correct[i] == 0) precision = 0;
			double recall = correct[i] / total;
			double FScore = 2*precision*recall/(precision+recall);
			double countThisSection = correct[i]+wrong[i];
			if (i > 0) {
				countThisSection -= (correct[i-1]+wrong[i-1]);
			}
			auc += precision*(countThisSection/total);
			buf.append("P = "+Lang.dblStr(precision)+" R = "+Lang.dblStr(recall)+" F = "+Lang.dblStr(FScore)+
					" (C = "+Lang.dblStr(correct[i]+wrong[i])+" T = "+Lang.dblStr(thresholds[i])+")"+"\n");
		}
		buf.append("AUC = "+auc); //not sure if computed correctly
		return buf.toString();
	}
	
	private static String stringCalibration(ArrayList<Pair<Double,Boolean>> pr, double total, int buckets) {
		StringBuffer buf = new StringBuffer();
		Collections.sort(pr, new FirstPairComparator(null));
		Collections.reverse(pr);
		double[] correct = new double[buckets];
		double[] answered = new double[buckets];
		double[] thresholds = new double[buckets];
		int bucketNdx = 0;
		for (int i = 0; i < pr.size(); ++i) {
			if (i >= (1+bucketNdx) * pr.size()/buckets && bucketNdx < buckets-1) {
				thresholds[bucketNdx] = pr.get(i).first;
				++bucketNdx;
			}
			++answered[bucketNdx];
			if (pr.get(i).second) {
				++correct[bucketNdx];
			}
		}	
		for (int i = 0; i < buckets; ++i) {
			double precision = correct[i] / answered[i];
			buf.append("P = "+Lang.dblStr(precision)+" Confidence = "+
					Lang.dblStr(thresholds[i])+" - "+(i>0?Lang.dblStr(thresholds[i-1]):"1.000")+"\n");
		}
		return buf.toString();
	}
	
	private static String stringPRCurve(ArrayList<Pair<Double,Boolean>> pr, double total, int buckets) {
		StringBuffer buf = new StringBuffer();
		Collections.sort(pr, new FirstPairComparator(null));
		Collections.reverse(pr);
		double[] correct = new double[buckets];
		double[] answered = new double[buckets];
		double[] thresholds = new double[buckets];
		int bucketNdx = 0;
		double cummulativeCorrect = 0;
		double auc = 0;
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
			if (pr.get(i).second) 
				++cummulativeCorrect;
			auc += cummulativeCorrect/((i+1) * pr.size());
		}
		
		for (int i = 0; i < buckets; ++i) {
			double precision = correct[i] / answered[i];
			double recall = correct[i] / total;
			double FScore = 2*precision*recall/(precision+recall);
			buf.append("P = "+Lang.dblStr(precision)+" R = "+Lang.dblStr(recall)+" F = "+Lang.dblStr(FScore)+
					" (C = "+(int)answered[i]+" T = "+Lang.dblStr(thresholds[i])+")"+"\n");
		}
		buf.append("AUC = "+auc); //not sure if computed correctly
		return buf.toString();
	}
}
