String upper(String s) {
   return s.uppercased;
}

void print(String s) {}

class X(String s) {
    shared class Y() {}
    shared void hello() {}
}

abstract class Z() {} 

void noop() {}

void higher1(String[] strings, Callable<Void,String> f) {
    void g(String str) = f;
    for (s in strings) {
        g(s);
    }
}

Y pass<X,Y>(Y f(X x), X x) {
    return f(x);
}

void higher2(String[] strings, void f(String str)) {
    for (s in strings) {
        f(s);
    }
    @error f(1);
    @error f();
    @error f("hello", 2);
}

String str(Float f) { return f.string; }

Float curried(Integer x)(Float y) {
    return x+y;
}

X->Y generic<X,Y>(Y f(X x), X x()) 
        given X satisfies Object 
        given Y satisfies Object { 
    X xx = x();
    return xx->f(xx);
}

T do<T>(T f()) { return f(); }

void method() {
    Callable<String,String> upperRef = upper;
    Callable<Void,String> printRef = print;    
    Callable<X,String> xRef = X;
    X x = X("hello");
    Callable<X.Y> yRef = x.Y;
    Callable<Void> helloRef = x.hello;
    Callable<Void> noopRef = noop;
    
    higher1({"hello", "world"}, print);
    higher1({"hello", "world"}, upper);
    higher1({"hello", "world"}, X);
    @error higher1({"hello", "world"}, noop);
    @error higher1({"hello", "world"}, str);
    
    higher2({"hello", "world"}, print);
    higher2({"hello", "world"}, upper);
    higher2({"hello", "world"}, X);
    @error higher2({"hello", "world"}, noop);
    @error higher2({"hello", "world"}, str);
    
    @type["String"] function up(String s) = upper;
    void pr(String s) = print;
    void np() = noop;
    
    @type["String"] up("hello");
    @type["Void"] pr("hello");
    @type["Void"] np();
    
    @type["X"] function good(String s) = X;
    X better(String s) = X;
    @type["X"] @error function bad() = X;
    @type["X"] function badder(@error Integer n) = X;
    @error String worse(String s) = X;
    @error String worst() = X;
    @error void broke() = noop();
    @error Z moreBroke() = Z;
    @error do(Z);
    @type["Void"] function z() { return Z; }
    
    String s1 = pass((String s) s, "hello");
    String s2 = pass((Float f) f.string, 1.0);
    @error String s3 = pass((Float f) f.string, "hello");
    
    higher2 { 
        strings = {"goodbye"};
        void f(String s) { print(s); }
    };
    higher2 { 
        strings = {"goodbye"};
        Integer f(String s) { print(s); return s.size; }
    };
    higher2 { 
        strings = {"goodbye"};
        @error void f(Integer n) { print(n.string); }
    };
    
    higher2 { 
        strings = {"goodbye"};
        function f(String s) { print(s); return s.size; }
    };
    higher2({"goodbye"}, (String s) print(s));
    higher2({"goodbye"}, function (String s) print(s));
    
    @error print(s);
    
    @type["Callable<Float,Float>"] curried(1);
    Float plus1(Float x) = curried(1);
    @type["Callable<Float,Float>"] value p1 = curried(1);
    Float three = plus1(2.0);
    Float four = curried(2)(2.0);
    @error curried(2)("foo");
    @error curried("foo")(2.0);
    @error curried(2)();
    @error curried(2)(2.0, "foo");
    //Float t1 = p1(2.0);
    
    function str(Float f) { return f.string; }
    function zero() { return 0.0; }
    @type["Entry<Float,String>"] generic(str,zero);
    @type["Entry<Float,String>"] generic(str,bottom);
    @type["Entry<Object,Object>"] generic((Object obj) obj, () "hello");
    @type["Entry<Object,String>"] generic((Object obj) obj.string, () "hello");
    @type["Entry<String,String>"] generic((String str) str, () "hello");
    
    function fx(String g()) = do<String>;
    @error function fy(String g()) = do;
    value fw = do<String>;
    @error value fz = do;
}

class Outer() {
    class Inner() {}
    Outer.Inner oi = Outer.Inner();
}

Comparison multiCompare()(Integer x, Integer y) {
    return x<=>y;
}
void testMultiCompare() {
    multiCompare()(1,1);
}

Callable<String> tester() {
    String f() { return "ok"; }
    return f;
}
void runTester() {
    print(tester()());
}

void moreTests() {
    void callFunction(String f(Integer i)) {
        print(f(0));
    }
    function f(Integer i) { 
        return (i+12).string;
    }
    callFunction(f);
    callFunction((Integer i) (i*3).string);
    callFunction {
        function f(Integer i) { 
            return (i**2).string;
        }
    };
}