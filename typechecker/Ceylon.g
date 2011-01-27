grammar Ceylon;

options {
    memoize=false;
    output=AST;
}

tokens {
    ANNOTATION;
    ANNOTATION_LIST;
    MEMBER_DECL;
    TYPE_DECL;
    ALIAS_DECL;
    ABSTRACT_MEMBER_DECL;
    ANNOTATION_NAME;
    ARG_LIST;
    ARG_NAME;
    ANON_METH;
    ATTRIBUTE_DECL;
    ATTRIBUTE_SETTER;
    BREAK_STMT;
    CALL_EXPR;
    CASE_LIST;
    CATCH_BLOCK;
    CATCH_STMT;
    CHAR_CST;
    CLASS_BODY;
    CLASS_DECL;
    OBJECT_DECL;
    CONDITION;
    CONTINUE_STMT;
    DO_BLOCK;
    DO_ITERATOR;
    EXPR;
    EXPR_LIST;
    EXPR_STMT;
    FINALLY_BLOCK;
    FORMAL_PARAMETER;
    FORMAL_PARAMETER_LIST;
    IF_FALSE;
    IF_STMT;
    IF_TRUE;
    IMPORT_DECL;
    IMPORT_LIST;
    IMPORT_WILDCARD;
    IMPORT_PATH;
    IMPORT_ELEM;
    INIT_EXPR;
    INTERFACE_DECL;
    INTERFACE_BODY;
    MEMBER_NAME;
    MEMBER_TYPE;
    METHOD_DECL;
    METATYPE_LIST;
    NAMED_ARG;
    SEQ_ARG;
    RET_STMT;
    BLOCK;
    THROW_STMT;
    RETRY_STMT;
    TRY_BLOCK;
    TRY_CATCH_STMT;
    TRY_RESOURCE;
    TRY_STMT;
    TYPE_ARG_LIST;
    TYPE_NAME;
    TYPE_PARAMETER_LIST;
    WHILE_BLOCK;
    WHILE_STMT;
    SWITCH_STMT;
    SWITCH_EXPR;
    SWITCH_CASE_LIST;
    CASE_ITEM;
    CASE_DEFAULT;
    TYPE_CONSTRAINT_LIST;
    TYPE;
    TYPE_CONSTRAINT;
    TYPE_DECL;
    SATISFIES_LIST;
    ABSTRACTS_LIST;
    SUBSCRIPT_EXPR;
    LOWER_BOUND;
    UPPER_BOUND;
    SELECTOR_LIST;
    SPEC_EXPR;
    SPEC_STMT;
    TYPE_VARIANCE;
    TYPE_PARAMETER;
    STRING_CONCAT;
    INT_CST;
    FLOAT_CST;
    STRING_CST;
    QUOTE_CST;
    FOR_STMT;
    FOR_ITERATOR;
    FAIL_BLOCK;
    LOOP_BLOCK;
    FOR_CONTAINMENT;
    ENUM_LIST;
    SUPERCLASS;
    POSTFIX_EXPR;
    EXISTS_EXPR;
    NONEMPTY_EXPR;
    IS_EXPR;
    SATISFIES_EXPR;
    SPECIAL_ARG;
    PRIMARY;
}

@parser::header { package com.redhat.ceylon.compiler.parser; }
@lexer::header { package com.redhat.ceylon.compiler.parser; }

compilationUnit
    : importDeclaration*
      annotatedDeclaration+
      EOF
    -> ^(IMPORT_LIST importDeclaration*)
       annotatedDeclaration+
    ;

typeDeclaration
    : classDeclaration
    -> ^(CLASS_DECL classDeclaration)
    | interfaceDeclaration
    -> ^(INTERFACE_DECL interfaceDeclaration)
    | objectDeclaration
    -> ^(OBJECT_DECL objectDeclaration)
    ;

importDeclaration
    : 'import' packagePath '{' importElements '}'
      -> ^(IMPORT_DECL packagePath importElements)
    ;

importElements
    : importElement (',' importElement)* (',' importWildcard)?
    | importWildcard
    ;

importElement
    : 'implicit'? importAlias? importedName
    -> ^(IMPORT_ELEM 'implicit'? importAlias? importedName)
    ;

importWildcard
    : '...'
    -> ^(IMPORT_WILDCARD)
    ;

