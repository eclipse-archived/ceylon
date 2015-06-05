interface Inter2 {
  shared String a() { return "a"; }
  shared formal String b();
}

class Impl1() satisfies Inter1 {
  shared actual String b() { return a()+"b"; }
}

Impl2 impl2 = Impl2();
