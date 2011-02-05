grammar Ceylon;

options {
    memoize=false;
    output=AST;
}

tokens {
    ANNOTATION;
    ANNOTATION_LIST;
    ANNOTATION_NAME;
    ATTRIBUTE_ARGUMENT;
    ATTRIBUTE_DECLARATION;
    ATTRIBUTE_GETTER;
    ATTRIBUTE_SETTER;
    BLOCK;
    CALL_EXPRESSION;
    CLASS_BODY;
    CLASS_DECLARATION;
    CONDITION;
    COMPILATION_UNIT;
    //DIRECTIVE;
    EXPRESSION;
    EXPRESSION_LIST;
    EXPRESSION_STATEMENT;
    FOR_ITERATOR;
    FOR_STATEMENT;
    PARAMETER_NAME;
    PARAMETER;
    PARAMETER_LIST;
    IF_STATEMENT;
    IMPORT_DECLARATION;
    IMPORT_LIST;
    IMPORT_WILDCARD;
    IMPORT_PATH;
    IMPORT_ELEM;
    IMPORT_ALIAS;
    INITIALIZER_EXPRESSION;
    INTERFACE_DECLARATION;
    INTERFACE_BODY;
    MEMBER_DECLARATION;
    MEMBER_NAME;
    MEMBER_EXPRESSION;
    BROKEN_MEMBER_BODY;
    METHOD_ARGUMENT;
    METHOD_DECLARATION;
    METATYPES;
    NAMED_ARGUMENT;
    NAMED_ARGUMENT_LIST;
    OBJECT_DECLARATION;
    OBJECT_ARGUMENT;
    POSITIONAL_ARGUMENT;
    POSITIONAL_ARGUMENT_LIST;
    POSTFIX_EXPRESSION;
    PRIMARY;
    SATISFIES_EXPRESSION;
    SEQUENCED_ARGUMENT;
    SEQUENCED_TYPE;
    SEQUENCED_TYPE_PARAMETER;
    SPECIAL_ARGUMENT;
    TRY_CATCH_STATEMENT;
    TRY_RESOURCE;
    TYPE_ARGUMENT_LIST;
    TYPE_DECLARATION;
    TYPE_NAME;
    TYPE_PARAMETER_LIST;
    TYPE_SPECIFIER;
    TYPE_REFERENCE;
    MEMBER_REFERENCE;
    WHILE_STATEMENT;
    DO_WHILE_STATEMENT;
    SWITCH_STATEMENT;
    SWITCH_CASE_LIST;
    TYPE_CONSTRAINT_LIST;
    TYPE;
    TYPE_CONSTRAINT;
    TYPE_DECLARATION;
    ABSTRACTED_TYPE;
    EXTENDED_TYPE;
    SATISFIED_TYPES;
    CASE_TYPES;
    SUBSCRIPT_EXPRESSION;
    LOWER_BOUND;
    UPPER_BOUND;
    SELECTOR_LIST;
    SEQUENCE_ENUMERATION;
    SPECIFIED_ARGUMENT;
    SPECIFIER_EXPRESSION;
    SPECIFIER_STATEMENT;
    //STATEMENT;
    TYPE_VARIANCE;
    TYPE_PARAMETER;
    STRING_TEMPLATE;
}

@parser::header { package com.redhat.ceylon.compiler.parser; }
@lexer::header { package com.redhat.ceylon.compiler.parser; }

compilationUnit
    : importDeclaration*
      annotatedDeclaration+
      EOF
    -> ^(COMPILATION_UNIT ^(IMPORT_LIST importDeclaration+)? annotatedDeclaration+)
    ;

typeDeclaration
    : classDeclaration
    -> ^(CLASS_DECLARATION classDeclaration)
    | interfaceDeclaration
    -> ^(INTERFACE_DECLARATION interfaceDeclaration)
    ;

importDeclaration
    : 'import' packagePath '{' importElements '}'
      -> ^(IMPORT_DECLARATION packagePath importElements)
    ;

importElements
    : importElement (','! importElement)* (','! importWildcard)?
    | importWildcard
    ;

