import check {...}

class TestObjects(Integer a, Integer b, Integer c) satisfies Iterable<Integer> {
  shared actual Iterator<Integer> iterator() {
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

object b411 {
    value bb = a411;
}

object a411 {
    value aa = b411;
}

shared class Fuera455<A>() {
  shared object dentro {
    shared void conv(A t) {
      check(`A`==`String`, "Related to #455 something's wrong");
    }
  }
}

void test_objects() {
  value t1 = TestObjects(1,2,3).iterator();
  value t2 = TestObjects(1,2,3).iterator();
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
  try {
    print(a411);
    fail("Cyclic initialization check failed");
  } catch (InitializationError er) {
    check(er.message == "Cyclic initialization trying to read the value of 'a411' before it was set",
          "InitializationError message ``er.message``");
  }
  try {
    Fuera455<String>().dentro.conv("X");
  } catch (Throwable t) {
    fail("#455");
  }
  class Test497() {
    variable Integer i=1;
    object o { i=5; }
    check(i==5, "#497.2");
  }
  variable Integer test497 = 0;
  object o497 { test497 = 42;}
  check(test497==42, "#497.1");
  Test497();
}

shared object testObject461 {
  shared void test() => check(true, "Import toplevel object");
}
