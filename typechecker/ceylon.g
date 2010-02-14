grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
}

compilationUnit
    : (importDeclaration)*
      (annotations? toplevelDeclaration)+
      EOF
    ;
    
toplevelDeclaration
    : classDeclaration 
    | interfaceDeclaration 
    //| converterDeclaration 
    //| decoratorDeclaration
    | aliasDeclaration
    ;

importDeclaration  
    : 'import' importElement ('.' importElement)* ('.' '*' | 'alias' typeName)? ';'
    ;
    
importElement
    : LIDENTIFIER | UIDENTIFIER
    ;

block
    : '{' localOrStatement* directiveStatement? '}'
    ;

//we could eliminate the backtracking by requiring
//local declarations to begin with a keyword
localOrStatement
    : //'local' annotations? localDeclaration
      (declarationStart) => annotations? localDeclaration
    | statement
    ;

localDeclaration
    //TODO: type inference: (type|'def')
    : type memberName initializer? ';'
    ;

inlineClassDeclaration
    : 'new' 
      annotations?
      regularType
      arguments
      satisfiedTypes?
      '{' memberOrStatement* '}'
    ;
    
//we could eliminate the backtracking by requiring
//all member declarations to begin with a keyword
memberOrStatement
    : //modifier annotations? ( memberDeclaration | toplevelDeclaration )
      (declarationStart) => annotations? ( memberDeclaration | toplevelDeclaration )
    | statement
    ;

functorHeader
    : 'functor' annotations? (type | 'void')
    ;

//a functor expression that can appear as the RHS of an 
//assignment, in an argument list, or in parens
functor 
    : functorHeader? formalParameters functorBody
    ;

//a functor expression that can appear inside an expression
//without the need for parens
/*primaryFunctor 
    : functorHeader? formalParameters block
    ;*/

//shortcut functor expression that makes the parameter list 
//optional, but can appear only using the special smalltalk
//style method protocol
specialFunctor
    : ( (formalParameterStart) => formalParameters )? functorBody
    ;

//we can support enumerations as functor bodies, but
//I think that's just confusing to the reader
functorBody
    : ('{') => block
    | implicationExpression
    ;

//special rule for syntactic predicates
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
formalParameterStart
    : '(' (declarationStart | ')')
    ;

//special rule for syntactic predicates
functorStart
    : 'functor' | formalParameterStart
    ;

//special rule for syntactic predicates
/*primaryFunctorStart
    : 'functor' | '(' (declarationStart | ')' '{')
    ;*/

//special rule for syntactic predicates
declarationStart
    :  declarationModifier 
    //TODO: type inference: (type|'assign'|'void'|'def')
    | ( userAnnotation annotations? )? (type|'assign'|'void') LIDENTIFIER
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
    : expression ';'
    | controlStructure
    ;

directiveStatement
    : directive ';'?
    ;

directive
    : 'return' assignable? 
    | 'produce' assignable
    | 'throw' expression? 
    | 'break' 
    | 'found'
    ;

// what I have here now allows method and attribute bodies 
// to omit the braces, just like functor bodies. The cost
// of doing that was the need to add a predicate, and we're
// still not sure if we really want this feature
memberDeclaration
    : voidMethodDeclaration 
    | methodOrAttributeDeclaration 
    | setterDeclaration
    ;

//TODO: we would not really need the syntactic predicate
//here if we left off recognizing methods vs. attributes
//until later
methodOrAttributeDeclaration
    : type memberName ( (typeParameterStart | formalParameterStart) => methodDefinition | attributeDefinition )
    ;

setterDeclaration
    : 'assign' memberName setterDefinition
    ;

voidMethodDeclaration 
    : 'void' memberName methodDefinition
    ;

typeParameterStart
    : '<'
    ;

setterDefinition
    : block
    ;

methodDefinition
    : typeParameters? formalParameters typeConstraints? ( block | ';' )
    ;

attributeDefinition
    : block | initializer ';'
    ;

/*decoratorDeclaration
    :
        'decorator'
        typeName
        typeParameters?
        formalParameters?
        satisfiedTypes?
        typeConstraints?
        '{' ( annotations? memberDeclaration )* '}'
    ;

converterDeclaration
    :
        'converter'
        type
        typeName
        typeParameters?
        typeConstraints?
        '(' annotations? type parameterName ')'
    ;*/

interfaceDeclaration
    :
        'interface'
        typeName
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        '{' memberStub* '}'
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

