class CtorGenericClass<T> {
    shared T fun(T t) => t;
    shared new (T t) {}
    shared new foo(T t) {}
    shared void m(T t) {
        CtorGenericClass<T> ctor = CtorGenericClass(t);
        CtorGenericClass<T> ctor2 = CtorGenericClass(t);
        CtorGenericClass<T> fooInst = foo(t);
        CtorGenericClass<T>(T) fooFun = foo;
        T(T) funRef = fun;
    }
    shared void n() {
        CtorGenericClass<String> ctor = package.CtorGenericClass<String>("");
        @error CtorGenericClass<String> ctor2 = package.CtorGenericClass<String>.CtorGenericClass("");
        CtorGenericClass<String> foo = package.CtorGenericClass<String>.foo("");
        CtorGenericClass<String>(String) fooFun = package.CtorGenericClass<String>.foo;
        String(String)(CtorGenericClass<String>) fun 
                = package.CtorGenericClass<String>.fun;
    }
}

void testCtorGenericClass() {
    CtorGenericClass<String> ctor = CtorGenericClass<String>("");
    @error CtorGenericClass<String> ctor2 = CtorGenericClass<String>.CtorGenericClass("");
    CtorGenericClass<String> foo = CtorGenericClass<String>.foo("");
    String(String)(CtorGenericClass<String>) fun 
            = CtorGenericClass<String>.fun;
}

class Foo<T> {
    shared new bar() {}
}

class Baz0a extends Foo<List<String>> {
    shared new create() extends Foo<List<String>>.bar() {}
}

class Baz0b extends Foo<Foo<String>> {
    shared new create() extends Foo<Foo<String>>.bar() {}
}

class Baz1 extends Foo<List<String>> {
    @error shared new create() extends Foo<List<Integer>>.bar() {}
}

class Baz2 extends Foo<Foo<String>> {
    @error shared new create() extends Foo<Foo<String>.Bar>.bar() {}
}


void check() {
    value bar1 = `Foo<String>.bar`;
    value bar2 = `new Foo.bar`;
}
