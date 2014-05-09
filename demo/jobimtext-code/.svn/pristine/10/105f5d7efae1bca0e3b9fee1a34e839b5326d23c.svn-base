package org.jobimtext.holing.segmenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;
import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;


public class SentenceTokenAnnotatorLucene extends JCasAnnotator_ImplBase {
	
  private SentenceDetectorME sentenceDetectorME;
  private TokenizerME tokenizerME;
    
  // FIXME: make path to model configuration parameter    
  private String sentenceModelPath = "../org.jobimtext.thirdparty/lib/en-sent.bin";
  //private String tokenModelPath = "../org.jobimtext.thirdparty/lib/en-token.bin";
  
  private Analyzer snowball;
  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    try {
      FileInputStream is = new FileInputStream(new File(sentenceModelPath));
      SentenceModel sentenceModel = new SentenceModel(is);
      sentenceDetectorME = new SentenceDetectorME(sentenceModel);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    snowball = new WatsonAnalyzer(Version.LUCENE_35);    
  }

  public void process(JCas aJCas) {
    String aText = aJCas.getDocumentText();
    for (Span sSpan : sentenceDetectorME.sentPosDetect(aText)) {
      Sentence s = createSentence(aJCas, sSpan.getStart(), sSpan.getEnd());
      if (s != null) {
        try {
          TokenStream ts = snowball.tokenStream("", new StringReader(s.getCoveredText()));
          while (ts.incrementToken()){
            OffsetAttribute oa = ts.getAttribute(OffsetAttribute.class);
            int start = oa.startOffset(); int end = oa.endOffset();
            createToken(aJCas, start + sSpan.getStart(), end + sSpan.getStart());
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private Sentence createSentence(JCas aJCas, int begin, int end) {
    if (isEmpty(begin, end)) return null;
    Sentence sentence = new Sentence(aJCas, begin, end);
    sentence.addToIndexes();
    return sentence;
  }
  
  private void createToken(JCas aJCas, int begin, int end) {
    if (isEmpty(begin, end)) return;
    Token token = new Token(aJCas, begin, end);
    token.addToIndexes();
  }
  
  public boolean isEmpty(int aBegin, int aEnd) {
    return aBegin > aEnd;
  }
  
}
