import check{...}

class C() satisfies A {
  shared actual String b() { return a()+"b"; }
}
C c = C();
interface A {
  shared String a() { return "a"; }
  shared formal String b();
}

shared void test() {
  check(C().b()=="ab", "inherited forward decl 1");
  check(Impl1().b()=="ab", "inherited forward decl 2");
  check(Impl2().b()=="ab", "inherited forward decl 3");
  check("Test468" in test468.string, "#468");
  results();
}

shared Test468 test468=Test468();

shared class Test468 {
  shared new Test468() {}
}
