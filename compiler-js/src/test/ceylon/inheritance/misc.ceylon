import check {...}

//Miscellaneous inheritance tests (for very simple interfaces)
class TestSized(size) satisfies Sized {
  shared actual Integer size;
}

class TestCategory() satisfies Category {
  shared actual Boolean contains(Object x) {
    if (is Integer x) {
      return x > 0 && x<=10;
    }
    return false;
  }
}

abstract class IndirectionTestParent() {
    shared String x() {
        return "x+"+y();
    }
    shared formal String y();
}
class IndirectionTestChild() extends IndirectionTestParent() {
    shared actual String y() {
        return "y";
    }
}

interface RefineTestBase {
    shared formal String s1;
    shared formal String s2(Integer i);
}
class ShortcutRefineTest(String(Integer) f) satisfies RefineTestBase {
    s1 = "s1";
    s2 = f;
}

void testMisc() {
  check(TestSized(0).empty, "Sized.empty");
  check(!TestSized(1).empty, "!Sized.empty");
  value testcat = TestCategory();
  check(testcat.contains(1), "Category.contains");
  check(!testcat.contains(0), "!Category.contains");
  check(testcat.containsEvery(1,2,3,4,5,6,7,8,9,10), "Category.containsEvery");
  check(!testcat.containsEvery(2,4,6,8,10,11), "!Category.containsEvery");
  check(testcat.containsAny(30,20,10,50), "Category.containsAny");
  check(!testcat.containsAny(0,20,30,40,50), "!Category.containsAny");
  check(IndirectionTestChild().x()=="x+y", "Inheritance");
  value reftst = ShortcutRefineTest((Integer i) => "i="i"");
  check(reftst.s1=="s1", "shortcut attribute refinement");
  check(reftst.s2(123)=="i=123", "shortcut method refinement");
}
