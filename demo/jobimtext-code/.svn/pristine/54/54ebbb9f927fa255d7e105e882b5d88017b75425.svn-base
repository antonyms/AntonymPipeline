package org.jobimtext.api.struct;

/*******************************************************************************
 * Copyright 2012 Technische Universitaet Darmstadt
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
public class Order2 {

	public Order2() {
	}
	
	public Order2(String key, Double score) {
		super();
		this.key = key;
		this.score = score;
	}
	public Order2(String key, Double score, Double contextScore) {
		super();
		this.key = key;
		this.score = score;
		this.contextScore=contextScore;
	}

	public Order2(Order2 c) {
		super();
		this.key = c.key;
		this.score = c.score;
		this.contextScore=c.contextScore;
	}

	public String key;
	public Double score;
	public Double contextScore;

	@Override
	public String toString() {
		return key + "#" + score+ "#" + contextScore;
	}
	@Override
	public boolean equals(Object o) {
		
		return ((Order2)o).key.equals(key);
		
	}
}
