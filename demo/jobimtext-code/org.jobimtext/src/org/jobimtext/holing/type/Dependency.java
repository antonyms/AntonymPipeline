

/* First created by JCasGen Wed Apr 24 10:00:07 EDT 2013 */
package org.jobimtext.holing.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * XML source: /Users/riedl/work/workspaces/workspace_ibm_contribution/org.jobimtext/descriptors/org/jobimtext/holing/type/JoBimTextTypeSystem.xml
 * @generated */
public class Dependency extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Dependency.class);
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
  protected Dependency() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Dependency(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Dependency(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Dependency(JCas jcas, int begin, int end) {
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
  //* Feature: Governor

  /** getter for Governor - gets 
   * @generated */
  public Token getGovernor() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_Governor == null)
      jcasType.jcas.throwFeatMissing("Governor", "org.jobimtext.holing.type.Dependency");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_Governor)));}
    
  /** setter for Governor - sets  
   * @generated */
  public void setGovernor(Token v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_Governor == null)
      jcasType.jcas.throwFeatMissing("Governor", "org.jobimtext.holing.type.Dependency");
    jcasType.ll_cas.ll_setRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_Governor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Dependent

  /** getter for Dependent - gets 
   * @generated */
  public Token getDependent() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_Dependent == null)
      jcasType.jcas.throwFeatMissing("Dependent", "org.jobimtext.holing.type.Dependency");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_Dependent)));}
    
  /** setter for Dependent - sets  
   * @generated */
  public void setDependent(Token v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_Dependent == null)
      jcasType.jcas.throwFeatMissing("Dependent", "org.jobimtext.holing.type.Dependency");
    jcasType.ll_cas.ll_setRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_Dependent, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: DependencyType

  /** getter for DependencyType - gets 
   * @generated */
  public String getDependencyType() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_DependencyType == null)
      jcasType.jcas.throwFeatMissing("DependencyType", "org.jobimtext.holing.type.Dependency");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Dependency_Type)jcasType).casFeatCode_DependencyType);}
    
  /** setter for DependencyType - sets  
   * @generated */
  public void setDependencyType(String v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_DependencyType == null)
      jcasType.jcas.throwFeatMissing("DependencyType", "org.jobimtext.holing.type.Dependency");
    jcasType.ll_cas.ll_setStringValue(addr, ((Dependency_Type)jcasType).casFeatCode_DependencyType, v);}    
  }

    