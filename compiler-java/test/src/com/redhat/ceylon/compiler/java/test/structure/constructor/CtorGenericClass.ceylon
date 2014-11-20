@noanno
class CtorGenericClass<T> {
    shared new CtorGenericClass(T? t=null) {
        
    }
    new Foo(T? t=null) {
        
    }
    shared void m(T t) {
        CtorGenericClass(t);
        CtorGenericClass{t=t;};
        Foo(t);
        Foo{t=t;};
    }
    shared void n() {
        package.CtorGenericClass<String>("");
        package.CtorGenericClass<String>{t="";};
        package.CtorGenericClass<String>.CtorGenericClass("");
        package.CtorGenericClass<String>.CtorGenericClass{t="";};
        package.CtorGenericClass<String>.Foo("");
        package.CtorGenericClass<String>.Foo{t="";};
        
    }
}