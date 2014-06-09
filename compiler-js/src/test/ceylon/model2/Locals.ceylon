import ceylon.language.meta.declaration { ClassDeclaration, FunctionDeclaration, ValueDeclaration, SetterDeclaration }
import check { check, fail }

shared class OuterLocalClass<Outer>(){

    ClassDeclaration classDecl1;
    if(true){
        class LocalClass<Inner>(){}
        class LocalClass2() extends LocalClass<String>(){
            shared LocalClass<String>&LocalClass2 f(){ return nothing; }
        }
        classDecl1 = `class LocalClass`;
        check(classDecl1.container == `class OuterLocalClass`, "locals 1");
        check(`class LocalClass2`.container == `class OuterLocalClass`, "locals 2");
    }
    ClassDeclaration classDecl2;
    if(true){
        class LocalClass<Inner>(){}
        classDecl2 = `class LocalClass`;
        check(classDecl2.container == `class OuterLocalClass`, "locals 3");
    }
    check(classDecl1 != classDecl2, "locals 4");
    
    if(true){
        void localMethod(){
            class LocalClassInLocalMethod<Inner>(){}
            value d = `class LocalClassInLocalMethod`;
            assert(is FunctionDeclaration lm = d.container);
            check(lm.name == "localMethod", "locals 5 got ``lm.name`` instead of localMethod");
            check(lm.container == `class OuterLocalClass`, "locals 6");
        }
        localMethod();
        
        variable ClassDeclaration? classDecl3 = null;
        variable ClassDeclaration? classDecl4 = null;
        Integer localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is ValueDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 7");
            check(lm.variable, "locals 8");
            check(lm.container == `class OuterLocalClass`, "locals 9");
            classDecl3 = d;
            return 1;
        }
        assign localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is SetterDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 10");
            check(lm.container == `class OuterLocalClass`, "locals 11");
            classDecl4 = d;
        }
        value attr = localAttribute;
        localAttribute = 1;
        assert(exists cd3 = classDecl3, exists cd4 = classDecl4);
        check(cd3 != cd4, "metamodel eq 6");
    }
    
    variable ClassDeclaration? classDecl3 = null;
    variable ClassDeclaration? classDecl4 = null;
    Integer privateAttribute {
        class LocalClassInLocalAttribute<Inner>(){}
        value d = `class LocalClassInLocalAttribute`;
        assert(is ValueDeclaration lm = d.container);
        check(lm.name == "privateAttribute", "locals 12");
        check(lm.variable, "locals 13");
        check(lm.container == `class OuterLocalClass`, "locals 14");
        classDecl3 = d;
        
        // make sure types contained in getter are supported in type parser
        class LocalClass<Inner>(){}
        class LocalClass2() extends LocalClass<String>(){
            shared LocalClass<String>&LocalClass2 f(){ return nothing; }
        }
        value lc = `class LocalClass`;
        check(lc.container == lm, "locals 15");
        // this causes loading of the return type
        try {
          check(`function LocalClass2.f`.name == "f", "locals 16");
        } catch (Exception e) {
            if ("lexical" in e.message) {
            } else {
                fail("local 16 doesn't work in lexical style");
            }
        }

        return 1;
    }
    assign privateAttribute {
        class LocalClassInLocalAttribute<Inner>(){}
        value d = `class LocalClassInLocalAttribute`;
        assert(is SetterDeclaration lm = d.container);
        check(lm.name == "privateAttribute", "locals 17");
        check(lm.container == `class OuterLocalClass`, "locals 18");
        classDecl4 = d;

        // make sure types contained in setter are supported in type parser
        class LocalClass<Inner>(){}
        class LocalClass2() extends LocalClass<String>(){
            shared LocalClass<String>&LocalClass2 f(){ return nothing; }
        }
        value lc = `class LocalClass`;
        check(lc.container == lm, "locals 19");
        // this causes loading of the return type
        try {
            check(`function LocalClass2.f`.name == "f", "locals 20");
        } catch (Exception ex) {
            if ("lexical" in ex.message) {
                print("local 20 won't pass in lexical style");
            } else {
                fail("locals 20 ``ex.message`` of ``className(ex)``");
            }
        }
    }
    // run getter and setter
    value attr = privateAttribute;
    privateAttribute = 1;
    
    assert(exists cd3 = classDecl3, exists cd4 = classDecl4);
    check(cd3 != cd4, "metamodel eq 7");
    
    // this method turns privateAttribute from a local declaration to a member
    shared void capture(){
        value attr = privateAttribute;
    }

    shared Integer outerAttribute {
        ClassDeclaration classDecl1;
        if(true){
            class LocalClass<Inner>(){}
            class LocalClass2() extends LocalClass<String>(){
                shared LocalClass<String>&LocalClass2 f(){ return nothing; }
            }
            classDecl1 = `class LocalClass`;
            check(classDecl1.container == `value outerAttribute`, "locals 21");
            check(`class LocalClass2`.container == `value outerAttribute`, "locals 22");
        }
        ClassDeclaration classDecl2;
        if(true){
            class LocalClass<Inner>(){}
            classDecl2 = `class LocalClass`;
            check(classDecl2.container == `value outerAttribute`, "locals 23");
        }
        check(classDecl1 != classDecl2, "locals 24");
        
        void localMethod(){
            class LocalClassInLocalMethod<Inner>(){}
            value d = `class LocalClassInLocalMethod`;
            assert(is FunctionDeclaration lm = d.container);
            check(lm.name == "localMethod", "locals 25");
            check(lm.container == `value outerAttribute`, "locals 26");
        }
        localMethod();
        
        Integer localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is ValueDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 27");
            check(lm.container == `value outerAttribute`, "locals 28");
            return 1;
        }
        value attr = localAttribute;

        return 1;
    }
    
    shared void outerMethod<Method>(Object o){
        ClassDeclaration classDecl1;
        if(true){
            class LocalClass<Inner>(){}
            class LocalClass2() extends LocalClass<Method>(){
                shared LocalClass<String>&LocalClass2 f(){ return nothing; }
            }
            classDecl1 = `class LocalClass`;
            check(classDecl1.container == `function outerMethod`, "locals 29");
            check(`class LocalClass2`.container == `function outerMethod`, "locals 30");
        }
        ClassDeclaration classDecl2;
        if(true){
            class LocalClass<Inner>(){}
            classDecl2 = `class LocalClass`;
            check(classDecl2.container == `function outerMethod`, "locals 31");
        }
        check(classDecl1 != classDecl2, "locals 32");
        
        void localMethod(){
            class LocalClassInLocalMethod<Inner>(){}
            value d = `class LocalClassInLocalMethod`;
            assert(is FunctionDeclaration lm = d.container);
            check(lm.name == "localMethod", "locals 33");
            check(lm.container == `function outerMethod`, "locals 34");
        }
        localMethod();
        
        Integer localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is ValueDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 35");
            check(lm.container == `function outerMethod`, "locals 36");
            return 1;
        }
        value attr1 = localAttribute;
        
        reifiedMethod<String>(null);
        value attr2 = outerAttribute;
        
        String str = string;
        Integer h = hash;
    }
    
    void reifiedMethod<Method>(Object? arg){
        class Inner<T>(){}
        
        if(exists arg){
            check(`Method` == `Integer`, "locals 37");
            // it is not an OuterLocalClass<Integer>.reifiedMethod<Integer>.Inner<Integer>
            check(!arg is Inner<Integer>, "37 arg shouldn't be OuterLocalClass<Integer>.reifiedMethod<Integer>.Inner<Integer>");
        }else{
            check(`Method` == `String`, "locals 38");
            // build a OuterLocalClass<Integer>.reifiedMethod<String>.Inner<Integer>
            Object inner = Inner<Integer>();
            check(inner is Inner<Integer>, "38 inner should be OuterLocalClass<Integer>.reifiedMethod<String>.Inner<Integer>");
            reifiedMethod<Integer>(inner);
        }
    }
    
    shared actual String string {
        class Local(){}
        check(`class Local`.container == `value string`, "locals 39");
        return "";
    }

    shared actual Integer hash {
        class Local(){}
        check(`class Local`.container == `value hash`, "locals 40");
        return 1;
    }
}

