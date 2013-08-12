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
  value cons_cname = className(cons);
  check(cons_cname=="ceylon.language::Callable" || cons_cname=="ceylon.language::JsCallable",
    "cons is Callable, ``cons_cname``");
  Outer.Inner|Null i2 = cons();
  if (exists i2) {
    fail("i2 should not exist");
  }
}
