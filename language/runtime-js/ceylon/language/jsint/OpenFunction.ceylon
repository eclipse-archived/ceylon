import ceylon.language.meta.declaration {
  FunctionDeclaration, Package, OpenType, Module,
  NestableDeclaration, FunctionOrValueDeclaration, TypeParameter
}
import ceylon.language.meta.model { Type, Method, Function }
import ceylon.language{AnnotationType = Annotation}

shared native class OpenFunction(pkg, meta) satisfies FunctionDeclaration {
  shared Package pkg;
  shared Object meta;
  shared actual Package containingPackage => pkg;
  shared actual native OpenType openType;
  shared actual native Boolean defaulted;
  shared actual native Boolean variadic;
  shared actual native Boolean parameter;
  shared actual native Boolean annotation;
  shared actual native NestableDeclaration|Package container;
  shared actual native FunctionOrValueDeclaration[] parameterDeclarations;
  shared actual native Boolean actual;
  shared actual native Boolean formal;
  shared actual native Boolean default;
  shared actual native Boolean shared;
  shared actual native Boolean toplevel;
  shared actual native String qualifiedName;
  shared actual native String name;
  shared actual native TypeParameter[] typeParameterDeclarations;
  shared actual Module containingModule => pkg.container;
  shared actual native Function<Return, Arguments> apply<Return=Anything, Arguments=Nothing>(Type<Anything>* typeArguments)
    given Arguments satisfies Anything[];
  shared actual native Method<Container, Return, Arguments> memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(
      Type<Object> containerType, Type<Anything>* typeArguments)
    given Arguments satisfies Anything[];
  shared actual native FunctionOrValueDeclaration? getParameterDeclaration(String name);
  shared actual native TypeParameter? getTypeParameterDeclaration(String name);
  shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
  shared actual native Boolean annotated<Annotation>()
        given Annotation satisfies AnnotationType;
  shared actual native Boolean equals(Object other);
  shared actual String string => "function ``qualifiedName``";
  shared actual Integer hash => string.hash;

  shared actual native Anything memberInvoke(Object container, Type<>[] typeArguments, Anything* arguments)/*
            => memberApply<Nothing, Anything, Nothing>(`Nothing`, *typeArguments).bind(container).apply(*arguments)*/;
}
