import ceylon.language.metamodel { ... }
import ceylon.language.metamodel.declaration {
    AttributeDeclaration,
    FunctionDeclaration,
    Declaration
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
}

void checkMemberAttributes(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    assert(exists string = noParamsType.getAttribute<NoParams, Attribute<String>>("str"));
    assert(string(noParamsInstance).get() == "a");
    
    assert(exists integer = noParamsType.getAttribute<NoParams, Attribute<Integer>>("integer"));
    assert(integer(noParamsInstance).get() == 1);
    
    assert(exists float = noParamsType.getAttribute<NoParams, Attribute<Float>>("float"));
    assert(float(noParamsInstance).get() == 1.2);
    
    assert(exists character = noParamsType.getAttribute<NoParams, Attribute<Character>>("character"));
    assert(character(noParamsInstance).get() == 'a');
    
    assert(exists boolean = noParamsType.getAttribute<NoParams, Attribute<Boolean>>("boolean"));
    assert(boolean(noParamsInstance).get() == true);
    
    assert(exists obj = noParamsType.getAttribute<NoParams, Attribute<NoParams>>("obj"));
    assert(obj(noParamsInstance).get() === noParamsInstance);

    assert(exists string2 = noParamsType.getAttribute<NoParams, Variable<String>>("str2"));
    value string2Bound = string2(noParamsInstance);
    assert(string2Bound.get() == "a");
    string2Bound.set("b");
    assert(string2Bound.get() == "b");
    assert(noParamsInstance.str2 == "b");
    
    assert(exists integer2 = noParamsType.getAttribute<NoParams, Variable<Integer>>("integer2"));
    value integer2Bound = integer2(noParamsInstance);
    assert(integer2Bound.get() == 1);
    integer2Bound.set(2);
    assert(integer2Bound.get() == 2);
    assert(noParamsInstance.integer2 == 2);

    assert(exists float2 = noParamsType.getAttribute<NoParams, Variable<Float>>("float2"));
    value float2Bound = float2(noParamsInstance);
    assert(float2Bound.get() == 1.2);
    float2Bound.set(2.1);
    assert(float2Bound.get() == 2.1);
    assert(noParamsInstance.float2 == 2.1);
    
    assert(exists character2 = noParamsType.getAttribute<NoParams, Variable<Character>>("character2"));
    value character2Bound = character2(noParamsInstance);
    assert(character2Bound.get() == 'a');
    character2Bound.set('b');
    assert(character2Bound.get() == 'b');
    assert(noParamsInstance.character2 == 'b');
    
    assert(exists boolean2 = noParamsType.getAttribute<NoParams, Variable<Boolean>>("boolean2"));
    value boolean2Bound = boolean2(noParamsInstance);
    assert(boolean2Bound.get() == true);
    boolean2Bound.set(false);
    assert(boolean2Bound.get() == false);
    assert(noParamsInstance.boolean2 == false);
    
    assert(exists obj2 = noParamsType.getAttribute<NoParams, Variable<Object>>("obj2"));
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

void checkMemberTypes(){
    value containerClassInstance = ContainerClass();
    value containerClassType = type(containerClassInstance);
    assert(is Class<ContainerClass, []> containerClassType);

    assert(exists innerClassType = containerClassType.getClassOrInterface<ContainerClass, Class<ContainerClass.InnerClass, []>>("InnerClass"));
    Anything o1 = innerClassType(containerClassInstance)();
    assert(is ContainerClass.InnerClass o1);

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
}

void checkUntypedFunctionToAppliedFunction(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    value stringType = typeLiteral<String>();
        
    assert(exists appliedFunctionMember1 = noParamsType.getFunction<NoParams, Function<NoParams, [String, Integer]>>("typeParams", stringType));
    value appliedFunction1 = appliedFunctionMember1(noParamsInstance);
    Anything o1 = appliedFunction1("a", 1);
    assert(is NoParams o1);
    
    assert(is Function<NoParams, [String, Integer]> appliedFunction2 = appliedFunction1.declaration.bindAndApply(noParamsInstance, stringType));
    Anything o2 = appliedFunction2("a", 1);
    assert(is NoParams o2);
    
    value appliedFunctionMember3 = appliedFunction1.declaration.memberApply<NoParams, Function<NoParams, [String, Integer]>>(stringType);
    value appliedFunction3 = appliedFunctionMember3(noParamsInstance);
    Anything o3 = appliedFunction3("a", 1);
    assert(is NoParams o3);
}

void checkHierarchy(){
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

void checkPackageAndModule(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    //
    // Package

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.name == "com.redhat.ceylon.compiler.java.test.metamodel");

    print(pkg.members<Declaration>().size);
    assert(pkg.members<Declaration>().size > 0);
    for(decl in pkg.members<Declaration>()){
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

    assert(pkg.members<Declaration>().find((Declaration decl) => decl.name == "toplevelInteger") exists);

    assert(is AttributeDeclaration toplevelIntegerDecl = pkg.getAttribute("toplevelInteger"));
    assert(is Attribute<Integer> toplevelIntegerAttribute = toplevelIntegerDecl.apply());
    assert(toplevelIntegerAttribute.get() == 1);

    assert(is AttributeDeclaration toplevelStringDecl = pkg.getAttribute("toplevelString"));
    assert(is Attribute<String> toplevelStringAttribute = toplevelStringDecl.apply());
    assert(toplevelStringAttribute.get() == "a");

    assert(is AttributeDeclaration toplevelFloatDecl = pkg.getAttribute("toplevelFloat"));
    assert(is Attribute<Float> toplevelFloatAttribute = toplevelFloatDecl.apply());
    assert(toplevelFloatAttribute.get() == 1.2);

    assert(is AttributeDeclaration toplevelCharacterDecl = pkg.getAttribute("toplevelCharacter"));
    assert(is Attribute<Character> toplevelCharacterAttribute = toplevelCharacterDecl.apply());
    assert(toplevelCharacterAttribute.get() == 'a');

    assert(is AttributeDeclaration toplevelBooleanDecl = pkg.getAttribute("toplevelBoolean"));
    assert(is Attribute<Boolean> toplevelBooleanAttribute = toplevelBooleanDecl.apply());
    assert(toplevelBooleanAttribute.get() == true);

    assert(is AttributeDeclaration toplevelObjectDecl = pkg.getAttribute("toplevelObject"));
    assert(is Attribute<Object> toplevelObjectAttribute = toplevelObjectDecl.apply());
    assert(toplevelObjectAttribute.get() == 2);

    //
    // variables

    assert(is AttributeDeclaration toplevelIntegerVariableDecl = pkg.getAttribute("toplevelInteger2"));
    assert(is Variable<Integer> toplevelIntegerVariable = toplevelIntegerVariableDecl.apply());
    assert(toplevelIntegerVariable.get() == 1);
    toplevelIntegerVariable.set(2);
    assert(toplevelIntegerVariable.get() == 2);
    assert(toplevelInteger2 == 2);

    assert(is AttributeDeclaration toplevelStringVariableDecl = pkg.getAttribute("toplevelString2"));
    assert(is Variable<String> toplevelStringVariable = toplevelStringVariableDecl.apply());
    assert(toplevelStringVariable.get() == "a");
    toplevelStringVariable.set("b");
    assert(toplevelStringVariable.get() == "b");
    assert(toplevelString2 == "b");

    assert(is AttributeDeclaration toplevelFloatVariableDecl = pkg.getAttribute("toplevelFloat2"));
    assert(is Variable<Float> toplevelFloatVariable = toplevelFloatVariableDecl.apply());
    assert(toplevelFloatVariable.get() == 1.2);
    toplevelFloatVariable.set(2.0);
    assert(toplevelFloatVariable.get() == 2.0);
    assert(toplevelFloat2 == 2.0);

    assert(is AttributeDeclaration toplevelCharacterVariableDecl = pkg.getAttribute("toplevelCharacter2"));
    assert(is Variable<Character> toplevelCharacterVariable = toplevelCharacterVariableDecl.apply());
    assert(toplevelCharacterVariable.get() == 'a');
    toplevelCharacterVariable.set('b');
    assert(toplevelCharacterVariable.get() == 'b');
    assert(toplevelCharacter2 == 'b');

    assert(is AttributeDeclaration toplevelBooleanVariableDecl = pkg.getAttribute("toplevelBoolean2"));
    assert(is Variable<Boolean> toplevelBooleanVariable = toplevelBooleanVariableDecl.apply());
    assert(toplevelBooleanVariable.get() == true);
    toplevelBooleanVariable.set(false);
    assert(toplevelBooleanVariable.get() == false);
    assert(toplevelBoolean2 == false);

    assert(is AttributeDeclaration toplevelObjectVariableDecl = pkg.getAttribute("toplevelObject2"));
    assert(is Variable<Object> toplevelObjectVariable = toplevelObjectVariableDecl.apply());
    assert(toplevelObjectVariable.get() == 2);
    toplevelObjectVariable.set(3);
    assert(toplevelObjectVariable.get() == 3);
    assert(toplevelObject2 == 3);
}

void checkToplevelFunctions(){
    value noParamsInstance = NoParams();
    value noParamsAppliedType = type(noParamsInstance);
    assert(is Class<String, [String]> stringType = type("foo"));
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.packageContainer;

    assert(pkg.members<Declaration>().find((Declaration decl) => decl.name == "fixedParams") exists);

    assert(exists f2 = pkg.getFunction("fixedParams"));
    assert(is Function<Anything,[String, Integer, Float, Character, Boolean, Object]> f2a = f2.apply());
    f2a("a", 1, 1.2, 'a', true, noParamsInstance);
    
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

    // FIXME: test members() wrt filtering
    // FIXME: test untyped class to applied class
}

