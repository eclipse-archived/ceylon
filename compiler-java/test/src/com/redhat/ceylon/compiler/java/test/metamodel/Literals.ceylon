import ceylon.language.meta.model { ... }
import ceylon.language.meta.declaration { ... }

class LitClass(Integer i){
    shared Integer attribute = 1;
    shared variable Integer variableAttribute = 1;
    shared Integer method(String s) => 1;
    shared T parameterisedMethod<T>(T s) => s;
    shared class Member(Integer j){}
}

class LitClassWithParameters(shared Integer parameterAndSharedAttribute,
                             Integer parameterAttribute,
                             sharedAttributeAndParameter,
                             attributeAndParameter,
                             // not supported yet by the backend? https://github.com/ceylon/ceylon-compiler/issues/1200
                             //shared Integer parameterAndSharedMethod(),
                             Integer parameterMethod(),
                             sharedMethodAndParameter,
                             methodAndParameter){
    shared Integer sharedAttributeAndParameter;
    Integer attributeAndParameter;
    shared Integer sharedMethodAndParameter();
    Integer methodAndParameter();
}

class LitParameterisedClassWithParameters<T>(shared Integer parameterAndSharedAttribute,
                                             Integer parameterAttribute,
                                             sharedAttributeAndParameter,
                                             attributeAndParameter,
                                             // not supported yet by the backend? https://github.com/ceylon/ceylon-compiler/issues/1200
                                             //shared Integer parameterAndSharedMethod(),
                                             Integer parameterMethod(),
                                             sharedMethodAndParameter,
                                             methodAndParameter){
    shared Integer sharedAttributeAndParameter;
    Integer attributeAndParameter;
    shared Integer sharedMethodAndParameter();
    Integer methodAndParameter();
}

class LitParameterisedClass<T>(T t){
    shared Integer attribute = 1;
    shared variable Integer variableAttribute = 1;
    shared Integer method(String s) => 1;
    shared T parameterisedMethod<T>(T s) => s;
    shared class Member<X>(X x){}
}
class LitClassWithConstructors {
    shared new (Integer i) {}
    shared new other(Integer i) {}
    
    shared class Member {
        shared new (Boolean b) {
        }
        shared new other(Boolean b) {
        }
    }
}
class LitParameterisedClassWithConstructors<T> extends LitClassWithConstructors{
    shared new (T t) extends LitClassWithConstructors(1) {}
    shared new other(T t) extends LitClassWithConstructors(1) {}
}
interface LitInterface{
    shared interface Member{}
}
interface LitParameterisedInterface<T>{
    shared interface Member<X>{}
}

Integer litValue = 2;
variable Integer litVariable = 2;
Integer litFunction(String s){ return 1; }
X litParameterisedFunction<X>(X s){ return s; }

