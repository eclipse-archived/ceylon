import check {...}

//Miscellaneous inheritance tests (for very simple interfaces)
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
    shared formal String s2(Integer i=7);
}
class ShortcutRefineTest1(String f(Integer i)) satisfies RefineTestBase {
    s1 = "s1";
    s2 = f;
}
class ShortcutRefineTest1a(String f(Integer i)) satisfies RefineTestBase {
    shared actual String s1;
    s1 = "s1";
    shared actual String s2(Integer i);
    s2 = f;
}
class ShortcutRefineTest2(String f(Integer i)) satisfies RefineTestBase {
    s1 => "s1";
    s2(Integer i) => f(i);
}
class ShortcutRefineTest2a(String f(Integer i)) satisfies RefineTestBase {
    shared actual String s1;
    s1 => "s1";
    shared actual String s2(Integer i);
    s2(Integer i) => f(i);
}
class ShortcutRefineTest3(String f(Integer i), Boolean flag) satisfies RefineTestBase {
    shared actual String s1;
    shared actual String s2(Integer i);
    if (flag) {
        s1 => "one";
        s2 = f;
    }
    else {
        s1 => "two";
        s2(Integer i) => "x="(i*i)"";
    }
}

void testMisc() {
  value testcat = TestCategory();
  check(testcat.contains(1), "Category.contains");
  check(!testcat.contains(0), "!Category.contains");
  check(testcat.containsEvery(1,2,3,4,5,6,7,8,9,10), "Category.containsEvery");
  check(!testcat.containsEvery(2,4,6,8,10,11), "!Category.containsEvery");
  check(testcat.containsAny(30,20,10,50), "Category.containsAny");
  check(!testcat.containsAny(0,20,30,40,50), "!Category.containsAny");
  check(IndirectionTestChild().x()=="x+y", "Inheritance");
  
  //TODO: failing tests commented out below. Make them work!
  value reftst1 = ShortcutRefineTest1((Integer i) => "i="i"");
  check(reftst1.s1=="s1", "shortcut attribute refinement");
  check(reftst1.s2(123)=="i=123", "shortcut method refinement");
  //check(reftst1.s2()=="i=7", "shortcut method refinement defaulted");
  value reftst1a = ShortcutRefineTest1a((Integer i) => "i="i"");
  check(reftst1a.s1=="s1", "attribute refinement with specifier");
  check(reftst1a.s2(123)=="i=123", "method refinement with specifier");
  //check(reftst1a.s2()=="i=7", "method refinement defaulted, with specifier");
  value reftst2 = ShortcutRefineTest2((Integer i) => "i="i"");
  check(reftst2.s1=="s1", "shortcut attribute refinement lazy");
  check(reftst2.s2(123)=="i=123", "shortcut method refinement parametrized");
  //check(reftst2.s2()=="i=7", "shortcut method refinement parametrized, defaulted");
  value reftst2a = ShortcutRefineTest2a((Integer i) => "i="i"");
  check(reftst2a.s1=="s1", "attribute refinement lazy, with specifier");
  check(reftst2a.s2(123)=="i=123", "method refinement parametrized, with specifier");
  //check(reftst2a.s2()=="i=7", "method refinement parametrized, defaulted, with specifier");
  value reftst3a = ShortcutRefineTest3((Integer i) => "i="i"", true);
  check(reftst3a.s1=="one", "attribute refinement with conditional specifier");
  check(reftst3a.s2(123)=="i=123", "method refinement with conditional specifier");
  //check(reftst3a.s2()=="i=7", "method refinement defaulted, with conditional specifier");
  value reftst3b = ShortcutRefineTest3((Integer i) => "i="i"", false);
  check(reftst3b.s1=="two", "attribute refinement lazy, with conditional specifier");
  check(reftst3b.s2(123)=="x=15129", "method refinement parametrized, with conditional specifier");
  //check(reftst3b.s2()=="x=49", "method refinement parametrized, defaulted, with conditional specifier");
}
