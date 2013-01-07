import check {...}

//Tests for named argument invocations with objects, methods, and getter arguments.
String namedFunc({Integer...} iter, String desc, Boolean match(Integer i)) {
  for (i in iter) {
    if (match(i)) {
      return "" desc ": " i "";
    }
  }
  return "[NOT FOUND]";
}

void testNamedArguments() {
  check(namedFunc {
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
  check(namedFunc {
    object iter satisfies Iterable<Integer> {
      shared actual Iterator<Integer> iterator {
        return { 2, 4, 6, 8, 9, 10 }.iterator;
      }
    }
    String desc {
      return "Odd";
    }
    match = (Integer x) => x%2==1;
  } == "Odd: 9", "named arguments 2");
  check(namedFunc {
    desc="Even";
    match=(Integer x)=>x==2;
    1, 5, 4, 3, 2, 9
  } == "Even: 2", "named arguments 3");
  check(namedFunc {
    desc="Even";
    match=(Integer x)=>x==2;
    for (i in 10..1) i
  } == "Even: 2", "named arguments 4");
}
