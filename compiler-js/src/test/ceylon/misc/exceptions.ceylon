import check { check, fail }

void m1() {
  throw Exception("Catch me!");
}

void m2() {
  m1();
}

void m3() {
  m2();
}

void testStackTrace() {
  try {
    print("Coming up, a strack trace...");
    m3();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
  print("You should have seen a stack trace");
  value e = Exception();
  check(e.suppressed.size==0, "Exception.suppressed 1");
  e.addSuppressed(Exception("another"));
  check(e.suppressed nonempty, "Exception.suppressed 2");
  try {
    dynamic {
      dynamic u = 1;
      u.somethingSomething();
    }
    fail("TypeError expected");
  } catch (Throwable ex) {
    check("Native" in className(ex), "Catch native error");
    check("TypeError" in ex.string, "TypeError expected");
    return;
  }
  fail("TypeError not caught");
}
