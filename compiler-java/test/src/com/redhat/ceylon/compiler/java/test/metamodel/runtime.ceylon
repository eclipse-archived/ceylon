import ceylon.language.model { ... }
import ceylon.language.model.declaration {
    ValueDeclaration,
    FunctionDeclaration,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    OpenParameterisedType,
    OpenTypeVariable,
    OpenUnion,
    Declaration,
    TopLevelOrMemberDeclaration,
    AliasDeclaration
}

void checkConstructors(){
    // no parameters
    value noParamsType = type(NoParams());
    assert(is Class<NoParams, []> noParamsType);
    value noParamsClass = noParamsType.declaration;
    assert(noParamsClass.name == "NoParams");
    // not sure how to do this ATM
    //value noParamsType2 = noParamsClass.apply();
    //assert(is ClassType<NoParams, []> noParamsType2);
    Anything noParams = noParamsType();
    assert(is NoParams noParams);
    
    // static parameters
    value fixedParamsType = type(FixedParams("a", 1, 1.2, 'a', true, noParams));
    assert(is Class<FixedParams, [String, Integer, Float, Character, Boolean, Object]> fixedParamsType);
    value fixedParamsClass = fixedParamsType.declaration;
    assert(fixedParamsClass.name == "FixedParams");
    Anything fixedParams = fixedParamsType("a", 1, 1.2, 'a', true, noParams);
    assert(is FixedParams fixedParams);

    // typed parameters
    value typeParamsType = type(TypeParams("a", 1));
    assert(is Class<TypeParams<String>, [String, Integer]> typeParamsType);
    value typeParamsClass = typeParamsType.declaration;
    assert(typeParamsClass.name == "TypeParams");
    Anything typeParams = typeParamsType("a", 1);
    // this checks that we did pass the reified type arguments correctly
    assert(is TypeParams<String> typeParams);

    // defaulted parameters
    value defaultedParamsType = typeLiteral<DefaultedParams>();
    assert(is Class<DefaultedParams, [Integer=, String=, Boolean=]> defaultedParamsType);
    Anything defaultedParams1 = defaultedParamsType();
    assert(is DefaultedParams defaultedParams1);
    Anything defaultedParams2 = defaultedParamsType(0);
    assert(is DefaultedParams defaultedParams2);
    Anything defaultedParams3 = defaultedParamsType(1, "b");
    assert(is DefaultedParams defaultedParams3);
    Anything defaultedParams4 = defaultedParamsType(2, "b", false);
    assert(is DefaultedParams defaultedParams4);

    value defaultedParams2Type = typeLiteral<DefaultedParams2>();
    assert(is Class<DefaultedParams2, [Boolean, Integer=, Integer=, Integer=, Integer=]> defaultedParams2Type);
    defaultedParams2Type(false);
    defaultedParams2Type(true, -1, -2, -3, -4);
    
    // private class with private constructor
    value privateClassType = `PrivateClass`;
    value privateClassInstance = privateClassType();
    assert(privateClassInstance.string == "d");
}