importElement
    : IMPLICIT? importAlias? importedName
    -> ^(IMPORT_ELEM IMPLICIT? importAlias? importedName)
    ;

importWildcard
    : ELLIPSIS
    -> ^(IMPORT_WILDCARD[$ELLIPSIS])
    ;

importAlias
    : 'local' importedName '='
    -> ^(IMPORT_ALIAS importedName)
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
memberBody[Object mt] options { backtrack=true; memoize=true; }
    : namedArguments //first try to match with no directives or control structures
    -> ^(BLOCK /*^(DIRECTIVE*/ ^(RETURN ^(EXPRESSION ^(PRIMARY ^(CALL_EXPRESSION ^(PRIMARY ^(TYPE_REFERENCE {((CommonTree)$mt).getChild(0)})) namedArguments)))))
    | block //if there is a "return" directive or control structure, it must be a block
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
      -> ^(EXPRESSION_STATEMENT expression) brokenMemberBody?
      | (',' expression)* 
      -> ^(EXPRESSION_LIST expression+)
      )
    ;
    
annotatedDeclarationOrStatement options {memoize=true;}
    : (annotatedDeclarationStart) => annotatedDeclaration
    | statement //-> ^(STATEMENT statement)
    ;

statement 
    : controlStructure 
    | expressionStatement 
    | specificationStatement
    ;

