//Model loader tests
import nesting { nameTest }
import check { check }
import ceylon.language.meta.declaration { FunctionDeclaration }

shared class Fuera() {
  shared class Dentro() {
    shared void f() {}
    shared class Cebolla() {
      shared void g() {}
    }
  }
}

shared annotation DumbAnnotation dumb() => DumbAnnotation();

shared final annotation class DumbAnnotation()
        satisfies OptionalAnnotation<DumbAnnotation,FunctionDeclaration> {}

shared class Padre() {
  shared void a() {}
  shared dumb void b() {}
}
shared class Hijo() {
  shared void c() {}
  shared dumb void d() {}
}

shared void testModelLoader() {
    print("Testing model loader");
    check(nameTest.mltest1("X") is {String*}, "Class from object");
    check(nameTest.mltest2() is Destroyable, "Interface from object");
    print(`function Fuera.Dentro.Cebolla.g`);
    print(`Fuera.Dentro.Cebolla.g`);
    check(`class Hijo`.annotatedMemberDeclarations<FunctionDeclaration,DumbAnnotation>().size==2, "annotatedMemberDeclarations");
}
