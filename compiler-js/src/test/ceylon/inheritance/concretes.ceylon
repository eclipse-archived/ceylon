import check {...}

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
  check(dude.hi()=="hi", "concrete interface member");
  check(dude.bye()=="bye", "formal interface member");
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
  check(m1 is Multi1, "multinheritance 1");
  check(m1 is Multi2, "multinheritance 2");
  check(!m1 is Multi3, "multinheritance 3");
  check(m2 is Multi1, "multinheritance 4");
  check(m2 is Multi2, "multinheritance 5");
  check(m2 is Multi3, "multinheritance 6");
  check(m3 is Multi1, "multinheritance 7");
  check(!m3 is Multi2, "multinheritance 8");
  check(m3 is Multi3, "multinheritance 9");
  check(m4 is Multi1, "multinheritance 10");
  check(m4 is Multi2, "multinheritance 11");
  check(m4 is Multi3, "multinheritance 12");
  check(m4 is Multimpl1, "multinheritance 13");
  check(Multimpl1().multi()=="MULTI1", "multinheritance 14");
  check(Multimpl2().multi()=="multi3", "multinheritance 15");
  check(Multimpl3().multi()=="multi3", "multinheritance 16");
  check(Multimpl4().multi()=="MULTI4", "multinheritance 17");
}
