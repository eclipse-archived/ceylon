class Union() {
    String[] strings = {};
    Iterator<String>? it1 = strings.iterator;
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
        print(s);
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
            @error if (is String s) {}
            s.join();
        }
    }
    
    Sequence<String>|Sequence<Integer> sssn = { "hello" };
    String|Integer sssnf = sssn.first;
    
    function first<T>(T... args) {
        if (nonempty seq = args.sequence) {
            return seq.first;
        }
        else {
            throw;
        }
    }
    @type["String|Integer|Float"] value ff1 = first({"hello", "world"}, {+1, -1}, {1.0}).first;
    @type["String|Integer|Float"] value ff2 = first({"hello", "world"}, {+1, -1, 1.0}).first;
    
    class Outer<out T>() {
        shared default class Inner<out U>(u) {
            shared String hello = "hello";
            shared U u;
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
    class ReallySpecialOuter<out T>() extends Outer<T>() {
        shared actual class Inner<out U>(U u) 
        extends super.Inner<U>(u) {}
    }
    
    Outer<String>.Inner<Integer>|Outer<Float>.Inner<Float> foobar1 = Outer<String>().Inner<Integer>(1);
    String foobarhello1 = foobar1.hello;
    @type["Integer|Float"] value foobaru1 = foobar1.u;
    Outer<String|Float>.Inner<Integer|Float> foobart1 = foobar1;
    @type["Union.Outer<String|Float>.Inner<Integer|Float>"] value foobarts1 = foobar1.get;
    
    SubOuter<String>.Inner<Integer>|Outer<Float>.Inner<Float> foobar2 = SubOuter<String>().Inner<Integer>(1);
    String foobarhello2 = foobar2.hello;
    @type["Integer|Float"] value foobaru2 = foobar2.u;
    Outer<String|Float>.Inner<Integer|Float> foobart2 = foobar2;
    @type["Union.Outer<String|Float>.Inner<Integer|Float>"] value foobarts2 = foobar2.get;
    
    SubOuter<String>.Inner<Integer>|SpecialOuter<Float>.Inner<Float> foobar3 = SubOuter<String>().Inner<Integer>(1);
    String foobarhello3 = foobar3.hello;
    @type["Integer|Float"] value foobaru3 = foobar3.u;
    Outer<String|Float>.Inner<Integer|Float> foobart3 = foobar3;
    @type["Union.Outer<String|Float>.Inner<Integer|Float>"] value foobarts3 = foobar3.get;
    
    SubOuter<Object>.Inner<Integer>|SpecialOuter<Float>.Inner<Object> foobar4 = SubOuter<String>().Inner<Integer>(1);
    @type["Union.SubOuter<Object>.Inner<Integer>|Union.SpecialOuter<Float>.Inner<Object>"] value foobar4check = foobar4;
    String foobarhello4 = foobar4.hello;
    @type["Object"] value foobaru4 = foobar4.u;
    Outer<Object>.Inner<Object> foobart4 = foobar4;
    @type["Union.Outer<Object>.Inner<Object>"] value foobarts4 = foobar4.get;
    
    SubOuter<Object>.Inner<Integer>|ReallySpecialOuter<Float>.Inner<Object> foobar5 = SubOuter<String>().Inner<Integer>(1);
    @type["Union.SubOuter<Object>.Inner<Integer>|Union.ReallySpecialOuter<Float>.Inner<Object>"] value foobar5check = foobar5;
    String foobarhello5 = foobar5.hello;
    @type["Object"] value foobaru5 = foobar5.u;
    Outer<Object>.Inner<Object> foobart5 = foobar5;
    @type["Union.Outer<Object>.Inner<Object>"] value foobarts5 = foobar5.get;
    
    class Sorted<out Elem>(Elem... them) 
            given Elem satisfies Comparable<Elem> {
        shared Elem[] elements = them.sequence;
    }
    Sorted<Integer>|Sorted<String> sorted = Sorted(+1,-1);
    @type["Empty|Sequence<Integer|String>"] value elems = sorted.elements;
    
    switch (xy)
    case (is X) {}
    case (is Y) {}
    
    @error switch (h)
    case (is X) {}
    case (is Hello) {}
    
    Sized|Category sc = "hello";
    @error switch (sc)
    case (is Sized) {}
    case (is Category) {}
    
    String? maybe = null;
    switch (maybe)
    case (is String) {}
    case (is Nothing) {}

    //@error 
    switch (maybe)
    case (is Object) {
        @type["String"] value ms = maybe;
    }
    case (is Nothing) {}
    
    switch (maybe)
    case (is String|Nothing) {}
    
    @error switch (maybe)
    case (is String) {}
    case (is String|Nothing) {}
    
    Comparable<String> elem1 = "hello";
    String elem2 = "world";
    @type["Sequence<String>"] value selfTypeSeq1 = { elem1, elem2 };
    @type["Sequence<String>"] value selfTypeSeq2 = { elem2, elem1 };
    @type["Sequence<String>"] value selfTypeSeq3 = true then selfTypeSeq1 else selfTypeSeq2;
    @type["Sequence<String>"] value selfTypeSeq4 = true then selfTypeSeq2 else selfTypeSeq1;
    Sequence<String> selfTypeSeq5 = selfTypeSeq1;
    Sequence<String> selfTypeSeq6 = selfTypeSeq2;
    Sequence<Comparable<String>> selfTypeSeq7 = selfTypeSeq1;
    Sequence<Comparable<String>> selfTypeSeq8 = selfTypeSeq2;
    
    for (String s in {elem1, elem2}) {}
    for (String s in {elem2, elem1}) {}
    for (Comparable<String> s in {elem1, elem2}) {}
    for (Comparable<String> s in {elem2, elem1}) {}
    
    for (s in {elem1, elem1}) {
        @type["Comparable<String>"] value sss = s;
    }
    for (s in {elem2, elem2}) {
        @type["String"] value sss = s;
    }
    for (s in {elem1, elem2}) {
        @type["String"] value sss = s;
    }
    for (s in {elem2, elem1}) {
        @type["String"] value sss = s;
    }
    
}