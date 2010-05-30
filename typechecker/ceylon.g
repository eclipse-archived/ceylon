grammar ceylon;

options {
    //backtrack=true;
    memoize=true;
}

compilationUnit
    : importDeclaration*
      (annotations? typeDeclaration)+
      EOF
    ;
    
typeDeclaration
    : classDeclaration
    | interfaceDeclaration
    | aliasDeclaration
    ;

importDeclaration
    : 'import' importPath ('.' '*' | alias)? ';'
    ;

importPath
    : importElement ('.' importElement)*
    ;

alias
    : 'alias' typeName
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
    ;

inlineClassBody
    : '{' declarationOrStatement* '}'
    ;*/

//We could eliminate the backtracking by requiring
//all member declarations to begin with a keyword
//Note that this accepts more then the language spec
//since it does not enforce that enumerated class
//instances have to be listed together at the top
//of the class body
declarationOrStatement
    : (declarationStart) => declaration | statement
    ;

declaration
    : annotations? ( memberDeclaration | typeDeclaration | instance )
    ;

//special rule for syntactic predicates
declarationStart
    :  declarationModifier 
    | ( userAnnotation annotations? )? ( memberDeclarationStart | typeDeclarationStart )
    ;

memberDeclarationStart
    : (type|'assign'|'void'|'case') LIDENTIFIER
    ;
    
typeDeclarationStart
    : ('class'|'interface'|'alias') UIDENTIFIER
    ;

//by making these things keywords, we reduce the amount of
//backtracking
declarationModifier
    : 'public'
    | 'module'
    | 'package'
    | 'private'
    | 'abstract'
    | 'default'
    | 'override'
    | 'optional'
    | 'mutable'
    | 'static'
    | 'once'
    | 'deprecated'
    | 'volatile'
    | 'extension'
    ;

statement 
    : expressionStatement
    | controlStructure
//    | eventListener
    ;

expressionStatement
    : expression ';'
    ;

directiveStatement
    : directive ';'?
    ;

directive
    : returnDirective | throwDirective | breakDirective | retryDirective
    ;

returnDirective
    : 'return' assignable?
    ;

throwDirective
    : 'throw' expression?
    ;

breakDirective
    : 'break' expression?
    ;
    
retryDirective
    : 'retry'
    ;

abstractDeclaration
    : abstractMemberDeclaration
    //TODO: nested type declarations
    ;

abstractMemberDeclaration
    : annotations?
      memberHeader
      memberParameters?
      ';'
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

//TODO: should we allow the shortcut style of method
//      definition for a method or getter which returns
//      a parExpression, just like we do for smalltalk
//      style parameters below?
memberDefinition
    : memberParameters? block
    | (specifier | initializer)? ';'
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
    : '{' abstractDeclaration* '}'
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
    : '{' declarationOrStatement* '}'
    ;

extendedType
    : 'extends' type positionalArguments
    ;

instance
    : 'case' memberName arguments? (','|';'|'...')
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
    : declarationModifier
    | userAnnotation
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

expression
    : assignmentExpression
    ;

//Even though it looks like this is non-associative
//assignment, it is actually right associative because
//assignable can be an assignment
//Note that = is not really an assignment operator, but 
//can be used to init locals
assignmentExpression 
    : implicationExpression 
      ( ('=' | ':=' | '.=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '&&=' | '||=' | '?=') assignable )?
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
    | functionalArgument
    | elementSelector
    | ('--' | '++')
    ;

member
    : ('.' | '^.' | '?.' | '*.') 
      ( memberName | typeName ) 
      ( (typeArguments '(') => typeArguments )?
    ;

elementSelector
    : '?'? '[' elementsSpec ']'
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

//a smalltalk-style parameter to a positional parameter
//invocation
functionalArgument
    : functionalArgumentHeader functionalArgumentDefinition
    ;
    
functionalArgumentHeader
    : parameterName
    | 'case' '(' expressions ')'
    ;

functionalArgumentDefinition
    : ( (formalParameterStart) => formalParameters )? 
      ( block | parExpression /*| literal | specialValue*/ )
    ;

specialArgument
    : type memberName (containment|specifier)
    //| isCondition
    //| existsCondition
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    ;

//special rule for syntactic predicates
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
formalParameterStart
    : '(' (declarationStart | ')')
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
    : caseItem+ defaultCaseItem?
    ;

caseItem
    : 'case' '(' caseCondition ')' block
    ;

defaultCaseItem
    : 'else' block
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
    : ('do' ('(' doIterator ')')? block? )?  
      'while' '(' condition ')' (block | ';')
    ;

//do iterators are allowed to be mutable and/or optional
doIterator
    : annotations? variable (specifier | initializer)
    ;

tryCatchFinally
    : tryBlock catchBlock* finallyBlock?
    ;

tryBlock
    : 'try' resourceList? block
    ;

resourceList
    : '(' resource (',' resource)* ')'
    ;

catchBlock
    : 'catch' '(' variable ')' block
    ;

finallyBlock
    : 'finally' block
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
