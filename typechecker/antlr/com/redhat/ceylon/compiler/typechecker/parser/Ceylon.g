
grammar Ceylon;

options {
    memoize=false;
}

@parser::header { package com.redhat.ceylon.compiler.typechecker.parser;
                  import com.redhat.ceylon.compiler.typechecker.tree.MissingToken;
                  import com.redhat.ceylon.compiler.typechecker.tree.Node;
                  import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
                  import static com.redhat.ceylon.compiler.typechecker.tree.CustomTree.*;
                  import static com.redhat.ceylon.compiler.typechecker.tree.CustomTree.Package; }
@lexer::header { package com.redhat.ceylon.compiler.typechecker.parser; }

@members {
    private java.util.List<ParseError> errors 
            = new java.util.ArrayList<ParseError>();

    public ParseError newParseError(String[] tn,
            RecognitionException re) {
        ParseError parseError = new ParseError(this, re, expecting, tn);
        expecting = -1;
        return parseError;
    }
    
    public ParseError newParseError(String[] tn, 
            RecognitionException re,
            int code) {
        ParseError parseError = new ParseError(this, re, tn, code);
        return parseError;
    }

    @Override public void displayRecognitionError(String[] tn,
            RecognitionException re) {
        errors.add(newParseError(tn, re));
    }
    public void displayRecognitionError(String[] tn,
            RecognitionException re, 
            int code) {
        errors.add(newParseError(tn, re, code));
    }
    public java.util.List<ParseError> getErrors() {
        return errors;
    }
    int expecting=-1;
    
    @Override
    protected Object getMissingSymbol(IntStream input,
            RecognitionException e,
            int expectedTokenType,
            BitSet follow) {
        String tokenText;
        if ( expectedTokenType==Token.EOF ) tokenText = "<missing EOF>";
        else tokenText = "<missing "+getTokenNames()[expectedTokenType]+">";
        MissingToken t = new MissingToken(expectedTokenType, tokenText);
        Token current = ((TokenStream)input).LT(1);
        if ( current.getType() == Token.EOF ) {
            current = ((TokenStream)input).LT(-1);
        }
        t.setLine(current.getLine());
        t.setCharPositionInLine(current.getCharPositionInLine());
        t.setChannel(DEFAULT_TOKEN_CHANNEL);
        t.setInputStream(current.getInputStream());
        return t;
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
    @init { $compilationUnit = new CompilationUnit(null);
            ImportList importList = new ImportList(null);
            $compilationUnit.setImportList(importList); }
    : (
        ca=compilerAnnotations
        SEMICOLON
        { $compilationUnit.getCompilerAnnotations().addAll($ca.annotations); }
      )?
      ( 
        importDeclaration 
        { importList.addImport($importDeclaration.importDeclaration); 
          $compilationUnit.connect(importList); }
      |
        (annotatedModuleDescriptorStart) =>
        moduleDescriptor 
        { $compilationUnit.addModuleDescriptor($moduleDescriptor.moduleDescriptor); }
      |
        (annotatedPackageDescriptorStart) =>
        packageDescriptor
        { $compilationUnit.addPackageDescriptor($packageDescriptor.packageDescriptor); }
      |
        toplevelDeclaration
        { if ($toplevelDeclaration.declaration!=null)
              $compilationUnit.addDeclaration($toplevelDeclaration.declaration); }
      | RBRACE
        { displayRecognitionError(getTokenNames(),
              new MismatchedTokenException(EOF, input)); }
      )*
      EOF
    ;

toplevelDeclaration returns [Declaration declaration]
    : ca=compilerAnnotations 
      d=declaration
      { $declaration = $d.declaration;
        if ($declaration!=null)
            $declaration.getCompilerAnnotations().addAll($ca.annotations); }
    ;

annotatedModuleDescriptorStart
    : compilerAnnotations annotations MODULE
    ;

annotatedPackageDescriptorStart
    : compilerAnnotations annotations PACKAGE ~MEMBER_OP
    ;

moduleDescriptor returns [ModuleDescriptor moduleDescriptor]
    : compilerAnnotations annotations
      MODULE 
      { $moduleDescriptor = new ModuleDescriptor($MODULE); 
        $moduleDescriptor.setAnnotationList($annotations.annotationList);
        $moduleDescriptor.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      p0=packagePath
      { $moduleDescriptor.setImportPath($p0.importPath); }
      (
        ins=importNamespace
        { $moduleDescriptor.setNamespace($ins.identifier); }
        SEGMENT_OP
        ( 
          s1=STRING_LITERAL
          { $moduleDescriptor.setGroupQuotedLiteral(new QuotedLiteral($s1)); }
        |
          p1=packagePath
          { $moduleDescriptor.setGroupImportPath($p1.importPath); }
        )
        (
          SEGMENT_OP
          s2=STRING_LITERAL
          { $moduleDescriptor.setArtifact(new QuotedLiteral($s2)); }
          (
            SEGMENT_OP
            s3=STRING_LITERAL
            { $moduleDescriptor.setClassifier(new QuotedLiteral($s3)); }
          )?
        )?
      )?
      (
        s3=STRING_LITERAL
        { $moduleDescriptor.setVersion(new QuotedLiteral($s3)); }
      )?
      importModuleList
      { $moduleDescriptor.setImportModuleList($importModuleList.importModuleList); }
    ;

importModuleList returns [ImportModuleList importModuleList]
    : LBRACE
      { $importModuleList = new ImportModuleList($LBRACE); }
      (
        compilerAnnotations annotations
        (
          c=inferredAttributeDeclaration
          { $c.declaration.setAnnotationList(new AnnotationList(null));
            $importModuleList.addConstant($c.declaration);
            if ($c.declaration!=null)
                $c.declaration.setAnnotationList($annotations.annotationList);
            if ($c.declaration!=null)
                $c.declaration.getCompilerAnnotations()
                    .addAll($compilerAnnotations.annotations); }
        |
          importModule
          { if ($importModule.importModule!=null)
                $importModuleList.addImportModule($importModule.importModule); 
            if ($importModule.importModule!=null)
                $importModule.importModule.setAnnotationList($annotations.annotationList);
            if ($importModule.importModule!=null)
                $importModule.importModule.getCompilerAnnotations()
                    .addAll($compilerAnnotations.annotations); }
        )
      )*
      RBRACE
      { $importModuleList.setEndToken($RBRACE); }
    ;

packageDescriptor returns [PackageDescriptor packageDescriptor]
    : compilerAnnotations annotations
      PACKAGE 
      { $packageDescriptor = new PackageDescriptor($PACKAGE); 
        $packageDescriptor.setAnnotationList($annotations.annotationList); 
        $packageDescriptor.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      packagePath
      { $packageDescriptor.setImportPath($packagePath.importPath); 
        expecting=SEMICOLON; }
      SEMICOLON
      { $packageDescriptor.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

importModule returns [ImportModule importModule]
    : IMPORT
      { $importModule = new ImportModule($IMPORT); }
      ((LIDENTIFIER SEGMENT_OP) =>
        ins=importNamespace
        { $importModule.setNamespace($ins.identifier); }
        SEGMENT_OP
      )?
      (
        ( 
          s1=STRING_LITERAL
          { $importModule.setQuotedLiteral(new QuotedLiteral($s1)); }
        |
          p1=packagePath
          { $importModule.setImportPath($p1.importPath); }
        )
        (
          SEGMENT_OP
          s2=STRING_LITERAL
          { $importModule.setArtifact(new QuotedLiteral($s2)); }
          (
            SEGMENT_OP
            s3=STRING_LITERAL
            { $importModule.setClassifier(new QuotedLiteral($s3)); }
          )?
        )?
      )
      (
        s3=STRING_LITERAL
        { $importModule.setVersion(new QuotedLiteral($s3)); 
          expecting=SEMICOLON; }
      |
        c=memberName
        { BaseMemberExpression bme = new BaseMemberExpression(null);
          bme.setIdentifier($c.identifier);
          bme.setTypeArguments(new InferredTypeArguments(null)); 
          $importModule.setConstantVersion(bme); 
          expecting=SEMICOLON; }
      )?
      SEMICOLON
      { $importModule.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

importNamespace returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER); }
    | { displayRecognitionError(getTokenNames(),
              new MismatchedTokenException(LIDENTIFIER, input), 5001); }
      UIDENTIFIER
      { $identifier = new Identifier($UIDENTIFIER); }
    ;

importDeclaration returns [Import importDeclaration]
    : IMPORT 
      { $importDeclaration = new Import($IMPORT); } 
      (
        packagePath
        { $importDeclaration.setImportPath($packagePath.importPath); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); }
      )
      importElementList
      { $importDeclaration.setImportMemberOrTypeList($importElementList.importMemberOrTypeList); }
    ;

importElementList returns [ImportMemberOrTypeList importMemberOrTypeList]
    @init { ImportMemberOrTypeList il=null; 
            boolean wildcarded = false; }
    : LBRACE
      { il = new ImportMemberOrTypeList($LBRACE);
        $importMemberOrTypeList = il; }
      (
      (
        ie1=importElement 
        { if ($ie1.importMemberOrType!=null)
              il.addImportMemberOrType($ie1.importMemberOrType); } 
      | iw1=importWildcard 
        { wildcarded = true;
          if ($iw1.importWildcard!=null) 
              il.setImportWildcard($iw1.importWildcard); }
      )
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
        | iw2=importWildcard
          { wildcarded = true;
            if ($iw2.importWildcard!=null) 
                il.setImportWildcard($iw2.importWildcard); 
            if ($iw2.importWildcard!=null) 
                il.setEndToken(null); } 
        | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(ELLIPSIS, input)); }
        )
      )*
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

packagePath returns [ImportPath importPath]
    @init { $importPath = new ImportPath(null); }
    : pn1=packageName 
      { if ($pn1.identifier!=null) 
            $importPath.addIdentifier($pn1.identifier); } 
      ( 
        m=MEMBER_OP 
        { $importPath.setEndToken($m); }
        ((LIDENTIFIER|UIDENTIFIER) =>
          pn2=packageName 
          { $importPath.addIdentifier($pn2.identifier); 
            $importPath.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(LIDENTIFIER, input)); }
        )
      )*
    ;

packageName returns [Identifier identifier]
    : LIDENTIFIER
      { $identifier = new Identifier($LIDENTIFIER);
        $LIDENTIFIER.setType(PIDENTIFIER);}
    | UIDENTIFIER
      { $identifier = new Identifier($UIDENTIFIER);
        $UIDENTIFIER.setType(PIDENTIFIER);}
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
              new MismatchedTokenException(LIDENTIFIER, input), 5001); }
      typeName { $identifier = $typeName.identifier; }
    ;

typeNameDeclaration returns [Identifier identifier]
    : typeName { $identifier = $typeName.identifier; }
    | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(UIDENTIFIER, input), 5002); }
      memberName { $identifier = $memberName.identifier; }
      
    ;

