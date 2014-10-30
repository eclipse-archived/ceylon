class CtorGenericClass<T> {
    shared T fun(T t) => t;
    shared new CtorGenericClass(T t) {}
    shared new Foo(T t) {}
    shared void m() {
        CtorGenericClass<String> ctor = CtorGenericClass<String>("");
        CtorGenericClass<String> ctor2 = CtorGenericClass<String>.CtorGenericClass("");
        CtorGenericClass<String> foo = CtorGenericClass<String>.Foo("");
        CtorGenericClass<String>(String) fooFun = CtorGenericClass<String>.Foo;
        String(String)(CtorGenericClass<String>) fun 
                = CtorGenericClass<String>.fun;
        
    }
}

void testCtorGenericClass() {
    CtorGenericClass<String> ctor = CtorGenericClass<String>("");
    CtorGenericClass<String> ctor2 = CtorGenericClass<String>.CtorGenericClass("");
    CtorGenericClass<String> foo = CtorGenericClass<String>.Foo("");
    String(String)(CtorGenericClass<String>) fun 
            = CtorGenericClass<String>.fun;
}