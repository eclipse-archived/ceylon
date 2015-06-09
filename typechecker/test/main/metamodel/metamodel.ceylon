import ceylon.language.meta.model {...}
import ceylon.language.meta.declaration {...}

interface List<Element> 
        satisfies {Element*} & 
            Correspondence<Integer,Element> {
    defines(Integer i) => nothing;
}

void test<T>() {
    @type:"ClassWithInitializerDeclaration" 
    value noParamsClass = `class NoParams`;
    @type:"Class<NoParams,Empty>" 
    value noParamsType = `NoParams`;
    @type:"Class<Params,Tuple<Integer|String,Integer,Tuple<String,String,Empty>>>" 
    value paramsType = `Params`;
    @type:"Class<ParameterisedClass<Integer>,Empty>" 
    value parameterisedType = `ParameterisedClass<Integer>`;
    @type:"ClassWithInitializerDeclaration" 
    value parameterisedTypeDeclClass = `class ParameterisedClass`;
    @error
    value parameterisedTypeDecl = `ParameterisedClass`;
    @type:"InterfaceDeclaration" 
    value interfaceInterface = `interface Interface1`;
    @type:"Interface<Interface1>" 
    value interfaceType = `Interface1`;
    @type:"Interface<ParameterisedInterface<Integer>>" 
    value parameterisedInterfaceType = `ParameterisedInterface<Integer>`;
    @type:"InterfaceDeclaration" 
    value parameterisedInterfaceDeclInterface = `interface ParameterisedInterface`;
    @error
    value parameterisedInterfaceDecl = `ParameterisedInterface`;
    @type:"UnionType<Interface1|Interface2>"
    value unionType = `Interface1|Interface2`;
    @type:"IntersectionType<Interface1&Interface2>" 
    value intersectionType = `Interface1&Interface2`;
    @type:"TypeParameter" 
    value parameterTypeParameter = `given T`;
    @type:"Type<T>" 
    value parameterType = `T`;
    @type:"Type<Nothing>"
    value nothingType = `String&Integer`;
    @type:"AliasDeclaration" 
    value aliasTypeAlias = `alias Alias`;
    @type:"Type<NoParams>" 
    value aliasType = `Alias`;
    
    // members
    @type:"ClassWithInitializerDeclaration"
    value memberClassTypeClass = `class Container.InnerClass`;
    @type:"MemberClass<Container,Container.InnerClass,Empty>"
    value memberClassType = `Container.InnerClass`;
    @type:"InterfaceDeclaration"
    value memberInterfaceTypeInterface = `interface Container.InnerInterface`;
    @type:"MemberInterface<Container,Container.InnerInterface>"
    value memberInterfaceType = `Container.InnerInterface`;
    
    @type:"MemberClass<ParameterisedContainer<String>,ParameterisedContainer<String>.InnerClass<Integer>,Empty>"
    value memberParameterisedClassType = `ParameterisedContainer<String>.InnerClass<Integer>`;
    @error
    value memberParameterisedClassDecl = `ParameterisedContainer.InnerClass`;
    @error
    value memberParameterisedClassTypeErr1 = `ParameterisedContainer<String>.InnerClass`;
    @error
    value memberParameterisedClassTypeErr2 = `ParameterisedContainer.InnerClass<Integer>`;

    @type:"MemberInterface<ParameterisedContainer<String>,ParameterisedContainer<String>.InnerInterface<Integer>>"
    value memberParameterisedInterfaceType = `ParameterisedContainer<String>.InnerInterface<Integer>`;
    @error
    value memberParameterisedInterfaceDecl = `ParameterisedContainer.InnerInterface`;
    @error
    value memberParameterisedInterfaceTypeErr1 = `ParameterisedContainer<String>.InnerInterface`;
    @error
    value memberParameterisedInterfaceTypeErr2 = `ParameterisedContainer.InnerInterface<Integer>`;

    // toplevel methods
    @type:"FunctionDeclaration"
    value toplevelMethodFunction = `function method`;
    @type:"Function<Integer,Tuple<String,String,Empty>>"
    value toplevelMethod = `method`;
    @error:"does not accept type arguments: 'method'"
    value toplevelMethodErr = `method<String>`;
    @error:"function or value does not exist: 'missingMethod'"
    value toplevelMethodErr2 = `missingMethod`;
    @type:"Function<Integer,Tuple<String,String,Empty>>"
    value toplevelParameterisedMethod = `parameterisedMethod<Integer,String>`;
    @type:"FunctionDeclaration"
    value toplevelParameterisedMethodErr = `function parameterisedMethod`;
    @type:"Function<Callable<Integer,Tuple<Boolean,Boolean,Empty>>,Tuple<String,String,Empty>>"
    value toplevelMPLMethod = `mplMethod`;

    // qualified methods
    @type:"FunctionDeclaration"
    value containerMethodFunction = `function Container.method`;
    @type:"Method<Container,Anything,Empty>"
    value containerMethod = `Container.method`;
    @error:"method or attribute does not exist: 'missing' in type 'Container'"
    value containerMethodErr = `Container.missing`;
    @type:"Method<ParameterisedContainer<String>,Anything,Tuple<Integer,Integer,Empty>>"
    value parameterisedContainerMethod = `ParameterisedContainer<String>.method<Integer>`;
    @type:"FunctionDeclaration"
    value parameterisedContainerMethodDeclFunction = `function ParameterisedContainer.method`;
    @error
    value parameterisedContainerMethodDecl = `ParameterisedContainer.method`;
    @error
    value parameterisedContainerMethodErr1 = `ParameterisedContainer<String>.method`;
    @error
    value parameterisedContainerMethodErr2 = `ParameterisedContainer.method<Integer>`;
    
    // toplevel attributes
    @type:"ValueDeclaration"
    value toplevelGetterValue = `value get`;
    @type:"Value<Integer,Nothing>"
    value toplevelGetter = `get`;
    @type:"ReferenceDeclaration"
    value toplevelAttributeValue = `value attribute`;
    @type:"Value<Integer,Nothing>"
    value toplevelAttribute = `attribute`;
    @error:"does not accept type arguments: 'attribute'"
    value toplevelAttributeErr = `attribute<String>`;
    @type:"ReferenceDeclaration"
    value toplevelVariableAttributeValue = `value variableAttribute`;
    @type:"Value<Integer,Integer>"
    value toplevelVariableAttribute = `variableAttribute`;

    // qualified attributes
    @type:"ReferenceDeclaration"
    value containerAttributeValue = `value Container.attribute`;
    @type:"Attribute<Container,Integer,Nothing>"
    value containerAttribute = `Container.attribute`;
    @type:"ReferenceDeclaration"
    value containerVariableAttributeValue = `value Container.variableAttribute`;
    @type:"Attribute<Container,Integer,Integer>"
    value containerVariableAttribute = `Container.variableAttribute`;
    @type:"Attribute<ParameterisedContainer<String>,String,Nothing>"
    value parameterisedContainerAttribute = `ParameterisedContainer<String>.attribute`;
    @type:"ReferenceDeclaration"
    value parameterisedContainerAttributeDeclValue = `value ParameterisedContainer.attribute`;
    @error
    value parameterisedContainerAttributeDecl = `ParameterisedContainer.attribute`;
    @type:"Attribute<ParameterisedContainer<String>,String,String>"
    value parameterisedContainerVariableAttribute = `ParameterisedContainer<String>.variableAttribute`;
    @error
    value parameterisedContainerVariableAttributeDecl = `ParameterisedContainer.variableAttribute`;
    
    // class parameters
    @error
    value classParameter = `Container.parameter`;
    @type:"ReferenceDeclaration"
    value classParameterAndSharedAttributeValue = `value Container.parameterAndSharedAttribute`;
    @type:"Attribute<Container,Integer,Nothing>"
    value classParameterAndSharedAttribute = `Container.parameterAndSharedAttribute`;
    @error
    value classParameterMethodValue = `value Container.parameterAndMethod`;
    @error
    value classParameterMethod = `Container.parameterAndMethod`;
    @type:"FunctionDeclaration"
    value classParameterAndSharedMethodFunction = `function Container.parameterAndSharedMethod`;
    @type:"Method<Container,Integer,Empty>"
    value classParameterAndSharedMethod = `Container.parameterAndSharedMethod`;

    @error
    value parameterisedClassParameterValue = `value ParameterisedContainer.parameter`;
    @error
    value parameterisedClassParameter = `ParameterisedContainer.parameter`;
    @error
    value parameterisedClassParameterErr = `ParameterisedContainer<String>.parameter`;
    @type:"Attribute<ParameterisedContainer<String>,Integer,Nothing>"
    value parameterisedClassParameterAndSharedAttribute = `ParameterisedContainer<String>.parameterAndSharedAttribute`;
    @type:"ReferenceDeclaration"
    value parameterisedClassParameterAndSharedAttributeDeclValue = `value ParameterisedContainer.parameterAndSharedAttribute`;
    @error
    value parameterisedClassParameterAndSharedAttributeDecl = `ParameterisedContainer.parameterAndSharedAttribute`;
    
    // class attributes that are parameters too
    @type:"ReferenceDeclaration"
    value classSharedAttributeAndParameterValue = `value Container.sharedAttributeAndParameter`;
    @type:"Attribute<Container,Integer,Nothing>"
    value classSharedAttributeAndParameter = `Container.sharedAttributeAndParameter`;
    @error
    value classAttributeAndParameterValue = `value Container.attributeAndParameter`;
    @error
    value classAttributeAndParameter = `Container.attributeAndParameter`;

    @type:"Attribute<ParameterisedContainer<String>,Integer,Nothing>"
    value parameterisedClassSharedAttributeAndParameter = `ParameterisedContainer<String>.sharedAttributeAndParameter`;
    @type:"ReferenceDeclaration"
    value parameterisedClassSharedAttributeAndParameterDeclValue = `value ParameterisedContainer.sharedAttributeAndParameter`;
    @error
    value parameterisedClassSharedAttributeAndParameterDecl = `ParameterisedContainer.sharedAttributeAndParameter`;

    @error
    value parameterisedClassAttributeAndParameter = `ParameterisedContainer<String>.attributeAndParameter`;
    @error
    value parameterisedClassAttributeAndParameterDecl = `ParameterisedContainer.attributeAndParameter`;

    // class methods that are parameters too
    @type:"FunctionDeclaration"
    value classSharedMethodAndParameterFunction = `function Container.sharedMethodAndParameter`;
    @type:"Method<Container,Integer,Empty>"
    value classSharedMethodAndParameter = `Container.sharedMethodAndParameter`;
    @error
    value classMethodAndParameterFunction = `function Container.methodAndParameter`;
    @error
    value classMethodAndParameter = `Container.methodAndParameter`;
    
    // private attributes
    @error:"method or attribute is not visible: 'privateAttribute' of type 'Container'"
    value privateAttributeValue = `value Container.privateAttribute`;
    @error:"method or attribute is not visible: 'privateAttribute' of type 'Container'"
    value privateAttribute = `Container.privateAttribute`;
    
    // local values and methods
    value localValue = 2;
    void localFunction(){}
    
    @error:"metamodel reference to local"
    value localValueDeclValue = `value localValue`;
    @error:"metamodel reference to local"
    value localFunctionDeclFunction = `function localFunction`;
    @error:"metamodel reference to local"
    value localValueDecl = `localValue`;
    @error:"metamodel reference to local"
    value localFunctionDecl = `localFunction`;
    
    class LocalClass(String arg) {
        shared String fun() => "hello";
    }
    @type:"Method<LocalClass,String,[]>" value localClassFun = `LocalClass.fun`;
    @type:"Class<LocalClass,Nothing>" value localClass = `LocalClass`;

    class OtherLocalClass {
        shared new create() {}
        shared new instance {}
        shared String fun() => "hello"; 
    }
    @type:"Class<OtherLocalClass,Nothing>" value otherLocalClass = `OtherLocalClass`;
    @type:"Constructor<OtherLocalClass,Nothing>" value otherLocalClassCtor = `OtherLocalClass.create`;
    @type:"Value<OtherLocalClass,Nothing>" value otherLocalClassSingCtor = `OtherLocalClass.instance`;
    @type:"Method<OtherLocalClass,String,[]>" value otherLocalClassFun = `OtherLocalClass.fun`;
    
}


