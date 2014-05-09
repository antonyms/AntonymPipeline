

/* First created by JCasGen Tue Aug 06 08:53:33 EDT 2013 */
package org.jobimtext.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Annotation to store the distributional expansions for a word.
 * Updated by JCasGen Tue Aug 06 08:59:12 EDT 2013
 * XML source: /home/mriedl/workspace/org.jobimtext/descriptors/com/ibm/sai/jobimtext/type/DistributionalExpansion.xml
 * @generated */
public class DistributionalExpansion extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DistributionalExpansion.class);
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
  protected DistributionalExpansion() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DistributionalExpansion(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DistributionalExpansion(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DistributionalExpansion(JCas jcas, int begin, int end) {
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
  //* Feature: key

  /** getter for key - gets 
   * @generated */
  public String getKey() {
    if (DistributionalExpansion_Type.featOkTst && ((DistributionalExpansion_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.jobimtext.type.DistributionalExpansion");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DistributionalExpansion_Type)jcasType).casFeatCode_key);}
    
  /** setter for key - sets  
   * @generated */
  public void setKey(String v) {
    if (DistributionalExpansion_Type.featOkTst && ((DistributionalExpansion_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.jobimtext.type.DistributionalExpansion");
    jcasType.ll_cas.ll_setStringValue(addr, ((DistributionalExpansion_Type)jcasType).casFeatCode_key, v);}    
   
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (DistributionalExpansion_Type.featOkTst && ((DistributionalExpansion_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "org.jobimtext.type.DistributionalExpansion");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((DistributionalExpansion_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (DistributionalExpansion_Type.featOkTst && ((DistributionalExpansion_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "org.jobimtext.type.DistributionalExpansion");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((DistributionalExpansion_Type)jcasType).casFeatCode_score, v);}    
  }

    