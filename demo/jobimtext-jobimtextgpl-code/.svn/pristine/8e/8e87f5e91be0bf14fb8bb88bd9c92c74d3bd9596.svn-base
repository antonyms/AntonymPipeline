package org.jobimtext.gpl.stanford;


/*******************************************************************************
 * Copyright 2012
 * FG Language Technologie
 * Technische Universität Darmstadt
 *
 * Licensed under the GPL, Version 3.0 (the "License");
 ******************************************************************************/


import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.JoBim;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

/**
 * 
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de) This class create JoBims from
 *         Dependencies, from a Parser (e.g. StanfordParser)
 * 
 */
public class DependencyAnnotator extends JCasAnnotator_ImplBase {
	// public static final String PARAM_TARGET_ANNOTATION = "TargetAnnotation";
	// @ConfigurationParameter(name = PARAM_TARGET_ANNOTATION,mandatory=false)
	// private Class<Annotation> targetAnnotationClass=null;
	public static final String PARAM_WITH_HOLE = "WithHole";
	@ConfigurationParameter(name = PARAM_WITH_HOLE, mandatory = false)
	private boolean withHole=false;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		if (withHole) {
			for (Dependency d : select(aJCas, Dependency.class)) {
				addJobim(aJCas, d.getGovernor(), d.getDependent(), d.getDependencyType(), 0);
				addJobim(aJCas, d.getDependent(), d.getGovernor(), d.getDependencyType(), 1);
			}
		} else {
			for (Dependency d : select(aJCas, Dependency.class)) {
				addJobim(aJCas, d.getGovernor(), d.getDependent(), d.getDependencyType());
				addJobim(aJCas, d.getDependent(), d.getGovernor(), "-" + d.getDependencyType());
			}
		}
	}

	public void addJobim(JCas jcas, Token t1, Token t2, String relation, int holePosition) {
		System.out.println(t1.getCoveredText());
		System.out.println(t2.getCoveredText());
		JoBim jb = createJobim(jcas, t1, t2, relation);
		jb.setHole(holePosition);
	}

	public void addJobim(JCas jcas, Token t1, Token t2, String relation) {
		createJobim(jcas, t1, t2, relation);
	}

	public JoBim createJobim(JCas jcas, Token t1, Token t2, String relation) {
		JoBim jb = new JoBim(jcas, t1.getBegin(), t1.getEnd());
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

