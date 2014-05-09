package org.jobimtext.api.annotator;

import java.util.List;

import org.jobimtext.api.struct.IThesaurusDatastructure;
import org.jobimtext.api.struct.Order1;
import org.jobimtext.api.struct.Order2;
import org.jobimtext.api.struct.Sense;
import org.jobimtext.holing.type.JoBim;


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

/**
 * 
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
 * 
 */
public class UimaJoBimThesaurus extends UimaThesaurus implements
		IThesaurusDatastructure<JoBim, JoBim> {
	public UimaJoBimThesaurus() {

	}

	public UimaJoBimThesaurus(
			IThesaurusDatastructure<String, String> thesaurus,
			String extractorConfigurationFile) {
		super(thesaurus, extractorConfigurationFile);
	}

	public List<Order2> getSimilarTerms(JoBim key) {
		return thesaurus.getSimilarTerms(extractor.extractKey(key));
	}

	public List<Order2> getSimilarTerms(JoBim key, int numberOfEntries) {
		return thesaurus.getSimilarTerms(extractor.extractKey(key),
				numberOfEntries);
	}

	public List<Order2> getSimilarTerms(JoBim key, double threshold) {
		return thesaurus.getSimilarTerms(extractor.extractKey(key), threshold);
	}

	public Long getTermCount(JoBim key) {
		return thesaurus.getTermCount(extractor.extractKey(key));
	}

	public Long getContextsCount(JoBim jb) {
		return thesaurus.getContextsCount(extractor.extractValues(jb));
	}

	public Double getTermContextsScore(JoBim key, JoBim val) {
		return thesaurus.getTermContextsScore(extractor.extractKey(key),
				extractor.extractValues(val));
	}

	@Override
	public boolean connect() {
		return thesaurus.connect();
	}

	@Override
	public List<Order1> getTermContextsScores(JoBim key) {
		return thesaurus.getTermContextsScores(extractor.extractKey(key));
	}

	@Override
	public List<Order1> getTermContextsScores(JoBim key, int numberOfEntries) {
		return thesaurus.getTermContextsScores(extractor.extractKey(key),
				numberOfEntries);
	}

	@Override
	public List<Order1> getTermContextsScores(JoBim key, double threshold) {
		return thesaurus.getTermContextsScores(extractor.extractKey(key),
				threshold);
	}

	@Override
	public Long getTermContextsCount(JoBim key, JoBim val) {
		return thesaurus.getTermContextsCount(extractor.extractKey(key),
				extractor.extractValues(val));

	}

	@Override
	public List<Order2> getSimilarContexts(JoBim key) {
		return thesaurus.getSimilarContexts(extractor.extractValues(key));
	}

	@Override
	public List<Order2> getSimilarContexts(JoBim key, int numberOfEntries) {
		return thesaurus.getSimilarContexts(extractor.extractValues(key),
				numberOfEntries);
	}

	@Override
	public List<Order2> getSimilarContexts(JoBim key, double threshold) {
		return thesaurus.getSimilarContexts(extractor.extractValues(key),
				threshold);
	}

	@Override
	public List<Sense> getSenses(JoBim key) {
		return thesaurus.getSenses(extractor.extractKey(key));
	}

	@Override
	public List<Sense> getIsas(JoBim key) {
		return thesaurus.getIsas(extractor.extractKey(key));
	}

	@Override
	public List<Sense> getSenseCUIs(JoBim key) {
		return thesaurus.getSenseCUIs(extractor.extractKey(key));
	}

	@Override
	public Double getSimilarTermScore(JoBim t1, JoBim t2) {
		return thesaurus.getSimilarTermScore(extractor.extractKey(t1), extractor.extractKey(t2));
	}

}
