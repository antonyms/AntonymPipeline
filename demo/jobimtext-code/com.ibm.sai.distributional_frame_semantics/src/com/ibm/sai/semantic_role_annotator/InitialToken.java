/*******************************************************************************
* Copyright 2013
* Copyright (c) 2013 IBM Corp.
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

package com.ibm.sai.semantic_role_annotator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author mchowdh
 *
 */
public class InitialToken implements Comparable<InitialToken> {

	private int index = -1;
	private String originalText = "";
	
	private String headWord = "";
	
	private String lemma = "";
	private String pos = "";
	private int begIndex = -1, endIndex = -1;
	
	private int governorIndex = -1;
	private String dependencyTypeWtihGovernor = "";
	private int senIndex = -1;
	
	private Map<Integer, String> mapParentPredicatesWithSemanticRoleTypes = new HashMap<Integer, String>();
	
	private boolean isItselfPredicate = false;
	
	/**
	 * 
	 * @param indx
	 */
	public void setIndex(int indx) {
		this.index = indx;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setText( String text ) {
		this.originalText = text;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getText() {
		return this.originalText;
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setHeadWord( String text ) {
		this.headWord = text;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHeadWord() {
		return this.headWord;
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setLemma (String text) {
		this.lemma = text;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLemma () {
		return this.lemma;
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setPOS (String text) {
		this.pos = text;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPOS () {
		return this.pos;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void setBegIndex ( int n ) {
		this.begIndex = n;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getBegIndex () {
		return this.begIndex;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void setEndIndex ( int n ) {
		this.endIndex = n;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEndIndex () {
		return this.endIndex;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void setGovernorIndex ( int n ) {
		this.governorIndex = n;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getGovernorIndex () {
		return this.governorIndex;
	}
	
	/**
	 * 
	 * @param depType
	 */
	public void setDependencyTypeWtihGovernor ( String depType ) {
		this.dependencyTypeWtihGovernor = depType;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDependencyTypeWtihGovernor () {
		return this.dependencyTypeWtihGovernor;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void setSentenceIndex (int n) {
		this.senIndex = n;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSentenceIndex () {
		return this.senIndex;
	}
	
	/**
	 * 
	 * @param val
	 */
	public void setItselfAsPredicate (boolean val) {
		this.isItselfPredicate = val;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isItselfPredicate () {
		return this.isItselfPredicate;
	}
	
	/**
	 * 
	 * @param ppIndex
	 * @param srlType
	 */
	public void addParentPredicateIndexAndSrlType ( int ppIndex, String srlType ) {
		this.mapParentPredicatesWithSemanticRoleTypes.put(ppIndex, srlType);
	}
	
	/**
	 * 
	 * @param mapParentPredicatesWithSemanticRoleTypes
	 */
	public void copyAllParentPredicateIndexAndSrlType ( Map<Integer, String> mapParentPredicatesWithSemanticRoleTypes ) {
		this.mapParentPredicatesWithSemanticRoleTypes.putAll(mapParentPredicatesWithSemanticRoleTypes);
	}
	
	/**
	 * 
	 */
	public void setParentPredicateIndexAndSrlTypeAsEmpty () {
		this.mapParentPredicatesWithSemanticRoleTypes = new HashMap<Integer, String>();
	}
	/**
	 * 
	 * @return
	 */
	public Set<Integer> getParentPredicateIndexes() {
		return this.mapParentPredicatesWithSemanticRoleTypes.keySet();
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<Integer, String> getAllParentPredicateIndexesWithSrlTypes() {
		return this.mapParentPredicatesWithSemanticRoleTypes;
	}
	
	/**
	 * 
	 * @param parentPredicateIndex
	 * @return
	 */
	public String getSrlType( int parentPredicateIndex) {
		return this.mapParentPredicatesWithSemanticRoleTypes.get(parentPredicateIndex);
	}
	
	/**
	 * 
	 * @param parentPredicateIndex
	 * @return
	 */
	public boolean containsParentPredicate( int parentPredicateIndex) {
		return this.mapParentPredicatesWithSemanticRoleTypes.containsKey(parentPredicateIndex);
	}
	
	/**
	 * 
	 * @param parentPredicateIndex
	 */
	public void removeParentPredicate( int parentPredicateIndex) {
  		this.mapParentPredicatesWithSemanticRoleTypes.remove(parentPredicateIndex);
	}
	
	/**
	 * 
	 * @param srlType
	 * @return
	 */
	public boolean containsSrlType( String srlType ) {
		return this.mapParentPredicatesWithSemanticRoleTypes.containsValue(srlType);
	}
	
	/**
	 * 
	 */
	public Term clone() {
		Term tok = new Term();
		tok.setIndex(this.index);
		tok.setText(this.originalText);
		tok.setLemma(this.lemma);
		tok.setPOS(this.pos);
		tok.setBegIndex(this.begIndex);
		tok.setEndIndex(this.endIndex);
		tok.setGovernorIndex(this.governorIndex);
		tok.setDependencyTypeWtihGovernor(this.dependencyTypeWtihGovernor);
		tok.setSentenceIndex(this.senIndex);
		tok.setItselfAsPredicate(this.isItselfPredicate);
		
		tok.setHeadWord(this.headWord);
		
		for ( int key : this.mapParentPredicatesWithSemanticRoleTypes.keySet() )
			tok.addParentPredicateIndexAndSrlType(key, this.mapParentPredicatesWithSemanticRoleTypes.get(key));

		return tok;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean hasGovernor () {
		if ( this.getGovernorIndex() < 0 )
			return false;
		 
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isGovernorAppearedEarlierInText () {
		if ( hasGovernor() && this.getGovernorIndex() < this.getIndex() )
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPreposition () {
		if ( this.getPOS().toUpperCase().equals("IN") )
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isBeVerb () {
		if ( this.lemma.equalsIgnoreCase("be") || this.originalText.equalsIgnoreCase("be") )
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAdjective () {
		if ( this.pos.toUpperCase().equals("JJ") )
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAdjectiveComparativeOrSuperlative () {
		if ( this.pos.toUpperCase().startsWith("JJ") && this.pos.length() > 2  )
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isVerb () {
		if ( this.pos.toUpperCase().startsWith("V") )
			return true;
		return false;
	}

	@Override
	public int compareTo(InitialToken o) {
		
		if ( this.index == o.index && this.originalText.equals(o.originalText) && this.senIndex == o.senIndex )
			return 0;
		if ( this.senIndex < o.senIndex )	
			return -1;
		if ( this.senIndex > o.senIndex )	
			return 1;
		
		if ( this.index < o.index )	
			return -1;
		if ( this.index > o.index )	
			return 1;
		
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		InitialToken tmp = (InitialToken) o;
		
		if ( this.index == tmp.index && this.originalText.equals(tmp.originalText) && this.senIndex == tmp.senIndex )
			return true;
		
		return false;
	}
}
