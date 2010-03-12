grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
    output=AST;
}

tokens {
	DECLARE;
	MEMBER_DECL;
	TYPE_DECL;
	ANNOTATION;
    USER_ANNOTATION;
	ANNOTATION_LIST;
    TYPE_NAME;
    MEMBER_NAME;
    MEMBER_TYPE;
    INIT_EXPR;
    CLASS_DECL;
    IMPORT_LIST;
    IMPORT_DECL;
    ALIAS_DECL;
    STMT_LIST;
    RET_STMT;
    ARG_LIST;
    METHOD_DECL;
    INSTANCE_LIST;
    CLASS_BODY;
    EXPR;
    INTERFACE_DECL;
    ABSTRACT_MEMBER_DECL;
    ANNOTATION_NAME;
    STRING_CST;
    CALL_EXPR;
    TYPE_PARAMETER_LIST;
    TYPE_ARG_LIST;
    DECL_MODIFIER;
    THROW_STMT;
    BREAK_STMT;
    NAMED_ARG;
    NIL;
    ARG_NAME;
    TRY_STMT;
    TRY_RESOURCE_LIST;
    TRY_BLOCK;
    CATCH_STMT;
    CATCH_BLOCK;
    FINALLY_BLOCK;
    TRY_CATCH_STMT;
    VAR_DECL;
}

compilationUnit
    : importDeclaration*
      (annotations? typeDeclaration)+
      EOF
      ->
      ^(IMPORT_LIST importDeclaration*)?
      (annotations? typeDeclaration)+
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
    : expression ';'
    -> ^(EXPR expression)
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
    : annotations?
      memberHeader
      memberParameters?
      -> ^(ABSTRACT_MEMBER_DECL annotations? memberHeader memberParameters?)
    ;

memberDeclaration
    : memberHeader memberDefinition
    ;

memberHeader
    : t=memberType name=memberName
        -> ^(MEMBER_TYPE $t $name)
    ;

memberType
    :	
    type | 'void' | 'assign' ;

memberParameters
    : typeParameters? formalParameters+ typeConstraints?
    ;

memberDefinition
    : (memberParameters? block)
    //allow omission of braces:
    /*: ( (memberParameterStart) => memberParameters )? 
      ( ('{') => block | implicationExpression ';' )*/
    | (init=memberInitializer ';'
    	-> $init)
    ;

memberInitializer
    : (specifier | initializer)?
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
        'interface'
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        interfaceBody
        ->
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        interfaceBody        
    ;

interfaceBody
    //TODO: why can't we have toplevel declarations 
    //      inside an interface dec?
    : '{' ( mem=abstractMemberDeclaration ';' )* '}'
       -> $mem*
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
        'class'
        name=typeName
        typeParms=typeParameters?
        args=formalParameters?
        ext=extendedType?
        sat=satisfiedTypes?
        constr=typeConstraints?
        body=classBody
     -> $name $typeParms? $args? $ext? $sat? $constr? $body
    ;

classBody
    : '{' ((instanceStart)=>instances)? declarationOrStatement* '}'
    -> instances? ^(STMT_LIST declarationOrStatement*)
 //    -> ^(CLASS_BODY $inst? ^(STMT_LIST $stmts))
 /*   -> instances? declarationOrStatement*  */
    ;

extendedType
    : 'extends' type positionalArguments
    ;
    
instances
    : instance (',' instance)* (';'|'...')
    -> ^(INSTANCE_LIST instance*)
    // FIXME: Need to add ellipsis
    ;

instance
    : annotations? 'case' memberName arguments?
    ;

//special rule for syntactic predicate
instanceStart
    : annotations? 'case'
    ;

typeConstraint
    : typeName ( ('>=' | '<=') type | '=' 'subtype' | formalParameters )
    ;
    
typeConstraints
    //TODO: should it be 'for'?
    : 'where' typeConstraint ('&' typeConstraint)*
    ;
    
satisfiedTypes
    : 'satisfies' type (',' type)*
    ;

type
    : qualifiedTypeName ( (typeParameterStart) => typeArguments )?
    | 'subtype'
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
    ;

qualifiedTypeName
    : //( identifier '.' )* 
    // UIDENTIFIER ('.' UIDENTIFIER)*
    UIDENTIFIER ('.' UIDENTIFIER)*
        ->^(TYPE_NAME UIDENTIFIER+)
    ;

typeName
    : UIDENTIFIER
        ->^(TYPE_NAME UIDENTIFIER+)
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
    : variance typeName
    ;

variance 
    : ('in'|'out')?
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
    : NATURALLITERAL
    | FLOATLITERAL
    | QUOTEDLITERAL
    | stringLiteral -> ^(STRING_CST stringLiteral)
    ;   

stringLiteral
    : SIMPLESTRINGLITERAL
    | LEFTSTRINGLITERAL expression (MIDDLESTRINGLITERAL expression)* RIGHTSTRINGLITERAL 
    ;

assignable 
    : reflectedLiteral
    | expression
    ;

//Even though it looks like this is non-associative
//assignment, it is actually right associative because
//assignable can be an assignment
//Note that = is not really an assignment operator, but 
//can be used to init locals
expression 
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
      (('<=>'^ |'<'^ |'>'^ |'<='^ |'>='^ |'in'^ |'is') defaultExpression)?
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
    : unaryExpression ('**' unaryExpression)?
    ;

unaryExpression 
    : ('$'^ |'-'^ |'++'^ |'--'^) unaryExpression
    | primary
    ;

primary
    : b=base 
    (s=selector+
     -> ^(CALL_EXPR $b $s+)
    | -> $b
    )
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
    //a special List literal syntax?
    //| '[' assignables? ']' 
    ;

selector 
    : memberInvocation
    | arguments
    | elementSelector
    | ('--' | '++')
    ;

memberInvocation
    : ('.' | '^.' | '?.' | '*.') memberName
    ;

/*parameterTypes
    : '(' ( type (',' type)* )? ')'
    ;*/

elementSelector
    : '[' elementsSpec ']'
    ;

elementsSpec
    : additiveExpression ( '...' | '..' additiveExpression )?
    |  '...' additiveExpression	
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
    -> ^(NAMED_ARG namedArgument)* ^(ARG_LIST varargArguments)?
    ;

varargArguments
    : assignables
    ;

assignables
    : assignable (',' assignable)*
    ;

parExpression 
    : '(' assignable ')'
    ;
    
positionalArguments
    : '(' positionalArgumentList ')'
   -> ^(ARG_LIST positionalArgumentList)
    ;

positionalArgumentList
    :  
    positionalArgument (',' positionalArgument)* -> positionalArgument+
    | -> NIL
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
    :  abstractMemberDeclaration 
      //annotations? type parameterName 
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
    : 'if' '(' condition ')' block ('else' 'if' '(' condition ')' block)* ('else' block)?
    ;
    
switchCaseElse
    : 'switch' '(' expression ')' ( '{' cases '}' | cases )
    ;
    
cases 
    : ('case' '(' caseCondition ')' block)+ ('else' block)?
    ;
    
caseCondition
    : expressions | isCaseCondition
    ;

expressions
    : expression (',' expression)*
    ;

isCaseCondition
    : 'is' type
    ;

forFail
    : 'for' '(' forIterator ')' block ('fail' block)?
    ;

forIterator
    : variable ('->' variable)? containment
    ;
    
containment
    : 'in' expression
    ;
    
doWhile
    :   
    ('do' ('(' doIterator ')')? block? )?  
    'while' '(' condition ')' (block | ';')
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
    :    '{' | '\\' | '"'
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
