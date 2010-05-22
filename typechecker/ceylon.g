grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
}

compilationUnit
    : (importDeclaration)*
      (annotations? typeDeclaration)+
      EOF
    ;
    
typeDeclaration
    : classDeclaration 
    | interfaceDeclaration
    | aliasDeclaration
    ;

importDeclaration  
    : 'import' importElement ('.' importElement)* ('.' '*' | 'alias' typeName)? ';'
    ;
    
importElement
    : LIDENTIFIER | UIDENTIFIER
    ;

block
    : '{' declarationOrStatement* directiveStatement? '}'
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

//we could eliminate the backtracking by requiring
//all member declarations to begin with a keyword
declarationOrStatement
    : //modifier annotations? ( memberDeclaration | toplevelDeclaration )
      (declarationStart) => annotations? ( memberDeclaration | typeDeclaration )
    | statement
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
    | ( userAnnotation annotations? )? ( (type|'assign'|'void') LIDENTIFIER | ('class'|'interface'|'alias') UIDENTIFIER )
    ;

//by making these things keywords, we reduce the amount of
//backtracking
declarationModifier 
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
//    | eventListener
    | controlStructure
    ;

expressionStatement
    : expression ';'
    ;

directiveStatement
    : directive ';'?
    ;

directive
    : 'return' assignable? 
    | 'throw' expression? 
    | 'break' expression?
    | 'retry'
    ;

abstractMemberDeclaration
    : annotations?
      memberHeader
      memberParameters?
    ;

memberDeclaration
    : memberHeader memberDefinition
    ;

memberHeader
    : (type | 'void' | 'assign') memberName
    ;

memberParameters
    : typeParameters? formalParameters+ typeConstraints?
    ;

memberDefinition
    : memberParameters? block
    //allow omission of braces:
    /*: ( (memberParameterStart) => memberParameters )? 
      ( ('{') => block | implicationExpression ';' )*/
    | (specifier | initializer)? ';'
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
    ;

interfaceBody
    //TODO: why can't we have toplevel declarations 
    //      inside an interface dec?
    : '{' ( abstractMemberDeclaration ';' )* '}'
    ;

aliasDeclaration
    :
        'alias'
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        ';'
    ;

classDeclaration
    :
        'class'
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
    ;

extendedType
    : 'extends' type positionalArguments
    ;
    
instances
    : instance (',' instance)* (';'|'...')
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
    : parameterizedType //( '[' parameterizedType? ']' )?
    | 'subtype'
    ;
    
parameterizedType
    : qualifiedTypeName typeArguments?
    ;

annotations
    : annotation+
    ;

annotation
    : declarationModifier | userAnnotation
    ;

//TODO: we could minimize backtracking by limiting the 
//kind of expressions that can appear as arguments to
//the annotation
userAnnotation 
    : annotationName annotationArguments?
    ;

annotationArguments
    : arguments | ( literal | reflectedLiteral )+
    ;

reflectedLiteral 
    : '#' ( memberName | (type ( '.' memberName )? ) )
    ;

qualifiedTypeName
    : typeName ('.' typeName)*
    ;

typeName
    : UIDENTIFIER
    ;

annotationName
    : LIDENTIFIER
    ;

memberName 
    : LIDENTIFIER
    ;

typeArguments
    : '<' type (',' type)* '>'
    ;

typeParameters
    : '<' typeParameter (',' typeParameter)* '>'
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
    ;

//for parameters
specifier
    : '=' assignable
    ;

literal
    : NATURALLITERAL
    | FLOATLITERAL
    | QUOTEDLITERAL
    | CHARLITERAL
    | stringLiteral
    ;   

stringLiteral
    : SIMPLESTRINGLITERAL
    | LEFTSTRINGLITERAL expression (MIDDLESTRINGLITERAL expression)* RIGHTSTRINGLITERAL 
    ;

