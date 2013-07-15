
void test<T>() {
    @type:"ClassDeclaration&Class<NoParams,Empty>" 
    value noParamsType = `NoParams`;
    @type:"ClassDeclaration&Class<Params,Tuple<Integer|String,Integer,Tuple<String,String,Empty>>>" 
    value paramsType = `Params`;
    @type:"Class<ParameterisedClass<Integer>,Empty>" 
    value parameterisedType = `ParameterisedClass<Integer>`;
    @type:"ClassDeclaration" 
    value parameterisedTypeDecl = `ParameterisedClass`;
    @type:"InterfaceDeclaration&Interface<Interface1>" 
    value interfaceType = `Interface1`;
    @type:"Interface<ParameterisedInterface<Integer>>" 
    value parameterisedInterfaceType = `ParameterisedInterface<Integer>`;
    @type:"InterfaceDeclaration" 
    value parameterisedInterfaceDecl = `ParameterisedInterface`;
    @type:"UnionType" 
    value unionType = `Interface1|Interface2`;
    @type:"IntersectionType" 
    value intersectionType = `Interface1&Interface2`;
    @type:"Type" 
    value parameterType = `T`;
    // couldn't find a way to assert that its type is really nothingType since it's an anonymous type
    @type:"Basic&Type" 
    value nothingType = `String&Integer`;
    @type:"ClassDeclaration&Class<NoParams,Empty>" 
    value aliasType = `Alias`;
    
    // members
    @type:"ClassDeclaration&MemberClass<Container,Container.InnerClass,Empty>"
    value memberClassType = `Container.InnerClass`;
    @type:"InterfaceDeclaration&MemberInterface<Container,Container.InnerInterface>"
    value memberInterfaceType = `Container.InnerInterface`;
    
    @type:"MemberClass<ParameterisedContainer<String>,ParameterisedContainer<String>.InnerClass<Integer>,Empty>"
    value memberParameterisedClassType = `ParameterisedContainer<String>.InnerClass<Integer>`;
    @type:"ClassDeclaration"
    value memberParameterisedClassDecl = `ParameterisedContainer.InnerClass`;
    @error:"mixed type and declaration literal"
    value memberParameterisedClassTypeErr1 = `ParameterisedContainer<String>.InnerClass`;
    @error:"mixed type and declaration literal"
    value memberParameterisedClassTypeErr2 = `ParameterisedContainer.InnerClass<Integer>`;

    @type:"MemberInterface<ParameterisedContainer<String>,ParameterisedContainer<String>.InnerInterface<Integer>>"
    value memberParameterisedInterfaceType = `ParameterisedContainer<String>.InnerInterface<Integer>`;
    @type:"InterfaceDeclaration"
    value memberParameterisedInterfaceDecl = `ParameterisedContainer.InnerInterface`;
    @error:"mixed type and declaration literal"
    value memberParameterisedInterfaceTypeErr1 = `ParameterisedContainer<String>.InnerInterface`;
    @error:"mixed type and declaration literal"
    value memberParameterisedInterfaceTypeErr2 = `ParameterisedContainer.InnerInterface<Integer>`;

    // toplevel methods
    @type:"FunctionDeclaration&Function<Integer,Tuple<String,String,Empty>>"
    value toplevelMethod = `method`;
    @error:"does not accept type arguments: method"
    value toplevelMethodErr = `method<String>`;
    @error:"function or value does not exist: method"
    value toplevelMethodErr2 = `missingMethod`;
    @type:"Function<Integer,Tuple<String,String,Empty>>"
    value toplevelParameterisedMethod = `parameterisedMethod<Integer,String>`;
    @type:"FunctionDeclaration"
    value toplevelParameterisedMethodErr = `parameterisedMethod`;
    @type:"FunctionDeclaration&Function<Callable<Integer,Tuple<Boolean,Boolean,Empty>>,Tuple<String,String,Empty>>"
    value toplevelMPLMethod = `mplMethod`;

    // qualified methods
    @type:"FunctionDeclaration&Method<Container,Anything,Empty>"
    value containerMethod = `Container.method`;
    @error:"member method or attribute is ambiguous: missing"
    value containerMethodErr = `Container.missing`;
    @type:"Method<ParameterisedContainer<String>,Anything,Tuple<Integer,Integer,Empty>>"
    value parameterisedContainerMethod = `ParameterisedContainer<String>.method<Integer>`;
    @type:"FunctionDeclaration"
    value parameterisedContainerMethodDecl = `ParameterisedContainer.method`;
    @error
    value parameterisedContainerMethodErr1 = `ParameterisedContainer<String>.method`;
    @error
    value parameterisedContainerMethodErr2 = `ParameterisedContainer.method<Integer>`;
    
    // toplevel attributes
    @type:"AttributeDeclaration&Value<Integer>"
    value toplevelAttribute = `attribute`;
    @error:"does not accept type arguments: attribute"
    value toplevelAttributeErr = `attribute<String>`;
    @type:"VariableDeclaration&Variable<Integer>"
    value toplevelVariableAttribute = `variableAttribute`;

    // qualified attributes
    @type:"AttributeDeclaration&Attribute<Container,Integer>"
    value containerAttribute = `Container.attribute`;
    @type:"VariableDeclaration&VariableAttribute<Container,Integer>"
    value containerVariableAttribute = `Container.variableAttribute`;
    @type:"Attribute<ParameterisedContainer<String>,String>"
    value parameterisedContainerAttribute = `ParameterisedContainer<String>.attribute`;
    @type:"AttributeDeclaration"
    value parameterisedContainerAttributeDecl = `ParameterisedContainer.attribute`;
    @type:"VariableAttribute<ParameterisedContainer<String>,String>"
    value parameterisedContainerVariableAttribute = `ParameterisedContainer<String>.variableAttribute`;
    @type:"VariableDeclaration"
    value parameterisedContainerVariableAttributeDecl = `ParameterisedContainer.variableAttribute`;
}


// put them after usage to make sure their types are available when we deal with literals

class NoParams(){}
class Params(Integer i, String s){}
class ParameterisedClass<T>(){}
interface Interface1{}
interface ParameterisedInterface<T>{}
interface Interface2{}
alias Alias => NoParams;

Integer attribute = 2;
variable Integer variableAttribute = 2;

Integer method(String p){ return 1; }
Integer mplMethod(String p)(Boolean b){ return 1; }
Ret parameterisedMethod<Ret, Arg>(Arg a){ return nothing; }

class Container(){
    shared interface InnerInterface{}
    shared class InnerClass(){}
    shared void method(){}
    shared Integer attribute = 2;
    shared variable Integer variableAttribute = 2;
}

class ParameterisedContainer<Outer>(Outer a){
    shared interface InnerInterface<Inner>{}
    shared class InnerClass<Inner>(){}
    shared void method<Inner>(Inner p){}
    shared Outer attribute = a;
    shared variable Outer variableAttribute = a;
}
