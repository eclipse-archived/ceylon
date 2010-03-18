grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
    output=AST;
}

tokens {
	ANNOTATION;
	ANNOTATION_LIST;
	DECLARE;
	MEMBER_DECL;
	TYPE_DECL;
    ABSTRACT_MEMBER_DECL;
    ALIAS_DECL;
    ANNOTATION_NAME;
    ARG_LIST;
    ARG_NAME;
    BREAK_STMT;
    CALL_EXPR;
    CATCH_BLOCK;
    CATCH_STMT;
    CLASS_BODY;
    CLASS_DECL;
    CONDITION;
    DECL_MODIFIER;
    DO_BLOCK;
    DO_ITERATOR;
    DO_ITERATOR;
    EXPR;
    FINALLY_BLOCK;
    IF_FALSE;
    IF_STMT;
    IF_TRUE;
    IMPORT_DECL;
    IMPORT_LIST;
    INIT_EXPR;
    INSTANCE_LIST;
    INTERFACE_DECL;
    MEMBER_NAME;
    MEMBER_TYPE;
    METHOD_DECL;
    NAMED_ARG;
    UNNAMED_ARG;
    NIL;
    RET_STMT;
    STMT_LIST;
    STRING_CST;
    THROW_STMT;
    TRY_BLOCK;
    TRY_CATCH_STMT;
    TRY_RESOURCE_LIST;
    TRY_STMT;
    TYPE_ARG_LIST;
    TYPE_NAME;
    TYPE_PARAMETER_LIST;
    USER_ANNOTATION;
    VAR_DECL;
    WHILE_BLOCK;
    WHILE_STMT;
    SWITCH_STMT;
    SWITCH_EXPR;
    SWITCH_CASE_LIST;
    CASE_ITEM;
    EXPR_LIST;
    CASE_DEFAULT;
    TYPE_CONSTRAINT_LIST;
    TYPE;
    TYPE_ARGS;
    TYPE_CONSTRAINT;
    TYPE_DECL;
    SATISFIES_LIST;
    ABSTRACT_METHOD_DECL;
    SUBSCRIPT_EXPR;
    LOWER_BOUND;
    UPPER_BOUND;
    SELECTOR_LIST;
    TYPE_VARIANCE;
    TYPE_PARAMETER;
    STRING_CONCAT;
    INT_CST;
    FLOAT_CST;
    QUOTE_CST;
    FOR_STMT;
FOR_ITERATOR;
FAIL_BLOCK;
LOOP_BLOCK;
FOR_CONTAINMENT;
REFLECTED_LITERAL;
ENUM_LIST;
SUPERCLASS;
}

compilationUnit
    : importDeclaration*
      (annotations? typeDeclaration)+
      EOF
      ->
      ^(IMPORT_LIST importDeclaration)*
      ^(TYPE_DECL annotations? typeDeclaration)+
     ;
       
typeDeclaration
    : classDeclaration -> ^(CLASS_DECL classDeclaration)
    | interfaceDeclaration -> ^(INTERFACE_DECL interfaceDeclaration)
    | aliasDeclaration -> ^(ALIAS_DECL aliasDeclaration)
    ;

importDeclaration  
    : 'import' importElement ('.' importElement)* ('.' wildcard | alias)? ';'
    -> ^(IMPORT_DECL ^(TYPE_NAME importElement*) wildcard? alias?)
    ;
    
wildcard:	'*'
    ;
    
alias	
    :
    'alias' typeName
    -> ^(ALIAS_DECL typeName)
    ;
    
importElement
    : LIDENTIFIER | UIDENTIFIER
    ;

block
    : '{' declarationOrStatement* directiveStatement? '}'
    -> ^(STMT_LIST declarationOrStatement* directiveStatement?)
    ;

/*inlineClassDeclaration
    : 'new' 
      annotations?
      type
      positionalArguments?
      satisfiedTypes?
      inlineClassBody
    ;*/

