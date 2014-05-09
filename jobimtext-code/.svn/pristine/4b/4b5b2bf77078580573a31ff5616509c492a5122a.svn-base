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

// $ANTLR 3.4 /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g 2012-10-18 09:44:34
 
package com.ibm.bluej.consistency.grammar; 



import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ConsistencyParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "AT", "BACKSLASH", "BETAFUNC", "BETA_BEGIN", "COMMA", "CONDITIONSEP", "DECLAREMARKER", "DEFMARKER", "DIGIT", "FACTORFUNC", "FORALLCONDITION", "LEFT_BRACE", "LEFT_BRACK", "LEFT_PAREN", "LETTER", "LINE_COMMENT", "MAKECONSTMARKER", "NAME", "NEGATION", "NEWLINE", "NUMBER", "OBJECTIVEMARKER", "OPERATOR", "PARAM_BEGIN", "PROPOSEMARKER", "RANDOMCONDITION", "RIGHT_BRACE", "RIGHT_BRACK", "RIGHT_PAREN", "SIGN", "SPACE", "STRINGCONST", "SUBPROPOSEMARKER", "TESTMARKER", "WEIGHTEDMAXCONDITION"
    };

    public static final int EOF=-1;
    public static final int AND=4;
    public static final int AT=5;
    public static final int BACKSLASH=6;
    public static final int BETAFUNC=7;
    public static final int BETA_BEGIN=8;
    public static final int COMMA=9;
    public static final int CONDITIONSEP=10;
    public static final int DECLAREMARKER=11;
    public static final int DEFMARKER=12;
    public static final int DIGIT=13;
    public static final int FACTORFUNC=14;
    public static final int FORALLCONDITION=15;
    public static final int LEFT_BRACE=16;
    public static final int LEFT_BRACK=17;
    public static final int LEFT_PAREN=18;
    public static final int LETTER=19;
    public static final int LINE_COMMENT=20;
    public static final int MAKECONSTMARKER=21;
    public static final int NAME=22;
    public static final int NEGATION=23;
    public static final int NEWLINE=24;
    public static final int NUMBER=25;
    public static final int OBJECTIVEMARKER=26;
    public static final int OPERATOR=27;
    public static final int PARAM_BEGIN=28;
    public static final int PROPOSEMARKER=29;
    public static final int RANDOMCONDITION=30;
    public static final int RIGHT_BRACE=31;
    public static final int RIGHT_BRACK=32;
    public static final int RIGHT_PAREN=33;
    public static final int SIGN=34;
    public static final int SPACE=35;
    public static final int STRINGCONST=36;
    public static final int SUBPROPOSEMARKER=37;
    public static final int TESTMARKER=38;
    public static final int WEIGHTEDMAXCONDITION=39;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public ConsistencyParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public ConsistencyParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return ConsistencyParser.tokenNames; }
    public String getGrammarFileName() { return "/home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g"; }


    public static class logicDesc_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "logicDesc"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:54:1: logicDesc : ( line )+ ;
    public final ConsistencyParser.logicDesc_return logicDesc() throws RecognitionException {
        ConsistencyParser.logicDesc_return retval = new ConsistencyParser.logicDesc_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.line_return line1 =null;



        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:54:11: ( ( line )+ )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:54:13: ( line )+
            {
            root_0 = (CommonTree)adaptor.nil();


            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:54:13: ( line )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==BETA_BEGIN||(LA1_0 >= DECLAREMARKER && LA1_0 <= DEFMARKER)||LA1_0==LEFT_PAREN||(LA1_0 >= MAKECONSTMARKER && LA1_0 <= NAME)||(LA1_0 >= NEWLINE && LA1_0 <= OBJECTIVEMARKER)||(LA1_0 >= PARAM_BEGIN && LA1_0 <= PROPOSEMARKER)||(LA1_0 >= SUBPROPOSEMARKER && LA1_0 <= TESTMARKER)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:54:13: line
            	    {
            	    pushFollow(FOLLOW_line_in_logicDesc787);
            	    line1=line();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, line1.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "logicDesc"


    public static class line_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "line"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:1: line : ( NEWLINE | betaSent NEWLINE !| factorSent NEWLINE !| ( definitionSent | definitionSentEmpty ) NEWLINE !| declaration NEWLINE !| testSent NEWLINE !| makeConstSent NEWLINE !| ( proposal | emptyProposal ) NEWLINE !| objectiveSent NEWLINE !);
    public final ConsistencyParser.line_return line() throws RecognitionException {
        ConsistencyParser.line_return retval = new ConsistencyParser.line_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NEWLINE2=null;
        Token NEWLINE4=null;
        Token NEWLINE6=null;
        Token NEWLINE9=null;
        Token NEWLINE11=null;
        Token NEWLINE13=null;
        Token NEWLINE15=null;
        Token NEWLINE18=null;
        Token NEWLINE20=null;
        ConsistencyParser.betaSent_return betaSent3 =null;

        ConsistencyParser.factorSent_return factorSent5 =null;

        ConsistencyParser.definitionSent_return definitionSent7 =null;

        ConsistencyParser.definitionSentEmpty_return definitionSentEmpty8 =null;

        ConsistencyParser.declaration_return declaration10 =null;

        ConsistencyParser.testSent_return testSent12 =null;

        ConsistencyParser.makeConstSent_return makeConstSent14 =null;

        ConsistencyParser.proposal_return proposal16 =null;

        ConsistencyParser.emptyProposal_return emptyProposal17 =null;

        ConsistencyParser.objectiveSent_return objectiveSent19 =null;


        CommonTree NEWLINE2_tree=null;
        CommonTree NEWLINE4_tree=null;
        CommonTree NEWLINE6_tree=null;
        CommonTree NEWLINE9_tree=null;
        CommonTree NEWLINE11_tree=null;
        CommonTree NEWLINE13_tree=null;
        CommonTree NEWLINE15_tree=null;
        CommonTree NEWLINE18_tree=null;
        CommonTree NEWLINE20_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:6: ( NEWLINE | betaSent NEWLINE !| factorSent NEWLINE !| ( definitionSent | definitionSentEmpty ) NEWLINE !| declaration NEWLINE !| testSent NEWLINE !| makeConstSent NEWLINE !| ( proposal | emptyProposal ) NEWLINE !| objectiveSent NEWLINE !)
            int alt4=9;
            switch ( input.LA(1) ) {
            case NEWLINE:
                {
                alt4=1;
                }
                break;
            case BETA_BEGIN:
                {
                alt4=2;
                }
                break;
            case LEFT_PAREN:
            case NAME:
            case NUMBER:
            case PARAM_BEGIN:
                {
                alt4=3;
                }
                break;
            case DEFMARKER:
                {
                alt4=4;
                }
                break;
            case DECLAREMARKER:
                {
                alt4=5;
                }
                break;
            case TESTMARKER:
                {
                alt4=6;
                }
                break;
            case MAKECONSTMARKER:
                {
                alt4=7;
                }
                break;
            case PROPOSEMARKER:
            case SUBPROPOSEMARKER:
                {
                alt4=8;
                }
                break;
            case OBJECTIVEMARKER:
                {
                alt4=9;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }

            switch (alt4) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:8: NEWLINE
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    NEWLINE2=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line797); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NEWLINE2_tree = 
                    (CommonTree)adaptor.create(NEWLINE2)
                    ;
                    adaptor.addChild(root_0, NEWLINE2_tree);
                    }

                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:18: betaSent NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_betaSent_in_line801);
                    betaSent3=betaSent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, betaSent3.getTree());

                    NEWLINE4=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line803); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:38: factorSent NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_factorSent_in_line808);
                    factorSent5=factorSent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, factorSent5.getTree());

                    NEWLINE6=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line810); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:60: ( definitionSent | definitionSentEmpty ) NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:60: ( definitionSent | definitionSentEmpty )
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==DEFMARKER) ) {
                        int LA2_1 = input.LA(2);

                        if ( (synpred5_Consistency()) ) {
                            alt2=1;
                        }
                        else if ( (true) ) {
                            alt2=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 1, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                        throw nvae;

                    }
                    switch (alt2) {
                        case 1 :
                            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:61: definitionSent
                            {
                            pushFollow(FOLLOW_definitionSent_in_line816);
                            definitionSent7=definitionSent();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, definitionSent7.getTree());

                            }
                            break;
                        case 2 :
                            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:78: definitionSentEmpty
                            {
                            pushFollow(FOLLOW_definitionSentEmpty_in_line820);
                            definitionSentEmpty8=definitionSentEmpty();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, definitionSentEmpty8.getTree());

                            }
                            break;

                    }


                    NEWLINE9=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line823); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:110: declaration NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_declaration_in_line828);
                    declaration10=declaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, declaration10.getTree());

                    NEWLINE11=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line830); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:133: testSent NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_testSent_in_line835);
                    testSent12=testSent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, testSent12.getTree());

                    NEWLINE13=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line837); if (state.failed) return retval;

                    }
                    break;
                case 7 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:153: makeConstSent NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_makeConstSent_in_line842);
                    makeConstSent14=makeConstSent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, makeConstSent14.getTree());

                    NEWLINE15=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line844); if (state.failed) return retval;

                    }
                    break;
                case 8 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:178: ( proposal | emptyProposal ) NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:178: ( proposal | emptyProposal )
                    int alt3=2;
                    alt3 = dfa3.predict(input);
                    switch (alt3) {
                        case 1 :
                            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:179: proposal
                            {
                            pushFollow(FOLLOW_proposal_in_line850);
                            proposal16=proposal();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, proposal16.getTree());

                            }
                            break;
                        case 2 :
                            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:190: emptyProposal
                            {
                            pushFollow(FOLLOW_emptyProposal_in_line854);
                            emptyProposal17=emptyProposal();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, emptyProposal17.getTree());

                            }
                            break;

                    }


                    NEWLINE18=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line857); if (state.failed) return retval;

                    }
                    break;
                case 9 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:216: objectiveSent NEWLINE !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_objectiveSent_in_line862);
                    objectiveSent19=objectiveSent();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, objectiveSent19.getTree());

                    NEWLINE20=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_line864); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "line"


    public static class endLine_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "endLine"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:57:1: endLine : ( NEWLINE | EOF );
    public final ConsistencyParser.endLine_return endLine() throws RecognitionException {
        ConsistencyParser.endLine_return retval = new ConsistencyParser.endLine_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set21=null;

        CommonTree set21_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:57:9: ( NEWLINE | EOF )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set21=(Token)input.LT(1);

            if ( input.LA(1)==EOF||input.LA(1)==NEWLINE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set21)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "endLine"


    public static class optionalSpace_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "optionalSpace"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:58:1: optionalSpace : ( SPACE )* ;
    public final ConsistencyParser.optionalSpace_return optionalSpace() throws RecognitionException {
        ConsistencyParser.optionalSpace_return retval = new ConsistencyParser.optionalSpace_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SPACE22=null;

        CommonTree SPACE22_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:58:15: ( ( SPACE )* )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:58:17: ( SPACE )*
            {
            root_0 = (CommonTree)adaptor.nil();


            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:58:17: ( SPACE )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==SPACE) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:58:17: SPACE
            	    {
            	    SPACE22=(Token)match(input,SPACE,FOLLOW_SPACE_in_optionalSpace885); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SPACE22_tree = 
            	    (CommonTree)adaptor.create(SPACE22)
            	    ;
            	    adaptor.addChild(root_0, SPACE22_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "optionalSpace"


    public static class conditionSep_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionSep"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:1: conditionSep : ( SPACE )* CONDITIONSEP ( SPACE )* ;
    public final ConsistencyParser.conditionSep_return conditionSep() throws RecognitionException {
        ConsistencyParser.conditionSep_return retval = new ConsistencyParser.conditionSep_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SPACE23=null;
        Token CONDITIONSEP24=null;
        Token SPACE25=null;

        CommonTree SPACE23_tree=null;
        CommonTree CONDITIONSEP24_tree=null;
        CommonTree SPACE25_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:14: ( ( SPACE )* CONDITIONSEP ( SPACE )* )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:16: ( SPACE )* CONDITIONSEP ( SPACE )*
            {
            root_0 = (CommonTree)adaptor.nil();


            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:16: ( SPACE )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==SPACE) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:16: SPACE
            	    {
            	    SPACE23=(Token)match(input,SPACE,FOLLOW_SPACE_in_conditionSep894); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SPACE23_tree = 
            	    (CommonTree)adaptor.create(SPACE23)
            	    ;
            	    adaptor.addChild(root_0, SPACE23_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            CONDITIONSEP24=(Token)match(input,CONDITIONSEP,FOLLOW_CONDITIONSEP_in_conditionSep897); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            CONDITIONSEP24_tree = 
            (CommonTree)adaptor.create(CONDITIONSEP24)
            ;
            adaptor.addChild(root_0, CONDITIONSEP24_tree);
            }

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:36: ( SPACE )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==SPACE) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:59:36: SPACE
            	    {
            	    SPACE25=(Token)match(input,SPACE,FOLLOW_SPACE_in_conditionSep899); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    SPACE25_tree = 
            	    (CommonTree)adaptor.create(SPACE25)
            	    ;
            	    adaptor.addChild(root_0, SPACE25_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "conditionSep"


    public static class betaSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "betaSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:60:1: betaSent : beta condition -> ^( BETAFUNC beta condition ) ;
    public final ConsistencyParser.betaSent_return betaSent() throws RecognitionException {
        ConsistencyParser.betaSent_return retval = new ConsistencyParser.betaSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.beta_return beta26 =null;

        ConsistencyParser.condition_return condition27 =null;


        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_beta=new RewriteRuleSubtreeStream(adaptor,"rule beta");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:60:10: ( beta condition -> ^( BETAFUNC beta condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:60:12: beta condition
            {
            pushFollow(FOLLOW_beta_in_betaSent908);
            beta26=beta();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_beta.add(beta26.getTree());

            pushFollow(FOLLOW_condition_in_betaSent910);
            condition27=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition27.getTree());

            // AST REWRITE
            // elements: condition, beta
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 60:27: -> ^( BETAFUNC beta condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:60:30: ^( BETAFUNC beta condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(BETAFUNC, "BETAFUNC")
                , root_1);

                adaptor.addChild(root_1, stream_beta.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "betaSent"


    public static class factorSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "factorSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:61:1: factorSent : factorFunc condition -> ^( FACTORFUNC factorFunc condition ) ;
    public final ConsistencyParser.factorSent_return factorSent() throws RecognitionException {
        ConsistencyParser.factorSent_return retval = new ConsistencyParser.factorSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.factorFunc_return factorFunc28 =null;

        ConsistencyParser.condition_return condition29 =null;


        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_factorFunc=new RewriteRuleSubtreeStream(adaptor,"rule factorFunc");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:61:12: ( factorFunc condition -> ^( FACTORFUNC factorFunc condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:61:14: factorFunc condition
            {
            pushFollow(FOLLOW_factorFunc_in_factorSent928);
            factorFunc28=factorFunc();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_factorFunc.add(factorFunc28.getTree());

            pushFollow(FOLLOW_condition_in_factorSent930);
            condition29=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition29.getTree());

            // AST REWRITE
            // elements: factorFunc, condition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 61:36: -> ^( FACTORFUNC factorFunc condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:61:39: ^( FACTORFUNC factorFunc condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(FACTORFUNC, "FACTORFUNC")
                , root_1);

                adaptor.addChild(root_1, stream_factorFunc.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "factorSent"


    public static class objectiveSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "objectiveSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:62:1: objectiveSent : OBJECTIVEMARKER optionalSpace factorSent -> ^( OBJECTIVEMARKER factorSent ) ;
    public final ConsistencyParser.objectiveSent_return objectiveSent() throws RecognitionException {
        ConsistencyParser.objectiveSent_return retval = new ConsistencyParser.objectiveSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token OBJECTIVEMARKER30=null;
        ConsistencyParser.optionalSpace_return optionalSpace31 =null;

        ConsistencyParser.factorSent_return factorSent32 =null;


        CommonTree OBJECTIVEMARKER30_tree=null;
        RewriteRuleTokenStream stream_OBJECTIVEMARKER=new RewriteRuleTokenStream(adaptor,"token OBJECTIVEMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_factorSent=new RewriteRuleSubtreeStream(adaptor,"rule factorSent");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:62:15: ( OBJECTIVEMARKER optionalSpace factorSent -> ^( OBJECTIVEMARKER factorSent ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:62:17: OBJECTIVEMARKER optionalSpace factorSent
            {
            OBJECTIVEMARKER30=(Token)match(input,OBJECTIVEMARKER,FOLLOW_OBJECTIVEMARKER_in_objectiveSent949); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_OBJECTIVEMARKER.add(OBJECTIVEMARKER30);


            pushFollow(FOLLOW_optionalSpace_in_objectiveSent951);
            optionalSpace31=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace31.getTree());

            pushFollow(FOLLOW_factorSent_in_objectiveSent953);
            factorSent32=factorSent();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_factorSent.add(factorSent32.getTree());

            // AST REWRITE
            // elements: OBJECTIVEMARKER, factorSent
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 62:58: -> ^( OBJECTIVEMARKER factorSent )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:62:61: ^( OBJECTIVEMARKER factorSent )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_OBJECTIVEMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_factorSent.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "objectiveSent"


    public static class factorFunc_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "factorFunc"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:63:1: factorFunc : ( compTerm | param | NUMBER );
    public final ConsistencyParser.factorFunc_return factorFunc() throws RecognitionException {
        ConsistencyParser.factorFunc_return retval = new ConsistencyParser.factorFunc_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NUMBER35=null;
        ConsistencyParser.compTerm_return compTerm33 =null;

        ConsistencyParser.param_return param34 =null;


        CommonTree NUMBER35_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:63:12: ( compTerm | param | NUMBER )
            int alt8=3;
            switch ( input.LA(1) ) {
            case LEFT_PAREN:
            case NAME:
                {
                alt8=1;
                }
                break;
            case PARAM_BEGIN:
                {
                alt8=2;
                }
                break;
            case NUMBER:
                {
                alt8=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }

            switch (alt8) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:63:14: compTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_compTerm_in_factorFunc968);
                    compTerm33=compTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, compTerm33.getTree());

                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:63:25: param
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_param_in_factorFunc972);
                    param34=param();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, param34.getTree());

                    }
                    break;
                case 3 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:63:33: NUMBER
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    NUMBER35=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_factorFunc976); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMBER35_tree = 
                    (CommonTree)adaptor.create(NUMBER35)
                    ;
                    adaptor.addChild(root_0, NUMBER35_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "factorFunc"


    public static class definitionSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "definitionSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:64:1: definitionSent : DEFMARKER optionalSpace compTerm condition -> ^( DEFMARKER compTerm condition ) ;
    public final ConsistencyParser.definitionSent_return definitionSent() throws RecognitionException {
        ConsistencyParser.definitionSent_return retval = new ConsistencyParser.definitionSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token DEFMARKER36=null;
        ConsistencyParser.optionalSpace_return optionalSpace37 =null;

        ConsistencyParser.compTerm_return compTerm38 =null;

        ConsistencyParser.condition_return condition39 =null;


        CommonTree DEFMARKER36_tree=null;
        RewriteRuleTokenStream stream_DEFMARKER=new RewriteRuleTokenStream(adaptor,"token DEFMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_compTerm=new RewriteRuleSubtreeStream(adaptor,"rule compTerm");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:64:16: ( DEFMARKER optionalSpace compTerm condition -> ^( DEFMARKER compTerm condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:64:18: DEFMARKER optionalSpace compTerm condition
            {
            DEFMARKER36=(Token)match(input,DEFMARKER,FOLLOW_DEFMARKER_in_definitionSent984); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DEFMARKER.add(DEFMARKER36);


            pushFollow(FOLLOW_optionalSpace_in_definitionSent986);
            optionalSpace37=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace37.getTree());

            pushFollow(FOLLOW_compTerm_in_definitionSent988);
            compTerm38=compTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compTerm.add(compTerm38.getTree());

            pushFollow(FOLLOW_condition_in_definitionSent990);
            condition39=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition39.getTree());

            // AST REWRITE
            // elements: DEFMARKER, compTerm, condition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 64:61: -> ^( DEFMARKER compTerm condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:64:64: ^( DEFMARKER compTerm condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_DEFMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_compTerm.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "definitionSent"


    public static class definitionSentEmpty_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "definitionSentEmpty"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:65:1: definitionSentEmpty : DEFMARKER optionalSpace compTerm -> ^( DEFMARKER compTerm ) ;
    public final ConsistencyParser.definitionSentEmpty_return definitionSentEmpty() throws RecognitionException {
        ConsistencyParser.definitionSentEmpty_return retval = new ConsistencyParser.definitionSentEmpty_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token DEFMARKER40=null;
        ConsistencyParser.optionalSpace_return optionalSpace41 =null;

        ConsistencyParser.compTerm_return compTerm42 =null;


        CommonTree DEFMARKER40_tree=null;
        RewriteRuleTokenStream stream_DEFMARKER=new RewriteRuleTokenStream(adaptor,"token DEFMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_compTerm=new RewriteRuleSubtreeStream(adaptor,"rule compTerm");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:65:21: ( DEFMARKER optionalSpace compTerm -> ^( DEFMARKER compTerm ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:65:23: DEFMARKER optionalSpace compTerm
            {
            DEFMARKER40=(Token)match(input,DEFMARKER,FOLLOW_DEFMARKER_in_definitionSentEmpty1007); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DEFMARKER.add(DEFMARKER40);


            pushFollow(FOLLOW_optionalSpace_in_definitionSentEmpty1009);
            optionalSpace41=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace41.getTree());

            pushFollow(FOLLOW_compTerm_in_definitionSentEmpty1011);
            compTerm42=compTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compTerm.add(compTerm42.getTree());

            // AST REWRITE
            // elements: compTerm, DEFMARKER
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 65:56: -> ^( DEFMARKER compTerm )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:65:59: ^( DEFMARKER compTerm )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_DEFMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_compTerm.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "definitionSentEmpty"


    public static class makeConstSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "makeConstSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:66:1: makeConstSent : MAKECONSTMARKER optionalSpace compTerm condition -> ^( MAKECONSTMARKER compTerm condition ) ;
    public final ConsistencyParser.makeConstSent_return makeConstSent() throws RecognitionException {
        ConsistencyParser.makeConstSent_return retval = new ConsistencyParser.makeConstSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token MAKECONSTMARKER43=null;
        ConsistencyParser.optionalSpace_return optionalSpace44 =null;

        ConsistencyParser.compTerm_return compTerm45 =null;

        ConsistencyParser.condition_return condition46 =null;


        CommonTree MAKECONSTMARKER43_tree=null;
        RewriteRuleTokenStream stream_MAKECONSTMARKER=new RewriteRuleTokenStream(adaptor,"token MAKECONSTMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_compTerm=new RewriteRuleSubtreeStream(adaptor,"rule compTerm");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:66:15: ( MAKECONSTMARKER optionalSpace compTerm condition -> ^( MAKECONSTMARKER compTerm condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:66:17: MAKECONSTMARKER optionalSpace compTerm condition
            {
            MAKECONSTMARKER43=(Token)match(input,MAKECONSTMARKER,FOLLOW_MAKECONSTMARKER_in_makeConstSent1026); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_MAKECONSTMARKER.add(MAKECONSTMARKER43);


            pushFollow(FOLLOW_optionalSpace_in_makeConstSent1028);
            optionalSpace44=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace44.getTree());

            pushFollow(FOLLOW_compTerm_in_makeConstSent1030);
            compTerm45=compTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_compTerm.add(compTerm45.getTree());

            pushFollow(FOLLOW_condition_in_makeConstSent1032);
            condition46=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition46.getTree());

            // AST REWRITE
            // elements: MAKECONSTMARKER, compTerm, condition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 66:66: -> ^( MAKECONSTMARKER compTerm condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:66:69: ^( MAKECONSTMARKER compTerm condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_MAKECONSTMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_compTerm.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "makeConstSent"


    public static class declaration_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "declaration"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:67:1: declaration : DECLAREMARKER optionalSpace fname= NAME conditionSep LEFT_BRACK jname= NAME RIGHT_BRACK -> ^( DECLAREMARKER $fname $jname) ;
    public final ConsistencyParser.declaration_return declaration() throws RecognitionException {
        ConsistencyParser.declaration_return retval = new ConsistencyParser.declaration_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token fname=null;
        Token jname=null;
        Token DECLAREMARKER47=null;
        Token LEFT_BRACK50=null;
        Token RIGHT_BRACK51=null;
        ConsistencyParser.optionalSpace_return optionalSpace48 =null;

        ConsistencyParser.conditionSep_return conditionSep49 =null;


        CommonTree fname_tree=null;
        CommonTree jname_tree=null;
        CommonTree DECLAREMARKER47_tree=null;
        CommonTree LEFT_BRACK50_tree=null;
        CommonTree RIGHT_BRACK51_tree=null;
        RewriteRuleTokenStream stream_LEFT_BRACK=new RewriteRuleTokenStream(adaptor,"token LEFT_BRACK");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_RIGHT_BRACK=new RewriteRuleTokenStream(adaptor,"token RIGHT_BRACK");
        RewriteRuleTokenStream stream_DECLAREMARKER=new RewriteRuleTokenStream(adaptor,"token DECLAREMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_conditionSep=new RewriteRuleSubtreeStream(adaptor,"rule conditionSep");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:67:13: ( DECLAREMARKER optionalSpace fname= NAME conditionSep LEFT_BRACK jname= NAME RIGHT_BRACK -> ^( DECLAREMARKER $fname $jname) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:67:15: DECLAREMARKER optionalSpace fname= NAME conditionSep LEFT_BRACK jname= NAME RIGHT_BRACK
            {
            DECLAREMARKER47=(Token)match(input,DECLAREMARKER,FOLLOW_DECLAREMARKER_in_declaration1049); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_DECLAREMARKER.add(DECLAREMARKER47);


            pushFollow(FOLLOW_optionalSpace_in_declaration1051);
            optionalSpace48=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace48.getTree());

            fname=(Token)match(input,NAME,FOLLOW_NAME_in_declaration1055); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(fname);


            pushFollow(FOLLOW_conditionSep_in_declaration1057);
            conditionSep49=conditionSep();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionSep.add(conditionSep49.getTree());

            LEFT_BRACK50=(Token)match(input,LEFT_BRACK,FOLLOW_LEFT_BRACK_in_declaration1059); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_BRACK.add(LEFT_BRACK50);


            jname=(Token)match(input,NAME,FOLLOW_NAME_in_declaration1063); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(jname);


            RIGHT_BRACK51=(Token)match(input,RIGHT_BRACK,FOLLOW_RIGHT_BRACK_in_declaration1065); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_BRACK.add(RIGHT_BRACK51);


            // AST REWRITE
            // elements: jname, DECLAREMARKER, fname
            // token labels: jname, fname
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleTokenStream stream_jname=new RewriteRuleTokenStream(adaptor,"token jname",jname);
            RewriteRuleTokenStream stream_fname=new RewriteRuleTokenStream(adaptor,"token fname",fname);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 67:101: -> ^( DECLAREMARKER $fname $jname)
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:67:104: ^( DECLAREMARKER $fname $jname)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_DECLAREMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_fname.nextNode());

                adaptor.addChild(root_1, stream_jname.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "declaration"


    public static class testSent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "testSent"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:68:1: testSent : TESTMARKER optionalSpace NAME termList -> ^( TESTMARKER NAME termList ) ;
    public final ConsistencyParser.testSent_return testSent() throws RecognitionException {
        ConsistencyParser.testSent_return retval = new ConsistencyParser.testSent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TESTMARKER52=null;
        Token NAME54=null;
        ConsistencyParser.optionalSpace_return optionalSpace53 =null;

        ConsistencyParser.termList_return termList55 =null;


        CommonTree TESTMARKER52_tree=null;
        CommonTree NAME54_tree=null;
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_TESTMARKER=new RewriteRuleTokenStream(adaptor,"token TESTMARKER");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_termList=new RewriteRuleSubtreeStream(adaptor,"rule termList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:68:10: ( TESTMARKER optionalSpace NAME termList -> ^( TESTMARKER NAME termList ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:68:12: TESTMARKER optionalSpace NAME termList
            {
            TESTMARKER52=(Token)match(input,TESTMARKER,FOLLOW_TESTMARKER_in_testSent1084); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_TESTMARKER.add(TESTMARKER52);


            pushFollow(FOLLOW_optionalSpace_in_testSent1086);
            optionalSpace53=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace53.getTree());

            NAME54=(Token)match(input,NAME,FOLLOW_NAME_in_testSent1088); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NAME.add(NAME54);


            pushFollow(FOLLOW_termList_in_testSent1090);
            termList55=termList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_termList.add(termList55.getTree());

            // AST REWRITE
            // elements: TESTMARKER, termList, NAME
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 68:51: -> ^( TESTMARKER NAME termList )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:68:54: ^( TESTMARKER NAME termList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_TESTMARKER.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_NAME.nextNode()
                );

                adaptor.addChild(root_1, stream_termList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "testSent"


    public static class proposal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "proposal"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:1: proposal : proMarker optionalSpace proChanges ( SPACE )* proCondition -> ^( proMarker proChanges proCondition ) ;
    public final ConsistencyParser.proposal_return proposal() throws RecognitionException {
        ConsistencyParser.proposal_return retval = new ConsistencyParser.proposal_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SPACE59=null;
        ConsistencyParser.proMarker_return proMarker56 =null;

        ConsistencyParser.optionalSpace_return optionalSpace57 =null;

        ConsistencyParser.proChanges_return proChanges58 =null;

        ConsistencyParser.proCondition_return proCondition60 =null;


        CommonTree SPACE59_tree=null;
        RewriteRuleTokenStream stream_SPACE=new RewriteRuleTokenStream(adaptor,"token SPACE");
        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_proMarker=new RewriteRuleSubtreeStream(adaptor,"rule proMarker");
        RewriteRuleSubtreeStream stream_proChanges=new RewriteRuleSubtreeStream(adaptor,"rule proChanges");
        RewriteRuleSubtreeStream stream_proCondition=new RewriteRuleSubtreeStream(adaptor,"rule proCondition");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:10: ( proMarker optionalSpace proChanges ( SPACE )* proCondition -> ^( proMarker proChanges proCondition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:12: proMarker optionalSpace proChanges ( SPACE )* proCondition
            {
            pushFollow(FOLLOW_proMarker_in_proposal1107);
            proMarker56=proMarker();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_proMarker.add(proMarker56.getTree());

            pushFollow(FOLLOW_optionalSpace_in_proposal1109);
            optionalSpace57=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace57.getTree());

            pushFollow(FOLLOW_proChanges_in_proposal1111);
            proChanges58=proChanges();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_proChanges.add(proChanges58.getTree());

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:47: ( SPACE )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==SPACE) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:47: SPACE
            	    {
            	    SPACE59=(Token)match(input,SPACE,FOLLOW_SPACE_in_proposal1113); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SPACE.add(SPACE59);


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            pushFollow(FOLLOW_proCondition_in_proposal1116);
            proCondition60=proCondition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_proCondition.add(proCondition60.getTree());

            // AST REWRITE
            // elements: proMarker, proChanges, proCondition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 69:67: -> ^( proMarker proChanges proCondition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:69:70: ^( proMarker proChanges proCondition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_proMarker.nextNode(), root_1);

                adaptor.addChild(root_1, stream_proChanges.nextTree());

                adaptor.addChild(root_1, stream_proCondition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "proposal"


    public static class emptyProposal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "emptyProposal"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:70:1: emptyProposal : proMarker optionalSpace proCondition -> ^( proMarker proCondition ) ;
    public final ConsistencyParser.emptyProposal_return emptyProposal() throws RecognitionException {
        ConsistencyParser.emptyProposal_return retval = new ConsistencyParser.emptyProposal_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.proMarker_return proMarker61 =null;

        ConsistencyParser.optionalSpace_return optionalSpace62 =null;

        ConsistencyParser.proCondition_return proCondition63 =null;


        RewriteRuleSubtreeStream stream_optionalSpace=new RewriteRuleSubtreeStream(adaptor,"rule optionalSpace");
        RewriteRuleSubtreeStream stream_proMarker=new RewriteRuleSubtreeStream(adaptor,"rule proMarker");
        RewriteRuleSubtreeStream stream_proCondition=new RewriteRuleSubtreeStream(adaptor,"rule proCondition");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:70:15: ( proMarker optionalSpace proCondition -> ^( proMarker proCondition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:70:17: proMarker optionalSpace proCondition
            {
            pushFollow(FOLLOW_proMarker_in_emptyProposal1134);
            proMarker61=proMarker();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_proMarker.add(proMarker61.getTree());

            pushFollow(FOLLOW_optionalSpace_in_emptyProposal1136);
            optionalSpace62=optionalSpace();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optionalSpace.add(optionalSpace62.getTree());

            pushFollow(FOLLOW_proCondition_in_emptyProposal1138);
            proCondition63=proCondition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_proCondition.add(proCondition63.getTree());

            // AST REWRITE
            // elements: proMarker, proCondition
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 70:54: -> ^( proMarker proCondition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:70:57: ^( proMarker proCondition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_proMarker.nextNode(), root_1);

                adaptor.addChild(root_1, stream_proCondition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "emptyProposal"


    public static class proMarker_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "proMarker"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:71:1: proMarker : ( PROPOSEMARKER | SUBPROPOSEMARKER );
    public final ConsistencyParser.proMarker_return proMarker() throws RecognitionException {
        ConsistencyParser.proMarker_return retval = new ConsistencyParser.proMarker_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set64=null;

        CommonTree set64_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:71:11: ( PROPOSEMARKER | SUBPROPOSEMARKER )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set64=(Token)input.LT(1);

            if ( input.LA(1)==PROPOSEMARKER||input.LA(1)==SUBPROPOSEMARKER ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set64)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "proMarker"


    public static class proChanges_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "proChanges"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:72:1: proChanges : conditionList -> ^( CONDITIONSEP conditionList ) ;
    public final ConsistencyParser.proChanges_return proChanges() throws RecognitionException {
        ConsistencyParser.proChanges_return retval = new ConsistencyParser.proChanges_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.conditionList_return conditionList65 =null;


        RewriteRuleSubtreeStream stream_conditionList=new RewriteRuleSubtreeStream(adaptor,"rule conditionList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:72:12: ( conditionList -> ^( CONDITIONSEP conditionList ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:72:14: conditionList
            {
            pushFollow(FOLLOW_conditionList_in_proChanges1166);
            conditionList65=conditionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionList.add(conditionList65.getTree());

            // AST REWRITE
            // elements: conditionList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 72:28: -> ^( CONDITIONSEP conditionList )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:72:31: ^( CONDITIONSEP conditionList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(CONDITIONSEP, "CONDITIONSEP")
                , root_1);

                adaptor.addChild(root_1, stream_conditionList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "proChanges"


    public static class proCondition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "proCondition"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:1: proCondition : (proSep= RANDOMCONDITION |proSep= WEIGHTEDMAXCONDITION |proSep= FORALLCONDITION ) ( SPACE )* conditionList -> ^( $proSep conditionList ) ;
    public final ConsistencyParser.proCondition_return proCondition() throws RecognitionException {
        ConsistencyParser.proCondition_return retval = new ConsistencyParser.proCondition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token proSep=null;
        Token SPACE66=null;
        ConsistencyParser.conditionList_return conditionList67 =null;


        CommonTree proSep_tree=null;
        CommonTree SPACE66_tree=null;
        RewriteRuleTokenStream stream_RANDOMCONDITION=new RewriteRuleTokenStream(adaptor,"token RANDOMCONDITION");
        RewriteRuleTokenStream stream_WEIGHTEDMAXCONDITION=new RewriteRuleTokenStream(adaptor,"token WEIGHTEDMAXCONDITION");
        RewriteRuleTokenStream stream_SPACE=new RewriteRuleTokenStream(adaptor,"token SPACE");
        RewriteRuleTokenStream stream_FORALLCONDITION=new RewriteRuleTokenStream(adaptor,"token FORALLCONDITION");
        RewriteRuleSubtreeStream stream_conditionList=new RewriteRuleSubtreeStream(adaptor,"rule conditionList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:14: ( (proSep= RANDOMCONDITION |proSep= WEIGHTEDMAXCONDITION |proSep= FORALLCONDITION ) ( SPACE )* conditionList -> ^( $proSep conditionList ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:16: (proSep= RANDOMCONDITION |proSep= WEIGHTEDMAXCONDITION |proSep= FORALLCONDITION ) ( SPACE )* conditionList
            {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:16: (proSep= RANDOMCONDITION |proSep= WEIGHTEDMAXCONDITION |proSep= FORALLCONDITION )
            int alt10=3;
            switch ( input.LA(1) ) {
            case RANDOMCONDITION:
                {
                alt10=1;
                }
                break;
            case WEIGHTEDMAXCONDITION:
                {
                alt10=2;
                }
                break;
            case FORALLCONDITION:
                {
                alt10=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:17: proSep= RANDOMCONDITION
                    {
                    proSep=(Token)match(input,RANDOMCONDITION,FOLLOW_RANDOMCONDITION_in_proCondition1185); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RANDOMCONDITION.add(proSep);


                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:42: proSep= WEIGHTEDMAXCONDITION
                    {
                    proSep=(Token)match(input,WEIGHTEDMAXCONDITION,FOLLOW_WEIGHTEDMAXCONDITION_in_proCondition1191); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WEIGHTEDMAXCONDITION.add(proSep);


                    }
                    break;
                case 3 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:72: proSep= FORALLCONDITION
                    {
                    proSep=(Token)match(input,FORALLCONDITION,FOLLOW_FORALLCONDITION_in_proCondition1197); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FORALLCONDITION.add(proSep);


                    }
                    break;

            }


            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:96: ( SPACE )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==SPACE) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:96: SPACE
            	    {
            	    SPACE66=(Token)match(input,SPACE,FOLLOW_SPACE_in_proCondition1200); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_SPACE.add(SPACE66);


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            pushFollow(FOLLOW_conditionList_in_proCondition1203);
            conditionList67=conditionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionList.add(conditionList67.getTree());

            // AST REWRITE
            // elements: conditionList, proSep
            // token labels: proSep
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleTokenStream stream_proSep=new RewriteRuleTokenStream(adaptor,"token proSep",proSep);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 73:117: -> ^( $proSep conditionList )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:73:120: ^( $proSep conditionList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_proSep.nextNode(), root_1);

                adaptor.addChild(root_1, stream_conditionList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "proCondition"


    public static class condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "condition"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:75:1: condition : conditionSep conditionList -> ^( CONDITIONSEP conditionList ) ;
    public final ConsistencyParser.condition_return condition() throws RecognitionException {
        ConsistencyParser.condition_return retval = new ConsistencyParser.condition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.conditionSep_return conditionSep68 =null;

        ConsistencyParser.conditionList_return conditionList69 =null;


        RewriteRuleSubtreeStream stream_conditionSep=new RewriteRuleSubtreeStream(adaptor,"rule conditionSep");
        RewriteRuleSubtreeStream stream_conditionList=new RewriteRuleSubtreeStream(adaptor,"rule conditionList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:75:11: ( conditionSep conditionList -> ^( CONDITIONSEP conditionList ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:75:13: conditionSep conditionList
            {
            pushFollow(FOLLOW_conditionSep_in_condition1221);
            conditionSep68=conditionSep();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionSep.add(conditionSep68.getTree());

            pushFollow(FOLLOW_conditionList_in_condition1223);
            conditionList69=conditionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionList.add(conditionList69.getTree());

            // AST REWRITE
            // elements: conditionList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 75:40: -> ^( CONDITIONSEP conditionList )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:75:43: ^( CONDITIONSEP conditionList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(CONDITIONSEP, "CONDITIONSEP")
                , root_1);

                adaptor.addChild(root_1, stream_conditionList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "condition"


    public static class conditionList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionList"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:1: conditionList : ( NEGATION )? compTerm ( conditionTail )? ;
    public final ConsistencyParser.conditionList_return conditionList() throws RecognitionException {
        ConsistencyParser.conditionList_return retval = new ConsistencyParser.conditionList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NEGATION70=null;
        ConsistencyParser.compTerm_return compTerm71 =null;

        ConsistencyParser.conditionTail_return conditionTail72 =null;


        CommonTree NEGATION70_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:15: ( ( NEGATION )? compTerm ( conditionTail )? )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:17: ( NEGATION )? compTerm ( conditionTail )?
            {
            root_0 = (CommonTree)adaptor.nil();


            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:17: ( NEGATION )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==NEGATION) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:18: NEGATION
                    {
                    NEGATION70=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_conditionList1240); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NEGATION70_tree = 
                    (CommonTree)adaptor.create(NEGATION70)
                    ;
                    adaptor.addChild(root_0, NEGATION70_tree);
                    }

                    }
                    break;

            }


            pushFollow(FOLLOW_compTerm_in_conditionList1244);
            compTerm71=compTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, compTerm71.getTree());

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:38: ( conditionTail )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==AND) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:76:39: conditionTail
                    {
                    pushFollow(FOLLOW_conditionTail_in_conditionList1247);
                    conditionTail72=conditionTail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionTail72.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "conditionList"


    public static class conditionTail_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionTail"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:77:1: conditionTail : AND conditionList -> conditionList ;
    public final ConsistencyParser.conditionTail_return conditionTail() throws RecognitionException {
        ConsistencyParser.conditionTail_return retval = new ConsistencyParser.conditionTail_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AND73=null;
        ConsistencyParser.conditionList_return conditionList74 =null;


        CommonTree AND73_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_conditionList=new RewriteRuleSubtreeStream(adaptor,"rule conditionList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:77:15: ( AND conditionList -> conditionList )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:77:17: AND conditionList
            {
            AND73=(Token)match(input,AND,FOLLOW_AND_in_conditionTail1258); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_AND.add(AND73);


            pushFollow(FOLLOW_conditionList_in_conditionTail1260);
            conditionList74=conditionList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_conditionList.add(conditionList74.getTree());

            // AST REWRITE
            // elements: conditionList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 77:35: -> conditionList
            {
                adaptor.addChild(root_0, stream_conditionList.nextTree());

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "conditionTail"


    public static class param_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "param"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:79:1: param : PARAM_BEGIN ^ termList RIGHT_BRACE ! ( LEFT_PAREN simpleTerm RIGHT_PAREN !)? ;
    public final ConsistencyParser.param_return param() throws RecognitionException {
        ConsistencyParser.param_return retval = new ConsistencyParser.param_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PARAM_BEGIN75=null;
        Token RIGHT_BRACE77=null;
        Token LEFT_PAREN78=null;
        Token RIGHT_PAREN80=null;
        ConsistencyParser.termList_return termList76 =null;

        ConsistencyParser.simpleTerm_return simpleTerm79 =null;


        CommonTree PARAM_BEGIN75_tree=null;
        CommonTree RIGHT_BRACE77_tree=null;
        CommonTree LEFT_PAREN78_tree=null;
        CommonTree RIGHT_PAREN80_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:79:7: ( PARAM_BEGIN ^ termList RIGHT_BRACE ! ( LEFT_PAREN simpleTerm RIGHT_PAREN !)? )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:79:9: PARAM_BEGIN ^ termList RIGHT_BRACE ! ( LEFT_PAREN simpleTerm RIGHT_PAREN !)?
            {
            root_0 = (CommonTree)adaptor.nil();


            PARAM_BEGIN75=(Token)match(input,PARAM_BEGIN,FOLLOW_PARAM_BEGIN_in_param1273); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PARAM_BEGIN75_tree = 
            (CommonTree)adaptor.create(PARAM_BEGIN75)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(PARAM_BEGIN75_tree, root_0);
            }

            pushFollow(FOLLOW_termList_in_param1276);
            termList76=termList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, termList76.getTree());

            RIGHT_BRACE77=(Token)match(input,RIGHT_BRACE,FOLLOW_RIGHT_BRACE_in_param1278); if (state.failed) return retval;

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:79:44: ( LEFT_PAREN simpleTerm RIGHT_PAREN !)?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==LEFT_PAREN) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:79:46: LEFT_PAREN simpleTerm RIGHT_PAREN !
                    {
                    LEFT_PAREN78=(Token)match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_param1283); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LEFT_PAREN78_tree = 
                    (CommonTree)adaptor.create(LEFT_PAREN78)
                    ;
                    adaptor.addChild(root_0, LEFT_PAREN78_tree);
                    }

                    pushFollow(FOLLOW_simpleTerm_in_param1285);
                    simpleTerm79=simpleTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleTerm79.getTree());

                    RIGHT_PAREN80=(Token)match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_param1287); if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "param"


    public static class beta_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "beta"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:80:1: beta : BETA_BEGIN ^ NAME RIGHT_BRACE ! LEFT_PAREN ! simpleTerm RIGHT_PAREN !;
    public final ConsistencyParser.beta_return beta() throws RecognitionException {
        ConsistencyParser.beta_return retval = new ConsistencyParser.beta_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BETA_BEGIN81=null;
        Token NAME82=null;
        Token RIGHT_BRACE83=null;
        Token LEFT_PAREN84=null;
        Token RIGHT_PAREN86=null;
        ConsistencyParser.simpleTerm_return simpleTerm85 =null;


        CommonTree BETA_BEGIN81_tree=null;
        CommonTree NAME82_tree=null;
        CommonTree RIGHT_BRACE83_tree=null;
        CommonTree LEFT_PAREN84_tree=null;
        CommonTree RIGHT_PAREN86_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:80:6: ( BETA_BEGIN ^ NAME RIGHT_BRACE ! LEFT_PAREN ! simpleTerm RIGHT_PAREN !)
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:80:8: BETA_BEGIN ^ NAME RIGHT_BRACE ! LEFT_PAREN ! simpleTerm RIGHT_PAREN !
            {
            root_0 = (CommonTree)adaptor.nil();


            BETA_BEGIN81=(Token)match(input,BETA_BEGIN,FOLLOW_BETA_BEGIN_in_beta1298); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            BETA_BEGIN81_tree = 
            (CommonTree)adaptor.create(BETA_BEGIN81)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(BETA_BEGIN81_tree, root_0);
            }

            NAME82=(Token)match(input,NAME,FOLLOW_NAME_in_beta1301); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME82_tree = 
            (CommonTree)adaptor.create(NAME82)
            ;
            adaptor.addChild(root_0, NAME82_tree);
            }

            RIGHT_BRACE83=(Token)match(input,RIGHT_BRACE,FOLLOW_RIGHT_BRACE_in_beta1303); if (state.failed) return retval;

            LEFT_PAREN84=(Token)match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_beta1306); if (state.failed) return retval;

            pushFollow(FOLLOW_simpleTerm_in_beta1309);
            simpleTerm85=simpleTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleTerm85.getTree());

            RIGHT_PAREN86=(Token)match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_beta1311); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "beta"


    public static class compTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compTerm"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:83:1: compTerm : ( compTermHead | compTermNoHead );
    public final ConsistencyParser.compTerm_return compTerm() throws RecognitionException {
        ConsistencyParser.compTerm_return retval = new ConsistencyParser.compTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.compTermHead_return compTermHead87 =null;

        ConsistencyParser.compTermNoHead_return compTermNoHead88 =null;



        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:83:10: ( compTermHead | compTermNoHead )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==NAME) ) {
                alt15=1;
            }
            else if ( (LA15_0==LEFT_PAREN) ) {
                alt15=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }
            switch (alt15) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:83:12: compTermHead
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_compTermHead_in_compTerm1322);
                    compTermHead87=compTermHead();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, compTermHead87.getTree());

                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:83:27: compTermNoHead
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_compTermNoHead_in_compTerm1326);
                    compTermNoHead88=compTermNoHead();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, compTermNoHead88.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "compTerm"


    public static class compTermHead_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compTermHead"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:84:1: compTermHead : NAME ^ LEFT_PAREN ! ( termList )? RIGHT_PAREN !;
    public final ConsistencyParser.compTermHead_return compTermHead() throws RecognitionException {
        ConsistencyParser.compTermHead_return retval = new ConsistencyParser.compTermHead_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME89=null;
        Token LEFT_PAREN90=null;
        Token RIGHT_PAREN92=null;
        ConsistencyParser.termList_return termList91 =null;


        CommonTree NAME89_tree=null;
        CommonTree LEFT_PAREN90_tree=null;
        CommonTree RIGHT_PAREN92_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:84:14: ( NAME ^ LEFT_PAREN ! ( termList )? RIGHT_PAREN !)
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:84:16: NAME ^ LEFT_PAREN ! ( termList )? RIGHT_PAREN !
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME89=(Token)match(input,NAME,FOLLOW_NAME_in_compTermHead1334); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NAME89_tree = 
            (CommonTree)adaptor.create(NAME89)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(NAME89_tree, root_0);
            }

            LEFT_PAREN90=(Token)match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_compTermHead1337); if (state.failed) return retval;

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:84:34: ( termList )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0 >= LEFT_BRACE && LA16_0 <= LEFT_PAREN)||LA16_0==NAME||LA16_0==NUMBER||LA16_0==OPERATOR||LA16_0==STRINGCONST) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:84:34: termList
                    {
                    pushFollow(FOLLOW_termList_in_compTermHead1340);
                    termList91=termList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, termList91.getTree());

                    }
                    break;

            }


            RIGHT_PAREN92=(Token)match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_compTermHead1343); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "compTermHead"


    public static class compTermNoHead_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compTermNoHead"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:85:1: compTermNoHead : LEFT_PAREN ^ termList RIGHT_PAREN !;
    public final ConsistencyParser.compTermNoHead_return compTermNoHead() throws RecognitionException {
        ConsistencyParser.compTermNoHead_return retval = new ConsistencyParser.compTermNoHead_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LEFT_PAREN93=null;
        Token RIGHT_PAREN95=null;
        ConsistencyParser.termList_return termList94 =null;


        CommonTree LEFT_PAREN93_tree=null;
        CommonTree RIGHT_PAREN95_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:85:16: ( LEFT_PAREN ^ termList RIGHT_PAREN !)
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:85:18: LEFT_PAREN ^ termList RIGHT_PAREN !
            {
            root_0 = (CommonTree)adaptor.nil();


            LEFT_PAREN93=(Token)match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_compTermNoHead1352); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFT_PAREN93_tree = 
            (CommonTree)adaptor.create(LEFT_PAREN93)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(LEFT_PAREN93_tree, root_0);
            }

            pushFollow(FOLLOW_termList_in_compTermNoHead1355);
            termList94=termList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, termList94.getTree());

            RIGHT_PAREN95=(Token)match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_compTermNoHead1357); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "compTermNoHead"


    public static class termList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "termList"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:86:1: termList : term ( termTail )? ;
    public final ConsistencyParser.termList_return termList() throws RecognitionException {
        ConsistencyParser.termList_return retval = new ConsistencyParser.termList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        ConsistencyParser.term_return term96 =null;

        ConsistencyParser.termTail_return termTail97 =null;



        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:86:10: ( term ( termTail )? )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:86:12: term ( termTail )?
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_term_in_termList1366);
            term96=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term96.getTree());

            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:86:17: ( termTail )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==COMMA||LA17_0==SPACE) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:86:17: termTail
                    {
                    pushFollow(FOLLOW_termTail_in_termList1368);
                    termTail97=termTail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, termTail97.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "termList"


    public static class termTail_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "termTail"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:1: termTail : ( ( SPACE )* COMMA ( SPACE )* | SPACE ) termList -> termList ;
    public final ConsistencyParser.termTail_return termTail() throws RecognitionException {
        ConsistencyParser.termTail_return retval = new ConsistencyParser.termTail_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SPACE98=null;
        Token COMMA99=null;
        Token SPACE100=null;
        Token SPACE101=null;
        ConsistencyParser.termList_return termList102 =null;


        CommonTree SPACE98_tree=null;
        CommonTree COMMA99_tree=null;
        CommonTree SPACE100_tree=null;
        CommonTree SPACE101_tree=null;
        RewriteRuleTokenStream stream_SPACE=new RewriteRuleTokenStream(adaptor,"token SPACE");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_termList=new RewriteRuleSubtreeStream(adaptor,"rule termList");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:10: ( ( ( SPACE )* COMMA ( SPACE )* | SPACE ) termList -> termList )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:12: ( ( SPACE )* COMMA ( SPACE )* | SPACE ) termList
            {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:12: ( ( SPACE )* COMMA ( SPACE )* | SPACE )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==SPACE) ) {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==COMMA||LA20_1==SPACE) ) {
                    alt20=1;
                }
                else if ( ((LA20_1 >= LEFT_BRACE && LA20_1 <= LEFT_PAREN)||LA20_1==NAME||LA20_1==NUMBER||LA20_1==OPERATOR||LA20_1==STRINGCONST) ) {
                    alt20=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA20_0==COMMA) ) {
                alt20=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }
            switch (alt20) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:13: ( SPACE )* COMMA ( SPACE )*
                    {
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:13: ( SPACE )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( (LA18_0==SPACE) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:13: SPACE
                    	    {
                    	    SPACE98=(Token)match(input,SPACE,FOLLOW_SPACE_in_termTail1379); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SPACE.add(SPACE98);


                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);


                    COMMA99=(Token)match(input,COMMA,FOLLOW_COMMA_in_termTail1382); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMMA.add(COMMA99);


                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:26: ( SPACE )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==SPACE) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:26: SPACE
                    	    {
                    	    SPACE100=(Token)match(input,SPACE,FOLLOW_SPACE_in_termTail1384); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_SPACE.add(SPACE100);


                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:88:35: SPACE
                    {
                    SPACE101=(Token)match(input,SPACE,FOLLOW_SPACE_in_termTail1389); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SPACE.add(SPACE101);


                    }
                    break;

            }


            pushFollow(FOLLOW_termList_in_termTail1392);
            termList102=termList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_termList.add(termList102.getTree());

            // AST REWRITE
            // elements: termList
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 88:51: -> termList
            {
                adaptor.addChild(root_0, stream_termList.nextTree());

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "termTail"


    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:1: term : ( simpleTerm | setTerm | bagTerm | OPERATOR );
    public final ConsistencyParser.term_return term() throws RecognitionException {
        ConsistencyParser.term_return retval = new ConsistencyParser.term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token OPERATOR106=null;
        ConsistencyParser.simpleTerm_return simpleTerm103 =null;

        ConsistencyParser.setTerm_return setTerm104 =null;

        ConsistencyParser.bagTerm_return bagTerm105 =null;


        CommonTree OPERATOR106_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:6: ( simpleTerm | setTerm | bagTerm | OPERATOR )
            int alt21=4;
            switch ( input.LA(1) ) {
            case LEFT_PAREN:
            case NAME:
            case NUMBER:
            case STRINGCONST:
                {
                alt21=1;
                }
                break;
            case LEFT_BRACE:
                {
                alt21=2;
                }
                break;
            case LEFT_BRACK:
                {
                alt21=3;
                }
                break;
            case OPERATOR:
                {
                alt21=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }

            switch (alt21) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:8: simpleTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_simpleTerm_in_term1403);
                    simpleTerm103=simpleTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, simpleTerm103.getTree());

                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:21: setTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_setTerm_in_term1407);
                    setTerm104=setTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, setTerm104.getTree());

                    }
                    break;
                case 3 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:31: bagTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_bagTerm_in_term1411);
                    bagTerm105=bagTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, bagTerm105.getTree());

                    }
                    break;
                case 4 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:89:41: OPERATOR
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    OPERATOR106=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_term1415); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    OPERATOR106_tree = 
                    (CommonTree)adaptor.create(OPERATOR106)
                    ;
                    adaptor.addChild(root_0, OPERATOR106_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term"


    public static class simpleTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "simpleTerm"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:1: simpleTerm : ( NAME | NUMBER | STRINGCONST | compTerm );
    public final ConsistencyParser.simpleTerm_return simpleTerm() throws RecognitionException {
        ConsistencyParser.simpleTerm_return retval = new ConsistencyParser.simpleTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME107=null;
        Token NUMBER108=null;
        Token STRINGCONST109=null;
        ConsistencyParser.compTerm_return compTerm110 =null;


        CommonTree NAME107_tree=null;
        CommonTree NUMBER108_tree=null;
        CommonTree STRINGCONST109_tree=null;

        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:12: ( NAME | NUMBER | STRINGCONST | compTerm )
            int alt22=4;
            switch ( input.LA(1) ) {
            case NAME:
                {
                int LA22_1 = input.LA(2);

                if ( (LA22_1==LEFT_PAREN) ) {
                    alt22=4;
                }
                else if ( (LA22_1==EOF||(LA22_1 >= COMMA && LA22_1 <= CONDITIONSEP)||LA22_1==NEWLINE||LA22_1==RIGHT_BRACE||LA22_1==RIGHT_PAREN||LA22_1==SPACE) ) {
                    alt22=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 22, 1, input);

                    throw nvae;

                }
                }
                break;
            case NUMBER:
                {
                alt22=2;
                }
                break;
            case STRINGCONST:
                {
                alt22=3;
                }
                break;
            case LEFT_PAREN:
                {
                alt22=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }

            switch (alt22) {
                case 1 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:14: NAME
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    NAME107=(Token)match(input,NAME,FOLLOW_NAME_in_simpleTerm1424); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NAME107_tree = 
                    (CommonTree)adaptor.create(NAME107)
                    ;
                    adaptor.addChild(root_0, NAME107_tree);
                    }

                    }
                    break;
                case 2 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:21: NUMBER
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    NUMBER108=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_simpleTerm1428); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMBER108_tree = 
                    (CommonTree)adaptor.create(NUMBER108)
                    ;
                    adaptor.addChild(root_0, NUMBER108_tree);
                    }

                    }
                    break;
                case 3 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:30: STRINGCONST
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    STRINGCONST109=(Token)match(input,STRINGCONST,FOLLOW_STRINGCONST_in_simpleTerm1432); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRINGCONST109_tree = 
                    (CommonTree)adaptor.create(STRINGCONST109)
                    ;
                    adaptor.addChild(root_0, STRINGCONST109_tree);
                    }

                    }
                    break;
                case 4 :
                    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:90:44: compTerm
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_compTerm_in_simpleTerm1436);
                    compTerm110=compTerm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, compTerm110.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "simpleTerm"


    public static class setTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "setTerm"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:92:1: setTerm : LEFT_BRACE simpleTerm condition RIGHT_BRACE -> ^( LEFT_BRACE simpleTerm condition ) ;
    public final ConsistencyParser.setTerm_return setTerm() throws RecognitionException {
        ConsistencyParser.setTerm_return retval = new ConsistencyParser.setTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LEFT_BRACE111=null;
        Token RIGHT_BRACE114=null;
        ConsistencyParser.simpleTerm_return simpleTerm112 =null;

        ConsistencyParser.condition_return condition113 =null;


        CommonTree LEFT_BRACE111_tree=null;
        CommonTree RIGHT_BRACE114_tree=null;
        RewriteRuleTokenStream stream_RIGHT_BRACE=new RewriteRuleTokenStream(adaptor,"token RIGHT_BRACE");
        RewriteRuleTokenStream stream_LEFT_BRACE=new RewriteRuleTokenStream(adaptor,"token LEFT_BRACE");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_simpleTerm=new RewriteRuleSubtreeStream(adaptor,"rule simpleTerm");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:92:9: ( LEFT_BRACE simpleTerm condition RIGHT_BRACE -> ^( LEFT_BRACE simpleTerm condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:92:11: LEFT_BRACE simpleTerm condition RIGHT_BRACE
            {
            LEFT_BRACE111=(Token)match(input,LEFT_BRACE,FOLLOW_LEFT_BRACE_in_setTerm1445); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_BRACE.add(LEFT_BRACE111);


            pushFollow(FOLLOW_simpleTerm_in_setTerm1447);
            simpleTerm112=simpleTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simpleTerm.add(simpleTerm112.getTree());

            pushFollow(FOLLOW_condition_in_setTerm1449);
            condition113=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition113.getTree());

            RIGHT_BRACE114=(Token)match(input,RIGHT_BRACE,FOLLOW_RIGHT_BRACE_in_setTerm1451); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_BRACE.add(RIGHT_BRACE114);


            // AST REWRITE
            // elements: LEFT_BRACE, condition, simpleTerm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 92:55: -> ^( LEFT_BRACE simpleTerm condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:92:58: ^( LEFT_BRACE simpleTerm condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_LEFT_BRACE.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_simpleTerm.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "setTerm"


    public static class bagTerm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "bagTerm"
    // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:93:1: bagTerm : LEFT_BRACK simpleTerm condition RIGHT_BRACK -> ^( LEFT_BRACK simpleTerm condition ) ;
    public final ConsistencyParser.bagTerm_return bagTerm() throws RecognitionException {
        ConsistencyParser.bagTerm_return retval = new ConsistencyParser.bagTerm_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LEFT_BRACK115=null;
        Token RIGHT_BRACK118=null;
        ConsistencyParser.simpleTerm_return simpleTerm116 =null;

        ConsistencyParser.condition_return condition117 =null;


        CommonTree LEFT_BRACK115_tree=null;
        CommonTree RIGHT_BRACK118_tree=null;
        RewriteRuleTokenStream stream_LEFT_BRACK=new RewriteRuleTokenStream(adaptor,"token LEFT_BRACK");
        RewriteRuleTokenStream stream_RIGHT_BRACK=new RewriteRuleTokenStream(adaptor,"token RIGHT_BRACK");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_simpleTerm=new RewriteRuleSubtreeStream(adaptor,"rule simpleTerm");
        try {
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:93:9: ( LEFT_BRACK simpleTerm condition RIGHT_BRACK -> ^( LEFT_BRACK simpleTerm condition ) )
            // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:93:11: LEFT_BRACK simpleTerm condition RIGHT_BRACK
            {
            LEFT_BRACK115=(Token)match(input,LEFT_BRACK,FOLLOW_LEFT_BRACK_in_bagTerm1468); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_BRACK.add(LEFT_BRACK115);


            pushFollow(FOLLOW_simpleTerm_in_bagTerm1470);
            simpleTerm116=simpleTerm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_simpleTerm.add(simpleTerm116.getTree());

            pushFollow(FOLLOW_condition_in_bagTerm1472);
            condition117=condition();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_condition.add(condition117.getTree());

            RIGHT_BRACK118=(Token)match(input,RIGHT_BRACK,FOLLOW_RIGHT_BRACK_in_bagTerm1474); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_BRACK.add(RIGHT_BRACK118);


            // AST REWRITE
            // elements: condition, simpleTerm, LEFT_BRACK
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 93:55: -> ^( LEFT_BRACK simpleTerm condition )
            {
                // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:93:58: ^( LEFT_BRACK simpleTerm condition )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_LEFT_BRACK.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_simpleTerm.nextTree());

                adaptor.addChild(root_1, stream_condition.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "bagTerm"

    // $ANTLR start synpred5_Consistency
    public final void synpred5_Consistency_fragment() throws RecognitionException {
        // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:61: ( definitionSent )
        // /home/mrglass/workspace/com.ibm.bluej.consistency/Consistency.g:56:61: definitionSent
        {
        pushFollow(FOLLOW_definitionSent_in_synpred5_Consistency816);
        definitionSent();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred5_Consistency

    // Delegated rules

    public final boolean synpred5_Consistency() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_Consistency_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\5\uffff";
    static final String DFA3_eofS =
        "\5\uffff";
    static final String DFA3_minS =
        "\1\35\2\17\2\uffff";
    static final String DFA3_maxS =
        "\1\45\2\47\2\uffff";
    static final String DFA3_acceptS =
        "\3\uffff\1\1\1\2";
    static final String DFA3_specialS =
        "\5\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\1\7\uffff\1\1",
            "\1\4\2\uffff\1\3\3\uffff\2\3\6\uffff\1\4\4\uffff\1\2\3\uffff"+
            "\1\4",
            "\1\4\2\uffff\1\3\3\uffff\2\3\6\uffff\1\4\4\uffff\1\2\3\uffff"+
            "\1\4",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "56:178: ( proposal | emptyProposal )";
        }
    }
 

    public static final BitSet FOLLOW_line_in_logicDesc787 = new BitSet(new long[]{0x0000006037641902L});
    public static final BitSet FOLLOW_NEWLINE_in_line797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_betaSent_in_line801 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factorSent_in_line808 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_definitionSent_in_line816 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_definitionSentEmpty_in_line820 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_declaration_in_line828 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_testSent_in_line835 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_makeConstSent_in_line842 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_proposal_in_line850 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_emptyProposal_in_line854 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectiveSent_in_line862 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_NEWLINE_in_line864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPACE_in_optionalSpace885 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_SPACE_in_conditionSep894 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_CONDITIONSEP_in_conditionSep897 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_SPACE_in_conditionSep899 = new BitSet(new long[]{0x0000000800000002L});
    public static final BitSet FOLLOW_beta_in_betaSent908 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_betaSent910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factorFunc_in_factorSent928 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_factorSent930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBJECTIVEMARKER_in_objectiveSent949 = new BitSet(new long[]{0x0000000812440000L});
    public static final BitSet FOLLOW_optionalSpace_in_objectiveSent951 = new BitSet(new long[]{0x0000000012440000L});
    public static final BitSet FOLLOW_factorSent_in_objectiveSent953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compTerm_in_factorFunc968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_param_in_factorFunc972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_factorFunc976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFMARKER_in_definitionSent984 = new BitSet(new long[]{0x0000000800440000L});
    public static final BitSet FOLLOW_optionalSpace_in_definitionSent986 = new BitSet(new long[]{0x0000000000440000L});
    public static final BitSet FOLLOW_compTerm_in_definitionSent988 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_definitionSent990 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFMARKER_in_definitionSentEmpty1007 = new BitSet(new long[]{0x0000000800440000L});
    public static final BitSet FOLLOW_optionalSpace_in_definitionSentEmpty1009 = new BitSet(new long[]{0x0000000000440000L});
    public static final BitSet FOLLOW_compTerm_in_definitionSentEmpty1011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MAKECONSTMARKER_in_makeConstSent1026 = new BitSet(new long[]{0x0000000800440000L});
    public static final BitSet FOLLOW_optionalSpace_in_makeConstSent1028 = new BitSet(new long[]{0x0000000000440000L});
    public static final BitSet FOLLOW_compTerm_in_makeConstSent1030 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_makeConstSent1032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DECLAREMARKER_in_declaration1049 = new BitSet(new long[]{0x0000000800400000L});
    public static final BitSet FOLLOW_optionalSpace_in_declaration1051 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_NAME_in_declaration1055 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_conditionSep_in_declaration1057 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_LEFT_BRACK_in_declaration1059 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_NAME_in_declaration1063 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RIGHT_BRACK_in_declaration1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TESTMARKER_in_testSent1084 = new BitSet(new long[]{0x0000000800400000L});
    public static final BitSet FOLLOW_optionalSpace_in_testSent1086 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_NAME_in_testSent1088 = new BitSet(new long[]{0x000000100A470000L});
    public static final BitSet FOLLOW_termList_in_testSent1090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_proMarker_in_proposal1107 = new BitSet(new long[]{0x0000000800C40000L});
    public static final BitSet FOLLOW_optionalSpace_in_proposal1109 = new BitSet(new long[]{0x0000000000C40000L});
    public static final BitSet FOLLOW_proChanges_in_proposal1111 = new BitSet(new long[]{0x0000008840008000L});
    public static final BitSet FOLLOW_SPACE_in_proposal1113 = new BitSet(new long[]{0x0000008840008000L});
    public static final BitSet FOLLOW_proCondition_in_proposal1116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_proMarker_in_emptyProposal1134 = new BitSet(new long[]{0x0000008840008000L});
    public static final BitSet FOLLOW_optionalSpace_in_emptyProposal1136 = new BitSet(new long[]{0x0000008040008000L});
    public static final BitSet FOLLOW_proCondition_in_emptyProposal1138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionList_in_proChanges1166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RANDOMCONDITION_in_proCondition1185 = new BitSet(new long[]{0x0000000800C40000L});
    public static final BitSet FOLLOW_WEIGHTEDMAXCONDITION_in_proCondition1191 = new BitSet(new long[]{0x0000000800C40000L});
    public static final BitSet FOLLOW_FORALLCONDITION_in_proCondition1197 = new BitSet(new long[]{0x0000000800C40000L});
    public static final BitSet FOLLOW_SPACE_in_proCondition1200 = new BitSet(new long[]{0x0000000800C40000L});
    public static final BitSet FOLLOW_conditionList_in_proCondition1203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionSep_in_condition1221 = new BitSet(new long[]{0x0000000000C40000L});
    public static final BitSet FOLLOW_conditionList_in_condition1223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_conditionList1240 = new BitSet(new long[]{0x0000000000440000L});
    public static final BitSet FOLLOW_compTerm_in_conditionList1244 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_conditionTail_in_conditionList1247 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_conditionTail1258 = new BitSet(new long[]{0x0000000000C40000L});
    public static final BitSet FOLLOW_conditionList_in_conditionTail1260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PARAM_BEGIN_in_param1273 = new BitSet(new long[]{0x000000100A470000L});
    public static final BitSet FOLLOW_termList_in_param1276 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_RIGHT_BRACE_in_param1278 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_param1283 = new BitSet(new long[]{0x0000001002440000L});
    public static final BitSet FOLLOW_simpleTerm_in_param1285 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_param1287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BETA_BEGIN_in_beta1298 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_NAME_in_beta1301 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_RIGHT_BRACE_in_beta1303 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_beta1306 = new BitSet(new long[]{0x0000001002440000L});
    public static final BitSet FOLLOW_simpleTerm_in_beta1309 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_beta1311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compTermHead_in_compTerm1322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compTermNoHead_in_compTerm1326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_compTermHead1334 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_compTermHead1337 = new BitSet(new long[]{0x000000120A470000L});
    public static final BitSet FOLLOW_termList_in_compTermHead1340 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_compTermHead1343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_compTermNoHead1352 = new BitSet(new long[]{0x000000100A470000L});
    public static final BitSet FOLLOW_termList_in_compTermNoHead1355 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_compTermNoHead1357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_termList1366 = new BitSet(new long[]{0x0000000800000202L});
    public static final BitSet FOLLOW_termTail_in_termList1368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPACE_in_termTail1379 = new BitSet(new long[]{0x0000000800000200L});
    public static final BitSet FOLLOW_COMMA_in_termTail1382 = new BitSet(new long[]{0x000000180A470000L});
    public static final BitSet FOLLOW_SPACE_in_termTail1384 = new BitSet(new long[]{0x000000180A470000L});
    public static final BitSet FOLLOW_SPACE_in_termTail1389 = new BitSet(new long[]{0x000000100A470000L});
    public static final BitSet FOLLOW_termList_in_termTail1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleTerm_in_term1403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_setTerm_in_term1407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bagTerm_in_term1411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPERATOR_in_term1415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_simpleTerm1424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_simpleTerm1428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRINGCONST_in_simpleTerm1432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_compTerm_in_simpleTerm1436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_BRACE_in_setTerm1445 = new BitSet(new long[]{0x0000001002440000L});
    public static final BitSet FOLLOW_simpleTerm_in_setTerm1447 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_setTerm1449 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_RIGHT_BRACE_in_setTerm1451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_BRACK_in_bagTerm1468 = new BitSet(new long[]{0x0000001002440000L});
    public static final BitSet FOLLOW_simpleTerm_in_bagTerm1470 = new BitSet(new long[]{0x0000000800000400L});
    public static final BitSet FOLLOW_condition_in_bagTerm1472 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_RIGHT_BRACK_in_bagTerm1474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_definitionSent_in_synpred5_Consistency816 = new BitSet(new long[]{0x0000000000000002L});

}