importAlias
    : 'local' importedName '='
    -> ^(ALIAS_DECL importedName)
    ;

importedName
    : typeName | memberName
    ;

packagePath
    : LIDENTIFIER ('.' LIDENTIFIER)*
    -> ^(IMPORT_PATH LIDENTIFIER*)
    ;
    
block
    : '{' blockDeclarationsAndStatements? '}'
    -> ^(BLOCK blockDeclarationsAndStatements?)
    ;

//This rule accounts for the problem that we
//can't tell whether a member body is a block
//or a named argument list until after we
//finish parsing it
blockDeclarationsAndStatements
    : controlStructure blockDeclarationsAndStatements?
    | directiveStatement
    | (specificationStart) => specificationStatement blockDeclarationsAndStatements?
    | (annotatedDeclarationStart) => annotatedDeclaration blockDeclarationsAndStatements?
    | expression 
    (
        ';' blockDeclarationsAndStatements?
      -> ^(EXPR_STMT expression) blockDeclarationsAndStatements?
      | (',' expression)* 
      -> ^(EXPR_LIST expression+)
    )
    ;

annotatedDeclarationOrStatement
    : controlStructure
    | (specificationStart) => specificationStatement
    | (annotatedDeclarationStart) => annotatedDeclaration
    | expressionStatement
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER '='
    ;

//we don't need to distinguish methods from attributes
//in the grammar
annotatedDeclaration
    :
    annotations?
    ( 
        memberDeclaration 
      -> ^(MEMBER_DECL memberDeclaration annotations?)
      | typeDeclaration 
      -> ^(TYPE_DECL typeDeclaration annotations?)
    )
    ;

//special rule for syntactic predicates
annotatedDeclarationStart
    : declarationStart
    | LIDENTIFIER
      ( 
          declarationStart
        | LIDENTIFIER
        | nonstringLiteral | stringLiteral
        | arguments annotatedDeclarationStart //we need to recurse because it could be an inline callable argument
      )
    ;

declarationStart
    : declarationKeyword 
    | type '...'? LIDENTIFIER
    ;

declarationKeyword
    : 'local' 
    | 'assign' 
    | 'void'
    | 'interface' 
    | 'class' 
    | 'object'
    ;

specificationStatement
    : memberName specifier ';'
    -> ^(SPEC_STMT memberName specifier)
    ;

expressionStatement
    : expression ';'
    -> ^(EXPR_STMT expression)
    ;

directiveStatement
    : directive (';'!)?
    ;

directive
    : returnDirective
    | throwDirective
    | breakDirective
    | continueDirective
    | retryDirective
    ;

returnDirective
    : 'return' expression?
    -> ^(RET_STMT expression?)
    ;

throwDirective
    : 'throw' expression?
    -> ^(THROW_STMT expression?)
    ;

breakDirective
    : 'break'
    -> ^(BREAK_STMT)
    ;

continueDirective
    : 'continue'
    -> ^(CONTINUE_STMT)
    ;

retryDirective
    : 'retry'
    -> ^(RETRY_STMT)
    ;

memberDeclaration
    : memberHeader
    ( 
      memberParameters memberDefinition
    -> ^(METHOD_DECL memberHeader memberParameters memberDefinition?)
    | memberDefinition 
    -> ^(ATTRIBUTE_DECL memberHeader memberDefinition?)
    )
    ;

memberHeader
    : memberType memberName
    -> ^(MEMBER_TYPE memberType) memberName
    | 'assign' memberName
    -> ^(ATTRIBUTE_SETTER) memberName
    ;

memberType
    : type | 'void' | 'local'
    ;

memberParameters
    : 
        typeParameters? 
        formalParameters+ 
        extraFormalParameters? 
        metatypes? 
        typeConstraints?
    ;

//TODO: should we allow the shortcut style of method
//      definition for a method or getter which returns
//      a parExpression, just like we do for Smalltalk
//      style parameters below?
memberDefinition
    : block | (specifier | initializer)? ';'!
    ;
    
interfaceDeclaration
    :
        'interface'!
        typeName
        typeParameters?
        caseTypes?
        metatypes?
        satisfiedTypes?
        typeConstraints?
        (interfaceBody | typeSpecifier ';'!)
    ;

