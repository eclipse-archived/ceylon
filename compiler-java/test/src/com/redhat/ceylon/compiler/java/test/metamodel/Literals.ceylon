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
    UnionType<Number|Closeable> unionType = `Number|Closeable`;
    IntersectionType<Number&Closeable> intersectionType = `Number&Closeable`;
    Type<Anything> nothingType = `String&Number`;
    
    Value<Integer> valueType1 = `litValue`;
    ValueDeclaration valueType2 = `value litValue`;
    value valueType3 = `litValue`;

    // Values and variables

    Attribute<LitClass,Integer> attributeType1 = `LitClass.attribute`;
    ValueDeclaration attributeType2 = `value LitClass.attribute`;
    value attributeType3 = `LitClass.attribute`;

    Attribute<LitParameterisedClass<Integer>,Integer> attributeType = `LitParameterisedClass<Integer>.attribute`;
    ValueDeclaration attributeDecl = `value LitParameterisedClass.attribute`;

    Variable<Integer> variableType1 = `litVariable`;
    VariableDeclaration variableType2 = `value litVariable`;
    value variableType3 = `litVariable`;

    // Attributes and variable attributes

    VariableAttribute<LitClass,Integer> variableAttributeType1 = `LitClass.variableAttribute`;
    VariableDeclaration variableAttributeType2 = `value LitClass.variableAttribute`;
    value variableAttributeType3 = `LitClass.variableAttribute`;

    VariableAttribute<LitParameterisedClass<Integer>,Integer> variableAttributeType = `LitParameterisedClass<Integer>.variableAttribute`;
    VariableDeclaration variableAttributeDecl = `value LitParameterisedClass.variableAttribute`;

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
}

alias Alias => LitClass;

class RelativeLiterals(shared String str = "a") {
    shared Integer integer = 0;
    shared class Inner(){}
    shared void f(){
        value temp1 = `str`;
        value temp2 = `integer`;
        value temp3 = `f`;
        value temp4 = `Inner`;
    }
}
