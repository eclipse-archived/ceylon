import check {check}

class Test6439(Object x) {
  //s and s2 should become attributes
  assert(is {Object*} s=x, is {Character*} s2=s);
  check(s.first exists, "#6439.1");
  check(s2=="ok", "6439.2");
  shared void test() {
    check(s.size == 2, "#6439.3");
    check(s2.first exists, "#6439.4");
    if (is String s3=s, is Character c=s.first) {
      check(s3=="ok", "#6439.5");
      check(c=='o', "#6439.6");
    }
  }
  test();
}

void testAssertions() {
  value t = Test6439("ok");
  t.test();
}
