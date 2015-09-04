import check { check }

interface Holder327<T> given T satisfies Object {
  shared formal T t;
}

class Fuera327<T>(T t)
    given T satisfies Object {
  shared class Parent(shared T other) {
  }
  shared class Child(Parent p) satisfies Holder327<T> {
    t = outer.t;
  }
}

shared void testIssues() {
  value a = Fuera327(1);
  value b = a.Parent(2);
  value c = a.Child(b);
  check(c.t == 1, "Issue 327");
}
