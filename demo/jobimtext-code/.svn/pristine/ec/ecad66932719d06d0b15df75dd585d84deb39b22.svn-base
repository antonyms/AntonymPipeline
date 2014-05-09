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


/* First created by JCasGen Tue Aug 13 09:56:27 EDT 2013 */
package com.ibm.sai.semantic_role_annotator.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** A Predicate holds the arguments that have semantic roles
 * Updated by JCasGen Mon Nov 25 13:44:26 EST 2013
 * @generated */
public class Predicate_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Predicate_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Predicate_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Predicate(addr, Predicate_Type.this);
  			   Predicate_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Predicate(addr, Predicate_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Predicate.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ibm.sai.semantic_role_annotator.types.Predicate");
 
  /** @generated */
  final Feature casFeat_predicateID;
  /** @generated */
  final int     casFeatCode_predicateID;
  /** @generated */ 
  public String getPredicateID(int addr) {
        if (featOkTst && casFeat_predicateID == null)
      jcas.throwFeatMissing("predicateID", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_predicateID);
  }
  /** @generated */    
  public void setPredicateID(int addr, String v) {
        if (featOkTst && casFeat_predicateID == null)
      jcas.throwFeatMissing("predicateID", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setStringValue(addr, casFeatCode_predicateID, v);}
    
  
 
  /** @generated */
  final Feature casFeat_listOfSemanticRoleArguments;
  /** @generated */
  final int     casFeatCode_listOfSemanticRoleArguments;
  /** @generated */ 
  public int getListOfSemanticRoleArguments(int addr) {
        if (featOkTst && casFeat_listOfSemanticRoleArguments == null)
      jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments);
  }
  /** @generated */    
  public void setListOfSemanticRoleArguments(int addr, int v) {
        if (featOkTst && casFeat_listOfSemanticRoleArguments == null)
      jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setRefValue(addr, casFeatCode_listOfSemanticRoleArguments, v);}
    
   /** @generated */
  public int getListOfSemanticRoleArguments(int addr, int i) {
        if (featOkTst && casFeat_listOfSemanticRoleArguments == null)
      jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i);
  }
   
  /** @generated */ 
  public void setListOfSemanticRoleArguments(int addr, int i, int v) {
        if (featOkTst && casFeat_listOfSemanticRoleArguments == null)
      jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleArguments), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_originalText;
  /** @generated */
  final int     casFeatCode_originalText;
  /** @generated */ 
  public String getOriginalText(int addr) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_originalText);
  }
  /** @generated */    
  public void setOriginalText(int addr, String v) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setStringValue(addr, casFeatCode_originalText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_sentenceIndex;
  /** @generated */
  final int     casFeatCode_sentenceIndex;
  /** @generated */ 
  public int getSentenceIndex(int addr) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sentenceIndex);
  }
  /** @generated */    
  public void setSentenceIndex(int addr, int v) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setIntValue(addr, casFeatCode_sentenceIndex, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lemma;
  /** @generated */
  final int     casFeatCode_lemma;
  /** @generated */ 
  public String getLemma(int addr) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lemma);
  }
  /** @generated */    
  public void setLemma(int addr, String v) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setStringValue(addr, casFeatCode_lemma, v);}
    
  
 
  /** @generated */
  final Feature casFeat_listOfParentPredicates;
  /** @generated */
  final int     casFeatCode_listOfParentPredicates;
  /** @generated */ 
  public int getListOfParentPredicates(int addr) {
        if (featOkTst && casFeat_listOfParentPredicates == null)
      jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates);
  }
  /** @generated */    
  public void setListOfParentPredicates(int addr, int v) {
        if (featOkTst && casFeat_listOfParentPredicates == null)
      jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setRefValue(addr, casFeatCode_listOfParentPredicates, v);}
    
   /** @generated */
  public int getListOfParentPredicates(int addr, int i) {
        if (featOkTst && casFeat_listOfParentPredicates == null)
      jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i);
  }
   
  /** @generated */ 
  public void setListOfParentPredicates(int addr, int i, int v) {
        if (featOkTst && casFeat_listOfParentPredicates == null)
      jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfParentPredicates), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_listOfSemanticRoleTypes;
  /** @generated */
  final int     casFeatCode_listOfSemanticRoleTypes;
  /** @generated */ 
  public int getListOfSemanticRoleTypes(int addr) {
        if (featOkTst && casFeat_listOfSemanticRoleTypes == null)
      jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes);
  }
  /** @generated */    
  public void setListOfSemanticRoleTypes(int addr, int v) {
        if (featOkTst && casFeat_listOfSemanticRoleTypes == null)
      jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setRefValue(addr, casFeatCode_listOfSemanticRoleTypes, v);}
    
   /** @generated */
  public String getListOfSemanticRoleTypes(int addr, int i) {
        if (featOkTst && casFeat_listOfSemanticRoleTypes == null)
      jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i);
  return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i);
  }
   
  /** @generated */ 
  public void setListOfSemanticRoleTypes(int addr, int i, String v) {
        if (featOkTst && casFeat_listOfSemanticRoleTypes == null)
      jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfSemanticRoleTypes), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_isNegated;
  /** @generated */
  final int     casFeatCode_isNegated;
  /** @generated */ 
  public boolean getIsNegated(int addr) {
        if (featOkTst && casFeat_isNegated == null)
      jcas.throwFeatMissing("isNegated", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isNegated);
  }
  /** @generated */    
  public void setIsNegated(int addr, boolean v) {
        if (featOkTst && casFeat_isNegated == null)
      jcas.throwFeatMissing("isNegated", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isNegated, v);}
    
  
 
  /** @generated */
  final Feature casFeat_listOfModals;
  /** @generated */
  final int     casFeatCode_listOfModals;
  /** @generated */ 
  public int getListOfModals(int addr) {
        if (featOkTst && casFeat_listOfModals == null)
      jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals);
  }
  /** @generated */    
  public void setListOfModals(int addr, int v) {
        if (featOkTst && casFeat_listOfModals == null)
      jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setRefValue(addr, casFeatCode_listOfModals, v);}
    
   /** @generated */
  public String getListOfModals(int addr, int i) {
        if (featOkTst && casFeat_listOfModals == null)
      jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i);
  return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i);
  }
   
  /** @generated */ 
  public void setListOfModals(int addr, int i, String v) {
        if (featOkTst && casFeat_listOfModals == null)
      jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfModals), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_headWord;
  /** @generated */
  final int     casFeatCode_headWord;
  /** @generated */ 
  public String getHeadWord(int addr) {
        if (featOkTst && casFeat_headWord == null)
      jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_headWord);
  }
  /** @generated */    
  public void setHeadWord(int addr, String v) {
        if (featOkTst && casFeat_headWord == null)
      jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    ll_cas.ll_setStringValue(addr, casFeatCode_headWord, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Predicate_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_predicateID = jcas.getRequiredFeatureDE(casType, "predicateID", "uima.cas.String", featOkTst);
    casFeatCode_predicateID  = (null == casFeat_predicateID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_predicateID).getCode();

 
    casFeat_originalText = jcas.getRequiredFeatureDE(casType, "originalText", "uima.cas.String", featOkTst);
    casFeatCode_originalText  = (null == casFeat_originalText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_originalText).getCode();

 
    casFeat_sentenceIndex = jcas.getRequiredFeatureDE(casType, "sentenceIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_sentenceIndex  = (null == casFeat_sentenceIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentenceIndex).getCode();

 
    casFeat_lemma = jcas.getRequiredFeatureDE(casType, "lemma", "uima.cas.String", featOkTst);
    casFeatCode_lemma  = (null == casFeat_lemma) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lemma).getCode();

 
    casFeat_listOfParentPredicates = jcas.getRequiredFeatureDE(casType, "listOfParentPredicates", "uima.cas.FSArray", featOkTst);
    casFeatCode_listOfParentPredicates  = (null == casFeat_listOfParentPredicates) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_listOfParentPredicates).getCode();

 
    casFeat_listOfSemanticRoleArguments = jcas.getRequiredFeatureDE(casType, "listOfSemanticRoleArguments", "uima.cas.FSArray", featOkTst);
    casFeatCode_listOfSemanticRoleArguments  = (null == casFeat_listOfSemanticRoleArguments) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_listOfSemanticRoleArguments).getCode();

 
    casFeat_listOfSemanticRoleTypes = jcas.getRequiredFeatureDE(casType, "listOfSemanticRoleTypes", "uima.cas.StringArray", featOkTst);
    casFeatCode_listOfSemanticRoleTypes  = (null == casFeat_listOfSemanticRoleTypes) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_listOfSemanticRoleTypes).getCode();

 
    casFeat_isNegated = jcas.getRequiredFeatureDE(casType, "isNegated", "uima.cas.Boolean", featOkTst);
    casFeatCode_isNegated  = (null == casFeat_isNegated) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isNegated).getCode();

 
    casFeat_listOfModals = jcas.getRequiredFeatureDE(casType, "listOfModals", "uima.cas.StringArray", featOkTst);
    casFeatCode_listOfModals  = (null == casFeat_listOfModals) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_listOfModals).getCode();

 
    casFeat_headWord = jcas.getRequiredFeatureDE(casType, "headWord", "uima.cas.String", featOkTst);
    casFeatCode_headWord  = (null == casFeat_headWord) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_headWord).getCode();

  }
}



    