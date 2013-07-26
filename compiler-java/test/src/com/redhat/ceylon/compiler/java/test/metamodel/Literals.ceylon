import ceylon.language.metamodel { ... }
import ceylon.language.metamodel.declaration { ... }

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
    ClassDeclaration classType2 = `LitClass`;
    value classType3 = `LitClass`;

    MemberClass<LitClass,LitClass.Member,[Integer]> memberClassType = `LitClass.Member`;
    ClassDeclaration memberClassType2 = `LitClass.Member`;
    value memberClassType3 = `LitClass.Member`;

    ClassDeclaration parameterisedClassDecl = `LitParameterisedClass`;
    Class<LitParameterisedClass<Integer>,[Integer]> parameterisedClassType = `LitParameterisedClass<Integer>`;

    ClassDeclaration parameterisedMemberClassDecl = `LitParameterisedClass.Member`;
    MemberClass<LitParameterisedClass<Integer>,LitParameterisedClass<Integer>.Member<String>,[String]> parameterisedMemberClassType = `LitParameterisedClass<Integer>.Member<String>`;
    
    // Interfaces
    
    Interface<LitInterface> interfaceType = `LitInterface`;
    InterfaceDeclaration interfaceType2 = `LitInterface`;
    value interfaceType3 = `LitInterface`;

    MemberInterface<LitInterface,LitInterface.Member> memberInterfaceType = `LitInterface.Member`;
    InterfaceDeclaration memberInterfaceType2 = `LitInterface.Member`;
    value memberInterfaceType3 = `LitInterface.Member`;

    InterfaceDeclaration parameterisedInterfaceDecl = `LitParameterisedInterface`;
    Interface<LitParameterisedInterface<Integer>> parameterisedInterfaceType = `LitParameterisedInterface<Integer>`;

    InterfaceDeclaration parameterisedMemberInterfaceDecl = `LitParameterisedInterface.Member`;
    MemberInterface<LitParameterisedInterface<Integer>, LitParameterisedInterface<Integer>.Member<String>> parameterisedMemberInterfaceType = `LitParameterisedInterface<Integer>.Member<String>`;
    
    // Other types
    
    Type parameterType = `T`;
    UnionType unionType = `Number|Closeable`;
    IntersectionType intersectionType = `Number&Closeable`;
    Type nothingType = `String&Number`;
    
    Value<Integer> valueType1 = `litValue`;
    ValueDeclaration valueType2 = `litValue`;
    value valueType3 = `litValue`;

    // Values and variables

    Attribute<LitClass,Integer> attributeType1 = `LitClass.attribute`;
    ValueDeclaration attributeType2 = `LitClass.attribute`;
    value attributeType3 = `LitClass.attribute`;

    Attribute<LitParameterisedClass<Integer>,Integer> attributeType = `LitParameterisedClass<Integer>.attribute`;
    ValueDeclaration attributeDecl = `LitParameterisedClass.attribute`;

    Variable<Integer> variableType1 = `litVariable`;
    VariableDeclaration variableType2 = `litVariable`;
    value variableType3 = `litVariable`;

    // Attributes and variable attributes

    VariableAttribute<LitClass,Integer> variableAttributeType1 = `LitClass.variableAttribute`;
    VariableDeclaration variableAttributeType2 = `LitClass.variableAttribute`;
    value variableAttributeType3 = `LitClass.variableAttribute`;

    VariableAttribute<LitParameterisedClass<Integer>,Integer> variableAttributeType = `LitParameterisedClass<Integer>.variableAttribute`;
    VariableDeclaration variableAttributeDecl = `LitParameterisedClass.variableAttribute`;

    // Functions

    Function<Integer,[String]> functionType1 = `litFunction`;
    FunctionDeclaration functionType2 = `litFunction`;
    value functionType3 = `litFunction`;

    Function<Integer,[Integer]> parameterisedFunctionType1 = `litParameterisedFunction<Integer>`;
    FunctionDeclaration parameterisedFunctionType2 = `litParameterisedFunction`;

    // Methods

    Method<LitClass,Integer,[String]> methodType1 = `LitClass.method`;
    FunctionDeclaration methodType2 = `LitClass.method`;
    value methodType3 = `LitClass.method`;

    Method<LitClass,String,[String]> parameterisedMethodType = `LitClass.parameterisedMethod<String>`;
    FunctionDeclaration parameterisedMethodDecl = `LitClass.parameterisedMethod`;

    Method<LitParameterisedClass<Integer>,String,[String]> parameterisedMethodType2 = `LitParameterisedClass<Integer>.parameterisedMethod<String>`;
    FunctionDeclaration parameterisedMethodDecl2 = `LitParameterisedClass.parameterisedMethod`;
    
    // Class parameters
    
    ValueDeclaration&Attribute<LitClassWithParameters,Integer> paramAndSharedAttr = `LitClassWithParameters.parameterAndSharedAttribute`;
    ValueDeclaration&Attribute<LitClassWithParameters,Integer> paramAttr = `LitClassWithParameters.parameterAttribute`;
    ValueDeclaration&Attribute<LitClassWithParameters,Integer> sharedAttrAndParam = `LitClassWithParameters.sharedAttributeAndParameter`;
    ValueDeclaration&Attribute<LitClassWithParameters,Integer> attrAndParam = `LitClassWithParameters.attributeAndParameter`;

    // not supported yet: https://github.com/ceylon/ceylon-compiler/issues/1200
    //FunctionDeclaration&Method<LitClassWithParameters,Integer,[]> paramAndSharedMethod = `LitClassWithParameters.parameterAndSharedMethod`;
    FunctionDeclaration&Method<LitClassWithParameters,Integer,[]> methodAttr = `LitClassWithParameters.parameterMethod`;
    FunctionDeclaration&Method<LitClassWithParameters,Integer,[]> sharedMethodAndParam = `LitClassWithParameters.sharedMethodAndParameter`;
    FunctionDeclaration&Method<LitClassWithParameters,Integer,[]> methodAndParam = `LitClassWithParameters.methodAndParameter`;

    ValueDeclaration parameterisedParamAndSharedAttr = `LitParameterisedClassWithParameters.parameterAndSharedAttribute`;
    ValueDeclaration parameterisedParamAttr = `LitParameterisedClassWithParameters.parameterAttribute`;
    ValueDeclaration parameterisedSharedAttrAndParam = `LitParameterisedClassWithParameters.sharedAttributeAndParameter`;
    ValueDeclaration parameterisedAttrAndParam = `LitParameterisedClassWithParameters.attributeAndParameter`;

    // not supported yet: https://github.com/ceylon/ceylon-compiler/issues/1200
    //FunctionDeclaration parameterisedParamAndSharedMethod = `LitParameterisedClassWithParameters.parameterAndSharedMethod`;
    FunctionDeclaration parameterisedMethodAttr = `LitParameterisedClassWithParameters.parameterMethod`;
    FunctionDeclaration parameterisedSharedMethodAndParam = `LitParameterisedClassWithParameters.sharedMethodAndParameter`;
    FunctionDeclaration parameterisedMethodAndParam = `LitParameterisedClassWithParameters.methodAndParameter`;
}

class RelativeLiterals(shared String str = "a") {
    shared Integer integer = 0;
    class Inner(){}
    shared void f(){
        value temp1 = `str`;
        value temp2 = `integer`;
        value temp3 = `f`;
        value temp4 = `Inner`;
    }
}
