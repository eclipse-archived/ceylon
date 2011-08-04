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
        String? fs = s.first;
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
        @error if (exists oabc) {}
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
    
    Sequence<String>|Sequence<Natural> sssn = { "hello" };
    String|Natural sssnf = sssn.first;
    
    function first<T>(T... args) {
        if (nonempty args) {
            return args.first; 
        }
        else {
            throw;
        }
    }
    @type["String|Integer|Float"] value ff1 = first({"hello", "world"}, {+1, -1}, {1.0}).first;
    @type["String|Integer|Float"] value ff2 = first({"hello", "world"}, {+1, -1, 1.0}).first;
    
    class Outer<out T>() {
        shared default class Inner<out U>(U u) {
            shared String hello = "hello";
            shared U u = u;
            shared Inner<U> get {
                return this;
            }
        }
    }
    class SubOuter<out T>() extends Outer<T>() {
        shared actual class Inner<out U>(U u) 
                extends super.Inner<U>(u) {}
    }
    class SpecialOuter<out T>() extends Outer<T>() {}
    
    class BadSubOuter<out T>() extends Outer<T>() {
        shared actual class Inner(String s) 
                extends super.Inner<String>(s) {}
    }
    
    Outer<String>.Inner<Natural>|Outer<Float>.Inner<Integer> foobar1 = Outer<String>().Inner<Natural>(1);
    String foobarhello1 = foobar1.hello;
    @type["Natural|Integer"] value foobaru1 = foobar1.u;
    Outer<String|Float>.Inner<Natural|Integer> foobart1 = foobar1;
    @type["Union.Outer<String|Float>.Inner<Natural|Integer>"] value foobarts1 = foobar1.get;
    
    SubOuter<String>.Inner<Natural>|Outer<Float>.Inner<Integer> foobar2 = SubOuter<String>().Inner<Natural>(1);
    String foobarhello2 = foobar2.hello;
    @type["Natural|Integer"] value foobaru2 = foobar2.u;
    Outer<String|Float>.Inner<Natural|Integer> foobart2 = foobar2;
    @type["Union.Outer<String|Float>.Inner<Natural|Integer>"] value foobarts2 = foobar2.get;
    
    SubOuter<String>.Inner<Natural>|SpecialOuter<Float>.Inner<Integer> foobar3 = SubOuter<String>().Inner<Natural>(1);
    String foobarhello3 = foobar3.hello;
    @type["Natural|Integer"] value foobaru3 = foobar2.u;
    Outer<String|Float>.Inner<Natural|Integer> foobart3 = foobar3;
    @type["Union.Outer<String|Float>.Inner<Natural|Integer>"] value foobarts3 = foobar3.get;
    
}