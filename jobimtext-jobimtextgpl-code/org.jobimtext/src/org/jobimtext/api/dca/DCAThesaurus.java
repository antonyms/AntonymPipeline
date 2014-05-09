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
package org.jobimtext.api.dca;

import java.io.IOException;
import java.util.Map;


import com.ibm.sai.dca.common.ContentValue;
import com.ibm.sai.dca.common.ContentValue.VALTYPE;
import com.ibm.sai.dca.common.ContentValue_Score;
import com.ibm.sai.dca.common.ContentValue_Table;

/**
 * 
 * @author riedl
 * 
 */
public class DCAThesaurus extends DCAResource implements IThesaurusContentValues<String,String>{
	public DCAThesaurus() throws IOException {

	}

	public DCAThesaurus(String xmlConfigurationFile) {
		super(xmlConfigurationFile);
	}

	public ContentValue_Table getSimilarTerms(String key) {
		return (ContentValue_Table) getClient().getContentValue(getDbTables().getTableSimilarTerms(), key, ContentValue.VALTYPE.TABLE);
	}

	public Long getTermCount(String key) {
		return ((ContentValue_Score) getClient().getContentValue(getDbTables().getTableTermCount(), key, VALTYPE.SCORE)).getScore();
	}

	public Long getContextsCount(String key) {
		return ((ContentValue_Score) getClient().getContentValue(getDbTables().getTableContextsCount(), key, VALTYPE.SCORE)).getScore();
	}

	public Double getTermContextsScore(String key, String val) {
		ContentValue_Table values = (ContentValue_Table) getClient().getContentValue(getDbTables().getTableSimilarTerms(), key, ContentValue.VALTYPE.TABLE);
		return values.get(val).doubleValue();
	}

	public ContentValue_Table getSimilarTerms(String key, int numberOfEntries) {
		return getSimilarTerms(key);
	}

	public ContentValue_Table getSimilarTerms(String key, double threshold) {
		return getSimilarTerms(key);
	}

	@Override
	public ContentValue_Table getTermContextsScores(String key) {
		return (ContentValue_Table) getClient().getContentValue(getDbTables().getTableTermContextsScore(), key, ContentValue.VALTYPE.TABLE);
	}

	@Override
	public ContentValue_Table getTermContextsScores(String key, int numberOfEntries) {
		return getTermContextsScores(key);
	}

	@Override
	public ContentValue_Table getTermContextsScores(String key, double threshold) {
		return getTermContextsScores(key);
	}
	@Override
	public Long getTermContextsCount(String key, String val) {
		throw new IllegalStateException("getKeyValuesCount is not supported");
	}

	@Override
	public ContentValue_Table getSimilarContexts(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public ContentValue_Table getSimilarContexts(String key, int numberOfEntries) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public ContentValue_Table getSimilarContexts(String key, double threshold) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public ContentValue_Table getSenses(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public ContentValue_Table getIsas(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public ContentValue_Table getSenseCUIs(String key) {
		throw new IllegalStateException("This method is not supported");
	}

	@Override
	public Double getSimilarTermScore(String t1, String t2) {
		ContentValue_Table t2s = getSimilarTerms(t1);
		if (t2s == null)
			return 0.0;
		Double score = t2s.get(t2);
		if (score ==null)
			return 0.0;
		return score;
	}
}
