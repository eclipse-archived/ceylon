class CtorGenericClass<T> {
    shared T fun(T t) => t;
    shared new CtorGenericClass(T t) {}
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
        CtorGenericClass<String> ctor2 = package.CtorGenericClass<String>.CtorGenericClass("");
        CtorGenericClass<String> foo = package.CtorGenericClass<String>.Foo("");
        CtorGenericClass<String>(String) fooFun = package.CtorGenericClass<String>.Foo;
        String(String)(CtorGenericClass<String>) fun 
                = package.CtorGenericClass<String>.fun;
    }
}

void testCtorGenericClass() {
    CtorGenericClass<String> ctor = CtorGenericClass<String>("");
    CtorGenericClass<String> ctor2 = CtorGenericClass<String>.CtorGenericClass("");
    CtorGenericClass<String> foo = CtorGenericClass<String>.Foo("");
    String(String)(CtorGenericClass<String>) fun 
            = CtorGenericClass<String>.fun;
}