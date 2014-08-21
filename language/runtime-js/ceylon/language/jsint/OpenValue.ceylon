import ceylon.language.meta.declaration {
  ValueDeclaration, ClassDeclaration, SetterDeclaration,
  NestableDeclaration, Package, Module, OpenType
}
import ceylon.language.meta.model {
  AppliedType=Type, Value, Attribute
}
import ceylon.language {
  AnnotationType=Annotation
}

shared native class OpenValue(pkg,meta) satisfies ValueDeclaration {
  shared Package pkg;
  shared Object meta;
  shared native actual String name;
  shared native actual Value<Get, Set> apply<Get=Anything, Set=Nothing>();
  shared native actual Attribute<Container, Get, Set> memberApply<Container=Nothing, Get=Anything, Set=Nothing>(AppliedType<Container> containerType);
  shared native actual void memberSet(Object container, Anything newValue);
  shared native actual Boolean variable;
  shared native actual Boolean objectValue;
  shared native actual ClassDeclaration? objectClass;
  shared actual Boolean defaulted => false;
  shared actual Boolean variadic => false;
  shared actual Boolean parameter => false;
  shared native actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
  shared native actual OpenType openType;
  shared native actual SetterDeclaration? setter;

  shared native actual Boolean actual;
  shared native actual Boolean formal;
  shared native actual Boolean default;
  shared native actual Boolean shared;
  shared native actual Boolean toplevel;
  shared native actual Package|NestableDeclaration container;
  shared native actual String qualifiedName;
  shared native actual Boolean equals(Object other);
  shared actual String string => "value ``qualifiedName``";
  shared actual Module containingModule => containingPackage.container;
  shared actual Package containingPackage => pkg;
}
