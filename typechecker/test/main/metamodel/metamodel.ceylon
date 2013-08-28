
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
    @type:"AliasDeclaration&Type" 
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
    @type:"ClassDeclaration"
    value memberParameterisedClassTypeErr1 = `ParameterisedContainer<String>.InnerClass`;
    @error:"mixed type and declaration literal"
    value memberParameterisedClassTypeErr2 = `ParameterisedContainer.InnerClass<Integer>`;

    @type:"MemberInterface<ParameterisedContainer<String>,ParameterisedContainer<String>.InnerInterface<Integer>>"
    value memberParameterisedInterfaceType = `ParameterisedContainer<String>.InnerInterface<Integer>`;
    @type:"InterfaceDeclaration"
    value memberParameterisedInterfaceDecl = `ParameterisedContainer.InnerInterface`;
    @type:"InterfaceDeclaration"
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
    @type:"FunctionDeclaration"
    value parameterisedContainerMethodErr1 = `ParameterisedContainer<String>.method`;
    @error
    value parameterisedContainerMethodErr2 = `ParameterisedContainer.method<Integer>`;
    
    // toplevel attributes
    @type:"ValueDeclaration&Value<Integer>"
    value toplevelAttribute = `attribute`;
    @error:"does not accept type arguments: attribute"
    value toplevelAttributeErr = `attribute<String>`;
    @type:"VariableDeclaration&Variable<Integer>"
    value toplevelVariableAttribute = `variableAttribute`;

    // qualified attributes
    @type:"ValueDeclaration&Attribute<Container,Integer>"
    value containerAttribute = `Container.attribute`;
    @type:"VariableDeclaration&VariableAttribute<Container,Integer>"
    value containerVariableAttribute = `Container.variableAttribute`;
    @type:"Attribute<ParameterisedContainer<String>,String>"
    value parameterisedContainerAttribute = `ParameterisedContainer<String>.attribute`;
    @type:"ValueDeclaration"
    value parameterisedContainerAttributeDecl = `ParameterisedContainer.attribute`;
    @type:"VariableAttribute<ParameterisedContainer<String>,String>"
    value parameterisedContainerVariableAttribute = `ParameterisedContainer<String>.variableAttribute`;
    @type:"VariableDeclaration"
    value parameterisedContainerVariableAttributeDecl = `ParameterisedContainer.variableAttribute`;
    
    // class parameters
    @error
    value classParameter = `Container.parameter`;
    @type:"ValueDeclaration&Attribute<Container,Integer>"
    value classParameterAndSharedAttribute = `Container.parameterAndSharedAttribute`;
    @error
    value classParameterMethod = `Container.parameterAndMethod`;
    @type:"FunctionDeclaration&Method<Container,Integer,Empty>"
    value classParameterAndSharedMethod = `Container.parameterAndSharedMethod`;

    @error
    value parameterisedClassParameter = `ParameterisedContainer.parameter`;
    @error
    value parameterisedClassParameterErr = `ParameterisedContainer<String>.parameter`;
    @type:"Attribute<ParameterisedContainer<String>,Integer>"
    value parameterisedClassParameterAndSharedAttribute = `ParameterisedContainer<String>.parameterAndSharedAttribute`;
    @type:"ValueDeclaration"
    value parameterisedClassParameterAndSharedAttributeDecl = `ParameterisedContainer.parameterAndSharedAttribute`;
    
    // class attributes that are parameters too
    @type:"ValueDeclaration&Attribute<Container,Integer>"
    value classSharedAttributeAndParameter = `Container.sharedAttributeAndParameter`;
    @error
    value classAttributeAndParameter = `Container.attributeAndParameter`;

    @type:"Attribute<ParameterisedContainer<String>,Integer>"
    value parameterisedClassSharedAttributeAndParameter = `ParameterisedContainer<String>.sharedAttributeAndParameter`;
    @type:"ValueDeclaration"
    value parameterisedClassSharedAttributeAndParameterDecl = `ParameterisedContainer.sharedAttributeAndParameter`;

    @error
    value parameterisedClassAttributeAndParameter = `ParameterisedContainer<String>.attributeAndParameter`;
    @error
    value parameterisedClassAttributeAndParameterDecl = `ParameterisedContainer.attributeAndParameter`;

    // class methods that are parameters too
    @type:"FunctionDeclaration&Method<Container,Integer,Empty>"
    value classSharedMethodAndParameter = `Container.sharedMethodAndParameter`;
    @error
    value classMethodAndParameter = `Container.methodAndParameter`;
    
    // private attributes
    @error:"metamodel references to non-shared attributes not supported yet"
    value privateAttribute = `Container.privateAttribute`;
    
    // local values and methods
    value localValue = 2;
    void localFunction(){}
    
    @error:"metamodel references to local values not supported"
    value localValueDecl = `localValue`;
    @error:"metamodel references to local functions not supported"
    value localFunctionDecl = `localFunction`;
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