inlineClassBody
    : '{' declarationOrStatement* '}'
    ;

typeParameterStart
    : '<'
    ;

//we could eliminate the backtracking by requiring
//all member declarations to begin with a keyword
declarationOrStatement
    : //modifier annotations? ( memberDeclaration | toplevelDeclaration )
    (declarationStart) => declaration
    | statement
    ;

declaration
    :
    ann=annotations? 
    (((memberHeader memberParameters) => 
            (mem=memberDeclaration 
                -> ^(METHOD_DECL $mem $ann?)))
    | (mem=memberDeclaration 
            -> ^(MEMBER_DECL $mem $ann?))
    | (typ=typeDeclaration 
            -> ^(TYPE_DECL $typ $ann?)))
    ;
//special rule for syntactic predicates
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
formalParameterStart
    : '(' (declarationStart | ')')
    ;

//special rule for syntactic predicates
declarationStart
    :  declarationModifier 
    //TODO: type inference: (type|'assign'|'void'|'def')
    | ( userAnnotation annotations? )? (type|'assign'|'void') LIDENTIFIER
    ;

//by making these things keywords, we reduce the amount of
//backtracking
declarationModifier 
    :
    modifier
    -> ^(DECL_MODIFIER modifier)
    ;

modifier
    : 'public'
    | 'package'
    | 'module'
    | 'override'
    | 'optional'
    | 'mutable'
    | 'abstract'
    | 'final'
    | 'static'
    | 'once'
    | 'deprecated'
    | 'volatile'
    | 'extension'
    ;

//Even though it looks like this is non-associative
//assignment, it is actually right associative because
//assignable can be an assignment
statement 
    : expressionStatement
    | controlStructure
    ;

expressionStatement
    : expression ';'!
    ;

directiveStatement
    : directive ';'? -> directive
    ;

directive
    : 'return' assignable? -> ^(RET_STMT assignable?)
    //| 'produce' assignable
    | 'throw' expression? -> ^(THROW_STMT expression?)
    | 'break' expression? -> ^(BREAK_STMT expression?)
    ;


abstractMemberDeclaration
    : ann=annotations?
    (((memberHeader memberParameters) => 
        (memberHeader memberParameters ';'
          -> ^(ABSTRACT_METHOD_DECL $ann? memberHeader memberParameters)))
    | (mem=memberDeclaration 
            -> ^(MEMBER_DECL $mem $ann?)))
    ;


/*
abstractMemberDeclaration
    : annotations?
      memberHeader
      memberParameters?
//      -> ^(ABSTRACT_MEMBER_DECL annotations? memberHeader memberParameters?)
    ;
*/

memberDeclaration
    : memberHeader memberDefinition
    ;

memberHeader
    : memberType memberName
    -> ^(MEMBER_TYPE memberType) memberName
    ;

memberType
    :	
    type | 'void' | 'assign' ;

memberParameters
    : typeParameters? formalParameters+ typeConstraints?
    ;

memberDefinition
    : memberParameters? block
    
    //allow omission of braces:
    /*: ( (memberParameterStart) => memberParameters )? 
      ( ('{') => block | implicationExpression ';' )*/
    | memberInitializer? ';'!
    ;

memberInitializer
    : (specifier | initializer)
    ;

//shortcut functor expression that makes the parameter list 
//optional, but can appear only using the special smalltalk
//style method protocol
undelimitedNamedArgumentDefinition
    : ( (formalParameterStart) => formalParameters )? 
      ( ('{') => block | implicationExpression )
    ;
    
interfaceDeclaration
    :
        'interface'!
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        interfaceBody
 //       ->
 //       typeName typeParameters? satisfiedTypes? typeConstraints? interfaceBody?
    ;

interfaceBody
    //TODO: why can't we have toplevel declarations 
    //      inside an interface dec?
    : '{' abstractMemberDeclaration* '}'
       -> abstractMemberDeclaration*
    ;

