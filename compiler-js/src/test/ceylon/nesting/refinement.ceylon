import check { check }

//subclasses must refine this
shared class X() {
    abstract shared class RefineTest1() {
        shared formal class Inner() {
            shared String origin = "RefineTest1.Inner";
            shared default String x() {
                return "x and " + y();
            }
            shared formal String y();
        }
        shared String outerx() {
            return Inner().x();
        }
    }
}

//subclasses can use Inner directly
abstract shared class RefineTest2() {
    shared class Inner() {
        shared String hello() { return "hello from RefineTest2.Inner"; }
    }
}

//subclasses can refine Inner
abstract shared class RefineTest3() {
    shared default class Inner() {
        shared default String x() {
            return "x";
        }
    }
}

shared class Y() extends X() {
    shared class SubRef1() extends RefineTest1() {
      shared actual class Inner() extends super.Inner() {
          shared String suborigin = "SubRef1.Inner";
          shared actual String x() {
              return "REFINED " + super.x();
          }
          shared actual String y() {
              return "y";
          }
      }
    }
}

shared class SubRef2() extends RefineTest2() {
    shared String x() {
        return Inner().hello();
    }
}

shared class SubRef3() extends RefineTest3() {
    shared String x() {
        return Inner().x();
    }
}
shared class SubRef31() extends SubRef3() {
    shared actual class Inner() extends super.Inner() {
        shared actual String x() { return "equis"; }
    }
}

void testRefinement() {
    value c1 = Y().SubRef1().Inner();
    check(className(c1) == "nesting::Y.SubRef1.Inner", "classname is " + className(c1));
    check(c1.origin == "RefineTest1.Inner", "refinement [1]");
    check(c1.suborigin == "SubRef1.Inner", "refinement [2]");
    check(c1.x() == "REFINED x and y", "refinement [3]");
    check(c1.x() == Y().SubRef1().outerx(), "refinement [4]");
    check(SubRef2().x() == "hello from RefineTest2.Inner", "refinement [5]");
    check(SubRef3().x() == "x", "refinement [6]");
    check(SubRef31().x() == "equis", "refinement [7]");
}
