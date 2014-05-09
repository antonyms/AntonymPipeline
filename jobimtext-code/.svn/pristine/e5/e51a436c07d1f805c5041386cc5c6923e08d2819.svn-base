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

package com.ibm.bluej.consistency.proposal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.ibm.bluej.consistency.CRFState;
import com.ibm.bluej.consistency.IndexEntry;
import com.ibm.bluej.consistency.ProposalFunction;
import com.ibm.bluej.consistency.SGSearch;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.Binds;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.StringTerm;
import com.ibm.bluej.consistency.validate.SGDebug;
import com.ibm.bluej.util.common.Lang;

public class Proposal {
	public static int RANDOM_TYPE = 0;
	public static int FORALL_TYPE = 1;
	public static int WEIGHTEDMAX_TYPE = 2;
	
	public static int NUM_RANDOM_RETRIES = 10;
	public static double SMOOTH_RATIO = 0.1; //to make weightedMax less greedy
	

	//PROPOSE> proposalFunctionPropose(pf, min, max, tries, sampleSize?) RANDOM: isRule(t1, t2, pt, pf)
	public static final StringTerm PROPOSAL_FUNCTION_PROPOSE = new StringTerm("proposalFunctionPropose");
	
	//TODO: a proposal can be training, or inference (MAP or marginal)
	
	public Proposal(ScanTerm[] toPropose, boolean[] createPositive, 
			ProposalCondition[] conditions, boolean[] condPositive, 
			int conditionType, CRFState crfState) {
		this.toPropose = toPropose;
		this.createPositive = createPositive;
		this.conditions = conditions;
		this.condPositive = condPositive;
		this.conditionType = conditionType;
		failedAt = new long[conditions.length];
		this.crfState = crfState;
	}
	
	CRFState crfState;
	int acceptedCount;
	double improvement;
	private long timeTaken; //in microseconds, but does not count time taken to rollback
	private long[] failedAt;
	private int conditionType;
	private ScanTerm[] toPropose;
	private boolean[] createPositive;
	private ProposalCondition[] conditions;
	private boolean[] condPositive;
	
	private Proposal subProposal;
	public void setSubProposal(Proposal sub) {
		subProposal = sub;
	}
	
	private ArrayList<Object> changed = new ArrayList<Object>();
	private ArrayList<Object> prevValue = new ArrayList<Object>();
	
	private void addChange(Object obj, Object prev) {
		changed.add(obj);
		prevValue.add(prev);
	}
	
	private double create(Binds binds, boolean doNotChange) {
		changed.clear();
		prevValue.clear();
		//keep a change list if the doNotChange flag is set
		//  it will then unwind the changes
		if (doNotChange) {
			crfState.transaction.setEnabled(false);
		}
		if (SGDebug.PROPOSAL) {
			System.out.println(this.toString()); 
			System.out.println(binds.toString());
		}
		for (int i = 0; i < toPropose.length; ++i) {
			if (toPropose[i].parts[0].equals(PROPOSAL_FUNCTION_PROPOSE)) {
				ATerm[] pfp = toPropose[i].parts;
				ProposalFunction pf = (ProposalFunction)pfp[1].ground(binds, null);
				double min = ATerm.getDouble(pfp[2].ground(binds,null));
				double max = ATerm.getDouble(pfp[3].ground(binds,null));
				int tries = (int)ATerm.getDouble(pfp[4].ground(binds,null));
				
				if (doNotChange) {
					addChange(pf, ATerm.getDouble(pf));
				} else {
					crfState.newValue(pf, Double.NaN); //save the current value in transaction
				}
				if (pfp.length > 5) {
					pf.scanMAP(crfState, SGSearch.rand, min, max, tries, (int)ATerm.getDouble(pfp[5].ground(binds,null)));
				} else {
					pf.scanMAP(crfState, SGSearch.rand, min, max, tries);
				}
				
			} else {
				ScanTerm gp = (ScanTerm)toPropose[i].ground(binds, null);
				//check that it is now ground
				if (SGDebug.PROPOSAL && !gp.isGround(null)) 
					throw new Error("Tried to assert a non-ground term: "+gp);
				if (createPositive[i]) {
					crfState.nowTrue(gp);
					if (SGDebug.PROPOSAL) System.out.println("Pro> "+gp); 
					if (doNotChange) addChange(gp, Boolean.FALSE);
				} else {
					crfState.nowFalse(gp);
					if (SGDebug.PROPOSAL) System.out.println("Pro> !"+gp);
					if (doNotChange) addChange(gp, Boolean.TRUE);
				}
			}
		}
		double weight = crfState.getWeight();
		if (doNotChange) {
			//undo the changes
			for (int i = changed.size()-1; i >= 0; --i) {
				if (changed.get(i) instanceof ScanTerm) {
					ScanTerm term = (ScanTerm)changed.get(i);
					if ((Boolean)prevValue.get(i)) {
						crfState.nowTrue(term);
					} else {
						crfState.nowFalse(term);
					}
				} else if (changed.get(i) instanceof ProposalFunction) {
					((ProposalFunction)changed.get(i)).setValue((Double)prevValue.get(i));
				} else {
					throw new Error("Unknown type in changed: "+changed.get(i));
				}
			}
			crfState.transaction.setEnabled(true);
		}
		//System.out.println("Created with binds "+binds);
		//System.out.println("  and weight = "+weight);
		return weight;
	}
	
