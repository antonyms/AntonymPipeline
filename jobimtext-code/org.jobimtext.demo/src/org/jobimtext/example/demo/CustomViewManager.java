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
import org.jobimtext.holing.type.JoBim;

import com.ibm.bluej.util.common.visualization.BasicJTree;

public abstract class CustomViewManager implements TreeSelectionListener {
	public abstract String getTabName();
	public abstract void changedSelectedJo(AnnoLabel a);
	
	private BasicJTree jobimView;
	protected BasicJTree view;	
	protected JobimAnnotationExtractor extractor;
	protected IThesaurusMap<String, String> dt;
	
	public void initialize(BasicJTree jobimView, BasicJTree view, JobimAnnotationExtractor extractor, IThesaurusMap<String, String> dt) {
		this.jobimView = jobimView;
		this.view = view;
		this.extractor = extractor;
		this.dt = dt;
	}
	
	public void clear() {
		view.top.removeAllChildren();
		view.top.setUserObject("");
		view.refresh(null);
	}
	
	public void setJoBims(Map<Annotation, ArrayList<JoBim>> jo2Bims) {
		
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jobimView.getLastSelectedPathComponent();

		// if nothing is selected
		if (node == null) {
			view.top.removeAllChildren();
			view.refresh(null);
			return;
		}

		if (node.getUserObject() instanceof AnnoLabel) {
			changedSelectedJo((AnnoLabel)node.getUserObject());
		}
	}
}