variable ClassDeclaration? classDecl3 = null;
variable ClassDeclaration? classDecl4 = null;

Integer toplevelAttribute {
    class LocalClassInLocalAttribute<Inner>(){}
    value d = `class LocalClassInLocalAttribute`;
    assert(is ValueDeclaration lm = d.container);
    check(lm.variable, "locals 41");
    check(lm.name == "toplevelAttribute", "locals 42");
    check(lm.container == `package model2`, "locals 43");
    classDecl3 = d;
    
    // make sure types contained in getter are supported in type parser
    class LocalClass<Inner>(){}
    class LocalClass2() extends LocalClass<String>(){
        shared LocalClass<String>&LocalClass2 f(){ return nothing; }
    }
    value lc = `class LocalClass`;
    check(lc.container == lm, "locals 44");
    // this causes loading of the return type
    try {
        check(`function LocalClass2.f`.name == "f", "locals 45");
    } catch (Exception ex) {
        if ("lexical" in ex.message) {
            fail("locals 45 won't work in lexical style");
        } else {
            fail("locals 45 ``ex``");
        }
    }
    
    Object o = LocalClass<Integer>();
    check(o is LocalClass<Integer>,"45 should be LocalClass<Integer>");
    
    return 1;
}
assign toplevelAttribute {
    class LocalClassInLocalAttribute<Inner>(){}
    value d = `class LocalClassInLocalAttribute`;
    assert(is SetterDeclaration lm = d.container);
    check(lm.name == "toplevelAttribute", "locals 46");
    check(lm.container == `package model2`, "locals 47");
    classDecl4 = d;
    
    // make sure types contained in setter are supported in type parser
    class LocalClass<Inner>(){}
    class LocalClass2() extends LocalClass<String>(){
        shared LocalClass<String>&LocalClass2 f(){ return nothing; }
    }
    value lc = `class LocalClass`;
    check(lc.container == lm, "locals 48");
    // this causes loading of the return type
    try {
        check(`function LocalClass2.f`.name == "f", "locals 49");
    } catch (Exception ex) {
        if ("lexical" in ex.message) {
            fail("locals 49 won't work in lexical style");
        } else {
            fail("locals 49: ``ex``");
        }
    }

    Object o = LocalClass<Integer>();
    check(o is LocalClass<Integer>, "48 o should be LocalClass<Integer>");
}

