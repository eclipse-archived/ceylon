import check {...}

shared void testNarrowElse(String|Integer x) {
  if (is Integer x) {
    check(x > 0, "#448.1");
  } else {
    check(x.uppercased=="HEY", "#448.2");
  }
  if (!is Integer x) {
    check(x.reversed=="yeh", "#448.3");
  } else {
    check(x%2==0, "#448.4");
  }
}
shared void testNotExists(String? x) {
  if (!exists x) {
    String? pn=x;
    check((pn else 1) == 1, "#448.5");
  } else {
    check(x.size == 3, "#448.6");
  }
}
shared void testNotNonempty([Integer*] x) {
  if (!nonempty x) {
    check(x.size==0, "#448.7");
  } else {
    check(x.size==3, "#448.8");
  }
}

shared void testConditions() {
    Boolean a = true;
    Boolean b = false;
    variable value count = 0;
    if (a && b) {
        fail("WTF?");
    } else if (a || b) {
        check(a||b, "boolean 1");
        count++;
    }
    String? c = "X";
    String? d = null;
    if (c exists && d exists) {
        fail("WTF exists");
    } else if (exists c1=c, c1[0] exists) {
        check(c1 == "X", "exists");
        count++;
    }
    String|Integer e = 1;
    if (e is Integer && d exists) {
        fail("WTF is");
    } else if (is Integer e, c exists) {
        check(e>0, "is");
        count++;
    }
    String[] f = ["a","b","c"];
    Integer[] g = [];
    if (f nonempty && g nonempty) {
        fail("WTF nonempty");
    } else if (nonempty f, !f.first.empty) {
        check(f.first.uppercased=="A", "nonempty");
        count++;
    }
    check(count==4, "some conditions were not met: ``count`` instead of 4");
    Integer|String zz = 1;
    assert(is Integer zz2=zz, zz2 > 0);
    check(zz2==1, "special -> boolean");
    //and now some comprehensions
    Sequence<Integer?> seq1 = [ 1, 2, 3, null, 4, 5, null, 6, 7, null, 10];
    value compr1 = { for (i in seq1) if (exists i, i%2==0) i*10 }.sequence();
    check(compr1==[20,40,60,100],"comprehension [1] is ``compr1`` and not [20,40,60,100]");
    value compr2 = [ for (i in seq1) if (exists i, i%2==0) i*10 ];
    check(compr2==[20,40,60,100],"comprehension [2] is ``compr2`` and not [20,40,60,100]");
    // !is
    if (!is String zz) {
        check(zz>0, "!is");
    } else {
        fail("!is");
    }
    if (!is String zz3=zz) {
        check(zz3<2, "!is with variable [1]");
    } else {
        fail("!is (again)");
    }
    assert(!is String zz4=zz, zz4<3);
    //several conditions chained together
    Empty|Sequence<Integer?> seq2 = seq1;
    if (nonempty seq2, exists zz5=seq2.first) {
        check(zz5==1, "nonempty,exists");
    } else { fail("nonempty,exists"); }
    if (nonempty seq2, is Integer zz5=seq2.first, zz5>0) {
        check(zz5==1, "nonempty,is");
    } else { fail("nonempty,is"); }
    if (is String c, c.lowercased=="x") {
        check(true,"is,boolean");
    } else { fail("is,boolean"); }
    if (exists c, nonempty seq2, exists zz5=c[0]) {
        check(zz5=='X',"exists,nonempty,boolean");
    } else { fail("exists,nonempty,boolean"); }
    //448
    testNarrowElse("hey");
    testNarrowElse(2);
    testNotExists(null);
    testNotExists("hey");
    testNotNonempty([]);
    testNotNonempty([1,2,3]);
    //620
    Integer[2]? i620=[1,0];
    if (exists [v6201, v6202] = i620) {
      check(v6202==0, "#620.1");
    } else {
      fail("#620.1");
    }
    Boolean[2]? b620=[true,false];
    if (exists [v6203, v6204] = b620) {
      check(!v6204, "#620.2");
    } else {
      fail("#620.2");
    }

    Integer x5876 = 1;
    Integer y5876 = 2;
    String text5876 = (if (x5876 > 0) then x5876.string + "a " else "") + y5876.string + "b";
    check(text5876 == "1a 2b", "#5876 expected '1a2b' got '``text5876``'");
}
