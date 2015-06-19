import check { check }

class ToplevelBug476 {
  shared Integer x;
  shared new(){
    x=1;
    check(x+1==2, "#484.1");
  }
  shared new bar(){
    value f2 = ToplevelBug476();
    x=2;
    check(x+1==3, "#484.2");
  }
  shared ToplevelBug476 test485_1() {
    return ToplevelBug476.bar();
  }
  shared ToplevelBug476 test485_2() {
    return bar();
  }
}

StringBuilder sb528 = StringBuilder();
class Test528 {
    sb528.append("1");
    shared new c1() {
        sb528.append(", 1.5");
    }
    sb528.append(", 2");
    shared new c2() extends c1() {
        sb528.append(", 2.5");
    }
    sb528.append(", 3");
    shared new c3() extends c1() {
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
    shared new c2() extends Test528Default() {
        sb528.append(", 2.5");
    }
    sb528.append(", 3");
    shared new c3() extends Test528Default() {
        sb528.append(", 3.5");
    }
    sb528.append(", 4");
}

class Test538 {
  Integer x;
  shared new foo(Integer a, Integer b) {
    x=a+b;
    check(a==1 && b==2, "#538.1");
  }
  hash => x;
}

class Test555<F> {
    shared F f;
    shared new bar(F f){
        this.f = f;
        check(`F`==`Integer`, "#555.2");
    }

    shared new (F f) extends bar(f){
        check(`F`==`Integer`, "#555.3");
    }
}

class Parent() {}

class Test557<F> extends Parent {
    sb528.append("1");
    shared F f;
    sb528.append(",2");
    shared new bar(F f) extends Parent() {
        this.f = f;
        check(`F`==`Integer`, "#557.3");
    }
    sb528.append(",3");

    shared new (F f) extends bar(f){
    }
    sb528.append(",4");
}

class Test566(){
    shared class Bar {
        shared new baz(){}
    }
}

class Test565 {
  value sb=StringBuilder();
  sb.append("a");
  abstract new baz(){
    sb.append("b");
  } 
  shared new() extends baz() {
    sb.append("c");
  }
  sb.append("d");
  shared actual String string=>sb.string;
}

class Test540 {
  shared new bar(Integer a=1) {
    check(a==1, "#540");
  }
  shared new() extends bar() {}
}

class Test541 {
  abstract new bar(){
    check(true,"#541");
  }
  shared new() extends bar() {}
}

class Test582<A>{
  shared A a;
  shared new bar(A i){
    check(`A`==`Integer`, "#582");
    a=i;
  }
}

class Test584 {
    shared new foo(){
      check(true,"#584");
    }
}
class Boom584() => Test584.foo();
class Check584() extends Boom584(){}

class Test573 {
    value sb = StringBuilder();
    sb.append("1");
    shared new (){
        sb.append("2");
    }
    shared new bar() extends Test573(){
        sb.append("3");
    }
    shared new baz() extends bar(){
        sb.append("4");
    }
    string => sb.string;
}

class Other573 {
    value sb = StringBuilder().append("1");
    shared new (){
        sb.append("a");
    }
    sb.append("2");
    shared new bar() extends Other573(){
        sb.append("b");
    }
    sb.append("3");
    shared new baz() extends bar(){
        sb.append("c");
    }
    sb.append("4");
    string => sb.string;
}

void testConstructors() {
  check(ToplevelBug476.bar().x==2, "#476 toplevel");
  class NestedBug476 {
    shared Integer x;
    shared new(){
      x=1;
    }
    shared new bar(){
      value f2 = NestedBug476();
      x=2;
    }
  }
  check(NestedBug476.bar().x==2, "#476 nested");
  check(ToplevelBug476().test485_1().x==2, "#485.1");
  check(ToplevelBug476().test485_2().x==2, "#485.2");
  Test528.c1();
  check(sb528.string=="1, 1.5, 2, 3, 4", "#528.1");
  sb528.clear();
  Test528.c2();
  check(sb528.string=="1, 1.5, 2, 2.5, 3, 4", "#528.2");
  sb528.clear();
  Test528.c3();
  check(sb528.string=="1, 1.5, 2, 3, 3.5, 4", "#528.3");
  sb528.clear();
  Test528Default();
  check(sb528.string=="1, 1.5, 2, 3, 4", "#536.1");
  sb528.clear();
  Test528Default.c2();
  check(sb528.string=="1, 1.5, 2, 2.5, 3, 4", "#536.2");
  sb528.clear();
  Test528Default.c3();
  check(sb528.string=="1, 1.5, 2, 3, 3.5, 4", "#536.3");
  sb528.clear();
  check(curry(Test538.foo)(1)(2).hash == 3, "#538.2");
  check(Test555(42).f==42, "#555.1");
  sb528.clear();
  Test557(1);
  check(sb528.string=="1,2,3,4", "#557.1 expected 1,2,3,4 got ``sb528``");
  sb528.clear();
  Test557.bar(1);
  check(sb528.string=="1,2,3,4", "#557.2 expected 1,2,3,4 got ``sb528``");
  sb528.clear();
  Anything test566 = Test566().Bar.baz();
  check(test566 exists, "#566");
  class Test564{ 
    shared new bar(){
      check(true, "#564");
    }
  }
  (Test564.bar)();
  check(Test565().string=="abcd", "#565");
  Test541();
  Test540();
  value test582 = object extends Test582<Integer>.bar(42){};
  check(test582.a==42);
  Check584();
  check(Test573().string=="12", "#573.1 expected cd got ``Test573()``");
  check(Test573.bar().string=="123", "#573.2 expected cde got ``Test573.bar()``");
  check(Test573.baz().string=="1234", "#573.3 expected cdef got ``Test573.baz()``");
  check(Other573().string=="1a234", "#573.4 expected cd got ``Other573()``");
  check(Other573.bar().string=="1a2b34", "#573.5 expected cde got ``Other573.bar()``");
  check(Other573.baz().string=="1a2b3c4", "#573.6 expected cdef got ``Other573.baz()``");
}
