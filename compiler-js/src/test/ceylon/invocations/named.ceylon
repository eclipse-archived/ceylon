import assert {...}

//Tests for named argument invocations with objects, methods, and getter arguments.
String namedFunc(Iterable<Integer> iter, String desc, Boolean match(Integer i)) {
  for (i in iter) {
    if (match(i)) {
      return "" desc ": " i "";
    }
  }
  return "[NOT FOUND]";
}

void testNamedArguments() {
  assert(namedFunc {
    object iter satisfies Iterable<Integer> {
      shared actual Iterator<Integer> iterator {
        return { 1, 3, 5, 8, 9 }.iterator;
      }
    }
    String desc {
      return "Even";
    }
    function match(Integer i) {
      return i%2==0;
    }
  } == "Even: 8", "named arguments 1");
  assert(namedFunc {
    object iter satisfies Iterable<Integer> {
      shared actual Iterator<Integer> iterator {
        return { 2, 4, 6, 8, 9, 10 }.iterator;
      }
    }
    String desc {
      return "Odd";
    }
    match = (Integer x) x%2==1;
  } == "Odd: 9", "named arguments 2");
}
