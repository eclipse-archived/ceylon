import assert { ... }

shared void test() {
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
  assert(s1.sequence=={ "h", "e", "l", "l", "o", "w", "o", "r", "l", "d" }, "comprehensions 3");
  assert(s2.sequence=={ `H`, `W` }, "comprehensions 4");
  assert(s3.sequence=={ "h", "e", "l", "l", "o" }, "comprehensions 5");
  assert(array(for (y in 1..5) for (x in 1..5) if (x>y) x*y)==array(2, 3, 4, 5, 6, 8, 10, 12, 15, 20), "comprehensions 6");
  assert(array(for (x in 1..6) if (x % 2 == 0) for (y in 1..3) x*y)==array(2,4,6,4,8,12,6,12,18), "comprehensions 7");
  assert(array(for (x in 1..6) if (x % 2 == 0) for (y in 1..3) if ((x*y)%3==0) x*y)==array(6,12,6,12,18), "comprehensions 8");
  assert(array(for (x in 1..10) if (x%2==0) if (x>5) x)==array(6,8,10), "comprehensions 9");
  variable Object check := s1;
  assert(is Iterable<String> check, "comprehension is Iterable");
  results();
  assert({for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>5) x}=={"goodbye"});
}
