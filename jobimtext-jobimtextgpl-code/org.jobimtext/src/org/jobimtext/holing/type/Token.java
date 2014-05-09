

/* First created by JCasGen Wed Apr 17 15:03:32 EDT 2013 */
package org.jobimtext.holing.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * XML source: /Users/riedl/work/workspaces/workspace_ibm_contribution/org.jobimtext/descriptors/org/jobimtext/holing/type/JoBimTextTypeSystem.xml
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
  //* Feature: POS

  /** getter for POS - gets 
   * @generated */
  public POS getPOS() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "org.jobimtext.holing.type.Token");
    return (POS)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Token_Type)jcasType).casFeatCode_POS)));}
    
  /** setter for POS - sets  
   * @generated */
  public void setPOS(POS v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_POS == null)
      jcasType.jcas.throwFeatMissing("POS", "org.jobimtext.holing.type.Token");
    jcasType.ll_cas.ll_setRefValue(addr, ((Token_Type)jcasType).casFeatCode_POS, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: skip

  /** getter for skip - gets 
   * @generated */
  public boolean getSkip() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_skip == null)
      jcasType.jcas.throwFeatMissing("skip", "org.jobimtext.holing.type.Token");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((Token_Type)jcasType).casFeatCode_skip);}
    
  /** setter for skip - sets  
   * @generated */
  public void setSkip(boolean v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_skip == null)
      jcasType.jcas.throwFeatMissing("skip", "org.jobimtext.holing.type.Token");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((Token_Type)jcasType).casFeatCode_skip, v);}    
  }

    