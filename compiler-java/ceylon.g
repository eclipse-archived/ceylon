grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
    //k=4;
}

compilationUnit
    : 
        (importDeclaration)*
        (annotation* toplevelDeclaration)+
    ;
    
toplevelDeclaration
    : classDeclaration 
    | interfaceDeclaration 
    | converterDeclaration 
    | decoratorDeclaration
    | aliasDeclaration
    ;

importDeclaration  
    : 'import' identifier ('.' identifier)* ('.' '*')? ';'
    ;

block
    :  '{' localOrStatement* directiveStatement? '}'
    ;

//we could eliminate the backtracking by requiring
//annotations on local  declarations to be keywords
localOrStatement
    : //'local' local
      (declarationStart) => annotation* localDeclaration ';'
    | statement
    ;

localDeclaration
    :  type LIDENTIFIER initializer?
    ;

inlineClassDeclaration
    :   
    'new' annotation*
    UIDENTIFIER ('.' UIDENTIFIER*)
    arguments
    '{' memberOrStatement* '}'
    ;
    
//we could eliminate the backtracking by requiring
//all member declarations to begin with a keyword
memberOrStatement
    :  //modifier member
       (declarationStart) => annotation* ( memberDeclaration | toplevelDeclaration )
    |  statement
    ;

//a normal functor expression
functor 
    : formalParameters functorBody
    ;

//shortcut functor expression that makes the parameter list 
//optional
specialFunctor 
    : ( (formalParameterStart) => formalParameters )? functorBody
    ;

//special rule for syntactic predicates
declarationStart
    :  annotation* (type|'assign'|'void') LIDENTIFIER
    ;

//special rule for syntactic predicates
formalParameterStart
    :  '(' (declarationStart | ')')
    ;

//we can support enumerations as functor bodies, but
//I think that's just confusing to the reader
functorBody
    : simpleExpression 
    //| (enumeration)=>enumeration
    //| statement
    | block
    ;

//Let's limit statements to things that make sense, like
//Java does (we could allow any arbitrary expression if
//we wanted to, but why?)
//FIXME: this rule is a bit of a mess
statement 
    :  //assignable ';'
    (
       ( '++' | '--' )? 
       primary 
       ( '++' | '--' | assignmentOp assignable | specialFunctorArguments)? ';'
    )
    | controlStructure
    ;

directiveStatement
    :  directive ';'?
    ;

directive
    : 'return' assignable 
    | 'produce' assignable
    | 'throw' assignable 
    | 'break' 
    | 'found'
    ;

// This is quite different from the grammar in the spec, but because
// attributes and methods are syntactically very similar it makes
// sense to recognize them in this way.  An attribute is a method with
// no formalParameters.
// FIXME: Allows "void foo" as an attribute declaration
memberDeclaration
    : ('assign' | type | 'void') LIDENTIFIER ( methodDefinition | attributeDefinition )
    ;
    
methodDefinition
    : typeParameters? formalParameters typeConstraints? (block | ';')
    ;
    
attributeDefinition
    : block | initializer? ';'
    ;

decoratorDeclaration
    :
        'decorator'
        UIDENTIFIER
        typeParameters?
        formalParameters?
        satisfiedTypes?
        typeConstraints?
        '{' ( annotation* memberDeclaration )* '}'
    ;

converterDeclaration
    :   
        'converter'
        type
        UIDENTIFIER
        typeParameters?
        typeConstraints?
        '(' annotation* type LIDENTIFIER ')'
    ;

interfaceDeclaration
    :
        'interface'
        UIDENTIFIER
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        '{' memberStub* '}'
    ;

aliasDeclaration
    :
        'alias'
        UIDENTIFIER
        typeParameters?
        typeConstraints?
        ';'
    ;

memberStub
    :
        annotation*
        (type | 'void')
        LIDENTIFIER
        (typeParameters?
         formalParameters
         typeConstraints?)?
        ';'
        ;

classDeclaration
    :
        'class'
        UIDENTIFIER
        typeParameters?
        formalParameters?
        extendedType?
        satisfiedTypes?
        typeConstraints?
        '{' instances? memberOrStatement* '}'
    ;

extendedType
    : 'extends' typeName typeParameters? arguments
    ;
    
instances
    : 'instances' instance (',' instance)* ';'
    ;

instance 
    : /*annotation**/ LIDENTIFIER arguments?
    ;

typeConstraint
    :   UIDENTIFIER ((('>=' | '<=') type )| formalParameters)
    ;
    
typeConstraints
    : 'where' typeConstraint ('&' typeConstraint)*
    ;
    
