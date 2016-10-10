import check { check, fail }

class Static1 {
  shared static Integer a = 1;
  shared static Integer b => 2;
  shared static Integer c() {
    return a+b;
  }
  shared static Integer d() => a+b;
  shared static Integer e() {
    return d();
  }
  shared static Integer f() => e();

  shared static interface Iface1 {
    shared Integer m() => 1;
    shared formal Integer f();
    shared formal Integer g();
  }
  shared static class Class1() {
    shared default Integer m() => 1;
  }

  shared new() {
    print("static1");
  }

  shared Iface1 iface1() => object satisfies Iface1 {
    shared actual Integer f() => 2;
    shared actual Integer g() => 3;
  };
}
class Regular1() extends Static1() {
  check(a == 1, "reg 2.1");
  check(b == 2, "reg 2.2");
  check(c() == 3, "reg 2.3");
  check(d() == 3, "reg 2.4");
  check(e() == 3, "reg 2.5");
  check(f() == 3, "reg 2.6");
}

class Static2 extends Static1 {
  shared static Integer sa = a;
  shared static Integer sb = b;
  shared static Integer sc() {
    return c();
  }
  shared static Integer sd() => d();
  shared static Integer se() {
    return e();
  }
  shared static Integer sf() => f();
  shared static interface Iface2 satisfies Iface1 {
    shared actual Integer f() => 2;
  }
  shared static class Class2() extends Class1() {
    shared actual Integer m() => super.m() + 1;
  }
  shared new() extends Static1() {
    print("static2");
  }
  shared Iface2 iface2() {
    object caca satisfies Iface2 {
      shared actual Integer g() => 3;
    }
    return caca;
  }
}

class Static3<T> {
  shared static class Class3(T t) {
    shared T get() => t;
  }
  shared static class Gen3<E>(T t, E e) {
    shared T getT() => t;
    shared E getE() => e;
  }
  shared new(){}
}

class Static4 {
    static shared String name = "ok";
    shared static class X() {
      check(name=="ok", "bye");
    }
    shared new () {
      check(name=="ok", "hello");
    }
}

void testStatics() {
  check(Static1.a == 1, "static 1.1");
  check(Static1.b == 2, "static 1.2");
  check(Static1.c() == 3, "static 1.3");
  check(Static1.d() == 3, "static 1.4");
  check(Static1.e() == 3, "static 1.5");
  check(Static1.f() == 3, "static 1.6");
  check(Static2.sa == 1, "static 3.1");
  check(Static2.sb == 2, "static 3.2");
  check(Static2.sc() == 3, "static 3.3");
  check(Static2.sd() == 3, "static 3.4");
  check(Static2.se() == 3, "static 3.5");
  check(Static2.sf() == 3, "static 3.6");
  Object ic1 = Static1.Class1();
  check(ic1 is Static1.Class1, "static 5.1");
  Object ic2 = Static2.Class1();
  check(ic2 is Static1.Class1, "static 5.2");
  check(ic2 is Static2.Class1, "static 5.3");
  Object ic3 = Static2.Class2();
  check(ic3 is Static2.Class2, "static 5.4");
  check(ic3 is Static2.Class1, "static 5.5");
  check(ic3 is Static1.Class1, "static 5.6");
  check(Static2.Class2().m() == 2, "static 5.7");
  value [c1, c2] = [Static1(), Static2()];
  Object ii1 = c1.iface1();
  check(ii1 is Static1.Iface1, "static 6.1");
  check(c1.iface1().m() == 1, "static 6.2");
  check(c1.iface1().f() == 2, "static 6.3");
  Object ii2 = c2.iface2();
  check(ii2 is Static2.Iface2, "static 6.4");
  check(ii2 is Static2.Iface1, "static 6.5");
  check(ii2 is Static1.Iface1, "static 6.6");
  check(c2.iface2().m() == 1, "static 6.7");
  check(c2.iface2().f() == 2, "static 6.8");
  check(c2.iface2().g() == 3, "static 6.9");
  Regular1();
  //refs
  value sref = Static1.c;
  check(sref() == 3, "static 8.1");

  value g1 = Static3<String>.Class3("one");
  check(g1.get() == "one", "static 9.1");
  check(Static3<String>.Class3("one").get() == "one", "static 9.2");
  value g2 = Static3<String>.Gen3<Integer>("one",2);
  check(g2.getT() == "one", "static 9.3");
  check(g2.getE() == 2, "static 9.4");
  Object og1 = g1;
  check(og1 is Static3<String>.Class3, "static 10.1");
  check(!og1 is Static3<Integer>.Class3, "static 10.2");
  Object og2 = g2;
  check(og2 is Static3<String>.Gen3<Integer>, "static 10.3");
  check(!og2 is Static3<String>.Gen3<String>, "static 10.4");
  check(!og2 is Static3<Integer>.Gen3<Integer>, "static 10.5");
  Static4();
  Static4.X();
  Static4.X() st4x = Static4.X;
  st4x();
}
