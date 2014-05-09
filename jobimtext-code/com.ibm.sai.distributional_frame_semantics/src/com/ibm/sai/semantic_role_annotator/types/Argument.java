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



/* First created by JCasGen Wed Aug 28 13:46:03 EDT 2013 */
package com.ibm.sai.semantic_role_annotator.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Nov 25 13:44:25 EST 2013
 * XML source: /users2/mchowdh/wksp_mchowdh_jobim/com.ibm.sai.semantic_role_annotator/descriptors/SemanticRoleTypeSystemDescriptor.xml
 * @generated */
public class Argument extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Argument.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Argument() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Argument(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Argument(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Argument(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: isItselfPredicate

  /** getter for isItselfPredicate - gets 
   * @generated */
  public boolean getIsItselfPredicate() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_isItselfPredicate == null)
      jcasType.jcas.throwFeatMissing("isItselfPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((Argument_Type)jcasType).casFeatCode_isItselfPredicate);}
    
  /** setter for isItselfPredicate - sets  
   * @generated */
  public void setIsItselfPredicate(boolean v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_isItselfPredicate == null)
      jcasType.jcas.throwFeatMissing("isItselfPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((Argument_Type)jcasType).casFeatCode_isItselfPredicate, v);}    
   
    
  //*--------------*
  //* Feature: correspondingPredicate

  /** getter for correspondingPredicate - gets 
   * @generated */
  public Predicate getCorrespondingPredicate() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_correspondingPredicate == null)
      jcasType.jcas.throwFeatMissing("correspondingPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return (Predicate)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Argument_Type)jcasType).casFeatCode_correspondingPredicate)));}
    
  /** setter for correspondingPredicate - sets  
   * @generated */
  public void setCorrespondingPredicate(Predicate v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_correspondingPredicate == null)
      jcasType.jcas.throwFeatMissing("correspondingPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setRefValue(addr, ((Argument_Type)jcasType).casFeatCode_correspondingPredicate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: originalText

  /** getter for originalText - gets 
   * @generated */
  public String getOriginalText() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_originalText);}
    
  /** setter for originalText - sets  
   * @generated */
  public void setOriginalText(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_originalText, v);}    
   
    
  //*--------------*
  //* Feature: sentenceIndex

  /** getter for sentenceIndex - gets 
   * @generated */
  public int getSentenceIndex() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Argument_Type)jcasType).casFeatCode_sentenceIndex);}
    
  /** setter for sentenceIndex - sets  
   * @generated */
  public void setSentenceIndex(int v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setIntValue(addr, ((Argument_Type)jcasType).casFeatCode_sentenceIndex, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: semanticRoleType

  /** getter for semanticRoleType - gets 
   * @generated */
  public String getSemanticRoleType() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_semanticRoleType == null)
      jcasType.jcas.throwFeatMissing("semanticRoleType", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_semanticRoleType);}
    
  /** setter for semanticRoleType - sets  
   * @generated */
  public void setSemanticRoleType(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_semanticRoleType == null)
      jcasType.jcas.throwFeatMissing("semanticRoleType", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_semanticRoleType, v);}    
   
    
  //*--------------*
  //* Feature: argumentID

  /** getter for argumentID - gets 
   * @generated */
  public String getArgumentID() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_argumentID == null)
      jcasType.jcas.throwFeatMissing("argumentID", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_argumentID);}
    
  /** setter for argumentID - sets  
   * @generated */
  public void setArgumentID(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_argumentID == null)
      jcasType.jcas.throwFeatMissing("argumentID", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_argumentID, v);}    
   
    
  //*--------------*
  //* Feature: ParentPredicate

  /** getter for ParentPredicate - gets 
   * @generated */
  public Predicate getParentPredicate() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_ParentPredicate == null)
      jcasType.jcas.throwFeatMissing("ParentPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return (Predicate)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Argument_Type)jcasType).casFeatCode_ParentPredicate)));}
    
  /** setter for ParentPredicate - sets  
   * @generated */
  public void setParentPredicate(Predicate v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_ParentPredicate == null)
      jcasType.jcas.throwFeatMissing("ParentPredicate", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setRefValue(addr, ((Argument_Type)jcasType).casFeatCode_ParentPredicate, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: headWord

  /** getter for headWord - gets 
   * @generated */
  public String getHeadWord() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_headWord == null)
      jcasType.jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_headWord);}
    
  /** setter for headWord - sets  
   * @generated */
  public void setHeadWord(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_headWord == null)
      jcasType.jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_headWord, v);}    
   
    
  //*--------------*
  //* Feature: posOfHeadWord

  /** getter for posOfHeadWord - gets 
   * @generated */
  public String getPosOfHeadWord() {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_posOfHeadWord == null)
      jcasType.jcas.throwFeatMissing("posOfHeadWord", "com.ibm.sai.semantic_role_annotator.types.Argument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Argument_Type)jcasType).casFeatCode_posOfHeadWord);}
    
  /** setter for posOfHeadWord - sets  
   * @generated */
  public void setPosOfHeadWord(String v) {
    if (Argument_Type.featOkTst && ((Argument_Type)jcasType).casFeat_posOfHeadWord == null)
      jcasType.jcas.throwFeatMissing("posOfHeadWord", "com.ibm.sai.semantic_role_annotator.types.Argument");
    jcasType.ll_cas.ll_setStringValue(addr, ((Argument_Type)jcasType).casFeatCode_posOfHeadWord, v);}    
  }

    