interfaceBody
    : '{' annotatedDeclaration* '}'
    -> ^(INTERFACE_BODY annotatedDeclaration*)
    ;

classDeclaration
    :
        'class'!
        typeName
        typeParameters?
        formalParameters
        extraFormalParameters?
        caseTypes?
        metatypes?
        extendedType?
        satisfiedTypes?
        typeConstraints?
        (classBody | typeSpecifier? ';'!)
    ;

objectDeclaration
    :
        'object'!
        memberName
        extendedType?
        satisfiedTypes?
        classBody
    ;

classBody
    : '{' annotatedDeclarationOrStatement* '}'
    -> ^(CLASS_BODY annotatedDeclarationOrStatement*)
    ;

extendedType
    : 'extends' type positionalArguments
    -> ^(SUPERCLASS type positionalArguments) 
    ;

satisfiedTypes
    : 'satisfies' type ('&' type)*
    -> ^(SATISFIES_LIST type+)
    ;

abstractedType
    : 'abstracts' type
    -> ^(ABSTRACTS_LIST type)
    ;
    
caseTypes
    : 'of' caseType ('|' caseType)*
    -> ^(CASE_LIST caseType+)
    ;

caseType 
    : type 
    | memberName
    //| (annotations? 'case' memberName) => annotations? 'case' memberName 
    ;

//Support for metatypes
//Note that we don't need this for now
metatypes
    : 'is' type ('&' type)* 
    -> ^(METATYPE_LIST type*)
    ;

typeConstraint
    : 
        'given'!
        typeName 
        typeArguments? 
        formalParameters? 
        caseTypes? 
        metatypes? 
        satisfiedTypes? 
        abstractedType?
    ;
    
typeConstraints
    : typeConstraint+
    -> ^(TYPE_CONSTRAINT_LIST ^(TYPE_CONSTRAINT typeConstraint)+)
    ;

type
    : staticType | runtimeType
    ;

staticType
    : typeNameWithArguments ('.' typeNameWithArguments)* abbreviation*
    -> ^(TYPE typeNameWithArguments+ abbreviation*)
    ;

runtimeType
    : 'subtype' abbreviation*
    -> ^(TYPE 'subtype' abbreviation*)
    /*| parameterName '.' 'subtype' abbreviation*
    -> ^(TYPE parameterName 'subtype' abbreviation*)*/
    ;

abbreviation
    : '?' | '[]' //| '[' dimension ']'
    ;


typeNameWithArguments
    : typeName typeArguments?
    ;
    
annotations
    : annotation+
    -> ^(ANNOTATION_LIST annotation+)
    ;

//TODO: we could minimize backtracking by limiting the 
//kind of expressions that can appear as arguments to
//the annotation
annotation
    : annotationName annotationArguments?
    -> ^(ANNOTATION annotationName annotationArguments?)
    ;

annotationArguments
    : arguments | ( nonstringLiteral | stringLiteral )+
    ;

typeName
    : UIDENTIFIER
    -> ^(TYPE_NAME UIDENTIFIER)
    ;

annotationName
    : LIDENTIFIER
    -> ^(ANNOTATION_NAME LIDENTIFIER)
    ;

memberName 
    : LIDENTIFIER
    -> ^(MEMBER_NAME LIDENTIFIER)
    ;

typeArguments
    : '<' typeArgument (',' typeArgument)* '>'
    -> ^(TYPE_ARG_LIST typeArgument+)
    ;

typeArgument
    : type '...'?
    ; /*| '#'! dimension
    ;

dimension
    : dimensionTerm ('+' dimensionTerm)*
    ;

dimensionTerm
    : (NATURALLITERAL '*')* dimensionAtom
    ;

dimensionAtom
    : NATURALLITERAL 
    | memberName 
    | parenDimension
    ;

parenDimension
    : '(' dimension ')'
    ;*/

typeParameters
    : '<' typeParameter (',' typeParameter)* '>'
    -> ^(TYPE_PARAMETER_LIST typeParameter+)
    ;

typeParameter
    : ordinaryTypeParameter '...'? 
    | '#'! dimensionalTypeParameter
    ;

ordinaryTypeParameter
    : variance? typeName
    -> ^(TYPE_PARAMETER ^(TYPE_VARIANCE variance)? typeName)
    ;

