/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.tudarmstadt.langtech.ChiWhiDisamb;

/**
 * 
 * @author biem
 * Maintains renumbering for edges
 */
class EdgeRenumberer implements Comparable{
	private final int newNodeID1;
	private final int newNodeID2;
	private final int edgeWeight;

	/**
	 * Creates new edge list Object.
	 * @param newNodeID1 The node ID 1.
	 * @param newNodeID2 The node ID 2.
	 * @param weight The weight.
	 */
	public EdgeRenumberer(int newNodeID1,int newNodeID2,int weight){
		this.newNodeID1=newNodeID1;
		this.newNodeID2=newNodeID2;
		this.edgeWeight=weight;
	}
	/**
	 * Sort by word_1, weight.
	 */
	@Override
	public int compareTo(Object o){
		EdgeRenumberer s = (EdgeRenumberer) o;

		if(s.getNewNodeID1()>this.newNodeID1){
			return -1;
		}
		else if(s.getNewNodeID1()<this.newNodeID1){
			return 1;
		}
		else{
			if(s.getEdgeWeight()>this.edgeWeight){
				return 1;
			}
			else if(s.getEdgeWeight()<this.edgeWeight){
				return -1;
			}
			else{
				return 0;
			}
		}
	}
	/**
	 * @return Returns the newNodeID1.
	 */
	public int getNewNodeID1() {
		return newNodeID1;
	}
	/**
	 * @return Returns the newNodeID2.
	 */
	public int getNewNodeID2() {
		return newNodeID2;
	}
	/**
	 * @return Returns the weight.
	 */
	public int getEdgeWeight() {
		return edgeWeight;
	}
}
