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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.jobimtext.holing.type.Jo;
import org.jobimtext.holing.type.JoBim;
import org.jobimtext.holing.type.Sentence;
import org.jobimtext.holing.type.Token;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class JogramHolingAnnotator extends JCasAnnotator_ImplBase {
  
  private static final String CONFIG_FILE_PATH = "configFilePath";
  
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
    logger.log(Level.FINE, "JogramHolingAnnotator:initialize");    
    String configFilePath = (String)aContext.getConfigParameterValue(CONFIG_FILE_PATH);
    Document doc = readConfiguration(configFilePath);
    if (doc != null) buildConfigurations(doc);
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // for all relations with hole and length, process(cas, hole, length)
    for (Relation r: relations) {
      process(aJCas, r);
    }
  }

  public void process(JCas aJCas, Relation rel) {
    // for each sentence collect all Jo annotations and
    // store them in a hashmaps by their start and ending
    // positions in the the sentence
    for (Sentence s : select(aJCas, Sentence.class)) {
//    	System.out.println(s.getCoveredText());
      Map<Integer, List<Jo>> beginMap = new HashMap<Integer, List<Jo>>();
      Map<Integer, List<Jo>> endMap = new HashMap<Integer, List<Jo>>();
      for (Jo j : selectCovered(Jo.class, s)) {        
        int begin = j.getBegin(); 
        if (!beginMap.containsKey(begin)) {
          beginMap.put(begin, new ArrayList<Jo>());
        }
        beginMap.get(begin).add(j);        
        int end = j.getEnd();
        if (!endMap.containsKey(end)) {
          endMap.put(end, new ArrayList<Jo>());
        }
        endMap.get(end).add(j);
      }
      
      // TODO: Implement this better.  This is real ugly but works
      // for jograms up to length 3 with just one skip token.
      // for each Jo capture @_Jo, Jo_@, @_Jo_Jo, Jo_@_Jo, Jo_Jo_@
      for (Jo j : selectCovered(Jo.class, s)) {
        
        List<Jo> leftJos = getLeftJos(j, endMap, s.getBegin());
        if (leftJos != null) {
          for (Jo l: leftJos) {
            if (rel.length == 2) {
              LinkedList<Annotation> ngram = new LinkedList<Annotation>();
              ngram.add(l); ngram.add(j);
              addJobimNgram(aJCas, ngram, rel.model, 2, rel.length, 0, rel);              
            }
            List<Jo>lleftJos = getLeftJos(l, endMap, s.getBegin());
            if (lleftJos != null && rel.length > 2) {
              for (Jo ll: lleftJos) {
                LinkedList<Annotation> ngram = new LinkedList<Annotation>();
                ngram.add(ll); ngram.add(l); ngram.add(j);
                addJobimNgram(aJCas, ngram, rel.model, 2, rel.length, rel.skip, rel);
                addJobimNgram(aJCas, ngram, rel.model, 3, rel.length, rel.skip, rel);
              }
            } 
          }
        }
        
        List<Jo> rightJos = getRightJos(j, beginMap, s.getEnd());
        if (rightJos != null) {
          for (Jo r: rightJos) {   
            if (rel.length == 2) {
              LinkedList<Annotation> ngram = new LinkedList<Annotation>();
              ngram.add(j); ngram.add(r);
              addJobimNgram(aJCas, ngram, rel.model, 1, rel.length, 0, rel);
            }
            List<Jo> rrightJos = getRightJos(r, beginMap, s.getEnd());
            if (rrightJos != null && rel.length > 2) {
              for (Jo rr: rrightJos) {
                LinkedList<Annotation> ngram = new LinkedList<Annotation>();
                ngram.add(j); ngram.add(r); ngram.add(rr);
                addJobimNgram(aJCas, ngram, rel.model, 1, rel.length, rel.skip, rel);
              }
            }
          }
        }
        
      }
      
    }
  }
  
  private List<Jo> getLeftJos(Jo j, Map<Integer, List<Jo>> map, int end) {
    List<Jo> jos = null;
    int index = j.getBegin();
    while (jos == null && index > end) {
      jos = map.get(index);
      index--;
    }
    return jos;
  }
  
  private List<Jo> getRightJos(Jo j, Map<Integer, List<Jo>> map, int end) {
    List<Jo> jos = null;
    int index = j.getEnd();
    while (jos == null && index < end) {
      jos = map.get(index);
      index++;
    }
    return jos;
  }
    
  private void addJobimNgram(JCas jcas, LinkedList<Annotation> al, 
      String model, int hole, int length, int skip, Relation r) {
    if (al.size() != length) {
      return;
    }    
    // if we are skipping, then ignore grams where we can't 
    // place a skip token (we never skip first or last tokens)
    if (skip > 0 && (length-hole <= skip) && (hole-1 <= skip)) {
      return;
    }
    
    JoBim jb = new JoBim(jcas);
    jb.setModel(model);  // TODO: this call breaks the demo
    FSArray array = new FSArray(jcas, length - 1);
    int j = 0;
    for (int i = 0; i < al.size(); i++) {
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
      else {
        array.set(j++, al.get(i));
      }
    }
    jb.setRelation(r.patterns[hole-1]);
    jb.setValues(array);
    jb.addToIndexes();
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
      String joLengthString = document.getAttributes().getNamedItem("ngramLength").getTextContent();
      
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
      
      name = name+"_"+joLengthString+"_"+length+"_"+hole+"_"+Math.max(skip,0);
      relations.add(new Relation(name, length, hole, skip));      
    }
  } 
  
  private class Relation {
    int length, hole, skip;
    String model;
    String patterns[];
    Relation(String model, int length, int hole, int skip) {
      this.model = model;
      this.length = length;
      this.hole = hole;
      this.skip = skip;
      patterns = new String[length];
      for (int i=0; i<length; i++) {
        StringBuilder sb = new StringBuilder();
        for (int j=0; j<length-1; j++) {
          sb.append("Jo_");
        }
        sb.append("Jo");
        sb.replace(i*3, (i*3)+2, "@");
        if (i==0 && skip > 0) {
          sb.replace(2, 4, "*");
        }
        else if (i==length-1 && skip > 0) {
          int j = length-2;
          sb.replace(j*3, (j*3)+2, "*"); 
        }
        patterns[i] = sb.toString();
      }
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
