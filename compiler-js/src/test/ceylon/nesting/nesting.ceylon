variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}

shared class Outer(String name) {
    value int = 10;
    shared Float float = int.float;
    void noop() {}
    class Inner() {
        void printName() {
            print(name);
        }
        shared Integer int {
            return outer.int;
        }
        shared Float float {
            return outer.float;
        }
        shared void noop() {
            outer.noop();
        }
    }
    value inner = Inner();
    print(inner.int);
    print(inner.float);
    //to fix, get typechecker to mark 
    //Outer.noop() as captured!
    inner.noop();
    noop();
}

shared void outr(String name) {
    String uname = name.uppercased;
    function inr() {
        return name;
    }
    value uinr {
        return uname;
    }
    String result = inr();
    String uresult = uinr;
    print(result);
    print(uresult);
}

shared class Holder(Object o) {
    shared Object get() {
        return o;
    }
    shared actual String string {
        return o.string;
    }
}

shared class Wrapper() {
    Object o = 100;
    shared Object get() {
        return o;
    }
    shared actual String string {
        return o.string;
    }
}

shared class Unwrapper() {
    shared Object o = 23.56;
    shared Object get() {
        return o;
    }
    shared actual String string {
        return o.string;
    }
}

Callable<Object> producer() {
    Object o = 123;
    function produce() { return o; }
    return produce;
}
    
Callable<Object> returner(Object o) {
    function produce() { return o; }
    return produce;
}

class A() {
    String foo = "foo";
    shared class B() {
        String qux = "qux";
        shared class C() {
            shared String foobar() {
                return foo;
            }
            shared String quxx() {
                return qux;
            }
        }
    }
    shared String baz() {
        class Baz() {
            shared String get() {
                return foo;
            }
        }
        return Baz().get();
    }
}

class O() {
    String s = "hello";
    class InnerClass() {
        shared String f() {
            return s;
        }
    }
    object innerObject {
        shared String f() {
            return s;
        }
    }
    interface InnerInterface {
        shared String f() {
            return s;
        }
    }
    shared String test1() {
        return InnerClass().f();
    } 
    shared String test2() {
        return innerObject.f();
    }
    shared String test3() {
        object obj satisfies InnerInterface {}
        return obj.f();
    }
}

class OuterC1() {
    class A() {
        shared String tst() {return "OuterC1.A.tst()"; }
    }
    class B() extends A() {}
    shared String tst() {return B().tst();}
}

String outerf() {
    class A() {
        shared String tst() {return "outerf.A.tst()"; }
    }
    class B() extends A() {}
    return B().tst();
}

class OuterC2() {
    class A() {
        shared String tst() {return "OuterC2.A.tst()"; }
    }
    shared String tst() {
        class B() extends A() {}
        return B().tst();
    }
}

shared void test() {
    outr("Hello");
    assert(Holder("ok").get().string=="ok","holder(ok)");
    assert(Holder("ok").string=="ok","holder.string");
    assert(Wrapper().get().string=="100","wrapper 1");
    assert(Wrapper().string=="100","wrapper 2");
    assert(Unwrapper().get().string=="23.56","unwrapper 1");
    assert(Unwrapper().o.string=="23.56","unwrapper 2");
    assert(Unwrapper().string=="23.56","unwrapper 3");
    //assert(is Callable<Integer> producer(), "function 1");
    assert(is Integer producer()(), "function 2");
    assert(123==producer()(), "function 3");
    assert("something"==returner("something")(), "function 4");
    assert(A().B().C().foobar()=="foo", "foobar");
    assert(A().B().C().quxx()=="qux", "quxx");
    assert(A().baz()=="foo", "baz");
    assert(O().test1()=="hello", "method instantiating inner class");
    assert(O().test2()=="hello", "method accessing inner object");
    assert(O().test3()=="hello", "method deriving inner interface");
    assert(OuterC1().tst()=="OuterC1.A.tst()", "");
    assert(outerf()=="outerf.A.tst()", "");
    assert(OuterC2().tst()=="OuterC2.A.tst()", "");
    Outer("Hello");
    results();
}
