void comprehensions() {
  //Used in initializers
  value s1 = { for (w in {"hello", "world"}) for (c in w) c.string };
  value s2 = { for (w in {"hello", "world"}) for (c in w) if (c in "hw") c.uppercased };
  value s3 = { for (c in "hello") c.string };
  //simple invocation
  assert(array(for (c in "hello") c.string)==array("h", "e", "l", "l", "o"), "comprehensions 1");
  //named arg invocation
  assert(array {
    for (c in "hello") c.string
  } == array("h", "e", "l", "l", "o"), "comprehensions 2");
  assert(s1=={ "h", "e", "l", "l", "o", "w", "o", "r", "l", "d" }, "comprehensions 3");
  assert(s2=={ `H`, `W` }, "comprehensions 4");
  assert(s3=={ "h", "e", "l", "l", "o" }, "comprehensions 5");
  assert({for (y in 1..5) for (x in 1..5) if (x>y) x*y}=={2, 3, 4, 5, 6, 8, 10, 12, 15, 20}, "comprehensions 6");
  assert({for (x in 1..6) if (x % 2 == 0) for (y in 1..3) x*y}=={2,4,6,4,8,12,6,12,18}, "comprehensions 7");
  assert({for (x in 1..6) if (x % 2 == 0) for (y in 1..3) if ((x*y)%3==0) x*y}=={6,12,6,12,18}, "comprehensions 8");
  assert({for (x in 1..10) if (x%2==0) if (x>5) x}=={6,8,10}, "comprehensions 9");
  variable Object check := s1;
  assert(is Iterable<Void> check, "comprehension is Iterable");
  assert({for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>5) x}=={"goodbye"}, "comprehensions w/exists 1");
  assert({for (x in {"a", "", "c"}) if (exists c=x[0]) c.uppercased}=={`A`, `C`}, "comprehensions w/exists 2");
  assert({for (x in {"a", "", "c"}) if (nonempty x) x.uppercased}=={"A", "C"}, "comprehensions w/nonempty 1");
  assert({for (x in {"a", "", "c"}) if (nonempty s=x) s.uppercased}=={"A", "C"}, "comprehensions w/nonempty 2");
  assert({for (x in {1,2,"3.1",4}) if (is String x) x}=={"3.1"}, "comprehensions w/is 1");
  assert({for (x in {1.1,2.2,3,4.4}) if (is Integer i=x) i}=={3}, "comprehensions w/is 2");
  assert(array {for (k->v in entries("a","b","c","d","e")) if (k%2==0) v.uppercased}==array("A","C","E"), "key-value comprehensions");

  //new comprehension-related functions
  assert(any(for (x in 1..5) x>4), "any");
  assert(every(for (x in 1..5) x>0), "every");
  if (exists ff=first(for (x in 1..5) if (x>3) x)) {
    assert(ff==4, "first [1]");
  } else { fail("first [1]"); }
  assert(!exists first(for (x in 1..5) if (x%6==0) x), "first [2]");
  assert(count { for (x in 1..5) x>4 } == 1, "count [1]");
  assert(count(for (x in 1..5) x>0) == 5, "count [2]");

  //*************Newly found bugs here
  //ceylon-compiler#598
  value b598 = { for (x in 0..10) if (x%2==0) x**2 };
  assert(b598 is Sequence<Void>, "ceylon-compiler #598 [1]");
  assert(b598.string=="{ 0, 4, 16, 36, 64, 100 }", "ceylon-compiler #598 [2]");
  //ceylon-compiler#599
  value b599_1 = elements { for (x in "hello") x };
  value b599_2 = {b599_1...};
  assert(b599_2 is Sequence<Void>, "ceylon-compiler #599 [1]");
  assert(b599_1.sequence == b599_2, "ceylon-compiler #599 [2]");
  //ceylon-compiler#601
  Iterable<String>? b601 = first({ for (s in "hello world".split()) s}, {""});
  if (exists b601) {
    assert(b601=={"hello", "world"}, "ceylon-compiler #601 [1]");
  } else { fail("ceylon-compiler #601 [2]"); }
  Iterable<String> b85 = elements { for (k->v in entries("a","b","c","d","e")) if (k%2==0) v.uppercased };
  Iterator<String> iter85 = b85.iterator;
  if (is String x=iter85.next()) {
    assert(x=="A", "ceylon-language #85");
  } else { fail("ceylon-language #85"); }
  if (is String x=iter85.next()) {
    assert(x=="C", "ceylon-language #85");
  } else { fail("ceylon-language #85"); }
  if (is String x=iter85.next()) {
    assert(x=="E", "ceylon-language #85");
  } else { fail("ceylon-language #85"); }
  if (is String x=iter85.next()) {
    fail("ceylon-language #85");
  }
}
