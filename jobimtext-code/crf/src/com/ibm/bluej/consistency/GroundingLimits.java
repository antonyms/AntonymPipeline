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

package com.ibm.bluej.consistency;


public final class GroundingLimits {
	//CONSIDER: move to FormulaFindGroundAdaptive?
	
	//when you hit CHECK_GROUND_OUT_COUNT the first time, put a time on groundOutTime, 
	//then every CHECK_GROUND_OUT_COUNT after check that you haven't exceeded MAX_GROUND_OUT_TIME
	
	private static final int CHECK_GROUND_OUT_COUNT = 1000;
	public long MAX_GROUND_OUT_TIME = 10000; //ten seconds is the default max
	private int groundOutCount = 0;
	private long groundOutTime = 0;
	final void clearGroundOutCount() {
		groundOutCount = 0;
	}
	//only called by Formula
	public final void zzCheckGroundOutCount() {
		++groundOutCount;
		if (groundOutCount % CHECK_GROUND_OUT_COUNT == 0) {
			if (groundOutCount == CHECK_GROUND_OUT_COUNT) {
				groundOutTime = System.currentTimeMillis();
			} else {
				if (System.currentTimeMillis() - groundOutTime > MAX_GROUND_OUT_TIME) {
					throw new SGSearch.ExcessiveGroundOut("Too many calls to groundOut: "+groundOutCount+
							" taking "+(System.currentTimeMillis() - groundOutTime)/1000.0+" secs. Max is "+MAX_GROUND_OUT_TIME/1000.0);
				}
			}
		}
	}

}
