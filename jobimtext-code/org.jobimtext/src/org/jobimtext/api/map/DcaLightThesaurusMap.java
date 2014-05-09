package org.jobimtext.api.map;
/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pig.impl.util.Pair;
import org.jobimtext.api.configuration.DcaLightThesaurusConfiguration;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.ibm.sai.distributional_similarity.api.data.DSDictionary;
import com.ibm.sai.distributional_similarity.api.data.SenseTableReader;
import com.ibm.sai.distributional_similarity.api.data.SimTableReader;
import com.ibm.sai.distributional_similarity.api.data.TableReader;

public class DcaLightThesaurusMap implements IThesaurusMap<String, String> {
	private DSDictionary dict;
	private IntObjectOpenHashMap<IntArrayList> thesaurus;
	private IntObjectOpenHashMap<IntArrayList> context;
	private IntObjectOpenHashMap<IntArrayList> senses;
	private String thesaurusPath;
	private String contextPath;
	private String sensesPath;
	private int maxEntries;

	public DcaLightThesaurusMap(String thesaurusPath, String contextPath,
			String sensesPath, int maxEntries) {
		super();
		this.thesaurusPath = thesaurusPath;
		this.contextPath = contextPath;
		this.sensesPath = sensesPath;
		this.maxEntries = maxEntries;
	}

	public DcaLightThesaurusMap(DcaLightThesaurusConfiguration config) {
		this(config.tables.getTableSimilarTerms(), config.tables
				.getTableTermContextsScore(), config.tables.getTableSenses(),
				config.max);

	}

	public DcaLightThesaurusMap(String modelConfig) {

		try {
			DcaLightThesaurusConfiguration conf = DcaLightThesaurusConfiguration
					.getFromXmlFile(modelConfig);
			thesaurusPath = conf.tables.getTableSimilarTerms();
			contextPath = conf.tables.getTableTermContextsScore();
			sensesPath = conf.tables.getTableSenses();
			this.maxEntries = conf.max;

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public boolean connect() {

		dict = new DSDictionary();
		thesaurus = new IntObjectOpenHashMap<IntArrayList>();
		context = new IntObjectOpenHashMap<IntArrayList>();
		senses = new IntObjectOpenHashMap<IntArrayList>();
		if (sensesPath != null && sensesPath.length() > 0) {
			TableReader senseReader = new SenseTableReader();
			senseReader.loadMapFromFileOrDir(sensesPath, dict, senses,
					maxEntries, "*");
		}
		if (thesaurusPath != null && thesaurusPath.length() > 0) {
			TableReader simReader = new SimTableReader();
			simReader.loadMapFromFileOrDir(thesaurusPath, dict, thesaurus,
					maxEntries, "*");
		}
		if (contextPath != null && contextPath.length() > 0) {
			TableReader conReader = new SimTableReader();
			conReader.loadMapFromFileOrDir(contextPath, dict, context,
					maxEntries, "*");
		}

		return true;
	}

	@Override
	public void destroy() {
		context = null;
		senses = null;
		dict = null;
		thesaurus = null;

	}

	@Override
	public Map<String, Double> getSimilarTerms(String key) {
		int index = dict.getIndex(key);
		if ( index < 0 )
			return new HashMap<String, Double>();;
			
		IntArrayList list = thesaurus.get(index);
		
		if ( list == null )
			return new HashMap<String, Double>();
		
		return intList2Map(list);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, int numberOfEntries) {
		
		Map<String, Double> result = getSimilarTerms(key);
				
		if ( result.size() <= numberOfEntries )
			return result;
		
		return removeTopN(result, numberOfEntries);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, double threshold) {
		return removeBelowThreshold(getSimilarTerms(key), threshold);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values) {
		int index = dict.getIndex(values);
		if ( index < 0 )
			return new HashMap<String, Double>();;
		
		IntArrayList list = context.get(index);
		return intList2Map(list);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values,
			int numberOfEntries) {
		return removeTopN(getSimilarContexts(values), numberOfEntries);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values,
			double threshold) {
		return removeBelowThreshold(getSimilarContexts(values), threshold);
	}

	@Override
	public Long getTermCount(String key) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Long getContextsCount(String key) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Long getTermContextsCount(String key, String val) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("getSenseCUIs is not supported");
	}

	@Override
	public Double getTermContextsScore(String key, String val) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key) {
		int index = dict.getIndex(key);
		if ( index < 0 )
			return new HashMap<String, Double>();;
		
		IntArrayList list = context.get(index);
		return intList2Map(list);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key,
			int numberOfEntries) {
		return removeTopN(getTermContextsScores(key), numberOfEntries);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key,
			double threshold) {
		return removeBelowThreshold(getTermContextsScores(key), threshold);
	}

	protected static Map<String, Double> removeBelowThreshold(Map<String, Double> similarTerms,
			double threshold) {
		ArrayList<String> keys = new ArrayList<String>(similarTerms.keySet());
		
		for (String k : keys) {
			if (similarTerms.get(k) < threshold) {
				similarTerms.remove(k);
			}
		}
		
		return similarTerms;
	}

	protected static Map<String, Double> removeTopN(Map<String, Double> similarTerms, int N) {
		List<Pair<String, Double>> list = new ArrayList<Pair<String, Double>>();
		for (Entry<String, Double> e : similarTerms.entrySet()) {
			list.add(new Pair<String, Double>(e.getKey(), e.getValue()));
		}
		Collections.sort(list, new Comparator<Pair<String, Double>>() {

			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				return o2.second.compareTo(o1.second);
			}
		});
		for (int i = N; i < list.size(); i++) {
			similarTerms.remove(list.get(i).first);
		}
		return similarTerms;
	}

	@Override
	public Map<Integer, List<String>> getSenses(String key) {
		Map<Integer, List<String>> value = new HashMap<Integer, List<String>>();
		int index = dict.getIndex(key);
		if ( index < 0 )
			return new HashMap<Integer, List<String>>();;
		
		IntArrayList list = senses.get(index);
		if (list != null) {
			int[] buffer = list.buffer;
			int size = list.size();
			for (int i = 0; i < size; i++) {
				// value.add("--"+Integer.toString(buffer[i])+"--");
				List<String> l = new ArrayList<String>();
				value.put(i, l);
				i++;
				while (i < size && buffer[i] >= 0) {
					String term = dict.getTerm(buffer[i]);
					l.add(term);
					i++;
				}
			}
		}
		return value;

	}

	@Override
	public Map<Integer, Map<String, Double>> getIsas(String key) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Map<Integer, Map<String, Double>> getSenseCUIs(String key) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("This method is not supported");
	}

	private Map<String, Double> intList2Map(IntArrayList list) {
		Map<String, Double> value = new HashMap<String, Double>();
		if (list != null) {
			int[] buffer = list.buffer;
			int size = list.size();
			for (int i = 0; i < size; i += 2) {
				String term = dict.getTerm(buffer[i]);
				int score = buffer[i + 1];
				value.put(term, (double) score);
			}
		}
		return value;
	}

	
	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		Map<String, Double> t1s = getSimilarTerms(t1);
		if (t1s == null)
			return 0.0;
		if (t1s.containsKey(t2))
			return t1s.get(t2);
		return 0.0;
	}
}