// put them after usage to make sure their types are available when we deal with literals

class NoParams(){}
class Params(Integer i, String s){}
see (`given T`)
class ParameterisedClass<T>(){}
interface Interface1{}
interface ParameterisedInterface<T>{}
interface Interface2{}
alias Alias => NoParams;

Integer attribute = 2;
variable Integer variableAttribute = 2;
Integer get => 0;

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

class Bar {
    shared new () {}
    shared new baz(String string) {}
    shared new instance {}
}

class Baz {
    shared new baz(String string) {}
    shared new instance {}
}

void meta() {
    @type:"Function<Sequence<String>,Tuple<String,String,Empty>>" 
    value fd1 = `singletonList<String>`;
    //@type:"FunctionDeclaration" @error
    //value fd3 = `function singletonList<String>`;
    @type:"FunctionDeclaration" 
    value fd2 = `function singletonList`;
    @error value ut1 = `List|String`; 
    @type:"UnionType<List<String>|String>" 
    value ut2 = `List<String>|String`; 
    @error value it1 = `List&String`; 
    @type:"Type<Nothing>"
    value it2 = `List<String>&Integer`; 
    @type:"Type<List<Character>>"
    value it3 = `List<Character>&{Character*}`; 
    @type:"IntersectionType<Category<Object>&Foo<Object>>" 
    value it4 = `Category&Foo<Object>`; 
    @type:"InterfaceDeclaration" 
    value id1 = `interface List`;
    @type:"FunctionDeclaration" 
    value md1 = `function List.defines`;
    @type:"Method<List<Integer>,Boolean,Tuple<Integer,Integer,Empty>>" 
    value md2 = `List<Integer>.defines`; 
    //@type:"FunctionDeclaration" @error 
    //value md8 = `function List<Integer>.defines`; 
    @type:"ClassWithInitializerDeclaration"
    value cd1 = `class Foo.Bar`;
    @type:"FunctionDeclaration"
    value md3 = `function Foo.Bar.x`;
    @type:"Method<Foo<Object>.Bar<Object>,Anything,Empty>"
    value md4 = `Foo<Object>.Bar<Object>.x<Integer>`;
    @error
    value md5 = `Foo.Bar<Object>.x<Integer>`;
    @error
    value md6 = `Foo<Object>.Bar.x<Integer>`;
    @error
    value md7 = `Foo<Object>.Bar<String>.x<List>`;
    //@type:"ClassDeclaration" @error
    //value cd2 = `class Foo<List<String>>.Bar`;
    @type:"ClassWithInitializerDeclaration"
    value cd3 = `class Foo`;
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
    
    @type:"Attribute<String|Integer,String,Nothing>" 
    value ctm1 = `<String|Integer>.string`;
    
    @type:"Attribute<Sequential<String>,Integer,Nothing>" 
    value ctm2 = `String[].size`;
    
    @type:"Value<Basic,Nothing>" value p = `process`; 
    
    @error value tparam = `given Map.Item`;
    @error see (`given Sequence.Element`);
    @error see (`value Map.key`);
    
    @type:"Class<Bar,Empty>" value cd15 = `Bar`;
    @type:"Class<Baz,Nothing>" value cd16 = `Baz`;
    @type:"ConstructorDeclaration" value cd9 = `new Bar`;
    //@type:"ConstructorDeclaration" value cd10a = `new Bar.Bar`;
    @type:"ConstructorDeclaration" value cd10b = `new Bar.baz`;
    @type:"ValueDeclaration" value cd10c = `value Bar.instance`;
    @type:"ConstructorDeclaration" value cd10d = `new Baz.baz`;
    @type:"ValueDeclaration" value cd10e = `value Baz.instance`;
    @error value cd11 = `class Bar.baz`;
    @error value cd12 = `interface Bar.baz`;
    @error value cd15 =  `new Bar.instance`;
    @type:"Constructor<Bar,Tuple<String,String,Empty>>" value cd13 = `Bar.baz`;
    @type:"Value<Bar,Nothing>" value cd17 = `Bar.instance`;
    @type:"Constructor<Baz,Tuple<String,String,Empty>>" value cd18 = `Baz.baz`;
    @type:"Value<Baz,Nothing>" value cd19 = `Baz.instance`;
    @error value cd14 = `new Foo`;
}

