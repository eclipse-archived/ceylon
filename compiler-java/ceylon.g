grammar ceylon;

options {
    /*    backtrack=true;  */
    memoize=true;
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
    ;

importDeclaration  
    :
        'import' 
        IDENTIFIER
        ('.' IDENTIFIER)*
        ('.' '*')?
        ';'
    ;

statement
    : (declaration) => declaration
    | expression? ';'
    ;

directive
    : 'return' expression 
    | 'throw' expression 
    | 'break' 
    | 'found'
    ;

block
    : '{' statement* ( directive ';'? )? '}'
    ;

declaration
    :  annotation* (attributeOrMethodDeclaration | toplevelDeclaration)
    ;

// This is quite different from the grammar in the spec, but because
// attributes and methods are syntactically very similar it makes
// sense to recognize them in this way.  An attribute is a method with
// no formalParameters.
// FIXME: Allows "void foo" as an attribute declaration
attributeOrMethodDeclaration
    :
        (type | 'void' | 'assign')
        IDENTIFIER
        (methodDefinition | attributeDefinition)
    ;
    
methodDefinition
    :   formalParameters typeConstraints?  (';' | block)
    ;
    
attributeDefinition
    :   ';' | block | initializer ';'
    ;

decoratorDeclaration
    :
        'decorator'
        IDENTIFIER
        typeParameters?
        formalParameters?
        satisfiedTypes?
        typeConstraints?
        '{' attributeOrMethodDeclaration* '}'
    ;

// FIXME: Not to spec.  I can't parse "type converter" because it's not LL, 
// but I can parse "converter type"
converterDeclaration
	:	
        'converter'
        type
        IDENTIFIER
        typeParameters?
        typeConstraints?
        '(' annotation* type IDENTIFIER ')'
	;

interfaceDeclaration
    :
        'interface'
        IDENTIFIER
        typeParameters?
        satisfiedTypes?
        typeConstraints?
        '{' attributeOrMethodStub* '}'
    ;

attributeOrMethodStub
    :
        annotation*
        (type | 'void')
        IDENTIFIER
        (formalParameters
         typeConstraints?)?
        ';'
        ;

classDeclaration
    :
        'class'
        IDENTIFIER
        typeParameters?
        formalParameters?
        ('extends' instantiation)?
        satisfiedTypes?
        typeConstraints?
        instanceEnumeration?
        block
    ;

instanceEnumeration
    :   'instances' instance (','instance?)*
    ;

instance 
    : IDENTIFIER ( '(' positionalArguments ')' )?
    ;

typeConstraint
    :   IDENTIFIER ((('>=' | '<=') type )| formalParameters)
    ;
    
typeConstraints
    : 'where' typeConstraint ('&' typeConstraint)*
    ;
    
satisfiedTypes
    : 'satisfies' type (',' type)*
    ;

type
    :   typeName typeParameters?
    ;

annotation
    :   
        '@' instantiation 
        | modifier
        | typeName literal? 
    ;

//I would really love for these to not
//be keywords, but for now they have
//to be, since module and package are
//also declarations
modifier
    : 'public'
    | 'module'
    | 'package'
    /*| 'abstract'
    | 'static'
    | 'mutable'
    | 'optional'
    | 'final'
    | 'override'
    | 'once'*/
    ;

typeName
    : IDENTIFIER ('.' IDENTIFIER)* 
    ;

typeParameters
    : '<' type (',' type)* '>'
    ;

initializer
    : '=' expression
    ;

literal
    : enumerationLiteral
    | integerLiteral
    | FLOATLITERAL
    | CHARLITERAL
    | stringLiteral
    | dateLiteral
    ;   

dateLiteral
    : DATELITERAL | TIMELITERAL;

integerLiteral
    : INTLITERAL
    ;

enumerationLiteral
    : 'none'
    ;

stringLiteral
    : SIMPLESTRINGLITERAL
        /* | LEFTSTRINGLITERAL expression RIGHTSTRINGLITERAL  */
    ;

expression 
    :
        implicationExpression (assignmentOp implicationExpression)?
        /*    | '{' expression ';' (expression ';')* '}' */
    ;
    
assignmentOp
    : '=' 
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
        dateCompositionExpression
        ('exists' | 'nonempty') ?
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

// FIXME: The spec says ** should be left-associative, but it's
// conventionally right-associative, which is what I've done here.
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
    :
    ( IDENTIFIER 
    | 'this' | 'super' | 'null'
    | literal
    | parExpression
    | enumerationInstantiation ) 
    selector*
    ;
    
instantiation
    : typeName typeParameters? arguments
    ;
    
enumerationInstantiation
    	: '{' expressionList '}'
    	;

selector  
    : selectorOp IDENTIFIER
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
           expression ( '...' | (',' expression)* | '..' expression )
        |  '...' expression	
        ;

arguments 
      :
          '(' positionalArguments ')'
      |   '{' namedArguments '}'
      ;

namedArgument
    :
        IDENTIFIER initializer
    ;

varargArguments
    :   
        expressionList
    ;

namedArguments
    :
        ((namedArgument ';') => namedArgument ';')* varargArguments?
    ;

parExpression 
    :   '(' expression ')'
    ;

identifierSuffix 
    :   arguments
    ;

positionalArguments
    :   expressionList?
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
    :   annotation* type IDENTIFIER ( '->' type IDENTIFIER )? (initializer | '...')?;
    

expressionList 
    :   expression (',' expression)*
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

SIMPLESTRINGLITERAL
    :   '"' 
        (    ~( '\r' | '\n' | '"' | '\\')   
        | EscapeSequence
        )*
        '"' 
    ;

/*

// There soesn't seem to be any reasonable way to lex these.

LEFTSTRINGLITERAL
    : '"'
        (    ~( '\r' | '\n' | '"' | '\\')   
        | EscapeSequence
        )*
        '${'
    ;

RIGHTSTRINGLITERAL
    : '}'
        (    ~( '\r' | '\n' | '"' | '\\')   
        | EscapeSequence
        )*
        '"'
    ;

*/

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

DEFAULT
    :   'default'
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

IMPLEMENTS
    :   'implements'
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

PACKAGE
    :   'package'
    ;

PRIVATE
    :   'private'
    ;

PROTECTED
    :   'protected'
    ;

PUBLIC
    :   'public'
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

//Surely we're going to need this...

VOLATILE
    :   'volatile'
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

MODULE
    :   'module'
    ;
    
CONVERTER
	:  'converter'
	;

IDENTIFIER
    :   IdentifierStart IdentifierPart*
    ;

// FIXME: Unicode identifiers
fragment
IdentifierStart
    :   'A'..'Z'
    |   '_'
    |   'a'..'z'
    ;                
                       
fragment 
IdentifierPart
    :   IdentifierStart
    |   '0'..'9'
    ;
