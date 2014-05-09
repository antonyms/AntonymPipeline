/*
Copyright (c) 2012 IBM Corp. and Michael Glass

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

package com.ibm.bluej.consistency.learning;

public interface IDeltaWeight {
	public IWorldWeight recordWeight();
	public void updateBestWeight(IWorldWeight bestWeight);
	public void addWeight(double v);
	public void addWeight(Learnable p, double v);
	public double getWeight();
	public void beginDelta();
	public boolean endDelta(LearningState learningState, double goldScore, double prevGoldScore);
	public void clear();
	public String deltaString();
}
