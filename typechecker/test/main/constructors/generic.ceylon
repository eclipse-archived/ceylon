class CtorGenericClass<T> {
    shared T fun(T t) => t;
    shared new (T t) {}
    shared new Foo(T t) {}
    shared void m(T t) {
        CtorGenericClass<T> ctor = CtorGenericClass(t);
        CtorGenericClass<T> ctor2 = CtorGenericClass(t);
        CtorGenericClass<T> foo = Foo(t);
        CtorGenericClass<T>(T) fooFun = Foo;
        T(T) funRef = fun;
    }
    shared void n() {
        CtorGenericClass<String> ctor = package.CtorGenericClass<String>("");
        @error CtorGenericClass<String> ctor2 = package.CtorGenericClass<String>.CtorGenericClass("");
        CtorGenericClass<String> foo = package.CtorGenericClass<String>.Foo("");
        CtorGenericClass<String>(String) fooFun = package.CtorGenericClass<String>.Foo;
        String(String)(CtorGenericClass<String>) fun 
                = package.CtorGenericClass<String>.fun;
    }
}

void testCtorGenericClass() {
    CtorGenericClass<String> ctor = CtorGenericClass<String>("");
    @error CtorGenericClass<String> ctor2 = CtorGenericClass<String>.CtorGenericClass("");
    CtorGenericClass<String> foo = CtorGenericClass<String>.Foo("");
    String(String)(CtorGenericClass<String>) fun 
            = CtorGenericClass<String>.fun;
}

class Foo<T> {
    shared new Bar() {}
}

class Baz0a extends Foo<List<String>> {
    shared new New() extends Foo<List<String>>.Bar() {}
}

class Baz0b extends Foo<Foo<String>> {
    shared new New() extends Foo<Foo<String>>.Bar() {}
}

class Baz1 extends Foo<List<String>> {
    @error shared new New() extends Foo<List<Integer>>.Bar() {}
}

class Baz2 extends Foo<Foo<String>> {
    @error shared new New() extends Foo<Foo<String>.Bar>.Bar() {}
}


void check() {
    value bar1 = `Foo<String>.Bar`;
    value bar2 = `new Foo.Bar`;
}
