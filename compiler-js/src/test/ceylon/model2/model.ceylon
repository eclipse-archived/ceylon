//Model loader tests
import nesting { nameTest }
import check { check }

shared class Fuera() {
  shared class Dentro() {
    shared void f() {}
    shared class Cebolla() {
      shared void g() {}
    }
  }
}

shared void testModelLoader() {
    print("Testing model loader");
    check(nameTest.mltest1("X") is {String*}, "Class from object");
    check(nameTest.mltest2() is Destroyable, "Interface from object");
    print(`function Fuera.Dentro.Cebolla.g`);
    print(`Fuera.Dentro.Cebolla.g`);
}