abstractMemberDeclarations
    : abstractMemberDeclaration*
    ;


aliasDeclaration
    :
        'alias'!
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        ';'
    ;

classDeclaration
    :
        'class'!
        typeName
        typeParameters?
        formalParameters?
        extendedType?
        satisfiedTypes?
        typeConstraints?
        classBody
    ;

classBody
    : '{' ((instanceStart)=>instances)? declarationOrStatement* '}'
    -> instances? ^(STMT_LIST declarationOrStatement*)
 //    -> ^(CLASS_BODY $inst? ^(STMT_LIST $stmts))
 /*   -> instances? declarationOrStatement*  */
    ;

extendedType
    : 'extends' type positionalArguments?
    -> ^(SUPERCLASS type positionalArguments?) 
    ;
    
instances
    : instance (',' instance)* (';'|'...')
    -> ^(INSTANCE_LIST instance*)
    // FIXME: Need to add ellipsis
    ;

instance
    : annotations? 'case'! memberName arguments?
    ;

//special rule for syntactic predicate
instanceStart
    : annotations? 'case'
    ;

typeConstraint
    : typeName ( ('>='^ type) | ('<='^ type) | ('='^ 'subtype') | formalParameters )
    ;
    
typeConstraints
    //TODO: should it be 'for'?
    : 'where' typeConstraint ('&' typeConstraint)*
    -> ^(TYPE_CONSTRAINT_LIST ^(TYPE_CONSTRAINT typeConstraint)+)
    ;
    
satisfiedTypes
    : 'satisfies' type (',' type)*
    -> ^(SATISFIES_LIST type+)
    ;

type
    : (qualifiedTypeName ( (typeParameterStart) => typeArguments )?
    | 'subtype')
    -> 'subtype'? ^(TYPE qualifiedTypeName ^(TYPE_ARGS typeArguments)?)?
    ;

annotations
    : annotation+ -> ^(ANNOTATION_LIST annotation+)
    ;

annotation
    : declarationModifier | userAnnotation
    ;

//TODO: we could minimize backtracking by limiting the 
//kind of expressions that can appear as arguments to
//the annotation
userAnnotation 
    : 
    annotationName annotationArguments?
    -> ^(USER_ANNOTATION ^(ANNOTATION_NAME annotationName) annotationArguments?)
//    name=annotationName args=annotationArguments?
    ;

annotationArguments
    : arguments | ( literal | reflectedLiteral )+
    ;

reflectedLiteral 
    : '#' ( memberName | (type ( '.' memberName )? ) )
    -> ^(REFLECTED_LITERAL type? memberName)
    ;

qualifiedTypeName
    : //( identifier '.' )* 
    // UIDENTIFIER ('.' UIDENTIFIER)*
    UIDENTIFIER ('.' UIDENTIFIER)*
        ->^(TYPE_NAME UIDENTIFIER+)
    ;

typeName
    : UIDENTIFIER
        ->^(TYPE_NAME UIDENTIFIER)
    ;

annotationName
    : //( identifier '.' )* 
    LIDENTIFIER
    ;

memberName 
    : LIDENTIFIER -> ^(MEMBER_NAME LIDENTIFIER)
    ;

typeArguments
    : '<' type (',' type)* '>'
    -> ^(TYPE_ARG_LIST type+)
    ;

typeParameters
    : '<' typeParameter (',' typeParameter)* '>'
    -> ^(TYPE_PARAMETER_LIST typeParameter+)
    ;

typeParameter
    : variance? typeName -> ^(TYPE_PARAMETER ^(TYPE_VARIANCE variance)? typeName)
    ;

variance
    : 'in' | 'out'
    ;
    
//for locals and attributes
initializer
    : ':=' assignable
    -> ^(INIT_EXPR assignable)
    ;

//for parameters
specifier
    : '=' assignable
    -> ^(INIT_EXPR assignable)
    ;