enumeratedObject returns [Enumerated declaration]
    : NEW
      { $declaration = new Enumerated($NEW); }
      (
        memberNameDeclaration
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
      (
        dc=delegatedConstructor
        { $declaration.setDelegatedConstructor($dc.delegatedConstructor); }
      )?
      (
        block
        { $declaration.setBlock($block.block); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      )
    ;
    
objectDeclaration returns [ObjectDefinition declaration]
    : OBJECT_DEFINITION
      { $declaration = new ObjectDefinition($OBJECT_DEFINITION); 
        $declaration.setType(new ValueModifier(null)); }
      (
        memberNameDeclaration
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
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
    ;

objectExpression returns [ObjectExpression objectExpression]
    : OBJECT_DEFINITION
      { $objectExpression = new ObjectExpression($OBJECT_DEFINITION); }
      ( 
        extendedType
        { $objectExpression.setExtendedType($extendedType.extendedType); } 
      )?
      ( 
        satisfiedTypes
        { $objectExpression.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); } 
      )?
      (
        classBody
        { $objectExpression.setClassBody($classBody.classBody); }
        | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $objectExpression.setEndToken($SEMICOLON); }
      )
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
      (
        memberNameDeclaration
        { dec.setIdentifier($memberNameDeclaration.identifier); 
          def.setIdentifier($memberNameDeclaration.identifier); }
      )?
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
      | 
        (
          functionSpecifier
          { dec.setSpecifierExpression($functionSpecifier.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { expecting=-1; }
        { $declaration.setEndToken($SEMICOLON); }
      )
    ;

setterDeclaration returns [AttributeSetterDefinition declaration]
    : ASSIGN 
      { $declaration = new AttributeSetterDefinition($ASSIGN); 
        $declaration.setType( new VoidModifier(null) ); }
      (
        memberNameDeclaration 
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
      ( 
        block
        { $declaration.setBlock($block.block); }
      | 
        (
          functionSpecifier
          { $declaration.setSpecifierExpression($functionSpecifier.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); 
          expecting=-1; }
      )
    ;

tuplePatternStart
    : LBRACKET
      (
        compilerAnnotations PRODUCT_OP? LIDENTIFIER
      |
        (compilerAnnotations declarationStart) => 
        (compilerAnnotations declarationStart)
      |
        tuplePatternStart
      )
    ;

variableOrTuplePattern returns [Pattern pattern]
    : 
      (tuplePatternStart) => tuplePattern
      { $pattern = $tuplePattern.pattern; }
    | 
      variablePattern
      { $pattern = $variablePattern.pattern; }
    ;

pattern returns [Pattern pattern]
    : 
      (variable ENTRY_OP) =>
      ki1=keyItemPattern
      { $pattern = $ki1.pattern; }
    |
      (tuplePattern ENTRY_OP) =>
      ki2=keyItemPattern
      { $pattern = $ki2.pattern; }
    |
      (tuplePatternStart) => 
      tuplePattern
      { $pattern = $tuplePattern.pattern; }
    | 
      variablePattern
      { $pattern = $variablePattern.pattern; }
    ;

tupleOrEntryPattern returns [Pattern pattern]
    : 
      (variable ENTRY_OP) =>
      ki1=keyItemPattern
      { $pattern = $ki1.pattern; }
    |
      (tuplePattern ENTRY_OP) =>
      ki2=keyItemPattern
      { $pattern = $ki2.pattern; }
    |
      tuplePattern
      { $pattern = $tuplePattern.pattern; }
    ;

variablePattern returns [VariablePattern pattern]
    : variable
      { $pattern = new VariablePattern(null);
        $pattern.setVariable($variable.variable); }
    ;

tuplePattern returns [TuplePattern pattern]
    : LBRACKET
      { $pattern = new TuplePattern($LBRACKET); }
      (
        v1=variadicPattern
        { $pattern.addPattern($v1.pattern); }
        (
          c1=COMMA
          { $pattern.setEndToken($c1); }
          (
            v2=variadicPattern
            { $pattern.addPattern($v2.pattern);
              $pattern.setEndToken(null); }
          )
        )*
      )?
      RBRACKET
      { $pattern.setEndToken($RBRACKET); }
    ;

variadicPattern returns [Pattern pattern]
    : (compilerAnnotations unionType? PRODUCT_OP) =>
      variadicVariable
      { VariablePattern vp = new VariablePattern(null);
        vp.setVariable($variadicVariable.variable); 
        $pattern = vp; }
	|
      p=pattern
      { $pattern = $p.pattern; }
    ;

variadicVariable returns [Variable variable]
    @init { $variable = new Variable(null); 
            Type t = new ValueModifier(null); }
    : compilerAnnotations
      { $variable.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      (
        unionType
        { t = $unionType.type; }
      )?
      (
        PRODUCT_OP
        { SequencedType st = new SequencedType($PRODUCT_OP);
          st.setType(t);
          st.setAtLeastOne(false);
          $variable.setType(st); }
      /*|
        SUM_OP
        { SequencedType st = new SequencedType($SUM_OP);
          st.setType(t);
          st.setAtLeastOne(true);
          $variable.setType(st); }*/
      )
      (
        memberNameDeclaration
        { $variable.setIdentifier($memberNameDeclaration.identifier); }
      )?
    ;

keyItemPattern returns [KeyValuePattern pattern]
    : v1=variableOrTuplePattern
      { $pattern = new KeyValuePattern(null);
        $pattern.setKey($v1.pattern); }
      ENTRY_OP
      { $pattern.setEndToken($ENTRY_OP); }
      (
        v2=variableOrTuplePattern
        { $pattern.setValue($v2.pattern); 
          $pattern.setEndToken(null); }
      )?
    ;

destructure returns [LetStatement statement]
    @init { Destructure d = null; }
    : VALUE_MODIFIER
      { $statement = new LetStatement($VALUE_MODIFIER);
        $statement.addUsageWarning(Warning.syntaxDeprecation,
            "use of 'value' for destructuring is deprecated (change to 'let' and add parentheses)"); }
      p=tupleOrEntryPattern
      { d = new Destructure(null);
        d.setPattern($p.pattern);
        $statement.addVariable(d); }
      (
        s=specifier
        { d.setSpecifierExpression($s.specifierExpression); }
        { expecting=SEMICOLON; }
      )?
      SEMICOLON
      { $statement.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

destructure2 returns [LetStatement statement]
    @init { Destructure d = null; }
    : LET
      { $statement = new LetStatement($LET); }
      LPAREN
      { $statement.setEndToken($LPAREN); }
      v1=letVariable
      { if ($v1.statement!=null) {
          $statement.addVariable($v1.statement); 
          $statement.setEndToken(null);
        } }
      (
        COMMA
        { $statement.setEndToken($COMMA); }
        v2=letVariable
        { if ($v2.statement!=null) {
            $statement.addVariable($v2.statement);
            $statement.setEndToken(null); 
          } }
      )*
      RPAREN
      { $statement.setEndToken($RPAREN); }
      SEMICOLON
      { $statement.setEndToken($SEMICOLON); }
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
      (
        memberNameDeclaration
        { dec.setIdentifier($memberNameDeclaration.identifier); 
          def.setIdentifier($memberNameDeclaration.identifier); }
      )?
      ( 
        (
          specifier
          { dec.setSpecifierOrInitializerExpression($specifier.specifierExpression); }
        | 
          lazySpecifier
          { dec.setSpecifierOrInitializerExpression($lazySpecifier.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); 
          expecting=-1; }
      | 
        { $declaration = def; }
        block
        { def.setBlock($block.block); }
      )
    ;

typedMethodOrAttributeDeclaration returns [TypedDeclaration declaration]
    @init { AttributeGetterDefinition adef=new AttributeGetterDefinition(null);
            AttributeDeclaration adec=new AttributeDeclaration(null);
            MethodDefinition mdef=new MethodDefinition(null);
            MethodDeclaration mdec=new MethodDeclaration(null); 
            $declaration = adec; }
    : ( variadicType
        { adef.setType($variadicType.type);
          adec.setType($variadicType.type); 
          mdef.setType($variadicType.type);
          mdec.setType($variadicType.type); }
      | DYNAMIC
        { DynamicModifier dm = new DynamicModifier($DYNAMIC);
          adef.setType(dm);
          adec.setType(dm); 
          mdef.setType(dm);
          mdec.setType(dm); }
      )
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
          b1=block
         { mdef.setBlock($b1.block); }
        | 
          (
            ms=functionSpecifier
           { mdec.setSpecifierExpression($ms.specifierExpression); }
          )?
          { expecting=SEMICOLON; }
          s1=SEMICOLON
          { $declaration.setEndToken($s1);
            expecting=-1; }
        )
      | 
        (
          as=specifier 
          { adec.setSpecifierOrInitializerExpression($as.specifierExpression); }
        | 
          ac=lazySpecifier 
          { adec.setSpecifierOrInitializerExpression($ac.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        s2=SEMICOLON
        { $declaration.setEndToken($s2); 
          expecting=-1; }
      | 
        { $declaration = adef; }
        b2=block
        { adef.setBlock($b2.block); }
      )
    ;

interfaceDeclaration returns [AnyInterface declaration]
    @init { InterfaceDefinition def=null; 
            InterfaceDeclaration dec=null; }
    : ( 
        INTERFACE_DEFINITION
        { def = new InterfaceDefinition($INTERFACE_DEFINITION); 
          dec = new InterfaceDeclaration($INTERFACE_DEFINITION);
          $declaration = dec; }
      | 
        DYNAMIC
        { def = new InterfaceDefinition($DYNAMIC);
          dec = new InterfaceDeclaration($DYNAMIC);
          def.setDynamic(true);
          $declaration = def; }
      )
      (
        typeNameDeclaration 
        { dec.setIdentifier($typeNameDeclaration.identifier); 
          def.setIdentifier($typeNameDeclaration.identifier); }
      )?
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
      /*(
        adaptedTypes
        { def.setAdaptedTypes($adaptedTypes.adaptedTypes); }
      )?*/
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
      | 
        (
          typeSpecifier
          { dec.setTypeSpecifier($typeSpecifier.typeSpecifier); }
        )? 
        { expecting=SEMICOLON; }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); 
          expecting=-1; }
      )
    ;

classDeclaration returns [AnyClass declaration]
    @init { ClassDefinition def=null; 
            ClassDeclaration dec=null; }
    : CLASS_DEFINITION 
      { def = new ClassDefinition($CLASS_DEFINITION); 
        dec = new ClassDeclaration($CLASS_DEFINITION);
        $declaration = dec; }
      (
        typeNameDeclaration
        { dec.setIdentifier($typeNameDeclaration.identifier); 
          def.setIdentifier($typeNameDeclaration.identifier); }
      )?
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
      | 
        (
          classSpecifier
          { dec.setClassSpecifier($classSpecifier.classSpecifier); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); 
          expecting=-1; }
      )
    ;

constructor returns [Constructor declaration]
    : NEW
      { $declaration = new Constructor($NEW); }
      (
        memberNameDeclaration
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
      (
        parameters
        { $declaration.setParameterList($parameters.parameterList); }
      )?
      (
        dc=delegatedConstructor
        { $declaration.setDelegatedConstructor($dc.delegatedConstructor); }
      )?
      (
        block
        { $declaration.setBlock($block.block); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LBRACE, input)); }
        SEMICOLON
        { $declaration.setEndToken($SEMICOLON); }
      )
    ;

delegatedConstructor returns [DelegatedConstructor delegatedConstructor]
    : EXTENDS
      { $delegatedConstructor = new DelegatedConstructor($EXTENDS); }
      (
        ci=classInstantiation
        { $delegatedConstructor.setType($ci.type);
          $delegatedConstructor.setInvocationExpression($ci.invocationExpression); }
      )?
    ;

aliasDeclaration returns [TypeAliasDeclaration declaration]
    : ALIAS
      { $declaration = new TypeAliasDeclaration($ALIAS);}
      (
        typeNameDeclaration 
        { $declaration.setIdentifier($typeNameDeclaration.identifier); }
      )?
      (
        typeParameters 
        { $declaration.setTypeParameterList($typeParameters.typeParameterList); }
      )?
      (
        typeConstraints
        { $declaration.setTypeConstraintList($typeConstraints.typeConstraintList); }
      )?
      (
        typeSpecifier
        { $declaration.setTypeSpecifier($typeSpecifier.typeSpecifier); }
      )?
      { expecting=SEMICOLON; }
      SEMICOLON
      { $declaration.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

assertion returns [Assertion assertion]
    : assertMessage
      ASSERT
      { $assertion = new Assertion($ASSERT); 
        $assertion.setAnnotationList($assertMessage.annotationList); }
      conditions
      { $assertion.setConditionList($conditions.conditionList); }
      { expecting=SEMICOLON; }
      SEMICOLON
      { $assertion.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

block returns [Block block]
    @init { ImportList importList = new ImportList(null); } 
    : LBRACE 
      { $block = new Block($LBRACE); }
      (
        importDeclaration 
        { importList.addImport($importDeclaration.importDeclaration); 
          $block.setImportList(importList); }
      |
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $block.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $block.setEndToken($RBRACE); }
    ;

//Note: interface bodies can't really contain 
//      statements, but error recovery works
//      much better if we validate that later
//      on, instead of doing it in the parser.
interfaceBody returns [InterfaceBody interfaceBody]
    @init { ImportList importList = new ImportList(null); } 
    : LBRACE 
      { $interfaceBody = new InterfaceBody($LBRACE); }
      (
        importDeclaration 
        { importList.addImport($importDeclaration.importDeclaration); 
          $interfaceBody.setImportList(importList); }
      |
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $interfaceBody.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $interfaceBody.setEndToken($RBRACE); }
    ;

classBody returns [ClassBody classBody]
    @init { ImportList importList = new ImportList(null); } 
    : LBRACE
      { $classBody = new ClassBody($LBRACE); }
      (
        importDeclaration 
        { importList.addImport($importDeclaration.importDeclaration); 
          $classBody.setImportList(importList); }
      |
        declarationOrStatement
        { if ($declarationOrStatement.statement!=null)
              $classBody.addStatement($declarationOrStatement.statement); }
      )*
      RBRACE
      { $classBody.setEndToken($RBRACE); }
    ;

extendedType returns [ExtendedType extendedType]
    : EXTENDS
      { $extendedType = new ExtendedType($EXTENDS); }
      (  
        ci=classInstantiation
        { $extendedType.setType($ci.type);
          $extendedType.setInvocationExpression($ci.invocationExpression); }
      )?
    ;

classSpecifier returns [ClassSpecifier classSpecifier]
    : (
        COMPUTE 
        { $classSpecifier = new ClassSpecifier($COMPUTE); }
      |
        SPECIFY 
        { $classSpecifier = new ClassSpecifier($SPECIFY); }
      )
      (
        ci=classInstantiation
        { $classSpecifier.setType($ci.type);
          $classSpecifier.setInvocationExpression($ci.invocationExpression); }
      )?
    ;

packageQualifiedClass returns [SimpleType type, ExtendedTypeExpression expression]
    @init { BaseType bt = null;
            QualifiedType qt = null; }
    : PACKAGE
      { bt = new BaseType($PACKAGE);
        bt.setPackageQualified(true);
        $type=bt; }
      (
        m1=MEMBER_OP
        { bt.setEndToken($m1); }
        (
          t1=typeNameWithArguments
          { if ($t1.identifier!=null) {
              bt.setEndToken(null);
              bt.setIdentifier($t1.identifier);
            }
            if ($t1.typeArgumentList!=null)
                bt.setTypeArgumentList($t1.typeArgumentList);
            $expression = new ExtendedTypeExpression(null);
            $expression.setType($type); }
          ( //constructor
            m2=MEMBER_OP
            { qt = new QualifiedType($m2);
              qt.setOuterType($type);
              $type=qt; }
            t2=memberNameWithArguments
            { if ($t2.identifier!=null) {
                qt.setIdentifier($t2.identifier);
              }
              if ($t2.typeArgumentList!=null)
                qt.setTypeArgumentList($t2.typeArgumentList);
              $expression = new ExtendedTypeExpression(null);
              $expression.setType($type); }
          )?
        )?
      )?
   ;

unqualifiedClass returns [SimpleType type, ExtendedTypeExpression expression]
    @init { BaseType bt = null;
            QualifiedType qt = null; }
    : t0=typeNameWithArguments
      { bt = new BaseType(null);
        bt.setIdentifier($t0.identifier);
        if ($t0.typeArgumentList!=null)
            bt.setTypeArgumentList($t0.typeArgumentList);
        $type=bt; 
        $expression = new ExtendedTypeExpression(null);
        $expression.setType($type); }
      ( //constructor:
        m3=MEMBER_OP
        { qt = new QualifiedType($m3);
          qt.setOuterType($type);
          $type=qt; }
        (
          t3=memberNameWithArguments
          { if ($t3.identifier!=null) {
              qt.setIdentifier($t3.identifier);
            }
            if ($t3.typeArgumentList!=null)
                qt.setTypeArgumentList($t3.typeArgumentList);
            $expression = new ExtendedTypeExpression(null);
            $expression.setType($type); }
        |
          (
            t5=typeNameWithArguments
            { if ($t5.identifier!=null) {
              qt.setEndToken(null);
              qt.setIdentifier($t5.identifier); }
            if ($t5.typeArgumentList!=null)
                bt.setTypeArgumentList($t5.typeArgumentList);
            $expression = new ExtendedTypeExpression(null);
            $expression.setType($type); }
          )
          (
            m4=MEMBER_OP
            { qt = new QualifiedType(null);
              qt.setOuterType($type);
              qt.setEndToken($m4);
              $type=qt; }
            (
              t6=typeNameWithArguments
              { if ($t6.identifier!=null) {
                qt.setEndToken(null);
                qt.setIdentifier($t6.identifier); }
              if ($t6.typeArgumentList!=null)
                  bt.setTypeArgumentList($t6.typeArgumentList);
              $expression = new ExtendedTypeExpression(null);
              $expression.setType($type); }
            )?
          )*
        )?
      )?
    | t4=memberNameWithArguments
      { bt = new BaseType(null);
        bt.setIdentifier($t4.identifier);
        if ($t4.typeArgumentList!=null)
            bt.setTypeArgumentList($t4.typeArgumentList);
        $type=bt; 
        $expression = new ExtendedTypeExpression(null);
        $expression.setType($type); }
    ;

superQualifiedClass returns [SimpleType type, ExtendedTypeExpression expression]
    @init { QualifiedType qt = null; }
    : SUPER 
      { SuperType st = new SuperType($SUPER); 
        qt = new QualifiedType(null); 
        qt.setOuterType(st); 
        $type=qt; }
      m4=MEMBER_OP
      { qt.setEndToken($m4); }
      (
        t4=typeNameWithArguments 
        { if ($t4.identifier!=null) {
            qt.setEndToken(null);
            qt.setIdentifier($t4.identifier);
          }
          if ($t4.typeArgumentList!=null)
            qt.setTypeArgumentList($t4.typeArgumentList);
          $expression = new ExtendedTypeExpression(null);
          $expression.setType($type); }
      |
        t5=memberNameWithArguments 
        { if ($t5.identifier!=null) {
            qt.setEndToken(null);
            qt.setIdentifier($t5.identifier);
          }
          if ($t5.typeArgumentList!=null)
            qt.setTypeArgumentList($t5.typeArgumentList);
          $expression = new ExtendedTypeExpression(null);
          $expression.setType($type); }
      )?
	  ;

classInstantiation returns [SimpleType type, InvocationExpression invocationExpression]
    @init { ExtendedTypeExpression ete = null; }
    : (
	      pq=packageQualifiedClass
	      { $type=$pq.type; ete=$pq.expression; }
	    | 
	      uq=unqualifiedClass
	      { $type=$uq.type; ete=$uq.expression; }
	    | 
	      sq=superQualifiedClass
	      { $type=$sq.type; ete=$sq.expression; }
	    )
      (
        pa=positionalArguments
        { $invocationExpression = new InvocationExpression(null);
          $invocationExpression.setPrimary(ete);
          $invocationExpression.setPositionalArgumentList($pa.positionalArgumentList); }
        /*|
        na=namedArguments
        { $invocationExpression = new InvocationExpression(null);
          $invocationExpression.setPrimary(ete);
          $invocationExpression.setNamedArgumentList($na.namedArgumentList); }*/
      )?
    ;

satisfiedTypes returns [SatisfiedTypes satisfiedTypes]
    : SATISFIES 
      { $satisfiedTypes = new SatisfiedTypes($SATISFIES); }
      ( 
        t1=primaryType 
        { if ($t1.type!=null) $satisfiedTypes.addType($t1.type); }
      )
      (
        ( 
          i=INTERSECTION_OP
          { $satisfiedTypes.setEndToken($i); }
        | 
          (COMMA|UNION_OP)
          { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(INTERSECTION_OP, input)); }
        )
        (
          t2=primaryType
          { if ($t2.type!=null) {
                $satisfiedTypes.addType($t2.type); 
                $satisfiedTypes.setEndToken(null); } }
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
        ( 
          u=UNION_OP 
          { $caseTypes.setEndToken($u); }
        | 
          (COMMA|INTERSECTION_OP)
          { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(UNION_OP, input)); }
        )
        (
          ct2=caseType
          { if ($ct2.type!=null) $caseTypes.addType($ct2.type); 
            if ($ct2.instance!=null) $caseTypes.addBaseMemberExpression($ct2.instance); 
            if ($ct2.type!=null||$ct2.instance!=null) $caseTypes.setEndToken(null); }
        )
      )*
    ;

caseType returns [StaticType type, StaticMemberOrTypeExpression instance]
    : t=primaryType 
      { $type=$t.type;}
    | m1=memberName
      { BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($m1.identifier);
        bme.setTypeArguments(new InferredTypeArguments(null)); 
        $instance = bme; }
    | PACKAGE MEMBER_OP m2=memberName
      { Package p = new Package($PACKAGE);
        p.setQualifier(true);
        QualifiedMemberExpression qme = new QualifiedMemberExpression(null);
        qme.setPrimary(p);
        qme.setMemberOperator(new MemberOp($MEMBER_OP));
        qme.setIdentifier($m2.identifier);
        qme.setTypeArguments(new InferredTypeArguments(null)); 
        $instance = qme; }
    ;

abstractedType returns [AbstractedType abstractedType]
    : ABSTRACTED_TYPE
      { $abstractedType = new AbstractedType($ABSTRACTED_TYPE); }
      primaryType
      { $abstractedType.setType($primaryType.type); }
    ;

parameters returns [ParameterList parameterList]
    : LPAREN
      { $parameterList=new ParameterList($LPAREN); }
      (
        ap1=parameterDeclarationOrRefOrPattern 
        { if ($ap1.parameter!=null)
              $parameterList.addParameter($ap1.parameter); }
        (
          c=COMMA
          { $parameterList.setEndToken($c); }
          (
            ap2=parameterDeclarationOrRefOrPattern
            { if ($ap2.parameter!=null) {
                  $parameterList.addParameter($ap2.parameter); 
                  $parameterList.setEndToken(null); } }
          |
            { displayRecognitionError(getTokenNames(),
                new MismatchedTokenException(UIDENTIFIER, input)); }
          )
        )*
      )?
      RPAREN
      { $parameterList.setEndToken($RPAREN); }
    ;

parameter returns [ParameterDeclaration parameter]
    : compilerAnnotations
      annotations
      parameterDeclaration
      { TypedDeclaration d = $parameterDeclaration.declaration;
        d.getCompilerAnnotations().addAll($compilerAnnotations.annotations);
        d.setAnnotationList($annotations.annotationList);
        if (d instanceof AttributeDeclaration) {
            ValueParameterDeclaration vp = new ValueParameterDeclaration(null);
            vp.setTypedDeclaration(d);
            $parameter = vp;
        }
        else if (d instanceof MethodDeclaration) {
            FunctionalParameterDeclaration fp = new FunctionalParameterDeclaration(null);
            fp.setTypedDeclaration(d);
            $parameter = fp;
        }
      }
    ;
    
parameterDeclaration returns [TypedDeclaration declaration]
    @init { AttributeDeclaration a = new AttributeDeclaration(null); 
            MethodDeclaration m = new MethodDeclaration(null);
            $declaration = a; }
    : ( 
        variadicType
        { a.setType($variadicType.type);
          m.setType($variadicType.type); }
      | VOID_MODIFIER
        { m.setType(new VoidModifier($VOID_MODIFIER));
          $declaration=m; }
      | FUNCTION_MODIFIER
        { m.setType(new FunctionModifier($FUNCTION_MODIFIER));
          $declaration=m; }
      | DYNAMIC
        { a.setType(new DynamicModifier($DYNAMIC));
          m.setType(new DynamicModifier($DYNAMIC)); }
      | VALUE_MODIFIER
        { a.setType(new ValueModifier($VALUE_MODIFIER)); }
      )
      memberNameDeclaration
      { a.setIdentifier($memberNameDeclaration.identifier);
        m.setIdentifier($memberNameDeclaration.identifier); }
      (
        (
          specifier
          { a.setSpecifierOrInitializerExpression($specifier.specifierExpression); }
        )?
      |
        (
          typeParameters
          { m.setTypeParameterList($typeParameters.typeParameterList); }
        )?
        (
          parameters
          { m.addParameterList($parameters.parameterList);
            $declaration=m; }
        )+
        (
          functionSpecifier
          { m.setSpecifierExpression($functionSpecifier.specifierExpression); }
        )?
      )
    ;

parameterRef returns [InitializerParameter parameter]
    : memberName
      { $parameter = new InitializerParameter(null);
        $parameter.setIdentifier($memberName.identifier); }
      (
        specifier
        { $parameter.setSpecifierExpression($specifier.specifierExpression); }
      )?
    ;

parameterDeclarationOrRefOrPattern returns [Parameter parameter]
    : (patternStart) => pattern
      { PatternParameter pp = new PatternParameter(null);
        pp.setPattern($pattern.pattern);
        $parameter = pp; }
    | parameterDeclarationOrRef
      { $parameter = $parameterDeclarationOrRef.parameter; }
    ;

parameterDeclarationOrRef returns [Parameter parameter]
    :
      parameter
      { $parameter = $parameter.parameter; }
    | 
      parameterRef
      { $parameter = $parameterRef.parameter; }
    ;

typeParameters returns [TypeParameterList typeParameterList]
    : SMALLER_OP
      { $typeParameterList = new TypeParameterList($SMALLER_OP); }
      tp1=typeParameter
      { if ($tp1.typeParameter instanceof TypeParameterDeclaration)
            $typeParameterList.addTypeParameterDeclaration($tp1.typeParameter); }
      (
        c=COMMA
        { $typeParameterList.setEndToken($c); }
        (
          tp2=typeParameter
          { if ($tp2.typeParameter instanceof TypeParameterDeclaration)
                $typeParameterList.addTypeParameterDeclaration($tp2.typeParameter);
            $typeParameterList.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(UIDENTIFIER, input)); }
        )
      )*
      LARGER_OP
      { $typeParameterList.setEndToken($LARGER_OP); }
    ;

typeParameter returns [TypeParameterDeclaration typeParameter]
    : { $typeParameter = new TypeParameterDeclaration(null); }
      compilerAnnotations
      ( 
        variance 
        { $typeParameter.setTypeVariance($variance.typeVariance); } 
      )?
      typeNameDeclaration
      { $typeParameter.setIdentifier($typeNameDeclaration.identifier); }
      (
        typeDefault
        { $typeParameter.setTypeSpecifier($typeDefault.typeSpecifier); }
      )?
      { $typeParameter.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

variance returns [TypeVariance typeVariance]
    : IN_OP
      { $typeVariance = new TypeVariance($IN_OP); }
    | OUT
      { $typeVariance = new TypeVariance($OUT); }
    ;
    
typeConstraint returns [TypeConstraint typeConstraint]
    : compilerAnnotations
      TYPE_CONSTRAINT
      { $typeConstraint = new TypeConstraint($TYPE_CONSTRAINT); 
        $typeConstraint.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
      (
        typeNameDeclaration 
        { $typeConstraint.setIdentifier($typeNameDeclaration.identifier); }
      )?
      (
        typeParameters
         { $typeConstraint.setTypeParameterList($typeParameters.typeParameterList); }
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

anonymousTypeConstraint returns [TypeConstraint typeConstraint]
    : TYPE_CONSTRAINT
      { $typeConstraint = new TypeConstraint($TYPE_CONSTRAINT); }
      typeNameDeclaration 
      { $typeConstraint.setIdentifier($typeNameDeclaration.identifier); }
      (
        caseTypes
        { $typeConstraint.setCaseTypes($caseTypes.caseTypes); }
      )?
      (
        satisfiedTypes
        { $typeConstraint.setSatisfiedTypes($satisfiedTypes.satisfiedTypes); }
      )?
    ;

typeConstraints returns [TypeConstraintList typeConstraintList]
    : { $typeConstraintList=new TypeConstraintList(null); }
      (
        typeConstraint
        { if ($typeConstraint.typeConstraint!=null)
            $typeConstraintList.addTypeConstraint($typeConstraint.typeConstraint); }
      )+
    ;

anonymousTypeConstraints returns [TypeConstraintList typeConstraintList]
    : { $typeConstraintList=new TypeConstraintList(null); }
      (
        anonymousTypeConstraint
        { if ($anonymousTypeConstraint.typeConstraint!=null)
            $typeConstraintList.addTypeConstraint($anonymousTypeConstraint.typeConstraint); }
      )+
    ;

destructureStart
    : VALUE_MODIFIER compilerAnnotations 
      (LBRACKET|UIDENTIFIER|VOID_MODIFIER|VALUE_MODIFIER|FUNCTION_MODIFIER|LIDENTIFIER ENTRY_OP)
    ;

declarationOrStatement returns [Statement statement]
    options {memoize=true;}
    : compilerAnnotations
      (
        (destructureStart) => destructure
        { $statement=$destructure.statement; }
      | destructure2
        { $statement=$destructure2.statement; }
      | (annotatedAssertionStart) => assertion
        { $statement = $assertion.assertion; }
      | (annotatedDeclarationStart) => declaration
        { $statement = $declaration.declaration; }
      | s=statement
        { $statement=$s.statement; }
      )
      { if ($statement!=null)
            $statement.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
    ;

declaration returns [Declaration declaration]
    @init { MissingDeclaration md = new MissingDeclaration(null); 
            $declaration = md; }
    : annotations
      { md.setAnnotationList($annotations.annotationList); }
    ( 
      classDeclaration
      { $declaration=$classDeclaration.declaration; }
    | (INTERFACE_DEFINITION|DYNAMIC UIDENTIFIER) => interfaceDeclaration
      { $declaration=$interfaceDeclaration.declaration; }
    | aliasDeclaration
      { $declaration=$aliasDeclaration.declaration; }
    | objectDeclaration
      { $declaration=$objectDeclaration.declaration; }
    | setterDeclaration
      { $declaration=$setterDeclaration.declaration; }
    | voidOrInferredMethodDeclaration
      { $declaration=$voidOrInferredMethodDeclaration.declaration; }
    | inferredAttributeDeclaration
      { $declaration=$inferredAttributeDeclaration.declaration; }
    | typedMethodOrAttributeDeclaration
      { $declaration=$typedMethodOrAttributeDeclaration.declaration; }
    | (NEW (LIDENTIFIER|UIDENTIFIER)? LPAREN) => 
      constructor
      { $declaration=$constructor.declaration; }
    | enumeratedObject
      { $declaration=$enumeratedObject.declaration; }
    /*| { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(CLASS_DEFINITION, input)); }
      SEMICOLON
      { $declaration=new BrokenDeclaration($SEMICOLON); }*/
    )
    { if ($declaration!=null)
          $declaration.setAnnotationList($annotations.annotationList); }
    ;

annotatedDeclarationStart
    : 
      (stringLiteral | annotation) 
      (LIDENTIFIER | (UIDENTIFIER) => UIDENTIFIER | (unambiguousType) => unambiguousType | declarationStart)
    |
      (unambiguousType) => unambiguousType 
    | 
      declarationStart
    ;

annotatedAssertionStart
    : stringExpression? ASSERT
    ;

//special rule for syntactic predicates
//that distinguish declarations from
//expressions
declarationStart
    : VALUE_MODIFIER
    | FUNCTION_MODIFIER (LIDENTIFIER|UIDENTIFIER) //to disambiguate anon functions
    | VOID_MODIFIER (LIDENTIFIER|UIDENTIFIER) //to disambiguate anon functions
    | ASSIGN
    | INTERFACE_DEFINITION
    | CLASS_DEFINITION
    | OBJECT_DEFINITION (LIDENTIFIER|UIDENTIFIER) //to disambiguate object expressions
    | NEW
    | ALIAS 
    | variadicType LIDENTIFIER
    | DYNAMIC (LIDENTIFIER|UIDENTIFIER)
    ;
    
// recognize some common patterns that are unambiguously
// type abbreviations - these are not necessary, but 
// help the IDE
fullQualifiedType
    : baseType (MEMBER_OP typeNameWithArguments)*
    ;
unambiguousType
    : fullQualifiedType 
      (
        (OPTIONAL | LBRACKET RBRACKET)? 
        ENTRY_OP fullQualifiedType
      )?
      (OPTIONAL | LBRACKET RBRACKET)
    | LBRACE 
      fullQualifiedType (OPTIONAL | LBRACKET RBRACKET)?
      (
        ENTRY_OP fullQualifiedType 
        (OPTIONAL | LBRACKET RBRACKET)?
      )? 
      (PRODUCT_OP|SUM_OP) 
      RBRACE
    | LBRACKET 
      fullQualifiedType (OPTIONAL | LBRACKET RBRACKET)? 
      (
        ENTRY_OP fullQualifiedType
        (OPTIONAL | LBRACKET RBRACKET)?
      )? 
      (
        COMMA 
        fullQualifiedType (OPTIONAL | LBRACKET RBRACKET)? 
        (
          ENTRY_OP fullQualifiedType 
          (OPTIONAL | LBRACKET RBRACKET)?
        )?
      )* 
      (PRODUCT_OP|SUM_OP) 
      RBRACKET
    ;

statement returns [Statement statement]
    : directiveStatement
      { $statement = $directiveStatement.directive; }
    | controlStatement
      { $statement = $controlStatement.controlStatement; }
    | expressionOrSpecificationStatement
      { $statement = $expressionOrSpecificationStatement.statement; }
    | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(RBRACE, input)); }
      SEMICOLON
    ;

expressionOrSpecificationStatement returns [Statement statement]
    @init { SpecifierStatement ss=new SpecifierStatement(null); 
            ExpressionStatement es=new ExpressionStatement(null); }
    : expression
      { $statement = es;
        if ($expression.expression!=null)
            es.setExpression($expression.expression);
        if ($expression.expression.getTerm() instanceof AssignOp) {
            AssignOp a = (AssignOp) $expression.expression.getTerm();
            Term lt = a.getLeftTerm();
            if (lt instanceof BaseMemberExpression ||
                lt instanceof ParameterizedExpression ||
                lt instanceof QualifiedMemberExpression &&
                    ((QualifiedMemberExpression) lt).getPrimary() instanceof This &&
                    ((QualifiedMemberExpression) lt).getMemberOperator() instanceof MemberOp) {
                Expression e = new Expression(null);
                e.setTerm(a.getRightTerm());
                SpecifierExpression se = new SpecifierExpression(a.getMainToken());
                se.setExpression(e);
                ss.setSpecifierExpression(se);
                ss.setBaseMemberExpression(a.getLeftTerm());
                $statement = ss;
            }
        }
      }
      (
        /*specifier
        { ss.setSpecifierExpression($specifier.specifierExpression);
          ss.setBaseMemberExpression($expression.expression.getTerm());
          $statement = ss; }
      | */
        lazySpecifier
        { ss.setSpecifierExpression($lazySpecifier.specifierExpression);
          ss.setBaseMemberExpression($expression.expression.getTerm()); 
          $statement = ss; }
      )?
      { expecting=SEMICOLON; }
      (
        SEMICOLON
        { $statement.setEndToken($SEMICOLON); }
      | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(SEMICOLON, input)); }
        COMMA
        { $statement.setEndToken($COMMA); }
      )
      { expecting=-1; }
    ;

directiveStatement returns [Directive directive]
    : d=directive 
      { $directive=$d.directive;
        expecting=SEMICOLON; }
      SEMICOLON
      { $directive.setEndToken($SEMICOLON);
        expecting=-1; }
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
    : (
        COMPUTE 
        { $typeSpecifier = new TypeSpecifier($COMPUTE); }
      |
        SPECIFY 
        { $typeSpecifier = new TypeSpecifier($SPECIFY); }
      )
      (
        type
        { $typeSpecifier.setType($type.type); }
      )?
    ;

typeDefault returns [TypeSpecifier typeSpecifier]
    : SPECIFY 
      { $typeSpecifier = new DefaultTypeArgument($SPECIFY); }
      type
      { $typeSpecifier.setType($type.type); }
    ;

specifier returns [SpecifierExpression specifierExpression]
    : SPECIFY
      { $specifierExpression = new SpecifierExpression($SPECIFY); }
      functionOrExpression
      { $specifierExpression.setExpression($functionOrExpression.expression); }
    ;

lazySpecifier returns [SpecifierExpression specifierExpression]
    : COMPUTE
      { $specifierExpression = new LazySpecifierExpression($COMPUTE); }
      functionOrExpression
      { $specifierExpression.setExpression($functionOrExpression.expression); }
    ;

functionSpecifier returns [SpecifierExpression specifierExpression]
    : (
        COMPUTE
        { $specifierExpression = new LazySpecifierExpression($COMPUTE); }
      |
        SPECIFY
        { $specifierExpression = new LazySpecifierExpression($SPECIFY); }
      )
      functionOrExpression
      { $specifierExpression.setExpression($functionOrExpression.expression); }
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
    | metaLiteral
      { $primary=$metaLiteral.meta; }
    | enumeration
      { $primary=$enumeration.sequenceEnumeration; }
    | tuple
      { $primary=$tuple.tuple; }
    | dynamicObject
      { $primary=$dynamicObject.dynamic; }
    | objectExpression
      { $primary = $objectExpression.objectExpression; }
    | parExpression
      { $primary=$parExpression.expression; }
    | baseReferenceOrParameterized
      { $primary=$baseReferenceOrParameterized.primary; }
    ;

baseReferenceOrParameterized returns [Primary primary]
    @init { BaseMemberOrTypeExpression be=null;
            QualifiedMemberOrTypeExpression qe=null;
            ParameterizedExpression pe=null; }
    : (LIDENTIFIER typeParameters? specifierParametersStart) => 
      memberName
      { be = new BaseMemberExpression(null);
        be.setTypeArguments(new InferredTypeArguments(null)); //yew!!
        be.setIdentifier($memberName.identifier); 
        pe = new ParameterizedExpression(null);
        pe.setPrimary(be); }
      (
        typeParameters
        { pe.setTypeParameterList($typeParameters.typeParameterList); }
      )?
      (
        (specifierParametersStart) => parameters
        { pe.addParameterList($parameters.parameterList);
          $primary = pe; }
      )+
    | baseReference
      { if ($baseReference.isMember)
            be = new BaseMemberExpression(null);
        else
            be = new BaseTypeExpression(null);
        be.setIdentifier($baseReference.identifier);
        if ($baseReference.typeArgumentList!=null)
            be.setTypeArguments($baseReference.typeArgumentList);
        else
            be.setTypeArguments(new InferredTypeArguments(null));
        $primary=be; }
    | selfReference
      { $primary=$selfReference.atom; }
      (
        (MEMBER_OP LIDENTIFIER typeParameters? specifierParametersStart) => 
        memberSelectionOperator
        { qe = new QualifiedMemberExpression(null); 
          qe.setMemberOperator($memberSelectionOperator.operator);
          qe.setTypeArguments(new InferredTypeArguments(null)); }
        memberName
        { qe.setIdentifier($memberName.identifier); 
          qe.setPrimary($primary);
          pe = new ParameterizedExpression(null);
          pe.setPrimary(qe); }
        (
          typeParameters
          { pe.setTypeParameterList($typeParameters.typeParameterList); }
        )?
        (
          (specifierParametersStart) => parameters
          { pe.addParameterList($parameters.parameterList);
            $primary = pe; }
        )+
      )?
    ;

baseReference returns [Identifier identifier, 
                       TypeArgumentList typeArgumentList, 
                       boolean isMember]
    : memberReference
      { $identifier = $memberReference.identifier;
        $typeArgumentList = $memberReference.typeArgumentList;
        $isMember = true; }
    | typeReference
      { $identifier = $typeReference.identifier;
        $typeArgumentList = $typeReference.typeArgumentList;
        $isMember = false; }
    ;

primary returns [Primary primary]
    : base
      { $primary=$base.primary; }
      (
        qualifiedReference
        { QualifiedMemberOrTypeExpression qe;
          if ($qualifiedReference.isMember)
              qe = new QualifiedMemberExpression(null);
          else
              qe = new QualifiedTypeExpression(null);
          qe.setPrimary($primary);
          qe.setMemberOperator($qualifiedReference.operator);
          qe.setIdentifier($qualifiedReference.identifier);
          if ($qualifiedReference.typeArgumentList!=null)
              qe.setTypeArguments($qualifiedReference.typeArgumentList);
          else 
              qe.setTypeArguments( new InferredTypeArguments(null) );
          $primary=qe; }
        | indexOrIndexRange 
          { $indexOrIndexRange.indexExpression.setPrimary($primary);
            $primary = $indexOrIndexRange.indexExpression; }
        | positionalArguments 
          { InvocationExpression ie = new InvocationExpression(null);
            ie.setPrimary($primary);
            ie.setPositionalArgumentList($positionalArguments.positionalArgumentList); 
            $primary=ie; }
        | namedArguments
          { InvocationExpression ie = new InvocationExpression(null);
            ie.setPrimary($primary);
            ie.setNamedArgumentList($namedArguments.namedArgumentList);
            $primary=ie; }
      )*
    ;

parameterStart
    : VOID_MODIFIER LIDENTIFIER
    | variadicType LIDENTIFIER
    | DYNAMIC LIDENTIFIER
    ;
    
annotatedParameterStart
    : 
      (stringLiteral | annotation) 
      (LIDENTIFIER | (UIDENTIFIER) => UIDENTIFIER | (unambiguousType) => unambiguousType | parameterStart)
    |
      (unambiguousType) => unambiguousType 
    | 
      parameterStart
    ;

specifierParametersStart
    : LPAREN 
      ( 
        compilerAnnotations annotatedParameterStart
      | RPAREN (SPECIFY | COMPUTE | specifierParametersStart)
      )
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

memberSelectionOperator returns [MemberOperator operator]
    : MEMBER_OP
      { $operator=new MemberOp($MEMBER_OP); }
    | SAFE_MEMBER_OP
      { $operator=new SafeMemberOp($SAFE_MEMBER_OP); }
    | SPREAD_OP
      { $operator=new SpreadOp($SPREAD_OP); }
    ;

statementStart
    : annotation* 
    ( 
        VALUE_MODIFIER | FUNCTION_MODIFIER 
      | variadicType LIDENTIFIER
      | OBJECT_DEFINITION LIDENTIFIER 
      | CLASS_DEFINITION | INTERFACE_DEFINITION
      | ASSERT
    )
    | RETURN | THROW | BREAK | CONTINUE
    ; 

enumeration returns [SequenceEnumeration sequenceEnumeration]
    : LBRACE 
      { $sequenceEnumeration = new SequenceEnumeration($LBRACE); } 
      (
        (statementStart) => declarationOrStatement
        { $sequenceEnumeration.addStatement($declarationOrStatement.statement); }
      )*
      (
        sequencedArgument
        { $sequenceEnumeration.setSequencedArgument($sequencedArgument.sequencedArgument); }
      )?
      RBRACE
      { $sequenceEnumeration.setEndToken($RBRACE); }
    ;

tuple returns [Tuple tuple]
    : LBRACKET 
      { $tuple = new Tuple($LBRACKET); }
      (
        sequencedArgument
        { $tuple.setSequencedArgument($sequencedArgument.sequencedArgument); }
      )?
      RBRACKET
      { $tuple.setEndToken($RBRACKET); }
    ;
    
dynamicObject returns [Dynamic dynamic]
    : DYNAMIC
      { $dynamic = new Dynamic($DYNAMIC); }
      (
        dynamicArguments
        { $dynamic.setNamedArgumentList($dynamicArguments.namedArgumentList); }
      | 
        LBRACKET COMMA RBRACKET
        { $dynamic.setEndToken($RBRACKET); }
      )
    ;

dynamicArguments returns [NamedArgumentList namedArgumentList]
    : LBRACKET
      { $namedArgumentList = new NamedArgumentList($LBRACKET); }
      ( //TODO: get rid of the predicate and use the approach
        //      in expressionOrSpecificationStatement
        (namedArgumentStart) 
        => namedArgument
        { if ($namedArgument.namedArgument!=null) 
              $namedArgumentList.addNamedArgument($namedArgument.namedArgument); }
      | (anonymousArgument)
        => anonymousArgument
        { if ($anonymousArgument.namedArgument!=null) 
              $namedArgumentList.addNamedArgument($anonymousArgument.namedArgument); }
      )*
      ( 
        sequencedArgument
        { $namedArgumentList.setSequencedArgument($sequencedArgument.sequencedArgument); }
      )?
      RBRACKET
      { $namedArgumentList.setEndToken($RBRACKET); }
    ;

valueCaseList returns [ExpressionList expressionList]
    : { $expressionList = new ExpressionList(null); }
      ie1=intersectionExpression 
      { Expression e = new Expression(null);
        e.setTerm($ie1.term);
        $expressionList.addExpression(e); }
      ( 
        (
          c=COMMA 
          { $expressionList.setEndToken($c);
            if ($expressionList.getErrors().isEmpty())
                $expressionList.addUsageWarning(Warning.syntaxDeprecation,
                    "use of ',' in case conditions is deprecated (change to '|')"); }
        | u=UNION_OP
          { $expressionList.setEndToken($u); }
        )
        (
          ie2=intersectionExpression
          { if ($ie2.term!=null) {
                Expression e = new Expression(null);
                e.setTerm($ie2.term);
                $expressionList.addExpression(e);
                $expressionList.setEndToken(null); } }
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
      variance? //not really legal here
      type
      (LARGER_OP|SMALLER_OP|COMMA)
    |
      SMALLER_OP
    | 
      LARGER_OP //for IDE only!
    )
    ;

indexOrIndexRange returns [IndexExpression indexExpression]
    //TODO: move indexOperator to ElementOrRange and
    //      make this rule return ElementOrRange instead
    //      of IndexExpression, instantiating IndexExpression
    //      from the calling primary rule
    : LBRACKET
      { $indexExpression = new IndexExpression($LBRACKET); }
      (
        e1=ELLIPSIS
        { $indexExpression.setEndToken($e1); }
        i=index
        { ElementRange er0 = new ElementRange(null);
          er0.setUpperBound($i.expression);
          $indexExpression.setElementOrRange(er0); 
          $indexExpression.setEndToken(null); }
      | (index (ELLIPSIS|RANGE_OP|SEGMENT_OP))=>
        l=index
        { Element e = new Element(null);
          e.setExpression($l.expression); 
          $indexExpression.setElementOrRange(e); }
        (
          e2=ELLIPSIS
          { $indexExpression.setEndToken($e2);
            ElementRange er1 = new ElementRange(null);
            er1.setLowerBound($l.expression);
            $indexExpression.setElementOrRange(er1); }
        | 
          RANGE_OP 
          { $indexExpression.setEndToken($RANGE_OP); }
          u=index 
          { ElementRange er2 = new ElementRange(null);
            er2.setLowerBound($l.expression); 
            er2.setUpperBound($u.expression); 
            $indexExpression.setElementOrRange(er2);
            $indexExpression.setEndToken(null); }
        | SEGMENT_OP
          { $indexExpression.setEndToken($SEGMENT_OP); }
          s=index 
          { ElementRange er3 = new ElementRange(null);
            er3.setLowerBound($l.expression); 
            er3.setLength($s.expression); 
            $indexExpression.setElementOrRange(er3);
            $indexExpression.setEndToken(null); }
        )?
      | fe=functionOrExpression
        { Element e = new Element(null);
          e.setExpression($fe.expression); 
          $indexExpression.setElementOrRange(e); }
      )
      RBRACKET
      { $indexExpression.setEndToken($RBRACKET); }
    ;

index returns [Expression expression]
    : additiveExpression 
      { $expression = new Expression(null);
        $expression.setTerm($additiveExpression.term); }
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
      | (anonymousArgument)
        => anonymousArgument
        { if ($anonymousArgument.namedArgument!=null) 
              $namedArgumentList.addNamedArgument($anonymousArgument.namedArgument); }
      )*
      ( 
        sequencedArgument
        { $namedArgumentList.setSequencedArgument($sequencedArgument.sequencedArgument); }
      )?
      RBRACE
      { $namedArgumentList.setEndToken($RBRACE); }
    ;

sequencedArgument returns [SequencedArgument sequencedArgument]
    : compilerAnnotations
      { $sequencedArgument = new SequencedArgument(null);
        $sequencedArgument.getCompilerAnnotations().addAll($compilerAnnotations.annotations); }
        (
          (FOR_CLAUSE | IF_CLAUSE conditions ~THEN_CLAUSE)=>
          c1=comprehension
          { if ($c1.comprehension!=null)
                $sequencedArgument.addPositionalArgument($c1.comprehension); }
        | 
          pa1=positionalArgument
          { if ($pa1.positionalArgument!=null)
                $sequencedArgument.addPositionalArgument($pa1.positionalArgument); }
        |
          sa1=spreadArgument
          { if ($sa1.positionalArgument!=null)
                $sequencedArgument.addPositionalArgument($sa1.positionalArgument); }
        )
        (
          c=COMMA
          { $sequencedArgument.setEndToken($c); }
          (
            (FOR_CLAUSE | IF_CLAUSE conditions ~THEN_CLAUSE)=>
            c2=comprehension
            { if ($c2.comprehension!=null) {
                  $sequencedArgument.addPositionalArgument($c2.comprehension);
                  sequencedArgument.setEndToken(null); } }
          | 
            pa2=positionalArgument
            { if ($pa2.positionalArgument!=null) {
                  $sequencedArgument.addPositionalArgument($pa2.positionalArgument); 
                  sequencedArgument.setEndToken(null); } }
          | 
            sa2=spreadArgument
            { if ($sa2.positionalArgument!=null) {
                  $sequencedArgument.addPositionalArgument($sa2.positionalArgument); 
                  sequencedArgument.setEndToken(null); } }
          |
            { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(LIDENTIFIER, input)); }
          )
        )* 
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
    : memberName
      { $specifiedArgument = new SpecifiedArgument(null); 
        $specifiedArgument.setIdentifier($memberName.identifier); }
      (
        specifier 
        { $specifiedArgument.setSpecifierExpression($specifier.specifierExpression); }
      )?
      { expecting=SEMICOLON; }
      SEMICOLON
      { $specifiedArgument.setEndToken($SEMICOLON);
        expecting=-1; }
    ;

anonymousArgument returns [SpecifiedArgument namedArgument]
    @init { $namedArgument = new SpecifiedArgument(null); }
    : functionOrExpression
     { SpecifierExpression se = new SpecifierExpression(null);
       se.setExpression($functionOrExpression.expression);
       $namedArgument.setSpecifierExpression(se); }   
      { expecting=SEMICOLON; }
      SEMICOLON
      { $namedArgument.setEndToken($SEMICOLON); 
        expecting=-1; }
    ;

objectArgument returns [ObjectArgument declaration]
    : OBJECT_DEFINITION 
      { $declaration = new ObjectArgument($OBJECT_DEFINITION); 
        $declaration.setType(new ValueModifier(null)); }
      (
        memberNameDeclaration
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
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
      (
        memberNameDeclaration 
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
      (
        typeParameters
        { $declaration.setTypeParameterList($typeParameters.typeParameterList); }
      )?
      (
        parameters
        { $declaration.addParameterList($parameters.parameterList); }
      )*
      (
        block
        { $declaration.setBlock($block.block); }
      | 
        (
          functionSpecifier
          { $declaration.setSpecifierExpression($functionSpecifier.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { expecting=-1;
          $declaration.setEndToken($SEMICOLON); }
      )
    ;

inferredGetterArgument returns [AttributeArgument declaration]
    : { $declaration=new AttributeArgument(null); }
      VALUE_MODIFIER 
      { $declaration.setType(new ValueModifier($VALUE_MODIFIER)); }
      (
        memberNameDeclaration 
        { $declaration.setIdentifier($memberNameDeclaration.identifier); }
      )?
      (
        block
        { $declaration.setBlock($block.block); }
      | 
        (
          specifier 
          { $declaration.setSpecifierExpression($specifier.specifierExpression); }
        | 
          lazySpecifier 
          { $declaration.setSpecifierExpression($lazySpecifier.specifierExpression); }
        )?
        { expecting=SEMICOLON; }
        SEMICOLON
        { expecting=-1;
          $declaration.setEndToken($SEMICOLON); }
      )
    ;

typedMethodOrGetterArgument returns [TypedArgument declaration]
    @init { MethodArgument marg = new MethodArgument(null);
            AttributeArgument aarg = new AttributeArgument(null); 
            $declaration=aarg; }
    : (
        type 
        { marg.setType($type.type);
          aarg.setType($type.type); }
      |
        DYNAMIC
        { DynamicModifier dm = new DynamicModifier($DYNAMIC);
          marg.setType(dm);
          aarg.setType(dm); }
      )
      memberNameDeclaration
      { marg.setIdentifier($memberNameDeclaration.identifier);
        aarg.setIdentifier($memberNameDeclaration.identifier); }
      (
        { $declaration = marg; }
        (
          typeParameters
          { marg.setTypeParameterList($typeParameters.typeParameterList); }
        )?
        (
          parameters
          { marg.addParameterList($parameters.parameterList); }
        )+
        (
          b1=block
          { marg.setBlock($b1.block); }
        | 
          (
            functionSpecifier
            { marg.setSpecifierExpression($functionSpecifier.specifierExpression); }
          )?
          { expecting=SEMICOLON; }
          s1=SEMICOLON
          { expecting=-1;
            $declaration.setEndToken($s1); }
        )
      |
        (
          b2=block
          { aarg.setBlock($b2.block); }
        | 
          (
            specifier 
            { aarg.setSpecifierExpression($specifier.specifierExpression); }
          | 
            lazySpecifier 
            { aarg.setSpecifierExpression($lazySpecifier.specifierExpression); }
          )?
          { expecting=SEMICOLON; }
          s2=SEMICOLON
          { expecting=-1;
            $declaration.setEndToken($s2); }
        )
      )
    ;

untypedMethodOrGetterArgument returns [TypedArgument declaration]
    @init { MethodArgument marg = new MethodArgument(null);
            marg.setType(new FunctionModifier(null));
            AttributeArgument aarg = new AttributeArgument(null);
            aarg.setType(new ValueModifier(null));
            $declaration=aarg; }
    : memberName
      { marg.setIdentifier($memberName.identifier);
        aarg.setIdentifier($memberName.identifier); }
      ( 
        { $declaration = marg; }
        (
          parameters
          { marg.addParameterList($parameters.parameterList); }
        )+
        (
          functionSpecifier
          { marg.setSpecifierExpression($functionSpecifier.specifierExpression); }
        )
      |
        //(
        //  specifier 
        //  { aarg.setSpecifierExpression($specifier.specifierExpression); }
        //|
          lazySpecifier 
          { aarg.setSpecifierExpression($lazySpecifier.specifierExpression); }
        //)
      )
      { expecting=SEMICOLON; }
      SEMICOLON
      { expecting=-1;
        $declaration.setEndToken($SEMICOLON); }
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
    | untypedMethodOrGetterArgument
      { $declaration=$untypedMethodOrGetterArgument.declaration; }
    ;

//special rule for syntactic predicate
//to distinguish between a named argument
//and a sequenced argument
namedArgumentStart
    : compilerAnnotations 
      (specificationStart | declarationStart)
    ;

namedAnnotationArgumentsStart
    : LBRACE ((namedArgumentStart)=>namedArgumentStart | iterableArgumentStart | RBRACE)
    ;

iterableArgumentStart
    : compilerAnnotations expression (COMMA|SEMICOLON|RBRACE)
    ;

//special rule for syntactic predicates
specificationStart
    : LIDENTIFIER parameters* (SPECIFY|COMPUTE)
    ;

parExpression returns [ParExpression expression] 
    : LPAREN 
      { $expression = new ParExpression($LPAREN); }
      functionOrExpression
      { if ($functionOrExpression.expression!=null)
            $expression.setTerm($functionOrExpression.expression.getTerm()); }
      RPAREN
      { $expression.setEndToken($RPAREN); }
    ;
        
positionalArguments returns [PositionalArgumentList positionalArgumentList]
    : LPAREN 
      { $positionalArgumentList = new PositionalArgumentList($LPAREN); }
      (
        sa=sequencedArgument
        { if ($sa.sequencedArgument!=null) {
              for (PositionalArgument pa: $sa.sequencedArgument.getPositionalArguments())
                  $positionalArgumentList.addPositionalArgument(pa);
              //TODO: this is really nasty, PositionalArgumentList should have
              //      or be a SequencedArgument, not copy its PositionalArguments!
              $positionalArgumentList.setEndToken($sa.sequencedArgument.getMainEndToken()); } }
      )?
      RPAREN
      { $positionalArgumentList.setEndToken($RPAREN); }
    ;

positionalArgument returns [ListedArgument positionalArgument]
    : { $positionalArgument = new ListedArgument(null); }
      functionOrExpression
      { $positionalArgument.setExpression($functionOrExpression.expression); }
    ;

spreadArgument returns [SpreadArgument positionalArgument]
    : PRODUCT_OP
      { $positionalArgument = new SpreadArgument($PRODUCT_OP); }
      unionExpression
      { Expression e = new Expression(null);
        e.setTerm($unionExpression.term);
        $positionalArgument.setExpression(e); }
    ;

inferrableParameterStart
    : VOID_MODIFIER LIDENTIFIER
    | variadicType LIDENTIFIER
    | DYNAMIC LIDENTIFIER
    | VALUE_MODIFIER LIDENTIFIER
    | FUNCTION_MODIFIER LIDENTIFIER
    ;
    
annotatedInferrableParameterStart
    : 
      (stringLiteral | annotation) 
      (LIDENTIFIER | (UIDENTIFIER) => UIDENTIFIER | (unambiguousType) => unambiguousType | inferrableParameterStart)
    |
      (unambiguousType) => unambiguousType 
    | 
      inferrableParameterStart
    ;

anonParametersStart
    : typeParameters?
      LPAREN
      ( 
        RPAREN
      | (LIDENTIFIER|LBRACKET) => pattern (COMMA | RPAREN anonParametersStart2)
      | compilerAnnotations annotatedInferrableParameterStart 
      )
    ;

anonParametersStart2
    : LPAREN
      (
        RPAREN anonParametersStart2
      | (LIDENTIFIER COMMA)*
        (
          (LIDENTIFIER|LBRACKET) => pattern RPAREN anonParametersStart2 
        | compilerAnnotations annotatedInferrableParameterStart
        )
      )
    | COMPUTE
    | LBRACE
    | TYPE_CONSTRAINT
    ;

anonymousFunctionStart
    : VOID_MODIFIER
    | FUNCTION_MODIFIER (SMALLER_OP|LPAREN)
    | anonParametersStart
    ;
    
functionOrExpression returns [Expression expression]
    : (anonymousFunctionStart) =>
      anonymousFunction
      { $expression = new Expression(null);
        $expression.setTerm($anonymousFunction.function); }
    | let
      { $expression = new Expression(null);
        $expression.setTerm($let.let); }
    | ifExpression
      { $expression = new Expression(null);
        $expression.setTerm($ifExpression.term); }
    | switchExpression
      { $expression = new Expression(null); 
        $expression.setTerm($switchExpression.term); }
    | e=expression
      { $expression = $e.expression; }
    ;

let returns [LetExpression let]
    : letClause
      { $let = new LetExpression(null);
        $let.setLetClause($letClause.letClause); }
    ;

patternStart
    : (variable ENTRY_OP) => variable ENTRY_OP | 
      tuplePatternStart
    ;

letVariable returns [Statement statement]
    @init { Destructure d=null; Variable v=null; }
    : (
        (patternStart) => pattern
        { d = new Destructure(null);
          d.setPattern($pattern.pattern);
          $statement = d; }
      |
        variable
        { v = $variable.variable;
          $statement=v; }
      )
      (
        specifier
        { if (d!=null)
            d.setSpecifierExpression($specifier.specifierExpression);
          if (v!=null)
            v.setSpecifierExpression($specifier.specifierExpression); }
      )?
    ;

letClause returns [LetClause letClause]
    : LET
      { $letClause = new LetClause($LET); }
      LPAREN
      { $letClause.setEndToken($LPAREN); }
      (
        v1=letVariable
        { $letClause.setEndToken(null);
          $letClause.addVariable($v1.statement); }
        (
          COMMA
          { $letClause.setEndToken($COMMA); }
          v2=letVariable
          { $letClause.setEndToken(null); 
            $letClause.addVariable($v2.statement); }
        )*
      )?
      RPAREN
      { $letClause.setEndToken($RPAREN); }
      conditionalBranch
      { $letClause.setExpression($conditionalBranch.expression); 
        $letClause.setEndToken(null); }
    ;

switchExpression returns [SwitchExpression term]
    : switchHeader
      { $term = new SwitchExpression(null);
        $term.setSwitchClause($switchHeader.clause); }
      caseExpressions
      { $term.setSwitchCaseList($caseExpressions.switchCaseList);
        //TODO: huge copy/paste job from switchCaseElse 
        Identifier id = null;
        Switched sw = $switchHeader.clause.getSwitched();
        if (sw!=null) {
          Expression ex = sw.getExpression();
          if (ex!=null && ex.getTerm() instanceof BaseMemberExpression) {
            id = ((BaseMemberExpression) ex.getTerm()).getIdentifier();
          }
          TypedDeclaration var = $switchHeader.clause.getSwitched().getVariable();
          if (var!=null) {
            id = var.getIdentifier();
          }
        }
        if (id!=null) {
          boolean found = false;
          for (CaseClause cc: $caseExpressions.switchCaseList.getCaseClauses()) {
            CaseItem item = cc.getCaseItem();
            if (item instanceof IsCase) {
              found = true;
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
            if (item instanceof MatchCase) {
              MatchCase mc = (MatchCase) item;
              ExpressionList el = mc.getExpressionList();
              if (el!=null) {
                for (Expression e: el.getExpressions()) {
                  if (!(e.getTerm() instanceof Literal)) {
                    found = true;
                    break;
                  }
                }
              }
            }
          } 
          ElseClause ec = $caseExpressions.switchCaseList.getElseClause();
          if (ec!=null && found) {
            Variable ev = new Variable(null);
            ev.setType(new SyntheticVariable(null));
            SpecifierExpression ese = new SpecifierExpression(null);
            Expression ee = new Expression(null);
            BaseMemberExpression ebme = new BaseMemberExpression(null);
            ebme.setTypeArguments( new InferredTypeArguments(null) );
            ee.setTerm(ebme);
            ese.setExpression(ee);
            ev.setSpecifierExpression(ese);
            ec.setVariable(ev);
            ebme.setIdentifier(id);
            ev.setIdentifier(id);
          }
        } 
      }
    ;

caseExpressions returns [SwitchCaseList switchCaseList]
    : { $switchCaseList = new SwitchCaseList(null); }
      (
        caseExpression
        { $switchCaseList.addCaseClause($caseExpression.caseClause); }
      )+
      (
        elseExpression
        { $switchCaseList.setElseClause($elseExpression.elseClause); }
      )?
    ;
    
caseExpression returns [CaseClause caseClause]
    : ELSE_CLAUSE?
      CASE_CLAUSE 
      { $caseClause = new CaseClause($CASE_CLAUSE); 
        $caseClause.setOverlapping($ELSE_CLAUSE!=null); }
      caseItemList
      { $caseClause.setCaseItem($caseItemList.item); }
      conditionalBranch
      { $caseClause.setExpression($conditionalBranch.expression); }
    ;

elseExpression returns [ElseClause elseClause]
    : ELSE_CLAUSE 
      { $elseClause = new ElseClause($ELSE_CLAUSE); }
      conditionalBranch
      { $elseClause.setExpression($conditionalBranch.expression); }
    ;

ifExpression returns [IfExpression term]
    : IF_CLAUSE
      { $term = new IfExpression($IF_CLAUSE); 
        $term.setIfClause(new IfClause(null)); }
      conditions
      { $term.getIfClause().setConditionList($conditions.conditionList); 
        $term.setIfClause($term.getIfClause()); } //yew!
      thenExpression
      { IfClause ic = $thenExpression.ifClause;
        if (ic!=null) {
          $term.setIfClause(ic); 
          ic.setConditionList($conditions.conditionList); 
        } }
      elseExpression
      { ElseClause ec = $elseExpression.elseClause;
        $term.setElseClause(ec);
        ConditionList cl = $conditions.conditionList;
        if (cl!=null) {
            List<Condition> conditions = cl.getConditions();
            if (conditions.size()==1) {
              Condition c = conditions.get(0);
              Identifier id = null;
              Type t = null;
              if (c instanceof ExistsOrNonemptyCondition) {
                Statement s = ((ExistsOrNonemptyCondition)c).getVariable();
                if (s instanceof Variable) {
                  Variable v = (Variable) s;
                  t = v.getType();
                  id = v.getIdentifier();
                }
              }
              else if (c instanceof IsCondition) {
                Variable v = (Variable) ((IsCondition)c).getVariable();
                if (v!=null) {
                  t = v.getType();
                  id = v.getIdentifier();
                }
              }
              if (id!=null && ec!=null && t instanceof SyntheticVariable) { 
                Variable ev = new Variable(null);
                ev.setType(new SyntheticVariable(null));
                SpecifierExpression ese = new SpecifierExpression(null);
                Expression ee = new Expression(null);
                BaseMemberExpression ebme = new BaseMemberExpression(null);
                ebme.setTypeArguments( new InferredTypeArguments(null) );
                ee.setTerm(ebme);
                ese.setExpression(ee);
                ev.setSpecifierExpression(ese);
                ec.setVariable(ev);
                ev.setIdentifier(id);
                ebme.setIdentifier(id);
              }
            }
        } 
      }
    ;

conditionalBranch returns [Expression expression]
    : ifExpression
      { $expression = new Expression(null);
        $expression.setTerm($ifExpression.term); }
    | let
      { $expression = new Expression(null);
        $expression.setTerm($let.let); }
    | disjunctionExpression
      { $expression = new Expression(null);
        $expression.setTerm($disjunctionExpression.term); }
    ;

thenExpression returns [IfClause ifClause]
    : THEN_CLAUSE
      { $ifClause = new IfClause($THEN_CLAUSE); }
      cb1=conditionalBranch
      { $ifClause.setExpression($cb1.expression); }
    ;

anonymousFunction returns [FunctionArgument function]
    @init { $function = new FunctionArgument(null);
            $function.setType(new FunctionModifier(null)); }
    : (
        FUNCTION_MODIFIER
        { $function.setType(new FunctionModifier($FUNCTION_MODIFIER)); }
      |
        VOID_MODIFIER
        { $function.setType(new VoidModifier($VOID_MODIFIER)); }
      )?
      (
        tp=typeParameters
        { $function.setTypeParameterList($tp.typeParameterList); }
      )?
      p1=parameters
      { $function.addParameterList($p1.parameterList); }
      ( 
        p2=parameters
        { $function.addParameterList($p2.parameterList); }
      )*
      (
        tc=typeConstraints
        { $function.setTypeConstraintList($tc.typeConstraintList); }
      )?
      ( 
        COMPUTE
        //TODO: should be a LazySpecifierExpression!
        fe=functionOrExpression
        { $function.setExpression($fe.expression); }
      |
        block
        { $function.setBlock($block.block); }
      )
    ;

comprehension returns [Comprehension comprehension]
    @init { $comprehension = new Comprehension(null); }
    : forComprehensionClause
      { $comprehension.setInitialComprehensionClause($forComprehensionClause.comprehensionClause); }
    | ifComprehensionClause
      { $comprehension.setInitialComprehensionClause($ifComprehensionClause.comprehensionClause); }
    ;

comprehensionClause returns [ComprehensionClause comprehensionClause]
    : forComprehensionClause 
      { $comprehensionClause = $forComprehensionClause.comprehensionClause; }
    | (IF_CLAUSE conditions ~THEN_CLAUSE) => 
      ifComprehensionClause 
      { $comprehensionClause = $ifComprehensionClause.comprehensionClause; }
    | expressionComprehensionClause 
      { $comprehensionClause = $expressionComprehensionClause.comprehensionClause; }
    ;

expressionComprehensionClause returns [ExpressionComprehensionClause comprehensionClause]
    : functionOrExpression
      { $comprehensionClause = new ExpressionComprehensionClause(null);
        $comprehensionClause.setExpression($functionOrExpression.expression); }
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
      conditions
      { $comprehensionClause.setConditionList($conditions.conditionList); }
      comprehensionClause
      { $comprehensionClause.setComprehensionClause($comprehensionClause.comprehensionClause); }
    ;
    
assignmentExpression returns [Term term]
    @init { QualifiedMemberOrTypeExpression qe=null; }
    : ee1=thenElseExpression
      { $term = $ee1.term; }
      (
        assignmentOperator 
        { $assignmentOperator.operator.setLeftTerm($term);
          $term = $assignmentOperator.operator; }
        ee2=functionOrExpression
        { if ($ee2.expression!=null)
              $assignmentOperator.operator.setRightTerm($ee2.expression.getTerm()); }
      )?
    ;

assignmentOperator returns [AssignmentOp operator]
    : SPECIFY { $operator = new AssignOp($SPECIFY); }
    | ADD_SPECIFY { $operator = new AddAssignOp($ADD_SPECIFY); }
    | SUBTRACT_SPECIFY { $operator = new SubtractAssignOp($SUBTRACT_SPECIFY); }
    | MULTIPLY_SPECIFY { $operator = new MultiplyAssignOp($MULTIPLY_SPECIFY); }
    | DIVIDE_SPECIFY { $operator = new DivideAssignOp($DIVIDE_SPECIFY); }
    | REMAINDER_SPECIFY { $operator = new RemainderAssignOp($REMAINDER_SPECIFY); }
    | INTERSECT_SPECIFY { $operator = new IntersectAssignOp($INTERSECT_SPECIFY); }
    | UNION_SPECIFY { $operator = new UnionAssignOp($UNION_SPECIFY); }
    | COMPLEMENT_SPECIFY { $operator = new ComplementAssignOp($COMPLEMENT_SPECIFY); }
    | AND_SPECIFY { $operator = new AndAssignOp($AND_SPECIFY); }
    | OR_SPECIFY { $operator = new OrAssignOp($OR_SPECIFY); }
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
    | e=expressionOrMeta
      { $term = $e.term; }
    ;

notOperator returns [NotOp operator]
    : NOT_OP 
      { $operator = new NotOp($NOT_OP); }
    ;

//TODO: NOT BLESSED BY SPEC!!!
expressionOrMeta returns [Term term]
    : modelRef
      { $term=$modelRef.meta; }
    | equalityExpression
      { $term = $equalityExpression.term; }
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
        co1=comparisonOperator 
        { $co1.operator.setLeftTerm($term);
          $term = $co1.operator; }
        ee2=existenceEmptinessExpression
        { $co1.operator.setRightTerm($ee2.term); }
      | lo1=largerOperator 
        { $lo1.operator.setLeftTerm($term);
          $term = $lo1.operator; }
        ee3=existenceEmptinessExpression
        { $lo1.operator.setRightTerm($ee3.term); }
      | so1=smallerOperator 
        { $so1.operator.setLeftTerm($term);
          $term = $so1.operator; }
        ee4=existenceEmptinessExpression
        { $so1.operator.setRightTerm($ee4.term); }
        ( 
          so2=smallerOperator
          ee5=existenceEmptinessExpression
          { WithinOp w = new WithinOp(null); 
            Bound lb = $so1.operator instanceof SmallerOp ?
                new OpenBound(null) : new ClosedBound(null);
            lb.setTerm($ee1.term);
            Bound ub = $so2.operator instanceof SmallerOp ?
                new OpenBound(null) : new ClosedBound(null);
            ub.setTerm($ee5.term);
            w.setLowerBound(lb);
            w.setUpperBound(ub);
            w.setTerm($ee4.term);
            $term = w; }
        )?
      | to1=typeOperator
        { $to1.operator.setTerm($ee1.term); 
          $term = $to1.operator; }
        t1=type
        { $to1.operator.setType($t1.type); }
      )?
    /*| to2=typeOperator
      { $term = $to2.operator; }
      t2=type
      { $to2.operator.setType($t2.type); }
      ee3=existenceEmptinessExpression
      { $to2.operator.setTerm($ee3.term); }*/
    ;

smallerOperator returns [ComparisonOp operator]
    : SMALL_AS_OP
      { $operator = new SmallAsOp($SMALL_AS_OP); }
    | SMALLER_OP
      { $operator = new SmallerOp($SMALLER_OP); }
    ;

largerOperator returns [ComparisonOp operator]
    : LARGE_AS_OP
      { $operator = new LargeAsOp($LARGE_AS_OP); }
    | LARGER_OP
      { $operator = new LargerOp($LARGER_OP); }
    ;

comparisonOperator returns [BinaryOperatorExpression operator]
    : COMPARE_OP 
      { $operator = new CompareOp($COMPARE_OP); }
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
    | CASE_TYPES
      { $operator = new OfOp($CASE_TYPES); }
    ;

existenceEmptinessExpression returns [Term term]
    : e=entryRangeExpression
      { $term = $e.term; }
      (
        eno=existsNonemptyOperator
        { $term = $eno.operator;
          $eno.operator.setTerm($e.term); }
      )?
    ;

existsNonemptyOperator returns [UnaryOperatorExpression operator]
    : EXISTS 
      { $operator = new Exists($EXISTS); }
    | NONEMPTY
      { $operator = new Nonempty($NONEMPTY); }
    ;

entryRangeExpression returns [Term term]
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
    : me1=scaleExpression
      { $term = $me1.term; }
      (
        additiveOperator 
        { $additiveOperator.operator.setLeftTerm($term);
          $term = $additiveOperator.operator; }
        me2=scaleExpression
        { $additiveOperator.operator.setRightTerm($me2.term); }
      )*
    ;

additiveOperator returns [BinaryOperatorExpression operator]
    : SUM_OP 
      { $operator = new SumOp($SUM_OP); }
    | DIFFERENCE_OP
      { $operator = new DifferenceOp($DIFFERENCE_OP); }
    ;

scaleExpression returns [Term term]
    : multiplicativeExpression
      { $term = $multiplicativeExpression.term; }
      (
        scaleOperator
        { $scaleOperator.operator.setLeftTerm($term);
          $term = $scaleOperator.operator; }
        se=scaleExpression
        { $scaleOperator.operator.setRightTerm($se.term); }
      )?
    ;

multiplicativeExpression returns [Term term]
    : ue1=unionExpression
      { $term = $ue1.term; }
      (
        multiplicativeOperator 
        { $multiplicativeOperator.operator.setLeftTerm($term);
          $term = $multiplicativeOperator.operator; }
        ue2=unionExpression
        { $multiplicativeOperator.operator.setRightTerm($ue2.term); }
      )*
    ;

multiplicativeOperator returns [BinaryOperatorExpression operator]
    : PRODUCT_OP 
      { $operator = new ProductOp($PRODUCT_OP); }
    | QUOTIENT_OP
      { $operator = new QuotientOp($QUOTIENT_OP); }
    | REMAINDER_OP
      { $operator = new RemainderOp($REMAINDER_OP); }
    ;

unionExpression returns [Term term]
    : ie1=intersectionExpression
      { $term = $ie1.term; }
      (
        unionOperator 
        { $unionOperator.operator.setLeftTerm($term);
          $term = $unionOperator.operator; }
        ie2=intersectionExpression
        { $unionOperator.operator.setRightTerm($ie2.term); }
      )*
    ;
    
unionOperator returns [BinaryOperatorExpression operator]
    : UNION_OP
      { $operator = new UnionOp($UNION_OP); }
    | COMPLEMENT_OP
      { $operator = new ComplementOp($COMPLEMENT_OP); }
    ;

intersectionExpression returns [Term term]
    : ne1=negationComplementExpression
      { $term = $ne1.term; }
      (
        intersectionOperator 
        { $intersectionOperator.operator.setLeftTerm($term);
          $term = $intersectionOperator.operator; }
        ne2=negationComplementExpression
        { $intersectionOperator.operator.setRightTerm($ne2.term); }
      )*
    ;
    
intersectionOperator returns [BinaryOperatorExpression operator]
    : INTERSECTION_OP
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

scaleOperator returns [ScaleOp operator]
    : SCALE_OP 
      { $operator = new ScaleOp($SCALE_OP); }
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
    : valueExpression 
      { $term = $valueExpression.term; } 
      (
        postfixOperator
        { $postfixOperator.operator.setTerm($term);
          $term = $postfixOperator.operator; }
      )*
    ;

declarationLiteralStart
    : (CLASS_DEFINITION|INTERFACE_DEFINITION|NEW|ALIAS|TYPE_CONSTRAINT)
    | (FUNCTION_MODIFIER|VALUE_MODIFIER|OBJECT_DEFINITION)
      (PACKAGE MEMBER_OP)?
      (LIDENTIFIER|UIDENTIFIER)
    | PACKAGE (~MEMBER_OP)
    | MODULE
    ;

valueExpression returns [Term term]
    : (declarationLiteralStart) => declarationRef
      { $term = $declarationRef.meta; } 
    | primary
      { $term = $primary.primary; } 
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
    | PACKAGE
      { $atom = new Package($PACKAGE); }
    ;
    
nonstringLiteral returns [Literal literal]
    : NATURAL_LITERAL 
      { $literal = new NaturalLiteral($NATURAL_LITERAL); }
    | FLOAT_LITERAL 
      { $literal = new FloatLiteral($FLOAT_LITERAL); }
    | CHAR_LITERAL 
      { $literal = new CharLiteral($CHAR_LITERAL); }
    ;

stringLiteral returns [StringLiteral stringLiteral]
    : STRING_LITERAL 
      { $stringLiteral = new StringLiteral($STRING_LITERAL); }
    | VERBATIM_STRING
      { $stringLiteral = new StringLiteral($VERBATIM_STRING); }
    ;

stringExpression returns [Atom atom]
    @init { StringTemplate st=null; }
    : sl1=stringLiteral
      { $atom=$sl1.stringLiteral; }
    | STRING_START
      { st = new StringTemplate(null);
        st.addStringLiteral(new StringLiteral($STRING_START));
        $atom=st; }
      e1=functionOrExpression
      { if ($e1.expression!=null) 
            st.addExpression($e1.expression); }
      (
        STRING_MID
        { st.addStringLiteral(new StringLiteral($STRING_MID)); }
        e2=functionOrExpression
        { if ($e2.expression!=null) 
              st.addExpression($e2.expression); }
      )*
      STRING_END
      { st.addStringLiteral(new StringLiteral($STRING_END)); }
    ;

typeArguments returns [TypeArgumentList typeArgumentList]
    @init { TypeVariance v=null; }
    : SMALLER_OP
      { $typeArgumentList = new TypeArgumentList($SMALLER_OP); }
      (
        (
          v1=variance
          { v = $v1.typeVariance; }
          (
            ta1=type
            { if ($ta1.type!=null)
                  $typeArgumentList.addType($ta1.type);
              if (v!=null && $ta1.type!=null) 
                  $ta1.type.setTypeVariance(v); }
          )?
        |
          ta0=type
          { if ($ta0.type!=null)
                $typeArgumentList.addType($ta0.type); }
        )
        (
          c=COMMA
          { $typeArgumentList.setEndToken($c); }
          (
            v2=variance
            { v = $v2.typeVariance; }
            (
              ta2=type
              { if ($ta2.type!=null) {
                    $typeArgumentList.addType($ta2.type);
                    if (v!=null && $ta2.type!=null) 
                        $ta2.type.setTypeVariance(v);
                    $typeArgumentList.setEndToken(null); } }
            | { displayRecognitionError(getTokenNames(), 
                      new MismatchedTokenException(UIDENTIFIER, input)); }
            )
          | 
            (
              ta3=type
              { if ($ta3.type!=null) {
                    $typeArgumentList.addType($ta3.type);
                    $typeArgumentList.setEndToken(null); } }
            | { displayRecognitionError(getTokenNames(), 
                  new MismatchedTokenException(UIDENTIFIER, input)); }
            )
          )
        )*
      )?
      LARGER_OP
      { $typeArgumentList.setEndToken($LARGER_OP); }
    ;

variadicType returns [Type type]
    : (unionType (PRODUCT_OP|SUM_OP))=>
      at=unionType
      { $type = $at.type; }
      (
        PRODUCT_OP
        { SequencedType st = new SequencedType(null);
          st.setType($at.type); 
          st.setEndToken($PRODUCT_OP);
          $type = st; }
      |
        SUM_OP
        { SequencedType st = new SequencedType(null);
          st.setType($at.type); 
          st.setEndToken($SUM_OP);
          st.setAtLeastOne(true);
          $type = st; }
      )?
    | t=type
      { $type = $t.type; }
    ;

defaultedType returns [Type type]
    : (type (SPECIFY))=>
      t=type
      { $type = $t.type; }
      (
        SPECIFY
        { DefaultedType st = new DefaultedType(null);
          st.setType($t.type); 
          st.setEndToken($SPECIFY);
          $type = st; }
      )?
    | variadicType
      { $type=$variadicType.type; }
    ;

spreadType returns [Type type]
    @init { SpreadType spt = null; }
    : PRODUCT_OP
      { spt = new SpreadType($PRODUCT_OP);
        $type=spt; }
      (
        sp=unionType
        { spt.setType($sp.type); }
      )?
    ;

tupleType returns [TupleType type]
    : LBRACKET
      { $type = new TupleType($LBRACKET); }
      (
        spt=spreadType
        { $type.addElementType($spt.type); }
      |
        t1=defaultedType
        { if ($t1.type!=null)
              $type.addElementType($t1.type); }
        (
          c=COMMA
          { $type.setEndToken($c); }
          t2=defaultedType
          { if ($t2.type!=null) {
                $type.addElementType($t2.type);
                $type.setEndToken(null); } }
        )*
      )?
      RBRACKET
      { $type.setEndToken($RBRACKET); }
    ;

groupedType returns [GroupedType type]
    : SMALLER_OP
      { $type = new GroupedType($SMALLER_OP); }
      t=type
      { $type.setType($t.type); }
      LARGER_OP
      { $type.setEndToken($LARGER_OP); }
    ;

iterableType returns [IterableType type]
   : LBRACE
     { $type = new IterableType($LBRACE); }
     (
       t=variadicType
       { $type.setElementType($t.type); }
     )?
     RBRACE
     { $type.setEndToken($RBRACE); }
   ;

type returns [StaticType type]
    @init { TypeConstructor ct=null; }
    : (typeParameters (TYPE_CONSTRAINT|COMPUTE)) =>
      typeParameters 
      { ct = new TypeConstructor(null);
        ct.setTypeParameterList($typeParameters.typeParameterList);
        $type = ct; }
      (
        anonymousTypeConstraints
        { ct.setTypeConstraintList($anonymousTypeConstraints.typeConstraintList); }
      )?
      COMPUTE 
      entryType
      { ct.setType($entryType.type); }
    | entryType
      { $type=$entryType.type; }
    ;

entryType returns [StaticType type]
    @init { EntryType bt=null; }
    : t1=unionType
      { $type=$t1.type; }
      (
        ENTRY_OP
        { bt=new EntryType(null);
          bt.setKeyType($type);
          bt.setEndToken($ENTRY_OP); 
          $type=bt; }
        (
          t2=unionType
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
            { if ($it2.type!=null) {
                  ut.addStaticType($it2.type);
                  ut.setEndToken(null); 
              } }
//          | { displayRecognitionError(getTokenNames(), 
//                new MismatchedTokenException(UIDENTIFIER, input)); }
          )
        )+
        { $type = ut; }
      )?
    ;

intersectionType returns [StaticType type]
    @init { IntersectionType it=null; }
    : at1=primaryType
      { $type = $at1.type;
        it = new IntersectionType(null);
        it.addStaticType($type); }
      ( 
        (
          i=INTERSECTION_OP
          { it.setEndToken($i); }
          (
            at2=primaryType
            { if ($at2.type!=null) {
                  it.addStaticType($at2.type);
                  it.setEndToken(null); 
              } }
//          | { displayRecognitionError(getTokenNames(), 
//                new MismatchedTokenException(UIDENTIFIER, input)); }
          )
        )+
        { $type = it; }
      )?
    ;

atomicType returns [StaticType type]
    : qualifiedType 
      { $type=$qualifiedType.type; }
    | tupleType 
      { $type=$tupleType.type; }
    | iterableType
      { $type=$iterableType.type; }
    ;

primaryType returns [StaticType type]
    @init { FunctionType bt=null; SequenceType st=null; }
    : atomicType
      { $type=$atomicType.type; }
      (
        OPTIONAL 
        { OptionalType ot = new OptionalType(null);
          ot.setDefiniteType($type);
          ot.setEndToken($OPTIONAL);
          $type=ot; }
      | LBRACKET
        { st = new SequenceType(null);
          st.setElementType($type);
          st.setEndToken($LBRACKET); }
        (
          NATURAL_LITERAL 
          { st.setLength(new NaturalLiteral($NATURAL_LITERAL)); 
            st.setEndToken($NATURAL_LITERAL); }
        )?
        RBRACKET 
        { st.setEndToken($RBRACKET);
          $type=st; }
      | LPAREN
        { bt = new FunctionType(null);
          bt.setEndToken($LPAREN);
          bt.setReturnType($type);
          $type=bt; }
          (
            spt=spreadType
            { bt.addArgumentType($spt.type); }
          |
            t1=defaultedType
            { if ($t1.type!=null)
                  bt.addArgumentType($t1.type); }
            (
              COMMA
              { bt.setEndToken($COMMA); }
              t2=defaultedType
              { if ($t2.type!=null)
                    bt.addArgumentType($t2.type); }
            )*
          )?
        RPAREN
        { bt.setEndToken($RPAREN); }
      )*
    ;

baseType returns [StaticType type]
    @init { BaseType pt = null; }
    : 
      tna1=typeNameWithArguments
      { BaseType bt = new BaseType(null);
        bt.setIdentifier($tna1.identifier);
        if ($tna1.typeArgumentList!=null)
            bt.setTypeArgumentList($tna1.typeArgumentList);
        $type=bt; }
    |
      groupedType
      { $type=$groupedType.type; }
    | PACKAGE
      { pt = new BaseType($PACKAGE); 
        pt.setPackageQualified(true);
        $type=pt; }
      MEMBER_OP
      { pt.setEndToken($MEMBER_OP); }
      tna2=typeNameWithArguments
      { pt.setEndToken(null);
        pt.setIdentifier($tna2.identifier);
        if ($tna2.typeArgumentList!=null)
            pt.setTypeArgumentList($tna2.typeArgumentList); }
    ;

qualifiedType returns [StaticType type]
    @init { QualifiedType qt = null; }
    : baseType
      { $type=$baseType.type; }
      ( (MEMBER_OP ~LIDENTIFIER) =>
        MEMBER_OP
        { qt = new QualifiedType($MEMBER_OP);
          qt.setOuterType($type);
          $type=qt; }
        ( (UIDENTIFIER) =>
          tna=typeNameWithArguments
          { qt.setIdentifier($tna.identifier);
            if ($tna.typeArgumentList!=null)
                qt.setTypeArgumentList($tna.typeArgumentList); }
        | 
          { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(UIDENTIFIER, input)); }
          { Identifier id = new Identifier($MEMBER_OP);
            id.setText(""); 
            qt.setIdentifier(id); }
        )
      )*
    ;

typeNameWithArguments returns [Identifier identifier, 
                               TypeArgumentList typeArgumentList]
    : typeName
      { $identifier = $typeName.identifier; } 
      (
        typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;
    
memberNameWithArguments returns [Identifier identifier, 
                                 TypeArgumentList typeArgumentList]
    : memberName
      { $identifier = $memberName.identifier; } 
      (
        typeArguments
        { $typeArgumentList = $typeArguments.typeArgumentList; }
      )?
    ;
    
annotations returns [AnnotationList annotationList]
    : { $annotationList = new AnnotationList(null); }
      (
        stringLiteral
        { if ($stringLiteral.stringLiteral.getToken().getType()==VERBATIM_STRING)
              $stringLiteral.stringLiteral.getToken().setType(AVERBATIM_STRING);
          else
              $stringLiteral.stringLiteral.getToken().setType(ASTRING_LITERAL);
          AnonymousAnnotation aa = new AnonymousAnnotation(null);
          aa.setStringLiteral($stringLiteral.stringLiteral);
          $annotationList.setAnonymousAnnotation(aa); }
      )?
      (
        annotation 
        { $annotationList.addAnnotation($annotation.annotation); 
          new com.redhat.ceylon.compiler.typechecker.tree.Visitor() { 
              public void visit(StringLiteral that) {
                  if (that.getToken().getType()==VERBATIM_STRING)
                      that.getToken().setType(AVERBATIM_STRING);
                  else
                      that.getToken().setType(ASTRING_LITERAL);
              }
          }.visit($annotation.annotation); }
      )*
    ;

annotation returns [Annotation annotation]
    : annotationName
      { $annotation = new Annotation(null);
        BaseMemberExpression bme = new BaseMemberExpression(null);
        bme.setIdentifier($annotationName.identifier);
        bme.setTypeArguments( new InferredTypeArguments(null) );
        $annotation.setPrimary(bme); }
    ( 
      positionalArguments
      { $annotation.setPositionalArgumentList($positionalArguments.positionalArgumentList); }
    | (namedAnnotationArgumentsStart) => //to distinguish a named arg from an iterable type!
      namedArguments
      { $annotation.setNamedArgumentList($namedArguments.namedArgumentList); }
    | { $annotation.setPositionalArgumentList(new PositionalArgumentList(null)); }
    )
    ;

assertMessage returns [AnnotationList annotationList]
    : { $annotationList = new AnnotationList(null); }
      (
        (stringLiteral) => stringLiteral
        { if ($stringLiteral.stringLiteral.getToken().getType()==VERBATIM_STRING)
              $stringLiteral.stringLiteral.getToken().setType(AVERBATIM_STRING);
          else
              $stringLiteral.stringLiteral.getToken().setType(ASTRING_LITERAL);
          AnonymousAnnotation aa = new AnonymousAnnotation(null);
          aa.setStringLiteral($stringLiteral.stringLiteral);
          $annotationList.setAnonymousAnnotation(aa); }
      | stringExpression
        { AnonymousAnnotation aa = new AnonymousAnnotation(null);
          aa.setStringTemplate((StringTemplate) $stringExpression.atom);
          $annotationList.setAnonymousAnnotation(aa); }
      )?
    ;

compilerAnnotations returns [List<CompilerAnnotation> annotations]
    : { $annotations = new ArrayList<CompilerAnnotation>(); }
    (
      compilerAnnotation
      { $annotations.add($compilerAnnotation.annotation); }
    )*
    ;
    
compilerAnnotation returns [CompilerAnnotation annotation]
    : (
        DOLLAR
        { $annotation=new CompilerAnnotation($DOLLAR); }
      |
        AT
        { $annotation=new CompilerAnnotation($AT); }
      )
      annotationName 
      { $annotation.setIdentifier($annotationName.identifier); }
      ( 
          SEGMENT_OP
          stringLiteral
          { $annotation.setStringLiteral($stringLiteral.stringLiteral); }
      )?
    ;

conditions returns [ConditionList conditionList]
    : LPAREN
      { $conditionList = new ConditionList($LPAREN); }
      (
      c1=condition
      { if ($c1.condition!=null) 
            $conditionList.addCondition($c1.condition); }
      ( c=COMMA 
        { $conditionList.setEndToken($c); }
        (
          c2=condition 
          { if ($c2.condition!=null) 
                $conditionList.addCondition($c2.condition);
            $conditionList.setEndToken(null); }
        | { displayRecognitionError(getTokenNames(), 
              new MismatchedTokenException(LIDENTIFIER, input)); } //TODO: sometimes it should be RPAREN!
        )
      )*
      )?
      RPAREN
      { $conditionList.setEndToken($RPAREN); }
    ;

condition returns [Condition condition]
    : existsCondition
      { $condition=$existsCondition.condition; }
    | nonemptyCondition
      { $condition=$nonemptyCondition.condition; }
    | isCondition 
      { $condition=$isCondition.condition; }
    | satisfiesCondition
      { $condition=$satisfiesCondition.condition; }
    | booleanCondition
      { $condition=$booleanCondition.condition; }
    ;
    
booleanCondition returns [BooleanCondition condition]
    : { $condition = new BooleanCondition(null); }
      functionOrExpression
      { $condition.setExpression($functionOrExpression.expression); }
    ;


letStart
    : (patternStart) => patternStart 
    | compilerAnnotations 
      (declarationStart|specificationStart)
    ;

existsCondition returns [ExistsCondition condition]
    : (
        NOT_OP
        { $condition = new ExistsCondition($NOT_OP);
          $condition.setNot(true); }
      )?
      EXISTS 
      { if ($condition==null)
            $condition = new ExistsCondition($EXISTS); }
      ( 
        (letStart) =>
        letVariable 
        { $condition.setVariable($letVariable.statement); }
      | (LIDENTIFIER (RPAREN|COMMA))=> iv1=impliedVariable
        { $condition.setVariable($iv1.variable); }
      | (expression (RPAREN|COMMA))=> e1=expression
        { $condition.setBrokenExpression($e1.expression); }
      | (LIDENTIFIER)=> iv2=impliedVariable
        { $condition.setVariable($iv2.variable); }
      | e2=expression
        { $condition.setBrokenExpression($e2.expression); }
      )
    ;
    
nonemptyCondition returns [NonemptyCondition condition]
    : (
        NOT_OP
        { $condition = new NonemptyCondition($NOT_OP);
          $condition.setNot(true); }
      )?
      NONEMPTY 
      { if ($condition==null)
            $condition = new NonemptyCondition($NONEMPTY); }
      ( 
        (letStart) =>
        letVariable 
        { $condition.setVariable($letVariable.statement); }
      | (LIDENTIFIER (RPAREN|COMMA))=> iv1=impliedVariable
        { $condition.setVariable($iv1.variable); }
      | (expression (RPAREN|COMMA))=> e1=expression
        { $condition.setBrokenExpression($e1.expression); }
      | (LIDENTIFIER)=> iv2=impliedVariable
        { $condition.setVariable($iv2.variable); }
      | e2=expression
        { $condition.setBrokenExpression($e2.expression); }
      )
    ;

isCondition returns [IsCondition condition]
    : (
        NOT_OP
        { $condition = new IsCondition($NOT_OP);
          $condition.setNot(true); }
      )?
      IS_OP 
      { if ($condition==null)
            $condition = new IsCondition($IS_OP); }
      type
      { $condition.setType($type.type); }
      (
        (LIDENTIFIER SPECIFY) =>
        v=isConditionVariable
        { $condition.setVariable($v.variable); }
      | impliedVariable 
        { $condition.setVariable($impliedVariable.variable); }
      )
    ;

isConditionVariable returns [Variable variable]
    @init { $variable = new Variable(null);
            $variable.setType(new ValueModifier(null));  }
    : memberNameDeclaration
      { $variable.setIdentifier($memberNameDeclaration.identifier); }
      specifier
      { $variable.setSpecifierExpression($specifier.specifierExpression); }
    ;

satisfiesCondition returns [SatisfiesCondition condition]
    : SATISFIES 
      { $condition = new SatisfiesCondition($SATISFIES); }
      type 
      { $condition.setType($type.type); }
      typeName
      { $condition.setIdentifier($typeName.identifier); }
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
    | dynamic
      { $controlStatement=$dynamic.statement; }
    ;

controlBlock returns [Block block]
    : ( (LBRACE)=> b=block
        { $block=$b.block; }
      | { displayRecognitionError(getTokenNames(), 
                new MismatchedTokenException(LBRACE, input)); }
      )
    ;

dynamic returns [DynamicStatement statement]
    : dynamicClause
      { $statement = new DynamicStatement(null); 
        $statement.setDynamicClause($dynamicClause.dynamic); }
    ;
    
dynamicClause returns [DynamicClause dynamic]
    : DYNAMIC 
      { $dynamic = new DynamicClause($DYNAMIC); }
      block
      { $dynamic.setBlock($block.block); }
    ;

ifElse returns [IfStatement statement]
    : { $statement = new IfStatement(null); }
      ifBlock 
      { $statement.setIfClause($ifBlock.clause); }
      ( 
        elseBlock
        { ElseClause ec = $elseBlock.clause;
          $statement.setElseClause(ec);
          ConditionList cl = $ifBlock.clause.getConditionList();
          if (cl!=null) {
            List<Condition> conditions = cl.getConditions();
            if (conditions.size()==1) {
              Condition c = conditions.get(0);
              Identifier id = null;
              Type t = null;
              if (c instanceof ExistsOrNonemptyCondition) {
                Statement s = ((ExistsOrNonemptyCondition)c).getVariable();
                if (s instanceof Variable) {
                  Variable v = (Variable) s;
                  t = v.getType();
                  id = v.getIdentifier();
                }
              }
              else if (c instanceof IsCondition) {
                Variable v = (Variable) ((IsCondition)c).getVariable();
                if (v!=null) {
                  t = v.getType();
                  id = v.getIdentifier();
                }
              }
              if (id!=null && ec!=null && t instanceof SyntheticVariable) { 
                Variable ev = new Variable(null);
                ev.setType(new SyntheticVariable(null));
                SpecifierExpression ese = new SpecifierExpression(null);
                Expression ee = new Expression(null);
                BaseMemberExpression ebme = new BaseMemberExpression(null);
                ebme.setTypeArguments( new InferredTypeArguments(null) );
                ee.setTerm(ebme);
                ese.setExpression(ee);
                ev.setSpecifierExpression(ese);
                ec.setVariable(ev);
                ev.setIdentifier(id);
                ebme.setIdentifier(id);
              }
            }
          }
        }
      )?
    ;

ifBlock returns [IfClause clause]
    : IF_CLAUSE 
      { $clause = new IfClause($IF_CLAUSE); }
      conditions
      { $clause.setConditionList($conditions.conditionList); }
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
    ;

switchCaseElse returns [SwitchStatement statement]
    : { $statement = new SwitchStatement(null); }
      switchHeader 
      { $statement.setSwitchClause($switchHeader.clause); }
      cases
      { $statement.setSwitchCaseList($cases.switchCaseList);
        Identifier id = null;
        Switched sw = $switchHeader.clause.getSwitched();
        if (sw!=null) {
          Expression ex = sw.getExpression();
          if (ex!=null && ex.getTerm() instanceof BaseMemberExpression) {
            id = ((BaseMemberExpression) ex.getTerm()).getIdentifier();
          }
          TypedDeclaration var = sw.getVariable();
          if (var!=null) {
            id = var.getIdentifier();
          }
        }
        if (id!=null) {
          boolean found = false;
          for (CaseClause cc: $cases.switchCaseList.getCaseClauses()) {
            CaseItem item = cc.getCaseItem();
            if (item instanceof IsCase) {
              found = true;
              IsCase ic = (IsCase) item;
              Variable v = new Variable(null);
              v.setType(new SyntheticVariable(null));
              SpecifierExpression se = new SpecifierExpression(null);
              Expression e = new Expression(null);
              BaseMemberExpression bme = new BaseMemberExpression(null);
              bme.setTypeArguments( new InferredTypeArguments(null) );
              e.setTerm(bme);
              se.setExpression(e);
              v.setSpecifierExpression(se);
              ic.setVariable(v);
              bme.setIdentifier(id);
              v.setIdentifier(id);
            }
            if (item instanceof MatchCase) {
              MatchCase mc = (MatchCase) item;
              ExpressionList el = mc.getExpressionList();
              if (el!=null) {
                for (Expression e: el.getExpressions()) {
                  if (!(e.getTerm() instanceof Literal)) {
                    found = true;
                    break;
                  }
                }
              }
            }
          }
          ElseClause ec = $cases.switchCaseList.getElseClause();
          if (ec!=null && found) {
            Variable ev = new Variable(null);
            ev.setType(new SyntheticVariable(null));
            SpecifierExpression ese = new SpecifierExpression(null);
            Expression ee = new Expression(null);
            BaseMemberExpression ebme = new BaseMemberExpression(null);
            ebme.setTypeArguments( new InferredTypeArguments(null) );
            ee.setTerm(ebme);
            ese.setExpression(ee);
            ev.setSpecifierExpression(ese);
            ec.setVariable(ev);
            ebme.setIdentifier(id);
            ev.setIdentifier(id);
          }
        }
      }
    ;

switchHeader returns [SwitchClause clause]
    : SWITCH_CLAUSE 
      { $clause = new SwitchClause($SWITCH_CLAUSE); }
      LPAREN
      { $clause.setEndToken($LPAREN); }
      (
        switched 
        { $clause.setSwitched($switched.switched); 
          clause.setEndToken(null); }
      )?
      RPAREN
      { $clause.setEndToken($RPAREN); }
    ;

compilerAnnotationStart
    : DOLLAR | AT
    ;

switched returns [Switched switched]
    @init { $switched = new Switched(null); }
    : ( (compilerAnnotationStart|declarationStart|specificationStart) 
        => specifiedVariable
        { $switched.setVariable($specifiedVariable.variable); }
      | expression
        { $switched.setExpression($expression.expression); }
      )
    ;

cases returns [SwitchCaseList switchCaseList]
    : { $switchCaseList = new SwitchCaseList(null); }
      (
        caseBlock
        { $switchCaseList.addCaseClause($caseBlock.clause); }
      )+
      (
        elseBlock
        { $switchCaseList.setElseClause($elseBlock.clause); }
      )?
    ;
    
caseBlock returns [CaseClause clause]
    : ELSE_CLAUSE?
      CASE_CLAUSE 
      { $clause = new CaseClause($CASE_CLAUSE); 
        $clause.setOverlapping($ELSE_CLAUSE!=null); }
      caseItemList
      { $clause.setCaseItem($caseItemList.item); }
      block
      { $clause.setBlock($block.block); }
    ;

caseItemList returns [CaseItem item]
    : LPAREN
      (
        ci=caseItem
        { $item = $ci.item;
          if ($item!=null) 
              $item.setEndToken($LPAREN); }
      )?
      RPAREN 
      { if ($item!=null) 
            $item.setEndToken($RPAREN); }
    ;

caseItem returns [CaseItem item]
    : (IS_OP) => isCaseCondition 
      { $item = $isCaseCondition.item; }
    | (SATISFIES) => satisfiesCaseCondition
      { $item = $satisfiesCaseCondition.item; }
    | (LBRACKET LIDENTIFIER) => matchCaseCondition
      { $item = $matchCaseCondition.item; }
    | (patternStart) => pattern
      { PatternCase pc = new PatternCase(null);
        pc.setPattern($pattern.pattern);
        $item = pc; }
    | matchCaseCondition
      { $item = $matchCaseCondition.item; }
    ;

matchCaseCondition returns [MatchCase item]
    : valueCaseList
      { $item = new MatchCase(null);
        $item.setExpressionList($valueCaseList.expressionList); }
    ;

isCaseCondition returns [IsCase item]
    @init { StaticType t = null; } 
    : IS_OP 
      { $item = new IsCase($IS_OP); }
      type
      { t = $type.type;
        $item.setType(t); }
      (
        MEMBER_OP
        { QualifiedType qt = new QualifiedType($MEMBER_OP);
          qt.setOuterType(t);
          $item.setType(qt);  }
      )?
    ;

satisfiesCaseCondition returns [SatisfiesCase item]
    : SATISFIES 
      { $item = new SatisfiesCase($SATISFIES); }
      type
      { $item.setType($type.type); }
    ;

forElse returns [ForStatement statement]
    : { $statement=new ForStatement(null); }
      forBlock 
      { $statement.setForClause($forBlock.clause); }
      (
        failBlock
        { $statement.setElseClause($failBlock.clause); }
      )?
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
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

forIterator returns [ForIterator iterator]
    @init { ValueIterator vi = null;
            PatternIterator pi = null; }
    : LPAREN
	    { vi = new ValueIterator($LPAREN); 
	      pi = new PatternIterator($LPAREN); 
	      $iterator = vi; }
	    ( 
	      (
	        (patternStart) => pattern
	        { pi.setPattern($pattern.pattern);
	          $iterator = pi; }
	      |
	        variable
	        { vi.setVariable($variable.variable); }
	      )
	      (
	        containment
	        { $iterator.setSpecifierExpression($containment.specifierExpression); }
	      )?
	    )?
	    RPAREN
	    { $iterator.setEndToken($RPAREN); }
    ;
    
containment returns [SpecifierExpression specifierExpression]
    : (
        IN_OP
        { $specifierExpression = new SpecifierExpression($IN_OP); }
      | 
        { displayRecognitionError(getTokenNames(), 
          new MismatchedTokenException(IN_OP, input)); }
        SEGMENT_OP
        { $specifierExpression = new SpecifierExpression($SEGMENT_OP); }
      )
      (
        expression
        { $specifierExpression.setExpression($expression.expression); }
      )?
    ;
    
whileLoop returns [WhileStatement statement]
    : { $statement = new WhileStatement(null); }
      whileBlock
      { $statement.setWhileClause($whileBlock.clause); }
    ;

whileBlock returns [WhileClause clause]
    : WHILE_CLAUSE
      { $clause = new WhileClause($WHILE_CLAUSE); }
      conditions
      { $clause.setConditionList($conditions.conditionList); }
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
    ;

tryBlock returns [TryClause clause]
    : TRY_CLAUSE 
      { $clause = new TryClause($TRY_CLAUSE); }
      (
        resources
        { $clause.setResourceList($resources.resources); }
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
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

catchVariable returns [CatchVariable catchVariable]
    : LPAREN 
      { $catchVariable=new CatchVariable($LPAREN); }
      (
      variable 
      { $catchVariable.setVariable($variable.variable); }
      )?
      RPAREN 
      { $catchVariable.setEndToken($RPAREN); }
    ;


finallyBlock returns [FinallyClause clause]
    : FINALLY_CLAUSE 
      { $clause = new FinallyClause($FINALLY_CLAUSE); }
      controlBlock
      { $clause.setBlock($controlBlock.block); }
    ;

resources returns [ResourceList resources]
    : LPAREN 
	    { $resources = new ResourceList($LPAREN); }
	    (
	      r1=resource
	      { $resources.addResource($r1.resource); }
	      (
	        c=COMMA 
	        { $resources.setEndToken($c); }
	        r2=resource
	        { $resources.addResource($r2.resource);
	          $resources.setEndToken(null); }
	      )*
	    )?
	    RPAREN
	    { $resources.setEndToken($RPAREN); }
    ;

resource returns [Resource resource]
    @init { $resource = new Resource(null); }
    : 
      (compilerAnnotationStart|declarationStart|specificationStart) => 
      specifiedVariable
      { $resource.setVariable($specifiedVariable.variable); }
    | 
      expression
      { $resource.setExpression($expression.expression); }
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
    @init { $variable = new Variable(null); }
    : 
      ( 
        type 
        { $variable.setType($type.type); }
      | 
        VOID_MODIFIER
        { $variable.setType(new VoidModifier($VOID_MODIFIER)); }
      | 
        FUNCTION_MODIFIER
        { $variable.setType(new FunctionModifier($FUNCTION_MODIFIER)); }
      | 
        VALUE_MODIFIER
        { $variable.setType(new ValueModifier($VALUE_MODIFIER)); }
      )
      mn1=memberNameDeclaration
      { $variable.setIdentifier($mn1.identifier); }
      ( 
        p1=parameters
        { $variable.addParameterList($p1.parameterList); }
      )*
    | 
      { $variable.setType( new ValueModifier(null) ); }
      mn2=memberName
      { $variable.setIdentifier($mn2.identifier); }
      (
        p2=parameters
        { $variable.setType( new FunctionModifier(null) );
          $variable.addParameterList($p2.parameterList); }
      )*
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
    ;

referencePathElement returns [Identifier identifier]
    : typeName 
      { $identifier=$typeName.identifier; }
    | memberName
      { $identifier=$memberName.identifier; }
    ;
    
referencePath returns [SimpleType type]
    : (
        e1=referencePathElement 
        { BaseType bt = new BaseType(null);
          bt.setIdentifier($e1.identifier);
          $type = bt; }
      | 
        PACKAGE
	      { BaseType pbt = new BaseType($PACKAGE);
	        pbt.setPackageQualified(true);
	        $type = pbt; }
	      o1=MEMBER_OP
	      { $type.setEndToken($o1); }
	      e2=referencePathElement
	      { $type.setEndToken(null);
	        $type.setIdentifier($e2.identifier); }
      )
      (
        o2=MEMBER_OP
        { QualifiedType qt = new QualifiedType($o2);
          qt.setOuterType($type);
          $type = qt; }
        e3=referencePathElement
        { $type.setIdentifier($e3.identifier); }
      )*
    ; 

moduleLiteral returns [ModuleLiteral literal]
 : MODULE
   { $literal = new ModuleLiteral($MODULE); }
   (
     p1=packagePath
     { $literal.setImportPath($p1.importPath); }
   )?
 ;

packageLiteral returns [PackageLiteral literal]
 : PACKAGE
   { $literal = new PackageLiteral($PACKAGE); }
   (
     p2=packagePath
     { $literal.setImportPath($p2.importPath); }
   )?
 ;

classLiteral returns [ClassLiteral literal]
 : CLASS_DEFINITION
   { $literal = new ClassLiteral($CLASS_DEFINITION); }
   (
     ct=referencePath
     { $literal.setType($ct.type); }
   )?
 ;

interfaceLiteral returns [InterfaceLiteral literal]
 : INTERFACE_DEFINITION
   { $literal = new InterfaceLiteral($INTERFACE_DEFINITION); }
   (
     it=referencePath
     { $literal.setType($it.type); }
   )?
 ;

newLiteral returns [NewLiteral literal]
 : NEW
   { $literal = new NewLiteral($NEW); }
   (
     nt=referencePath
     { $literal.setType($nt.type); }
   )?
 ;

aliasLiteral returns [AliasLiteral literal]
 : ALIAS
   { $literal = new AliasLiteral($ALIAS); }
   (
     at=referencePath
     { $literal.setType($at.type); }
   )?
 ;

typeParameterLiteral returns [TypeParameterLiteral literal]
 : TYPE_CONSTRAINT
   { $literal = new TypeParameterLiteral($TYPE_CONSTRAINT); }
   (
     tt=referencePath
     { $literal.setType($tt.type); }
   )?
 ;

valueLiteral returns [ValueLiteral literal]
  : (
      VALUE_MODIFIER
      { $literal = new ValueLiteral($VALUE_MODIFIER); }
    |
      OBJECT_DEFINITION
      { $literal = new ValueLiteral($OBJECT_DEFINITION);
        $literal.setBroken(true); }
    )
    vt=referencePath
    {
      if ($vt.type instanceof QualifiedType) {
        $literal.setType(((QualifiedType)$vt.type).getOuterType());
        $literal.setIdentifier($vt.type.getIdentifier());
        $literal.setTypeArgumentList($vt.type.getTypeArgumentList());
      }
      else if ($vt.type instanceof BaseType) {
        $literal.setIdentifier($vt.type.getIdentifier());
        $literal.setTypeArgumentList($vt.type.getTypeArgumentList());
      }
    }
  ;

functionLiteral returns [FunctionLiteral literal]
  : FUNCTION_MODIFIER
    { $literal = new FunctionLiteral($FUNCTION_MODIFIER); }
    ft=referencePath
    {
      if ($ft.type instanceof QualifiedType) {
        QualifiedType qt = (QualifiedType) $ft.type;
        $literal.setType(qt.getOuterType());
        $literal.setIdentifier(qt.getIdentifier());
        $literal.setTypeArgumentList(qt.getTypeArgumentList());
      }
      else if ($ft.type instanceof BaseType) {
        BaseType bt = (BaseType) $ft.type;
        $literal.setIdentifier(bt.getIdentifier());
        $literal.setTypeArgumentList(bt.getTypeArgumentList());
        $literal.setPackageQualified(bt.getPackageQualified());
      }
    }
  ;

memberPathElement returns [Identifier identifier, 
                          TypeArgumentList typeArgumentList]
    : memberName
      { $identifier=$memberName.identifier; }
      (
        typeArguments
        { $typeArgumentList=$typeArguments.typeArgumentList; }
      )?
    ;
    

memberModelExpression returns [MemberLiteral literal]
    @init { $literal = new MemberLiteral(null); }
    : 
      e1=memberPathElement 
      { $literal.setIdentifier($e1.identifier);
        $literal.setTypeArgumentList($e1.typeArgumentList); }
    |
      PACKAGE
      { $literal.setToken($PACKAGE);
        $literal.setPackageQualified(true);  }
      o2=MEMBER_OP
      { $literal.setEndToken($o2); }
      e2=memberPathElement
      { $literal.setEndToken(null);
        $literal.setIdentifier($e2.identifier); 
        $literal.setTypeArgumentList($e2.typeArgumentList); }
    | 
      at=primaryType
      { $literal.setType($at.type); }
      o3=MEMBER_OP
      { $literal.setEndToken($o3); }
      e3=memberPathElement
      { $literal.setEndToken(null);
        $literal.setIdentifier($e3.identifier);
        $literal.setTypeArgumentList($e3.typeArgumentList); }
    ; 

typeModelExpression returns [TypeLiteral literal]
    @init { $literal = new TypeLiteral(null); }
    : type
      { $literal.setType($type.type); }
    ;

modelExpression returns [MetaLiteral meta]
  :
    (((PACKAGE|primaryType) MEMBER_OP)? LIDENTIFIER) =>
    memberModelExpression
    { $meta=$memberModelExpression.literal; }
  | 
    typeModelExpression
    { $meta=$typeModelExpression.literal; }
  ;

metaLiteral returns [MetaLiteral meta]
    : d1=BACKTICK
    { $meta = new TypeLiteral($d1); }
    ( declarationRef
      { $meta=$declarationRef.meta; 
        $meta.setToken($d1); }
    | modelExpression
      { $meta=$modelExpression.meta; 
        $meta.setToken($d1); }     
    )
    d2=BACKTICK
    { $meta.setEndToken($d2); }
    ;

modelRef returns [MetaLiteral meta]
    : m=POWER_OP
      { $meta = new TypeLiteral($m); }
      modelExpression
      { if ($modelExpression.meta!=null) {
          $meta=$modelExpression.meta; 
          $meta.setToken($m);
        } }
    ;
    
declarationRef returns [MetaLiteral meta]
    : moduleLiteral
      { $meta=$moduleLiteral.literal; }
    | packageLiteral
      { $meta=$packageLiteral.literal; }
    | classLiteral
      { $meta=$classLiteral.literal; }
    | newLiteral
      { $meta=$newLiteral.literal; }
    | interfaceLiteral
      { $meta=$interfaceLiteral.literal; }
    | aliasLiteral
      { $meta=$aliasLiteral.literal; }
    | typeParameterLiteral
      { $meta=$typeParameterLiteral.literal; }
    | valueLiteral
      { $meta=$valueLiteral.literal; }
    | functionLiteral
      { $meta=$functionLiteral.literal; }
    ;


// Lexer

fragment
Digits
    : Digit ('_' | Digit)*
    ;

fragment
HexDigits
    : HexDigit ('_' | HexDigit)*
    ;

fragment
BinaryDigits
    : BinaryDigit ('_' | BinaryDigit)*
    ;

fragment 
Exponent    
    : ( 'e' | 'E' ) ( '+' | '-' )? Digit*
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
    | '#' HexDigits
    | '$' BinaryDigits
    ;
    
fragment ASTRING_LITERAL:;
fragment AVERBATIM_STRING:;

CHAR_LITERAL
    :   '\'' CharPart '\''
    ;

fragment STRING_START:;
STRING_LITERAL
    :   '"' StringPart ( '"' | '``' { $type = STRING_START; } (('`' ~'`') => '`')? )?
    ;

fragment STRING_MID:;
STRING_END
    :   '``' StringPart ( '"' | '``' { $type = STRING_MID; } (('`' ~'`') => '`')? )?
    ;

VERBATIM_STRING
    :	'"""' (~'"' | '"' ~'"' | '""' ~'"')* ('"' ('"' ('"' ('"' '"'?)?)?)?)?
    ;

/*
 Stef: we must take 32-bit code points into account here because AntLR considers each
 character to be 16-bit like in Java, which means that it will consider a high/low surrogate
 pair as two characters and refuse it in a character literal. So we match either a code point
 pair, or a normal 16-bit code point.
*/
fragment
CharPart
    : ( ~('\\' | '\'') | EscapeSequence )*
    ;
    
/*
 Stef: AntLR considers each character to be 16-bit like in Java, which means that it will consider a 
 high/low surrogate pair as two characters, BUT because the special characters we care about, such
 as termination quotes or backslash escapes (including unicode escapes) all fall outside the range
 of both the high and low surrogate parts of 32-bit code points character pairs, so we don't care
 because 32-bit code points, even taken as two chars, cannot match any of our special characters,
 so we can just process them as-is.
*/
fragment
StringPart
    : ( ~('\\' | '"' | '`') | ( ('`' ~'`') => '`' ) | EscapeSequence )*
    ;

fragment
EscapeSequence 
    :   '\\' 
        ( ~'{' | '{' (~'}')* '}'? )?
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

BACKTICK
    : '`'
    ;

/*
    List of keywords.
    
    Note that this must be kept in sync with com.redhat.ceylon.compiler.typechecker.parser.ParseUtil
*/

ABSTRACTED_TYPE
    :   'abstracts'
    ;

ALIAS
    :   'alias'
    ;

ASSEMBLY
    : 'assembly'
    ;

ASSERT
    : 'assert'
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
    
DYNAMIC
    :   'dynamic'
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

FUNCTION_MODIFIER
    :   'function'
    ;

TYPE_CONSTRAINT
    :   'given'
    ;

IF_CLAUSE
    :   'if'
    ;

IMPORT
    :   'import'
    ;

IN_OP
    :   'in'
    ;

INTERFACE_DEFINITION
    :   'interface'
    ;

IS_OP
    :   'is'
    ;

LET
    :   'let'
    ;

MODULE
    :   'module'
    ;

NEW
    :   'new'
    ;

NONEMPTY
    :   'nonempty'
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

OUTER
    :   'outer'
    ;

PACKAGE
    :   'package'
    ;

RETURN
    :   'return'
    ;

SATISFIES
    :   'satisfies'
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

THROW
    :   'throw'
    ;

TRY_CLAUSE
    :   'try'
    ;

VALUE_MODIFIER
    :   'value'
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

LBRACKET
    :   '['
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

COMPUTE
    :   '=>'
    ;

SAFE_MEMBER_OP
    :   '?.'
    ;

OPTIONAL
    :    '?'
    ;

NOT_OP
    :   '!'
    ;

COMPLEMENT_OP
    :   '~'
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

SPREAD_OP
    :    '*.'
    ;

SCALE_OP
    :    '**'
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
    :   ('>=' (' ' | '\r' | '\t' | '\f' | '\n')* ~('>' | ']' | ')' | ',' | ' ' | '\r' | '\t' | '\f' | '\n')) 
        => '>='
    |   '>' { $type=LARGER_OP; }
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
    
POWER_OP
    :    '^'
    ;

ADD_SPECIFY
    :   '+='
    ;

SUBTRACT_SPECIFY
    :   '-='
    ;

MULTIPLY_SPECIFY
    :   '*='
    ;

DIVIDE_SPECIFY
    :   '/='
    ;

INTERSECT_SPECIFY
    :   '&='
    ;

UNION_SPECIFY
    :   '|='
    ;

COMPLEMENT_SPECIFY
    :   '~='
    ;
    
REMAINDER_SPECIFY
    :   '%='
    ;

AND_SPECIFY
    :   '&&='
    ;

OR_SPECIFY
    :   '||='
    ;

DOLLAR
    :   '$'
    ;

AT
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
    ;

fragment
Letter
    : 'a'..'z' 
    | 'A'..'Z' 
    | '\u0080'..'\uffff'
    ;

fragment
Digit
    : '0'..'9'
    ;

fragment
HexDigit
    : '0'..'9' | 'A'..'F' | 'a'..'f'
    ;

fragment
BinaryDigit
    : '0'|'1'
    ;
