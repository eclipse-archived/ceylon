class Union() {
    String[] strings = {};
    Iterator<String> it1 = strings.iterator;
    Iterable<String> it2 = strings; 

    interface Hello {
        shared formal String hello;
    }
    class X() satisfies Hello {
        shared actual String hello = "hello";
    }
    class Y() satisfies Hello {
        shared actual String hello = "hola";
    }
    
    X|Y xy = X();
    
    String xys = xy.hello;
    Hello h = xy;

    interface Container<out T> {
        shared formal String hello;
    }
    class U() satisfies Container<String> {
        shared actual String hello = "hello";
    }
    class V() satisfies Container<Bottom> {
        shared actual String hello = "hola";
    }
    class W<T>() satisfies Container<T> {
        shared actual String hello = "hi";
    }
    
    U|V|W<String> uv = U();
    
    String uvs = uv.hello;
    Container<String> c = uv;
    
    Container<Bottom> cb = V();
    Container<String> ss  = cb;

    interface BadContainer<T> {
        shared formal String hello;
    }
    class BadU() satisfies BadContainer<String> {
        shared actual String hello = "hello";
    }
    class BadV() satisfies BadContainer<Bottom> {
        shared actual String hello = "hola";
    }
    
    BadU|BadV buv = BadU();
    
    @error String buvs = buv.hello;
    @error Container<String> bc = buv;
    
    BadContainer<Bottom> bcb = BadV();
    @error BadContainer<String> bss  = bcb;
    
    class Foo<T>(T t) {
        shared T|S|String hello<S>(S s) { return t; }
    }
    class Bar(String s) extends Foo<String>(s) {}
    Bar f = Bar("hello");
    String fh = f.hello("hi");
    
    T?[] method<T>() { return {null}; }
    
    String?[] mr = method<String>();
    
    if (exists s = method<String>().first) {
        writeLine(s);
    }
    
    T[]? method2<T>() { return {}; }
    
    String[]? mr2 = method2<String>();
    
    if (exists s = method2<String>()) {
        String? f = s.first;
    }
    
    class S() {
        shared void hello() {}
    }
    class A() {
        shared void hi() {}
    }
    class B() extends S() {}
    class C() extends S() {}
    
    A|B|C abc = A();
    if (is B|C abc) {
        abc.hello();
    }
    
    @type["Union.S|Union.A"] S|A|B|C sabc = S();
    @type["Union.A|Union.S"] A|B|C|S abcs = S();
    
    @type ["Nothing|Union.A|Union.B|Union.C"] A?|B?|C? oabc = null;
    if (is A|B|C oabc) {
        //@error if (exists oabc) {}
    }
    if (is B|C oabc) {
        oabc.hello();
    }
    if (is A? oabc) {
        if (exists oabc) {
            oabc.hi();
        }
    }
    
    String[]? strs = null;
    
    if (is String[] strs) {
        for (s in strs) {}
        if (is Sequence<String> strs) {}
    }
    
    String?[] ostrs = {null};
    
    if (is Sequence<String?> strs) {
        for (s in strs) {
            if (is String s) {}
        }
    }
    
}