/*eventListener
    : 'on' '(' expression ')' 
      'assign'? memberName formalParameters?
      block
    ;*/

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
      ( ('=' | ':=' | '.=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '&&=' | '||=' | '?=') assignable )?
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
      ('=>' disjunctionExpression)?
    ;

//should '^' have a higher precedence?
disjunctionExpression
    : conjunctionExpression 
      ('||' conjunctionExpression)?
    ;

conjunctionExpression
    : logicalNegationExpression 
      ('&&' logicalNegationExpression)*
    ;

logicalNegationExpression
    : '!' logicalNegationExpression
    | equalityExpression
    ;

equalityExpression
    : comparisonExpression
      (('=='|'!='|'===') comparisonExpression)?
    ;

comparisonExpression
    : defaultExpression
      (('<=>'|'<'|'>'|'<='|'>='|'in'|'is') defaultExpression)?
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
      (('..'|'->') additiveExpression)?
    //: additiveExpression ('->' additiveExpression)?
    //| '[' additiveExpression ',' additiveExpression ']'
    ;

additiveExpression
    : multiplicativeExpression
      (('+' | '-' | '|' | '^') multiplicativeExpression)*
    ;

multiplicativeExpression 
    : exponentiationExpression
      (('*' | '/' | '%' | '&') exponentiationExpression)*
    ;

exponentiationExpression
    : unaryExpression ('**' unaryExpression)?
    ;

unaryExpression 
    : ('$'|'-'|'++'|'--') unaryExpression
    | primary
    ;

specialValue
    : 'this' 
    | 'super' 
    | 'null'
    | 'none'
//    | 'delegate'
    ;

enumeration
    : '{' assignables? '}'
    //a special List literal syntax?
    //| '[' assignables? ']' 
    ;

primary
    : ('get'|'set')? base selector* 
    ;
    
base 
    : literal
    | parExpression
    | enumeration
    | specialValue
    | memberName
    | typeName
    //| inlineClassDeclaration
    ;
    
selector 
    : member
    | arguments
    | elementSelector
    | ('--' | '++')
    ;

member
    : ('.' | '^.' | '?.' | '*.') 
      ( memberName | typeName ) 
      ( (typeArguments '(') => typeArguments )?
    ;

elementSelector
    : '[' elementsSpec ']'
    ;

elementsSpec
    : additiveExpression ( '...' | '..' additiveExpression )?
    //|  '...' additiveExpression	
    //: additiveExpression ( ',' additiveExpression? )?
    ;

arguments 
    : positionalArguments | namedArguments
    ;
    
namedArgument
    : 'assign'? parameterName memberDefinition
    ;
    
parameterName
    : LIDENTIFIER
    ;

namedArguments
    : '{' ((namedArgument) => namedArgument)* varargArguments? '}'
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
    : '(' ( positionalArgument (',' positionalArgument)* )? ')'
    ;
    
positionalArgument
    : (variableStart) => specialArgument
    | assignable
    ;

specialArgument
    : type memberName (containment|specifier)
    //| isCondition
    //| existsCondition
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
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
    'try' ( '(' resource (',' resource)* ')' )? block
    ('catch' '(' variable ')' block)*
    ('finally' block)?
    ;
    
resource
    : (variableStart) => variable specifier 
    | expression
    ;

variable
    : type/*?*/ memberName
    ;

//special rule for syntactic predicate
variableStart
    : type/*?*/ memberName ('in'|'=')
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

CHARLITERAL
    :   '@' ( ~ NonCharacterChars | EscapeSequence )
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
NonCharacterChars
    :    ' ' | '\\' | '\t' | '\n' | '\f' | '\r' | '\b'
    ;

fragment
StringPart
    :    ( ~ NonStringChars | EscapeSequence ) *
    ;
    
fragment
EscapeSequence 
    :   '\\' (
            'b' 
        |   't' 
        |   'n' 
        |   'f' 
        |   'r'
        |   's' 
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

GET
    :   'get'
    ;

SET
    :   'set'
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

SUBTYPE
    :   'subtype'
    ;

THROW
    :   'throw'
    ;

TRY
    :   'try'
    ;

RETRY
    : 'retry'
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
