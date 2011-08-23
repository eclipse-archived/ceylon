grammar Ceylon;

options {
    memoize=false;
}

@parser::header { package com.redhat.ceylon.compiler.typechecker.parser; import static com.redhat.ceylon.compiler.typechecker.tree.Tree.*; }
@lexer::header { package com.redhat.ceylon.compiler.typechecker.parser; import static com.redhat.ceylon.compiler.typechecker.tree.Tree.*; }

@members {
    private java.util.List<ParseError> errors 
            = new java.util.ArrayList<ParseError>();
    @Override public void displayRecognitionError(String[] tn,
            RecognitionException re) {
        errors.add(new ParseError(this, re, tn));
    }
    public java.util.List<ParseError> getErrors() {
        return errors;
    }
    
    private CompilationUnit compilationUnit;
    
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
}

@lexer::members {
    private java.util.List<LexError> errors 
            = new java.util.ArrayList<LexError>();
    @Override public void displayRecognitionError(String[] tn,
            RecognitionException re) {
        errors.add(new LexError(this, re, tn));
    }
    public java.util.List<LexError> getErrors() {
        return errors;
    }
}

compilationUnit returns [CompilationUnit compilationUnit]
    : { $compilationUnit = new CompilationUnit(null); }
      importList { $compilationUnit.setImportList($importList.importList); }
      ( statement { $compilationUnit.addStatement($statement.statement); } )*
      EOF { this.compilationUnit = $compilationUnit; }
    ;

importList returns [ImportList importList]
    : { $importList = new ImportList(null); } 
      ( importDeclaration { $importList.addImport($importDeclaration.importDeclaration); } )*
    ;

importDeclaration returns [Import importDeclaration]
    : IMPORT { $importDeclaration = new Import($IMPORT); } 
      packagePath { $importDeclaration.setImportPath($packagePath.importPath); }
    LBRACE
    ( 
      ie1=importElement { $importDeclaration.addImportMemberOrType($ie1.importMemberOrType); } 
      ( COMMA ie2=importElement { $importDeclaration.addImportMemberOrType($ie2.importMemberOrType); } )* 
      ( COMMA iw=importWildcard { $importDeclaration.setImportWildcard($iw.importWildcard); } )?
    | iw=importWildcard { $importDeclaration.setImportWildcard($iw.importWildcard); }
    )
    RBRACE
    ;

importElement returns [ImportMemberOrType importMemberOrType]
    : memberAlias? memberName
      { $importMemberOrType = new ImportMember(null);
        $importMemberOrType.setAlias($memberAlias.alias);
        $importMemberOrType.setIdentifier($memberName.identifier); }
    | typeAlias? typeName
      { $importMemberOrType = new ImportType(null);
        $importMemberOrType.setAlias($typeAlias.alias);
        $importMemberOrType.setIdentifier($typeName.identifier); }
    ;

importWildcard returns [ImportWildcard importWildcard]
    : ELLIPSIS
      { $importWildcard = new ImportWildcard($ELLIPSIS); }
    ;

memberAlias returns [Alias alias]
    : memberName SPECIFY
      { $alias = new Alias($SPECIFY); 
        $alias.setIdentifier($memberName.identifier); }
    ;

typeAlias returns [Alias alias]
    : typeName SPECIFY
      { $alias = new Alias($SPECIFY); 
        $alias.setIdentifier($typeName.identifier); }
    ;

packagePath returns [ImportPath importPath]
    : { $importPath = new ImportPath(null); } 
      pn1=packageName { $importPath.addIdentifier($pn1.identifier); } 
      ( MEMBER_OP pn2=packageName { $importPath.addIdentifier($pn2.identifier); } )*
    ;

packageName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); }
    ;

typeName returns [Identifier identifier]
    : UIDENTIFIER
      { $identifier = new Identifier($UIDENTIFIER); }
    ;

annotationName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); }
    ;

memberName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); }
    ;
    
statement returns [Statement statement]
    : specificationStatement
      { $statement = $specificationStatement.specifierStatement; }
    | expressionStatement
      { $statement = $expressionStatement.expressionStatement; }
    | directiveStatement
      { $statement = $directiveStatement.directive; }
    //| controlStructure
    ;

