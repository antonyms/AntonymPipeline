package org.jobimtext.api.struct;
/*******************************************************************************
 * Copyright 2014 FG Language Technology
 * Technische Universitaet Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
import java.util.List;
/**
 * 
 * @author riedl
 *
 */
		
public class Sense {
	String cui;
	List<String> senses;
	List<String> isas;
	
	
	public String getCui() {
		return cui;
	}
	public void setCui(String cui) {
		this.cui = cui;
	}
	public List<String> getSenses() {
		return senses;
	}
	public void setSenses(List<String> senses) {
		this.senses = senses;
	}
	public List<String> getIsas() {
		return isas;
	}
	public void setIsas(List<String> isas) {
		this.isas = isas;
	}
	@Override
	public String toString() {
		return cui+": "+getSenses()+"\t"+getIsas();
	}
	
}
