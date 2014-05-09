package org.jobimtext.example.demo;

import java.io.IOException;
import java.util.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.api.map.IThesaurusMap;
import org.jobimtext.example.demo.JoBimDemo.AnnoLabel;
import org.jobimtext.holing.extractor.JobimAnnotationExtractor;

import com.ibm.bluej.util.common.*;
import com.ibm.bluej.util.common.visualization.*;

import edu.antonym.MarketMatrixTensor;
import edu.antonym.RawPILSAVec;
import edu.antonym.prototype.VectorEmbedding;
import edu.antonym.prototype.Vocabulary;

public class SensesRankingViewManager implements TreeSelectionListener {

  private BasicJTree jobimView;
  private BasicJTree view;
  
  private IThesaurusMap<String, String> dt;
  private JobimAnnotationExtractor extractor;

  public VectorEmbedding embeddingTensor;
  public VectorEmbedding embeddingPILSA;
  public Vocabulary voc;
  public HashMap<String,Double> map = new HashMap<String,Double>();
    
  public SensesRankingViewManager(
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
		  	
	      embeddingTensor = new MarketMatrixTensor(0);
	      embeddingPILSA = new RawPILSAVec(false);
	      
	      for (Pair<Integer,Double> sense : senseScoresSorted) {
	    	int key = sense.first;
	        List<String> l = senses.get(key);
	        DefaultMutableTreeNode senseNode = BasicJTree
	            .makeGrouping("Sense "+count++);
	        view.top.add(senseNode);
	        for (String s : l) {
	        	DefaultMutableTreeNode vectorNode1 = BasicJTree.makeGrouping("MarketMatrixTensor");
	        	ArrayList<String> ss = processWordsense(joString,s,1);
	        	//DefaultMutableTreeNode expNode1 = new DefaultMutableTreeNode(ss);
	        	int size = ss.size();
	        	if(size<=20){
	        		for(int i=0;i<size;i++){
	        			vectorNode1.add(new DefaultMutableTreeNode(ss.get(i)));
	        		}
	        	}
	        	else{
	        		for(int i=0;i<10;i++){
	        			vectorNode1.add(new DefaultMutableTreeNode(ss.get(i)));
	        		}
	        		vectorNode1.add(new DefaultMutableTreeNode("..."));
	        		for(int i=size-10;i<size;i++){
	        			vectorNode1.add(new DefaultMutableTreeNode(ss.get(i)));
	        		}
	        	}
	        	//vectorNode1.add(expNode1);
	        	senseNode.add(vectorNode1);
	        	
	        	DefaultMutableTreeNode vectorNode2 = BasicJTree.makeGrouping("RawPILSAVector");
	        	ArrayList<String> sss = processWordsense(joString,s,2);
	        	size = sss.size();
	        	if(size<=20){
	        		for(int i=0;i<size;i++){
	        			vectorNode2.add(new DefaultMutableTreeNode(sss.get(i)));
	        		}
	        	}
	        	else{
	        		for(int i=0;i<10;i++){
	        			vectorNode2.add(new DefaultMutableTreeNode(sss.get(i)));
	        		}
	        		vectorNode2.add(new DefaultMutableTreeNode("..."));
	        		for(int i=size-10;i<size;i++){
	        			vectorNode2.add(new DefaultMutableTreeNode(sss.get(i)));
	        		}
	        	}
	        	//DefaultMutableTreeNode expNode2 = new DefaultMutableTreeNode(sss);
	        	//vectorNode2.add(expNode2);
	        	senseNode.add(vectorNode2);
	        	//System.out.println("senseNode print: " + expNode);
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
    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
  }
  
  public String plainword(String s){
	  int index = s.indexOf("#");
	  return s.substring(0,index);
	
  }
  public ArrayList<String> processWordsense(String target,String s, int choice) throws IOException{
	  	if(choice==1){
	  		voc = embeddingTensor.getVocab();
	  	}
	  	else if(choice==2){
	  		voc = embeddingPILSA.getVocab();
	  	}
	  	else{
	  		System.out.println("did not find corresponding embedding");
	  		System.exit(0);
	  	}

	  	String[] words = s.split(", ");
	  	String ss = null;
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> rank = new ArrayList<String>();
		
		int t = voc.lookupWord(plainword(target));
		int OOV = voc.OOVindex();
		if(t!=OOV){
			for(int i=0;i<words.length;i++){
				int index = voc.lookupWord(plainword(words[i]));
				if(index!=OOV){
					if(choice==1){
						double sim = embeddingTensor.similarity(t, index);
						map.put(words[i], sim);
					}
					else if(choice==2){
						double sim = embeddingPILSA.similarity(t, index);
						map.put(words[i], sim);
					}
					result.add(words[i]);
				}
				else{
					//System.out.println("cannot find: "+ words[i]);
				}
			}
			Collections.sort(result,new EntityComparator());
			StringBuilder sb = new StringBuilder();
			for(int j=0;j<result.size();j++){
				//double value = Math.floor(map.get(result.get(j)) * 100000) / 100000;
				//sb.append(result.get(j)+":"+value+ " ");
				rank.add(result.get(j)+": "+map.get(result.get(j)));
			}
		}
		map.clear();
	    return rank;
	  
  }
  public class EntityComparator implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			return NumberUtils.compare(map.get(o2), map.get(o1));
		}
		
	}
  
}
