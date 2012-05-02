import assert {...}

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

void testMisc() {
  assert(TestSized(0).empty, "Sized.empty");
  assert(!TestSized(1).empty, "!Sized.empty");
  value testcat = TestCategory();
  assert(testcat.contains(1), "Category.contains");
  assert(!testcat.contains(0), "!Category.contains");
  assert(testcat.containsEvery(1,2,3,4,5,6,7,8,9,10), "Category.containsEvery");
  assert(!testcat.containsEvery(2,4,6,8,10,11), "!Category.containsEvery");
  assert(testcat.containsAny(30,20,10,50), "Category.containsAny");
  assert(!testcat.containsAny(0,20,30,40,50), "!Category.containsAny");
}
