grammar Ceylon;

options {
    memoize=false;
    output=AST;
}

tokens {
    ANNOTATION;
    ANNOTATION_LIST;
    ATTRIBUTE_ARGUMENT;
    ATTRIBUTE_DECLARATION;
    ATTRIBUTE_GETTER_DEFINITION;
    ATTRIBUTE_SETTER_DEFINITION;
    BLOCK;
    METHOD_DEFINITION;
    INTERFACE_DECLARATION;
    CLASS_DECLARATION;
    INVOCATION_EXPRESSION;
    CLASS_BODY;
    BOOLEAN_CONDITION;
    COMPILATION_UNIT;
    EXPRESSION;
    EXPRESSION_LIST;
    EXPRESSION_STATEMENT;
    FOR_ITERATOR;
    FOR_STATEMENT;
    VALUE_PARAMETER_DECLARATION;
    FUNCTIONAL_PARAMETER_DECLARATION;
    PARAMETER_LIST;
    IF_STATEMENT;
    IMPORT_LIST;
    IMPORT_WILDCARD;
    IMPORT_PATH;
    IMPORT_MEMBER;
    IMPORT_TYPE;
    ALIAS;
    INITIALIZER_EXPRESSION;
    INTERFACE_BODY;
    BROKEN_MEMBER_BODY;
    METHOD_ARGUMENT;
    METHOD_DECLARATION;
    METATYPES;
    NAMED_ARGUMENT_LIST;
    OBJECT_ARGUMENT;
    POSITIONAL_ARGUMENT;
    POSITIONAL_ARGUMENT_LIST;
    POSTFIX_OPERATOR_EXPRESSION;
    SATISFIES_EXPRESSION;
    SEQUENCED_ARGUMENT;
    SEQUENCED_TYPE;
    SEQUENCED_TYPE_PARAMETER;
    SPECIAL_ARGUMENT;
    TRY_CATCH_STATEMENT;
    SPECIFIED_VARIABLE_OR_EXPRESSION;
    TYPE_ARGUMENT_LIST;
    TYPE_DECLARATION;
    TYPE_PARAMETER_LIST;
    TYPE_SPECIFIER;
    WHILE_STATEMENT;
    DO_WHILE_STATEMENT;
    SWITCH_STATEMENT;
    SWITCH_CASE_LIST;
    TYPE_CONSTRAINT_LIST;
    TYPE_DECLARATION;
    INDEX_EXPRESSION;
    LOWER_BOUND;
    UPPER_BOUND;
    SELECTOR_LIST;
    SEQUENCE_ENUMERATION;
    SPECIFIED_ARGUMENT;
    SPECIFIER_EXPRESSION;
    SPECIFIER_STATEMENT;
    TYPE_VARIANCE;
    TYPE_PARAMETER_DECLARATION;
    STRING_TEMPLATE;
    VARIABLE;
    EXISTS_CONDITION;
    NONEMPTY_CONDITION;
    SATISFIES_CONDITION;
    IS_CONDITION;
    IS_CASE;
    SATISFIES_CASE;
    MATCH_CASE;
    POSTFIX_INCREMENT_OP;
    POSTFIX_DECREMENT_OP;
    NEGATIVE_OP;
    POSITIVE_OP;
    FLIP_OP;
    IDENTIFIER;
    VALUE_ITERATOR;
    KEY_VALUE_ITERATOR;
    SATISFIED_TYPES;
    EXTENDED_TYPE;
    ELEMENT;
    ELEMENT_RANGE;
    BASE_TYPE;
    QUALIFIED_TYPE;
    UNION_TYPE;
    BASE_TYPE_EXPRESSION;
    BASE_MEMBER_EXPRESSION;
    QUALIFIED_MEMBER_EXPRESSION;
    QUALIFIED_TYPE_EXPRESSION;
    SUBTYPE_EXPRESSION;
    SUPER_TYPE;
    LAMBDA;
    SYNTHETIC_VARIABLE;
}

@parser::header { package com.redhat.ceylon.compiler.typechecker.parser; }
@lexer::header { package com.redhat.ceylon.compiler.typechecker.parser; }

