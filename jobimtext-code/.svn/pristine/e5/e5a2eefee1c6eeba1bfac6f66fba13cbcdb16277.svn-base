
/* First created by JCasGen Fri Nov 02 09:35:28 CET 2012 */
package org.jobimtext.holing.type;

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

/** A JoBim is a pair of a key and several entries, that have a relationship, named in the field relation
The key and the entries are of type Entry, which is an Annotation extended by a field value, that will be used for describing the value that is extracted from the entry e.g. word#pos
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * @generated */
public class JoBim_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (JoBim_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = JoBim_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new JoBim(addr, JoBim_Type.this);
  			   JoBim_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new JoBim(addr, JoBim_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JoBim.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.holing.type.JoBim");
 
  /** @generated */
  final Feature casFeat_relation;
  /** @generated */
  final int     casFeatCode_relation;
  /** @generated */ 
  public String getRelation(int addr) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "org.jobimtext.holing.type.JoBim");
    return ll_cas.ll_getStringValue(addr, casFeatCode_relation);
  }
  /** @generated */    
  public void setRelation(int addr, String v) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "org.jobimtext.holing.type.JoBim");
    ll_cas.ll_setStringValue(addr, casFeatCode_relation, v);}
    
  
 
  /** @generated */
  final Feature casFeat_key;
  /** @generated */
  final int     casFeatCode_key;
  /** @generated */ 
  public int getKey(int addr) {
        if (featOkTst && casFeat_key == null)
      jcas.throwFeatMissing("key", "org.jobimtext.holing.type.JoBim");
    return ll_cas.ll_getRefValue(addr, casFeatCode_key);
  }
  /** @generated */    
  public void setKey(int addr, int v) {
        if (featOkTst && casFeat_key == null)
      jcas.throwFeatMissing("key", "org.jobimtext.holing.type.JoBim");
    ll_cas.ll_setRefValue(addr, casFeatCode_key, v);}
    
  
 
  /** @generated */
  final Feature casFeat_values;
  /** @generated */
  final int     casFeatCode_values;
  /** @generated */ 
  public int getValues(int addr) {
        if (featOkTst && casFeat_values == null)
      jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    return ll_cas.ll_getRefValue(addr, casFeatCode_values);
  }
  /** @generated */    
  public void setValues(int addr, int v) {
        if (featOkTst && casFeat_values == null)
      jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    ll_cas.ll_setRefValue(addr, casFeatCode_values, v);}
    
   /** @generated */
  public int getValues(int addr, int i) {
        if (featOkTst && casFeat_values == null)
      jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_values), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_values), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_values), i);
  }
   
  /** @generated */ 
  public void setValues(int addr, int i, int v) {
        if (featOkTst && casFeat_values == null)
      jcas.throwFeatMissing("values", "org.jobimtext.holing.type.JoBim");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_values), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_values), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_values), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_hole;
  /** @generated */
  final int     casFeatCode_hole;
  /** @generated */ 
  public int getHole(int addr) {
        if (featOkTst && casFeat_hole == null)
      jcas.throwFeatMissing("hole", "org.jobimtext.holing.type.JoBim");
    return ll_cas.ll_getIntValue(addr, casFeatCode_hole);
  }
  /** @generated */    
  public void setHole(int addr, int v) {
        if (featOkTst && casFeat_hole == null)
      jcas.throwFeatMissing("hole", "org.jobimtext.holing.type.JoBim");
    ll_cas.ll_setIntValue(addr, casFeatCode_hole, v);}
    
  
 
  /** @generated */
  final Feature casFeat_model;
  /** @generated */
  final int     casFeatCode_model;
  /** @generated */ 
  public String getModel(int addr) {
        if (featOkTst && casFeat_model == null)
      jcas.throwFeatMissing("model", "org.jobimtext.holing.type.JoBim");
    return ll_cas.ll_getStringValue(addr, casFeatCode_model);
  }
  /** @generated */    
  public void setModel(int addr, String v) {
        if (featOkTst && casFeat_model == null)
      jcas.throwFeatMissing("model", "org.jobimtext.holing.type.JoBim");
    ll_cas.ll_setStringValue(addr, casFeatCode_model, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public JoBim_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_relation = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.String", featOkTst);
    casFeatCode_relation  = (null == casFeat_relation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_relation).getCode();

 
    casFeat_key = jcas.getRequiredFeatureDE(casType, "key", "uima.tcas.Annotation", featOkTst);
    casFeatCode_key  = (null == casFeat_key) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_key).getCode();

 
    casFeat_values = jcas.getRequiredFeatureDE(casType, "values", "uima.cas.FSArray", featOkTst);
    casFeatCode_values  = (null == casFeat_values) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_values).getCode();

 
    casFeat_hole = jcas.getRequiredFeatureDE(casType, "hole", "uima.cas.Integer", featOkTst);
    casFeatCode_hole  = (null == casFeat_hole) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_hole).getCode();

 
    casFeat_model = jcas.getRequiredFeatureDE(casType, "model", "uima.cas.String", featOkTst);
    casFeatCode_model  = (null == casFeat_model) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_model).getCode();

  }
}



    