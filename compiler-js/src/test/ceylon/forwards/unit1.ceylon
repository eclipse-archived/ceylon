interface Inter1 {
  shared String a() { return "a"; }
  shared formal String b();
}

class Impl2() satisfies Inter2 {
  shared actual String b() { return a()+"b"; }
}

Impl1 impl1 = Impl1();
