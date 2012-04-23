import assert {...}

void test_outer_inner_safety() {
  class Outer() {
    shared class Inner() { }
  }
  Outer? o = null;
  Outer.Inner? i1 = o?.Inner();
  Outer.Inner? cons() = o?.Inner;
  if (exists i1) {
    fail("i1 should be null");
  }
  assert(className(cons)=="ceylon.language.JsCallable", "cons is Callable");
  Outer.Inner|Nothing i2 = cons();
  if (exists i2) {
    fail("i2 should not exist");
  }
}
