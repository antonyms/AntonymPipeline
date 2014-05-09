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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.type.Jo;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;

/**
 * The JoAnnotator is used to annotate n-grams up to the size '{@link #size}' in a {@link Sentence}.
 *
 */
public class JoAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_JO_TOKEN_LENGTH = "joTokenLength";
	private Logger logger;
	private int size = 1;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		logger = context.getLogger();
		logger.log(Level.FINE, this.getClass().getName() + ":initialize");
		size = (Integer) context.getConfigParameterValue(PARAM_JO_TOKEN_LENGTH);
	}

//	@Override
//	public void process(JCas jcas) throws AnalysisEngineProcessException {
//		logger.log(Level.INFO, "JoAnnotator: Start");
//		for (int i = 0; i < size; i++) {
//			process(jcas, i + 1);
//		}
//		logger.log(Level.INFO, "JoAnnotator: Stop");
//	}
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		logger.log(Level.INFO, "JoAnnotator: Start");
		
		for (Sentence s : select(jcas, Sentence.class)) {
			LinkedList<Token> list = new LinkedList<Token>();
			Set<String> set = new HashSet<String>();
			for (Token t : selectCovered(Token.class, s)) {
				if (t instanceof Jo)
					continue;
				list.add(t);
				addList(list,set,jcas);
			}
		}
		logger.log(Level.INFO, "JoAnnotator: Stop");
	}
	

	private void addList(LinkedList<Token> list, Set<String> set,JCas jcas) {
		for(int i=1;i<=Math.min(size,list.size());i++){
			int begin =list.get(list.size()-i).getBegin();
					int end =list.get(list.size()-1).getEnd();
			String idx = begin + ":" + end;
			if (set.contains(idx))
				return;
			set.add(idx);
			Jo jo = new Jo(jcas);
			jo.setBegin(begin);
			jo.setEnd(end);
			jo.addToIndexes();
			//System.out.println("JO"+ jo.getCoveredText());
			
		}
		if(list.size()==size){
			list.removeFirst();
		}
	}

	public void process(JCas jcas, final int length) {
		LinkedList<Annotation> al = new LinkedList<Annotation>();
		for (Sentence s : select(jcas, Sentence.class)) {
			Set<String> set = new HashSet<String>();
			for (Token t : selectCovered(Token.class, s)) {
				if (t instanceof Jo)
					continue;
				al.add(t);
				addJo(jcas, al, length, set);
			}
		}
	}

	public void addJo(JCas jcas, LinkedList<Annotation> al, int length,
			Set<String> sets) {
		if (al.size() != length) {
			return;
		}
		String idx = al.getFirst().getBegin() + ":" + al.getLast().getEnd();
		if (sets.contains(idx))
			return;
		sets.add(idx);
		Jo jo = new Jo(jcas);
		jo.setBegin(al.getFirst().getBegin());
		jo.setEnd(al.getLast().getEnd());
		jo.addToIndexes();
		al.removeFirst();
	}

}
