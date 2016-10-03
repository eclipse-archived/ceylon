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
  shared new() {
  }
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
  shared new() extends Static1() {
  }
}

void testStatics() {
  check(Static1.a == 1, "static 1.1");
  check(Static1.b == 2, "static 1.2");
  check(Static1.c() == 3, "static 1.3");
  check(Static1.d() == 3, "static 1.4");
  check(Static1.e() == 3, "static 1.5");
  check(Static1.f() == 3, "static 1.6");
  value c1 = Static1();
  check(c1.a == 1, "static 2.1");
  check(c1.b == 2, "static 2.2");
  check(c1.c() == 3, "static 2.3");
  check(c1.d() == 3, "static 2.4");
  check(c1.e() == 3, "static 2.5");
  check(c1.f() == 3, "static 2.6");
  check(Static2.sa == 1, "static 3.1");
  check(Static2.sb == 2, "static 3.2");
  check(Static2.sc() == 3, "static 3.3");
  check(Static2.sd() == 3, "static 3.4");
  check(Static2.se() == 3, "static 3.5");
  check(Static2.sf() == 3, "static 3.6");
  value c2 = Static2();
  check(c2.sa == 1, "static 4.1");
  check(c2.sb == 2, "static 4.2");
  check(c2.sc() == 3, "static 4.3");
  check(c2.sd() == 3, "static 4.4");
  check(c2.se() == 3, "static 4.5");
  check(c2.sf() == 3, "static 4.6");
}