variance
    : 'in' -> IN 
    | 'out' -> OUT
    ;
    
dimensionalTypeParameter
    : memberName
    -> ^(TYPE_PARAMETER memberName)
    ;
    
initializer
    : ':=' expression
    -> ^(INIT_EXPR expression)
    ;

specifier
    : '=' expression
    -> ^(SPEC_EXPR expression)
    ;

typeSpecifier
    : '='! type
    ;

nonstringLiteral
    : NATURALLITERAL
    -> ^(INT_CST NATURALLITERAL)
    | FLOATLITERAL
    -> ^(FLOAT_CST FLOATLITERAL)
    | QUOTEDLITERAL
    -> ^(QUOTE_CST QUOTEDLITERAL)
    | CHARLITERAL
    -> ^(CHAR_CST CHARLITERAL)
    ;

stringExpression
    : (SIMPLESTRINGLITERAL interpolatedExpressionStart) 
        => stringTemplate
    -> ^(STRING_CONCAT stringTemplate)
    | stringLiteral
    ;

stringLiteral
    : SIMPLESTRINGLITERAL
    -> ^(STRING_CST SIMPLESTRINGLITERAL)
    ;

stringTemplate
    : SIMPLESTRINGLITERAL 
    ((interpolatedExpressionStart) => expression SIMPLESTRINGLITERAL)+
    ;

//special rule for syntactic predicates
//this includes every token that could be 
//the beginning of an expression, except 
//for SIMPLESTRINGLITERAL and '['
interpolatedExpressionStart
    : '(' 
    | '{'
    | LIDENTIFIER 
    | UIDENTIFIER 
    | selfReference 
    | 'outer' 
    | 'subtype'
    | nonstringLiteral
    | prefixOperator
    ;

prefixOperator
    : '$' | '-' |'++' | '--' | '~'
    ;

expression
    : assignmentExpression
    -> ^(EXPR assignmentExpression)
    ;

assignmentExpression
    : disjunctionExpression
      ((':='^ | '.='^ | '+='^ | '-='^ | '*='^ | '/='^ | '%='^ | '&='^ | '|='^ | '^='^ | '~='^ | '&&='^ | '||='^ | '?='^) expression )?
    ;

//should '^' have a higher precedence?
disjunctionExpression
    : conjunctionExpression 
      ('||'^ conjunctionExpression)*
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
    : existenceEmptinessExpression
      (('<=>'^ |'<'^ |'>'^ |'<='^ |'>='^ |'in'^ |'is'^|'extends'^|'satisfies'^) existenceEmptinessExpression)?
    ;

existenceEmptinessExpression
    : defaultExpression
    (
        'exists' -> ^(EXISTS_EXPR defaultExpression)
      | 'nonempty' -> ^(NONEMPTY_EXPR defaultExpression) 
      | -> defaultExpression
    )
    ;

defaultExpression
    : rangeIntervalEntryExpression 
      ('?'^ defaultExpression)?
    ;

//I wonder if it would it be cleaner to give 
//'..' a higher precedence than '->'

rangeIntervalEntryExpression
    : additiveExpression
      (('..'^ | '->'^) additiveExpression)?
    ;

additiveExpression
    : multiplicativeExpression
      (('+'^ | '-'^ | '|'^ | '^'^ | '~'^) multiplicativeExpression)*
    ;

multiplicativeExpression 
    : negationComplementExpression
      (('*'^ | '/'^ | '%'^ | '&'^) negationComplementExpression)*
    ;

negationComplementExpression 
    : ('-'^ | '~'^ | '$'^) negationComplementExpression
    | exponentiationExpression
    ;

exponentiationExpression
    : incrementDecrementExpression 
      ('**'^ incrementDecrementExpression)?
    ;

incrementDecrementExpression
    : ('++'^ | '--'^) incrementDecrementExpression
    | primary
    ;

selfReference
    : 'this' | 'super'
    ;

enumeration
    : '{' expressions '}'
    -> ^(ENUM_LIST expressions)
    ;
    
primary
    : base selector*
    -> ^(PRIMARY base selector*)
    ;

postfixOperator
    : '--' | '++'
    ;	

base 
    : nonstringLiteral
    | stringExpression
    | parExpression
    | enumeration
    | selfReference
    | nameAndTypeArguments
    ;
    
