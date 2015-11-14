import ceylon.language.meta.declaration {
  InterfaceDeclaration, Package, NestableDeclaration, Module,
  OpenType, TypeParameter, OpenClassType, OpenInterfaceType
}
import ceylon.language.meta.model {
  Interface, Member, ClassOrInterface, MemberInterface,
  AppliedType=Type
}
import ceylon.language {
    AnnotationType=Annotation
}

shared native class OpenInterface(shared Package pkg, shared Object meta) satisfies InterfaceDeclaration {
  shared actual native Interface<Type> interfaceApply<Type=Anything>(AppliedType<Anything>* typeArguments);
  shared actual native MemberInterface<Container, Type> memberInterfaceApply<Container=Nothing, Type=Anything>(AppliedType<Object> containerType, AppliedType<Anything>* typeArguments);
  shared actual native Interface<Type> apply<Type=Anything>(AppliedType<Anything>* typeArguments);
  shared actual native Member<Container, ClassOrInterface<Type>> & ClassOrInterface<Type> 
    memberApply<Container=Nothing, Type=Anything>(AppliedType<Object> containerType, AppliedType<Anything>* typeArguments);

  shared actual native Kind[] memberDeclarations<Kind>() 
            given Kind satisfies NestableDeclaration;
  shared actual native Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies NestableDeclaration
            given Annotation satisfies AnnotationType;
  shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
  shared actual native Boolean annotated<Annotation>()
          given Annotation satisfies AnnotationType;
  shared actual native Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies NestableDeclaration;
  shared actual native OpenType openType;
  shared actual native TypeParameter[] typeParameterDeclarations;
  shared actual native TypeParameter? getTypeParameterDeclaration(String name);

  shared actual native OpenClassType? extendedType;
  shared actual native OpenInterfaceType[] satisfiedTypes;
  shared actual native String name;

  shared actual native Boolean actual;
  shared actual native Boolean formal;
  shared actual native Boolean default;
  shared actual native Boolean shared;
  shared actual native Boolean toplevel;
  shared actual native String qualifiedName;
  shared actual native NestableDeclaration|Package container;

  shared actual native OpenType[] caseTypes;

  shared actual native Kind[] declaredMemberDeclarations<Kind>()
      given Kind satisfies NestableDeclaration;
  shared actual native Kind[] annotatedDeclaredMemberDeclarations<Kind, Annotation>()
      given Kind satisfies NestableDeclaration
      given Annotation satisfies AnnotationType;
  shared actual native Kind? getDeclaredMemberDeclaration<Kind>(String name)
      given Kind satisfies NestableDeclaration;

  shared actual native Boolean equals(Object other);

  shared actual Boolean isAlias => false;
  shared actual String string=>"interface ``qualifiedName``";
  shared actual Integer hash =>string.hash;
  shared actual Package containingPackage => pkg;
  shared actual Module containingModule => pkg.container;
}