specificationStatement returns [SpecifierStatement specifierStatement]
    : memberName 
      { $specifierStatement = new SpecifierStatement(null); 
        BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($memberName.identifier);
        $specifierStatement.setBaseMemberExpression(bme); }
      specifier
      { $specifierStatement.setSpecifierExpression($specifier.specifierExpression); }
      ';'
    ;

expressionStatement returns [ExpressionStatement expressionStatement]
    : expression 
      { $expressionStatement = new ExpressionStatement(null); 
        $expressionStatement.setExpression($expression.expression); } 
      ';'
    ;
directiveStatement returns [Directive directive]
    : d=directive 
      { $directive=$d.directive; } 
      SEMICOLON
    ;

directive returns [Directive directive]
    : returnDirective
      { $directive = $returnDirective.directive; }
    | throwDirective
      { $directive = $throwDirective.directive; }
    | breakDirective
      { $directive = $breakDirective.directive; }
    | continueDirective
      { $directive = $continueDirective.directive; }
    ;

returnDirective returns [Return directive]
    : RETURN 
      { $directive = new Return($RETURN); }
      (
        expression
        { $directive.setExpression($expression.expression); }
      )?
    ;

throwDirective returns [Throw directive]
    : THROW
      { $directive = new Throw($THROW); }
      ( 
        expression
        { $directive.setExpression($expression.expression); }
      )?
    ;

breakDirective returns [Break directive]
    : BREAK
      { $directive = new Break($BREAK); }
    ;

continueDirective returns [Continue directive]
    : CONTINUE
      { $directive = new Continue($CONTINUE); }
    ;

specifier returns [SpecifierExpression specifierExpression]
    : SPECIFY 
      { $specifierExpression = new SpecifierExpression($SPECIFY); }
      expression
      { $specifierExpression.setExpression($expression.expression); }
    ;

expression returns [Expression expression]
    : { $expression = new Expression(null); }
      assignmentExpression
      { $expression.setTerm($assignmentExpression.term); }
    ;

base returns [Primary primary]
    : nonstringLiteral
      { $primary=$nonstringLiteral.literal; }
    | stringExpression
      { $primary=$stringExpression.atom; }
    | enumeration
      { $primary=$enumeration.sequenceEnumeration; }
    | selfReference
      { $primary=$selfReference.atom; }
    | typeReference
      { BaseTypeExpression bte = new BaseTypeExpression(null);
        bte.setIdentifier($typeReference.identifier);
        bte.setTypeArguments($typeReference.typeArgumentList);
        $primary=bte; }
    | memberReference
      { BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($memberReference.identifier);
        bme.setTypeArguments($memberReference.typeArgumentList);
        $primary=bme; }
    | parExpression
      { $primary=$parExpression.expression; }
    ;

primary returns [Primary primary]
    : base
      { $primary=$base.primary; }
    (          
        qualifiedMemberReference
      { QualifiedMemberExpression bme = new QualifiedMemberExpression(null);
        bme.setPrimary($primary);
        bme.setIdentifier($qualifiedMemberReference.identifier);
        bme.setTypeArguments($qualifiedMemberReference.typeArgumentList);
        $primary=bme; }
      | qualifiedTypeReference
      { QualifiedTypeExpression bte = new QualifiedTypeExpression(null);
        bte.setPrimary($primary);
        bte.setIdentifier($qualifiedTypeReference.identifier);
        bte.setTypeArguments($qualifiedTypeReference.typeArgumentList);
        $primary=bte; }
      | indexExpression
        { IndexExpression xe = new IndexExpression(null);
          xe.setPrimary($primary);
          xe.setElementOrRange($indexExpression.elementOrRange);
          xe.setIndexOperator($indexExpression.operator); 
          $primary=xe; }
      | arguments
        { InvocationExpression ie = new InvocationExpression(null);
          ie.setPrimary($primary);
          if ($arguments.argumentList instanceof PositionalArgumentList) {
              ie.setPositionalArgumentList((PositionalArgumentList)$arguments.argumentList);
          }
          if ($arguments.argumentList instanceof NamedArgumentList) {
              ie.setNamedArgumentList((NamedArgumentList)$arguments.argumentList);
          }
          $primary=ie; }
    )*
    ;
   
