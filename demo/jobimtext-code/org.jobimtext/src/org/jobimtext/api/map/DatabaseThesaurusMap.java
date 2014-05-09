package org.jobimtext.api.map;
/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and
* 
* FG Sprachtechnologie / Language Technology
* Technische Universitaet Darmstadt
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
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jobimtext.api.configuration.DatabaseThesaurusConfiguration;
import org.jobimtext.api.db.DatabaseResource;
import org.jobimtext.api.db.DatabaseThesaurus;


public class DatabaseThesaurusMap extends DatabaseResource implements
		IThesaurusMap<String, String> {

	DatabaseThesaurus dbThesaurus= new DatabaseThesaurus();

	public DatabaseThesaurusMap(File dbConfigurationFile) {
		dbThesaurus.setDbConfigurationFile(dbConfigurationFile);
	}

	public DatabaseThesaurusMap(DatabaseThesaurusConfiguration conf) {
		dbThesaurus.setDbConf(conf);
	}

	public DatabaseThesaurusMap(String dbConfigurationFile) {
		this(new File(dbConfigurationFile));
	}

	public DatabaseThesaurusMap(File dbConfigurationFile,
			File tunnelConfigurationFile) {
		dbThesaurus.setDbConfigurationFile(dbConfigurationFile);
		dbThesaurus.setTunnelConfigurationFile(tunnelConfigurationFile);
	}

	public DatabaseThesaurusMap() {

	}

	@Override
	public boolean connect() {
		boolean value = dbThesaurus.connect();
		return value;
	}

	@Override
	public void destroy() {
		super.destroy();
		dbThesaurus.destroy();
	}

	@Override
	public Long getTermCount(String key) {
		return dbThesaurus.getTermCount(key);
	}

	@Override
	public Long getContextsCount(String key) {
		return dbThesaurus.getContextsCount(key);
	}

	@Override
	public Long getTermContextsCount(String key, String val) {
		return dbThesaurus.getTermContextsCount(key, val);
	}

	@Override
	public Double getTermContextsScore(String key, String val) {
		return dbThesaurus.getTermContextsScore(key, val);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key) {
		ResultSet set = dbThesaurus.getSimilarTerms(key);

		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, int numberOfEntries) {
		ResultSet set = dbThesaurus.getSimilarTerms(key, numberOfEntries);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, double threshold) {
		ResultSet set = dbThesaurus.getSimilarTerms(key, threshold);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values) {
		ResultSet set = dbThesaurus.getSimilarContexts(values);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values,
			int numberOfEntries) {
		ResultSet set = dbThesaurus.getSimilarContexts(values, numberOfEntries);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String values, double threshold) {
		ResultSet set = dbThesaurus.getSimilarContexts(values, threshold);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key) {
		ResultSet set = dbThesaurus.getTermContextsScores(key);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key,
			int numberOfEntries) {
		ResultSet set = dbThesaurus.getTermContextsScores(key, numberOfEntries);
		return getStringDoubleMap(set);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key, double threshold) {
		ResultSet set = dbThesaurus.getTermContextsScores(key, threshold);

		return getStringDoubleMap(set);
	}

	public Map<String, Double> getStringDoubleMap(ResultSet set) {
		Map<String, Double> map = new HashMap<String, Double>();
		if (set == null)
			return map;
		try {
			while (set.next()) {
				
				map.put(set.getString(1), Double.parseDouble(set.getString(2)));
			}
			set.getStatement().close();
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map<Integer, List<String>> getSenses(String key) {
		Map<Integer, List<String>> results = new HashMap<Integer, List<String>>();

		ResultSet result = dbThesaurus.getSenses(key);
		if(result==null)return new HashMap<Integer, List<String>>();
		try {
			while (result.next()) {
				if (!results.containsKey(result.getInt(1)))
					results.put(result.getInt(1), new ArrayList<String>());
				results.get(result.getInt(1)).add(result.getString(2));
			}

			result.getStatement().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public Map<Integer, Map<String, Double>> getIsas(String key) {
		Map<Integer, Map<String, Double>> clusterIsas = new HashMap<Integer, Map<String, Double>>();
		ResultSet set = dbThesaurus.getIsas(key);
		try {
			while (set.next()) {
				int cid = set.getInt(1);
				String isaStr = set.getString(2);
				if (isaStr != null) {
					Map<String, Double> isaMap = clusterIsas.get(cid);
					if (isaMap == null) {
						isaMap = new HashMap<String, Double>();
						clusterIsas.put(cid, isaMap);
					}
					for (String isaPart : isaStr.split("##")) {
						int colon = isaPart.lastIndexOf(':');
						String w = isaPart.substring(0, colon);
						double score = Double.parseDouble(isaPart
								.substring(colon + 1));
						isaMap.put(w, score);
					}
				}
			}
			set.getStatement().close();
			
		} catch (SQLException e) {
			throw new Error(e);
		}
		return clusterIsas;

	}

	@Override
	public Map<Integer, Map<String, Double>> getSenseCUIs(String key) {
		ResultSet set = dbThesaurus.getSenseCUIs(key);
		Map<Integer, Map<String, Double>> clusterCUIs = new HashMap<Integer, Map<String, Double>>();
		try {
			while (set.next()) {
				int cid = set.getInt(1);
				String cuiScores = set.getString(2);
				if (cuiScores != null) {
					Map<String, Double> cuiMap = clusterCUIs.get(cid);
					if (cuiMap == null) {
						cuiMap = new HashMap<String, Double>();
						clusterCUIs.put(cid, cuiMap);
					}
					for (String cuiScore : cuiScores.split("##")) {
						int colon = cuiScore.lastIndexOf(':');
						String w = cuiScore.substring(0, colon);
						double score = Double.parseDouble(cuiScore
								.substring(colon + 1));
						cuiMap.put(w, score);
					}
				}
			}
			set.getStatement().close();
		} catch (SQLException e) {
			throw new Error(e);
		}
		return clusterCUIs;
	}

	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		return dbThesaurus.getSimilarTermScore(t1,t2);
		
	}

}
