

/* First created by JCasGen Tue Aug 06 08:59:13 EDT 2013 */
package org.jobimtext.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Aug 06 08:59:13 EDT 2013
 * XML source: /home/mriedl/workspace/org.jobimtext/descriptors/com/ibm/sai/jobimtext/type/DistributionalExpansion.xml
 * @generated */
public class DistributionalExpansions extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DistributionalExpansions.class);
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
  protected DistributionalExpansions() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DistributionalExpansions(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DistributionalExpansions(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DistributionalExpansions(JCas jcas, int begin, int end) {
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
  //* Feature: entries

  /** getter for entries - gets 
   * @generated */
  public FSArray getEntries() {
    if (DistributionalExpansions_Type.featOkTst && ((DistributionalExpansions_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries)));}
    
  /** setter for entries - sets  
   * @generated */
  public void setEntries(FSArray v) {
    if (DistributionalExpansions_Type.featOkTst && ((DistributionalExpansions_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    jcasType.ll_cas.ll_setRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for entries - gets an indexed value - 
   * @generated */
  public DistributionalExpansion getEntries(int i) {
    if (DistributionalExpansions_Type.featOkTst && ((DistributionalExpansions_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries), i);
    return (DistributionalExpansion)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries), i)));}

  /** indexed setter for entries - sets an indexed value - 
   * @generated */
  public void setEntries(int i, DistributionalExpansion v) { 
    if (DistributionalExpansions_Type.featOkTst && ((DistributionalExpansions_Type)jcasType).casFeat_entries == null)
      jcasType.jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DistributionalExpansions_Type)jcasType).casFeatCode_entries), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    