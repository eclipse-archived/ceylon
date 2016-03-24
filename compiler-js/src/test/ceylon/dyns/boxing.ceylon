import check { check, fail }

void testArrayBoxing() {
  Array<Integer> ai;
  Array<String> as;
  Array<Integer> ae;
  dynamic {
    ai = dynamic[1,2,3];
    if (exists ai0 = ai[0]) {
      check(ai0==1,"#5825.1");
    } else {
      fail("#5825.1");
    }
    as = dynamic["a", "b", "c"];
    if (exists as1=as[1]) {
      check(as1=="b", "#5825.2");
    } else {
      fail("#5825.2");
    }
    try {
      ae = dynamic[1,"2",3];
    } catch(Throwable t) {
      check("Expected element type ceylon.language::Integer" in t.message, "#5825 element type message ``t.message``");
    }
  }
}
