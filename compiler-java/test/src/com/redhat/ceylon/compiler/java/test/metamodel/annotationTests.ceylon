
import ceylon.language.metamodel{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations,
    OptionalAnnotation, SequencedAnnotation, Annotated, 
    ClassOrInterface, Class, Interface,
    Function, Value, VariableDeclaration=Variable
}
import ceylon.language.metamodel.untyped {
    ValueDeclaration = Value,
    UntypedDeclaration = Declaration,
    Package
}
/*
ClassOrInterface<T> annotationType<T>(T t) {
    assert(is ClassOrInterface<T> annoType = type(t));
    return annoType;
}
ClassOrInterface<Shared> sharedAnnotation = annotationType(Shared());
ClassOrInterface<Abstract> abstractAnnotation = annotationType(Abstract());
ClassOrInterface<Variable> variableAnnotation = annotationType(Variable());
ClassOrInterface<Doc> docAnnotation = annotationType(Doc(""));
ClassOrInterface<ThrownException> thrownAnnotation = annotationType(ThrownException(Exception(), ""));
ClassOrInterface<See> seeAnnotation = annotationType(See(""));

void checkClassAnnotations() {
    value noParams = NoParams();
    assert(is Class<NoParams, Empty> noParamsType = type(NoParams()));
    assert(annotations(sharedAnnotation, noParamsType) exists);
    assert(! annotations(abstractAnnotation, noParamsType) exists);
    assert(optionalAnnotation(sharedAnnotation, noParamsType) exists);
    assert(! optionalAnnotation(abstractAnnotation, noParamsType) exists);
    
    
    variable ThrownException[] thrown = annotations(thrownAnnotation, noParamsType);
    assert(thrown.size == 2);
    assert(exists ex=thrown[0], exists ex2=thrown[1]);
    // TODO Should be able to assert based on order
    assert(ex.when == "Never, actually" || ex2.when == "Never, actually");
    assert(ex.when == "Also never" || ex2.when == "Also never");
    
    thrown = sequencedAnnotations(thrownAnnotation, noParamsType);
    assert(thrown.size == 2);
    assert(exists ex3=thrown[0], exists ex4=thrown[1]);
    // TODO Should be able to assert based on order
    assert(ex3.when == "Never, actually" || ex4.when == "Never, actually");
    assert(ex3.when == "Also never" || ex4.when == "Also never");
    // TODO assertions on ex.type
    
    variable See[] sees = annotations(seeAnnotation, noParamsType);
    assert(sees.size == 0);
    sees = sequencedAnnotations(seeAnnotation, noParamsType);
    assert(sees.size == 0);
    
    value fixedParamsType = type(FixedParams("a", 1, 1.2, 'a', true, noParams));
    assert(is Class<FixedParams, [String, Integer, Float, Character, Boolean, Object]> fixedParamsType);
    assert(optionalAnnotation(sharedAnnotation, fixedParamsType) exists);
    sees = sequencedAnnotations(seeAnnotation, fixedParamsType);
    assert(sees.size == 1);
    
    value typeParamsType = type(TypeParams("a", 1));
    assert(is Class<TypeParams<String>, [String, Integer]> typeParamsType);
    assert(optionalAnnotation(sharedAnnotation, typeParamsType) exists);
}

void checkInterfaceAnnotations() {
    value implementorAppliedType = type(Implementor());
    
    assert(is Class<Anything,[]> implementorAppliedType);
    assert(exists d = optionalAnnotation(docAnnotation, implementorAppliedType), d.description == "implementor");
    
    value implementorDecl = implementorAppliedType.declaration;
    
    //TODO value interfaceType = implementorDecl.superclass;
    ///assert(is Interface<Anything> interfaceType);
    //assert(optionalAnnotation(sharedAnnotation, interfaceType) exists);
    //assert(exists doc=optionalAnnotation(docAnnotation, interfaceType), 
    //    doc.description == "interface");
    
}

void checkMemberAttributeAnnotations(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    assert(exists string = noParamsType.getAttribute<NoParams, Value<String>>("str"));
    assert(string(noParamsInstance).get() == "a");
    
    assert(exists integer = noParamsType.getAttribute<NoParams, Value<Integer>>("integer"));
    assert(integer(noParamsInstance).get() == 1);
    
    assert(exists float = noParamsType.getAttribute<NoParams, Value<Float>>("float"));
    assert(float(noParamsInstance).get() == 1.2);
    
    assert(exists character = noParamsType.getAttribute<NoParams, Value<Character>>("character"));
    assert(character(noParamsInstance).get() == 'a');
    
    assert(exists boolean = noParamsType.getAttribute<NoParams, Value<Boolean>>("boolean"));
    assert(boolean(noParamsInstance).get() == true);
    
    assert(exists obj = noParamsType.getAttribute<NoParams, Value<NoParams>>("obj"));
    assert(obj(noParamsInstance).get() === noParamsInstance);

    assert(exists string2 = noParamsType.getAttribute<NoParams, VariableDeclaration<String>>("str2"));
    value string2Bound = string2(noParamsInstance);
    assert(string2Bound.get() == "a");
    string2Bound.set("b");
    assert(string2Bound.get() == "b");
    assert(noParamsInstance.str2 == "b");
    
    assert(exists integer2 = noParamsType.getAttribute<NoParams, VariableDeclaration<Integer>>("integer2"));
    value integer2Bound = integer2(noParamsInstance);
    assert(integer2Bound.get() == 1);
    integer2Bound.set(2);
    assert(integer2Bound.get() == 2);
    assert(noParamsInstance.integer2 == 2);

    assert(exists float2 = noParamsType.getAttribute<NoParams, VariableDeclaration<Float>>("float2"));
    value float2Bound = float2(noParamsInstance);
    assert(float2Bound.get() == 1.2);
    float2Bound.set(2.1);
    assert(float2Bound.get() == 2.1);
    assert(noParamsInstance.float2 == 2.1);
    
    assert(exists character2 = noParamsType.getAttribute<NoParams, VariableDeclaration<Character>>("character2"));
    value character2Bound = character2(noParamsInstance);
    assert(character2Bound.get() == 'a');
    character2Bound.set('b');
    assert(character2Bound.get() == 'b');
    assert(noParamsInstance.character2 == 'b');
    
    assert(exists boolean2 = noParamsType.getAttribute<NoParams, VariableDeclaration<Boolean>>("boolean2"));
    value boolean2Bound = boolean2(noParamsInstance);
    assert(boolean2Bound.get() == true);
    boolean2Bound.set(false);
    assert(boolean2Bound.get() == false);
    assert(noParamsInstance.boolean2 == false);
    
    assert(exists obj2 = noParamsType.getAttribute<NoParams, VariableDeclaration<Object>>("obj2"));
    value obj2Bound = obj2(noParamsInstance);
    assert(obj2Bound.get() == 2);
    obj2Bound.set(3);
    assert(obj2Bound.get() == 3);
    assert(noParamsInstance.obj2 == 3);
}

void checkMemberFunctionAnnotations(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    assert(is Class<String, []> stringType = type("foo"));
    
    assert(exists f1 = noParamsType.getFunction<NoParams, Function<NoParams, []>>("noParams"));
    Anything o1 = f1(noParamsInstance)();
    assert(is NoParams o1);
    
    assert(exists f2 = noParamsType.getFunction<NoParams, Function<NoParams, [String, Integer, Float, Character, Boolean, Object]>>("fixedParams"));
    Anything o3 = f2(noParamsInstance)("a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3);
    
    assert(exists f3 = noParamsType.getFunction<NoParams, Function<NoParams, [String, Integer]>>("typeParams", stringType));
    Anything o5 = f3(noParamsInstance)("a", 1);
    assert(is NoParams o5);

    assert(exists f4 = noParamsType.getFunction<NoParams, Function<String, []>>("getString"));
    assert(f4(noParamsInstance)() == "a");
    assert(exists f5 = noParamsType.getFunction<NoParams, Function<Integer, []>>("getInteger"));
    assert(f5(noParamsInstance)() == 1);
    assert(exists f6 = noParamsType.getFunction<NoParams, Function<Float, []>>("getFloat"));
    assert(f6(noParamsInstance)() == 1.2);
    assert(exists f7 = noParamsType.getFunction<NoParams, Function<Character, []>>("getCharacter"));
    assert(f7(noParamsInstance)() == 'a');
    assert(exists f8 = noParamsType.getFunction<NoParams, Function<Boolean, []>>("getBoolean"));
    assert(f8(noParamsInstance)() == true);
}

void checkHierarchyAnnotations(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;
    
    assert(noParamsDecl.name == "NoParams");
    
    value basicType = noParamsDecl.superclass;
    
    assert(exists basicType);
    
    assert(basicType.declaration.name == "Basic");

    value objectType = basicType.declaration.superclass;
    
    assert(exists objectType);
    
    assert(objectType.declaration.name == "Object");
    
    value anythingType = objectType.declaration.superclass;
    
    assert(exists anythingType);
    
    assert(anythingType.declaration.name == "Anything");

    assert(!anythingType.declaration.superclass exists);
}

void checkPackageAndModuleAnnotations(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    //
    // Package

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.name == "com.redhat.ceylon.compiler.java.test.metamodel");

    print(pkg.members<UntypedDeclaration>().size);
    assert(pkg.members<UntypedDeclaration>().size > 0);
    for(decl in pkg.members<UntypedDeclaration>()){
        print("decl: ``decl.name``");
    }

    //
    // Module

    value mod = pkg.container;

    assert(mod.name == "com.redhat.ceylon.compiler.java.test.metamodel");
    assert(mod.version == "123");
    
    assert(mod.members.size == 1);
    assert(exists modPackage = mod.members[0], modPackage === pkg);
}

void checkToplevelAttributeAnnotations() {
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.members<UntypedDeclaration>().find((UntypedDeclaration decl) => decl.name == "toplevelInteger") exists);

    assert(is ValueDeclaration toplevelIntegerDecl = pkg.getAttribute("toplevelInteger"));
    assert(is Value<Integer> toplevelIntegerValue = toplevelIntegerDecl.apply());
    assert(toplevelIntegerValue.get() == 1);

    assert(is ValueDeclaration toplevelStringDecl = pkg.getAttribute("toplevelString"));
    assert(is Value<String> toplevelStringValue = toplevelStringDecl.apply());
    assert(toplevelStringValue.get() == "a");

    assert(is ValueDeclaration toplevelFloatDecl = pkg.getAttribute("toplevelFloat"));
    assert(is Value<Float> toplevelFloatValue = toplevelFloatDecl.apply());
    assert(toplevelFloatValue.get() == 1.2);

    assert(is ValueDeclaration toplevelCharacterDecl = pkg.getAttribute("toplevelCharacter"));
    assert(is Value<Character> toplevelCharacterValue = toplevelCharacterDecl.apply());
    assert(toplevelCharacterValue.get() == 'a');

    assert(is ValueDeclaration toplevelBooleanDecl = pkg.getAttribute("toplevelBoolean"));
    assert(is Value<Boolean> toplevelBooleanValue = toplevelBooleanDecl.apply());
    assert(toplevelBooleanValue.get() == true);

    assert(is ValueDeclaration toplevelObjectDecl = pkg.getAttribute("toplevelObject"));
    assert(is Value<Object> toplevelObjectValue = toplevelObjectDecl.apply());
    assert(toplevelObjectValue.get() == 2);

    //
    // variables

    assert(is ValueDeclaration toplevelIntegerVariableDecl = pkg.getAttribute("toplevelInteger2"));
    assert(is VariableDeclaration<Integer> toplevelIntegerVariable = toplevelIntegerVariableDecl.apply());
    assert(toplevelIntegerVariable.get() == 1);
    toplevelIntegerVariable.set(2);
    assert(toplevelIntegerVariable.get() == 2);
    assert(toplevelInteger2 == 2);

    assert(is ValueDeclaration toplevelStringVariableDecl = pkg.getAttribute("toplevelString2"));
    assert(is VariableDeclaration<String> toplevelStringVariable = toplevelStringVariableDecl.apply());
    assert(toplevelStringVariable.get() == "a");
    toplevelStringVariable.set("b");
    assert(toplevelStringVariable.get() == "b");
    assert(toplevelString2 == "b");

    assert(is ValueDeclaration toplevelFloatVariableDecl = pkg.getAttribute("toplevelFloat2"));
    assert(is VariableDeclaration<Float> toplevelFloatVariable = toplevelFloatVariableDecl.apply());
    assert(toplevelFloatVariable.get() == 1.2);
    toplevelFloatVariable.set(2.0);
    assert(toplevelFloatVariable.get() == 2.0);
    assert(toplevelFloat2 == 2.0);

    assert(is ValueDeclaration toplevelCharacterVariableDecl = pkg.getAttribute("toplevelCharacter2"));
    assert(is VariableDeclaration<Character> toplevelCharacterVariable = toplevelCharacterVariableDecl.apply());
    assert(toplevelCharacterVariable.get() == 'a');
    toplevelCharacterVariable.set('b');
    assert(toplevelCharacterVariable.get() == 'b');
    assert(toplevelCharacter2 == 'b');

    assert(is ValueDeclaration toplevelBooleanVariableDecl = pkg.getAttribute("toplevelBoolean2"));
    assert(is VariableDeclaration<Boolean> toplevelBooleanVariable = toplevelBooleanVariableDecl.apply());
    assert(toplevelBooleanVariable.get() == true);
    toplevelBooleanVariable.set(false);
    assert(toplevelBooleanVariable.get() == false);
    assert(toplevelBoolean2 == false);

    assert(is ValueDeclaration toplevelObjectVariableDecl = pkg.getAttribute("toplevelObject2"));
    assert(is VariableDeclaration<Object> toplevelObjectVariable = toplevelObjectVariableDecl.apply());
    assert(toplevelObjectVariable.get() == 2);
    toplevelObjectVariable.set(3);
    assert(toplevelObjectVariable.get() == 3);
    assert(toplevelObject2 == 3);
}

*/