void reifiedMethod<Method>(Object? arg){
    class Inner<T>(){}
    
    if(exists arg){
        check(`Method` == `Integer`, "locals 50");
        // it is not an reifiedMethod<Integer>.Inner<Integer>
        check(!arg is Inner<Integer>, "50 should not be reifiedMethod<Integer>.Inner<Integer>");
    }else{
        check(`Method` == `String`, "locals 51");
        // build a reifiedMethod<String>.Inner<Integer>
        Object inner = Inner<Integer>();
        check(inner is Inner<Integer>, "51 should be reifiedMethod<String>.Inner<Integer>");
        reifiedMethod<Integer>(inner);
    }
}

void testToplevels(){
    // run getter and setter
    value attr = toplevelAttribute;
    toplevelAttribute = 1;
    assert(exists cd3 = classDecl3, exists cd4 = classDecl4);
    check(cd3 != cd4, "metamodel eq 8");
    
    reifiedMethod<String>(null);
}

class Outer<O>(){
    shared class Inner<O>(){}
    shared void f<O>(Object? o){
        class LocalClass<O>(){}
        check(`O` == `String`, "locals 52");
        if(exists o){
            // make sure it is a f<String>.LocalClass<Integer>
            check(o is LocalClass<Integer>, "52 should be f<String>.LocalClass<Integer>");
        }else{
            // if LocalClass.$getType refers to f.O incorrectly as LocalClass.O,
            // this instance will be f<Integer>.LocalClass<Integer>
            // instead of f<String>.LocalClass<Integer>
            f<String>(LocalClass<Integer>());
        }
    }
}

