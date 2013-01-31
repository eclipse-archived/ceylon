import check {...}

//Tests for named argument invocations with objects, methods, and getter arguments.
String namedFunc({Integer*} iter, String desc, Boolean match(Integer i)) {
  for (i in iter) {
    if (match(i)) {
      return "" desc ": " i "";
    }
  }
  return "[NOT FOUND]";
}

//This one used to break when having seq arg but not all defaulted
String order(String product, Integer count=1, Float discount=0.0,
          {String*} comments={}) {
    String commentStr = ", ".join(for (c in comments) "'"c"'");
    return "Order '" product "', quantity " count ", discount "
          discount ", comments: " commentStr "";
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
  check(order { product = "Mouse"; }=="Order 'Mouse', quantity 1, discount 0, comments: ",
        "defaulted & sequenced named [1]");
  check(order { product = "Rhinoceros"; discount = 10.0; }
        =="Order 'Rhinoceros', quantity 1, discount 10, comments: ",
        "defaulted & sequenced named [2]");
  check(order { product = "Bee"; count = 531; "Express delivery", "Send individually" }
        =="Order 'Bee', quantity 531, discount 0, comments: 'Express delivery', 'Send individually'",
        "defaulted & sequenced named [3]");
}
