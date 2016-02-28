import check { check, fail }

shared abstract class Class350() {
  shared formal dynamic nat;
}
shared class Kid350(dynamic n) extends Class350() {
  shared actual dynamic nat {
    dynamic {
      return n;
    }
  }
}

dynamic f366_1() {
  dynamic {
    return dynamic[a=1;];
  }
}

Object f366_2() {
  dynamic {
    return dynamic[a=2;];
  }
}

dynamic f366_3() {
  dynamic {
    return dynamic[3,4];
  }
}

void issue350() {
    Kid350 i350;
    dynamic {
      i350 = Kid350(dynamic[a=1;b="2";]);
    }
    dynamic {
      //Can leak, because the declaration is dynamic
      check(i350.nat.a == 1, "Issue 350");
    }
}

void issue366() {
    dynamic f1;
    dynamic {
        f1=f366_1();
    }
    dynamic {
      check(f1.a==1, "Issue 366 #1");
    }
    try {
        f366_2();
    } catch (Throwable ex) {
        check("xpected ceylon.language::Object" in ex.message, "Issue 366 #2 msg ``ex.message``");
    }
    try {
        dynamic {
            check({1,2,*f366_1()}.sequence().size==4, "Issue 366 #3.1");
        }
        fail("Issue 366 #3");
    } catch (Throwable ex) {
        check("xpected JS Array" in ex.message, "Issue 366 #3.2 msg ``ex.message``");
    }
    dynamic {
        check({1,2,*f366_3()}.sequence().size == 4, "Issue 366 #4");
    }
}

void issue369() {
  Float r;
  Integer f;
  Float r2;
  dynamic {
    r = \iMath.random();
    f = \iMath.floor(1.5);
    r2 = \iMath.floor(1.1);
  }
  Object x = r;
  Object y = f;
  Object z = r2;
  check(x is Float, "Issue 369 #1");
  check(x is Number<Float>, "Issue 369 #1.1");
  check(z is Float, "Issue 369 #1.2");
  check(z is Number<Float>, "Issue 369 #1.3");
  check(!x is Integer, "Issue 369 #2");
  check(!x is Number<Integer>, "Issue 369 #2.1");
  check(!z is Integer, "Issue 369 #2.2");
  check(!z is Number<Integer>, "Issue 369 #2.3");
  check(y is Integer, "Issue 369 #3");
  check(y is Number<Integer>, "Issue 369 #3.1");
  check(!y is Float, "Issue 369 #4");
  check(!y is Number<Float>, "Issue 369 #4.1");
  //Check it has methods
  check(!r.undefined, "Issue 369 #5");
  check(!r2.undefined, "Issue 369 #5.1");
  check(r.largerThan(-1.0), "Issue 369 #6");
  check(!r2.largerThan(1.0), "Issue 369 #6.1");
  check(f.negated.sign!=f.sign, "Issue 369 #7");
  check(f.largerThan(-1), "Issue 369 #8");
  check(r.fractionalPart>0.0, "Issue 369 #9");
  check(r2.fractionalPart==0.0, "Issue 369 #9.1");
}

void dynamicNumbers() {
  Float f;
  Integer i;
  dynamic {
    f=\iMath.random();
    i=\iMath.random();
  }
  variable Object x = f;
  check(x is Float, "dynamic float #1");
  check(0.0 <= f <= 1.0, "dynamic float #2");
  check(!x is Integer, "dynamic float #3");
  x = i;
  check(x is Integer, "dynamic int #1");
  check(i == 0, "dynamic int #2");
  check(!x is Float, "dynamic int #3");
}

void issue604() {
  dynamic {
    dynamic a="3";
    dynamic b=20;
    try {
      print(b<=>a);
      fail("604.1");
    } catch (Throwable ex) {
      check(true);
    }
    try {
      print(a<=>b);
      fail("604.2");
    } catch (Throwable ex) {
      check(true);
    }
    try {
      print(a+b);
      fail("604.3 string+int");
    } catch (Throwable ex) {
      check(true);
    }
    try {
      print(b+a);
      fail("604.4 int+string");
    } catch (Throwable ex) {
      check(true);
    }
    try {
      check(a-b == -17, "string-int should be native JS subtraction");
    } catch (Throwable ex) {
      fail("#604.5 string-int");
    }
    try {
      print(b-a);
      fail("604.6 int-string");
    } catch (Throwable ex) {
      check(true);
    }
  }
}