literal
    : NATURALLITERAL -> ^(INT_CST NATURALLITERAL)
    | FLOATLITERAL -> ^(FLOAT_CST FLOATLITERAL)
    | QUOTEDLITERAL -> ^(QUOTE_CST QUOTEDLITERAL)
    | SIMPLESTRINGLITERAL -> ^(STRING_CST SIMPLESTRINGLITERAL)
    | stringExpr -> ^(STRING_CONCAT stringExpr)
    ;   

stringExpr
    : 
    leftStringLiteral innerStringExpr (middleStringLiteral innerStringExpr)* rightStringLiteral
    ;

innerStringExpr
    :
    expression
    ;

leftStringLiteral
    : LEFTSTRINGLITERAL -> ^(STRING_CST LEFTSTRINGLITERAL)
    ;
    
middleStringLiteral
    : MIDDLESTRINGLITERAL -> ^(STRING_CST MIDDLESTRINGLITERAL)
    ;
    
rightStringLiteral
    : RIGHTSTRINGLITERAL -> ^(STRING_CST RIGHTSTRINGLITERAL)
    ;
    
assignable 
    : reflectedLiteral
    | expression
    ;

expression
    : expr
    -> ^(EXPR expr)
    ;

//Even though it looks like this is non-associative
//assignment, it is actually right associative because
//assignable can be an assignment
//Note that = is not really an assignment operator, but 
//can be used to init locals
expr
    : methodExpression 
      ( op=('='^ | ':='^ | '.='^ | '+='^ | '-='^ | '*='^ | '/='^ | '%='^ | '&='^ | '|='^ | '^='^ | '&&='^ | '||='^ | '?='^) assignable )?
    ;

methodExpression
    : implicationExpression undelimitedNamedArgument*
    ;

undelimitedNamedArgument
    : ( memberName | 'case' '(' expressions ')' ) 
      undelimitedNamedArgumentDefinition 
    ;

implicationExpression
    : disjunctionExpression 
      ('=>'^ disjunctionExpression)?
    ;

//should '^' have a higher precedence?
disjunctionExpression
    : conjunctionExpression 
      ('||'^ conjunctionExpression)?
    ;

conjunctionExpression
    : logicalNegationExpression 
      ('&&'^ logicalNegationExpression)*
    ;

logicalNegationExpression
    : '!'^ logicalNegationExpression
    | equalityExpression
    ;

equalityExpression
    : comparisonExpression
      (('=='^|'!='^|'==='^) comparisonExpression)?
    ;

comparisonExpression
    : defaultExpression
      (('<=>'^ |'<'^ |'>'^ |'<='^ |'>='^ |'in'^ |'is'^) defaultExpression)?
    ;

//should we reverse the precedence order 
//of '?' and 'exists'/'nonempty'?
defaultExpression
    : existenceEmptinessExpression 
      ('?' defaultExpression)?
    ;

existenceEmptinessExpression
    : rangeIntervalEntryExpression ('exists' | 'nonempty')?
    ;

//I wonder if it would it be cleaner to give 
//'..' a higher precedence than '->'
rangeIntervalEntryExpression
    : additiveExpression
      (('..'^ |'->') additiveExpression)?
    ;

additiveExpression
    : multiplicativeExpression
      (('+'^ | '-'^ | '|'^ | '^'^) multiplicativeExpression)*
    ;

multiplicativeExpression 
    : exponentiationExpression
      (('*'^ | '/'^ | '%'^ | '&'^) exponentiationExpression)*
    ;

exponentiationExpression
    : unaryExpression ('**'^ unaryExpression)?
    ;

unaryExpression 
    : ('$'^ |'-'^ |'++'^ |'--'^) unaryExpression
    | primary
    ;

