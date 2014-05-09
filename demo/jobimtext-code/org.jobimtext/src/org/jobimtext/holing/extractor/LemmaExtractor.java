package org.jobimtext.holing.extractor;



import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;
import org.jobimtext.holing.extractor.JobimExtractorConfiguration;
import org.jobimtext.holing.type.Lemma;


public class LemmaExtractor {

	/**
	 * returns the covered text of the Annotation
	 */
	public static class CoveredText extends JobimAnnotationExtractor {
		public CoveredText(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {

			return a.getCoveredText();
		}
	}

	/**
	 * return the token lowercased and a * if tokens are skipped
	 * 
	 * @author mriedl
	 * 
	 */
	public static class LemmaLowercase extends JobimAnnotationExtractor {
		public LemmaLowercase(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			// make sure we don't have any invalid XML characters
			// text = text.replaceAll("&(?!(?:[a-z]+|#[0-9]+|#x[0-9a-f]+);)",
			// "&amp;");
			// text = text.replaceAll("<(?![_:a-z][-._:a-z0-9]*\\b[^<>]*>)",
			// "&lt;");
			// text = text.replaceAll("[\u0000-\u001f]", "");
			Lemma t = (Lemma) a;
	//		System.out.print("Lemma = " + t.getBegin() + ", " + t.getEnd());
		//	System.out.println(" " + t.getValue());
			String text = t.getValue();
			text = text.toLowerCase();
			return text;
		}
	}
	
	public static class LemmaText extends JobimAnnotationExtractor {
		public LemmaText(JobimExtractorConfiguration configuration) {
			super(configuration);
		}

		public String extract(Annotation a) {
			Lemma t = (Lemma) a;
			String text = t.getValue();
			return text;
		}
	}

}

