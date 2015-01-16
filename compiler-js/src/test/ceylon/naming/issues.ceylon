import check { check }

shared object toplevel483 {
  string = "object.";
}

shared Object getToplevel483() {
  return object {
    string = "method.";
  };
}

shared class Test483() {
  shared object nested483 {
    string="object.";
  }
  shared Object getNested483() {
    return object {
      string="method.";
    };
  }
  check(nested483.string=="object.", "#483.1 member (inside)");
  check(getNested483().string=="method.", "#483.2 member (inside)");
}

void issues() {
  check(toplevel483.string=="object.", "#483.1 toplevel");
  check(getToplevel483()=="method.", "#483.2 toplevel");
  object nested483 {
    string="object.";
  }
  Object getNested483() {
    return object {
      string="method.";
    };
  }
  check(nested483.string=="object.", "#483.1 in method");
  check(getNested483()=="method.", "#483.2 in method");
  value t=Test483();
  check(t.nested483.string=="object.", "#483.1 member (outside)");
  check(t.getNested483()=="method.", "#483.2 member (outside)");
}
