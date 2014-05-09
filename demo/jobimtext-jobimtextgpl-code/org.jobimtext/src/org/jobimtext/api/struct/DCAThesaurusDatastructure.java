/*******************************************************************************
 * Copyright 2012
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
package org.jobimtext.api.struct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jobimtext.api.dca.DCAResource;

import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.common.ContentValue.VALTYPE;
import com.ibm.sai.dca.common.ContentValue_Score;
import com.ibm.sai.dca.common.ContentValue_Table;

/**
 * 
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
 * 
 *         In Comparison to the class DCAThesaurus this class returns the
 *         similar terms in the Order2 datastructure, which is also used within
 *         the class DatabaseConnection
 */
public class DCAThesaurusDatastructure extends DCAResource implements
		IThesaurusDatastructure<String, String> {

	public DCAThesaurusDatastructure() {

	}

	public DCAThesaurusDatastructure(String xmlConfigurationFile) {
		super(xmlConfigurationFile);
	}

	public DCAThesaurusDatastructure(String xmlConfigurationFile, boolean sort) {
		super(xmlConfigurationFile, sort);
	}

	public List<Order2> getSimilarTerms(String key) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);
		List<Order2> list = convertCvToOrder2List(cv);
		return list;
	}

	private List<Order2> convertCvToOrder2List(ContentValue_Table cv) {
		List<Order2> list = new ArrayList<Order2>();
		for (String k : cv.keySet()) {
			list.add(new Order2(k, cv.get(k)));
		}
		if (isSort()) {
			Collections.sort(list, new Comparator<Order2>() {
				public int compare(Order2 arg0, Order2 arg1) {
					return arg1.score.compareTo(arg0.score);
				}
			});
		}
		return list;
	}

	public Long getTermCount(String key) {
		System.out.println(key);
		ContentValue_Score cv = (ContentValue_Score) getClient()
				.getContentValue(getDbTables().getTableTermCount(), key,
						VALTYPE.SCORE);
		if (cv == null || cv.getScore() == null) {
			return 0L;
		}
		return cv.getScore();
	}

	public static void main(String[] args) throws IOException {
		new DCAThesaurusDatastructure();
	}

	public Long getContextsCount(String key) {
		ContentValue_Score cv = (ContentValue_Score) getClient()
				.getContentValue(getDbTables().getTableContextsCount(), key,
						VALTYPE.SCORE);
		if (cv == null) {
			return 0L;
		}
		return cv.getScore();
	}

	public Double getTermContextsScore(String key, String val) {
		ContentValue_Table values = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableTermContextsScore(), key,
						ContentValue.VALTYPE.TABLE);
		if (values.get(val) == null) {
			return 0.0;
		}
		return values.get(val).doubleValue();
	}

	public List<Order2> getSimilarTerms(String key, int numberOfEntries) {
		if (isSort()) {
			List<Order2> exps = getSimilarTerms(key);
			if (numberOfEntries > exps.size()) {
				return exps;
			} else {
				return exps.subList(0, numberOfEntries);
			}

		}
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);
		return getNumberOfEntries(cv, numberOfEntries);
	}

	private List<Order2> getNumberOfEntries(ContentValue_Table cv,
			int numberOfEntries) {
		List<Order2> list = new ArrayList<Order2>();

		for (String k : cv.keySet()) {
			list.add(new Order2(k, cv.get(k)));
			if (list.size() == numberOfEntries) {
				return list;
			}
		}
		return list;

	}

	public List<Order2> getSimilarTerms(String key, double threshold) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);

		return filterOrder2Scores(cv, threshold);
	}

	private List<Order2> filterOrder2Scores(ContentValue_Table cv, double threshold) {
		List<Order2> list = new ArrayList<Order2>();
		for (String k : cv.keySet()) {
			Double score = cv.get(k);
			if (score > threshold) {
				list.add(new Order2(k, cv.get(k)));
			}
		}
		return list;
	}

	@Override
	public List<Order1> getTermContextsScores(String key) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarTerms(), key,
						ContentValue.VALTYPE.TABLE);
		List<Order1> list = new ArrayList<Order1>();
		for (String k : cv.keySet()) {
			list.add(new Order1(k, cv.get(k)));
		}
		if (isSort()) {
			Collections.sort(list, new Comparator<Order1>() {
				public int compare(Order1 arg0, Order1 arg1) {
					return arg1.score.compareTo(arg0.score);
				}
			});
		}
		return list;
	}

	@Override
	public List<Order1> getTermContextsScores(String key, int numberOfEntries) {
		List<Order1> list = getTermContextsScores(key);
		for (int i = numberOfEntries; i < list.size();) {
			list.remove(i);
		}
		return list;
	}

	@Override
	public List<Order1> getTermContextsScores(String key, double threshold) {
		List<Order1> list = getTermContextsScores(key);
		for (int i = 0; i < list.size();) {
			if (list.get(i).score < threshold) {
				list.remove(i);
			} else {
				i++;
			}
		}
		return list;
	}

	@Override
	public Long getTermContextsCount(String key, String val) {
		throw new IllegalAccessError("This method is yet not supported");
	}

	@Override
	public List<Order2> getSimilarContexts(String value, int numberOfEntries) {
		if (isSort()) {
			List<Order2> exps = getValueExpansions(value);
			if (numberOfEntries > exps.size()) {
				return exps;
			} else {
				return exps.subList(0, numberOfEntries);
			}

		}
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarContexts(), value,
						ContentValue.VALTYPE.TABLE);
		List<Order2> list = new ArrayList<Order2>();

		for (String k : cv.keySet()) {
			list.add(new Order2(k, cv.get(k)));
			if (list.size() == numberOfEntries) {
				return list;
			}
		}
		return list;
	}

	private List<Order2> getValueExpansions(String value) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarContexts(), value,
						ContentValue.VALTYPE.TABLE);
		List<Order2> list = new ArrayList<Order2>();
		for (String k : cv.keySet()) {
			list.add(new Order2(k, cv.get(k)));
		}
		if (isSort()) {
			Collections.sort(list, new Comparator<Order2>() {
				public int compare(Order2 arg0, Order2 arg1) {
					return arg1.score.compareTo(arg0.score);
				}
			});
		}
		return list;
	}

	@Override
	public List<Order2> getSimilarContexts(String key) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarContexts(), key,
						ContentValue.VALTYPE.TABLE);
		List<Order2> list = convertCvToOrder2List(cv);
		return list;
	}

	@Override
	public List<Order2> getSimilarContexts(String key, double threshold) {
		ContentValue_Table cv = (ContentValue_Table) getClient()
				.getContentValue(getDbTables().getTableSimilarContexts(), key,
						ContentValue.VALTYPE.TABLE);

		return filterOrder2Scores(cv, threshold);
	}

	@Override
	public List<Sense> getSenses(String key) {
		throw new IllegalStateException("This method is not supported");
		
	}

	@Override
	public List<Sense> getIsas(String key) {
		throw new IllegalStateException("This method is not supported");
		
	}

	@Override
	public List<Sense> getSenseCUIs(String key) {
		throw new IllegalStateException("This method is not supported");
		
	}

	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		List<Order2> t2s = getSimilarTerms(t1);
		if (t2s == null)
			return 0.0;
		for(Order2 o2:t2s){
			if(o2.key.equals(t2))
				return o2.score;
		}
		return 0.0;
	}

}
