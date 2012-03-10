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
  assert(testCallable("Hello".initial)=="H", "higher-class 1");
  value ini1 = "Hello".initial;
  assert(testCallable(ini1)=="H", "higher-class 2");
  value ini2 = seq[].initial;
  function ini3(Integer i) = ini2;
  String[] ini4(Integer i) = seq[].initial;
  //assert(ini3(1) == {"H", "W"}, "higher-class 3");
  //assert(ini4(1) == {"H", "W"}, "higher-class 4");
  function ini5(Integer i) = TestCallable("Some string").something;
  assert(ini5(4) == "Some", "higher-class 5");
  assert(testCallable(TestCallable("Moar").something) == "M", "higher-class 6");
  value tc = TestCallable("Less").something;
  assert("Callable" in className(tc), "Callable classname");
  assert(testCallable(tc) == "L", "higher-class 7");
}
