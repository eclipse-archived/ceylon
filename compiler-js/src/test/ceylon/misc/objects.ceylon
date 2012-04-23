import assert {...}

class TestObjects(Integer a, Integer b, Integer c) satisfies Iterable<Integer> {
  shared actual Iterator<Integer> iterator {
    object iter satisfies Iterator<Integer> {
      variable Integer index:=0;
      shared actual Integer|Finished next() {
        index++;
        if (index == 1) { return a; }
        else if (index == 2) { return b; }
        else if (index == 3) { return c; }
        return exhausted;
      }
    }
    return iter;
  }
}

void test_objects() {
  print("testing objects");
  value t1 = TestObjects(1,2,3).iterator;
  value t2 = TestObjects(1,2,3).iterator;
  if (is Integer i=t1.next()) {
    assert(i==1, "objects 1");
  }
  if (is Integer i=t1.next()) {
    assert(i==2, "objects 2");
  }
  if (is Integer i=t2.next()) {
    assert(i==1, "objects 3");
  }
  if (is Integer i=t1.next()) {
    assert(i==3, "objects 4");
  }
  assert(is Finished t1.next(), "objects 5");
  if (is Integer i=t2.next()) {
    assert(i==2, "objects 6");
  }
  if (is Integer i=t2.next()) {
    assert(i==3, "objects 7");
  }
}
