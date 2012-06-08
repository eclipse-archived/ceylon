grammar Ceylon;

options {
    memoize=false;
}

@parser::header { package com.redhat.ceylon.compiler.typechecker.parser;
                  import com.redhat.ceylon.compiler.typechecker.tree.Node;
                  import static com.redhat.ceylon.compiler.typechecker.tree.CustomTree.*; }
@lexer::header { package com.redhat.ceylon.compiler.typechecker.parser; }

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
      ( 
        ca1=compilerAnnotations
        SEMICOLON
        { $compilationUnit.getCompilerAnnotations().addAll($ca1.annotations); }
      )?
      importList 
      { $compilationUnit.setImportList($importList.importList); }
      ( 
        ca2=compilerAnnotations declaration
        { if ($declaration.declaration!=null)
              $compilationUnit.addDeclaration($declaration.declaration); 
          if ($declaration.declaration!=null)
              $declaration.declaration.getCompilerAnnotations().addAll($ca2.annotations); } 
      )*
      EOF
    ;

importList returns [ImportList importList]
    : { $importList = new ImportList(null); } 
      ( importDeclaration { $importList.addImport($importDeclaration.importDeclaration); } )*
    ;

importDeclaration returns [Import importDeclaration]
    @init { ImportPath importPath=null; }
    : IMPORT 
      { $importDeclaration = new Import($IMPORT); } 
      ( 
        pn1=packageName 
        { importPath = new ImportPath(null);
          if ($pn1.identifier!=null) 
              importPath.addIdentifier($pn1.identifier); 
          $importDeclaration.setImportPath(importPath); } 
        ( 
          m=MEMBER_OP 
          { importPath.setEndToken($m); }
          (
            pn2=packageName 
            { importPath.addIdentifier($pn2.identifier); 
              importPath.setEndToken(null); }
          | { displayRecognitionError(getTokenNames(), 
                  new MismatchedTokenException(LIDENTIFIER, input)); }
          )
        )*
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); }
      )
      importElementList
      { $importDeclaration.setImportMemberOrTypeList($importElementList.importMemberOrTypeList); }
      ;

importElementList returns [ImportMemberOrTypeList importMemberOrTypeList]
    @init { ImportMemberOrTypeList il=null; 
            boolean wildcarded = false; }
    :
    LBRACE
    { il = new ImportMemberOrTypeList($LBRACE);
      $importMemberOrTypeList = il; }
    ( 
      ie1=importElement 
      { if ($ie1.importMemberOrType!=null)
            il.addImportMemberOrType($ie1.importMemberOrType); } 
      ( 
        c1=COMMA 
        { il.setEndToken($c1); 
          if (wildcarded) 
              displayRecognitionError(getTokenNames(), 
                  new MismatchedTokenException(RBRACE, input)); }
        (
          ie2=importElement 
          { if ($ie2.importMemberOrType!=null)
                il.addImportMemberOrType($ie2.importMemberOrType);
            if ($ie2.importMemberOrType!=null)
                il.setEndToken(null); } 
        | iw=importWildcard
          { wildcarded = true;
            if ($iw.importWildcard!=null) 
                il.setImportWildcard($iw.importWildcard); 
            if ($iw.importWildcard!=null) 
                il.setEndToken(null); } 
        | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(ELLIPSIS, input)); }
        )
      )*
    | iw=importWildcard { il.setImportWildcard($iw.importWildcard); }
    )?
    RBRACE
    { il.setEndToken($RBRACE); }
    ;

importElement returns [ImportMemberOrType importMemberOrType]
    @init { Alias alias = null; }
    : compilerAnnotations
    ( in1=importName 
      { $importMemberOrType = new ImportMember(null);
        $importMemberOrType.setIdentifier($in1.identifier); }
      ( SPECIFY
        { alias = new Alias($SPECIFY);
          alias.setIdentifier($in1.identifier);
          $importMemberOrType.setAlias(alias); 
          $importMemberOrType.setIdentifier(null); }
        (
          in2=importName 
          { $importMemberOrType.setIdentifier($in2.identifier); }
        | { displayRecognitionError(getTokenNames(), 
                  new MismatchedTokenException($in1.identifier.getToken().getType(), input)); }
        )
      )?
      (
        iel2=importElementList
        { $importMemberOrType.setImportMemberOrTypeList($iel2.importMemberOrTypeList); }
      )?
    )
    { if ($importMemberOrType!=null)
        $importMemberOrType.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

importWildcard returns [ImportWildcard importWildcard]
    : ELLIPSIS
      { $importWildcard = new ImportWildcard($ELLIPSIS); }
    ;

importName returns [Identifier identifier]
    : memberName { $identifier=$memberName.identifier; }
    | typeName { $identifier=$typeName.identifier; }
    ;

packageName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); 
        $LIDENTIFIER.setType(PIDENTIFIER);}
    ;

typeName returns [Identifier identifier]
    : UIDENTIFIER
      { $identifier = new Identifier($UIDENTIFIER); }
    ;

annotationName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); 
        $LIDENTIFIER.setType(AIDENTIFIER); }
    ;

memberName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); }
    ;
    
memberNameDeclaration returns [Identifier identifier]
    : memberName { $identifier = $memberName.identifier; }
    | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); }
      typeName { $identifier = $typeName.identifier; }
      
    ;

typeNameDeclaration returns [Identifier identifier]
    : typeName { $identifier = $typeName.identifier; }
    | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(UIDENTIFIER, input)); }
      memberName { $identifier = $memberName.identifier; }
      
    ;

objectDeclaration returns [ObjectDefinition declaration]
    : OBJECT_DEFINITION
      { $declaration = new ObjectDefinition($OBJECT_DEFINITION); 
        $declaration.setType(new ValueModifier(null)); }
      memberNameDeclaration
      { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        extendedType
        { $declaration.setExtendedType($extendedType.extendedType); } 
      )?
      ( 
        satisfiedTypes
        { $declaration.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); } 
      )?
      (
        classBody
        { $declaration.setClassBody($classBody.classBody); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      )
    //-> ^(OBJECT_DEFINITION VALUE_MODIFIER memberName extendedType? satisfiedTypes? classBody?) 
    ;

voidOrInferredMethodDeclaration returns [AnyMethod declaration]
    @init { MethodDefinition def=null;
            MethodDeclaration dec=null; }
    : (
        VOID_MODIFIER
        { VoidModifier vm = new VoidModifier($VOID_MODIFIER);
          def = new MethodDefinition($VOID_MODIFIER);
          dec = new MethodDeclaration($VOID_MODIFIER);
          def.setType(vm);
          dec.setType(vm);
          $declaration = dec; }
      | FUNCTION_MODIFIER
        { FunctionModifier fm = new FunctionModifier($FUNCTION_MODIFIER);
          def = new MethodDefinition($FUNCTION_MODIFIER);
          dec = new MethodDeclaration($FUNCTION_MODIFIER);
          def.setType(fm);
          dec.setType(fm);
          $declaration = dec; }
      )
      memberNameDeclaration
      { dec.setIdentifier($memberNameDeclaration.identifier); 
        def.setIdentifier($memberNameDeclaration.identifier); }
      (
        typeParameters
        { def.setTypeParameterList($typeParameters.typeParameterList); 
          dec.setTypeParameterList($typeParameters.typeParameterList); }        
      )?
      (
        parameters
        { def.addParameterList($parameters.parameterList); 
          dec.addParameterList($parameters.parameterList); }
      )*
      //metatypes? 
      (
        typeConstraints
        { def.setTypeConstraintList($typeConstraints.typeConstraintList); 
          dec.setTypeConstraintList($typeConstraints.typeConstraintList); }
      )?
      ( 
        { $declaration = def; }
        block 
        { def.setBlock($block.block); }
      //-> ^(METHOD_DEFINITION VOID_MODIFIER memberName methodParameters? block)   
      | 
        (
          specifier
          { dec.setSpecifierExpression($specifier.specifierExpression); }
        )?
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      //-> ^(METHOD_DECLARATION VOID_MODIFIER memberName methodParameters? specifier?)   
      )
    ;

setterDeclaration returns [AttributeSetterDefinition declaration]
    : ASSIGN 
      { $declaration = new AttributeSetterDefinition($ASSIGN); 
        $declaration.setType( new VoidModifier(null) ); }
      memberNameDeclaration 
      { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        block
        { $declaration.setBlock($block.block); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      )
    //-> ^(ATTRIBUTE_SETTER_DEFINITION[$ASSIGN] VOID_MODIFIER memberName block)
    ;

inferredAttributeDeclaration returns [AnyAttribute declaration]
    @init { AttributeGetterDefinition def=null;
            AttributeDeclaration dec=null; }
    : VALUE_MODIFIER 
      { ValueModifier fm = new ValueModifier($VALUE_MODIFIER);
        def = new AttributeGetterDefinition($VALUE_MODIFIER);
        dec = new AttributeDeclaration($VALUE_MODIFIER);
        def.setType(fm);
        dec.setType(fm);
        $declaration = dec; }
      memberNameDeclaration
      { dec.setIdentifier($memberNameDeclaration.identifier); 
        def.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        (
          specifier 
          { dec.setSpecifierOrInitializerExpression($specifier.specifierExpression); }
        | 
          initializer
          { dec.setSpecifierOrInitializerExpression($initializer.initializerExpression); }
        )?
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
        //-> ^(ATTRIBUTE_DECLARATION VALUE_MODIFIER memberName specifier? initializer?)
      | 
        { $declaration = def; }
        block
        { def.setBlock($block.block); }
        //-> ^(ATTRIBUTE_GETTER_DEFINITION VALUE_MODIFIER memberName block)
      )
    ;

typedMethodOrAttributeDeclaration returns [TypedDeclaration declaration]
    @init { AttributeGetterDefinition adef=new AttributeGetterDefinition(null);
            AttributeDeclaration adec=new AttributeDeclaration(null);
            MethodDefinition mdef=new MethodDefinition(null);
            MethodDeclaration mdec=new MethodDeclaration(null); 
            $declaration = adec; }
    : type
      { adef.setType($type.type);
        adec.setType($type.type); 
        mdef.setType($type.type);
        mdec.setType($type.type); }
      memberNameDeclaration
      { adef.setIdentifier($memberNameDeclaration.identifier);
        adec.setIdentifier($memberNameDeclaration.identifier); 
        mdef.setIdentifier($memberNameDeclaration.identifier);
        mdec.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        { $declaration = mdec; }
        (
          typeParameters
          { mdef.setTypeParameterList($typeParameters.typeParameterList);
            mdec.setTypeParameterList($typeParameters.typeParameterList); }
        )?
        (
          parameters
          { mdef.addParameterList($parameters.parameterList);
            mdec.addParameterList($parameters.parameterList); }
        )+
        //metatypes? 
        ( 
          typeConstraints
          { mdef.setTypeConstraintList($typeConstraints.typeConstraintList);
            mdec.setTypeConstraintList($typeConstraints.typeConstraintList); }
        )?
        ( 
          { $declaration = mdef; }
          mb=methodBody[$type.type] 
         { mdef.setBlock($mb.block); }
        //-> ^(METHOD_DEFINITION unionType memberName methodParameters memberBody)
        | 
          (
            ms=specifier
           { mdec.setSpecifierExpression($ms.specifierExpression); }
          )?
          s1=SEMICOLON
          { $declaration.setEndToken($s1); }
        //-> ^(METHOD_DECLARATION unionType memberName methodParameters specifier?)
        )
      | 
        (
          as=specifier 
          { adec.setSpecifierOrInitializerExpression($as.specifierExpression); }
        | 
          initializer
          { adec.setSpecifierOrInitializerExpression($initializer.initializerExpression); }
        )?
        s2=SEMICOLON
        { $declaration.setEndToken($s2); }
      //-> ^(ATTRIBUTE_DECLARATION unionType memberName specifier? initializer?)
      | 
        { $declaration = adef; }
        ab=attributeBody[$type.type]
        { if ($ab.result instanceof Block)
              adef.setBlock((Block)$ab.result); 
          else {
              $declaration = adec;
              adec.setSpecifierOrInitializerExpression((SpecifierExpression)$ab.result);
          } }
      //-> ^(ATTRIBUTE_GETTER_DEFINITION unionType memberName memberBody)      
      )
    ;

interfaceDeclaration returns [AnyInterface declaration]
    @init { InterfaceDefinition def=null; 
            InterfaceDeclaration dec=null; }
    : INTERFACE_DEFINITION
      { def = new InterfaceDefinition($INTERFACE_DEFINITION); 
        dec = new InterfaceDeclaration($INTERFACE_DEFINITION);
        $declaration = dec; }
      typeNameDeclaration 
      { dec.setIdentifier($typeNameDeclaration.identifier); 
        def.setIdentifier($typeNameDeclaration.identifier); }
      (
        typeParameters 
        { def.setTypeParameterList($typeParameters.typeParameterList); 
          dec.setTypeParameterList($typeParameters.typeParameterList); }
      )?
      (
        caseTypes
        { def.setCaseTypes($caseTypes.caseTypes); 
          dec.setCaseTypes($caseTypes.caseTypes); }
      )?
      /*metatypes?*/ 
      (
        adaptedTypes
        { def.setAdaptedTypes($adaptedTypes.adaptedTypes); }
      )?
      (
        satisfiedTypes
        { def.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); 
          dec.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); }
      )?
      (
        typeConstraints
        { def.setTypeConstraintList($typeConstraints.typeConstraintList); 
          dec.setTypeConstraintList($typeConstraints.typeConstraintList); }
      )?
      (
        { $declaration = def; }
        interfaceBody
        { def.setInterfaceBody($interfaceBody.interfaceBody); }
      //-> ^(INTERFACE_DEFINITION typeName interfaceParameters? interfaceBody)
      | 
        (
          typeSpecifier
          { dec.setTypeSpecifier($typeSpecifier.typeSpecifier); }
        )? 
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      //-> ^(INTERFACE_DECLARATION[$INTERFACE_DEFINITION] typeName interfaceParameters? typeSpecifier?)
      )
    ;