primary
/*
    : b=base 
    ((selector+
     -> ^(SELECTOR_LIST $b selector+))
    | -> $b
    )
*/
// This syntactic predicate really shouldn't be necessary, and the ANTLR
// book seems to agree, but the above doesn't work.
    : ((base selector) => base selector+ -> ^(SELECTOR_LIST base selector+))
    | base
    ;



base 
    : type
    | memberName
    | literal
    | parExpression
    | enumeration
    | specialValue
    //| inlineClassDeclaration
    ;
    

specialValue
    : 'this' 
    | 'super' 
    | 'null'
    | 'none'
    ;

enumeration
    : '{' assignables? '}'
    -> ^(ENUM_LIST assignables?)
    //a special List literal syntax?
    //| '[' assignables? ']' 
    ;

selector 
    : memberInvocation
    | (arguments -> ^(CALL_EXPR ^(ARG_LIST arguments))) 
    | elementSelector
    | ('--' | '++')
    ;

memberInvocation
    : ('.'^ | '^.'^ | '?.'^ | '*.'^) memberName
    ;

/*parameterTypes
    : '(' ( type (',' type)* )? ')'
    ;*/

elementSelector
    : '[' elementsSpec ']'
    -> ^(SUBSCRIPT_EXPR elementsSpec)
    ;

elementsSpec
    : (lo=additiveExpression ( '...' | '..' hi=additiveExpression )?
    |  '...' hi=additiveExpression)
    -> ^(LOWER_BOUND $lo)? ^(UPPER_BOUND $hi)?	
    ;

arguments 
    : positionalArguments | namedArguments
    ;
    
namedArgument
    : 'assign'? parameterName memberDefinition
    ;
    
parameterName
    : LIDENTIFIER -> ^(ARG_NAME LIDENTIFIER)
    ;

namedArguments
    : '{' ((namedArgument) => namedArgument)* varargArguments? '}'
    -> ^(NAMED_ARG namedArgument)* varargArguments?
    ;

varargArguments
    : unnamedArg (','! unnamedArg)*
    ;

unnamedArg
    : assignable
    -> ^(UNNAMED_ARG assignable)
    ;

assignables
    : assignable (','! assignable)*
    ;

parExpression 
    : '(' assignable ')'
    ;
    
positionalArguments
    : '(' positionalArgumentList ')'
   -> ^(ARG_LIST positionalArgumentList?)
    ;

positionalArgumentList
    :  
    positionalArgument (',' positionalArgument)* -> positionalArgument+
    | 
    ;

positionalArgument
    : (variableStart) => special 
    | assignable
    ;

special
    : type memberName (containment|specifier)
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    -> ^(ARG_LIST formalParameter*)
    ;

// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
formalParameter
    : annotations?
      memberHeader
      memberParameters? 
      ( '->' type parameterName | '..' parameterName )? 
      (specifier | '...')?
    ;

// Control structures.

condition
    : expression | existsCondition | isCondition
    ;

// Backtracking here is needed for exactly the same reason as localOrStatement.
existsCondition
    : ('exists' | 'nonempty') ( (variableStart) => variable specifier | expression )
    ;
    
isCondition
    : 'is' type ( (memberName '=') => memberName specifier | expression )
    ;
    
controlStructure
    : ifElse | switchCaseElse | doWhile | forFail | tryCatchFinally
    ;
    
ifElse
    : 	
    'if' '(' condition ')' ifBlock=block 
      ('else' (ifElse | elseBlock=block))?
    -> ^(IF_STMT ^(CONDITION condition) ^(IF_TRUE $ifBlock) ^(IF_FALSE $elseBlock? ifElse?)?)
    ;

switchCaseElse
    : 'switch' '(' expression ')' ( '{' cases '}' | cases )
    -> ^(SWITCH_STMT ^(SWITCH_EXPR expression) ^(SWITCH_CASE_LIST cases))
    ;
    
cases 
    : caseItem+ ('else' block)?
    -> caseItem+ ^(CASE_DEFAULT  block)?
    ;
    
