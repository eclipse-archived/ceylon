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

class Test528 {
    value sb = StringBuilder();
    sb.append("1");
    shared new C1() {
        sb.append(", 1.5");
    }
    sb.append(", 2");
    shared new C2() extends C1() {
        sb.append(", 2.5");
    }
    sb.append(", 3");
    shared new C3() extends C1() {
        sb.append(", 3.5");
    }
    sb.append(", 4");
    string => sb.string;
}

class Test528Default {
    value sb = StringBuilder();
    sb.append("1");
    shared new () {
        sb.append(", 1.5");
    }
    sb.append(", 2");
    shared new C2() extends Test528Default() {
        sb.append(", 2.5");
    }
    sb.append(", 3");
    shared new C3() extends Test528Default() {
        sb.append(", 3.5");
    }
    sb.append(", 4");
    string => sb.string;
}

class Test538 {
  Integer x;
  shared new Foo(Integer a, Integer b) {
    x=a+b;
    check(a==1 && b==2, "#538.1");
  }
  hash => x;
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
  check(Test528.C1().string=="1, 1.5, 2, 3, 4", "#528.1");
  check(Test528.C2().string=="1, 1.5, 2, 2.5, 3, 4", "#528.2");
  check(Test528.C3().string=="1, 1.5, 2, 3, 3.5, 4", "#528.3");
  check(Test528Default().string=="1, 1.5, 2, 3, 4", "#536.1");
  check(Test528Default.C2().string=="1, 1.5, 2, 2.5, 3, 4", "#536.2");
  check(Test528Default.C3().string=="1, 1.5, 2, 3, 3.5, 4", "#536.3");
  check(curry(Test538.Foo)(1)(2).hash == 3, "#538.2");
}