selector 
    : memberSelector
    | argumentsWithFunctionalArguments
    -> ^(CALL_EXPR argumentsWithFunctionalArguments)
    | elementSelector
    | postfixOperator 
    -> ^(POSTFIX_EXPR postfixOperator)
    ;

memberSelector
    : memberOperator nameAndTypeArguments
    ;

memberOperator
    : '.' | '?.' | '[].'
    ;

nameAndTypeArguments
    : typeNameAndTypeArguments 
    | memberNameAndTypeArguments 
    | 'subtype' 
    | 'outer'
    ;

typeNameAndTypeArguments
    : typeName ( (typeArguments) => typeArguments )?
      //('[]' | ('?') => '?' )*
    ;

memberNameAndTypeArguments
    : memberName ( (typeArguments) => typeArguments )?
    ;

elementSelector
    : ('?[' | '[') elementsSpec ']'
    -> ^(SUBSCRIPT_EXPR '?['? elementsSpec)
    ;

/*selectorStart
    : '?[' | '['
    | '('
    | '{'
    | memberOperator
    | postfixOperator
    ;*/

elementsSpec
    : additiveExpression ( '...' | '..' additiveExpression )?
    -> ^(LOWER_BOUND additiveExpression) ^(UPPER_BOUND additiveExpression)?	
    ;

argumentsWithFunctionalArguments
    : arguments functionalArgument*
    ;
    
arguments
    : positionalArguments | namedArguments
    ;
    
namedArgument
    : namedSpecifiedArgument | namedFunctionalArgument
    ;

namedFunctionalArgument
    : memberType parameterName formalParameters* block
    ;

namedSpecifiedArgument
    : parameterName specifier ';'!
    ;

namedArgumentStart
    : specificationStart
    | declarationStart
    ;

parameterName
    : LIDENTIFIER
    -> ^(ARG_NAME LIDENTIFIER)
    ;

namedArguments
    : '{' ((namedArgumentStart) => namedArgument)* expressions? '}'
    -> ^(ARG_LIST ^(NAMED_ARG namedArgument)* ^(SEQ_ARG expressions)?)
    ;

parExpression 
    : '('! expression ')'!
    ;
    
positionalArguments
    : '(' ( positionalArgument (',' positionalArgument)* )? ')'
    -> ^(ARG_LIST positionalArgument*)
    ;

positionalArgument
    : (declarationStart) => specialArgument
    | expression
    ;

//a smalltalk-style parameter to a positional parameter
//invocation
functionalArgument
    : functionalArgumentHeader functionalArgumentDefinition
    -> ^(NAMED_ARG functionalArgumentHeader? ^(ANON_METH functionalArgumentDefinition))
    ;
    
functionalArgumentHeader
    : parameterName
    -> ^(ARG_NAME parameterName)
    /*| 'case' '(' expressions ')'
    -> ^(CASE_ITEM expressions)*/
    ;

functionalArgumentDefinition
    : ( (formalParametersStart) => formalParameters )? 
      ( block | parExpression /*| literal | specialValue*/ )
    ;

//Support "T x in arg" in positional argument lists
//Note that we don't need to support this yet
specialArgument
    : variableType memberName (containment | specifier)
    -> ^(SPECIAL_ARG variableType? memberName containment? specifier?)
    //| isCondition
    //| existsCondition
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    -> ^(FORMAL_PARAMETER_LIST ^(FORMAL_PARAMETER formalParameter)*)
    ;

//Support for declaring functional formal parameters outside
//of the parenthesized list
//Note that this is just a TODO in the spec
extraFormalParameters
    : extraFormalParameter+
    -> ^(FORMAL_PARAMETER_LIST ^(FORMAL_PARAMETER extraFormalParameter)+)
    ;

//special rule for syntactic predicates
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
formalParametersStart
    : '(' ( annotatedDeclarationStart | ')' )
    ;
    
// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
formalParameter
    : annotations? 
      formalParameterType 
      (parameterName | 'this') 
      formalParameters*   //for callable parameters
      (   //more exotic stuff
          valueFormalParameter 
        | iteratedFormalParameter 
        | (specifiedFormalParameterStart) => specifiedFormalParameter 
      )? 
      specifier?   //for defaulted parameters
    ;

