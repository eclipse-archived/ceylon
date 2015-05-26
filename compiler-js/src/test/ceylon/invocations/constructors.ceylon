import check { check }

class ToplevelBug476 {
  shared Integer x;
  shared new(){
    x=1;
    check(x+1==2, "#484.1");
  }
  shared new Bar(){
    value f2 = ToplevelBug476();
    x=2;
    check(x+1==3, "#484.2");
  }
  shared ToplevelBug476 test485_1() {
    return ToplevelBug476.Bar();
  }
  shared ToplevelBug476 test485_2() {
    return Bar();
  }
}

StringBuilder sb528 = StringBuilder();
class Test528 {
    sb528.append("1");
    shared new C1() {
        sb528.append(", 1.5");
    }
    sb528.append(", 2");
    shared new C2() extends C1() {
        sb528.append(", 2.5");
    }
    sb528.append(", 3");
    shared new C3() extends C1() {
        sb528.append(", 3.5");
    }
    sb528.append(", 4");
}

class Test528Default {
    sb528.append("1");
    shared new () {
        sb528.append(", 1.5");
    }
    sb528.append(", 2");
    shared new C2() extends Test528Default() {
        sb528.append(", 2.5");
    }
    sb528.append(", 3");
    shared new C3() extends Test528Default() {
        sb528.append(", 3.5");
    }
    sb528.append(", 4");
}

class Test538 {
  Integer x;
  shared new Foo(Integer a, Integer b) {
    x=a+b;
    check(a==1 && b==2, "#538.1");
  }
  hash => x;
}

class Test555<F> {
    shared F f;
    shared new Bar(F f){
        this.f = f;
        check(`F`==`Integer`, "#555.2");
    }

    shared new (F f) extends Bar(f){
        check(`F`==`Integer`, "#555.3");
    }
}

class Parent() {}

class Test557<F> extends Parent {
    sb528.append("1");
    shared F f;
    sb528.append(",2");
    shared new Bar(F f) extends Parent() {
        this.f = f;
        print(`F`==`Integer`);
    }
    sb528.append(",3");

    shared new (F f) extends Bar(f){
    }
    sb528.append(",4");
}

class Test566(){
    shared class Bar {
        shared new Baz(){}
    }
}

class Test565 {
  value sb=StringBuilder();
  sb.append("a");
  abstract new Baz(){
    sb.append("b");
  } 
  shared new() extends Baz() {
    sb.append("c");
  }
  sb.append("d");
  shared actual String string=>sb.string;
}

void testConstructors() {
  check(ToplevelBug476.Bar().x==2, "#476 toplevel");
  class NestedBug476 {
    shared Integer x;
    shared new(){
      x=1;
    }
    shared new Bar(){
      value f2 = NestedBug476();
      x=2;
    }
  }
  check(NestedBug476.Bar().x==2, "#476 nested");
  check(ToplevelBug476().test485_1().x==2, "#485.1");
  check(ToplevelBug476().test485_2().x==2, "#485.2");
  Test528.C1();
  check(sb528.string=="1, 1.5, 2, 3, 4", "#528.1");
  sb528.clear();
  Test528.C2();
  check(sb528.string=="1, 1.5, 2, 2.5, 3, 4", "#528.2");
  sb528.clear();
  Test528.C3();
  check(sb528.string=="1, 1.5, 2, 3, 3.5, 4", "#528.3");
  sb528.clear();
  Test528Default();
  check(sb528.string=="1, 1.5, 2, 3, 4", "#536.1");
  sb528.clear();
  Test528Default.C2();
  check(sb528.string=="1, 1.5, 2, 2.5, 3, 4", "#536.2");
  sb528.clear();
  Test528Default.C3();
  check(sb528.string=="1, 1.5, 2, 3, 3.5, 4", "#536.3");
  sb528.clear();
  check(curry(Test538.Foo)(1)(2).hash == 3, "#538.2");
  check(Test555(42).f==42, "#555.1");
  sb528.clear();
  Test557(1);
  check(sb528.string=="1,2,3,4", "#557.1");
  sb528.clear();
  Test557.Bar(1);
  check(sb528.string=="1,2,3,4", "#557.2");
  sb528.clear();
  Anything test566 = Test566().Bar.Baz();
  check(test566 exists, "#566");
  class Test564{ 
    shared new Bar(){
      check(true, "#564");
    }
  }
  (Test564.Bar)();
  check(Test565().string=="abcd", "#565");
}
