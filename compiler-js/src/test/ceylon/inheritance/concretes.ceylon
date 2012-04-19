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

