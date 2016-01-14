import check {...}

shared class Foo5867 {
    shared Bar5867 bar;

    shared new () {
        bar = Bar5867();

        // Assign a listener function to bar.
        bar._onUpdate = (Integer val) {
            // Access the bar instance of outer foo within the listener.
            bar.style = "updated";
        };
    }
}

shared class Bar5867() {
    shared variable String style = "";

    variable Integer _val = 0;

    shared variable Anything(Integer) _onUpdate = (Integer x) => null;

    shared void update(Integer val) {
        _val = val;
        _onUpdate(val);
    }
}

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
  value foo = Foo5867();
  foo.bar.update(3);
  check(foo.bar.style=="updated", "#5867");
}