classDeclaration returns [AnyClass declaration]
    @init { ClassDefinition def=null; 
            ClassDeclaration dec=null; }
    : CLASS_DEFINITION 
      { def = new ClassDefinition($CLASS_DEFINITION); 
        dec = new ClassDeclaration($CLASS_DEFINITION);
        $declaration = dec; }
      typeNameDeclaration
      { dec.setIdentifier($typeNameDeclaration.identifier); 
        def.setIdentifier($typeNameDeclaration.identifier); }
      (
        typeParameters 
        { def.setTypeParameterList($typeParameters.typeParameterList); 
          dec.setTypeParameterList($typeParameters.typeParameterList); }
      )?
      (
        parameters
        { def.setParameterList($parameters.parameterList); 
          dec.setParameterList($parameters.parameterList); }
      )?
      (
        caseTypes
        { def.setCaseTypes($caseTypes.caseTypes); 
          dec.setCaseTypes($caseTypes.caseTypes); }
      )?
      /*metatypes?*/ 
      (
        extendedType
        { def.setExtendedType($extendedType.extendedType); 
          dec.setExtendedType($extendedType.extendedType); }
      )? 
      (
        satisfiedTypes
        { def.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); 
          dec.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); }
      )?
      (
        typeConstraints
        { def.setTypeConstraintList($typeConstraints.typeConstraintList); 
          dec.setTypeConstraintList($typeConstraints.typeConstraintList); }
      )?
      (
        { $declaration = def; }
        classBody
        { def.setClassBody($classBody.classBody); }
      //-> ^(CLASS_DEFINITION typeName classParameters? classBody)
      | 
        (
          typeSpecifier
          { dec.setTypeSpecifier($typeSpecifier.typeSpecifier); }
        )?
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      //-> ^(CLASS_DECLARATION[$CLASS_DEFINITION] typeName classParameters? typeSpecifier?)
      )
    ;

    
block returns [Block block]
    : LBRACE 
      { $block = new Block($LBRACE); }
      (
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $block.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $block.setEndToken($RBRACE); }
    //-> ^(BLOCK[$LBRACE] annotatedDeclarationOrStatement*)
    ;

//Note: interface bodies can't really contain 
//      statements, but error recovery works
//      much better if we validate that later
//      on, instead of doing it in the parser.
interfaceBody returns [InterfaceBody interfaceBody]
    : LBRACE 
      { $interfaceBody = new InterfaceBody($LBRACE); }
      (
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $interfaceBody.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $interfaceBody.setEndToken($RBRACE); }
    //-> ^(INTERFACE_BODY[$LBRACE] annotatedDeclarationOrStatement2*)
    ;

classBody returns [ClassBody classBody]
    : LBRACE
      { $classBody = new ClassBody($LBRACE); }
      (
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $classBody.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $classBody.setEndToken($RBRACE); }
    //-> ^(CLASS_BODY[$LBRACE] annotatedDeclarationOrStatement2*)
    ;

//This rule accounts for the problem that we
//can't tell whether a member body is a block
//or a named argument list until after we
//finish parsing it
attributeBody[StaticType type] returns [Node result]
      //options { memoize=true; }
    : 
      (namedArguments)
      => namedArguments //first try to match with no directives or control structures
      { SpecifierExpression specifier = new SyntheticSpecifierExpression(null);
        SimpleType t = $type instanceof SimpleType ? (SimpleType) $type : null;
        Expression e = new Expression(null);
        InvocationExpression ie = new InvocationExpression(null);
        BaseTypeExpression bme = new BaseTypeExpression(null);
        if (t!=null) bme.setIdentifier(t.getIdentifier());
        bme.setTypeArguments(new InferredTypeArguments(null));
        if (t!=null && t.getTypeArgumentList()!=null)
            bme.setTypeArguments(t.getTypeArgumentList());
        ie.setPrimary(bme);
        ie.setNamedArgumentList($namedArguments.namedArgumentList);
        e.setTerm(ie);
        specifier.setExpression(e);
        $result=specifier; }
    | block //if there is a "return" directive or control structure, it must be a block
      { $result=$block.block; } 
    ;

//This rule accounts for the problem that we
//can't tell whether a member body is a block
//or a named argument list until after we
//finish parsing it
methodBody[StaticType type] returns [Block block]
      //options { memoize=true; }
    : 
      (namedArguments)
      => namedArguments //first try to match with no directives or control structures
      { $block = new SyntheticBlock(null);
        SimpleType t = $type instanceof SimpleType ? (SimpleType) $type : null;
        Return r = new Return(null);
        Expression e = new Expression(null);
        InvocationExpression ie = new InvocationExpression(null);
        BaseTypeExpression bme = new BaseTypeExpression(null);
        if (t!=null) bme.setIdentifier(t.getIdentifier());
        bme.setTypeArguments(new InferredTypeArguments(null));
        if (t!=null && t.getTypeArgumentList()!=null)
            bme.setTypeArguments(t.getTypeArgumentList());
        ie.setPrimary(bme);
        ie.setNamedArgumentList($namedArguments.namedArgumentList);
        e.setTerm(ie);
        r.setExpression(e);
        $block.addStatement(r); }
    //-> ^(BLOCK ^(RETURN ^(EXPRESSION ^(INVOCATION_EXPRESSION ^(BASE_TYPE_EXPRESSION { ((CommonTree)$mt).getChild(0) } { ((CommonTree)$mt).getChild(1) } ) namedArguments))))
    | block //if there is a "return" directive or control structure, it must be a block
      { $block=$block.block; } 
    ;

