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
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.cas.StringArray;


import org.apache.uima.jcas.cas.IntegerList;


/** A Predicate holds the arguments that have semantic roles
 * Updated by JCasGen Mon Nov 25 13:44:26 EST 2013
 * XML source: /users2/mchowdh/wksp_mchowdh_jobim/com.ibm.sai.semantic_role_annotator/descriptors/SemanticRoleTypeSystemDescriptor.xml
 * @generated */
public class Predicate extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Predicate.class);
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
  protected Predicate() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Predicate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Predicate(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Predicate(JCas jcas, int begin, int end) {
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
  //* Feature: predicateID

  /** getter for predicateID - gets 
   * @generated */
  public String getPredicateID() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_predicateID == null)
      jcasType.jcas.throwFeatMissing("predicateID", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_predicateID);}
    
  /** setter for predicateID - sets  
   * @generated */
  public void setPredicateID(String v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_predicateID == null)
      jcasType.jcas.throwFeatMissing("predicateID", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_predicateID, v);}    
   
    
  //*--------------*
  //* Feature: listOfSemanticRoleArguments

  /** getter for listOfSemanticRoleArguments - gets 
   * @generated */
  public FSArray getListOfSemanticRoleArguments() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleArguments == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments)));}
    
  /** setter for listOfSemanticRoleArguments - sets  
   * @generated */
  public void setListOfSemanticRoleArguments(FSArray v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleArguments == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for listOfSemanticRoleArguments - gets an indexed value - 
   * @generated */
  public Argument getListOfSemanticRoleArguments(int i) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleArguments == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments), i);
    return (Argument)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments), i)));}

  /** indexed setter for listOfSemanticRoleArguments - sets an indexed value - 
   * @generated */
  public void setListOfSemanticRoleArguments(int i, Argument v) { 
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleArguments == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleArguments", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleArguments), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: originalText

  /** getter for originalText - gets 
   * @generated */
  public String getOriginalText() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_originalText);}
    
  /** setter for originalText - sets  
   * @generated */
  public void setOriginalText(String v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_originalText, v);}    
   
    
  //*--------------*
  //* Feature: sentenceIndex

  /** getter for sentenceIndex - gets 
   * @generated */
  public int getSentenceIndex() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Predicate_Type)jcasType).casFeatCode_sentenceIndex);}
    
  /** setter for sentenceIndex - sets  
   * @generated */
  public void setSentenceIndex(int v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setIntValue(addr, ((Predicate_Type)jcasType).casFeatCode_sentenceIndex, v);}    
   
    
  //*--------------*
  //* Feature: lemma

  /** getter for lemma - gets 
   * @generated */
  public String getLemma() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_lemma);}
    
  /** setter for lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_lemma == null)
      jcasType.jcas.throwFeatMissing("lemma", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_lemma, v);}    
   
    
  //*--------------*
  //* Feature: listOfParentPredicates

  /** getter for listOfParentPredicates - gets 
   * @generated */
  public FSArray getListOfParentPredicates() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfParentPredicates == null)
      jcasType.jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates)));}
    
  /** setter for listOfParentPredicates - sets  
   * @generated */
  public void setListOfParentPredicates(FSArray v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfParentPredicates == null)
      jcasType.jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for listOfParentPredicates - gets an indexed value - 
   * @generated */
  public Predicate getListOfParentPredicates(int i) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfParentPredicates == null)
      jcasType.jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates), i);
    return (Predicate)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates), i)));}

  /** indexed setter for listOfParentPredicates - sets an indexed value - 
   * @generated */
  public void setListOfParentPredicates(int i, Predicate v) { 
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfParentPredicates == null)
      jcasType.jcas.throwFeatMissing("listOfParentPredicates", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfParentPredicates), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: listOfSemanticRoleTypes

  /** getter for listOfSemanticRoleTypes - gets 
   * @generated */
  public StringArray getListOfSemanticRoleTypes() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleTypes == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes)));}
    
  /** setter for listOfSemanticRoleTypes - sets  
   * @generated */
  public void setListOfSemanticRoleTypes(StringArray v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleTypes == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for listOfSemanticRoleTypes - gets an indexed value - 
   * @generated */
  public String getListOfSemanticRoleTypes(int i) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleTypes == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes), i);}

  /** indexed setter for listOfSemanticRoleTypes - sets an indexed value - 
   * @generated */
  public void setListOfSemanticRoleTypes(int i, String v) { 
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfSemanticRoleTypes == null)
      jcasType.jcas.throwFeatMissing("listOfSemanticRoleTypes", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfSemanticRoleTypes), i, v);}
   
    
  //*--------------*
  //* Feature: isNegated

  /** getter for isNegated - gets 
   * @generated */
  public boolean getIsNegated() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_isNegated == null)
      jcasType.jcas.throwFeatMissing("isNegated", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((Predicate_Type)jcasType).casFeatCode_isNegated);}
    
  /** setter for isNegated - sets  
   * @generated */
  public void setIsNegated(boolean v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_isNegated == null)
      jcasType.jcas.throwFeatMissing("isNegated", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((Predicate_Type)jcasType).casFeatCode_isNegated, v);}    
   
    
  //*--------------*
  //* Feature: listOfModals

  /** getter for listOfModals - gets 
   * @generated */
  public StringArray getListOfModals() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfModals == null)
      jcasType.jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals)));}
    
  /** setter for listOfModals - sets  
   * @generated */
  public void setListOfModals(StringArray v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfModals == null)
      jcasType.jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for listOfModals - gets an indexed value - 
   * @generated */
  public String getListOfModals(int i) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfModals == null)
      jcasType.jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals), i);}

  /** indexed setter for listOfModals - sets an indexed value - 
   * @generated */
  public void setListOfModals(int i, String v) { 
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_listOfModals == null)
      jcasType.jcas.throwFeatMissing("listOfModals", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Predicate_Type)jcasType).casFeatCode_listOfModals), i, v);}
   
    
  //*--------------*
  //* Feature: headWord

  /** getter for headWord - gets 
   * @generated */
  public String getHeadWord() {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_headWord == null)
      jcasType.jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_headWord);}
    
  /** setter for headWord - sets  
   * @generated */
  public void setHeadWord(String v) {
    if (Predicate_Type.featOkTst && ((Predicate_Type)jcasType).casFeat_headWord == null)
      jcasType.jcas.throwFeatMissing("headWord", "com.ibm.sai.semantic_role_annotator.types.Predicate");
    jcasType.ll_cas.ll_setStringValue(addr, ((Predicate_Type)jcasType).casFeatCode_headWord, v);}    
  }

    