caseItem
    : ('case' '(' caseCondition ')' block)
    -> ^(CASE_ITEM caseCondition block)
    ;	 
    
caseCondition
    : expressions | isCaseCondition
    ;

expressions
    : expression (',' expression)*
    -> ^(EXPR_LIST expression+)
    ;

isCaseCondition
    : 'is' type
    ;

forFail
    : 'for' '(' forIterator ')' loopBlock=block ('fail' failBlock=block)?
    -> ^(FOR_STMT forIterator ^(LOOP_BLOCK $loopBlock) ^(FAIL_BLOCK $failBlock)?)
    ;

forIterator
    : v1=variable ('->' v2=variable)? containment
    -> ^(FOR_ITERATOR $v1 $v2? containment)
    ;
    
containment
    : 'in' expression
    -> ^(FOR_CONTAINMENT expression)
    ;
    
doWhile
    :   
    ('do' ('(' doIterator ')')? b1=block? )?  
    'while' '(' condition ')' (b2=block | ';')
     -> ^(WHILE_STMT ^(CONDITION condition) ^(DO_ITERATOR doIterator)? ^(DO_BLOCK $b1)?
         ^(WHILE_BLOCK $b2)?)
    ;

//do iterators are allowed to be mutable and/or optional
doIterator
    : annotations? variable (specifier | initializer)
    ;

tryCatchFinally
    :
    tryStmt
    catchStmts
    finallyStmt
    -> ^(TRY_CATCH_STMT tryStmt catchStmts? finallyStmt?)
    ;
  
tryStmt
    :
    'try' ( '(' resource (',' resource)* ')' )? block
    -> ^(TRY_STMT ^(TRY_RESOURCE_LIST resource)? ^(TRY_BLOCK block))
    ;
  
catchStmts
    :
    ('catch' '(' variable ')' block)*
    -> ^(CATCH_STMT variable ^(CATCH_BLOCK block))*
    ;
    
finallyStmt
    :	
    ('finally' block)?
    -> ^(FINALLY_BLOCK block)?
    ;    	    

resource
    : (variableStart) => variable specifier 
    | expression
    ;

variable
    : type memberName
    -> ^(VAR_DECL type memberName)
    ;

variableStart
    : type memberName ('in'|'=')
    ;

// Lexer

NATURALLITERAL
    : Digit Digit*
    ;

fragment
Digit 
    : '0'..'9'
    ;

FLOATLITERAL
    :   ('0' .. '9')+ '.' ('0' .. '9')+ Exponent?  
    |   '.' ( '0' .. '9' )+ Exponent?  
    |   ('0' .. '9')+ Exponent  
    ;

fragment 
Exponent    
    :   ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;

QUOTEDLITERAL
    :   '\''
        StringPart
        '\''
    ;

SIMPLESTRINGLITERAL
    :   '"'
        StringPart
        '"'
    ;

LEFTSTRINGLITERAL
    :   '"'
        StringPart
        '${'
    ;

RIGHTSTRINGLITERAL
    :   '}$'
        StringPart
        '"'
    ;

MIDDLESTRINGLITERAL
    :   '}$'
        StringPart
        '${'
    ;

fragment
NonStringChars
    :    '{' | '\\' | '"' | '$' | '\''
    ;

fragment
StringPart
    :    ( ~ NonStringChars | EscapeSequence) *
    ;
    
fragment
EscapeSequence 
    :   '\\' (
            'b' 
        |   't' 
        |   'n' 
        |   'f' 
        |   'r' 
        |   '\"' 
        |   '\''
        |   '$'
        |   '{'
        |   '}' 
        )          
    ;

WS  
    :   (
            ' '
        |    '\r'
        |    '\t'
        |    '\u000C'
        |    '\n'
        ) 
        {
            skip();
        }          
    ;

LINE_COMMENT
    :   '//' ~('\n'|'\r')*  ('\r\n' | '\r' | '\n') 
        {
            skip();
        }
    |   '//' ~('\n'|'\r')*
        {
            skip();
        }
    ;   

