class Assignability() {
    
    class X() {
        shared String hello = "Hello";
    }
    class Y() {
        shared String name = "Gavin";
    }
    
    void print(String s) {}
    
    void method(X arg1, Y arg2) {
        print(arg1.hello + arg2.name);
    }
    
    method(X(),Y());
    
    method { arg1=X(); arg2=Y(); };
    
    @error method(Y(), Y());
    @error method(X(), X());

    method { arg1=X(); @error arg2=X(); };
    method { @error arg1=Y(); arg2=Y(); };

    this.X();
    this.X{};
    
    @error X(Y());
    @error method(X());
    @error method(X(),Y(),this);
    
    X{ @error y=Y(); };
    @error method{ arg1=X(); };
    method{ arg1=X(); arg2=Y(); @error arg3=this; };
    
    X{ @error Y() };
    method{ arg1=X(); arg2=Y(); @error this };
    
    method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
    };
    
    method { 
        object arg1 extends X() {}
        object arg2 extends Y() {}
    };
    
    method { 
        X arg1 { return X(); }
        @error X arg2 { return X(); }
    };
    
    method { 
        object arg1 extends X() {}
        @error object arg2 extends X() {}
    };
    
    @error method { 
        X arg1 { return X(); }
    };
    
    method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
        @error X arg3 { return X(); }
    };
    
    X x1 = X();
    
    @error X x2 = Y();
    
    X x3;
    x3 = X();
    
    X x4;
    @error x4 = Y();
    
    variable X var = X(); 
    
    X attx { return var; }
    assign attx { var = attx; }
    
    Y atty { @error return var; }
    assign atty { @error var = atty; }
    
    X methx { return var; }
    Y methy { @error return var; }
    
    void methv() { @error return var; }
    
    void methv2() {
        if ("Hello"=="Goodbye") {
            @error return var;
        }
    }
    
    X methx2 {
        if ("Hello">"Goodbye") {
            return var;
        }
        else {
            return X();
        }
    }
    
    Y methy2 {
        if ("Hello"<"Goodbye") {
            @error return var;
        }
        return Y();
    }
    
    X outerMethod() {
        Y innerMethod() {
            return Y();
        }
        return X();
    }
    
    X outerAtt {
        Y innerAtt {
            return Y();
        }
        return X();
    }
        
    void hello(String greeting = "Hello", String name = "World") {}
    
    hello("Hi", "Gavin");
    hello("Hi");
    hello();
    @error hello("Hi", "Gavin", "Foo");
    
    hello{ name = "Gavin"; };
    hello{ greeting = "Hi"; };
    hello{ greeting = "Hi"; name = "Gavin"; };
    hello{};
    
    hello { name="Gavin"; @error name="Tom"; };
    
    void hi(@error String greeting = 1, String name = "World", Integer times = 1) {}
    class Hi(@error String greeting = 23.0, String name = "World", @error Integer times = "X") {}
    
    X something = X();
    X? nothing {
        return null;
    }
    @error print(nothing.hello);
    Boolean b = 1<2;
    
    if (b) {}
    
    @error if (something) {}
        
    @error if (exists nothing) {
        print(nothing.hello);
    }
    
    X[] sequenceofx = [X()];
    if (sequenceofx[0] exists) {}
    
    //@error if (exists "Hello") {}
    @error if (exists something) {}
    if (exists @error s = something) {}
    
    if (exists X x = nothing) {
        print(x.hello);
    }
    
    if (exists xx = nothing) {
        print(xx.hello);
    }
    
    if (exists @error Y y = nothing) {
        @error print(y.hello);
        print(y.name);
    }
    
    if (exists @error X x = something) {
        print(x.hello);
    }
    
    if (exists @error xx = something) {
        print(xx.hello);
    }
    
    @error if (something.hello exists) {}
    if (exists @error h = something.hello) {}
    
    @error if (is Y something) {
        @error print(something.name);
    }

    @error if (is Y y = something) {
        @error print(y.name);
    }
    
    @error if (X() is Y) {}
    @error if (X() is Object ) {}
    @error if (is Y x = X()) {}
    @error if (is Object x = X()) {}
    
    X[]? seq = null;
    
    if (nonempty seq) {
        print(seq.size.string);
    }
    
    Integer? nat = null;
    @error if (nonempty nat) {}
    
    void m<T>() {
        T[] ts = {};
        if (nonempty ts) {
            T t=ts.first;
        }
    }
    
    String[] strngs = {};
    if (nonempty strngs) {
        String s=strngs.first;
    }
    
    for (X x in {X(), X()}) {
        print(x.hello);
    }
    
    for (x in {X(), X()} ) {
        print(x.hello);
    }
    
    for (@error Y y in {X(), X()} ) {
        print(y.name);
    }
    
    for (@error X x in 46 ) {}
    
    for (Integer i in {}) {
        print(i.string);
    }
    for (Integer i in {-1,+2}) {
        print(i.string);
    }
    @error if ({} nonempty) {}
    @error if ([-2,+0,+1] nonempty) {}
    if (nonempty @error e = {}) {}
    if (nonempty @error s = [-2,+0,+1]) {}
    Integer[] ints = [-2,+0,+1];
    if (nonempty ints) {
        Integer i = ints.first;
    }
    
    Integer[] noints = {};
    if (nonempty noints) {}
    
    //for (@error x in 46) {}
    
    for (X x -> Y y in {X()->Y(), X()->Y()}) {
        print(x.hello + " " + y.name);
    }
    
    for (x->y in {X()->Y(), X()->Y()}) {
        print(x.hello + " " + y.name);
    }
    
    for (@error X x -> X y in {X()->Y(), X()->Y()}) {
        print(x.hello);
    }
    
    for (@error Y x -> Y y in {X()->Y(), X()->Y()}) {
        print(y.name);
    }
    
    for (@error X x -> Y y in 12) {}
    
    for (Integer i->String s in entries<String>(["hello", "world", "!"]*)) {
        print(i.string + ": " + s);
    }
    
    for (Integer i->String s in entries<String>("hello", "world", "!")) {
        print(i.string + ": " + s);
    }
    
    for (i->s in entries(["hello", "world", "!"]*)) {
        print(i.string + ": " + s);
    }
    
    for (i->s in entries("hello", "world", "!")) {
        print(i.string + ": " + s);
    }
    
    for (Integer i in -10..+10) {
        print(i.string);
    }
    for (Integer n in 0..100k) {
        print(n.string);
    }
    for (@error Float i in -10..+10) {
        print(i.string);
    }
    
    //for (@error x -> y in 12) {}
    
    void printStrings(String* strings) {}
    void printStrings0({String*} strings) {}
    
    printStrings();
    @error printStrings0();
    printStrings {};
    @error printStrings0 {};
    
    printStrings("Hello", "World");
    @error printStrings0("Hello", "World");

    printStrings { @error "Hello", "World" };
    printStrings0 { "Hello", "World" };

    printStrings0 {  strings={"Hello", "World"}; };
    printStrings0 { @error strings="Hello"; @error strings="World"; };
    printStrings {  @error strings={"Hello", "World"}; };
    printStrings { @error strings="Hello"; @error strings="World"; };
    printStrings { strings=["Hello", "World"]; };

    printStrings { @error strings={"Hello", "World"}; @error "Hello", "World" };
    printStrings { @error strings="Hello"; @error strings="World"; @error "Hello", "World" };
    printStrings { strings=["Hello", "World"]; @error "Hello", "World" };
    printStrings0 { @error strings="Hello"; @error strings="World"; @error "Hello", "World" };
    printStrings0 {  strings={"Hello", "World"}; @error "Hello", "World" };

    @error printStrings(1, 2);

    printStrings { @error 1, 2 };

    printStrings { @error strings={1, 2}; };
    
    @error printStrings("hello", 1);
    
    printStrings { @error "hello", 1 };
    
    printStrings { @error strings={"hello", 1}; };
    
    {String*} strings1 = { "hello" };
    [String*] strings2 = [ "hello" ];
    
    @error printStrings(strings1*);
    printStrings0 { strings1* };
    printStrings(strings2*);
    printStrings0 {strings2*};
    
    String joinStrings(Character char, String* strings) { throw; }
    String joinStrings0(Character char, {String*} strings) { throw; }
    
    joinStrings(`.`, "Hello", "World");

    joinStrings { char=` `; @error "Hello", "World" };

    joinStrings0 { char=` `; "Hello", "World" };

    joinStrings { char=` `; @error strings={"Hello", "World"}; };

    joinStrings0 { char=` `; strings={"Hello", "World"}; };

    joinStrings { char=` `; @error strings="Hello"; @error strings="World"; };

    joinStrings { char=` `; strings=["Hello", "World"]; };

    joinStrings0 { char=` `; @error strings="Hello"; @error strings="World"; };

    @error joinStrings(`.`, 1, 2);

    joinStrings { char=` `; @error 1, 2 };

    joinStrings { char=` `; @error strings={1, 2}; };
    
    void w<W>(W* ws) {}
    w<String>("foo");
    w<String>(["foo"]*);
    @error w<String>({"foo"}*);
    w<String>("foo", "bar");
    w<String>(["foo", "bar"]*);
    w("foo");
    w(["foo"]*);
    @error w({"foo"}*);
    w("foo", "bar");
    w(["foo", "bar"]*);
    @error w({"foo", "bar"}*);
    {String*} iterable = {"foo", "bar"};
    [String*] sequential = ["foo", "bar"];
    @error w(iterable*);
    w(sequential*);
    
    object o { shared String hello = "hello"; }
    @type:"Basic" value oo = o;
    Object ooo = o;
    @type:"String" value so = o.hello;
    @error value soo = oo.hello;
    
    object x extends X() {}
    X xx = x;
    @error X xxx = o;
    
    Iterable<Entry<Integer,Integer>>? map = null;
    if (exists map) {
        for (Integer i -> Integer j in map) {}
    }
    
    function foo() { throw; }
    @type:"Nothing" foo();
    
    function f<T>(T a, T b) {
        if (true) {
            return a;
        }
        else {
            return b;
        }
    }
    @type:"Tuple<String,String,Empty>|Tuple<Integer,Integer,Empty>" value ut = f(["aaa"],[1]);
    Sequence<Object> st1 = ut;
    Sequence<String|Integer> st2 = ut;
    @type:"Null|String|Integer" value item = ut[0];
    @type:"Sequential<String>|Sequential<Integer>" value items = ut[1..2];
    
    class Invariant<T>(T t) {}
    Ordinal<Integer> ii1 = +1;
    Integer ii2 = ii1 of Integer;
    Ordinal<Integer> ii3 = ii2;
    Invariant<Ordinal<Integer>> iii1 = Invariant(ii1);
//    Invariant<Integer> iii2 = iii1;
//    Invariant<Ordinal<Integer>> iii3 = iii2;
    
    Anything v = null;
    if (exists v) {
        @type:"Object" value val = v;
        print(v.string);
    }
    if (is String v) {
        @type:"String" value val = v;
        print(v.size.string);
    }
    
    Object? mo = null;
    if (exists mo) {
        @type:"Object" value val = mo;
    }
    if (is String mo) {
        @type:"String" value val = mo;
    }
    
    Set<String>? stringSet = null;
    @error if (nonempty stringSet) {
        
    }
    //if (is FixedSized<String> stringSet) {
    //    if (nonempty stringSet) {
    //        @type:"Set<String>&Some<String>" value ness = stringSet;
    //    }
    //}
    
}