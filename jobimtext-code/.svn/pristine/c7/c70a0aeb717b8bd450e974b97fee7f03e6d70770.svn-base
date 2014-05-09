/*******************************************************************************
* Copyright 2013
* Copyright (c) 2013 IBM Corp.
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

package com.ibm.sai.distributional_frame_semantics.utility;

import java.util.ArrayList;

/**
 * This customized list allow to store case sensitive strings,
 * while at the same allowing finding elements regardless of their case.
 * 
 * @author mchowdh
 *
 */
public class CaseInsensitiveStringList extends ArrayList<String> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3645251370056311466L;

	@Override
    public boolean contains(Object o) {
        String paramStr = (String)o;
        for (String s : this) {
            if (paramStr.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
	
	@Override
    public int indexOf(Object o) {
        String paramStr = (String)o;
        int x=0;
        for (String s : this) {
            if (paramStr.equalsIgnoreCase(s)) 
            	return x;
            x++;
        }
        
        return -1;
    }
}