extendedType returns [ExtendedType extendedType]
    : EXTENDS
      { $extendedType = new ExtendedType($EXTENDS); }
      (
        qualifiedType
        { $extendedType.setType($qualifiedType.type); }
        //-> ^(EXTENDED_TYPE[$EXTENDS] type ^(INVOCATION_EXPRESSION ^(EXTENDED_TYPE_EXPRESSION) positionalArguments))
      | SUPER MEMBER_OP 
        typeReference 
        { QualifiedType qt=new QualifiedType(null);
          SuperType st = new SuperType($SUPER);
          qt.setOuterType(st);
          qt.setIdentifier($typeReference.identifier);
          if ($typeReference.typeArgumentList!=null)
              qt.setTypeArgumentList($typeReference.typeArgumentList);
          $extendedType.setType(qt); }
        //-> ^(EXTENDED_TYPE[$EXTENDS] ^(QUALIFIED_TYPE SUPER_TYPE[$SUPER] typeReference) ^(INVOCATION_EXPRESSION ^(EXTENDED_TYPE_EXPRESSION) positionalArguments))
      )
      (
        positionalArguments
        { InvocationExpression ie = new InvocationExpression(null);
          ExtendedTypeExpression ete = new ExtendedTypeExpression(null);
          ie.setPrimary(ete);
          ete.setExtendedType($extendedType.getType());
          ie.setPositionalArgumentList($positionalArguments.positionalArgumentList);
          $extendedType.setInvocationExpression(ie); }
      | { displayRecognitionError(getTokenNames(),
            new MismatchedTokenException(LPAREN, input)); }
      )
    ;