@members {
    private java.util.List<ParseError> errors 
            = new java.util.ArrayList<ParseError>();
    @Override public void displayRecognitionError(String[] tn,
            RecognitionException re) {
        errors.add(new ParseError(re, tn));
    }
    public java.util.List<ParseError> getErrors() {
        return errors;
    }
}

@lexer::members {
    private java.util.List<LexError> errors 
            = new java.util.ArrayList<LexError>();
    @Override public void displayRecognitionError(String[] tn,
            RecognitionException re) {
        errors.add(new LexError(re, tn));
    }
    public java.util.List<LexError> getErrors() {
        return errors;
    }
}

compilationUnit
    : importDeclaration*
      annotatedDeclaration2+
      EOF
    -> ^(COMPILATION_UNIT ^(IMPORT_LIST importDeclaration+)? annotatedDeclaration2+)
    ;

importDeclaration
    : 'import'^ packagePath '{'! importElements '}'!
    ;

importElement2
    : compilerAnnotation* importElement^
    ;

importElements
    : importElement2 (','! importElement2)* (','! importWildcard)?
    | importWildcard
    ;

importElement
    : memberAlias? memberName
    -> ^(IMPORT_MEMBER memberAlias? memberName)
    | typeAlias? typeName
    -> ^(IMPORT_TYPE typeAlias? typeName)
    ;

importWildcard
    : ELLIPSIS
    -> ^(IMPORT_WILDCARD[$ELLIPSIS])
    ;

memberAlias
    : memberName '='
    -> ^(ALIAS memberName)
    ;

typeAlias
    : typeName '='
    -> ^(ALIAS typeName)
    ;

packagePath
    : packageName ('.' packageName)*
    -> ^(IMPORT_PATH packageName*)
    ;

packageName
    : LIDENTIFIER
    -> ^(IDENTIFIER[$LIDENTIFIER])
    ;

block
    : LBRACE annotatedDeclarationOrStatement2* '}'
    -> ^(BLOCK[$LBRACE] annotatedDeclarationOrStatement2*)
    ;

//This rule accounts for the problem that we
//can't tell whether a member body is a block
//or a named argument list until after we
//finish parsing it
memberBody[Object mt] options { backtrack=true; memoize=true; }
    : namedArguments //first try to match with no directives or control structures
    -> ^(BLOCK ^(RETURN ^(EXPRESSION ^(INVOCATION_EXPRESSION ^(BASE_TYPE_EXPRESSION { ((CommonTree)$mt).getChild(0) } { ((CommonTree)$mt).getChild(1) } ) namedArguments))))
    | block //if there is a "return" directive or control structure, it must be a block
    //if it doesn't match as a block or as a named argument
    //list, then there must be an error somewhere, so parse
    //it again looking for the error
    | '{' brokenMemberBody? '}' 
    -> ^(BROKEN_MEMBER_BODY brokenMemberBody?)
    ;

brokenMemberBody
    : (annotatedDeclarationStart) => annotatedDeclaration brokenMemberBody?
    | ( 
        specificationStatement brokenMemberBody?
      | controlStructure brokenMemberBody?
      | directiveStatement brokenMemberBody?
      | expressionStatementOrList
      )
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

annotatedDeclarationOrStatement2
    : compilerAnnotation* annotatedDeclarationOrStatement^
    ;

annotatedDeclarationOrStatement options {memoize=true;}
    : (annotatedDeclarationStart) => annotatedDeclaration
    | statement
    ;

annotatedDeclaration2
    : compilerAnnotation* annotatedDeclaration^
    ;