satisfiedTypes
    : 'satisfies' type (',' type)*
    ;

type
    :  regularType 
    |  functorType
    ;

regularType
    :   typeName typeParameters?
    ;

functorType
    :   'F' '<' formalParameters ',' (type|'void') '>'
    ;

annotation 
    : annotationName ( arguments | literal )?
    ;


typeName
    : //( identifier '.' )* 
    UIDENTIFIER
    ;

annotationName
    : //( identifier '.' )* 
    LIDENTIFIER
    ;

identifier 
    : LIDENTIFIER
    | UIDENTIFIER
    ;

typeParameters
    : '<' type (',' type)* '>'
    ;

initializer
    : ('=' | ':=') assignable
    ;

literal
    : enumerationLiteral
    | integerLiteral
    | FLOATLITERAL
    | CHARLITERAL
    | stringLiteral
    | dateLiteral
    | typeLiteral
    | regexLiteral
    ;   

//FIXME: member literals are a problem!
typeLiteral
    : '#' typeName
    ;

dateLiteral
    : DATELITERAL 
    | TIMELITERAL
    ;

integerLiteral
    : INTLITERAL
    ;

enumerationLiteral
    : 'none'
    ;

stringLiteral
    : SIMPLESTRINGLITERAL
       | LEFTSTRINGLITERAL expression RIGHTSTRINGLITERAL 
    ;

regexLiteral
    : '`' (~ '`' | '\\`')* '`'
    ;

//This one is fully general
assignable 
    : //'~' specialFunctor
    (formalParameterStart) => functor 
    | enumeration
    | expression
    ;

//This one is for use as a functor body
simpleAssignable 
    : enumeration   
    | simpleExpression
    ;

//This one is fully general
//should we reall allow assigments here???
expression
    : inlineClassDeclaration
    | implicationExpression (assignmentOp assignable | specialFunctorArguments)?
        /*    | '{' expression ';' (expression ';')* '}' */
    ;

//This one is for use as a functor body
//should we reall allow assigments here???
simpleExpression
    : implicationExpression (assignmentOp simpleAssignable)?
        /*    | '{' expression ';' (expression ';')* '}' */
    ;
    
assignmentOp
    : ':=' 
    | '+=' 
    | '-=' 
    | '*=' 
    | '/=' 
    | '%=' 
    | '&=' 
    | '|=' 
    | '^=' 
    | '&&=' 
    | '||=' 
    | '?='
    ;

implicationExpression
    :
        disjunctionExpression ('=>' disjunctionExpression)?
    ;

disjunctionExpression
    :
        conjunctionExpression (('||' | '|' '^') conjunctionExpression)?
    ;

conjunctionExpression
    :
        logicalNegationExpression (('&&' | '&') logicalNegationExpression)*
    ;

logicalNegationExpression
    :
        '!' logicalNegationExpression
    |
        equalityExpression
    ;

equalityExpression
    :
        comparisonExpression
        (('=='|'!='|'===') comparisonExpression)?
    ;

comparisonExpression
    :
        defaultExpression
        (('<=>'|'<'|'>'|'<='|'>='|'in') defaultExpression)?
    ;

defaultExpression
    :   
        existenceEmptinessExpression ('?' defaultExpression)?
    ;

existenceEmptinessExpression
    :
        dateCompositionExpression ('exists' | 'nonempty')?
    ;

rangeIntervalEntryExpression
    :
        dateCompositionExpression
        (('..'|'->') dateCompositionExpression)?
    ;

dateCompositionExpression
    :
        additiveExpression ('@' additiveExpression)?
    ;

additiveExpression
    :   multiplicativeExpression
        (('+' |  '-') multiplicativeExpression)*
    ;

multiplicativeExpression 
    : exponentiationExpression
        (('*' | '/' | '%') exponentiationExpression)*
    ;

exponentiationExpression
    :
        postfixExpression ('**' postfixExpression)?
    ;

postfixExpression
    :
        unaryExpression ('--' | '++')*
    ;

unaryExpression 
    :   ('$'|'-'|'++'|'--') unaryExpression
    |   primary
    ;

primary
    : base selector*
    ;

base 
    : identifier
    | literal
    | parExpression
    //| enumerationInstantiation 
    | 'this' 
    | 'super' 
    | 'null'
    ;

enumeration
    : '{' assignable (',' assignable)* '}'
    ;

selector 
    : selectorOp identifier
    | arguments
    | elementSelector
    ;
    
selectorOp
    : '.' 
    | '^.' 
    | '?.' 
    | '*.' 
    | '#'
    ;

