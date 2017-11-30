import org.eclipse.ceylon.compiler.java.test.structure.reified { JavaClass { StaticMember }}

native("jvm") class CeylonClass<Element>()
    extends JavaClass<Element>()
    satisfies JavaInterface<Element>{
    
    shared actual void foo<T>() given T satisfies Object{} 
}

native("jvm") interface CeylonInterface<Element> satisfies JavaInterface<Element>{}

void foo<T>(){}

native("jvm") void reifiedInstantiateInterop(){
    value c = CeylonClass<Integer>();
    c.foo();
    c.foo{};
    value f = c.foo<Integer>;
    f();
    value c2 = JavaClass<Integer>();
    value constr = JavaClass<Integer>;
    value c3 = constr();
    
    // These three should be the same with a raw container
    foo<JavaClass<String>.StaticMember<Integer>>();
    foo<JavaClass.StaticMember<Integer>>();
    foo<StaticMember<Integer>>();
    // This one must have its non-raw container
    foo<JavaClass<String>.Member<Integer>>();
}