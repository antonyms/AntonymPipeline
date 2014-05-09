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
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** Token inside a sentence. A token can consist of multi-words.
 * Updated by JCasGen Mon Nov 25 13:44:26 EST 2013
 * XML source: /users2/mchowdh/wksp_mchowdh_jobim/com.ibm.sai.semantic_role_annotator/descriptors/SemanticRoleTypeSystemDescriptor.xml
 * @generated */
public class Token extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Token.class);
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
  protected Token() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Token(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Token(JCas jcas, int begin, int end) {
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
  //* Feature: tokenID

  /** getter for tokenID - gets 
   * @generated */
  public String getTokenID() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_tokenID == null)
      jcasType.jcas.throwFeatMissing("tokenID", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_tokenID);}
    
  /** setter for tokenID - sets  
   * @generated */
  public void setTokenID(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_tokenID == null)
      jcasType.jcas.throwFeatMissing("tokenID", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_tokenID, v);}    
   
    
  //*--------------*
  //* Feature: originalText

  /** getter for originalText - gets 
   * @generated */
  public String getOriginalText() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_originalText);}
    
  /** setter for originalText - sets  
   * @generated */
  public void setOriginalText(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_originalText, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: pos

  /** getter for pos - gets 
   * @generated */
  public String getPos() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_pos);}
    
  /** setter for pos - sets  
   * @generated */
  public void setPos(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_pos, v);}    
   
    
  //*--------------*
  //* Feature: governorToken

  /** getter for governorToken - gets 
   * @generated */
  public Token getGovernorToken() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_governorToken == null)
      jcasType.jcas.throwFeatMissing("governorToken", "com.ibm.sai.semantic_role_annotator.types.Token");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_governorToken)));}
    
  /** setter for governorToken - sets  
   * @generated */
  public void setGovernorToken(Token v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_governorToken == null)
      jcasType.jcas.throwFeatMissing("governorToken", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setRefValue(addr, ((Token_Type)jcasType).casFeatCode_governorToken, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: dependencyTypeWithGovernor

  /** getter for dependencyTypeWithGovernor - gets 
   * @generated */
  public String getDependencyTypeWithGovernor() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_dependencyTypeWithGovernor == null)
      jcasType.jcas.throwFeatMissing("dependencyTypeWithGovernor", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_dependencyTypeWithGovernor);}
    
  /** setter for dependencyTypeWithGovernor - sets  
   * @generated */
  public void setDependencyTypeWithGovernor(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_dependencyTypeWithGovernor == null)
      jcasType.jcas.throwFeatMissing("dependencyTypeWithGovernor", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_dependencyTypeWithGovernor, v);}    
   
    
  //*--------------*
  //* Feature: listOfDependents

  /** getter for listOfDependents - gets 
   * @generated */
  public FSArray getListOfDependents() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_listOfDependents == null)
      jcasType.jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents)));}
    
  /** setter for listOfDependents - sets  
   * @generated */
  public void setListOfDependents(FSArray v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_listOfDependents == null)
      jcasType.jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for listOfDependents - gets an indexed value - 
   * @generated */
  public Token getListOfDependents(int i) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_listOfDependents == null)
      jcasType.jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents), i);
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents), i)));}

  /** indexed setter for listOfDependents - sets an indexed value - 
   * @generated */
  public void setListOfDependents(int i, Token v) { 
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_listOfDependents == null)
      jcasType.jcas.throwFeatMissing("listOfDependents", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_listOfDependents), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: sentenceIndex

  /** getter for sentenceIndex - gets 
   * @generated */
  public int getSentenceIndex() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Token");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Token_Type)jcasType).casFeatCode_sentenceIndex);}
    
  /** setter for sentenceIndex - sets  
   * @generated */
  public void setSentenceIndex(int v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Token");
    jcasType.ll_cas.ll_setIntValue(addr, ((Token_Type)jcasType).casFeatCode_sentenceIndex, v);}    
  }

    