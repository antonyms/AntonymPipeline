package org.jobimtext.example.demo;

import java.util.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;


import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.api.map.IThesaurusMap;
import org.jobimtext.example.demo.JoBimDemo.AnnoLabel;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;

import com.ibm.bluej.util.common.*;
import com.ibm.bluej.util.common.visualization.*;

public class SensesViewManager implements TreeSelectionListener {

  private BasicJTree jobimView;
  private BasicJTree view;
  
private IThesaurusMap<String, String> dt;
private JobimAnnotationExtractor extractor;
    
  public SensesViewManager(
      BasicJTree jobimView, 
      BasicJTree view, 
      IThesaurusMap<String, String> dt, 
      JobimAnnotationExtractor extractor) {
    this.jobimView = jobimView;
    this.view = view;
    this.dt = dt;
    this.extractor = extractor;
  }
  
  public void valueChanged(TreeSelectionEvent event) {
	  /*if (dt.getSenses() == null || dt.getPriors() == null) {
		  view.top.removeAllChildren();
	      view.refresh(null);
		  return;
	  }*/
    // remove highlights from sentence
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobimView
        .getLastSelectedPathComponent();
    
    // if nothing is selected
    if (node == null) {
      view.top.removeAllChildren();
      view.refresh(null);
      return;
    }
    try {
	    if (node.getUserObject() instanceof AnnoLabel) {
	      Annotation jo = ((AnnoLabel) node.getUserObject()).anno;
	      view.top.removeAllChildren();
	      view.top.setUserObject(extractor.extract(jo));
	            
	      int count = 0;
	      String joString = extractor.extract(jo);
	      Map<Integer, List<String>> senses = dt.getSenses(joString);
	      Map<String,Double> priors = dt.getSimilarTerms(joString,JoBimDemo.MAX_EXPANSIONS);
	      Map<Integer, Double> senseScores = new HashMap<Integer,Double>();
	      for (Map.Entry<Integer, List<String>> e : senses.entrySet()) {
	    	  if (e.getValue().isEmpty())
	    		  continue;
	    	  double sumScore = 0;
	    	  for (String s : e.getValue()) {
	    		  sumScore += Lang.NVL(priors.get(s), 0.0);
	    	  }
	    	  senseScores.put(e.getKey(), sumScore/e.getValue().size());
	      }
	      ArrayList<Pair<Integer,Double>> senseScoresSorted = HashMapUtil.toPairs(senseScores);
	      SecondPairComparator.sortR(senseScoresSorted);
	      for (Pair<Integer,Double> sense : senseScoresSorted) {
	    	int key = sense.first;
	        List<String> l = senses.get(key);
	        DefaultMutableTreeNode senseNode = BasicJTree
	            .makeGrouping("Sense "+count++);
	        view.top.add(senseNode);
	        for (String s : l) {
	          DefaultMutableTreeNode expNode = new DefaultMutableTreeNode(s);
	          senseNode.add(expNode);
	        }
	      }
	      
	      view.refresh(new FunST<DefaultMutableTreeNode, Boolean>() {
	        public Boolean f(DefaultMutableTreeNode n) {
	          return n.getLevel() < 2;
	        }
	      });
	    }
    } catch (UnsupportedOperationException e) {
    	view.top.removeAllChildren();
    	view.top.setUserObject("Not supported");
    	view.refresh(null);
    }
  }
  
}