annotatedDeclaration
    :
    annotations?
    ( 
        memberDeclaration 
      -> ^(MEMBER_DECLARATION memberDeclaration annotations?)
      | typeDeclaration 
      -> ^(TYPE_DECLARATION typeDeclaration annotations?)
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
    | type ('...' | LIDENTIFIER)
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
    -> ^(SPECIFIER_STATEMENT ^(MEMBER_REFERENCE memberName) specifier)
    ;

expressionStatement
    : expression ';'
    -> ^(EXPRESSION_STATEMENT expression)
    ;

directiveStatement
    : directive ';'?
    //-> ^(DIRECTIVE directive)
    ;

directive
    : returnDirective
    | throwDirective
    | breakDirective
    | continueDirective
    | retryDirective
    ;

returnDirective
    : 'return'^ expression?
    ;

throwDirective
    : 'throw'^ expression?
    ;

breakDirective
    : 'break'^
    ;

continueDirective
    : 'continue'^
    ;

retryDirective
    : 'retry'^
    ;

//TODO: should we allow the shortcut style of method
//      definition for a method or getter which returns
//      a parExpression, just like we do for Smalltalk
//      style parameters below?
memberDeclaration
    : objectDeclaration
    -> ^(OBJECT_DECLARATION objectDeclaration)
    | setterDeclaration
    -> ^(ATTRIBUTE_SETTER setterDeclaration)
    | voidMethodDeclaration
    -> ^(METHOD_DECLARATION voidMethodDeclaration)
    | typedMethodOrAttributeDeclaration
    ;

objectDeclaration
    : 'object'! memberName extendedType? satisfiedTypes? classBody
    ;

voidMethodDeclaration
    : 'void' memberName methodParameters (block | specifier? ';'!)
    ;

setterDeclaration
    : 'assign'! memberName block
    ;

typedMethodOrAttributeDeclaration
    : inferrableType memberName
    ( 
      methodParameters (memberBody[$inferrableType.tree] | specifier? ';')
    -> ^(METHOD_DECLARATION inferrableType memberName methodParameters memberBody? specifier?)
    | (specifier | initializer)? ';'
    -> ^(ATTRIBUTE_DECLARATION inferrableType memberName specifier? initializer?)
    | memberBody[$inferrableType.tree]
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
    : 'extends'^ type positionalArguments
    ;

satisfiedTypes
    : 'satisfies'^ type ('&'! type)*
    ;

abstractedType
    : 'abstracts'^ type
    ;
    
caseTypes
    : 'of'^ caseType ('|'! caseType)*
    ;

caseType 
    : type | memberName
    //| (annotations? 'case' memberName) => annotations? 'case' memberName 
    ;

//Support for metatypes
//Note that we don't need this for now
metatypes
    : 'is' type ('&' type)* 
    -> ^(METATYPES type*)
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
    : (unabbreviatedType -> unabbreviatedType) 
      (typeAbbreviation -> ^(TYPE typeAbbreviation ^(TYPE_ARGUMENT_LIST $type)))*
    ;

unabbreviatedType
    : typeNameWithArguments ('.' typeNameWithArguments)* 
    -> ^(TYPE typeNameWithArguments+ )
    | SUBTYPE 
    -> ^(TYPE SUBTYPE)
    /*| parameterName '.' 'subtype' abbreviation*
    -> ^(TYPE parameterName 'subtype' abbreviation*)*/
    ;

typeAbbreviation
    : DEFAULT_OP
    -> ^(TYPE_NAME UIDENTIFIER[$DEFAULT_OP,"Optional"])
    | ARRAY 
    -> ^(TYPE_NAME UIDENTIFIER[$ARRAY,"Sequence"])
    //| '[' dimension ']'
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
    -> ^(POSITIONAL_ARGUMENT_LIST ^(POSITIONAL_ARGUMENT literalArgument)+)
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
    -> ^(TYPE_ARGUMENT_LIST typeArgument+)
    ;

typeArgument
    : type ( '...' -> ^(SEQUENCED_TYPE type) | -> type ) 
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
    : variance? typeName
    -> ^(TYPE_PARAMETER ^(TYPE_VARIANCE variance)? typeName)
    | typeName '...'
    -> ^(SEQUENCED_TYPE_PARAMETER typeName)
    //| '#'! dimensionalTypeParameter
    ;

variance
    : 'in' | 'out'
    ;
    
dimensionalTypeParameter
    : memberName
    -> ^(TYPE_PARAMETER memberName)
    ;
    
initializer
    : ':=' expression
    -> ^(INITIALIZER_EXPRESSION expression)
    ;

specifier
    : '=' expression
    -> ^(SPECIFIER_EXPRESSION expression)
    ;

typeSpecifier
    : '=' type
    -> ^(TYPE_SPECIFIER type)
    ;

nonstringLiteral
    : NATURAL_LITERAL
    | FLOAT_LITERAL
    | QUOTED_LITERAL
    | CHAR_LITERAL
    ;

stringExpression
    : (STRING_LITERAL interpolatedExpressionStart) 
        => stringTemplate
    -> ^(STRING_TEMPLATE stringTemplate)
    | stringLiteral
    ;

stringLiteral
    : STRING_LITERAL
    ;

stringTemplate
    : STRING_LITERAL 
      ((interpolatedExpressionStart) => expression STRING_LITERAL)+
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
    -> ^(EXPRESSION assignmentExpression)
    ;

assignmentExpression
    : disjunctionExpression
      ((':='^ | '.='^ | '+='^ | '-='^ | '*='^ | '/='^ | '%='^ | '&='^ | '|='^ | '^='^ | '~='^ | '&&='^ | '||='^ | '?='^) assignmentExpression )?
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
        EXISTS -> ^(EXISTS defaultExpression)
      | NONEMPTY -> ^(NONEMPTY defaultExpression) 
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
      ('**'^ exponentiationExpression)?
    ;

incrementDecrementExpression
    : ('++'^ | '--'^) incrementDecrementExpression
    | primary -> ^(PRIMARY primary)
    ;

selfReference
    : 'this' | 'super'
    ;

enumeration
    : '{' expressions '}'
    -> ^(SEQUENCE_ENUMERATION expressions)
    ;

primary
    : (base -> base)
    ( 
        memberSelector
      -> ^(MEMBER_EXPRESSION ^(PRIMARY $primary) memberSelector)
      | argumentsWithFunctionalArguments
      -> ^(CALL_EXPRESSION ^(PRIMARY $primary) argumentsWithFunctionalArguments)
      | elementSelector
      -> ^(SUBSCRIPT_EXPRESSION ^(PRIMARY $primary) elementSelector)
      | postfixOperator 
      -> ^(POSTFIX_EXPRESSION ^(PRIMARY $primary) postfixOperator)
    )*
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
    | typeReference
    | memberReference
    ;

memberSelector
    : ('.' | '?.' | '[].') (memberReference | typeReference)
    ;

typeReference
    : typeInExpression ( (typeInExpressionStart) => '.' typeInExpression )*
    -> ^(TYPE_REFERENCE typeInExpression+)
    ;

memberReference
    : memberInExpression
    -> ^(MEMBER_REFERENCE memberInExpression)
    | 'subtype' 
    | 'outer'
    ;

memberInExpression
    : memberName ((typeArgumentsStart) => typeArguments)?
    ;

typeInExpression
    : typeName ((typeArgumentsStart) => typeArguments)?
    ;

//special rule for syntactic predicate so
//that qualified type names are consumed
//greedily into a single TYPE_REF node
typeInExpressionStart
    : '.' typeName
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
    -> ^(NAMED_ARGUMENT_LIST functionalArgument+)
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
    -> parameterName ^(OBJECT_ARGUMENT parameterName extendedType? satisfiedTypes? classBody)
    ;

voidMethodArgument
    : VOID_MODIFIER parameterName formalParameters+ block
    -> parameterName ^(METHOD_ARGUMENT VOID_MODIFIER parameterName formalParameters+ block)
    ;

typedMethodOrGetterArgument
    : inferrableType parameterName
    ( 
      (formalParameters+ memberBody[$inferrableType.tree])
    -> parameterName ^(METHOD_ARGUMENT inferrableType parameterName formalParameters+ memberBody)
    | memberBody[$inferrableType.tree]
    -> parameterName ^(ATTRIBUTE_ARGUMENT inferrableType parameterName memberBody)      
    )
    ;

namedSpecifiedArgument
    : parameterName specifier ';'
    -> ^(SPECIFIED_ARGUMENT parameterName specifier)
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
    -> ^(PARAMETER_NAME LIDENTIFIER)
    ;

namedArguments
    : '{' ((namedArgumentStart) => namedArgument)* expressions? '}'
    -> ^(NAMED_ARGUMENT_LIST ^(NAMED_ARGUMENT namedArgument)* ^(SEQUENCED_ARGUMENT expressions)?)
    ;

parExpression 
    : '('! expression ')'!
    ;
    
positionalArguments
    : '(' ( positionalArgument (',' positionalArgument)* )? ')'
    -> ^(POSITIONAL_ARGUMENT_LIST ^(POSITIONAL_ARGUMENT positionalArgument)*)
    ;

positionalArgument
    : (declarationStart) => specialArgument
    | expression
    ;

//a smalltalk-style parameter to a positional parameter
//invocation
functionalArgument
    : parameterName functionalArgumentDefinition
    -> ^(NAMED_ARGUMENT parameterName ^(METHOD_ARGUMENT 'local' parameterName functionalArgumentDefinition))
    ;

functionalArgumentDefinition
    : functionalArgumentParameters functionalArgumentBody
    ;

functionalArgumentParameters
    : (formalParametersStart) => formalParameters
    | -> ^(PARAMETER_LIST)
    ;

functionalArgumentBody
    : block
    | parExpression -> ^(BLOCK /*^(DIRECTIVE*/ ^(RETURN parExpression))
    ;

//Support "T x in arg" in positional argument lists
//Note that we don't need to support this yet
specialArgument
    : inferrableType memberName (containment | specifier)
    -> ^(SPECIAL_ARGUMENT inferrableType memberName containment? specifier?)
    //| isCondition
    //| existsCondition
    ;

formalParameters
    : '(' (formalParameter (',' formalParameter)*)? ')'
    -> ^(PARAMETER_LIST ^(PARAMETER formalParameter)*)
    ;

//Support for declaring functional formal parameters outside
//of the parenthesized list
//Note that this is just a TODO in the spec
extraFormalParameters
    : extraFormalParameter+
    -> ^(PARAMETER_LIST ^(PARAMETER extraFormalParameter)+)
    ;

//special rule for syntactic predicate
//to distinguish between a formal 
//parameter list and a parenthesized body 
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
    : type ( '...' -> ^(SEQUENCED_TYPE type) | -> type )
    | VOID_MODIFIER -> VOID_MODIFIER
    ;

// Control structures.

controlCondition
    : condition
    -> ^(CONDITION condition)
    ;

condition
    : expression 
    | existsCondition 
    | nonemptyCondition 
    | isCondition 
    | satisfiesCondition
    ;

existsCondition
    : 'exists'^ controlVariableOrExpression
    ;
    
nonemptyCondition
    : 'nonempty'^ controlVariableOrExpression
    ;

isCondition
    : 'is'^ type (memberName specifier | expression)
    ;

satisfiesCondition
    : 'satisfies'^ type type
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
    -> ^(IF_STATEMENT ifBlock elseBlock?)
    ;

ifBlock
    : 'if'^ '('! controlCondition ')'! block
    ;

elseBlock
    : 'else'^ (ifElse | block)
    ;

switchCaseElse
    : switchHeader ( '{' cases '}' | cases )
    -> ^(SWITCH_STATEMENT switchHeader cases)
    ;

switchHeader
    : 'switch'^ '('! expression ')'!
    ;

cases 
    : caseItem+ defaultCaseItem?
    -> ^(SWITCH_CASE_LIST caseItem+ defaultCaseItem?)
    ;
    
caseItem
    : 'case'^ '('! caseCondition ')'! block
    ;

defaultCaseItem
    : 'else'^ block
    ;

caseCondition
    : expressions 
    | isCaseCondition 
    | satisfiesCaseCondition
    ;

expressions
    : expression (',' expression)*
    -> ^(EXPRESSION_LIST expression+)
    ;

isCaseCondition
    : 'is'^ type
    ;

satisfiesCaseCondition
    : 'satisfies'^ type
    ;

forFail
    : forBlock failBlock?
    -> ^(FOR_STATEMENT forBlock failBlock?)
    ;

forBlock
    : 'for'^ '('! forIterator ')'! block
    ;

failBlock
    : 'fail'^ block
    ;

forIterator
    : variable ('->' variable)? containment
    -> ^(FOR_ITERATOR variable+ containment)
    ;
    
containment
    : 'in'^ expression
    ;
    
doWhile
    : doBlock whileCondition ';'
    -> ^(DO_WHILE_STATEMENT doBlock whileCondition)
    ;

whileCondition
    : 'while'^ '('! controlCondition ')'!
    ;

simpleWhile
    : whileBlock
    -> ^(WHILE_STATEMENT whileBlock)
    ;

whileBlock
    : 'while'^ '('! controlCondition ')'! block
    ;

doBlock
    : 'do'^ block
    ;

tryCatchFinally
    : tryBlock catchBlock* finallyBlock?
    -> ^(TRY_CATCH_STATEMENT tryBlock catchBlock* finallyBlock?)
    ;

tryBlock
    : 'try'^ ('('! resource ')'!)? block
    ;

catchBlock
    : 'catch'^ '('! variable ')'! block
    ;

finallyBlock
    : 'finally'^ block
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
    : 'k' | 'M' | 'G' | 'T' | 'P'
    ;

fragment
FractionalMagnitude
    : 'm' | 'u' | 'n' | 'p' | 'f'
    ;
    
fragment FLOAT_LITERAL :;
//distinguish a float literal from 
//a natural literals followed by a
//member invocation or range op
NATURAL_LITERAL
    : Digits
      ( 
        ('.' ('0'..'9')) => 
        '.' Digits (Exponent|Magnitude|FractionalMagnitude)? { $type = FLOAT_LITERAL; } 
      | Magnitude?
      )
    ;
    
fragment SPREAD_OP: '[].';
fragment ARRAY: '[]';
fragment LBRACKET: '[';
//distinguish the spread operator "x[]."
//from a sequenced type "T[]..."
LBRACKETS
    : '['
    (
      (']' '.' ~'.') => '].' { $type = SPREAD_OP; }
    | (']') => ']' { $type = ARRAY; }
    | { $type = LBRACKET; }
    )
    ;

fragment SAFE_MEMBER: '?.';
fragment SAFE_INDEX: '?[';
fragment DEFAULT_OP: '?';
//distinguish the safe index operator "x?[i]"
//from an abbreviated type "T?[]"
//and the safe member operator "x?.y" from 
//the sequenced type "T?..."
QMARKS
    : '?'
    (
      ('[' ~']') => '[' { $type = SAFE_INDEX; }
    | ('.' ~'.') => '.' { $type = SAFE_MEMBER; }
    | { $type = DEFAULT_OP; }
    )
    ;

CHAR_LITERAL
    :   '`' ( ~ NonCharacterChars | EscapeSequence ) '`'
    ;

fragment
NonCharacterChars
    :    '`' | '\\' | '\t' | '\n' | '\f' | '\r' | '\b'
    ;

QUOTED_LITERAL
    :   '\'' QuotedLiteralPart '\''
    ;

fragment
QuotedLiteralPart
    : ~('\'')*
    ;

STRING_LITERAL
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
        (    ~('/'|'*')
        |    ('/' ~'*') => '/'
        |    ('*' ~'/') => '*'
        |    MULTI_COMMENT
        )*
        '*/'
        {
            skip();
        }
        ;
        
ABSTRACTED_TYPE
    :   'abstracts'
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

EXTENDED_TYPE
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

SATISFIED_TYPES
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

CASE_TYPES
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
    :   'retry'
    ;

VOID_MODIFIER
    :   'void'
    ;

WHILE
    :   'while'
    ;

ELLIPSIS
    :   '...';

RANGE_OP
    :   '..';

DOT
    :   '.' ;

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

SEMICOLON
    :   ';'
    ;

COMMA
    :   ','
    ;

HASH
    :   '#'
    ;

COLON
    :   ':'
    ;
    
SPECIFY
    :   '='
    ;

FORMAT_OP
    :   '$'
    ;

NOT_OP
    :   '!'
    ;

COMPLEMENT_OP
    :   '~'
    ;

ASSIGN_OP
    :   ':='
    ;

EQUAL_OP
    :   '=='
    ;

IDENTICAL_OP
    :   '==='
    ;

AND_OP
    :   '&&'
    ;

OR_OP
    :   '||'
    ;

INCREMENT_OP
    :   '++'
    ;

DECREMENT_OP
    :   '--'
    ;

SUM_OP
    :   '+'
    ;

DIFFERENCE_OP
    :   '-'
    ;

PRODUCT_OP
    :   '*'
    ;

QUOTIENT_OP
    :   '/'
    ;

INTERSECTION_OP
    :   '&'
    ;

UNION_OP
    :   '|'
    ;

XOR_OP
    :   '^'
    ;

REMAINDER_OP
    :   '%'
    ;

NOT_EQUAL_OP
    :   '!='
    ;

LARGER_OP
    :   '>'
    ;

SMALLER_OP
    :   '<'
    ;        

LARGE_AS_OP
    :   '>='
    ;

SMALL_AS_OP
    :   '<='
    ;        

ENTRY_OP
    :   '->'
    ;
    
COMPARE_OP
    :   '<=>'
    ;
    
IN_OP
    :   'in'
    ;

IS_OP
    :   'is'
    ;

POWER_OP
    :    '**'
    ;

APPLY_OP
    :   '.='
    ;

ADD_ASSIGN_OP
    :   '+='
    ;

SUBTRACT_ASSIGN_OP
    :   '-='
    ;

MULTIPLY_ASSIGN_OP
    :   '*='
    ;

DIVIDE_ASSIGN_OP
    :   '/='
    ;

INTERSECT_ASSIGN_OP
    :   '&='
    ;

UNION_ASSIGN_OP
    :   '|='
    ;

XOR_ASSIGN_OP
    :   '^='
    ;

COMPLEMENT_ASSIGN_OP
    :   '~='
    ;
    
REMAINDER_ASSIGN_OP
    :   '%='
    ;

DEFAULT_ASSIGN_OP
    :   '?='
    ;

AND_ASSIGN_OP
    :   '&&='
    ;

OR_ASSIGN_OP
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
