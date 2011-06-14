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
    
    variable X var := X(); 
    
    X attx { return var; }
    assign attx { var := attx; }
    
    Y atty { @error return var; }
    assign atty { @error var := atty; }
    
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
    
    void hi(@error String greeting = 1, String name = "World", Natural times = 1) {}
    class Hi(@error String greeting = 23.0, String name = "World", @error Natural times = "X") {}
    
    X something = X();
    X? nothing {
        return null;
    }
    @error print(nothing.hello);
    Boolean b = 1<2;
    
    if (b) {}
    
    @error if (something) {}
        
    if (exists nothing) {
        print(nothing.hello);
    }
    
    if (exists {X()}[0]) {}
    
    //if (@error exists "Hello") {}
    //if (@error exists something) {}
    
    if (exists X x = nothing) {
        print(x.hello);
    }
    
    if (exists local xx = nothing) {
        print(xx.hello);
    }
    
    if (exists @error Y y = nothing) {
        @error print(y.hello);
        print(y.name);
    }
    
    if (exists @error X x = something) {
        print(x.hello);
    }
    
    if (exists @error local xx = something) {
        @error print(xx.hello);
    }
    
    @error if (exists something.hello) {}
    //@error if (exists something) {}
    
    if (is Y something) {
        print(something.name);
    }

    if (is Y y = something) {
        print(y.name);
    }
    
    if (is Y X()) {}
    
    X[]? seq = null;
    
    if (nonempty seq) {
        print(seq.size.string);
    }
    
    //Natural? nat = null;
    //@error if (nonempty nat) {} 
    
    for (X x in {X(), X()} ) {
        print(x.hello);
    }
    
    for (local x in {X(), X()} ) {
        print(x.hello);
    }
    
    for (@error Y y in {X(), X()} ) {
        print(y.name);
    }
    
    for (@error X x in 46 ) {}
    
    for (Integer i in {}) {
        print($i);
    }
    for (Integer i in {-1,+2}) {
        print($i);
    }
    if (nonempty {}) {}
    @error if (nonempty {-2,+0,+1}) {}
    Integer[] ints = {-2,+0,+1};
    if (nonempty ints) {
        Integer i = ints.first;
    }
    
    Integer[] noints = {};
    if (nonempty noints) {}
    
    //for (@error local x in 46 ) {}
    
    for (X x -> Y y in {X()->Y(), X()->Y()}) {
        print(x.hello + " " + y.name);
    }
    
    for (local x -> local y in {X()->Y(), X()->Y()}) {
        print(x.hello + " " + y.name);
    }
    
    for (@error X x -> X y in {X()->Y(), X()->Y()}) {
        print(x.hello);
    }
    
    for (@error Y x -> Y y in {X()->Y(), X()->Y()}) {
        print(y.name);
    }
    
    for (@error X x -> Y y in 12) {}
    
    for (Natural i->String s in entries<String>({"hello", "world", "!"})) {
        print($i + ": " + s);
    }
    
    for (Natural i->String s in entries<String>("hello", "world", "!")) {
        print($i + ": " + s);
    }
    
    for (Integer i in -10..+10) {
        print($i);
    }
    for (Natural n in 0..100k) {
        print($n);
    }
    for (@error Natural i in -10..+10) {
        print($i);
    }
    
    //for (@error local x -> local y in 12) {}
    
    void printStrings(String... strings) {}
    
    printStrings("Hello", "World");

    printStrings { "Hello", "World" };

    printStrings { strings={"Hello", "World"}; };


    @error printStrings(1, 2);

    printStrings { @error 1, 2 };

    printStrings { @error strings={1, 2}; };
    
    
    String joinStrings(Character char, String... strings) { throw; }
    
    joinStrings(`.`, "Hello", "World");

    joinStrings { char=` `; "Hello", "World" };

    joinStrings { char=` `; strings={"Hello", "World"}; };


    @error joinStrings(`.`, 1, 2);

    joinStrings { char=` `; @error 1, 2 };

    joinStrings { char=` `; @error strings={1, 2}; };
    
    void w<W>(W... ws) {}
    w<String>("foo");
    w<String>({"foo"});
    w<String>("foo", "bar");
    w<String>({"foo", "bar"});
    
}