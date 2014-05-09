
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

/** Information for passing information about documents
				between JoBim ingestion pieces.
 * Updated by JCasGen Mon Sep 16 13:25:14 EDT 2013
 * @generated */
public class DocumentInfo_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DocumentInfo_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DocumentInfo_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DocumentInfo(addr, DocumentInfo_Type.this);
  			   DocumentInfo_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DocumentInfo(addr, DocumentInfo_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DocumentInfo.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.jobimtext.ingestion.type.DocumentInfo");
 
  /** @generated */
  final Feature casFeat_inputFile;
  /** @generated */
  final int     casFeatCode_inputFile;
  /** @generated */ 
  public String getInputFile(int addr) {
        if (featOkTst && casFeat_inputFile == null)
      jcas.throwFeatMissing("inputFile", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getStringValue(addr, casFeatCode_inputFile);
  }
  /** @generated */    
  public void setInputFile(int addr, String v) {
        if (featOkTst && casFeat_inputFile == null)
      jcas.throwFeatMissing("inputFile", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setStringValue(addr, casFeatCode_inputFile, v);}
    
  
 
  /** @generated */
  final Feature casFeat_threadId;
  /** @generated */
  final int     casFeatCode_threadId;
  /** @generated */ 
  public int getThreadId(int addr) {
        if (featOkTst && casFeat_threadId == null)
      jcas.throwFeatMissing("threadId", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getIntValue(addr, casFeatCode_threadId);
  }
  /** @generated */    
  public void setThreadId(int addr, int v) {
        if (featOkTst && casFeat_threadId == null)
      jcas.throwFeatMissing("threadId", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setIntValue(addr, casFeatCode_threadId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_error;
  /** @generated */
  final int     casFeatCode_error;
  /** @generated */ 
  public boolean getError(int addr) {
        if (featOkTst && casFeat_error == null)
      jcas.throwFeatMissing("error", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_error);
  }
  /** @generated */    
  public void setError(int addr, boolean v) {
        if (featOkTst && casFeat_error == null)
      jcas.throwFeatMissing("error", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_error, v);}
    
  
 
  /** @generated */
  final Feature casFeat_errorMessage;
  /** @generated */
  final int     casFeatCode_errorMessage;
  /** @generated */ 
  public String getErrorMessage(int addr) {
        if (featOkTst && casFeat_errorMessage == null)
      jcas.throwFeatMissing("errorMessage", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getStringValue(addr, casFeatCode_errorMessage);
  }
  /** @generated */    
  public void setErrorMessage(int addr, String v) {
        if (featOkTst && casFeat_errorMessage == null)
      jcas.throwFeatMissing("errorMessage", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setStringValue(addr, casFeatCode_errorMessage, v);}
    
  
 
  /** @generated */
  final Feature casFeat_serializedDocument;
  /** @generated */
  final int     casFeatCode_serializedDocument;
  /** @generated */ 
  public int getSerializedDocument(int addr) {
        if (featOkTst && casFeat_serializedDocument == null)
      jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument);
  }
  /** @generated */    
  public void setSerializedDocument(int addr, int v) {
        if (featOkTst && casFeat_serializedDocument == null)
      jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setRefValue(addr, casFeatCode_serializedDocument, v);}
    
   /** @generated */
  public byte getSerializedDocument(int addr, int i) {
        if (featOkTst && casFeat_serializedDocument == null)
      jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i);
	return ll_cas.ll_getByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i);
  }
   
  /** @generated */ 
  public void setSerializedDocument(int addr, int i, byte v) {
        if (featOkTst && casFeat_serializedDocument == null)
      jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    if (lowLevelTypeChecks)
      ll_cas.ll_setByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i);
    ll_cas.ll_setByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_serializedDocument), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_last;
  /** @generated */
  final int     casFeatCode_last;
  /** @generated */ 
  public boolean getLast(int addr) {
        if (featOkTst && casFeat_last == null)
      jcas.throwFeatMissing("last", "org.jobimtext.ingestion.type.DocumentInfo");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_last);
  }
  /** @generated */    
  public void setLast(int addr, boolean v) {
        if (featOkTst && casFeat_last == null)
      jcas.throwFeatMissing("last", "org.jobimtext.ingestion.type.DocumentInfo");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_last, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DocumentInfo_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_inputFile = jcas.getRequiredFeatureDE(casType, "inputFile", "uima.cas.String", featOkTst);
    casFeatCode_inputFile  = (null == casFeat_inputFile) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_inputFile).getCode();

 
    casFeat_threadId = jcas.getRequiredFeatureDE(casType, "threadId", "uima.cas.Integer", featOkTst);
    casFeatCode_threadId  = (null == casFeat_threadId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_threadId).getCode();

 
    casFeat_error = jcas.getRequiredFeatureDE(casType, "error", "uima.cas.Boolean", featOkTst);
    casFeatCode_error  = (null == casFeat_error) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_error).getCode();

 
    casFeat_errorMessage = jcas.getRequiredFeatureDE(casType, "errorMessage", "uima.cas.String", featOkTst);
    casFeatCode_errorMessage  = (null == casFeat_errorMessage) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_errorMessage).getCode();

 
    casFeat_serializedDocument = jcas.getRequiredFeatureDE(casType, "serializedDocument", "uima.cas.ByteArray", featOkTst);
    casFeatCode_serializedDocument  = (null == casFeat_serializedDocument) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_serializedDocument).getCode();

 
    casFeat_last = jcas.getRequiredFeatureDE(casType, "last", "uima.cas.Boolean", featOkTst);
    casFeatCode_last  = (null == casFeat_last) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_last).getCode();

  }
}



    