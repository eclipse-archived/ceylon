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
    return value{a=1;};
  }
}

Object f366_2() {
  dynamic {
    return value{a=2;};
  }
}

dynamic f366_3() {
  dynamic {
    return value{3,4};
  }
}

void issue350() {
    Kid350 i350;
    dynamic {
      i350 = Kid350(value {a=1;b="2";});
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
    } catch (Exception ex) {
        check("dynamic" in ex.message, "Issue 366 #2");
    }
    try {
        dynamic {
            check({1,2,*f366_1()}.sequence().size==4, "Issue 366 #3.1");
        }
        fail("Issue 366 #3");
    } catch (Exception ex) {
        check("dynamic" in ex.message, "Issue 366 #3.2");
    }
    dynamic {
        check({1,2,*f366_3()}.sequence().size == 4, "Issue 366 #4");
    }
}

void issue369() {
  Float r;
  Integer f;
  dynamic {
    r = \iMath.random();
    f = \iMath.floor(1.5);
  }
  Object x = r;
  Object y = f;
  check(x is Float, "Issue 369 #1");
  check(!x is Integer, "Issue 369 #2");
  check(y is Integer, "Issue 369 #3");
  check(!y is Float, "Issue 369 #4");
  //Check it has methods
  check(!r.undefined, "Issue 369 #5");
  check(r.largerThan(-1.0), "Issue 369 #6");
  check(f.negated.sign!=f.sign, "Issue 369 #7");
  check(f.largerThan(-1), "Issue 369 #8");
  check(r.fractionalPart>0.0, "Issue 369 #9");
}

shared void issues() {
    issue350();
    issue366();
    issue369();
}