class Container(shared Integer parameterAndSharedAttribute,
                Integer parameter,
                sharedAttributeAndParameter,
                attributeAndParameter,
                shared Integer parameterAndSharedMethod(),
                Integer parameterAndMethod(),
                sharedMethodAndParameter,
                methodAndParameter){
    shared Integer sharedAttributeAndParameter;
    Integer attributeAndParameter;
    shared Integer sharedMethodAndParameter();
    Integer methodAndParameter();
    shared interface InnerInterface{}
    shared class InnerClass(){}
    shared void method(){}
    shared Integer attribute = 2;
    shared variable Integer variableAttribute = 2;
    Integer privateAttribute = 2;
}

class ParameterisedContainer<Outer>(Outer a,
                                    shared Integer parameterAndSharedAttribute,
                                    Integer parameter,
                                    sharedAttributeAndParameter,
                                    attributeAndParameter){
    shared Integer sharedAttributeAndParameter;
    Integer attributeAndParameter;
    shared interface InnerInterface<Inner>{}
    shared class InnerClass<Inner>(){}
    shared void method<Inner>(Inner p){}
    shared Outer attribute = a;
    shared variable Outer variableAttribute = a;
}


[T+] singletonList<T>(T t) given T satisfies Object => [t];

class Foo<S>() {
    shared class Bar<T>() {
        shared void x<X>() {}
    }
}

void meta() {
    @type:"Function<Sequence<String>,Tuple<String,String,Empty>>" 
    value fd1 = `singletonList<String>`;
    @type:"FunctionDeclaration" 
    value fd2 = `singletonList`;
    @error value ut1 = `List|String`; 
    @type:"UnionType" 
    value ut2 = `List<String>|String`; 
    @error value it1 = `List&String`; 
    @type:"Basic&Type"
    value it2 = `List<String>&Integer`; 
    @type:"Interface<List<Character>>"
    value it3 = `List<Character>&{Character*}`; 
    @type:"IntersectionType" 
    value it4 = `Category&Foo<Object>`; 
    @type:"InterfaceDeclaration" 
    value id1 = `List`;
    @type:"FunctionDeclaration" 
    value md1 = `List.defines`;
    @type:"Method<List<Integer>,Boolean,Tuple<Integer,Integer,Empty>>" 
    value md2 = `List<Integer>.defines`; 
    @type:"ClassDeclaration"
    value cd1 = `Foo.Bar`;
    @type:"FunctionDeclaration"
    value md3 = `Foo.Bar.x`;
    @type:"Method<Foo<Object>.Bar<Object>,Anything,Empty>"
    value md4 = `Foo<Object>.Bar<Object>.x<Integer>`;
    @error
    value md5 = `Foo.Bar<Object>.x<Integer>`;
    @error
    value md6 = `Foo<Object>.Bar.x<Integer>`;
    @error
    value md7 = `Foo<Object>.Bar<String>.x<List>`;
    @type:"ClassDeclaration"
    value cd2 = `Foo<List<String>>.Bar`;
    @type:"ClassDeclaration"
    value cd3 = `Foo`;
    @type:"Class<Foo<Object>,Empty>"
    value cd4 = `Foo<Object>`;
    @type:"MemberClass<Foo<Anything>,Foo<Anything>.Bar<Anything>,Empty>"
    value cd5 = `Foo<Anything>.Bar<Anything>`;
    @error
    value cd6 = `Foo.Bar<Object>`;
    @error
    value cd7 = `Foo<List>.Bar<Object>`;
    @error
    value cd8 = `Foo<Object>.Bar<List>`;
}

void testPackagesModules() {
    @type:"Module" value m0 = `module ceylon.language`;
    @type:"Package" value p0 = `package ceylon.language.model`;
    @type:"Package" value p1 = `package metamodel`;
    @error value m1 = `module ceylon.language.model`;
    @error value m2 = `module foo.bar`;
    @error value p2 = `package foo.bar.baz`;
}

