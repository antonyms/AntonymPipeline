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

package com.ibm.bluej.consistency.validate;

public class SGDebug {
	public static final boolean INTERACTIVE_CONTROL = false;
	public static final boolean PROPOSAL = false;
	public static final boolean BURN_IN = false;
	
	public static final boolean TRAIN = false;
	public static final boolean END_DELTA = false;
	public static final boolean FORMULA_FIND_GROUND = false;
	
	public static final boolean INDEXING = false;
	public static final boolean BASIC = false;
	public static final boolean CANON_TOSTRING = false; //orders elements in sets/bags so they have canonical string representation
	public static final boolean FUNC_NOLEAK = false;
	public static final boolean DEFINE_DEBUG = false;
	
	public static final boolean MAP_INFERENCE = false;
	
	public static final boolean SAMPLE_RANK = false;
	public static final boolean GROUND_OUT = false; //trace Formula.groundOut function
	public static final boolean TRACK_TRUTH = false; //dump truth table after every now{True|False}Internal
	public static final boolean TRACK_BEST = false; //dump truth table and weight table after a new best is found
	public static final boolean LOGGING = false; //guard log statements with static final to reduce costly toString
	
	public static final boolean ALREADY_TRUTH_IS_ERROR = false;
	/**/
}
