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

void test628() {
  interface A {
      String ident() => "A";
      shared default String identS() => "As";
      shared interface B satisfies A {
          String ident() => "B";
          shared actual String identS() => "Bs";

          shared String outerIdent() => outer.ident();
          shared String identA1() => super.ident();
          shared String identA2() => (super of A).ident();
          shared String identB() => ident();

          shared String outerIdentS() => outer.identS();
          shared String identA1S() => super.identS();
          shared String identA2S() => (super of A).identS();
          shared String identBS() => identS();

      }
      shared B b => object satisfies B {};
  }
  object a satisfies A {}

  check(a.b.outerIdent()=="A", "#628.1");
  check(a.b.identA1()=="A", "#628.2");
  check(a.b.identA2()=="A", "#628.3");
  check(a.b.identB()=="B",  "#628.4");
  check(a.b.outerIdentS()=="As", "#628.5");
  check(a.b.identA1S()=="As", "#628.6");
  check(a.b.identA2S()=="As", "#628.7");
  check(a.b.identBS()=="Bs",  "#628.8");
}

void test665() {
    interface I {
        shared interface J {}
    }

    class C() satisfies I {
        shared Integer b => 2;
        shared class Middle() {
            shared class D() satisfies J {
                shared Integer ob => b; // TypeError: Cannot read property 'b' of undefined
            }
        }
    }
    check(C().Middle().D().ob == 2, "#665");
}

shared void testIssues() {
  value a = Fuera327(1);
  value b = a.Parent(2);
  value c = a.Child(b);
  check(c.t == 1, "Issue 327");
  test628();
  test665();
}
