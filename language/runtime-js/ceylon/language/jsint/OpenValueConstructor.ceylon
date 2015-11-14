import ceylon.language.meta.declaration {
  ValueConstructorDeclaration, FunctionOrValueDeclaration, Package,
  ClassDeclaration, Module, OpenType
}
import ceylon.language.meta.model {
  ValueConstructor, MemberClassValueConstructor, Type
}
import ceylon.language { AnnotationType=Annotation }

shared native class OpenValueConstructor(containingPackage, shared Anything meta) satisfies ValueConstructorDeclaration {
  shared actual native ClassDeclaration container;
  shared actual native ValueConstructor<Result> apply<Result=Anything>();
  shared actual native MemberClassValueConstructor<Container, Result> memberApply<Container=Nothing, Result=Anything>(Type<Object> containerType);

  shared actual native Boolean shared;
  shared actual native Boolean formal;
  shared actual native Boolean default;
  shared actual native Boolean actual;
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
}
