grammar Consistency;


//java -cp ~/workspace/com.ibm.bluej.consistency/lib/antlr-3.4-complete.jar org.antlr.Tool -o ~/workspace/com.ibm.bluej.consistency/src/com/ibm/bluej/consistency/grammar ~/workspace/com.ibm.bluej.consistency/src/com/ibm/bluej/consistency/grammar/Consistency.g


options {
     backtrack = true;
    output=AST;
    ASTLabelType=CommonTree; // type of $stat.tree ref etc...
}


tokens {
	DEFMARKER                    = 'DEF>' ;
	MAKECONSTMARKER              = 'MAKECONST>' ;
	DECLAREMARKER                = 'DECLARE>' ;
	PROPOSEMARKER                = 'PROPOSE>' ;
	SUBPROPOSEMARKER             = 'SUBPROPOSE>' ;
	OBJECTIVEMARKER              = 'OBJECTIVE>' ;
	TESTMARKER                   = 'TEST>' ;
	PARAM_BEGIN                  = 'T_{' ;
	BETA_BEGIN                   = 'B_{' ;
	CONDITIONSEP                 = ':' ;
	RANDOMCONDITION              = 'RANDOM:' ;
	WEIGHTEDMAXCONDITION         = 'WEIGHTEDMAX:' ;
	FORALLCONDITION              = 'FORALL:' ;
	COMMA                        = ',' ;
	SPACE                        = ' ' ;
	LEFT_PAREN                   = '(' ;
	RIGHT_PAREN                  = ')' ;
	LEFT_BRACE                   = '{' ;
	RIGHT_BRACE                  = '}' ;
	LEFT_BRACK                   = '[' ;
	RIGHT_BRACK                  = ']' ;
	AND                          = ' ^ ' ;
	NEGATION                     = '!' ;
	AT                           = '@' ;
	BACKSLASH                    = '\\';
	FACTORFUNC                   = 'FACTORFUNC>' ;
	BETAFUNC                     = 'BETAFUNC>' ;
}

@parser::header { 
package com.ibm.bluej.consistency.grammar; 
}
@lexer::header { 
package com.ibm.bluej.consistency.grammar; 
}

// PARSER RULES
//prog : ( logicDesc {System.out.println($logicDesc.tree.toStringTree());} )+ ;

logicDesc : line+ ;

line : NEWLINE | betaSent NEWLINE! | factorSent NEWLINE! | (definitionSent | definitionSentEmpty) NEWLINE! | declaration NEWLINE! | testSent NEWLINE! | makeConstSent NEWLINE! | (proposal | emptyProposal) NEWLINE! | objectiveSent NEWLINE! ;
endLine : NEWLINE | EOF ;
optionalSpace : SPACE* ;
conditionSep : SPACE* CONDITIONSEP SPACE* ;
betaSent : beta condition -> ^(BETAFUNC beta condition) ;
factorSent : factorFunc condition  -> ^(FACTORFUNC factorFunc condition) ;
objectiveSent : OBJECTIVEMARKER optionalSpace factorSent -> ^(OBJECTIVEMARKER factorSent);
factorFunc : compTerm | param | NUMBER ;
definitionSent : DEFMARKER optionalSpace compTerm condition -> ^(DEFMARKER compTerm condition);
definitionSentEmpty : DEFMARKER optionalSpace compTerm -> ^(DEFMARKER compTerm);
makeConstSent : MAKECONSTMARKER optionalSpace compTerm condition -> ^(MAKECONSTMARKER compTerm condition);
declaration : DECLAREMARKER optionalSpace fname=NAME conditionSep LEFT_BRACK jname=NAME RIGHT_BRACK -> ^(DECLAREMARKER $fname $jname);
testSent : TESTMARKER optionalSpace NAME termList -> ^(TESTMARKER NAME termList);
proposal : proMarker optionalSpace proChanges SPACE* proCondition -> ^(proMarker proChanges proCondition) ;
emptyProposal : proMarker optionalSpace proCondition -> ^(proMarker proCondition) ;
proMarker : PROPOSEMARKER | SUBPROPOSEMARKER ;
proChanges : conditionList -> ^(CONDITIONSEP conditionList) ;
proCondition : (proSep=RANDOMCONDITION | proSep=WEIGHTEDMAXCONDITION | proSep=FORALLCONDITION) SPACE* conditionList -> ^($proSep conditionList) ;

condition : conditionSep conditionList -> ^(CONDITIONSEP conditionList) ;
conditionList : (NEGATION)? compTerm (conditionTail)? ; 
conditionTail : AND conditionList -> conditionList ;

param : PARAM_BEGIN^ termList RIGHT_BRACE! ( LEFT_PAREN simpleTerm RIGHT_PAREN! )?;
beta : BETA_BEGIN^ NAME RIGHT_BRACE! LEFT_PAREN! simpleTerm RIGHT_PAREN! ;

//termlist optional to support zero arg functions
compTerm : compTermHead | compTermNoHead ;
compTermHead : NAME^ LEFT_PAREN! termList? RIGHT_PAREN! ;
compTermNoHead : LEFT_PAREN^ termList RIGHT_PAREN! ;
termList : term termTail? ;
//make COMMA optional for (i = j)
termTail : (SPACE* COMMA SPACE* | SPACE) termList -> termList;
term : simpleTerm | setTerm | bagTerm | OPERATOR ; 
simpleTerm : NAME | NUMBER | STRINGCONST | compTerm ;

setTerm : LEFT_BRACE simpleTerm condition RIGHT_BRACE -> ^(LEFT_BRACE simpleTerm condition);
bagTerm : LEFT_BRACK simpleTerm condition RIGHT_BRACK -> ^(LEFT_BRACK simpleTerm condition) ;



// LEXER RULES

NAME : (LETTER | '_' | BACKSLASH) (LETTER | DIGIT | '_' | '$' | '@' | '\.' )* ;
 
NUMBER  : SIGN? (DIGIT)+ ('\.' (DIGIT)+)? (('e' | 'E')(SIGN)?(DIGIT)+)?;

STRINGCONST : '"' .* '"' ;

OPERATOR : '!=' | '=' | '<=' | '>=' | '<' | '>' | '+' | '-' | '*' | '/' ;

NEWLINE : '\n' | '\r\n'  ;

//WHITESPACE : ( '\t' | ' ' | '\u000C' )+    { $channel = HIDDEN; } ;

LINE_COMMENT : '//' (~'\n')* { $channel = HIDDEN; };


fragment LETTER : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT  : '0'..'9' ;
fragment SIGN : '+' | '-' ;



