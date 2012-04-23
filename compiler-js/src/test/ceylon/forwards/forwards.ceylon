import assert{...}

class C() satisfies A {
  shared actual String b() { return a()+"b"; }
}
C c = C();
interface A {
  shared String a() { return "a"; }
  shared formal String b();
}

shared void test() {
  assert(C().b()=="ab", "inherited forward decl 1");
  assert(Impl1().b()=="ab", "inherited forward decl 2");
  assert(Impl2().b()=="ab", "inherited forward decl 3");
  results();
}