void checkMemberAttributes(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    assert(exists string = noParamsType.getAttribute<NoParams, String>("str"));
    assert(string(noParamsInstance).get() == "a");
    
    assert(exists integer = noParamsType.getAttribute<NoParams, Integer>("integer"));
    assert(integer(noParamsInstance).get() == 1);
    
    assert(exists float = noParamsType.getAttribute<NoParams, Float>("float"));
    assert(float(noParamsInstance).get() == 1.2);
    
    assert(exists character = noParamsType.getAttribute<NoParams, Character>("character"));
    assert(character(noParamsInstance).get() == 'a');
    
    assert(exists boolean = noParamsType.getAttribute<NoParams, Boolean>("boolean"));
    assert(boolean(noParamsInstance).get() == true);
    
    assert(exists obj = noParamsType.getAttribute<NoParams, NoParams>("obj"));
    assert(obj(noParamsInstance).get() === noParamsInstance);

    assert(exists string2 = noParamsType.getVariableAttribute<NoParams, String>("str2"));
    value string2Bound = string2(noParamsInstance);
    assert(string2Bound.get() == "a");
    string2Bound.set("b");
    assert(string2Bound.get() == "b");
    assert(noParamsInstance.str2 == "b");
    
    assert(exists integer2 = noParamsType.getVariableAttribute<NoParams, Integer>("integer2"));
    value integer2Bound = integer2(noParamsInstance);
    assert(integer2Bound.get() == 1);
    integer2Bound.set(2);
    assert(integer2Bound.get() == 2);
    assert(noParamsInstance.integer2 == 2);

    assert(exists float2 = noParamsType.getVariableAttribute<NoParams, Float>("float2"));
    value float2Bound = float2(noParamsInstance);
    assert(float2Bound.get() == 1.2);
    float2Bound.set(2.1);
    assert(float2Bound.get() == 2.1);
    assert(noParamsInstance.float2 == 2.1);
    
    assert(exists character2 = noParamsType.getVariableAttribute<NoParams, Character>("character2"));
    value character2Bound = character2(noParamsInstance);
    assert(character2Bound.get() == 'a');
    character2Bound.set('b');
    assert(character2Bound.get() == 'b');
    assert(noParamsInstance.character2 == 'b');
    
    assert(exists boolean2 = noParamsType.getVariableAttribute<NoParams, Boolean>("boolean2"));
    value boolean2Bound = boolean2(noParamsInstance);
    assert(boolean2Bound.get() == true);
    boolean2Bound.set(false);
    assert(boolean2Bound.get() == false);
    assert(noParamsInstance.boolean2 == false);
    
    assert(exists obj2 = noParamsType.getVariableAttribute<NoParams, Object>("obj2"));
    value obj2Bound = obj2(noParamsInstance);
    assert(obj2Bound.get() == 2);
    obj2Bound.set(3);
    assert(obj2Bound.get() == 3);
    assert(noParamsInstance.obj2 == 3);
}

