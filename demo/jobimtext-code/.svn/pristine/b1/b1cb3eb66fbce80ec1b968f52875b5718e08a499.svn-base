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
package org.jobimtext.holing.output;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPOutputStream;


import org.apache.uima.UimaContext;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.jobimtext.holing.type.JoBim;

/**
 * This class outputs the content of the holing system to the OS file system.
 */
public class HolingOutputIndexedFS extends HolingOutputIndexed {
	
  private Map<String, WriterRecord> writerMap = 
      new HashMap<String, WriterRecord>();

  private int countThresh = 1073741824; //bytes = 1 GB
  //private int countThresh = 10485760; //bytes = 1 MB
  //private int countThresh = 102400; //bytes = 100 KB
  
	private static AtomicLong uid = new AtomicLong(0L);

	private boolean zip = true;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		// make sure output directory exists
		File outputDir = new File(outputPath);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
	}
	
	private void initFileForModel(String model, int part) throws IOException {
		long id = uid.incrementAndGet();
		String path = outputPath + "/" + model + "_id_" + id + "_part_"+part;
		Writer w;
		if (!zip) {
			path = path + ".txt";
			w = new BufferedWriter(new FileWriter(new File(path), true));
		} else {
			path = path + ".gz";
			w = new OutputStreamWriter(new GZIPOutputStream(
					new BufferedOutputStream(new FileOutputStream(
							new File(path), true))));
		}
		writerMap.put(model, new WriterRecord(w, part, 0));
	}
	
  private void closeWriter(Writer w) {
    try {
      w.flush(); w.close(); 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

	@Override
	public void destroy() {
		super.destroy();
		for (WriterRecord rec : writerMap.values()) {
		  closeWriter(rec.writer);
		}
	}

	@Override
	protected void writeJoBimString(String filename, String unitId, JoBim jobim) {
		if (unitId.length() > 0) {
			try {
				if (!writerMap.containsKey(filename)) {
					initFileForModel(filename, 0);
				}
				String sep = extractor.getConfiguration()
						.getKeyValuesDelimiter();
				String output = extractor.extractKey(jobim) + sep
						+ extractor.extractValues(jobim);
				output += sep + unitId;
				output += sep + getIndex(jobim.getKey());
				output+=sep;
				for(int i=0;i<jobim.getValues().size();i++){
					output+=getIndex(jobim.getValues(i))+";";
				}
				//System.out.println(output);
        WriterRecord rec = writerMap.get(filename);
        rec.count += output.getBytes().length;
        
        if (rec.count > countThresh) {            
          closeWriter(rec.writer);
          rec.part = rec.part+1;
          initFileForModel(filename, rec.part);
        }
        rec = writerMap.get(filename);
        
				rec.writer.write(output+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getIndex(Annotation a){
		return a.getBegin() + ":"
				+ a.getEnd();
	}
	
  private class WriterRecord {
    private Writer writer;
    private int count;
    private int part;
    private WriterRecord(Writer writer, int part, int count) {
      this.writer = writer;
      this.part = part;
      this.count = count;
    }
  }

  @Override
  protected void closeWriter(String filename) {
    WriterRecord rec = writerMap.get(filename);
    closeWriter(rec.writer);
    writerMap.remove(filename);
  }
}
