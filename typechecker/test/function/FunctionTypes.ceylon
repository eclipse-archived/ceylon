String upper(String s) {
   return s.uppercased;
}

void print(String s) {}

class X(String s) {
    shared class Y() {}
    shared void hello() {}
}

void noop() {}

void higher(String[] strings, Callable<Void,String> f) {
    @error void fn(String str) = f;
    for (s in strings) {
        fn(s);
    }
}

void method() {
    Callable<String,String> upperRef = upper;
    Callable<Void,String> printRef = print;    
    Callable<X,String> xRef = X;
    X x = X("hello");
    Callable<X.Y> yRef = x.Y;
    Callable<Void> helloRef = x.hello;
    Callable<Void> noopRef = noop;
    higher({"hello", "world"}, print);
    higher({"hello", "world"}, upper);
    higher({"hello", "world"}, X);
    @error higher({"hello", "world"}, noop);
    
    function up(String s) = upper;
    void pr(String s) = print;
    void np() = noop;
    
    function good(String s) = X;
    X better(String s) = X;
    @error function bad() = X;
    @error function badder(Natural n) = X;
    @error String worse(String s) = X;
    @error String worst() = X;
    @error void broke() = noop();
}

class Outer() {
    class Inner() {}
    Outer.Inner oi = Outer.Inner();
}