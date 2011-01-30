grammar Ceylon;

options {
    memoize=false;
    output=AST;
}

tokens {
    ANNOTATION;
    ANNOTATION_LIST;
    ALIAS_DECL;
    ANNOTATION_NAME;
    ATTRIBUTE_ARG;
    ATTRIBUTE_DECL;
    ATTRIBUTE_GETTER;
    ATTRIBUTE_SETTER;
    BLOCK;
    BREAK_STMT;
    CALL_EXPR;
    CASE_DEFAULT;
    CASE_ITEM;
    CASE_LIST;
    CATCH_BLOCK;
    CATCH_STMT;
    CHAR_CST;
    CLASS_BODY;
    CLASS_DECL;
    CONDITION;
    CONTINUE_STMT;
    DO_BLOCK;
    DO_ITERATOR;
    EXPR;
    EXPR_LIST;
    EXPR_STMT;
    FAIL_BLOCK;
    FINALLY_BLOCK;
    FOR_CONTAINMENT;
    FOR_ITERATOR;
    FOR_STMT;
    PARAM_NAME;
    PARAM;
    PARAM_LIST;
    IF_FALSE;
    IF_STMT;
    IF_TRUE;
    IMPORT_DECL;
    IMPORT_LIST;
    IMPORT_WILDCARD;
    IMPORT_PATH;
    IMPORT_ELEM;
    INIT_EXPR;
    INLINE_METHOD_ARG;
    INLINE_ARG_LIST;
    INTERFACE_DECL;
    INTERFACE_BODY;
    MEMBER_DECL;
    MEMBER_NAME;
    BROKEN_MEMBER_BODY;
    METHOD_ARG;
    METHOD_DECL;
    METATYPE_LIST;
    NAMED_ARG;
    NAMED_ARG_LIST;
    OBJECT_DECL;
    OBJECT_ARG;
    POS_ARG;
    POS_ARG_LIST;
    POSTFIX_EXPR;
    PRIMARY;
    RET_STMT;
    RETRY_STMT;
    SATISFIES_EXPR;
    SEQ_ARG;
    SPECIAL_ARG;
    THROW_STMT;
    TRY_BLOCK;
    TRY_CATCH_STMT;
    TRY_RESOURCE;
    TRY_STMT;
    TYPE_ARG_LIST;
    TYPE_DECL;
    TYPE_NAME;
    TYPE_PARAMETER_LIST;
    TYPE_SPECIFIER;
    WHILE_BLOCK;
    WHILE_STMT;
    SWITCH_STMT;
    SWITCH_EXPR;
    SWITCH_CASE_LIST;
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
    SEQUENCE_ENUM;
    SPEC_ARG;
    SPEC_EXPR;
    SPEC_STMT;
    SUPERCLASS;
    TYPE_VARIANCE;
    TYPE_PARAMETER;
    STRING_CONCAT;
    INT_CST;
    FLOAT_CST;
    STRING_CST;
    QUOTE_CST;
    LOOP_BLOCK;
    EXISTS_EXPR;
    NONEMPTY_EXPR;
    IS_EXPR;
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
    : '{' annotatedDeclarationOrStatement* directiveStatement? '}'
    -> ^(BLOCK annotatedDeclarationOrStatement* directiveStatement?)
    ;

//This rule accounts for the problem that we
//can't tell whether a member body is a block
//or a named argument list until after we
//finish parsing it
memberBody
    : (namedArguments) => namedArguments //first try to match with no directives or control structures
    | (block) => block //if there is a "return" directives control structures, it must be a block
    //if it doesn't match as a block or as a named argument
    //list, then there must be an error somewhere, so parse
    //it again looking for the error
    | '{' brokenMemberBody? '}' 
    -> ^(BROKEN_MEMBER_BODY brokenMemberBody?)
    ;

brokenMemberBody
    : (annotatedDeclarationStart) => annotatedDeclaration brokenMemberBody?
    | ( specificationStatement brokenMemberBody?
    | controlStructure brokenMemberBody?
    | expressionStatementOrList
    | directiveStatement )
    ;
    
expressionStatementOrList
    : expression 
      (
        ';' brokenMemberBody?
      -> ^(EXPR_STMT expression) brokenMemberBody?
      | (',' expression)* 
      -> ^(EXPR_LIST expression+)
      )
    ;
    
annotatedDeclarationOrStatement
    : (annotatedDeclarationStart) => annotatedDeclaration
    | (controlStructure | expressionStatement | specificationStatement)
    ;

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

//special rule for syntactic predicate
//to distinguish between an annotation
//and an expression statement
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

//special rule for syntactic predicates
//that distinguish declarations from
//expressions
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

//TODO: should we allow the shortcut style of method
//      definition for a method or getter which returns
//      a parExpression, just like we do for Smalltalk
//      style parameters below?
memberDeclaration
    : objectDeclaration
    -> ^(OBJECT_DECL objectDeclaration)
    | setterDeclaration
    -> ^(ATTRIBUTE_SETTER setterDeclaration)
    | voidMethodDeclaration
    -> ^(METHOD_DECL voidMethodDeclaration)
    | typedMethodOrAttributeDeclaration
    ;

objectDeclaration
    : 'object' memberName extendedType? satisfiedTypes? classBody
    ;

voidMethodDeclaration
    : 'void' memberName methodParameters (block | specifier? ';'!)
    ;

setterDeclaration
    : 'assign' memberName block
    ;

typedMethodOrAttributeDeclaration
    : inferrableType memberName
    ( 
      methodParameters (memberBody | specifier? ';')
    -> ^(METHOD_DECL inferrableType memberName methodParameters memberBody? specifier?)
    | (specifier | initializer)? ';'
    -> ^(ATTRIBUTE_DECL inferrableType memberName specifier? initializer?)
    | memberBody
    -> ^(ATTRIBUTE_GETTER inferrableType memberName memberBody)      
    )
    ;

inferrableType
    : type | 'local'
    ;

methodParameters
    : 
        typeParameters? 
        formalParameters+ 
        extraFormalParameters? 
        metatypes? 
        typeConstraints?
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
    : typeNameWithArguments ('.' typeNameWithArguments)* typeAbbreviation*
    -> ^(TYPE typeNameWithArguments+ typeAbbreviation*)
    ;

runtimeType
    : 'subtype' typeAbbreviation*
    -> ^(TYPE 'subtype' typeAbbreviation*)
    /*| parameterName '.' 'subtype' abbreviation*
    -> ^(TYPE parameterName 'subtype' abbreviation*)*/
    ;

typeAbbreviation
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
    : arguments | literalArguments
    ;

literalArguments
    : literalArgument+
    -> ^(POS_ARG_LIST ^(POS_ARG literalArgument)+)
    ;
    
literalArgument
    : nonstringLiteral | stringLiteral
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
    : '=' type
    -> ^(TYPE_SPECIFIER type)
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

//special rule for syntactic predicate
//to distinguish an interpolated expression
//in a string template
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
    -> ^(SEQUENCE_ENUM expressions)
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
    -> ^(SUBSCRIPT_EXPR elementSelector)
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
    : (typeName|memberName) ((typeArgumentsStart) => typeArguments)?
    | 'subtype' 
    | 'outer'
    ;

//special rule for syntactic predicate to 
//determine if we have a < operator, or a
//type argument list
typeArgumentsStart
    : '<' 
      (UIDENTIFIER ('.' UIDENTIFIER)* | 'subtype') 
      typeAbbreviation*
      ('>'|'<'|','|'...')
    ;

elementSelector
    : ('?[' | '[') elementsSpec ']'
    ;

elementsSpec
    : additiveExpression ( '...' | '..' additiveExpression )?
    -> ^(LOWER_BOUND additiveExpression) ^(UPPER_BOUND additiveExpression)?	
    ;

argumentsWithFunctionalArguments
    : arguments functionalArguments?
    ;

functionalArguments
    : functionalArgument+
    -> ^(INLINE_ARG_LIST functionalArgument+)
    ;

arguments
    : positionalArguments | namedArguments
    ;
    
namedArgument
    : namedSpecifiedArgument | namedArgumentDeclaration
    ;

namedArgumentDeclaration
    : objectArgument
    | voidMethodArgument
    | typedMethodOrGetterArgument
    ;
    
objectArgument
    : 'object' parameterName extendedType? satisfiedTypes? classBody
    -> parameterName ^(OBJECT_ARG parameterName extendedType? satisfiedTypes? classBody)
    ;

voidMethodArgument
    : 'void' parameterName formalParameters+ block
    -> parameterName ^(METHOD_ARG 'void' parameterName formalParameters+ block)
    ;

typedMethodOrGetterArgument
    : inferrableType parameterName
    ( 
      (formalParameters+ memberBody)
    -> parameterName ^(METHOD_ARG inferrableType parameterName formalParameters+ memberBody)
    | memberBody
    -> parameterName ^(ATTRIBUTE_ARG inferrableType parameterName memberBody)      
    )
    ;

namedSpecifiedArgument
    : parameterName specifier ';'
    -> ^(SPEC_ARG parameterName specifier)
    ;

//special rule for syntactic predicate
//to distinguish between a named argument
//and a sequenced argument
namedArgumentStart
    : specificationStart | declarationStart
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER '='
    ;

parameterName
    : LIDENTIFIER
    -> ^(PARAM_NAME LIDENTIFIER)
    ;

namedArguments
    : '{' ((namedArgumentStart) => namedArgument)* expressions? '}'
    -> ^(NAMED_ARG_LIST ^(NAMED_ARG namedArgument)* ^(SEQ_ARG expressions)?)
    ;

parExpression 
    : '('! expression ')'!
    ;
    
positionalArguments
    : '(' ( positionalArgument (',' positionalArgument)* )? ')'
    -> ^(POS_ARG_LIST ^(POS_ARG positionalArgument)*)
    ;

positionalArgument
    : (declarationStart) => specialArgument
    | expression
    ;

//a smalltalk-style parameter to a positional parameter
//invocation
functionalArgument
    : parameterName functionalArgumentDefinition
    -> ^(NAMED_ARG parameterName ^(INLINE_METHOD_ARG parameterName functionalArgumentDefinition))
    ;

functionalArgumentDefinition
    : ( (formalParametersStart) => formalParameters )? 
      (block | parExpression)
    ;

//Support "T x in arg" in positional argument lists
//Note that we don't need to support this yet
specialArgument
    : inferrableType memberName (containment | specifier)
    -> ^(SPECIAL_ARG inferrableType memberName containment? specifier?)
    //| isCondition
    //| existsCondition
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    -> ^(PARAM_LIST ^(PARAM formalParameter)*)
    ;

//Support for declaring functional formal parameters outside
//of the parenthesized list
//Note that this is just a TODO in the spec
extraFormalParameters
    : extraFormalParameter+
    -> ^(PARAM_LIST ^(PARAM extraFormalParameter)+)
    ;

//special rule for syntactic predicate
//to distinguish between a formal 
//parameter list and a parenthisized body 
//of an inline callable argument
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

//special rule for syntactic predicate
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
    : 'is' type (memberName specifier | expression)
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
    : inferrableType memberName formalParameters*
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
