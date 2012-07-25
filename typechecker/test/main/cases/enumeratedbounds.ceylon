void qux<T>(T t) 
        given T of String | Integer {
    switch (t)
    case (is String) {
        print(t);
    }
    case (is Integer) {
        print((t/100.0).string);
    }
}

class Foo<T>(T... ti) given T of Float|Integer {
    shared actual String string {
    	T[] ts = ti.sequence;
        switch (ts)
        case(is Empty) { print(ts); return "empty"; }
        case(is Sequence<T>) { print(ts); return "sequence";}
    }
}

void testEnumeratedBounds() {
    qux(20);
    qux("hello");
    @error qux(1.0);
    
    @type["Foo<Integer>"] Foo<Integer>();
    @type["Foo<Float>"] Foo(1.0, 2.0);
    @error Foo("foo", "bar");
    @error Foo<Object>();
}