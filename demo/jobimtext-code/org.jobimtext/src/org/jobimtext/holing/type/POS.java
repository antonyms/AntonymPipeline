

/* First created by JCasGen Tue Apr 23 21:50:58 EDT 2013 */
package org.jobimtext.holing.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * XML source: /Users/riedl/work/workspaces/workspace_ibm_contribution/org.jobimtext/descriptors/org/jobimtext/holing/type/JoBimTextTypeSystem.xml
 * @generated */
public class POS extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(POS.class);
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
  protected POS() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public POS(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public POS(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public POS(JCas jcas, int begin, int end) {
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
  //* Feature: PosValue

  /** getter for PosValue - gets 
   * @generated */
  public String getPosValue() {
    if (POS_Type.featOkTst && ((POS_Type)jcasType).casFeat_PosValue == null)
      jcasType.jcas.throwFeatMissing("PosValue", "org.jobimtext.holing.type.POS");
    return jcasType.ll_cas.ll_getStringValue(addr, ((POS_Type)jcasType).casFeatCode_PosValue);}
    
  /** setter for PosValue - sets  
   * @generated */
  public void setPosValue(String v) {
    if (POS_Type.featOkTst && ((POS_Type)jcasType).casFeat_PosValue == null)
      jcasType.jcas.throwFeatMissing("PosValue", "org.jobimtext.holing.type.POS");
    jcasType.ll_cas.ll_setStringValue(addr, ((POS_Type)jcasType).casFeatCode_PosValue, v);}    
  }

    