@noanno
void literals<T>(){
    
    // Classes
    
    Class<LitClass,[Integer]> classType = `LitClass`;
    ClassDeclaration classType2 = `class LitClass`;
    value classType3 = `LitClass`;

    MemberClass<LitClass,LitClass.Member,[Integer]> memberClassType = `LitClass.Member`;
    ClassDeclaration memberClassType2 = `class LitClass.Member`;
    value memberClassType3 = `LitClass.Member`;

    ClassDeclaration parameterisedClassDecl = `class LitParameterisedClass`;
    Class<LitParameterisedClass<Integer>,[Integer]> parameterisedClassType = `LitParameterisedClass<Integer>`;

    ClassDeclaration parameterisedMemberClassDecl = `class LitParameterisedClass.Member`;
    MemberClass<LitParameterisedClass<Integer>,LitParameterisedClass<Integer>.Member<String>,[String]> parameterisedMemberClassType = `LitParameterisedClass<Integer>.Member<String>`;
    
    // constructors
    assert(is Function<LitClassWithConstructors, [Integer]> ctor = `LitClassWithConstructors`.getConstructor(""));
    ConstructorDeclaration ctorDecl = `new LitClassWithConstructors`;
    Function<LitClassWithConstructors, [Integer]> ctorOther = `LitClassWithConstructors.other`; 
    ConstructorDeclaration ctorOtherDecl = `new LitClassWithConstructors.other`;
    assert(is Function<LitParameterisedClassWithConstructors<String>, [String]> ctor2 = `LitParameterisedClassWithConstructors<String>`.getConstructor(""));
    ConstructorDeclaration ctorDecl2 = `new LitParameterisedClassWithConstructors`;
    Function<LitParameterisedClassWithConstructors<String>, [String]> ctorOther2 = `LitParameterisedClassWithConstructors<String>.other`;
    ConstructorDeclaration ctorDeclOther2 = `new LitParameterisedClassWithConstructors.other`;
    
    // Interfaces
    
    Interface<LitInterface> interfaceType = `LitInterface`;
    InterfaceDeclaration interfaceType2 = `interface LitInterface`;
    value interfaceType3 = `LitInterface`;

    MemberInterface<LitInterface,LitInterface.Member> memberInterfaceType = `LitInterface.Member`;
    InterfaceDeclaration memberInterfaceType2 = `interface LitInterface.Member`;
    value memberInterfaceType3 = `LitInterface.Member`;

    InterfaceDeclaration parameterisedInterfaceDecl = `interface LitParameterisedInterface`;
    Interface<LitParameterisedInterface<Integer>> parameterisedInterfaceType = `LitParameterisedInterface<Integer>`;

    InterfaceDeclaration parameterisedMemberInterfaceDecl = `interface LitParameterisedInterface.Member`;
    MemberInterface<LitParameterisedInterface<Integer>, LitParameterisedInterface<Integer>.Member<String>> parameterisedMemberInterfaceType = `LitParameterisedInterface<Integer>.Member<String>`;
    
    // Other types
    
    Type<Anything> parameterType = `T`;
    UnionType<Number<Integer>|Destroyable> unionType = `Number<Integer>|Destroyable`;
    IntersectionType<Number<Integer>&Destroyable> intersectionType = `Number<Integer>&Destroyable`;
    Type<Anything> nothingType = `String&Number<Integer>`;
    
    Value<Integer> valueType1 = `litValue`;
    ValueDeclaration valueType2 = `value litValue`;
    value valueType3 = `litValue`;

    // Values and variables

    Attribute<LitClass,Integer> attributeType1 = `LitClass.attribute`;
    ValueDeclaration attributeType2 = `value LitClass.attribute`;
    value attributeType3 = `LitClass.attribute`;

    Attribute<LitParameterisedClass<Integer>,Integer> attributeType = `LitParameterisedClass<Integer>.attribute`;
    ValueDeclaration attributeDecl = `value LitParameterisedClass.attribute`;

    Value<Integer,Integer> variableType1 = `litVariable`;
    ValueDeclaration variableType2 = `value litVariable`;
    value variableType3 = `litVariable`;

    // Attributes and variable attributes

    Attribute<LitClass,Integer,Integer> variableAttributeType1 = `LitClass.variableAttribute`;
    ValueDeclaration variableAttributeType2 = `value LitClass.variableAttribute`;
    value variableAttributeType3 = `LitClass.variableAttribute`;

    Attribute<LitParameterisedClass<Integer>,Integer,Integer> variableAttributeType = `LitParameterisedClass<Integer>.variableAttribute`;
    ValueDeclaration variableAttributeDecl = `value LitParameterisedClass.variableAttribute`;

    // Functions

    Function<Integer,[String]> functionType1 = `litFunction`;
    FunctionDeclaration functionType2 = `function litFunction`;
    value functionType3 = `litFunction`;

    Function<Integer,[Integer]> parameterisedFunctionType1 = `litParameterisedFunction<Integer>`;
    FunctionDeclaration parameterisedFunctionType2 = `function litParameterisedFunction`;

    // Methods

    Method<LitClass,Integer,[String]> methodType1 = `LitClass.method`;
    FunctionDeclaration methodType2 = `function LitClass.method`;
    value methodType3 = `LitClass.method`;

    Method<LitClass,String,[String]> parameterisedMethodType = `LitClass.parameterisedMethod<String>`;
    FunctionDeclaration parameterisedMethodDecl = `function LitClass.parameterisedMethod`;

    Method<LitParameterisedClass<Integer>,String,[String]> parameterisedMethodType2 = `LitParameterisedClass<Integer>.parameterisedMethod<String>`;
    FunctionDeclaration parameterisedMethodDecl2 = `function LitParameterisedClass.parameterisedMethod`;
    
    // Class parameters
    
    ValueDeclaration paramAndSharedAttr1 = `value LitClassWithParameters.parameterAndSharedAttribute`;
    Attribute<LitClassWithParameters,Integer> paramAndSharedAttr2 = `LitClassWithParameters.parameterAndSharedAttribute`;
    
    ValueDeclaration sharedAttrAndParam1 = `value LitClassWithParameters.sharedAttributeAndParameter`;
    Attribute<LitClassWithParameters,Integer> sharedAttrAndParam2 = `LitClassWithParameters.sharedAttributeAndParameter`;

    // not supported yet: https://github.com/ceylon/ceylon-compiler/issues/1200
    //FunctionDeclaration&Method<LitClassWithParameters,Integer,[]> paramAndSharedMethod = `LitClassWithParameters.parameterAndSharedMethod`;
    FunctionDeclaration sharedMethodAndParam1 = `function LitClassWithParameters.sharedMethodAndParameter`;
    Method<LitClassWithParameters,Integer,[]> sharedMethodAndParam2 = `LitClassWithParameters.sharedMethodAndParameter`;

    ValueDeclaration parameterisedParamAndSharedAttr = `value LitParameterisedClassWithParameters.parameterAndSharedAttribute`;
    ValueDeclaration parameterisedSharedAttrAndParam = `value LitParameterisedClassWithParameters.sharedAttributeAndParameter`;

    // not supported yet: https://github.com/ceylon/ceylon-compiler/issues/1200
    //FunctionDeclaration parameterisedParamAndSharedMethod = `LitParameterisedClassWithParameters.parameterAndSharedMethod`;
    FunctionDeclaration parameterisedSharedMethodAndParam = `function LitParameterisedClassWithParameters.sharedMethodAndParameter`;

    // Aliases
    AliasDeclaration aliasDecl = `alias Alias`;

    // Modules and packages
    Module m = `module com.redhat.ceylon.compiler.java.test.metamodel`;
    Package p = `package com.redhat.ceylon.compiler.java.test.metamodel`;
    
    TypeParameter tp = `given T`;
    Type<Anything> tptype = `T`;
    
    ValueDeclaration objectAttribute = `value obj.attribute`;
    Attribute<\Iobj,Integer> objectValue = `\Iobj.attribute`;

    FunctionDeclaration objectMethod = `function obj.method`;
    Method<\Iobj,Integer,[Integer]> objectFunction = `\Iobj.method<Integer>`;
}

shared object obj {
    shared Integer attribute = 2;
    shared T method<T>(T t) => t;
}

alias Alias => LitClass;

class RelativeLiterals<T>(shared String str = "a") {
    shared Integer integer = 0;
    shared class Inner(){}
    shared void f<G>(){
        value temp1 = `str`;
        value temp2 = `integer`;
        value temp3 = `f<Integer>`;
        value temp4 = `Inner`;
        TypeParameter tp1 = `given T`;
        TypeParameter tp2 = `given G`;
    }
}

void casting(Type<LitClass> l){
    casting(`LitClass`);
    Integer i = `LitClass.attribute`(LitClass(2)).get();
    String j = `LitClass.parameterisedMethod<String>`(LitClass(2))("a");
    Integer i2 = `litValue`.get();
    String j2 = `litParameterisedFunction<String>`("a");
}