void checkMemberFunctions(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    assert(is Class<String, [String]> stringType = type("foo"));
    
    assert(exists f1 = noParamsType.getMethod<NoParams, NoParams, []>("noParams"));
    Anything o1 = f1(noParamsInstance)();
    assert(is NoParams o1);
    
    assert(exists f2 = noParamsType.getMethod<NoParams, NoParams, [String, Integer, Float, Character, Boolean, Object]>("fixedParams"));
    Anything o3 = f2(noParamsInstance)("a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3);
    
    assert(exists f3 = noParamsType.getMethod<NoParams, NoParams, [String, Integer]>("typeParams", stringType));
    Anything o5 = f3(noParamsInstance)("a", 1);
    assert(is NoParams o5);

    assert(exists f4 = noParamsType.getMethod<NoParams, String, []>("getString"));
    assert(f4(noParamsInstance)() == "a");
    assert(exists f5 = noParamsType.getMethod<NoParams, Integer, []>("getInteger"));
    assert(f5(noParamsInstance)() == 1);
    assert(exists f6 = noParamsType.getMethod<NoParams, Float, []>("getFloat"));
    assert(f6(noParamsInstance)() == 1.2);
    assert(exists f7 = noParamsType.getMethod<NoParams, Character, []>("getCharacter"));
    assert(f7(noParamsInstance)() == 'a');
    assert(exists f8 = noParamsType.getMethod<NoParams, Boolean, []>("getBoolean"));
    assert(f8(noParamsInstance)() == true);
}

void checkMemberTypes(){
    value containerClassInstance = ContainerClass();
    value containerClassType = type(containerClassInstance);
    assert(is Class<ContainerClass, []> containerClassType);

    assert(exists innerClassType = containerClassType.getClassOrInterface<ContainerClass, Class<ContainerClass.InnerClass, []>>("InnerClass"));
    Anything o1 = innerClassType(containerClassInstance)();
    assert(is ContainerClass.InnerClass o1);

    assert(exists innerDefaultedClassType = containerClassType.getClassOrInterface<ContainerClass, Class<ContainerClass.DefaultedParams, [Integer, Integer=]>>("DefaultedParams"));
    Anything o1_2 = innerDefaultedClassType(containerClassInstance)(0);
    assert(is ContainerClass.DefaultedParams o1_2);
    Anything o1_3 = innerDefaultedClassType(containerClassInstance)(2, 2);
    assert(is ContainerClass.DefaultedParams o1_3);

    value containerInterfaceImplInstance = ContainerInterfaceImpl();
    value containerInterfaceType = typeLiteral<ContainerInterface>();
    assert(is Interface<ContainerInterface> containerInterfaceType);

    assert(exists innerInterfaceClassType = containerInterfaceType.getClassOrInterface<ContainerInterface, Class<ContainerInterface.InnerClass, []>>("InnerClass"));
    Anything o2 = innerInterfaceClassType(containerInterfaceImplInstance)();
    assert(is ContainerInterface.InnerClass o2);
    
    value parameterisedClassType = typeLiteral<ParameterisedContainerClass<Integer>>();
    assert(is Class<ParameterisedContainerClass<Integer>,[]> parameterisedClassType);
    value parameterisedInnerClassMember = parameterisedClassType.getClassOrInterface<ParameterisedContainerClass<Integer>, Class<ParameterisedContainerClass<Integer>.InnerClass<String>,[]>>("InnerClass", typeLiteral<String>());
    assert(exists parameterisedInnerClassMember);
    Anything parameterisedInnerClassType = parameterisedInnerClassMember(ParameterisedContainerClass<Integer>());
    assert(is Class<ParameterisedContainerClass<Integer>.InnerClass<String>,[]> parameterisedInnerClassType);
    
    // private member type
    assert(exists privateMemberType = `PrivateClass`.getClassOrInterface<PrivateClass, Class<Object,[]>>("Inner"));
    value privateMember = privateMemberType(PrivateClass())();
    print(privateMember.string);
    assert(privateMember.string == "c");
}

void checkUntypedFunctionToAppliedFunction(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    value stringType = typeLiteral<String>();
        
    assert(exists appliedFunctionMember1 = noParamsType.getMethod<NoParams, NoParams, [String, Integer]>("typeParams", stringType));
    value appliedFunction1 = appliedFunctionMember1(noParamsInstance);
    Anything o1 = appliedFunction1("a", 1);
    assert(is NoParams o1);
    
    assert(is Function<NoParams, [String, Integer]> appliedFunction2 = appliedFunction1.declaration.bindAndApply(noParamsInstance, stringType));
    Anything o2 = appliedFunction2("a", 1);
    assert(is NoParams o2);
    
    value appliedFunctionMember3 = appliedFunction1.declaration.memberApply<NoParams, NoParams, [String, Integer]>(stringType);
    value appliedFunction3 = appliedFunctionMember3(noParamsInstance);
    Anything o3 = appliedFunction3("a", 1);
    assert(is NoParams o3);
}

void checkHierarchy(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;
    
    assert(noParamsDecl.name == "NoParams");
    
    value basicType = noParamsDecl.superclassDeclaration;
    
    assert(exists basicType);
    
    assert(basicType.declaration.name == "Basic");

    value objectType = basicType.declaration.superclassDeclaration;
    
    assert(exists objectType);
    
    assert(objectType.declaration.name == "Object");
    
    value anythingType = objectType.declaration.superclassDeclaration;
    
    assert(exists anythingType);
    
    assert(anythingType.declaration.name == "Anything");

    assert(!anythingType.declaration.superclassDeclaration exists);
}

void checkPackageAndModule(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    //
    // Package

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.name == "com.redhat.ceylon.compiler.java.test.metamodel");

    print(pkg.members<TopLevelOrMemberDeclaration>().size);
    assert(pkg.members<TopLevelOrMemberDeclaration>().size > 0);
    for(decl in pkg.members<TopLevelOrMemberDeclaration>()){
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

void checkToplevelAttributes(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.members<TopLevelOrMemberDeclaration>().find((Declaration decl) => decl.name == "toplevelInteger") exists);

    assert(is ValueDeclaration toplevelIntegerDecl = pkg.getValue("toplevelInteger"));
    assert(is Value<Integer> toplevelIntegerAttribute = toplevelIntegerDecl.apply());
    assert(toplevelIntegerAttribute.get() == 1);

    assert(is ValueDeclaration toplevelStringDecl = pkg.getValue("toplevelString"));
    assert(is Value<String> toplevelStringAttribute = toplevelStringDecl.apply());
    assert(toplevelStringAttribute.get() == "a");

    assert(is ValueDeclaration toplevelFloatDecl = pkg.getValue("toplevelFloat"));
    assert(is Value<Float> toplevelFloatAttribute = toplevelFloatDecl.apply());
    assert(toplevelFloatAttribute.get() == 1.2);

    assert(is ValueDeclaration toplevelCharacterDecl = pkg.getValue("toplevelCharacter"));
    assert(is Value<Character> toplevelCharacterAttribute = toplevelCharacterDecl.apply());
    assert(toplevelCharacterAttribute.get() == 'a');

    assert(is ValueDeclaration toplevelBooleanDecl = pkg.getValue("toplevelBoolean"));
    assert(is Value<Boolean> toplevelBooleanAttribute = toplevelBooleanDecl.apply());
    assert(toplevelBooleanAttribute.get() == true);

    assert(is ValueDeclaration toplevelObjectDecl = pkg.getValue("toplevelObject"));
    assert(is Value<Object> toplevelObjectAttribute = toplevelObjectDecl.apply());
    assert(toplevelObjectAttribute.get() == 2);

    //
    // variables

    assert(is ValueDeclaration toplevelIntegerVariableDecl = pkg.getValue("toplevelInteger2"));
    assert(is Variable<Integer> toplevelIntegerVariable = toplevelIntegerVariableDecl.apply());
    assert(toplevelIntegerVariable.get() == 1);
    toplevelIntegerVariable.set(2);
    assert(toplevelIntegerVariable.get() == 2);
    assert(toplevelInteger2 == 2);

    assert(is ValueDeclaration toplevelStringVariableDecl = pkg.getValue("toplevelString2"));
    assert(is Variable<String> toplevelStringVariable = toplevelStringVariableDecl.apply());
    assert(toplevelStringVariable.get() == "a");
    toplevelStringVariable.set("b");
    assert(toplevelStringVariable.get() == "b");
    assert(toplevelString2 == "b");

    assert(is ValueDeclaration toplevelFloatVariableDecl = pkg.getValue("toplevelFloat2"));
    assert(is Variable<Float> toplevelFloatVariable = toplevelFloatVariableDecl.apply());
    assert(toplevelFloatVariable.get() == 1.2);
    toplevelFloatVariable.set(2.0);
    assert(toplevelFloatVariable.get() == 2.0);
    assert(toplevelFloat2 == 2.0);

    assert(is ValueDeclaration toplevelCharacterVariableDecl = pkg.getValue("toplevelCharacter2"));
    assert(is Variable<Character> toplevelCharacterVariable = toplevelCharacterVariableDecl.apply());
    assert(toplevelCharacterVariable.get() == 'a');
    toplevelCharacterVariable.set('b');
    assert(toplevelCharacterVariable.get() == 'b');
    assert(toplevelCharacter2 == 'b');

    assert(is ValueDeclaration toplevelBooleanVariableDecl = pkg.getValue("toplevelBoolean2"));
    assert(is Variable<Boolean> toplevelBooleanVariable = toplevelBooleanVariableDecl.apply());
    assert(toplevelBooleanVariable.get() == true);
    toplevelBooleanVariable.set(false);
    assert(toplevelBooleanVariable.get() == false);
    assert(toplevelBoolean2 == false);

    assert(is ValueDeclaration toplevelObjectVariableDecl = pkg.getValue("toplevelObject2"));
    assert(is Variable<Object> toplevelObjectVariable = toplevelObjectVariableDecl.apply());
    assert(toplevelObjectVariable.get() == 2);
    toplevelObjectVariable.set(3);
    assert(toplevelObjectVariable.get() == 3);
    assert(toplevelObject2 == 3);
    
    // private attr
    value privateToplevelAttributeModel = `privateToplevelAttribute`;
    assert(privateToplevelAttributeModel.get() == "a");
}

void checkToplevelFunctions(){
    value noParamsInstance = NoParams();
    value noParamsAppliedType = type(noParamsInstance);
    assert(is Class<String, [String]> stringType = type("foo"));
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.members<TopLevelOrMemberDeclaration>().find((Declaration decl) => decl.name == "fixedParams") exists);

    assert(exists f2 = pkg.getFunction("fixedParams"));
    assert(is Function<Anything,[String, Integer, Float, Character, Boolean, Object, NoParams]> f2a = f2.apply());
    f2a("a", 1, 1.2, 'a', true, noParamsInstance, noParamsInstance);

    assert(exists f3 = pkg.getFunction("typeParams"));
    assert(is Function<String, [String, Integer]> f3a = f3.apply(stringType));
    assert(f3a("a", 1) == "a");

    assert(exists f4 = pkg.getFunction("getString"));
    assert(is Function<String, []> f4a = f4.apply());
    assert(f4a() == "a");

    assert(exists f5 = pkg.getFunction("getInteger"));
    assert(is Function<Integer, []> f5a = f5.apply());
    assert(f5a() == 1);

    assert(exists f6 = pkg.getFunction("getFloat"));
    assert(is Function<Float, []> f6a = f6.apply());
    assert(f6a() == 1.2);

    assert(exists f7 = pkg.getFunction("getCharacter"));
    assert(is Function<Character, []> f7a = f7.apply());
    assert(f7a() == 'a');

    assert(exists f8 = pkg.getFunction("getBoolean"));
    assert(is Function<Boolean, []> f8a = f8.apply());
    assert(f8a() == true);
    
    assert(exists f9 = pkg.getFunction("getObject"));
    assert(is Function<Object, []> f9a = f9.apply());
    assert(f9a() == 2);

    assert(exists f10 = pkg.getFunction("toplevelWithMultipleParameterLists"));
    assert(is Function<String(String),[Integer]> f10a = f10.apply());
    assert(f10a(1)("a") == "a1");

    // FIXME: MPL info is lost in the model loader
    //toplevelWithMultipleParameterLists{i = 1;}{s = "a"; };
    //assert(exists f10p1 = f10.parameters.first, "i" == f10p1.name);
    //print(f10.parameterLists.size);
    //assert(f10.parameterLists.size == 2);
    //assert(exists f10p12 = f10.parameterLists.first[0], "i" == f10p12.name);
    //assert(exists f10pl2 = f10.parameterLists[1], exists f10p2 = f10pl2.first, "s" == f10p2.name);

    assert(exists f11 = pkg.getFunction("defaultedParams"));
    assert(is Function<Anything,[Integer=, String=, Boolean=]> f11a = f11.apply());
    f11a();
    f11a(0);
    f11a(1, "b");
    f11a(2, "b", false);

    assert(exists f12 = pkg.getFunction("defaultedParams2"));
    assert(is Function<Anything,[Boolean, Integer=, Integer=, Integer=, Integer=]> f12a = f12.apply());
    f12a(false);
    f12a(true, -1, -2, -3, -4);
    
    // check its parameters metamodel
    assert(f12.parameterDeclarations.size == 5);
    assert(exists f12p0 = f12.parameterDeclarations[0], f12p0.name == "set", f12p0.defaulted == false);
    assert(exists f12p1 = f12.parameterDeclarations[1], f12p1.name == "a", f12p1.defaulted == true);
    assert(exists f12p2 = f12.parameterDeclarations[2], f12p2.name == "b", f12p2.defaulted == true);
    assert(exists f12p3 = f12.parameterDeclarations[3], f12p3.name == "c", f12p3.defaulted == true);
    assert(exists f12p4 = f12.parameterDeclarations[4], f12p4.name == "d", f12p4.defaulted == true);

    assert(exists f13 = pkg.getFunction("variadicParams"));
    assert(is Function<Anything,[Integer=, String*]> f13a = f13.apply());
    f13a();
    f13a(0);
    f13a(1, "a");
    f13a(2, "a", "a");
    // check its parameters metamodel
    assert(f13.parameterDeclarations.size == 2);
    assert(exists f13p0 = f13.parameterDeclarations[0], f13p0.name == "count", f13p0.defaulted == true, f13p0.variadic == false);
    assert(exists f13p1 = f13.parameterDeclarations[1], f13p1.name == "strings", f13p1.defaulted == false, f13p1.variadic == true);

    assert(exists f14 = pkg.getFunction("getAndTakeNoParams"));
    assert(is Function<NoParams, [NoParams]> f14a = f14.apply());
    assert(f14a(noParamsInstance) == noParamsInstance);

    // private function
    value privateToplevelFunctionModel = `privateToplevelFunction`;
    assert(privateToplevelFunctionModel() == "b");
}

