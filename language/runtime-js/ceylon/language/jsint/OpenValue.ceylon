import ceylon.language.meta.declaration {
  ValueDeclaration, ClassDeclaration, SetterDeclaration,
  NestableDeclaration, Package, Module, OpenType,
  FunctionalDeclaration
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
  shared native actual default String name;
  shared native actual default Value<Get, Set> apply<Get=Anything, Set=Nothing>();
  shared native actual default Attribute<Container, Get, Set> memberApply<Container=Nothing, Get=Anything, Set=Nothing>(AppliedType<Object> containerType);
  shared native actual default void memberSet(Object container, Anything newValue);
  shared native actual default Boolean variable;
  shared native actual default Boolean late;
  shared native actual Boolean objectValue;
  shared native actual ClassDeclaration? objectClass;
  shared native actual default Boolean defaulted;
  shared native actual default Boolean variadic;
  shared native actual default Boolean parameter;
  shared native actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
  shared native actual Boolean annotated<Annotation>()
          given Annotation satisfies AnnotationType;
  shared native actual default OpenType openType;
  shared native actual SetterDeclaration? setter;

  shared native actual default Boolean actual;
  shared native actual default Boolean formal;
  shared native actual default Boolean default;
  shared native actual default Boolean shared;
  shared native actual default Boolean toplevel;
  shared native actual default Package|NestableDeclaration container;
  shared native actual default String qualifiedName;
  shared native actual default Boolean equals(Object other);
  shared actual String string=>"value ``qualifiedName``";
  shared actual Integer hash =>string.hash;
  shared actual Module containingModule => containingPackage.container;
  shared actual Package containingPackage => pkg;
}

shared native class ValParamDecl(shared NestableDeclaration&FunctionalDeclaration cont, shared Object param) extends OpenValue(cont.containingPackage, param) {
  shared native actual Boolean actual;
  shared native actual Boolean formal;
  shared native actual Boolean default;
  shared native actual Boolean shared;
  shared native actual Boolean variadic;
  shared native actual Boolean variable;
  shared native actual Boolean defaulted;
  shared native default actual OpenType openType;
  shared native actual String name;
  shared native actual String qualifiedName;
  shared native actual Value<Get, Set> apply<Get=Anything, Set=Nothing>();
  shared native actual Attribute<Container, Get, Set> memberApply<Container=Nothing, Get=Anything, Set=Nothing>(AppliedType<Object> containerType);
  shared native actual void memberSet(Object container, Anything newValue);
  shared native actual Anything memberGet(Object container);
  shared native actual default Boolean equals(Object other);
  container=>cont;
  parameter=>true;
  toplevel=>false;
}