MULTI_COMMENT
        :       '/*'
                {
                        $channel=HIDDEN;
                }
                (       ~('/'|'*')
                        |       ('/' ~'*') => '/'
                        |       ('*' ~'/') => '*'
                        |       MULTI_COMMENT
                )*
                '*/'
        ;


ASSIGN
    :   'assign'
    ;
    
BREAK
    :   'break'
    ;
    
CASE
    :   'case'
    ;

CATCH
    :   'catch'
    ;

CLASS
    :   'class'
    ;

DECORATOR
    :   'decorator'
    ;

DO
    :   'do'
    ;
    
ELSE
    :   'else'
    ;            

EXISTS
    :   'exists'
    ;

FINALLY
    :   'finally'
    ;

FOR
    :   'for'
    ;
    
IF
    :   'if'
    ;

SATISFIES
    :   'satisfies'
    ;

IMPORT
    :   'import'
    ;

INTERFACE
    :   'interface'
    ;
    
NONE
    :   'none'
    ;

NULL
    :   'null'
    ;

NONEMPTY
    :   'nonempty'
    ;

/*PRODUCE
    :   'produce'
    ;*/

RETURN
    :   'return'
    ;

SUPER
    :   'super'
    ;

SWITCH
    :   'switch'
    ;

THIS
    :   'this'
    ;

SUBTYPE
    :   'subtype'
    ;

THROW
    :   'throw'
    ;

TRY
    :   'try'
    ;

VOID
    :   'void'
    ;

WHILE
    :   'while'
    ;

LPAREN
    :   '('
    ;

RPAREN
    :   ')'
    ;

LBRACE
    :   '{'
    ;

RBRACE
    :   '}'
    ;

LBRACKET
    :   '['
    ;

RBRACKET
    :   ']'
    ;

SEMI
    :   ';'
    ;

COMMA
    :   ','
    ;

DOT
    :   '.'
    ;

ELLIPSIS
    :   '...'
    ;

EQ
    :   '='
    ;

BANG
    :   '!'
    ;

TILDE
    :   '~'
    ;

QUES
    :   '?'
    ;

COLON
    :   ':'
    ;
    
COLONEQ
    :   ':='
    ;

EQEQ
    :   '=='
    ;

AMPAMP
    :   '&&'
    ;

BARBAR
    :   '||'
    ;

PLUSPLUS
    :   '++'
    ;

SUBSUB
    :   '--'
    ;

PLUS
    :   '+'
    ;

SUB
    :   '-'
    ;

STAR
    :   '*'
    ;

SLASH
    :   '/'
    ;

AMP
    :   '&'
    ;

BAR
    :   '|'
    ;

CARET
    :   '^'
    ;

PERCENT
    :   '%'
    ;

AT
    :   '@'
    ;

BANGEQ
    :   '!='
    ;

GT
    :   '>'
    ;

LT
    :   '<'
    ;        

ENTRY
    :   '->'
    ;

RANGE
    :   '..'
    ;
    
COMPARE
    :   '<=>'
    ;
    
IN
    :   'in'
    ;

IS
    :   'is'
    ;

HASH
    :   '#'
    ;

PLUSEQ
    :   '+='
    ;
    
CONVERTER
    :  'converter'
    ;
    
LIDENTIFIER 
    :   LIdentifierPart IdentifierPart*
    ;

UIDENTIFIER 
    :   UIdentifierPart IdentifierPart*
    ;

// FIXME: Unicode identifiers
fragment
LIdentifierPart
    :   '_'
    |   'a'..'z'
    ;       
                       
// FIXME: Unicode identifiers
fragment
UIdentifierPart
    :   'A'..'Z'
    ;       
                       
fragment 
IdentifierPart
    :   LIdentifierPart 
    |   UIdentifierPart
    |   '0'..'9'
    ;
