/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and 
* FG Language Technology
* Technische Universitaet Darmstadt
* 
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
package org.jobimtext.holing.annotator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.type.Jo;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;


/**
 * The TrigramHolingAnnotator generates all possible trigram combinations of multi-word {@link Jo}s.
 * The hole position is in the middle of the trigram: Jo_@_Jo.
 *
 */
public class TrigramHolingAnnotator extends JCasAnnotator_ImplBase {
	public static final String PARAM_SENTENCE_ANNOTATION="SentenceAnnotation";
	public static final String PARAM_TOKEN_ANNOTATION="TokenAnnotation";
	public static final String PARAM_JO_ANNOTATION="JoAnnotation";
	public static final String PARAM_BIM_ANNOTATION="BimAnnotation";
	public static final String PARAM_EMPTY_BIMS_FOR_ALL_JOS="addEmptyBimsForAllJos";
	public static final String PARAM_RELATION_NAME="RelationName";
	
	private Logger logger;

	private Class<? extends Annotation> sentenceAnnotation = Sentence.class;
	private Class<? extends Annotation> tokenAnnotation = Token.class;
	private Class<? extends Annotation> joAnnotation = Jo.class;
	private Class<? extends Annotation> bimAnnotation= Token.class;
	
	private boolean addEmptyBimsForJos=false;
	private String relationName = "Bim_@_Bim";
	
	/**
	 * Initializes the annotator. Set the annotation classes.
	 * 
	 * @param aContext
	 *            UIMA Annotator context
	 */
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		logger = aContext.getLogger();
		logger.log(Level.FINE, this.getClass().getName() + ":initialize");
		Object empty=aContext.getConfigParameterValue(PARAM_RELATION_NAME);
		
