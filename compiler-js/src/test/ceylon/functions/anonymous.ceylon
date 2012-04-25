import assert { ... }

Element? find<Element>(Array<Element> a, Boolean f(Element x)) {
  for (Element e in a) {
    if (f(e)) {
      return e;
    }
  }
  return null;
}

void testAnonymous() {
  print("Testing anonymous functions...");
  value nums = array(1,2,3,4,5);
  //Test positional argument call
  variable value found := find(nums, (Integer i) i%2==0);
  if (exists i=found) {
    assert(i == 2, "anonfunc positional");
  } else { fail("anonfunc positional"); }
  //Named argument call
  found := find{
    function f(Integer i) {
      return i%2==0;
    }
    a=nums;
  };
  if (exists i=found) {
    assert(i == 2, "anonfunc named");
  } else { fail("anonfunc named"); }
}