valueFormalParameter
    : '->' type parameterName
    ;

//Support for "X x in Iterable<X> param" in formal parameter lists
//Note that this is just a TODO in the spec
iteratedFormalParameter
    : 'in' type parameterName
    ;

//Support for "X x = X? param" in formal parameter lists
//Note that this is just a TODO in the spec
specifiedFormalParameter
    : '=' type parameterName
    ;

specifiedFormalParameterStart
    : '=' declarationStart
    ;

extraFormalParameter
    : formalParameterType parameterName formalParameters*
    ;

formalParameterType
    : type '...'? | 'void'
    ;

// Control structures.

condition
    : expression 
    | existsCondition 
    | nonemptyCondition 
    | isCondition 
    | satisfiesCondition
    ;

existsCondition
    : 'exists' controlVariableOrExpression
    -> ^(EXISTS_EXPR controlVariableOrExpression)
    ;
    
nonemptyCondition
    : 'nonempty' controlVariableOrExpression
    -> ^(NONEMPTY_EXPR controlVariableOrExpression)
    ;

isCondition
    : 'is' type ( (memberName '=') => memberName specifier | expression )
    -> ^(IS_EXPR type memberName? specifier? expression?)
    ;

satisfiesCondition
    : 'satisfies' type type
    -> ^(SATISFIES_EXPR type type)
    ;

controlStructure
    : ifElse 
    | switchCaseElse 
    | simpleWhile 
    | doWhile 
    | forFail 
    | tryCatchFinally
    ;
    
ifElse
    : ifBlock elseBlock?
    -> ^(IF_STMT ifBlock elseBlock?)
    ;

ifBlock
    : 'if' '(' condition ')' block
    -> ^(CONDITION condition) ^(IF_TRUE block)
    ;

elseBlock
    : 'else' (ifElse | block)
    -> ^(IF_FALSE block? ifElse?)
    ;

switchCaseElse
    : switchHeader ( '{' cases '}' | cases )
    -> ^(SWITCH_STMT switchHeader cases)
    ;

switchHeader
    : 'switch' '(' expression ')'
    -> ^(SWITCH_EXPR expression)
    ;

cases 
    : caseItem+ defaultCaseItem?
    -> ^(SWITCH_CASE_LIST caseItem+ defaultCaseItem?)
    ;
    
caseItem
    : 'case' '(' caseCondition ')' block
    -> ^(CASE_ITEM caseCondition block)
    ;

defaultCaseItem
    : 'else' block
    -> ^(CASE_DEFAULT block)
    ;

caseCondition
    : expressions 
    | isCaseCondition 
    | satisfiesCaseCondition
    ;

expressions
    : expression (',' expression)*
    -> ^(EXPR_LIST expression+)
    ;

isCaseCondition
    : 'is' type
    -> ^(IS_EXPR type)
    ;

satisfiesCaseCondition
    : 'satisfies' type
    -> ^(SATISFIES_EXPR type)
    ;

forFail
    : forBlock failBlock?
    -> ^(FOR_STMT forBlock failBlock?)
    ;

forBlock
    : 'for' '(' forIterator ')' block
    -> forIterator ^(LOOP_BLOCK block)
    ;

failBlock
    : 'fail' block
    -> ^(FAIL_BLOCK block)
    ;

forIterator
    : variable ('->' variable)? containment
    -> ^(FOR_ITERATOR variable+ containment)
    ;
    
containment
    : 'in' expression
    -> ^(FOR_CONTAINMENT expression)
    ;
    
doWhile
    : doBlock loopCondition ';'
    -> ^(WHILE_STMT doBlock loopCondition)
    ;

simpleWhile
    : loopCondition whileBlock
    -> ^(WHILE_STMT loopCondition whileBlock)
    ;

loopCondition
    : 'while' '(' condition ')'
    -> ^(CONDITION condition)
    ;

whileBlock
    : block
    -> ^(WHILE_BLOCK block)
    ;

doBlock
    : 'do' block
    -> ^(DO_BLOCK block)
    ;

tryCatchFinally
    : tryBlock catchBlock* finallyBlock?
    -> ^(TRY_CATCH_STMT tryBlock catchBlock* finallyBlock?)
    ;

tryBlock
    : 'try' ('(' resource ')')? block
    -> ^(TRY_STMT resource? ^(TRY_BLOCK block))
    ;