interface OuterInterface<Outer>{
    // make sure these ones have annotations
    Integer privateAttr {
        class Local(){}
        return 1;
    }
    assign privateAttr {
        class Local(){}
    }
    // make sure this one has annotations
    void privateDefaulted(Integer i=1){}
    // make sure this one has no annotation
    shared void defaulted(Integer i=1){}
    
    shared Integer outerAttribute {
        ClassDeclaration classDecl1;
        if(true){
            class LocalClass<Inner>(){}
            class LocalClass2() extends LocalClass<String>(){
                shared LocalClass<String>&LocalClass2 f(){ return nothing; }
            }
            classDecl1 = `class LocalClass`;
            check(classDecl1.container == `value outerAttribute`, "locals 53");
            check(`class LocalClass2`.container == `value outerAttribute`, "locals 54");
        }
        ClassDeclaration classDecl2;
        if(true){
            class LocalClass<Inner>(){}
            classDecl2 = `class LocalClass`;
            check(classDecl2.container == `value outerAttribute`, "locals 55");
        }
        check(classDecl1 != classDecl2, "locals 56");
        
        void localMethod(){
            class LocalClassInLocalMethod<Inner>(){}
            value d = `class LocalClassInLocalMethod`;
            assert(is FunctionDeclaration lm = d.container);
            check(lm.name == "localMethod", "locals 57");
            check(lm.container == `value outerAttribute`, "locals 58");
        }
        localMethod();
        
        Integer localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is ValueDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 59");
            check(lm.container == `value outerAttribute`, "locals 60");
            return 1;
        }
        value attr = localAttribute;
        
        return 1;
    }

    shared void outerMethod<Method>(Object o){
        ClassDeclaration classDecl1;
        if(true){
            class LocalClass<Inner>(){}
            class LocalClass2() extends LocalClass<Method>(){
                shared LocalClass<String>&LocalClass2 f(){ return nothing; }
            }
            classDecl1 = `class LocalClass`;
            check(classDecl1.container == `function outerMethod`, "locals 61");
            check(`class LocalClass2`.container == `function outerMethod`, "locals 62");
        }
        ClassDeclaration classDecl2;
        if(true){
            class LocalClass<Inner>(){}
            classDecl2 = `class LocalClass`;
            check(classDecl2.container == `function outerMethod`, "locals 63");
        }
        check(classDecl1 != classDecl2, "locals 64");
        
        void localMethod(){
            class LocalClassInLocalMethod<Inner>(){}
            value d = `class LocalClassInLocalMethod`;
            assert(is FunctionDeclaration lm = d.container);
            check(lm.name == "localMethod", "locals 65");
            check(lm.container == `function outerMethod`, "locals 66");
        }
        localMethod();
        
        Integer localAttribute {
            class LocalClassInLocalAttribute<Inner>(){}
            value d = `class LocalClassInLocalAttribute`;
            assert(is ValueDeclaration lm = d.container);
            check(lm.name == "localAttribute", "locals 67");
            check(lm.container == `function outerMethod`, "locals 68");
            return 1;
        }
        value attr1 = localAttribute;
        
        reifiedMethod<String>(null);
        
        value outerAttr = outerAttribute;
        
        privateMethodForLocalInterfaces();
    }
    
    void privateMethodForLocalInterfaces(){
        // make sure private methods are loaded for local interfaces
        interface LocalI1 {
            void privateMethod(){
                interface LocalI2{
                }
                check(`interface LocalI2`.container.name == "privateMethod", "locals 69");
            }
            shared void f(){
                privateMethod();
            }
        }
        class LocalC() satisfies LocalI1{}
        LocalC().f();
    }
    
    void reifiedMethod<Method>(Object? arg){
        class Inner<T>(){}
        
        if(exists arg){
            check(`Method` == `Integer`, "locals 70");
            // it is not an OuterInterface<Integer>.reifiedMethod<Integer>.Inner<Integer>
            check(!arg is Inner<Integer>, "70 arg shouldn't be OuterInterface<Integer>.reifiedMethod<Integer>.Inner<Integer>");
        }else{
            check(`Method` == `String`, "locals 71");
            // build a OuterInterface<Integer>.reifiedMethod<String>.Inner<Integer>
            Object inner = Inner<Integer>();
            check(inner is Inner<Integer>, "71 inner should be OuterInterface<Integer>.reifiedMethod<String>.Inner<Integer>");
            reifiedMethod<Integer>(inner);
        }
    }
}

