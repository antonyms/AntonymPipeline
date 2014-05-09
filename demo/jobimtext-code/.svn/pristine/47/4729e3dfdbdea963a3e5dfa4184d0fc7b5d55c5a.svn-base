/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.util.common.visualization;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.ibm.bluej.util.common.*;

public class BasicJTree extends JTree {
	private DefaultTreeModel model;
	public DefaultMutableTreeNode top;
	
	public BasicJTree(Object topNode) {
		top =  new DefaultMutableTreeNode(topNode);
		model = new DefaultTreeModel(top);
		this.setModel(model);
		this.setEditable(false);
		NodeDetailRenderer renderer = new NodeDetailRenderer();
        Icon noIcon = null;
        renderer.setLeafIcon(noIcon);
        renderer.setClosedIcon(noIcon);
        renderer.setOpenIcon(noIcon);
        this.setCellRenderer(renderer);
	}
	
	public void refresh(FunST<DefaultMutableTreeNode, Boolean> expandIt) {
		model.reload(top);
		DefaultMutableTreeNode currentNode = top.getNextNode();
		while (currentNode != null) {
			if (expandIt == null || expandIt.f(currentNode)) {
				this.expandPath(new TreePath(currentNode.getPath()));
			}
		    currentNode = currentNode.getNextNode();
		}
		this.repaint();
	}
	
	public static DefaultMutableTreeNode makeGrouping(String label) {
		return new DefaultMutableTreeNode(new GroupingLabel(label));
	}
	public static DefaultMutableTreeNode makeHighlighted(String label, Color color) {
		return new DefaultMutableTreeNode(new HighlightLabel(label, color));
	}
	public static boolean isGroupNamed(DefaultMutableTreeNode n, String label) {
		return n.getUserObject() instanceof GroupingLabel && ((GroupingLabel)n.getUserObject()).label.equals(label);
	}
	
	static class NodeDetailRenderer extends DefaultTreeCellRenderer {
		public Component getTreeCellRendererComponent(JTree tree, Object value, 
				boolean sel, boolean expanded, boolean leaf, int row,  boolean hasFocus) 
		{

			super.getTreeCellRendererComponent(
			                tree, value, sel,
			                expanded, leaf, row,
			                hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            
			if (node.getUserObject() instanceof Renderable) {
				((Renderable)node.getUserObject()).render(this);
			} else {
				this.setOpaque(false);
			}
			
			return this;
		}
	}
	
	public interface Renderable {
		void render(DefaultTreeCellRenderer comp);
	}
	
	public static class GroupingLabel implements Renderable {
		public String label;
		public String toString() {return label;}
		public GroupingLabel(String label) {this.label = label;}
		public void render(DefaultTreeCellRenderer comp) {
			comp.setOpaque(false);
			comp.setForeground(Color.BLUE);
		}
	}
	public static class HighlightLabel implements Renderable {
		public String label;
		public Color color;
		public String toString() {return label;}
		public HighlightLabel(String label, Color color) {this.label = label; this.color = color;}
		@Override
		public void render(DefaultTreeCellRenderer comp) {
			if(color != null) {
        		comp.setBackground(color);
        		comp.setOpaque(true);
        	} else {
        		comp.setOpaque(false);
        	}
		}
	}	
}
