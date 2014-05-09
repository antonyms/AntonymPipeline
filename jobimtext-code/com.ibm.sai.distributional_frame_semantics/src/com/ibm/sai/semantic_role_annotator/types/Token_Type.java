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


/* First created by JCasGen Thu Aug 22 16:46:59 EDT 2013 */
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

/** Token inside a sentence. A token can consist of multi-words.
 * Updated by JCasGen Mon Nov 25 13:44:26 EST 2013
 * @generated */
public class Token_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Token_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Token(addr, Token_Type.this);
  			   Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Token(addr, Token_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Token.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ibm.sai.semantic_role_annotator.types.Token");
 
  /** @generated */
  final Feature casFeat_tokenID;
  /** @generated */
  final int     casFeatCode_tokenID;
  /** @generated */ 
  public String getTokenID(int addr) {
        if (featOkTst && casFeat_tokenID == null)
      jcas.throwFeatMissing("tokenID", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tokenID);
  }
  /** @generated */    
  public void setTokenID(int addr, String v) {
        if (featOkTst && casFeat_tokenID == null)
      jcas.throwFeatMissing("tokenID", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_tokenID, v);}
    
  
 
  /** @generated */
  final Feature casFeat_originalText;
  /** @generated */
  final int     casFeatCode_originalText;
  /** @generated */ 
  public String getOriginalText(int addr) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_originalText);
  }
  /** @generated */    
  public void setOriginalText(int addr, String v) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_originalText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_lemma;
  /** @generated */
  final int     casFeatCode_lemma;
  /** @generated */ 
  public String getLemma(int addr) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_lemma);
  }
  /** @generated */    
  public void setLemma(int addr, String v) {
        if (featOkTst && casFeat_lemma == null)
      jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_lemma, v);}
    
  
 
  /** @generated */
  final Feature casFeat_pos;
  /** @generated */
  final int     casFeatCode_pos;
  /** @generated */ 
  public String getPos(int addr) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_pos);
  }
  /** @generated */    
  public void setPos(int addr, String v) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_pos, v);}
    
  
 
  /** @generated */
  final Feature casFeat_governorToken;
  /** @generated */
  final int     casFeatCode_governorToken;
  /** @generated */ 
  public int getGovernorToken(int addr) {
        if (featOkTst && casFeat_governorToken == null)
      jcas.throwFeatMissing("governorToken", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getRefValue(addr, casFeatCode_governorToken);
  }
  /** @generated */    
  public void setGovernorToken(int addr, int v) {
        if (featOkTst && casFeat_governorToken == null)
      jcas.throwFeatMissing("governorToken", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setRefValue(addr, casFeatCode_governorToken, v);}
    
  
 
  /** @generated */
  final Feature casFeat_dependencyTypeWithGovernor;
  /** @generated */
  final int     casFeatCode_dependencyTypeWithGovernor;
  /** @generated */ 
  public String getDependencyTypeWithGovernor(int addr) {
        if (featOkTst && casFeat_dependencyTypeWithGovernor == null)
      jcas.throwFeatMissing("dependencyTypeWithGovernor", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_dependencyTypeWithGovernor);
  }
  /** @generated */    
  public void setDependencyTypeWithGovernor(int addr, String v) {
        if (featOkTst && casFeat_dependencyTypeWithGovernor == null)
      jcas.throwFeatMissing("dependencyTypeWithGovernor", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_dependencyTypeWithGovernor, v);}
    
  
 
  /** @generated */
  final Feature casFeat_listOfDependents;
  /** @generated */
  final int     casFeatCode_listOfDependents;
  /** @generated */ 
  public int getListOfDependents(int addr) {
        if (featOkTst && casFeat_listOfDependents == null)
      jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents);
  }
  /** @generated */    
  public void setListOfDependents(int addr, int v) {
        if (featOkTst && casFeat_listOfDependents == null)
      jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setRefValue(addr, casFeatCode_listOfDependents, v);}
    
   /** @generated */
  public int getListOfDependents(int addr, int i) {
        if (featOkTst && casFeat_listOfDependents == null)
      jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i);
  }
   
  /** @generated */ 
  public void setListOfDependents(int addr, int i, int v) {
        if (featOkTst && casFeat_listOfDependents == null)
      jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_listOfDependents), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_sentenceIndex;
  /** @generated */
  final int     casFeatCode_sentenceIndex;
  /** @generated */ 
  public int getSentenceIndex(int addr) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Token");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sentenceIndex);
  }
  /** @generated */    
  public void setSentenceIndex(int addr, int v) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Token");
    ll_cas.ll_setIntValue(addr, casFeatCode_sentenceIndex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Token_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tokenID = jcas.getRequiredFeatureDE(casType, "tokenID", "uima.cas.String", featOkTst);
    casFeatCode_tokenID  = (null == casFeat_tokenID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokenID).getCode();

 
    casFeat_originalText = jcas.getRequiredFeatureDE(casType, "originalText", "uima.cas.String", featOkTst);
    casFeatCode_originalText  = (null == casFeat_originalText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_originalText).getCode();

 
    casFeat_lemma = jcas.getRequiredFeatureDE(casType, "lemma", "uima.cas.String", featOkTst);
    casFeatCode_lemma  = (null == casFeat_lemma) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lemma).getCode();

 
    casFeat_pos = jcas.getRequiredFeatureDE(casType, "pos", "uima.cas.String", featOkTst);
    casFeatCode_pos  = (null == casFeat_pos) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pos).getCode();

 
    casFeat_governorToken = jcas.getRequiredFeatureDE(casType, "governorToken", "com.ibm.sai.semantic_role_annotator.types.Token", featOkTst);
    casFeatCode_governorToken  = (null == casFeat_governorToken) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_governorToken).getCode();

 
    casFeat_dependencyTypeWithGovernor = jcas.getRequiredFeatureDE(casType, "dependencyTypeWithGovernor", "uima.cas.String", featOkTst);
    casFeatCode_dependencyTypeWithGovernor  = (null == casFeat_dependencyTypeWithGovernor) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_dependencyTypeWithGovernor).getCode();

 
    casFeat_listOfDependents = jcas.getRequiredFeatureDE(casType, "listOfDependents", "uima.cas.FSArray", featOkTst);
    casFeatCode_listOfDependents  = (null == casFeat_listOfDependents) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_listOfDependents).getCode();

 
    casFeat_sentenceIndex = jcas.getRequiredFeatureDE(casType, "sentenceIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_sentenceIndex  = (null == casFeat_sentenceIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentenceIndex).getCode();

  }
}



    