/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 

* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package org.jobimtext.ingestion;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.jobimtext.ingestion.type.DocumentInfo;
import org.apache.uima.fit.util.JCasUtil;


public class SimpleDocumentSplitter extends JoBimDocumentSplitter {
	
	private List<String> documents = new ArrayList<String>();
	private int index = 0;
	
	@Override
	public boolean hasNext() {
		return index < documents.size();
	}

	@Override
	protected byte[] getNextDocument() throws Exception {
		return documents.get(index++).getBytes();
	}

	@Override
	protected void readFile() throws Exception {		
		String line; int count = 0;
		StringBuffer docBuffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		while ((line = br.readLine()) != null) {
		  docBuffer.append(line+' ');
		}
    br.close();
    documents.add(docBuffer.toString());
		count++;

		logger.log(Level.INFO, "Read "+count+" documents");
	}

  @Override
  protected void setDocumentText(JCas aJCas) {
    DocumentInfo docInfo = JCasUtil.selectSingle(aJCas, DocumentInfo.class);
    byte[] serializedDocument = docInfo.getSerializedDocument().toArray();
    aJCas.setDocumentText(new String(serializedDocument));
  }

  @Override
  protected void setDocumentLanguage(JCas aJCas) {
    aJCas.setDocumentLanguage("en");
  }

	
}
