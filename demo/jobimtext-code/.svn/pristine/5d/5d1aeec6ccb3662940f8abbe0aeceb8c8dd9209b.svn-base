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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

import com.ibm.bluej.consistency.formula.Creator;
import com.ibm.bluej.consistency.formula.FactorCreator;
import com.ibm.bluej.consistency.formula.FactorFunctionCreator;
import com.ibm.bluej.consistency.formula.Formula;
import com.ibm.bluej.consistency.grammar.ConsistencyLexer;
import com.ibm.bluej.consistency.grammar.ConsistencyParser;
import com.ibm.bluej.consistency.grammar.ConsistencyParser.logicDesc_return;
import com.ibm.bluej.consistency.learning.BetaWeightCreator;
import com.ibm.bluej.consistency.learning.ObjectiveCreator;
import com.ibm.bluej.consistency.learning.ParamCreator;
import com.ibm.bluej.consistency.learning.ParamWeight;
import com.ibm.bluej.consistency.proposal.Proposal;
import com.ibm.bluej.consistency.proposal.ProposalCondition;
import com.ibm.bluej.consistency.proposal.ProposalConditionCheckTerm;
import com.ibm.bluej.consistency.proposal.ProposalConditionExpandTerm;
import com.ibm.bluej.consistency.proposal.ProposalConditionScan;
import com.ibm.bluej.consistency.proposal.ProposalSet;
import com.ibm.bluej.consistency.rl.GroundingPlan;
import com.ibm.bluej.consistency.term.ATerm;
import com.ibm.bluej.consistency.term.BagDefinition;
import com.ibm.bluej.consistency.term.CheckTerm;
import com.ibm.bluej.consistency.term.CompositeTerm;
import com.ibm.bluej.consistency.term.ExpandTerm;
import com.ibm.bluej.consistency.term.Function;
import com.ibm.bluej.consistency.term.NumberTerm;
import com.ibm.bluej.consistency.term.ScanTerm;
import com.ibm.bluej.consistency.term.SetDefinition;
import com.ibm.bluej.consistency.term.VarTerm;
import com.ibm.bluej.consistency.validate.ConstantCheck;
import com.ibm.bluej.consistency.validate.GroundableCheck;
import com.ibm.bluej.util.common.FileUtil;
import com.ibm.bluej.util.common.Lang;
import com.ibm.bluej.util.common.Pair;

public class ParserAntrl extends ParserBasic {
	
	//TODO: implement the functions used by other classes in SGSParser
	
	public static CRFDescription parse(CRFState crfState, String fileContents) {
		return new ParserAntrl(crfState).readString(fileContents);
	}
	
	public ParserAntrl(CRFState crfState) {
		super(crfState);
	}
	
	public static void printTree(Tree t, int indent) {
		if (t.getText() != null)
			System.out.println(Lang.indent(t.getText().toString(), indent));
		for (int ci = 0; ci < t.getChildCount(); ++ci) {
    		printTree(t.getChild(ci), indent + 2);
    	}
	}
	
