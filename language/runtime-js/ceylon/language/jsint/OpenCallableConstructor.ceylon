import ceylon.language.meta.declaration {
  CallableConstructorDeclaration, FunctionOrValueDeclaration, Package,
  ClassDeclaration, Module, OpenType, TypeParameter
}
import ceylon.language.meta.model {
  CallableConstructor, MemberClassCallableConstructor, Type
}
import ceylon.language { AnnotationType=Annotation }

shared native class OpenCallableConstructor(containingPackage, shared Anything meta) satisfies CallableConstructorDeclaration {
  shared actual native Boolean abstract;
  shared actual native Boolean defaultConstructor;
  shared actual native ClassDeclaration container;
  shared actual native Object invoke(Type<>[] typeArguments, Anything* arguments);
  shared actual native Object memberInvoke(Object container, Type<>[] typeArguments, Anything* arguments);
  shared actual native CallableConstructor<Result,Arguments> apply<Result=Object,Arguments=Nothing>(Type<>* typeArguments)
            given Arguments satisfies Anything[];
  shared actual native MemberClassCallableConstructor<Container,Result,Arguments> memberApply<Container=Nothing,Result=Object,Arguments=Nothing>(Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];

  shared actual native Boolean shared;
  shared actual native Boolean formal;
  shared actual native Boolean default;
  shared actual native Boolean actual;
  //FunctionalDeclaration
  annotation=false;
  shared actual native FunctionOrValueDeclaration[] parameterDeclarations;
  shared actual native FunctionOrValueDeclaration? getParameterDeclaration(String name);
  //Contained
  toplevel=false;
  shared actual Package containingPackage;
  shared actual native Module containingModule => containingPackage.container;
  //TypedDeclaration
  shared actual native OpenType openType;
  //AnnotatedDeclaration
  shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
  shared actual native Boolean annotated<Annotation>()
          given Annotation satisfies AnnotationType;
  //Declaration
  shared actual native String name;
  shared actual native String qualifiedName;
  shared actual String string => "new ``qualifiedName``";
  shared actual native Boolean equals(Object other);

  //GenericDeclaration
  shared actual native TypeParameter[] typeParameterDeclarations;
  shared actual native TypeParameter? getTypeParameterDeclaration(String name);
}
