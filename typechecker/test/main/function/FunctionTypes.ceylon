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

void higher1(String[] strings, Callable<Void,[String]> f) {
    void g(String str) => f;
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

String str(Float f) => f.string;

Float curried(Integer x)(Float y) => x+y;

[X,Y] generic<X,Y>(Y f(X x), X x()) 
        given X satisfies Object 
        given Y satisfies Object { 
    X xx = x();
    return xx->f(xx);
}

T do<T>(T f()) { return f(); }

void method() {
    Callable<String,[String]> upperRef = upper;
    Callable<Void,[String]> printRef = print;
    Callable<Void,[Bottom]> printRefContra = print;
    Callable<X,[String]> xRef = X;
    Callable<Void,Bottom> xRefContra = X;
    X x = X("hello");
    Callable<X.Y,[]> yRef = x.Y;
    Callable<Void,[]> helloRef = x.hello;
    Callable<Void,[]> noopRef = noop;
    
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
    
    @type:"String" function up(String s) => upper(s);
    void pr(String s) => print;
    void np() => noop;
    
    @type:"String" up("hello");
    @type:"Void" pr("hello");
    @type:"Void" np();
    
    @type:"X" function good(String s) => X(s);
    X better(String s) => X(s);
    @type:"X" @error function bad() => X();
    @type:"X" @error function badder(Integer n) => X(n);
    @error String worse(String s) => X;
    @error String worst() => X;
    void broke() => noop();
    @error Z moreBroke() => Z;
    @error do(Z);
    @type:"Void" function z() => Z;
    
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
    higher2({"goodbye"}, (String s) print(s));
    
    @error print(s);
    
    @type:"Callable<Float,Tuple<Float,Float,Empty>>" curried(1);
    Float plus1(Float x) => curried(1)(x);
    @type:"Callable<Float,Tuple<Float,Float,Empty>>" value p1 = curried(1);
    Float three = plus1(2.0);
    Float four = curried(2)(2.0);
    @error curried(2)("foo");
    @error curried("foo")(2.0);
    @error curried(2)();
    @error curried(2)(2.0, "foo");
    //Float t1 = p1(2.0);
    
    function str(Float f) => f.string;
    function zero() => 0.0;
    @type:"Entry<Float,String>" generic(str,zero);
    @type:"Entry<Float,String>" generic(str,bottom);
    @type:"Entry<Object,Object>" generic((Object obj) obj, () "hello");
    @type:"Entry<Object,String>" generic((Object obj) obj.string, () "hello");
    @type:"Entry<String,String>" generic((String str) str, () "hello");
    
    function fx(String g()) => do<String>;
    @error function fy(String g()) => do;
    value fw = do<String>;
    @error value fz = do;

    function sqrt(Float x) => x**0.5;
    value temp = sqrt;
    Float root(Float x) => temp(x);
    
    @error Callable<Void> reallyBroken(void foo()) {
        @error return foo;
    }
    
    Nothing foo(Integer... seq) => null;
    Nothing bar(Integer... ints) => foo(ints...);
    Nothing baz(Integer... seq); baz = foo;
    Nothing qux(Integer... ints) => baz(ints...);
    Nothing ok(Integer ints) => foo(ints);
    @error Nothing broke(Integer ints) => foo(ints...);
    Nothing notBroke(Integer ints); notBroke = foo;
    Nothing alsoBroke(Integer... ints); @error alsoBroke = ok;
    Nothing reallyBroke(Integer... ints); reallyBroke(@error Integer[] ints) => foo(ints...);
    Nothing badlyBroke(Integer... ints); badlyBroke(@error Integer[] ints) => ok(ints.first else 0);
    Nothing terrible(Integer... ints); @error terrible(Integer... ints) => foo;    
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

Callable<String,[]> tester() {
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
    function f(Integer i) => (i+12).string;
    callFunction(f);
    callFunction((Integer i) (i*3).string);
    callFunction {
        function f(Integer i) { 
            return (i**2).string;
        }
    };
}

Sequence<String()> singletonStringFunc = Singleton<String()>(()"hello");
Sequence<Boolean()(String)> singletonBooleanFunc = Singleton<Boolean()(String)>((String s)()s=="hello");

void sequencedParams() {
    value str = string;
    Void(Character...) str0 = str;
    Void(Character...) str0p = string;
    Void(Character) str1 = str;
    Void(Character, Character) str2 = str;
    str("hello".characters...);
    str();
    str(`X`);
    str(`h`, `e`, `l`, `l`, `o`);
    @error str(1);
    @error str("hello".characters);
    @error str(`X`...);
}

 class Outer1() {
   shared class Inner() { }
 }
 Outer1? o = null;
 Outer1.Inner? i1 = o?.Inner();
 Outer1.Inner? cons() => o?.Inner();
 
void foo(Integer... seq) {}
void bar(Integer... ints) => foo;

alias CSI => Callable<String,[Integer]>;
String callCSI(CSI csi) {
    return csi(1);
}
