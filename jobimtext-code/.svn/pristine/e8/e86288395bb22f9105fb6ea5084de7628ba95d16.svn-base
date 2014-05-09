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

package com.ibm.bluej.consistency.optimize;

public class L1Regularizer implements Regularizer {
	private double L1lambda;
	
	public L1Regularizer(double L1lambda) {
		this.L1lambda = L1lambda;
	}
	
	/**
	 * 
	 * @param oldParamValue
	 * @param newParamValue
	 * @return the value that should be ADDED to the weight as a result of the parameter change
	 */
	public double regularizeDelta(double oldParamValue, double newParamValue) {
		return L1lambda*(Math.abs(oldParamValue) - Math.abs(newParamValue));
	}
}