		if (empty !=null){
			relationName = empty.toString();
		}
		empty=aContext.getConfigParameterValue(PARAM_EMPTY_BIMS_FOR_ALL_JOS);
		if(empty != null)	
			addEmptyBimsForJos = Boolean.parseBoolean(empty.toString());
		initAnnotationClass(aContext, PARAM_SENTENCE_ANNOTATION , sentenceAnnotation);
		initAnnotationClass(aContext, PARAM_TOKEN_ANNOTATION , tokenAnnotation);
		initAnnotationClass(aContext, PARAM_JO_ANNOTATION , joAnnotation);
		initAnnotationClass(aContext, PARAM_BIM_ANNOTATION , bimAnnotation);
	}
	@SuppressWarnings("unchecked")
	private void initAnnotationClass(
			UimaContext aContext, String paramName,Class<? extends Annotation> annotationVariable) {
		
		Object className  = aContext.getConfigParameterValue(paramName).toString();
		if(className==null ||className.toString().length()==0){
			return; 
		}
		try {
			annotationVariable = (Class<? extends Annotation>) Class
					.forName(className.toString());
		} catch (Exception e) {
			System.err.println("Annotation class "+className.toString()+"does not exists: "+e.getMessage());
		}
	}

	/**
	 * Process all sentences and annotate trigram JoBims witch holes.
	 * The hole '@' is in the middle (position 1).
	 * A Jo can be an n-gram of Tokens. The resulting pattern is "Jo_@_Jo".
	 * 
	 *  @param aJCas the target CAS
	 */
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// for all relations with hole and length, process(cas, hole, length)
		logger.log(Level.INFO, "TrigramHolingAnnotator: Start");

		int prev = 0;
		for (Annotation s : JCasUtil.select(aJCas, sentenceAnnotation)) {
			// create maps for retrieving the previous/next Jos
			// The rMap maps the beginning of a word to the end position of the previous Token.
			// The lMap maps the end position of a word to the beginning of the next Token.
			// As there are possibly multiple spaces or tabs between two Tokens,
			// we cannot assume that e.g. term.getBegin()-1 is the last position of the previous Token.
			HashMap<Integer, Integer> lMap = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> rMap = new HashMap<Integer, Integer>();
			for (Annotation t : JCasUtil.selectCovered(tokenAnnotation, s)) {
				// process only single-word Tokens, skip annotated Jos
				if ( joAnnotation.isInstance(t) ) {
					continue;
				}

				rMap.put(t.getBegin(), prev);
				lMap.put(prev, t.getBegin());
				prev = t.getEnd();
			}
			
			// the maps receive a (sentence_begin,sentence_begin) entry (in the for-loop above)
			// to signify the beginning of the sentence, e.g. (0,0)
			// and a (sentence_end, sentence_end) entry for the sentence end
			rMap.put(prev, prev);
			lMap.put(prev, prev);
			// System.out.println(rMap);
			// System.out.println(lMap);
			
			// create maps: Jo -> a list of left or right neighbor Jos	
			HashMap<Integer, List<Annotation>> leftNeighbor = new HashMap<Integer, List<Annotation>>();
			HashMap<Integer, List<Annotation>> rightNeighbor = new HashMap<Integer, List<Annotation>>();
			
			for (Annotation bim : JCasUtil.selectCovered(bimAnnotation, s)) {
				
				// process only Jos
				if(!bimAnnotation.equals(joAnnotation)&& joAnnotation.isInstance(bim)){
					continue;
				}
//				 System.out.println(jo.getBegin() + "\t" + jo.getEnd() + "\t"
//				 + jo.getCoveredText());

				// get beginning of next Jo
				int rightIdx = lMap.get(bim.getEnd());
				// add Jo as leftNeighbor of next Jo
				add(bim, leftNeighbor, rightIdx);
				
				// get end of previous Jo
				int leftIdx = rMap.get(bim.getBegin());
				// add Jo as rightNeighbor of previous Jo
				add(bim, rightNeighbor, leftIdx);
			}

			lMap.clear();
			rMap.clear();
	
			if(addEmptyBimsForJos){
				// add 'empty' Jos as neighbors for all Jos  
				// this allows us to capture bigrams along with trigrams
				for (List<Annotation> jos : leftNeighbor.values()) {
					jos.add(new Annotation(aJCas, 0, 0));
				}
				for (List<Annotation> jos : rightNeighbor.values()) {
					jos.add(new Annotation(aJCas, 0, 0));
				}
			}
			int i=0;
			int j =0;
			int k = 0;
			System.out.println(JCasUtil.selectCovered(Annotation.class, s).size());
			
			// JoBim generation for each Jo
			for (Annotation jo : JCasUtil.selectCovered(joAnnotation, s)) {
				
				if(jo.getEnd()-jo.getBegin()==0)continue;
				List<Annotation> ls = leftNeighbor.get(jo.getBegin());
				List<Annotation> rs = rightNeighbor.get(jo.getEnd());
				if (ls == null || ls.size() == 0) {
					ls = Collections.singletonList(new Annotation(aJCas, 0, 0));
				}
				if (rs == null || ls.size() == 0) {
					rs = Collections.singletonList(new Annotation(aJCas, 0, 0));
				}
				
				// build all possible combinations of left and right neighbors
				// annotate Jo with its neighbors as a trigram JoBim 
				for (Annotation left : ls) {
					for (Annotation right : rs) {
						// at least one of the Jos (left or right) has to be a valid 'word'
						// with a span != 0
						if (!(left.getEnd() - left.getBegin() == 0 && 
								right.getEnd() - right.getBegin() == 0)) {
							JoBim jb = new JoBim(aJCas, jo.getBegin(), jo.getEnd());
							jb.setRelation(relationName);
							jb.setKey(jo);
							jb.setHole(1);
							
							FSArray arr = new FSArray(aJCas, 2);
							arr.set(0, left);
							arr.set(1, right);
							jb.setValues(arr);
							jb.addToIndexes();
							// System.out.println(jo.getBegin()+"\t"+jo.getEnd()+"\t"+left.getBegin()+"\t"+left.getEnd()+"\t"+right.getBegin()+"\t"+right.getEnd());
							// System.out.flush();
//							System.out.println("\t"+i+"\t"+j+"\t"+k+"\tJO:"+jo.getBegin()+":"+jo.getEnd() +"\t"+left.getBegin()+":"+left.getEnd()+"\t"+right.getBegin()+":"+right.getEnd());
							// System.out.println(jo.getCoveredText() + "\t"
							// + left.getCoveredText() + "\t"
							// + right.getCoveredText());
						}
						k++;
					}
					k = 0;
					j++;
					
				}
				j = 0;
				i++;
			}
			logger.log(Level.INFO, "TrigramHolingAnnotator: " + i + " Annotations with " 
				+ j + " left " + k + " right neighbors overall");
		}
		logger.log(Level.INFO, "TrigramHolingAnnotator: Finish");		
		

	}


	/**
	 * Add Jo as neighbor of the next/previous Jo. 
	 * The neighborsMap can be a map of left or right neighbors.
	 * @param jo the jo that is added as neighbor
	 * @param neighborsMap map of an index position to a list of neighbors
	 * @param searchIdx search index position: start of next or end of previous Jo
	 */
	private void add(Annotation jo, HashMap<Integer, List<Annotation>> neighborsMap, int searchIdx) {
		List<Annotation> lefts = neighborsMap.get(searchIdx);
		if (lefts == null) {
			lefts = new LinkedList<Annotation>();
			neighborsMap.put(searchIdx, lefts);

		}
		lefts.add(jo);
	}

	public static <T extends Annotation> List<T> selectPreceding(JCas aJCas,
			Class<T> aType, Annotation annotation, int count) {
		Type t = getType(aJCas, aType);
		return cast(selectPreceding(aJCas.getCas(), t, annotation, count));
	}

	public static Type getType(JCas jCas, Class<?> type) {
		return CasUtil.getType(jCas.getCas(), type);
	}

	@SuppressWarnings({ "cast", "unchecked", "rawtypes" })
	private static <T> List<T> cast(List aCollection) {
		return (List<T>) aCollection;
	}

	public static List<AnnotationFS> selectPreceding(CAS cas, Type type,
			AnnotationFS annotation, int count) {
		if (!cas.getTypeSystem().subsumes(cas.getAnnotationType(), type)) {
			throw new IllegalArgumentException("Type [" + type.getName()
					+ "] is not an annotation type");
		}

		// move to first previous annotation
		FSIterator<AnnotationFS> itr = cas.getAnnotationIndex(type).iterator();
		itr.moveTo(annotation);

		// make sure we're past the beginning of the reference annotation
		while (itr.isValid() && itr.get().getEnd() > annotation.getEnd()) {
			itr.moveToPrevious();
		}

		// add annotations from the iterator into the result list
		List<AnnotationFS> precedingAnnotations = new LinkedList<AnnotationFS>();
		for (int i = 0; i < count && itr.isValid(); itr.moveToPrevious()) {

			if (itr.get().getEnd() == annotation.getBegin()) {
				precedingAnnotations.add(itr.get());
				i++;
			}
		}

		// return in correct order
		Collections.reverse(precedingAnnotations);
		return precedingAnnotations;
	}

//	private void addJobim(JCas aJCas, Annotation jo, Annotation left, Annotation right,
//			String string) {
//		JoBim jb = new JoBim(aJCas, jo.getBegin(), jo.getEnd());
//		jb.setRelation(string);
//		jb.setKey(jo);
//		FSArray arr = new FSArray(aJCas, 2);
//		arr.set(0, left);
//		jb.setHole(1);
//		arr.set(1, right);
//		jb.setValues(arr);
//
//		// System.out.println(jo.getBegin()+"\t"+jo.getEnd()+"\t"+left.getBegin()+"\t"+left.getEnd()+"\t"+right.getBegin()+"\t"+right.getEnd());
//		// System.out.flush();
//		jb.addToIndexes();
//	}

}