memberStub
    :
        annotations?
        (type | 'void')
        memberName
        (typeParameters?
         formalParameters
         typeConstraints?)?
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
        '{' ((instanceStart)=>instances)? memberOrStatement* '}'
    ;

extendedType
    : 'extends' regularType arguments
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
    : regularType | functorType
    ;

regularType
    : qualifiedTypeName ( (typeParameterStart) => typeArguments )?
    ;

functorType
    : functorHeader formalParameters
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
    : annotationName ( annotationArguments | arguments )?
    ;

annotationArguments
    : ( literal | reflectedLiteral )+
    ;

//TODO: why can't I get the typeArguments in here??
reflectedLiteral
    : qualifiedTypeName reflectedMember
    | reflectedBase
    ;

qualifiedTypeName
    : //( identifier '.' )* 
    UIDENTIFIER ('.' UIDENTIFIER)*
    ;

typeName
    : UIDENTIFIER
    ;

annotationName
    : //( identifier '.' )* 
    LIDENTIFIER
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
    : ('=' | ':=') assignable
    ;

//for parameters
specifier
    : '=' assignable
    ;

literal
    : NATURALLITERAL
    | FLOATLITERAL
    | CHARLITERAL
    | DATELITERAL
    | TIMELITERAL
    | REGEXPLITERAL
    | stringLiteral
    ;   

stringLiteral
    : SIMPLESTRINGLITERAL
    | LEFTSTRINGLITERAL expression (MIDDLESTRINGLITERAL expression)* RIGHTSTRINGLITERAL 
    ;

assignable 
    : (functorStart) => functor
    | expression
    ;

//Even though it looks like this is non-associative
//assignment, it is actually right associative because
//assignable can be an assignment
//Note that = is not really an assignment operator, but 
//can be used to init locals
expression 
    : methodExpression 
      ( ('=' | ':=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '&&=' | '||=' | '?=') assignable )?
    ;

methodExpression
    : implicationExpression specialMethodArgument*
    ;

specialMethodArgument
    :(  methodOrParameterName | 'case' '(' expressions ')' ) specialFunctor 
    ;

//if we want to support Smalltalk-style invocation with no arguments
/*methodExpression
    : implicationExpression specialMethodInvocation?
    ;

specialMethodInvocation
    : methodOrParameterName ( specialFunctor specialMethodArgument* )?
    | caseMethodParameter specialFunctor specialMethodArgument*
    ;

specialMethodArgument
    : ( methodOrParameterName | caseMethodParameter ) specialFunctor
    ;

caseMethodParameter
    : 'case' '(' expressions ')'
    ;*/

methodOrParameterName
    : LIDENTIFIER
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
    : dateCompositionExpression
      (('..'|'->') dateCompositionExpression)?
    ;

dateCompositionExpression
    :  additiveExpression 
       ('@' additiveExpression)?
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

primary
    : base selector*
    ;
    
base 
    : regularType
    | memberName
    | literal
    //| (primaryFunctorStart) => primaryFunctor
    | parExpression
    | enumeration
    | 'this' 
    | 'super' 
    | 'null'
    | 'none'
    | inlineClassDeclaration
    | reflectedBase
    ;

enumeration
    : '{' assignables? '}'
    ;

selector 
    : memberInvocation
    | reflectedMember
    | arguments
    | elementSelector
    | ('--' | '++')
    ;

memberInvocation
    : ('.' | '^.' | '?.' | '*.') memberName
    ;

reflectedBase
    : '#' ( regularType | memberName )
    ;
    
reflectedMember
    : '#' memberName
    ;

elementSelector
    : '[' elementsSpec ']'
    ;

elementsSpec
    : additiveExpression ( '...' | '..' additiveExpression | (',' additiveExpression)+ )?
    |  '...' additiveExpression	
    ;

arguments 
    : positionalArguments | namedArguments
    ;
      
namedArgument
    : parameterName specifier /*(',' assignable)**/ ';'
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
    : (variableStart) => special 
    | assignable
    ;

special
    : type memberName (containment|specifier)
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    ;

// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
formalParameter
    :  annotations? type parameterName 
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
    : annotations? variable initializer
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
    : type memberName
    ;

variableStart
    : type memberName ('in'|'=')
    ;

// Lexer

NATURALLITERAL
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

MIDDLESTRINGLITERAL
    : '}$'
        StringPart
        '${'
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

REGEXPLITERAL
    : '`' (~( '`' | '\\') | RegexEscapeSequence)* '`'
    ;

fragment
RegexEscapeSequence
    :    ('\\' ~ '\n')
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
