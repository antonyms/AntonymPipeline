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

package com.ibm.bluej.consistency.learning;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.term.ValueTerm;
import com.ibm.bluej.util.common.ConvertedIterator;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.RandomUtil;

public class LearningState {
	
	public boolean randomInitialValue;
	long timestep;
	HashMap<MultiTermKey, ParamWeight> allParams = new HashMap<MultiTermKey, ParamWeight>();
	IdentityHashMap<ParamWeight, AvgParamValue> avgParams = new IdentityHashMap<ParamWeight, AvgParamValue>();
	
	public void incrementTimestep() {
		++timestep;
	}
	
	static final ATerm[] TIMESTEP_KEY = new ATerm[]{new StringTerm("SAMPLERANK_TIMESTEPS")};

	static ATerm[] keyFromString(String str) {
		str = str.substring(ParamWeight.BEGIN_STR.length(),str.length()-ParamWeight.END_STR.length());
		String[] strParts = str.split(",");
		ATerm[] parts = new ATerm[strParts.length];
		for (int i = 0; i < strParts.length; ++i) {
			parts[i] = ValueTerm.basicFromString(Lang.urlDecode(strParts[i]));
		}
		return parts;
	}
	
	private static class MultiTermKey implements Serializable {
		private static final long serialVersionUID = 1L;
		ATerm[] parts;
		MultiTermKey() {}
		MultiTermKey(ATerm[] parts) {
			this.parts = new ATerm[parts.length];
			for (int i = 0; i < parts.length; ++i) {
				this.parts[i] = parts[i].valueClone();
			}
		}

		//toString() and fromString() need to escape special characters
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append(ParamWeight.BEGIN_STR);
			for (int i = 0; i < parts.length; ++i) {
				if (i != 0) {
					buf.append(",");
				}
				if (parts[i] instanceof StringTerm && Lang.isDouble(ATerm.getString(parts[i]))) {
					buf.append(Lang.urlEncode("\""+parts[i].toString()+"\""));
				} else {
					buf.append(Lang.urlEncode(parts[i].toString()));
				}
			}
			buf.append(ParamWeight.END_STR);
			return buf.toString();
		}

		//Value hashCode and equals
		public boolean equals(Object o) {
			if (!(o instanceof MultiTermKey)) {
				return false;
			}
			MultiTermKey k = (MultiTermKey)o;
			return equals(this.parts, k.parts);
		}
		private static boolean equals(ATerm[] p1, ATerm[] p2) {
			if (p1.length != p2.length) {
				return false;
			}
			for (int i = 0; i < p1.length; ++i) {
				if (!p1[i].valueEquals(p2[i])) {
					return false;
				}
			}
			return true;
		}

