/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.ibm.bluej.consistency.interactive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


public class SGIResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	static class Row implements Serializable {
		private static final long serialVersionUID = 1L;
		String[] parts;
		String[] attributes;
	}
	
	public SGIResponse(String type) {
		this.type = type;
	}
	
	String type;
	Collection<Row> rows = new ArrayList<Row>();
	//itemId to name/linkId
	//Map<Integer, Pair<String,Integer>> links = new HashMap<Integer, Pair<String,Integer>>();
	//private transient MutableInteger itemIdGen = new MutableInteger(0);
	
	void addSimpleRow(String... cols) {
		Row r = new Row();
		r.parts = cols;
		rows.add(r);
	}
	
}
