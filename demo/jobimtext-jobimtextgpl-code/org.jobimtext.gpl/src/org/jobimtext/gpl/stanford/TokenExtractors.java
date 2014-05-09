package org.jobimtext.gpl.stanford;
/*******************************************************************************
 * Copyright 2012
 * FG Language Technologie
 * Technische UniversitŠt Darmstadt
 *
 * Licensed under the GPL, Version 3.0 (the "License");
 ******************************************************************************/


import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;


import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TokenExtractors {

	/**
	 * The TokenPos extractor extracts the Token and POS tag of a token
	 * 
	 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
	 * 
	 */
	public static class TokenPos extends JobimAnnotationExtractor {
		public TokenPos(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			Token t = (Token) a;
			String word = t.getCoveredText();
			
			if (t.getPos() != null) {
				return word + conf.attributeDelimiter + t.getPos().getPosValue();
			} else {
				return word + conf.attributeDelimiter + "UNK";
			}
		}
	}

	/**
	 * The LemmaPos extractor extracts the Lemma and POS tag of a token and cuts
	 * the POS tasg to length 2 except for NN* and NNP* which will be converted
	 * to NN and NP
	 * 
	 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
	 * 
	 */
	public static class LemmaPosNPNN extends JobimAnnotationExtractor {
		public LemmaPosNPNN(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			Token t = (Token) a;
			String word;
			if (t.getLemma() != null) {
				word = t.getLemma().getValue();
			} else {
				word = t.getCoveredText();
			}
			if (t.getPos() != null) {
				String pos = t.getPos().getPosValue();
				if (pos.length() > 2 && pos.matches("[VBN].*")) {
					if (pos.matches("NNP.*")) {
						pos = "NP";
					} else if (pos.matches("NN")) {
						pos = "NN";
					} else {
						pos = pos.substring(0, 2);
					}
				}
				return word + conf.attributeDelimiter + pos;
			} else {
				return word + conf.attributeDelimiter + "UNK";
			}
		}

	}

	public static class LemmaLowercasePosNPNN extends JobimAnnotationExtractor {
		public LemmaLowercasePosNPNN(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			Token t = (Token) a;
			String word;
			if (t.getLemma() != null) {
				word = t.getLemma().getValue();
			} else {
				word = t.getCoveredText();
			}
			word = word.toLowerCase();
			if (t.getPos() != null) {
				String pos = t.getPos().getPosValue();
				if (t.getLemma() != null && pos.length() > 2 && pos.matches("[VBN].*")) {
					if (pos.matches("NNP.*")) {
						pos = "NP";
					} else if (pos.matches("NN")) {
						pos = "NN";
					} else {
						pos = pos.substring(0, 2);
					}
				}
				return word + conf.attributeDelimiter + pos;
			} else {
				return word + conf.attributeDelimiter + "UNK";
			}
		}

	}
	/**
	 * The LemmaPos extractor extracts the Lemma and POS tag of a token
	 * 
	 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
	 * 
	 */
	public static class LemmaPos extends JobimAnnotationExtractor {
		public LemmaPos(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			Token t = (Token) a;
			String word;
			if (t.getLemma() != null) {
				word = t.getLemma().getValue();
			} else {
				word = t.getCoveredText();
			}
			if (t.getPos() != null) {
				String pos = t.getPos().getPosValue();
				if (t.getLemma() != null && pos.length() > 2 && pos.matches("[VBN].*")) {
					pos = pos.substring(0, 2);
				}
				return word + conf.attributeDelimiter + pos;
			} else {
				return word + conf.attributeDelimiter + "UNK";
			}
		}

	}

	/**
	 * returns the covered text of the Annotation
	 * 
	 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
	 * 
	 */
	public static class CoveredText extends JobimAnnotationExtractor {

		public CoveredText(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			return a.getCoveredText();
		}

	}

	public static class CoveredTextLowercase extends JobimAnnotationExtractor {

		public CoveredTextLowercase(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			return a.getCoveredText().toLowerCase();
		}

	}
}
