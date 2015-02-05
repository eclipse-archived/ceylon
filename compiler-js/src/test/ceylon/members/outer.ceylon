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
  //Issue 499
  value cr1=o?.Inner;
  check(!cr1() exists, "#499.1");
  Outer? o2=Outer();
  value cr2=o2?.Inner;
  check(cr2() exists, "#499.2");
  Anything() cr3=Outer().Inner;
  check(cr3() exists, "#499.3");
}
