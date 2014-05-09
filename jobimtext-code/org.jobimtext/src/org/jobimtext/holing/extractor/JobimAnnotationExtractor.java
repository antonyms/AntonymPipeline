/*******************************************************************************
* Copyright 2012
* FG Language Technologie
* Technische Universitaet Darmstadt
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
package org.jobimtext.holing.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.uima.jcas.tcas.Annotation;
import org.jobimtext.holing.type.JoBim;

public abstract class JobimAnnotationExtractor  implements JobimExtractor<JoBim, Annotation> {
  
  public JobimExtractorConfiguration conf;
  
  public JobimExtractorConfiguration getConfiguration() {
    return conf;
  }

  public JobimAnnotationExtractor(JobimExtractorConfiguration configuration) {
    this.conf = configuration;
  }

  @Override
  public abstract String extract(Annotation val);

  @Override
  public String extractKey(JoBim jb) {
    return extract(jb.getKey());
  }

  @Override
  public String extractValues(JoBim jb) {
    String value = "";
    for (int i = 0; i < jb.getValues().size(); i++) {
      if (conf.holeSymbol != null && jb.getHole() == i) {
        value += conf.holeSymbol + conf.valueDelimiter;
      }
      value += extract(jb.getValues(i)) + conf.valueDelimiter;
    }
    if (jb.getValues().size() > 0) {
      value = value.substring(0, value.length() - conf.valueDelimiter.length());
    }
    if (conf.holeSymbol != null && jb.getValues().size() == jb.getHole()) {
      value += conf.valueDelimiter + conf.holeSymbol;

    }
  
    String valueString = conf.valueRelationPattern.replace("$relation", jb.getRelation());
    return valueString.replace("$values", value);
  }
  
  public String extractRelationName(String bim){
	  String pattern = conf.valueRelationPattern.replace("$relation", "\\E(.*)\\Q");
	  pattern = "\\Q"+pattern+"\\E";
	  pattern = pattern.replace("$values", "\\E.*\\Q");
	  Pattern p = Pattern.compile(pattern);
	  Matcher m = p.matcher(bim);
	  if(m.find()){
		  return m.group(1);
	  }
	  return null;
	  		
  }

}
