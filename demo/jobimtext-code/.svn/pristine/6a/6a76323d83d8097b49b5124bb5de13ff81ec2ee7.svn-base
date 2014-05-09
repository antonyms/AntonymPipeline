package org.jobimtext.holing.postagger;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;
import static org.apache.uima.fit.util.JCasUtil.toText;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.POS;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;


public class POSTaggerAnnotator extends JCasAnnotator_ImplBase {
  
  private POSTaggerME posTaggerME;
  
  private String posTaggerModelPath = "lib/en-pos-maxent.bin";

  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);    
    try {
      File file = new File(posTaggerModelPath);
      FileInputStream is = new FileInputStream(file);
      POSModel model = new POSModel(is);
      posTaggerME = new POSTaggerME(model);      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    for (Sentence sentence : select(aJCas, Sentence.class)) {
      List<Token> tokens = selectCovered(aJCas, Token.class, sentence);
      String[] tokenTexts = toText(tokens).toArray(new String[tokens.size()]);
      String[] tags = posTaggerME.tag(tokenTexts);
      int i = 0;
      for (Token t : tokens) {
        POS pos = new POS(aJCas, t.getBegin(), t.getEnd());
        pos.setPosValue(tags[i]);
        pos.addToIndexes();
        t.setPOS(pos);
        i++;
      }
    }    
  }
  
}
