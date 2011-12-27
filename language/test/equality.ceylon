class Foo<out T>(T t) given T satisfies Equality {
    T t = t;
    shared actual Boolean equals(Equality that) {
        if (is Foo<Equality> that) {
            return t==that.t;
        }
        else {
            return false;
        }
    }
}

void equality() {
    assert(Foo(1)==Foo(1), "");
    assert(Foo("hi")==Foo("hi"), "");
    assert(Foo(1)!=Foo(2), "");
    assert(Foo(1)!=Foo("hello"), "");
    assert(Foo(0)!=0, "");
    assert(Foo("hello")!="hello", "");
}