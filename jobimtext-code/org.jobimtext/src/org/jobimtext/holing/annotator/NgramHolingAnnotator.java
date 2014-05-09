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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class NgramHolingAnnotator extends JCasAnnotator_ImplBase {
  
  private static final String CONFIG_FILE_PATH = "configFilePath";
  private static final String NGRAM_REL = "gram";
  
  private Logger logger;  
  private List<Relation> relations;
  
  /**
   * Initializes the annotator.
   * @param aContext UIMA context
   */
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    logger = aContext.getLogger();
    logger.log(Level.FINE, "FeatureCounter:initialize");    
        
    String configFilePath = (String)aContext.getConfigParameterValue(CONFIG_FILE_PATH);
    Document doc = readConfiguration(configFilePath);
    if (doc != null) buildConfigurations(doc);
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // for all relations with hole and length, process(cas, hole, length)
    for (Relation r: relations) {
      if (r.hole > 0) {
        process(aJCas, r.model, r.hole, r.length, r.skip, r);
      }
      else {
        for (int i=1; i<r.length+1; i++) {
          process(aJCas, r.model, i, r.length, r.skip, r);
        }
      }
    }
  }

  public void process(JCas aJCas, String model, int hole, 
      int length, int skip, Relation r) {
    LinkedList<Annotation> al = new LinkedList<Annotation>();    
    // read the serialized document as an array of bytes and 
    // transform to string and do simple tokenizing for now
    for (Sentence s : select(aJCas, Sentence.class)) {
      for (int i = 1; i < hole; i++) {
        al.add(new Token(aJCas));
      }
      for (Token t : selectCovered(Token.class, s)) {
        al.add(t);
        addJobimNgram(aJCas, al, model, hole, length, skip, r);
      }
      for (int i = 0; i < length - hole; i++) {
        al.add(new Token(aJCas));
        addJobimNgram(aJCas, al, model, hole, length, skip, r);
      }
    }
  }

  private void addJobimNgram(JCas jcas, LinkedList<Annotation> al, 
      String model, int hole, int length, int skip, Relation r) {
    // if the gram is not large enough ignore
    if (al.size() != length) {
      return;
    }
    // if we are skipping, then ignore grams where we can't 
    // place a skip token (we never skip first or last tokens)
    if (skip > 0 && (length-hole <= skip) && (hole-1 <= skip)) {
      al.removeFirst();
      return;
    }
    JoBim jb = new JoBim(jcas);
    jb.setModel(model);
    FSArray array = new FSArray(jcas, length - 1);
    int j = 0;
    for (int i = 0; i < al.size(); i++) {
      // set the hole token
      if (i == (hole - 1)) {
        jb.setKey(al.get(i));
        jb.setBegin(al.get(i).getBegin());
        jb.setEnd(al.get(i).getEnd());
        jb.setHole(hole-1);
      } 
      // add skip tokens
      else if(skip > 0 && Math.abs((hole-1)-i) == skip) {
        Token t = new Token(jcas);
        t.setSkip(true);
        array.set(j++, t);
      }
      // add gram tokens
      else {
        array.set(j++, al.get(i));
      }
    }
    jb.setRelation(length+NGRAM_REL+"_"+hole+(skip>0?("_"+skip):""));
    jb.setValues(array);
    jb.addToIndexes();
    al.removeFirst();
  }
  
  private Document readConfiguration(String configFile) {
    Document doc = null;
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File(configFile));
      doc.getDocumentElement().normalize();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return doc;
  }
  
  private void buildConfigurations(Document configDoc) {
    relations = new ArrayList<Relation>();
    NodeList relationNodes = configDoc.getElementsByTagName("ngramRelation");
    for (int i = 0; i < relationNodes.getLength(); i++) {
      Element document = (Element) relationNodes.item(i);
      String name = document.getAttributes().getNamedItem("relationPrefix").getTextContent();
      String lengthString = document.getAttributes().getNamedItem("ngramLength").getTextContent();
      
      String holeString = "*";
      Node holeNode = document.getAttributes().getNamedItem("hole");
      if (holeNode != null) {
        holeString = holeNode.getTextContent();
      }
      
      String skipString = "-1";
      Node skipNode = document.getAttributes().getNamedItem("skip");
      if (skipNode != null) {
        skipString = skipNode.getTextContent();
      }
      
      int length = validLength(lengthString);
      int hole = validHole(length, holeString);
      int skip = validSkip(length, hole, skipString);
      
      name = name+"_"+length+"_"+hole+"_"+Math.max(skip, 0);
      relations.add(new Relation(name, length, hole, skip));      
    }
  } 
  
  private class Relation {
    int length, hole, skip;
    String model;
    Relation(String model, int length, int hole, int skip) {
      this.model = model;
      this.length = length;
      this.hole = hole;
      this.skip = skip;
    }
  }
  
  private int validLength(String lengthString) {
    int length;
    try {
      length = Integer.parseInt(lengthString);
    } catch (NumberFormatException e) {
      length = 2;
    }
    return length;
  }
  
  private int validHole(int length, String holeString) {
    int hole;
    try {
      hole = ("*".equals(holeString)) ? 0 : Integer.parseInt(holeString);
      if (hole > length) {
        hole = 1;
      }
    } catch (NumberFormatException e) {
      logger.log(Level.SEVERE, "Invalid hole location "+holeString);
      hole = 0;
    }
    return hole;
  }
  
  private int validSkip(int length, int hole, String skipString) {
    if (length < 3) return -1;
    int skip;
    try {
      skip = Integer.parseInt(skipString);
      if (skip >= length || skip <= 0) {
        skip = -1;
      }
    } catch (NumberFormatException e) {
      skip = -1;
    }
    return skip;
  }

}
