
/* First created by JCasGen Mon Sep 16 13:25:14 EDT 2013 */
package org.jobimtext.ingestion.type;

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

/** Information regarding the corpus.
 * Updated by JCasGen Mon Sep 16 13:25:14 EDT 2013
 * @generated */
public class CorpusInfo_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CorpusInfo_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CorpusInfo_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CorpusInfo(addr, CorpusInfo_Type.this);
  			   CorpusInfo_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CorpusInfo(addr, CorpusInfo_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CorpusInfo.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.ingestion.type.CorpusInfo");
 
  /** @generated */
  final Feature casFeat_numFiles;
  /** @generated */
  final int     casFeatCode_numFiles;
  /** @generated */ 
  public int getNumFiles(int addr) {
        if (featOkTst && casFeat_numFiles == null)
      jcas.throwFeatMissing("numFiles", "org.jobimtext.ingestion.type.CorpusInfo");
    return ll_cas.ll_getIntValue(addr, casFeatCode_numFiles);
  }
  /** @generated */    
  public void setNumFiles(int addr, int v) {
        if (featOkTst && casFeat_numFiles == null)
      jcas.throwFeatMissing("numFiles", "org.jobimtext.ingestion.type.CorpusInfo");
    ll_cas.ll_setIntValue(addr, casFeatCode_numFiles, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CorpusInfo_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_numFiles = jcas.getRequiredFeatureDE(casType, "numFiles", "uima.cas.Integer", featOkTst);
    casFeatCode_numFiles  = (null == casFeat_numFiles) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_numFiles).getCode();

  }
}



    