@noanno
class CtorGenericClass<T> {
    shared new (T? t=null) {
        
    }
    new foo(T? t=null) {
        
    }
    shared void m(T t) {
        CtorGenericClass(t);
        CtorGenericClass{t=t;};
        foo(t);
        foo{t=t;};
    }
    shared void n() {
        package.CtorGenericClass<String>("");
        package.CtorGenericClass<String>{t="";};
        package.CtorGenericClass<String>{};
        package.CtorGenericClass<String>("");
        package.CtorGenericClass<String>{t="";};
        package.CtorGenericClass<String>{};
        package.CtorGenericClass<String>.foo("");
        package.CtorGenericClass<String>.foo{t="";};
        package.CtorGenericClass<String>.foo{};
        
    }
}