void testPackagesModules() {
    @type:"Module" value m0 = `module ceylon.language`;
    @type:"Package" value p0 = `package ceylon.language.meta.model`;
    @type:"Package" value p1 = `package metamodel`;
    @error value m1 = `module ceylon.language.model`;
    @error value m2 = `module foo.bar`;
    @error value p2 = `package foo.bar.baz`;
}

void testMethodOfGeneric() {
    Method<Integer, Integer, [Integer]> plus1 = `Summable<Integer>.plus`;
    Method<Integer, Integer, [Integer]> plus2 = `Integer.plus`;
}

void objectMemberRefs() {
    ValueDeclaration processValueDec = `value process`;
    ClassDeclaration processClassDec = `class process`; 
    FunctionDeclaration processWriteLineDec = `function process.writeLine`; 
    ValueDeclaration processArgumentsDec = `value process.arguments`;
    Value<Basic,Nothing> obj = `process`;
    Method<\Iprocess,Anything,[]|[String]> fun = `\Iprocess.writeLine`;
    Attribute<\Iprocess,String[],Nothing> att = `\Iprocess.arguments`;
}

void testImplicitRefs() {
    Module mod = `module`;
    Package pack = `package`;
    class C() {
        ClassDeclaration clazz = `class`;
        @error value int = `interface`; 
    } 
    interface I {
        void run() {
            InterfaceDeclaration int = `interface`;
            @error value clazz = `class`;
        } 
    } 
    @error value int = `interface`; 
    @error value clazz = `class`; 
}

