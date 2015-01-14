import check { check }
class ToplevelBug476 {
  shared Integer x;
  shared new ToplevelBug476(){
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

void testConstructors() {
  check(ToplevelBug476.Bar().x==2, "#476 toplevel");
  class NestedBug476 {
    shared Integer x;
    shared new NestedBug476(){
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
}
