grammar ceylon;

options {
    /*    backtrack=true;  */
    memoize=true;
}

compilationUnit 
    :
        '{'
        (statement ';'
        | block
        )*
        '}'
    ;

statement
    : declaration
    | expression
    |
    ;

block
    : '{' (statement ';')+ '}'
    ;
        

declaration    :
       /* (modifier | instantiation)+ ':'
        typeName typeParameters? IDENTIFIER ('=' expression)?
        | */
       	type IDENTIFIER ('=' expression)?
    ;

type	:	typeName typeParameters?
	;

/* declaration
	: type ((arguments type)+ ':' type)?
	   IDENTIFIER ('=' expression)?
	;
*/

modifier
    : 'public'
    | 'module'
    | 'package'
    | 'abstract'
    | 'static'
    | 'mutable'
    | 'optional'
    | 'final'
    | 'override'
    | 'once'
    ;

typeName
    :   IDENTIFIER ('.'IDENTIFIER)* 
    ;

typeParameters
    : '<' typeName typeParameters? (',' typeName typeParameters?)* '>'
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
    |   '{'( SIMPLESTRINGLITERAL)? (',' SIMPLESTRINGLITERAL)* '}'
    ;

stringLiteral
    : SIMPLESTRINGLITERAL
        /* | LEFTSTRINGLITERAL expression RIGHTSTRINGLITERAL  */
    ;

expression 
    :   implicationExpression
        (('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '&&=' | '||=') expression)?
        /*    | '{' expression ';' (expression ';')* '}' */
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
    : '!' logicalNegationExpression
    | equalityExpression
    ;

equalityExpression
    :   comparisonExpression
        (('=='|'!='|'===') comparisonExpression)?
    ;

comparisonExpression
    :
        defaultExpression
        (('<=>'|'<'|'>'|'<='|'>='|'in') defaultExpression)?
    ;

defaultExpression
    :   
        existenceEmptinessExpression ('default' defaultExpression)?
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

// FIXME: The spec says ** should be left-associative, but it's conventionally
// right-associative, which is what I've done here.
exponentiationExpression
    : postfixExpression ('**' exponentiationExpression)?
    ;

postfixExpression
    : unaryExpression ('--' | '++')*
    ;   

unaryExpression 
    :   '$'  unaryExpression
    |   '-' unaryExpression
    |   '++' unaryExpression
    |   '--' unaryExpression
    |   '~' unaryExpression
    |   primary
    ;

primary
    : IDENTIFIER | 'this' | 'super'
        selector*
    | literal
    | parExpression
    ;
    
instantiation
    : typeName typeParameters? arguments
    ;

selector  
    :   ('.' | '^.'|'?.') (IDENTIFIER | 'this' | 'super' )
    |
        arguments
    |   '[' 
        ( expression ('...'
                | (',' expression)*
                | '..' expression)
            | '...' expression)
        ']'        
    ;

arguments 
    :
        '(' positionalArguments ')'
    |
        '{' namedArguments '}'
    ;

namedArgument
    :
        IDENTIFIER '=' expression
    ;

varargArguments
    :   
        expression (',' expression)*
    ;

namedArguments
    :
        ((namedArgument ';') => namedArgument ';')* varargArguments?
    ;

expressionList 
    :   expression
        (',' expression)*
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

CASE
    :   'case'
    ;

CATCH
    :   'catch'
    ;

CLASS
    :   'class'
    ;

CONST
    :   'const'
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

ENUM
    :   'enum'
    ;             

EXISTS
    : 'exists'
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

IMPLEMENTS
    :   'implements'
    ;

IMPORT
    :   'import'
    ;

INTERFACE
    :   'interface'
    ;
    
NONEMPTY
: 'nonempty'
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

STATIC
    :   'static'
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

/*

Surely we're going to need this...

VOLATILE
    :   'volatile'
    ;

*/

WHILE
    :   'while'
    ;

TRUE
    :   'true'
    ;

FALSE
    :   'false'
    ;

OPENPAREN
    :   '('
    ;

CLOSEPAREN
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

DOTS
    : '..'
    ;
    
NEQUAL  :   '<=>'
    ;
    
IN  :   'in'
    ;

    
HASH    :   '#'
    ;

PLUSEQ  :   '+='
    ;

MODULE
    : 'module'
    ;

IDENTIFIER
    :   IdentifierStart IdentifierPart*
    ;

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
