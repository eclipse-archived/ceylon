String testCallable(String f(Integer i)) {
  return f(1);
}

class TestCallable(String what) {
  shared String something(Integer index) {
    return what.initial(index);
  }
}

void callables() {
  value seq = {"Hello", "World"};
  check(testCallable("Hello".initial)=="H", "higher-class 1");
  value ini1 = "Hello".initial;
  check(testCallable(ini1)=="H", "higher-class 2");
  /*
  value ini2 = seq[].initial;
  function ini3(Integer i) = ini2;
  String[] ini4(Integer i) = seq[].initial;
  check(ini3(1) == {"H", "W"}, "higher-class 3 (ceylon-js #53)");
  check(ini4(1) == {"H", "W"}, "higher-class 4 (ceylon-js #53)");
  check("Callable" in className(ini2), "Spread Callable className");
  */
  function ini5(Integer i) => TestCallable("Some string").something(i);
  check(ini5(4) == "Some", "higher-class 5");
  check(testCallable(TestCallable("Moar").something) == "M", "higher-class 6");
  value tc = TestCallable("Less").something;
  check("callable" in className(tc).lowercased, "Callable classname is " + className(tc));
  check(testCallable(tc) == "L", "higher-class 7");
  
  TestCallable(String) clazz = TestCallable;
  TestCallable inst = clazz("hello");
  String(Integer) meth = inst.something;
  Object(Bottom) clazzSuper = TestCallable;
  Void(Bottom) methSuper = inst.something;
  function noop(Object x, Object y) { return x; }
  Object(String,Integer) noopRef = noop;
  check("hello"==noopRef("hello",2), "Callable contravariance");

  //From #56
  void resolve(Integer g()) {
    value which=g();
    check(which==2, "closures: callable returns " which " instead of 2");
  }
  variable Callable<Integer,[]>? f = null;
  for (i in 1..2) {
    if (i > 0) {
      value j = i*2;
      if (i == 1) {
        Integer g() { return j; }
        f = g;
      }
    }
  }
  if (exists g=f) {
    resolve(g);
  } else {
    fail("WTF this should never happen!");
  }
  f=null;
  for (i in 1..5) {
    if (i > 0) {
      variable value j = 0;
      if (i == 2) {
        Integer g() { return j/2; }
        f = g;
      }
      j = i*2;
    }
  }
  if (exists g=f) { resolve(g); }
  else { fail("WTF g doesn't exist"); }
}
