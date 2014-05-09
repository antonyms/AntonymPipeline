package org.jobimtext.api.struct;

/*******************************************************************************
 * Copyright 2012 FG Language Technology
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
public class Order1 {

	public Order1() {
	}

	public Order1(String key, Double score) {
		super();
		this.key = key;
		this.score = score;
	}

	public Order1(String key, Double score,Long count) {
		super();
		this.key = key;
		this.score = score;
		this.count = count;
	}
	
	public String key;
	public Double score;
	public Long count;

	@Override
	public String toString() {
		return key + "#" + score+"#"+count;
	}
	@Override
	public int hashCode() {
		
		return key.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Order1){
			Order1 o1 = (Order1)obj;
			return o1.key.equals(key);
		}else{
			return key.equals(obj.toString());
		}
		
	}
}