void checkModules(){
    print("Loaded modules:");
    for(mod in modules.list){
        print(" Module: ``mod.name``/``mod.version``");
    }
    assert(modules.list.size >= 2);
    assert(exists languageModule = modules.find("ceylon.language", language.version));
    assert(languageModule.name == "ceylon.language", languageModule.version == language.version);
    assert(exists thisModule = modules.find("com.redhat.ceylon.compiler.java.test.metamodel", "123"));
    assert(thisModule.name == "com.redhat.ceylon.compiler.java.test.metamodel", thisModule.version == "123");
    assert(!modules.find("nonexistant", "123") exists);
    assert(!modules.find("ceylon.language", "0.1.1235") exists);
    assert(!modules.find("com.redhat.ceylon.compiler.java.test.metamodel", "54321") exists);
}

void checkObjectDeclaration(){
    // get it via its package
    value noParamsClass = type(NoParams());
    value pkg = noParamsClass.declaration.packageContainer;
    assert(exists topLevelObjectDeclarationAttribute = pkg.getValue("topLevelObjectDeclaration"));
    assert(is OpenParameterisedType<ClassDeclaration> topLevelObjectTypeDeclaration = topLevelObjectDeclarationAttribute.openType);
    value topLevelObjectClassDeclaration = topLevelObjectTypeDeclaration.declaration;
    assert(topLevelObjectClassDeclaration.name == "topLevelObjectDeclaration");
    assert(topLevelObjectClassDeclaration.anonymous);
    
    // get it via its type
    value topLevelObjectClass = type(topLevelObjectDeclaration);
    // make sure we can't instantiate it
    // FIXME: this may actually be wrong and we may want to be able to instantiate them
    assert(!is Class<Anything, []> topLevelObjectClass);
}