shared void issues() {
    issue350();
    issue366();
    issue369();
    dynamicNumbers();
    try {
        dynamic {
            dynamic test381 = 1;
            dynamic check381 = Singleton<String>(test381);
            fail("Singleton<String>(dynamic) should throw");
        }
    } catch (Throwable ex) {
        check("xpected ceylon.language::String" in ex.message, "Issue #381 [1] msg ``ex.message``");
    }
    dynamic {
        test418 = "OK!";
        check(test418 == "OK!", "#418.1");
        test418 = "Again";
        check(test418 == "Again", "#418.2");
        //431
        dynamic i431 = dynamic [for (i in 0:10) i^3];
        check(i431.length == 10, "#431");
        check(!i431 is [Anything*], "#431 is not [Anything*]");
    }
    //#439.1
    try {
        dynamic {
            \iFoo.Bar();
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: Foo");
    }
    //#439.2
    try {
        dynamic {
            foo.bar += 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar -= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar *= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar /= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar %= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    value test465=["a","b"];
    dynamic {
        dynamic n465_1=dynamic[*test465];
        dynamic n465_2=dynamic[1,2,*test465];
        check(n465_1.length==2, "Spread in native array 1");
        check(n465_1[0]=="a", "Spread in native array 2");
        check(n465_1[1]=="b", "Spread in native array 3");
        check(n465_2.length==4, "Spread in native array 4");
        check(n465_2[0]==1, "#465.5");
        check(n465_2[1]==2, "#465.6");
        check(n465_2[2]=="a", "#465.7");
        check(n465_2[3]=="b", "#465.8");
    }
    Float r576;
    dynamic {
        Float n576 = Math.log(100.0);
        r576 = n576 / Math.\iLN10;
    }
    check(r576 == 2.0, "#576");
    /*try {
        dynamic {
            foo.bar |= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar &= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }
    try {
        dynamic {
            foo.bar ~= 1;
        }
    } catch (Exception e) {
        check(e.message == "Undefined or null reference: foo");
    }*/
    issue604();
    issue5952();
    issue5959();
    issue6057();
}

void issue5952() {
  dynamic {
    dynamic a=Test5952.a;
    dynamic b=Test5952.a();
    check(a()=="Test.a","#5952.1");
    check(b=="Test.a","#5952.2");
    dynamic c=Test5952.a.b;
    dynamic d=Test5952.a.b();
    check(c()=="Test.a.b","#5952.3");
    check(d=="Test.a.b","#5952.4");
    //dynamic e=Test5952.a.b;
    dynamic f=Test5952.A.b;
    dynamic g=Test5952.A.b();
    check(f().string=="Test.A.b","#5952.5");
    check(g.string=="Test.A.b","#5952.6 ``g``");
    dynamic h=Test5952.A.B;
    dynamic i=Test5952.A.B();
    check(h().string=="Test.A.B","#5952.7 ``h()``");
    check(i.string=="Test.A.B","#5952.8 ``i``");
  }
}

dynamic A5959 {
  shared formal String m1();
  shared formal Integer p1;
  shared formal B5959 b;
}
dynamic B5959 {
  shared formal String m2();
  shared formal Integer p2;
  shared formal A5959 a;
}
dynamic C5959<T>{
  shared formal T t;
}
dynamic D5959<S> satisfies C5959<String> {
  shared formal S s;
}

void issue5959() {
  dynamic {
    A5959 a = test5959();
    dynamic b = a;
    check(b is B5959, "#5959 1");
    check(b is A5959, "#5959 2");
    check(b.b is A5959, "#5959 3");
    check(b.a is B5959, "#5959 4");
    check(b is A5959&B5959, "#5959 5");
    check(b.b is A5959&B5959, "#5959 6");
    check(b.a is A5959&B5959, "#5959 7");
    C5959<String> c = genC5959();
    check((c of Object) is C5959<String>, "#5959 targs 1");
    D5959<Integer> d = genD5959();
    check((d of Object) is D5959<Integer>, "#5959 targs 2");
    check((d of Object) is C5959<String>, "#5959 targs3");
  }
}

void issue6057() {
  dynamic DefaultObject {}
  dynamic { DefaultObject o = eval("this"); }
  check(identityHash(object extends Basic() {}) > 0, "#6057 1");
}
