

/* First created by JCasGen Mon Sep 16 13:25:13 EDT 2013 */
package org.jobimtext.ingestion.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Information regarding the corpus.
 * Updated by JCasGen Mon Sep 16 13:25:13 EDT 2013
 * XML source: /users3/mriedl/workspace4/org.jobimtext/descriptors/com/ibm/sai/jobimtext/ingestion/type/JoBimIngestionTypeSystem.xml
 * @generated */
public class CorpusInfo extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CorpusInfo.class);
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
  protected CorpusInfo() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CorpusInfo(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CorpusInfo(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public CorpusInfo(JCas jcas, int begin, int end) {
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
  //* Feature: numFiles

  /** getter for numFiles - gets Number of files in the corpus.
   * @generated */
  public int getNumFiles() {
    if (CorpusInfo_Type.featOkTst && ((CorpusInfo_Type)jcasType).casFeat_numFiles == null)
      jcasType.jcas.throwFeatMissing("numFiles", "org.jobimtext.ingestion.type.CorpusInfo");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CorpusInfo_Type)jcasType).casFeatCode_numFiles);}
    
  /** setter for numFiles - sets Number of files in the corpus. 
   * @generated */
  public void setNumFiles(int v) {
    if (CorpusInfo_Type.featOkTst && ((CorpusInfo_Type)jcasType).casFeat_numFiles == null)
      jcasType.jcas.throwFeatMissing("numFiles", "org.jobimtext.ingestion.type.CorpusInfo");
    jcasType.ll_cas.ll_setIntValue(addr, ((CorpusInfo_Type)jcasType).casFeatCode_numFiles, v);}    
  }

    