elementSelector
    : 
    '[' elementsSpec ']'
    ;

elementsSpec
        :  
           assignable ( '...' | (',' assignable)* | '..' assignable )
        |  '...' assignable 
        ;

arguments 
      : positionalArguments | namedArguments
      ;
    
specialFunctorArguments 
      : specialFunctorArgument+
      ;

specialFunctorArgument
    : LIDENTIFIER specialFunctor
    ;
      
namedArgument
    : LIDENTIFIER initializer
    ;

varargArguments
    :   assignable (',' assignable)*
    ;

namedArguments
    :
        '{' ((namedArgument ';') => namedArgument ';')* varargArguments? '}'
    ;

parExpression 
    :   '(' assignable ')'
    ;
    
positionalArguments
    :   '(' (assignable (',' assignable)*)? ')'
    ;

formalParameters
    :   
    '(' (formalParameter (',' formalParameter)*)? ')'
    ;

// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
formalParameter
    :   annotation* type LIDENTIFIER ( ('->'|'..') type LIDENTIFIER )? (initializer | '...')?
    ;

// Control structures.

controlStructure
    : ifElse | switchCaseElse | doWhile | forFail | tryCatchFinally ;
    
ifElse
    : 'if' '(' expression ')' block ('else' block)?
    ;
    
switchCaseElse
    : 'switch' '(' expression ')' '{' cases '}'
    ;
    
cases 
    : caseNull caseStmt+ caseElse?
    ;
    
caseNull
    : 'case' 'null' block
    ;
    
caseStmt 
    : 'case' '(' caseExprs ')' block
    ;
    
caseExprs
    : expression (',' expression)*
    ;
    
caseElse
    : 'else' block
    ;
    
forFail
    : 'for' '(' forIterator ')' block ('fail' block)?
    ;
    
forIterator
    : controllingVariable 'in' simpleExpression
    ;
    
controllingVariable
    : annotation* type LIDENTIFIER ( ('->'|'..') type LIDENTIFIER )?
    ;
    
doWhile
    :   
    ('do' ('(' doIterator ')')? block? )?  
    'while' '(' expression ')' (block | ';')
    ;

doIterator
    :   
    localDeclaration
    ;

tryCatchFinally
    :
    'try' ('(' resource ')')?
    block
    ('catch' '(' localDeclaration ')' block)*
    ('finally' block)?
    ;
    
resource
    :   
    localDeclaration | expression
    ;

// Lexer

INTLITERAL
    : ('0' .. '9')('0' .. '9')*
    | '\'' HexDigit HexDigit HexDigit HexDigit '\''
    | '\'' HexDigit HexDigit '\''
    ;

fragment
Digit 
    : '0'..'9'
    ;

fragment
Digit2
    : Digit Digit
    ;

// FIXME: Doesn't allow ISO date format.
DATELITERAL
    : '\'' Digit Digit? '/' Digit Digit '/' Digit Digit Digit Digit  '\''
    ;

TIMELITERAL
    : '\'' Digit Digit? ':' Digit Digit (':' Digit Digit ( ':' Digit Digit Digit)?)? '\''
    ;

fragment
HexDigit
    :   ('0'..'9'|'a'..'f'|'A'..'F')
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

CHARLITERAL
    :   '\'' 
        (    ~( '\'' | '\r' | '\n' | '\\')
        | EscapeSequence
        ) 
        '\''
    ; 

/*

SIMPLESTRINGLITERAL
    :   ('"' | '}$')
        (    ~( '\r' | '\n' | '"' | '\\' | '$' | '{' )   
        | EscapeSequence
        )*
        ('"' | '${')
    ;
*/

SIMPLESTRINGLITERAL
    :   ('"')
        StringPart
        ('"')
    ;

LEFTSTRINGLITERAL
    : '"'
        StringPart
        '${'
    ;

RIGHTSTRINGLITERAL
    : '}$'
        StringPart
        '"'
    ;

fragment
NonStringChars
    :    '$' | '{' | '\\' | '"'
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
    :    'exists'
    ;

FINALLY
    :   'finally'
    ;

FOR
    :   'for'
    ;

FOUND
    :   'found'
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

PRODUCE
    :   'produce'
    ;

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
    : '->'
    ;

DOTS
    : '..'
    ;
    
COMPARE
    :   '<=>'
    ;
    
IN
    :   'in'
    ;
    
HASH
    :   '#'
    ;

PLUSEQ
    :   '+='
    ;
    
COLONEQ
    :   ':='
    ;
    
F
    :   'F'
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
