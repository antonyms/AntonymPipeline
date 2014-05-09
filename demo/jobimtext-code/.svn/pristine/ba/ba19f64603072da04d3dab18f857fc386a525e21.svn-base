package org.jobimtext.example.demo;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;


import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.api.map.IThesaurusMap;
import org.jobimtext.example.demo.JoBimDemo.AnnoLabel;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;

import com.ibm.bluej.util.common.*;
import com.ibm.bluej.util.common.visualization.*;

public class ExpansionsViewManager implements TreeSelectionListener {

  private BasicJTree jobimView;
  private BasicJTree view;
  /*private DistributionalSemanticsAPI distSim;*/
  
  private JobimAnnotationExtractor extractor;
private IThesaurusMap<String, String> dt;
    
  public ExpansionsViewManager(
      BasicJTree jobimView, 
      BasicJTree view, 
      IThesaurusMap<String, String> dt,
      JobimAnnotationExtractor extractor) {
    this.jobimView = jobimView;
    this.view = view;
		this.dt = dt;
    this.extractor = extractor;
  }
  
  public void valueChanged(TreeSelectionEvent e) {

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobimView
        .getLastSelectedPathComponent();
    
    // if nothing is selected
    if (node == null) {
      view.top.removeAllChildren();
      view.refresh(null);
      return;
    }

    if (node.getUserObject() instanceof AnnoLabel) {
      Annotation jo = ((AnnoLabel) node.getUserObject()).anno;           
      view.top.removeAllChildren();
      view.top.setUserObject(extractor.extract(jo));
      //DefaultMutableTreeNode priorExp = BasicJTree
      //    .makeGrouping("Prior Expansion");
      //view.top.add(priorExp);
      DefaultMutableTreeNode priorExp = view.top;
      ArrayList<Pair<String, Double>> ps = 
          HashMapUtil.toPairs(dt.getSimilarTerms(extractor.extract(jo),JoBimDemo.MAX_EXPANSIONS));//.getPriors().get(jo));
      SecondPairComparator.sortR(ps);
      for (Pair<String, Double> p : ps) {
        DefaultMutableTreeNode expNode = 
            new DefaultMutableTreeNode(
                Lang.dblStr(p.second) + ": " + p.first);
        priorExp.add(expNode);
        // look up the contexts associated with this term
        Map<String, MutableDouble> m = SparseVectors.fromImmutable(dt.getTermContextsScores(p.first,JoBimDemo.MAX_EXPANSIONS));
        SparseVectors.trimToTopN(m, 40);
        for (Map.Entry<String, MutableDouble> ce : m.entrySet()) {
          expNode.add(new DefaultMutableTreeNode(
              Lang.dblStr(ce.getValue().value) + ": " + ce.getKey()));
        }
      }
      //DefaultMutableTreeNode contextExp = BasicJTree
      //    .makeGrouping("In Context Expansion");
      //view.top.add(contextExp);
      //TODO CONTEXTUALIZATION SHOULD BE HERE
      /*ps = HashMapUtil.toPairs(dt.getTermContextsScores(extractor.extract(jo),JoBimDemo.MAX_EXPANSIONS));
      SecondPairComparator.sortR(ps);
      for (Pair<String, Double> p : ps) {
        DefaultMutableTreeNode expNode = 
            new DefaultMutableTreeNode(
                Lang.dblStr(p.second) + ": " + p.first);
        contextExp.add(expNode);
        Map<String, MutableDouble> m =  SparseVectors.fromImmutable(dt.getTermContextsScores(p.first,JoBimDemo.MAX_EXPANSIONS));
        SparseVectors.trimToTopN(m, 40);
        for (Map.Entry<String, MutableDouble> ce : m.entrySet()) {
          expNode.add(new DefaultMutableTreeNode(
              Lang.dblStr(ce.getValue().value) + ": " + ce.getKey()));
        }
      }*/
      view.refresh(new FunST<DefaultMutableTreeNode, Boolean>() {
        public Boolean f(DefaultMutableTreeNode n) {
          return n.getLevel() < 1;
        }
      });
    }
  }
  
}
