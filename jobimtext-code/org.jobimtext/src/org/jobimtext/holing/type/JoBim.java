

/* First created by JCasGen Fri Nov 02 09:35:28 CET 2012 */
package org.jobimtext.holing.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** A JoBim is a pair of a key and several entries, that have a relationship, named in the field relation
The key and the entries are of type Entry, which is an Annotation extended by a field value, that will be used for describing the value that is extracted from the entry e.g. word#pos
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * XML source: /Users/riedl/work/workspaces/workspace_ibm_contribution/org.jobimtext/descriptors/org/jobimtext/holing/type/JoBimTextTypeSystem.xml
 * @generated */
public class JoBim extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(JoBim.class);
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
  protected JoBim() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public JoBim(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public JoBim(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public JoBim(JCas jcas, int begin, int end) {
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
  //* Feature: relation

  /** getter for relation - gets 
   * @generated */
  public String getRelation() {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.jobimtext.holing.type.JoBim");
    return jcasType.ll_cas.ll_getStringValue(addr, ((JoBim_Type)jcasType).casFeatCode_relation);}
    
  /** setter for relation - sets  
   * @generated */
  public void setRelation(String v) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "org.jobimtext.holing.type.JoBim");
    jcasType.ll_cas.ll_setStringValue(addr, ((JoBim_Type)jcasType).casFeatCode_relation, v);}    
   
    
  //*--------------*
  //* Feature: key

  /** getter for key - gets 
   * @generated */
  public Annotation getKey() {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.jobimtext.holing.type.JoBim");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_key)));}
    
  /** setter for key - sets  
   * @generated */
  public void setKey(Annotation v) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "org.jobimtext.holing.type.JoBim");
    jcasType.ll_cas.ll_setRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_key, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: values

  /** getter for values - gets 
   * @generated */
  public FSArray getValues() {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values)));}
    
  /** setter for values - sets  
   * @generated */
  public void setValues(FSArray v) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    jcasType.ll_cas.ll_setRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for values - gets an indexed value - 
   * @generated */
  public Annotation getValues(int i) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values), i);
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values), i)));}

  /** indexed setter for values - sets an indexed value - 
   * @generated */
  public void setValues(int i, Annotation v) { 
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_values == null)
      jcasType.jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((JoBim_Type)jcasType).casFeatCode_values), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: hole

  /** getter for hole - gets 
   * @generated */
  public int getHole() {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_hole == null)
      jcasType.jcas.throwFeatMissing("hole", "org.jobimtext.holing.type.JoBim");
    return jcasType.ll_cas.ll_getIntValue(addr, ((JoBim_Type)jcasType).casFeatCode_hole);}
    
  /** setter for hole - sets  
   * @generated */
  public void setHole(int v) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_hole == null)
      jcasType.jcas.throwFeatMissing("hole", "org.jobimtext.holing.type.JoBim");
    jcasType.ll_cas.ll_setIntValue(addr, ((JoBim_Type)jcasType).casFeatCode_hole, v);}    
   
    
  //*--------------*
  //* Feature: model

  /** getter for model - gets Specifies the class of the jobim annotation.
   * @generated */
  public String getModel() {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_model == null)
      jcasType.jcas.throwFeatMissing("model", "org.jobimtext.holing.type.JoBim");
    return jcasType.ll_cas.ll_getStringValue(addr, ((JoBim_Type)jcasType).casFeatCode_model);}
    
  /** setter for model - sets Specifies the class of the jobim annotation. 
   * @generated */
  public void setModel(String v) {
    if (JoBim_Type.featOkTst && ((JoBim_Type)jcasType).casFeat_model == null)
      jcasType.jcas.throwFeatMissing("model", "org.jobimtext.holing.type.JoBim");
    jcasType.ll_cas.ll_setStringValue(addr, ((JoBim_Type)jcasType).casFeatCode_model, v);}    
  }

    