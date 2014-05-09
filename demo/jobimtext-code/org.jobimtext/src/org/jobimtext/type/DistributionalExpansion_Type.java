
/* First created by JCasGen Tue Aug 06 08:53:33 EDT 2013 */
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

/** Annotation to store the distributional expansions for a word.
 * Updated by JCasGen Tue Aug 06 08:59:13 EDT 2013
 * @generated */
public class DistributionalExpansion_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DistributionalExpansion_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DistributionalExpansion_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DistributionalExpansion(addr, DistributionalExpansion_Type.this);
  			   DistributionalExpansion_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DistributionalExpansion(addr, DistributionalExpansion_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DistributionalExpansion.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.type.DistributionalExpansion");
 
  /** @generated */
  final Feature casFeat_key;
  /** @generated */
  final int     casFeatCode_key;
  /** @generated */ 
  public String getKey(int addr) {
        if (featOkTst && casFeat_key == null)
      jcas.throwFeatMissing("key", "org.jobimtext.type.DistributionalExpansion");
    return ll_cas.ll_getStringValue(addr, casFeatCode_key);
  }
  /** @generated */    
  public void setKey(int addr, String v) {
        if (featOkTst && casFeat_key == null)
      jcas.throwFeatMissing("key", "org.jobimtext.type.DistributionalExpansion");
    ll_cas.ll_setStringValue(addr, casFeatCode_key, v);}
    
  
 
  /** @generated */
  final Feature casFeat_score;
  /** @generated */
  final int     casFeatCode_score;
  /** @generated */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "org.jobimtext.type.DistributionalExpansion");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_score);
  }
  /** @generated */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "org.jobimtext.type.DistributionalExpansion");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_score, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DistributionalExpansion_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_key = jcas.getRequiredFeatureDE(casType, "key", "uima.cas.String", featOkTst);
    casFeatCode_key  = (null == casFeat_key) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_key).getCode();

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Double", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

  }
}



    