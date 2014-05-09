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
package org.jobimtext.hadoop;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.pig.PigServer;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PigJob implements JobimJob {
  
  private String path;
  private Map<String, String>params;
  private String name;
  public PigJob(){
	  
  }
  public PigJob(String name, String path, Map<String, String>params) {
    this.path = path;
    this.params = params;
    this.name = name;
  }
  
  public String getName()  {
    return name;
  }

  public void run() throws Exception {
    PigServer pig = new PigServer("mapreduce");
    pig.registerScript(path, params);
    pig.shutdown();
  }  

}