void checkAliases(){
    // get it via its package
    value pkg = `package com.redhat.ceylon.compiler.java.test.metamodel`;
    assert(exists aliasDeclaration = pkg.getAlias("TypeAliasToClass"));
    // check it
    assert(aliasDeclaration.name == "TypeAliasToClass");
    assert(aliasDeclaration.typeParameterDeclarations.size == 0);
    assert(is OpenParameterisedType<ClassDeclaration> aliasedType = aliasDeclaration.openType);
    assert(aliasedType.declaration.name == "NoParams");
    assert(aliasedType.typeArguments.size == 0);
    // check that getMember also works
    assert(exists aliasDeclaration2 = pkg.getMember<AliasDeclaration>("TypeAliasToClass"));
    assert(aliasDeclaration2.name == "TypeAliasToClass");
    // and members
    assert(pkg.members<AliasDeclaration>().size == 3);
    
    // get one with type parameters
    assert(exists aliasDeclarationTP = pkg.getAlias("TypeAliasToClassTP"));
    // check it
    assert(aliasDeclarationTP.name == "TypeAliasToClassTP");
    assert(aliasDeclarationTP.typeParameterDeclarations.size == 1);
    assert(aliasDeclarationTP.getTypeParameterDeclaration("J") exists);
    // make sure it points to TypeParams<T=J>
    assert(is OpenParameterisedType<ClassDeclaration> aliasedTypeTP = aliasDeclarationTP.openType);
    assert(aliasedTypeTP.declaration.name == "TypeParams");
    assert(aliasedTypeTP.typeArguments.size == 1);
    assert(exists aliasedDeclarationTPTypeParam = aliasedTypeTP.declaration.getTypeParameterDeclaration("T"));
    assert(is OpenTypeVariable aliasedTypeTPArg = aliasedTypeTP.typeArguments[aliasedDeclarationTPTypeParam]);
    assert(aliasedTypeTPArg.declaration.name == "J");

    // get one pointing to a union
    assert(exists aliasDeclarationUnion = pkg.getAlias("TypeAliasToUnion"));
    // check it
    assert(aliasDeclarationUnion.name == "TypeAliasToUnion");
    // make sure it points to Integer|String
    assert(is OpenUnion aliasedTypeUnion = aliasDeclarationUnion.openType);
    assert(aliasedTypeUnion.caseTypes.size == 2);
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> firstUnion = aliasedTypeUnion.caseTypes[0], 
            firstUnion.declaration.name == "Integer");
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> secondUnion = aliasedTypeUnion.caseTypes[1], 
            secondUnion.declaration.name == "String");
}

