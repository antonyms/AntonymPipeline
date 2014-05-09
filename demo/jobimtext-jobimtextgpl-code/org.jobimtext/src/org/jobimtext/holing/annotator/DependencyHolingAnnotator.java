/*******************************************************************************
 * Copyright 2013
 * Copyright (c) 2013 IBM Corp.
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

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.Dependency;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Token;


public class DependencyHolingAnnotator extends JCasAnnotator_ImplBase {

	private boolean withHole = true;

	private static String MODEL_ID = "DependencyParseModel";

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		if (withHole) {
			for (Dependency d : select(aJCas, Dependency.class)) {
				addJobim(aJCas, d.getGovernor(), d.getDependent(),
						d.getDependencyType(), 0);
				addJobim(aJCas, d.getDependent(), d.getGovernor(),
						d.getDependencyType(), 1);
			}
		} else {
			for (Dependency d : select(aJCas, Dependency.class)) {
				addJobim(aJCas, d.getGovernor(), d.getDependent(),
						d.getDependencyType());
				addJobim(aJCas, d.getDependent(), d.getGovernor(),
						"-" + d.getDependencyType());
			}
		}
	}

	public void addJobim(JCas jcas, Token t1, Token t2, String relation,
			int holePosition) {
		JoBim jb = createJobim(jcas, t1, t2, relation);
		jb.setHole(holePosition);
	}

	public void addJobim(JCas jcas, Token t1, Token t2, String relation) {
		createJobim(jcas, t1, t2, relation);
	}

	public JoBim createJobim(JCas jcas, Token t1, Token t2, String relation) {
		JoBim jb = new JoBim(jcas, t1.getBegin(), t1.getEnd());
		jb.setModel(MODEL_ID);
		jb.setKey(t1);
		FSArray array = new FSArray(jcas, 1);
		array.set(0, t2);
		jb.setValues(array);
		jb.setRelation(relation);
		// no hole visible
		jb.setHole(-1);
		jb.addToIndexes();
		return jb;
	}

}