	private boolean tryOnceCondition(Binds binds) {
		try {
			for (int i = 0; i < conditions.length; ++i) {
				if (conditions[i].expandRandom(binds, crfState) != condPositive[i]) {
					++failedAt[i];
					return false;
				}
			}
			return true;
		} catch (RuntimeException e) {
			System.err.println(this);
			System.err.println(binds);
			throw e;
		}
	}
	
	private void getAllPossible(Binds binds, int conditionNumber, Collection<Binds> possible) {
		if (conditionNumber >= conditions.length) {
			possible.add(binds.copy());
			return;
		}
		Iterator<Binds> expand = conditions[conditionNumber].expandAll(binds, crfState);
		
		if (!condPositive[conditionNumber]) {
			if (expand != null) return;
			getAllPossible(binds, conditionNumber+1, possible);
		} else {
			if (expand == null) return;
			while (expand.hasNext()) {
				Binds exp = expand.next();
				Object cp = binds.checkpoint();
				binds.expand(exp);
				getAllPossible(binds, conditionNumber+1, possible);
				binds.rollback(cp);
			}
		}
	}
	
	private boolean proposeInternal(Binds binds) {
		boolean proposed = false;
		if (conditionType == RANDOM_TYPE) {
			for (int ti = 0; ti < NUM_RANDOM_RETRIES; ++ti) {
				binds.quickCheckPoint();
				if (tryOnceCondition(binds)) {
					try {
						create(binds, false); 
					} catch (Error e) {
						e.printStackTrace();
						System.err.println("Truth table:");
						Iterator<IndexEntry> it = crfState.posGrnd.getGroundTerms();
						while (it.hasNext()) {
							System.err.println(it.next().term);
						}
					}
					proposed = true;
					break;
				}
				binds.quickRollback();
			}	
		} else if (conditionType == WEIGHTEDMAX_TYPE) {
			
			//get all possible binds
			ArrayList<Binds> options = new ArrayList<Binds>();
			ArrayList<Double> oddsRatios = new ArrayList<Double>();
			getAllPossible(binds, 0, options);
			if (options.size() > 0) {
				//find oddsRatio of each option
				double miniZ = 0;
				double baseW = crfState.getWeight();
				for (Binds b : options) {
					double r = Math.exp(create(b, true) - baseW);
					oddsRatios.add(r);
					miniZ += r;
				}
				//smooth the weightedmax to ensure "effective ergodicity"
				double smoothAdd = SMOOTH_RATIO * miniZ/options.size();
				miniZ += smoothAdd * options.size();
				//this is weightedmax
				int selected = options.size() - 1;
				double selectOption = Math.random();
				double cumW = 0;
				for (int i = 0; i < options.size(); ++i) {
					cumW += (oddsRatios.get(i)+smoothAdd)/miniZ;
					if (selectOption <= cumW) {
						selected = i;
						break;
					}
				}
				create(options.get(selected), false);
				proposed = true;
			}
		} else {
			//get all possible binds
			ArrayList<Binds> options = new ArrayList<Binds>();
			getAllPossible(binds, 0, options);
			for (Binds o : options) {
				create(o, false);
			}
			proposed = true;
		}
		if (proposed && subProposal != null) {
			return subProposal.proposeInternal(binds);
		}
		return proposed;
	}
	
	int debugCount = 10;
	
	boolean propose() {	
		boolean madeProposal = false;
		long time = System.nanoTime();
		if (SGDebug.PROPOSAL && debugCount > 0 && Math.random() < 0.001) {
			--debugCount;
			Binds b = new Binds(crfState.getQuicksave());
			System.out.println("Proposing "+this.toString());
			System.out.println("   "+crfState.getWorldWeight().toString().replace('\n', ' '));
			madeProposal = proposeInternal(b);
			if (madeProposal) {
				System.out.println("Proposed");
				System.out.println("   "+b+" "+crfState.toString().replace('\n', ' '));
			} else {
				System.out.println("Could not propose");
			}
			//Lang.readln();
			
		} else {
			madeProposal = proposeInternal(new Binds(crfState.getQuicksave()));
		}
		timeTaken += (System.nanoTime() - time)/1000;
		return madeProposal;
	}
	
	//TODO: add time and acceptCount
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("PROPOSE> ");
		for (int i = 0; i < toPropose.length; ++i) {
			if (i != 0) {
				buf.append(" ^ ");
			}
			if (!createPositive[i]) {
				buf.append('!');
			}
			buf.append(toPropose[i]);
		}
		if (conditionType == RANDOM_TYPE) {
			buf.append(" RANDOM: ");
		} else if (conditionType == WEIGHTEDMAX_TYPE) {
			buf.append(" WEIGHTEDMAX: ");
		} else {
			buf.append(" FORALL: ");
		}
		for (int i = 0; i < conditions.length; ++i) {
			if (i != 0) {
				buf.append(" ^ ");
			}
			if (!condPositive[i]) {
				buf.append('!');
			}
			buf.append(conditions[i]);
			buf.append("<").append(failedAt[i]).append(">");
		}
		if (subProposal != null) {
			buf.append(", SUB").append(subProposal.toString());
		}
		buf.append(" "+Lang.dblStr(timeTaken/1000000.0)+" seconds, accepted "+this.acceptedCount+" improvement "+this.improvement);
		return buf.toString();
	}
}
