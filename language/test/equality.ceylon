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
    assert(Foo(1)==Foo(1), "Foo(1)==Foo(1)");
    assert(Foo("hi")==Foo("hi"), "Foo(hi)==Foo(hi)");
    assert(Foo(1)!=Foo(2), "Foo(1)!=Foo(2)");
    assert(Foo(1)!=Foo("hello"), "Foo(1)!=Foo(hello)");
    assert(Foo(0)!=0, "Foo(0)!=0");
    assert(Foo("hello")!="hello", "Foo(hello)!=hello");
}
