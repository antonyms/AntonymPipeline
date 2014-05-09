package org.jobimtext.holing.segmenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;


public class SentenceTokenAnnotator extends JCasAnnotator_ImplBase {
	
  private SentenceDetectorME sentenceDetectorME;
  private TokenizerME tokenizerME;
    
  // FIXME: make path to model configuration parameter    
  private String sentenceModelPath = "../org.jobimtext.thirdparty/lib/en-sent.bin";
  private String tokenModelPath = "../org.jobimtext.thirdparty/lib/en-token.bin";
  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    /*sentenceModelPath = this.getClass().getResource("en-sent.bin").getPath();
    tokenModelPath = this.getClass().getResource("en-token.bin").getPath();*/
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

    try {
      FileInputStream is = new FileInputStream(new File(tokenModelPath));
      TokenizerModel tokenizerModel = new TokenizerModel(is);
      tokenizerME = new TokenizerME(tokenizerModel);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }

  public void process(JCas aJCas) {
    String aText = aJCas.getDocumentText();
    for (Span sSpan : sentenceDetectorME.sentPosDetect(aText)) {
      createSentence(aJCas, sSpan.getStart(), sSpan.getEnd());
      for (Span tSpan : tokenizerME.tokenizePos(aText.substring(sSpan.getStart(), sSpan.getEnd()))) {
        createToken(aJCas, tSpan.getStart() + sSpan.getStart(), tSpan.getEnd() + sSpan.getStart());
      }
    }
  }
  
  private void createSentence(JCas aJCas, int begin, int end) {
    if (isEmpty(begin, end)) return;
    Sentence sentence = new Sentence(aJCas, begin, end);
    sentence.addToIndexes();
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
