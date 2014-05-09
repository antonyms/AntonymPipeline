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

package com.ibm.bluej.util.common;

public class PeriodicChecker {
	double seconds;
	long lastTime;
	long firstTime;
	int checkEvery;
	int checkNumber;
	
	public PeriodicChecker(double seconds) {
		this(seconds, false);
	}
	
	public double getInterval() {
		return seconds;
	}
	
	public PeriodicChecker(double seconds, boolean alwaysCheck) {
		this.seconds = seconds;
		lastTime = System.currentTimeMillis();
		firstTime = lastTime;
		checkEvery = alwaysCheck ? 0 : 1;
		checkNumber = 0;
	}
	
	/**
	 * 
	 * @return time elapsed since construction in milliseconds
	 */
	public long elapsedTime() {
		return System.currentTimeMillis() - firstTime;
	}
	
	/**
	 * Set the last time to the current time.
	 */
	public void reset() {
		lastTime = System.currentTimeMillis();
	}
	
	/**
	 * Checks if the time specified in the constructor has elapsed, resets clock if it has and returns true
	 * @return
	 */
	public boolean isTime() {
		if (checkEvery > 0 && ++checkNumber % checkEvery != 0) {
			return false;
		}
		long time = System.currentTimeMillis();
		double elapsed = (time - lastTime)/1000.0;
		if (elapsed < seconds/20) {
			checkEvery *= 2;
		}
		if (elapsed < seconds) {
			return false;
		}
		if (elapsed > seconds * 1.5) {
			checkEvery /= 4;
			if (checkEvery == 0) checkEvery = 1;
		}
		lastTime = time;
		checkNumber = 0;
		return true;
	}
}
