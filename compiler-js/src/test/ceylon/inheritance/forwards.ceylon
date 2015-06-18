import check{...}

class C() satisfies A {
  shared actual String b() { return a()+"b"; }
}
C c = C();
interface A {
  shared String a() { return "a"; }
  shared formal String b();
}

class TopChild578() extends TopParent578.foo(){}
class TopParent578 { shared new foo(){} }

shared void testForwardDeclarations() {
  check(C().b()=="ab", "inherited forward decl 1");
  check(Impl1().b()=="ab", "inherited forward decl 2");
  check(Impl2().b()=="ab", "inherited forward decl 3");
  check("Test468" in test468.string, "#468");
  Object o469_1=create469();
  check(o469_1 is Test469, "#469 toplevel");
  Object o469_2=Outer469().bar();
  check(o469_2 is Outer469.Inner469, "#469 nested");
  check((TopChild578() of Anything) exists, "#578.1");
  class LocalChild578() extends LocalParent578.foo(){}
  class LocalParent578 { shared new foo(){} }
  check((LocalChild578() of Anything) exists, "#578.2");
}

shared Test468 test468=Test468();

shared class Test468 {
  shared new() {}
}

Test469 create469(){
    dynamic{
        return dynamic[];
    }
}
Test469 foo = create469();

dynamic Test469{}

class Outer469() {
  shared Inner469 bar() {
    dynamic {
      return dynamic[a=1;];
    }
  }
  shared dynamic Inner469{}
}
