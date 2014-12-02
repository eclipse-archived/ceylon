import ceylon.language.meta.declaration {
  ConstructorDeclaration, FunctionOrValueDeclaration, Package,
  ClassDeclaration, Module, OpenType
}
import ceylon.language { AnnotationType=Annotation }

shared native class OpenConstructor(containingPackage, shared Anything meta) satisfies ConstructorDeclaration {
  shared actual native Boolean defaultConstructor;
  shared actual native ClassDeclaration container;
  shared actual native Boolean shared;
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
  //Declaration
  shared actual native String name;
  shared actual native String qualifiedName;
  shared actual String string => "new ``qualifiedName``";
}