void checkTypeParameters(){
    value tpTest = `class TypeParameterTest`;
    assert(tpTest.typeParameterDeclarations.size == 3);
    
    assert(exists tp1 = tpTest.typeParameterDeclarations[0]);
    assert(tp1.name == "P");
    assert(tp1.invariant, !tp1.covariant, !tp1.contravariant);
    assert(!tp1.defaulted, !tp1.defaultValue exists);

    assert(tp1.enumeratedBounds.size == 2);
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> enumB1 = tp1.enumeratedBounds[0], enumB1.declaration.name == "TP1");
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> enumB2 = tp1.enumeratedBounds[1], enumB2.declaration.name == "TP2");

    assert(tp1.upperBounds.size == 2);
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> upperB1 = tp1.upperBounds[0], upperB1.declaration.name == "TPA");
    assert(is OpenParameterisedType<ClassOrInterfaceDeclaration> upperB2 = tp1.upperBounds[1], upperB2.declaration.name == "TPB");

    assert(exists tp2 = tpTest.typeParameterDeclarations[1]);
    assert(tp2.name == "T");
    assert(!tp2.invariant, !tp2.covariant, tp2.contravariant);
    assert(tp2.defaulted, is OpenTypeVariable tv2 = tp2.defaultValue, tv2.declaration.name == "P");

    assert(tp2.enumeratedBounds.size == 0);
    assert(tp2.upperBounds.size == 0);

    assert(exists tp3 = tpTest.typeParameterDeclarations[2]);
    assert(tp3.name == "V");
    assert(!tp3.invariant, tp3.covariant, !tp3.contravariant);
    assert(tp3.defaulted, is OpenParameterisedType<ClassOrInterfaceDeclaration> tv3 = tp3.defaultValue, tv3.declaration.name == "Integer");

    assert(tp3.enumeratedBounds.size == 0);
    assert(tp3.upperBounds.size == 0);
    
    value tpToplevelMethod = `function typeParameterTest`;
    assert(is OpenTypeVariable tpToplevelMethodTPType = tpToplevelMethod.openType);
    // this used to NPE
    assert(tpToplevelMethodTPType.declaration.name == "T");
}

