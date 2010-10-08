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
    :  userAnnotation* ( langAnnotation | memberDeclarationStart | typeDeclarationStart )
    ;

memberDeclarationStart
    : (type|'assign'|'void'|'case') LIDENTIFIER
    ;
    
typeDeclarationStart
    : ('class'|'interface'|'alias') UIDENTIFIER
    ;

//by making these things keywords, we reduce the amount of
//backtracking
langAnnotation
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
    | 'extension'
    | 'volatile'
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
    : 'return' expression?
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
    : annotations? ( abstractMemberDeclaration | typeDeclaration )
    ;

abstractMemberDeclaration
    : memberHeader memberParameters? ';'
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
//      a parExpression, just like we do for Smalltalk
//      style parameters below?
memberDefinition
    : memberParameters?
      ( block | (specifier | initializer)? ';' )
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
    : langAnnotation
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
    : '#' ( memberName | (parameterizedType ( '.' memberName )? ) )
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
    : variance? typeName
    ;

variance
    : 'in' | 'out'
    ;
    
//for locals and attributes
initializer
    : ':=' expression
    ;

//for parameters
specifier
    : '=' expression
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
      ( ('=' | ':=' | '.=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '&&=' | '||=' | '?=') expression )?
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
    | reflectedLiteral //needs to be here since it can contain type args
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
    : '{' expressions? '}'
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
    | nameAndTypeArguments
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
    : ('.' | '?.' | '*.') nameAndTypeArguments
    ;

nameAndTypeArguments
    : ( memberName | typeName ) 
      ( ( typeArguments ('('|'{') ) => typeArguments )?
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
    : namedSpecifiedArgument | namedFunctionalArgument
    ;

namedFunctionalArgument
    : formalParameterType parameterName formalParameters* block
    ;

namedSpecifiedArgument
    : parameterName specifier ';'
    ;

parameterName
    : LIDENTIFIER
    ;

namedArguments
    : '{' ((namedArgument) => namedArgument)* expressions? '}'
    ;

parExpression 
    : '(' expression ')'
    ;
    
positionalArguments
    : '(' ( positionalArgument (',' positionalArgument)* )? ')'
    ;
    
positionalArgument
    : (variableStart) => specialArgument
    | expression
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
    : '(' ( userAnnotation* ( langAnnotation | formalParameterType LIDENTIFIER ) | ')' )
    ;

// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
formalParameter
    : annotations? formalParameterType parameterName formalParameters*
      ( '->' type parameterName | '..' parameterName )? 
      specifier?
    ;

formalParameterType
    : (type|'void') '...'?
    ;

// Control structures.

condition
    : expression | existsCondition | nonemptyCondition | isCondition
    ;

existsCondition
    : 'exists' controlVariableOrExpression
    ;
    
nonemptyCondition
    : 'nonempty' controlVariableOrExpression
    ;
    
isCondition
    : 'is' type ( (memberName '=') => memberName specifier | expression )
    ;
    
controlStructure
    : ifElse | switchCaseElse | doWhile | forFail | tryCatchFinally
    ;
    
ifElse
    : ifBlock elseBlock?
    ;

ifBlock
    : 'if' '(' condition ')' block
    ;

elseBlock
    : 'else' (ifElse | block)
    ;

switchCaseElse
    : switchHeader ( '{' cases '}' | cases )
    ;

switchHeader
    : 'switch' '(' expression ')'
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
    : forBlock failBlock?
    ;

forBlock
    : 'for' '(' forIterator ')' block
    ;

failBlock
    : 'fail' block
    ;

forIterator
    : variable ('->' variable)? containment
    ;
    
containment
    : 'in' expression
    ;
    
doWhile
    : doBlock? whileBlock
    ;

doBlock
    : 'do' ('(' doIterator ')')? block?
    ;

whileBlock
    : 'while' '(' condition ')' (block | ';')
    ;

//do iterators are allowed to be mutable and/or optional
doIterator
    : annotations? variable (specifier | initializer)
    ;

tryCatchFinally
    : tryBlock catchBlock* finallyBlock?
    ;

tryBlock
    : 'try' resources? block
    ;

resources
    : '(' resource (',' resource)* ')'
    ;

catchBlock
    : 'catch' '(' variable ')' block
    ;

finallyBlock
    : 'finally' block
    ;
    
resource
    : controlVariableOrExpression
    ;

controlVariableOrExpression
    : (variableStart) => variable specifier 
    | expression
    ;

variable
    : type/*?*/ memberName
    ;

//special rule for syntactic predicate
variableStart
    : variable ('in'|'=')
    ;

// Lexer

fragment
Digits
    : ('0'..'9')+
    ;

fragment 
Exponent    
    :   ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;

fragment FLOATLITERAL :;
fragment ELLIPSIS :;
fragment RANGE :;
fragment DOT :;
NATURALLITERAL
    : Digits 
      ( { input.LA(2) != '.' }? => '.' Digits Exponent? { $type = FLOATLITERAL; } )?
    | '.' ( '..' { $type = ELLIPSIS; } | '.'  { $type = RANGE; } | { $type = DOT; } )
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
    :    '{' | '\\' | '"' | '$' | '\''
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

DO
    :   'do'
    ;
    
ELSE
    :   'else'
    ;            

EXISTS
    :   'exists'
    ;

EXTENDS
    :   'extends'
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

EQ
    :   '='
    ;

RENDER
    :   '$'
    ;

NOT
    :   '!'
    ;

BITWISENOT
    :   '~'
    ;

DEFAULT
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

IDENTICAL
    :   '==='
    ;

AND
    :   '&&'
    ;

OR
    :   '||'
    ;

IMPLIES
    :   '=>'
    ;

INCREMENT
    :   '++'
    ;

DECREMENT
    :   '--'
    ;

PLUS
    :   '+'
    ;

MINUS
    :   '-'
    ;

TIMES
    :   '*'
    ;

DIVIDED
    :   '/'
    ;

BITWISEAND
    :   '&'
    ;

BITWISEOR
    :   '|'
    ;

BITWISEXOR
    :   '^'
    ;

REMAINDER
    :   '%'
    ;

NOTEQ
    :   '!='
    ;

GT
    :   '>'
    ;

LT
    :   '<'
    ;        

GTEQ
    :   '>='
    ;

LTEQ
    :   '<='
    ;        

ENTRY
    :   '->'
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

QUESDOT
    :    '?.'
    ;

STARDOT
    :    '*.'
    ;

POWER
    :    '**'
    ;

DOTEQ
    :   '.='
    ;

PLUSEQ
    :   '+='
    ;

MINUSEQ
    :   '-='
    ;

TIMESEQ
    :   '*='
    ;

DIVIDEDEQ
    :   '/='
    ;

BITWISEANDEQ
    :   '&='
    ;

BITWISEOREQ
    :   '|='
    ;

BITWISEXOREQ
    :   '^='
    ;

REMAINDEREQ
    :   '%='
    ;

DEFAULTEQ
    :   '?='
    ;

ANDEQ
    :   '&&='
    ;

OREQ
    :   '||='
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
