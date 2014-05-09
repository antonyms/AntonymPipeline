
/* First created by JCasGen Tue Aug 06 08:59:13 EDT 2013 */
package org.jobimtext.type;

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

/** 
 * Updated by JCasGen Tue Aug 06 08:59:13 EDT 2013
 * @generated */
public class DistributionalExpansions_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DistributionalExpansions_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DistributionalExpansions_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DistributionalExpansions(addr, DistributionalExpansions_Type.this);
  			   DistributionalExpansions_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DistributionalExpansions(addr, DistributionalExpansions_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DistributionalExpansions.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.type.DistributionalExpansions");
 
  /** @generated */
  final Feature casFeat_entries;
  /** @generated */
  final int     casFeatCode_entries;
  /** @generated */ 
  public int getEntries(int addr) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    return ll_cas.ll_getRefValue(addr, casFeatCode_entries);
  }
  /** @generated */    
  public void setEntries(int addr, int v) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    ll_cas.ll_setRefValue(addr, casFeatCode_entries, v);}
    
   /** @generated */
  public int getEntries(int addr, int i) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
  }
   
  /** @generated */ 
  public void setEntries(int addr, int i, int v) {
        if (featOkTst && casFeat_entries == null)
      jcas.throwFeatMissing("entries", "org.jobimtext.type.DistributionalExpansions");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_entries), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DistributionalExpansions_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_entries = jcas.getRequiredFeatureDE(casType, "entries", "uima.cas.FSArray", featOkTst);
    casFeatCode_entries  = (null == casFeat_entries) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_entries).getCode();

  }
}



    