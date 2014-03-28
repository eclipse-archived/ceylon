import check {...}

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

Callable<Object,[]> producer() {
    Integer o = 123;
    function produce() { return o; }
    return produce;
}
    
Callable<Object,[]> returner(Object o) {
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

shared class NameTest() {
    shared String x = "1"; 
    shared class NameTest() {
        shared String x = "2";
        shared String f() {
            class NameTest() {
                shared String x = "3";
                shared class NameTest() {
                    shared String x = "4";
                }
                shared String f() { return x + this.NameTest().x; }
            }
            return outer.x + x + NameTest().f();
        } 
    }
    shared String f() { return this.NameTest().f(); }
}

shared object nameTest {
    shared String x = "1"; 
    shared object nameTest {
        shared String x = "2";
        shared String f() {
            object nameTest {
                shared String x = "3";
                shared object nameTest {
                    shared String x = "4";
                }
                shared String f() { return x + this.nameTest.x; }
            }
            return outer.x + x + nameTest.f();
        }
    }
    shared interface IfaceInObject satisfies Closeable {}
    shared class ClassInObject<out T>(shared T t) extends Singleton<T>(t)
            given T satisfies Object {}
    shared String f() { return this.nameTest.f(); }
    shared Object mltest1<T>(T t)
        given T satisfies Object => ClassInObject(t);
    shared Object mltest2() {
        object rval satisfies IfaceInObject {
            shared actual void open() {}
            shared actual void close(Throwable? e) {}
        }
        return rval;
    }
}

shared class C1() {
    shared default String x = "1";
    shared class C1() {
        shared default String x = "11"; 
    }
    shared class C3() extends C1() {
        shared actual default String x = "13";
        shared String f() {
            return "``outer.x``-``super.x``-``outer.C1().x``-``x``-``C3().x``";
        }
    }
}
shared class C2() extends C1() {
    shared actual String x = "2";
    shared class C2() extends super.C1() {
        shared actual String x = "22";
        shared class C2() extends C3() {
            shared actual String x = "222";
        }
        shared String f() {
            return "``outer.x``-``C1().x``-``x``-``super.x``-``C3().x``-``C2().x``-``C2().f()``-``C3().f()``";
        }
    } 
}

shared void test() {
    outr("Hello");
    check(Holder("ok").get().string=="ok","holder(ok)");
    check(Holder("ok").string=="ok","holder.string");
    check(Wrapper().get().string=="100","wrapper 1");
    check(Wrapper().string=="100","wrapper 2");
    check(Unwrapper().get().string=="23.56","unwrapper 1");
    check(Unwrapper().o.string=="23.56","unwrapper 2");
    check(Unwrapper().string=="23.56","unwrapper 3");
    check(producer() is Callable<Integer,[]>, "function 1 is ``className(producer())``");
    check(producer()() is Integer, "function 2");
    check(123==producer()(), "function 3");
    check("something"==returner("something")(), "function 4");
    check(A().B().C().foobar()=="foo", "foobar");
    check(A().B().C().quxx()=="qux", "quxx");
    check(A().baz()=="foo", "baz");
    check(O().test1()=="hello", "method instantiating inner class");
    check(O().test2()=="hello", "method accessing inner object");
    check(O().test3()=="hello", "method deriving inner interface");
    check(OuterC1().tst()=="OuterC1.A.tst()", "");
    check(outerf()=="outerf.A.tst()", "");
    check(OuterC2().tst()=="OuterC2.A.tst()", "");
    Outer("Hello");
    check(NameTest().f()=="1234", "Nested class with same name");
    check(nameTest.f()=="1234", "Nested object with same name");
    check(C1().C3().f()=="1-11-11-13-13", "Several nested classes with same name (1)");
    check(C2().C2().f()=="2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13", "Several nested classes with same name (2)");
    testRefinement();
    testRefinement2();
    testIssues();
    results();
}
