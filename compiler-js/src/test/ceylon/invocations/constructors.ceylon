import check { check }
class ToplevelBug476 {
  shared Integer x;
  shared new ToplevelBug476(){
    x=1;
  }
  shared new Bar(){
    value f2 = ToplevelBug476();
    x=2;
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
}
