class TypeArgInference() {
    
    class X<T>(T t, T s) {
        shared class Y<S>(S ss) {}
        shared R? opt<R>(R r) { return null; }
    }
    @type["TypeArgInference.X<String>"] X("hello", "world");
    @type["TypeArgInference.X<Float|Natural>"] X(1.0, 1000);
    @type["TypeArgInference.X<String>.Y<Float>"] X("hello", "world").Y(3.0);
    @type["Nothing|String"] X(1,2).opt("hello");
    
    U first<U>(U u, U v) { return u; }
    @type["String"] first("hello", "world");
    @type["Integer|TypeArgInference.X<String>"] first(+1,X("hello", "world"));
    
    @error X x = X(1,2);
    
    //@error String firstString(String x, String y) = first;
    //String secondString(String x, String y) = first<String>;
        
    class Const<T,S>(T t) given T satisfies Numeric<T> {}
    @type["TypeArgInference.Const<Natural,Bottom>"] Const(1);
    @error Const("hello");

}