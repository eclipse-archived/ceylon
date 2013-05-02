import check {...}

void test_outer_inner_safety() {
  class Outer() {
    shared class Inner() { }
  }
  Outer? o = null;
  Outer.Inner? i1 = o?.Inner();
  Outer.Inner? cons() => o?.Inner();
  if (exists i1) {
    fail("i1 should be null");
  }
  check(className(cons)=="ceylon.language::Callable", "cons is Callable, ``className(cons)``");
  Outer.Inner|Null i2 = cons();
  if (exists i2) {
    fail("i2 should not exist");
  }
}
