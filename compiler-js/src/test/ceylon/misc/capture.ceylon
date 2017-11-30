import check { check, fail }

{Integer*} captest1() {
    variable {Integer*} res = {};
    for (i in 1:3) {
        res = res.follow(i*i);
    }
    return res;
}

{Integer*} captest2() {
    variable {Integer*} res = {};
    for (i in 1:3) {
        res = { i*i, *res };
    }
    return res;
}

{Integer*} captest3() {
    variable {Integer*} res = {};
    for (i in 1:3) {
        value res2 = res;
        res = { i*i, *res2 };
    }
    return res;
}

{{Integer*}*} captest4() {
    variable {{Integer*}*} res = {};
    for (i in 1:3) {
        res = res.follow({ i*i});
    }
    return res;
}

class Captest5({Integer*} ints) {
  shared void test() {
    for (i in ints) {
      check(i%2==0, "no capture here");
    }
  }
}

void issue7106() {
  {[String, String]*} array = {
    ["a", "1"],
    ["b", "2"]
  };

  abstract class Foo({String*} child) {
    shared actual String string => "<li>``child``</li>";
  }
  class Li(shared {String*} child) extends Foo(child) {
  }

  check([ for (elt in array)
            Li{"``elt[0]`` -> ``elt[1]``"} ].collect(Li.string) == 
        [ for ([l, d] in array)
            Li{"``l`` -> `` d``"} ].collect(Li.string), "#7106");
}

void testCapture() {
    check(captest1().string == "{ 9, 4, 1 }", "#6698.1");
    if (exists y0 = captest2().first) {
      check(y0==9, "#6698.2");
    } else {
      fail("#6698.2 is empty!");
    }
    check(captest3().string == "{ 9, 4, 1 }", "#6698.3");
    check(captest4().string == "{ { 9 }, { 4 }, { 1 } }", "#6698.4");
    Captest5({2,4}).test();
    issue7106();
}
