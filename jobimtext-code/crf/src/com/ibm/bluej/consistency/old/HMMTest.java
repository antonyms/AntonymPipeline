/*
Copyright (c) 2012 IBM Corp. and Michael Glass

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

package com.ibm.bluej.consistency.old;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.inference.MultiMarginal;
import com.ibm.bluej.consistency.inference.SingleMAP;
import com.ibm.bluej.consistency.inference.SingleMarginal;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.consistency.validate.SGLog;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.FunST;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;
import com.ibm.bluej.util.common.RandomUtil;


public class HMMTest {
	//recency weighted average of numberOfFlips between deltas
	//avg_{t+1} = (1-\lambda)avg_t + numberOfFlipsBeforeDelta_t
	//avg_{t+1} = avg_t + \alpha(numberOfFlipsBeforeDelta_t - avg_t)
	//  \alpha starts 1/t (with t starting at 1)
	//  \alpha then decays to \minAlpha
	//  have a range of minAlpha
	
	//TODO: verify that internal state representation corresponds to state index in SGSearch
	//for every delta, show the difference in state
	
	//TODO: examine DVL step by step
	
	//TODO: how does straight EM do
	// how does straight clustering do?
	// clustering + DVL?
	
	private static boolean MARGINAL = false;
	private static double EPSILON_SOFT = 0.15;
	
	//CONSIDER: things to learn:
	//   epsilon_soft as a function of number of flips so far, recency weighted acceptance rate..
	//   number of positive deltas before greedy accept
	//   balance between word and class proposals
	
	//CONSIDER: during DVL evidence float
	//  there should be a different (obv) proposal distribution
	//  it should learn things based on a reward of finding a badDelta
	
	private static String[] pClasses = new String[] {"V", "D", "N"};
	private static String[] pWords = new String[] {"go", "work", "smile", "a", "an", "the", "cat", "bob", "dog"};
	
	//(pClasses U {BEGIN}) x (pClasses U {END})
	//pClasses x pWords
	//don't create a purely random matrix
	private static double[][] pClassT = new double[][] {
		//V  D   N   END
		{0, .4, .4, .2}, //V
		{0,  0,  1,  0}, //D
		{.4, 0, .4, .2}, //N
		{.3, .5, .2, 0}  //BEGIN
	};
	private static double[][] pC2W = new double[][] {
		//go work smile  a   an  the  cat  bob  dog
		{.3,   .3,  .3,  0,  0,   0,   0,   0,  .1}, //V
		{0,     0,   0, .3, .3,  .4,   0,   0,   0}, //D
		{0,    .2,  .1,  0,  0,   0,  .2,  .2,  .3}  //N
	};
	

	
	private static void toCDF(double[] dist) {
		double sum = 0;
		for (double p : dist) {
			sum += p;
		}
		double cumulative = 0;
		for (int i = 0; i < dist.length; ++i) {
			cumulative += dist[i];
			dist[i] = cumulative / sum;
		}
	}
	
	//for eval
	private static void updateConfusionMatrix(int[] gold, int[] output, double[][] confusion) {
		for (int i = 0; i < gold.length; ++i) {
			++confusion[output[i]][gold[i]];
			//System.out.print(pClasses[output[i]]+"/"+pClasses[gold[i]]+" ");
		}
		//System.out.println();
	}
	private static double maxAccuracy(double[][] confusion) {
		//just approximate by max
		double fullSum = 0;
		double maxSum = 0;
		for (int oi = 0; oi < confusion.length; ++oi) {
			double max = 0;
			for (int gi = 0; gi < confusion[oi].length; ++gi) {
				fullSum += confusion[oi][gi];
				max = Math.max(max, confusion[oi][gi]);
			}
			maxSum += max;
		}
		return maxSum/fullSum;
	}
	private static void updateConfusionMatrix(String[] goldClasses, Collection<ScanTerm> bestOut, 
			Map<String, Integer> classToNdx, double[][] confusion) 
	{
		//converts gold and output to int[] using classToNdx (also fills classToNdx)
		int[] gold = new int[goldClasses.length];
		int[] output = new int[gold.length];
		for (int i = 0; i < goldClasses.length; ++i) {
			Integer cndx = classToNdx.get(goldClasses[i]);
			gold[i] = cndx;
		}
		for (ScanTerm bt : bestOut) {
			if (bt.parts[0].equals(classAt)) {
				int position = (int)((NumberTerm)bt.parts[1]).value;
				String cl = ((StringTerm)bt.parts[2]).value;
				Integer cndx = classToNdx.get(cl);
				output[position] = cndx;
			}
		}
		updateConfusionMatrix(gold, output, confusion);
	}
	
	static {
		//convert transition matrices to Cumulative probability distributions
		for (double[] dist : pClassT) {
			toCDF(dist);
		}
		for (double[] dist : pC2W) {
			toCDF(dist);
		}
	}
	
	//generate words and classes
	private static Pair<ScanTerm[],String[]> generateWords() {
		//use pWords, pClasses and transition matrix to generate a random "sentence"
		ArrayList<String> wordSeq = new ArrayList<String>();
		ArrayList<String> classSeq = new ArrayList<String>();
		int currentClass = pClasses.length;
		while (true) {
			//select next class
			double crnd = Math.random();
			for (int i = 0; i < pClassT[currentClass].length; ++i) {
				if (crnd <= pClassT[currentClass][i]) {
					currentClass = i;
					break;
				}
			}
			if (currentClass == pClasses.length) {
				break;
			}
			//select word
			double wrnd = Math.random();
			for (int i = 0; i < pC2W[currentClass].length; ++i) {
				if (crnd <= pC2W[currentClass][i]) {
					wordSeq.add(pWords[i]);
					classSeq.add(pClasses[currentClass]);
					break;
				}
			}
		} 
		
		if (SGDebug.LOGGING) SGLog.fine(Lang.stringList(wordSeq, " "));
		
		//wordSeq to ScanTerm
		ScanTerm[] words = new ScanTerm[wordSeq.size()];
		for (int i = 0; i < words.length; ++i) {
			words[i] = new ScanTerm(new ATerm[] {
				wordAt, new NumberTerm(i), new StringTerm(wordSeq.get(i))	
			});
		}
		
		return new Pair<ScanTerm[], String[]>(words,
				classSeq.toArray(new String[classSeq.size()]));
	}
	
	private HMMTest(ScanTerm[] fixedWords, ScanTerm[] goldClasses) {
		this.fixedWords = fixedWords;
		this.goldClasses = goldClasses;
		this.currentWords = new ScanTerm[fixedWords.length];
		this.classes = new ScanTerm[fixedWords.length];
	}
	

	private static StringTerm classAt = new StringTerm("classAt");
	private static StringTerm wordAt = new StringTerm("wordAt");
	
	private ScanTerm[] fixedWords; 
	private ScanTerm[] currentWords;
	private ScanTerm[] goldClasses;
	private ScanTerm[] classes;
	
	private ScanTerm makeClassAt(int pos, int classNdx) {
		return new ScanTerm(new ATerm[] {
				classAt, 
				new NumberTerm(pos), 
				new StringTerm(pClasses[classNdx])});
	}
	
	private boolean classFlip(int whichWord) {
		//int whichWord = RandomUtil.randomInt(0, fixedWords.length);
		ScanTerm cl = classes[whichWord];
		if (cl != null) {
			nowFalse(cl);
		}
		ScanTerm ncl = null;
		do {
			ncl = makeClassAt(whichWord, RandomUtil.randomInt(0,pClasses.length));
		} while (ncl.equals(cl));
		nowTrue(ncl);

		//possible rollback
		//if MARGINAL is enabled then use metroHastings
		boolean changed;
		if (MARGINAL) {
			changed = SGSearch.metroHastings();
		} else {
			changed = SGSearch.epsilonSoft(EPSILON_SOFT);
		}
		if (changed) {
			classes[whichWord] = ncl;
		}
		return changed;
	}

	private void showState() {
		for (int i = 0; i < currentWords.length; ++i) {
			System.err.print(Lang.RPAD(classes[i] == null ? "null" : classes[i].parts[2].toString(), 15));
		}
		System.err.println();
		for (int i = 0; i < currentWords.length; ++i) {
			System.err.print(Lang.RPAD(currentWords[i] == null ? "null" : currentWords[i].parts[2].toString(), 15));
		}
		System.err.println();
		for (int i = 0; i < currentWords.length; ++i) {
			System.err.print(Lang.RPAD(fixedWords[i] == null ? "null" : fixedWords[i].parts[2].toString(), 15));
		}
		System.err.println();
	}
	
	private double nowFalse(ScanTerm t) {
		try {
			return SGSearch.nowFalse(t);
		} catch (Error e) {
			showState();
			throw e;
		}
	}
	
	private double nowTrue(ScanTerm t) {
		try {
			return SGSearch.nowTrue(t);
		} catch (Error e) {
			showState();
			throw e;
		}
	}
	
	private boolean simpleWordFlip(int whichWord) {
		ScanTerm cw = currentWords[whichWord];
		nowFalse(cw);
		ScanTerm nw = null;
		do {
			nw = new ScanTerm(new ATerm[] {
				wordAt, 
				new NumberTerm(whichWord), 
				new StringTerm(pWords[RandomUtil.randomInt(0,pWords.length)])});
		} while (cw.equals(nw));
		nowTrue(nw);
			
		//possible rollback
		if (SGSearch.epsilonSoft(EPSILON_SOFT)) {
			currentWords[whichWord] = nw;
			return true;
		} else {
			return false;
		}		
	}
	private void resetToActualWord(int whichWord) {
		if (fixedWords[whichWord].equals(currentWords[whichWord])) {
			return;
		}
		nowFalse(currentWords[whichWord]);
		nowTrue(fixedWords[whichWord]);
		currentWords[whichWord] = fixedWords[whichWord];
		SGSearch.commit();
	}
	
	private boolean wordFlip(ArrayList<Integer> changedWords, int maxChanges) {
		if (changedWords.size() >= maxChanges) {
			//we must restore a word change
			int changedNdx = RandomUtil.randomInt(0, changedWords.size());
			int toRestore = changedWords.get(changedNdx);
			changedWords.remove(changedNdx);
			nowFalse(currentWords[toRestore]);
			nowTrue(fixedWords[toRestore]);
			currentWords[toRestore] = fixedWords[toRestore];
			SGSearch.commit();
			return true;
			//we never roll this back
		} else {		
			int whichWord = RandomUtil.randomInt(0, fixedWords.length);
			ScanTerm cw = currentWords[whichWord];
			nowFalse(cw);
			ScanTerm nw = null;
			do {
				nw = new ScanTerm(new ATerm[] {
					wordAt, 
					new NumberTerm(whichWord), 
					new StringTerm(pWords[RandomUtil.randomInt(0,pWords.length)])});
			} while (cw.equals(nw));
			nowTrue(nw);
				
			//possible rollback
			if (SGSearch.epsilonSoft(EPSILON_SOFT)) {
				currentWords[whichWord] = nw;
				changedWords.add(whichWord);
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	private void initialize() {
		SGSearch.setTransaction(false); //always accept the initialization
		//first fix the currentWords to be the fixedWords
		for (int i = 0; i < fixedWords.length; ++i) {
			if (currentWords[i] == null) {
				nowTrue(fixedWords[i]);
			} else if (!currentWords[i].equals(fixedWords[i])) {
				nowFalse(currentWords[i]);
				nowTrue(fixedWords[i]);
			}
			currentWords[i] = fixedWords[i];
			if (goldClasses == null) {
				while (classes[i] == null) {
					classFlip(i);
				}
			} else {
				if (classes[i] != null) {
					nowFalse(classes[i]);
				}
				nowTrue(goldClasses[i]);
				classes[i] = goldClasses[i];
			}
		}
		//SGSearch.checkBest(); //really don't need this here
		SGSearch.setTransaction(true);
	}
	
	private void metroHastings(int numFlips) {
		initialize();
		MARGINAL = true;
		//propose all kinds of different class assignments
		for (int fi = 0; fi < numFlips; ++fi) {
			classFlip(RandomUtil.randomInt(0, fixedWords.length));
		}
	}
	
	// we call this after SampleRank proceeds for a while
	// because our bestClass assignments will change
	// alternatively, you can use straight up SampleRank with the proposal restricted close to data
	// implement both
	private Collection<ScanTerm> searchToBest(int numFlips) {
		initialize();
		SingleMAP map = new SingleMAP(SGSearch.getCRFState());
		SGSearch.setMAP(map);
		
		//propose all kinds of different class assignments
		for (int fi = 0; fi < numFlips; ++fi) {
			classFlip(RandomUtil.randomInt(0, fixedWords.length));
		}
		//reloadBest is wrong...
		//  we lose track of the our state
		//  should use clear(), then getBest()
		//  then assert the best in our representation and the SGSearch
		SGSearch.newProblem();
		Collection<ScanTerm> best = map.getBest();
		for (ScanTerm t : best) {
			SGSearch.nowTrue(t);
			if (t.parts[0] == wordAt) {
				currentWords[(int)((NumberTerm)t.parts[1]).value] = t;
			} else if (t.parts[0] == classAt) {
				classes[(int)((NumberTerm)t.parts[1]).value] = t;
			} else {
				throw new Error("Odd term in best terms: "+t);
			}
			//TODO: verify that there are no nulls
		}
		return best;
	}
	
	private void searchVariationsFromMAP(int numFlips, int fidelity) {
		//clear fixedMAP SGWeight
		ArrayList<Integer> changedWords = new ArrayList<Integer>();
		//FIXME: SGSearch.nextDelta(-changedWords.size());
		for (int fi = 0; fi < numFlips; ++fi) {
			boolean changed = false;
			if (Math.random() > 0.5) {
				changed = classFlip(RandomUtil.randomInt(0, fixedWords.length));
			} else {
				changed = wordFlip(changedWords,fidelity);
			}
			//FIXME: SGSearch.nextDelta(-changedWords.size());
		    //TODO: break if update occurred
		}
		
	}
	
	private static void unsupervisedLearning(String filename, int numSamples, int numFlips, int fidelity) {
		for (int si = 0; si < numSamples; ++si) {
			if (si > 0 && si % 100 == 0) {
				System.out.println("On sample: "+si);
			}
			HMMTest hmm = new HMMTest(generateWords().first, null);
			SGSearch.newProblem();
			hmm.unsupervisedSampleRank(numFlips, fidelity);
		}
		//save avg parameters
		SGSearch.setParametersToAverage();
		FileUtil.writeFileAsString(filename, SGSearch.stringParameters());
	}
	
	private void unsupervisedSampleRankOld(int numFlips, int fidelity) {	
		initialize();
		ArrayList<Integer> changedWords = new ArrayList<Integer>();
		//FIXME: SGSearch.nextDelta(-changedWords.size());
		for (int fi = 0; fi < numFlips; ++fi) {
			boolean changed = false;
			if (changedWords.size() > 0 && Math.random() > 0.05 || Math.random() > 0.5) {
				if (changedWords.size() > 0 && Math.random() > 0.2) {
					changed = classFlip(RandomUtil.randomMember(changedWords));
				} else {
					changed = classFlip(RandomUtil.randomInt(0, fixedWords.length));
				}
			} else {
				changed = wordFlip(changedWords,fidelity);
			}
			//if (changed) {
			//FIXME: SGSearch.nextDelta(-changedWords.size());
			//}
		}
	}
	
	private double sampleRankFreqUnsupervisedEval(int numFlips) {
		double badDeltaCount = 0;
		initialize();
		double prevGoldScore = 0;
		double prevWeight;
		for (int wi = 0; wi < fixedWords.length; ++wi) {
			for (int fi = 0; fi < numFlips; ++fi) {
				prevWeight = SGSearch.getWeight();
				if (Math.random() > 0.5) {
					classFlip(RandomUtil.randomInt(0, fixedWords.length));
					/*
					if (Math.random() > 0.7) {
						int tof = wi;
						if (Math.random() > 0.5) {
							tof = wi + RandomUtil.randomInt(-1,2);
						}
						classFlip(Math.min(Math.max(0, tof),fixedWords.length-1));
					} else {
						
					}
					*/
				} else {
					simpleWordFlip(wi);
					double nextGoldScore = (currentWords[wi] == fixedWords[wi]) ? 0 : -1;
					if (nextGoldScore != prevGoldScore) {
						if (nextGoldScore > prevGoldScore != SGSearch.getWeight() > prevWeight) {
							++badDeltaCount;
						}
					}
					prevGoldScore = nextGoldScore;
				}
			}
			resetToActualWord(wi);
		}
		return badDeltaCount/(fixedWords.length * numFlips);		
	}
	
	private double unsupervisedEval(int numFlips) {
		MARGINAL = true;
		double score = 0;
		initialize();
		for (int wi = 0; wi < fixedWords.length; ++wi) {
			//setMarginal
			SingleMarginal qd = new SingleMarginal(fixedWords[wi]);
			SGSearch.setMarginal(qd);
			for (int fi = 0; fi < numFlips; ++fi) {
				if (Math.random() > 0.5) {
					classFlip(RandomUtil.randomInt(0, fixedWords.length));
					/*
					if (Math.random() > 0.7) {
						int tof = wi;
						if (Math.random() > 0.5) {
							tof = wi + RandomUtil.randomInt(-1,2);
						}
						classFlip(Math.min(Math.max(0, tof),fixedWords.length-1));
					} else {
						
					}
					*/
				} else {
					simpleWordFlip(wi);
				}
			}
			//get prob, put in log space
			score += qd.getProb();//Math.log(qd.getProb());
			resetToActualWord(wi);
		}
		return score/fixedWords.length;
	}
	
	private void unsupervisedSampleRank(int numFlips, int fidelity) {	
		initialize();
		//FIXME: SGSearch.nextDelta(0);
		for (int wi = 0; wi < fixedWords.length; ++wi) {		
			for (int fi = 0; fi < numFlips; ++fi) {
				boolean changed = false;
				if (Math.random() > 0.5) {
					changed = classFlip(RandomUtil.randomInt(0, fixedWords.length));
					if (Math.random() > 0) {
						int tof = wi;
						if (Math.random() > 0.5) {
							tof = wi + RandomUtil.randomInt(-1,2);
						}
						changed = classFlip(Math.min(Math.max(0, tof),fixedWords.length-1));
					} else {
						changed = classFlip(RandomUtil.randomInt(0, fixedWords.length));
					}
				} else {
					changed = simpleWordFlip(wi);
				}
				//if (changed) {
				//FIXME: SGSearch.nextDelta(fixedWords[wi].equals(currentWords[wi]) ? 0 : -1);
				//}
			}
			resetToActualWord(wi);
			//FIXME: SGSearch.nextDelta(0);
		}
		
	}
	
	private void supervisedSampleRank(int numFlips) {	
		initialize();
		HashSet<Integer> changedClasses = new HashSet<Integer>();
		//FIXME: SGSearch.nextDelta(-changedClasses.size());
		for (int fi = 0; fi < numFlips; ++fi) {	
			int whichWord = RandomUtil.randomInt(0, fixedWords.length);
			classFlip(whichWord);
			if (classes[whichWord].equals(goldClasses[whichWord])) {
				changedClasses.remove(whichWord);
			} else {
				changedClasses.add(whichWord);
			}
			//FIXME: SGSearch.nextDelta(-changedClasses.size());
		}	
	}
	
	private void supervisedJointSampleRank(int numFlips) {	
		initialize();
		HashSet<Integer> changedClasses = new HashSet<Integer>();
		ArrayList<Integer> changedWords = new ArrayList<Integer>();
		//FIXME: SGSearch.nextDelta(0);
		for (int fi = 0; fi < numFlips; ++fi) {	
			
			if (changedWords.size() == 0 || changedClasses.size() != 0) {
				int whichWord = RandomUtil.randomInt(0, fixedWords.length);
				classFlip(whichWord);
				if (classes[whichWord].equals(goldClasses[whichWord])) {
					changedClasses.remove(whichWord);
				} else {
					changedClasses.add(whichWord);
				}
			} else {
				wordFlip(changedWords, 1);
			}
			//FIXME: SGSearch.nextDelta(-changedClasses.size() - changedWords.size());
		}	
	}
	
	private static void supervisedJointLearning(String filename, int numSamples, int numFlips) {
		//for each sampled sentence
		for (int si = 0; si < numSamples; ++si) {
			if (si % 100 == 0) {
				System.out.println("On training example: "+si);
			}
			Pair<ScanTerm[], String[]> gen = generateWords();
			//ScanTerms from goldClasses
			ScanTerm[] goldClasses = new ScanTerm[gen.second.length];
			for (int i = 0; i < goldClasses.length; ++i) {
				goldClasses[i] = new ScanTerm(new ATerm[] {classAt, new NumberTerm(i), new StringTerm(gen.second[i])});
			}
			HMMTest hmm = new HMMTest(gen.first, goldClasses);
			SGSearch.newProblem(); //do we need this and hmm.clear()?
			hmm.supervisedJointSampleRank(numFlips);
		}
		//save avg parameters
		SGSearch.setParametersToAverage();
		FileUtil.writeFileAsString(filename, SGSearch.stringParameters());
	}
	
	private static void supervisedLearning(String filename, int numSamples, int numFlips) {
		//for each sampled sentence
		for (int si = 0; si < numSamples; ++si) {
			if (si % 100 == 0) {
				System.out.println("On training example: "+si);
			}
			Pair<ScanTerm[], String[]> gen = generateWords();
			//ScanTerms from goldClasses
			ScanTerm[] goldClasses = new ScanTerm[gen.second.length];
			for (int i = 0; i < goldClasses.length; ++i) {
				goldClasses[i] = new ScanTerm(new ATerm[] {classAt, new NumberTerm(i), new StringTerm(gen.second[i])});
			}
			HMMTest hmm = new HMMTest(gen.first, goldClasses);
			SGSearch.newProblem(); //do we need this and hmm.clear()?
			hmm.supervisedSampleRank(numFlips);
		}
		//save avg parameters
		SGSearch.setParametersToAverage();
		FileUtil.writeFileAsString(filename, SGSearch.stringParameters());
	}
	
	private static void unsupervisedLearningMAP(String filename, int numSamples, int numVariations, int fidelity) {
		int numFlips = 1000;
		//for each sampled sentence
		for (int si = 0; si < numSamples; ++si) {
			if (si % 100 == 0) {
				System.out.println("On training example: "+si);
			}
			HMMTest hmm = new HMMTest(generateWords().first, null);
			SGSearch.newProblem(); 
			
			//try finding a badDelta variation (10 attempts)
			for (int vi = 0; vi < numVariations; ++vi) {
				//try your best to find the MAP
				if (vi == 0) {
					hmm.searchToBest(numFlips);
				} else {
					hmm.searchToBest(numFlips/10);
				}
				//try to find variation
				hmm.searchVariationsFromMAP(numFlips,fidelity);
			}
		}
		//save avg parameters
		SGSearch.setParametersToAverage();
		FileUtil.writeFileAsString(filename, SGSearch.stringParameters());
	}

	
	private static Map<String,Integer> getClassToNdx() {
		HashMap<String,Integer> classToNdx = new HashMap<String,Integer>();
		for (int i = 0; i < pClasses.length; ++i) {
			classToNdx.put(pClasses[i],i);
		}
		return classToNdx;
	}
	
	private static void evalUnsup(String savedParamsFile, int numSamples, int numFlips) {
		
		System.out.println(SGSearch.getLearningState().allToString());
		double score = 0;
		for (int si = 0; si < numSamples; ++si) {
			HMMTest hmm = new HMMTest(generateWords().first, null);
			SGSearch.newProblem();
			//score += hmm.unsupervisedEval(numFlips);
			score += hmm.sampleRankFreqUnsupervisedEval(numFlips);
		}
		System.out.println(score/numSamples);
	}
	
	private static void evalMAP(String savedParamsFile, int numSamples, int numFlips) {
		
		System.out.println(SGSearch.getLearningState().allToString());
		Map<String, Integer> classToNdx = getClassToNdx();
		double[][] confusion = new double[pClasses.length][pClasses.length];
		for (int si = 0; si < numSamples; ++si) {
			Pair<ScanTerm[], String[]> gen = generateWords();
			HMMTest hmm = new HMMTest(gen.first, null);
			SGSearch.newProblem();
			updateConfusionMatrix(gen.second, hmm.searchToBest(numFlips), classToNdx, confusion);
		}
		printAccuracy(confusion, savedParamsFile);
	}
	
	private static Collection<ScanTerm> getBestMarginal(MultiMarginal marginal) {
		Collection<Pair<ScanTerm,Double>> qp = marginal.getProbs();
		HashMap<Integer, Pair<ScanTerm, Double>> bestClasses = new HashMap<Integer, Pair<ScanTerm,Double>>();
		for (Pair<ScanTerm,Double> p : qp) {
			Integer ndx = (int)((NumberTerm)p.first.parts[1]).value;
			Pair<ScanTerm,Double> curBest = bestClasses.get(ndx);
			if (curBest == null || curBest.second < p.second) {
				bestClasses.put(ndx,p);
			}
		}
		Collection<ScanTerm> best = new ArrayList<ScanTerm>();
		for (Pair<ScanTerm,Double> p : bestClasses.values()) {
			best.add(p.first);
		}
		return best;
	}
	
	private void exactMarginal() {
		//initialize classes to pClasses[0]
		SGSearch.setTransaction(false);
		for (int wi = 0; wi < fixedWords.length; ++wi) {
			currentWords[wi] = fixedWords[wi];
			SGSearch.nowTrue(fixedWords[wi]);
			classes[wi] = makeClassAt(wi,0);
			SGSearch.nowTrue(classes[wi]);
		}
		double Z = Math.exp(SGSearch.getWeight());
		
		//compute partition, then fix the correct class and sum over remaining 
		int numConfigs = 1;
		for (int wi = 0; wi < fixedWords.length; ++wi) {
			numConfigs *= pClasses.length;
		}	
		for (int config = 1; config < numConfigs; ++config) {
			int remainingConfig = config;
			for (int wi = 0; wi < fixedWords.length; ++wi) {
				int pcNdx = remainingConfig % pClasses.length;		
				//set classes[wi] to pClasses[pcNdx]
				ScanTerm nc = makeClassAt(wi, pcNdx);
				if (!nc.equals(classes[wi])) {
					SGSearch.nowFalse(classes[wi]);
					classes[wi] = nc;
					SGSearch.nowTrue(classes[wi]);
				}
				remainingConfig = remainingConfig / pClasses.length;	
			}
			Z += Math.exp(SGSearch.getWeight());
		}
		
		double[] probs = new double[fixedWords.length];
		for (int wi = 0; wi < fixedWords.length; ++wi) {
			//set classes[wi] to goldClasses[wi]
			SGSearch.nowFalse(classes[wi]);
			SGSearch.nowTrue(goldClasses[wi]);
			classes[wi] = goldClasses[wi];
			//set others to pClasses[0]
			for (int owi = 0; owi < fixedWords.length; ++owi) {
				if (wi == owi) {
					continue;
				}
				SGSearch.nowFalse(classes[owi]);
				classes[owi] = makeClassAt(owi, 0);
				SGSearch.nowTrue(classes[owi]);
			}
			double num = Math.exp(SGSearch.getWeight());
			//sum over other configs
			numConfigs = 1;
			for (int owi = 0; owi < fixedWords.length; ++owi) {
				if (wi == owi) {
					continue;
				}
				numConfigs *= pClasses.length;
			}
			for (int config = 1; config < numConfigs; ++config) {
				int remainingConfig = config;
				for (int owi = 0; owi < fixedWords.length; ++owi) {
					if (owi == wi) {
						continue;
					}
					int pcNdx = remainingConfig % pClasses.length;		
					//set classes[wi] to pClasses[pcNdx]
					ScanTerm nc = makeClassAt(owi, pcNdx);
					if (!nc.equals(classes[owi])) {
						SGSearch.nowFalse(classes[owi]);
						classes[owi] = nc;
						SGSearch.nowTrue(classes[owi]);
					}
					remainingConfig = remainingConfig / pClasses.length;	
				}
				num += Math.exp(SGSearch.getWeight());
			}
			probs[wi] = num / Z;
		}
		
		//compare metro hastings sampling
		//TODO: burn-in
		MultiMarginal marginal = new MultiMarginal(new FunST<ScanTerm,Boolean>() {
			public Boolean f(ScanTerm o) {
				return o.parts.length == 3 && o.parts[0] == classAt;
			}	
		});
		SGSearch.setMarginal(marginal);
		metroHastings(1000); 
		
		double[] mhProbs = new double[fixedWords.length];
		double[][] allMHProbs = new double[fixedWords.length][pClasses.length];
		Collection<Pair<ScanTerm,Double>> qp = marginal.getProbs();
		for (int i = 0; i < fixedWords.length; ++i) {
			System.out.print(Lang.LPAD(goldClasses[i].parts[2].toString(), 8));
		}
		System.out.println();
		for (int i = 0; i < fixedWords.length; ++i) {
			System.out.print(Lang.LPAD(fixedWords[i].parts[2].toString(), 8));
		}
		System.out.println();
		for (Pair<ScanTerm,Double> p : qp) {
			int wi = (int)ATerm.getDouble(p.first.parts[1]);
			if (p.first.equals(goldClasses[wi])) {
				mhProbs[wi] = p.second;
			}
			for (int ci = 0; ci < pClasses.length; ++ci) {
				if (pClasses[ci].equals(ATerm.getString(p.first.parts[2]))) {
					allMHProbs[wi][ci] = p.second;
				}
			}
		}
		
		//TODO: find average percent difference
		for (int i = 0; i < fixedWords.length; ++i) {
			System.out.print(Lang.LPAD(""+probs[i], 5)+" ~ "+Lang.LPAD(""+mhProbs[i], 5)+"  [");
			for (int ci = 0; ci < pClasses.length; ++ci) {
				System.out.print(" "+allMHProbs[i][ci]);
			}
			System.out.println(" ]");
		}
	}

	private static void testMarginal(String savedParamsFile, int numSamples) {
		
		System.out.println(SGSearch.getLearningState().allToString());
		for (int si = 0; si < numSamples; ++si) {
			Pair<ScanTerm[], String[]> gen = generateWords();
			if (gen.first.length > 8) {
				--si;
				continue;
			}
			//ScanTerms from goldClasses
			ScanTerm[] goldClasses = new ScanTerm[gen.second.length];
			for (int i = 0; i < goldClasses.length; ++i) {
				goldClasses[i] = new ScanTerm(new ATerm[] {classAt, new NumberTerm(i), new StringTerm(gen.second[i])});
			}
			HMMTest hmm = new HMMTest(gen.first, goldClasses);
			SGSearch.newProblem(); 
			hmm.exactMarginal();
		}
	}
	
	private static void evalMarginal(String savedParamsFile, int numSamples, int numFlips) {
		
		System.out.println(SGSearch.getLearningState().allToString());
		Map<String, Integer> classToNdx = getClassToNdx();
		double[][] confusion = new double[pClasses.length][pClasses.length];
		for (int si = 0; si < numSamples; ++si) {
			Pair<ScanTerm[], String[]> gen = generateWords();
			HMMTest hmm = new HMMTest(gen.first, null);
			SGSearch.newProblem();
			MultiMarginal marginal = new MultiMarginal(new FunST<ScanTerm,Boolean>() {
				public Boolean f(ScanTerm o) {
					return o.parts.length == 3 && o.parts[0] == classAt;
				}	
			});
			SGSearch.setMarginal(marginal);
			hmm.metroHastings(numFlips);
			updateConfusionMatrix(gen.second, getBestMarginal(marginal), classToNdx, confusion);
		}
		printAccuracy(confusion, savedParamsFile);		
	}
	
	private static void printAccuracy(double[][] confusion, String paramFilename) {
		System.out.print(Lang.LPAD("",10));
		for (int i = 0; i < confusion.length; ++i) {
			System.out.print(Lang.LPAD(pClasses[i],10));
		}
		System.out.println();
		for (int i = 0; i < confusion.length; ++i) {
			System.out.print(Lang.LPAD(pClasses[i],10));
			for (int j = 0; j < confusion[i].length; ++j) {
				System.out.print(Lang.LPAD((int)confusion[i][j]+"", 10));
			}
			System.out.println();
		}
		System.out.println("Accuracy for "+paramFilename+" "+maxAccuracy(confusion));		
	}
	
	private static void testGen() {
		for (int i = 0; i < 40; ++i) {
			Pair<ScanTerm[], String[]> gen = generateWords();
			for (String s : gen.second) {
				System.out.print(Lang.LPAD(s,7));
			}
			System.out.println();
			for (ScanTerm s : gen.first) {
				System.out.print(Lang.LPAD(s.parts[2].toString(),7));
			}
			System.out.println("\n");
		}
	}
	
	//Another possible unsupervised eval: the weights of the MAP for each possible completion
	//  find the weight of the MAP for the gold completion
	//  then find the weights for the k-Best MAP
	
	//use traditional supervised SampleRank to check that things work
	//   can still try linguistics and likelihood
	//   make a simpler DVL?
	public static void main(String[] args) {
		String savedParamsFilename = "HMM-super.param";
		SGSearch.loadDescription(FileUtil.readResourceAsString("HMM.con"));
		//Lang.readln();
		long t = System.currentTimeMillis();
		//SGSearch.setLogLevel(Level.INFO);
		//Lang.readln(); //pause for profiling
		//testGen();
		
		//ParamWeight.save("baseline.param");
		//eval("baseline.param", 1000);
		
		//SGWeight.FIXED_SCALE = 0;
		//unsupervisedLearning("HMM-DVL.param", 10000, 1, 1);
		
		//TODO: generate learning curve for different fidelities
		//performance may depend a lot on the first symmetry breaking example...
		
		//looks like a fidelity of 2 or 3 is much better than 1 and a good bit better than unlimited
		//you can explore different fidelities by checking deltaRate, look for the one that makes it improve the fastest
		//consider not only deltaRates but delta sizes...
		//  we want the actual data to be relatively likely
		
		//unsupervisedLearning("HMM-un1.param", 1000, 100, 1);
		//evalMAP("HMM-un1.param", 100, 1000);
		//evalUnsup("HMM-un1.param", 100, 1000); //8.04E-4
		
		//supervisedLearning("HMM-super.param", 1000, 1000);
		//evalMAP("HMM-super.param", 100, 1000); //map beats marginal, perhaps because the acceptance function was esoft in training?
		SGSearch.loadParamters(FileUtil.readFileAsString(savedParamsFilename));
		evalMarginal(savedParamsFilename, 100, 5000);
		//evalUnsup("HMM-super.param", 100, 1000); //0.158, 7.32E-4
		//supervisedJointLearning("HMM-superJoint.param", 1000, 1000);
		//evalUnsup("HMM-superJoint.param", 100, 1000); //0.157, 7.61E-4
		//On unsupervised eval un1 beats super?!
		// yes, because un1 knows what the most likely words are
		
		//testMarginal("HMM-super.param", 10);
		
		//  logging and debugging for SGWeight
		//ParamWeight.load("HMM-DVL.param");
		//ParamWeight.print();
		System.out.println("Runtime = "+(System.currentTimeMillis()-t)/1000.0);
	}
}
