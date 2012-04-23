interface Concrete {
  shared String hi() {
    return "hi";
  }
  shared formal String bye();
}

class Dude() satisfies Concrete {
  shared actual String bye() {
    return "bye";
  }
}

void test_concrete_members() {
  value dude = Dude();
  assert(dude.hi()=="hi", "concrete interface member");
  assert(dude.bye()=="bye", "formal interface member");
}

interface Multi1 {
  shared default String multi() { return "multi1"; }
}

interface Multi2 {
  shared default String multi2() { return "multi2"; }
}

interface Multi3 satisfies Multi1 {
  shared default actual String multi() { return "multi3"; }
}

class Multimpl1() satisfies Multi1 & Multi2 {
  shared default actual String multi() { return "MULTI1"; }
}

class Multimpl2() satisfies Multi2 & Multi3 {
}

class Multimpl3() satisfies Multi1 & Multi3 {
}

class Multimpl4() extends Multimpl1() satisfies Multi3 {
  shared actual String multi() { return "MULTI4"; }
}

void testMultipleInheritance() {
  Object m1 = Multimpl1();
  Object m2 = Multimpl2();
  Object m3 = Multimpl3();
  Object m4 = Multimpl4();
  assert(is Multi1 m1, "multinheritance 1");
  assert(is Multi2 m1, "multinheritance 2");
  assert(!is Multi3 m1, "multinheritance 3");
  assert(is Multi1 m2, "multinheritance 4");
  assert(is Multi2 m2, "multinheritance 5");
  assert(is Multi3 m2, "multinheritance 6");
  assert(is Multi1 m3, "multinheritance 7");
  assert(!is Multi2 m3, "multinheritance 8");
  assert(is Multi3 m3, "multinheritance 9");
  assert(is Multi1 m4, "multinheritance 10");
  assert(is Multi2 m4, "multinheritance 11");
  assert(is Multi3 m4, "multinheritance 12");
  assert(is Multimpl1 m4, "multinheritance 13");
  assert(Multimpl1().multi()=="MULTI1", "multinheritance 14");
  assert(Multimpl2().multi()=="multi3", "multinheritance 15");
  assert(Multimpl3().multi()=="multi3", "multinheritance 16");
  assert(Multimpl4().multi()=="MULTI4", "multinheritance 17");
}
