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

package com.ibm.sai.distributional_frame_semantics.utility;

/**
 * 
 * @author mchowdh
 *
 */
abstract class OrigTextNormTextHeadFreq {
	
	private CaseInsensitiveStringList listOfPreferredNormalizedMWEs = new CaseInsensitiveStringList(), 
			listOfOriginalMWEs = new CaseInsensitiveStringList(), 
			listOfSyntacticHeads = new CaseInsensitiveStringList();
	private CaseInsensitiveStringList listOfAlternativeNormalizedMWEs = new CaseInsensitiveStringList();
	private String pos = "", posOfFirstTok = "";
	private int freq = 0;
		
	public void addFrequency ( int freq ) {
		this.freq += freq;
	}
		
	public int getFrequency () {
		return freq;
	}
	
	public void setPOS ( String pos ) {
		this.pos = pos;
	}
		
	public String getPOS () {
		return pos;
	}
	
	public void setPosOfFirstTok ( String posOfFirstTok ) {
		this.posOfFirstTok = posOfFirstTok;
	}
		
	public String getPosOfFirstTok () {
		return posOfFirstTok;
	}
	
	/**
	 * 
	 * @param list
	 * @param newVariation
	 */
	private void keepStringWithMostNumberOfLowerCaseChars ( CaseInsensitiveStringList list, String newVariation ) {
		int x = list.indexOf(newVariation);
		
		if ( x < 0 ) {
			list.add(newVariation);
			return;
		}
			
		String str = list.get(x);
		if ( str.replaceAll("[^a-z]", "").length() < newVariation.replaceAll("[^a-z]", "").length() )
			list.set(x, newVariation);
	}
	
	public void addSyntacticHeadVariations ( String newVariation ) {
		keepStringWithMostNumberOfLowerCaseChars( this.listOfSyntacticHeads, newVariation);
	}
		
	public CaseInsensitiveStringList getAllSyntacticHeadVariations () {
		return this.listOfSyntacticHeads;
	}
	
	public void addMweVariation ( String newVariation ) {
		keepStringWithMostNumberOfLowerCaseChars( this.listOfOriginalMWEs, newVariation);
	}
	
	public void addMweVariations ( CaseInsensitiveStringList listOfNewVariations ) {
		for ( String newVariation : listOfNewVariations )
			keepStringWithMostNumberOfLowerCaseChars( this.listOfOriginalMWEs, newVariation);
	}
	
	public CaseInsensitiveStringList getAllMweVariations () {
		return this.listOfOriginalMWEs;
	}
	
	public String getMostPreferredMweVariation () {
		if ( this.listOfOriginalMWEs.isEmpty() )
			return null;
		
		return this.listOfOriginalMWEs.get(0);
	}
	
	/**
	 * x must be lower case.
	 * 
	 * @param x
	 * @return
	 */
	public boolean isOrigTextContainedByStringX ( String x ) {
		
		for ( String str : this.listOfOriginalMWEs ) {
			if ( x.contains(str) )
				return true;
		}
		
		return false;
	}
	
	public void addPreferredNormalizedMweVariations ( String newVariation ) {
		keepStringWithMostNumberOfLowerCaseChars( this.listOfPreferredNormalizedMWEs, newVariation);
	}
	
	public void addPreferredNormalizedMweVariations ( CaseInsensitiveStringList listOfNewVariations ) {
		for ( String newVariation : listOfNewVariations )
			keepStringWithMostNumberOfLowerCaseChars( this.listOfPreferredNormalizedMWEs, newVariation);
	}
	
	public String getMostPreferredNormalizedMweVariations () {
		if ( this.listOfPreferredNormalizedMWEs.isEmpty() )
			return null;
		
		return this.listOfPreferredNormalizedMWEs.get(0);
	}
	
	public CaseInsensitiveStringList getAllPreferredNormalizedMweVariations () {
		return this.listOfPreferredNormalizedMWEs;
	}
	
	public void keepOnlyShortestNormalizedMweVariation () {
		// keep only the shortest
		for ( int y=this.listOfPreferredNormalizedMWEs.size()-1; y>0; y-- ) {
			if ( this.listOfPreferredNormalizedMWEs.get(y).length() 
					< this.listOfPreferredNormalizedMWEs.get(0).length() ) {
				this.listOfPreferredNormalizedMWEs.set(0, this.listOfPreferredNormalizedMWEs.get(y));						
			}
			
			this.listOfPreferredNormalizedMWEs.remove(y);
		}
	}
	
	public void removeAllPreferredNormalizedMweVariation () {
		this.listOfPreferredNormalizedMWEs = new CaseInsensitiveStringList();
	}
	
	
	public boolean consistsOfMultipleWords () {
		if ( this.listOfPreferredNormalizedMWEs.isEmpty() )
			return false;
		
		return this.getMostPreferredMweVariation().contains(" ");
	}
	
	public void addAlternativeNormalizedMweVariations ( String newVariation ) {
		keepStringWithMostNumberOfLowerCaseChars( this.listOfAlternativeNormalizedMWEs, newVariation);
	}
	
	public void addAlternativeNormalizedMweVariations ( CaseInsensitiveStringList listOfNewVariations ) {
		for ( String newVariation : listOfNewVariations )
			keepStringWithMostNumberOfLowerCaseChars( this.listOfAlternativeNormalizedMWEs, newVariation);
	}
	
	public CaseInsensitiveStringList getAllAlternativeNormalizedMweVariations () {
		return this.listOfAlternativeNormalizedMWEs;
	}
}
