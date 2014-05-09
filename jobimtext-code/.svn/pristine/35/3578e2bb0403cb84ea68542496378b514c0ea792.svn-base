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


public class DenseVectors {
	public static double klDivergence(double[] v1, double[] v2) {
		return klDivergence(v1,v2,0);
	}
	public static double klDivergence(double[] v1, double[] v2, double smooth) {
		if (v1.length != v2.length)
			throw new IllegalArgumentException();
		double smoothNorm1 = oneNorm(v1) + v1.length * smooth;
		double smoothNorm2 = oneNorm(v2) + v2.length * smooth;
		double kl = 0;
		for (int i = 0; i < v1.length; ++i) {		
			double pi = ((v1[i]+smooth)/smoothNorm1);
			double qi = ((v2[i]+smooth)/smoothNorm2);
			if (pi != 0)
				kl += Math.log(pi/qi) * pi;
		}
		return kl;
	}
	
	public static double[] fromSparse(Map<? extends Comparable,Double> sparse) {
		ArrayList<Comparable> keySet = new ArrayList<Comparable>(sparse.keySet());
		Collections.sort(keySet);
		double[] v = new double[keySet.size()];
		for (int i = 0; i < v.length; ++i) {
			v[i] = sparse.get(keySet.get(i));
		}
		return v;
	}
	
	public static double oneNorm(double[] v) {
		double n = 0;
		for (double vi : v) {
			n += Math.abs(vi);
		}
		return n;
	}
	
	public static double twoNorm(double[] v) {
		double n = 0;
		for (double vi : v) {
			n += vi * vi;
		}
		return Math.sqrt(n);
	}
	
	public static boolean hasNaN(double[] v) {
		for (double vi : v)
			if (Double.isNaN(vi) || Double.isInfinite(vi))
				return true;
		return false;
	}
	
	public static double sum(double[] v) {
		double sum = 0;
		for (double vi : v) {
			sum += vi;
		}
		return sum;
	}
	
	public static double mean(double[] v) {
		return sum(v) / v.length;
	}
	
