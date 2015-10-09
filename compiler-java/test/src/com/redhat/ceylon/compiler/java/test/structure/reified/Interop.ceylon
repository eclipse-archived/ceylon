native("jvm") class CeylonClass<Element>()
    extends JavaClass<Element>()
    satisfies JavaInterface<Element>{
    
    shared actual void foo<T>() given T satisfies Object{} 
}

native("jvm") interface CeylonInterface<Element> satisfies JavaInterface<Element>{}

native("jvm") void reifiedInstantiateInterop(){
    value c = CeylonClass<Integer>();
    c.foo();
    c.foo{};
    value f = c.foo<Integer>;
    f();
    value c2 = JavaClass<Integer>();
    value constr = JavaClass<Integer>;
    value c3 = constr();
}