Package aPackage {
    value aClassType = type(AClass(""));
    assert(is Class<AClass,[String]> aClassType);
    value aClassDecl = aClassType.declaration;
    value pkg = aClassDecl.packageContainer;
    return pkg;
}

ClassOrInterface<T> annotationType<T>(T t) {
    assert(is ClassOrInterface<T> annoType = type(t));
    return annoType;
}

ClassOrInterface<Shared> sharedAnnotation = annotationType(Shared());
ClassOrInterface<Abstract> abstractAnnotation = annotationType(Abstract());
ClassOrInterface<Variable> variableAnnotation = annotationType(Variable());
ClassOrInterface<Doc> docAnnotation = annotationType(Doc(""));
ClassOrInterface<Seq> seqAnnotation = annotationType(Seq(""));

void checkToplevelValueAnnotations() {

    assert(is ValueDeclaration aToplevelValueDecl = aPackage.getAttribute("aToplevelValue"));
    assert(annotations(sharedAnnotation, aToplevelValueDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelValueDecl) exists);
    assert(exists doc = annotations(docAnnotation, aToplevelValueDecl), 
        doc.description == "aToplevelValue");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelValueDecl), 
        doc2.description == "aToplevelValue");
    
    variable value seqs = annotations(seqAnnotation, aToplevelValueDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], seq.string == "aToplevelFunction");
    assert(exists seq2 = seqs[0], seq2.string == "aToplevelGetterSetter");
    assert(sequencedAnnotations(seqAnnotation, aToplevelValueDecl).size == 2);

}

void annotationTests() {
    checkToplevelValueAnnotations();
    //checkClassAnnotations();
    //checkInterfaceAnnotations();
    // TODO checkMemberAttributeAnnotations();
    // TODO checkMemberFunctionAnnotations();
    // TODO checkHierarchyAnnotations();
    // TODO checkPackageAndModuleAnnotations();
    // TODO checkToplevelAttributeAnnotations();
}