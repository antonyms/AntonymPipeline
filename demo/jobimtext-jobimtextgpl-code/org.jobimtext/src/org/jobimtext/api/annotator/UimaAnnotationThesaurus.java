package org.jobimtext.api.annotator;

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
import java.util.List;

import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.api.struct.IThesaurusDatastructure;
import org.jobimtext.api.struct.Order1;
import org.jobimtext.api.struct.Order2;
import org.jobimtext.api.struct.Sense;


/**
 * 
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
 * 
 */
public class UimaAnnotationThesaurus extends UimaThesaurus implements
		IThesaurusDatastructure<Annotation, Annotation> {
	public UimaAnnotationThesaurus() {

	}

	public UimaAnnotationThesaurus(
			IThesaurusDatastructure<String, String> thesaurus,
			String extractorConfigurationFile) {
		super(thesaurus, extractorConfigurationFile);
	}

	public List<Order2> getSimilarTerms(Annotation key) {
		return thesaurus.getSimilarTerms(extractor.extract(key));
	}

	public List<Order2> getSimilarTerms(Annotation key, int numberOfEntries) {
		return thesaurus.getSimilarTerms(extractor.extract(key),
				numberOfEntries);
	}

	public List<Order2> getSimilarTerms(Annotation key, double threshold) {
		return thesaurus.getSimilarTerms(extractor.extract(key), threshold);
	}

	public Long getTermCount(Annotation key) {
		return thesaurus.getTermCount(extractor.extract(key));
	}

	public Long getContextsCount(Annotation value) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	public Double getTermContextsScore(Annotation key, Annotation val) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public boolean connect() {
		return thesaurus.connect();
	}

	@Override
	public List<Order1> getTermContextsScores(Annotation key) {
		return thesaurus.getTermContextsScores(extractor.extract(key));
	}

	@Override
	public List<Order1> getTermContextsScores(Annotation key,
			int numberOfEntries) {
		return thesaurus.getTermContextsScores(extractor.extract(key),
				numberOfEntries);
	}

	@Override
	public List<Order1> getTermContextsScores(Annotation key, double threshold) {
		return thesaurus.getTermContextsScores(extractor.extract(key),
				threshold);
	}

	@Override
	public Long getTermContextsCount(Annotation key, Annotation value) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Order2> getSimilarContexts(Annotation value) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Order2> getSimilarContexts(Annotation value, int numberOfEntries) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Order2> getSimilarContexts(Annotation value, double threshold) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Sense> getSenses(Annotation key) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Sense> getIsas(Annotation key) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public List<Sense> getSenseCUIs(Annotation key) {
		throw new IllegalAccessError(
				"This context-aware function is not implented in the UIMA Annotation thesaurus");
	}

	@Override
	public Double getSimilarTermScore(Annotation t1, Annotation t2) {
		return thesaurus.getSimilarTermScore(extractor.extract(t1),
				extractor.extract(t2));
	}

}