qualifiedMemberReference returns [Identifier identifier, MemberOperator operator, TypeArgumentList typeArgumentList]
    : memberSelectionOperator 
      { $operator = $memberSelectionOperator.operator; }
      memberReference
      { $identifier = $memberReference.identifier;
        $typeArgumentList = $memberReference.typeArgumentList; }
    ;

qualifiedTypeReference returns [Identifier identifier, MemberOperator operator, TypeArgumentList typeArgumentList]
    : memberSelectionOperator 
      { $operator = $memberSelectionOperator.operator; }
      typeReference
      { $identifier = $typeReference.identifier;
        $typeArgumentList = $typeReference.typeArgumentList; }
    ;

indexExpression returns [IndexOperator operator, ElementOrRange elementOrRange]
    : elementSelectionOperator 
      { $operator=$elementSelectionOperator.operator; }
      indexOrIndexRange 
      { $elementOrRange=$indexOrIndexRange.elementOrRange; }
      RBRACKET
    ;

memberSelectionOperator returns [MemberOperator operator]
    : MEMBER_OP
      { $operator=new MemberOp($MEMBER_OP); }
    | SAFE_MEMBER_OP
      { $operator=new SafeMemberOp($SAFE_MEMBER_OP); }
    | SPREAD_OP
      { $operator=new SpreadOp($SPREAD_OP); }
    ;

elementSelectionOperator returns [IndexOperator operator]
    : SAFE_INDEX_OP
      { $operator=new SafeIndexOp($SAFE_INDEX_OP); }
    | INDEX_OP
      { $operator=new IndexOp($INDEX_OP); }
    ;

enumeration returns [SequenceEnumeration sequenceEnumeration]
    : LBRACE { $sequenceEnumeration = new SequenceEnumeration($LBRACE); } 
      (
        expressions
        { $sequenceEnumeration.setExpressionList($expressions.expressionList); }
      )?
      RBRACE
    ;

expressions returns [ExpressionList expressionList]
    : { $expressionList = new ExpressionList(null); }
      e1=expression 
      { $expressionList.addExpression($e1.expression); }
      ( COMMA 
        e2=expression 
        { $expressionList.addExpression($e2.expression); } 
      )*
    ;

