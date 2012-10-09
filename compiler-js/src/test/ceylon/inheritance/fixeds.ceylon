import check {...}

class TestNone() satisfies None<Integer> {
  shared actual TestNone clone { return this; }
  shared actual Nothing last { return null; }
}

class TestSome(Integer i, Integer... items) satisfies Some<Integer> {

  value sap = SequenceBuilder<Integer>();
  sap.append(i);
  sap.appendAll(items...);
  value elems = sap.sequence;

  shared actual Iterator<Integer> iterator { return elems.iterator; }
  shared actual Integer size = elems.size;

  shared actual Integer[] rest {
    if (nonempty elems) {
      value sub = elems.rest;
      if (nonempty sub) {
        value s2 = sub.rest;
        if (nonempty s2) {
          return sub;
        } else {
          return Singleton(sub.first);
        }
      }
    }
    return {};
  }
  shared actual TestSome clone { return this; }
  shared actual Integer last { return elems.last else -1; }
}


void testFixedSized() {
  value some = TestSome(1,2,3);
  check(some.first == 1, "Some.first");
  //Now for inherited
  check(!some.empty, "Some.empty");
  check(some.size == 3, "Some.size");
  check(3 in some, "Some.contains");
  check(some.containsAny(5,3,0), "Some.containsAny");
  check(some.containsEvery(2,3,1), "Some.containsEvery");
  check(some.count(equalTo(1)) == 1, "Some.count");
  variable FixedSized<Integer> s2 := some.rest;
  check(!s2.empty, "Some.empty 2");
  check(s2.size == 2, "Some.size 2");
  if (is Some<Integer> s3 = s2) {
    s2 := TestSome(s3.rest.first else 0, s3.rest.rest...);
  } else {
    fail("s2.rest 1");
  }
  check(nonempty s2, "nonempty Some.rest");
  check(!s2.empty, "Some.empty 3");
  check(s2.size == 1, "Some.size 3");
  check(is Integer s2.first, "Some.first 2");
  if (is Some<Integer> s3 = s2) {
    check(s3.first == 3, "Some.first 3");
    check(!exists s3.rest.first, "empty Some.rest");
    s2 := TestNone();
  } else {
    fail("s2.rest 2");
  }
  check(!nonempty s2, "Some.rest -> empty");
  check(s2.size == 0, "None.size");
  check(!exists s2.first, "None.first");
}
