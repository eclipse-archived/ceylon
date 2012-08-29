class Foo<out T>(T t) given T satisfies Object {
    shared actual Boolean equals(Object that) {
        if (is Foo<Object> that) {
            return t==that.t;
        }
        else {
            return false;
        }
    }
}

void equality() {
    check(Foo(1)==Foo(1), "Foo(1)==Foo(1)");
    check(Foo("hi")==Foo("hi"), "Foo(hi)==Foo(hi)");
    check(Foo(1)!=Foo(2), "Foo(1)!=Foo(2)");
    check(Foo(1)!=Foo("hello"), "Foo(1)!=Foo(hello)");
    check(Foo(0)!=0, "Foo(0)!=0");
    check(Foo("hello")!="hello", "Foo(hello)!=hello");
}
