grammar ceylon;

options {
    backtrack=true;
    memoize=true;
}


compilationUnit 
	:
	'{'
	(expression ';')*
	'}'
	;

literal
	: enumerationLiteral
	| integerLiteral
	| FLOATLITERAL
	| CHARLITERAL
	| STRINGLITERAL
;	

integerLiteral
	: INTLITERAL
	| 
	| 
	;

expression 
    :   additiveExpression
    | '{' expression ';' (expression ';')* '}'
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
	|	'{'( STRINGLITERAL)? (',' STRINGLITERAL)* '}'
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
    :   '(' (expressionList
        )? ')'
    ;

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


/********************************************************************************************
                  Lexer section
*********************************************************************************************/


INTLITERAL
: ('0' .. '9')('0' .. '9')*
	| '\'' HexDigit HexDigit HexDigit HexDigit '\''
	| '\'' HexDigit HexDigit '\''
	;

fragment
HexDigit
    :   ('0'..'9'|'a'..'f'|'A'..'F')
    ;

fragment
LongSuffix
    :   'l' | 'L'
    ;


fragment
NonIntegerNumber
    :   ('0' .. '9')+ '.' ('0' .. '9')+ Exponent?  
    |   '.' ( '0' .. '9' )+ Exponent?  
    |   ('0' .. '9')+ Exponent  
        ;
        
fragment 
Exponent    
    :   ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;
    
FLOATLITERAL
    :   NonIntegerNumber
    ;
    
CHARLITERAL
    :   '\'' 
        (   EscapeSequence 
        |   ~( '\'' | '\\' | '\r' | '\n' )
        ) 
        '\''
    ; 

STRINGLITERAL
    :   '"' 
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )        
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
             |   '\\' 
             |       
                 ('0'..'3') ('0'..'7') ('0'..'7')
             |       
                 ('0'..'7') ('0'..'7') 
             |       
                 ('0'..'7')
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
    
COMMENT
         @init{
            boolean isJavaDoc = false;
        }
    :   '/*'
            {
                if((char)input.LA(1) == '*'){
                    isJavaDoc = true;
                }
            }
        (options {greedy=false;} : . )* 
        '*/'
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
