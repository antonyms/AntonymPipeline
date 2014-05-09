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

/*
 * Created on Apr 23, 2008
 * 
 */
package com.ibm.bluej.util.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class MutableDoubleHashMap {
	public static <T> void toTSVString(Map<T,MutableDouble> m, String filename) {
		PrintStream out = null;
		FileUtil.ensureWriteable(new File(filename));
		try {
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			out.println(e.getKey().toString()+'\t'+e.getValue());
		}
		out.close();
	}
	public static <T> String toTSVString(Map<T,MutableDouble> m) {
		StringBuffer buf = new StringBuffer();
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			buf.append(e.getKey().toString()+'\t'+e.getValue()+'\n');
		}
		return buf.toString();
	}
	public static HashMap<String,MutableDouble> fromTSVString(String tsv, double min) {
		String[] lines = tsv.split("\n");
		return fromTSVString(Arrays.asList(lines), min);
	}
	public static HashMap<String,MutableDouble> fromTSVString(Iterable<String> lines, double min) {
		HashMap<String,MutableDouble> m = new HashMap<String,MutableDouble>();
		for (String line : lines) {
			int tbNdx = line.lastIndexOf('\t');
			double v = Double.parseDouble(line.substring(tbNdx+1));
			if (v >= min)
				m.put(line.substring(0,tbNdx), new MutableDouble(v));
		}
		return m;
	}
	
	public static <T> boolean equals(Map<T,MutableDouble> m1, Map<T,MutableDouble> m2) {
		if (m1.size() != m2.size())
			return false;
		for (Map.Entry<T, MutableDouble> e : m1.entrySet()) {
			MutableDouble d2 = m2.get(e.getKey());
			if (d2 == null)
				return false;
			if (e.getValue().value != d2.value)
				return false;
		}
		return true;
	}
	
	//CONSIDER: least to greatest iterator? greatest to least iterator?
	//n-largest? n-smallest?
	public static class OverlapRecord<T> implements Comparable {
		public OverlapRecord(T key, MutableDouble val1, MutableDouble val2, double overlap) {
			this.key = key;
			this.val1 = val1;
			this.val2 = val2;
			this.overlap = overlap;
		}
		public T key;
		public MutableDouble val1, val2;
		public double overlap;
		
		public int compareTo(Object o) {
			if (!(o instanceof OverlapRecord)) {
				return 0;
			}
			return (int)Math.signum(this.overlap - ((OverlapRecord)o).overlap);
		}
		
		public String toString() {
			return key + " " + Lang.dblStr(overlap)+ "("+Lang.dblStr(val1.value)+", "+Lang.dblStr(val2.value)+")";
		}
	}
	//sorted from least overlap to most?
	public static <T> List<OverlapRecord<T>> getOverlap(Map<T,MutableDouble> m1, Map<T,MutableDouble> m2) {
		List<OverlapRecord<T>> overlap = new ArrayList<OverlapRecord<T>>();
		for (Map.Entry<T, MutableDouble> em1 : m1.entrySet()) {
			MutableDouble vm2 = m2.get(em1.getKey());
			if (vm2 != null) {
				MutableDouble vm1 = em1.getValue();
				double ov = Math.min(vm1.value, vm2.value);
				overlap.add(new OverlapRecord<T>(em1.getKey(), vm1, vm2, ov));
			}
		}
		Collections.sort(overlap);
		return overlap;
	}
	
	public static <T> List<OverlapRecord<T>> getOverlapDirectional(Map<T,MutableDouble> m1, Map<T,MutableDouble> m2) {
		List<OverlapRecord<T>> overlap = new ArrayList<OverlapRecord<T>>();
		for (Map.Entry<T, MutableDouble> em1 : m1.entrySet()) {
			MutableDouble vm2 = m2.get(em1.getKey());
			MutableDouble vm1 = em1.getValue();
			if (vm2 != null) {
				double ov = Math.min(vm1.value, vm2.value);
				overlap.add(new OverlapRecord<T>(em1.getKey(), vm1, vm2, ov));
			} else {
				overlap.add(new OverlapRecord<T>(em1.getKey(), vm1, new MutableDouble(), 0));
			}
		}
		Collections.sort(overlap);
		return overlap;
	}
	
	public static <T> void subtract(Map<T,MutableDouble> from, Map<T,MutableDouble> x, boolean newEntries) {
		for (Map.Entry<T, MutableDouble> e : x.entrySet()) {
			MutableDouble d = from.get(e.getKey());
			if (d == null) {
				if (newEntries) {
					from.put(e.getKey(), new MutableDouble(-e.getValue().value));
				}
			} else {
				d.value -= e.getValue().value;
				if (!newEntries && d.value < 0) {
					d.value = 0;
				}
			}
		}
	}
	
	public static <T> HashMap<T,MutableDouble> copyValues(Map<T,MutableDouble> map) {
		HashMap<T,MutableDouble> cmap = new HashMap<T,MutableDouble>();
		for (Map.Entry<T, MutableDouble> e : map.entrySet()) {
			cmap.put(e.getKey(), new MutableDouble(e.getValue().value));
		}
		return cmap;
	}
	
	public static <T> List<Pair<T, MutableDouble>> sorted(Map<T, MutableDouble> m, SecondPairComparator<T, MutableDouble> comp) {
		ArrayList<Pair<T, MutableDouble>> list = new ArrayList<Pair<T, MutableDouble>>();
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			list.add(new Pair<T, MutableDouble>(e.getKey(), e.getValue()));
		}
		Collections.sort(list, comp);
		return list;
	}
	
	/**
	 * least to greatest
	 * @param m
	 * @return
	 */
	public static <T> List<Pair<T, MutableDouble>> sorted(Map<T, MutableDouble> m) {
		return sorted(m, new SecondPairComparator<T, MutableDouble>());
	}
	
	/**
	 * greatest to least
	 * @param m
	 * @return
	 */
	public static <T> List<Pair<T, MutableDouble>> sortedReverse(Map<T, MutableDouble> m) {
		return sorted(m, new SecondPairComparator<T, MutableDouble>(SecondPairComparator.REVERSE));
	}
	
	public static <T> HashMap<T, MutableDouble> getGrubbsZ(Map<T, MutableDouble> m, double mean, double variance) {
		double stdDev = Math.sqrt(variance);
		HashMap<T, MutableDouble> Zs = new HashMap<T, MutableDouble>();
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			Zs.put(e.getKey(), new MutableDouble(Math.abs(e.getValue().value - mean) / stdDev));
		}
		return Zs;
	}
	
	/*//TODO: use apache commons math TDistribution to get p from t,df for one tailed or two tailed
	public static <T> HashMap<T, MutableDouble> getGrubbsP(Map<T, MutableDouble> Zs, double mean, double variance) {
		double N = Zs.size();
		for (Map.Entry<T, MutableDouble> e : Zs.entrySet()) {
			double Z = e.getValue().value;
			double T = Math.sqrt((N*(N-2)*Z*Z)/
					((N-1)*(N-1)-N*Z*Z));
			//TODO: look up p value corresponding to this T value for two-tailed test with N-2 degrees of freedom.
		}
	}
	*/
	
	public static double getMean(Map<?, MutableDouble> m) {
		double total = 0;
		for (MutableDouble d : m.values()) {
			total += d.value;
		}
		return total/m.size();
	}
	public static double getVariance(Map<?, MutableDouble> m, double mean) {
		double totalV = 0;
		for (MutableDouble d : m.values()) {
			totalV += (mean - d.value)*(mean - d.value);
		}
		return totalV/m.size();
	}
	//thresholds must be least to greatest
	public static int[] getHisto(Map<?, MutableDouble> m, double[] thresholds) {
		int[] histos = new int[thresholds.length+1];
		for (MutableDouble d : m.values()) {
			boolean found = false;
			for (int i = 0; i < thresholds.length && !found; ++i) {
				if (d.value < thresholds[i]) {
					++histos[i];
					found = true;
				}
			}
			if (!found) {
				++histos[histos.length-1];
			}
		}
		return histos;
	}
	public static int[] getHisto2(Map<?, ? extends Map<?, MutableDouble>> m, double[] thresholds) {
		int[] histos = new int[thresholds.length+1];
		for (Map<?,MutableDouble> e : m.values()) {
			for (MutableDouble d : e.values()) {
				boolean found = false;
				for (int i = 0; i < thresholds.length && !found; ++i) {
					if (d.value < thresholds[i]) {
						++histos[i];
						found = true;
					}
				}
				if (!found) {
					++histos[histos.length-1];
				}
			}
		}
		return histos;
	}
	public static String stringHisto(double[] thresholds, int[] counts) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < thresholds.length; ++i) {
			buf.append(Lang.LPAD("< "+thresholds[i], 20)+" "+counts[i]+"\n");
		}
		buf.append(Lang.LPAD(">= "+thresholds[thresholds.length-1],20)+" "+counts[thresholds.length]+"\n");
		return buf.toString();
	}
	
	public static <K> double getDefaultZero(Map<K, MutableDouble> m, K key) {
		MutableDouble d = m.get(key);
		if (d == null) {
			return 0;
		}
		return d.value;
	}
	
	public static <S, T, M extends Map<T, MutableDouble>> double getDefaultZero(Map<S, M> map, S key1, T key2) {
		Map<T, MutableDouble> m2 = map.get(key1);
		if (m2 == null) {
			return 0;
		}
		MutableDouble d = m2.get(key2);
		if (d == null) {
			return 0;
		}
		return d.value;
	}
	
	public static <T> Pair<T,MutableDouble> maxEntry(Map<T, MutableDouble> map) {
		T maxKey = null;
		MutableDouble max = null;
		for (Map.Entry<T, MutableDouble> e : map.entrySet()) {
			if (max == null || e.getValue().value > max.value) {
				maxKey = e.getKey();
				max = e.getValue();
			}
		}
		if (max == null) {
			return null;
		}
		return Pair.make(maxKey, max);
	}
	
	/**
	 * Rank 1 means key is the maxKey, rank 2 means one entry is greater...
	 * Null when key is not present
	 * @param map
	 * @param key
	 * @return
	 */
	public static <T> Double rank(Map<T,MutableDouble> map, T key) {
		MutableDouble d = map.get(key);
		if (d == null) 
			return null;
		double rank = 1;
		for (MutableDouble o : map.values()) {
			if (o.value > d.value) {
				rank += 1;
			}
			if (o.value == d.value) {
				rank += 0.5;
			}
		}
		return rank - 0.5;
	}
	
	public static <T> T maxKey(Map<T, MutableDouble> map) {
		T maxKey = null;
		MutableDouble max = null;
		for (Map.Entry<T, MutableDouble> e : map.entrySet()) {
			if (max == null || e.getValue().value > max.value) {
				maxKey = e.getKey();
				max = e.getValue();
			}
		}
		return maxKey;
	}

	public static MutableDouble maxValue(Map<?, MutableDouble> map) {
		MutableDouble max = null;
		for (MutableDouble d : map.values()) {
			if (max == null || (d != null && max.value < d.value)) {
				max = d;
			}
		}
		return max;
	}
	public static MutableDouble minValue(Map<?, MutableDouble> map) {
		MutableDouble min = null;
		for (MutableDouble d : map.values()) {
			if (min == null || (d != null && min.value > d.value)) {
				min = d;
			}
		}
		return min;
	}
	//TODO: approximate median
	
	public static <S, T, M extends Map<T, MutableDouble>> int trimDoubleByThreshold(Map<S, M> doubleMap, double removeBelow) {
		Iterator<Map.Entry<S, M>> oit = (Iterator<Map.Entry<S, M>>)doubleMap.entrySet().iterator();
		int newSize = 0;
		while (oit.hasNext()) {
			Map.Entry<S, M> entry = oit.next();
			Map<T, MutableDouble> hashMap = entry.getValue();
			Iterator<Map.Entry<T, MutableDouble>> it = (Iterator<Map.Entry<T, MutableDouble>>)hashMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<T, MutableDouble> e = it.next();
				if (e.getValue().value < removeBelow) {
					it.remove();
				} else {
					++newSize;
				}
			}
			if (hashMap.isEmpty()) {
				oit.remove();
			}
		}
		return newSize;
	}
	
	public static <S, T, M extends Map<T, MutableDouble>> int thresholds(Map<S, M> doubleMap, double minValue, double maxValue) {
		Iterator<Map.Entry<S, M>> oit = (Iterator<Map.Entry<S, M>>)doubleMap.entrySet().iterator();
		int newSize = 0;
		while (oit.hasNext()) {
			Map.Entry<S, M> entry = oit.next();
			Map<T, MutableDouble> hashMap = entry.getValue();
			Iterator<Map.Entry<T, MutableDouble>> it = (Iterator<Map.Entry<T, MutableDouble>>)hashMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<T, MutableDouble> e = it.next();
				if (e.getValue().value < minValue) {
					it.remove();
				} else {
					if (e.getValue().value > maxValue) {
						e.getValue().value = maxValue;
					}
					++newSize;
				}
			}
			if (hashMap.isEmpty()) {
				oit.remove();
			}
		}
		return newSize;
	}
	
	public static <S, T, M extends Map<T, MutableDouble>> int thresholdsAbs(Map<S, M> doubleMap, double minValue, double maxValue) {
		Iterator<Map.Entry<S, M>> oit = (Iterator<Map.Entry<S, M>>)doubleMap.entrySet().iterator();
		int newSize = 0;
		while (oit.hasNext()) {
			Map.Entry<S, M> entry = oit.next();
			Map<T, MutableDouble> hashMap = entry.getValue();
			Iterator<Map.Entry<T, MutableDouble>> it = (Iterator<Map.Entry<T, MutableDouble>>)hashMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<T, MutableDouble> e = it.next();
				if (Math.abs(e.getValue().value) < minValue) {
					it.remove();
				} else {
					if (Math.abs(e.getValue().value) > maxValue) {
						e.getValue().value = Math.signum(e.getValue().value) * maxValue;
					}
					++newSize;
				}
			}
			if (hashMap.isEmpty()) {
				oit.remove();
			}
		}
		return newSize;
	}
	
	public static <K> void trimByThreshold(Map<K, MutableDouble> hashMap, double removeBelow) {	
		Iterator<Map.Entry<K, MutableDouble>> it = (Iterator<Map.Entry<K, MutableDouble>>)hashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<K, MutableDouble> entry = it.next();
			if (entry.getValue().value < removeBelow) {
				it.remove();
			}
		}
	}
	
	public static <K> void trimByThresholdAbs(Map<K, MutableDouble> hashMap, double removeBelow) {	
		Iterator<Map.Entry<K, MutableDouble>> it = (Iterator<Map.Entry<K, MutableDouble>>)hashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<K, MutableDouble> entry = it.next();
			if (Math.abs(entry.getValue().value) < removeBelow) {
				it.remove();
			}
		}
	}
	
	public static <T> void setMax(Map<T,MutableDouble> map, T key, double maybeMax) {
		MutableDouble old = map.get(key);
		if (old == null) {
			map.put(key, new MutableDouble(maybeMax));
			return;
		}
		old.value = Math.max(old.value, maybeMax);
	}
	
	public static <T> void addTo(Map<T, MutableDouble> addTo, Map<T, MutableDouble> toAdd) {
		for (Map.Entry<T, MutableDouble> entry : toAdd.entrySet()) {	
			MutableDoubleHashMap.increase(addTo, entry.getKey(), entry.getValue().value);
		}
	}
	public static <S, T> void addTo2(Map<T, HashMap<S, MutableDouble>> addTo, Map<T, HashMap<S, MutableDouble>> toAdd) {
		for (Map.Entry<T, HashMap<S, MutableDouble>> e1 : toAdd.entrySet()) {
			for (Map.Entry<S, MutableDouble> e2 : e1.getValue().entrySet())
				MutableDoubleHashMap.increase(addTo, e1.getKey(), e2.getKey(), e2.getValue().value);
		}
	}
	
	public static <T> void averageIn(int newSize, Map<T, MutableDouble> average, Map<T, MutableDouble> toAdd) {
		averageIn(newSize, average, toAdd, 1.0);
	}
	public static <T> void averageIn(double newSize, Map<T, MutableDouble> average, Map<T, MutableDouble> toAdd, double count) {
		scale(average, (newSize-count)/newSize);
		for (Map.Entry<T, MutableDouble> entry : toAdd.entrySet()) {			
			T id = entry.getKey();
			MutableDouble centroidCount = average.get(id);
			if (centroidCount == null) {
				centroidCount = new MutableDouble(0);
				average.put(id, centroidCount);
			}
			centroidCount.value += count/newSize * entry.getValue().value;
		}
		
	}

	public static <T> void averageOut(int oldSize, Map<T, MutableDouble> average, Map<T, MutableDouble> toSubtract) {
		if (oldSize-1 == 0) {
			average.clear();
			return;
		}
		for (Map.Entry<T, MutableDouble> entry : toSubtract.entrySet()) {			
			T id = entry.getKey();
			MutableDouble centroidCount = average.get(id);
			if (centroidCount != null) {
				centroidCount.value = Math.max(0, 
						centroidCount.value -  1.0/oldSize * entry.getValue().value);
			} else {
				//ERROR!
			}	
		}
		scale(average, oldSize/(oldSize-1.0));
	}
	
	/**
	 * Increase the key1->key2->v by value
	 * @param doubleMap
	 * @param key1
	 * @param key2
	 * @param value 
	 * @return true if there was a new entry (key1, key2) previously unseen
	 */
	public static <S, T> boolean increase(Map<S, HashMap<T, MutableDouble>> doubleMap, S key1, T key2, double value) {
		HashMap<T, MutableDouble> m = doubleMap.get(key1);
		if (m == null) {
			m = new HashMap<T, MutableDouble>();
			doubleMap.put(key1, m);
		}
		return increase(m, key2, value);
	}
	
	/**
	 * 
	 * @param doubleMap
	 * @param factory
	 * @param key1
	 * @param key2
	 * @param value
	 * @return true if it created a new entry
	 */
	public static <S, T, M extends Map<T, MutableDouble>> boolean increase(Map<S, M> doubleMap, FunVT<M> factory, S key1, T key2, double value) {
		M map = doubleMap.get(key1);
		if (map == null) {
			map = factory.f();
			doubleMap.put(key1, map);
		}
		return increase(map, key2, value);
	}
	
	//returns true if it created a new entry
	public static <T> boolean increase(Map<T, MutableDouble> map, T key, double value) {
		boolean created = false;
		MutableDouble d = map.get(key);
		if (d == null) {
			d = new MutableDouble();
			map.put(key, d);
			created = true;
		}
		d.value += value;
		return created;
	}
	
	public static HashMap<?, IndexValuePair> convertToIndex(Map<?, MutableDouble> hashMap, int firstIndex, double minimum) {
		HashMap index = new HashMap((int) Math.ceil(hashMap.size()*1.34));
		for (Map.Entry<?, MutableDouble> entry : hashMap.entrySet()) {
			double value = entry.getValue().value;
			if (value >= minimum) {
				Object key = entry.getKey();
				index.put(key, new IndexValuePair(firstIndex, value));
				++firstIndex;
			}
		}
		return index;
	}
	
	
	public static double sum(Map<?, MutableDouble> c1) {
		double sum = 0;
		for (MutableDouble d : c1.values()) {
			sum += d.value;
		}
		return sum;
	}
	
	public static double sum2(Map<?, ? extends Map<?, MutableDouble>> c1) {
		double sum = 0;
		for (Map<?, MutableDouble> e : c1.values()) {
			for (MutableDouble d : e.values()) {
				sum += d.value;
			}
		}
		return sum;
	}
	
	public static double normalize(Map<?, MutableDouble> m) {
		double norm = getNorm(m);
		scale(m, 1/norm);
		return norm;
	}
	public static double normalizeOne(Map<?, MutableDouble> m) {
		double norm = getOneNorm(m);
		scale(m, 1/norm);
		return norm;
	}
	
	public static void scale(Map<?, MutableDouble> m, double scalingFactor) {
		for (MutableDouble d : m.values()) {
			d.value *= scalingFactor;
		}
	}
	
	public static double getNorm(Map<?, MutableDouble> m) {
		return Math.sqrt(getTwoNorm(m));
	}

	/**
	 * Version of getNorm that doesn't take the square root
	 * @param m a sparse vector
	 * @return The two norm WITHOUT the square root
	 */
	public static double getTwoNorm(Map<?, MutableDouble> m) {
		double norm = 0;
		for (MutableDouble d : m.values()) {
			norm += d.value * d.value;
		}
		return norm;
	}
	
	public static double getOneNorm(Map<?, MutableDouble> m) {
		double norm = 0;
		for (MutableDouble d : m.values()) {
			norm += Math.abs(d.value);
		}
		return norm;
	}
	
	public static <S> double dotProduct(Map<S, MutableDouble> c1, Map<S, MutableDouble> c2) {
		double sumProd = 0;
		for (Map.Entry<S, MutableDouble> e1 : c1.entrySet()) {
			S element = e1.getKey();
			MutableDouble vm1 = e1.getValue();

			double vm1Val = vm1.value;
			MutableDouble vm2 = c2.get(element);
			if (vm2 != null) {
				double vm2Val = vm2.value;
				sumProd += vm1Val * vm2Val;
			}
		}
		return sumProd;
	}
	
	//faster version of cosineSimilarity, should really just replace the old version
	public static <S> double cosineSimilarityAlt(Map<S, MutableDouble> c1, Map<S, MutableDouble> c2, 
			double sizePenalty) 
	{
		//CONSIDER: not a great size penalty, should ignore top N overlapped dims
		double numer = 0;
		double len1 = 0;
		double len2 = 0;
		double overlapedDims = 0;
		for (Map.Entry<S, MutableDouble> e1 : c1.entrySet()) {
			S element = e1.getKey();
			MutableDouble vm1 = e1.getValue();

			double vm1Val = vm1.value;
			len1 += vm1Val * vm1Val;
			MutableDouble vm2 = c2.get(element);
			if (vm2 != null) {
				double vm2Val = vm2.value;
				numer += vm1Val * vm2Val;
				++overlapedDims;
			}
		}
		for (Map.Entry<S, MutableDouble> e2 : c2.entrySet()) {
			double vm2Val = e2.getValue().value;
			len2 += vm2Val * vm2Val;
		}
		
		double length = Math.sqrt(len1 * len2);
		if (length == 0) {
			return 0;
		}
		
		double sim = numer / length;
		if (Double.isNaN(sim)) {
			System.err.println("one of the vectors has a NaN or Infinity value");
			return 0;
		}
		if (overlapedDims == 0) {
			return 0;
		}
		return Math.max(0, sim * (1.0-Math.min(1.0, sizePenalty/overlapedDims)));
	}
	
	public static <S> double cosineSimilarityFlex(Map<S, MutableDouble> c1, Map<S, MutableDouble> c2, 
			double sizePenalty, FunST<S,Double> weightFun, FunST<Double, Double> thresholdFun) 
	{
		//not a great size penalty, should ignore top N overlapped dims
		double numer = 0;
		double len1 = 0;
		double len2 = 0;
		double overlapedDims = 0;
		for (Map.Entry<S, MutableDouble> e1 : c1.entrySet()) {
			S element = e1.getKey();
			MutableDouble vm1 = e1.getValue();
			double weight = weightFun.f(element);
			double vm1Val = vm1.value * weight;
			vm1Val = thresholdFun.f(vm1Val);
			if (vm1Val == 0) {
				continue;
			}
			len1 += vm1Val * vm1Val;
			MutableDouble vm2 = c2.get(element);
			if (vm2 != null) {
				double vm2Val = vm2.value * weight;
				vm2Val = thresholdFun.f(vm2Val);
				numer += vm1Val * vm2Val;
				++overlapedDims;
			}
		}
		for (Map.Entry<S, MutableDouble> e2 : c2.entrySet()) {
			double vm2Val = e2.getValue().value * weightFun.f(e2.getKey());
			vm2Val = thresholdFun.f(vm2Val);
			len2 += vm2Val * vm2Val;
		}
		
		double length = Math.sqrt(len1 * len2);
		if (length == 0) {
			return 0;
		}
		
		double sim = numer / length;
		if (Double.isNaN(sim)) {
			System.err.println("one of the vectors has a NaN or Infinity value");
			return 0;
		}
		
		return Math.max(0, sim * (1.0-Math.min(1.0, sizePenalty/overlapedDims)));
	}
	
	public static <S> double cosineSimilarity(Map<S, MutableDouble> c1, Map<S, MutableDouble> c2) {
		return cosineSimilarity(c1,c2,0);
	}
	public static <S> double cosineSimilarity(Map<S, MutableDouble> c1, Map<S, MutableDouble> c2, double sizePenalty) {
		return cosineSimilarityAlt(c1,c2,sizePenalty);
	}
	
	/**
	 * @deprecated
	 * @param c1
	 * @param c2
	 * @param sizePenalty
	 * @return
	 */
	public static double cosineSimilarityOld(Map<?, MutableDouble> c1, Map<?, MutableDouble> c2, double sizePenalty) {
		if (c1 == null || c2 == null) {
			throw new IllegalArgumentException("similarity HashMap vector argument was null");
		}

		HashSet<Object> nonzeros = new HashSet<Object>();
		nonzeros.addAll(c1.keySet());
		nonzeros.addAll(c2.keySet());
		
		if (nonzeros.isEmpty()) {
			System.err.println("similarity HashMap vector argument was empty");
			return 0;
			//throw new IllegalArgumentException("similarity HashMap vector argument was empty");
		}
		
		double numer = 0;
		double len1 = 0;
		double len2 = 0;
		int overlapedDims = 0;
		for (Object i : nonzeros) {
			MutableDouble d1 = c1.get(i);
			MutableDouble d2 = c2.get(i);
			
			if (d1 == null) {
				len2 += Math.pow(d2.value, 2);
			} else if (d2 == null) {
				len1 += Math.pow(d1.value, 2);
			} else {
				numer += d1.value * d2.value;
				len2 += Math.pow(d2.value, 2);
				len1 += Math.pow(d1.value, 2);
				++overlapedDims;
			}

		}
		double length = Math.sqrt(len1 * len2);
		if (length == 0) {
			return 0;
		}
		
		double sim = numer / length;
		if (Double.isNaN(sim)) {
			System.err.println("one of the vectors has a NaN or Infinity value");
			return 0;
		}
		
		return Math.max(0, sim * (1.0-Math.min(1.0, sizePenalty/overlapedDims)));
	}
	
	
	public static double jaccard(Map<?, MutableDouble> c1, Map<?, MutableDouble> c2) {
		if (c1 == null || c2 == null) {
			throw new IllegalArgumentException("similarity HashMap vector argument was null");
		}

		HashSet<Object> nonzeros = new HashSet<Object>();
		nonzeros.addAll(c1.keySet());
		nonzeros.addAll(c2.keySet());
		
		if (nonzeros.isEmpty()) {
			throw new IllegalArgumentException("similarity HashMap vector argument was empty");
		}
		
		double minSum = 0;
		double maxSum = 0;
		for (Object i : nonzeros) {
			MutableDouble d1 = c1.get(i);
			MutableDouble d2 = c2.get(i);
			if (d1 == null) {
				maxSum += d2.value;
			} else if (d2 == null) {
				maxSum += d1.value;
			} else {
				minSum += Math.min(d1.value, d2.value);
				maxSum += Math.max(d1.value, d2.value);
			}
		}
		if (maxSum == 0) {
			return 0;
		}
		
		double sim = minSum/maxSum;
		if (Double.isNaN(sim)) {
			System.err.println("one of the vectors has a NaN or Infinity value");
			return 0;
		}
		
		return sim;		
	}
	
	//recreates the DIRT similarity metric when used with MICondition
	public static double totalOverlap(Map<?, MutableDouble> c1, Map<?, MutableDouble> c2) {
		if (c1 == null || c2 == null) {
			throw new IllegalArgumentException("similarity HashMap vector argument was null");
		}

		HashSet<Object> nonzeros = new HashSet<Object>();
		nonzeros.addAll(c1.keySet());
		nonzeros.addAll(c2.keySet());
		
		if (nonzeros.isEmpty()) {
			throw new IllegalArgumentException("similarity HashMap vector argument was empty");
		}
		
		double overlap = 0;
		double nonOverlap = 0;
		for (Object i : nonzeros) {
			MutableDouble d1 = c1.get(i);
			MutableDouble d2 = c2.get(i);
			if (d1 == null) {
				nonOverlap += d2.value;
			} else if (d2 == null) {
				nonOverlap += d1.value;
			} else {
				overlap += d1.value + d2.value;
			}
		}
		if (nonOverlap + overlap == 0) {
			return 0;
		}
		
		double sim = overlap / (overlap + nonOverlap);
		if (Double.isNaN(sim)) {
			System.err.println("one of the vectors has a NaN or Infinity value");
			return 0;
		}
		
		return sim;
	}
	
	public static <T> String toString(Map<T, MutableDouble> m) {
		return toString(m, 0);
	}
	public static <T> String toString(Map<T, MutableDouble> m, int topN) {
		return toString(m,topN,30);
	}
	
	public static <T> String toString(Map<T, MutableDouble> m, int topN, int width) {
		StringBuffer buf = new StringBuffer();
		SecondPairComparator<T,MutableDouble> comp = new SecondPairComparator<T,MutableDouble>(new MutableDouble.AbsValueComparator());
		comp.setReverseOrdering();
		List<Pair<T, MutableDouble>> sorted = sorted(m, comp); //absolute value, descending
		int printed = 0;
		for (Pair<T, MutableDouble> e : sorted) {
			buf.append(Lang.LPAD(e.first.toString(),width)+" "+e.second).append('\n');
			if (topN > 0 && ++printed >= topN) {
				break;
			}
		}
		return buf.toString();
	}
	
	public static <S,T,M extends Map<T,MutableDouble>> void trimDimensions2(Map<S,M> coOccurrence, double retainLength) {
		for (Map.Entry<S, M> e : coOccurrence.entrySet()) {
			Map<T,MutableDouble> map = e.getValue();
			double norm = getTwoNorm(map);
			if (norm > 0){
				double dropLength = norm * (1 - retainLength);
				List<Pair<T,MutableDouble>> sortedDim = sorted(map);
				double lengthSeen = 0.0;
				for(Pair<T,MutableDouble> dim : sortedDim) {
					lengthSeen += Math.pow(dim.second.value, 2);
					if (lengthSeen > dropLength) break;
					else map.remove(dim.first);
				}
			}
		}
	}
	
	public static <T> void trimDimensions(Map<T,MutableDouble> map, double retainLength) {
		double norm = getTwoNorm(map);
		if (norm > 0){
			double dropLength = norm * (1 - retainLength);
			List<Pair<T,MutableDouble>> sortedDim = sorted(map);
			double lengthSeen = 0.0;
			for(Pair<T,MutableDouble> dim : sortedDim) {
				lengthSeen += Math.pow(dim.second.value, 2);
				if (lengthSeen > dropLength) break;
				else map.remove(dim.first);
			}
		}	
	}
	
	public static <T> void trimToTopN(Map<T,MutableDouble> map, int maxSize) {
		if (maxSize >= map.size())
			return;
		List<Pair<T,MutableDouble>> sortedDim = sorted(map);
		int numDimsToTrim = map.size() - maxSize;
		for(Pair<T,MutableDouble> dim : sortedDim) {
			map.remove(dim.first);
			if (--numDimsToTrim <= 0)
				break;
		}
	}
	
	public static <S,T,M extends Map<T,MutableDouble>> void trimDouble(Map<S,M> coOccurrence, double minFirst, double minSecond) {
		Map<S, MutableDouble> firstFreq = new HashMap<S, MutableDouble>();
		Map<T, MutableDouble> secondFreq = new HashMap<T, MutableDouble>();
		double total = 0;
		for (Map.Entry<S, M> co1 : coOccurrence.entrySet()) {
			S i1 = co1.getKey();
			for (Map.Entry<T, MutableDouble> co2 : co1.getValue().entrySet()) {			
				T i2 = co2.getKey();
				MutableDouble count = co2.getValue();
				
				MutableDoubleHashMap.increase(firstFreq, i1, count.value);
				MutableDoubleHashMap.increase(secondFreq, i2, count.value);
				total += count.value;
			}
		}
		if (minFirst > 0) {
			for (Map.Entry<S,MutableDouble> e : firstFreq.entrySet()) {
				if (e.getValue().value < minFirst) {
					coOccurrence.remove(e.getKey());
				}
			}
		}
		if (minSecond > 0) {
			for (Map.Entry<S, M> e1 : coOccurrence.entrySet()) {
				ArrayList<T> toRemove = new ArrayList<T>();
				for (Map.Entry<T, MutableDouble> e : e1.getValue().entrySet()) {
					if (secondFreq.get(e.getKey()).value < minSecond) {
						toRemove.add(e.getKey());
					}
				}
				for (T r : toRemove) {
					e1.getValue().remove(r);
				}
			}
		}		
	}
	
	//TODO: lexographer's PMI
	
	public static <S,T,M extends Map<T,MutableDouble>> void conditionPMI(Map<S,M> coOccurrence, double minFirst, double minSecond) {
		Map<S, MutableDouble> firstFreq = new HashMap<S, MutableDouble>();
		Map<T, MutableDouble> secondFreq = new HashMap<T, MutableDouble>();
		double total = 0;
		for (Map.Entry<S, M> co1 : coOccurrence.entrySet()) {
			S i1 = co1.getKey();
			for (Map.Entry<T, MutableDouble> co2 : co1.getValue().entrySet()) {			
				T i2 = co2.getKey();
				MutableDouble count = co2.getValue();
				
				MutableDoubleHashMap.increase(firstFreq, i1, count.value);
				MutableDoubleHashMap.increase(secondFreq, i2, count.value);
				total += count.value;
			}
		}
		if (minFirst > 0) {
			for (Map.Entry<S,MutableDouble> e : firstFreq.entrySet()) {
				if (e.getValue().value < minFirst) {
					coOccurrence.remove(e.getKey());
				}
			}
		}
		if (minSecond > 0) {
			for (Map.Entry<S, M> e1 : coOccurrence.entrySet()) {
				ArrayList<T> toRemove = new ArrayList<T>();
				for (Map.Entry<T, MutableDouble> e : e1.getValue().entrySet()) {
					if (secondFreq.get(e.getKey()).value < minSecond) {
						toRemove.add(e.getKey());
					}
				}
				for (T r : toRemove) {
					e1.getValue().remove(r);
				}
			}
		}
		conditionPMI(coOccurrence, firstFreq, secondFreq, total, total, total);
	}
	
	public static double[] toDense(Map<Integer, MutableDouble> m) {
		int maxKey = 0;
		for (Integer k : m.keySet()) {
			if (k > maxKey) maxKey = k;
			if (k < 0) throw new IllegalArgumentException("All keys must be non-negative");
		}
		double[] dense = new double[maxKey+1];
		for (Map.Entry<Integer, MutableDouble> e : m.entrySet()) {
			dense[e.getKey()] = e.getValue().value;
		}
		return dense;
	}
	
	public static <T> HashMap<T,MutableDouble> fromImmutable(Map<T,Double> im) {
		HashMap<T,MutableDouble> m = new HashMap<T,MutableDouble>();
		for (Map.Entry<T, Double> e : im.entrySet()) {
			m.put(e.getKey(), new MutableDouble(e.getValue()));
		}
		return m;
	}
	
	public static <T> HashMap<T,Double> toImmutable(Map<T,MutableDouble> m) {
		HashMap<T,Double> im = new HashMap<T,Double>();
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			im.put(e.getKey(), e.getValue().value);
		}
		return im;
	}
	
	public static <S,T,M extends Map<T,MutableDouble>> void conditionPMI(Map<S,M> coOccurrence)
	{
		conditionPMI(coOccurrence, 0, 0);
	}
	
	public static <S,T,M extends Map<T,MutableDouble>> void conditionPMI(
			Map<S, M> coOccurrence, 
			Map<S, MutableDouble> firstFreq, 
			Map<T, MutableDouble> secondFreq, 
			double firstTotal,
			double secondTotal,
			double coTotal) 
	{
		MutableDouble smooth = new MutableDouble(1);
		for (Map.Entry<S, M> co1 : coOccurrence.entrySet()) {
			S i1 = co1.getKey();
			MutableDouble f1 = Lang.NVL(firstFreq.get(i1), smooth);
			for (Map.Entry<T, MutableDouble> co2 : co1.getValue().entrySet()) {			
				T i2 = co2.getKey();
				MutableDouble count = co2.getValue();		
				
				MutableDouble f2 = Lang.NVL(secondFreq.get(i2), smooth);
				count.value = 
					((count.value-1)/coTotal)/
					((f1.value/firstTotal) * (f2.value/secondTotal));
				//section 3 in Discovering Word Senses from Text
				double discountingFactor = (count.value/(count.value+1)) * 
					(Math.min(f1.value,f2.value)/(Math.min(f1.value,f2.value)+1));
				//count.value *= discountingFactor;
			}
		}
		
	}
	
	//key dims for one
	public static <T> Collection<T> getKeyDims(Map<T,MutableDouble> map, int numDims) {
		NBest<Pair<T,MutableDouble>> best = new NBest<Pair<T,MutableDouble>>(numDims, new SecondPairComparator());
		for (Map.Entry<T,MutableDouble> p : map.entrySet()) {
			best.add(new Pair<T,MutableDouble>(p.getKey(), p.getValue()));
		}
		Collection<T> keyDims = new ArrayList<T>();
		for (Pair<T,MutableDouble> p : best.empty()) {
			keyDims.add(p.first);
		}
		return keyDims;
	}
	
	public static <S,T,M extends Map<T, MutableDouble>> Map<T, ArrayList<S>> buildKeyDims(Map<S, M> coOccurrence, int numDims) {
		Map<T, ArrayList<S>> keyDims = new HashMap<T, ArrayList<S>>();
		for (Map.Entry<S, M> e : coOccurrence.entrySet()) {
			S key = e.getKey();
			NBest<Pair<T,MutableDouble>> best = new NBest<Pair<T,MutableDouble>>(numDims, new SecondPairComparator());
			for (Map.Entry<T,MutableDouble> p : e.getValue().entrySet()) {
				best.add(new Pair<T,MutableDouble>(p.getKey(), p.getValue()));
			}
			for (Pair<T,MutableDouble> b : best) {
				HashMapUtil.addAL(keyDims, b.first, key);
			}
			
		}
		return keyDims;
	}
	
	public static <T> void divideByAdd1(Map<T,MutableDouble> m, Map<T,MutableDouble> div) {
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			MutableDouble divd = div.get(e.getKey());
			double divBy = 1.0;
			if (divd != null) {
				divBy = divd.value + 1.0;
			}
			e.getValue().value /= divBy;
		}
	}
	
	public static <T> void divideBy(Map<T,MutableDouble> m, Map<T,MutableDouble> div) {
		for (Map.Entry<T, MutableDouble> e : m.entrySet()) {
			MutableDouble divd = div.get(e.getKey());
			double divBy = 0.0;
			if (divd != null) {
				divBy = divd.value;
			}
			e.getValue().value /= divBy;
		}
	}
	
	public static <T> void linearScaleUnitVariance(Map<T,MutableDouble> m) {
		double mean = MutableDoubleHashMap.getMean(m);
		double variance = MutableDoubleHashMap.getVariance(m, mean);
		for (MutableDouble x : m.values()) {
			x.value = (x.value - mean)/variance;
		}
	}
	

}
