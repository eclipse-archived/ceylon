import check {...}

class TestObjects(Integer a, Integer b, Integer c) satisfies Iterable<Integer> {
  shared actual Iterator<Integer> iterator {
    object iter satisfies Iterator<Integer> {
      variable Integer index=0;
      shared actual Integer|Finished next() {
        index++;
        if (index == 1) { return a; }
        else if (index == 2) { return b; }
        else if (index == 3) { return c; }
        return finished;
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
    check(i==1, "objects 1");
  }
  if (is Integer i=t1.next()) {
    check(i==2, "objects 2");
  }
  if (is Integer i=t2.next()) {
    check(i==1, "objects 3");
  }
  if (is Integer i=t1.next()) {
    check(i==3, "objects 4");
  }
  check(t1.next() is Finished, "objects 5");
  if (is Integer i=t2.next()) {
    check(i==2, "objects 6");
  }
  if (is Integer i=t2.next()) {
    check(i==3, "objects 7");
  }
}
