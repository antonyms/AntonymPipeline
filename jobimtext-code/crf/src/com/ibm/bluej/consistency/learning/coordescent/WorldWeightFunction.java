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

package com.ibm.bluej.consistency.learning.coordescent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.learning.WorldWeightParam;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.util.common.IdentitySet;
import com.ibm.bluej.util.common.MutableDouble;


//NOTE: this doesn't support incremental recomputing with ProposalFunctions
//  you would need a usedBy list - basically need to implement Updatable
public abstract class WorldWeightFunction implements Comparable<WorldWeightFunction>, Serializable {
	private static final long serialVersionUID = 1L;
	private WorldWeightParam weight;
	private double value = Double.NaN;
	//allowed args are WorldWeightFunction, ParamWeight, Term that can Term.getDouble(), Double, MutableDouble
	private Object[] args;
	
	//constructor that takes the args
	public WorldWeightFunction(Object... args) {
		this.args = args;
	}
	
	
	//TODO: get list of all ParamWeights in formula
	//  boolean isWeightComputable Set<ParamWeight>
	//  shuffle list, add ParamWeights until isWeightComputable is true
	//  shuffle list again, remove ParamWeights as long as isWeightComputable is still true
	//three possibilities, uncomputable=-1, computable=1, fixed=0
	private int isWeightComputable(Set<ParamWeight> fixed) {
		int unfixed = 0;
		for (Object arg : args) {
			if (arg instanceof WorldWeightFunction) {
				int subR = ((WorldWeightFunction)arg).isWeightComputable(fixed);
				if (subR == -1) {
					return -1;
				}
				unfixed += subR;
			} else if (arg instanceof ParamWeight && !fixed.contains(arg)) {
				++unfixed;
			}
		}
		if (!multipleUnfixedAllowed() && unfixed > 1) {
			return -1;
		}
		if (unfixed == 0) {
			return 0;
		}
		return 1;
	}
	
	private void gatherAllOtherParamWeight(Set<ParamWeight> existing, Set<ParamWeight> other) {
		for (Object arg : args) {
			if (arg instanceof WorldWeightFunction) {
				((WorldWeightFunction)arg).gatherAllOtherParamWeight(existing, other);

			} else if (arg instanceof ParamWeight && !existing.contains(arg)) {
				other.add((ParamWeight)arg);
			}
		}
	}
	
	//given a set of fixed add everything else that is needed to make it computable (breaking ties randomly)
	public void completeFixed(Set<ParamWeight> fixed) {
		Set<ParamWeight> otherSet = new IdentitySet<ParamWeight>();
		gatherAllOtherParamWeight(fixed, otherSet);
		ArrayList<ParamWeight> others = new ArrayList<ParamWeight>(otherSet);
		Collections.shuffle(others);
		for (ParamWeight toFix : others) {
			if (isWeightComputable(fixed) != -1) {
				break;
			}
			fixed.add(toFix);
		}
		Collections.shuffle(others);
		for (ParamWeight toUnFix : others) {
			if (fixed.remove(toUnFix) && isWeightComputable(fixed) == -1) {
				fixed.add(toUnFix);
			}
		}
		
	}
	
	protected abstract WorldWeightParam recompute(Set<ParamWeight> fixed);
	protected abstract boolean multipleUnfixedAllowed();
	protected abstract double recomputeValue();
	

	protected String argsToString(String sep) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < args.length; ++i) {
			if (i != 0) buf.append(sep);
			buf.append(args[i]);
		}
		return buf.toString();
	}
	
	protected int getNumArgs() {
		return args.length;
	}
	
	public double getArgValue(int i) {
		if (args[i] instanceof ParamWeight) {
			return ((ParamWeight)args[i]).getWeight();
		}
		if (args[i] instanceof WorldWeightFunction) {
			return ((WorldWeightFunction)args[i]).getValue();
		}
		if (args[i] instanceof ATerm) {
			return ATerm.getDouble((ATerm)args[i]);
		}
		if (args[i] instanceof Double) {
			return (Double)args[i];
		}
		if (args[i] instanceof MutableDouble) {
			return ((MutableDouble)args[i]).value;
		}
		throw new IllegalArgumentException("Unknown argument type "+args[i].getClass());		
	}
	
	protected WorldWeightParam getArgWeight(int i, Set<ParamWeight> fixed) {
		if (args[i] instanceof WorldWeightFunction) {
			return ((WorldWeightFunction)args[i]).getWeight(fixed);
		}
		WorldWeightParam ww = new WorldWeightParam();
		if (args[i] instanceof ParamWeight) {
			if (fixed != null && !fixed.contains(args[i])) {
				ww.update((ParamWeight)args[i], 1.0);
			}
			ww.update(((ParamWeight)args[i]).getWeight());
		} else if (args[i] instanceof ATerm) {
			ww.update(ATerm.getDouble((ATerm)args[i]));
		} else if (args[i] instanceof Double) {
			ww.update((Double)args[i]);
		} else if (args[i] instanceof MutableDouble) {
			ww.update(((MutableDouble)args[i]).value);
		} else {
			throw new IllegalArgumentException("Unknown argument type "+args[i].getClass());
		}
		return ww;
	}
	
	protected boolean isConst(Set<ParamWeight> fixed) {
		for (Object arg : args) {
			if (arg instanceof WorldWeightFunction && !((WorldWeightFunction)arg).isConst(fixed)) {
				return false;
			} else if (arg instanceof ParamWeight && (fixed == null || !fixed.contains(arg))) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isConst(int i, Set<ParamWeight> fixed) {

		if (args[i] instanceof WorldWeightFunction && !((WorldWeightFunction)args[i]).isConst(fixed)) {
			return false;
		} else if (args[i] instanceof ParamWeight && (fixed == null || !fixed.contains(args[i]))) {
			return false;
		}
		
		return true;
	}
	
	public double getValue() {
		if (Double.isNaN(value)) {
			value = recomputeValue();
		}
		return value;
	}
	
	/**
	 * least to greatest ordering
	 */
	public int compareTo(WorldWeightFunction wwf) {
		return (int)Math.signum(this.getValue() - wwf.getValue());
	}
	
	public WorldWeightParam getWeight(Set<ParamWeight> fixed) {
		if (weight == null) {
			weight = recompute(fixed);
		}
		return weight;
	}
	
	public void clear() {
		value = Double.NaN;
		weight = null;
		for (Object o : args) {
			if (o instanceof WorldWeightFunction) {
				((WorldWeightFunction)o).clear();
			}
		}
	}

}
