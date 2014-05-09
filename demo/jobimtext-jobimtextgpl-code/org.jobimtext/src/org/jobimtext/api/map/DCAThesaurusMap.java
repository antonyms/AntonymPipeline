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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jobimtext.api.dca.DCAResource;

import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.common.ContentValue_Table;


public class DCAThesaurusMap extends DCAResource implements
		IThesaurusMap<String, String> {

	public DCAThesaurusMap(String string) {
		super(string);
	}

	public DCAThesaurusMap(String string, boolean sort) {
		super(string, sort);
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);
		return contentValueTable2Map(cv);
	}

	public static Map<String, Double> contentValueTable2Map(
			ContentValue_Table cv) {
		Map<String, Double> result = new HashMap<String, Double>();
		
		if ( cv == null )
			return result;
		
		for (String key : cv.keySet()) {
			result.put(key, cv.get(key));
		}
		return result;
	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, int numberOfEntries) {
		return DcaLightThesaurusMap.removeTopN(getSimilarTerms(key), numberOfEntries);

	}

	@Override
	public Map<String, Double> getSimilarTerms(String key, double threshold) {
		return DcaLightThesaurusMap.removeBelowThreshold(getSimilarTerms(key), threshold);
	}
	
	@Override
	public Long getTermCount(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Long getContextsCount(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Long getTermContextsCount(String key, String val) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Double getTermContextsScore(String key, String val) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key) {
		
		String tblTermContextsScore = getDbTables().getTableTermContextsScore();
		if ( tblTermContextsScore == null )
			return new HashMap<String, Double>();
		
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(tblTermContextsScore, key, ContentValue.VALTYPE.TABLE);
		
		return contentValueTable2Map(cv);
	}

	@Override
	public Map<String, Double> getTermContextsScores(String key,
			int numberOfEntries) {
		return DcaLightThesaurusMap.removeTopN(getTermContextsScores(key), numberOfEntries);

	}

	@Override
	public Map<String, Double> getTermContextsScores(String key,
			double threshold) {
		return DcaLightThesaurusMap.removeBelowThreshold(getTermContextsScores(key), threshold);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String key) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);
		return contentValueTable2Map(cv);
	}

	@Override
	public Map<String, Double> getSimilarContexts(String key,
			int numberOfEntries) {
		return DcaLightThesaurusMap.removeTopN(getSimilarContexts(key), numberOfEntries);

	}

	@Override
	public Map<String, Double> getSimilarContexts(String key, double threshold) {
		return DcaLightThesaurusMap.removeBelowThreshold(getSimilarContexts(key), threshold);
	}

	@Override
	public Map<Integer, List<String>> getSenses(String key) {
		return new HashMap<Integer, List<String>>();
		// FIXME: This method is not implemented
		//throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Map<Integer, Map<String, Double>> getIsas(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Map<Integer, Map<String, Double>> getSenseCUIs(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		Map<String, Double> t2s = getSimilarTerms(t1);
		if (t2s == null)
			return 0.0;
		if (!t2s.containsKey(t2))
			return 0.0;
		return t2s.get(t2);
	}

}