annotatedDeclaration
    : annotations?
    ( 
      objectDeclaration^
    | setterDeclaration^
    | voidMethodDeclaration^
    | typedMethodOrAttributeDeclaration^
    | classDeclaration^
    | interfaceDeclaration^
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
    | unionType ('...' | LIDENTIFIER)
    ;

declarationKeyword
    : 'value'
    | 'function' 
    | 'assign'
    | 'void'
    | 'interface' 
    | 'class' 
    | 'object'
    ;

statement
    : specificationStatement
    | expressionStatement
    | controlStructure
    | directiveStatement
    ;

specificationStatement
    : memberName specifier ';'
    -> ^(SPECIFIER_STATEMENT ^(BASE_MEMBER_EXPRESSION memberName) specifier)
    ;

expressionStatement
    : expression ';'
    -> ^(EXPRESSION_STATEMENT expression)
    ;

directiveStatement
    : directive ';'!
    ;

/*semi
    : {input.LT(1).getType()==RBRACE}? | ';'!
    ;*/

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

objectDeclaration
    : OBJECT_DEFINITION memberName extendedType? satisfiedTypes? (classBody|';')
    -> ^(OBJECT_DEFINITION VALUE_MODIFIER memberName extendedType? satisfiedTypes? classBody?) 
    ;

voidMethodDeclaration
    : VOID_MODIFIER memberName methodParameters?
      ( 
        block 
      -> ^(METHOD_DEFINITION VOID_MODIFIER memberName methodParameters? block)   
      | specifier? ';'
      -> ^(METHOD_DECLARATION VOID_MODIFIER memberName methodParameters? specifier?)   
      )
    ;

setterDeclaration
    : ASSIGN memberName block
    -> ^(ATTRIBUTE_SETTER_DEFINITION[$ASSIGN] VOID_MODIFIER memberName block)
    ;

typedMethodOrAttributeDeclaration
    : inferableType memberName
    ( 
      methodParameters 
      (
        memberBody[$inferableType.tree] 
      -> ^(METHOD_DEFINITION inferableType memberName methodParameters memberBody)
      | specifier? ';'
      -> ^(METHOD_DECLARATION inferableType memberName methodParameters specifier?)
    )
    | (specifier | initializer)? ';'
    -> ^(ATTRIBUTE_DECLARATION inferableType memberName specifier? initializer?)
    | memberBody[$inferableType.tree]
    -> ^(ATTRIBUTE_GETTER_DEFINITION inferableType memberName memberBody)      
    )
    ;

inferableType
    : unionType 
    | 'value' 
    | 'function'
    ;

interfaceDeclaration
    : INTERFACE_DEFINITION
      typeName interfaceParameters
      (
        interfaceBody
      -> ^(INTERFACE_DEFINITION typeName interfaceParameters? interfaceBody)
      | typeSpecifier? ';'
      -> ^(INTERFACE_DECLARATION[$INTERFACE_DEFINITION] typeName interfaceParameters? typeSpecifier?)
      )
    ;

classDeclaration
    : CLASS_DEFINITION typeName classParameters?
      (
        classBody
      -> ^(CLASS_DEFINITION typeName classParameters? classBody)
      | typeSpecifier? ';'
      -> ^(CLASS_DECLARATION[$CLASS_DEFINITION] typeName classParameters? typeSpecifier?)
      )
    ;

methodParameters
    : typeParameters? parameters extraParameters? 
      metatypes? 
      typeConstraints?
    ;
    
interfaceParameters
    : typeParameters?
      caseTypes? metatypes? adaptedTypes? satisfiedTypes?
      typeConstraints?
    ;

classParameters
    : typeParameters? parameters extraParameters?
      caseTypes? metatypes? extendedType? satisfiedTypes?
      typeConstraints?
    ;

//Note: interface bodies can't really contain 
//      statements, but error recovery works
//      much better if we validate that later
//      on, instead of doing it in the parser.
interfaceBody
    : LBRACE annotatedDeclarationOrStatement2* '}'
    -> ^(INTERFACE_BODY[$LBRACE] annotatedDeclarationOrStatement2*)
    ;

classBody
    : LBRACE annotatedDeclarationOrStatement2* '}'
    -> ^(CLASS_BODY[$LBRACE] annotatedDeclarationOrStatement2*)
    ;

extendedType
    : EXTENDS 
    (
      bt=typeReference positionalArguments
      -> ^(EXTENDED_TYPE[$EXTENDS] ^(BASE_TYPE $bt) ^(INVOCATION_EXPRESSION ^(BASE_TYPE_EXPRESSION $bt) positionalArguments))
    | SUPER MEMBER_OP qt=typeReference positionalArguments
      -> ^(EXTENDED_TYPE[$EXTENDS] ^(QUALIFIED_TYPE SUPER_TYPE[$SUPER] $qt) ^(INVOCATION_EXPRESSION ^(QUALIFIED_TYPE_EXPRESSION SUPER MEMBER_OP $qt) positionalArguments))
    )
    ;

satisfiedTypes
    : SATISFIES type ('&' type)*
    -> ^(SATISFIED_TYPES[$SATISFIES] type+)
    ;

abstractedType
    : 'abstracts'^ type
    ;

adaptedTypes
    : 'adapts'^ type ('&' type)*
    ;

caseTypes
    : 'of'^ caseType ('|'! caseType)*
    ;

caseType 
    : type 
    | memberName -> ^(BASE_MEMBER_EXPRESSION memberName)
    //| (annotations? 'case' memberName) => annotations? 'case' memberName 
    ;

//Support for metatypes
//Note that we don't need this for now
metatypes
    : IS_OP type ('&' type)* 
    -> ^(METATYPES[$IS_OP] type*)
    ;

/*selfType
    : 'this' 'is'
    -> ^(SELF_TYPE)
    ;*/

typeConstraint
    : 'given'^
      //selfType?
      typeName 
      typeParameters? 
      parameters? 
      caseTypes? 
      metatypes? 
      satisfiedTypes? 
      abstractedType?
    ;

typeConstraint2
    : compilerAnnotation* typeConstraint^
    ;

typeConstraints
    : typeConstraint2+
    -> ^(TYPE_CONSTRAINT_LIST typeConstraint2+)
    ;

unionType
    : (abbreviatedType -> abbreviatedType)
    ( ('|' abbreviatedType)+
      -> ^(UNION_TYPE $unionType abbreviatedType+)
    )?
    ;

abbreviatedType
    : (type -> type)
      (
        DEFAULT_OP 
      -> ^(UNION_TYPE ^(BASE_TYPE ^(IDENTIFIER[$DEFAULT_OP,"Nothing"])) $abbreviatedType)
      | ARRAY 
      -> ^(UNION_TYPE ^(BASE_TYPE ^(IDENTIFIER[$ARRAY,"Empty"])) ^(BASE_TYPE ^(IDENTIFIER[$ARRAY,"Sequence"]) ^(TYPE_ARGUMENT_LIST $abbreviatedType)))
      )*
    ;

type
    : (ot=typeNameWithArguments -> ^(BASE_TYPE $ot))
      (MEMBER_OP it=typeNameWithArguments -> ^(QUALIFIED_TYPE[$MEMBER_OP] $type $it))*
    | SUBTYPE
    /*| parameterName '.' 'subtype' abbreviation*
    -> ^(TYPE parameterName 'subtype' abbreviation*)*/
    ;

typeNameWithArguments
    : typeName typeArguments?
    ;
    
annotations
    : annotation+
    -> ^(ANNOTATION_LIST ^(ANNOTATION annotation)+)
    ;

//TODO: we could minimize backtracking by limiting the 
//kind of expressions that can appear as arguments to
//the annotation
annotation
    : annotationName
    -> ^(BASE_MEMBER_EXPRESSION annotationName) ^(POSITIONAL_ARGUMENT_LIST)
    | annotationName annotationArguments
    -> ^(BASE_MEMBER_EXPRESSION annotationName) annotationArguments
    ;

compilerAnnotation
    : '@'^ annotationName ( '['! STRING_LITERAL ']'! )?
    ;

annotationArguments
    : arguments | literalArguments
    ;

literalArguments
    : literalArgument+
    -> ^(POSITIONAL_ARGUMENT_LIST ^(POSITIONAL_ARGUMENT ^(EXPRESSION literalArgument))+)
    ;
    
literalArgument
    : nonstringLiteral | stringLiteral
    ;

typeName
    : UIDENTIFIER
    -> ^(IDENTIFIER[$UIDENTIFIER])
    ;

annotationName
    : LIDENTIFIER
    -> ^(IDENTIFIER[$LIDENTIFIER])
    ;

memberName 
    : LIDENTIFIER
    -> ^(IDENTIFIER[$LIDENTIFIER])
    ;

typeArguments
    : SMALLER_OP typeArgument (',' typeArgument)* '>'
    -> ^(TYPE_ARGUMENT_LIST[$SMALLER_OP] typeArgument+)
    ;

typeArgument
    : unionType ( '...' -> ^(SEQUENCED_TYPE unionType) | -> unionType ) 
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
    : SMALLER_OP typeParameter (',' typeParameter)* '>'
    -> ^(TYPE_PARAMETER_LIST[$SMALLER_OP] typeParameter+)
    ;

typeParameter
    : variance? typeName
    -> ^(TYPE_PARAMETER_DECLARATION variance? typeName)
    | typeName '...'
    -> ^(SEQUENCED_TYPE_PARAMETER typeName)
    //| '#'! dimensionalTypeParameter
    ;

variance
    : IN_OP -> ^(TYPE_VARIANCE[$IN_OP])
    | OUT -> ^(TYPE_VARIANCE[$OUT])
    ;
    
dimensionalTypeParameter
    : memberName
    -> ^(TYPE_PARAMETER_DECLARATION memberName)
    ;
    
initializer
    : ASSIGN_OP expression
    -> ^(INITIALIZER_EXPRESSION[$ASSIGN_OP] expression)
    ;

specifier
    : SPECIFY expression
    -> ^(SPECIFIER_EXPRESSION[$SPECIFY] expression)
    ;

typeSpecifier
    : SPECIFY type
    -> ^(TYPE_SPECIFIER[$SPECIFY] type)
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
    | 'subtype'
    | nonstringLiteral
    | prefixOperator
    ;

prefixOperator
    : '$' | '-' /*| '+'*/ |'++' | '--' | '~'
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
    : unaryMinusOrComplementOperator^ negationComplementExpression
    | exponentiationExpression
    ;

unaryMinusOrComplementOperator
    : DIFFERENCE_OP -> NEGATIVE_OP[$DIFFERENCE_OP]
    | SUM_OP -> POSITIVE_OP[$SUM_OP]
    | COMPLEMENT_OP -> FLIP_OP[$COMPLEMENT_OP]
    | '$'
    ;

exponentiationExpression
    : incrementDecrementExpression 
      ('**'^ exponentiationExpression)?
    ;

incrementDecrementExpression
    : ('++'^ | '--'^) incrementDecrementExpression
    | primary
    ;

selfReference
    : 'this' | 'super' | 'outer'
    ;

enumeration
    : LBRACE expressions? '}'
    -> ^(SEQUENCE_ENUMERATION[$LBRACE] expressions?)
    ;

primary
    : (base -> base)
    ( 
        memberSelectionOperator 
        (
          m=memberReference 
      -> ^(QUALIFIED_MEMBER_EXPRESSION $primary memberSelectionOperator $m)
        | t=typeReference
      -> ^(QUALIFIED_TYPE_EXPRESSION $primary memberSelectionOperator $t)
        )
      | elementSelectionOperator elementsSpec ']'
      -> ^(elementSelectionOperator $primary elementsSpec)
      | postfixOperator 
      -> ^(postfixOperator $primary)
      | argumentsWithFunctionalArguments
      -> ^(INVOCATION_EXPRESSION $primary argumentsWithFunctionalArguments)
    )*
   ;

base 
    : nonstringLiteral
    | stringExpression
    | enumeration
    | selfReference
    | typeReference
    -> ^(BASE_TYPE_EXPRESSION typeReference)
    | memberReference
    -> ^(BASE_MEMBER_EXPRESSION memberReference)
    | 'subtype' 
    -> ^(SUBTYPE_EXPRESSION)
    | (parametersStart) => lambda
    | parExpression
    ;

lambda
    : /*'function'*/ parameters functionalArgumentBody
    -> ^(LAMBDA parameters functionalArgumentBody)
    ;

postfixOperator
    : DECREMENT_OP -> ^(POSTFIX_DECREMENT_OP[$DECREMENT_OP])
    | INCREMENT_OP -> ^(POSTFIX_INCREMENT_OP[$INCREMENT_OP])
    ;

memberSelectionOperator
    : '.' | '?.' | '[].' 
    ;

memberReference
    : memberName ((typeArgumentsStart) => typeArguments)? 
    ;

typeReference
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
      (DEFAULT_OP|ARRAY)*
      ('>'|'<'|','|'...')
    ;

elementSelectionOperator
    : '?[' | '['
    ;

elementsSpec
    : l=index
    (
      -> ^(ELEMENT $l)
      | '...' 
      -> ^(ELEMENT_RANGE $l)
      | '..' u=index 
      -> ^(ELEMENT_RANGE $l $u)
    )
    ;

index
    : additiveExpression 
    -> ^(EXPRESSION additiveExpression)
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

namedArgument2
    : compilerAnnotation* namedArgument^
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
    : OBJECT_DEFINITION memberName extendedType? satisfiedTypes? (classBody|';')
    -> ^(OBJECT_ARGUMENT[$OBJECT_DEFINITION] VALUE_MODIFIER memberName extendedType? satisfiedTypes? classBody?)
    ;

voidMethodArgument
    : VOID_MODIFIER memberName parameters* block
    -> ^(METHOD_ARGUMENT VOID_MODIFIER memberName parameters* block)
    ;

typedMethodOrGetterArgument
    : inferableType memberName
    ( 
      (parameters+ memberBody[$inferableType.tree])
    -> ^(METHOD_ARGUMENT inferableType memberName parameters+ memberBody)
    | memberBody[$inferableType.tree]
    -> ^(ATTRIBUTE_ARGUMENT inferableType memberName memberBody)      
    )
    ;

namedSpecifiedArgument
    : memberName specifier ';'
    -> ^(SPECIFIED_ARGUMENT memberName specifier)
    ;

//special rule for syntactic predicate
//to distinguish between a named argument
//and a sequenced argument
namedArgumentStart
    : compilerAnnotation* (specificationStart | declarationStart)
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER '='
    ;

namedArguments
    : LBRACE ((namedArgumentStart) => namedArgument2)* expressions2? '}'
    -> ^(NAMED_ARGUMENT_LIST[$LBRACE] namedArgument2* ^(SEQUENCED_ARGUMENT expressions2)?)
    ;

parExpression 
    : '('! expression ')'!
    ;
    
positionalArguments
    : LPAREN ( positionalArgument (',' positionalArgument)* )? ')'
    -> ^(POSITIONAL_ARGUMENT_LIST[$LPAREN] ^(POSITIONAL_ARGUMENT positionalArgument)*)
    ;

positionalArgument
    : (declarationStart) => specialArgument
    | expression
    ;

//a smalltalk-style parameter to a positional parameter
//invocation
functionalArgument
    : memberName functionalArgumentDefinition
    -> ^(METHOD_ARGUMENT FUNCTION_MODIFIER memberName functionalArgumentDefinition)
    ;

functionalArgumentDefinition
    : functionalArgumentParameters functionalArgumentBody
    ;

functionalArgumentParameters
    : (parametersStart) => parameters
    | -> ^(PARAMETER_LIST)
    ;

functionalArgumentBody
    : block
    | parExpression -> ^(BLOCK /*^(DIRECTIVE*/ ^(RETURN parExpression))
    ;

//Support "T x in arg" in positional argument lists
//Note that we don't need to support this yet
specialArgument
    : inferableType memberName (containment | specifier)
    -> ^(SPECIAL_ARGUMENT inferableType memberName containment? specifier?)
    //| isCondition
    //| existsCondition
    ;

parameters
    : LPAREN (annotatedParameter2 (',' annotatedParameter2)*)? ')'
    -> ^(PARAMETER_LIST[$LPAREN] annotatedParameter2*)
    ;

//Support for declaring functional formal parameters outside
//of the parenthesized list
//Note that this is just a TODO in the spec
extraParameters
    : extraParameter+
    -> ^(PARAMETER_LIST ^(FUNCTIONAL_PARAMETER_DECLARATION extraParameter)+)
    ;

//special rule for syntactic predicate
//to distinguish between a formal 
//parameter list and a parenthesized body 
//of an inline callable argument
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
parametersStart
    : '(' ( annotatedDeclarationStart | ')' )
    ;
    
// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
parameter
    : parameterType memberName
      (
          valueParameter? specifier?
        -> ^(VALUE_PARAMETER_DECLARATION parameterType memberName specifier?)
        |  parameters+ specifier? //for callable parameters
        -> ^(FUNCTIONAL_PARAMETER_DECLARATION parameterType memberName parameters+ specifier?)
      /*| iteratedParameter 
      | (specifiedParameterStart) => specifiedParameter*/
      )
    ;

annotatedParameter
    : annotations? parameter^
    ;

annotatedParameter2
    : compilerAnnotation* annotatedParameter^
    ;

valueParameter
    : '->' unionType memberName
    ;

/*
//Support for "X x in Iterable<X> param" in formal parameter lists
//Note that this is just a TODO in the spec
iteratedParameter
    : 'in' type memberName
    ;

//Support for "X x = X? param" in formal parameter lists
//Note that this is just a TODO in the spec
specifiedParameter
    : '=' type memberName
    ;

//special rule for syntactic predicate
specifiedParameterStart
    : '=' declarationStart
    ;
*/

extraParameter
    : parameterType memberName parameters*
    ;

parameterType
    : unionType ( '...' -> ^(SEQUENCED_TYPE unionType) | -> unionType )
    | VOID_MODIFIER -> VOID_MODIFIER
    ;

// Control structures.

controlCondition
    : condition
    ;

condition
    : booleanCondition 
    | existsCondition
    | nonemptyCondition
    | isCondition 
    | satisfiesCondition
    ;
    
booleanCondition
    : '(' expression ')'
    -> ^(BOOLEAN_CONDITION expression)
    ;
        
existsCondition
    : ('(' EXISTS LIDENTIFIER ')') => '(' EXISTS impliedVariable ')'
    -> ^(EXISTS_CONDITION[$EXISTS] impliedVariable)
    | '(' EXISTS specifiedVariable2 ')'
    -> ^(EXISTS_CONDITION[$EXISTS] specifiedVariable2)
    ;
    
nonemptyCondition
    : ('(' NONEMPTY LIDENTIFIER ')') => '(' NONEMPTY impliedVariable ')'
    -> ^(NONEMPTY_CONDITION[$NONEMPTY] impliedVariable)
    | '(' NONEMPTY specifiedVariable2 ')' 
    -> ^(NONEMPTY_CONDITION[$NONEMPTY] specifiedVariable2)
    ;

isCondition
    : ('(' IS_OP unionType LIDENTIFIER ')') => '(' IS_OP unionType memberName ')'
    -> ^(IS_CONDITION[$IS_OP] unionType ^(VARIABLE SYNTHETIC_VARIABLE memberName ^(SPECIFIER_EXPRESSION ^(EXPRESSION ^(BASE_MEMBER_EXPRESSION memberName)))))
    | '(' IS_OP unionType (memberName specifier) ')'
    -> ^(IS_CONDITION[$IS_OP] unionType ^(VARIABLE unionType memberName specifier))
    ;

satisfiesCondition
    : '(' SATISFIES type type ')'
    -> ^(SATISFIES_CONDITION[$SATISFIES] type+)
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
    : 'if'^ controlCondition block
    ;

elseBlock
    : 'else'^ (elseIf | block)
    ;

elseIf
    : ifElse 
    -> ^(BLOCK ifElse)
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
    -> ^(MATCH_CASE expressions)
    | isCaseCondition 
    | satisfiesCaseCondition
    ;

expressions2
    : compilerAnnotation* expressions
    ;

expressions
    : expression (',' expression)*
    -> ^(EXPRESSION_LIST expression+)
    ;

isCaseCondition
    : IS_OP unionType
    -> ^(IS_CASE[$IS_OP] unionType)
    ;

satisfiesCaseCondition
    : SATISFIES type
    -> ^(SATISFIES_CASE[$SATISFIES] type)
    ;

forFail
    : forBlock failBlock?
    -> ^(FOR_STATEMENT forBlock failBlock?)
    ;

forBlock
    : 'for'^ '('! forIterator2 ')'! block
    ;

failBlock
    : 'fail'^ block
    ;

forIterator2
    : compilerAnnotation* forIterator^
    ;

forIterator
    : v=variable
    (
      containment
      -> ^(VALUE_ITERATOR $v containment)
      | '->' vv=variable containment
      -> ^(KEY_VALUE_ITERATOR $v $vv containment)
    )
    ;
    
containment
    : 'in' expression
    -> ^(SPECIFIER_EXPRESSION expression)
    ;
    
doWhile
    : doBlock ';'
    -> ^(DO_WHILE_STATEMENT doBlock)
    ;

simpleWhile
    : whileBlock
    -> ^(WHILE_STATEMENT whileBlock)
    ;

whileBlock
    : 'while'^ controlCondition block
    ;

doBlock
    : 'do'^ block 'while'! controlCondition
    ;

tryCatchFinally
    : tryBlock catchBlock* finallyBlock?
    -> ^(TRY_CATCH_STATEMENT tryBlock catchBlock* finallyBlock?)
    ;

tryBlock
    : 'try'^ ('('! specifiedVariableOrExpression ')'!)? block
    ;

catchBlock
    : 'catch'^ '('! variable2 ')'! block
    ;

finallyBlock
    : 'finally'^ block
    ;

specifiedVariableOrExpression
    : (declarationStart|specificationStart) => specifiedVariable 
    -> ^(SPECIFIED_VARIABLE_OR_EXPRESSION specifiedVariable)
    | expression
    -> ^(SPECIFIED_VARIABLE_OR_EXPRESSION expression)
    ;

specifiedVariable2
    : compilerAnnotation* specifiedVariable^
    ;

specifiedVariable
    : variable^ specifier
    ;

variable2
    : compilerAnnotation* variable^
    ;

variable
    : unionType memberName parameters*
    -> ^(VARIABLE unionType memberName parameters*)
    | memberName
    -> ^(VARIABLE VALUE_MODIFIER memberName)
    | memberName parameters+
    -> ^(VARIABLE FUNCTION_MODIFIER memberName parameters+)
    ;

impliedVariable
    : memberName 
    -> ^(VARIABLE SYNTHETIC_VARIABLE memberName ^(SPECIFIER_EXPRESSION ^(EXPRESSION ^(BASE_MEMBER_EXPRESSION memberName))))
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
fragment INDEX_OP: '[';
//distinguish the spread operator "x[]."
//from a sequenced type "T[]..."
LBRACKETS
    : '['
    (
      (']' '.' ~'.') => '].' { $type = SPREAD_OP; }
    | (']') => ']' { $type = ARRAY; }
    | { $type = INDEX_OP; }
    )
    ;

fragment SAFE_MEMBER_OP: '?.';
fragment SAFE_INDEX_OP: '?[';
fragment DEFAULT_OP: '?';
//distinguish the safe index operator "x?[i]"
//from an abbreviated type "T?[]"
//and the safe member operator "x?.y" from 
//the sequenced type "T?..."
QMARKS
    : '?'
    (
      ('[' ~']') => '[' { $type = SAFE_INDEX_OP; }
    | ('.' ~'.') => '.' { $type = SAFE_MEMBER_OP; }
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

ADAPTED_TYPES
    :   'adapts'
    ;

ASSIGN
    :   'assign'
    ;
    
BREAK
    :   'break'
    ;

CASE_CLAUSE
    :   'case'
    ;

CATCH_CLAUSE
    :   'catch'
    ;

CLASS_DEFINITION
    :   'class'
    ;

CONTINUE
    :   'continue'
    ;

DO_CLAUSE
    :   'do'
    ;
    
ELSE_CLAUSE
    :   'else'
    ;            

EXISTS
    :   'exists'
    ;

EXTENDS
    :   'extends'
    ;

FINALLY_CLAUSE
    :   'finally'
    ;

FOR_CLAUSE
    :   'for'
    ;

FAIL_CLAUSE
    :   'fail'
    ;

TYPE_CONSTRAINT
    :   'given'
    ;

IF_CLAUSE
    :   'if'
    ;

SATISFIES
    :   'satisfies'
    ;

IMPORT
    :   'import'
    ;

INTERFACE_DEFINITION
    :   'interface'
    ;

VALUE_MODIFIER
    :   'value'
    ;

FUNCTION_MODIFIER
    :   'function'
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

SWITCH_CLAUSE
    :   'switch'
    ;

THIS
    :   'this'
    ;

OUTER
    :   'outer'
    ;

OBJECT_DEFINITION
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

TRY_CLAUSE
    :   'try'
    ;

RETRY
    :   'retry'
    ;

VOID_MODIFIER
    :   'void'
    ;

WHILE_CLAUSE
    :   'while'
    ;

ELLIPSIS
    :   '...';

RANGE_OP
    :   '..';

MEMBER_OP
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

COMPILER_ANNOTATION
    :   '@'
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
