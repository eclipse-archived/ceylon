import ceylon.language.meta { ... }
import ceylon.language.meta.model { ... }
import ceylon.language.meta.declaration {
    ValueDeclaration,
    FunctionDeclaration,
    ClassDeclaration,
    InterfaceDeclaration,
    ClassOrInterfaceDeclaration,
    OpenClassOrInterfaceType,
    OpenClassType,
    OpenTypeVariable,
    OpenUnion,
    OpenIntersection,
    Declaration,
    NestableDeclaration,
    AliasDeclaration,
    covariant, contravariant, invariant, FunctionOrValueDeclaration,
    PackageDeclaration = Package,
    ModuleDeclaration = Module,
    TypeParameterDeclaration = TypeParameter
}

@test
shared void checkConstructors(){
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
    Anything fixedParams2 = fixedParamsType.apply("a", 1, 1.2, 'a', true, noParams);
    assert(is FixedParams fixedParams2);
    Anything fixedParams3 = fixedParamsType.declaration.instantiate([], "a", 1, 1.2, 'a', true, noParams);
    assert(is FixedParams fixedParams3);

    // check its parameter types
    assert(fixedParamsType.parameterTypes == [`String`, `Integer`, `Float`, `Character`, `Boolean`, `Object`]);

    // typed parameters
    value typeParamsType = type(TypeParams("a", 1));
    assert(is Class<TypeParams<String>, [String, Integer]> typeParamsType);
    value typeParamsClass = typeParamsType.declaration;
    assert(typeParamsClass.name == "TypeParams");
    Anything typeParams = typeParamsType("a", 1);
    // this checks that we did pass the reified type arguments correctly
    assert(is TypeParams<String> typeParams);
    Anything typeParams2 = typeParamsType.apply("a", 1);
    // this checks that we did pass the reified type arguments correctly
    assert(is TypeParams<String> typeParams2);
    Anything typeParams3 = typeParamsClass.instantiate([`String`], "a", 1);
    // this checks that we did pass the reified type arguments correctly
    assert(is TypeParams<String> typeParams3);

    // check its parameter types
    assert(typeParamsType.parameterTypes == [`String`, `Integer`]);

    // defaulted parameters
    value defaultedParamsType = typeLiteral<DefaultedParams>();
    assert(is Class<DefaultedParams, [Integer=, String=, Boolean=]> defaultedParamsType);
    Anything defaultedParams1 = defaultedParamsType();
    assert(is DefaultedParams defaultedParams1);
    Anything defaultedParams1b = defaultedParamsType.apply();
    assert(is DefaultedParams defaultedParams1b);
    Anything defaultedParams1c = defaultedParamsType.declaration.instantiate();
    assert(is DefaultedParams defaultedParams1c);
    Anything defaultedParams2 = defaultedParamsType(0);
    assert(is DefaultedParams defaultedParams2);
    Anything defaultedParams2b = defaultedParamsType.apply(0);
    assert(is DefaultedParams defaultedParams2b);
    Anything defaultedParams2c = defaultedParamsType.declaration.instantiate([], 0);
    assert(is DefaultedParams defaultedParams2c);
    Anything defaultedParams3 = defaultedParamsType(1, "b");
    assert(is DefaultedParams defaultedParams3);
    Anything defaultedParams3b = defaultedParamsType.apply(1, "b");
    assert(is DefaultedParams defaultedParams3b);
    Anything defaultedParams4 = defaultedParamsType(2, "b", false);
    assert(is DefaultedParams defaultedParams4);
    Anything defaultedParams4b = defaultedParamsType.apply(2, "b", false);
    assert(is DefaultedParams defaultedParams4b);

    value defaultedParams2Type = typeLiteral<DefaultedParams2>();
    assert(is Class<DefaultedParams2, [Boolean, Integer=, Integer=, Integer=, Integer=]> defaultedParams2Type);
    defaultedParams2Type(false);
    defaultedParams2Type.apply(false);
    defaultedParams2Type(true, -1, -2, -3, -4);
    defaultedParams2Type.apply(true, -1, -2, -3, -4);
    
    // private class with private constructor
    value privateClassType = `PrivateClass`;
    value privateClassInstance = privateClassType();
    assert(privateClassInstance.string == "d");
    value privateClassInstance2 = privateClassType.apply();
    assert(privateClassInstance2.string == "d");
    
    // constructor that throws
    try {
        `ThrowsMyException`(true);
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    try {
        `ThrowsMyException`.apply(true);
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    
    try {
        `ThrowsException`(true);
        assert(false);
    }catch(Exception x){
        assert(!x is MyException);
    }
    try {
        `ThrowsException`.apply(true);
        assert(false);
    }catch(Exception x){
        assert(!x is MyException);
    }
    
    try {
        `ThrowsMyError`(true);
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    try {
        `ThrowsMyError`.apply(true);
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    
    try {
        `ThrowsError`(true);
        assert(false);
    }catch(Error x){
        assert(!x is MyError);
    }
    try {
        `ThrowsError`.apply(true);
        assert(false);
    }catch(Error x){
        assert(!x is MyError);
    }
    
    value variadicClass = `VariadicParams`;
    variadicClass();
    variadicClass.apply();
    variadicClass(0);
    variadicClass.apply(0);
    variadicClass(1, "a");
    variadicClass.apply(1, "a");
    variadicClass(2, "a", "a");
    variadicClass.apply(2, "a", "a");
    unflatten(variadicClass)([2, "a", "a"]);
    
    try{
        `Modifiers`();
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }
    try{
        `Modifiers`.apply();
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }
}

@test
shared void checkMemberAttributes(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    assert(exists string = noParamsType.getAttribute<NoParams, String>("str"));
    assert(!string.declaration.variable);
    assert(string.declaration.name == "str");
    assert(string.declaration.qualifiedName == "metamodel::NoParams.str");
    assert(string(noParamsInstance).get() == "a");
    assert(string.bind(noParamsInstance).get() == "a");
    assert(exists stringValue = string.declaration.memberGet(noParamsInstance), stringValue == "a");
    // make sure immutable attributes have Set=Nothing
    assert(!is Attribute<NoParams, String, String> string);
    assert(!string(noParamsInstance) is Value<String, String>);
    assert(!string.bind(noParamsInstance) is Value<String, String>);
    
    assert(exists integer = noParamsType.getAttribute<NoParams, Integer>("integer"));
    assert(integer(noParamsInstance).get() == 1);
    assert(integer.bind(noParamsInstance).get() == 1);
    
    assert(exists float = noParamsType.getAttribute<NoParams, Float>("float"));
    assert(float(noParamsInstance).get() == 1.2);
    assert(float.bind(noParamsInstance).get() == 1.2);
    
    assert(exists character = noParamsType.getAttribute<NoParams, Character>("character"));
    assert(character(noParamsInstance).get() == 'a');
    assert(character.bind(noParamsInstance).get() == 'a');
    
    assert(exists boolean = noParamsType.getAttribute<NoParams, Boolean>("boolean"));
    assert(boolean(noParamsInstance).get() == true);
    assert(boolean.bind(noParamsInstance).get() == true);
    
    assert(exists obj = noParamsType.getAttribute<NoParams, NoParams>("obj"));
    assert(obj(noParamsInstance).get() === noParamsInstance);
    assert(obj.bind(noParamsInstance).get() === noParamsInstance);

    assert(is Attribute<NoParams, String, String> string2 = noParamsType.getAttribute<NoParams, String, String>("str2"));
    assert(string2.declaration.variable);
    value string2Bound = string2(noParamsInstance);
    assert(string2Bound.get() == "a");
    string2Bound.set("b");
    string2Bound.setIfAssignable("b");
    string2Bound.declaration.memberSet(noParamsInstance, "b");
    assert(string2Bound.get() == "b");
    assert(noParamsInstance.str2 == "b");
    
    assert(is Attribute<NoParams, Integer, Integer> integer2 = noParamsType.getAttribute<NoParams, Integer, Integer>("integer2"));
    value integer2Bound = integer2(noParamsInstance);
    assert(integer2Bound.get() == 1);
    integer2Bound.set(2);
    integer2Bound.setIfAssignable(2);
    assert(integer2Bound.get() == 2);
    assert(noParamsInstance.integer2 == 2);

    assert(is Attribute<NoParams, Float, Float> float2 = noParamsType.getAttribute<NoParams, Float, Float>("float2"));
    value float2Bound = float2(noParamsInstance);
    assert(float2Bound.get() == 1.2);
    float2Bound.set(2.1);
    float2Bound.setIfAssignable(2.1);
    assert(float2Bound.get() == 2.1);
    assert(noParamsInstance.float2 == 2.1);
    
    assert(is Attribute<NoParams, Character, Character> character2 = noParamsType.getAttribute<NoParams, Character, Character>("character2"));
    value character2Bound = character2(noParamsInstance);
    assert(character2Bound.get() == 'a');
    character2Bound.set('b');
    character2Bound.setIfAssignable('b');
    assert(character2Bound.get() == 'b');
    assert(noParamsInstance.character2 == 'b');
    
    assert(is Attribute<NoParams, Boolean, Boolean> boolean2 = noParamsType.getAttribute<NoParams, Boolean, Boolean>("boolean2"));
    value boolean2Bound = boolean2(noParamsInstance);
    assert(boolean2Bound.get() == true);
    boolean2Bound.set(false);
    boolean2Bound.setIfAssignable(false);
    assert(boolean2Bound.get() == false);
    assert(noParamsInstance.boolean2 == false);
    
    assert(is Attribute<NoParams, Object, Object> obj2 = noParamsType.getAttribute<NoParams, Object, Object>("obj2"));
    value obj2Bound = obj2(noParamsInstance);
    assert(obj2Bound.get() == 2);
    obj2Bound.set(3);
    obj2Bound.setIfAssignable(3);
    assert(obj2Bound.get() == 3);
    assert(noParamsInstance.obj2 == 3);

    // getter that throws
    ThrowsMyException tme = ThrowsMyException(false);
    try {
        `ThrowsMyException.getter`(tme).get();
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    try {
        `ThrowsMyException.getter`(tme).set(1);
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    try {
        `ThrowsMyException.getter`(tme).setIfAssignable(1);
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    try {
        `value ThrowsMyException.getter`.memberSet(tme, 1);
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    
    ThrowsMyError tmer = ThrowsMyError(false);
    try {
        `ThrowsMyError.getter`(tmer).get();
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    try {
        `ThrowsMyError.getter`(tmer).set(1);
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    try {
        `ThrowsMyError.getter`(tmer).setIfAssignable(1);
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    try {
        `value ThrowsMyError.getter`.memberSet(tmer, 1);
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
}

@test
shared void checkMemberFunctions(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    assert(is Class<String, [String]> stringType = type("foo"));
    
    assert(exists f1 = noParamsType.getMethod<NoParams, NoParams, []>("noParams"));
    assert(f1.declaration.name == "noParams");
    assert(f1.declaration.qualifiedName == "metamodel::NoParams.noParams");
    // check its parameter types
    assert(f1.parameterTypes == []);

    Anything o1 = f1(noParamsInstance)();
    assert(is NoParams o1);
    Anything o1b = f1.bind(noParamsInstance).apply();
    assert(is NoParams o1b);
    Anything o1c = f1.declaration.memberInvoke(noParamsInstance);
    assert(is NoParams o1c);
    
    assert(exists f2 = noParamsType.getMethod<NoParams, NoParams, [String, Integer, Float, Character, Boolean, Object]>("fixedParams"));
    // check its parameter types
    assert(f2.parameterTypes == [`String`, `Integer`, `Float`, `Character`, `Boolean`, `Object`]);

    Anything o3 = f2(noParamsInstance)("a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3);
    Anything o3b = f2.bind(noParamsInstance).apply("a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3b);
    Anything o3c = f2.declaration.memberInvoke(noParamsInstance, [], "a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3c);
    
    assert(exists f3 = noParamsType.getMethod<NoParams, NoParams, [String, Integer]>("typeParams", stringType));
    // check its parameter types
    assert(f3.parameterTypes == [`String`, `Integer`]);

    Anything o5 = f3(noParamsInstance)("a", 1);
    assert(is NoParams o5);
    Anything o5b = f3.bind(noParamsInstance).apply("a", 1);
    assert(is NoParams o5b);
    Anything o5c = f3.declaration.memberInvoke(noParamsInstance, [stringType], "a", 1);
    assert(is NoParams o5c);

    assert(exists f4 = noParamsType.getMethod<NoParams, String, []>("getString"));
    assert(f4(noParamsInstance)() == "a");
    assert(f4.bind(noParamsInstance).apply() == "a");
    assert(exists f5 = noParamsType.getMethod<NoParams, Integer, []>("getInteger"));
    assert(f5(noParamsInstance)() == 1);
    assert(f5.bind(noParamsInstance).apply() == 1);
    assert(exists f6 = noParamsType.getMethod<NoParams, Float, []>("getFloat"));
    assert(f6(noParamsInstance)() == 1.2);
    assert(f6.bind(noParamsInstance).apply() == 1.2);
    assert(exists f7 = noParamsType.getMethod<NoParams, Character, []>("getCharacter"));
    assert(f7(noParamsInstance)() == 'a');
    assert(f7.bind(noParamsInstance).apply() == 'a');
    assert(exists f8 = noParamsType.getMethod<NoParams, Boolean, []>("getBoolean"));
    assert(f8(noParamsInstance)() == true);
    assert(f8.bind(noParamsInstance).apply() == true);
    
    // method that throws
    ThrowsMyException tme = ThrowsMyException(false);
    try {
        `ThrowsMyException.method`(tme)();
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    try {
        `ThrowsMyException.method`.bind(tme).apply();
        assert(false);
    }catch(Exception x){
        assert(x is MyException);
    }
    // invalid container type
    try {
        `ThrowsMyException.method`.bind(1);
        assert(false);
    }catch(Exception x){
        assert(x is IncompatibleTypeException);
    }
    
    ThrowsMyError tmer = ThrowsMyError(false);
    try {
        `ThrowsMyError.method`(tmer)();
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    try {
        `ThrowsMyError.method`.bind(tmer).apply();
        assert(false);
    }catch(Error x){
        assert(x is MyError);
    }
    // invalid container type
    try {
        `ThrowsMyError.method`.bind(1);
        assert(false);
    }catch(Exception x){
        assert(x is IncompatibleTypeException);
    }
}

@test
shared void checkMemberTypes(){
    value containerClassInstance = ContainerClass();
    value containerClassType = type(containerClassInstance);
    assert(is Class<ContainerClass, []> containerClassType);

    assert(exists innerClassType = containerClassType.getClass<ContainerClass, ContainerClass.InnerClass, []>("InnerClass"));
    // check its parameter types
    assert(innerClassType.parameterTypes == []);

    Anything o1 = innerClassType(containerClassInstance)();
    assert(is ContainerClass.InnerClass o1);
    Anything o1b = innerClassType.bind(containerClassInstance).apply();
    assert(is ContainerClass.InnerClass o1b);
    Anything o1c = innerClassType.declaration.memberInstantiate(containerClassInstance);
    assert(is ContainerClass.InnerClass o1c);
    // make sure type doesn't throw at it
    assert(type(o1) == innerClassType);
    assert(`class ContainerClass.InnerClass`.name == "InnerClass");
    assert(`class ContainerClass.InnerClass`.qualifiedName == "metamodel::ContainerClass.InnerClass");

    assert(exists innerDefaultedClassType = containerClassType.getClass<ContainerClass, ContainerClass.DefaultedParams, [Integer, Integer=]>("DefaultedParams"));
    // check its parameter types
    assert(innerDefaultedClassType.parameterTypes == [`Integer`, `Integer`]);

    Anything o1_2 = innerDefaultedClassType(containerClassInstance)(0);
    assert(is ContainerClass.DefaultedParams o1_2);
    Anything o1_2b = innerDefaultedClassType.bind(containerClassInstance).apply(0);
    assert(is ContainerClass.DefaultedParams o1_2b);
    Anything o1_3 = innerDefaultedClassType(containerClassInstance)(2, 2);
    assert(is ContainerClass.DefaultedParams o1_3);
    Anything o1_3b = innerDefaultedClassType.bind(containerClassInstance).apply(2, 2);
    assert(is ContainerClass.DefaultedParams o1_3b);

    value containerInterfaceImplInstance = ContainerInterfaceImpl();
    value containerInterfaceType = typeLiteral<ContainerInterface>();
    assert(is Interface<ContainerInterface> containerInterfaceType);

    assert(exists innerInterfaceClassType = containerInterfaceType.getClass<ContainerInterface, ContainerInterface.InnerClass, []>("InnerClass"));
    Anything o2 = innerInterfaceClassType(containerInterfaceImplInstance)();
    assert(is ContainerInterface.InnerClass o2);
    Anything o2b = innerInterfaceClassType.bind(containerInterfaceImplInstance).apply();
    assert(is ContainerInterface.InnerClass o2b);
    
    value parameterisedClassType = typeLiteral<ParameterisedContainerClass<Integer>>();
    assert(is Class<ParameterisedContainerClass<Integer>,[]> parameterisedClassType);
    value parameterisedInnerClassMember = parameterisedClassType.getClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>,[]>("InnerClass", typeLiteral<String>());
    assert(exists parameterisedInnerClassMember);
    Anything parameterisedInnerClassType = parameterisedInnerClassMember(ParameterisedContainerClass<Integer>());
    assert(is Class<ParameterisedContainerClass<Integer>.InnerClass<String>,[]> parameterisedInnerClassType);

    // inner interface
    Anything innerInterfaceModel = containerClassType.getInterface<ContainerClass, ContainerClass.InnerInterface>("InnerInterface");
    assert(is MemberInterface<ContainerClass, ContainerClass.InnerInterface> innerInterfaceModel);
    
    try{
        // get a class as an interface
        containerClassType.getInterface<ContainerClass, ContainerClass.InnerClass>("InnerClass");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }

    try{
        // get an interface as a class
        containerClassType.getClass<ContainerClass, ContainerClass.InnerInterface, Nothing>("InnerInterface");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    
    // private member type
    assert(exists privateMemberType = `PrivateClass`.getDeclaredClassOrInterface<PrivateClass, Class<Object,[]>>("Inner"));
    value privateMember = privateMemberType(PrivateClass())();
    assert(privateMember.string == "c");
    assert(exists privateInheritedMember1 = `PrivateSubclass`.getClassOrInterface<PrivateSubclass, Class<Object,[]>>("OtherInner"));
    assert(!`PrivateSubclass`.getDeclaredClass<PrivateSubclass, PrivateClass.OtherInner,[]>("OtherInner") exists);
    
    // make super extendedType and satisfiedTypes work with members
    assert(exists isbExtendedType = `ContainerClass.InnerSubClass`.extendedType, isbExtendedType == `ContainerClass.InnerClass`);
    assert(exists isbSatisfiedType = `ContainerClass.InnerSubClass`.satisfiedTypes[0], isbSatisfiedType == `ContainerClass.InnerInterface`);
}

@test
shared void checkUntypedFunctionToAppliedFunction(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is Class<NoParams, []> noParamsType);
    
    value stringType = typeLiteral<String>();
        
    assert(exists appliedFunctionMember1 = noParamsType.getMethod<NoParams, NoParams, [String, Integer]>("typeParams", stringType));
    value appliedFunction1 = appliedFunctionMember1(noParamsInstance);
    Anything o1 = appliedFunction1("a", 1);
    assert(is NoParams o1);
    
    value appliedFunctionMember3 = appliedFunction1.declaration.memberApply<NoParams, NoParams, [String, Integer]>(noParamsType, stringType);
    value appliedFunction3 = appliedFunctionMember3(noParamsInstance);
    Anything o3 = appliedFunction3("a", 1);
    assert(is NoParams o3);
}

@test
shared void checkHierarchy(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;
    
    assert(noParamsDecl.name == "NoParams");
    assert(noParamsDecl.qualifiedName == "metamodel::NoParams");
    
    value basicType = noParamsDecl.extendedType;
    
    assert(exists basicType);
    
    assert(basicType.declaration.name == "Basic");

    value objectType = basicType.declaration.extendedType;
    
    assert(exists objectType);
    
    assert(objectType.declaration.name == "Object");
    
    value anythingType = objectType.declaration.extendedType;
    
    assert(exists anythingType);
    
    assert(anythingType.declaration.name == "Anything");

    assert(!anythingType.declaration.extendedType exists);
    
    assert(exists throwableSup=`class Throwable`.extendedType,
            throwableSup.declaration.name == "Basic");
    assert(`class Throwable`.caseTypes.size == 2);
    assert(`class Throwable`.abstract);
    assert(is OpenClassType[] throwableCases=`class Throwable`.caseTypes);
    assert(`class Exception` in throwableCases*.declaration); 
    assert(exists exceptionSup=`class Exception`.extendedType,
            exceptionSup.declaration.name== "Throwable");
    assert(exists myExceptionSup=`class MyException`.extendedType,
            myExceptionSup.declaration.name == "Exception");
    
    assert(`class Error` in throwableCases*.declaration);
    assert(exists errorSup=`class Error`.extendedType,
            errorSup.declaration.name == "Throwable");
    assert(exists myErrorSup=`class MyError`.extendedType,
            myErrorSup.declaration.name == "Error");
}

@test
shared void checkPackageAndModule(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    //
    // Package

    value pkg = noParamsDecl.containingPackage;

    assert(pkg.name == "metamodel");
    assert(pkg.qualifiedName == "metamodel");

    assert(pkg.members<NestableDeclaration>().size > 0);
    assert(pkg.members<FunctionOrValueDeclaration>().size > 0);

    //
    // Module

    value mod = pkg.container;

    assert(mod.name == "metamodel");
    assert(mod.qualifiedName == "metamodel");
    assert(mod.version == "0.1");
    
    assert(mod.members.size == 1);
    assert(exists modPackage = mod.members[0], modPackage === pkg);
}

@test
shared void checkToplevelAttributes(){
    value noParamsAppliedType = type(NoParams());
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.containingPackage;

    assert(pkg.members<NestableDeclaration>().find((Declaration decl) => decl.name == "toplevelInteger") exists);

    assert(is ValueDeclaration toplevelIntegerDecl = pkg.getValue("toplevelInteger"));
    Value<Integer> toplevelIntegerAttribute = toplevelIntegerDecl.apply<Integer>();
    assert(toplevelIntegerAttribute.get() == 1);
    assert(exists toplevelIntegerValue = toplevelIntegerDecl.get(), toplevelIntegerValue == 1);
    // make sure immutable values have Set=Nothing
    assert(!is Value<Integer, Integer> toplevelIntegerAttribute);

    assert(is ValueDeclaration toplevelStringDecl = pkg.getValue("toplevelString"));
    Value<String> toplevelStringAttribute = toplevelStringDecl.apply<String>();
    assert(toplevelStringAttribute.get() == "a");

    assert(is ValueDeclaration toplevelFloatDecl = pkg.getValue("toplevelFloat"));
    Value<Float> toplevelFloatAttribute = toplevelFloatDecl.apply<Float>();
    assert(toplevelFloatAttribute.get() == 1.2);

    assert(is ValueDeclaration toplevelCharacterDecl = pkg.getValue("toplevelCharacter"));
    Value<Character> toplevelCharacterAttribute = toplevelCharacterDecl.apply<Character>();
    assert(toplevelCharacterAttribute.get() == 'a');

    assert(is ValueDeclaration toplevelBooleanDecl = pkg.getValue("toplevelBoolean"));
    Value<Boolean> toplevelBooleanAttribute = toplevelBooleanDecl.apply<Boolean>();
    assert(toplevelBooleanAttribute.get() == true);

    assert(is ValueDeclaration toplevelObjectDecl = pkg.getValue("toplevelObject"));
    Value<Object> toplevelObjectAttribute = toplevelObjectDecl.apply<Object>();
    assert(toplevelObjectAttribute.get() == 2);
    
    // immutable attribute
    try{
        toplevelFloatAttribute.setIfAssignable(1.2);
        assert(false);
    }catch(Exception x){
        assert(is MutationException x);
    }

    //
    // variables

    assert(is ValueDeclaration toplevelIntegerVariableDecl = pkg.getValue("toplevelInteger2"));
    Value<Integer,Integer> toplevelIntegerVariable = toplevelIntegerVariableDecl.apply<Integer,Integer>();
    assert(toplevelIntegerVariable.get() == 1);
    toplevelIntegerVariable.set(2);
    toplevelIntegerVariable.setIfAssignable(2);
    assert(toplevelIntegerVariable.get() == 2);
    assert(toplevelInteger2 == 2);

    assert(is ValueDeclaration toplevelStringVariableDecl = pkg.getValue("toplevelString2"));
    Value<String,String> toplevelStringVariable = toplevelStringVariableDecl.apply<String,String>();
    assert(toplevelStringVariable.get() == "a");
    toplevelStringVariable.set("b");
    toplevelStringVariable.setIfAssignable("b");
    toplevelStringVariableDecl.set("b");
    assert(toplevelStringVariable.get() == "b");
    assert(toplevelString2 == "b");

    assert(is ValueDeclaration toplevelFloatVariableDecl = pkg.getValue("toplevelFloat2"));
    Value<Float,Float> toplevelFloatVariable = toplevelFloatVariableDecl.apply<Float,Float>();
    assert(toplevelFloatVariable.get() == 1.2);
    toplevelFloatVariable.set(2.0);
    toplevelFloatVariable.setIfAssignable(2.0);
    assert(toplevelFloatVariable.get() == 2.0);
    assert(toplevelFloat2 == 2.0);

    assert(is ValueDeclaration toplevelCharacterVariableDecl = pkg.getValue("toplevelCharacter2"));
    Value<Character,Character> toplevelCharacterVariable = toplevelCharacterVariableDecl.apply<Character,Character>();
    assert(toplevelCharacterVariable.get() == 'a');
    toplevelCharacterVariable.set('b');
    toplevelCharacterVariable.setIfAssignable('b');
    assert(toplevelCharacterVariable.get() == 'b');
    assert(toplevelCharacter2 == 'b');

    assert(is ValueDeclaration toplevelBooleanVariableDecl = pkg.getValue("toplevelBoolean2"));
    Value<Boolean,Boolean> toplevelBooleanVariable = toplevelBooleanVariableDecl.apply<Boolean,Boolean>();
    assert(toplevelBooleanVariable.get() == true);
    toplevelBooleanVariable.set(false);
    toplevelBooleanVariable.setIfAssignable(false);
    assert(toplevelBooleanVariable.get() == false);
    assert(toplevelBoolean2 == false);

    assert(is ValueDeclaration toplevelObjectVariableDecl = pkg.getValue("toplevelObject2"));
    Value<Object,Object> toplevelObjectVariable = toplevelObjectVariableDecl.apply<Object,Object>();
    assert(toplevelObjectVariable.get() == 2);
    toplevelObjectVariable.set(3);
    assert(toplevelObjectVariable.get() == 3);
    assert(toplevelObject2 == 3);
    
    // private attr
    value privateToplevelAttributeModel = `privateToplevelAttribute`;
    assert(privateToplevelAttributeModel.get() == "a");

    // invalid type
    try{
        toplevelFloatVariable.setIfAssignable(true);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
}

@test
shared void checkToplevelFunctions(){
    value noParamsInstance = NoParams();
    value noParamsAppliedType = type(noParamsInstance);
    assert(is Class<String, [String]> stringType = type("foo"));
    
    assert(is Class<Anything,[]> noParamsAppliedType);
    
    value noParamsDecl = noParamsAppliedType.declaration;

    value pkg = noParamsDecl.containingPackage;

    assert(pkg.members<NestableDeclaration>().find((Declaration decl) => decl.name == "fixedParams") exists);

    assert(exists f2 = pkg.getFunction("fixedParams"));
    assert(is Function<Anything,[String, Integer, Float, Character, Boolean, Object, NoParams]> f2a = f2.apply<Anything,Nothing>());

    // check its parameter types
    assert(f2a.parameterTypes == [`String`, `Integer`, `Float`, `Character`, `Boolean`, `Object`, `NoParams`]);

    f2a("a", 1, 1.2, 'a', true, noParamsInstance, noParamsInstance);
    f2a.apply("a", 1, 1.2, 'a', true, noParamsInstance, noParamsInstance);
    f2a.declaration.invoke([], "a", 1, 1.2, 'a', true, noParamsInstance, noParamsInstance);

    assert(exists f3 = pkg.getFunction("typeParams"));
    assert(is Function<String, [String, Integer]> f3a = f3.apply<Anything,Nothing>(stringType));

    // check its parameter types
    assert(f3a.parameterTypes == [`String`, `Integer`]);

    assert(f3a("a", 1) == "a");
    assert(f3a.apply("a", 1) == "a");
    assert(exists f3aret = f3a.declaration.invoke([stringType], "a", 1), f3aret == "a");

    assert(exists f4 = pkg.getFunction("getString"));
    assert(is Function<String, []> f4a = f4.apply<Anything,Nothing>());
    assert(f4a() == "a");
    assert(f4a.apply() == "a");

    try{
        // too many params
        f4a.apply(1);
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }

    assert(exists f5 = pkg.getFunction("getInteger"));
    assert(is Function<Integer, []> f5a = f5.apply<Anything,Nothing>());
    assert(f5a() == 1);
    assert(f5a.apply() == 1);

    assert(exists f6 = pkg.getFunction("getFloat"));
    assert(is Function<Float, []> f6a = f6.apply<Anything,Nothing>());
    assert(f6a() == 1.2);
    assert(f6a.apply() == 1.2);

    assert(exists f7 = pkg.getFunction("getCharacter"));
    assert(is Function<Character, []> f7a = f7.apply<Anything,Nothing>());
    assert(f7a() == 'a');
    assert(f7a.apply() == 'a');

    assert(exists f8 = pkg.getFunction("getBoolean"));
    assert(is Function<Boolean, []> f8a = f8.apply<Anything,Nothing>());
    assert(f8a() == true);
    assert(f8a.apply() == true);
    
    assert(exists f9 = pkg.getFunction("getObject"));
    assert(is Function<Object, []> f9a = f9.apply<Anything,Nothing>());
    assert(f9a() == 2);
    assert(f9a.apply() == 2);

    assert(exists f10 = pkg.getFunction("toplevelWithMultipleParameterLists"));
    Function<String(String),[Integer]> f10a = f10.apply<String(String),[Integer]>();
    assert(f10a(1)("a") == "a1");
    assert(f10a.apply(1)("a") == "a1");

    // FIXME: MPL info is lost in the model loader
    //toplevelWithMultipleParameterLists{i = 1;}{s = "a"; };
    //assert(exists f10p1 = f10.parameters.first, "i" == f10p1.name);
    //print(f10.parameterLists.size);
    //assert(f10.parameterLists.size == 2);
    //assert(exists f10p12 = f10.parameterLists.first[0], "i" == f10p12.name);
    //assert(exists f10pl2 = f10.parameterLists[1], exists f10p2 = f10pl2.first, "s" == f10p2.name);

    assert(exists f11 = pkg.getFunction("defaultedParams"));
    assert(is Function<Anything,[Integer=, String=, Boolean=]> f11a = f11.apply<Anything,Nothing>());
    f11a();
    f11a.apply();
    f11a(0);
    f11a.apply(0);
    f11a(1, "b");
    f11a.apply(1, "b");
    f11a(2, "b", false);
    f11a.apply(2, "b", false);

    assert(exists f12 = pkg.getFunction("defaultedParams2"));
    assert(f12.name == "defaultedParams2");
    assert(f12.qualifiedName == "metamodel::defaultedParams2");
    assert(is Function<Anything,[Boolean, Integer=, Integer=, Integer=, Integer=]> f12a = f12.apply<Anything,Nothing>());
    f12a(false);
    f12a.apply(false);
    f12a(true, -1, -2, -3, -4);
    f12a.apply(true, -1, -2, -3, -4);

    try{
        // invalid type
        f12a.apply("a");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        // not enough params
        f12a.apply();
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }
    try{
        // too many params
        f12a.apply(true, -1, -2, -3, -4, -5);
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }
    
    // check its parameters metamodel
    assert(f12.parameterDeclarations.size == 5);
    assert(exists f12p0 = f12.parameterDeclarations[0], f12p0.name == "set", f12p0.defaulted == false, f12p0.parameter);
    assert(f12p0.qualifiedName == "metamodel::defaultedParams2.set");
    assert(exists f12p1 = f12.parameterDeclarations[1], f12p1.name == "a", f12p1.defaulted == true);
    assert(exists f12p2 = f12.parameterDeclarations[2], f12p2.name == "b", f12p2.defaulted == true);
    assert(exists f12p3 = f12.parameterDeclarations[3], f12p3.name == "c", f12p3.defaulted == true);
    assert(exists f12p4 = f12.parameterDeclarations[4], f12p4.name == "d", f12p4.defaulted == true);

    assert(exists f13 = pkg.getFunction("variadicParams"));
    assert(is Function<Anything,[Integer=, String*]> f13a = f13.apply<Anything,Nothing>());
    f13a();
    f13a.apply();
    f13a(0);
    f13a.apply(0);
    f13a(1, "a");
    f13a.apply(1, "a");
    f13a(2, "a", "a");
    f13a.apply(2, "a", "a");
    unflatten(f13a)([2, "a", "a"]);
    
    // check its parameters metamodel
    assert(f13.parameterDeclarations.size == 2);
    assert(exists f13p0 = f13.parameterDeclarations[0], f13p0.name == "count", f13p0.defaulted == true, f13p0.variadic == false);
    assert(exists f13p1 = f13.parameterDeclarations[1], f13p1.name == "strings", f13p1.defaulted == false, f13p1.variadic == true);

    assert(exists f14 = pkg.getFunction("getAndTakeNoParams"));
    assert(is Function<NoParams, [NoParams]> f14a = f14.apply<Anything,Nothing>());
    assert(f14a(noParamsInstance) == noParamsInstance);
    assert(f14a.apply(noParamsInstance) == noParamsInstance);

    // private function
    value privateToplevelFunctionModel = `privateToplevelFunction`;
    assert(privateToplevelFunctionModel() == "b");
    assert(privateToplevelFunctionModel.apply() == "b");
}

@test
shared void checkModules(){
    assert(modules.list.size >= 2);
    assert(exists languageModule = modules.find("ceylon.language", language.version));
    assert(languageModule.name == "ceylon.language", languageModule.version == language.version);
    assert(exists thisModule = modules.find("metamodel", "0.1"));
    assert(thisModule.name == "metamodel", thisModule.version == "0.1");
    assert(!modules.find("nonexistant", "123") exists);
    assert(!modules.find("ceylon.language", "0.1.1235") exists);
    assert(!modules.find("metamodel", "54321") exists);
    
    assert(!thisModule.findPackage("ceylon.language") exists);
    assert(exists p1 = thisModule.findImportedPackage("ceylon.language"), p1 == `package ceylon.language`);
    assert(exists p2 = thisModule.findPackage("metamodel"), p2 == `package metamodel`);
    assert(exists p3 = thisModule.findImportedPackage("metamodel"), p3 == `package metamodel`);
}

@test
shared void checkObjectDeclaration(){
    // get it via its package
    value noParamsClass = type(NoParams());
    value pkg = noParamsClass.declaration.containingPackage;
    assert(exists topLevelObjectDeclarationAttribute = pkg.getValue("topLevelObjectDeclaration"));
    assert(is OpenClassOrInterfaceType topLevelObjectTypeDeclaration = topLevelObjectDeclarationAttribute.openType);
    assert(is ClassDeclaration topLevelObjectClassDeclaration = topLevelObjectTypeDeclaration.declaration);
    assert(topLevelObjectClassDeclaration.name == "topLevelObjectDeclaration");
    assert(topLevelObjectClassDeclaration.anonymous);
    
    // get it via its type
    value topLevelObjectClass = type(topLevelObjectDeclaration);
    // make sure we can't instantiate it
    // FIXME: this may actually be wrong and we may want to be able to instantiate them
    assert(!is Class<Anything, []> topLevelObjectClass);
    assert(is Class<Anything, Nothing> topLevelObjectClass);
    
    assert(topLevelObjectClass.parameterTypes == []);
    
    // make sure we get a proper exception when trying to instantiate it
    try{
        topLevelObjectClass.apply();
        assert(false);
    }catch(Exception x){
        assert(is InvocationException x);
    }
}

@test
shared void checkAliases(){
    // get it via its package
    value pkg = `package metamodel`;
    assert(exists aliasDeclaration = pkg.getAlias("TypeAliasToClass"));
    // check it
    assert(aliasDeclaration.name == "TypeAliasToClass");
    assert(aliasDeclaration.extendedType == `class NoParams`.openType);
    assert(aliasDeclaration.typeParameterDeclarations.size == 0);
    assert(is OpenClassOrInterfaceType aliasedType = aliasDeclaration.openType);
    assert(aliasedType.declaration.name == "NoParams");
    assert(aliasedType.typeArguments.size == 0);
    // check that getMember also works
    assert(exists aliasDeclaration2 = pkg.getMember<AliasDeclaration>("TypeAliasToClass"));
    assert(aliasDeclaration2.name == "TypeAliasToClass");
    // and members
    assert(pkg.members<AliasDeclaration>().size == 4);
    
    // get one with type parameters
    assert(exists aliasDeclarationTP = pkg.getAlias("TypeAliasToClassTP"));
    // check it
    assert(aliasDeclarationTP.name == "TypeAliasToClassTP");
    assert(aliasDeclarationTP.typeParameterDeclarations.size == 1);
    assert(aliasDeclarationTP.getTypeParameterDeclaration("J") exists);
    // make sure it points to TypeParams<T=J>
    assert(is OpenClassOrInterfaceType aliasedTypeTP = aliasDeclarationTP.openType);
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
    assert(is OpenClassOrInterfaceType firstUnion = aliasedTypeUnion.caseTypes[0], 
            firstUnion.declaration.name == "Integer");
    assert(is OpenClassOrInterfaceType secondUnion = aliasedTypeUnion.caseTypes[1], 
            secondUnion.declaration.name == "String");
}

@test
shared void checkTypeParameters(){
    value tpTest = `class TypeParameterTest`;
    assert(tpTest.typeParameterDeclarations.size == 3);
    
    assert(exists tp1 = tpTest.typeParameterDeclarations[0]);
    assert(tp1.name == "P");
    assert(tp1.qualifiedName == "metamodel::TypeParameterTest.P");
    assert(tp1.variance == invariant);
    assert(!tp1.defaulted, !tp1.defaultTypeArgument exists);

    assert(tp1.caseTypes.size == 2);
    assert(is OpenClassOrInterfaceType enumB1 = tp1.caseTypes[0], enumB1.declaration.name == "TP1");
    assert(is OpenClassOrInterfaceType enumB2 = tp1.caseTypes[1], enumB2.declaration.name == "TP2");

    assert(tp1.satisfiedTypes.size == 2);
    assert(is OpenClassOrInterfaceType upperB1 = tp1.satisfiedTypes[0], upperB1.declaration.name == "TPA");
    assert(is OpenClassOrInterfaceType upperB2 = tp1.satisfiedTypes[1], upperB2.declaration.name == "TPB");

    assert(exists tp2 = tpTest.typeParameterDeclarations[1]);
    assert(tp2.name == "T");
    assert(tp2.variance == contravariant);
    assert(tp2.defaulted, is OpenTypeVariable tv2 = tp2.defaultTypeArgument, tv2.declaration.name == "P");

    assert(tp2.caseTypes.size == 0);
    assert(tp2.satisfiedTypes.size == 0);

    assert(exists tp3 = tpTest.typeParameterDeclarations[2]);
    assert(tp3.name == "V");
    assert(tp3.variance == covariant);
    assert(tp3.defaulted, is OpenClassOrInterfaceType tv3 = tp3.defaultTypeArgument, tv3.declaration.name == "Integer");

    assert(tp3.caseTypes.size == 0);
    assert(tp3.satisfiedTypes.size == 0);
    
    value tpToplevelMethod = `function typeParameterTest`;
    assert(is OpenTypeVariable tpToplevelMethodTPType = tpToplevelMethod.openType);
    // this used to NPE
    assert(tpToplevelMethodTPType.declaration.name == "T");
}

@test
shared void checkClassOrInterfaceCaseTypes(){
    value iwct = `interface InterfaceWithCaseTypes`;
    assert(iwct.caseTypes.size == 2);
    assert(is OpenClassType iwcta = iwct.caseTypes[0],
           iwcta.declaration.name == "iwcta",
           iwcta.declaration.anonymous);
    assert(is OpenClassType iwctb = iwct.caseTypes[1],
           iwctb.declaration.name == "iwctb",
           iwctb.declaration.anonymous);
    
    value iwst = `interface InterfaceWithSelfType`;
    assert(iwst.caseTypes.size == 1);
    assert(is OpenTypeVariable iwsta = iwst.caseTypes[0]);
    assert(iwsta.declaration.name == "T");
}

@test
shared void checkModifiers(){
    value mods = `class Modifiers`;
    assert(!mods.annotation, !mods.final, mods.abstract, mods.shared, !mods.formal, !mods.actual, !mods.default);
    assert(exists inner = mods.getDeclaredMemberDeclaration<ClassDeclaration>("NonShared"));
    value submods = `class SubModifiers`;
    assert(exists decltest2 = submods.getMemberDeclaration<ClassDeclaration>("Private2"));
    assert(!submods.getDeclaredMemberDeclaration<ClassDeclaration>("Private2") exists);
    assert(exists decltest4 = submods.getDeclaredMemberDeclaration<ClassDeclaration>("SubPrivate"));
    assert(!inner.abstract, !inner.shared, !inner.formal, !inner.actual, !inner.default);
    assert(exists m = mods.getMemberDeclaration<FunctionDeclaration>("method"));
    assert(!m.annotation, m.shared, m.formal, !m.actual, !m.default, !m.parameter);
    assert(exists v = mods.getMemberDeclaration<ValueDeclaration>("string"));
    assert(v.shared, !v.formal, v.actual, v.default, !m.parameter);
    value fin = `class Final`;
    assert(fin.final);
    assert(`class Annot`.annotation);
    assert(`function annot`.annotation);
}

@test
shared void checkContainers(){
    assert(`class ContainerClass.InnerClass`.container.name == "ContainerClass");
    assert(`class ContainerClass`.container.name == "metamodel");
    assert(`value NoParams.str`.container.name == "NoParams");
    assert(`function NoParams.noParams`.container.name == "NoParams");
}

@test
shared void checkLocalTypes(){
    class Foo(shared String str) {
        shared class Bar() {}
    }
    // at this point we're only making sure that we don't have exceptions here, because local
    // types are not fully implemented yet
    Object methodType = `Foo.str`.type;
    value innerType = `Foo.Bar`;
}

@test
shared void checkEqualityAndHash(){
    // declarations
    
    ClassDeclaration noParamsDecl = `class NoParams`;
    ClassDeclaration fixedParamsDecl = `class FixedParams`;
    assert(noParamsDecl == noParamsDecl);
    assert(noParamsDecl.hash == noParamsDecl.hash);
    assert(noParamsDecl != fixedParamsDecl);
    assert(noParamsDecl.hash != fixedParamsDecl.hash);
    assert(noParamsDecl.string == "class metamodel::NoParams");
    
    InterfaceDeclaration tpaDecl = `interface TPA`;
    InterfaceDeclaration tpbDecl = `interface TPB`;
    assert(tpaDecl == tpaDecl);
    assert(tpaDecl.hash == tpaDecl.hash);
    assert(tpaDecl != tpbDecl);
    assert(tpaDecl.hash != tpbDecl.hash);
    assert(tpaDecl.string == "interface metamodel::TPA");
    
    AliasDeclaration alias1Decl = `alias TypeAliasToClass`;
    AliasDeclaration alias2Decl = `alias TypeAliasToUnion`;
    assert(alias1Decl == alias1Decl);
    assert(alias1Decl.hash == alias1Decl.hash);
    assert(alias1Decl != alias2Decl);
    assert(alias1Decl.hash != alias2Decl.hash);
    assert(alias1Decl.string == "alias metamodel::TypeAliasToClass");
    
    ValueDeclaration attr1Decl = `value NoParams.str`;
    ValueDeclaration attr2Decl = `value NoParams.integer`;
    assert(attr1Decl == attr1Decl);
    assert(attr1Decl.hash == attr1Decl.hash);
    assert(attr1Decl != attr2Decl);
    assert(attr1Decl.hash != attr2Decl.hash);
    assert(attr1Decl.string == "value metamodel::NoParams.str");
    
    FunctionDeclaration f1Decl = `function NoParams.noParams`;
    FunctionDeclaration f2Decl = `function NoParams.fixedParams`;
    assert(f1Decl == f1Decl);
    assert(f1Decl.hash == f1Decl.hash);
    assert(f1Decl != f2Decl);
    assert(f1Decl.hash != f2Decl.hash);
    assert(f1Decl.string == "function metamodel::NoParams.noParams");
    
    PackageDeclaration p1Decl = `package ceylon.language.meta`;
    PackageDeclaration p2Decl = `package ceylon.language`;
    assert(p1Decl == p1Decl);
    assert(p1Decl.hash == p1Decl.hash);
    assert(p1Decl != p2Decl);
    assert(p1Decl.hash != p2Decl.hash);
    assert(p1Decl.string == "package ceylon.language.meta");
    
    ModuleDeclaration m1Decl = `module ceylon.language`;
    ModuleDeclaration m2Decl = `module metamodel`;
    assert(m1Decl == m1Decl);
    assert(m1Decl.hash == m1Decl.hash);
    assert(m1Decl != m2Decl);
    assert(m1Decl.hash != m2Decl.hash);
    assert(m1Decl.string == "module ceylon.language/"+language.version);
    
    assert(is TypeParameterDeclaration tp1Decl = `class TypeParams`.getTypeParameterDeclaration("T"));
    assert(is TypeParameterDeclaration tp2Decl = `class ParameterisedContainerClass`.getTypeParameterDeclaration("Outer"));
    assert(tp1Decl == tp1Decl);
    assert(tp1Decl.hash == tp1Decl.hash);
    assert(tp1Decl != tp2Decl);
    assert(tp1Decl.hash != tp2Decl.hash);
    assert(tp1Decl.string == "given metamodel::TypeParams.T");
    
    // FIXME: add SetterDeclaration tests
    
    // open types
    
    assert(is OpenClassType pt1OpenType = `class Sub1`.extendedType);
    assert(is OpenClassType pt2OpenType = `class Sub2`.extendedType);
    assert(pt1OpenType == pt1OpenType);
    assert(pt1OpenType.hash == pt1OpenType.hash);
    assert(pt1OpenType != pt2OpenType);
    assert(pt1OpenType.hash != pt2OpenType.hash);
    assert(pt1OpenType.string == "metamodel::TypeParams<ceylon.language::Integer>");

    assert(is OpenUnion u1OpenType = `value NoParams.union1`.openType);
    assert(is OpenUnion u2OpenType = `value NoParams.union2`.openType);
    assert(is OpenUnion u3OpenType = `value NoParams.union3`.openType);
    assert(u1OpenType == u1OpenType);
    assert(u1OpenType.hash == u1OpenType.hash);
    assert(u1OpenType == u2OpenType);
    assert(u1OpenType.hash == u2OpenType.hash);
    assert(u1OpenType != u3OpenType);
    assert(u1OpenType.hash != u3OpenType.hash);
    assert(u1OpenType.string == "metamodel::TPA|metamodel::TPB");
    
    assert(is OpenIntersection i1OpenType = `value NoParams.intersection1`.openType);
    assert(is OpenIntersection i2OpenType = `value NoParams.intersection2`.openType);
    assert(is OpenIntersection i3OpenType = `value NoParams.intersection3`.openType);
    assert(i1OpenType == i1OpenType);
    assert(i1OpenType.hash == i1OpenType.hash);
    assert(i1OpenType == i2OpenType);
    assert(i1OpenType.hash == i2OpenType.hash);
    assert(i1OpenType != i3OpenType);
    assert(i1OpenType.hash != i3OpenType.hash);
    assert(i1OpenType.string == "metamodel::TPA&metamodel::TPB");
    
    assert(is OpenTypeVariable tp1OpenType = `value TypeParams.t1`.openType);
    assert(is OpenTypeVariable tp2OpenType = `value TypeParams.t2`.openType);
    assert(is OpenTypeVariable tp3OpenType = `value TypeParams2.t1`.openType);
    assert(tp1OpenType == tp1OpenType);
    assert(tp1OpenType.hash == tp1OpenType.hash);
    assert(tp1OpenType == tp2OpenType);
    assert(tp1OpenType.hash == tp2OpenType.hash);
    assert(tp1OpenType != tp3OpenType);
    assert(tp1OpenType.hash != tp3OpenType.hash);
    assert(tp1OpenType.string == "metamodel::TypeParams.T");
    
    // models
    
    value pt1Type = `TypeParams<Integer>`;
    value pt2Type = `TypeParams<String>`;
    assert(pt1Type == pt1Type);
    assert(pt1Type.hash == pt1Type.hash);
    assert(pt1Type != pt2Type);
    assert(pt1Type.hash != pt2Type.hash);
    assert(pt1Type.string == "metamodel::TypeParams<ceylon.language::Integer>");

    value ipt1Type = `TPA`;
    value ipt2Type = `TPB`;
    assert(ipt1Type == ipt1Type);
    assert(ipt1Type.hash == ipt1Type.hash);
    assert(ipt1Type != ipt2Type);
    assert(ipt1Type.hash != ipt2Type.hash);
    assert(ipt1Type.string == "metamodel::TPA");
    
    value pt1Function = `typeParams<Integer>`;
    value pt2Function = `typeParams<String>`;
    assert(pt1Function == pt1Function);
    assert(pt1Function.hash == pt1Function.hash);
    assert(pt1Function != pt2Function);
    assert(pt1Function.hash != pt2Function.hash);
    assert(pt1Function.string == "metamodel::typeParams<ceylon.language::Integer>");
    
    value value1 = `toplevelString`;
    value value2 = `toplevelInteger`;
    assert(value1 == value1);
    assert(value1.hash == value1.hash);
    assert(value1 != value2);
    assert(value1.hash != value2.hash);
    assert(value1.string == "metamodel::toplevelString");
    
    // members
    
    value ic1Type = `ContainerClass.InnerClass`;
    value ic2Type = `ContainerClass.DefaultedParams`;
    assert(ic1Type == ic1Type);
    assert(ic1Type.hash == ic1Type.hash);
    assert(ic1Type != ic2Type);
    assert(ic1Type.hash != ic2Type.hash);
    assert(ic1Type.string == "metamodel::ContainerClass.InnerClass");
    
    // bound
    value bic1Type = `ContainerClass.InnerClass`(ContainerClass());
    value bic2Type = `ContainerClass.InnerClass`(ContainerClass());
    assert(bic1Type == bic1Type);
    assert(bic1Type.hash == bic1Type.hash);
    assert(bic1Type != bic2Type);
    assert(bic1Type.hash != bic2Type.hash);
    assert(bic1Type.string == "metamodel::ContainerClass.InnerClass");
    
    value ii1Type = `ContainerClass.InnerInterface`;
    value ii2Type = `ContainerClass.InnerInterface2`;
    assert(ii1Type == ii1Type);
    assert(ii1Type.hash == ii1Type.hash);
    assert(ii1Type != ii2Type);
    assert(ii1Type.hash != ii2Type.hash);
    assert(ii1Type.string == "metamodel::ContainerClass.InnerInterface");
    
    // bound
    value bii1Type = `ContainerClass.InnerInterface`(ContainerClass());
    value bii2Type = `ContainerClass.InnerInterface`(ContainerClass());
    assert(bii1Type == bii1Type);
    assert(bii1Type.hash == bii1Type.hash);
    assert(bii1Type != bii2Type);
    assert(bii1Type.hash != bii2Type.hash);
    assert(bii1Type.string == "metamodel::ContainerClass.InnerInterface");
    
    value method1 = `NoParams.tp1<String>`;
    value method2 = `NoParams.tp1<Integer>`;
    assert(method1 == method1);
    assert(method1.hash == method1.hash);
    assert(method1 != method2);
    assert(method1.hash != method2.hash);
    assert(method1.string == "metamodel::NoParams.tp1<ceylon.language::String>");
    
    // bound
    value bmethod1 = `NoParams.tp1<String>`(NoParams());
    value bmethod2 = `NoParams.tp1<String>`(NoParams());
    assert(bmethod1 == bmethod1);
    assert(bmethod1.hash == bmethod1.hash);
    assert(bmethod1 != bmethod2);
    assert(bmethod1.hash != bmethod2.hash);
    assert(bmethod1.string == "metamodel::NoParams.tp1<ceylon.language::String>");
    
    value attr1 = `NoParams.str`;
    value attr2 = `NoParams.integer`;
    assert(attr1 == attr1);
    assert(attr1.hash == attr1.hash);
    assert(attr1 != attr2);
    assert(attr1.hash != attr2.hash);
    assert(attr1.string == "metamodel::NoParams.str");
    
    // bound
    value battr1 = `NoParams.str`(NoParams());
    value battr2 = `NoParams.str`(NoParams());
    assert(battr1 == battr1);
    assert(battr1.hash == battr1.hash);
    assert(battr1 != battr2);
    assert(battr1.hash != battr2.hash);
    assert(battr1.string == "metamodel::NoParams.str");
    
    value u1Type = `TypeParams<Integer|String>`;
    value u2Type = `TypeParams<String|Integer>`;
    value u3Type = `TypeParams<String|Integer|Float>`;
    assert(u1Type == u1Type);
    assert(u1Type.hash == u1Type.hash);
    assert(u1Type == u2Type);
    assert(u1Type.hash == u2Type.hash);
    assert(u1Type != u3Type);
    assert(u1Type.hash != u3Type.hash);
    assert(u1Type.string == "metamodel::TypeParams<ceylon.language::Integer|ceylon.language::String>");
    
    value i1Type = `TypeParams<TPA&TPB>`;
    value i2Type = `TypeParams<TPB&TPA>`;
    value i3Type = `TypeParams<TPA&TPB&Number<Float>>`;
    assert(i1Type == i1Type);
    assert(i1Type.hash == i1Type.hash);
    assert(i1Type == i2Type);
    assert(i1Type.hash == i2Type.hash);
    assert(i1Type != i3Type);
    assert(i1Type.hash != i3Type.hash);
    assert(i1Type.string == "metamodel::TypeParams<metamodel::TPA&metamodel::TPB>");
}

@test
shared void checkApplyTypeConstraints(){
    ClassDeclaration ctpClass = `class ConstrainedTypeParams`;
    try{
        ctpClass.apply(`String`, `Object`);
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpClass.apply(`Object`, `TPA`);
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpClass.apply();
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpClass.apply(`Integer`, `TPA`, `String`);
        assert(false);
    }catch(TypeApplicationException x){
    }

    value ctpFunction = `function constrainedTypeParams`;
    try{
        ctpFunction.apply(`String`, `Object`);
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpFunction.apply(`Object`, `TPA`);
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpFunction.apply();
        assert(false);
    }catch(TypeApplicationException x){
    }
    try{
        ctpFunction.apply(`Integer`, `TPA`, `String`);
        assert(false);
    }catch(TypeApplicationException x){
    }
}

@test
shared void checkApplications(){
    Object topLevelValue = `value toplevelString`.apply<String>();
    assert(is Value<String> topLevelValue);

    Object memberValue = `value TypeParams.t1`.memberApply<TypeParams<String>,String>(`TypeParams<String>`);
    assert(is Attribute<TypeParams<String>,String> memberValue);

    // application by ClassOrInterface.getAttribute
    Object? memberValue2 = `TypeParams<String>`.getAttribute<TypeParams<String>, String>("t1");
    assert(is Attribute<TypeParams<String>,String> memberValue2);
    assert(memberValue == memberValue2);

    Object topLevelFunction = `function typeParams`.apply<String, [String, Integer]>(`String`);
    assert(is Function<String, [String, Integer]> topLevelFunction);

    Object method = `function TypeParams.method`.memberApply<TypeParams<String>, String, [String, Integer]>(`TypeParams<String>`, `Integer`);
    assert(is Method<TypeParams<String>, String, [String, Integer]> method);

    // application by ClassOrInterface.getMethod
    Object? method2 = `TypeParams<String>`.getMethod<TypeParams<String>, String, [String, Integer]>("method", `Integer`);
    assert(is Method<TypeParams<String>, String, [String, Integer]> method2);
    assert(method == method2);

    Object topLevelClass = `class TypeParams`.classApply<TypeParams<String>, [String, Integer]>(`String`);
    assert(is Class<TypeParams<String>, [String, Integer]> topLevelClass);
    Object topLevelClass2 = `class TypeParams`.apply<TypeParams<String>>(`String`);
    assert(is Class<TypeParams<String>, [String, Integer]> topLevelClass2);
    
    Object memberClass = `class ParameterisedContainerClass.InnerClass`.memberClassApply<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []>(`ParameterisedContainerClass<Integer>`, `String`);
    assert(is MemberClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []> memberClass);
    
    Object memberClass2 = `class ParameterisedContainerClass.InnerClass`.memberApply<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>>(`ParameterisedContainerClass<Integer>`, `String`);
    assert(is MemberClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []> memberClass2);
    assert(memberClass == memberClass2);

    // application by ClassOrInterface.getClass
    Object? memberClass3 = `ParameterisedContainerClass<Integer>`.getClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []>("InnerClass", `String`);
    assert(is MemberClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []> memberClass3);
    assert(memberClass == memberClass3);
    
    // application by ClassOrInterface.getClassOrInterface
    Object? memberClass4 = `ParameterisedContainerClass<Integer>`.getClassOrInterface<ParameterisedContainerClass<Integer>, Class<ParameterisedContainerClass<Integer>.InnerClass<String>, []>>("InnerClass", `String`);
    assert(is MemberClass<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerClass<String>, []> memberClass4);
    assert(memberClass == memberClass4);

    // member interfaces
    Object memberInterface = `interface ParameterisedContainerClass.InnerInterface`.memberInterfaceApply<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>>(`ParameterisedContainerClass<Integer>`, `String`);
    assert(is MemberInterface<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>> memberInterface);
    
    Object memberInterface2 = `interface ParameterisedContainerClass.InnerInterface`.memberApply<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>>(`ParameterisedContainerClass<Integer>`, `String`);
    assert(is MemberInterface<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>> memberInterface2);
    assert(memberInterface == memberInterface2);
    
    // application by ClassOrInterface.getInterface
    Object? memberInterface3 = `ParameterisedContainerClass<Integer>`.getInterface<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>>("InnerInterface", `String`);
    assert(is MemberInterface<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>> memberInterface3);
    assert(memberInterface == memberInterface3);
    
    // application by ClassOrInterface.getClassOrInterface
    Object? memberInterface4 = `ParameterisedContainerClass<Integer>`.getClassOrInterface<ParameterisedContainerClass<Integer>, Interface<ParameterisedContainerClass<Integer>.InnerInterface<String>>>("InnerInterface", `String`);
    assert(is MemberInterface<ParameterisedContainerClass<Integer>, ParameterisedContainerClass<Integer>.InnerInterface<String>> memberInterface4);
    assert(memberInterface == memberInterface4);
/*
FIXME: to be determined wrt container types
    Object mixedAlias = `alias TypeAliasToMemberAndTopLevel`.apply<TPA & ContainerInterface.InnerClass>();
    assert(is IntersectionType<TPA & ContainerInterface.InnerClass> mixedAlias);
*/
}

@test
shared void checkInheritedVsDeclared(){
    value bottomType = `BottomClass`;
    
    // method
    value inheritedMethod = bottomType.getMethod<Nothing,Anything,Nothing>("inheritedMethod");
    assert(exists inheritedMethod, inheritedMethod == `Top<String>.inheritedMethod`);
    
    assert(!bottomType.getDeclaredMethod("inheritedMethod") exists);
    try{
        bottomType.getDeclaredMethod("myOwnBottomMethod");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    assert(bottomType.getDeclaredMethod<Nothing,Anything,Nothing>("myOwnBottomMethod") exists);

    value declaredMethod = bottomType.getMethod<Nothing,Anything,Nothing>("declaredMethod");
    assert(exists declaredMethod, declaredMethod == `BottomClass.declaredMethod`);

    // attribute
    value inheritedAttribute = bottomType.getAttribute<Nothing,Anything>("inheritedAttribute");
    assert(exists inheritedAttribute, inheritedAttribute == `Top<String>.inheritedAttribute`);
    
    assert(!bottomType.getDeclaredAttribute("inheritedAttribute") exists);
    
    value declaredAttribute = bottomType.getAttribute<Nothing,Anything>("declaredAttribute");
    assert(exists declaredAttribute, declaredAttribute == `BottomClass.declaredAttribute`);
    assert(bottomType.getDeclaredAttribute<BottomClass,String>("declaredAttribute") exists);

    // class
    value inheritedClass = bottomType.getClass<Nothing,Anything,Nothing>("InheritedClass");
    assert(exists inheritedClass, inheritedClass == `Top<String>.InheritedClass`);
    
    assert(!bottomType.getDeclaredClass("InheritedClass") exists);
    
    value declaredClass = bottomType.getClass<Nothing,Anything,Nothing>("DeclaredClass");
    assert(exists declaredClass, declaredClass == `BottomClass.DeclaredClass`);

    // class via class or interface
    value inheritedClass2 = bottomType.getClassOrInterface<Nothing,Class<Anything,Nothing>>("InheritedClass");
    assert(exists inheritedClass2, inheritedClass2 == `Top<String>.InheritedClass`);
    
    assert(!bottomType.getDeclaredClassOrInterface("InheritedClass") exists);
    
    value declaredClass2 = bottomType.getClassOrInterface<Nothing,Class<Anything,Nothing>>("DeclaredClass");
    assert(exists declaredClass2, declaredClass2 == `BottomClass.DeclaredClass`);
    assert(bottomType.getDeclaredClassOrInterface<Nothing,Class<Anything,Nothing>>("DeclaredClass") exists);
    
    // interface
    value inheritedInterface = bottomType.getInterface<Nothing,Anything>("InheritedInterface");
    assert(exists inheritedInterface, inheritedInterface == `Top<String>.InheritedInterface`);
    
    assert(!bottomType.getDeclaredInterface("InheritedInterface") exists);
    
    value declaredInterface = bottomType.getInterface<Nothing,Anything>("DeclaredInterface");
    assert(exists declaredInterface, declaredInterface == `BottomClass.DeclaredInterface`);
    assert(bottomType.getDeclaredInterface<Nothing,Anything>("DeclaredInterface") exists);

    // interface via class or interface
    value inheritedInterface2 = bottomType.getClassOrInterface<Nothing,Interface<Anything>>("InheritedInterface");
    assert(exists inheritedInterface2, inheritedInterface2 == `Top<String>.InheritedInterface`);
    
    assert(!bottomType.getDeclaredClassOrInterface("InheritedInterface") exists);
    
    value declaredInterface2 = bottomType.getClassOrInterface<Nothing,Interface<Anything>>("DeclaredInterface");
    assert(exists declaredInterface2, declaredInterface2 == `BottomClass.DeclaredInterface`);
    assert(bottomType.getDeclaredClassOrInterface<Nothing,Interface<Anything>>("DeclaredInterface") exists);
}

@test
shared void checkTests(){
    assert(`NoParams`.typeOf(NoParams()));
    assert(!`Integer`.typeOf(NoParams()));
    assert(`TPA & TPB`.typeOf(TP1()));
    assert(!`TPA & TPB & Integer`.typeOf(TP1()));
    assert(`NoParams | Integer`.typeOf(NoParams()));
    assert(!`String | Integer`.typeOf(NoParams()));
    assert(!`Nothing`.typeOf(NoParams()));
    
    assert(`NoParams`.subtypeOf(`Object`));
    assert(!`NoParams`.subtypeOf(`TPA`));
    assert(`Nothing`.subtypeOf(`Nothing`));
    assert(`Nothing`.subtypeOf(`NoParams`));

    assert(`Object`.supertypeOf(`NoParams`));
    assert(!`TPA`.supertypeOf(`NoParams`));
    assert(`Nothing`.supertypeOf(`Nothing`));
    assert(!`Nothing`.supertypeOf(`Object`));

    assert(`NoParams`.exactly(`NoParams`));
    assert(!`NoParams`.exactly(`Object`));
    assert(`TPA & TPB`.exactly(`TPB & TPA & TPB`));
    assert(`Nothing`.exactly(`Nothing`));
    assert(!`Nothing`.exactly(`Object`));
}

@test
shared void checkTypeArgumentChecks(){
    try{
        `class NoParams`.apply();
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `class ContainerClass.InnerClass`.memberApply(`ContainerClass`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `ContainerClass`.getClassOrInterface("InnerClass");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `class FixedParams`.classApply<FixedParams,[]>();
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `class ContainerClass.InnerClass`.memberClassApply(`ContainerClass`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `function getString`.apply<Integer,[String]>();
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `function NoParams.noParams`.memberApply(`NoParams`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `NoParams`.getMethod("noParams");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `value NoParams.str`.memberApply(`NoParams`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        // invalid because non-mutable
        `value NoParams.str`.memberApply<NoParams,String,String>(`NoParams`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        // invalid because non-mutable
        `value toplevelInteger`.apply<Integer,Integer>();
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `NoParams`.getAttribute("str");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        // invalid because non-mutable
        `NoParams`.getAttribute<NoParams,String,String>("str");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    // invalid containers types
    try{
        `value NoParams.str`.memberApply<ContainerClass,String>(`ContainerClass`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `function NoParams.noParams`.memberApply<ContainerClass,NoParams,[]>(`ContainerClass`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
    try{
        `class ContainerClass.InnerClass`.memberApply<NoParams,ContainerClass.InnerClass>(`NoParams`);
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
}

shared void run() {
    print("Running Metamodel tests");
    variable value total=0;
    variable value pass =0;
    try {
        total++;
        visitStringHierarchy();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed visit string hierarchy"); e.printStackTrace(); }
    try {
        total++;
        checkPackageAndModule();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed check package and module"); e.printStackTrace(); }
    try {
        total++;
        checkHierarchy();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed hierarchy"); e.printStackTrace(); }
    try {
        total++;
        checkConstructors();    
        pass++;
    } catch (Exception|AssertionError e) { print("Failed constructors"); e.printStackTrace(); }
    try {
        total++;
        checkMemberFunctions();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed member functions"); e.printStackTrace(); }
    try {
        total++;
        checkMemberAttributes();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed member attributes"); e.printStackTrace(); }
    try {
        total++;
        checkMemberTypes();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed member types"); e.printStackTrace(); }
    try {
        total++;
        checkToplevelAttributes();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed top level attributes"); e.printStackTrace(); }
    try {
        total++;
        checkToplevelFunctions();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed top level functions"); e.printStackTrace(); }
    try {
        total++;
        checkUntypedFunctionToAppliedFunction();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed untyped to applied function"); e.printStackTrace(); }
    try {
        total++;
        checkModules();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed modules "); e.printStackTrace(); }
    try {
        total++;
        checkObjectDeclaration();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed object declaration"); e.printStackTrace(); }
    try {
        total++;
        checkAliases();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed aliases"); e.printStackTrace(); }
    try {
        total++;
        checkTypeParameters();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed type parameters"); e.printStackTrace(); }
    try {
        total++;
        checkClassOrInterfaceCaseTypes();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed class/interface case types"); e.printStackTrace(); }
    try {
        total++;
        checkModifiers();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed modifiers"); e.printStackTrace(); }
    try {
        total++;
        checkContainers();
        pass++;
    } catch (Exception|AssertionError e) {
        if ("lexical" in e.message) {
            print("Containers test won't work in lexical scope style");
        } else {
            print("Failed containers"); e.printStackTrace();
        }
    }
    try {
        total++;
        checkLocalTypes();
        pass++;
    } catch (Exception|AssertionError e) {
        if ("lexical" in e.message) {
            print("Local types test won't work in lexical scope style");
        } else {
            print("Failed local types"); e.printStackTrace();
        }
    }
    try {
        total++;
        checkEqualityAndHash();
        pass++;
    } catch (Exception|AssertionError e) {
        if ("lexical" in e.message) {
            print("Equals/hash test won't work in lexical scope style");
        } else {
            print("Failed equals/hash"); e.printStackTrace();
        }
    }
    try {
        total++;
        checkApplyTypeConstraints();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed apply type constraints"); e.printStackTrace(); }
    try {
        total++;
        checkApplications();
        pass++;
    } catch (Exception|AssertionError e) {
        if ("lexical" in e.message) {
            print("Applications test won't work in lexical scope style");
        } else {
            print("Failed applications"); e.printStackTrace();
        }
    }
    try {
        total++;
        checkTests();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed tests"); e.printStackTrace(); }
    try {
        total++;
        checkTypeArgumentChecks();
        pass++;
    } catch (Exception|AssertionError e) { print("Failed type arguments"); e.printStackTrace(); }
    // ATTENTION!
    // When you add new test methods here make sure they are "shared" and marked "@test"!

    // FIXME: test members() wrt filtering
    // FIXME: test untyped class to applied class
    print("``pass``/``total`` so far. Starting bug tests...");
    void sandbox(void t()) {
      try {
        total++;
        t();
        pass++;
      } catch (Exception|AssertionError e) {
        if ("lexical" in e.message) {
            print("Bug test won't work in lexical scope style");
        } else {
            print("Failed bug test ``total``");
            e.printStackTrace();
        }
      }
    }
    sandbox(bug237);
    sandbox(bug238);
    sandbox(bug245);
    sandbox(bug257);
    sandbox(bug258);
    sandbox(bug260);
    sandbox(bug263);
    sandbox(bug284);
    sandbox(bug285);
    sandbox(bug286);
    sandbox(bug300);
    sandbox(bug303);
    sandbox(bug304);
    sandbox(bug307);
    sandbox(bug308);
    sandbox(bug318);
    sandbox(bug320);
    sandbox(bug329);
    sandbox(bug347);
    sandbox(bug349);
    sandbox(bug366);
    sandbox(bug388);
    sandbox(bug411);
    sandbox(bug433);
    sandbox(bug434);
    // those were filed for the JVM compiler initially
    sandbox(bugC1196test);
    sandbox(bugC1197);
    sandbox(bugC1198);
    sandbox(bugC1199);
    sandbox(bugC1201);
    sandbox(bugC1210);
    sandbox(bugC1244);
    // ATTENTION!
    // When you add new test methods here make sure they are "shared" and marked "@test"!
    print(pass==total then "Metamodel tests OK (``total`` total)" else "Metamodel tests ``pass``/``total``");
}
shared void test() { run(); }
