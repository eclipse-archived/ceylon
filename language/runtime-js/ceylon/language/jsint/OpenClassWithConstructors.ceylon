import ceylon.language { AnnotationType = Annotation }
import ceylon.language.meta.declaration {
  ClassDeclaration, ValueDeclaration, Package, Module,
  NestableDeclaration, OpenInterfaceType, OpenClassType, OpenType,
  FunctionOrValueDeclaration, TypeParameter, ConstructorDeclaration,
  ClassWithConstructorsDeclaration
}
import ceylon.language.meta.model {
    Class, ClassOrInterface,
    MemberClass, Member,
    AppliedType = Type
}

shared native class OpenClassWithConstructors(pkg, meta) satisfies ClassWithConstructorsDeclaration {
    shared Package pkg;
    shared Object meta;
    shared native actual Class<Type, Arguments> classApply<Type=Anything, Arguments=Nothing>(AppliedType<Anything>* typeArguments)
        given Arguments satisfies Anything[];
    shared native actual MemberClass<Container, Type, Arguments> memberClassApply<Container=Nothing, Type=Anything, Arguments=Nothing>(AppliedType<Object> containerType, AppliedType<Anything>* typeArguments)
        given Arguments satisfies Anything[];
    shared native actual Boolean anonymous;
    shared native actual Boolean abstract;
    shared native actual Boolean final;
    shared native actual Boolean annotation;
    shared native actual Boolean serializable;
    shared native actual OpenType openType;
    shared native actual FunctionOrValueDeclaration? getParameterDeclaration(String name);
    shared native actual OpenClassType? extendedType;
    shared native actual OpenInterfaceType[] satisfiedTypes;
    shared native actual String name;
    shared actual Package containingPackage => pkg;
    shared actual Module containingModule => pkg.container;
    shared native actual Boolean toplevel;
    shared native actual String qualifiedName;
    shared native actual Boolean equals(Object other);
    shared native actual Boolean shared;
    shared native actual Boolean formal;
    shared native actual Boolean default;
    shared native actual Boolean actual;
    shared native actual Boolean isAlias => false;
    shared native actual Kind? getMemberDeclaration<Kind>(String name) 
        given Kind satisfies NestableDeclaration;
    shared native actual Kind? getDeclaredMemberDeclaration<Kind>(String name)
        given Kind satisfies NestableDeclaration;
    shared native actual FunctionOrValueDeclaration[] parameterDeclarations;
    shared native actual Kind[] memberDeclarations<Kind>()
        given Kind satisfies NestableDeclaration;
    shared native actual OpenType[] caseTypes;
    shared native actual ClassOrInterface<Type> apply<Type=Anything>(AppliedType<Anything>* typeArguments);
    shared native actual Member<Container, ClassOrInterface<Type>> & ClassOrInterface<Type> 
        memberApply<Container=Nothing, Type=Anything>(AppliedType<Object> containerType, AppliedType<Anything>* typeArguments);
    shared native actual NestableDeclaration|Package container;
    shared native actual Kind[] annotatedMemberDeclarations<Kind, Annotation>()
        given Kind satisfies NestableDeclaration
        given Annotation satisfies AnnotationType;
    shared native actual Kind[] annotatedDeclaredMemberDeclarations<Kind, Annotation>()
        given Kind satisfies NestableDeclaration
        given Annotation satisfies AnnotationType;
    shared native actual TypeParameter[] typeParameterDeclarations;
    shared native actual TypeParameter? getTypeParameterDeclaration(String name);
    shared native actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
    shared actual native Boolean annotated<Annotation>()
            given Annotation satisfies AnnotationType;
    shared native actual Kind[] declaredMemberDeclarations<Kind>() 
        given Kind satisfies NestableDeclaration;

    shared native actual ValueDeclaration? objectValue;
    shared actual String string=>"class ``qualifiedName``";
    shared actual Integer hash =>string.hash;

    shared native actual ConstructorDeclaration? getConstructorDeclaration(String name);
    shared native actual ConstructorDeclaration? defaultConstructorDeclaration;
    shared native actual ConstructorDeclaration[] constructorDeclarations();
    shared native actual ConstructorDeclaration[] annotatedConstructorDeclarations<Annotation>()
            given Annotation satisfies AnnotationType;
}