	private static Tree getTree(String description) {
		String contents = (description+"\n")
				.replaceAll("//[^\n]*", "").replaceAll("[ \t]+\n", "\n").replaceAll("[ \t]+", " ").
				replaceAll("\n+", "\n").replace("( ", "(").replace(" )", ")").trim()+"\n";
		//System.out.println(contents);
		
		ConsistencyLexer lex = new ConsistencyLexer(new ANTLRStringStream(contents));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        ConsistencyParser parser = new ConsistencyParser(tokens);
        try {
        	logicDesc_return r = parser.logicDesc();
        	Tree t = (Tree)r.getTree();
        	return t;
        } catch (RecognitionException e)  {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
	}

	private Formula handleDefinitionSent(Tree defined, Tree condition) {
		ATerm st = handleCompositeTerm(defined);
		return makeFormula(new DefineCreator((ScanTerm)st), condition);
	}
	
	private Formula handleMakeConstSent(Tree defined, Tree condition) {
		ATerm st = handleCompositeTerm(defined);
		return makeFormula(new MakeConstCreator((ScanTerm)st), condition);
	}
	
	private Formula makeFormula(Creator c, Tree condition) {
		ArrayList<ATerm> condList = new ArrayList<ATerm>();
		ArrayList<Boolean> positive = new ArrayList<Boolean>();
		if (condition != null) {
			handleCondition(condition, condList, positive);
		}
		return new Formula(c, 
				condList.toArray(new ATerm[condList.size()]), 
				toBoolArray(positive),
				new GroundingPlan(condList.size()), crfState);
	}
	
	void handleCondition(Tree condition, ArrayList<ATerm> terms, ArrayList<Boolean> positive) {
		for (int i = 0; i < condition.getChildCount(); ++i) {
			Tree maybeNeg = condition.getChild(i);
			if (maybeNeg.getText().equals("!")) {
				++i;
				positive.add(Boolean.FALSE);
			} else {
				positive.add(Boolean.TRUE);
			}
			terms.add(handleCompositeTerm(condition.getChild(i)));
		}	
	}
	
	Formula handleFormula(Tree c) {
		//find what type of formula:
		//  definition, factorFunc, beta
		if (c.getText().equals("DEF>")) {
			return handleDefinitionSent(c.getChild(0), c.getChild(1));
		} else if (c.getText().equals("FACTORFUNC")) {
			return handleFactorSent(c.getChild(0), c.getChild(1));
		} else if (c.getText().equals("BETAFUNC")) {
			return handleBetaSent(c.getChild(0), c.getChild(1));
		} else if (c.getText().equals("MAKECONST>")) {
			return handleMakeConstSent(c.getChild(0), c.getChild(1));
		}
		
		throw new Error("Unknown formula type: "+c.getText());
	}
	
	boolean isObjective(Tree c) {
		return c.getText().equals("OBJECTIVE>");
	}
	Formula handleObjective(Tree c) {
		Formula f =  handleFormula(c.getChild(0));
		if (f.creator instanceof FactorCreator) {
			FactorCreator fc = (FactorCreator)f.creator;
			f.creator = new ObjectiveCreator(fc.getWeight(), null);
		} else {
			throw new Error("Unknown creator "+f.creator);
		}
		return f;
	}
	
	boolean isProposal(Tree c) {
		return c.getText().endsWith("PROPOSE>");
	}
	private Proposal handleProposal(Tree create, int type, Tree condition) {
		ArrayList<ATerm> terms = new ArrayList<ATerm>();
		ArrayList<Boolean> positive = new ArrayList<Boolean>();
		boolean[] createPositive = new boolean[0];
		ScanTerm[] toPropose = new ScanTerm[0];
		if (create != null) {
			handleCondition(create, terms, positive);
			createPositive = toBoolArray(positive);
			toPropose = new ScanTerm[terms.size()];
			for (int i = 0; i < toPropose.length; ++i) {
				toPropose[i] = (ScanTerm)terms.get(i);
			}
			terms.clear(); positive.clear();
		}
		handleCondition(condition, terms, positive);
		boolean[] condPositive = toBoolArray(positive);
		ProposalCondition[] conditions = new ProposalCondition[terms.size()];
		
		for (int i = 0; i < conditions.length; ++i) {
			ATerm c = terms.get(i);
			ProposalCondition pc = null;
			if (c instanceof ExpandTerm) {
				pc = new ProposalConditionExpandTerm((ExpandTerm)c);
			} else if (c instanceof CheckTerm) {
				pc = new ProposalConditionCheckTerm((CheckTerm)c);
			} else if (c instanceof ScanTerm) {
				pc = new ProposalConditionScan(((ScanTerm)c).parts);
			} else {
				throw new Error("Unacceptable type: "+c);
			}
			conditions[i] = pc;
		}
		
		return new Proposal(toPropose, createPositive, conditions, condPositive, type, crfState);
	}
	Pair<Proposal,Boolean> handleProposal(Tree c, Proposal lastProposalLine, Map<String, VarTerm> lastProposalVars) {
		boolean isSub = c.getText().startsWith("SUB");
		if (isSub) {
			if (lastProposalLine == null) {
				throw new Error("Cannot have a SUBPROPOSAL with out a preceeding PROPOSAL");
			}
			curVarId = lastProposalVars;
		}
		int ptype = Proposal.RANDOM_TYPE;
		String typeNode = c.getChild(c.getChildCount()-1).getText();
		if (typeNode.equals("FORALL:")) {
			ptype = Proposal.FORALL_TYPE;
		} else if (typeNode.equals("WEIGHTEDMAX:")) {
			ptype = Proposal.WEIGHTEDMAX_TYPE;
		}
		Tree create = (c.getChildCount() > 1 ? c.getChild(0) : null);
		Proposal pro = handleProposal(create, ptype, c.getChild(c.getChildCount()-1));
		
		if (isSub) {
			lastProposalLine.setSubProposal(pro);
		} else {
			lastProposalVars = curVarId;
		}

		return Pair.make(pro, isSub);
	}
	
	boolean isDeclaration(Tree c) {
		return c.getText().equals("DECLARE>");
	}
	void addDeclaration(Tree tree) {
		String funName = tree.getChild(0).getText();
		String jclassName = tree.getChild(1).getText();
		
		Class c = null;
		Class prevMapping = funNameToClass.get(funName);
		if (prevMapping != null) {
			System.out.println("WARNING: "+funName+" redefined from "+prevMapping.getName()+" to "+jclassName);
		}
		try {
			c = Class.forName(jclassName);
		} catch (Exception e) {
			System.err.println(e);
			if (SKIP_CLASSNOTFOUND) {
				return;
			}
			throw new Error("Can't find class for "+funName+": "+jclassName);
		}
		if (!Function.class.isAssignableFrom(c)) {
			throw new Error(jclassName+" is not a "+Function.class.getCanonicalName());
		}
		funNameToClass.put(funName, c);
	}
	
	CRFDescription readString(String description) {
		Tree tree = getTree(description);
		Proposal lastProposalLine = null; //used for SUBPROPOSAL>
		Map<String, VarTerm> lastProposalVars = null;
        ArrayList<Formula> formulas = new ArrayList<Formula>();
        ArrayList<Proposal> proposals = new ArrayList<Proposal>();
        ArrayList<Formula> objective = new ArrayList<Formula>();
        
        for (int ci = 0; ci < tree.getChildCount(); ++ci) {
        	super.clearVars();
        	
        	Tree c = tree.getChild(ci);
        	if (isDeclaration(c)) {
        		addDeclaration(c);
        	} else if (isProposal(c)) {
        		Pair<Proposal,Boolean> p = handleProposal(c, lastProposalLine, lastProposalVars);
        		lastProposalLine = p.first;
				lastProposalVars = new HashMap<String,VarTerm>(curVarId);
        		if (!p.second) {
        			proposals.add(p.first);
        		}   		
        	} else if (isObjective(c)) {
        		objective.add(handleObjective(c));
        	} else {
        		formulas.add(handleFormula(c));
        	}
        }
        
        clearVars(); //check var count on last line
		
		HashSet<String> constErrors = ConstantCheck.checkConstant(formulas);
		HashSet<String> bindErrors = GroundableCheck.checkBindable(formulas);
		for (String ce : constErrors) {
			System.err.println(ce);
		}
		for (String be : bindErrors) {
			System.err.println(be);
		}
		CRFDescription desc = new CRFDescription();
		desc.formulas = formulas;
		if (proposals.size() > 0) {
			desc.proposals = new ProposalSet(proposals);
		}
		if (objective.size() > 0) {
			desc.objectives = objective;
		}
		desc.constants = constants;
		desc.funNameToClass = funNameToClass;
		desc.maxVarCount = maxVarCount;
		desc.focusIndicators = focusIndicators;
		return desc;        
	}
	
	private BagDefinition handleBag(Tree setTerm) {
		ATerm created = handleSimpleTerm(setTerm.getChild(0));
		ArrayList<ATerm> condList = new ArrayList<ATerm>();
		ArrayList<Boolean> positive = new ArrayList<Boolean>();
		handleCondition(setTerm.getChild(1), condList, positive);
		ATerm[] condition = condList.toArray(new ATerm[condList.size()]);
	
		return new BagDefinition(created, condition, toBoolArray(positive), crfState);
	}
	
	private SetDefinition handleSet(Tree setTerm) {
		ATerm created = handleSimpleTerm(setTerm.getChild(0));
		ArrayList<ATerm> condList = new ArrayList<ATerm>();
		ArrayList<Boolean> positive = new ArrayList<Boolean>();
		handleCondition(setTerm.getChild(1), condList, positive);
		ATerm[] condition = condList.toArray(new ATerm[condList.size()]);
	
		return new SetDefinition(created, condition, toBoolArray(positive), crfState);
	}	

	private String normalizePredStr(String predStr, List<ATerm> args) {
		if (predStr.equals(">")) {
			predStr = "<";
			Collections.reverse(args);
		} else if (predStr.equals(">=")) {
			predStr = "<=";
			Collections.reverse(args);
		}
		return predStr;
	}
	
	private ATerm handleCompositeTerm(Tree compt) {
		LinkedList<ATerm> args = new LinkedList<ATerm>();
		//handle infix like (x + y) or (x = y)
		if (compt.getChildCount() == 3 && Arrays.binarySearch(OPERATORS, compt.getChild(1).getText()) >= 0) {
			String infix = compt.getChild(1).getText();
			args.add(handleSimpleTerm(compt.getChild(0)));
			args.add(handleSimpleTerm(compt.getChild(2)));
			infix = normalizePredStr(infix, args);
			return makeFunction(infix, funNameToClass, args.toArray(new ATerm[args.size()]));
		}
		
		String predStr = compt.getText();
		int firstArgNdx = 0;
		if (predStr.equals("(")) {
			predStr = compt.getChild(0).getText();
			firstArgNdx = 1;
		}
		if (predStr.startsWith("\\") || Arrays.binarySearch(OPERATORS, predStr) >= 0) {
			for (int i = firstArgNdx; i < compt.getChildCount(); ++i) {
				args.add(handleTerm(compt.getChild(i)));
			}
			predStr = normalizePredStr(predStr, args);
			String focusIndicator = null;
			int atPos = predStr.indexOf('@');
			if (atPos != -1) {
				focusIndicator = predStr.substring(atPos+1);
				predStr = predStr.substring(0, atPos);
			}
			try {
				Function f = makeFunction(predStr, funNameToClass, args.toArray(new ATerm[args.size()]));
				if (focusIndicator != null) {
					f.parserSetsFocusIndicator(focusIndicators.get(focusIndicator));
				}
				return f;
			} catch (Error e) {
				if (!SKIP_CLASSNOTFOUND) {
					throw e;
				} else {
					System.err.println(e);
					args.clear();
				}
			}		
		}
		
		//make this work for ScanTerms (x y) != x(y)
		if (!compt.getText().equals("(")) {
			args.add(getStrConst(compt.getText()));
		}
		for (int i = 0; i < compt.getChildCount(); ++i) {
			args.add(handleTerm(compt.getChild(i)));
		}
		return new ScanTerm(args.toArray(new ATerm[args.size()]));
	}
	

	//string, number or composite
	private ATerm handleSimpleTerm(Tree tnode) {
		//System.out.println("simpleterm: "+tnode.toString());
		if (tnode.getChildCount() > 0) {
			return handleCompositeTerm(tnode);
		}
		String text = tnode.getText();
		//can be parsed as double -> NumberTerm
		if (Lang.isDouble(text)) {
			return new NumberTerm(Double.parseDouble(text));
		}
		//starts lowercase -> varIdentifier
		if (Character.isLowerCase(text.charAt(0)) || text.startsWith("$") || text.equals("_")) {
			return getVarId(text);	
		}
		
		//starts '\' its a Function variable
		if (text.startsWith("\\")) {
			try {
			Function f = makeFunction(text, funNameToClass, CompositeTerm.EMPTY_TERM);
			f.zzParserInitUsedBy();
			return f;
			} catch (Error e) {
				if (!SKIP_CLASSNOTFOUND) {
					throw e;
				} else {
					System.err.println(e);
				}
			}
		}
		
		//starts uppercase -> StringTerm
		return getStrConst(text);
	}
	
	
	

	private ATerm handleTerm(Tree tnode) {
		//System.out.println("term: "+tnode.toString());
		//first switch on what kind of term:
		//  Variable, String, Number, Composite, Bag/Set
		if (tnode.getText().equals("{")) {
			return handleSet(tnode);
		} else if (tnode.getText().equals("[")) {
			return handleBag(tnode);
		} else {
			return handleSimpleTerm(tnode);
		}

	}
	
	private BetaWeightCreator handleBeta(Tree beta) {
		//create the BetaWeightCreator with the appropriate subscript and vector variable
		String idName = beta.getChild(0).getText();
		ATerm arg = handleSimpleTerm(beta.getChild(1));
		return new BetaWeightCreator(idName, arg);
	}
	private Formula handleBetaSent(Tree beta, Tree condition) {
		return makeFormula(handleBeta(beta), condition);
	}
	
	private Formula handleFactorSent(Tree weight, Tree condition) {
		Creator c = null;
		if (weight.getChildCount() == 0) {
			c = new FactorCreator(Double.parseDouble(weight.getText()));
		} else if (weight.getText().equals("(") || weight.getText().startsWith("\\")) {
			c = new FactorFunctionCreator((ParamWeight)null,handleCompositeTerm(weight), crfState);
		} else if (weight.getText().equals("T_{")) {
			ATerm f = null;
			ArrayList<ATerm> specList = new ArrayList<ATerm>();
			for (int i = 0; i < weight.getChildCount(); ++i) {
				if (weight.getChild(i).getText().equals("(")) {
					if (i != weight.getChildCount()-2) {
						throw new Error("Unexpected composite!");
					}
					f = handleCompositeTerm(weight.getChild(i+1));
					i = weight.getChildCount();
				} else {
					specList.add(handleSimpleTerm(weight.getChild(i)));
				}
			}
			

			ATerm[] specs = specList.toArray(new ATerm[specList.size()]);
			if (f != null) {
				//check if there are any vars in the ParamWeight specifiers
				boolean constant = true;
				for (ATerm t : specs) {
					if (!t.isGround(null)) {
						constant = false;
						break;
					}
				}
				if (constant) {
					c = new FactorFunctionCreator(crfState.learningState.instance(specs), f, crfState);
				} else {
					c = new FactorFunctionCreator(specs,f, crfState);
				}
			} else {
				c = new ParamCreator(specs, null, 0);
			}
			
			//c = new FactorCreator(Double.parseDouble("1001"));
		}
		if (c == null) {
			System.err.println("Bad factor sent:");
			printTree(weight, 0);
			printTree(condition, 0);
			throw new Error("Could not parse factor sent!");
		}

		return makeFormula(c, condition);
	}	
	
	
	
	private static boolean SKIP_CLASSNOTFOUND = false;
	public static void main(String[] args) throws Exception {
		SKIP_CLASSNOTFOUND = true;
		String toTestFile = "/home/mrglass/workspace/com.ibm.bluej.consistency/descriptors/jmeans.con";
		//"/home/mrglass/workspace/com.ibm.bluej.consistency/descriptors/nerTest1.con";
		//"/home/mrglass/workspace/com.ibm.bluej.consistency/descriptors/mst.con";
		// "/users/glassm/Medical/UM/testing.con";
		// "/home/mrglass/workspace/com.ibm.watsonmd.structterm/descriptors/structTermPCFGv.con";
		// "/home/mrglass/workspace/com.ibm.bluej.consistency/descriptors/HMM.con"
		ParserAntrl p = new ParserAntrl(new CRFState());
		CRFDescription c = p.readString(FileUtil.readFileAsString(toTestFile));
		System.out.println("FORMULAS:\n"+Lang.stringList(c.formulas, "\n"));
		if (c.objectives != null)
			System.out.println("OBJECTIVES:\n"+Lang.stringList(c.objectives, "\n"));
		if (c.proposals != null)
			System.out.println("PROPOSALS:\n"+Lang.stringList(c.proposals.getProposals(), "\n"));
		System.out.println("NUM VARS:\n"+c.maxVarCount);
		System.out.println("CONSTANTS:\n"+Lang.stringList(c.constants.keySet(), "\n"));
		
		Lang.readln();
		//System.out.println("")
		
		printTree(getTree(FileUtil.readFileAsString(toTestFile)),0);
	}
}