memberReference returns [Identifier identifier, TypeArgumentList typeArgumentList]
    : memberName
      { $identifier = $memberName.identifier; }
      (
        (typeArgumentsStart)
        => typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;

typeReference returns [Identifier identifier, TypeArgumentList typeArgumentList]
    : typeName 
      { $identifier = $typeName.identifier; }
      (
        (typeArgumentsStart)
        => typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;

//special rule for syntactic predicate to 
//determine if we have a < operator, or a
//type argument list
typeArgumentsStart
    : SMALLER_OP
      UIDENTIFIER ('.' UIDENTIFIER)* /*| 'subtype')*/ (DEFAULT_OP|ARRAY)*
      ((INTERSECTION_OP|UNION_OP) UIDENTIFIER ('.' UIDENTIFIER)* (DEFAULT_OP|ARRAY)*)*
      (LARGER_OP|SMALLER_OP|COMMA|ELLIPSIS)
    ;

indexOrIndexRange returns [ElementOrRange elementOrRange]
    : l=index
    (
      { Element e = new Element(null);
        $elementOrRange = e;
        e.setExpression($l.expression); }
      | '...' 
      { ElementRange er1 = new ElementRange(null);
        $elementOrRange = er1;
        er1.setLowerBound($l.expression); }
      | '..' u=index 
      { ElementRange er2 = new ElementRange(null);
        $elementOrRange = er2;
        er2.setLowerBound($l.expression); 
        er2.setUpperBound($u.expression); }
    )
    ;

index returns [Expression expression]
    : additiveExpression 
      { $expression = new Expression(null);
        $expression.setTerm($additiveExpression.term); }
    ;

arguments returns [ArgumentList argumentList]
    : positionalArguments 
      { $argumentList = $positionalArguments.positionalArgumentList; }
    | namedArguments
      { $argumentList = $namedArguments.namedArgumentList; }
    ;

namedArguments returns [NamedArgumentList namedArgumentList]
    : LBRACE 
      { $namedArgumentList = new NamedArgumentList($LBRACE); }
      (
        (namedArgumentStart) 
        => namedArgument
        { $namedArgumentList.addNamedArgument($namedArgument.namedArgument); }
      )* 
      ( 
        expressions
        { SequencedArgument se = new SequencedArgument(null);
          se.setExpressionList($expressions.expressionList);
          $namedArgumentList.setSequencedArgument(se); }
      )?
      RBRACE
    ;

namedArgument returns [NamedArgument namedArgument]
    : namedSpecifiedArgument
      { $namedArgument = $namedSpecifiedArgument.specifiedArgument; }
    //| namedArgumentDeclaration
    ;

namedSpecifiedArgument returns [SpecifiedArgument specifiedArgument]
    : memberName 
      { $specifiedArgument = new SpecifiedArgument(null); 
        $specifiedArgument.setIdentifier($memberName.identifier); }
      specifier 
      { $specifiedArgument.setSpecifierExpression($specifier.specifierExpression); }
    SEMICOLON
    ;

//special rule for syntactic predicate
//to distinguish between a named argument
//and a sequenced argument
namedArgumentStart
    : specificationStart 
    //| declarationStart
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER '='
    ;

parExpression returns [Expression expression] 
    : LPAREN 
      e=expression
      { $expression = $e.expression; }
      RPAREN
    ;
    
positionalArguments returns [PositionalArgumentList positionalArgumentList]
    : LPAREN 
      { $positionalArgumentList = new PositionalArgumentList($LPAREN); }
      ( pa1=positionalArgument
        { $positionalArgumentList.addPositionalArgument($pa1.positionalArgument); }
        (
          COMMA 
          pa2=positionalArgument
          { $positionalArgumentList.addPositionalArgument($pa2.positionalArgument); }
        )* 
        (
          ELLIPSIS
          { $positionalArgumentList.setEllipsis( new Ellipsis($ELLIPSIS) ); }
        )?
      )? 
      RPAREN
    ;

positionalArgument returns [PositionalArgument positionalArgument]
    : //(declarationStart) => specialArgument | 
      expression
      { $positionalArgument = new PositionalArgument(null);
        $positionalArgument.setExpression($expression.expression); }
    ;
    
assignmentExpression returns [Term term]
    : ee1=disjunctionExpression
      { $term = $ee1.term; }
      (
        assignmentOperator 
        { $assignmentOperator.operator.setLeftTerm($term);
          $term = $assignmentOperator.operator; }
        ee2=assignmentExpression
        { $assignmentOperator.operator.setRightTerm($ee2.term); }
      )?
    ;

assignmentOperator returns [AssignmentOp operator]
    : ASSIGN_OP { $operator = new AssignOp($ASSIGN_OP); }
    //| '.=' 
    | ADD_ASSIGN_OP { $operator = new AddAssignOp($ADD_ASSIGN_OP); }
    | SUBTRACT_ASSIGN_OP { $operator = new SubtractAssignOp($SUBTRACT_ASSIGN_OP); }
    | MULTIPLY_ASSIGN_OP { $operator = new MultiplyAssignOp($MULTIPLY_ASSIGN_OP); }
    | DIVIDE_ASSIGN_OP { $operator = new DivideAssignOp($DIVIDE_ASSIGN_OP); }
    | REMAINDER_ASSIGN_OP { $operator = new RemainderAssignOp($REMAINDER_ASSIGN_OP); }
    | INTERSECT_ASSIGN_OP { $operator = new IntersectAssignOp($INTERSECT_ASSIGN_OP); }
    | UNION_ASSIGN_OP { $operator = new UnionAssignOp($UNION_ASSIGN_OP); }
    | XOR_ASSIGN_OP { $operator = new XorAssignOp($XOR_ASSIGN_OP); }
    | COMPLEMENT_ASSIGN_OP { $operator = new ComplementAssignOp($COMPLEMENT_ASSIGN_OP); }
    | AND_ASSIGN_OP { $operator = new AndAssignOp($AND_ASSIGN_OP); }
    | OR_ASSIGN_OP { $operator = new OrAssignOp($OR_ASSIGN_OP); }
    | DEFAULT_ASSIGN_OP { $operator = new DefaultAssignOp($DEFAULT_ASSIGN_OP); }
    ;

disjunctionExpression returns [Term term]
    : me1=conjunctionExpression
      { $term = $me1.term; }
      (
        disjunctionOperator 
        { $disjunctionOperator.operator.setLeftTerm($term);
          $term = $disjunctionOperator.operator; }
        me2=conjunctionExpression
        { $disjunctionOperator.operator.setRightTerm($me2.term); }
      )*
    ;

disjunctionOperator returns [OrOp operator]
    : OR_OP 
      { $operator = new OrOp($OR_OP); }
    ;

conjunctionExpression returns [Term term]
    : me1=logicalNegationExpression
      { $term = $me1.term; }
      (
        conjunctionOperator 
        { $conjunctionOperator.operator.setLeftTerm($term);
          $term = $conjunctionOperator.operator; }
        me2=logicalNegationExpression
        { $conjunctionOperator.operator.setRightTerm($me2.term); }
      )*
    ;

conjunctionOperator returns [AndOp operator]
    : AND_OP 
      { $operator = new AndOp($AND_OP); }
    ;

logicalNegationExpression returns [Term term]
    : notOperator 
      { $term = $notOperator.operator; }
      le=logicalNegationExpression
      { $notOperator.operator.setTerm($le.term); }
    | equalityExpression
      { $term = $equalityExpression.term; }
    ;

notOperator returns [NotOp operator]
    : NOT_OP 
      { $operator = new NotOp($NOT_OP); }
    ;

equalityExpression returns [Term term]
    : ee1=comparisonExpression
      { $term = $ee1.term; }
      (
        equalityOperator 
        { $equalityOperator.operator.setLeftTerm($term);
          $term = $equalityOperator.operator; }
        ee2=comparisonExpression
        { $equalityOperator.operator.setRightTerm($ee2.term); }
      )?
    ;

equalityOperator returns [BinaryOperatorExpression operator]
    : EQUAL_OP 
      { $operator = new EqualOp($EQUAL_OP); }
    | NOT_EQUAL_OP
      { $operator = new NotEqualOp($NOT_EQUAL_OP); }
    | IDENTICAL_OP
      { $operator = new IdenticalOp($IDENTICAL_OP); }
    ;

comparisonExpression returns [Term term]
    : ee1=existenceEmptinessExpression
      { $term = $ee1.term; }
      (
        comparisonOperator 
        { $comparisonOperator.operator.setLeftTerm($term);
          $term = $comparisonOperator.operator; }
        ee2=existenceEmptinessExpression
        { $comparisonOperator.operator.setRightTerm($ee2.term); }
      //| ('is'|'extends'|'satisfies') type
      )?
    ;

comparisonOperator returns [BinaryOperatorExpression operator]
    : COMPARE_OP 
      { $operator = new CompareOp($COMPARE_OP); }
    | SMALL_AS_OP
      { $operator = new SmallAsOp($SMALL_AS_OP); }
    | LARGE_AS_OP
      { $operator = new LargeAsOp($LARGE_AS_OP); }
    | LARGER_OP
      { $operator = new LargerOp($LARGER_OP); }
    | SMALLER_OP
      { $operator = new SmallerOp($SMALLER_OP); }
    | IN_OP
      { $operator = new InOp($IN_OP); }
    ;

existenceEmptinessExpression returns [Term term]
    : defaultExpression
      { $term = $defaultExpression.term; }
      (
        existsNonemptyOperator
        { $term = $existsNonemptyOperator.operator;
          $existsNonemptyOperator.operator.setTerm($defaultExpression.term); }
      )?
    ;

existsNonemptyOperator returns [UnaryOperatorExpression operator]
    : EXISTS 
      { $operator = new Exists($EXISTS); }
    | NONEMPTY
      { $operator = new Nonempty($NONEMPTY); }
    ;

defaultExpression returns [Term term]
    : rangeIntervalEntryExpression
      { $term = $rangeIntervalEntryExpression.term; }
      (
        defaultOperator 
        { $defaultOperator.operator.setLeftTerm($term);
          $term = $defaultOperator.operator; }
        de=defaultExpression
        { $defaultOperator.operator.setRightTerm($de.term); }
      )?
    ;

defaultOperator returns [DefaultOp operator]
    : DEFAULT_OP 
      { $operator = new DefaultOp($DEFAULT_OP); }
    ;

rangeIntervalEntryExpression returns [Term term]
    : ae1=additiveExpression
      { $term = $ae1.term; }
      (
        rangeIntervalEntryOperator 
        { $rangeIntervalEntryOperator.operator.setLeftTerm($term);
          $term = $rangeIntervalEntryOperator.operator; }
        ae2=additiveExpression
        { $rangeIntervalEntryOperator.operator.setRightTerm($ae2.term); }
      )?
    ;

rangeIntervalEntryOperator returns [BinaryOperatorExpression operator]
    : RANGE_OP 
      { $operator = new RangeOp($RANGE_OP); }
    | ENTRY_OP
      { $operator = new EntryOp($ENTRY_OP); }
    ;

additiveExpression returns [Term term]
    : me1=multiplicativeExpression
      { $term = $me1.term; }
      (
        additiveOperator 
        { $additiveOperator.operator.setLeftTerm($term);
          $term = $additiveOperator.operator; }
        me2=multiplicativeExpression
        { $additiveOperator.operator.setRightTerm($me2.term); }
      )*
    ;

additiveOperator returns [BinaryOperatorExpression operator]
    : SUM_OP 
      { $operator = new SumOp($SUM_OP); }
    | DIFFERENCE_OP
      { $operator = new DifferenceOp($DIFFERENCE_OP); }
    | UNION_OP
      { $operator = new UnionOp($UNION_OP); }
    | XOR_OP
      { $operator = new XorOp($XOR_OP); }
    | COMPLEMENT_OP
      { $operator = new ComplementOp($COMPLEMENT_OP); }
    ;

multiplicativeExpression returns [Term term]
    : ne1=negationComplementExpression
      { $term = $ne1.term; }
      (
        multiplicativeOperator 
        { $multiplicativeOperator.operator.setLeftTerm($term);
          $term = $multiplicativeOperator.operator; }
        ne2=negationComplementExpression
        { $multiplicativeOperator.operator.setRightTerm($ne2.term); }
      )*
    ;

multiplicativeOperator returns [BinaryOperatorExpression operator]
    : PRODUCT_OP 
      { $operator = new ProductOp($PRODUCT_OP); }
    | QUOTIENT_OP
      { $operator = new QuotientOp($QUOTIENT_OP); }
    | REMAINDER_OP
      { $operator = new RemainderOp($REMAINDER_OP); }
    | INTERSECTION_OP
      { $operator = new IntersectionOp($INTERSECTION_OP); }
    ;

negationComplementExpression returns [Term term]
    : unaryMinusOrComplementOperator 
      { $term = $unaryMinusOrComplementOperator.operator; }
      ne=negationComplementExpression
      { $unaryMinusOrComplementOperator.operator.setTerm($ne.term); }
    | exponentiationExpression
      { $term = $exponentiationExpression.term; }
    ;

unaryMinusOrComplementOperator returns [UnaryOperatorExpression operator]
    : DIFFERENCE_OP 
      { $operator = new NegativeOp($DIFFERENCE_OP); }
    | SUM_OP
      { $operator = new PositiveOp($SUM_OP); }
    | COMPLEMENT_OP
      { $operator = new FlipOp($COMPLEMENT_OP); }
    | FORMAT_OP
      { $operator = new FormatOp($FORMAT_OP); }
    ;

exponentiationExpression returns [Term term]
    : incrementDecrementExpression
      { $term = $incrementDecrementExpression.term; }
      (
        exponentiationOperator
        { $exponentiationOperator.operator.setLeftTerm($term);
          $term = $exponentiationOperator.operator; }
        ee=exponentiationExpression
        { $exponentiationOperator.operator.setRightTerm($ee.term); }
      )?
    ;

exponentiationOperator returns [PowerOp operator]
    : POWER_OP 
      { $operator = new PowerOp($POWER_OP); }
    ;

incrementDecrementExpression returns [Term term]
    : prefixOperator
      { $term = $prefixOperator.operator; }
      ie=incrementDecrementExpression
      { $prefixOperator.operator.setTerm($ie.term); }
    | postfixIncrementDecrementExpression
      { $term = $postfixIncrementDecrementExpression.term; }
    ;

prefixOperator returns [PrefixOperatorExpression operator]
    : DECREMENT_OP 
      { $operator = new DecrementOp($DECREMENT_OP); }
    | INCREMENT_OP 
      { $operator = new IncrementOp($INCREMENT_OP); }
    ;

postfixIncrementDecrementExpression returns [Term term]
    : primary 
      { $term = $primary.primary; } 
      (
        postfixOperator
        { $postfixOperator.operator.setTerm($term);
          $term = $postfixOperator.operator; }
      )*
    ;

postfixOperator returns [PostfixOperatorExpression operator]
    : DECREMENT_OP 
      { $operator = new PostfixDecrementOp($DECREMENT_OP); }
    | INCREMENT_OP 
      { $operator = new PostfixIncrementOp($INCREMENT_OP); }
    ;

selfReference returns [Atom atom]
    : THIS
      { $atom = new This($THIS); }
    | SUPER 
      { $atom = new Super($SUPER); }
    | OUTER
      { $atom = new Outer($OUTER); }
    ;
    
nonstringLiteral returns [Literal literal]
    : NATURAL_LITERAL 
      { $literal = new NaturalLiteral($NATURAL_LITERAL); }
    | FLOAT_LITERAL 
      { $literal = new FloatLiteral($FLOAT_LITERAL); }
    | QUOTED_LITERAL 
      { $literal = new QuotedLiteral($QUOTED_LITERAL); }
    | CHAR_LITERAL 
      { $literal = new CharLiteral($CHAR_LITERAL); }
    ;

stringExpression returns [Atom atom]
    : (STRING_LITERAL interpolatedExpressionStart) 
       => stringTemplate 
      { $atom = $stringTemplate.stringTemplate; }
    | stringLiteral 
      { $atom = $stringLiteral.stringLiteral; }
    ;

stringLiteral returns [StringLiteral stringLiteral]
    : STRING_LITERAL 
      { $stringLiteral = new StringLiteral($STRING_LITERAL); }
    ;

stringTemplate returns [StringTemplate stringTemplate]
    : sl1=stringLiteral 
      { $stringTemplate = new StringTemplate($sl1.stringLiteral.getToken()); 
        $stringTemplate.addStringLiteral($sl1.stringLiteral); }
      (
        (interpolatedExpressionStart) 
         => expression sl2=stringLiteral
        { $stringTemplate.addExpression($expression.expression);
          $stringTemplate.addStringLiteral($sl2.stringLiteral); }
      )+
    ;

typeArguments returns [TypeArgumentList typeArgumentList]
    : SMALLER_OP
      { $typeArgumentList = new TypeArgumentList($SMALLER_OP); }
      ta1=typeArgument 
      { $typeArgumentList.addType($ta1.type); }
      (
        COMMA 
        ta2=typeArgument
        { $typeArgumentList.addType($ta2.type); }
      )* 
      LARGER_OP
    ;

typeArgument returns [Type type]
    : unionType
      { $type = $unionType.type; }
      ( 
        ELLIPSIS
        { SequencedType st = new SequencedType($ELLIPSIS);
          st.setType($unionType.type); 
          $type = st; }
      )? 
    ;
    
unionType returns [StaticType type]
    @init { UnionType ut = new UnionType(null);
            ut.addStaticType($type); }
    : it1=intersectionType
      { $type = $it1.type; }
      ( 
        (
          UNION_OP
          it2=intersectionType
          { ut.addStaticType($it2.type); }
        )+
        { $type = ut; }
      )?
    ;

intersectionType returns [StaticType type]
    @init { IntersectionType it = new IntersectionType(null); 
            it.addStaticType($type); }
    : at1=abbreviatedType
      { $type = $at1.type; }
      ( 
        (
          INTERSECTION_OP
          at2=abbreviatedType
          { it.addStaticType($at2.type); }
        )+
        { $type = it; }
      )?
    ;

abbreviatedType returns [StaticType type]
    : t=type
      { $type=$t.type; }
      (
        DEFAULT_OP 
        { UnionType ot = new UnionType($DEFAULT_OP);
          ot.addStaticType($type);
          CommonToken tok = new CommonToken($DEFAULT_OP);
          tok.setText("Nothing");
          BaseType bt = new BaseType($DEFAULT_OP);
          bt.setIdentifier( new Identifier(tok) );
          ot.addStaticType(bt);
          $type=ot; }
      | ARRAY 
        { UnionType ot = new UnionType($ARRAY);
          CommonToken tok = new CommonToken($ARRAY);
          tok.setText("Empty");
          BaseType bt = new BaseType($ARRAY);
          bt.setIdentifier( new Identifier(tok) );
          ot.addStaticType(bt);
          tok = new CommonToken($ARRAY);
          tok.setText("Sequence");
          bt = new BaseType($ARRAY);
          bt.setIdentifier( new Identifier(tok) );
          TypeArgumentList tal = new TypeArgumentList(null);
          tal.addType($type);
          ot.addStaticType(bt);
          $type=ot; }
      )*
    ;

type returns [StaticType type]
    : ot=typeNameWithArguments
      { BaseType bt = new BaseType(null);
        bt.setIdentifier($ot.identifier);
        bt.setTypeArgumentList($ot.typeArgumentList);
        $type=bt; }
      (
        MEMBER_OP 
        it=typeNameWithArguments
        { QualifiedType qt = new QualifiedType($MEMBER_OP);
          qt.setIdentifier($it.identifier);
          qt.setTypeArgumentList($it.typeArgumentList);
          qt.setOuterType($type);
          $type=qt; }
      )*
    ;

typeNameWithArguments returns [Identifier identifier, TypeArgumentList typeArgumentList]
    : typeName
      { $identifier = $typeName.identifier; } 
      (
        typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;
    
/*annotations
    : annotation+
    -> ^(ANNOTATION_LIST ^(ANNOTATION annotation)+)
    ;

annotation
    : annotationName
    -> ^(BASE_MEMBER_EXPRESSION annotationName) ^(POSITIONAL_ARGUMENT_LIST)
    | annotationName annotationArguments
    -> ^(BASE_MEMBER_EXPRESSION annotationName) annotationArguments
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
    ;*/

//special rule for syntactic predicate
//to distinguish an interpolated expression
//in a string template
//this includes every token that could be 
//the beginning of an expression, except 
//for SIMPLESTRINGLITERAL and '['
interpolatedExpressionStart
    : LPAREN
    | LBRACE
    | LIDENTIFIER 
    | UIDENTIFIER 
    | selfReference
    | nonstringLiteral
    | prefixOperatorStart
    ;

prefixOperatorStart
    : FORMAT_OP 
    | DIFFERENCE_OP
    | INCREMENT_OP 
    | DECREMENT_OP 
    | COMPLEMENT_OP
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
    :   ('//'|'#!') ~('\n'|'\r')*  ('\r\n' | '\r' | '\n')?
        {
            $channel = HIDDEN;
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
            $channel = HIDDEN;
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

THROW
    :   'throw'
    ;

TRY_CLAUSE
    :   'try'
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
