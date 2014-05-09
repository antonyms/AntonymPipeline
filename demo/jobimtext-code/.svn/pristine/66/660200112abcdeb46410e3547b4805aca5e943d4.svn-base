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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.sai.semantic_role_annotator.util.DateString;

/**
 * 
 * @author mchowdh
 *
 */
public class Sentence {
	private int senIndex = -1;
	private List<InitialToken> listOfInitialTokens = new ArrayList<InitialToken>();
	private List<Term> listOfTerms = new ArrayList<Term>();
	
	

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
	 * @param tok
	 */
	public void addInitialToken(InitialToken tok) {
		this.listOfInitialTokens.add(tok);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasInitialToken () {
		return this.listOfInitialTokens.size() > 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<InitialToken> getInitialTokens() {
		return this.listOfInitialTokens;
	}
	
	/**
	 * 
	 * @param t
	 */
	public void addTerm(Term t) {
		this.listOfTerms.add(t);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Term> getTerms() {
		return this.listOfTerms;
	}
	
    /**
     * 
     * @param listOfTerms
     */
    public void printTokens () {
    	//*
    	for ( int i=0; i<listOfTerms.size(); i++ ) {
      		
    		System.out.print(i + "\t" + listOfTerms.get(i).getText() + "\t" + listOfTerms.get(i).getBegIndex() + "," + listOfTerms.get(i).getEndIndex() + ", s:" + listOfTerms.get(i).getSentenceIndex() 
      				+ "\t" + listOfTerms.get(i).getLemma() 
      				+ "\t" + listOfTerms.get(i).getPOS() 
      				+ "\t" + listOfTerms.get(i).getGovernorIndex() 
      				+ "\t" + listOfTerms.get(i).getDependencyTypeWtihGovernor()
      				+ "\t" 
      				);
      	
      		int x = 0;
      		for ( int predID : listOfTerms.get(i).getParentPredicateIndexes() ) {
      			if ( x > 0 )
      				System.out.print(";");
      			System.out.print(predID + ":" + listOfTerms.get(i).getSrlType(predID));
      			x++;
      		}
      		
      		System.out.println();
      	}
      
      	System.out.println();
      	//*/
    }

    
    /**
     * 
     * @param tok
     */
    private void propogateParentPredicatesToDependents ( InitialToken tok ) {
    	
    	if ( tok.getAllParentPredicateIndexesWithSrlTypes() == null )
    		return;
    	
    	// looking at the terms which has the current term as governor
		for ( int k=0; k < listOfTerms.size(); k ++ ) {
			  if ( k != tok.getIndex() ) {
				  if ( listOfTerms.get(k).getGovernorIndex() == tok.getIndex() ) {
					  if ( !listOfTerms.get(k).getText().matches(".*[a-zA-Z0-9].*") )
						  continue;
					  
					  if ( listOfTerms.get(k).getAllParentPredicateIndexesWithSrlTypes() == null )
						  listOfTerms.get(k).setParentPredicateIndexAndSrlTypeAsEmpty();
					  listOfTerms.get(k).copyAllParentPredicateIndexAndSrlType(tok.getAllParentPredicateIndexesWithSrlTypes());
				  }
			  }  
		}
		
		tok.setParentPredicateIndexAndSrlTypeAsEmpty();
    }
    
    /**
     * 
     */
    public void collapseDependency () {
     	
        for ( int tokIndx=0; tokIndx < listOfTerms.size(); tokIndx ++ ) {
      	  	InitialToken tok = listOfTerms.get(tokIndx);
      	  		
      	  	if ( tok.isPreposition() )
      	  		propogateParentPredicatesToDependents(tok);
       	        	
      	  	if ( !tok.hasGovernor() ) {      		  
      	  		continue;
      	  	}
      	  
      	  	InitialToken gov = listOfTerms.get(tok.getGovernorIndex());
      	  
      	  	//System.out.println(tok.originalText);
      	  	if ( tok.isGovernorAppearedEarlierInText() 
      	  			&& ( (tok.isPreposition() && !gov.isBeVerb())
      			  		|| (tok.isAdjective() && tok.getAllParentPredicateIndexesWithSrlTypes() != null 
      			  		&& tok.containsSrlType("A2-PRD")) ) 
      			  && gov.isVerb() 
      	//		  && !listOfTerms.get(tok.getGovernorIndex()).lemma.equalsIgnoreCase("be") 
      			  && tokIndx - tok.getGovernorIndex() <= 5 ) {
      		  
      		  	if ( tok.isAdjective() )
      		  		propogateParentPredicatesToDependents(tok);
      		  //--------
          	  // if any other word of the sentence has one of the merged words as predicate, change the pointer to the new index of the merged words
          	  for ( int k=0; k<listOfTerms.size(); k++ ) {
          		
          		  if( listOfTerms.get(k).getIndex() > tokIndx )
          			listOfTerms.get(k).setIndex(listOfTerms.get(k).getIndex()-1);
          		  
          		  if( listOfTerms.get(k).getGovernorIndex() > tokIndx )
          			listOfTerms.get(k).setGovernorIndex(listOfTerms.get(k).getGovernorIndex()-1);
          		  
          		  Map<Integer, String> listTmp = new HashMap<Integer, String>();
          		  for ( int p : listOfTerms.get(k).getParentPredicateIndexes() ) {
          			  if ( p > tokIndx )
          				  listTmp.put(p-1, listOfTerms.get(k).getSrlType(p));
          			  else
          				listTmp.put(p, listOfTerms.get(k).getSrlType(p));    		  
          		  }
          		  
          		  listOfTerms.get(k).setParentPredicateIndexAndSrlTypeAsEmpty();
          		  listOfTerms.get(k).copyAllParentPredicateIndexAndSrlType(listTmp);
          	  }
      	      //-----------
      		  listOfTerms.get(tok.getGovernorIndex()).setText(listOfTerms.get(tok.getGovernorIndex()).getText() + "_" + tok.getText());
      		  listOfTerms.get(tok.getGovernorIndex()).setLemma(listOfTerms.get(tok.getGovernorIndex()).getLemma() + "_" + tok.getLemma());
      		  //listOfTerms.get(tok.getGovernorIndex()).getEndIndex() = tok.getEndIndex();
      		  
      		  listOfTerms.remove(tokIndx);
      		  tokIndx--;
      	  }
      		  
        }   
        
       // System.out.println("After collapsing dependency:");
      	printTokens();      	
    }

    /**
     * 
     * @param listOfTokenIndexesToMerge
     * @param isCreatingNPChunk
     */
    private void updateGovernorAndPredicateIndexes ( List<List<Integer>> listOfTokenIndexesToMerge, boolean isCreatingNPChunk ) {
        
    	List<Integer> listOfTermsToBeRemoved = new ArrayList<Integer>();
        for ( int i=listOfTokenIndexesToMerge.size()-1; i>=0; i-- ) {
        	
        	List<Integer> tmp = listOfTokenIndexesToMerge.get(i);
        	if ( tmp.isEmpty() )
        		continue;
        	
  		  	int headTokenIndex = getHeadTokenIndex(tmp);
		  
  		  	listOfTerms.get(tmp.get(0)).setHeadWord(listOfTerms.get(headTokenIndex).getLemma());
  		  	listOfTerms.get(tmp.get(0)).setGovernorIndex(listOfTerms.get(headTokenIndex).getGovernorIndex());
  		  	listOfTerms.get(tmp.get(0)).setDependencyTypeWtihGovernor(listOfTerms.get(headTokenIndex).getDependencyTypeWtihGovernor());

  		  	for ( int k=1; k<tmp.size(); k++ ) {
  		  		String space = listOfTerms.get(tmp.get(k)).getBegIndex() - listOfTerms.get(tmp.get(k-1)).getEndIndex() > 0 ? " " : "";
  		  		listOfTerms.get(tmp.get(0)).setText(listOfTerms.get(tmp.get(0)).getText() + space + listOfTerms.get(tmp.get(k)).getText());
  		  		listOfTerms.get(tmp.get(0)).setLemma(listOfTerms.get(tmp.get(0)).getLemma() + space + listOfTerms.get(tmp.get(k)).getLemma());
      	
  		  		listOfTerms.get(tmp.get(0)).copyAllParentPredicateIndexAndSrlType(listOfTerms.get(tmp.get(k)).getAllParentPredicateIndexesWithSrlTypes());
  		  	}
      	  
      	  // removing self-predicates
      	  for ( int k=0; k<tmp.size(); k++ )    		  
      		  listOfTerms.get(tmp.get(0)).removeParentPredicate(tmp.get(k));
    	        	  
      	  
      	  // if any other word of the sentence has one of the merged words as predicate, change the pointer to the new index of the merged words
      	  for ( int k=0; k<listOfTerms.size(); k++ ) {
      		  
      		  if( tmp.contains(listOfTerms.get(k).getGovernorIndex()) )
      			  listOfTerms.get(k).setGovernorIndex(tmp.get(0));
      		  else if( listOfTerms.get(k).getGovernorIndex() > tmp.get(0) )
      			listOfTerms.get(k).setGovernorIndex(listOfTerms.get(k).getGovernorIndex() - (tmp.size()-1));//(listOfTerms.get(k).getGovernorIndex() - tmp.get(0));
      		        		  
      		  Map<Integer, String> listTmp = new HashMap<Integer, String>();
      		  for ( int p : listOfTerms.get(k).getParentPredicateIndexes() ) {
      			  if ( p > tmp.get(0) )
      				  listTmp.put(p-(tmp.size()-1), listOfTerms.get(k).getSrlType(p));
      			  else
      				listTmp.put(p, listOfTerms.get(k).getSrlType(p));    		  
      		  }
      		  
      		listOfTerms.get(k).setParentPredicateIndexAndSrlTypeAsEmpty();
      		listOfTerms.get(k).copyAllParentPredicateIndexAndSrlType(listTmp);
      	  }
      	  
      	  
      	  if ( tmp.size() > 1 ) {
      		  if ( isCreatingNPChunk )
      			  listOfTerms.get(tmp.get(0)).setPOS("NOUN");
      		  listOfTerms.get(tmp.get(0)).setEndIndex(listOfTerms.get(tmp.get(tmp.size()-1)).getEndIndex());
      	  }
      	  
      	  // remove the tokens which are merged
      	  for ( int k=tmp.size()-1; k>0; k-- ) {
      		  listOfTermsToBeRemoved.add(tmp.get(k));
      		  //listOfTerms.remove(tmp.get(k));
      	  }
        }
        
        List<Term> newList = new ArrayList<Term>();
        
        int x = 0;
        for ( int i=0; i<listOfTerms.size(); i++ ) {
      	  if ( !listOfTermsToBeRemoved.contains(i) ) {
      		  listOfTerms.get(i).setIndex(x);
      		  x++;
      		  newList.add(listOfTerms.get(i));
      	  }
        }
        
        listOfTerms = newList;
    }

    
    /**
     * 
     * @param listOfAlreadyCheckedTokens
     * @param ti
     * @param listOfDependents
     * @param isGovernor
     * @return
     */
    private boolean isValidToBeInNPChunk ( List<Integer> listOfAlreadyCheckedTokens, int ti, List<Integer> listOfDependents, boolean isGovernor ) {
    	
    	if ( listOfAlreadyCheckedTokens.contains(ti)
    			|| listOfDependents.contains(ti) )
    		return false;
    	
    	if ( ti > 0 && listOfTerms.get(ti-1).getText().equals("-") && 
    				listOfTerms.get(ti-1).getEndIndex() ==  listOfTerms.get(ti).getBegIndex() )
    		return true;
    	
    	if ( ti < listOfTerms.size()-1 && listOfTerms.get(ti+1).getText().equals("-") && 
				listOfTerms.get(ti).getEndIndex() ==  listOfTerms.get(ti+1).getBegIndex() )
    		return true;
    	

    	if ( listOfTerms.get(ti).getPOS().equalsIgnoreCase("DT") && !listOfTerms.get(ti).getText().equalsIgnoreCase("the") )
    		return false;
  	
    	
    	if ( (isGovernor && !(listOfTerms.get(ti).getPOS().startsWith("NN") || listOfTerms.get(ti).getPOS().startsWith("??") || listOfTerms.get(ti).getPOS().startsWith("CD")))
    			|| listOfTerms.get(ti).isVerb()
    			
    			|| listOfTerms.get(ti).getPOS().toUpperCase().startsWith("W")
    			
    			|| listOfTerms.get(ti).getPOS().toUpperCase().equals("POS")
    			
    			|| listOfTerms.get(ti).isAdjectiveComparativeOrSuperlative() 
    			
    			|| (listOfTerms.get(ti).isPreposition() && !listOfTerms.get(ti).getText().equalsIgnoreCase("of"))
    			|| listOfTerms.get(ti).getText().equals(";")
    			|| listOfTerms.get(ti).getText().equals(",")
    
    			|| listOfTerms.get(ti).getText().contains("'")
    			|| listOfTerms.get(ti).getText().contains("\"")
    
    			|| listOfTerms.get(ti).getText().matches("[(){}\\[\\]]")
    			    
    			|| listOfTerms.get(ti).getText().equalsIgnoreCase("i.e.")    			
    			|| listOfTerms.get(ti).getText().equalsIgnoreCase("e.g.")
    	)
    		return false;
    	
    	if ( ti > 0 && listOfDependents.contains(ti-1) 
    			&& listOfTerms.get(ti).getBegIndex() == listOfTerms.get(ti-1).getEndIndex()
				&& listOfTerms.get(ti).getText().matches("[0-9A-Za-z].*")
				&& listOfTerms.get(ti-1).getText().matches(".*[0-9A-Za-z]")
		 )
    		return false;
    	

    	if ( ti < listOfTerms.size()-1 && listOfDependents.contains(ti+1) 
    			&& listOfTerms.get(ti+1).getBegIndex() == listOfTerms.get(ti).getEndIndex()
				&& listOfTerms.get(ti+1).getText().matches("[0-9A-Za-z].*")
				&& listOfTerms.get(ti).getText().matches(".*[0-9A-Za-z]")
		 )
    		return false;
				
    	/*
    	 *  if there is token sequence such as "Vice President of your/a Bank", only "Vice President" will be considered   
    	 */    	
	  	if ( listOfTerms.get(ti).getPOS().toUpperCase().contains("PRP")
	  			||  listOfTerms.get(ti).getText().equalsIgnoreCase("a")
	  			||  listOfTerms.get(ti).getText().equalsIgnoreCase("an")
	  			) 
	  		return false;
	  	
	  	// not all "and" should be part of NP chunk
	  	if ( listOfTerms.get(ti).getPOS().toUpperCase().contains("CC") ) {
	  		if ( ti > 1 && ti < listOfTerms.size()-2 && listOfTerms.get(ti-1).getText().equals("-") && listOfTerms.get(ti+1).getText().equals("-") )
	  			return true;
	  		else
	  			return false;
	  	}
	  	
	  	// numbers enclosed by brackets should not be allowed
	  	if ( listOfTerms.get(ti).getPOS().toUpperCase().contains("CD") && ti > 0 && ti < listOfTerms.size()-1 
	  			&& listOfTerms.get(ti-1).getText().matches("[\\[{(]") && listOfTerms.get(ti+1).getText().matches("[\\])}]") ) {
			return false;
	  	}
	  	
    	return true;
    }
    
    /**
     * 
     * @param pi
     * @param listOfAlreadyCheckedTokens
     * @return
     */
    private List<Integer> getAllDependents ( int pi, List<Integer> listOfAlreadyCheckedTokens ) {
    	
    	int curTI = pi; 
    	List<Integer> listOfDependents = new ArrayList<Integer>();
    	
    	listOfDependents.add(pi);
    	int cursor = 0;
    	while ( (pi=listOfTerms.get(pi).getGovernorIndex()) > -1 
    			&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, pi, listOfDependents, true)	) {    		
    		listOfDependents.add(pi);
    	}
    	
    	while ( cursor < listOfDependents.size() ) {
	    	for ( int tokIndx=0; tokIndx < listOfTerms.size(); tokIndx ++ ) {
	    	
	    		if ( listOfTerms.get(tokIndx).getGovernorIndex() == listOfDependents.get(cursor) 
	    				&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, tokIndx, listOfDependents, false) ) {
	    			listOfDependents.add(tokIndx);
	    		}
	    	}
	    	cursor++;
    	}
    	    	
    	if ( listOfDependents.size() < 2 )
    		return new ArrayList<Integer>();
    	
    	// sort
    	Collections.sort(listOfDependents);
    	
    	// only one "of" is allowed
    	boolean foundOf = false;
    	for ( int k=listOfDependents.size()-1; k>=0; k-- ) {
    		if ( listOfTerms.get(listOfDependents.get(k)).getText().equalsIgnoreCase("of") ) {
    			if ( !foundOf )
    				foundOf = true;
    			else {
    				for ( ; k>=0; k-- ) {
    		    		listOfDependents.remove(k);
    				}
    			}
    		}
    	}
    	
    	/**
    	 * Any token sequence has to be continuous w.r.t. to the position of the current token
    	 */
    	int x=0;
    	for ( ; x<listOfDependents.size(); x++ ){
    		if ( listOfDependents.get(x) == curTI )
    			break;
    	}

    	
    	for ( int k=0; k<x; k++ ) {
    		if ( curTI - listOfDependents.get(k) !=  x-k ) {
    			listOfDependents.remove(k);
    			k--;
    			x--;
    		}
    	}
    	
    	for ( int k=listOfDependents.size()-1; k>x; k-- ) {
    		if ( listOfDependents.get(k) - curTI !=  k-x ) {
    			listOfDependents.remove(k);
    		}
    	}    	
    	
    	// the first word must contain at least one alphanumeric character
    	for ( int k=0; k<listOfDependents.size(); ){
    		if ( listOfTerms.get(listOfDependents.get(k)).getText().matches(".*[0-9a-zA-Z].*") 
    				||  (listOfDependents.size() > 1 &&  listOfTerms.get(listOfDependents.get(k)).getEndIndex() == listOfTerms.get(listOfDependents.get(k+1)).getBegIndex() ) )
    			break;
    		
    		listOfDependents.remove(k);
    	}
    	
    	// this will keep terms such as "day-to-day operation" intact
		while (listOfDependents.size() > 0) {
			int beg = listOfDependents.get(0);
			int end = listOfDependents.get(listOfDependents.size()-1);
			
			if ( beg > 1 
					&& !listOfTerms.get(beg-1).getText().matches(".*[0-9a-zA-Z].*")
					&& listOfTerms.get(beg-1).getEndIndex() == listOfTerms.get(beg).getBegIndex()
					&& listOfTerms.get(beg-2).getEndIndex() == listOfTerms.get(beg-2).getBegIndex()
					&& listOfTerms.get(beg-2).getText().matches(".*[0-9a-zA-Z].*")
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, beg-2, listOfDependents, false)
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, beg-1, listOfDependents, false) ) {
				listOfDependents.add(0, beg-2);
				listOfDependents.add(1, beg-1);
			}
			else if ( beg > 0 
					&& !listOfTerms.get(beg).getText().matches(".*[0-9a-zA-Z].*")
					&& listOfTerms.get(beg-1).getEndIndex() == listOfTerms.get(beg).getBegIndex()					
					&& listOfTerms.get(beg-1).getText().matches(".*[0-9a-zA-Z].*") 
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, beg-1, listOfDependents, false) ) {
				listOfDependents.add(0, beg-1);
			}
			else if ( end < listOfTerms.size()-2 
					&& !listOfTerms.get(end+1).getText().matches(".*[0-9a-zA-Z].*") 
					&& listOfTerms.get(end+1).getBegIndex() == listOfTerms.get(end).getEndIndex()
					&& listOfTerms.get(end+2).getBegIndex() == listOfTerms.get(end+1).getEndIndex()
					&& listOfTerms.get(end+2).getText().matches(".*[0-9a-zA-Z].*") 
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, end+2, listOfDependents, false)
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, end+1, listOfDependents, false) ) {
				listOfDependents.add(end+1);
				listOfDependents.add(end+2);
			}
			else if ( end < listOfTerms.size()-1
					&& !listOfTerms.get(end).getText().matches(".*[0-9a-zA-Z].*") 
					&& listOfTerms.get(end+1).getBegIndex() == listOfTerms.get(end).getEndIndex()
					&& listOfTerms.get(end+1).getText().matches(".*[0-9a-zA-Z].*")
					&& isValidToBeInNPChunk(listOfAlreadyCheckedTokens, end+1, listOfDependents, false) ) {
				listOfDependents.add(end+1);
			}
			else
				break;
		}
        
    	    	    	
    	// checking bracket closing
    	int limit = listOfDependents.size()-1;
    	for ( int k=0; k<=limit; k++ ){
    		String str = listOfTerms.get(listOfDependents.get(k)).getText();
    		
    		String w = "";
    		if ( str.equals("(") )
    			w = ")";
    		else if ( str.equals("{") )
    			w = "}";
    		else if ( str.equals("[") )
    			w = "]";
    		else if ( str.equals("\"") )
    			w = "\"";
    		
    		if ( !w.isEmpty() ){
    			for ( ; limit>k; limit-- ) {
    				if ( listOfTerms.get(listOfDependents.get(limit)).getText().equals(w) )
    					break;
    			}
    		}
    		
    		if ( k>=limit && !w.isEmpty() ) {
    			for ( ; k<listOfDependents.size(); ){
    				listOfDependents.remove(k);
    			}
    		}
    	}
    	    	
    	
    	while ( listOfDependents.size() > 0 ) {
    		boolean somethingChanged = false;
	
    		// the last word must contain at least one alphanumeric character or brackets
        	for ( int k=listOfDependents.size()-1; k>=0; k-- ){
        		if ( listOfTerms.get(listOfDependents.get(k)).getText().matches(".*[0-9a-zA-Z\\])}].*") )
        			break;
        	
        		somethingChanged = true;
        		listOfDependents.remove(k);
    			k--;
        	}
    
        	// ( and  ) at the end of NP chunk should be discarded
	    	if ( listOfDependents.size() >= 2 ) {
	    		if ( 
	    			(listOfTerms.get(listOfDependents.get(listOfDependents.size()-1)).getText().equals(")") 
	    				&& listOfTerms.get(listOfDependents.get(0)).getText().equals("("))
	    			|| (listOfTerms.get(listOfDependents.get(listOfDependents.size()-1)).getText().equals("]") 
	    	    				&& listOfTerms.get(listOfDependents.get(0)).getText().equals("["))
	    	    	|| (listOfTerms.get(listOfDependents.get(listOfDependents.size()-1)).getText().equals("}") 
	    	    				&& listOfTerms.get(listOfDependents.get(0)).getText().equals("{"))
	    				) {
	    			listOfDependents.remove(listOfDependents.size()-1);
	    			listOfDependents.remove(0);
	    			somethingChanged = true;
	    		}
	    	}
	    	
	    	// the last word cannot be words such as IN or DT
	    	if ( listOfDependents.size() > 0 && listOfTerms.get(listOfDependents.get(listOfDependents.size()-1)).getPOS().matches("[a-zA-Z]+") 
	    			&& !listOfTerms.get(listOfDependents.get(listOfDependents.size()-1)).getPOS().matches("(N|CD).*") ) {
    			listOfDependents.remove(listOfDependents.size()-1);
    			somethingChanged = true;
	    	}
	    	
    		if ( !somethingChanged )
    			break;
    	}
    	
    	if ( listOfDependents.size() < 2 )
    		return new ArrayList<Integer>();
    	
    	return listOfDependents;
    }
    
    /**
     * 
     * @param listOfTerms
     */
    public void createNPChunks (  ) {
     	
        List<List<Integer>> listOfTokenIndexesToMerge = new ArrayList<List<Integer>>();
        List<Integer> listOfTokenIndexesChecked = new ArrayList<Integer>();
        
        List<Integer> tmp = new ArrayList<Integer>();
        
        for ( int tokIndx=0; tokIndx < listOfTerms.size(); tokIndx ++ ) {
      	
      	  	if ( isValidToBeInNPChunk(listOfTokenIndexesChecked, tokIndx, new ArrayList<Integer>(), true) ) {
          	
            	tmp = getAllDependents(tokIndx, listOfTokenIndexesChecked);
            	listOfTokenIndexesChecked.addAll(tmp);            	
            
            	if ( tmp.size() > 1 && listOfTerms.get(tmp.get(0)).getPOS().equalsIgnoreCase("DT") )
          		  tmp.remove(0);
          	    
            	if (tmp.size() > 1) {
            		  listOfTokenIndexesToMerge.add(tmp);
            	}
                  
            	tmp = new ArrayList<Integer>();
            }
        }
      
        if (tmp.size() > 0)
            listOfTokenIndexesToMerge.add(tmp);
         
        updateGovernorAndPredicateIndexes(listOfTokenIndexesToMerge, true);
        
       // System.out.println("After cretaing NP chunks:");
        printTokens();      	
    }
    
    
    /**
     * 
     * @param listOfTerms
     * /
    public void createNPChunks_based_on_POS () {
     	
    	String lastToken = null, lastTag = null;
        
        List<List<Integer>> listOfTokenIndexesToMerge = new ArrayList<List<Integer>>();
        
        List<Integer> tmp = new ArrayList<Integer>();
        
        int indexOf = -1;
        for ( int tokIndx=0; tokIndx < listOfTerms.size(); tokIndx ++ ) {
      	  	InitialToken tok = listOfTerms.get(tokIndx);
      	  	
      	  	InitialToken nextTok = listOfTerms.size() > tokIndx+1 ? listOfTerms.get(tokIndx) : null;
        
      	  	boolean skip = false;
      	  	
      	  	if ( nextTok != null && tok.getText().equalsIgnoreCase("of") 
      	  			&& !tok.getPOS().toUpperCase().contains("PRP") 
      	  			&& !(tok.getPOS().equalsIgnoreCase("DT") && !tok.getText().equalsIgnoreCase("the"))
      	  			)
      	  		skip = true;
      	  		
            if ( !skip &&
            		(tok.getPOS().equals("JJ") 
            		|| tok.getPOS().startsWith("NN")
            		|| tok.getPOS().startsWith("??")
            		|| (lastTag != null && lastTag.startsWith("NN") && tok.getText().equalsIgnoreCase("of"))
            		|| (lastToken != null && lastToken.equalsIgnoreCase("of") && tok.getText().equalsIgnoreCase("the") )
            		)
                ) {
          	
          	  	if ( tok.getText().equalsIgnoreCase("of") ) {
          		  indexOf = tokIndx - 1;
          	  	}
          	  	tmp.add(tokIndx);
            } 
            else {
            	if ( tmp.size() > 1 && listOfTerms.get(tmp.get(0)).getPOS().equalsIgnoreCase("DT") )
          		  tmp.remove(0);
          	  
            	if ( tmp.size() > 1 && tok.getPOS().equalsIgnoreCase("DT") )
          		  tmp.remove(tmp.size()-1);
          	    
            	// if the chunk is too large, break it
            	if ( tmp.size() > 6 && indexOf > -1 ) {
            		for ( int zz=0; zz < tmp.size(); zz++ ) {
            			if ( tmp.get(zz) == indexOf ) {
            				for ( ; zz < tmp.size(); zz++ ) {
            					tmp.remove(zz);
            				}
            				tokIndx = indexOf;
            			}            				
            		}
            	}
            	
            	if (tmp.size() > 1) {
            		  listOfTokenIndexesToMerge.add(tmp);
            	}
                  
            	tmp = new ArrayList<Integer>();
                indexOf = -1;
            }
            
            lastTag = tok.pos;
            lastToken = tok.originalText;
        }
      
        if (tmp.size() > 0)
            listOfTokenIndexesToMerge.add(tmp);
         
        updateGovernorAndPredicateIndexes(listOfTokenIndexesToMerge, true);
        
        System.out.println("After cretaing NP chunks:");
        printTokens();      	
    }
    */
    
    /**
     * 
     * @param x
     * @param y
     * @param a
     * @param b
     * @return
     */
    public static boolean overlaps ( int x, int y, int a, int b ) {
    	
    	if ( x<=a && a<=y )
    		return true;
    	
    	if ( x<=b && b<=y )
    		return true;
    	
    	return false;
    }

    
    /**
     * 
     * @param listOfTIs
     * @return
     */
    public int getHeadTokenIndex ( List<Integer> listOfTIs ) {
    
    	// if there is a NP containing the word "of"
    	for ( int x=1; x < listOfTIs.size()-1; x++ ) {
    		int ti = listOfTIs.get(x);
    		if (  listOfTerms.get(ti).getText().equalsIgnoreCase("of") ) {
    			return ti-1;
    		}
    	}
    	
    	// the token which governs at least one of the other tokens in the list but is not dependent on any of these tokens
    	int hi = -1;
    	for ( int x=0; x < listOfTIs.size(); x++ ) {
    		int ti = listOfTIs.get(x);
    		int gi = listOfTerms.get(ti).getGovernorIndex();
    		if ( listOfTIs.contains(gi) 
    				&& !listOfTIs.contains(listOfTerms.get(gi).getGovernorIndex()) ) {
    			hi = gi;
    		}
    	}
    	
    	if ( hi > -1 )
    		return hi;
    	
    	// the rightmost token
    	return listOfTIs.get(listOfTIs.size()-1);
    	
    }
    
    /**
     * 
     * @param mapDates
     */
    public void createDateExpressions ( Map<Integer, DateString> mapDates ) {
     	
        List<List<Integer>> listOfTokenIndexesToMerge = new ArrayList<List<Integer>>();
        
        List<Integer> tmp = new ArrayList<Integer>();
         
        int tokIndx=0;
        for ( DateString dateExp : mapDates.values() ) {
        	
        	for ( ; tokIndx < listOfTerms.size(); tokIndx ++ ) {
          	  	InitialToken tok = listOfTerms.get(tokIndx);
          
          	  	if ( overlaps( dateExp.getBegIndex(), dateExp.getEndIndex()-1, tok.getBegIndex(), tok.getEndIndex()-1) 
          	  			&& !listOfTerms.get(tokIndx).getPOS().equalsIgnoreCase("DT") ) {
          	  		tmp.add(tokIndx);
          	  	}
          	  	else if ( dateExp.getEndIndex() < tok.getBegIndex() ) {
          	  		break;
          	  	}
        	}
        	  	          
            if (tmp.size() > 0) {
                listOfTokenIndexesToMerge.add(tmp);
            }
            
            tmp = new ArrayList<Integer>();
            if ( tokIndx >= listOfTerms.size() )
            	break;
        }
           
        updateGovernorAndPredicateIndexes(listOfTokenIndexesToMerge, true);
        
     //   System.out.println("After merging tokens of dates:");
        //printTokens();
    }
}