object foo {
    shared object bar {
        shared class Bar() {
            shared String name = "Gavin";
            shared class Baz<T>(T t) {}
        }
    }
}

object x { 
    shared object y {}
    shared class Y() {}
    shared interface I {}
    shared String name = ""; 
    shared void do() {}
}


shared void testObjectMetamodelRefs() {
    ClassDeclaration cd = `class foo.bar.Bar`;
    ValueDeclaration vd = `value foo.bar.Bar.name`;
    Attribute<\Ifoo,Basic,Nothing> vbn = `\Ifoo.bar`;
    Attribute<\Ifoo.\Ibar.Bar,String,Nothing> vsn = `\Ifoo.\Ibar.Bar.name`;
    MemberClass<\Ifoo.\Ibar,\Ifoo.\Ibar.Bar,[]> it1 = `\Ifoo.\Ibar.Bar`;
    MemberClass<\Ifoo.\Ibar.Bar,\Ifoo.\Ibar.Bar.Baz<String>,[String]> it2 
            = `\Ifoo.\Ibar.Bar.Baz<String>`;
    @type:"Attribute<x,Basic,Nothing>"
    value xy = `\Ix.y`;
    @type:"MemberClass<x,x.Y,Empty>"
    value xY = `\Ix.Y`;
    @type:"MemberInterface<x,x.I>"
    value xI = `\Ix.I`;
    @type:"Attribute<x,String,Nothing>"
    value xName = `\Ix.name`;
    @type:"Method<x,Anything,Empty>"
    value xDo = `\Ix.do`;
    
}

String packageFun() => "hello";
String packageValue => "hello";
class PackageClass() {
    shared String val => "hello";
}
interface PackageInterface {
    shared String val => "hello";
}

shared void packageQualifiedMetamodel() {
    void packageFun() {}
    Integer packageValue => 0;
    class PackageClass(Integer i) {}
    interface PackageInterface {}
    Function<String,[]> pf = `package.packageFun`;
    Value<String> pv = `package.packageValue`;
    Class<package.PackageClass,[]> pc = `package.PackageClass`;
    Interface<package.PackageInterface> pi = `package.PackageInterface`;
    Attribute<package.PackageClass,String> pca = `package.PackageClass.val`;
    Attribute<package.PackageInterface,String> pci = `package.PackageInterface.val`;
}

class Bug() { shared object foo { shared object baz {} } }

void bug() {
    value bugo = `Bug.\Ifoo.baz`;
    value bug1 = `Bug.\Ifoo.\Ibaz`;
    value bug2 = `Bug[][].size`;
    value bug3 = `<Bug[]|{Bug*}>.size`;
}