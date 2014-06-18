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

shared void issues() {
    Kid350 i350;
    dynamic {
      i350 = Kid350(value {a=1;b="2";});
    }
    dynamic {
      //Can leak, because the declaration is dynamic
      check(i350.nat.a == 1, "Issue 350");
    }
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