void checkClassOrInterfaceCaseTypes(){
    value iwct = `interface InterfaceWithCaseTypes`;
    assert(iwct.caseTypes.size == 2);
    assert(is OpenParameterisedType<ClassDeclaration> iwcta = iwct.caseTypes[0],
           iwcta.declaration.name == "iwcta",
           iwcta.declaration.anonymous);
    assert(is OpenParameterisedType<ClassDeclaration> iwctb = iwct.caseTypes[1],
           iwctb.declaration.name == "iwctb",
           iwctb.declaration.anonymous);
    
    value iwst = `interface InterfaceWithSelfType`;
    assert(iwst.caseTypes.size == 1);
    assert(is OpenTypeVariable iwsta = iwst.caseTypes[0]);
    assert(iwsta.declaration.name == "T");
}

shared void checkModifiers(){
    value mods = `class Modifiers`;
    assert(mods.abstract, mods.shared, !mods.formal, !mods.actual, !mods.default);
    assert(exists inner = mods.getMemberDeclaration<ClassDeclaration>("NonShared"));
    assert(!inner.abstract, !inner.shared, !inner.formal, !inner.actual, !inner.default);
    assert(exists m = mods.getMemberDeclaration<FunctionDeclaration>("method"));
    assert(m.shared, m.formal, !m.actual, !m.default);
    assert(exists v = mods.getMemberDeclaration<ValueDeclaration>("string"));
    assert(v.shared, !v.formal, v.actual, v.default);
}

shared void runtime() {
    visitStringHierarchy();

    checkPackageAndModule();

    checkHierarchy();

    checkConstructors();    

    checkMemberFunctions();

    checkMemberAttributes();

    checkMemberTypes();

    checkToplevelAttributes();

    checkToplevelFunctions();

    checkUntypedFunctionToAppliedFunction();

    checkModules();

    checkObjectDeclaration();

    checkAliases();

    checkTypeParameters();

    checkClassOrInterfaceCaseTypes();

    checkModifiers();
    // FIXME: test members() wrt filtering
    // FIXME: test untyped class to applied class
}
