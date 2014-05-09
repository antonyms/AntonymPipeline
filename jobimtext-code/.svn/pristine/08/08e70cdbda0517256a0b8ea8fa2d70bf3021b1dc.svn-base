

/* First created by JCasGen Mon Sep 16 13:25:14 EDT 2013 */
package org.jobimtext.ingestion.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.ByteArray;
import org.apache.uima.jcas.tcas.Annotation;


/** Information for passing information about documents
				between JoBim ingestion pieces.
 * Updated by JCasGen Mon Sep 16 13:25:14 EDT 2013
 * XML source: /users3/mriedl/workspace4/org.jobimtext/descriptors/com/ibm/sai/jobimtext/ingestion/type/JoBimIngestionTypeSystem.xml
 * @generated */
public class DocumentInfo extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DocumentInfo.class);
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
  protected DocumentInfo() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DocumentInfo(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DocumentInfo(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DocumentInfo(JCas jcas, int begin, int end) {
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
  //* Feature: inputFile

  /** getter for inputFile - gets file containing document
   * @generated */
  public String getInputFile() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_inputFile == null)
      jcasType.jcas.throwFeatMissing("inputFile", "org.jobimtext.ingestion.type.DocumentInfo");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_inputFile);}
    
  /** setter for inputFile - sets file containing document 
   * @generated */
  public void setInputFile(String v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_inputFile == null)
      jcasType.jcas.throwFeatMissing("inputFile", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_inputFile, v);}    
   
    
  //*--------------*
  //* Feature: threadId

  /** getter for threadId - gets Id of the thread for this file.
   * @generated */
  public int getThreadId() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_threadId == null)
      jcasType.jcas.throwFeatMissing("threadId", "org.jobimtext.ingestion.type.DocumentInfo");
    return jcasType.ll_cas.ll_getIntValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_threadId);}
    
  /** setter for threadId - sets Id of the thread for this file. 
   * @generated */
  public void setThreadId(int v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_threadId == null)
      jcasType.jcas.throwFeatMissing("threadId", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setIntValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_threadId, v);}    
   
    
  //*--------------*
  //* Feature: error

  /** getter for error - gets Whether or not there is an error with the document.
   * @generated */
  public boolean getError() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_error == null)
      jcasType.jcas.throwFeatMissing("error", "org.jobimtext.ingestion.type.DocumentInfo");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_error);}
    
  /** setter for error - sets Whether or not there is an error with the document. 
   * @generated */
  public void setError(boolean v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_error == null)
      jcasType.jcas.throwFeatMissing("error", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_error, v);}    
   
    
  //*--------------*
  //* Feature: errorMessage

  /** getter for errorMessage - gets Message associated with the error.
   * @generated */
  public String getErrorMessage() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_errorMessage == null)
      jcasType.jcas.throwFeatMissing("errorMessage", "org.jobimtext.ingestion.type.DocumentInfo");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_errorMessage);}
    
  /** setter for errorMessage - sets Message associated with the error. 
   * @generated */
  public void setErrorMessage(String v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_errorMessage == null)
      jcasType.jcas.throwFeatMissing("errorMessage", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_errorMessage, v);}    
   
    
  //*--------------*
  //* Feature: serializedDocument

  /** getter for serializedDocument - gets Serialized document byte array.
   * @generated */
  public ByteArray getSerializedDocument() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_serializedDocument == null)
      jcasType.jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    return (ByteArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument)));}
    
  /** setter for serializedDocument - sets Serialized document byte array. 
   * @generated */
  public void setSerializedDocument(ByteArray v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_serializedDocument == null)
      jcasType.jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for serializedDocument - gets an indexed value - Serialized document byte array.
   * @generated */
  public byte getSerializedDocument(int i) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_serializedDocument == null)
      jcasType.jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument), i);
    return jcasType.ll_cas.ll_getByteArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument), i);}

  /** indexed setter for serializedDocument - sets an indexed value - Serialized document byte array.
   * @generated */
  public void setSerializedDocument(int i, byte v) { 
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_serializedDocument == null)
      jcasType.jcas.throwFeatMissing("serializedDocument", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument), i);
    jcasType.ll_cas.ll_setByteArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_serializedDocument), i, v);}
   
    
  //*--------------*
  //* Feature: last

  /** getter for last - gets If this document represents the last document in a
						file.
   * @generated */
  public boolean getLast() {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_last == null)
      jcasType.jcas.throwFeatMissing("last", "org.jobimtext.ingestion.type.DocumentInfo");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_last);}
    
  /** setter for last - sets If this document represents the last document in a
						file. 
   * @generated */
  public void setLast(boolean v) {
    if (DocumentInfo_Type.featOkTst && ((DocumentInfo_Type)jcasType).casFeat_last == null)
      jcasType.jcas.throwFeatMissing("last", "org.jobimtext.ingestion.type.DocumentInfo");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((DocumentInfo_Type)jcasType).casFeatCode_last, v);}    
  }

    