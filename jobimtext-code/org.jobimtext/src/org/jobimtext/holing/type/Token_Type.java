
/* First created by JCasGen Wed Apr 17 15:03:32 EDT 2013 */
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

/** 
 * Updated by JCasGen Mon Mar 03 10:45:14 CET 2014
 * @generated */
public class Token_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Token_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Token(addr, Token_Type.this);
  			   Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Token(addr, Token_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Token.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.holing.type.Token");



  /** @generated */
  final Feature casFeat_POS;
  /** @generated */
  final int     casFeatCode_POS;
  /** @generated */ 
  public int getPOS(int addr) {
        if (featOkTst && casFeat_POS == null)
      jcas.throwFeatMissing("POS", "org.jobimtext.holing.type.Token");
    return ll_cas.ll_getRefValue(addr, casFeatCode_POS);
  }
  /** @generated */    
  public void setPOS(int addr, int v) {
        if (featOkTst && casFeat_POS == null)
      jcas.throwFeatMissing("POS", "org.jobimtext.holing.type.Token");
    ll_cas.ll_setRefValue(addr, casFeatCode_POS, v);}
    
  
 
  /** @generated */
  final Feature casFeat_skip;
  /** @generated */
  final int     casFeatCode_skip;
  /** @generated */ 
  public boolean getSkip(int addr) {
        if (featOkTst && casFeat_skip == null)
      jcas.throwFeatMissing("skip", "org.jobimtext.holing.type.Token");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_skip);
  }
  /** @generated */    
  public void setSkip(int addr, boolean v) {
        if (featOkTst && casFeat_skip == null)
      jcas.throwFeatMissing("skip", "org.jobimtext.holing.type.Token");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_skip, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Token_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_POS = jcas.getRequiredFeatureDE(casType, "POS", "org.jobimtext.holing.type.POS", featOkTst);
    casFeatCode_POS  = (null == casFeat_POS) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_POS).getCode();

 
    casFeat_skip = jcas.getRequiredFeatureDE(casType, "skip", "uima.cas.Boolean", featOkTst);
    casFeatCode_skip  = (null == casFeat_skip) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_skip).getCode();

  }
}



    