import check { ... }

Element? find<Element>(Array<Element> a, Boolean f(Element x)) {
  for (Element e in a) {
    if (f(e)) {
      return e;
    }
  }
  return null;
}

Element? find2<Element>(Array<Element> a, Boolean f(Element x) => true) {
  for (Element e in a) {
    if (f(e)) {
      return e;
    }
  } else {
    return null;
  }
}

Callable<String, [Integer]> subtract(Integer howMuch) {
  return (Integer i) => (i-howMuch).string;
}

void testAnonymous() {
  print("Testing anonymous functions...");
  value nums = Array{1,2,3,4,5};
  //Test positional argument call
  variable value found = find(nums, (Integer i) => i%2==0);
  if (exists i=found) {
    check(i == 2, "anonfunc positional");
  } else { fail("anonfunc positional"); }
  //Named argument call
  found = find{
    function f(Integer i) {
      return i%2==0;
    }
    a=nums;
  };
  if (exists i=found) {
    check(i == 2, "anonfunc named");
  } else { fail("anonfunc named"); }

  //Gavin's test
  void callFunction(String f(Integer i), String expect) {
    check(f(0)==expect, "anon func returns ``f(0)`` instead of ``expect``");
  }

  function f(Integer i) {
    return (i+12).string;
  }
  callFunction(f, "12");

  callFunction((Integer i) => (i*3).string, "0");

  callFunction {
    expect = "0";
    function f(Integer i) {
      return (i^2).string;
    }
  };

  value f2 = (Integer i) => (i-10).string;
  callFunction(f2, "-10");
  callFunction(subtract(5), "-5");
  //As defaulted param
  found = find2(nums, (Integer i) => i>2);
  if (exists i=found) {
    check(i==3, "anonfunc i>2 [1]");
  } else { fail("anonfunc i>2 [2]"); }
  found = find2(nums);
  if (exists i=found) {
    check(i==1, "anonfunc defaulted param [1]");
  } else { fail("anonfunc defaulted param [2]"); }
}

shared interface LeaveThereHereForMetamodelTests{}
