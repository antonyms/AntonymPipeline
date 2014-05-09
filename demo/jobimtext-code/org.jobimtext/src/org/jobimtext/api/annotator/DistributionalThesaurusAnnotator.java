/*******************************************************************************
 * Copyright 2012
 * FG Language Technologie
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
package org.jobimtext.api.annotator;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.Collection;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.jobimtext.api.struct.Order2;
import org.jobimtext.holing.type.Token;
import org.jobimtext.type.DistributionalExpansion;
import org.jobimtext.type.DistributionalExpansions;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;


/**
 * 
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
 * 
 */
public class DistributionalThesaurusAnnotator extends JCasAnnotator_ImplBase {
	public final static String PARAM_MODEL_THESAURUS = "ModelThesaurus";
	public static final String PARAM_NUMBER_OF_TOP_ENTRIES = "NumberOfTopEntries";
	public final static String PARAM_NEGLECT_FIRST_ENTRY = "NeglectFirstEntry";
	public final static String PARAM_TARGET_ANNOTATION = "AnnotationType";
	public final static String PARAM_KEY_ANNOTATION = "KeyAnnotation";
	// public static final String PARAM_EXTRACTOR_CLASS_NAME =
	// JobimTextParameter.PARAM_EXTRACTOR_CLASS_NAME;
	// public static final String PARAM_ATTRIBUTE_DELIMITER =
	// JobimTextParameter.PARAM_ATTRIBUTE_DELIMITER;

	@ExternalResource(key = PARAM_MODEL_THESAURUS)
	private UimaAnnotationThesaurus model;

	@ConfigurationParameter(name = PARAM_TARGET_ANNOTATION, mandatory = false, description = "Specifies the name of the Anntation that is iterated and expanded")
	private String annotationClassName = Token.class.getName();

	@ConfigurationParameter(name = PARAM_KEY_ANNOTATION, mandatory = false, description = "Specifies the Annotation that should be used, which contains the Target Annotation (e.g. a Target Annotation is used to expand only this token, but as sometimes also POS, Lemma is needed, we use this trick)")
	private String keyAnnotationClassName = null;

	private Class<Annotation> annotationClass;
	private Class<Annotation> keyAnnotationClass;

	/**
	 * The most similar entry to a key is the key itself, to avoid using this
	 * key the first entry can be neglected during the annotation
	 */
	@ConfigurationParameter(name = PARAM_NEGLECT_FIRST_ENTRY, mandatory = false)
	private boolean neglectFirstEntry = false;

	@ConfigurationParameter(name = PARAM_NUMBER_OF_TOP_ENTRIES, mandatory = true)
	private int topEntries;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		try {
			annotationClass = (Class<Annotation>) Class
					.forName(annotationClassName);
			if (keyAnnotationClassName != null) {
				if (keyAnnotationClassName.equals(annotationClassName)) {
					keyAnnotationClassName = null;
				} else {
					keyAnnotationClass = (Class<Annotation>) Class
							.forName(keyAnnotationClassName);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Annotation t : select(jcas, annotationClass)) {
			if (keyAnnotationClassName == null) {
				addDistributionalExpansions(t, jcas);
			} else {
				Collection<Annotation> s = selectCovered(keyAnnotationClass, t);
				if (s.size() == 1) {
					addDistributionalExpansions(s.iterator().next(), jcas);
				} else {
					getLogger().log(
							Level.WARNING,
							"The Annotation " + keyAnnotationClassName
									+ " could not be found for the target: "
									+ t.getCoveredText());
				}

			}

		}
	}

	public void addDistributionalExpansions(Annotation t, JCas jcas) {
		List<Order2> e = null;
		// load the data from the model und neglect first entry (as this is
		// the same word as the word that is expanded)

		if (neglectFirstEntry) {
			e = model.getSimilarTerms(t, topEntries + 1);
		} else {
			e = model.getSimilarTerms(t, topEntries);
		}

		if (e != null && e.size() > 0) {
			FSArray array = new FSArray(jcas, e.size());
			for (int i = 0; i < e.size(); i++) {
				Order2 order2 = e.get(i);
				DistributionalExpansion entry = new DistributionalExpansion(
						jcas, t.getBegin(), t.getEnd());
				entry.setKey(order2.key);
				entry.setScore(order2.score);
				entry.addToIndexes();
				array.set(i, entry);
			}
			array.addToIndexes();
			DistributionalExpansions entries = new DistributionalExpansions(
					jcas, t.getBegin(), t.getEnd());
			entries.setEntries(array);
			entries.addToIndexes();
		}
	}
	

}
