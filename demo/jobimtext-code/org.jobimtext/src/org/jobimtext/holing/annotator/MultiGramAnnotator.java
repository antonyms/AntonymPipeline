/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and
* 
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

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.LinkedList;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Token;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;



public class MultiGramAnnotator extends JCasAnnotator_ImplBase {
	public static final String PARAM_NGRAM = "Ngram";
	public static final String PARAM_HOLE = "Hole";
	public static final String PARAM_MULTIWORD_LENGTH = "MultiWordLength";
	public static final String PARAM_SENTENCE_ANNOTATION = "Annotation";

	@ConfigurationParameter(name = PARAM_HOLE, mandatory = true)
	private int hole;
	@ConfigurationParameter(name = PARAM_NGRAM, mandatory = true)
	private int ngram;
	@ConfigurationParameter(name = PARAM_MULTIWORD_LENGTH, mandatory = true)
	private int length;
	@ConfigurationParameter(name = PARAM_SENTENCE_ANNOTATION, mandatory = true)
	private Class<? extends Annotation> annotation;
	
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		if (ngram < hole) {
			throw new IllegalArgumentException(
					"The hole can not be larger than the ngram !");
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		
		for (int k = 1; k <= length; k++) {
			
			for (Annotation s : select(jcas, annotation)) {
				
				LinkedList<Annotation> al = new LinkedList<Annotation>();
				for (int i = 1; i < hole; i++) {
					al.add(new Token(jcas));
				}	
				for (Token t : selectCovered(Token.class, s)) {
					
					al.add(t);
					addJobimNgram(jcas, al, k);
				}
				for (int i = 0; i < ngram - hole; i++) {
					al.add(new Token(jcas, al.getLast().getEnd(), al.getLast()
							.getEnd()));
					addJobimNgram(jcas, al, k);

				}
			}
		}
	}

	public void printLIst(LinkedList<Annotation> al) {
		System.out.print(al.size() + "\t");
		for (Annotation a : al) {
			System.out.print(" " + a.getCoveredText());
		}
		System.out.println();
	}

	public void addJobimNgram(JCas jcas, LinkedList<Annotation> al, int k) {
		if (al.size() != ngram + k - 1) {
			
			return;
		}

		JoBim jb = new JoBim(jcas);
		jb.setHole(hole-1);
		FSArray array = new FSArray(jcas, ngram - 1);
		int j = 0;
		for (int i = 0; i < al.size(); i++) {
			if (i == (hole - 1)) {
				Token a = new Token(jcas);
				a.setBegin(al.get(i).getBegin());

				i = i + k - 1;
				a.setEnd(al.get(i).getEnd());
				a.addToIndexes();

				jb.setKey(a);
				jb.setBegin(a.getBegin());
				jb.setEnd(a.getEnd());

			} else {
				array.set(j++, al.get(i));
			}
		}

		jb.setRelation(ngram + "-gram" + hole);
		jb.setValues(array);
		jb.addToIndexes();
		al.removeFirst();

	}
}