satisfiedTypes returns [SatisfiedTypes satisfiedTypes]
    : SATISFIES 
      { $satisfiedTypes = new SatisfiedTypes($SATISFIES); }
      t1=qualifiedType 
      { $satisfiedTypes.addType($t1.type); }
      (
        i=INTERSECTION_OP
        { $satisfiedTypes.setEndToken($i); }
        (
          t2=qualifiedType
          { $satisfiedTypes.addType($t2.type); 
            $satisfiedTypes.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(),
              new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )*
    //-> ^(SATISFIED_TYPES[$SATISFIES] type+)
    ;

abstractedType returns [AbstractedType abstractedType]
    : ABSTRACTED_TYPE
      { $abstractedType = new AbstractedType($ABSTRACTED_TYPE); }
      qualifiedType
      { $abstractedType.setType($qualifiedType.type); }
    ;

adaptedTypes returns [AdaptedTypes adaptedTypes]
    : ADAPTED_TYPES 
      { $adaptedTypes = new AdaptedTypes($ADAPTED_TYPES); }
      t1=qualifiedType 
      { $adaptedTypes.addType($t1.type); }
      (
        i=INTERSECTION_OP 
        { $adaptedTypes.setEndToken($i); }
        (
          t2=qualifiedType
          { $adaptedTypes.addType($t2.type); 
            $adaptedTypes.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(),
              new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )*
    ;

caseTypes returns [CaseTypes caseTypes]
    : CASE_TYPES
      { $caseTypes = new CaseTypes($CASE_TYPES); }
      ct1=caseType 
      { if ($ct1.type!=null) $caseTypes.addType($ct1.type); 
        if ($ct1.instance!=null) $caseTypes.addBaseMemberExpression($ct1.instance); }
      (
        u=UNION_OP 
        { $caseTypes.setEndToken($u); }
        (
          ct2=caseType
          { if ($ct2.type!=null) $caseTypes.addType($ct2.type); 
            if ($ct2.instance!=null) $caseTypes.addBaseMemberExpression($ct2.instance); 
            $caseTypes.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(),
              new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )*
    ;

caseType returns [SimpleType type, BaseMemberExpression instance]
    : t=qualifiedType 
      { $type=$t.type;}
    | memberName //-> ^(BASE_MEMBER_EXPRESSION memberName)
      { $instance = new BaseMemberExpression(null);
        $instance.setIdentifier($memberName.identifier);
        $instance.setTypeArguments( new InferredTypeArguments(null) ); }
    ;

//Support for metatypes
//Note that we don't need this for now
/*metatypes
    : IS_OP type ('&' type)* 
    -> ^(METATYPES[$IS_OP] type*)
    ;*/

parameters returns [ParameterList parameterList]
    : LPAREN
      { $parameterList=new ParameterList($LPAREN); }
      (
        ap1=parameterDeclaration 
        { if ($ap1.parameter!=null)
              $parameterList.addParameter($ap1.parameter); }
        (
          c=COMMA
          { $parameterList.setEndToken($c); }
          (
            (~(COMPILER_ANNOTATION | LIDENTIFIER | UIDENTIFIER)) => 
            { displayRecognitionError(getTokenNames(),
                new MismatchedTokenException(UIDENTIFIER, input)); }
          | 
            ap2=parameterDeclaration
            { $parameterList.addParameter($ap2.parameter); 
              $parameterList.setEndToken(null); }
          )
        )*
      )? 
      RPAREN
      { $parameterList.setEndToken($RPAREN); }
      //-> ^(PARAMETER_LIST[$LPAREN] annotatedParameter*)
    ;

//special rule for syntactic predicate
//to distinguish between a formal 
//parameter list and a parenthesized body 
//of an inline callable argument
//be careful with this one, since it 
//matches "()", which can also be an 
//argument list
parametersStart
    : LPAREN ( annotatedDeclarationStart | RPAREN )
    ;
    
// FIXME: This accepts more than the language spec: named arguments
// and varargs arguments can appear in any order.  We'll have to
// enforce the rule that the ... appears at the end of the parapmeter
// list in a later pass of the compiler.
parameter returns [Parameter parameter]
    @init { ValueParameterDeclaration vp = new ValueParameterDeclaration(null); 
            FunctionalParameterDeclaration fp = new FunctionalParameterDeclaration(null); 
            $parameter = vp; }
    : parameterType
      { vp.setType($parameterType.type);
        fp.setType($parameterType.type); }
      memberName
      { vp.setIdentifier($memberName.identifier);
        fp.setIdentifier($memberName.identifier); }
      (
        (
          valueParameter
        )?
        //-> ^(VALUE_PARAMETER_DECLARATION parameterType memberName specifier?)
      |
        (
          parameters
          { fp.addParameterList($parameters.parameterList); }
        )+
        { $parameter=fp; }
          //for callable parameters
        //-> ^(FUNCTIONAL_PARAMETER_DECLARATION parameterType memberName parameters+ specifier?)
      )
      (
        specifier
        { DefaultArgument da = new DefaultArgument(null);
          $parameter.setDefaultArgument(da);
          da.setSpecifierExpression($specifier.specifierExpression); }
      )?
    ;

parameterRef returns [Parameter parameter]
    @init { ValueParameterDeclaration vp = new ValueParameterDeclaration(null);
            vp.setType(new ValueModifier(null));
            $parameter = vp; }
    : memberName
      { vp.setIdentifier($memberName.identifier); }
      (
        specifier
        { DefaultArgument da = new DefaultArgument(null);
          $parameter.setDefaultArgument(da);
          da.setSpecifierExpression($specifier.specifierExpression); }
      )?
    ;

parameterDeclaration returns [Parameter parameter]
    : compilerAnnotations
      (
        (LIDENTIFIER (SPECIFY|COMMA|RPAREN)) =>
        r=parameterRef
        { $parameter=$r.parameter; }
      | 
        annotations 
        p=parameter
        { $parameter=$p.parameter;
          $parameter.setAnnotationList($annotations.annotationList); }
      )
      { if ($parameter!=null)
        $parameter.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

valueParameter
    : ENTRY_OP type memberName
    ;

parameterType returns [Type type]
    : type 
      { $type = $type.type; }
      ( 
        ELLIPSIS
        { SequencedType st = new SequencedType($ELLIPSIS);
          st.setType($type);
          $type = st; }
      )?
    | VOID_MODIFIER
      { $type = new VoidModifier($VOID_MODIFIER); }
    ;

typeParameters returns [TypeParameterList typeParameterList]
    : SMALLER_OP
      { $typeParameterList = new TypeParameterList($SMALLER_OP); }
      tp1=typeParameter
      { if ($tp1.typeParameter instanceof TypeParameterDeclaration)
            $typeParameterList.addTypeParameterDeclaration((TypeParameterDeclaration)$tp1.typeParameter);
        if ($tp1.typeParameter instanceof SequencedTypeParameterDeclaration)
            $typeParameterList.setSequencedTypeParameterDeclaration((SequencedTypeParameterDeclaration)$tp1.typeParameter); }
      (
        c=COMMA
        { $typeParameterList.setEndToken($c); }
        (
          tp2=typeParameter
          { if ($tp2.typeParameter instanceof TypeParameterDeclaration)
                $typeParameterList.addTypeParameterDeclaration((TypeParameterDeclaration)$tp2.typeParameter);
            if ($tp2.typeParameter instanceof SequencedTypeParameterDeclaration)
                $typeParameterList.setSequencedTypeParameterDeclaration((SequencedTypeParameterDeclaration)$tp2.typeParameter); 
            $typeParameterList.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )*
      LARGER_OP
      { $typeParameterList.setEndToken($LARGER_OP); }
    //-> ^(TYPE_PARAMETER_LIST[$SMALLER_OP] typeParameter+)
    ;

typeParameter returns [Declaration typeParameter]
    @init { TypeParameterDeclaration tpd = new TypeParameterDeclaration(null);
            SequencedTypeParameterDeclaration spd = new SequencedTypeParameterDeclaration(null); }
    : { $typeParameter = tpd; }
      ( 
        variance 
        { tpd.setTypeVariance($variance.typeVariance); } 
      )? 
      typeNameDeclaration
      { tpd.setIdentifier($typeNameDeclaration.identifier); } 
    //-> ^(TYPE_PARAMETER_DECLARATION variance? typeName)
    | { $typeParameter = spd; }
      typeNameDeclaration
      { spd.setIdentifier($typeNameDeclaration.identifier); } 
      ELLIPSIS
      { $typeParameter.setEndToken($ELLIPSIS); }
    //-> ^(SEQUENCED_TYPE_PARAMETER typeName)
    ;

variance returns [TypeVariance typeVariance]
    : IN_OP //-> ^(TYPE_VARIANCE[$IN_OP])
      { $typeVariance = new TypeVariance($IN_OP); }
    | OUT //-> ^(TYPE_VARIANCE[$OUT])
      { $typeVariance = new TypeVariance($OUT); }
    ;
    
typeConstraint returns [TypeConstraint typeConstraint]
    : compilerAnnotations
      TYPE_CONSTRAINT
      { $typeConstraint = new TypeConstraint($TYPE_CONSTRAINT); 
        $typeConstraint.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      typeNameDeclaration 
      { $typeConstraint.setIdentifier($typeNameDeclaration.identifier); }
      //(typeParameters)?
      (
        parameters
        { $typeConstraint.setParameterList($parameters.parameterList); }
      )?
      (
        caseTypes
        { $typeConstraint.setCaseTypes($caseTypes.caseTypes); }
      )?
      //metatypes? 
      (
        satisfiedTypes
        { $typeConstraint.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); }
      )?
      ( 
        abstractedType
        { $typeConstraint.setAbstractedType($abstractedType.abstractedType); }
      )?
    ;

typeConstraints returns [TypeConstraintList typeConstraintList]
    : { $typeConstraintList=new TypeConstraintList(null); }
      (
        typeConstraint
        { $typeConstraintList.addTypeConstraint($typeConstraint.typeConstraint); }
      )+
    //-> ^(TYPE_CONSTRAINT_LIST typeConstraint2+)
    ;

declarationOrStatement returns [Statement statement]
    options {memoize=true;}
    : compilerAnnotations
      ( 
        (annotatedDeclarationStart) => declaration
        { $statement=$declaration.declaration; }
      | s=statement
        { $statement=$s.statement; }
      )
      { if ($statement!=null)
            $statement.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

declaration returns [Declaration declaration]
    : annotations
    ( 
      objectDeclaration
      { $declaration=$objectDeclaration.declaration; }
    | setterDeclaration
      { $declaration=$setterDeclaration.declaration; }
    | voidOrInferredMethodDeclaration
      { $declaration=$voidOrInferredMethodDeclaration.declaration; }
    | inferredAttributeDeclaration
      { $declaration=$inferredAttributeDeclaration.declaration; }
    | typedMethodOrAttributeDeclaration
      { $declaration=$typedMethodOrAttributeDeclaration.declaration; }
    | classDeclaration
      { $declaration=$classDeclaration.declaration; }
    | interfaceDeclaration
      { $declaration=$interfaceDeclaration.declaration; }
    /*| { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(CLASS_DEFINITION, input)); }
      SEMICOLON
      { $declaration=new BrokenDeclaration($SEMICOLON); }*/
    )
    { if ($declaration!=null)
          $declaration.setAnnotationList($annotations.annotationList);  }
    ;

//special rule for syntactic predicate
//to distinguish between an annotation
//and an expression statement
/*annotatedDeclarationStart
    : declarationStart
    | LIDENTIFIER
      ( 
          declarationStart
        | LIDENTIFIER
        | nonstringLiteral | stringLiteral
        | arguments annotatedDeclarationStart //we need to recurse because it could be an inline callable argument
      )
    ;*/

annotatedDeclarationStart
    : annotation* declarationStart
    ;

//special rule for syntactic predicates
//that distinguish declarations from
//expressions
declarationStart
    : declarationKeyword 
    | type (ELLIPSIS | LIDENTIFIER)
    ;

declarationKeyword
    : VALUE_MODIFIER
    | FUNCTION_MODIFIER
    | ASSIGN
    | VOID_MODIFIER
    | INTERFACE_DEFINITION
    | CLASS_DEFINITION
    | OBJECT_DEFINITION
    ;

statement returns [Statement statement]
    : directiveStatement
      { $statement = $directiveStatement.directive; }
    | controlStatement
      { $statement = $controlStatement.controlStatement; }
    | expressionOrSpecificationStatement
      { $statement = $expressionOrSpecificationStatement.statement; }
    ;

expressionOrSpecificationStatement returns [Statement statement]
    @init { SpecifierStatement ss=new SpecifierStatement(null); 
            ExpressionStatement es=new ExpressionStatement(null); }
    : expression 
      { if ($expression.expression!=null) 
            es.setExpression($expression.expression);
        $statement = es; }
      (
        specifier
        { ss.setSpecifierExpression($specifier.specifierExpression);
          
          ss.setBaseMemberExpression($expression.expression.getTerm()); 
          $statement = ss; }
      )?
      (
        SEMICOLON
        { $statement.setEndToken($SEMICOLON); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(SEMICOLON, input)); }
        COMMA
        { $statement.setEndToken($COMMA); }
    )
    ;

directiveStatement returns [Directive directive]
    : d=directive 
      { $directive=$d.directive; } 
      SEMICOLON
      { $directive.setEndToken($SEMICOLON); }
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
        functionOrExpression
        { $directive.setExpression($functionOrExpression.expression); }
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

typeSpecifier returns [TypeSpecifier typeSpecifier]
    : SPECIFY 
      { $typeSpecifier = new TypeSpecifier($SPECIFY); }
      qualifiedType
      { $typeSpecifier.setType($qualifiedType.type); }
    //-> ^(TYPE_SPECIFIER[$SPECIFY] type)
    ;

specifier returns [SpecifierExpression specifierExpression]
    : SPECIFY 
      { $specifierExpression = new SpecifierExpression($SPECIFY); }
      functionOrExpression
      { $specifierExpression.setExpression($functionOrExpression.expression); }
    ;

initializer returns [InitializerExpression initializerExpression]
    : ASSIGN_OP 
      { $initializerExpression = new InitializerExpression($ASSIGN_OP); }
      expression
      { $initializerExpression.setExpression($expression.expression); }
    //-> ^(INITIALIZER_EXPRESSION[$ASSIGN_OP] expression)
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
        bte.setTypeArguments( new InferredTypeArguments(null) );
        if ($typeReference.typeArgumentList!=null)
            bte.setTypeArguments($typeReference.typeArgumentList);
        $primary=bte; }
    | memberReference
      { BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($memberReference.identifier);
        bme.setTypeArguments( new InferredTypeArguments(null) );
        if ($memberReference.typeArgumentList!=null)
            bme.setTypeArguments($memberReference.typeArgumentList);
        $primary=bme; }
    | parExpression
      { $primary=$parExpression.expression; }
    ;

primary returns [Primary primary]
    : base
      { $primary=$base.primary; }
    (   
      //TODO: re-enable:
        /*inlineFunctionalArgument
      |*/ qualifiedReference
      { QualifiedMemberOrTypeExpression qe;
        if ($qualifiedReference.isMember)
            qe = new QualifiedMemberExpression(null);
        else
            qe = new QualifiedTypeExpression(null);
        qe.setPrimary($primary);
        qe.setMemberOperator($qualifiedReference.operator);
        qe.setIdentifier($qualifiedReference.identifier);
        qe.setTypeArguments( new InferredTypeArguments(null) );
        if ($qualifiedReference.typeArgumentList!=null)
            qe.setTypeArguments($qualifiedReference.typeArgumentList);
        $primary=qe; }
      | indexExpression
        { IndexExpression xe = new IndexExpression(null);
          xe.setPrimary($primary);
          xe.setIndexOperator($indexExpression.operator); 
          xe.setElementOrRange($indexExpression.elementOrRange);
          $primary=xe; }
      | arguments
        { InvocationExpression ie = new InvocationExpression(null);
          ie.setPrimary($primary);
          if ($arguments.argumentList instanceof PositionalArgumentList)
              ie.setPositionalArgumentList((PositionalArgumentList)$arguments.argumentList);
          if ($arguments.argumentList instanceof NamedArgumentList)
              ie.setNamedArgumentList((NamedArgumentList)$arguments.argumentList);
          $primary=ie; }
    )*
    ;

qualifiedReference returns [Identifier identifier, MemberOperator operator, 
                            TypeArgumentList typeArgumentList, boolean isMember]
    : memberSelectionOperator 
      { $operator = $memberSelectionOperator.operator;
        $identifier = new Identifier($operator.getToken());
        $identifier.setText("");
        $isMember=true; }
      ( 
        memberReference
        { $identifier = $memberReference.identifier;
          $typeArgumentList = $memberReference.typeArgumentList; }
      | typeReference
        { $identifier = $typeReference.identifier;
          $typeArgumentList = $typeReference.typeArgumentList;  
          $isMember=false; }
      | (~(LIDENTIFIER|UIDENTIFIER))=>
        { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); }
      )
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
        sequencedArgument
        { $sequenceEnumeration.setSequencedArgument($sequencedArgument.sequencedArgument); }
      | 
        comprehension
        { $sequenceEnumeration.setComprehension($comprehension.comprehension); }
      )?
      RBRACE
      { $sequenceEnumeration.setEndToken($RBRACE); }
    ;

expressions returns [ExpressionList expressionList]
    : { $expressionList = new ExpressionList(null); }
      e1=expression 
      { $expressionList.addExpression($e1.expression); }
      ( c=COMMA 
        { $expressionList.setEndToken($c); }
        (
          e2=expression 
          { $expressionList.addExpression($e2.expression);
            $expressionList.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); } //TODO: sometimes it should be RPAREN!
        )
      )*
    ;

memberReference returns [Identifier identifier, 
        TypeArgumentList typeArgumentList]
    : memberName
      { $identifier = $memberName.identifier; }
      (
        (typeArgumentsStart)
        => typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;

typeReference returns [Identifier identifier, 
        TypeArgumentList typeArgumentList]
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
    (
      UIDENTIFIER ('.' UIDENTIFIER)* /*| 'subtype')*/ (DEFAULT_OP|ARRAY)*
      ((INTERSECTION_OP|UNION_OP|ENTRY_OP) UIDENTIFIER ('.' UIDENTIFIER)* (DEFAULT_OP|ARRAY)*)*
      (LARGER_OP|SMALLER_OP|COMMA|ELLIPSIS)
    | 
      LARGER_OP //for IDE only!
    )
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
      | ':' s=index 
      { ElementRange er3 = new ElementRange(null);
        $elementOrRange = er3;
        er3.setLowerBound($l.expression); 
        er3.setLength($s.expression); }
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
      ( //TODO: get rid of the predicate and use the approach
        //      in expressionOrSpecificationStatement
        (namedArgumentStart) 
        => namedArgument
        { if ($namedArgument.namedArgument!=null) 
              $namedArgumentList.addNamedArgument($namedArgument.namedArgument); }
      )* 
      ( 
        sequencedArgument
        { $namedArgumentList.setSequencedArgument($sequencedArgument.sequencedArgument); }
      | 
        comprehension
        { $namedArgumentList.setComprehension($comprehension.comprehension); }
      )?
      RBRACE
      { $namedArgumentList.setEndToken($RBRACE); }
    ;

sequencedArgument returns [SequencedArgument sequencedArgument]
    : compilerAnnotations
      expressions
      { sequencedArgument = new SequencedArgument(null);
        sequencedArgument.setExpressionList($expressions.expressionList);
        sequencedArgument.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      (
        ELLIPSIS
        { sequencedArgument.setEllipsis(new Ellipsis($ELLIPSIS)); }
      )?
    ;

namedArgument returns [NamedArgument namedArgument]
    : compilerAnnotations 
    ( 
      namedSpecifiedArgument
      { $namedArgument = $namedSpecifiedArgument.specifiedArgument; }
    | 
      namedArgumentDeclaration
      { $namedArgument = $namedArgumentDeclaration.declaration; }
    )
    { if ($namedArgument!=null)
          namedArgument.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

namedSpecifiedArgument returns [SpecifiedArgument specifiedArgument]
    : memberNameDeclaration 
      { $specifiedArgument = new SpecifiedArgument(null); 
        $specifiedArgument.setIdentifier($memberNameDeclaration.identifier); }
      specifier 
      { $specifiedArgument.setSpecifierExpression($specifier.specifierExpression); }
      SEMICOLON
      { $specifiedArgument.setEndToken($SEMICOLON); }
    ;

objectArgument returns [ObjectArgument declaration]
    : OBJECT_DEFINITION 
      { $declaration = new ObjectArgument($OBJECT_DEFINITION); 
        $declaration.setType(new ValueModifier(null)); }
      memberNameDeclaration
      { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        extendedType
        { $declaration.setExtendedType($extendedType.extendedType); } 
      )?
      ( 
        satisfiedTypes
        { $declaration.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); } 
      )?
      (
        classBody
        { $declaration.setClassBody($classBody.classBody); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      )
    //-> ^(OBJECT_ARGUMENT[$OBJECT_DEFINITION] VALUE_MODIFIER memberName extendedType? satisfiedTypes? classBody?)
    ;

voidOrInferredMethodArgument returns [MethodArgument declaration]
    : { $declaration=new MethodArgument(null); }
      (
        VOID_MODIFIER
      { $declaration.setType(new VoidModifier($VOID_MODIFIER)); }
      |
        FUNCTION_MODIFIER
      { $declaration.setType(new FunctionModifier($FUNCTION_MODIFIER)); }
      ) 
      memberNameDeclaration 
      { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      (
        parameters
        { $declaration.addParameterList($parameters.parameterList); }
      )*
      block
      { $declaration.setBlock($block.block); }
    //-> ^(METHOD_ARGUMENT VOID_MODIFIER memberName parameters* block)
    ;

inferredGetterArgument returns [AttributeArgument declaration]
    : { $declaration=new AttributeArgument(null); }
      VALUE_MODIFIER 
      { $declaration.setType(new ValueModifier($VALUE_MODIFIER)); }
      memberNameDeclaration 
      { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      block
      { $declaration.setBlock($block.block); }
      //-> ^(ATTRIBUTE_ARGUMENT VALUE_MODIFIER memberName block)      
    ;

typedMethodOrGetterArgument returns [TypedArgument declaration]
    @init { MethodArgument marg = new MethodArgument(null);
            AttributeArgument aarg = new AttributeArgument(null); 
            $declaration=aarg; }
    : type 
      { marg.setType($type.type);
        aarg.setType($type.type); }
      memberNameDeclaration
      { marg.setIdentifier($memberNameDeclaration.identifier);
        aarg.setIdentifier($memberNameDeclaration.identifier); }
      ( 
        { $declaration = marg; }
        (
          parameters
          { marg.addParameterList($parameters.parameterList); }
        )+
      )?
      methodBody[$type.type]
      { marg.setBlock($methodBody.block); 
        aarg.setBlock($methodBody.block); }
      //-> ^(METHOD_ARGUMENT unionType memberName parameters+ memberBody)
      //-> ^(ATTRIBUTE_ARGUMENT unionType memberName memberBody)      
    ;

namedArgumentDeclaration returns [NamedArgument declaration]
    : objectArgument
      { $declaration=$objectArgument.declaration; }
    | typedMethodOrGetterArgument
      { $declaration=$typedMethodOrGetterArgument.declaration; }
    | voidOrInferredMethodArgument
      { $declaration=$voidOrInferredMethodArgument.declaration; }
    | inferredGetterArgument
      { $declaration=$inferredGetterArgument.declaration; }
    ;

//special rule for syntactic predicate
//to distinguish between a named argument
//and a sequenced argument
namedArgumentStart
    : compilerAnnotation* 
      (specificationStart | declarationStart)
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER '='
    ;

parExpression returns [Expression expression] 
    : LPAREN 
      { $expression = new Expression($LPAREN); }
      assignmentExpression
      { $expression.setTerm($assignmentExpression.term); }
      RPAREN
      { $expression.setEndToken($RPAREN); }
    ;
    
positionalArguments returns [PositionalArgumentList positionalArgumentList]
    : LPAREN 
      { $positionalArgumentList = new PositionalArgumentList($LPAREN); }
      ( pa1=positionalArgument
        { if ($pa1.positionalArgument!=null)
              $positionalArgumentList.addPositionalArgument($pa1.positionalArgument); }
        (
          c=COMMA 
          { $positionalArgumentList.setEndToken($c); }
          (
            pa2=positionalArgument
            { $positionalArgumentList.addPositionalArgument($pa2.positionalArgument); 
              positionalArgumentList.setEndToken(null); }
          | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(LIDENTIFIER, input)); }
          )
        )* 
        (
          ELLIPSIS
          { $positionalArgumentList.setEllipsis( new Ellipsis($ELLIPSIS) ); }
        )?
      )? 
      (
        comprehension
        { $positionalArgumentList.setComprehension($comprehension.comprehension); }
      )?
      RPAREN
      { $positionalArgumentList.setEndToken($RPAREN); }
    ;

positionalArgument returns [PositionalArgument positionalArgument]
    @init { $positionalArgument = new PositionalArgument(null); }
    : functionOrExpression
      { $positionalArgument.setExpression($functionOrExpression.expression); }
    ;
    
functionOrExpression returns [Expression expression]
    @init { FunctionArgument fa = new FunctionArgument(null);
            fa.setType(new FunctionModifier(null)); }
    : (FUNCTION_MODIFIER|VOID_MODIFIER|parametersStart)=>
      (
        FUNCTION_MODIFIER 
        { fa.setType(new FunctionModifier($FUNCTION_MODIFIER)); }
      | VOID_MODIFIER
         { fa.setType(new VoidModifier($VOID_MODIFIER)); }
      )?
      p1=parameters
      { fa.addParameterList($p1.parameterList); }
      ( 
        (parametersStart)=> 
        p2=parameters
        { fa.addParameterList($p2.parameterList); }
      )*
      e1=expression
      { fa.setExpression($e1.expression); 
        $expression = new Expression(null); 
        $expression.setTerm(fa); }
    /*| VALUE_MODIFIER
      { fa.setType(new FunctionModifier($VALUE_MODIFIER)); } 
      e1=expression
      { fa.addParameterList(new ParameterList(null));
        fa.setExpression($e1.expression); 
        $positionalArgument = fa; }*/
    | e2=expression
      { $expression = $e2.expression; }
    ;
    
/*inlineFunctionalArgument
    : memberName ((parametersStart) => parameters)? 
      (LPAREN expression RPAREN | block)
    ;*/

comprehension returns [Comprehension comprehension]
    @init { $comprehension = new Comprehension(null); }
    : forComprehensionClause
      { $comprehension.setForComprehensionClause($forComprehensionClause.comprehensionClause); }
    ;

comprehensionClause returns [ComprehensionClause comprehensionClause]
    : forComprehensionClause 
      { $comprehensionClause = $forComprehensionClause.comprehensionClause; }
    | ifComprehensionClause 
      { $comprehensionClause = $ifComprehensionClause.comprehensionClause; }
    | expressionComprehensionClause 
      { $comprehensionClause = $expressionComprehensionClause.comprehensionClause; }
    ;

expressionComprehensionClause returns [ExpressionComprehensionClause comprehensionClause]
    : expression
      { $comprehensionClause = new ExpressionComprehensionClause(null);
        $comprehensionClause.setExpression($expression.expression); }
    | { displayRecognitionError(getTokenNames(), 
          new MismatchedTokenException(LIDENTIFIER, input)); }
    ;

forComprehensionClause returns [ForComprehensionClause comprehensionClause]
    : FOR_CLAUSE
      { $comprehensionClause = new ForComprehensionClause($FOR_CLAUSE); }
      forIterator
      { $comprehensionClause.setForIterator($forIterator.iterator); }
      comprehensionClause
      { $comprehensionClause.setComprehensionClause($comprehensionClause.comprehensionClause); }
    ;
    
ifComprehensionClause returns [IfComprehensionClause comprehensionClause]
    : IF_CLAUSE
      { $comprehensionClause = new IfComprehensionClause($IF_CLAUSE); }
      condition
      { $comprehensionClause.setCondition($condition.condition); }
      comprehensionClause
      { $comprehensionClause.setComprehensionClause($comprehensionClause.comprehensionClause); }
    ;
    
assignmentExpression returns [Term term]
    : ee1=thenElseExpression
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
    //| APPLY_OP 
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
    //| DEFAULT_ASSIGN_OP { $operator = new DefaultAssignOp($DEFAULT_ASSIGN_OP); }
    ;

thenElseExpression returns [Term term]
    : de1=disjunctionExpression
      { $term = $de1.term; }
      (
        thenElseOperator 
        { $thenElseOperator.operator.setLeftTerm($term);
          $term = $thenElseOperator.operator; }
        de2=disjunctionExpression
        { $thenElseOperator.operator.setRightTerm($de2.term); }
      )*
    ;

thenElseOperator returns [BinaryOperatorExpression operator]
    : ELSE_CLAUSE 
      { $operator = new DefaultOp($ELSE_CLAUSE); }
    | THEN_CLAUSE
      { $operator = new ThenOp($THEN_CLAUSE); }
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
      | to1=typeOperator
        { $to1.operator.setTerm($ee1.term); 
          $term = $to1.operator;}
        t1=qualifiedType
        { $to1.operator.setType($t1.type); }
      )?
    | to2=typeOperator
      { $term = $to2.operator; }
      t2=qualifiedType //TODO: support "type" here, using a predicate
      { $to2.operator.setType($t2.type); }
      ee3=existenceEmptinessExpression
      { $to2.operator.setTerm($ee3.term); }
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

typeOperator returns [TypeOperatorExpression operator]
    : IS_OP
      { $operator = new IsOp($IS_OP); }
    | EXTENDS
      { $operator = new Extends($EXTENDS); }
    | SATISFIES
      { $operator = new Satisfies($SATISFIES); }
    ;

existenceEmptinessExpression returns [Term term]
    : de1=rangeIntervalEntryExpression
      { $term = $de1.term; }
      (
        eno1=existsNonemptyOperator
        { $term = $eno1.operator;
          $eno1.operator.setTerm($de1.term); }
      )?
    | eno2=existsNonemptyOperator
      { $term = $eno2.operator; }
      de2=rangeIntervalEntryExpression
      { $eno2.operator.setTerm($de2.term); }
    ;

existsNonemptyOperator returns [UnaryOperatorExpression operator]
    : EXISTS 
      { $operator = new Exists($EXISTS); }
    | NONEMPTY
      { $operator = new Nonempty($NONEMPTY); }
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
    | SEGMENT_OP
      { $operator = new SegmentOp($SEGMENT_OP); }
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
    : ne1=defaultExpression
      { $term = $ne1.term; }
      (
        multiplicativeOperator 
        { $multiplicativeOperator.operator.setLeftTerm($term);
          $term = $multiplicativeOperator.operator; }
        ne2=defaultExpression
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

defaultExpression returns [Term term]
    : rie1=negationComplementExpression
      { $term = $rie1.term; }
      (
        defaultOperator 
        { $defaultOperator.operator.setLeftTerm($term);
          $term = $defaultOperator.operator; }
        rie2=negationComplementExpression
        { $defaultOperator.operator.setRightTerm($rie2.term); }
      )*
    ;

defaultOperator returns [DefaultOp operator]
    : DEFAULT_OP 
      { $operator = new DefaultOp($DEFAULT_OP); }
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
         => e=expression sl2=stringLiteral
        { $stringTemplate.addExpression($e.expression);
          if (sl2!=null) 
              $stringTemplate.addStringLiteral($sl2.stringLiteral); }
      )+
    ;

typeArguments returns [TypeArgumentList typeArgumentList]
    : SMALLER_OP
      { $typeArgumentList = new TypeArgumentList($SMALLER_OP); }
      ta1=typeArgument 
      { if ($ta1.type!=null)
            $typeArgumentList.addType($ta1.type); }
      (
        c=COMMA
        { $typeArgumentList.setEndToken($c); }
        (
          ta2=typeArgument
          { $typeArgumentList.addType($ta2.type); 
            $typeArgumentList.setEndToken(null); }
          | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )* 
      LARGER_OP
      { $typeArgumentList.setEndToken($LARGER_OP); }
    ;

typeArgument returns [Type type]
    : t=type
      { $type = $t.type; }
      ( 
        ELLIPSIS
        { SequencedType st = new SequencedType($ELLIPSIS);
          st.setType($t.type); 
          $type = st; }
      )? 
    ;
    
type returns [StaticType type]
    : unionType 
      { $type=$unionType.type; }
    ;
    
entryType returns [StaticType type]
    @init { EntryType bt=null; }
    : t1=abbreviatedType
      { $type=$t1.type; }
      (
        ENTRY_OP
        { bt=new EntryType(null);
          bt.setKeyType($type);
          bt.setEndToken($ENTRY_OP); 
          $type=bt; }
        (
          t2=abbreviatedType
          { bt.setValueType($t2.type);
            bt.setEndToken(null); }
//        | { displayRecognitionError(getTokenNames(), 
//                new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )?
    ;

unionType returns [StaticType type]
    @init { UnionType ut=null; }
    : it1=intersectionType
      { $type = $it1.type;
        ut = new UnionType(null);
        ut.addStaticType($type); }
      ( 
        (
          u=UNION_OP
          { ut.setEndToken($u); }
          (
            it2=intersectionType
            { ut.addStaticType($it2.type);
              ut.setEndToken(null); }
//          | { displayRecognitionError(getTokenNames(), 
//                new MismatchedTokenException(UIDENTIFIER, input)); }
          )
        )+
        { $type = ut; }
      )?
    ;

intersectionType returns [StaticType type]
    @init { IntersectionType it=null; }
    : at1=entryType
      { $type = $at1.type;
        it = new IntersectionType(null);
        it.addStaticType($type); }
      ( 
        (
          i=INTERSECTION_OP
          { it.setEndToken($i); }
          (
            at2=entryType
            { it.addStaticType($at2.type);
              it.setEndToken(null); }
//          | { displayRecognitionError(getTokenNames(), 
//                new MismatchedTokenException(UIDENTIFIER, input)); }
          )
        )+
        { $type = it; }
      )?
    ;

abbreviatedType returns [StaticType type]
    @init { FunctionType bt=null; }
    : qualifiedType
      { $type=$qualifiedType.type; }
      (
        DEFAULT_OP 
        { OptionalType ot = new OptionalType(null);
          ot.setDefiniteType($type);
          ot.setEndToken($DEFAULT_OP);
          $type=ot; }
      | ARRAY 
        { SequenceType ot = new SequenceType(null);
          ot.setElementType($type);
          ot.setEndToken($DEFAULT_OP);
          $type=ot; }
      | LPAREN
        { bt = new FunctionType(null);
          bt.setEndToken($LPAREN);
          bt.setReturnType($type);
          $type=bt; }
          (
            t1=type
            { if ($t1.type!=null)
                  bt.addArgumentType($t1.type); }
            (
              COMMA
              { bt.setEndToken($COMMA); } 
              t2=type
              { if ($t2.type!=null)
                  bt.getArgumentTypes().add($t2.type); }
            )*
          )?
        RPAREN
        { bt.setEndToken($RPAREN); }
      )*
    ;
    
qualifiedType returns [SimpleType type]
    : ot=typeNameWithArguments
      { BaseType bt = new BaseType(null);
        bt.setIdentifier($ot.identifier);
        if ($ot.typeArgumentList!=null)
            bt.setTypeArgumentList($ot.typeArgumentList);
        $type=bt; }
      (
        MEMBER_OP 
        it=typeNameWithArguments
        { QualifiedType qt = new QualifiedType($MEMBER_OP);
          qt.setIdentifier($it.identifier);
          if ($it.typeArgumentList!=null)
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
    
annotations returns [AnnotationList annotationList]
    : { $annotationList = new AnnotationList(null); }
      ( 
        annotation 
        { $annotationList.addAnnotation($annotation.annotation); }
      )*
    ;

annotation returns [Annotation annotation]
    : annotationName
      { $annotation = new Annotation(null);
        BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($annotationName.identifier);
        bme.setTypeArguments( new InferredTypeArguments(null) );
        $annotation.setPrimary(bme); }
      annotationArguments
      { if ($annotationArguments.argumentList instanceof PositionalArgumentList)
            $annotation.setPositionalArgumentList((PositionalArgumentList)$annotationArguments.argumentList);
        if ($annotationArguments.argumentList instanceof NamedArgumentList)
            $annotation.setNamedArgumentList((NamedArgumentList)$annotationArguments.argumentList); }
    ;

annotationArguments returns [ArgumentList argumentList]
    : arguments 
      { $argumentList=$arguments.argumentList; }
    | literalArguments 
      { $argumentList=$literalArguments.argumentList; }
    ;

literalArguments returns [PositionalArgumentList argumentList]
    : { $argumentList = new PositionalArgumentList(null); }
      (
        literalArgument
        { $argumentList.addPositionalArgument($literalArgument.positionalArgument); }
      )*
    ;
    
literalArgument returns [PositionalArgument positionalArgument]
    : nonstringLiteral
      { $positionalArgument = new PositionalArgument(null);
        Expression e = new Expression(null);
        e.setTerm($nonstringLiteral.literal);
        $positionalArgument.setExpression(e); }
    | stringLiteral
      { $positionalArgument = new PositionalArgument(null);
        Expression e = new Expression(null);
        e.setTerm($stringLiteral.stringLiteral);
        $positionalArgument.setExpression(e); 
        $stringLiteral.stringLiteral.getToken().setType(ASTRING_LITERAL); }
    ;

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
    : DIFFERENCE_OP
    | INCREMENT_OP 
    | DECREMENT_OP 
    | COMPLEMENT_OP
    ;
    
compilerAnnotations returns [List<CompilerAnnotation> annotations]
    : { $annotations = new ArrayList<CompilerAnnotation>(); }
    (
      compilerAnnotation
      { $annotations.add($compilerAnnotation.annotation); }
    )*
    ;
    
compilerAnnotation returns [CompilerAnnotation annotation]
    : COMPILER_ANNOTATION 
      { $annotation=new CompilerAnnotation($COMPILER_ANNOTATION); }
      annotationName 
      { $annotation.setIdentifier($annotationName.identifier); }
      ( 
          INDEX_OP
          stringLiteral
          { $annotation.setStringLiteral($stringLiteral.stringLiteral); }
          RBRACKET
      )?
    ;


condition returns [Condition condition]
    : (LPAREN EXISTS)=>existsCondition
      { $condition=$existsCondition.condition; }
    | (LPAREN NONEMPTY)=>nonemptyCondition
      { $condition=$nonemptyCondition.condition; }
    | (LPAREN IS_OP)=>isCondition 
      { $condition=$isCondition.condition; }
    | (LPAREN SATISFIES)=>satisfiesCondition
      { $condition=$satisfiesCondition.condition; }
    | booleanCondition
      { $condition=$booleanCondition.condition; }
    ;
    
booleanCondition returns [BooleanCondition condition]
    : LPAREN
      { $condition = new BooleanCondition($LPAREN); }
      (expression
      { $condition.setExpression($expression.expression); })?
      RPAREN
      { $condition.setEndToken($RPAREN); }
    //-> ^(BOOLEAN_CONDITION expression)
    ;
    
existsCondition returns [Condition condition]
    @init { ExistsCondition ec = null; }
    : (LPAREN EXISTS LIDENTIFIER RPAREN)
      => l1=LPAREN 
      { ec = new ExistsCondition($l1); 
        $condition = ec; }
      e1=EXISTS impliedVariable
      { ec.setVariable($impliedVariable.variable); }
      r1=RPAREN
      { ec.setEndToken($r1); }
    //-> ^(EXISTS_CONDITION[$EXISTS] impliedVariable)
    | (LPAREN EXISTS compilerAnnotations (declarationStart|specificationStart))
      => l2=LPAREN 
      { ec = new ExistsCondition($l2); 
        $condition = ec; }
      e2=EXISTS 
      specifiedVariable 
      { ec.setVariable($specifiedVariable.variable); }
      r2=RPAREN
      { ec.setEndToken($r2); }
    //-> ^(EXISTS_CONDITION[$EXISTS] specifiedVariable2)
    | bc=booleanCondition
      { $condition=$bc.condition; }
    ;
    
nonemptyCondition returns [Condition condition]
    @init { NonemptyCondition nc = null; }
    : (LPAREN NONEMPTY LIDENTIFIER RPAREN) 
      => l1=LPAREN 
      { nc = new NonemptyCondition($l1); 
        $condition = nc; }
      n1=NONEMPTY impliedVariable 
      { nc.setVariable($impliedVariable.variable); }
      r1=RPAREN
      { nc.setEndToken($r1); }
    //-> ^(NONEMPTY_CONDITION[$NONEMPTY] impliedVariable)
    | (LPAREN NONEMPTY compilerAnnotations (declarationStart|specificationStart))
      => l2=LPAREN 
      { nc = new NonemptyCondition($l2); 
        $condition = nc; }
      n2=NONEMPTY 
      (specifiedVariable 
      { nc.setVariable($specifiedVariable.variable); })?
      r2=RPAREN
      { nc.setEndToken($r2); }
    //-> ^(NONEMPTY_CONDITION[$NONEMPTY] specifiedVariable2)
    | bc=booleanCondition
      { $condition=$bc.condition; }
    ;

isCondition returns [Condition condition]
    @init { IsCondition ic = null; }
    : (LPAREN IS_OP type LIDENTIFIER RPAREN) 
      => l1=LPAREN 
      { ic = new IsCondition($l1); 
        $condition = ic; }
      i1=IS_OP t1=type impliedVariable 
      { ic.setType($t1.type);
        ic.setVariable($impliedVariable.variable); }
      r1=RPAREN
      { ic.setEndToken($r1); }
    //-> ^(IS_CONDITION[$IS_OP] unionType ^(VARIABLE SYNTHETIC_VARIABLE memberName ^(SPECIFIER_EXPRESSION ^(EXPRESSION ^(BASE_MEMBER_EXPRESSION memberName)))))
    | (LPAREN IS_OP type LIDENTIFIER SPECIFY)
      => l2=LPAREN
      { ic = new IsCondition($l2); 
        $condition = ic; }
      i2=IS_OP 
      ( t2=type
      { ic.setType($t2.type);
        Variable v = new Variable(null);
        v.setType($t2.type); 
        ic.setVariable(v); }
      memberName
      { ic.getVariable().setIdentifier($memberName.identifier); }
      specifier
      { ic.getVariable().setSpecifierExpression($specifier.specifierExpression); })?
      r2=RPAREN
      { ic.setEndToken($r2); }
    //-> ^(IS_CONDITION[$IS_OP] unionType ^(VARIABLE unionType memberName specifier))
    | bc=booleanCondition
      { $condition=$bc.condition; }
    ;

satisfiesCondition returns [SatisfiesCondition condition]
    : LPAREN 
      { $condition = new SatisfiesCondition($LPAREN); }
      SATISFIES 
      (t1=qualifiedType 
      { $condition.setLeftType($t1.type); }
      t2=qualifiedType 
      { $condition.setRightType($t2.type); })?
      RPAREN
      { $condition.setEndToken($RPAREN); }
    //-> ^(SATISFIES_CONDITION[$SATISFIES] type+)
    ;

controlStatement returns [ControlStatement controlStatement]
    : ifElse 
      { $controlStatement=$ifElse.statement; }
    | switchCaseElse 
      { $controlStatement=$switchCaseElse.statement; }
    | whileLoop 
      { $controlStatement=$whileLoop.statement; }
    | forElse 
      { $controlStatement=$forElse.statement; }
    | tryCatchFinally
      { $controlStatement=$tryCatchFinally.statement; }
    ;

controlBlock returns [Block block]
    : ( (LBRACE)=> b=block
        { $block=$b.block; }
      | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(LBRACE, input)); }
      )
    ;

ifElse returns [IfStatement statement]
    : { $statement=new IfStatement(null); }
      ifBlock 
      { $statement.setIfClause($ifBlock.clause); }
      ( 
        elseBlock
        { $statement.setElseClause($elseBlock.clause); }
      )?
    //-> ^(IF_STATEMENT ifBlock elseBlock?)
    ;

ifBlock returns [IfClause clause]
    : IF_CLAUSE 
      { $clause = new IfClause($IF_CLAUSE); }
      condition
      { $clause.setCondition($condition.condition); }
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

elseBlock returns [ElseClause clause]
    : ELSE_CLAUSE 
      { $clause = new ElseClause($ELSE_CLAUSE); }
      (
        elseIf 
        { $clause.setBlock($elseIf.block); }
      | 
        block
        { $clause.setBlock($block.block); }
      )
    ;

elseIf returns [Block block]
    : ifElse
      { $block = new Block(null);
        $block.addStatement($ifElse.statement); }
    //-> ^(BLOCK ifElse)
    ;

switchCaseElse returns [SwitchStatement statement]
    : { $statement = new SwitchStatement(null); }
      switchHeader 
      { $statement.setSwitchClause($switchHeader.clause); }
      cases
      { $statement.setSwitchCaseList($cases.switchCaseList);
        Expression ex = $switchHeader.clause.getExpression();
        if (ex!=null && ex.getTerm() instanceof BaseMemberExpression) {
          Identifier id = ((BaseMemberExpression) ex.getTerm()).getIdentifier();
          for (CaseClause cc: $cases.switchCaseList.getCaseClauses()) {
            CaseItem item = cc.getCaseItem();
            if (item instanceof IsCase) {
              IsCase ic = (IsCase) item;
              Variable v = new Variable(null);
              v.setType(new SyntheticVariable(null));
              v.setIdentifier(id);
              SpecifierExpression se = new SpecifierExpression(null);
              Expression e = new Expression(null);
              BaseMemberExpression bme = new BaseMemberExpression(null);
              bme.setIdentifier(id);
              bme.setTypeArguments( new InferredTypeArguments(null) );
              e.setTerm(bme);
              se.setExpression(e);
              v.setSpecifierExpression(se);
              ic.setVariable(v);
            }
          } 
        } 
      }
    //-> ^(SWITCH_STATEMENT switchHeader cases)
    ;

switchHeader returns [SwitchClause clause]
    : SWITCH_CLAUSE 
      { $clause = new SwitchClause($SWITCH_CLAUSE); }
      LPAREN 
      expression 
      { $clause.setExpression($expression.expression); }
      RPAREN
    ;

cases returns [SwitchCaseList switchCaseList]
    : { $switchCaseList = new SwitchCaseList(null); }
      (
        caseBlock
        { $switchCaseList.addCaseClause($caseBlock.clause); }
      )+
      (
        defaultCaseBlock
        { $switchCaseList.setElseClause($defaultCaseBlock.clause); }
      )?
    //-> ^(SWITCH_CASE_LIST caseItem+ defaultCaseItem?)
    ;
    
caseBlock returns [CaseClause clause]
    : CASE_CLAUSE 
      { $clause = new CaseClause($CASE_CLAUSE); }
      LPAREN 
      caseItem 
      { $clause.setCaseItem($caseItem.item); }
      RPAREN 
      block
      { $clause.setBlock($block.block); }
    ;

defaultCaseBlock returns [ElseClause clause]
    : ELSE_CLAUSE 
      { $clause = new ElseClause($ELSE_CLAUSE); }
      block
      { $clause.setBlock($block.block); }
    ;

caseItem returns [CaseItem item]
    : (IS_OP)=>isCaseCondition 
      { $item=$isCaseCondition.item; }
    | (SATISFIES)=>satisfiesCaseCondition
      { $item=$satisfiesCaseCondition.item; }
    | matchCaseCondition
      { $item=$matchCaseCondition.item; }
    ;

matchCaseCondition returns [MatchCase item]
    : expressions
      { $item = new MatchCase(null);
        $item.setExpressionList($expressions.expressionList); }
    //-> ^(MATCH_CASE expressions)
    ;

isCaseCondition returns [IsCase item]
    : IS_OP 
      { $item = new IsCase($IS_OP); }
      type
      { $item.setType($type.type); }
    //-> ^(IS_CASE[$IS_OP] unionType)
    ;

satisfiesCaseCondition returns [SatisfiesCase item]
    : SATISFIES 
      { $item = new SatisfiesCase($SATISFIES); }
      qualifiedType
      { $item.setType($qualifiedType.type); }
    //-> ^(SATISFIES_CASE[$SATISFIES] type)
    ;

forElse returns [ForStatement statement]
    : { $statement=new ForStatement(null); }
      forBlock 
      { $statement.setForClause($forBlock.clause); }
      (
        failBlock
        { $statement.setElseClause($failBlock.clause); }
      )?
    //-> ^(FOR_STATEMENT forBlock failBlock?)
    ;

forBlock returns [ForClause clause]
    : FOR_CLAUSE 
      { $clause = new ForClause($FOR_CLAUSE); }
      forIterator 
      { $clause.setForIterator($forIterator.iterator); }
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

failBlock returns [ElseClause clause]
    : ELSE_CLAUSE 
      { $clause = new ElseClause($ELSE_CLAUSE); }
      block
      { $clause.setBlock($block.block); }
    ;

forIterator returns [ForIterator iterator]
    @init { ValueIterator vi = null;
            KeyValueIterator kvi = null; }
    : LPAREN
    { vi = new ValueIterator($LPAREN); 
      kvi = new KeyValueIterator($LPAREN); 
      $iterator = vi; }
    compilerAnnotations
    ( 
      v1=var
      (
        { vi.setVariable($v1.variable); }
        c1=containment
        { vi.setSpecifierExpression($c1.specifierExpression); }
      //-> ^(VALUE_ITERATOR $v1 containment)
      | 
        { $iterator = kvi; }
        ENTRY_OP
        { kvi.setKeyVariable($v1.variable); }
        v2=var
        { kvi.setValueVariable($v2.variable); }
        c2=containment
        {  kvi.setSpecifierExpression($c2.specifierExpression); }
      //-> ^(KEY_VALUE_ITERATOR $v1 $v2 containment)
      )?
    )?
    { if ($iterator!=null)
          $iterator.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    RPAREN
    { $iterator.setEndToken($RPAREN); }
    ;
    
containment returns [SpecifierExpression specifierExpression]
    : IN_OP 
      { $specifierExpression = new SpecifierExpression($IN_OP); }
      (expression
      { $specifierExpression.setExpression($expression.expression); })?
    //-> ^(SPECIFIER_EXPRESSION expression)
    ;
    
whileLoop returns [WhileStatement statement]
    : { $statement = new WhileStatement(null); }
      whileBlock
      { $statement.setWhileClause($whileBlock.clause); }
    //-> ^(WHILE_STATEMENT whileBlock)
    ;

whileBlock returns [WhileClause clause]
    : WHILE_CLAUSE
      { $clause = new WhileClause($WHILE_CLAUSE); }
      condition 
      { $clause.setCondition($condition.condition); }
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

tryCatchFinally returns [TryCatchStatement statement]
    : { $statement = new TryCatchStatement(null); }
      tryBlock 
      { $statement.setTryClause($tryBlock.clause); }
      (
        catchBlock
      { $statement.addCatchClause($catchBlock.clause); }
      )* 
      ( 
        finallyBlock
      { $statement.setFinallyClause($finallyBlock.clause); }
      )?
    //-> ^(TRY_CATCH_STATEMENT tryBlock catchBlock* finallyBlock?)
    ;

tryBlock returns [TryClause clause]
    : TRY_CLAUSE 
      { $clause = new TryClause($TRY_CLAUSE); }
      (
        resource
        { $clause.setResource($resource.resource); }
        controlBlock
        { $clause.setBlock($controlBlock.block); }
      |
        block
        { $clause.setBlock($block.block); }
      )
    ;

catchBlock returns [CatchClause clause]
    : CATCH_CLAUSE 
      { $clause = new CatchClause($CATCH_CLAUSE); }
      catchVariable
      { $clause.setCatchVariable($catchVariable.catchVariable); }
      block
      { $clause.setBlock($block.block); }
    ;

catchVariable returns [CatchVariable catchVariable]
    : LPAREN 
      { $catchVariable=new CatchVariable($LPAREN); }
      (variable 
      { $catchVariable.setVariable($variable.variable); })?
      RPAREN 
      { $catchVariable.setEndToken($RPAREN); }
    ;


finallyBlock returns [FinallyClause clause]
    : FINALLY_CLAUSE 
      { $clause = new FinallyClause($FINALLY_CLAUSE); }
      block
      { $clause.setBlock($block.block); }
    ;

resource returns [Resource resource]
    : LPAREN 
    { $resource = new Resource($LPAREN); }
    (
    (COMPILER_ANNOTATION|declarationStart|specificationStart) 
      => specifiedVariable
      { $resource.setVariable($specifiedVariable.variable); }
    //-> ^(RESOURCE specifiedVariable2)
    | expression
      { $resource.setExpression($expression.expression); }
    //-> ^(RESOURCE expression)
    )
    RPAREN
    { $resource.setEndToken($RPAREN); }
    ;

specifiedVariable returns [Variable variable]
    : v=variable 
      { $variable = $v.variable; }
      (
        specifier
        { $variable.setSpecifierExpression($specifier.specifierExpression); }
      )?
    ;

variable returns [Variable variable]
    : compilerAnnotations
      var
      { $variable=$var.variable;
        $variable.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;
    
var returns [Variable variable]
    : { $variable = new Variable(null); }
    (
      type 
      { $variable.setType($type.type); }
      mn1=memberName 
      { $variable.setIdentifier($mn1.identifier); }
      ( 
        p1=parameters
        { $variable.addParameterList($p1.parameterList); }
      )*
    //-> ^(VARIABLE unionType memberName parameters*)
    | 
      { $variable.setType( new ValueModifier(null) ); }
      mn2=memberName
      { $variable.setIdentifier($mn2.identifier); }
    //-> ^(VARIABLE VALUE_MODIFIER memberName)
    | 
      { $variable.setType( new FunctionModifier(null) ); }
      mn3=memberName 
      { $variable.setIdentifier($mn3.identifier); }
      (
        p3=parameters
        { $variable.addParameterList($p3.parameterList); }
      )+
    )
    //-> ^(VARIABLE FUNCTION_MODIFIER memberName parameters+)
    ;

impliedVariable returns [Variable variable]
    : memberName 
      { Variable v = new Variable(null);
        v.setType(new SyntheticVariable(null));
        v.setIdentifier($memberName.identifier);
        SpecifierExpression se = new SpecifierExpression(null);
        Expression e = new Expression(null);
        BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($memberName.identifier);
        bme.setTypeArguments( new InferredTypeArguments(null) );
        e.setTerm(bme);
        se.setExpression(e);
        v.setSpecifierExpression(se); 
        $variable = v; }
    //-> ^(VARIABLE SYNTHETIC_VARIABLE memberName ^(SPECIFIER_EXPRESSION ^(EXPRESSION ^(BASE_MEMBER_EXPRESSION memberName))))
    ;


// Lexer

fragment
Digits
    : Digit+ ('_' Digit+)*
    ;

fragment 
Exponent    
    : ( 'e' | 'E' ) ( '+' | '-' )? Digit+ 
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
      | FractionalMagnitude { $type = FLOAT_LITERAL; }
      | Magnitude?
      )
    ;
    
fragment SPREAD_OP: '[].';
fragment ARRAY: '[]';
fragment INDEX_OP: '[';
//distinguish the spread operator "x[]."
//from a sequenced type "T[]..."
LBRACKET
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
QMARK
    : '?'
    (
      ('[' ~']') => '[' { $type = SAFE_INDEX_OP; }
    | ('.' ~'.') => '.' { $type = SAFE_MEMBER_OP; }
    | { $type = DEFAULT_OP; }
    )
    ;

CHAR_LITERAL
    :   '`' ( ~ ('`' | '\\') | EscapeSequence ) '`'
    ;

QUOTED_LITERAL
    :   '\'' QuotedLiteralPart '\''?
    ;

fragment
ASTRING_LITERAL:;

fragment
QuotedLiteralPart
    : ~('\'')*
    ;

STRING_LITERAL
    :   '"' StringPart '"'?
    ;

fragment
StringPart
    : ( ~ ('\\' | '"') | EscapeSequence) *
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
        |   '{' HexDigits HexDigits? '}'
        )
    ;

fragment
HexDigits
    : HexDigit HexDigit HexDigit HexDigit
    ;

fragment
HexDigit
    : '0'..'9' | 'A'..'F' | 'a'..'f'
    ;

WS  
    :   (
             ' '
        |    '\r'
        |    '\t'
        |    '\f'
        |    '\n'
        )+
        {
            //skip();
            $channel = HIDDEN;
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
        '*/'?
        //('*/'/|{displayRecognitionError(getTokenNames(), new MismatchedSetException(null,input));})
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

THEN_CLAUSE
    :   'then'
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
    :   '...'
    ;

RANGE_OP
    :   '..'
    ;

SEGMENT_OP
    :   ':'
    ;

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

/*APPLY_OP
    :   '.='
    ;*/

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

/*DEFAULT_ASSIGN_OP
    :   '?='
    ;*/

AND_ASSIGN_OP
    :   '&&='
    ;

OR_ASSIGN_OP
    :   '||='
    ;

COMPILER_ANNOTATION
    :   '@'
    ;

fragment
LIDENTIFIER :;
fragment
PIDENTIFIER :;
fragment
AIDENTIFIER :;

UIDENTIFIER 
    :   IdentifierStart IdentifierPart*
        { int cp = $text.codePointAt(0);
          if (cp=='_' || Character.isLowerCase(cp)) $type=LIDENTIFIER; }
    |   UIdentifierPrefix name=IdentifierPart+
        { setText($text.substring(2)); }
    |   LIdentifierPrefix name=IdentifierPart+
        { setText($text.substring(2)); $type=LIDENTIFIER; }
    ;

fragment
IdentifierStart
    :   '_'
    |   Letter
        { if (!Character.isJavaIdentifierStart($text.codePointAt(0))) { 
          //TODO: error!
        } }
    ;       

fragment
LIdentifierPrefix
    : '\\i'
    ;

fragment
UIdentifierPrefix
    : '\\I'
    ;
    
fragment 
IdentifierPart
    :   '_'
    |   Digit
    |   Letter
        { if (!Character.isJavaIdentifierPart($text.codePointAt(0))) { 
          //TODO: error!
        } }
    ;

fragment
Letter
    : 'a'..'z' 
    | 'A'..'Z' 
    | '\u00c0'..'\ufffe'
    ;

fragment
Digit
    : '0'..'9'
    ;