	public static int maxIndex(double[] v) {
		int maxNdx = 0;
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < v.length; ++i) {
			if (v[i] > max) {
				max = v[i];
				maxNdx = i;
			}
		}
		return maxNdx;
	}
	public static int minIndex(double[] v) {
		int minNdx = 0;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < v.length; ++i) {
			if (v[i] < min) {
				min = v[i];
				minNdx = i;
			}
		}
		return minNdx;
	}
	public static double stdDev(double[] v, double m) {
		double sum = 0;
		for (double vi : v) {
			sum += (vi-m) * (vi-m);
		}
		return Math.sqrt(sum/v.length);
	}
	
	public static HashMap<Integer,MutableDouble> toSparse(double[] v) {
		HashMap<Integer,MutableDouble> s = new HashMap<Integer,MutableDouble>();
		for (int vi = 0; vi < v.length; ++vi) {
			s.put(vi, new MutableDouble(v[vi]));
		}
		return s;
	}
	
	public static double[] extend(double[] v1, double... add) {
		double[] ve = new double[v1.length+add.length];
		System.arraycopy(v1, 0, ve, 0, v1.length);
		System.arraycopy(add, 0, ve, v1.length, add.length);
		return ve;
	}
	
	//rank correlation
	public static double kendallTau(double[] x, double[] y) {
		if (x.length != y.length || x.length < 2)
			throw new IllegalArgumentException("Invalid lengths "+x.length+" "+y.length);
		double sum = 0;
		for (int i = 0; i < x.length; ++i) {
			for (int j = 0; j < x.length; ++j) {
				sum += Math.signum(x[i] - x[j]) * Math.signum(y[i] - y[j]);
			}
		}
		return sum / (x.length * (x.length-1));
	}
	
	public static double pearsonsR(double[] x, double[] y) {
		PearsonsCorrelation pc = new PearsonsCorrelation();
		double corrValue = pc.correlation(x, y);

		if (new Double(corrValue).isNaN())
			corrValue = 0.0;
		
		return corrValue;
	}
	/*
	public static double pearsonsR(double[] v1, double[] v2) {
		double m1 = mean(v1);
		double m2 = mean(v2);
		double s1 = stdDev(v1,m1)+0.0000001;
		double s2 = stdDev(v2,m2)+0.0000001;
		double sum = 0;
		for (int i = 0; i < v1.length; ++i) {
			sum += ((v1[i]-m1)/s1) * ((v2[i]-m2)/s2);
		}
		return (1.0/(v1.length - 1)) * sum;
	}
	*/
	public static void addTo(double[] v, double scalar) {
		for (int i = 0; i < v.length; ++i)
			v[i] += scalar;
	}
	public static void addTo(double[] v1, double[] v2) {
		addTo(v1,v2,1.0);
	}
	public static void addTo(double[] v1, double[] v2, double scaleV2) {
		for (int i = 0; i < v1.length; ++i) {
			v1[i] += scaleV2 * v2[i];
		}
	}
	
	public static void scale(double[] v, double[] s) {
		for (int i = 0; i < v.length; ++i) {
			v[i] *= s[i];
		}
	}
	
	public static void scale(double[] v, double s) {
		for (int i = 0; i < v.length; ++i) {
			v[i] *= s;
		}
	}
	
	public static double euclidean(double[] v1, double[] v2) {
		double distSum = 0;
		if (v1.length != v2.length)
			throw new IllegalArgumentException("vectors not same length: "+v1.length+" != "+v2.length);
		for (int i = 0; i < v1.length; ++i) {
			distSum += (v1[i]-v2[i])*(v1[i]-v2[i]);
		}
		return Math.sqrt(distSum);
	}
	
	public static double cosine(double[] v1, double[] v2) {
		double numer = 0;
		double len1 = 0;
		double len2 = 0;
		for (int i = 0; i < v1.length; ++i) {
			double d1 = v1[i];
			double d2 = v2[i];
			numer += d1 * d2;
			len2 += d2 * d2;
			len1 += d1 * d1;
		}
		double length = Math.sqrt(len1 * len2);
		if (length == 0) {
			return 0;
		}
		
		double sim = numer / length;
		return sim;
	}
	
	public static double dotProduct(double[] v1, double[] v2) {
		double dp = 0;
		for (int i = 0; i < v1.length; ++i) {
			dp += v1[i] * v2[i];
		}
		return dp;
	}
	
	public static double[] toPrimativeArray(Collection<Double> l) {
		double[] d = new double[l.size()];
		int ndx = 0;
		for (Double di : l) {
			d[ndx++] = di;
		}
		return d;
	}
	
	public static double[] randomVector(int len) {
		double[] v = new double[len];
		for (int i = 0; i < len; ++i)
			v[i] = Math.random();
		return v;
	}
	
	public static String toString(int[] v) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < v.length; ++i) {
			if (i != 0) {
				buf.append(", ");
			}
			buf.append(v[i]);
		}
		buf.append("]");
		return buf.toString();
	}
	
	public static Double[] toObjectArray(double[] v) {
		Double[] vo = new Double[v.length];
		for (int i = 0; i < v.length; ++i)
			vo[i] = v[i];
		return vo;
	}
	
	public static String toString(double[] v, String separator) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < v.length; ++i) {
			if (i != 0) {
				buf.append(separator);
			}
			buf.append(v[i]);
		}
		buf.append("]");
		return buf.toString();
	}
	
	public static String toString(double[] v) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < v.length; ++i) {
			if (i != 0) {
				buf.append(", ");
			}
			buf.append(v[i]);
		}
		buf.append("]");
		return buf.toString();
	}
	
	public static int randomFromPdf(double[] pdf, Random rand) {
		int ndx = 0;
		double cummulative = 0;
		double val = rand.nextDouble();
		for (ndx = 0; ndx < pdf.length-1; ++ndx) {
			cummulative += pdf[ndx];
			if (val < cummulative)
				break;
		}
		return ndx;
	}
	
	public static double gapFirstAndSecond(double[] v) {
		if (v.length < 2)
			throw new IllegalArgumentException("Cannot compute gap on vector with length "+v.length);
		
		double first = Double.NEGATIVE_INFINITY;
		double second = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < v.length; ++i) {
			if (v[i] > first) {
				second = first;
				first = v[i];
			} else if (v[i] > second) {
				second = v[i];
			}
		}
		return first - second;
	}
}
