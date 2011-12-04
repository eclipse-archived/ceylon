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
    void fn(String str) = f;
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
}

class Outer() {
    class Inner() {}
    Outer.Inner oi = Outer.Inner();
}