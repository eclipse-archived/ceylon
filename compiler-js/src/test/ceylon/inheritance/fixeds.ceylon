import assert {...}

class TestNone() satisfies None<Integer> {
  shared actual TestNone clone { return this; }
}

class TestSome(Integer i, Integer... items) satisfies Some<Integer> {

  value sap = SequenceBuilder<Integer>();
  sap.append(i);
    sap.appendAll(items...);
  value elems = sap.sequence;

  shared actual Iterator<Integer> iterator { return elems.iterator; }
  shared actual Integer size = elems.size;

  shared actual FixedSized<Integer> rest {
    if (nonempty elems) {
      value sub = elems.rest;
      if (nonempty sub) {
        value s2 = sub.rest;
        if (nonempty s2) {
          return TestSome(sub.first, s2...);
        } else {
          return TestSome(sub.first);
        }
      }
    }
    return TestNone();
  }
  shared actual TestSome clone { return this; }
}


void testFixedSized() {
  value some = TestSome(1,2,3);
  assert(some.first == 1, "Some.first");
  //Now for inherited
  assert(!some.empty, "Some.empty");
  assert(some.size == 3, "Some.size");
  assert(3 in some, "Some.contains");
  assert(some.containsAny(5,3,0), "Some.containsAny");
  assert(some.containsEvery(2,3,1), "Some.containsEvery");
  assert(some.count(1) == 1, "Some.count");
  variable FixedSized<Integer> s2 := some.rest;
  assert(!s2.empty, "Some.empty 2");
  assert(s2.size == 2, "Some.size 2");
  if (is Some<Integer> s3 = s2) {
    s2 := s3.rest;
  } else {
    fail("s2.rest 1");
  }
  assert(nonempty s2, "nonempty Some.rest");
  assert(!s2.empty, "Some.empty 3");
  assert(s2.size == 1, "Some.size 3");
  assert(is Integer s2.first, "Some.first 2");
  if (is Some<Integer> s3 = s2) {
    s2 := s3.rest;
    assert(s3.first == 3, "Some.first 3");
  } else {
    fail("s2.rest 2");
  }
  assert(is TestNone s2, "Some.rest -> empty");
  assert(!nonempty s2, "!nonempty TestNone");
  assert(s2.size == 0, "None.size");
  assert(!exists s2.first, "None.first");
}
