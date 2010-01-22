grammar ceylon;

options {
    backtrack=true;
    memoize=true;
}


compilationUnit 
	:
        '{'
        (statement ';')*
        '}'
	;

statement
	:	
	(declaration
	| expression) ;

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

expression 
    :   additiveExpression
    ('=' expression)?
/*    | '{' expression ';' (expression ';')* '}' */
    ;

additiveExpression
    :   multiplicativeExpression
        (   
            (   '+'
            |   '-'
            )
            multiplicativeExpression
        )*
    ;

multiplicativeExpression 
    :
        unaryExpression
        (   
            (   '*'
            |   '/'
            |   '%'
            )
            unaryExpression
        )*
	;

unaryExpression 
    :   '+'  unaryExpression
    |   '-' unaryExpression
    |   '++' unaryExpression
    |   '--' unaryExpression
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus 
    :   '~' unaryExpression
    |   '!' unaryExpression
    |   primary   
    ;


enumerationLiteral
    : 'none'
	|	'{'( SIMPLESTRINGLITERAL)? (',' SIMPLESTRINGLITERAL)* '}'
	;


stringLiteral
    : SIMPLESTRINGLITERAL
    ;


orderedParameterValues
	:  '(' expression (COMMA expression)* ')'
	;


methodInvocation
	: (primary '.') IDENTIFIER orderedParameterValues
	;

selector  
    :   '.' IDENTIFIER
        (arguments
        )?
    |   '.' 'this'
    |   '.' 'super'
    ;

primary
	:   IDENTIFIER
        ('.' IDENTIFIER
        )*
        (arguments)?
    | literal
    | parExpression
        

        
	;
arguments 
:

	(positionalArguments) (namedArguments)?| (namedArguments) ;

expressionList 
    :   expression
        (',' expression
        )*
    ;

parExpression 
    :   '(' expression ')'
    ;

identifierSuffix 
    :	arguments
    ;

positionalArguments
    :   '(' (expressionList
        )? ')'
    ;
	
namedArgument
    :
    IDENTIFIER '=' expression
    |
    ;

varargArguments
:	expression (',' expression)*
	;
	
namedArguments
	:
	'{'
	(namedArgument ';')* varargArguments?
	'}'
	;
	
declaration
	:	IDENTIFIER (selector)* IDENTIFIER ('=' expression)?
	;
/********************************************************************************************
                  Lexer section
*********************************************************************************************/


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
    |   '//' ~('\n'|'\r')*     // a line comment could appear at the end of the file without CR/LF
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

CONTINUE
    :   'continue'
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

VOLATILE
    :   'volatile'
    ;

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
    
NULL	
	: 'null'
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