class OuterInterfaceImpl<T>() satisfies OuterInterface<T>{}

void metamodelEquality(){
    variable Object? value1 = null;
    variable Object? value2 = null;
    variable Object? method1 = null;
    variable Object? method2 = null;
    variable Object? class1 = null;
    variable Object? class2 = null;
    variable Object? interface1 = null;
    variable Object? interface2 = null;
    variable Object? setter1 = null;
    variable Object? setter2 = null;
    if(true){
        Integer getter {
            class C(){}
            interface I{}
            class1 = `class C`;
            interface1 = `interface I`;
            value1 = `class C`.container;
            return 1;
        }
        assign getter {
            class C(){}
            setter1 = `class C`.container;
        }
        void m(){
            class C(){}
            method1 = `class C`.container;
        }
        Integer i = getter;
        getter = 1;
        m();
    }
    if(true){
        Integer getter {
            class C(){}
            interface I{}
            class2 = `class C`;
            interface2 = `interface I`;
            value2 = `class C`.container;
            return 1;
        }
        assign getter {
            class C(){}
            setter2 = `class C`.container;
        }
        void m(){
            class C(){}
            method2 = `class C`.container;
        }
        Integer i = getter;
        getter = 1;
        m();
    }
    assert(exists c1=class1, exists c2=class2);
    check(c1 != c2, "metamodel eq 1");
    assert(exists i1=interface1, exists i2=interface2);
    check(i1 != i2, "metamodel eq 2");
    assert(exists v1=value1, exists v2=value2);
    check(v1 != v2, "metamodel eq 3");
    assert(exists s1=setter1, exists s2=setter2);
    check(s1 != s2, "metamodel eq 4");
    assert(exists m1=method1, exists m2=method2);
    check(m1 != m2, "metamodel eq 5");
}

void localWithoutTypes(){
    void localFunction(){}
    Integer localGetter {
        return 1;
    }
}

class C(){
    shared object l{}
}

shared void locals(){
    OuterLocalClass<Integer>().outerMethod<String>(1);
    OuterInterfaceImpl<Integer>().outerMethod<String>(1);
    testToplevels();
    value o = Outer<String>();
    Object i = o.Inner<Integer>();
    check(i is Outer<String>.Inner<Integer>,"locals 74");
    o.f<String>(null);
    metamodelEquality();
    // make sure we can load the getter for l
    try {
      check(`value C.l`.name == "l", "locals 72");
    } catch (Exception ex) {
      if ("reference not found" in ex.message) {
        fail("locals 72 won't work in lexical style");
      } else {
        throw ex;
      }
    }
    value m1 = MapTest<Integer, String>(1->"A", 2->"B", 3->"C", 4->"B");
    check(m1.clone() == m1, "locals 73");

}

final annotation class Foo() satisfies OptionalAnnotation<Foo> {}

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
        given Key satisfies Object
        given Item satisfies Object {
    shared formal Entry<Key, Item>[] entries;
}

class MapTest<Key, Item>(<Key->Item>* entry)
        extends Object()
        satisfies MapTestBase<Key, Item>
        given Key satisfies Object
        given Item satisfies Object {
    shared actual Entry<Key, Item>[] entries = entry.sequence();
    shared actual Integer size { return entries.size; }
    shared actual Boolean empty { return entries.empty; }
    shared actual MapTest<Key, Item> clone() { return this; }
    shared actual Iterator<Key->Item> iterator() { return entries.iterator(); }
    shared actual Item? get(Object key) {
        for (e in entries) {
            if (e.key == key) { return e.item; }
        }
        return null;
    }
}