catchBlock
    : 'catch' '(' variable ')' block
    -> ^(CATCH_STMT variable ^(CATCH_BLOCK block))
    ;

finallyBlock
    : 'finally' block
    -> ^(FINALLY_BLOCK block)
    ;

resource
    : controlVariableOrExpression
    -> ^(TRY_RESOURCE controlVariableOrExpression)
    ;

controlVariableOrExpression
    : (declarationStart) => variable specifier 
    | expression
    ;

variable
    : variableType memberName formalParameters*
    ;

variableType
    : type | 'local'
    ;

// Lexer

fragment
Digits
    : ('0'..'9')+ ('_' ('0'..'9')+)*
    ;

fragment 
Exponent    
    : ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;

fragment
Magnitude
    : 'k' | 'M' | 'G' | 'T'
    ;

fragment
FractionalMagnitude
    : 'm' | 'u' | 'n' | 'p'
    ;
    
fragment ELLIPSIS: '...';
fragment RANGE: '..';
fragment DOT: '.' ;
fragment FLOATLITERAL :;
NATURALLITERAL
    : Digits
      ( Magnitude | { input.LA(2) != '.' }? => '.' Digits (Exponent|Magnitude|FractionalMagnitude)? { $type = FLOATLITERAL; } )?
    | '.' ( '..' { $type = ELLIPSIS; } | '.'  { $type = RANGE; } | { $type = DOT; } )
    ;
    
fragment SPREAD:'[].';
fragment LBRACKET:'[';
fragment ARRAY:'[]';
BRACKETS
    : '['
    ( 
      ( { input.LA(1) == ']' && input.LA(2) == '.' && input.LA(3) != '.' }? => '].' { $type = SPREAD; } )
    | ( { input.LA(1) == ']' }? => ']' { $type = ARRAY; } )
    | { $type = LBRACKET; } 
    )
    ;    

fragment SAFEMEMBER:'?.';
fragment SAFEINDEX:'?[';
fragment QMARK:'?';
QMARKS
    : '?'
    (
      ( { input.LA(1) == '[' && input.LA(2) != ']' }? => '[' { $type = SAFEINDEX; } )
    | ( { input.LA(1) == '.' && input.LA(2) != '.' }? => '.' { $type = SAFEMEMBER; } ) 
    | { $type = QMARK; }
    )
    ;

CHARLITERAL
    :   '`' ( ~ NonCharacterChars | EscapeSequence ) '`'
    ;

fragment
NonCharacterChars
    :    '`' | '\\' | '\t' | '\n' | '\f' | '\r' | '\b'
    ;

QUOTEDLITERAL
    :   '\'' QuotedLiteralPart '\''
    ;

fragment
QuotedLiteralPart
    : ~('\'')*
    ;

SIMPLESTRINGLITERAL
    :   '"' StringPart '"'
    ;

fragment
NonStringChars
    :    '\\' | '"'
    ;

fragment
StringPart
    : ( ~ /* NonStringChars*/ ('\\' | '"')
    | EscapeSequence) *
    ;
    
fragment
EscapeSequence 
    :   '\\' 
        (
            'b'
        |   't'
        |   'n'
        |   'f'
        |   'r'
        |   '\\'
        |   '"'
        |   '\''
        |   '`'
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
    :   '/*'
        {
            $channel=HIDDEN;
        }
        (    ~('/'|'*')
        |    ('/' ~'*') => '/'
        |    ('*' ~'/') => '*'
        |    MULTI_COMMENT
        )*
        '*/'
        ;
ABSTRACTS
    : 'abstracts'
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

CONTINUE
    :   'continue'
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

FAIL
    :   'fail'
    ;

GIVEN
    :   'given'
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

IMPLICIT
    :   'implicit'
    ;

INTERFACE
    :   'interface'
    ;

LOCAL
    :   'local'
    ;

NONEMPTY
    :   'nonempty'
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

OUTER
    :   'outer'
    ;

OBJECT
    :   'object'
    ;

OF
    :   'of'
    ;

OUT
    :   'out'
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

UNION
    : 'union'
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

BITWISNOTEQ
    :   '~='
    ;
REMAINDEREQ
    :   '%='
    ;

QMARKEQ
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
