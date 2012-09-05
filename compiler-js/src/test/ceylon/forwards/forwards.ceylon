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
  results();
}