		public int hashCode() {
			try {
				return hashCode(parts, ATerm.mdjbFirst());
			} catch (UnsupportedOperationException e) {
				System.err.println("failed hashcode attempt on key: ");
				for (ATerm p : parts) {
					System.err.println("   "+p);
				}
				throw e;
			}
		}	
		private static int hashCode(ATerm[] p, int h) {
			for (ATerm t : p) {
				h = ATerm.mdjbNext(h, t.valueHash());
			}
			return h;
		}
	}
	private MultiTermKey probe = new MultiTermKey();
	
	public ParamWeight instance(ATerm[] paramKey, InitializerFunction initial, double initValue) {
		probe.parts = paramKey;
		ParamWeight pw = allParams.get(probe);
		if (pw == null) {
			if (randomInitialValue) 
				pw = new ParamWeight(probe.toString(), Math.random() - 0.5);
			else
				pw = new ParamWeight(probe.toString(), initial == null ? initValue : initial.initialValue(paramKey));
			ATerm[] paramKeyCpy = new ATerm[paramKey.length];
			for (int i = 0; i < paramKey.length; ++i) {
				paramKeyCpy[i] = paramKey[i].valueClone();
			}
			allParams.put(new MultiTermKey(paramKeyCpy), pw);
		}
		return pw;
	}

	public void set(ATerm[] paramKey, double value) {
		probe.parts = paramKey;
		ParamWeight pw = allParams.get(probe);
		if (pw == null) {
			pw = new ParamWeight(probe.toString(), value);
			allParams.put(new MultiTermKey(paramKey), pw);
		}
		pw.value.value = value;
	}
	public ParamWeight instance(ATerm[] paramKey) {
		return instance(paramKey, null, 0);
	}
	
	Object reverseLookup(ParamWeight param) {
		for (Map.Entry<MultiTermKey, ParamWeight> e : allParams.entrySet()) {
			if (e.getValue() == param) {
				return e.getKey();
			}
		}
		return null;
	}
	
	public String allToString() {
		ArrayList<String> tosort = new ArrayList<String>();
		for (Map.Entry<MultiTermKey, ParamWeight> e : allParams.entrySet()) {
			//if (Math.abs(e.getValue().value.value) > 0.01) {
				tosort.add(e.getKey() + EQUALS_STR+e.getValue().value);
			//}
		}
		Collections.sort(tosort);
		return Lang.stringList(tosort, "\n");
	}
	
	//enable iterating over Term[] that specify parameters
	public Iterator<ATerm[]> getAllSpecifiers() {
		return new ConvertedIterator<MultiTermKey,ATerm[]>(allParams.keySet().iterator(), new FunST<MultiTermKey,ATerm[]>() {
			public ATerm[] f(MultiTermKey o) {
				return o.parts;
			}
		});
	}
	
	public Iterable<ParamWeight> getAllParamWeights() {
		return allParams.values();
	}
	
	public void setParametersToAverage() {
		for (Map.Entry<MultiTermKey, ParamWeight> pe : allParams.entrySet()) {
			ATerm[] spec = pe.getKey().parts;
			
			if (Arrays.equals(spec, TIMESTEP_KEY) || spec[0].equals(ParamWeight.NON_WEIGHT)) {
				continue;
			}
			ParamWeight param = pe.getValue();
			
			AvgParamValue avg = avgParams.get(param);
			if (avg == null) {
				avg = new AvgParamValue(param.value.value);				
				avgParams.put(param,avg);
			}
			avg.update(timestep);
			param.value.value = avg.getSum() / timestep;
			avg.update(param.value.value, timestep);
		}
		recordTimestep();
	}

	public long getTimestep() {
		return timestep;
	}
	public void recordTimestep() {
		set(TIMESTEP_KEY, timestep);
	}

	static class AvgParamValue {
		AvgParamValue(double initial) {
			lastValue = initial;
		}
		
		//after a while the sumValue will not change if you add smallish (-3 3) numbers to it
		double sumSum = 0;
		long lastSumSumTimestep;
		
		double sumValue = 0; 
		double lastValue;
		long lastTimestep = 0;
		
		/**
		 * Sparse averages new value with old
		 * @param value The new value for the ParamWeight
		 * @param timestep The first timestep is 1. Timestep 0 is the initial value.
		 */
		void update(double value, long timestep) {
			update(timestep);
			lastValue = value;
		}
		/**
		 * Finishes the sparse averaging
		 * @param timestep One greater than the last timestep where learning took place
		 */
		void update(long timestep) {
			sumValue += lastValue * (timestep - lastTimestep);
			lastTimestep = timestep;
			if (lastTimestep > lastSumSumTimestep + 10000) {
				sumSum += sumValue;
				lastSumSumTimestep = lastTimestep;
				sumValue = 0;
			}
		}
		double getSum() {
			sumSum += sumValue;
			lastSumSumTimestep = lastTimestep;
			sumValue = 0;
			return sumSum;
		}
	}
	
	public void load(String[] lines) {
		timestep = LearningState.readIn(lines, allParams);
	}
	
	private static final String EQUALS_STR = " = ";
	static long readIn(String[] lines, HashMap<MultiTermKey, ParamWeight> paramsMap) {
		long timestep = 0;
		for (String line : lines) {
			if (!line.startsWith(ParamWeight.BEGIN_STR)) {
				continue;
			}
			int equalsPos = line.indexOf(EQUALS_STR);
			MultiTermKey multiTermKey = new MultiTermKey(keyFromString(line.substring(0, equalsPos)));
			double val = Double.parseDouble(line.substring(equalsPos+EQUALS_STR.length()));
			if (multiTermKey.equals(multiTermKey.parts, LearningState.TIMESTEP_KEY)) {
				timestep = (long)val;
			}
			ParamWeight prevVal = paramsMap.get(multiTermKey);
			if (prevVal != null) {
				prevVal.value.value = val;
			} else {
				paramsMap.put(multiTermKey, new ParamWeight(multiTermKey.toString(), val));
			}
		}
		return timestep;
	}
	
	public void merge(String[] lines) {
		//NOTE: also averages pseudo-params like sentenceCount
		//consider creating special PSEUDO marker for them
		long currentTimestep = (long)instance(TIMESTEP_KEY).value.value;
		HashMap<MultiTermKey, ParamWeight> toMergeParams = new HashMap<MultiTermKey, ParamWeight>();
		long toMergeTimestep = readIn(lines, toMergeParams);
		double weightCurrent = 1.0*currentTimestep/(currentTimestep+toMergeTimestep);
		//averageIn, also average the implicit zeros in the toMergeParams
		for (Map.Entry<MultiTermKey, ParamWeight> ce : allParams.entrySet()) {
			ParamWeight c = ce.getValue();
			ParamWeight m = toMergeParams.get(ce.getKey());
			double mv = 0;
			if (m != null) {
				mv = m.value.value;
			}
			c.value.value = c.value.value*weightCurrent + mv*(1-weightCurrent);
		}
		for (Map.Entry<MultiTermKey, ParamWeight> me : toMergeParams.entrySet()) {
			if (allParams.containsKey(me.getKey())) {
				continue;
			}
			me.getValue().value.value *= (1-weightCurrent);
			allParams.put(me.getKey(), me.getValue());
		}
		set(TIMESTEP_KEY, currentTimestep+toMergeTimestep);
	}
	
	private static class BDAvgParamValue {
		BDAvgParamValue(double initial) {
			lastValue = new BigDecimal(initial);
		}
		
		//CONSIDER: BigDecimal?
		BigDecimal sumValue = new BigDecimal(0);
		BigDecimal lastValue;
		int lastTimestep = 0;
		
		/**
		 * Sparse averages new value with old
		 * @param value The new value for the ParamWeight
		 * @param timestep The first timestep is 1. Timestep 0 is the initial value.
		 */
		void update(double value, int timestep) {
			sumValue = sumValue.add(lastValue.multiply(new BigDecimal(timestep - lastTimestep)));
			lastTimestep = timestep;
			lastValue = new BigDecimal(value);
		}
		/**
		 * Finishes the sparse averaging
		 * @param timestep One greater than the last timestep where learning took place
		 */
		void update(int timestep) {
			sumValue = sumValue.add(lastValue.multiply(new BigDecimal(timestep - lastTimestep)));
			lastTimestep = timestep;
		}
		double getSum() {
			return sumValue.doubleValue();
		}
	}
	
	//check for numeric stability
	//check that the periodic averaging works ok
	public static void main(String[] args) {
		double learnRate = 0.3;
		double acceptableDiff = 0.001;
		int numTimeSteps = 3000;
		//test sparse parameter averaging with comparison to dense parameter averaging
		ParamWeight[] params = new ParamWeight[50];
		BigDecimal[] denseAvg = new BigDecimal[params.length];
		LearningState state = new LearningState();
		for (int i = 0; i < params.length; ++i) {
			params[i] = state.instance(new ATerm[] {new NumberTerm(i)});
			params[i].value.value = Math.random() - 0.5;
			denseAvg[i] = new BigDecimal(params[i].value.value);
		}
		//NOTE: timestep starts at 1, initialization is timestep zero
		System.out.println("Error reductions");
		for (int ts = 1; ts < numTimeSteps; ++ts) {
			if (ts % 100 == 0) {
				state.setParametersToAverage();
			} else {
			
				WorldWeightParam w = new WorldWeightParam();
				int wsize = RandomUtil.randomInt(10, 100);
				for (int i = 0; i < wsize; ++i) {
					//CONSIDER: make some parameters more likely to result in bad Deltas
					int pNdx = RandomUtil.randomInt(0,params.length);
					double c = RandomUtil.randomInt(-9,10);
					w.update(params[pNdx], c);
				}
			
				double err = w.getWeight();
				if (err > 0) {
					w.badDelta(state);
				}
	
			}
	
			//update dense
			for (int i = 0; i < params.length; ++i) {
				denseAvg[i] = denseAvg[i].add(new BigDecimal(params[i].value.value));
			}
	
			//NOTE: this applied to an earlier version 
			// check that badDelta reduces them by a factor of (1-learnRate)
			/*
			double expectedNewError = err * (1-learnRate);
			if (Math.abs(expectedNewError - w.getWeight()) > acceptableDiff) {
				System.err.println("Too different: "+expectedNewError+" != "+w.getWeight());
			} else {
				System.out.println("  "+expectedNewError+" ~= "+w.getWeight());
			}
			*/	
		}
		//NOTE: final timestep is one past the last timestep of learning
		state.setParametersToAverage();
		for (int i = 0; i < params.length; ++i) {
			denseAvg[i] = denseAvg[i].divide(new BigDecimal(numTimeSteps), 1000, RoundingMode.HALF_UP);
		}

		//compare sparse and dense
		System.out.println("Sparse average vs. dense average");
		for (int i = 0; i < params.length; ++i) {
			
			if (Math.abs(params[i].value.value - denseAvg[i].doubleValue()) > acceptableDiff) {
				System.err.println("Too different: "+params[i].value+ " != "+ denseAvg[i].doubleValue());
			} else {
				System.out.println("  "+params[i].value+ " ~= "+ denseAvg[i].doubleValue());
			}
		}
	}
}
