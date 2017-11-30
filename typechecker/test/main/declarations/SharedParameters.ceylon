interface SharedParameters {

    class WithSharedParam<T>(shared T t) {
        print(t);
    }

    class ExtendsWithSharedParam() 
            extends WithSharedParam<String>("") {}

    void check() {
        WithSharedParam<String> wsp = ExtendsWithSharedParam();
        String s1 = wsp.t;
        String s2 = ExtendsWithSharedParam().t;
    }
    
    class Foo(bar="baz", options) {
        shared String bar;
        shared String* options;
    }

    class Bar(shared String bar="baz", 
            shared String* options) {}
    
    abstract class Abstract() {
        shared formal String? str;
        shared formal String[] strings;
    }
    
    class Concrete(shared actual String str, 
                   shared actual String* strings) 
            extends Abstract() {}
   

}