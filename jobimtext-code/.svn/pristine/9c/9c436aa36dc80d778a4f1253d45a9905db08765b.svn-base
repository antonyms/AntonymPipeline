/* This file is part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */

package de.tudarmstadt.langtech.ChiWhiDisamb;

/**
 * 
 * @author biem
 * NodeRenumberer takes care of renumbering nodes
 */
class NodeRenumberer implements Comparable{
	private final int newWordNumber;
	private final int oldWordNumber;
	private final String word;

	/**
	 * Creates new Object.
	 * @param newWordNumber The new word number.
	 * @param oldWordNumber The old word number.
	 * @param word The word.
	 */
	public NodeRenumberer(int newWordNumber,int oldWordNumber,String word){
		this.newWordNumber=newWordNumber;
		this.oldWordNumber=oldWordNumber;
		this.word=word;
	}

	@Override
	public int compareTo(Object o){
		if(((NodeRenumberer) o).getNewNodeID() > newWordNumber) {
			return -1;
		}
		else if(((NodeRenumberer) o).getNewNodeID() < newWordNumber) {
			return 1;
		}
		else {
			return 0;
		}
	}
	/**
	 * Get the word
	 * @return The word.
	 */
	public String getWord(){
		return this.word;
	}
	/**
	 * Get the old word number.
	 * @return The old word number.
	 */
	public int getOldWordNumber(){
		return this.oldWordNumber;
	}
	/**
	 * Get the new word number.
	 * @return The old word number.
	 */
	public int getNewNodeID(){
		return this.newWordNumber;
	}

}

