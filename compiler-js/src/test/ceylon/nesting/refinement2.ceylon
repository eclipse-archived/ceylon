import check { check }

//refinement tests with parameters and type parameters
shared class X2(String a) {
    abstract shared class RefineTest1(Integer b) {
        shared formal class Inner<Element>(Element c)
                given Element satisfies Object {
            shared String origin = "RefineTest1.Inner (" c.string ")";
            shared default String x() {
                return "x and " y() " and a:" a ", b:" b ", c:" c ".";
            }
            shared formal String y();
        }
        shared String outerx() {
            return Inner(a.uppercased).x();
        }
    }
}

//subclasses can use Inner directly
abstract shared class RefineTest4(String d) {
    shared class Inner(Integer e) {
        shared String hello() { return "hello from RefineTest2.Inner with " e "."; }
    }
}

//subclasses can refine Inner
abstract shared class RefineTest5(Integer f) {
    shared default class Inner(String g) {
        shared default String x() {
            return g.repeat(f);
        }
    }
}

shared class Y2(String h) extends X2(h) {
    shared class SubRef1(Integer d) extends RefineTest1(1) {
      shared actual class Inner<Element>(Element d2) extends super.Inner<Element>(d2)
              given Element satisfies Object {
          shared String suborigin = "SubRef1.Inner";
          shared actual String x() {
              return "REFINED " + super.x();
          }
          shared actual String y() {
              return "y" h ",d:" d ",d2:" d2 ".";
          }
      }
    }
}

shared class SubRef4() extends RefineTest4("t4") {
    shared String x() {
        return Inner(5).hello();
    }
}

shared class SubRef5() extends RefineTest5(6) {
    shared String x() {
        return Inner("sr5").x();
    }
}
shared class SubRef51() extends SubRef5() {
    shared actual class Inner(String subg55) extends super.Inner(subg55) {
        shared actual String x() { return "equis" subg55 "."; }
    }
}

void testRefinement2() {
    value c1 = Y2("y2").SubRef1(99).Inner("with parm");
    check(className(c1) == "nesting::Y2.SubRef1.Inner<ceylon.language::String>", "classname is " + className(c1));
    check(c1.origin == "RefineTest1.Inner (with parm)", "refinement [1] " + c1.origin);
    check(c1.suborigin == "SubRef1.Inner", "refinement [2] " + c1.suborigin);
    check(c1.x() == "REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.", "refinement [3] " + c1.x());
    check(Y2("y3").SubRef1(10).outerx() == "REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.", "refinement [4] " + Y2("y3").SubRef1(10).outerx());
    check(SubRef4().x() == "hello from RefineTest2.Inner with 5.", "refinement [5] " + SubRef4().x());
    check(SubRef5().x() == "sr5sr5sr5sr5sr5sr5", "refinement [6] " + SubRef5().x());
    check(SubRef51().x() == "equissr5.", "refinement [7] " + SubRef51().x());
}
