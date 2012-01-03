shared class Outer(String name) {
    value int = 10;
    shared Float float = int.float;
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
    }
    value inner = Inner();
    print(inner.int);
    print(inner.float);
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
    
shared void test() {
    outr("Hello");
    print(Holder("ok").get());
    print(Holder("ok"));
    print(Wrapper().get());
    print(Wrapper());
    print(Unwrapper().get());
    print(Unwrapper().o);
    print(Unwrapper());
    print(producer()());
    print(returner("something")());
    //Outer("Hello");
}