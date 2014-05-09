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

import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.corefunc.SimpleNumberFunction;

public class FuncOddsScore extends SimpleNumberFunction {

	@Override
	protected double compute() {
		double pprob = FuncLogProbScore.scoreToProb(ATerm.getDouble(parts[0]) - ATerm.getDouble(parts[1]));
		if (pprob > 0.99)
			pprob = 0.99;
		if (pprob < 0.01)
			pprob = 0.01;
		return Math.log(pprob);
	}

}
