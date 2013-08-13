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
  } catch (Exception e) {
    e.printStackTrace();
  }
  print("You should have seen a stack trace");
}
